/*     */ package org.apache.hc.core5.pool;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ import org.apache.hc.core5.util.Deadline;
/*     */ import org.apache.hc.core5.util.DeadlineTimeoutException;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public class StrictConnPool<T, C extends ModalCloseable>
/*     */   implements ManagedConnPool<T, C>
/*     */ {
/*     */   private final TimeValue timeToLive;
/*     */   private final PoolReusePolicy policy;
/*     */   private final DisposalCallback<C> disposalCallback;
/*     */   private final ConnPoolListener<T> connPoolListener;
/*     */   private final Map<T, PerRoutePool<T, C>> routeToPool;
/*     */   private final LinkedList<LeaseRequest<T, C>> pendingRequests;
/*     */   private final Set<PoolEntry<T, C>> leased;
/*     */   private final LinkedList<PoolEntry<T, C>> available;
/*     */   private final ConcurrentLinkedQueue<LeaseRequest<T, C>> completedRequests;
/*     */   private final Map<T, Integer> maxPerRoute;
/*     */   private final Lock lock;
/*     */   private final AtomicBoolean isShutDown;
/*     */   private volatile int defaultMaxPerRoute;
/*     */   private volatile int maxTotal;
/*     */   
/*     */   public StrictConnPool(int defaultMaxPerRoute, int maxTotal, TimeValue timeToLive, PoolReusePolicy policy, DisposalCallback<C> disposalCallback, ConnPoolListener<T> connPoolListener) {
/*  98 */     Args.positive(defaultMaxPerRoute, "Max per route value");
/*  99 */     Args.positive(maxTotal, "Max total value");
/* 100 */     this.timeToLive = TimeValue.defaultsToNegativeOneMillisecond(timeToLive);
/* 101 */     this.policy = (policy != null) ? policy : PoolReusePolicy.LIFO;
/* 102 */     this.disposalCallback = disposalCallback;
/* 103 */     this.connPoolListener = connPoolListener;
/* 104 */     this.routeToPool = new HashMap<>();
/* 105 */     this.pendingRequests = new LinkedList<>();
/* 106 */     this.leased = new HashSet<>();
/* 107 */     this.available = new LinkedList<>();
/* 108 */     this.completedRequests = new ConcurrentLinkedQueue<>();
/* 109 */     this.maxPerRoute = new HashMap<>();
/* 110 */     this.lock = new ReentrantLock();
/* 111 */     this.isShutDown = new AtomicBoolean(false);
/* 112 */     this.defaultMaxPerRoute = defaultMaxPerRoute;
/* 113 */     this.maxTotal = maxTotal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StrictConnPool(int defaultMaxPerRoute, int maxTotal, TimeValue timeToLive, PoolReusePolicy policy, ConnPoolListener<T> connPoolListener) {
/* 125 */     this(defaultMaxPerRoute, maxTotal, timeToLive, policy, null, connPoolListener);
/*     */   }
/*     */   
/*     */   public StrictConnPool(int defaultMaxPerRoute, int maxTotal) {
/* 129 */     this(defaultMaxPerRoute, maxTotal, TimeValue.NEG_ONE_MILLISECOND, PoolReusePolicy.LIFO, null);
/*     */   }
/*     */   
/*     */   public boolean isShutdown() {
/* 133 */     return this.isShutDown.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 138 */     if (this.isShutDown.compareAndSet(false, true)) {
/* 139 */       fireCallbacks();
/* 140 */       this.lock.lock();
/*     */       try {
/* 142 */         for (PerRoutePool<T, C> pool : this.routeToPool.values()) {
/* 143 */           pool.shutdown(closeMode);
/*     */         }
/* 145 */         this.routeToPool.clear();
/* 146 */         this.leased.clear();
/* 147 */         this.available.clear();
/* 148 */         this.pendingRequests.clear();
/*     */       } finally {
/* 150 */         this.lock.unlock();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 157 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */   
/*     */   private PerRoutePool<T, C> getPool(T route) {
/* 161 */     PerRoutePool<T, C> pool = this.routeToPool.get(route);
/* 162 */     if (pool == null) {
/* 163 */       pool = new PerRoutePool<>(route, this.disposalCallback);
/* 164 */       this.routeToPool.put(route, pool);
/*     */     } 
/* 166 */     return pool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<PoolEntry<T, C>> lease(T route, Object state, Timeout requestTimeout, FutureCallback<PoolEntry<T, C>> callback) {
/*     */     boolean acquiredLock;
/* 174 */     Args.notNull(route, "Route");
/* 175 */     Args.notNull(requestTimeout, "Request timeout");
/* 176 */     Asserts.check(!this.isShutDown.get(), "Connection pool shut down");
/* 177 */     Deadline deadline = Deadline.calculate((TimeValue)requestTimeout);
/* 178 */     BasicFuture<PoolEntry<T, C>> future = new BasicFuture<PoolEntry<T, C>>(callback)
/*     */       {
/*     */         
/*     */         public synchronized PoolEntry<T, C> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
/*     */         {
/*     */           try {
/* 184 */             return (PoolEntry<T, C>)super.get(timeout, unit);
/* 185 */           } catch (TimeoutException ex) {
/* 186 */             cancel();
/* 187 */             throw ex;
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 195 */       acquiredLock = this.lock.tryLock(requestTimeout.getDuration(), requestTimeout.getTimeUnit());
/* 196 */     } catch (InterruptedException interruptedException) {
/* 197 */       Thread.currentThread().interrupt();
/* 198 */       future.cancel();
/* 199 */       return (Future<PoolEntry<T, C>>)future;
/*     */     } 
/*     */     
/* 202 */     if (acquiredLock) {
/*     */       try {
/* 204 */         LeaseRequest<T, C> request = new LeaseRequest<>(route, state, requestTimeout, future);
/* 205 */         boolean completed = processPendingRequest(request);
/* 206 */         if (!request.isDone() && !completed) {
/* 207 */           this.pendingRequests.add(request);
/*     */         }
/* 209 */         if (request.isDone()) {
/* 210 */           this.completedRequests.add(request);
/*     */         }
/*     */       } finally {
/* 213 */         this.lock.unlock();
/*     */       } 
/* 215 */       fireCallbacks();
/*     */     } else {
/* 217 */       future.failed((Exception)DeadlineTimeoutException.from(deadline));
/*     */     } 
/*     */     
/* 220 */     return (Future<PoolEntry<T, C>>)future;
/*     */   }
/*     */   
/*     */   public Future<PoolEntry<T, C>> lease(T route, Object state) {
/* 224 */     return lease(route, state, Timeout.DISABLED, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void release(PoolEntry<T, C> entry, boolean reusable) {
/* 229 */     if (entry == null) {
/*     */       return;
/*     */     }
/* 232 */     if (this.isShutDown.get()) {
/*     */       return;
/*     */     }
/* 235 */     if (!reusable) {
/* 236 */       entry.discardConnection(CloseMode.GRACEFUL);
/*     */     }
/* 238 */     this.lock.lock();
/*     */     try {
/* 240 */       if (this.leased.remove(entry)) {
/* 241 */         if (this.connPoolListener != null) {
/* 242 */           this.connPoolListener.onRelease(entry.getRoute(), this);
/*     */         }
/* 244 */         PerRoutePool<T, C> pool = getPool(entry.getRoute());
/* 245 */         boolean keepAlive = (entry.hasConnection() && reusable);
/* 246 */         pool.free(entry, keepAlive);
/* 247 */         if (keepAlive) {
/* 248 */           switch (this.policy) {
/*     */             case LIFO:
/* 250 */               this.available.addFirst(entry);
/*     */               break;
/*     */             case FIFO:
/* 253 */               this.available.addLast(entry);
/*     */               break;
/*     */             default:
/* 256 */               throw new IllegalStateException("Unexpected ConnPoolPolicy value: " + this.policy);
/*     */           } 
/*     */         } else {
/* 259 */           entry.discardConnection(CloseMode.GRACEFUL);
/*     */         } 
/* 261 */         processNextPendingRequest();
/*     */       } else {
/* 263 */         throw new IllegalStateException("Pool entry is not present in the set of leased entries");
/*     */       } 
/*     */     } finally {
/* 266 */       this.lock.unlock();
/*     */     } 
/* 268 */     fireCallbacks();
/*     */   }
/*     */   
/*     */   private void processPendingRequests() {
/* 272 */     ListIterator<LeaseRequest<T, C>> it = this.pendingRequests.listIterator();
/* 273 */     while (it.hasNext()) {
/* 274 */       LeaseRequest<T, C> request = it.next();
/* 275 */       BasicFuture<PoolEntry<T, C>> future = request.getFuture();
/* 276 */       if (future.isCancelled()) {
/* 277 */         it.remove();
/*     */         continue;
/*     */       } 
/* 280 */       boolean completed = processPendingRequest(request);
/* 281 */       if (request.isDone() || completed) {
/* 282 */         it.remove();
/*     */       }
/* 284 */       if (request.isDone()) {
/* 285 */         this.completedRequests.add(request);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processNextPendingRequest() {
/* 291 */     ListIterator<LeaseRequest<T, C>> it = this.pendingRequests.listIterator();
/* 292 */     while (it.hasNext()) {
/* 293 */       LeaseRequest<T, C> request = it.next();
/* 294 */       BasicFuture<PoolEntry<T, C>> future = request.getFuture();
/* 295 */       if (future.isCancelled()) {
/* 296 */         it.remove();
/*     */         continue;
/*     */       } 
/* 299 */       boolean completed = processPendingRequest(request);
/* 300 */       if (request.isDone() || completed) {
/* 301 */         it.remove();
/*     */       }
/* 303 */       if (request.isDone()) {
/* 304 */         this.completedRequests.add(request);
/*     */       }
/* 306 */       if (completed)
/*     */         return; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean processPendingRequest(LeaseRequest<T, C> request) {
/*     */     PoolEntry<T, C> entry;
/* 313 */     T route = request.getRoute();
/* 314 */     Object state = request.getState();
/* 315 */     Deadline deadline = request.getDeadline();
/*     */     
/* 317 */     if (deadline.isExpired()) {
/* 318 */       request.failed((Exception)DeadlineTimeoutException.from(deadline));
/* 319 */       return false;
/*     */     } 
/*     */     
/* 322 */     PerRoutePool<T, C> pool = getPool(route);
/*     */     
/*     */     while (true) {
/* 325 */       entry = pool.getFree(state);
/* 326 */       if (entry == null) {
/*     */         break;
/*     */       }
/* 329 */       if (entry.getExpiryDeadline().isExpired()) {
/* 330 */         entry.discardConnection(CloseMode.GRACEFUL);
/* 331 */         this.available.remove(entry);
/* 332 */         pool.free(entry, false);
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 337 */     if (entry != null) {
/* 338 */       this.available.remove(entry);
/* 339 */       this.leased.add(entry);
/* 340 */       request.completed(entry);
/* 341 */       if (this.connPoolListener != null) {
/* 342 */         this.connPoolListener.onLease(entry.getRoute(), this);
/*     */       }
/* 344 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 348 */     int maxPerRoute = getMax(route);
/*     */     
/* 350 */     int excess = Math.max(0, pool.getAllocatedCount() + 1 - maxPerRoute);
/* 351 */     if (excess > 0) {
/* 352 */       for (int i = 0; i < excess; i++) {
/* 353 */         PoolEntry<T, C> lastUsed = pool.getLastUsed();
/* 354 */         if (lastUsed == null) {
/*     */           break;
/*     */         }
/* 357 */         lastUsed.discardConnection(CloseMode.GRACEFUL);
/* 358 */         this.available.remove(lastUsed);
/* 359 */         pool.remove(lastUsed);
/*     */       } 
/*     */     }
/*     */     
/* 363 */     if (pool.getAllocatedCount() < maxPerRoute) {
/* 364 */       int freeCapacity = Math.max(this.maxTotal - this.leased.size(), 0);
/* 365 */       if (freeCapacity == 0) {
/* 366 */         return false;
/*     */       }
/* 368 */       int totalAvailable = this.available.size();
/* 369 */       if (totalAvailable > freeCapacity - 1) {
/* 370 */         PoolEntry<T, C> lastUsed = this.available.removeLast();
/* 371 */         lastUsed.discardConnection(CloseMode.GRACEFUL);
/* 372 */         PerRoutePool<T, C> otherpool = getPool(lastUsed.getRoute());
/* 373 */         otherpool.remove(lastUsed);
/*     */       } 
/*     */       
/* 376 */       entry = pool.createEntry(this.timeToLive);
/* 377 */       this.leased.add(entry);
/* 378 */       request.completed(entry);
/* 379 */       if (this.connPoolListener != null) {
/* 380 */         this.connPoolListener.onLease(entry.getRoute(), this);
/*     */       }
/* 382 */       return true;
/*     */     } 
/* 384 */     return false;
/*     */   }
/*     */   
/*     */   private void fireCallbacks() {
/*     */     LeaseRequest<T, C> request;
/* 389 */     while ((request = this.completedRequests.poll()) != null) {
/* 390 */       BasicFuture<PoolEntry<T, C>> future = request.getFuture();
/* 391 */       Exception ex = request.getException();
/* 392 */       PoolEntry<T, C> result = request.getResult();
/* 393 */       boolean successfullyCompleted = false;
/* 394 */       if (ex != null) {
/* 395 */         future.failed(ex);
/* 396 */       } else if (result != null) {
/* 397 */         if (future.completed(result)) {
/* 398 */           successfullyCompleted = true;
/*     */         }
/*     */       } else {
/* 401 */         future.cancel();
/*     */       } 
/* 403 */       if (!successfullyCompleted) {
/* 404 */         release(result, true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void validatePendingRequests() {
/* 410 */     this.lock.lock();
/*     */     try {
/* 412 */       long now = System.currentTimeMillis();
/* 413 */       ListIterator<LeaseRequest<T, C>> it = this.pendingRequests.listIterator();
/* 414 */       while (it.hasNext()) {
/* 415 */         LeaseRequest<T, C> request = it.next();
/* 416 */         BasicFuture<PoolEntry<T, C>> future = request.getFuture();
/* 417 */         if (future.isCancelled() && !request.isDone()) {
/* 418 */           it.remove(); continue;
/*     */         } 
/* 420 */         Deadline deadline = request.getDeadline();
/* 421 */         if (deadline.isBefore(now)) {
/* 422 */           request.failed((Exception)DeadlineTimeoutException.from(deadline));
/*     */         }
/* 424 */         if (request.isDone()) {
/* 425 */           it.remove();
/* 426 */           this.completedRequests.add(request);
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 431 */       this.lock.unlock();
/*     */     } 
/* 433 */     fireCallbacks();
/*     */   }
/*     */   
/*     */   private int getMax(T route) {
/* 437 */     Integer v = this.maxPerRoute.get(route);
/* 438 */     if (v != null) {
/* 439 */       return v.intValue();
/*     */     }
/* 441 */     return this.defaultMaxPerRoute;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 446 */     Args.positive(max, "Max value");
/* 447 */     this.lock.lock();
/*     */     try {
/* 449 */       this.maxTotal = max;
/*     */     } finally {
/* 451 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 457 */     this.lock.lock();
/*     */     try {
/* 459 */       return this.maxTotal;
/*     */     } finally {
/* 461 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 467 */     Args.positive(max, "Max value");
/* 468 */     this.lock.lock();
/*     */     try {
/* 470 */       this.defaultMaxPerRoute = max;
/*     */     } finally {
/* 472 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 478 */     this.lock.lock();
/*     */     try {
/* 480 */       return this.defaultMaxPerRoute;
/*     */     } finally {
/* 482 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(T route, int max) {
/* 488 */     Args.notNull(route, "Route");
/* 489 */     this.lock.lock();
/*     */     try {
/* 491 */       if (max > -1) {
/* 492 */         this.maxPerRoute.put(route, Integer.valueOf(max));
/*     */       } else {
/* 494 */         this.maxPerRoute.remove(route);
/*     */       } 
/*     */     } finally {
/* 497 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(T route) {
/* 503 */     Args.notNull(route, "Route");
/* 504 */     this.lock.lock();
/*     */     try {
/* 506 */       return getMax(route);
/*     */     } finally {
/* 508 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 514 */     this.lock.lock();
/*     */     try {
/* 516 */       return new PoolStats(this.leased
/* 517 */           .size(), this.pendingRequests
/* 518 */           .size(), this.available
/* 519 */           .size(), this.maxTotal);
/*     */     } finally {
/*     */       
/* 522 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(T route) {
/* 528 */     Args.notNull(route, "Route");
/* 529 */     this.lock.lock();
/*     */     try {
/* 531 */       PerRoutePool<T, C> pool = getPool(route);
/* 532 */       int pendingCount = 0;
/* 533 */       for (LeaseRequest<T, C> request : this.pendingRequests) {
/* 534 */         if (Objects.equals(route, request.getRoute())) {
/* 535 */           pendingCount++;
/*     */         }
/*     */       } 
/* 538 */       return new PoolStats(pool
/* 539 */           .getLeasedCount(), pendingCount, pool
/*     */           
/* 541 */           .getAvailableCount(), 
/* 542 */           getMax(route));
/*     */     } finally {
/* 544 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<T> getRoutes() {
/* 555 */     this.lock.lock();
/*     */     try {
/* 557 */       return new HashSet(this.routeToPool.keySet());
/*     */     } finally {
/* 559 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enumAvailable(Callback<PoolEntry<T, C>> callback) {
/* 569 */     this.lock.lock();
/*     */     try {
/* 571 */       Iterator<PoolEntry<T, C>> it = this.available.iterator();
/* 572 */       while (it.hasNext()) {
/* 573 */         PoolEntry<T, C> entry = it.next();
/* 574 */         callback.execute(entry);
/* 575 */         if (!entry.hasConnection()) {
/* 576 */           PerRoutePool<T, C> pool = getPool(entry.getRoute());
/* 577 */           pool.remove(entry);
/* 578 */           it.remove();
/*     */         } 
/*     */       } 
/* 581 */       processPendingRequests();
/* 582 */       purgePoolMap();
/*     */     } finally {
/* 584 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void enumLeased(Callback<PoolEntry<T, C>> callback) {
/* 594 */     this.lock.lock();
/*     */     try {
/* 596 */       Iterator<PoolEntry<T, C>> it = this.leased.iterator();
/* 597 */       while (it.hasNext()) {
/* 598 */         PoolEntry<T, C> entry = it.next();
/* 599 */         callback.execute(entry);
/*     */       } 
/* 601 */       processPendingRequests();
/*     */     } finally {
/* 603 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void purgePoolMap() {
/* 608 */     Iterator<Map.Entry<T, PerRoutePool<T, C>>> it = this.routeToPool.entrySet().iterator();
/* 609 */     while (it.hasNext()) {
/* 610 */       Map.Entry<T, PerRoutePool<T, C>> entry = it.next();
/* 611 */       PerRoutePool<T, C> pool = entry.getValue();
/* 612 */       if (pool.getAllocatedCount() == 0) {
/* 613 */         it.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdle(TimeValue idleTime) {
/* 620 */     long deadline = System.currentTimeMillis() - (TimeValue.isPositive(idleTime) ? idleTime.toMilliseconds() : 0L);
/* 621 */     enumAvailable(entry -> {
/*     */           if (entry.getUpdated() <= deadline) {
/*     */             entry.discardConnection(CloseMode.GRACEFUL);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 630 */     long now = System.currentTimeMillis();
/* 631 */     enumAvailable(entry -> {
/*     */           if (entry.getExpiryDeadline().isBefore(now)) {
/*     */             entry.discardConnection(CloseMode.GRACEFUL);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 640 */     StringBuilder buffer = new StringBuilder();
/* 641 */     buffer.append("[leased: ");
/* 642 */     buffer.append(this.leased.size());
/* 643 */     buffer.append("][available: ");
/* 644 */     buffer.append(this.available.size());
/* 645 */     buffer.append("][pending: ");
/* 646 */     buffer.append(this.pendingRequests.size());
/* 647 */     buffer.append("]");
/* 648 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class LeaseRequest<T, C extends ModalCloseable>
/*     */   {
/*     */     private final T route;
/*     */ 
/*     */     
/*     */     private final Object state;
/*     */ 
/*     */     
/*     */     private final Deadline deadline;
/*     */ 
/*     */     
/*     */     private final BasicFuture<PoolEntry<T, C>> future;
/*     */ 
/*     */     
/*     */     private final AtomicBoolean completed;
/*     */ 
/*     */     
/*     */     private volatile PoolEntry<T, C> result;
/*     */ 
/*     */     
/*     */     private volatile Exception ex;
/*     */ 
/*     */ 
/*     */     
/*     */     public LeaseRequest(T route, Object state, Timeout requestTimeout, BasicFuture<PoolEntry<T, C>> future) {
/* 678 */       this.route = route;
/* 679 */       this.state = state;
/* 680 */       this.deadline = Deadline.calculate((TimeValue)requestTimeout);
/* 681 */       this.future = future;
/* 682 */       this.completed = new AtomicBoolean(false);
/*     */     }
/*     */     
/*     */     public T getRoute() {
/* 686 */       return this.route;
/*     */     }
/*     */     
/*     */     public Object getState() {
/* 690 */       return this.state;
/*     */     }
/*     */     
/*     */     public Deadline getDeadline() {
/* 694 */       return this.deadline;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDone() {
/* 701 */       return (this.ex != null || this.result != null);
/*     */     }
/*     */     
/*     */     public void failed(Exception ex) {
/* 705 */       if (this.completed.compareAndSet(false, true)) {
/* 706 */         this.ex = ex;
/*     */       }
/*     */     }
/*     */     
/*     */     public void completed(PoolEntry<T, C> result) {
/* 711 */       if (this.completed.compareAndSet(false, true)) {
/* 712 */         this.result = result;
/*     */       }
/*     */     }
/*     */     
/*     */     public BasicFuture<PoolEntry<T, C>> getFuture() {
/* 717 */       return this.future;
/*     */     }
/*     */     
/*     */     public PoolEntry<T, C> getResult() {
/* 721 */       return this.result;
/*     */     }
/*     */     
/*     */     public Exception getException() {
/* 725 */       return this.ex;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 730 */       StringBuilder buffer = new StringBuilder();
/* 731 */       buffer.append("[");
/* 732 */       buffer.append(this.route);
/* 733 */       buffer.append("][");
/* 734 */       buffer.append(this.state);
/* 735 */       buffer.append("]");
/* 736 */       return buffer.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class PerRoutePool<T, C extends ModalCloseable>
/*     */   {
/*     */     private final T route;
/*     */     
/*     */     private final Set<PoolEntry<T, C>> leased;
/*     */     private final LinkedList<PoolEntry<T, C>> available;
/*     */     private final DisposalCallback<C> disposalCallback;
/*     */     
/*     */     PerRoutePool(T route, DisposalCallback<C> disposalCallback) {
/* 750 */       this.route = route;
/* 751 */       this.disposalCallback = disposalCallback;
/* 752 */       this.leased = new HashSet<>();
/* 753 */       this.available = new LinkedList<>();
/*     */     }
/*     */     
/*     */     public final T getRoute() {
/* 757 */       return this.route;
/*     */     }
/*     */     
/*     */     public int getLeasedCount() {
/* 761 */       return this.leased.size();
/*     */     }
/*     */     
/*     */     public int getAvailableCount() {
/* 765 */       return this.available.size();
/*     */     }
/*     */     
/*     */     public int getAllocatedCount() {
/* 769 */       return this.available.size() + this.leased.size();
/*     */     }
/*     */     
/*     */     public PoolEntry<T, C> getFree(Object state) {
/* 773 */       if (!this.available.isEmpty()) {
/* 774 */         if (state != null) {
/* 775 */           Iterator<PoolEntry<T, C>> iterator = this.available.iterator();
/* 776 */           while (iterator.hasNext()) {
/* 777 */             PoolEntry<T, C> entry = iterator.next();
/* 778 */             if (state.equals(entry.getState())) {
/* 779 */               iterator.remove();
/* 780 */               this.leased.add(entry);
/* 781 */               return entry;
/*     */             } 
/*     */           } 
/*     */         } 
/* 785 */         Iterator<PoolEntry<T, C>> it = this.available.iterator();
/* 786 */         while (it.hasNext()) {
/* 787 */           PoolEntry<T, C> entry = it.next();
/* 788 */           if (entry.getState() == null) {
/* 789 */             it.remove();
/* 790 */             this.leased.add(entry);
/* 791 */             return entry;
/*     */           } 
/*     */         } 
/*     */       } 
/* 795 */       return null;
/*     */     }
/*     */     
/*     */     public PoolEntry<T, C> getLastUsed() {
/* 799 */       return this.available.peekLast();
/*     */     }
/*     */     
/*     */     public boolean remove(PoolEntry<T, C> entry) {
/* 803 */       return (this.available.remove(entry) || this.leased.remove(entry));
/*     */     }
/*     */     
/*     */     public void free(PoolEntry<T, C> entry, boolean reusable) {
/* 807 */       boolean found = this.leased.remove(entry);
/* 808 */       Asserts.check(found, "Entry %s has not been leased from this pool", entry);
/* 809 */       if (reusable) {
/* 810 */         this.available.addFirst(entry);
/*     */       }
/*     */     }
/*     */     
/*     */     public PoolEntry<T, C> createEntry(TimeValue timeToLive) {
/* 815 */       PoolEntry<T, C> entry = new PoolEntry<>(this.route, timeToLive, this.disposalCallback);
/* 816 */       this.leased.add(entry);
/* 817 */       return entry;
/*     */     }
/*     */     
/*     */     public void shutdown(CloseMode closeMode) {
/*     */       PoolEntry<T, C> availableEntry;
/* 822 */       while ((availableEntry = this.available.poll()) != null) {
/* 823 */         availableEntry.discardConnection(closeMode);
/*     */       }
/* 825 */       for (PoolEntry<T, C> entry : this.leased) {
/* 826 */         entry.discardConnection(closeMode);
/*     */       }
/* 828 */       this.leased.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 833 */       StringBuilder buffer = new StringBuilder();
/* 834 */       buffer.append("[route: ");
/* 835 */       buffer.append(this.route);
/* 836 */       buffer.append("][leased: ");
/* 837 */       buffer.append(this.leased.size());
/* 838 */       buffer.append("][available: ");
/* 839 */       buffer.append(this.available.size());
/* 840 */       buffer.append("]");
/* 841 */       return buffer.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/StrictConnPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */