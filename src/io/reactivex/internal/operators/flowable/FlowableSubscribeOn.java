/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Scheduler;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FlowableSubscribeOn<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final Scheduler scheduler;
/*     */   final boolean nonScheduledRequests;
/*     */   
/*     */   public FlowableSubscribeOn(Flowable<T> source, Scheduler scheduler, boolean nonScheduledRequests) {
/*  37 */     super(source);
/*  38 */     this.scheduler = scheduler;
/*  39 */     this.nonScheduledRequests = nonScheduledRequests;
/*     */   }
/*     */ 
/*     */   
/*     */   public void subscribeActual(Subscriber<? super T> s) {
/*  44 */     Scheduler.Worker w = this.scheduler.createWorker();
/*  45 */     SubscribeOnSubscriber<T> sos = new SubscribeOnSubscriber<T>(s, w, (Publisher<T>)this.source, this.nonScheduledRequests);
/*  46 */     s.onSubscribe(sos);
/*     */     
/*  48 */     w.schedule(sos);
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SubscribeOnSubscriber<T>
/*     */     extends AtomicReference<Thread>
/*     */     implements FlowableSubscriber<T>, Subscription, Runnable
/*     */   {
/*     */     private static final long serialVersionUID = 8094547886072529208L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final Scheduler.Worker worker;
/*     */     
/*     */     final AtomicReference<Subscription> upstream;
/*     */     
/*     */     final AtomicLong requested;
/*     */     final boolean nonScheduledRequests;
/*     */     Publisher<T> source;
/*     */     
/*     */     SubscribeOnSubscriber(Subscriber<? super T> actual, Scheduler.Worker worker, Publisher<T> source, boolean requestOn) {
/*  69 */       this.downstream = actual;
/*  70 */       this.worker = worker;
/*  71 */       this.source = source;
/*  72 */       this.upstream = new AtomicReference<Subscription>();
/*  73 */       this.requested = new AtomicLong();
/*  74 */       this.nonScheduledRequests = !requestOn;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  79 */       lazySet(Thread.currentThread());
/*  80 */       Publisher<T> src = this.source;
/*  81 */       this.source = null;
/*  82 */       src.subscribe((Subscriber)this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  87 */       if (SubscriptionHelper.setOnce(this.upstream, s)) {
/*  88 */         long r = this.requested.getAndSet(0L);
/*  89 */         if (r != 0L) {
/*  90 */           requestUpstream(r, s);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  97 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/* 102 */       this.downstream.onError(t);
/* 103 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 108 */       this.downstream.onComplete();
/* 109 */       this.worker.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 114 */       if (SubscriptionHelper.validate(n)) {
/* 115 */         Subscription s = this.upstream.get();
/* 116 */         if (s != null) {
/* 117 */           requestUpstream(n, s);
/*     */         } else {
/* 119 */           BackpressureHelper.add(this.requested, n);
/* 120 */           s = this.upstream.get();
/* 121 */           if (s != null) {
/* 122 */             long r = this.requested.getAndSet(0L);
/* 123 */             if (r != 0L) {
/* 124 */               requestUpstream(r, s);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void requestUpstream(long n, Subscription s) {
/* 132 */       if (this.nonScheduledRequests || Thread.currentThread() == get()) {
/* 133 */         s.request(n);
/*     */       } else {
/* 135 */         this.worker.schedule(new Request(s, n));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 141 */       SubscriptionHelper.cancel(this.upstream);
/* 142 */       this.worker.dispose();
/*     */     }
/*     */     
/*     */     static final class Request implements Runnable {
/*     */       final Subscription upstream;
/*     */       final long n;
/*     */       
/*     */       Request(Subscription s, long n) {
/* 150 */         this.upstream = s;
/* 151 */         this.n = n;
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/* 156 */         this.upstream.request(this.n);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSubscribeOn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */