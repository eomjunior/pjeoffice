/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.MissingBackpressureException;
/*     */ import io.reactivex.internal.disposables.SequentialDisposable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import io.reactivex.subscribers.SerializedSubscriber;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public final class FlowableThrottleFirstTimed<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long timeout;
/*     */   final TimeUnit unit;
/*     */   final Scheduler scheduler;
/*     */   
/*     */   public FlowableThrottleFirstTimed(Flowable<T> source, long timeout, TimeUnit unit, Scheduler scheduler) {
/*  37 */     super(source);
/*  38 */     this.timeout = timeout;
/*  39 */     this.unit = unit;
/*  40 */     this.scheduler = scheduler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  45 */     this.source.subscribe(new DebounceTimedSubscriber((Subscriber<?>)new SerializedSubscriber(s), this.timeout, this.unit, this.scheduler
/*     */           
/*  47 */           .createWorker()));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class DebounceTimedSubscriber<T>
/*     */     extends AtomicLong
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = -9102637559663639004L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     final long timeout;
/*     */     final TimeUnit unit;
/*     */     final Scheduler.Worker worker;
/*     */     Subscription upstream;
/*  62 */     final SequentialDisposable timer = new SequentialDisposable();
/*     */     
/*     */     volatile boolean gate;
/*     */     
/*     */     boolean done;
/*     */     
/*     */     DebounceTimedSubscriber(Subscriber<? super T> actual, long timeout, TimeUnit unit, Scheduler.Worker worker) {
/*  69 */       this.downstream = actual;
/*  70 */       this.timeout = timeout;
/*  71 */       this.unit = unit;
/*  72 */       this.worker = worker;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  77 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  78 */         this.upstream = s;
/*  79 */         this.downstream.onSubscribe(this);
/*  80 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  86 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*  90 */       if (!this.gate) {
/*  91 */         this.gate = true;
/*  92 */         long r = get();
/*  93 */         if (r != 0L) {
/*  94 */           this.downstream.onNext(t);
/*  95 */           BackpressureHelper.produced(this, 1L);
/*     */         } else {
/*  97 */           this.done = true;
/*  98 */           cancel();
/*  99 */           this.downstream.onError((Throwable)new MissingBackpressureException("Could not deliver value due to lack of requests"));
/*     */           
/*     */           return;
/*     */         } 
/* 103 */         Disposable d = (Disposable)this.timer.get();
/* 104 */         if (d != null) {
/* 105 */           d.dispose();
/*     */         }
/*     */         
/* 108 */         this.timer.replace(this.worker.schedule(this, this.timeout, this.unit));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 114 */       this.gate = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 119 */       if (this.done) {
/* 120 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/* 123 */       this.done = true;
/* 124 */       this.downstream.onError(t);
/* 125 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 130 */       if (this.done) {
/*     */         return;
/*     */       }
/* 133 */       this.done = true;
/* 134 */       this.downstream.onComplete();
/* 135 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 140 */       if (SubscriptionHelper.validate(n)) {
/* 141 */         BackpressureHelper.add(this, n);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 147 */       this.upstream.cancel();
/* 148 */       this.worker.dispose();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableThrottleFirstTimed.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */