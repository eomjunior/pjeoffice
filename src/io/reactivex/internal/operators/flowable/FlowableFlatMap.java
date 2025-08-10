/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Function;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.reactivestreams.Subscriber;
/*     */ import org.reactivestreams.Subscription;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableFlatMap<T, U>
/*     */   extends AbstractFlowableWithUpstream<T, U>
/*     */ {
/*     */   final Function<? super T, ? extends Publisher<? extends U>> mapper;
/*     */   final boolean delayErrors;
/*     */   final int maxConcurrency;
/*     */   final int bufferSize;
/*     */   
/*     */   public FlowableFlatMap(Flowable<T> source, Function<? super T, ? extends Publisher<? extends U>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
/*  41 */     super(source);
/*  42 */     this.mapper = mapper;
/*  43 */     this.delayErrors = delayErrors;
/*  44 */     this.maxConcurrency = maxConcurrency;
/*  45 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super U> s) {
/*  50 */     if (FlowableScalarXMap.tryScalarXMapSubscribe((Publisher<T>)this.source, s, this.mapper)) {
/*     */       return;
/*     */     }
/*  53 */     this.source.subscribe(subscribe(s, this.mapper, this.delayErrors, this.maxConcurrency, this.bufferSize));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T, U> FlowableSubscriber<T> subscribe(Subscriber<? super U> s, Function<? super T, ? extends Publisher<? extends U>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
/*  59 */     return new MergeSubscriber<T, U>(s, mapper, delayErrors, maxConcurrency, bufferSize);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class MergeSubscriber<T, U>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -2117620485640801370L;
/*     */     
/*     */     final Subscriber<? super U> downstream;
/*     */     final Function<? super T, ? extends Publisher<? extends U>> mapper;
/*     */     final boolean delayErrors;
/*     */     final int maxConcurrency;
/*     */     final int bufferSize;
/*     */     volatile SimplePlainQueue<U> queue;
/*     */     volatile boolean done;
/*  76 */     final AtomicThrowable errs = new AtomicThrowable();
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*  80 */     final AtomicReference<FlowableFlatMap.InnerSubscriber<?, ?>[]> subscribers = (AtomicReference)new AtomicReference<FlowableFlatMap.InnerSubscriber<?, ?>>();
/*     */     
/*  82 */     static final FlowableFlatMap.InnerSubscriber<?, ?>[] EMPTY = (FlowableFlatMap.InnerSubscriber<?, ?>[])new FlowableFlatMap.InnerSubscriber[0];
/*     */     
/*  84 */     static final FlowableFlatMap.InnerSubscriber<?, ?>[] CANCELLED = (FlowableFlatMap.InnerSubscriber<?, ?>[])new FlowableFlatMap.InnerSubscriber[0];
/*     */     
/*  86 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     long uniqueId;
/*     */     
/*     */     long lastId;
/*     */     
/*     */     int lastIndex;
/*     */     int scalarEmitted;
/*     */     final int scalarLimit;
/*     */     
/*     */     MergeSubscriber(Subscriber<? super U> actual, Function<? super T, ? extends Publisher<? extends U>> mapper, boolean delayErrors, int maxConcurrency, int bufferSize) {
/*  99 */       this.downstream = actual;
/* 100 */       this.mapper = mapper;
/* 101 */       this.delayErrors = delayErrors;
/* 102 */       this.maxConcurrency = maxConcurrency;
/* 103 */       this.bufferSize = bufferSize;
/* 104 */       this.scalarLimit = Math.max(1, maxConcurrency >> 1);
/* 105 */       this.subscribers.lazySet(EMPTY);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 110 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 111 */         this.upstream = s;
/* 112 */         this.downstream.onSubscribe(this);
/* 113 */         if (!this.cancelled) {
/* 114 */           if (this.maxConcurrency == Integer.MAX_VALUE) {
/* 115 */             s.request(Long.MAX_VALUE);
/*     */           } else {
/* 117 */             s.request(this.maxConcurrency);
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*     */       Publisher<? extends U> p;
/* 127 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 132 */         p = (Publisher<? extends U>)ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
/* 133 */       } catch (Throwable e) {
/* 134 */         Exceptions.throwIfFatal(e);
/* 135 */         this.upstream.cancel();
/* 136 */         onError(e);
/*     */         return;
/*     */       } 
/* 139 */       if (p instanceof Callable) {
/*     */         U u;
/*     */         
/*     */         try {
/* 143 */           u = ((Callable)p).call();
/* 144 */         } catch (Throwable ex) {
/* 145 */           Exceptions.throwIfFatal(ex);
/* 146 */           this.errs.addThrowable(ex);
/* 147 */           drain();
/*     */           
/*     */           return;
/*     */         } 
/* 151 */         if (u != null) {
/* 152 */           tryEmitScalar(u);
/*     */         }
/* 154 */         else if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled && ++this.scalarEmitted == this.scalarLimit) {
/*     */           
/* 156 */           this.scalarEmitted = 0;
/* 157 */           this.upstream.request(this.scalarLimit);
/*     */         } 
/*     */       } else {
/*     */         
/* 161 */         FlowableFlatMap.InnerSubscriber<T, U> inner = new FlowableFlatMap.InnerSubscriber<T, U>(this, this.uniqueId++);
/* 162 */         if (addInner(inner)) {
/* 163 */           p.subscribe((Subscriber)inner);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean addInner(FlowableFlatMap.InnerSubscriber<T, U> inner) {
/*     */       while (true) {
/* 170 */         FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber1 = (FlowableFlatMap.InnerSubscriber[])this.subscribers.get();
/* 171 */         if (arrayOfInnerSubscriber1 == CANCELLED) {
/* 172 */           inner.dispose();
/* 173 */           return false;
/*     */         } 
/* 175 */         int n = arrayOfInnerSubscriber1.length;
/* 176 */         FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber2 = new FlowableFlatMap.InnerSubscriber[n + 1];
/* 177 */         System.arraycopy(arrayOfInnerSubscriber1, 0, arrayOfInnerSubscriber2, 0, n);
/* 178 */         arrayOfInnerSubscriber2[n] = inner;
/* 179 */         if (this.subscribers.compareAndSet(arrayOfInnerSubscriber1, arrayOfInnerSubscriber2))
/* 180 */           return true; 
/*     */       } 
/*     */     }
/*     */     void removeInner(FlowableFlatMap.InnerSubscriber<T, U> inner) {
/*     */       FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber1;
/*     */       FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber2;
/*     */       do {
/* 187 */         arrayOfInnerSubscriber1 = (FlowableFlatMap.InnerSubscriber[])this.subscribers.get();
/* 188 */         int n = arrayOfInnerSubscriber1.length;
/* 189 */         if (n == 0) {
/*     */           return;
/*     */         }
/* 192 */         int j = -1;
/* 193 */         for (int i = 0; i < n; i++) {
/* 194 */           if (arrayOfInnerSubscriber1[i] == inner) {
/* 195 */             j = i;
/*     */             break;
/*     */           } 
/*     */         } 
/* 199 */         if (j < 0) {
/*     */           return;
/*     */         }
/*     */         
/* 203 */         if (n == 1) {
/* 204 */           FlowableFlatMap.InnerSubscriber<?, ?>[] b = EMPTY;
/*     */         } else {
/* 206 */           arrayOfInnerSubscriber2 = new FlowableFlatMap.InnerSubscriber[n - 1];
/* 207 */           System.arraycopy(arrayOfInnerSubscriber1, 0, arrayOfInnerSubscriber2, 0, j);
/* 208 */           System.arraycopy(arrayOfInnerSubscriber1, j + 1, arrayOfInnerSubscriber2, j, n - j - 1);
/*     */         } 
/* 210 */       } while (!this.subscribers.compareAndSet(arrayOfInnerSubscriber1, arrayOfInnerSubscriber2));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     SimpleQueue<U> getMainQueue() {
/*     */       SpscArrayQueue spscArrayQueue;
/* 217 */       SimplePlainQueue<U> q = this.queue;
/* 218 */       if (q == null) {
/* 219 */         if (this.maxConcurrency == Integer.MAX_VALUE) {
/* 220 */           SpscLinkedArrayQueue spscLinkedArrayQueue = new SpscLinkedArrayQueue(this.bufferSize);
/*     */         } else {
/* 222 */           spscArrayQueue = new SpscArrayQueue(this.maxConcurrency);
/*     */         } 
/* 224 */         this.queue = (SimplePlainQueue<U>)spscArrayQueue;
/*     */       } 
/* 226 */       return (SimpleQueue<U>)spscArrayQueue;
/*     */     }
/*     */     
/*     */     void tryEmitScalar(U value) {
/* 230 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 231 */         long r = this.requested.get();
/* 232 */         SimplePlainQueue<U> simplePlainQueue = this.queue;
/* 233 */         if (r != 0L && (simplePlainQueue == null || simplePlainQueue.isEmpty())) {
/* 234 */           this.downstream.onNext(value);
/* 235 */           if (r != Long.MAX_VALUE) {
/* 236 */             this.requested.decrementAndGet();
/*     */           }
/* 238 */           if (this.maxConcurrency != Integer.MAX_VALUE && !this.cancelled && ++this.scalarEmitted == this.scalarLimit) {
/*     */             
/* 240 */             this.scalarEmitted = 0;
/* 241 */             this.upstream.request(this.scalarLimit);
/*     */           } 
/*     */         } else {
/* 244 */           SimpleQueue<U> simpleQueue; if (simplePlainQueue == null) {
/* 245 */             simpleQueue = getMainQueue();
/*     */           }
/* 247 */           if (!simpleQueue.offer(value)) {
/* 248 */             onError(new IllegalStateException("Scalar queue full?!"));
/*     */             return;
/*     */           } 
/*     */         } 
/* 252 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 256 */         SimpleQueue<U> q = getMainQueue();
/* 257 */         if (!q.offer(value)) {
/* 258 */           onError(new IllegalStateException("Scalar queue full?!"));
/*     */           return;
/*     */         } 
/* 261 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 265 */       drainLoop();
/*     */     }
/*     */     SimpleQueue<U> getInnerQueue(FlowableFlatMap.InnerSubscriber<T, U> inner) {
/*     */       SpscArrayQueue spscArrayQueue;
/* 269 */       SimpleQueue<U> q = inner.queue;
/* 270 */       if (q == null) {
/* 271 */         spscArrayQueue = new SpscArrayQueue(this.bufferSize);
/* 272 */         inner.queue = (SimpleQueue<U>)spscArrayQueue;
/*     */       } 
/* 274 */       return (SimpleQueue<U>)spscArrayQueue;
/*     */     }
/*     */     
/*     */     void tryEmit(U value, FlowableFlatMap.InnerSubscriber<T, U> inner) {
/* 278 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 279 */         long r = this.requested.get();
/* 280 */         SimpleQueue<U> q = inner.queue;
/* 281 */         if (r != 0L && (q == null || q.isEmpty())) {
/* 282 */           this.downstream.onNext(value);
/* 283 */           if (r != Long.MAX_VALUE) {
/* 284 */             this.requested.decrementAndGet();
/*     */           }
/* 286 */           inner.requestMore(1L);
/*     */         } else {
/* 288 */           if (q == null) {
/* 289 */             q = getInnerQueue(inner);
/*     */           }
/* 291 */           if (!q.offer(value)) {
/* 292 */             onError((Throwable)new MissingBackpressureException("Inner queue full?!"));
/*     */             return;
/*     */           } 
/*     */         } 
/* 296 */         if (decrementAndGet() == 0)
/*     */           return; 
/*     */       } else {
/*     */         SpscArrayQueue spscArrayQueue;
/* 300 */         SimpleQueue<U> q = inner.queue;
/* 301 */         if (q == null) {
/* 302 */           spscArrayQueue = new SpscArrayQueue(this.bufferSize);
/* 303 */           inner.queue = (SimpleQueue<U>)spscArrayQueue;
/*     */         } 
/* 305 */         if (!spscArrayQueue.offer(value)) {
/* 306 */           onError((Throwable)new MissingBackpressureException("Inner queue full?!"));
/*     */           return;
/*     */         } 
/* 309 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 313 */       drainLoop();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 319 */       if (this.done) {
/* 320 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 323 */       if (this.errs.addThrowable(t)) {
/* 324 */         this.done = true;
/* 325 */         if (!this.delayErrors) {
/* 326 */           for (FlowableFlatMap.InnerSubscriber<?, ?> a : (FlowableFlatMap.InnerSubscriber[])this.subscribers.getAndSet(CANCELLED)) {
/* 327 */             a.dispose();
/*     */           }
/*     */         }
/* 330 */         drain();
/*     */       } else {
/* 332 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 339 */       if (this.done) {
/*     */         return;
/*     */       }
/* 342 */       this.done = true;
/* 343 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 348 */       if (SubscriptionHelper.validate(n)) {
/* 349 */         BackpressureHelper.add(this.requested, n);
/* 350 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 356 */       if (!this.cancelled) {
/* 357 */         this.cancelled = true;
/* 358 */         this.upstream.cancel();
/* 359 */         disposeAll();
/* 360 */         if (getAndIncrement() == 0) {
/* 361 */           SimplePlainQueue<U> simplePlainQueue = this.queue;
/* 362 */           if (simplePlainQueue != null) {
/* 363 */             simplePlainQueue.clear();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void drain() {
/* 370 */       if (getAndIncrement() == 0) {
/* 371 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 376 */       Subscriber<? super U> child = this.downstream;
/* 377 */       int missed = 1;
/*     */       while (true) {
/* 379 */         if (checkTerminate()) {
/*     */           return;
/*     */         }
/* 382 */         SimplePlainQueue<U> svq = this.queue;
/*     */         
/* 384 */         long r = this.requested.get();
/* 385 */         boolean unbounded = (r == Long.MAX_VALUE);
/*     */         
/* 387 */         long replenishMain = 0L;
/*     */         
/* 389 */         if (svq != null) {
/*     */           U o; do {
/* 391 */             long scalarEmission = 0L;
/* 392 */             o = null;
/* 393 */             while (r != 0L) {
/* 394 */               o = (U)svq.poll();
/*     */               
/* 396 */               if (checkTerminate()) {
/*     */                 return;
/*     */               }
/* 399 */               if (o == null) {
/*     */                 break;
/*     */               }
/*     */               
/* 403 */               child.onNext(o);
/*     */               
/* 405 */               replenishMain++;
/* 406 */               scalarEmission++;
/* 407 */               r--;
/*     */             } 
/* 409 */             if (scalarEmission == 0L)
/* 410 */               continue;  if (unbounded) {
/* 411 */               r = Long.MAX_VALUE;
/*     */             } else {
/* 413 */               r = this.requested.addAndGet(-scalarEmission);
/*     */             }
/*     */           
/* 416 */           } while (r != 0L && o != null);
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 422 */         boolean d = this.done;
/* 423 */         svq = this.queue;
/* 424 */         FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber = (FlowableFlatMap.InnerSubscriber[])this.subscribers.get();
/* 425 */         int n = arrayOfInnerSubscriber.length;
/*     */         
/* 427 */         if (d && (svq == null || svq.isEmpty()) && n == 0) {
/* 428 */           Throwable ex = this.errs.terminate();
/* 429 */           if (ex != ExceptionHelper.TERMINATED) {
/* 430 */             if (ex == null) {
/* 431 */               child.onComplete();
/*     */             } else {
/* 433 */               child.onError(ex);
/*     */             } 
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/* 439 */         boolean innerCompleted = false;
/* 440 */         if (n != 0) {
/* 441 */           long startId = this.lastId;
/* 442 */           int index = this.lastIndex;
/*     */           
/* 444 */           if (n <= index || (arrayOfInnerSubscriber[index]).id != startId) {
/* 445 */             if (n <= index) {
/* 446 */               index = 0;
/*     */             }
/* 448 */             int k = index;
/* 449 */             for (int m = 0; m < n && 
/* 450 */               (arrayOfInnerSubscriber[k]).id != startId; m++) {
/*     */ 
/*     */               
/* 453 */               k++;
/* 454 */               if (k == n) {
/* 455 */                 k = 0;
/*     */               }
/*     */             } 
/* 458 */             index = k;
/* 459 */             this.lastIndex = k;
/* 460 */             this.lastId = (arrayOfInnerSubscriber[k]).id;
/*     */           } 
/*     */           
/* 463 */           int j = index;
/*     */           
/* 465 */           for (int i = 0;; i++);
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
/* 546 */           this.lastIndex = j;
/* 547 */           this.lastId = (arrayOfInnerSubscriber[j]).id;
/*     */         } 
/*     */         
/* 550 */         if (replenishMain != 0L && !this.cancelled) {
/* 551 */           this.upstream.request(replenishMain);
/*     */         }
/* 553 */         if (innerCompleted) {
/*     */           continue;
/*     */         }
/* 556 */         missed = addAndGet(-missed);
/* 557 */         if (missed == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     boolean checkTerminate() {
/* 564 */       if (this.cancelled) {
/* 565 */         clearScalarQueue();
/* 566 */         return true;
/*     */       } 
/* 568 */       if (!this.delayErrors && this.errs.get() != null) {
/* 569 */         clearScalarQueue();
/* 570 */         Throwable ex = this.errs.terminate();
/* 571 */         if (ex != ExceptionHelper.TERMINATED) {
/* 572 */           this.downstream.onError(ex);
/*     */         }
/* 574 */         return true;
/*     */       } 
/* 576 */       return false;
/*     */     }
/*     */     
/*     */     void clearScalarQueue() {
/* 580 */       SimplePlainQueue<U> simplePlainQueue = this.queue;
/* 581 */       if (simplePlainQueue != null) {
/* 582 */         simplePlainQueue.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     void disposeAll() {
/* 587 */       FlowableFlatMap.InnerSubscriber[] arrayOfInnerSubscriber = (FlowableFlatMap.InnerSubscriber[])this.subscribers.get();
/* 588 */       if (arrayOfInnerSubscriber != CANCELLED) {
/* 589 */         arrayOfInnerSubscriber = (FlowableFlatMap.InnerSubscriber[])this.subscribers.getAndSet(CANCELLED);
/* 590 */         if (arrayOfInnerSubscriber != CANCELLED) {
/* 591 */           for (FlowableFlatMap.InnerSubscriber<?, ?> inner : arrayOfInnerSubscriber) {
/* 592 */             inner.dispose();
/*     */           }
/* 594 */           Throwable ex = this.errs.terminate();
/* 595 */           if (ex != null && ex != ExceptionHelper.TERMINATED) {
/* 596 */             RxJavaPlugins.onError(ex);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void innerError(FlowableFlatMap.InnerSubscriber<T, U> inner, Throwable t) {
/* 603 */       if (this.errs.addThrowable(t)) {
/* 604 */         inner.done = true;
/* 605 */         if (!this.delayErrors) {
/* 606 */           this.upstream.cancel();
/* 607 */           for (FlowableFlatMap.InnerSubscriber<?, ?> a : (FlowableFlatMap.InnerSubscriber[])this.subscribers.getAndSet(CANCELLED)) {
/* 608 */             a.dispose();
/*     */           }
/*     */         } 
/* 611 */         drain();
/*     */       } else {
/* 613 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static final class InnerSubscriber<T, U>
/*     */     extends AtomicReference<Subscription>
/*     */     implements FlowableSubscriber<U>, Disposable
/*     */   {
/*     */     private static final long serialVersionUID = -4606175640614850599L;
/*     */     final long id;
/*     */     final FlowableFlatMap.MergeSubscriber<T, U> parent;
/*     */     final int limit;
/*     */     final int bufferSize;
/*     */     volatile boolean done;
/*     */     volatile SimpleQueue<U> queue;
/*     */     long produced;
/*     */     int fusionMode;
/*     */     
/*     */     InnerSubscriber(FlowableFlatMap.MergeSubscriber<T, U> parent, long id) {
/* 633 */       this.id = id;
/* 634 */       this.parent = parent;
/* 635 */       this.bufferSize = parent.bufferSize;
/* 636 */       this.limit = this.bufferSize >> 2;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 641 */       if (SubscriptionHelper.setOnce(this, s)) {
/*     */         
/* 643 */         if (s instanceof QueueSubscription) {
/*     */           
/* 645 */           QueueSubscription<U> qs = (QueueSubscription<U>)s;
/* 646 */           int m = qs.requestFusion(7);
/* 647 */           if (m == 1) {
/* 648 */             this.fusionMode = m;
/* 649 */             this.queue = (SimpleQueue<U>)qs;
/* 650 */             this.done = true;
/* 651 */             this.parent.drain();
/*     */             return;
/*     */           } 
/* 654 */           if (m == 2) {
/* 655 */             this.fusionMode = m;
/* 656 */             this.queue = (SimpleQueue<U>)qs;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 661 */         s.request(this.bufferSize);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(U t) {
/* 667 */       if (this.fusionMode != 2) {
/* 668 */         this.parent.tryEmit(t, this);
/*     */       } else {
/* 670 */         this.parent.drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 676 */       lazySet((Subscription)SubscriptionHelper.CANCELLED);
/* 677 */       this.parent.innerError(this, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 682 */       this.done = true;
/* 683 */       this.parent.drain();
/*     */     }
/*     */     
/*     */     void requestMore(long n) {
/* 687 */       if (this.fusionMode != 1) {
/* 688 */         long p = this.produced + n;
/* 689 */         if (p >= this.limit) {
/* 690 */           this.produced = 0L;
/* 691 */           get().request(p);
/*     */         } else {
/* 693 */           this.produced = p;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 700 */       SubscriptionHelper.cancel(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 705 */       return (get() == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableFlatMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */