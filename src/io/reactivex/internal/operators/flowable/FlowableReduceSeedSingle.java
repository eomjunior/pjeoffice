/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.Single;
/*     */ import io.reactivex.SingleObserver;
/*     */ import io.reactivex.disposables.Disposable;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ 
/*     */ 
/*     */ public final class FlowableReduceSeedSingle<T, R>
/*     */   extends Single<R>
/*     */ {
/*     */   final Publisher<T> source;
/*     */   final R seed;
/*     */   final BiFunction<R, ? super T, R> reducer;
/*     */   
/*     */   public FlowableReduceSeedSingle(Publisher<T> source, R seed, BiFunction<R, ? super T, R> reducer) {
/*  42 */     this.source = source;
/*  43 */     this.seed = seed;
/*  44 */     this.reducer = reducer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super R> observer) {
/*  49 */     this.source.subscribe((Subscriber)new ReduceSeedObserver<T, R>(observer, this.reducer, this.seed));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ReduceSeedObserver<T, R>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super R> downstream;
/*     */     
/*     */     final BiFunction<R, ? super T, R> reducer;
/*     */     R value;
/*     */     Subscription upstream;
/*     */     
/*     */     ReduceSeedObserver(SingleObserver<? super R> actual, BiFunction<R, ? super T, R> reducer, R value) {
/*  63 */       this.downstream = actual;
/*  64 */       this.value = value;
/*  65 */       this.reducer = reducer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  70 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  71 */         this.upstream = s;
/*     */         
/*  73 */         this.downstream.onSubscribe(this);
/*     */         
/*  75 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T value) {
/*  81 */       R v = this.value;
/*  82 */       if (v != null) {
/*     */         try {
/*  84 */           this.value = (R)ObjectHelper.requireNonNull(this.reducer.apply(v, value), "The reducer returned a null value");
/*  85 */         } catch (Throwable ex) {
/*  86 */           Exceptions.throwIfFatal(ex);
/*  87 */           this.upstream.cancel();
/*  88 */           onError(ex);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable e) {
/*  95 */       if (this.value != null) {
/*  96 */         this.value = null;
/*  97 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  98 */         this.downstream.onError(e);
/*     */       } else {
/* 100 */         RxJavaPlugins.onError(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 106 */       R v = this.value;
/* 107 */       if (v != null) {
/* 108 */         this.value = null;
/* 109 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 110 */         this.downstream.onSuccess(v);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void dispose() {
/* 116 */       this.upstream.cancel();
/* 117 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDisposed() {
/* 122 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableReduceSeedSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */