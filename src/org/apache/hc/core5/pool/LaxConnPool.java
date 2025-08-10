/*     */ package org.apache.hc.core5.pool;
/*     */ 
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicMarkableReference;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Experimental;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ @Experimental
/*     */ public class LaxConnPool<T, C extends ModalCloseable>
/*     */   implements ManagedConnPool<T, C>
/*     */ {
/*     */   private final TimeValue timeToLive;
/*     */   private final PoolReusePolicy policy;
/*     */   private final DisposalCallback<C> disposalCallback;
/*     */   private final ConnPoolListener<T> connPoolListener;
/*     */   private final ConcurrentMap<T, PerRoutePool<T, C>> routeToPool;
/*     */   private final AtomicBoolean isShutDown;
/*     */   private volatile int defaultMaxPerRoute;
/*     */   
/*     */   public LaxConnPool(int defaultMaxPerRoute, TimeValue timeToLive, PoolReusePolicy policy, DisposalCallback<C> disposalCallback, ConnPoolListener<T> connPoolListener) {
/*  93 */     Args.positive(defaultMaxPerRoute, "Max per route value");
/*  94 */     this.timeToLive = TimeValue.defaultsToNegativeOneMillisecond(timeToLive);
/*  95 */     this.policy = (policy != null) ? policy : PoolReusePolicy.LIFO;
/*  96 */     this.disposalCallback = disposalCallback;
/*  97 */     this.connPoolListener = connPoolListener;
/*  98 */     this.routeToPool = new ConcurrentHashMap<>();
/*  99 */     this.isShutDown = new AtomicBoolean(false);
/* 100 */     this.defaultMaxPerRoute = defaultMaxPerRoute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LaxConnPool(int defaultMaxPerRoute, TimeValue timeToLive, PoolReusePolicy policy, ConnPoolListener<T> connPoolListener) {
/* 111 */     this(defaultMaxPerRoute, timeToLive, policy, null, connPoolListener);
/*     */   }
/*     */   
/*     */   public LaxConnPool(int defaultMaxPerRoute) {
/* 115 */     this(defaultMaxPerRoute, TimeValue.NEG_ONE_MILLISECOND, PoolReusePolicy.LIFO, null, null);
/*     */   }
/*     */   
/*     */   public boolean isShutdown() {
/* 119 */     return this.isShutDown.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 124 */     if (this.isShutDown.compareAndSet(false, true)) {
/* 125 */       for (Iterator<PerRoutePool<T, C>> it = this.routeToPool.values().iterator(); it.hasNext(); ) {
/* 126 */         PerRoutePool<T, C> routePool = it.next();
/* 127 */         routePool.shutdown(closeMode);
/*     */       } 
/* 129 */       this.routeToPool.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 135 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */   
/*     */   private PerRoutePool<T, C> getPool(T route) {
/* 139 */     PerRoutePool<T, C> routePool = this.routeToPool.get(route);
/* 140 */     if (routePool == null) {
/* 141 */       PerRoutePool<T, C> newRoutePool = new PerRoutePool<>(route, this.defaultMaxPerRoute, this.timeToLive, this.policy, this, this.disposalCallback, this.connPoolListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       routePool = this.routeToPool.putIfAbsent(route, newRoutePool);
/* 150 */       if (routePool == null) {
/* 151 */         routePool = newRoutePool;
/*     */       }
/*     */     } 
/* 154 */     return routePool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<PoolEntry<T, C>> lease(T route, Object state, Timeout requestTimeout, FutureCallback<PoolEntry<T, C>> callback) {
/* 162 */     Args.notNull(route, "Route");
/* 163 */     Asserts.check(!this.isShutDown.get(), "Connection pool shut down");
/* 164 */     PerRoutePool<T, C> routePool = getPool(route);
/* 165 */     return routePool.lease(state, requestTimeout, callback);
/*     */   }
/*     */   
/*     */   public Future<PoolEntry<T, C>> lease(T route, Object state) {
/* 169 */     return lease(route, state, Timeout.DISABLED, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void release(PoolEntry<T, C> entry, boolean reusable) {
/* 174 */     if (entry == null) {
/*     */       return;
/*     */     }
/* 177 */     if (this.isShutDown.get()) {
/*     */       return;
/*     */     }
/* 180 */     PerRoutePool<T, C> routePool = getPool(entry.getRoute());
/* 181 */     routePool.release(entry, reusable);
/*     */   }
/*     */   
/*     */   public void validatePendingRequests() {
/* 185 */     for (PerRoutePool<T, C> routePool : this.routeToPool.values()) {
/* 186 */       routePool.validatePendingRequests();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {}
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 196 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 201 */     Args.positive(max, "Max value");
/* 202 */     this.defaultMaxPerRoute = max;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 207 */     return this.defaultMaxPerRoute;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(T route, int max) {
/* 212 */     Args.notNull(route, "Route");
/* 213 */     PerRoutePool<T, C> routePool = getPool(route);
/* 214 */     routePool.setMax((max > -1) ? max : this.defaultMaxPerRoute);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(T route) {
/* 219 */     Args.notNull(route, "Route");
/* 220 */     PerRoutePool<T, C> routePool = getPool(route);
/* 221 */     return routePool.getMax();
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 226 */     int leasedTotal = 0;
/* 227 */     int pendingTotal = 0;
/* 228 */     int availableTotal = 0;
/* 229 */     int maxTotal = 0;
/* 230 */     for (PerRoutePool<T, C> routePool : this.routeToPool.values()) {
/* 231 */       leasedTotal += routePool.getLeasedCount();
/* 232 */       pendingTotal += routePool.getPendingCount();
/* 233 */       availableTotal += routePool.getAvailableCount();
/* 234 */       maxTotal += routePool.getMax();
/*     */     } 
/* 236 */     return new PoolStats(leasedTotal, pendingTotal, availableTotal, maxTotal);
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(T route) {
/* 241 */     Args.notNull(route, "Route");
/* 242 */     PerRoutePool<T, C> routePool = getPool(route);
/* 243 */     return new PoolStats(routePool
/* 244 */         .getLeasedCount(), routePool
/* 245 */         .getPendingCount(), routePool
/* 246 */         .getAvailableCount(), routePool
/* 247 */         .getMax());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<T> getRoutes() {
/* 252 */     return new HashSet<>(this.routeToPool.keySet());
/*     */   }
/*     */   
/*     */   public void enumAvailable(Callback<PoolEntry<T, C>> callback) {
/* 256 */     for (PerRoutePool<T, C> routePool : this.routeToPool.values()) {
/* 257 */       routePool.enumAvailable(callback);
/*     */     }
/*     */   }
/*     */   
/*     */   public void enumLeased(Callback<PoolEntry<T, C>> callback) {
/* 262 */     for (PerRoutePool<T, C> routePool : this.routeToPool.values()) {
/* 263 */       routePool.enumLeased(callback);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdle(TimeValue idleTime) {
/* 269 */     long deadline = System.currentTimeMillis() - (TimeValue.isPositive(idleTime) ? idleTime.toMilliseconds() : 0L);
/* 270 */     enumAvailable(entry -> {
/*     */           if (entry.getUpdated() <= deadline) {
/*     */             entry.discardConnection(CloseMode.GRACEFUL);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 279 */     long now = System.currentTimeMillis();
/* 280 */     enumAvailable(entry -> {
/*     */           if (entry.getExpiryDeadline().isBefore(now)) {
/*     */             entry.discardConnection(CloseMode.GRACEFUL);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 289 */     PoolStats totalStats = getTotalStats();
/* 290 */     StringBuilder buffer = new StringBuilder();
/* 291 */     buffer.append("[leased: ");
/* 292 */     buffer.append(totalStats.getLeased());
/* 293 */     buffer.append("][available: ");
/* 294 */     buffer.append(totalStats.getAvailable());
/* 295 */     buffer.append("][pending: ");
/* 296 */     buffer.append(totalStats.getPending());
/* 297 */     buffer.append("]");
/* 298 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   static class LeaseRequest<T, C extends ModalCloseable>
/*     */     implements Cancellable
/*     */   {
/*     */     private final Object state;
/*     */     
/*     */     private final Deadline deadline;
/*     */     
/*     */     private final BasicFuture<PoolEntry<T, C>> future;
/*     */     
/*     */     LeaseRequest(Object state, Timeout requestTimeout, BasicFuture<PoolEntry<T, C>> future) {
/* 312 */       this.state = state;
/* 313 */       this.deadline = Deadline.calculate((TimeValue)requestTimeout);
/* 314 */       this.future = future;
/*     */     }
/*     */     
/*     */     BasicFuture<PoolEntry<T, C>> getFuture() {
/* 318 */       return this.future;
/*     */     }
/*     */     
/*     */     public Object getState() {
/* 322 */       return this.state;
/*     */     }
/*     */     
/*     */     public Deadline getDeadline() {
/* 326 */       return this.deadline;
/*     */     }
/*     */     
/*     */     public boolean isDone() {
/* 330 */       return this.future.isDone();
/*     */     }
/*     */     
/*     */     public boolean completed(PoolEntry<T, C> result) {
/* 334 */       return this.future.completed(result);
/*     */     }
/*     */     
/*     */     public boolean failed(Exception ex) {
/* 338 */       return this.future.failed(ex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean cancel() {
/* 343 */       return this.future.cancel();
/*     */     } }
/*     */   static class PerRoutePool<T, C extends ModalCloseable> { private final T route; private final TimeValue timeToLive; private final PoolReusePolicy policy; private final DisposalCallback<C> disposalCallback; private final ConnPoolListener<T> connPoolListener; private final ConnPoolStats<T> connPoolStats; private final ConcurrentMap<PoolEntry<T, C>, Boolean> leased; private final Deque<AtomicMarkableReference<PoolEntry<T, C>>> available; private final Deque<LaxConnPool.LeaseRequest<T, C>> pending; private final AtomicBoolean terminated;
/*     */     private final AtomicInteger allocated;
/*     */     private final AtomicLong releaseSeqNum;
/*     */     private volatile int max;
/*     */     
/* 350 */     private enum RequestServiceStrategy { FIRST_SUCCESSFUL, ALL; }
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
/*     */     PerRoutePool(T route, int max, TimeValue timeToLive, PoolReusePolicy policy, ConnPoolStats<T> connPoolStats, DisposalCallback<C> disposalCallback, ConnPoolListener<T> connPoolListener) {
/* 376 */       this.route = route;
/* 377 */       this.timeToLive = timeToLive;
/* 378 */       this.policy = policy;
/* 379 */       this.connPoolStats = connPoolStats;
/* 380 */       this.disposalCallback = disposalCallback;
/* 381 */       this.connPoolListener = connPoolListener;
/* 382 */       this.leased = new ConcurrentHashMap<>();
/* 383 */       this.available = new ConcurrentLinkedDeque<>();
/* 384 */       this.pending = new ConcurrentLinkedDeque<>();
/* 385 */       this.terminated = new AtomicBoolean(false);
/* 386 */       this.allocated = new AtomicInteger(0);
/* 387 */       this.releaseSeqNum = new AtomicLong(0L);
/* 388 */       this.max = max;
/*     */     }
/*     */     
/*     */     public void shutdown(CloseMode closeMode) {
/* 392 */       if (this.terminated.compareAndSet(false, true)) {
/*     */         AtomicMarkableReference<PoolEntry<T, C>> entryRef;
/* 394 */         while ((entryRef = this.available.poll()) != null) {
/* 395 */           ((PoolEntry)entryRef.getReference()).discardConnection(closeMode);
/*     */         }
/* 397 */         for (PoolEntry<T, C> entry : this.leased.keySet()) {
/* 398 */           entry.discardConnection(closeMode);
/*     */         }
/* 400 */         this.leased.clear();
/*     */         LaxConnPool.LeaseRequest<T, C> leaseRequest;
/* 402 */         while ((leaseRequest = this.pending.poll()) != null) {
/* 403 */           leaseRequest.cancel();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private PoolEntry<T, C> createPoolEntry() {
/* 409 */       int poolMax = this.max;
/*     */       
/*     */       while (true) {
/* 412 */         int prev = this.allocated.get();
/* 413 */         int next = (prev < poolMax) ? (prev + 1) : prev;
/* 414 */         if (this.allocated.compareAndSet(prev, next))
/* 415 */           return (prev < next) ? new PoolEntry<>(this.route, this.timeToLive, this.disposalCallback) : null; 
/*     */       } 
/*     */     }
/*     */     private void deallocatePoolEntry() {
/* 419 */       this.allocated.decrementAndGet();
/*     */     }
/*     */     
/*     */     private void addLeased(PoolEntry<T, C> entry) {
/* 423 */       if (this.leased.putIfAbsent(entry, Boolean.TRUE) != null)
/* 424 */         throw new IllegalStateException("Pool entry already present in the set of leased entries"); 
/* 425 */       if (this.connPoolListener != null) {
/* 426 */         this.connPoolListener.onLease(this.route, this.connPoolStats);
/*     */       }
/*     */     }
/*     */     
/*     */     private void removeLeased(PoolEntry<T, C> entry) {
/* 431 */       if (this.connPoolListener != null) {
/* 432 */         this.connPoolListener.onRelease(this.route, this.connPoolStats);
/*     */       }
/* 434 */       if (!this.leased.remove(entry, Boolean.TRUE)) {
/* 435 */         throw new IllegalStateException("Pool entry is not present in the set of leased entries");
/*     */       }
/*     */     }
/*     */     
/*     */     private PoolEntry<T, C> getAvailableEntry(Object state) {
/* 440 */       for (Iterator<AtomicMarkableReference<PoolEntry<T, C>>> it = this.available.iterator(); it.hasNext(); ) {
/* 441 */         AtomicMarkableReference<PoolEntry<T, C>> ref = it.next();
/* 442 */         PoolEntry<T, C> entry = ref.getReference();
/* 443 */         if (ref.compareAndSet(entry, entry, false, true)) {
/* 444 */           it.remove();
/* 445 */           if (entry.getExpiryDeadline().isExpired()) {
/* 446 */             entry.discardConnection(CloseMode.GRACEFUL);
/*     */           }
/* 448 */           if (!Objects.equals(entry.getState(), state)) {
/* 449 */             entry.discardConnection(CloseMode.GRACEFUL);
/*     */           }
/* 451 */           return entry;
/*     */         } 
/*     */       } 
/* 454 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Future<PoolEntry<T, C>> lease(Object state, Timeout requestTimeout, FutureCallback<PoolEntry<T, C>> callback) {
/* 461 */       Asserts.check(!this.terminated.get(), "Connection pool shut down");
/* 462 */       BasicFuture<PoolEntry<T, C>> future = new BasicFuture<PoolEntry<T, C>>(callback)
/*     */         {
/*     */           
/*     */           public synchronized PoolEntry<T, C> get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
/*     */           {
/*     */             try {
/* 468 */               return (PoolEntry<T, C>)super.get(timeout, unit);
/* 469 */             } catch (TimeoutException ex) {
/* 470 */               cancel();
/* 471 */               throw ex;
/*     */             } 
/*     */           }
/*     */         };
/*     */       
/* 476 */       long releaseState = this.releaseSeqNum.get();
/* 477 */       PoolEntry<T, C> entry = null;
/* 478 */       if (this.pending.isEmpty()) {
/* 479 */         entry = getAvailableEntry(state);
/* 480 */         if (entry == null) {
/* 481 */           entry = createPoolEntry();
/*     */         }
/*     */       } 
/* 484 */       if (entry != null) {
/* 485 */         addLeased(entry);
/* 486 */         future.completed(entry);
/*     */       } else {
/* 488 */         this.pending.add(new LaxConnPool.LeaseRequest<>(state, requestTimeout, future));
/* 489 */         if (releaseState != this.releaseSeqNum.get()) {
/* 490 */           servicePendingRequest();
/*     */         }
/*     */       } 
/* 493 */       return (Future<PoolEntry<T, C>>)future;
/*     */     }
/*     */     
/*     */     public void release(PoolEntry<T, C> releasedEntry, boolean reusable) {
/* 497 */       removeLeased(releasedEntry);
/* 498 */       if (!reusable || releasedEntry.getExpiryDeadline().isExpired()) {
/* 499 */         releasedEntry.discardConnection(CloseMode.GRACEFUL);
/*     */       }
/* 501 */       if (releasedEntry.hasConnection()) {
/* 502 */         switch (this.policy) {
/*     */           case LIFO:
/* 504 */             this.available.addFirst(new AtomicMarkableReference<>(releasedEntry, false));
/*     */             break;
/*     */           case FIFO:
/* 507 */             this.available.addLast(new AtomicMarkableReference<>(releasedEntry, false));
/*     */             break;
/*     */           default:
/* 510 */             throw new IllegalStateException("Unexpected ConnPoolPolicy value: " + this.policy);
/*     */         } 
/*     */       
/*     */       } else {
/* 514 */         deallocatePoolEntry();
/*     */       } 
/* 516 */       this.releaseSeqNum.incrementAndGet();
/* 517 */       servicePendingRequest();
/*     */     }
/*     */ 
/*     */     
/*     */     private void servicePendingRequest() {
/* 522 */       servicePendingRequests(RequestServiceStrategy.FIRST_SUCCESSFUL);
/*     */     }
/*     */     
/*     */     private void servicePendingRequests(RequestServiceStrategy serviceStrategy) {
/*     */       LaxConnPool.LeaseRequest<T, C> leaseRequest;
/* 527 */       while ((leaseRequest = this.pending.poll()) != null) {
/* 528 */         if (leaseRequest.isDone()) {
/*     */           continue;
/*     */         }
/* 531 */         Object state = leaseRequest.getState();
/* 532 */         Deadline deadline = leaseRequest.getDeadline();
/*     */         
/* 534 */         if (deadline.isExpired()) {
/* 535 */           leaseRequest.failed((Exception)DeadlineTimeoutException.from(deadline)); continue;
/*     */         } 
/* 537 */         long releaseState = this.releaseSeqNum.get();
/* 538 */         PoolEntry<T, C> entry = getAvailableEntry(state);
/* 539 */         if (entry == null) {
/* 540 */           entry = createPoolEntry();
/*     */         }
/* 542 */         if (entry != null) {
/* 543 */           addLeased(entry);
/* 544 */           if (!leaseRequest.completed(entry)) {
/* 545 */             release(entry, true);
/*     */           }
/* 547 */           if (serviceStrategy == RequestServiceStrategy.FIRST_SUCCESSFUL) {
/*     */             break;
/*     */           }
/*     */           continue;
/*     */         } 
/* 552 */         this.pending.addFirst(leaseRequest);
/* 553 */         if (releaseState == this.releaseSeqNum.get()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void validatePendingRequests() {
/* 562 */       Iterator<LaxConnPool.LeaseRequest<T, C>> it = this.pending.iterator();
/* 563 */       while (it.hasNext()) {
/* 564 */         LaxConnPool.LeaseRequest<T, C> request = it.next();
/* 565 */         BasicFuture<PoolEntry<T, C>> future = request.getFuture();
/* 566 */         if (future.isCancelled() && !request.isDone()) {
/* 567 */           it.remove(); continue;
/*     */         } 
/* 569 */         Deadline deadline = request.getDeadline();
/* 570 */         if (deadline.isExpired()) {
/* 571 */           request.failed((Exception)DeadlineTimeoutException.from(deadline));
/*     */         }
/* 573 */         if (request.isDone()) {
/* 574 */           it.remove();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public final T getRoute() {
/* 581 */       return this.route;
/*     */     }
/*     */     
/*     */     public int getMax() {
/* 585 */       return this.max;
/*     */     }
/*     */     
/*     */     public void setMax(int max) {
/* 589 */       this.max = max;
/*     */     }
/*     */     
/*     */     public int getPendingCount() {
/* 593 */       return this.pending.size();
/*     */     }
/*     */     
/*     */     public int getLeasedCount() {
/* 597 */       return this.leased.size();
/*     */     }
/*     */     
/*     */     public int getAvailableCount() {
/* 601 */       return this.available.size();
/*     */     }
/*     */     
/*     */     public void enumAvailable(Callback<PoolEntry<T, C>> callback) {
/* 605 */       for (Iterator<AtomicMarkableReference<PoolEntry<T, C>>> it = this.available.iterator(); it.hasNext(); ) {
/* 606 */         AtomicMarkableReference<PoolEntry<T, C>> ref = it.next();
/* 607 */         PoolEntry<T, C> entry = ref.getReference();
/* 608 */         if (ref.compareAndSet(entry, entry, false, true)) {
/* 609 */           callback.execute(entry);
/* 610 */           if (!entry.hasConnection()) {
/* 611 */             deallocatePoolEntry();
/* 612 */             it.remove();
/*     */             continue;
/*     */           } 
/* 615 */           ref.set(entry, false);
/*     */         } 
/*     */       } 
/*     */       
/* 619 */       this.releaseSeqNum.incrementAndGet();
/* 620 */       servicePendingRequests(RequestServiceStrategy.ALL);
/*     */     }
/*     */     
/*     */     public void enumLeased(Callback<PoolEntry<T, C>> callback) {
/* 624 */       for (Iterator<PoolEntry<T, C>> it = this.leased.keySet().iterator(); it.hasNext(); ) {
/* 625 */         PoolEntry<T, C> entry = it.next();
/* 626 */         callback.execute(entry);
/* 627 */         if (!entry.hasConnection()) {
/* 628 */           deallocatePoolEntry();
/* 629 */           it.remove();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 636 */       StringBuilder buffer = new StringBuilder();
/* 637 */       buffer.append("[route: ");
/* 638 */       buffer.append(this.route);
/* 639 */       buffer.append("][leased: ");
/* 640 */       buffer.append(this.leased.size());
/* 641 */       buffer.append("][available: ");
/* 642 */       buffer.append(this.available.size());
/* 643 */       buffer.append("][pending: ");
/* 644 */       buffer.append(this.pending.size());
/* 645 */       buffer.append("]");
/* 646 */       return buffer.toString();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/pool/LaxConnPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */