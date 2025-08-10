/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.queue.SpscLinkedArrayQueue;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.processors.UnicastProcessor;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.reactivestreams.Processor;
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
/*     */ public final class FlowableWindow<T>
/*     */   extends AbstractFlowableWithUpstream<T, Flowable<T>>
/*     */ {
/*     */   final long size;
/*     */   final long skip;
/*     */   final int bufferSize;
/*     */   
/*     */   public FlowableWindow(Flowable<T> source, long size, long skip, int bufferSize) {
/*  36 */     super(source);
/*  37 */     this.size = size;
/*  38 */     this.skip = skip;
/*  39 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super Flowable<T>> s) {
/*  44 */     if (this.skip == this.size) {
/*  45 */       this.source.subscribe(new WindowExactSubscriber<T>(s, this.size, this.bufferSize));
/*     */     }
/*  47 */     else if (this.skip > this.size) {
/*  48 */       this.source.subscribe(new WindowSkipSubscriber<T>(s, this.size, this.skip, this.bufferSize));
/*     */     } else {
/*  50 */       this.source.subscribe(new WindowOverlapSubscriber<T>(s, this.size, this.skip, this.bufferSize));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowExactSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -2365647875069161133L;
/*     */     
/*     */     final Subscriber<? super Flowable<T>> downstream;
/*     */     
/*     */     final long size;
/*     */     
/*     */     final AtomicBoolean once;
/*     */     
/*     */     final int bufferSize;
/*     */     
/*     */     long index;
/*     */     
/*     */     Subscription upstream;
/*     */     UnicastProcessor<T> window;
/*     */     
/*     */     WindowExactSubscriber(Subscriber<? super Flowable<T>> actual, long size, int bufferSize) {
/*  75 */       super(1);
/*  76 */       this.downstream = actual;
/*  77 */       this.size = size;
/*  78 */       this.once = new AtomicBoolean();
/*  79 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  84 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  85 */         this.upstream = s;
/*  86 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  92 */       long i = this.index;
/*     */       
/*  94 */       UnicastProcessor<T> w = this.window;
/*  95 */       if (i == 0L) {
/*  96 */         getAndIncrement();
/*     */         
/*  98 */         w = UnicastProcessor.create(this.bufferSize, this);
/*  99 */         this.window = w;
/*     */         
/* 101 */         this.downstream.onNext(w);
/*     */       } 
/*     */       
/* 104 */       i++;
/*     */       
/* 106 */       w.onNext(t);
/*     */       
/* 108 */       if (i == this.size) {
/* 109 */         this.index = 0L;
/* 110 */         this.window = null;
/* 111 */         w.onComplete();
/*     */       } else {
/* 113 */         this.index = i;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 119 */       UnicastProcessor<T> unicastProcessor = this.window;
/* 120 */       if (unicastProcessor != null) {
/* 121 */         this.window = null;
/* 122 */         unicastProcessor.onError(t);
/*     */       } 
/*     */       
/* 125 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 130 */       UnicastProcessor<T> unicastProcessor = this.window;
/* 131 */       if (unicastProcessor != null) {
/* 132 */         this.window = null;
/* 133 */         unicastProcessor.onComplete();
/*     */       } 
/*     */       
/* 136 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 141 */       if (SubscriptionHelper.validate(n)) {
/* 142 */         long u = BackpressureHelper.multiplyCap(this.size, n);
/* 143 */         this.upstream.request(u);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 149 */       if (this.once.compareAndSet(false, true)) {
/* 150 */         run();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 156 */       if (decrementAndGet() == 0) {
/* 157 */         this.upstream.cancel();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowSkipSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -8792836352386833856L;
/*     */     
/*     */     final Subscriber<? super Flowable<T>> downstream;
/*     */     
/*     */     final long size;
/*     */     
/*     */     final long skip;
/*     */     
/*     */     final AtomicBoolean once;
/*     */     
/*     */     final AtomicBoolean firstRequest;
/*     */     
/*     */     final int bufferSize;
/*     */     
/*     */     long index;
/*     */     
/*     */     Subscription upstream;
/*     */     UnicastProcessor<T> window;
/*     */     
/*     */     WindowSkipSubscriber(Subscriber<? super Flowable<T>> actual, long size, long skip, int bufferSize) {
/* 187 */       super(1);
/* 188 */       this.downstream = actual;
/* 189 */       this.size = size;
/* 190 */       this.skip = skip;
/* 191 */       this.once = new AtomicBoolean();
/* 192 */       this.firstRequest = new AtomicBoolean();
/* 193 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 198 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 199 */         this.upstream = s;
/* 200 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 206 */       long i = this.index;
/*     */       
/* 208 */       UnicastProcessor<T> w = this.window;
/* 209 */       if (i == 0L) {
/* 210 */         getAndIncrement();
/*     */         
/* 212 */         w = UnicastProcessor.create(this.bufferSize, this);
/* 213 */         this.window = w;
/*     */         
/* 215 */         this.downstream.onNext(w);
/*     */       } 
/*     */       
/* 218 */       i++;
/*     */       
/* 220 */       if (w != null) {
/* 221 */         w.onNext(t);
/*     */       }
/*     */       
/* 224 */       if (i == this.size) {
/* 225 */         this.window = null;
/* 226 */         w.onComplete();
/*     */       } 
/*     */       
/* 229 */       if (i == this.skip) {
/* 230 */         this.index = 0L;
/*     */       } else {
/* 232 */         this.index = i;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 238 */       UnicastProcessor<T> unicastProcessor = this.window;
/* 239 */       if (unicastProcessor != null) {
/* 240 */         this.window = null;
/* 241 */         unicastProcessor.onError(t);
/*     */       } 
/*     */       
/* 244 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 249 */       UnicastProcessor<T> unicastProcessor = this.window;
/* 250 */       if (unicastProcessor != null) {
/* 251 */         this.window = null;
/* 252 */         unicastProcessor.onComplete();
/*     */       } 
/*     */       
/* 255 */       this.downstream.onComplete();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 260 */       if (SubscriptionHelper.validate(n)) {
/* 261 */         if (!this.firstRequest.get() && this.firstRequest.compareAndSet(false, true)) {
/* 262 */           long u = BackpressureHelper.multiplyCap(this.size, n);
/* 263 */           long v = BackpressureHelper.multiplyCap(this.skip - this.size, n - 1L);
/* 264 */           long w = BackpressureHelper.addCap(u, v);
/* 265 */           this.upstream.request(w);
/*     */         } else {
/* 267 */           long u = BackpressureHelper.multiplyCap(this.skip, n);
/* 268 */           this.upstream.request(u);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 275 */       if (this.once.compareAndSet(false, true)) {
/* 276 */         run();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 282 */       if (decrementAndGet() == 0) {
/* 283 */         this.upstream.cancel();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class WindowOverlapSubscriber<T>
/*     */     extends AtomicInteger
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 2428527070996323976L;
/*     */     
/*     */     final Subscriber<? super Flowable<T>> downstream;
/*     */     
/*     */     final SpscLinkedArrayQueue<UnicastProcessor<T>> queue;
/*     */     
/*     */     final long size;
/*     */     
/*     */     final long skip;
/*     */     
/*     */     final ArrayDeque<UnicastProcessor<T>> windows;
/*     */     
/*     */     final AtomicBoolean once;
/*     */     
/*     */     final AtomicBoolean firstRequest;
/*     */     
/*     */     final AtomicLong requested;
/*     */     
/*     */     final AtomicInteger wip;
/*     */     
/*     */     final int bufferSize;
/*     */     
/*     */     long index;
/*     */     
/*     */     long produced;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     volatile boolean done;
/*     */     Throwable error;
/*     */     volatile boolean cancelled;
/*     */     
/*     */     WindowOverlapSubscriber(Subscriber<? super Flowable<T>> actual, long size, long skip, int bufferSize) {
/* 326 */       super(1);
/* 327 */       this.downstream = actual;
/* 328 */       this.size = size;
/* 329 */       this.skip = skip;
/* 330 */       this.queue = new SpscLinkedArrayQueue(bufferSize);
/* 331 */       this.windows = new ArrayDeque<UnicastProcessor<T>>();
/* 332 */       this.once = new AtomicBoolean();
/* 333 */       this.firstRequest = new AtomicBoolean();
/* 334 */       this.requested = new AtomicLong();
/* 335 */       this.wip = new AtomicInteger();
/* 336 */       this.bufferSize = bufferSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 341 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 342 */         this.upstream = s;
/* 343 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 349 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 353 */       long i = this.index;
/*     */       
/* 355 */       if (i == 0L && 
/* 356 */         !this.cancelled) {
/* 357 */         getAndIncrement();
/*     */         
/* 359 */         UnicastProcessor<T> w = UnicastProcessor.create(this.bufferSize, this);
/*     */         
/* 361 */         this.windows.offer(w);
/*     */         
/* 363 */         this.queue.offer(w);
/* 364 */         drain();
/*     */       } 
/*     */ 
/*     */       
/* 368 */       i++;
/*     */       
/* 370 */       for (Processor<T, T> w : this.windows) {
/* 371 */         w.onNext(t);
/*     */       }
/*     */       
/* 374 */       long p = this.produced + 1L;
/* 375 */       if (p == this.size) {
/* 376 */         this.produced = p - this.skip;
/*     */         
/* 378 */         Processor<T, T> w = (Processor<T, T>)this.windows.poll();
/* 379 */         if (w != null) {
/* 380 */           w.onComplete();
/*     */         }
/*     */       } else {
/* 383 */         this.produced = p;
/*     */       } 
/*     */       
/* 386 */       if (i == this.skip) {
/* 387 */         this.index = 0L;
/*     */       } else {
/* 389 */         this.index = i;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 395 */       if (this.done) {
/* 396 */         RxJavaPlugins.onError(t);
/*     */         
/*     */         return;
/*     */       } 
/* 400 */       for (Processor<T, T> w : this.windows) {
/* 401 */         w.onError(t);
/*     */       }
/* 403 */       this.windows.clear();
/*     */       
/* 405 */       this.error = t;
/* 406 */       this.done = true;
/* 407 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 412 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/* 416 */       for (Processor<T, T> w : this.windows) {
/* 417 */         w.onComplete();
/*     */       }
/* 419 */       this.windows.clear();
/*     */       
/* 421 */       this.done = true;
/* 422 */       drain();
/*     */     }
/*     */     
/*     */     void drain() {
/* 426 */       if (this.wip.getAndIncrement() != 0) {
/*     */         return;
/*     */       }
/*     */       
/* 430 */       Subscriber<? super Flowable<T>> a = this.downstream;
/* 431 */       SpscLinkedArrayQueue<UnicastProcessor<T>> q = this.queue;
/* 432 */       int missed = 1;
/*     */ 
/*     */       
/*     */       do {
/* 436 */         long r = this.requested.get();
/* 437 */         long e = 0L;
/*     */         
/* 439 */         while (e != r) {
/* 440 */           boolean d = this.done;
/*     */           
/* 442 */           UnicastProcessor<T> t = (UnicastProcessor<T>)q.poll();
/*     */           
/* 444 */           boolean empty = (t == null);
/*     */           
/* 446 */           if (checkTerminated(d, empty, a, q)) {
/*     */             return;
/*     */           }
/*     */           
/* 450 */           if (empty) {
/*     */             break;
/*     */           }
/*     */           
/* 454 */           a.onNext(t);
/*     */           
/* 456 */           e++;
/*     */         } 
/*     */         
/* 459 */         if (e == r && 
/* 460 */           checkTerminated(this.done, q.isEmpty(), a, q)) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 465 */         if (e != 0L && r != Long.MAX_VALUE) {
/* 466 */           this.requested.addAndGet(-e);
/*     */         }
/*     */         
/* 469 */         missed = this.wip.addAndGet(-missed);
/* 470 */       } while (missed != 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean checkTerminated(boolean d, boolean empty, Subscriber<?> a, SpscLinkedArrayQueue<?> q) {
/* 477 */       if (this.cancelled) {
/* 478 */         q.clear();
/* 479 */         return true;
/*     */       } 
/*     */       
/* 482 */       if (d) {
/* 483 */         Throwable e = this.error;
/*     */         
/* 485 */         if (e != null) {
/* 486 */           q.clear();
/* 487 */           a.onError(e);
/* 488 */           return true;
/*     */         } 
/* 490 */         if (empty) {
/* 491 */           a.onComplete();
/* 492 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 496 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 501 */       if (SubscriptionHelper.validate(n)) {
/* 502 */         BackpressureHelper.add(this.requested, n);
/*     */         
/* 504 */         if (!this.firstRequest.get() && this.firstRequest.compareAndSet(false, true)) {
/* 505 */           long u = BackpressureHelper.multiplyCap(this.skip, n - 1L);
/* 506 */           long v = BackpressureHelper.addCap(this.size, u);
/* 507 */           this.upstream.request(v);
/*     */         } else {
/* 509 */           long u = BackpressureHelper.multiplyCap(this.skip, n);
/* 510 */           this.upstream.request(u);
/*     */         } 
/*     */         
/* 513 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 519 */       this.cancelled = true;
/* 520 */       if (this.once.compareAndSet(false, true)) {
/* 521 */         run();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 527 */       if (decrementAndGet() == 0)
/* 528 */         this.upstream.cancel(); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */