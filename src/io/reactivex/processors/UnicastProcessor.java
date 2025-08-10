/*     */ package io.reactivex.processors;
/*     */ 
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class UnicastProcessor<T>
/*     */   extends FlowableProcessor<T>
/*     */ {
/*     */   final SpscLinkedArrayQueue<T> queue;
/*     */   final AtomicReference<Runnable> onTerminate;
/*     */   final boolean delayError;
/*     */   volatile boolean done;
/*     */   Throwable error;
/*     */   final AtomicReference<Subscriber<? super T>> downstream;
/*     */   volatile boolean cancelled;
/*     */   final AtomicBoolean once;
/*     */   final BasicIntQueueSubscription<T> wip;
/*     */   final AtomicLong requested;
/*     */   boolean enableOperatorFusion;
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> UnicastProcessor<T> create() {
/* 183 */     return new UnicastProcessor<T>(bufferSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> UnicastProcessor<T> create(int capacityHint) {
/* 195 */     return new UnicastProcessor<T>(capacityHint);
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> UnicastProcessor<T> create(boolean delayError) {
/* 209 */     return new UnicastProcessor<T>(bufferSize(), null, delayError);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> UnicastProcessor<T> create(int capacityHint, Runnable onCancelled) {
/* 227 */     ObjectHelper.requireNonNull(onCancelled, "onTerminate");
/* 228 */     return new UnicastProcessor<T>(capacityHint, onCancelled);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> UnicastProcessor<T> create(int capacityHint, Runnable onCancelled, boolean delayError) {
/* 248 */     ObjectHelper.requireNonNull(onCancelled, "onTerminate");
/* 249 */     return new UnicastProcessor<T>(capacityHint, onCancelled, delayError);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnicastProcessor(int capacityHint) {
/* 258 */     this(capacityHint, null, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnicastProcessor(int capacityHint, Runnable onTerminate) {
/* 269 */     this(capacityHint, onTerminate, true);
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
/*     */   
/*     */   UnicastProcessor(int capacityHint, Runnable onTerminate, boolean delayError) {
/* 282 */     this.queue = new SpscLinkedArrayQueue(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
/* 283 */     this.onTerminate = new AtomicReference<Runnable>(onTerminate);
/* 284 */     this.delayError = delayError;
/* 285 */     this.downstream = new AtomicReference<Subscriber<? super T>>();
/* 286 */     this.once = new AtomicBoolean();
/* 287 */     this.wip = new UnicastQueueSubscription();
/* 288 */     this.requested = new AtomicLong();
/*     */   }
/*     */   
/*     */   void doTerminate() {
/* 292 */     Runnable r = this.onTerminate.getAndSet(null);
/* 293 */     if (r != null) {
/* 294 */       r.run();
/*     */     }
/*     */   }
/*     */   
/*     */   void drainRegular(Subscriber<? super T> a) {
/* 299 */     int missed = 1;
/*     */     
/* 301 */     SpscLinkedArrayQueue<T> q = this.queue;
/* 302 */     boolean failFast = !this.delayError;
/*     */     
/*     */     do {
/* 305 */       long r = this.requested.get();
/* 306 */       long e = 0L;
/*     */       
/* 308 */       while (r != e) {
/* 309 */         boolean d = this.done;
/*     */         
/* 311 */         T t = (T)q.poll();
/* 312 */         boolean empty = (t == null);
/*     */         
/* 314 */         if (checkTerminated(failFast, d, empty, a, q)) {
/*     */           return;
/*     */         }
/*     */         
/* 318 */         if (empty) {
/*     */           break;
/*     */         }
/*     */         
/* 322 */         a.onNext(t);
/*     */         
/* 324 */         e++;
/*     */       } 
/*     */       
/* 327 */       if (r == e && checkTerminated(failFast, this.done, q.isEmpty(), a, q)) {
/*     */         return;
/*     */       }
/*     */       
/* 331 */       if (e != 0L && r != Long.MAX_VALUE) {
/* 332 */         this.requested.addAndGet(-e);
/*     */       }
/*     */       
/* 335 */       missed = this.wip.addAndGet(-missed);
/* 336 */     } while (missed != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void drainFused(Subscriber<? super T> a) {
/* 343 */     int missed = 1;
/*     */     
/* 345 */     SpscLinkedArrayQueue<T> q = this.queue;
/* 346 */     boolean failFast = !this.delayError;
/*     */     
/*     */     do {
/* 349 */       if (this.cancelled) {
/* 350 */         this.downstream.lazySet(null);
/*     */         
/*     */         return;
/*     */       } 
/* 354 */       boolean d = this.done;
/*     */       
/* 356 */       if (failFast && d && this.error != null) {
/* 357 */         q.clear();
/* 358 */         this.downstream.lazySet(null);
/* 359 */         a.onError(this.error);
/*     */         return;
/*     */       } 
/* 362 */       a.onNext(null);
/*     */       
/* 364 */       if (d) {
/* 365 */         this.downstream.lazySet(null);
/*     */         
/* 367 */         Throwable ex = this.error;
/* 368 */         if (ex != null) {
/* 369 */           a.onError(ex);
/*     */         } else {
/* 371 */           a.onComplete();
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 376 */       missed = this.wip.addAndGet(-missed);
/* 377 */     } while (missed != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void drain() {
/* 384 */     if (this.wip.getAndIncrement() != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 388 */     int missed = 1;
/*     */     
/* 390 */     Subscriber<? super T> a = this.downstream.get();
/*     */     while (true) {
/* 392 */       if (a != null) {
/*     */         
/* 394 */         if (this.enableOperatorFusion) {
/* 395 */           drainFused(a);
/*     */         } else {
/* 397 */           drainRegular(a);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 402 */       missed = this.wip.addAndGet(-missed);
/* 403 */       if (missed == 0) {
/*     */         break;
/*     */       }
/* 406 */       a = this.downstream.get();
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean checkTerminated(boolean failFast, boolean d, boolean empty, Subscriber<? super T> a, SpscLinkedArrayQueue<T> q) {
/* 411 */     if (this.cancelled) {
/* 412 */       q.clear();
/* 413 */       this.downstream.lazySet(null);
/* 414 */       return true;
/*     */     } 
/*     */     
/* 417 */     if (d) {
/* 418 */       if (failFast && this.error != null) {
/* 419 */         q.clear();
/* 420 */         this.downstream.lazySet(null);
/* 421 */         a.onError(this.error);
/* 422 */         return true;
/*     */       } 
/* 424 */       if (empty) {
/* 425 */         Throwable e = this.error;
/* 426 */         this.downstream.lazySet(null);
/* 427 */         if (e != null) {
/* 428 */           a.onError(e);
/*     */         } else {
/* 430 */           a.onComplete();
/*     */         } 
/* 432 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 436 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Subscription s) {
/* 441 */     if (this.done || this.cancelled) {
/* 442 */       s.cancel();
/*     */     } else {
/* 444 */       s.request(Long.MAX_VALUE);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 450 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */     
/* 452 */     if (this.done || this.cancelled) {
/*     */       return;
/*     */     }
/*     */     
/* 456 */     this.queue.offer(t);
/* 457 */     drain();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 462 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */     
/* 464 */     if (this.done || this.cancelled) {
/* 465 */       RxJavaPlugins.onError(t);
/*     */       
/*     */       return;
/*     */     } 
/* 469 */     this.error = t;
/* 470 */     this.done = true;
/*     */     
/* 472 */     doTerminate();
/*     */     
/* 474 */     drain();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 479 */     if (this.done || this.cancelled) {
/*     */       return;
/*     */     }
/*     */     
/* 483 */     this.done = true;
/*     */     
/* 485 */     doTerminate();
/*     */     
/* 487 */     drain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/* 492 */     if (!this.once.get() && this.once.compareAndSet(false, true)) {
/*     */       
/* 494 */       s.onSubscribe((Subscription)this.wip);
/* 495 */       this.downstream.set(s);
/* 496 */       if (this.cancelled) {
/* 497 */         this.downstream.lazySet(null);
/*     */       } else {
/* 499 */         drain();
/*     */       } 
/*     */     } else {
/* 502 */       EmptySubscription.error(new IllegalStateException("This processor allows only a single Subscriber"), s);
/*     */     } 
/*     */   }
/*     */   
/*     */   final class UnicastQueueSubscription
/*     */     extends BasicIntQueueSubscription<T>
/*     */   {
/*     */     private static final long serialVersionUID = -4896760517184205454L;
/*     */     
/*     */     @Nullable
/*     */     public T poll() {
/* 513 */       return (T)UnicastProcessor.this.queue.poll();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 518 */       return UnicastProcessor.this.queue.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 523 */       UnicastProcessor.this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public int requestFusion(int requestedMode) {
/* 528 */       if ((requestedMode & 0x2) != 0) {
/* 529 */         UnicastProcessor.this.enableOperatorFusion = true;
/* 530 */         return 2;
/*     */       } 
/* 532 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 537 */       if (SubscriptionHelper.validate(n)) {
/* 538 */         BackpressureHelper.add(UnicastProcessor.this.requested, n);
/* 539 */         UnicastProcessor.this.drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 545 */       if (UnicastProcessor.this.cancelled) {
/*     */         return;
/*     */       }
/* 548 */       UnicastProcessor.this.cancelled = true;
/*     */       
/* 550 */       UnicastProcessor.this.doTerminate();
/*     */       
/* 552 */       UnicastProcessor.this.downstream.lazySet(null);
/* 553 */       if (UnicastProcessor.this.wip.getAndIncrement() == 0) {
/* 554 */         UnicastProcessor.this.downstream.lazySet(null);
/* 555 */         if (!UnicastProcessor.this.enableOperatorFusion) {
/* 556 */           UnicastProcessor.this.queue.clear();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasSubscribers() {
/* 564 */     return (this.downstream.get() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 570 */     if (this.done) {
/* 571 */       return this.error;
/*     */     }
/* 573 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 578 */     return (this.done && this.error == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 583 */     return (this.done && this.error != null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/processors/UnicastProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */