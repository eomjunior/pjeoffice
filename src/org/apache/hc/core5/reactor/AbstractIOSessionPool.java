/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.HashSet;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.concurrent.FutureContribution;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public abstract class AbstractIOSessionPool<T>
/*     */   implements ModalCloseable
/*     */ {
/*  63 */   private final ConcurrentMap<T, PoolEntry> sessionPool = new ConcurrentHashMap<>();
/*  64 */   private final AtomicBoolean closed = new AtomicBoolean(false);
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
/*     */   public final void close(CloseMode closeMode) {
/*  82 */     if (this.closed.compareAndSet(false, true)) {
/*  83 */       for (PoolEntry poolEntry : this.sessionPool.values()) {
/*  84 */         synchronized (poolEntry) {
/*  85 */           if (poolEntry.session != null) {
/*  86 */             closeSession(poolEntry.session, closeMode);
/*  87 */             poolEntry.session = null;
/*     */           } 
/*  89 */           if (poolEntry.sessionFuture != null) {
/*  90 */             poolEntry.sessionFuture.cancel(true);
/*  91 */             poolEntry.sessionFuture = null;
/*     */           } 
/*     */           while (true) {
/*  94 */             FutureCallback<IOSession> callback = poolEntry.requestQueue.poll();
/*  95 */             if (callback != null) {
/*  96 */               callback.cancelled();
/*     */               continue;
/*     */             } 
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 103 */       this.sessionPool.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/* 109 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */   
/*     */   PoolEntry getPoolEntry(T endpoint) {
/* 113 */     PoolEntry poolEntry = this.sessionPool.get(endpoint);
/* 114 */     if (poolEntry == null) {
/* 115 */       PoolEntry newPoolEntry = new PoolEntry();
/* 116 */       poolEntry = this.sessionPool.putIfAbsent(endpoint, newPoolEntry);
/* 117 */       if (poolEntry == null) {
/* 118 */         poolEntry = newPoolEntry;
/*     */       }
/*     */     } 
/* 121 */     return poolEntry;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Future<IOSession> getSession(final T endpoint, final Timeout connectTimeout, FutureCallback<IOSession> callback) {
/* 128 */     Args.notNull(endpoint, "Endpoint");
/* 129 */     Asserts.check(!this.closed.get(), "Connection pool shut down");
/* 130 */     final ComplexFuture<IOSession> future = new ComplexFuture(callback);
/* 131 */     final PoolEntry poolEntry = getPoolEntry(endpoint);
/* 132 */     getSessionInternal(poolEntry, false, endpoint, connectTimeout, new FutureCallback<IOSession>()
/*     */         {
/*     */           public void completed(IOSession ioSession)
/*     */           {
/* 136 */             AbstractIOSessionPool.this.validateSession(ioSession, result -> {
/*     */                   if (result.booleanValue()) {
/*     */                     future.completed(ioSession);
/*     */                   } else {
/*     */                     AbstractIOSessionPool.this.getSessionInternal(poolEntry, true, (T)endpoint, connectTimeout, (FutureCallback<IOSession>)new FutureContribution<IOSession>((BasicFuture)future)
/*     */                         {
/*     */                           
/*     */                           public void completed(IOSession ioSession1)
/*     */                           {
/* 145 */                             future.completed(ioSession1);
/*     */                           }
/*     */                         });
/*     */                   } 
/*     */                 });
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void failed(Exception ex) {
/* 155 */             future.failed(ex);
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 160 */             future.cancel();
/*     */           }
/*     */         });
/*     */     
/* 164 */     return (Future<IOSession>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getSessionInternal(final PoolEntry poolEntry, boolean requestNew, T namedEndpoint, Timeout connectTimeout, FutureCallback<IOSession> callback) {
/* 173 */     synchronized (poolEntry) {
/* 174 */       if (poolEntry.session != null && requestNew) {
/* 175 */         closeSession(poolEntry.session, CloseMode.GRACEFUL);
/* 176 */         poolEntry.session = null;
/*     */       } 
/* 178 */       if (poolEntry.session != null && !poolEntry.session.isOpen()) {
/* 179 */         poolEntry.session = null;
/*     */       }
/* 181 */       if (poolEntry.session != null) {
/* 182 */         callback.completed(poolEntry.session);
/*     */       } else {
/* 184 */         poolEntry.requestQueue.add(callback);
/* 185 */         if (poolEntry.sessionFuture != null && poolEntry.sessionFuture.isDone()) {
/* 186 */           poolEntry.sessionFuture = null;
/*     */         }
/* 188 */         if (poolEntry.sessionFuture == null) {
/* 189 */           poolEntry.sessionFuture = connectSession(namedEndpoint, connectTimeout, new FutureCallback<IOSession>()
/*     */               {
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void completed(IOSession result)
/*     */                 {
/* 196 */                   synchronized (poolEntry) {
/* 197 */                     poolEntry.session = result;
/*     */                     while (true) {
/* 199 */                       FutureCallback<IOSession> callback = poolEntry.requestQueue.poll();
/* 200 */                       if (callback != null) {
/* 201 */                         callback.completed(result);
/*     */                         continue;
/*     */                       } 
/*     */                       break;
/*     */                     } 
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void failed(Exception ex) {
/* 211 */                   synchronized (poolEntry) {
/* 212 */                     poolEntry.session = null;
/*     */                     while (true) {
/* 214 */                       FutureCallback<IOSession> callback = poolEntry.requestQueue.poll();
/* 215 */                       if (callback != null) {
/* 216 */                         callback.failed(ex);
/*     */                         continue;
/*     */                       } 
/*     */                       break;
/*     */                     } 
/*     */                   } 
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void cancelled() {
/* 226 */                   failed((Exception)new ConnectionClosedException("Connection request cancelled"));
/*     */                 }
/*     */               });
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void enumAvailable(Callback<IOSession> callback) {
/* 236 */     for (PoolEntry poolEntry : this.sessionPool.values()) {
/* 237 */       if (poolEntry.session != null) {
/* 238 */         synchronized (poolEntry) {
/* 239 */           if (poolEntry.session != null) {
/* 240 */             callback.execute(poolEntry.session);
/* 241 */             if (!poolEntry.session.isOpen()) {
/* 242 */               poolEntry.session = null;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void closeIdle(TimeValue idleTime) {
/* 251 */     long deadline = System.currentTimeMillis() - (TimeValue.isPositive(idleTime) ? idleTime.toMilliseconds() : 0L);
/* 252 */     for (PoolEntry poolEntry : this.sessionPool.values()) {
/* 253 */       if (poolEntry.session != null) {
/* 254 */         synchronized (poolEntry) {
/* 255 */           if (poolEntry.session != null && poolEntry.session.getLastReadTime() <= deadline) {
/* 256 */             closeSession(poolEntry.session, CloseMode.GRACEFUL);
/* 257 */             poolEntry.session = null;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public final Set<T> getRoutes() {
/* 265 */     return new HashSet<>(this.sessionPool.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 270 */     StringBuilder buffer = new StringBuilder();
/* 271 */     buffer.append("I/O sessions: ");
/* 272 */     buffer.append(this.sessionPool.size());
/* 273 */     return buffer.toString();
/*     */   }
/*     */   
/*     */   protected abstract Future<IOSession> connectSession(T paramT, Timeout paramTimeout, FutureCallback<IOSession> paramFutureCallback);
/*     */   
/*     */   protected abstract void validateSession(IOSession paramIOSession, Callback<Boolean> paramCallback);
/*     */   
/*     */   protected abstract void closeSession(IOSession paramIOSession, CloseMode paramCloseMode);
/*     */   
/*     */   static class PoolEntry {
/* 283 */     final Queue<FutureCallback<IOSession>> requestQueue = new ArrayDeque<>();
/*     */     volatile Future<IOSession> sessionFuture;
/*     */     volatile IOSession session;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/AbstractIOSessionPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */