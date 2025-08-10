/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.BiFunction;
/*     */ import io.reactivex.internal.functions.ObjectHelper;
/*     */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*     */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*     */ import io.reactivex.plugins.RxJavaPlugins;
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
/*     */ public final class FlowableReduce<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   final BiFunction<T, T, T> reducer;
/*     */   
/*     */   public FlowableReduce(Flowable<T> source, BiFunction<T, T, T> reducer) {
/*  36 */     super(source);
/*  37 */     this.reducer = reducer;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  42 */     this.source.subscribe(new ReduceSubscriber<T>(s, this.reducer));
/*     */   }
/*     */   
/*     */   static final class ReduceSubscriber<T>
/*     */     extends DeferredScalarSubscription<T>
/*     */     implements FlowableSubscriber<T>
/*     */   {
/*     */     private static final long serialVersionUID = -4663883003264602070L;
/*     */     final BiFunction<T, T, T> reducer;
/*     */     Subscription upstream;
/*     */     
/*     */     ReduceSubscriber(Subscriber<? super T> actual, BiFunction<T, T, T> reducer) {
/*  54 */       super(actual);
/*  55 */       this.reducer = reducer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  60 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  61 */         this.upstream = s;
/*     */         
/*  63 */         this.downstream.onSubscribe((Subscription)this);
/*     */         
/*  65 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  71 */       if (this.upstream == SubscriptionHelper.CANCELLED) {
/*     */         return;
/*     */       }
/*     */       
/*  75 */       T v = (T)this.value;
/*  76 */       if (v == null) {
/*  77 */         this.value = t;
/*     */       } else {
/*     */         try {
/*  80 */           this.value = ObjectHelper.requireNonNull(this.reducer.apply(v, t), "The reducer returned a null value");
/*  81 */         } catch (Throwable ex) {
/*  82 */           Exceptions.throwIfFatal(ex);
/*  83 */           this.upstream.cancel();
/*  84 */           onError(ex);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  91 */       if (this.upstream == SubscriptionHelper.CANCELLED) {
/*  92 */         RxJavaPlugins.onError(t);
/*     */         return;
/*     */       } 
/*  95 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  96 */       this.downstream.onError(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/* 101 */       if (this.upstream == SubscriptionHelper.CANCELLED) {
/*     */         return;
/*     */       }
/* 104 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */       
/* 106 */       T v = (T)this.value;
/* 107 */       if (v != null) {
/* 108 */         complete(v);
/*     */       } else {
/* 110 */         this.downstream.onComplete();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 116 */       super.cancel();
/* 117 */       this.upstream.cancel();
/* 118 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableReduce.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */