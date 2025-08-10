/*     */ package io.reactivex.internal.operators.observable;
/*     */ 
/*     */ import io.reactivex.Observable;
/*     */ import io.reactivex.ObservableSource;
/*     */ import io.reactivex.Observer;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.DisposableHelper;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.observers.QueueDrainObserver;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.observers.SerializedObserver;
/*     */ import io.reactivex.subjects.UnicastSubject;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObservableWindowTimed<T>
/*     */   extends AbstractObservableWithUpstream<T, Observable<T>>
/*     */ {
/*     */   final long timespan;
/*     */   final long timeskip;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final long maxSize;
/*     */   final int bufferSize;
/*     */   final boolean restartTimerOnMaxSize;
/*     */   
/*     */   public ObservableWindowTimed(ObservableSource<T> source, long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, long maxSize, int bufferSize, boolean restartTimerOnMaxSize) {
/*  44 */     super(source);
/*  45 */     this.timespan = timespan;
/*  46 */     this.timeskip = timeskip;
/*  47 */     this.unit = unit;
/*  48 */     this.scheduler = scheduler;
/*  49 */     this.maxSize = maxSize;
/*  50 */     this.bufferSize = bufferSize;
/*  51 */     this.restartTimerOnMaxSize = restartTimerOnMaxSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Observer<? super Observable<T>> t) {
/*  56 */     SerializedObserver<Observable<T>> actual = new SerializedObserver(t);
/*     */     
/*  58 */     if (this.timespan == this.timeskip) {
/*  59 */       if (this.maxSize == Long.MAX_VALUE) {
/*  60 */         this.source.subscribe(new WindowExactUnboundedObserver<T>((Observer<? super Observable<T>>)actual, this.timespan, this.unit, this.scheduler, this.bufferSize));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  65 */       this.source.subscribe((Observer)new WindowExactBoundedObserver<T>((Observer<? super Observable<T>>)actual, this.timespan, this.unit, this.scheduler, this.bufferSize, this.maxSize, this.restartTimerOnMaxSize));
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  71 */     this.source.subscribe((Observer)new WindowSkipObserver<T>((Observer<? super Observable<T>>)actual, this.timespan, this.timeskip, this.unit, this.scheduler
/*  72 */           .createWorker(), this.bufferSize));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowExactUnboundedObserver<T>
/*     */     extends QueueDrainObserver<T, Object, Observable<T>>
/*     */     implements Observer<T>, Disposable, Runnable
/*     */   {
/*     */     final long timespan;
/*     */     
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     final int bufferSize;
/*     */     Disposable upstream;
/*     */     UnicastSubject<T> window;
/*  87 */     final SequentialDisposable timer = new SequentialDisposable();
/*     */     
/*  89 */     static final Object NEXT = new Object();
/*     */     
/*     */     volatile boolean terminated;
/*     */ 
/*     */     
/*     */     WindowExactUnboundedObserver(Observer<? super Observable<T>> actual, long timespan, TimeUnit unit, Scheduler scheduler, int bufferSize) {
/*  95 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  96 */       this.timespan = timespan;
/*  97 */       this.unit = unit;
/*  98 */       this.scheduler = scheduler;
/*  99 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 104 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 105 */         this.upstream = d;
/*     */         
/* 107 */         this.window = UnicastSubject.create(this.bufferSize);
/*     */         
/* 109 */         Observer<? super Observable<T>> a = this.downstream;
/* 110 */         a.onSubscribe(this);
/*     */         
/* 112 */         a.onNext(this.window);
/*     */         
/* 114 */         if (!this.cancelled) {
/* 115 */           Disposable task = this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit);
/* 116 */           this.timer.replace(task);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 123 */       if (this.terminated) {
/*     */         return;
/*     */       }
/* 126 */       if (fastEnter()) {
/* 127 */         this.window.onNext(t);
/* 128 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 132 */         this.queue.offer(NotificationLite.next(t));
/* 133 */         if (!enter()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 137 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 142 */       this.error = t;
/* 143 */       this.done = true;
/* 144 */       if (enter()) {
/* 145 */         drainLoop();
/*     */       }
/*     */       
/* 148 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 153 */       this.done = true;
/* 154 */       if (enter()) {
/* 155 */         drainLoop();
/*     */       }
/*     */       
/* 158 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 163 */       this.cancelled = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 168 */       return this.cancelled;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 173 */       if (this.cancelled) {
/* 174 */         this.terminated = true;
/*     */       }
/* 176 */       this.queue.offer(NEXT);
/* 177 */       if (enter()) {
/* 178 */         drainLoop();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     void drainLoop() {
/* 184 */       MpscLinkedQueue<Object> q = (MpscLinkedQueue<Object>)this.queue;
/* 185 */       Observer<? super Observable<T>> a = this.downstream;
/* 186 */       UnicastSubject<T> w = this.window;
/*     */       
/* 188 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 192 */         boolean term = this.terminated;
/*     */         
/* 194 */         boolean d = this.done;
/*     */         
/* 196 */         Object o = q.poll();
/*     */         
/* 198 */         if (d && (o == null || o == NEXT)) {
/* 199 */           this.window = null;
/* 200 */           q.clear();
/* 201 */           Throwable err = this.error;
/* 202 */           if (err != null) {
/* 203 */             w.onError(err);
/*     */           } else {
/* 205 */             w.onComplete();
/*     */           } 
/* 207 */           this.timer.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 211 */         if (o == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 231 */           missed = leave(-missed);
/* 232 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         if (o == NEXT) {
/*     */           w.onComplete();
/*     */           if (!term) {
/*     */             w = UnicastSubject.create(this.bufferSize);
/*     */             this.window = w;
/*     */             a.onNext(w);
/*     */             continue;
/*     */           } 
/*     */           this.upstream.dispose();
/*     */           continue;
/*     */         } 
/*     */         w.onNext(NotificationLite.getValue(o));
/*     */       } 
/*     */     } }
/*     */   static final class WindowExactBoundedObserver<T> extends QueueDrainObserver<T, Object, Observable<T>> implements Disposable { final long timespan; final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     final int bufferSize;
/*     */     final boolean restartTimerOnMaxSize;
/*     */     final long maxSize;
/*     */     final Scheduler.Worker worker;
/*     */     long count;
/*     */     long producerIndex;
/*     */     Disposable upstream;
/*     */     UnicastSubject<T> window;
/*     */     volatile boolean terminated;
/* 261 */     final SequentialDisposable timer = new SequentialDisposable();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     WindowExactBoundedObserver(Observer<? super Observable<T>> actual, long timespan, TimeUnit unit, Scheduler scheduler, int bufferSize, long maxSize, boolean restartTimerOnMaxSize) {
/* 267 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/* 268 */       this.timespan = timespan;
/* 269 */       this.unit = unit;
/* 270 */       this.scheduler = scheduler;
/* 271 */       this.bufferSize = bufferSize;
/* 272 */       this.maxSize = maxSize;
/* 273 */       this.restartTimerOnMaxSize = restartTimerOnMaxSize;
/* 274 */       if (restartTimerOnMaxSize) {
/* 275 */         this.worker = scheduler.createWorker();
/*     */       } else {
/* 277 */         this.worker = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 283 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 284 */         Disposable task; this.upstream = d;
/*     */         
/* 286 */         Observer<? super Observable<T>> a = this.downstream;
/*     */         
/* 288 */         a.onSubscribe(this);
/*     */         
/* 290 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 294 */         UnicastSubject<T> w = UnicastSubject.create(this.bufferSize);
/* 295 */         this.window = w;
/*     */         
/* 297 */         a.onNext(w);
/*     */ 
/*     */         
/* 300 */         ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
/* 301 */         if (this.restartTimerOnMaxSize) {
/* 302 */           task = this.worker.schedulePeriodically(consumerIndexHolder, this.timespan, this.timespan, this.unit);
/*     */         } else {
/* 304 */           task = this.scheduler.schedulePeriodicallyDirect(consumerIndexHolder, this.timespan, this.timespan, this.unit);
/*     */         } 
/*     */         
/* 307 */         this.timer.replace(task);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 313 */       if (this.terminated) {
/*     */         return;
/*     */       }
/*     */       
/* 317 */       if (fastEnter()) {
/* 318 */         UnicastSubject<T> w = this.window;
/* 319 */         w.onNext(t);
/*     */         
/* 321 */         long c = this.count + 1L;
/*     */         
/* 323 */         if (c >= this.maxSize) {
/* 324 */           this.producerIndex++;
/* 325 */           this.count = 0L;
/*     */           
/* 327 */           w.onComplete();
/*     */           
/* 329 */           w = UnicastSubject.create(this.bufferSize);
/* 330 */           this.window = w;
/* 331 */           this.downstream.onNext(w);
/* 332 */           if (this.restartTimerOnMaxSize) {
/* 333 */             Disposable tm = (Disposable)this.timer.get();
/* 334 */             tm.dispose();
/* 335 */             Disposable task = this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit);
/*     */ 
/*     */             
/* 338 */             DisposableHelper.replace((AtomicReference)this.timer, task);
/*     */           } 
/*     */         } else {
/* 341 */           this.count = c;
/*     */         } 
/*     */         
/* 344 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 348 */         this.queue.offer(NotificationLite.next(t));
/* 349 */         if (!enter()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 353 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 358 */       this.error = t;
/* 359 */       this.done = true;
/* 360 */       if (enter()) {
/* 361 */         drainLoop();
/*     */       }
/*     */       
/* 364 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 369 */       this.done = true;
/* 370 */       if (enter()) {
/* 371 */         drainLoop();
/*     */       }
/*     */       
/* 374 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 379 */       this.cancelled = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 384 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void disposeTimer() {
/* 388 */       DisposableHelper.dispose((AtomicReference)this.timer);
/* 389 */       Scheduler.Worker w = this.worker;
/* 390 */       if (w != null) {
/* 391 */         w.dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 396 */       MpscLinkedQueue<Object> q = (MpscLinkedQueue<Object>)this.queue;
/* 397 */       Observer<? super Observable<T>> a = this.downstream;
/* 398 */       UnicastSubject<T> w = this.window;
/*     */       
/* 400 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 404 */         if (this.terminated) {
/* 405 */           this.upstream.dispose();
/* 406 */           q.clear();
/* 407 */           disposeTimer();
/*     */           
/*     */           return;
/*     */         } 
/* 411 */         boolean d = this.done;
/*     */         
/* 413 */         Object o = q.poll();
/*     */         
/* 415 */         boolean empty = (o == null);
/* 416 */         boolean isHolder = o instanceof ConsumerIndexHolder;
/*     */         
/* 418 */         if (d && (empty || isHolder)) {
/* 419 */           this.window = null;
/* 420 */           q.clear();
/* 421 */           Throwable err = this.error;
/* 422 */           if (err != null) {
/* 423 */             w.onError(err);
/*     */           } else {
/* 425 */             w.onComplete();
/*     */           } 
/* 427 */           disposeTimer();
/*     */           
/*     */           return;
/*     */         } 
/* 431 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 477 */           missed = leave(-missed);
/* 478 */           if (missed == 0)
/*     */             break;  continue;
/*     */         }  if (isHolder) { ConsumerIndexHolder consumerIndexHolder = (ConsumerIndexHolder)o; if (!this.restartTimerOnMaxSize || this.producerIndex == consumerIndexHolder.index) { w.onComplete(); this.count = 0L; w = UnicastSubject.create(this.bufferSize); this.window = w; a.onNext(w); }
/*     */            continue; }
/*     */          w.onNext(NotificationLite.getValue(o)); long c = this.count + 1L; if (c >= this.maxSize) {
/*     */           this.producerIndex++; this.count = 0L; w.onComplete(); w = UnicastSubject.create(this.bufferSize); this.window = w; this.downstream.onNext(w); if (this.restartTimerOnMaxSize) {
/*     */             Disposable tm = (Disposable)this.timer.get(); tm.dispose(); Disposable task = this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit); if (!this.timer.compareAndSet(tm, task))
/*     */               task.dispose(); 
/*     */           }  continue;
/*     */         }  this.count = c;
/* 488 */       }  } static final class ConsumerIndexHolder implements Runnable { final long index; ConsumerIndexHolder(long index, ObservableWindowTimed.WindowExactBoundedObserver<?> parent) { this.index = index;
/* 489 */         this.parent = parent; }
/*     */       
/*     */       final ObservableWindowTimed.WindowExactBoundedObserver<?> parent;
/*     */       
/*     */       public void run() {
/* 494 */         ObservableWindowTimed.WindowExactBoundedObserver<?> p = this.parent;
/*     */         
/* 496 */         if (!p.cancelled) {
/* 497 */           p.queue.offer(this);
/*     */         } else {
/* 499 */           p.terminated = true;
/*     */         } 
/* 501 */         if (p.enter()) {
/* 502 */           p.drainLoop();
/*     */         }
/*     */       } }
/*     */      }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class WindowSkipObserver<T>
/*     */     extends QueueDrainObserver<T, Object, Observable<T>>
/*     */     implements Disposable, Runnable
/*     */   {
/*     */     final long timespan;
/*     */     
/*     */     final long timeskip;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     final int bufferSize;
/*     */     final List<UnicastSubject<T>> windows;
/*     */     Disposable upstream;
/*     */     volatile boolean terminated;
/*     */     
/*     */     WindowSkipObserver(Observer<? super Observable<T>> actual, long timespan, long timeskip, TimeUnit unit, Scheduler.Worker worker, int bufferSize) {
/* 526 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/* 527 */       this.timespan = timespan;
/* 528 */       this.timeskip = timeskip;
/* 529 */       this.unit = unit;
/* 530 */       this.worker = worker;
/* 531 */       this.bufferSize = bufferSize;
/* 532 */       this.windows = new LinkedList<UnicastSubject<T>>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Disposable d) {
/* 537 */       if (DisposableHelper.validate(this.upstream, d)) {
/* 538 */         this.upstream = d;
/*     */         
/* 540 */         this.downstream.onSubscribe(this);
/*     */         
/* 542 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 546 */         UnicastSubject<T> w = UnicastSubject.create(this.bufferSize);
/* 547 */         this.windows.add(w);
/*     */         
/* 549 */         this.downstream.onNext(w);
/* 550 */         this.worker.schedule(new CompletionTask(w), this.timespan, this.unit);
/*     */         
/* 552 */         this.worker.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 559 */       if (fastEnter()) {
/* 560 */         for (UnicastSubject<T> w : this.windows) {
/* 561 */           w.onNext(t);
/*     */         }
/* 563 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 567 */         this.queue.offer(t);
/* 568 */         if (!enter()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 572 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 577 */       this.error = t;
/* 578 */       this.done = true;
/* 579 */       if (enter()) {
/* 580 */         drainLoop();
/*     */       }
/*     */       
/* 583 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 588 */       this.done = true;
/* 589 */       if (enter()) {
/* 590 */         drainLoop();
/*     */       }
/*     */       
/* 593 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 598 */       this.cancelled = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 603 */       return this.cancelled;
/*     */     }
/*     */     
/*     */     void complete(UnicastSubject<T> w) {
/* 607 */       this.queue.offer(new SubjectWork<T>(w, false));
/* 608 */       if (enter()) {
/* 609 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop()
/*     */     {
/* 615 */       MpscLinkedQueue<Object> q = (MpscLinkedQueue<Object>)this.queue;
/* 616 */       Observer<? super Observable<T>> a = this.downstream;
/* 617 */       List<UnicastSubject<T>> ws = this.windows;
/*     */       
/* 619 */       int missed = 1;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 624 */         if (this.terminated) {
/* 625 */           this.upstream.dispose();
/* 626 */           q.clear();
/* 627 */           ws.clear();
/* 628 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 632 */         boolean d = this.done;
/*     */         
/* 634 */         Object v = q.poll();
/*     */         
/* 636 */         boolean empty = (v == null);
/* 637 */         boolean sw = v instanceof SubjectWork;
/*     */         
/* 639 */         if (d && (empty || sw)) {
/* 640 */           q.clear();
/* 641 */           Throwable e = this.error;
/* 642 */           if (e != null) {
/* 643 */             for (UnicastSubject<T> w : ws) {
/* 644 */               w.onError(e);
/*     */             }
/*     */           } else {
/* 647 */             for (UnicastSubject<T> w : ws) {
/* 648 */               w.onComplete();
/*     */             }
/*     */           } 
/* 651 */           ws.clear();
/* 652 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 656 */         if (empty) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 687 */           missed = leave(-missed);
/* 688 */           if (missed == 0)
/*     */             break;  continue;
/*     */         }  if (sw) { SubjectWork<T> work = (SubjectWork<T>)v; if (work.open) {
/*     */             if (this.cancelled)
/*     */               continue;  UnicastSubject<T> w = UnicastSubject.create(this.bufferSize); ws.add(w); a.onNext(w); this.worker.schedule(new CompletionTask(w), this.timespan, this.unit); continue;
/*     */           }  ws.remove(work.w); work.w.onComplete(); if (ws.isEmpty() && this.cancelled)
/*     */             this.terminated = true;  continue; }
/*     */          for (UnicastSubject<T> w : ws)
/*     */           w.onNext(v); 
/* 697 */       }  } public void run() { UnicastSubject<T> w = UnicastSubject.create(this.bufferSize);
/*     */       
/* 699 */       SubjectWork<T> sw = new SubjectWork<T>(w, true);
/* 700 */       if (!this.cancelled) {
/* 701 */         this.queue.offer(sw);
/*     */       }
/* 703 */       if (enter())
/* 704 */         drainLoop();  }
/*     */ 
/*     */     
/*     */     static final class SubjectWork<T> {
/*     */       final UnicastSubject<T> w;
/*     */       final boolean open;
/*     */       
/*     */       SubjectWork(UnicastSubject<T> w, boolean open) {
/* 712 */         this.w = w;
/* 713 */         this.open = open;
/*     */       }
/*     */     }
/*     */     
/*     */     final class CompletionTask implements Runnable {
/*     */       private final UnicastSubject<T> w;
/*     */       
/*     */       CompletionTask(UnicastSubject<T> w) {
/* 721 */         this.w = w;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 726 */         ObservableWindowTimed.WindowSkipObserver.this.complete(this.w);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/observable/ObservableWindowTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */