/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.EmptyDisposable;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.observers.QueueDrainObserver;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.util.ObservableQueueDrain;
/*     */ import io.reactivex.internal.util.QueueDrainHelper;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class ObservableBufferTimed<T, U extends Collection<? super T>>
/*     */   extends AbstractObservableWithUpstream<T, U>
/*     */ {
/*     */   final long timespan;
/*     */   final long timeskip;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final Callable<U> bufferSupplier;
/*     */   final int maxSize;
/*     */   final boolean restartTimerOnMaxSize;
/*     */   
/*     */   public ObservableBufferTimed(ObservableSource<T> source, long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, Callable<U> bufferSupplier, int maxSize, boolean restartTimerOnMaxSize) {
/*  45 */     super(source);
/*  46 */     this.timespan = timespan;
/*  47 */     this.timeskip = timeskip;
/*  48 */     this.unit = unit;
/*  49 */     this.scheduler = scheduler;
/*  50 */     this.bufferSupplier = bufferSupplier;
/*  51 */     this.maxSize = maxSize;
/*  52 */     this.restartTimerOnMaxSize = restartTimerOnMaxSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Observer<? super U> t) {
/*  57 */     if (this.timespan == this.timeskip && this.maxSize == Integer.MAX_VALUE) {
/*  58 */       this.source.subscribe((Observer)new BufferExactUnboundedObserver<Object, U>((Observer<? super U>)new SerializedObserver(t), this.bufferSupplier, this.timespan, this.unit, this.scheduler));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  63 */     Scheduler.Worker w = this.scheduler.createWorker();
/*     */     
/*  65 */     if (this.timespan == this.timeskip) {
/*  66 */       this.source.subscribe((Observer)new BufferExactBoundedObserver<Object, U>((Observer<? super U>)new SerializedObserver(t), this.bufferSupplier, this.timespan, this.unit, this.maxSize, this.restartTimerOnMaxSize, w));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  75 */     this.source.subscribe((Observer)new BufferSkipBoundedObserver<Object, U>((Observer<? super U>)new SerializedObserver(t), this.bufferSupplier, this.timespan, this.timeskip, this.unit, w));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferExactUnboundedObserver<T, U extends Collection<? super T>>
/*     */     extends QueueDrainObserver<T, U, U>
/*     */     implements Runnable, Disposable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     
/*     */     final long timespan;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler scheduler;
/*     */     Disposable upstream;
/*     */     U buffer;
/*  92 */     final AtomicReference<Disposable> timer = new AtomicReference<Disposable>();
/*     */ 
/*     */ 
/*     */     
/*     */     BufferExactUnboundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier, long timespan, TimeUnit unit, Scheduler scheduler) {
/*  97 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  98 */       this.bufferSupplier = bufferSupplier;
/*  99 */       this.timespan = timespan;
/* 100 */       this.unit = unit;
/* 101 */       this.scheduler = scheduler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 106 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 107 */         Collection collection; this.upstream = d;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 112 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/* 113 */         } catch (Throwable e) {
/* 114 */           Exceptions.throwIfFatal(e);
/* 115 */           dispose();
/* 116 */           EmptyDisposable.error(e, this.downstream);
/*     */           
/*     */           return;
/*     */         } 
/* 120 */         this.buffer = (U)collection;
/*     */         
/* 122 */         this.downstream.onSubscribe(this);
/*     */         
/* 124 */         if (!this.cancelled) {
/* 125 */           Disposable task = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
/* 126 */           if (!this.timer.compareAndSet(null, task)) {
/* 127 */             task.dispose();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 135 */       synchronized (this) {
/* 136 */         U b = this.buffer;
/* 137 */         if (b == null) {
/*     */           return;
/*     */         }
/* 140 */         b.add(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 146 */       synchronized (this) {
/* 147 */         this.buffer = null;
/*     */       } 
/* 149 */       this.downstream.onError(t);
/* 150 */       DisposableHelper.dispose(this.timer);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       U b;
/* 156 */       synchronized (this) {
/* 157 */         b = this.buffer;
/* 158 */         this.buffer = null;
/*     */       } 
/* 160 */       if (b != null) {
/* 161 */         this.queue.offer(b);
/* 162 */         this.done = true;
/* 163 */         if (enter()) {
/* 164 */           QueueDrainHelper.drainLoop(this.queue, this.downstream, false, null, (ObservableQueueDrain)this);
/*     */         }
/*     */       } 
/* 167 */       DisposableHelper.dispose(this.timer);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 172 */       DisposableHelper.dispose(this.timer);
/* 173 */       this.upstream.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 178 */       return (this.timer.get() == DisposableHelper.DISPOSED);
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       Collection collection;
/*     */       U current;
/*     */       try {
/* 186 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
/* 187 */       } catch (Throwable e) {
/* 188 */         Exceptions.throwIfFatal(e);
/* 189 */         this.downstream.onError(e);
/* 190 */         dispose();
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 196 */       synchronized (this) {
/* 197 */         current = this.buffer;
/* 198 */         if (current != null) {
/* 199 */           this.buffer = (U)collection;
/*     */         }
/*     */       } 
/*     */       
/* 203 */       if (current == null) {
/* 204 */         DisposableHelper.dispose(this.timer);
/*     */         
/*     */         return;
/*     */       } 
/* 208 */       fastPathEmit(current, false, this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Observer<? super U> a, U v) {
/* 213 */       this.downstream.onNext(v);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferSkipBoundedObserver<T, U extends Collection<? super T>>
/*     */     extends QueueDrainObserver<T, U, U>
/*     */     implements Runnable, Disposable
/*     */   {
/*     */     final Callable<U> bufferSupplier;
/*     */     final long timespan;
/*     */     final long timeskip;
/*     */     final TimeUnit unit;
/*     */     final Scheduler.Worker w;
/*     */     final List<U> buffers;
/*     */     Disposable upstream;
/*     */     
/*     */     BufferSkipBoundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier, long timespan, long timeskip, TimeUnit unit, Scheduler.Worker w) {
/* 231 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/* 232 */       this.bufferSupplier = bufferSupplier;
/* 233 */       this.timespan = timespan;
/* 234 */       this.timeskip = timeskip;
/* 235 */       this.unit = unit;
/* 236 */       this.w = w;
/* 237 */       this.buffers = new LinkedList<U>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 242 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 243 */         Collection collection; this.upstream = d;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 248 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/* 249 */         } catch (Throwable e) {
/* 250 */           Exceptions.throwIfFatal(e);
/* 251 */           d.dispose();
/* 252 */           EmptyDisposable.error(e, this.downstream);
/* 253 */           this.w.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 257 */         this.buffers.add((U)collection);
/*     */         
/* 259 */         this.downstream.onSubscribe(this);
/*     */         
/* 261 */         this.w.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
/*     */         
/* 263 */         this.w.schedule(new RemoveFromBufferEmit((U)collection), this.timespan, this.unit);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 269 */       synchronized (this) {
/* 270 */         for (Collection<T> collection : this.buffers) {
/* 271 */           collection.add(t);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 278 */       this.done = true;
/* 279 */       clear();
/* 280 */       this.downstream.onError(t);
/* 281 */       this.w.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*     */       List<U> bs;
/* 287 */       synchronized (this) {
/* 288 */         bs = new ArrayList<U>(this.buffers);
/* 289 */         this.buffers.clear();
/*     */       } 
/*     */       
/* 292 */       for (Collection collection : bs) {
/* 293 */         this.queue.offer(collection);
/*     */       }
/* 295 */       this.done = true;
/* 296 */       if (enter()) {
/* 297 */         QueueDrainHelper.drainLoop(this.queue, this.downstream, false, (Disposable)this.w, (ObservableQueueDrain)this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 303 */       if (!this.cancelled) {
/* 304 */         this.cancelled = true;
/* 305 */         clear();
/* 306 */         this.upstream.dispose();
/* 307 */         this.w.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 313 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void clear() {
/* 317 */       synchronized (this) {
/* 318 */         this.buffers.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       Collection collection;
/* 324 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 330 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
/* 331 */       } catch (Throwable e) {
/* 332 */         Exceptions.throwIfFatal(e);
/* 333 */         this.downstream.onError(e);
/* 334 */         dispose();
/*     */         
/*     */         return;
/*     */       } 
/* 338 */       synchronized (this) {
/* 339 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/* 342 */         this.buffers.add((U)collection);
/*     */       } 
/*     */       
/* 345 */       this.w.schedule(new RemoveFromBuffer((U)collection), this.timespan, this.unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Observer<? super U> a, U v) {
/* 350 */       a.onNext(v);
/*     */     }
/*     */     
/*     */     final class RemoveFromBuffer implements Runnable {
/*     */       private final U b;
/*     */       
/*     */       RemoveFromBuffer(U b) {
/* 357 */         this.b = b;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 362 */         synchronized (ObservableBufferTimed.BufferSkipBoundedObserver.this) {
/* 363 */           ObservableBufferTimed.BufferSkipBoundedObserver.this.buffers.remove(this.b);
/*     */         } 
/*     */         
/* 366 */         ObservableBufferTimed.BufferSkipBoundedObserver.this.fastPathOrderedEmit(this.b, false, (Disposable)ObservableBufferTimed.BufferSkipBoundedObserver.this.w);
/*     */       }
/*     */     }
/*     */     
/*     */     final class RemoveFromBufferEmit implements Runnable {
/*     */       private final U buffer;
/*     */       
/*     */       RemoveFromBufferEmit(U buffer) {
/* 374 */         this.buffer = buffer;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 379 */         synchronized (ObservableBufferTimed.BufferSkipBoundedObserver.this) {
/* 380 */           ObservableBufferTimed.BufferSkipBoundedObserver.this.buffers.remove(this.buffer);
/*     */         } 
/*     */         
/* 383 */         ObservableBufferTimed.BufferSkipBoundedObserver.this.fastPathOrderedEmit(this.buffer, false, (Disposable)ObservableBufferTimed.BufferSkipBoundedObserver.this.w);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class BufferExactBoundedObserver<T, U extends Collection<? super T>>
/*     */     extends QueueDrainObserver<T, U, U>
/*     */     implements Runnable, Disposable
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
/*     */     Disposable upstream;
/*     */     long producerIndex;
/*     */     long consumerIndex;
/*     */     
/*     */     BufferExactBoundedObserver(Observer<? super U> actual, Callable<U> bufferSupplier, long timespan, TimeUnit unit, int maxSize, boolean restartOnMaxSize, Scheduler.Worker w) {
/* 412 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/* 413 */       this.bufferSupplier = bufferSupplier;
/* 414 */       this.timespan = timespan;
/* 415 */       this.unit = unit;
/* 416 */       this.maxSize = maxSize;
/* 417 */       this.restartTimerOnMaxSize = restartOnMaxSize;
/* 418 */       this.w = w;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 423 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 424 */         Collection collection; this.upstream = d;
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 429 */           collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/* 430 */         } catch (Throwable e) {
/* 431 */           Exceptions.throwIfFatal(e);
/* 432 */           d.dispose();
/* 433 */           EmptyDisposable.error(e, this.downstream);
/* 434 */           this.w.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 438 */         this.buffer = (U)collection;
/*     */         
/* 440 */         this.downstream.onSubscribe(this);
/*     */         
/* 442 */         this.timer = this.w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       U b;
/*     */       Collection collection;
/* 449 */       synchronized (this) {
/* 450 */         b = this.buffer;
/* 451 */         if (b == null) {
/*     */           return;
/*     */         }
/*     */         
/* 455 */         b.add(t);
/*     */         
/* 457 */         if (b.size() < this.maxSize) {
/*     */           return;
/*     */         }
/* 460 */         this.buffer = null;
/* 461 */         this.producerIndex++;
/*     */       } 
/*     */       
/* 464 */       if (this.restartTimerOnMaxSize) {
/* 465 */         this.timer.dispose();
/*     */       }
/*     */       
/* 468 */       fastPathOrderedEmit(b, false, this);
/*     */       
/*     */       try {
/* 471 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The buffer supplied is null");
/* 472 */       } catch (Throwable e) {
/* 473 */         Exceptions.throwIfFatal(e);
/* 474 */         this.downstream.onError(e);
/* 475 */         dispose();
/*     */         
/*     */         return;
/*     */       } 
/* 479 */       synchronized (this) {
/* 480 */         this.buffer = (U)collection;
/* 481 */         this.consumerIndex++;
/*     */       } 
/* 483 */       if (this.restartTimerOnMaxSize) {
/* 484 */         this.timer = this.w.schedulePeriodically(this, this.timespan, this.timespan, this.unit);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 490 */       synchronized (this) {
/* 491 */         this.buffer = null;
/*     */       } 
/* 493 */       this.downstream.onError(t);
/* 494 */       this.w.dispose();
/*     */     }
/*     */     
/*     */     public void onComplete() {
/*     */       U b;
/* 499 */       this.w.dispose();
/*     */ 
/*     */       
/* 502 */       synchronized (this) {
/* 503 */         b = this.buffer;
/* 504 */         this.buffer = null;
/*     */       } 
/*     */       
/* 507 */       if (b != null) {
/* 508 */         this.queue.offer(b);
/* 509 */         this.done = true;
/* 510 */         if (enter()) {
/* 511 */           QueueDrainHelper.drainLoop(this.queue, this.downstream, false, this, (ObservableQueueDrain)this);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void accept(Observer<? super U> a, U v) {
/* 518 */       a.onNext(v);
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 523 */       if (!this.cancelled) {
/* 524 */         this.cancelled = true;
/* 525 */         this.upstream.dispose();
/* 526 */         this.w.dispose();
/* 527 */         synchronized (this) {
/* 528 */           this.buffer = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 535 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       Collection collection;
/*     */       U current;
/*     */       try {
/* 543 */         collection = (Collection)ObjectHelper.requireNonNull(this.bufferSupplier.call(), "The bufferSupplier returned a null buffer");
/* 544 */       } catch (Throwable e) {
/* 545 */         Exceptions.throwIfFatal(e);
/* 546 */         dispose();
/* 547 */         this.downstream.onError(e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 553 */       synchronized (this) {
/* 554 */         current = this.buffer;
/* 555 */         if (current == null || this.producerIndex != this.consumerIndex) {
/*     */           return;
/*     */         }
/* 558 */         this.buffer = (U)collection;
/*     */       } 
/*     */       
/* 561 */       fastPathOrderedEmit(current, false, this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableBufferTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */