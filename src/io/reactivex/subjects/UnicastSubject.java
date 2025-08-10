/*     */ package io.reactivex.subjects;
/*     */ 
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.annotations.CheckReturnValue;
/*     */ import io.reactivex.annotations.NonNull;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.observers.BasicIntQueueDisposable;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnicastSubject<T>
/*     */   extends Subject<T>
/*     */ {
/*     */   final SpscLinkedArrayQueue<T> queue;
/*     */   final AtomicReference<Observer<? super T>> downstream;
/*     */   final AtomicReference<Runnable> onTerminate;
/*     */   final boolean delayError;
/*     */   volatile boolean disposed;
/*     */   volatile boolean done;
/*     */   Throwable error;
/*     */   final AtomicBoolean once;
/*     */   final BasicIntQueueDisposable<T> wip;
/*     */   boolean enableOperatorFusion;
/*     */   
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> UnicastSubject<T> create() {
/* 184 */     return new UnicastSubject<T>(bufferSize(), true);
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
/*     */   public static <T> UnicastSubject<T> create(int capacityHint) {
/* 196 */     return new UnicastSubject<T>(capacityHint, true);
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
/*     */   public static <T> UnicastSubject<T> create(int capacityHint, Runnable onTerminate) {
/* 214 */     return new UnicastSubject<T>(capacityHint, onTerminate, true);
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
/*     */   public static <T> UnicastSubject<T> create(int capacityHint, Runnable onTerminate, boolean delayError) {
/* 234 */     return new UnicastSubject<T>(capacityHint, onTerminate, delayError);
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
/*     */   @CheckReturnValue
/*     */   @NonNull
/*     */   public static <T> UnicastSubject<T> create(boolean delayError) {
/* 251 */     return new UnicastSubject<T>(bufferSize(), delayError);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   UnicastSubject(int capacityHint, boolean delayError) {
/* 262 */     this.queue = new SpscLinkedArrayQueue(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
/* 263 */     this.onTerminate = new AtomicReference<Runnable>();
/* 264 */     this.delayError = delayError;
/* 265 */     this.downstream = new AtomicReference<Observer<? super T>>();
/* 266 */     this.once = new AtomicBoolean();
/* 267 */     this.wip = new UnicastQueueDisposable();
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
/*     */   UnicastSubject(int capacityHint, Runnable onTerminate) {
/* 279 */     this(capacityHint, onTerminate, true);
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
/*     */   UnicastSubject(int capacityHint, Runnable onTerminate, boolean delayError) {
/* 292 */     this.queue = new SpscLinkedArrayQueue(ObjectHelper.verifyPositive(capacityHint, "capacityHint"));
/* 293 */     this.onTerminate = new AtomicReference(ObjectHelper.requireNonNull(onTerminate, "onTerminate"));
/* 294 */     this.delayError = delayError;
/* 295 */     this.downstream = new AtomicReference<Observer<? super T>>();
/* 296 */     this.once = new AtomicBoolean();
/* 297 */     this.wip = new UnicastQueueDisposable();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super T> observer) {
/* 302 */     if (!this.once.get() && this.once.compareAndSet(false, true)) {
/* 303 */       observer.onSubscribe((Disposable)this.wip);
/* 304 */       this.downstream.lazySet(observer);
/* 305 */       if (this.disposed) {
/* 306 */         this.downstream.lazySet(null);
/*     */         return;
/*     */       } 
/* 309 */       drain();
/*     */     } else {
/* 311 */       EmptyDisposable.error(new IllegalStateException("Only a single observer allowed."), observer);
/*     */     } 
/*     */   }
/*     */   
/*     */   void doTerminate() {
/* 316 */     Runnable r = this.onTerminate.get();
/* 317 */     if (r != null && this.onTerminate.compareAndSet(r, null)) {
/* 318 */       r.run();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSubscribe(Disposable d) {
/* 324 */     if (this.done || this.disposed) {
/* 325 */       d.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNext(T t) {
/* 331 */     ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 332 */     if (this.done || this.disposed) {
/*     */       return;
/*     */     }
/* 335 */     this.queue.offer(t);
/* 336 */     drain();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onError(Throwable t) {
/* 341 */     ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/* 342 */     if (this.done || this.disposed) {
/* 343 */       RxJavaPlugins.onError(t);
/*     */       return;
/*     */     } 
/* 346 */     this.error = t;
/* 347 */     this.done = true;
/*     */     
/* 349 */     doTerminate();
/*     */     
/* 351 */     drain();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onComplete() {
/* 356 */     if (this.done || this.disposed) {
/*     */       return;
/*     */     }
/* 359 */     this.done = true;
/*     */     
/* 361 */     doTerminate();
/*     */     
/* 363 */     drain();
/*     */   }
/*     */   
/*     */   void drainNormal(Observer<? super T> a) {
/* 367 */     int missed = 1;
/* 368 */     SpscLinkedArrayQueue<T> spscLinkedArrayQueue = this.queue;
/* 369 */     boolean failFast = !this.delayError;
/* 370 */     boolean canBeError = true;
/*     */ 
/*     */     
/*     */     while (true) {
/* 374 */       if (this.disposed) {
/* 375 */         this.downstream.lazySet(null);
/* 376 */         spscLinkedArrayQueue.clear();
/*     */         
/*     */         return;
/*     */       } 
/* 380 */       boolean d = this.done;
/* 381 */       T v = (T)this.queue.poll();
/* 382 */       boolean empty = (v == null);
/*     */       
/* 384 */       if (d) {
/* 385 */         if (failFast && canBeError) {
/* 386 */           if (failedFast((SimpleQueue<T>)spscLinkedArrayQueue, a)) {
/*     */             return;
/*     */           }
/* 389 */           canBeError = false;
/*     */         } 
/*     */ 
/*     */         
/* 393 */         if (empty) {
/* 394 */           errorOrComplete(a);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/* 399 */       if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 406 */         missed = this.wip.addAndGet(-missed);
/* 407 */         if (missed == 0)
/*     */           break; 
/*     */         continue;
/*     */       } 
/*     */       a.onNext(v);
/*     */     } 
/*     */   } void drainFused(Observer<? super T> a) {
/* 414 */     int missed = 1;
/*     */     
/* 416 */     SpscLinkedArrayQueue<T> q = this.queue;
/* 417 */     boolean failFast = !this.delayError;
/*     */ 
/*     */     
/*     */     do {
/* 421 */       if (this.disposed) {
/* 422 */         this.downstream.lazySet(null);
/*     */         return;
/*     */       } 
/* 425 */       boolean d = this.done;
/*     */       
/* 427 */       if (failFast && d && 
/* 428 */         failedFast((SimpleQueue<T>)q, a)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 433 */       a.onNext(null);
/*     */       
/* 435 */       if (d) {
/* 436 */         errorOrComplete(a);
/*     */         
/*     */         return;
/*     */       } 
/* 440 */       missed = this.wip.addAndGet(-missed);
/* 441 */     } while (missed != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void errorOrComplete(Observer<? super T> a) {
/* 448 */     this.downstream.lazySet(null);
/* 449 */     Throwable ex = this.error;
/* 450 */     if (ex != null) {
/* 451 */       a.onError(ex);
/*     */     } else {
/* 453 */       a.onComplete();
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean failedFast(SimpleQueue<T> q, Observer<? super T> a) {
/* 458 */     Throwable ex = this.error;
/* 459 */     if (ex != null) {
/* 460 */       this.downstream.lazySet(null);
/* 461 */       q.clear();
/* 462 */       a.onError(ex);
/* 463 */       return true;
/*     */     } 
/* 465 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   void drain() {
/* 470 */     if (this.wip.getAndIncrement() != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 474 */     Observer<? super T> a = this.downstream.get();
/* 475 */     int missed = 1;
/*     */ 
/*     */     
/*     */     while (true) {
/* 479 */       if (a != null) {
/* 480 */         if (this.enableOperatorFusion) {
/* 481 */           drainFused(a);
/*     */         } else {
/* 483 */           drainNormal(a);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/* 488 */       missed = this.wip.addAndGet(-missed);
/* 489 */       if (missed == 0) {
/*     */         break;
/*     */       }
/*     */       
/* 493 */       a = this.downstream.get();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasObservers() {
/* 499 */     return (this.downstream.get() != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getThrowable() {
/* 505 */     if (this.done) {
/* 506 */       return this.error;
/*     */     }
/* 508 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasThrowable() {
/* 513 */     return (this.done && this.error != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasComplete() {
/* 518 */     return (this.done && this.error == null);
/*     */   }
/*     */   
/*     */   final class UnicastQueueDisposable
/*     */     extends BasicIntQueueDisposable<T>
/*     */   {
/*     */     private static final long serialVersionUID = 7926949470189395511L;
/*     */     
/*     */     public int requestFusion(int mode) {
/* 527 */       if ((mode & 0x2) != 0) {
/* 528 */         UnicastSubject.this.enableOperatorFusion = true;
/* 529 */         return 2;
/*     */       } 
/* 531 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 537 */       return (T)UnicastSubject.this.queue.poll();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 542 */       return UnicastSubject.this.queue.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 547 */       UnicastSubject.this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 552 */       if (!UnicastSubject.this.disposed) {
/* 553 */         UnicastSubject.this.disposed = true;
/*     */         
/* 555 */         UnicastSubject.this.doTerminate();
/*     */         
/* 557 */         UnicastSubject.this.downstream.lazySet(null);
/* 558 */         if (UnicastSubject.this.wip.getAndIncrement() == 0) {
/* 559 */           UnicastSubject.this.downstream.lazySet(null);
/* 560 */           if (!UnicastSubject.this.enableOperatorFusion) {
/* 561 */             UnicastSubject.this.queue.clear();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 569 */       return UnicastSubject.this.disposed;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/subjects/UnicastSubject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */