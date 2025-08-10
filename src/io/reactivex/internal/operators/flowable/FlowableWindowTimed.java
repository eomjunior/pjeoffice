/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.fuseable.SimplePlainQueue;
/*     */ import io.reactivex.internal.queue.MpscLinkedQueue;
/*     */ import io.reactivex.internal.subscribers.QueueDrainSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.NotificationLite;
/*     */ import io.reactivex.processors.UnicastProcessor;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class FlowableWindowTimed<T>
/*     */   extends AbstractFlowableWithUpstream<T, Flowable<T>>
/*     */ {
/*     */   final long timespan;
/*     */   final long timeskip;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final long maxSize;
/*     */   final int bufferSize;
/*     */   final boolean restartTimerOnMaxSize;
/*     */   
/*     */   public FlowableWindowTimed(Flowable<T> source, long timespan, long timeskip, TimeUnit unit, Scheduler scheduler, long maxSize, int bufferSize, boolean restartTimerOnMaxSize) {
/*  46 */     super(source);
/*  47 */     this.timespan = timespan;
/*  48 */     this.timeskip = timeskip;
/*  49 */     this.unit = unit;
/*  50 */     this.scheduler = scheduler;
/*  51 */     this.maxSize = maxSize;
/*  52 */     this.bufferSize = bufferSize;
/*  53 */     this.restartTimerOnMaxSize = restartTimerOnMaxSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super Flowable<T>> s) {
/*  58 */     SerializedSubscriber<Flowable<T>> actual = new SerializedSubscriber(s);
/*     */     
/*  60 */     if (this.timespan == this.timeskip) {
/*  61 */       if (this.maxSize == Long.MAX_VALUE) {
/*  62 */         this.source.subscribe(new WindowExactUnboundedSubscriber<T>((Subscriber<? super Flowable<T>>)actual, this.timespan, this.unit, this.scheduler, this.bufferSize));
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  67 */       this.source.subscribe((FlowableSubscriber)new WindowExactBoundedSubscriber<T>((Subscriber<? super Flowable<T>>)actual, this.timespan, this.unit, this.scheduler, this.bufferSize, this.maxSize, this.restartTimerOnMaxSize));
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  73 */     this.source.subscribe((FlowableSubscriber)new WindowSkipSubscriber<T>((Subscriber<? super Flowable<T>>)actual, this.timespan, this.timeskip, this.unit, this.scheduler
/*  74 */           .createWorker(), this.bufferSize));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowExactUnboundedSubscriber<T>
/*     */     extends QueueDrainSubscriber<T, Object, Flowable<T>>
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     final long timespan;
/*     */     
/*     */     final TimeUnit unit;
/*     */     final Scheduler scheduler;
/*     */     final int bufferSize;
/*     */     Subscription upstream;
/*     */     UnicastProcessor<T> window;
/*  89 */     final SequentialDisposable timer = new SequentialDisposable();
/*     */     
/*  91 */     static final Object NEXT = new Object();
/*     */     
/*     */     volatile boolean terminated;
/*     */ 
/*     */     
/*     */     WindowExactUnboundedSubscriber(Subscriber<? super Flowable<T>> actual, long timespan, TimeUnit unit, Scheduler scheduler, int bufferSize) {
/*  97 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/*  98 */       this.timespan = timespan;
/*  99 */       this.unit = unit;
/* 100 */       this.scheduler = scheduler;
/* 101 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 106 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 107 */         this.upstream = s;
/*     */         
/* 109 */         this.window = UnicastProcessor.create(this.bufferSize);
/*     */         
/* 111 */         Subscriber<? super Flowable<T>> a = this.downstream;
/* 112 */         a.onSubscribe(this);
/*     */         
/* 114 */         long r = requested();
/* 115 */         if (r != 0L) {
/* 116 */           a.onNext(this.window);
/* 117 */           if (r != Long.MAX_VALUE) {
/* 118 */             produced(1L);
/*     */           }
/*     */         } else {
/* 121 */           this.cancelled = true;
/* 122 */           s.cancel();
/* 123 */           a.onError((Throwable)new MissingBackpressureException("Could not deliver first window due to lack of requests."));
/*     */           
/*     */           return;
/*     */         } 
/* 127 */         if (!this.cancelled && 
/* 128 */           this.timer.replace(this.scheduler.schedulePeriodicallyDirect(this, this.timespan, this.timespan, this.unit))) {
/* 129 */           s.request(Long.MAX_VALUE);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 137 */       if (this.terminated) {
/*     */         return;
/*     */       }
/* 140 */       if (fastEnter()) {
/* 141 */         this.window.onNext(t);
/* 142 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 146 */         this.queue.offer(NotificationLite.next(t));
/* 147 */         if (!enter()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 151 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 156 */       this.error = t;
/* 157 */       this.done = true;
/* 158 */       if (enter()) {
/* 159 */         drainLoop();
/*     */       }
/*     */       
/* 162 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 167 */       this.done = true;
/* 168 */       if (enter()) {
/* 169 */         drainLoop();
/*     */       }
/*     */       
/* 172 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 177 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 182 */       this.cancelled = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 187 */       if (this.cancelled) {
/* 188 */         this.terminated = true;
/*     */       }
/* 190 */       this.queue.offer(NEXT);
/* 191 */       if (enter()) {
/* 192 */         drainLoop();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     void drainLoop() {
/* 198 */       SimplePlainQueue<Object> q = this.queue;
/* 199 */       Subscriber<? super Flowable<T>> a = this.downstream;
/* 200 */       UnicastProcessor<T> w = this.window;
/*     */       
/* 202 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 206 */         boolean term = this.terminated;
/*     */         
/* 208 */         boolean d = this.done;
/*     */         
/* 210 */         Object o = q.poll();
/*     */         
/* 212 */         if (d && (o == null || o == NEXT)) {
/* 213 */           this.window = null;
/* 214 */           q.clear();
/* 215 */           Throwable err = this.error;
/* 216 */           if (err != null) {
/* 217 */             w.onError(err);
/*     */           } else {
/* 219 */             w.onComplete();
/*     */           } 
/* 221 */           this.timer.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 225 */         if (o == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 258 */           missed = leave(-missed);
/* 259 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/*     */         if (o == NEXT) {
/*     */           w.onComplete();
/*     */           if (!term) {
/*     */             w = UnicastProcessor.create(this.bufferSize);
/*     */             this.window = w;
/*     */             long r = requested();
/*     */             if (r != 0L) {
/*     */               a.onNext(w);
/*     */               if (r != Long.MAX_VALUE)
/*     */                 produced(1L); 
/*     */               continue;
/*     */             } 
/*     */             this.window = null;
/*     */             this.queue.clear();
/*     */             this.upstream.cancel();
/*     */             a.onError((Throwable)new MissingBackpressureException("Could not deliver first window due to lack of requests."));
/*     */             this.timer.dispose();
/*     */             return;
/*     */           } 
/*     */           this.upstream.cancel();
/*     */           continue;
/*     */         } 
/*     */         w.onNext(NotificationLite.getValue(o));
/*     */       } 
/* 287 */     } } static final class WindowExactBoundedSubscriber<T> extends QueueDrainSubscriber<T, Object, Flowable<T>> implements Subscription { final long timespan; final TimeUnit unit; final Scheduler scheduler; final int bufferSize; final boolean restartTimerOnMaxSize; final long maxSize; final Scheduler.Worker worker; long count; long producerIndex; Subscription upstream; UnicastProcessor<T> window; volatile boolean terminated; final SequentialDisposable timer = new SequentialDisposable();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     WindowExactBoundedSubscriber(Subscriber<? super Flowable<T>> actual, long timespan, TimeUnit unit, Scheduler scheduler, int bufferSize, long maxSize, boolean restartTimerOnMaxSize) {
/* 293 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/* 294 */       this.timespan = timespan;
/* 295 */       this.unit = unit;
/* 296 */       this.scheduler = scheduler;
/* 297 */       this.bufferSize = bufferSize;
/* 298 */       this.maxSize = maxSize;
/* 299 */       this.restartTimerOnMaxSize = restartTimerOnMaxSize;
/* 300 */       if (restartTimerOnMaxSize) {
/* 301 */         this.worker = scheduler.createWorker();
/*     */       } else {
/* 303 */         this.worker = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 309 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*     */         Disposable task;
/* 311 */         this.upstream = s;
/*     */         
/* 313 */         Subscriber<? super Flowable<T>> a = this.downstream;
/*     */         
/* 315 */         a.onSubscribe(this);
/*     */         
/* 317 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 321 */         UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize);
/* 322 */         this.window = w;
/*     */         
/* 324 */         long r = requested();
/* 325 */         if (r != 0L) {
/* 326 */           a.onNext(w);
/* 327 */           if (r != Long.MAX_VALUE) {
/* 328 */             produced(1L);
/*     */           }
/*     */         } else {
/* 331 */           this.cancelled = true;
/* 332 */           s.cancel();
/* 333 */           a.onError((Throwable)new MissingBackpressureException("Could not deliver initial window due to lack of requests."));
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 338 */         ConsumerIndexHolder consumerIndexHolder = new ConsumerIndexHolder(this.producerIndex, this);
/* 339 */         if (this.restartTimerOnMaxSize) {
/* 340 */           task = this.worker.schedulePeriodically(consumerIndexHolder, this.timespan, this.timespan, this.unit);
/*     */         } else {
/* 342 */           task = this.scheduler.schedulePeriodicallyDirect(consumerIndexHolder, this.timespan, this.timespan, this.unit);
/*     */         } 
/*     */         
/* 345 */         if (this.timer.replace(task)) {
/* 346 */           s.request(Long.MAX_VALUE);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 353 */       if (this.terminated) {
/*     */         return;
/*     */       }
/*     */       
/* 357 */       if (fastEnter()) {
/* 358 */         UnicastProcessor<T> w = this.window;
/* 359 */         w.onNext(t);
/*     */         
/* 361 */         long c = this.count + 1L;
/*     */         
/* 363 */         if (c >= this.maxSize) {
/* 364 */           this.producerIndex++;
/* 365 */           this.count = 0L;
/*     */           
/* 367 */           w.onComplete();
/*     */           
/* 369 */           long r = requested();
/*     */           
/* 371 */           if (r != 0L) {
/* 372 */             w = UnicastProcessor.create(this.bufferSize);
/* 373 */             this.window = w;
/* 374 */             this.downstream.onNext(w);
/* 375 */             if (r != Long.MAX_VALUE) {
/* 376 */               produced(1L);
/*     */             }
/* 378 */             if (this.restartTimerOnMaxSize) {
/* 379 */               Disposable tm = (Disposable)this.timer.get();
/*     */               
/* 381 */               tm.dispose();
/* 382 */               Disposable task = this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit);
/*     */               
/* 384 */               this.timer.replace(task);
/*     */             } 
/*     */           } else {
/* 387 */             this.window = null;
/* 388 */             this.upstream.cancel();
/* 389 */             this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver window due to lack of requests"));
/* 390 */             disposeTimer();
/*     */             return;
/*     */           } 
/*     */         } else {
/* 394 */           this.count = c;
/*     */         } 
/*     */         
/* 397 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 401 */         this.queue.offer(NotificationLite.next(t));
/* 402 */         if (!enter()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 406 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 411 */       this.error = t;
/* 412 */       this.done = true;
/* 413 */       if (enter()) {
/* 414 */         drainLoop();
/*     */       }
/*     */       
/* 417 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 422 */       this.done = true;
/* 423 */       if (enter()) {
/* 424 */         drainLoop();
/*     */       }
/*     */       
/* 427 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 432 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 437 */       this.cancelled = true;
/*     */     }
/*     */     
/*     */     public void disposeTimer() {
/* 441 */       this.timer.dispose();
/* 442 */       Scheduler.Worker w = this.worker;
/* 443 */       if (w != null) {
/* 444 */         w.dispose();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop() {
/* 449 */       SimplePlainQueue<Object> q = this.queue;
/* 450 */       Subscriber<? super Flowable<T>> a = this.downstream;
/* 451 */       UnicastProcessor<T> w = this.window;
/*     */       
/* 453 */       int missed = 1;
/*     */ 
/*     */       
/*     */       while (true) {
/* 457 */         if (this.terminated) {
/* 458 */           this.upstream.cancel();
/* 459 */           q.clear();
/* 460 */           disposeTimer();
/*     */           
/*     */           return;
/*     */         } 
/* 464 */         boolean d = this.done;
/*     */         
/* 466 */         Object o = q.poll();
/*     */         
/* 468 */         boolean empty = (o == null);
/* 469 */         boolean isHolder = o instanceof ConsumerIndexHolder;
/*     */         
/* 471 */         if (d && (empty || isHolder)) {
/* 472 */           this.window = null;
/* 473 */           q.clear();
/* 474 */           Throwable err = this.error;
/* 475 */           if (err != null) {
/* 476 */             w.onError(err);
/*     */           } else {
/* 478 */             w.onComplete();
/*     */           } 
/* 480 */           disposeTimer();
/*     */           
/*     */           return;
/*     */         } 
/* 484 */         if (empty)
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 554 */           missed = leave(-missed);
/* 555 */           if (missed == 0)
/*     */             break;  continue; }  if (isHolder) { ConsumerIndexHolder consumerIndexHolder = (ConsumerIndexHolder)o; if (!this.restartTimerOnMaxSize || this.producerIndex == consumerIndexHolder.index) { w.onComplete(); this.count = 0L; w = UnicastProcessor.create(this.bufferSize); this.window = w; long r = requested(); if (r != 0L) { a.onNext(w); if (r != Long.MAX_VALUE)
/*     */                 produced(1L);  continue; }
/*     */              this.window = null; this.queue.clear(); this.upstream.cancel(); a.onError((Throwable)new MissingBackpressureException("Could not deliver first window due to lack of requests.")); disposeTimer(); return; }
/*     */            continue; }
/*     */          w.onNext(NotificationLite.getValue(o)); long c = this.count + 1L; if (c >= this.maxSize) { this.producerIndex++; this.count = 0L; w.onComplete(); long r = requested(); if (r != 0L) { w = UnicastProcessor.create(this.bufferSize); this.window = w; this.downstream.onNext(w); if (r != Long.MAX_VALUE)
/*     */               produced(1L);  if (this.restartTimerOnMaxSize) { Disposable tm = (Disposable)this.timer.get(); tm.dispose(); Disposable task = this.worker.schedulePeriodically(new ConsumerIndexHolder(this.producerIndex, this), this.timespan, this.timespan, this.unit); this.timer.replace(task); }
/*     */              continue; }
/*     */            this.window = null; this.upstream.cancel(); this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver window due to lack of requests")); disposeTimer(); return; }
/*     */          this.count = c;
/* 565 */       }  } static final class ConsumerIndexHolder implements Runnable { final long index; ConsumerIndexHolder(long index, FlowableWindowTimed.WindowExactBoundedSubscriber<?> parent) { this.index = index;
/* 566 */         this.parent = parent; }
/*     */       
/*     */       final FlowableWindowTimed.WindowExactBoundedSubscriber<?> parent;
/*     */       
/*     */       public void run() {
/* 571 */         FlowableWindowTimed.WindowExactBoundedSubscriber<?> p = this.parent;
/*     */         
/* 573 */         if (!p.cancelled) {
/* 574 */           p.queue.offer(this);
/*     */         } else {
/* 576 */           p.terminated = true;
/*     */         } 
/* 578 */         if (p.enter()) {
/* 579 */           p.drainLoop();
/*     */         }
/*     */       } }
/*     */      }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class WindowSkipSubscriber<T>
/*     */     extends QueueDrainSubscriber<T, Object, Flowable<T>>
/*     */     implements Subscription, Runnable
/*     */   {
/*     */     final long timespan;
/*     */     
/*     */     final long timeskip;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     final int bufferSize;
/*     */     final List<UnicastProcessor<T>> windows;
/*     */     Subscription upstream;
/*     */     volatile boolean terminated;
/*     */     
/*     */     WindowSkipSubscriber(Subscriber<? super Flowable<T>> actual, long timespan, long timeskip, TimeUnit unit, Scheduler.Worker worker, int bufferSize) {
/* 603 */       super(actual, (SimplePlainQueue)new MpscLinkedQueue());
/* 604 */       this.timespan = timespan;
/* 605 */       this.timeskip = timeskip;
/* 606 */       this.unit = unit;
/* 607 */       this.worker = worker;
/* 608 */       this.bufferSize = bufferSize;
/* 609 */       this.windows = new LinkedList<UnicastProcessor<T>>();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 614 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*     */         
/* 616 */         this.upstream = s;
/*     */         
/* 618 */         this.downstream.onSubscribe(this);
/*     */         
/* 620 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 624 */         long r = requested();
/* 625 */         if (r != 0L) {
/* 626 */           UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize);
/* 627 */           this.windows.add(w);
/*     */           
/* 629 */           this.downstream.onNext(w);
/* 630 */           if (r != Long.MAX_VALUE) {
/* 631 */             produced(1L);
/*     */           }
/* 633 */           this.worker.schedule(new Completion(w), this.timespan, this.unit);
/*     */           
/* 635 */           this.worker.schedulePeriodically(this, this.timeskip, this.timeskip, this.unit);
/*     */           
/* 637 */           s.request(Long.MAX_VALUE);
/*     */         } else {
/*     */           
/* 640 */           s.cancel();
/* 641 */           this.downstream.onError((Throwable)new MissingBackpressureException("Could not emit the first window due to lack of requests"));
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 648 */       if (fastEnter()) {
/* 649 */         for (UnicastProcessor<T> w : this.windows) {
/* 650 */           w.onNext(t);
/*     */         }
/* 652 */         if (leave(-1) == 0) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 656 */         this.queue.offer(t);
/* 657 */         if (!enter()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 661 */       drainLoop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 666 */       this.error = t;
/* 667 */       this.done = true;
/* 668 */       if (enter()) {
/* 669 */         drainLoop();
/*     */       }
/*     */       
/* 672 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 677 */       this.done = true;
/* 678 */       if (enter()) {
/* 679 */         drainLoop();
/*     */       }
/*     */       
/* 682 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 687 */       requested(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 692 */       this.cancelled = true;
/*     */     }
/*     */     
/*     */     void complete(UnicastProcessor<T> w) {
/* 696 */       this.queue.offer(new SubjectWork<T>(w, false));
/* 697 */       if (enter()) {
/* 698 */         drainLoop();
/*     */       }
/*     */     }
/*     */     
/*     */     void drainLoop()
/*     */     {
/* 704 */       SimplePlainQueue<Object> q = this.queue;
/* 705 */       Subscriber<? super Flowable<T>> a = this.downstream;
/* 706 */       List<UnicastProcessor<T>> ws = this.windows;
/*     */       
/* 708 */       int missed = 1;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 713 */         if (this.terminated) {
/* 714 */           this.upstream.cancel();
/* 715 */           q.clear();
/* 716 */           ws.clear();
/* 717 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 721 */         boolean d = this.done;
/*     */         
/* 723 */         Object v = q.poll();
/*     */         
/* 725 */         boolean empty = (v == null);
/* 726 */         boolean sw = v instanceof SubjectWork;
/*     */         
/* 728 */         if (d && (empty || sw)) {
/* 729 */           q.clear();
/* 730 */           Throwable e = this.error;
/* 731 */           if (e != null) {
/* 732 */             for (UnicastProcessor<T> w : ws) {
/* 733 */               w.onError(e);
/*     */             }
/*     */           } else {
/* 736 */             for (UnicastProcessor<T> w : ws) {
/* 737 */               w.onComplete();
/*     */             }
/*     */           } 
/* 740 */           ws.clear();
/* 741 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 745 */         if (empty)
/*     */         
/*     */         { 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 784 */           missed = leave(-missed);
/* 785 */           if (missed == 0)
/*     */             break;  continue; }  if (sw) { SubjectWork<T> work = (SubjectWork<T>)v; if (work.open) { if (this.cancelled)
/*     */               continue;  long r = requested(); if (r != 0L) { UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize); ws.add(w); a.onNext(w); if (r != Long.MAX_VALUE)
/*     */                 produced(1L);  this.worker.schedule(new Completion(w), this.timespan, this.unit); continue; }
/*     */              a.onError((Throwable)new MissingBackpressureException("Can't emit window due to lack of requests")); continue; }
/*     */            ws.remove(work.w); work.w.onComplete(); if (ws.isEmpty() && this.cancelled)
/*     */             this.terminated = true;  continue; }
/*     */          for (UnicastProcessor<T> w : ws)
/*     */           w.onNext(v); 
/* 794 */       }  } public void run() { UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize);
/*     */       
/* 796 */       SubjectWork<T> sw = new SubjectWork<T>(w, true);
/* 797 */       if (!this.cancelled) {
/* 798 */         this.queue.offer(sw);
/*     */       }
/* 800 */       if (enter())
/* 801 */         drainLoop();  }
/*     */ 
/*     */     
/*     */     static final class SubjectWork<T> {
/*     */       final UnicastProcessor<T> w;
/*     */       final boolean open;
/*     */       
/*     */       SubjectWork(UnicastProcessor<T> w, boolean open) {
/* 809 */         this.w = w;
/* 810 */         this.open = open;
/*     */       }
/*     */     }
/*     */     
/*     */     final class Completion implements Runnable {
/*     */       private final UnicastProcessor<T> processor;
/*     */       
/*     */       Completion(UnicastProcessor<T> processor) {
/* 818 */         this.processor = processor;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 823 */         FlowableWindowTimed.WindowSkipSubscriber.this.complete(this.processor);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableWindowTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */