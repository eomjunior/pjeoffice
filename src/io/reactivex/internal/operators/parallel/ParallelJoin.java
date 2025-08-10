/*     */ package io.reactivex.internal.operators.parallel;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.parallel.ParallelFlowable;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
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
/*     */ public final class ParallelJoin<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final ParallelFlowable<? extends T> source;
/*     */   final int prefetch;
/*     */   final boolean delayErrors;
/*     */   
/*     */   public ParallelJoin(ParallelFlowable<? extends T> source, int prefetch, boolean delayErrors) {
/*  44 */     this.source = source;
/*  45 */     this.prefetch = prefetch;
/*  46 */     this.delayErrors = delayErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*     */     JoinSubscriptionBase<T> parent;
/*  52 */     if (this.delayErrors) {
/*  53 */       parent = new JoinSubscriptionDelayError<T>(s, this.source.parallelism(), this.prefetch);
/*     */     } else {
/*  55 */       parent = new JoinSubscription<T>(s, this.source.parallelism(), this.prefetch);
/*     */     } 
/*  57 */     s.onSubscribe(parent);
/*  58 */     this.source.subscribe((Subscriber[])parent.subscribers);
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class JoinSubscriptionBase<T>
/*     */     extends AtomicInteger
/*     */     implements Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 3100232009247827843L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     final ParallelJoin.JoinInnerSubscriber<T>[] subscribers;
/*  70 */     final AtomicThrowable errors = new AtomicThrowable();
/*     */     
/*  72 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*  76 */     final AtomicInteger done = new AtomicInteger();
/*     */     
/*     */     JoinSubscriptionBase(Subscriber<? super T> actual, int n, int prefetch) {
/*  79 */       this.downstream = actual;
/*     */       
/*  81 */       ParallelJoin.JoinInnerSubscriber[] arrayOfJoinInnerSubscriber = new ParallelJoin.JoinInnerSubscriber[n];
/*     */       
/*  83 */       for (int i = 0; i < n; i++) {
/*  84 */         arrayOfJoinInnerSubscriber[i] = new ParallelJoin.JoinInnerSubscriber<T>(this, prefetch);
/*     */       }
/*     */       
/*  87 */       this.subscribers = (ParallelJoin.JoinInnerSubscriber<T>[])arrayOfJoinInnerSubscriber;
/*  88 */       this.done.lazySet(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  93 */       if (SubscriptionHelper.validate(n)) {
/*  94 */         BackpressureHelper.add(this.requested, n);
/*  95 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 101 */       if (!this.cancelled) {
/* 102 */         this.cancelled = true;
/*     */         
/* 104 */         cancelAll();
/*     */         
/* 106 */         if (getAndIncrement() == 0) {
/* 107 */           cleanup();
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     void cancelAll() {
/* 113 */       for (ParallelJoin.JoinInnerSubscriber<T> s : this.subscribers) {
/* 114 */         s.cancel();
/*     */       }
/*     */     }
/*     */     
/*     */     void cleanup() {
/* 119 */       for (ParallelJoin.JoinInnerSubscriber<T> s : this.subscribers)
/* 120 */         s.queue = null; 
/*     */     }
/*     */     
/*     */     abstract void onNext(ParallelJoin.JoinInnerSubscriber<T> param1JoinInnerSubscriber, T param1T);
/*     */     
/*     */     abstract void onError(Throwable param1Throwable);
/*     */     
/*     */     abstract void onComplete();
/*     */     
/*     */     abstract void drain();
/*     */   }
/*     */   
/*     */   static final class JoinSubscription<T>
/*     */     extends JoinSubscriptionBase<T>
/*     */   {
/*     */     private static final long serialVersionUID = 6312374661811000451L;
/*     */     
/*     */     JoinSubscription(Subscriber<? super T> actual, int n, int prefetch) {
/* 138 */       super(actual, n, prefetch);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(ParallelJoin.JoinInnerSubscriber<T> inner, T value) {
/* 143 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 144 */         if (this.requested.get() != 0L) {
/* 145 */           this.downstream.onNext(value);
/* 146 */           if (this.requested.get() != Long.MAX_VALUE) {
/* 147 */             this.requested.decrementAndGet();
/*     */           }
/* 149 */           inner.request(1L);
/*     */         } else {
/* 151 */           SimplePlainQueue<T> q = inner.getQueue();
/*     */           
/* 153 */           if (!q.offer(value)) {
/* 154 */             cancelAll();
/* 155 */             MissingBackpressureException missingBackpressureException = new MissingBackpressureException("Queue full?!");
/* 156 */             if (this.errors.compareAndSet(null, missingBackpressureException)) {
/* 157 */               this.downstream.onError((Throwable)missingBackpressureException);
/*     */             } else {
/* 159 */               RxJavaPlugins.onError((Throwable)missingBackpressureException);
/*     */             } 
/*     */             return;
/*     */           } 
/*     */         } 
/* 164 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 168 */         SimplePlainQueue<T> q = inner.getQueue();
/*     */         
/* 170 */         if (!q.offer(value)) {
/* 171 */           cancelAll();
/* 172 */           onError((Throwable)new MissingBackpressureException("Queue full?!"));
/*     */           
/*     */           return;
/*     */         } 
/* 176 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */       
/* 181 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/* 186 */       if (this.errors.compareAndSet(null, e)) {
/* 187 */         cancelAll();
/* 188 */         drain();
/*     */       }
/* 190 */       else if (e != this.errors.get()) {
/* 191 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 198 */       this.done.decrementAndGet();
/* 199 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 204 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 208 */       drainLoop();
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 212 */       int missed = 1;
/*     */       
/* 214 */       ParallelJoin.JoinInnerSubscriber<T>[] s = this.subscribers;
/* 215 */       int n = s.length;
/* 216 */       Subscriber<? super T> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 220 */         long r = this.requested.get();
/* 221 */         long e = 0L;
/*     */ 
/*     */         
/* 224 */         label70: while (e != r) {
/* 225 */           if (this.cancelled) {
/* 226 */             cleanup();
/*     */             
/*     */             return;
/*     */           } 
/* 230 */           Throwable ex = (Throwable)this.errors.get();
/* 231 */           if (ex != null) {
/* 232 */             cleanup();
/* 233 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 237 */           boolean d = (this.done.get() == 0);
/*     */           
/* 239 */           boolean empty = true;
/*     */           
/* 241 */           for (int i = 0; i < s.length; i++) {
/* 242 */             ParallelJoin.JoinInnerSubscriber<T> inner = s[i];
/* 243 */             SimplePlainQueue<T> q = inner.queue;
/* 244 */             if (q != null) {
/* 245 */               T v = (T)q.poll();
/*     */ 
/*     */               
/* 248 */               empty = false;
/* 249 */               a.onNext(v);
/* 250 */               inner.requestOne();
/* 251 */               if (v != null && ++e == r) {
/*     */                 break label70;
/*     */               }
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 258 */           if (d && empty) {
/* 259 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/* 263 */           if (empty) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/* 268 */         if (e == r) {
/* 269 */           if (this.cancelled) {
/* 270 */             cleanup();
/*     */             
/*     */             return;
/*     */           } 
/* 274 */           Throwable ex = (Throwable)this.errors.get();
/* 275 */           if (ex != null) {
/* 276 */             cleanup();
/* 277 */             a.onError(ex);
/*     */             
/*     */             return;
/*     */           } 
/* 281 */           boolean d = (this.done.get() == 0);
/*     */           
/* 283 */           boolean empty = true;
/*     */           
/* 285 */           for (int i = 0; i < n; i++) {
/* 286 */             ParallelJoin.JoinInnerSubscriber<T> inner = s[i];
/*     */             
/* 288 */             SimplePlainQueue<T> simplePlainQueue = inner.queue;
/* 289 */             if (simplePlainQueue != null && !simplePlainQueue.isEmpty()) {
/* 290 */               empty = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 295 */           if (d && empty) {
/* 296 */             a.onComplete();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 301 */         if (e != 0L && r != Long.MAX_VALUE) {
/* 302 */           this.requested.addAndGet(-e);
/*     */         }
/*     */         
/* 305 */         int w = get();
/* 306 */         if (w == missed) {
/* 307 */           missed = addAndGet(-missed);
/* 308 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 312 */         missed = w;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class JoinSubscriptionDelayError<T>
/*     */     extends JoinSubscriptionBase<T>
/*     */   {
/*     */     private static final long serialVersionUID = -5737965195918321883L;
/*     */     
/*     */     JoinSubscriptionDelayError(Subscriber<? super T> actual, int n, int prefetch) {
/* 323 */       super(actual, n, prefetch);
/*     */     }
/*     */ 
/*     */     
/*     */     void onNext(ParallelJoin.JoinInnerSubscriber<T> inner, T value) {
/* 328 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 329 */         if (this.requested.get() != 0L) {
/* 330 */           this.downstream.onNext(value);
/* 331 */           if (this.requested.get() != Long.MAX_VALUE) {
/* 332 */             this.requested.decrementAndGet();
/*     */           }
/* 334 */           inner.request(1L);
/*     */         } else {
/* 336 */           SimplePlainQueue<T> q = inner.getQueue();
/*     */           
/* 338 */           if (!q.offer(value)) {
/* 339 */             inner.cancel();
/* 340 */             this.errors.addThrowable((Throwable)new MissingBackpressureException("Queue full?!"));
/* 341 */             this.done.decrementAndGet();
/* 342 */             drainLoop();
/*     */             return;
/*     */           } 
/*     */         } 
/* 346 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 350 */         SimplePlainQueue<T> q = inner.getQueue();
/*     */         
/* 352 */         if (!q.offer(value) && 
/* 353 */           inner.cancel()) {
/* 354 */           this.errors.addThrowable((Throwable)new MissingBackpressureException("Queue full?!"));
/* 355 */           this.done.decrementAndGet();
/*     */         } 
/*     */ 
/*     */         
/* 359 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */       
/* 364 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     void onError(Throwable e) {
/* 369 */       this.errors.addThrowable(e);
/* 370 */       this.done.decrementAndGet();
/* 371 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void onComplete() {
/* 376 */       this.done.decrementAndGet();
/* 377 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void drain() {
/* 382 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 386 */       drainLoop();
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 390 */       int missed = 1;
/*     */       
/* 392 */       ParallelJoin.JoinInnerSubscriber<T>[] s = this.subscribers;
/* 393 */       int n = s.length;
/* 394 */       Subscriber<? super T> a = this.downstream;
/*     */ 
/*     */       
/*     */       while (true) {
/* 398 */         long r = this.requested.get();
/* 399 */         long e = 0L;
/*     */ 
/*     */         
/* 402 */         label70: while (e != r) {
/* 403 */           if (this.cancelled) {
/* 404 */             cleanup();
/*     */             
/*     */             return;
/*     */           } 
/* 408 */           boolean d = (this.done.get() == 0);
/*     */           
/* 410 */           boolean empty = true;
/*     */           
/* 412 */           for (int i = 0; i < n; i++) {
/* 413 */             ParallelJoin.JoinInnerSubscriber<T> inner = s[i];
/*     */             
/* 415 */             SimplePlainQueue<T> q = inner.queue;
/* 416 */             if (q != null) {
/* 417 */               T v = (T)q.poll();
/*     */ 
/*     */               
/* 420 */               empty = false;
/* 421 */               a.onNext(v);
/* 422 */               inner.requestOne();
/* 423 */               if (v != null && ++e == r) {
/*     */                 break label70;
/*     */               }
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 430 */           if (d && empty) {
/* 431 */             Throwable ex = (Throwable)this.errors.get();
/* 432 */             if (ex != null) {
/* 433 */               a.onError(this.errors.terminate());
/*     */             } else {
/* 435 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 440 */           if (empty) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/* 445 */         if (e == r) {
/* 446 */           if (this.cancelled) {
/* 447 */             cleanup();
/*     */             
/*     */             return;
/*     */           } 
/* 451 */           boolean d = (this.done.get() == 0);
/*     */           
/* 453 */           boolean empty = true;
/*     */           
/* 455 */           for (int i = 0; i < n; i++) {
/* 456 */             ParallelJoin.JoinInnerSubscriber<T> inner = s[i];
/*     */             
/* 458 */             SimplePlainQueue<T> simplePlainQueue = inner.queue;
/* 459 */             if (simplePlainQueue != null && !simplePlainQueue.isEmpty()) {
/* 460 */               empty = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 465 */           if (d && empty) {
/* 466 */             Throwable ex = (Throwable)this.errors.get();
/* 467 */             if (ex != null) {
/* 468 */               a.onError(this.errors.terminate());
/*     */             } else {
/* 470 */               a.onComplete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 476 */         if (e != 0L && r != Long.MAX_VALUE) {
/* 477 */           this.requested.addAndGet(-e);
/*     */         }
/*     */         
/* 480 */         int w = get();
/* 481 */         if (w == missed) {
/* 482 */           missed = addAndGet(-missed);
/* 483 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 487 */         missed = w;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class JoinInnerSubscriber<T>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 8410034718427740355L;
/*     */     
/*     */     final ParallelJoin.JoinSubscriptionBase<T> parent;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     long produced;
/*     */     
/*     */     volatile SimplePlainQueue<T> queue;
/*     */     
/*     */     JoinInnerSubscriber(ParallelJoin.JoinSubscriptionBase<T> parent, int prefetch) {
/* 510 */       this.parent = parent;
/* 511 */       this.prefetch = prefetch;
/* 512 */       this.limit = prefetch - (prefetch >> 2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 517 */       SubscriptionHelper.setOnce(this, s, this.prefetch);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 522 */       this.parent.onNext(this, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 527 */       this.parent.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 532 */       this.parent.onComplete();
/*     */     }
/*     */     
/*     */     public void requestOne() {
/* 536 */       long p = this.produced + 1L;
/* 537 */       if (p == this.limit) {
/* 538 */         this.produced = 0L;
/* 539 */         get().request(p);
/*     */       } else {
/* 541 */         this.produced = p;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void request(long n) {
/* 546 */       long p = this.produced + n;
/* 547 */       if (p >= this.limit) {
/* 548 */         this.produced = 0L;
/* 549 */         get().request(p);
/*     */       } else {
/* 551 */         this.produced = p;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean cancel() {
/* 556 */       return SubscriptionHelper.cancel(this);
/*     */     }
/*     */     SimplePlainQueue<T> getQueue() {
/*     */       SpscArrayQueue spscArrayQueue;
/* 560 */       SimplePlainQueue<T> q = this.queue;
/* 561 */       if (q == null) {
/* 562 */         spscArrayQueue = new SpscArrayQueue(this.prefetch);
/* 563 */         this.queue = (SimplePlainQueue<T>)spscArrayQueue;
/*     */       } 
/* 565 */       return (SimplePlainQueue<T>)spscArrayQueue;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/parallel/ParallelJoin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */