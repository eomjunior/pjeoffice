/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.annotations.Nullable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.fuseable.ConditionalSubscriber;
/*     */ import io.reactivex.internal.fuseable.QueueSubscription;
/*     */ import io.reactivex.internal.fuseable.SimpleQueue;
/*     */ import io.reactivex.internal.queue.SpscArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ public final class FlowableObserveOn<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Scheduler scheduler;
/*     */   final boolean delayError;
/*     */   final int prefetch;
/*     */   
/*     */   public FlowableObserveOn(Flowable<T> source, Scheduler scheduler, boolean delayError, int prefetch) {
/*  42 */     super(source);
/*  43 */     this.scheduler = scheduler;
/*  44 */     this.delayError = delayError;
/*  45 */     this.prefetch = prefetch;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*  50 */     Scheduler.Worker worker = this.scheduler.createWorker();
/*     */     
/*  52 */     if (s instanceof ConditionalSubscriber) {
/*  53 */       this.source.subscribe(new ObserveOnConditionalSubscriber((ConditionalSubscriber)s, worker, this.delayError, this.prefetch));
/*     */     } else {
/*     */       
/*  56 */       this.source.subscribe(new ObserveOnSubscriber<T>(s, worker, this.delayError, this.prefetch));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class BaseObserveOnSubscriber<T>
/*     */     extends BasicIntQueueSubscription<T>
/*     */     implements FlowableSubscriber<T>, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -8241002408341274697L;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     final boolean delayError;
/*     */     
/*     */     final int prefetch;
/*     */     
/*     */     final int limit;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     SimpleQueue<T> queue;
/*     */     
/*     */     volatile boolean cancelled;
/*     */     
/*     */     volatile boolean done;
/*     */     
/*     */     Throwable error;
/*     */     
/*     */     int sourceMode;
/*     */     
/*     */     long produced;
/*     */     
/*     */     boolean outputFused;
/*     */ 
/*     */     
/*     */     BaseObserveOnSubscriber(Scheduler.Worker worker, boolean delayError, int prefetch) {
/*  95 */       this.worker = worker;
/*  96 */       this.delayError = delayError;
/*  97 */       this.prefetch = prefetch;
/*  98 */       this.requested = new AtomicLong();
/*  99 */       this.limit = prefetch - (prefetch >> 2);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onNext(T t) {
/* 104 */       if (this.done) {
/*     */         return;
/*     */       }
/* 107 */       if (this.sourceMode == 2) {
/* 108 */         trySchedule();
/*     */         return;
/*     */       } 
/* 111 */       if (!this.queue.offer(t)) {
/* 112 */         this.upstream.cancel();
/*     */         
/* 114 */         this.error = (Throwable)new MissingBackpressureException("Queue is full?!");
/* 115 */         this.done = true;
/*     */       } 
/* 117 */       trySchedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onError(Throwable t) {
/* 122 */       if (this.done) {
/* 123 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 126 */       this.error = t;
/* 127 */       this.done = true;
/* 128 */       trySchedule();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void onComplete() {
/* 133 */       if (!this.done) {
/* 134 */         this.done = true;
/* 135 */         trySchedule();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public final void request(long n) {
/* 141 */       if (SubscriptionHelper.validate(n)) {
/* 142 */         BackpressureHelper.add(this.requested, n);
/* 143 */         trySchedule();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public final void cancel() {
/* 149 */       if (this.cancelled) {
/*     */         return;
/*     */       }
/*     */       
/* 153 */       this.cancelled = true;
/* 154 */       this.upstream.cancel();
/* 155 */       this.worker.dispose();
/*     */       
/* 157 */       if (!this.outputFused && getAndIncrement() == 0) {
/* 158 */         this.queue.clear();
/*     */       }
/*     */     }
/*     */     
/*     */     final void trySchedule() {
/* 163 */       if (getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/* 166 */       this.worker.schedule(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void run() {
/* 171 */       if (this.outputFused) {
/* 172 */         runBackfused();
/* 173 */       } else if (this.sourceMode == 1) {
/* 174 */         runSync();
/*     */       } else {
/* 176 */         runAsync();
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void runBackfused();
/*     */     
/*     */     abstract void runSync();
/*     */     
/*     */     abstract void runAsync();
/*     */     
/*     */     final boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a) {
/* 187 */       if (this.cancelled) {
/* 188 */         clear();
/* 189 */         return true;
/*     */       } 
/* 191 */       if (d) {
/* 192 */         if (this.delayError) {
/* 193 */           if (empty) {
/* 194 */             this.cancelled = true;
/* 195 */             Throwable e = this.error;
/* 196 */             if (e != null) {
/* 197 */               a.onError(e);
/*     */             } else {
/* 199 */               a.onComplete();
/*     */             } 
/* 201 */             this.worker.dispose();
/* 202 */             return true;
/*     */           } 
/*     */         } else {
/* 205 */           Throwable e = this.error;
/* 206 */           if (e != null) {
/* 207 */             this.cancelled = true;
/* 208 */             clear();
/* 209 */             a.onError(e);
/* 210 */             this.worker.dispose();
/* 211 */             return true;
/*     */           } 
/* 213 */           if (empty) {
/* 214 */             this.cancelled = true;
/* 215 */             a.onComplete();
/* 216 */             this.worker.dispose();
/* 217 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 222 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public final int requestFusion(int requestedMode) {
/* 227 */       if ((requestedMode & 0x2) != 0) {
/* 228 */         this.outputFused = true;
/* 229 */         return 2;
/*     */       } 
/* 231 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public final void clear() {
/* 236 */       this.queue.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isEmpty() {
/* 241 */       return this.queue.isEmpty();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ObserveOnSubscriber<T>
/*     */     extends BaseObserveOnSubscriber<T>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -4547113800637756442L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */ 
/*     */     
/*     */     ObserveOnSubscriber(Subscriber<? super T> actual, Scheduler.Worker worker, boolean delayError, int prefetch) {
/* 257 */       super(worker, delayError, prefetch);
/* 258 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 263 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 264 */         this.upstream = s;
/*     */         
/* 266 */         if (s instanceof QueueSubscription) {
/*     */           
/* 268 */           QueueSubscription<T> f = (QueueSubscription<T>)s;
/*     */           
/* 270 */           int m = f.requestFusion(7);
/*     */           
/* 272 */           if (m == 1) {
/* 273 */             this.sourceMode = 1;
/* 274 */             this.queue = (SimpleQueue<T>)f;
/* 275 */             this.done = true;
/*     */             
/* 277 */             this.downstream.onSubscribe((Subscription)this);
/*     */             return;
/*     */           } 
/* 280 */           if (m == 2) {
/* 281 */             this.sourceMode = 2;
/* 282 */             this.queue = (SimpleQueue<T>)f;
/*     */             
/* 284 */             this.downstream.onSubscribe((Subscription)this);
/*     */             
/* 286 */             s.request(this.prefetch);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/* 292 */         this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
/*     */         
/* 294 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/* 296 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void runSync() {
/* 302 */       int missed = 1;
/*     */       
/* 304 */       Subscriber<? super T> a = this.downstream;
/* 305 */       SimpleQueue<T> q = this.queue;
/*     */       
/* 307 */       long e = this.produced;
/*     */ 
/*     */       
/*     */       while (true) {
/* 311 */         long r = this.requested.get();
/*     */         
/* 313 */         while (e != r) {
/*     */           T v;
/*     */           
/*     */           try {
/* 317 */             v = (T)q.poll();
/* 318 */           } catch (Throwable ex) {
/* 319 */             Exceptions.throwIfFatal(ex);
/* 320 */             this.cancelled = true;
/* 321 */             this.upstream.cancel();
/* 322 */             a.onError(ex);
/* 323 */             this.worker.dispose();
/*     */             
/*     */             return;
/*     */           } 
/* 327 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/* 330 */           if (v == null) {
/* 331 */             this.cancelled = true;
/* 332 */             a.onComplete();
/* 333 */             this.worker.dispose();
/*     */             
/*     */             return;
/*     */           } 
/* 337 */           a.onNext(v);
/*     */           
/* 339 */           e++;
/*     */         } 
/*     */         
/* 342 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 346 */         if (q.isEmpty()) {
/* 347 */           this.cancelled = true;
/* 348 */           a.onComplete();
/* 349 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 353 */         int w = get();
/* 354 */         if (missed == w) {
/* 355 */           this.produced = e;
/* 356 */           missed = addAndGet(-missed);
/* 357 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 361 */         missed = w;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void runAsync() {
/* 368 */       int missed = 1;
/*     */       
/* 370 */       Subscriber<? super T> a = this.downstream;
/* 371 */       SimpleQueue<T> q = this.queue;
/*     */       
/* 373 */       long e = this.produced;
/*     */ 
/*     */       
/*     */       while (true) {
/* 377 */         long r = this.requested.get();
/*     */         
/* 379 */         while (e != r) {
/* 380 */           T v; boolean d = this.done;
/*     */ 
/*     */           
/*     */           try {
/* 384 */             v = (T)q.poll();
/* 385 */           } catch (Throwable ex) {
/* 386 */             Exceptions.throwIfFatal(ex);
/*     */             
/* 388 */             this.cancelled = true;
/* 389 */             this.upstream.cancel();
/* 390 */             q.clear();
/*     */             
/* 392 */             a.onError(ex);
/* 393 */             this.worker.dispose();
/*     */             
/*     */             return;
/*     */           } 
/* 397 */           boolean empty = (v == null);
/*     */           
/* 399 */           if (checkTerminated(d, empty, a)) {
/*     */             return;
/*     */           }
/*     */           
/* 403 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 407 */           a.onNext(v);
/*     */           
/* 409 */           e++;
/* 410 */           if (e == this.limit) {
/* 411 */             if (r != Long.MAX_VALUE) {
/* 412 */               r = this.requested.addAndGet(-e);
/*     */             }
/* 414 */             this.upstream.request(e);
/* 415 */             e = 0L;
/*     */           } 
/*     */         } 
/*     */         
/* 419 */         if (e == r && checkTerminated(this.done, q.isEmpty(), a)) {
/*     */           return;
/*     */         }
/*     */         
/* 423 */         int w = get();
/* 424 */         if (missed == w) {
/* 425 */           this.produced = e;
/* 426 */           missed = addAndGet(-missed);
/* 427 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 431 */         missed = w;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void runBackfused() {
/* 438 */       int missed = 1;
/*     */ 
/*     */       
/*     */       do {
/* 442 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 446 */         boolean d = this.done;
/*     */         
/* 448 */         this.downstream.onNext(null);
/*     */         
/* 450 */         if (d) {
/* 451 */           this.cancelled = true;
/* 452 */           Throwable e = this.error;
/* 453 */           if (e != null) {
/* 454 */             this.downstream.onError(e);
/*     */           } else {
/* 456 */             this.downstream.onComplete();
/*     */           } 
/* 458 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 462 */         missed = addAndGet(-missed);
/* 463 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 472 */       T v = (T)this.queue.poll();
/* 473 */       if (v != null && this.sourceMode != 1) {
/* 474 */         long p = this.produced + 1L;
/* 475 */         if (p == this.limit) {
/* 476 */           this.produced = 0L;
/* 477 */           this.upstream.request(p);
/*     */         } else {
/* 479 */           this.produced = p;
/*     */         } 
/*     */       } 
/* 482 */       return v;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class ObserveOnConditionalSubscriber<T>
/*     */     extends BaseObserveOnSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = 644624475404284533L;
/*     */ 
/*     */     
/*     */     final ConditionalSubscriber<? super T> downstream;
/*     */ 
/*     */     
/*     */     long consumed;
/*     */ 
/*     */     
/*     */     ObserveOnConditionalSubscriber(ConditionalSubscriber<? super T> actual, Scheduler.Worker worker, boolean delayError, int prefetch) {
/* 501 */       super(worker, delayError, prefetch);
/* 502 */       this.downstream = actual;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 507 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 508 */         this.upstream = s;
/*     */         
/* 510 */         if (s instanceof QueueSubscription) {
/*     */           
/* 512 */           QueueSubscription<T> f = (QueueSubscription<T>)s;
/*     */           
/* 514 */           int m = f.requestFusion(7);
/*     */           
/* 516 */           if (m == 1) {
/* 517 */             this.sourceMode = 1;
/* 518 */             this.queue = (SimpleQueue<T>)f;
/* 519 */             this.done = true;
/*     */             
/* 521 */             this.downstream.onSubscribe((Subscription)this);
/*     */             return;
/*     */           } 
/* 524 */           if (m == 2) {
/* 525 */             this.sourceMode = 2;
/* 526 */             this.queue = (SimpleQueue<T>)f;
/*     */             
/* 528 */             this.downstream.onSubscribe((Subscription)this);
/*     */             
/* 530 */             s.request(this.prefetch);
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         
/* 536 */         this.queue = (SimpleQueue<T>)new SpscArrayQueue(this.prefetch);
/*     */         
/* 538 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/* 540 */         s.request(this.prefetch);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void runSync() {
/* 546 */       int missed = 1;
/*     */       
/* 548 */       ConditionalSubscriber<? super T> a = this.downstream;
/* 549 */       SimpleQueue<T> q = this.queue;
/*     */       
/* 551 */       long e = this.produced;
/*     */ 
/*     */       
/*     */       while (true) {
/* 555 */         long r = this.requested.get();
/*     */         
/* 557 */         while (e != r) {
/*     */           T v;
/*     */           try {
/* 560 */             v = (T)q.poll();
/* 561 */           } catch (Throwable ex) {
/* 562 */             Exceptions.throwIfFatal(ex);
/* 563 */             this.cancelled = true;
/* 564 */             this.upstream.cancel();
/* 565 */             a.onError(ex);
/* 566 */             this.worker.dispose();
/*     */             
/*     */             return;
/*     */           } 
/* 570 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/* 573 */           if (v == null) {
/* 574 */             this.cancelled = true;
/* 575 */             a.onComplete();
/* 576 */             this.worker.dispose();
/*     */             
/*     */             return;
/*     */           } 
/* 580 */           if (a.tryOnNext(v)) {
/* 581 */             e++;
/*     */           }
/*     */         } 
/*     */         
/* 585 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 589 */         if (q.isEmpty()) {
/* 590 */           this.cancelled = true;
/* 591 */           a.onComplete();
/* 592 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 596 */         int w = get();
/* 597 */         if (missed == w) {
/* 598 */           this.produced = e;
/* 599 */           missed = addAndGet(-missed);
/* 600 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 604 */         missed = w;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void runAsync() {
/* 611 */       int missed = 1;
/*     */       
/* 613 */       ConditionalSubscriber<? super T> a = this.downstream;
/* 614 */       SimpleQueue<T> q = this.queue;
/*     */       
/* 616 */       long emitted = this.produced;
/* 617 */       long polled = this.consumed;
/*     */ 
/*     */       
/*     */       while (true) {
/* 621 */         long r = this.requested.get();
/*     */         
/* 623 */         while (emitted != r) {
/* 624 */           T v; boolean d = this.done;
/*     */           
/*     */           try {
/* 627 */             v = (T)q.poll();
/* 628 */           } catch (Throwable ex) {
/* 629 */             Exceptions.throwIfFatal(ex);
/*     */             
/* 631 */             this.cancelled = true;
/* 632 */             this.upstream.cancel();
/* 633 */             q.clear();
/*     */             
/* 635 */             a.onError(ex);
/* 636 */             this.worker.dispose();
/*     */             return;
/*     */           } 
/* 639 */           boolean empty = (v == null);
/*     */           
/* 641 */           if (checkTerminated(d, empty, (Subscriber<?>)a)) {
/*     */             return;
/*     */           }
/*     */           
/* 645 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 649 */           if (a.tryOnNext(v)) {
/* 650 */             emitted++;
/*     */           }
/*     */           
/* 653 */           polled++;
/*     */           
/* 655 */           if (polled == this.limit) {
/* 656 */             this.upstream.request(polled);
/* 657 */             polled = 0L;
/*     */           } 
/*     */         } 
/*     */         
/* 661 */         if (emitted == r && checkTerminated(this.done, q.isEmpty(), (Subscriber<?>)a)) {
/*     */           return;
/*     */         }
/*     */         
/* 665 */         int w = get();
/* 666 */         if (missed == w) {
/* 667 */           this.produced = emitted;
/* 668 */           this.consumed = polled;
/* 669 */           missed = addAndGet(-missed);
/* 670 */           if (missed == 0)
/*     */             break; 
/*     */           continue;
/*     */         } 
/* 674 */         missed = w;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void runBackfused() {
/* 682 */       int missed = 1;
/*     */ 
/*     */       
/*     */       do {
/* 686 */         if (this.cancelled) {
/*     */           return;
/*     */         }
/*     */         
/* 690 */         boolean d = this.done;
/*     */         
/* 692 */         this.downstream.onNext(null);
/*     */         
/* 694 */         if (d) {
/* 695 */           this.cancelled = true;
/* 696 */           Throwable e = this.error;
/* 697 */           if (e != null) {
/* 698 */             this.downstream.onError(e);
/*     */           } else {
/* 700 */             this.downstream.onComplete();
/*     */           } 
/* 702 */           this.worker.dispose();
/*     */           
/*     */           return;
/*     */         } 
/* 706 */         missed = addAndGet(-missed);
/* 707 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public T poll() throws Exception {
/* 716 */       T v = (T)this.queue.poll();
/* 717 */       if (v != null && this.sourceMode != 1) {
/* 718 */         long p = this.consumed + 1L;
/* 719 */         if (p == this.limit) {
/* 720 */           this.consumed = 0L;
/* 721 */           this.upstream.request(p);
/*     */         } else {
/* 723 */           this.consumed = p;
/*     */         } 
/*     */       } 
/* 726 */       return v;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableObserveOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */