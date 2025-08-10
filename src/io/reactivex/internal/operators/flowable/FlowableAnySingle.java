/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Predicate;
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
/*     */ public final class FlowableAnySingle<T>
/*     */   extends Single<Boolean>
/*     */   implements FuseToFlowable<Boolean>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public FlowableAnySingle(Flowable<T> source, Predicate<? super T> predicate) {
/*  31 */     this.source = source;
/*  32 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super Boolean> observer) {
/*  37 */     this.source.subscribe(new AnySubscriber<T>(observer, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<Boolean> fuseToFlowable() {
/*  42 */     return RxJavaPlugins.onAssembly(new FlowableAny<T>(this.source, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AnySubscriber<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super Boolean> downstream;
/*     */     
/*     */     final Predicate<? super T> predicate;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     AnySubscriber(SingleObserver<? super Boolean> actual, Predicate<? super T> predicate) {
/*  56 */       this.downstream = actual;
/*  57 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  62 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  63 */         this.upstream = s;
/*  64 */         this.downstream.onSubscribe(this);
/*  65 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  71 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  76 */         b = this.predicate.test(t);
/*  77 */       } catch (Throwable e) {
/*  78 */         Exceptions.throwIfFatal(e);
/*  79 */         this.upstream.cancel();
/*  80 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  81 */         onError(e);
/*     */         return;
/*     */       } 
/*  84 */       if (b) {
/*  85 */         this.done = true;
/*  86 */         this.upstream.cancel();
/*  87 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  88 */         this.downstream.onSuccess(Boolean.valueOf(true));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  94 */       if (this.done) {
/*  95 */         RxJavaPlugins.onError(t);
/*     */         
/*     */         return;
/*     */       } 
/*  99 */       this.done = true;
/* 100 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 101 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 106 */       if (!this.done) {
/* 107 */         this.done = true;
/* 108 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 109 */         this.downstream.onSuccess(Boolean.valueOf(false));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 115 */       this.upstream.cancel();
/* 116 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 121 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableAnySingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */