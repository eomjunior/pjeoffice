/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*     */ public final class FlowableTake<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final long limit;
/*     */   
/*     */   public FlowableTake(Flowable<T> source, long limit) {
/*  27 */     super(source);
/*  28 */     this.limit = limit;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  33 */     this.source.subscribe(new TakeSubscriber<T>(s, this.limit));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class TakeSubscriber<T>
/*     */     extends AtomicBoolean
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     private static final long serialVersionUID = -5636543848937116287L;
/*     */     
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final long limit;
/*     */     boolean done;
/*     */     Subscription upstream;
/*     */     long remaining;
/*     */     
/*     */     TakeSubscriber(Subscriber<? super T> actual, long limit) {
/*  51 */       this.downstream = actual;
/*  52 */       this.limit = limit;
/*  53 */       this.remaining = limit;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  58 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  59 */         this.upstream = s;
/*  60 */         if (this.limit == 0L) {
/*  61 */           s.cancel();
/*  62 */           this.done = true;
/*  63 */           EmptySubscription.complete(this.downstream);
/*     */         } else {
/*  65 */           this.downstream.onSubscribe(this);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  72 */       if (!this.done && this.remaining-- > 0L) {
/*  73 */         boolean stop = (this.remaining == 0L);
/*  74 */         this.downstream.onNext(t);
/*  75 */         if (stop) {
/*  76 */           this.upstream.cancel();
/*  77 */           onComplete();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  84 */       if (!this.done) {
/*  85 */         this.done = true;
/*  86 */         this.upstream.cancel();
/*  87 */         this.downstream.onError(t);
/*     */       } else {
/*  89 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  95 */       if (!this.done) {
/*  96 */         this.done = true;
/*  97 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/* 103 */       if (!SubscriptionHelper.validate(n)) {
/*     */         return;
/*     */       }
/* 106 */       if (!get() && compareAndSet(false, true) && 
/* 107 */         n >= this.limit) {
/* 108 */         this.upstream.request(Long.MAX_VALUE);
/*     */         
/*     */         return;
/*     */       } 
/* 112 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 117 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTake.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */