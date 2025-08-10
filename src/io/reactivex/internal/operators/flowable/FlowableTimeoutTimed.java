/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionArbiter;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.ExceptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableTimeoutTimed<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   final Publisher<? extends T> other;
/*     */   
/*     */   public FlowableTimeoutTimed(Flowable<T> source, long timeout, TimeUnit unit, Scheduler scheduler, Publisher<? extends T> other) {
/*  36 */     super(source);
/*  37 */     this.timeout = timeout;
/*  38 */     this.unit = unit;
/*  39 */     this.scheduler = scheduler;
/*  40 */     this.other = other;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  45 */     if (this.other == null) {
/*  46 */       TimeoutSubscriber<T> parent = new TimeoutSubscriber<T>(s, this.timeout, this.unit, this.scheduler.createWorker());
/*  47 */       s.onSubscribe(parent);
/*  48 */       parent.startTimeout(0L);
/*  49 */       this.source.subscribe(parent);
/*     */     } else {
/*  51 */       TimeoutFallbackSubscriber<T> parent = new TimeoutFallbackSubscriber<T>(s, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
/*  52 */       s.onSubscribe((Subscription)parent);
/*  53 */       parent.startTimeout(0L);
/*  54 */       this.source.subscribe(parent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutSubscriber<T>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription, TimeoutSupport
/*     */   {
/*     */     private static final long serialVersionUID = 3764492702657003550L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     final SequentialDisposable task;
/*     */     final AtomicReference<Subscription> upstream;
/*     */     final AtomicLong requested;
/*     */     
/*     */     TimeoutSubscriber(Subscriber<? super T> actual, long timeout, TimeUnit unit, Scheduler.Worker worker) {
/*  78 */       this.downstream = actual;
/*  79 */       this.timeout = timeout;
/*  80 */       this.unit = unit;
/*  81 */       this.worker = worker;
/*  82 */       this.task = new SequentialDisposable();
/*  83 */       this.upstream = new AtomicReference<Subscription>();
/*  84 */       this.requested = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  89 */       SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  94 */       long idx = get();
/*  95 */       if (idx == Long.MAX_VALUE || !compareAndSet(idx, idx + 1L)) {
/*     */         return;
/*     */       }
/*     */       
/*  99 */       ((Disposable)this.task.get()).dispose();
/*     */       
/* 101 */       this.downstream.onNext(t);
/*     */       
/* 103 */       startTimeout(idx + 1L);
/*     */     }
/*     */     
/*     */     void startTimeout(long nextIndex) {
/* 107 */       this.task.replace(this.worker.schedule(new FlowableTimeoutTimed.TimeoutTask(nextIndex, this), this.timeout, this.unit));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 112 */       if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 113 */         this.task.dispose();
/*     */         
/* 115 */         this.downstream.onError(t);
/*     */         
/* 117 */         this.worker.dispose();
/*     */       } else {
/* 119 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 125 */       if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 126 */         this.task.dispose();
/*     */         
/* 128 */         this.downstream.onComplete();
/*     */         
/* 130 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(long idx) {
/* 136 */       if (compareAndSet(idx, Long.MAX_VALUE)) {
/* 137 */         SubscriptionHelper.cancel(this.upstream);
/*     */         
/* 139 */         this.downstream.onError(new TimeoutException(ExceptionHelper.timeoutMessage(this.timeout, this.unit)));
/*     */         
/* 141 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 147 */       SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 152 */       SubscriptionHelper.cancel(this.upstream);
/* 153 */       this.worker.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class TimeoutTask
/*     */     implements Runnable
/*     */   {
/*     */     final FlowableTimeoutTimed.TimeoutSupport parent;
/*     */     final long idx;
/*     */     
/*     */     TimeoutTask(long idx, FlowableTimeoutTimed.TimeoutSupport parent) {
/* 164 */       this.idx = idx;
/* 165 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 170 */       this.parent.onTimeout(this.idx);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TimeoutFallbackSubscriber<T>
/*     */     extends SubscriptionArbiter
/*     */     implements FlowableSubscriber<T>, TimeoutSupport
/*     */   {
/*     */     private static final long serialVersionUID = 3764492702657003550L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final long timeout;
/*     */     
/*     */     final TimeUnit unit;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     final SequentialDisposable task;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicLong index;
/*     */     
/*     */     long consumed;
/*     */     Publisher<? extends T> fallback;
/*     */     
/*     */     TimeoutFallbackSubscriber(Subscriber<? super T> actual, long timeout, TimeUnit unit, Scheduler.Worker worker, Publisher<? extends T> fallback) {
/* 199 */       super(true);
/* 200 */       this.downstream = actual;
/* 201 */       this.timeout = timeout;
/* 202 */       this.unit = unit;
/* 203 */       this.worker = worker;
/* 204 */       this.fallback = fallback;
/* 205 */       this.task = new SequentialDisposable();
/* 206 */       this.upstream = new AtomicReference<Subscription>();
/* 207 */       this.index = new AtomicLong();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 212 */       if (SubscriptionHelper.setOnce(this.upstream, s)) {
/* 213 */         setSubscription(s);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 219 */       long idx = this.index.get();
/* 220 */       if (idx == Long.MAX_VALUE || !this.index.compareAndSet(idx, idx + 1L)) {
/*     */         return;
/*     */       }
/*     */       
/* 224 */       ((Disposable)this.task.get()).dispose();
/*     */       
/* 226 */       this.consumed++;
/*     */       
/* 228 */       this.downstream.onNext(t);
/*     */       
/* 230 */       startTimeout(idx + 1L);
/*     */     }
/*     */     
/*     */     void startTimeout(long nextIndex) {
/* 234 */       this.task.replace(this.worker.schedule(new FlowableTimeoutTimed.TimeoutTask(nextIndex, this), this.timeout, this.unit));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 239 */       if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 240 */         this.task.dispose();
/*     */         
/* 242 */         this.downstream.onError(t);
/*     */         
/* 244 */         this.worker.dispose();
/*     */       } else {
/* 246 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 252 */       if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
/* 253 */         this.task.dispose();
/*     */         
/* 255 */         this.downstream.onComplete();
/*     */         
/* 257 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onTimeout(long idx) {
/* 263 */       if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
/* 264 */         SubscriptionHelper.cancel(this.upstream);
/*     */         
/* 266 */         long c = this.consumed;
/* 267 */         if (c != 0L) {
/* 268 */           produced(c);
/*     */         }
/*     */         
/* 271 */         Publisher<? extends T> f = this.fallback;
/* 272 */         this.fallback = null;
/*     */         
/* 274 */         f.subscribe((Subscriber)new FlowableTimeoutTimed.FallbackSubscriber<T>(this.downstream, this));
/*     */         
/* 276 */         this.worker.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 282 */       super.cancel();
/* 283 */       this.worker.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class FallbackSubscriber<T>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     final SubscriptionArbiter arbiter;
/*     */     
/*     */     FallbackSubscriber(Subscriber<? super T> actual, SubscriptionArbiter arbiter) {
/* 294 */       this.downstream = actual;
/* 295 */       this.arbiter = arbiter;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/* 300 */       this.arbiter.setSubscription(s);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/* 305 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 310 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 315 */       this.downstream.onComplete();
/*     */     }
/*     */   }
/*     */   
/*     */   static interface TimeoutSupport {
/*     */     void onTimeout(long param1Long);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTimeoutTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */