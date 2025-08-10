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
/*     */ 
/*     */ public final class FlowableAllSingle<T>
/*     */   extends Single<Boolean>
/*     */   implements FuseToFlowable<Boolean>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final Predicate<? super T> predicate;
/*     */   
/*     */   public FlowableAllSingle(Flowable<T> source, Predicate<? super T> predicate) {
/*  32 */     this.source = source;
/*  33 */     this.predicate = predicate;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super Boolean> observer) {
/*  38 */     this.source.subscribe(new AllSubscriber<T>(observer, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<Boolean> fuseToFlowable() {
/*  43 */     return RxJavaPlugins.onAssembly(new FlowableAll<T>(this.source, this.predicate));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class AllSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super Boolean> downstream;
/*     */     
/*     */     final Predicate<? super T> predicate;
/*     */     Subscription upstream;
/*     */     boolean done;
/*     */     
/*     */     AllSubscriber(SingleObserver<? super Boolean> actual, Predicate<? super T> predicate) {
/*  57 */       this.downstream = actual;
/*  58 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  63 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  64 */         this.upstream = s;
/*  65 */         this.downstream.onSubscribe(this);
/*  66 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onNext(T t) {
/*     */       boolean b;
/*  72 */       if (this.done) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/*  77 */         b = this.predicate.test(t);
/*  78 */       } catch (Throwable e) {
/*  79 */         Exceptions.throwIfFatal(e);
/*  80 */         this.upstream.cancel();
/*  81 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  82 */         onError(e);
/*     */         return;
/*     */       } 
/*  85 */       if (!b) {
/*  86 */         this.done = true;
/*  87 */         this.upstream.cancel();
/*  88 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  89 */         this.downstream.onSuccess(Boolean.valueOf(false));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  95 */       if (this.done) {
/*  96 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  99 */       this.done = true;
/* 100 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 101 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 106 */       if (this.done) {
/*     */         return;
/*     */       }
/* 109 */       this.done = true;
/* 110 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */       
/* 112 */       this.downstream.onSuccess(Boolean.valueOf(true));
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 117 */       this.upstream.cancel();
/* 118 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 123 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableAllSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */