/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public final class FlowableSingleSingle<T>
/*     */   extends Single<T>
/*     */   implements FuseToFlowable<T>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final T defaultValue;
/*     */   
/*     */   public FlowableSingleSingle(Flowable<T> source, T defaultValue) {
/*  33 */     this.source = source;
/*  34 */     this.defaultValue = defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  39 */     this.source.subscribe(new SingleElementSubscriber<T>(observer, this.defaultValue));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<T> fuseToFlowable() {
/*  44 */     return RxJavaPlugins.onAssembly(new FlowableSingle<T>(this.source, this.defaultValue, true));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SingleElementSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     final T defaultValue;
/*     */     
/*     */     Subscription upstream;
/*     */     
/*     */     boolean done;
/*     */     T value;
/*     */     
/*     */     SingleElementSubscriber(SingleObserver<? super T> actual, T defaultValue) {
/*  61 */       this.downstream = actual;
/*  62 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  67 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  68 */         this.upstream = s;
/*  69 */         this.downstream.onSubscribe(this);
/*  70 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  76 */       if (this.done) {
/*     */         return;
/*     */       }
/*  79 */       if (this.value != null) {
/*  80 */         this.done = true;
/*  81 */         this.upstream.cancel();
/*  82 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  83 */         this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
/*     */         return;
/*     */       } 
/*  86 */       this.value = t;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  91 */       if (this.done) {
/*  92 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  95 */       this.done = true;
/*  96 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  97 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 102 */       if (this.done) {
/*     */         return;
/*     */       }
/* 105 */       this.done = true;
/* 106 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 107 */       T v = this.value;
/* 108 */       this.value = null;
/* 109 */       if (v == null) {
/* 110 */         v = this.defaultValue;
/*     */       }
/*     */       
/* 113 */       if (v != null) {
/* 114 */         this.downstream.onSuccess(v);
/*     */       } else {
/* 116 */         this.downstream.onError(new NoSuchElementException());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 122 */       this.upstream.cancel();
/* 123 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 128 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableSingleSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */