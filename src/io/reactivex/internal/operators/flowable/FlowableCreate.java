/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.BackpressureStrategy;
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableEmitter;
/*     */ import io.reactivex.FlowableOnSubscribe;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.functions.Cancellable;
/*     */ import io.reactivex.internal.disposables.CancellableDisposable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.AtomicThrowable;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
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
/*     */ public final class FlowableCreate<T>
/*     */   extends Flowable<T>
/*     */ {
/*     */   final FlowableOnSubscribe<T> source;
/*     */   final BackpressureStrategy backpressure;
/*     */   
/*     */   public FlowableCreate(FlowableOnSubscribe<T> source, BackpressureStrategy backpressure) {
/*  38 */     this.source = source;
/*  39 */     this.backpressure = backpressure;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> t) {
/*     */     BaseEmitter<T> emitter;
/*  46 */     switch (this.backpressure) {
/*     */       case MISSING:
/*  48 */         emitter = new MissingEmitter<T>(t);
/*     */         break;
/*     */       
/*     */       case ERROR:
/*  52 */         emitter = new ErrorAsyncEmitter<T>(t);
/*     */         break;
/*     */       
/*     */       case DROP:
/*  56 */         emitter = new DropAsyncEmitter<T>(t);
/*     */         break;
/*     */       
/*     */       case LATEST:
/*  60 */         emitter = new LatestAsyncEmitter<T>(t);
/*     */         break;
/*     */       
/*     */       default:
/*  64 */         emitter = new BufferAsyncEmitter<T>(t, bufferSize());
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/*  69 */     t.onSubscribe(emitter);
/*     */     try {
/*  71 */       this.source.subscribe(emitter);
/*  72 */     } catch (Throwable ex) {
/*  73 */       Exceptions.throwIfFatal(ex);
/*  74 */       emitter.onError(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SerializedEmitter<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableEmitter<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4883307006032401862L;
/*     */ 
/*     */     
/*     */     final FlowableCreate.BaseEmitter<T> emitter;
/*     */ 
/*     */     
/*     */     final AtomicThrowable error;
/*     */     
/*     */     final SimplePlainQueue<T> queue;
/*     */     
/*     */     volatile boolean done;
/*     */ 
/*     */     
/*     */     SerializedEmitter(FlowableCreate.BaseEmitter<T> emitter) {
/*  98 */       this.emitter = emitter;
/*  99 */       this.error = new AtomicThrowable();
/* 100 */       this.queue = (SimplePlainQueue<T>)new SpscLinkedArrayQueue(16);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 105 */       if (this.emitter.isCancelled() || this.done) {
/*     */         return;
/*     */       }
/* 108 */       if (t == null) {
/* 109 */         onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         return;
/*     */       } 
/* 112 */       if (get() == 0 && compareAndSet(0, 1)) {
/* 113 */         this.emitter.onNext(t);
/* 114 */         if (decrementAndGet() == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 118 */         SimplePlainQueue<T> q = this.queue;
/* 119 */         synchronized (q) {
/* 120 */           q.offer(t);
/*     */         } 
/* 122 */         if (getAndIncrement() != 0) {
/*     */           return;
/*     */         }
/*     */       } 
/* 126 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 131 */       if (!tryOnError(t)) {
/* 132 */         RxJavaPlugins.onError(t);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable t) {
/* 138 */       if (this.emitter.isCancelled() || this.done) {
/* 139 */         return false;
/*     */       }
/* 141 */       if (t == null) {
/* 142 */         t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       }
/* 144 */       if (this.error.addThrowable(t)) {
/* 145 */         this.done = true;
/* 146 */         drain();
/* 147 */         return true;
/*     */       } 
/* 149 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 154 */       if (this.emitter.isCancelled() || this.done) {
/*     */         return;
/*     */       }
/* 157 */       this.done = true;
/* 158 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 162 */       if (getAndIncrement() == 0) {
/* 163 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 168 */       FlowableCreate.BaseEmitter<T> e = this.emitter;
/* 169 */       SimplePlainQueue<T> q = this.queue;
/* 170 */       AtomicThrowable error = this.error;
/* 171 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 175 */         if (e.isCancelled()) {
/* 176 */           q.clear();
/*     */           
/*     */           return;
/*     */         } 
/* 180 */         if (error.get() != null) {
/* 181 */           q.clear();
/* 182 */           e.onError(error.terminate());
/*     */           
/*     */           return;
/*     */         } 
/* 186 */         boolean d = this.done;
/*     */         
/* 188 */         T v = (T)q.poll();
/*     */         
/* 190 */         boolean empty = (v == null);
/*     */         
/* 192 */         if (d && empty) {
/* 193 */           e.onComplete();
/*     */           
/*     */           return;
/*     */         } 
/* 197 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 204 */           missed = addAndGet(-missed);
/* 205 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         e.onNext(v);
/*     */       } 
/*     */     }
/*     */     public void setDisposable(Disposable d) {
/* 213 */       this.emitter.setDisposable(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setCancellable(Cancellable c) {
/* 218 */       this.emitter.setCancellable(c);
/*     */     }
/*     */ 
/*     */     
/*     */     public long requested() {
/* 223 */       return this.emitter.requested();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCancelled() {
/* 228 */       return this.emitter.isCancelled();
/*     */     }
/*     */ 
/*     */     
/*     */     public FlowableEmitter<T> serialize() {
/* 233 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 238 */       return this.emitter.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseEmitter<T>
/*     */     extends AtomicLong
/*     */     implements FlowableEmitter<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 7326289992464377023L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final SequentialDisposable serial;
/*     */     
/*     */     BaseEmitter(Subscriber<? super T> downstream) {
/* 252 */       this.downstream = downstream;
/* 253 */       this.serial = new SequentialDisposable();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 258 */       complete();
/*     */     }
/*     */     
/*     */     protected void complete() {
/* 262 */       if (isCancelled()) {
/*     */         return;
/*     */       }
/*     */       try {
/* 266 */         this.downstream.onComplete();
/*     */       } finally {
/* 268 */         this.serial.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onError(Throwable e) {
/* 274 */       if (!tryOnError(e)) {
/* 275 */         RxJavaPlugins.onError(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable e) {
/* 281 */       return error(e);
/*     */     }
/*     */     
/*     */     protected boolean error(Throwable e) {
/* 285 */       if (e == null) {
/* 286 */         e = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       }
/* 288 */       if (isCancelled()) {
/* 289 */         return false;
/*     */       }
/*     */       try {
/* 292 */         this.downstream.onError(e);
/*     */       } finally {
/* 294 */         this.serial.dispose();
/*     */       } 
/* 296 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void cancel() {
/* 301 */       this.serial.dispose();
/* 302 */       onUnsubscribed();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void onUnsubscribed() {}
/*     */ 
/*     */     
/*     */     public final boolean isCancelled() {
/* 311 */       return this.serial.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/* 316 */       if (SubscriptionHelper.validate(n)) {
/* 317 */         BackpressureHelper.add(this, n);
/* 318 */         onRequested();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void onRequested() {}
/*     */ 
/*     */     
/*     */     public final void setDisposable(Disposable d) {
/* 328 */       this.serial.update(d);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void setCancellable(Cancellable c) {
/* 333 */       setDisposable((Disposable)new CancellableDisposable(c));
/*     */     }
/*     */ 
/*     */     
/*     */     public final long requested() {
/* 338 */       return get();
/*     */     }
/*     */ 
/*     */     
/*     */     public final FlowableEmitter<T> serialize() {
/* 343 */       return new FlowableCreate.SerializedEmitter<T>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 348 */       return String.format("%s{%s}", new Object[] { getClass().getSimpleName(), super.toString() });
/*     */     }
/*     */   }
/*     */   
/*     */   static final class MissingEmitter<T>
/*     */     extends BaseEmitter<T> {
/*     */     private static final long serialVersionUID = 3776720187248809713L;
/*     */     
/*     */     MissingEmitter(Subscriber<? super T> downstream) {
/* 357 */       super(downstream);
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       long r;
/* 362 */       if (isCancelled()) {
/*     */         return;
/*     */       }
/*     */       
/* 366 */       if (t != null) {
/* 367 */         this.downstream.onNext(t);
/*     */       } else {
/* 369 */         onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         
/*     */         return;
/*     */       } 
/*     */       do {
/* 374 */         r = get();
/* 375 */       } while (r != 0L && !compareAndSet(r, r - 1L));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class NoOverflowBaseAsyncEmitter<T>
/*     */     extends BaseEmitter<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4127754106204442833L;
/*     */ 
/*     */     
/*     */     NoOverflowBaseAsyncEmitter(Subscriber<? super T> downstream) {
/* 388 */       super(downstream);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onNext(T t) {
/* 393 */       if (isCancelled()) {
/*     */         return;
/*     */       }
/*     */       
/* 397 */       if (t == null) {
/* 398 */         onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         
/*     */         return;
/*     */       } 
/* 402 */       if (get() != 0L) {
/* 403 */         this.downstream.onNext(t);
/* 404 */         BackpressureHelper.produced(this, 1L);
/*     */       } else {
/* 406 */         onOverflow();
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void onOverflow();
/*     */   }
/*     */   
/*     */   static final class DropAsyncEmitter<T>
/*     */     extends NoOverflowBaseAsyncEmitter<T> {
/*     */     private static final long serialVersionUID = 8360058422307496563L;
/*     */     
/*     */     DropAsyncEmitter(Subscriber<? super T> downstream) {
/* 418 */       super(downstream);
/*     */     }
/*     */ 
/*     */     
/*     */     void onOverflow() {}
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ErrorAsyncEmitter<T>
/*     */     extends NoOverflowBaseAsyncEmitter<T>
/*     */   {
/*     */     private static final long serialVersionUID = 338953216916120960L;
/*     */ 
/*     */     
/*     */     ErrorAsyncEmitter(Subscriber<? super T> downstream) {
/* 433 */       super(downstream);
/*     */     }
/*     */ 
/*     */     
/*     */     void onOverflow() {
/* 438 */       onError((Throwable)new MissingBackpressureException("create: could not emit value due to lack of requests"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferAsyncEmitter<T>
/*     */     extends BaseEmitter<T>
/*     */   {
/*     */     private static final long serialVersionUID = 2427151001689639875L;
/*     */     
/*     */     final SpscLinkedArrayQueue<T> queue;
/*     */     
/*     */     Throwable error;
/*     */     volatile boolean done;
/*     */     final AtomicInteger wip;
/*     */     
/*     */     BufferAsyncEmitter(Subscriber<? super T> actual, int capacityHint) {
/* 455 */       super(actual);
/* 456 */       this.queue = new SpscLinkedArrayQueue(capacityHint);
/* 457 */       this.wip = new AtomicInteger();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 462 */       if (this.done || isCancelled()) {
/*     */         return;
/*     */       }
/*     */       
/* 466 */       if (t == null) {
/* 467 */         onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         return;
/*     */       } 
/* 470 */       this.queue.offer(t);
/* 471 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable e) {
/* 476 */       if (this.done || isCancelled()) {
/* 477 */         return false;
/*     */       }
/*     */       
/* 480 */       if (e == null) {
/* 481 */         e = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
/*     */       }
/*     */       
/* 484 */       this.error = e;
/* 485 */       this.done = true;
/* 486 */       drain();
/* 487 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 492 */       this.done = true;
/* 493 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void onRequested() {
/* 498 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void onUnsubscribed() {
/* 503 */       if (this.wip.getAndIncrement() == 0) {
/* 504 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     void drain() {
/* 509 */       if (this.wip.getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 513 */       int missed = 1;
/* 514 */       Subscriber<? super T> a = this.downstream;
/* 515 */       SpscLinkedArrayQueue<T> q = this.queue;
/*     */       
/*     */       do {
/* 518 */         long r = get();
/* 519 */         long e = 0L;
/*     */         
/* 521 */         while (e != r) {
/* 522 */           if (isCancelled()) {
/* 523 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 527 */           boolean d = this.done;
/*     */           
/* 529 */           T o = (T)q.poll();
/*     */           
/* 531 */           boolean empty = (o == null);
/*     */           
/* 533 */           if (d && empty) {
/* 534 */             Throwable ex = this.error;
/* 535 */             if (ex != null) {
/* 536 */               error(ex);
/*     */             } else {
/* 538 */               complete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 543 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 547 */           a.onNext(o);
/*     */           
/* 549 */           e++;
/*     */         } 
/*     */         
/* 552 */         if (e == r) {
/* 553 */           if (isCancelled()) {
/* 554 */             q.clear();
/*     */             
/*     */             return;
/*     */           } 
/* 558 */           boolean d = this.done;
/*     */           
/* 560 */           boolean empty = q.isEmpty();
/*     */           
/* 562 */           if (d && empty) {
/* 563 */             Throwable ex = this.error;
/* 564 */             if (ex != null) {
/* 565 */               error(ex);
/*     */             } else {
/* 567 */               complete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 573 */         if (e != 0L) {
/* 574 */           BackpressureHelper.produced(this, e);
/*     */         }
/*     */         
/* 577 */         missed = this.wip.addAndGet(-missed);
/* 578 */       } while (missed != 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class LatestAsyncEmitter<T>
/*     */     extends BaseEmitter<T>
/*     */   {
/*     */     private static final long serialVersionUID = 4023437720691792495L;
/*     */     
/*     */     final AtomicReference<T> queue;
/*     */     
/*     */     Throwable error;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     final AtomicInteger wip;
/*     */     
/*     */     LatestAsyncEmitter(Subscriber<? super T> downstream) {
/* 597 */       super(downstream);
/* 598 */       this.queue = new AtomicReference<T>();
/* 599 */       this.wip = new AtomicInteger();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 604 */       if (this.done || isCancelled()) {
/*     */         return;
/*     */       }
/*     */       
/* 608 */       if (t == null) {
/* 609 */         onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */         return;
/*     */       } 
/* 612 */       this.queue.set(t);
/* 613 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryOnError(Throwable e) {
/* 618 */       if (this.done || isCancelled()) {
/* 619 */         return false;
/*     */       }
/* 621 */       if (e == null) {
/* 622 */         onError(new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources."));
/*     */       }
/* 624 */       this.error = e;
/* 625 */       this.done = true;
/* 626 */       drain();
/* 627 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 632 */       this.done = true;
/* 633 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void onRequested() {
/* 638 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     void onUnsubscribed() {
/* 643 */       if (this.wip.getAndIncrement() == 0) {
/* 644 */         this.queue.lazySet(null);
/*     */       }
/*     */     }
/*     */     
/*     */     void drain() {
/* 649 */       if (this.wip.getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 653 */       int missed = 1;
/* 654 */       Subscriber<? super T> a = this.downstream;
/* 655 */       AtomicReference<T> q = this.queue;
/*     */       
/*     */       do {
/* 658 */         long r = get();
/* 659 */         long e = 0L;
/*     */         
/* 661 */         while (e != r) {
/* 662 */           if (isCancelled()) {
/* 663 */             q.lazySet(null);
/*     */             
/*     */             return;
/*     */           } 
/* 667 */           boolean d = this.done;
/*     */           
/* 669 */           T o = q.getAndSet(null);
/*     */           
/* 671 */           boolean empty = (o == null);
/*     */           
/* 673 */           if (d && empty) {
/* 674 */             Throwable ex = this.error;
/* 675 */             if (ex != null) {
/* 676 */               error(ex);
/*     */             } else {
/* 678 */               complete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/* 683 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 687 */           a.onNext(o);
/*     */           
/* 689 */           e++;
/*     */         } 
/*     */         
/* 692 */         if (e == r) {
/* 693 */           if (isCancelled()) {
/* 694 */             q.lazySet(null);
/*     */             
/*     */             return;
/*     */           } 
/* 698 */           boolean d = this.done;
/*     */           
/* 700 */           boolean empty = (q.get() == null);
/*     */           
/* 702 */           if (d && empty) {
/* 703 */             Throwable ex = this.error;
/* 704 */             if (ex != null) {
/* 705 */               error(ex);
/*     */             } else {
/* 707 */               complete();
/*     */             } 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 713 */         if (e != 0L) {
/* 714 */           BackpressureHelper.produced(this, e);
/*     */         }
/*     */         
/* 717 */         missed = this.wip.addAndGet(-missed);
/* 718 */       } while (missed != 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableCreate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */