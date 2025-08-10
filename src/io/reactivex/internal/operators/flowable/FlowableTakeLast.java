/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.internal.util.BackpressureHelper;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public final class FlowableTakeLast<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final int count;
/*     */   
/*     */   public FlowableTakeLast(Flowable<T> source, int count) {
/*  29 */     super(source);
/*  30 */     this.count = count;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  35 */     this.source.subscribe(new TakeLastSubscriber<T>(s, this.count));
/*     */   }
/*     */   
/*     */   static final class TakeLastSubscriber<T>
/*     */     extends ArrayDeque<T>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = 7240042530241604978L;
/*     */     final Subscriber<? super T> downstream;
/*     */     final int count;
/*     */     Subscription upstream;
/*     */     volatile boolean done;
/*     */     volatile boolean cancelled;
/*  48 */     final AtomicLong requested = new AtomicLong();
/*     */     
/*  50 */     final AtomicInteger wip = new AtomicInteger();
/*     */     
/*     */     TakeLastSubscriber(Subscriber<? super T> actual, int count) {
/*  53 */       this.downstream = actual;
/*  54 */       this.count = count;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  59 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  60 */         this.upstream = s;
/*  61 */         this.downstream.onSubscribe(this);
/*  62 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  68 */       if (this.count == size()) {
/*  69 */         poll();
/*     */       }
/*  71 */       offer(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  76 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  81 */       this.done = true;
/*  82 */       drain();
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*  87 */       if (SubscriptionHelper.validate(n)) {
/*  88 */         BackpressureHelper.add(this.requested, n);
/*  89 */         drain();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/*  95 */       this.cancelled = true;
/*  96 */       this.upstream.cancel();
/*     */     }
/*     */     
/*     */     void drain() {
/* 100 */       if (this.wip.getAndIncrement() == 0) {
/* 101 */         Subscriber<? super T> a = this.downstream;
/* 102 */         long r = this.requested.get();
/*     */         do {
/* 104 */           if (this.cancelled) {
/*     */             return;
/*     */           }
/* 107 */           if (!this.done)
/* 108 */             continue;  long e = 0L;
/*     */           
/* 110 */           while (e != r) {
/* 111 */             if (this.cancelled) {
/*     */               return;
/*     */             }
/* 114 */             T v = poll();
/* 115 */             if (v == null) {
/* 116 */               a.onComplete();
/*     */               return;
/*     */             } 
/* 119 */             a.onNext(v);
/* 120 */             e++;
/*     */           } 
/* 122 */           if (e == 0L || r == Long.MAX_VALUE)
/* 123 */             continue;  r = this.requested.addAndGet(-e);
/*     */         
/*     */         }
/* 126 */         while (this.wip.decrementAndGet() != 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTakeLast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */