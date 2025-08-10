/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.subscribers.QueueDrainSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.QueueDrain;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class FlowableBufferTimed<T, U extends Collection<? super T>>
/*     */   extends AbstractFlowableWithUpstream<T, U>
/*     */ {
/*     */   final long timespan;
/*     */   final long timeskip;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final Callable<U> bufferSupplier;
/*     */   final int maxSize;
/*     */   final boolean restartTimerOnMaxSize;
/*     */   
/*     */   public FlowableBufferTimed(Flowable<T> source, long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, Callable<U> bufferSupplier, int maxSize, boolean restartTimerOnMaxSize) {
/*  46 */     super(source);
/*  47 */     this.timespan = timespan;
/*  48 */     this.timeskip = timeskip;
/*  49 */     this.unit = unit;
/*  50 */     this.scheduler = scheduler;
/*  51 */     this.bufferSupplier = bufferSupplier;
/*  52 */     this.maxSize = maxSize;
/*  53 */     this.restartTimerOnMaxSize = restartTimerOnMaxSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super U> s) {
/*  58 */     if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
/*  59 */       this.source.subscribe((FlowableSubscriber)new BufferExactUnboundedSubscriber<Object, U>((Subscriber<? super U>)new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.unit, this.scheduler));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  64 */     Scheduler.Worker w = this.scheduler.createWorker();
/*     */     
/*  66 */     if (this.timespan == this.timeskip) {
/*  67 */       this.source.subscribe((FlowableSubscriber)new BufferExactBoundedSubscriber<Object, U>((Subscriber<? super U>)new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, w));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  76 */     this.source.subscribe((FlowableSubscriber)new BufferSkipBoundedSubscriber<Object, U>((Subscriber<? super U>)new SerializedSubscriber(s), this.bufferSupplier, this.timespan, this.timeskip, this.unit, w));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferExactUnboundedSubscriber<T, U extends Collection<? super T>>
/*     */     extends QueueDrainSubscriber<T, U, U>
/*     */     implements Subscription, Runnable, Disposable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     
/*     */     final long timespan;
/*     */     
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     Subscription upstream;
/*     */     U buffer;
/*  92 */     final AtomicReference<Disposable> timer = new AtomicReference<Disposable>();
/*     */ 
/*     */ 
/*     */     
/*     */     BufferExactUnboundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier, long timespan, TimeUnit unit, Scheduler scheduler) {
/*  97 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  98 */       this.bufferSupplier = bufferSupplier;
/*  99 */       this.timespan = timespan;
/* 100 */       this.unit = unit;
/* 101 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 106 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 107 */         Collection collection; this.upstream = s;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 112 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
/* 113 */         } catch (Throwable e) {
/* 114 */           Exceptions.throwIfFatal(e);
/* 115 */           cancel();
/* 116 */           EmptySubscription.error(e, this.downstream);
/*     */           
/*     */           return;
/*     */         } 
/* 120 */         this.buffer = (U)collection;
/*     */         
/* 122 */         this.downstream.onSubscribe(this);
/*     */         
/* 124 */         if (!this.cancelled) {
/* 125 */           s.request(Long.MAX_VALUE);
/*     */           
/* 127 */           Disposable d = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
/* 128 */           if (!this.timer.compareAndSet(null, d)) {
/* 129 */             d.dispose();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 137 */       synchronized (this) {
/* 138 */         U b = this.buffer;
/* 139 */         if (b != null) {
/* 140 */           b.add(t);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 147 */       DisposableHelper.dispose(this.timer);
/* 148 */       synchronized (this) {
/* 149 */         this.buffer = null;
/*     */       } 
/* 151 */       this.downstream.onError(t);
/*     */     }
/*     */     
/*     */     public void onComplete() {
/*     */       U b;
/* 156 */       DisposableHelper.dispose(this.timer);
/*     */       
/* 158 */       synchronized (this) {
/* 159 */         b = this.buffer;
/* 160 */         if (b == null) {
/*     */           return;
/*     */         }
/* 163 */         this.buffer = null;
/*     */       } 
/* 165 */       this.queue.offer(b);
/* 166 */       this.done = true;
/* 167 */       if (enter()) {
/* 168 */         QueueDrainHelper.drainMaxLoop(this.queue, this.downstream, false, null, (QueueDrain)this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 174 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 179 */       this.cancelled = true;
/* 180 */       this.upstream.cancel();
/* 181 */       DisposableHelper.dispose(this.timer);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       Collection collection;
/*     */       U current;
/*     */       try {
/* 189 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
/* 190 */       } catch (Throwable e) {
/* 191 */         Exceptions.throwIfFatal(e);
/* 192 */         cancel();
/* 193 */         this.downstream.onError(e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 199 */       synchronized (this) {
/* 200 */         current = this.buffer;
/* 201 */         if (current == null) {
/*     */           return;
/*     */         }
/* 204 */         this.buffer = (U)collection;
/*     */       } 
/*     */       
/* 207 */       fastPathEmitMax(current, false, this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean accept(Subscriber<? super U> a, U v) {
/* 212 */       this.downstream.onNext(v);
/* 213 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 218 */       cancel();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 223 */       return (this.timer.get() == DisposableHelper.DISPOSED);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferSkipBoundedSubscriber<T, U extends Collection<? super T>>
/*     */     extends QueueDrainSubscriber<T, U, U>
/*     */     implements Subscription, Runnable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     final long timespan;
/*     */     final long timeskip;
/*     */     final TimeUnit unit;
/*     */     final Scheduler.Worker w;
/*     */     final List<U> buffers;
/*     */     Subscription upstream;
/*     */     
/*     */     BufferSkipBoundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier, long timespan, long timeskip, TimeUnit unit, Scheduler.Worker w) {
/* 241 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/* 242 */       this.bufferSupplier = bufferSupplier;
/* 243 */       this.timespan = timespan;
/* 244 */       this.timeskip = timeskip;
/* 245 */       this.unit = unit;
/* 246 */       this.w = w;
/* 247 */       this.buffers = new LinkedList<U>();
/*     */     }
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*     */       Collection collection;
/* 252 */       if (!SubscriptionHelper.validate(this.upstream, s)) {
/*     */         return;
/*     */       }
/* 255 */       this.upstream = s;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 260 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
/* 261 */       } catch (Throwable e) {
/* 262 */         Exceptions.throwIfFatal(e);
/* 263 */         this.w.dispose();
/* 264 */         s.cancel();
/* 265 */         EmptySubscription.error(e, this.downstream);
/*     */         
/*     */         return;
/*     */       } 
/* 269 */       this.buffers.add((U)collection);
/*     */       
/* 271 */       this.downstream.onSubscribe(this);
/*     */       
/* 273 */       s.request(Long.MAX_VALUE);
/*     */       
/* 275 */       this.w.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
/*     */       
/* 277 */       this.w.schedule(new RemoveFromBuffer((U)collection), this.timespan, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 282 */       synchronized (this) {
/* 283 */         for (Collection<T> collection : this.buffers) {
/* 284 */           collection.add(t);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 291 */       this.done = true;
/* 292 */       this.w.dispose();
/* 293 */       clear();
/* 294 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       List<U> bs;
/* 300 */       synchronized (this) {
/* 301 */         bs = new ArrayList<U>(this.buffers);
/* 302 */         this.buffers.clear();
/*     */       } 
/*     */       
/* 305 */       for (Collection collection : bs) {
/* 306 */         this.queue.offer(collection);
/*     */       }
/* 308 */       this.done = true;
/* 309 */       if (enter()) {
/* 310 */         QueueDrainHelper.drainMaxLoop(this.queue, this.downstream, false, (Disposable)this.w, (QueueDrain)this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 316 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 321 */       this.cancelled = true;
/* 322 */       this.upstream.cancel();
/* 323 */       this.w.dispose();
/* 324 */       clear();
/*     */     }
/*     */     
/*     */     void clear() {
/* 328 */       synchronized (this) {
/* 329 */         this.buffers.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       Collection collection;
/* 335 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 341 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
/* 342 */       } catch (Throwable e) {
/* 343 */         Exceptions.throwIfFatal(e);
/* 344 */         cancel();
/* 345 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 349 */       synchronized (this) {
/* 350 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 353 */         this.buffers.add((U)collection);
/*     */       } 
/*     */       
/* 356 */       this.w.schedule(new RemoveFromBuffer((U)collection), this.timespan, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean accept(Subscriber<? super U> a, U v) {
/* 361 */       a.onNext(v);
/* 362 */       return true;
/*     */     }
/*     */     
/*     */     final class RemoveFromBuffer implements Runnable {
/*     */       private final U buffer;
/*     */       
/*     */       RemoveFromBuffer(U buffer) {
/* 369 */         this.buffer = buffer;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 374 */         synchronized (FlowableBufferTimed.BufferSkipBoundedSubscriber.this) {
/* 375 */           FlowableBufferTimed.BufferSkipBoundedSubscriber.this.buffers.remove(this.buffer);
/*     */         } 
/*     */         
/* 378 */         FlowableBufferTimed.BufferSkipBoundedSubscriber.this.fastPathOrderedEmitMax(this.buffer, false, (Disposable)FlowableBufferTimed.BufferSkipBoundedSubscriber.this.w);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferExactBoundedSubscriber<T, U extends Collection<? super T>>
/*     */     extends QueueDrainSubscriber<T, U, U>
/*     */     implements Subscription, Runnable, Disposable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     
/*     */     final long timespan;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final int maxSize;
/*     */     
/*     */     final boolean restartTimerOnMaxSize;
/*     */     
/*     */     final Scheduler.Worker w;
/*     */     
/*     */     U buffer;
/*     */     Disposable timer;
/*     */     Subscription upstream;
/*     */     long producerIndex;
/*     */     long consumerIndex;
/*     */     
/*     */     BufferExactBoundedSubscriber(Subscriber<? super U> actual, Callable<U> bufferSupplier, long timespan, TimeUnit unit, int maxSize, boolean restartOnMaxSize, Scheduler.Worker w) {
/* 407 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/* 408 */       this.bufferSupplier = bufferSupplier;
/* 409 */       this.timespan = timespan;
/* 410 */       this.unit = unit;
/* 411 */       this.maxSize = maxSize;
/* 412 */       this.restartTimerOnMaxSize = restartOnMaxSize;
/* 413 */       this.w = w;
/*     */     }
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*     */       Collection collection;
/* 418 */       if (!SubscriptionHelper.validate(this.upstream, s)) {
/*     */         return;
/*     */       }
/* 421 */       this.upstream = s;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 426 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
/* 427 */       } catch (Throwable e) {
/* 428 */         Exceptions.throwIfFatal(e);
/* 429 */         this.w.dispose();
/* 430 */         s.cancel();
/* 431 */         EmptySubscription.error(e, this.downstream);
/*     */         
/*     */         return;
/*     */       } 
/* 435 */       this.buffer = (U)collection;
/*     */       
/* 437 */       this.downstream.onSubscribe(this);
/*     */       
/* 439 */       this.timer = this.w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
/*     */       
/* 441 */       s.request(Long.MAX_VALUE);
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       U b;
/*     */       Collection collection;
/* 447 */       synchronized (this) {
/* 448 */         b = this.buffer;
/* 449 */         if (b == null) {
/*     */           return;
/*     */         }
/*     */         
/* 453 */         b.add(t);
/*     */         
/* 455 */         if (b.size() < this.maxSize) {
/*     */           return;
/*     */         }
/*     */         
/* 459 */         this.buffer = null;
/* 460 */         this.producerIndex++;
/*     */       } 
/*     */       
/* 463 */       if (this.restartTimerOnMaxSize) {
/* 464 */         this.timer.dispose();
/*     */       }
/*     */       
/* 467 */       fastPathOrderedEmitMax(b, false, this);
/*     */       
/*     */       try {
/* 470 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
/* 471 */       } catch (Throwable e) {
/* 472 */         Exceptions.throwIfFatal(e);
/* 473 */         cancel();
/* 474 */         this.downstream.onError(e);
/*     */         
/*     */         return;
/*     */       } 
/* 478 */       synchronized (this) {
/* 479 */         this.buffer = (U)collection;
/* 480 */         this.consumerIndex++;
/*     */       } 
/* 482 */       if (this.restartTimerOnMaxSize) {
/* 483 */         this.timer = this.w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 489 */       synchronized (this) {
/* 490 */         this.buffer = null;
/*     */       } 
/* 492 */       this.downstream.onError(t);
/* 493 */       this.w.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       U b;
/* 499 */       synchronized (this) {
/* 500 */         b = this.buffer;
/* 501 */         this.buffer = null;
/*     */       } 
/*     */       
/* 504 */       if (b != null) {
/* 505 */         this.queue.offer(b);
/* 506 */         this.done = true;
/* 507 */         if (enter()) {
/* 508 */           QueueDrainHelper.drainMaxLoop(this.queue, this.downstream, false, this, (QueueDrain)this);
/*     */         }
/* 510 */         this.w.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean accept(Subscriber<? super U> a, U v) {
/* 516 */       a.onNext(v);
/* 517 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 522 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 527 */       if (!this.cancelled) {
/* 528 */         this.cancelled = true;
/* 529 */         dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 535 */       synchronized (this) {
/* 536 */         this.buffer = null;
/*     */       } 
/* 538 */       this.upstream.cancel();
/* 539 */       this.w.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 544 */       return this.w.isDisposed();
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       Collection collection;
/*     */       U current;
/*     */       try {
/* 552 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The supplied buffer is null");
/* 553 */       } catch (Throwable e) {
/* 554 */         Exceptions.throwIfFatal(e);
/* 555 */         cancel();
/* 556 */         this.downstream.onError(e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 562 */       synchronized (this) {
/* 563 */         current = this.buffer;
/* 564 */         if (current == null || this.producerIndex != this.consumerIndex) {
/*     */           return;
/*     */         }
/* 567 */         this.buffer = (U)collection;
/*     */       } 
/*     */       
/* 570 */       fastPathOrderedEmitMax(current, false, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableBufferTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */