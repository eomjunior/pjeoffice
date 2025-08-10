/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Maybe;
/*     */ import io.reactivex.MaybeObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableSingleMaybe<T>
/*     */   extends Maybe<T>
/*     */   implements FuseToFlowable<T>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   
/*     */   public FlowableSingleMaybe(Flowable<T> source) {
/*  29 */     this.source = source;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(MaybeObserver<? super T> observer) {
/*  34 */     this.source.subscribe(new SingleElementSubscriber<T>(observer));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<T> fuseToFlowable() {
/*  39 */     return RxJavaPlugins.onAssembly(new FlowableSingle<T>(this.source, null, false));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SingleElementSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final MaybeObserver<? super T> downstream;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     boolean done;
/*     */     T value;
/*     */     
/*     */     SingleElementSubscriber(MaybeObserver<? super T> downstream) {
/*  54 */       this.downstream = downstream;
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
/*  68 */       if (this.done) {
/*     */         return;
/*     */       }
/*  71 */       if (this.value != null) {
/*  72 */         this.done = true;
/*  73 */         this.upstream.cancel();
/*  74 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  75 */         this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
/*     */         return;
/*     */       } 
/*  78 */       this.value = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  83 */       if (this.done) {
/*  84 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  87 */       this.done = true;
/*  88 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  89 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  94 */       if (this.done) {
/*     */         return;
/*     */       }
/*  97 */       this.done = true;
/*  98 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  99 */       T v = this.value;
/* 100 */       this.value = null;
/* 101 */       if (v == null) {
/* 102 */         this.downstream.onComplete();
/*     */       } else {
/* 104 */         this.downstream.onSuccess(v);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 110 */       this.upstream.cancel();
/* 111 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 116 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSingleMaybe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */