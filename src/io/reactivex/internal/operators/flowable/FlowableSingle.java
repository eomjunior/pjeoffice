/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class FlowableSingle<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final T defaultValue;
/*     */   final boolean failOnEmpty;
/*     */   
/*     */   public FlowableSingle(Flowable<T> source, T defaultValue, boolean failOnEmpty) {
/*  31 */     super(source);
/*  32 */     this.defaultValue = defaultValue;
/*  33 */     this.failOnEmpty = failOnEmpty;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  38 */     this.source.subscribe(new SingleElementSubscriber<T>(s, this.defaultValue, this.failOnEmpty));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SingleElementSubscriber<T>
/*     */     extends DeferredScalarSubscription<T>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -5526049321428043809L;
/*     */     
/*     */     final T defaultValue;
/*     */     
/*     */     final boolean failOnEmpty;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     SingleElementSubscriber(Subscriber<? super T> actual, T defaultValue, boolean failOnEmpty) {
/*  55 */       super(actual);
/*  56 */       this.defaultValue = defaultValue;
/*  57 */       this.failOnEmpty = failOnEmpty;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  62 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  63 */         this.upstream = s;
/*  64 */         this.downstream.onSubscribe((Subscription)this);
/*  65 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  71 */       if (this.done) {
/*     */         return;
/*     */       }
/*  74 */       if (this.value != null) {
/*  75 */         this.done = true;
/*  76 */         this.upstream.cancel();
/*  77 */         this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
/*     */         return;
/*     */       } 
/*  80 */       this.value = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  85 */       if (this.done) {
/*  86 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  89 */       this.done = true;
/*  90 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  95 */       if (this.done) {
/*     */         return;
/*     */       }
/*  98 */       this.done = true;
/*  99 */       T v = (T)this.value;
/* 100 */       this.value = null;
/* 101 */       if (v == null) {
/* 102 */         v = this.defaultValue;
/*     */       }
/* 104 */       if (v == null) {
/* 105 */         if (this.failOnEmpty) {
/* 106 */           this.downstream.onError(new NoSuchElementException());
/*     */         } else {
/* 108 */           this.downstream.onComplete();
/*     */         } 
/*     */       } else {
/* 111 */         complete(v);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 117 */       super.cancel();
/* 118 */       this.upstream.cancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */