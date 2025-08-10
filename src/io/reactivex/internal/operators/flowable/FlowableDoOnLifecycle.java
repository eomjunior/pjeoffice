/*     */ package io.reactivex.internal.operators.flowable;
/*     */ 
/*     */ import io.reactivex.Flowable;
/*     */ import io.reactivex.FlowableSubscriber;
/*     */ import io.reactivex.exceptions.Exceptions;
/*     */ import io.reactivex.functions.Action;
/*     */ import io.reactivex.functions.Consumer;
/*     */ import io.reactivex.functions.LongConsumer;
/*     */ import io.reactivex.internal.subscriptions.EmptySubscription;
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
/*     */ public final class FlowableDoOnLifecycle<T>
/*     */   extends AbstractFlowableWithUpstream<T, T>
/*     */ {
/*     */   private final Consumer<? super Subscription> onSubscribe;
/*     */   private final LongConsumer onRequest;
/*     */   private final Action onCancel;
/*     */   
/*     */   public FlowableDoOnLifecycle(Flowable<T> source, Consumer<? super Subscription> onSubscribe, LongConsumer onRequest, Action onCancel) {
/*  30 */     super(source);
/*  31 */     this.onSubscribe = onSubscribe;
/*  32 */     this.onRequest = onRequest;
/*  33 */     this.onCancel = onCancel;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(Subscriber<? super T> s) {
/*  38 */     this.source.subscribe(new SubscriptionLambdaSubscriber<T>(s, this.onSubscribe, this.onRequest, this.onCancel));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class SubscriptionLambdaSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Subscription
/*     */   {
/*     */     final Subscriber<? super T> downstream;
/*     */     
/*     */     final Consumer<? super Subscription> onSubscribe;
/*     */     final LongConsumer onRequest;
/*     */     final Action onCancel;
/*     */     Subscription upstream;
/*     */     
/*     */     SubscriptionLambdaSubscriber(Subscriber<? super T> actual, Consumer<? super Subscription> onSubscribe, LongConsumer onRequest, Action onCancel) {
/*  53 */       this.downstream = actual;
/*  54 */       this.onSubscribe = onSubscribe;
/*  55 */       this.onCancel = onCancel;
/*  56 */       this.onRequest = onRequest;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*     */       try {
/*  63 */         this.onSubscribe.accept(s);
/*  64 */       } catch (Throwable e) {
/*  65 */         Exceptions.throwIfFatal(e);
/*  66 */         s.cancel();
/*  67 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  68 */         EmptySubscription.error(e, this.downstream);
/*     */         return;
/*     */       } 
/*  71 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  72 */         this.upstream = s;
/*  73 */         this.downstream.onSubscribe(this);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  79 */       this.downstream.onNext(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void onError(Throwable t) {
/*  84 */       if (this.upstream != SubscriptionHelper.CANCELLED) {
/*  85 */         this.downstream.onError(t);
/*     */       } else {
/*  87 */         RxJavaPlugins.onError(t);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onComplete() {
/*  93 */       if (this.upstream != SubscriptionHelper.CANCELLED) {
/*  94 */         this.downstream.onComplete();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void request(long n) {
/*     */       try {
/* 101 */         this.onRequest.accept(n);
/* 102 */       } catch (Throwable e) {
/* 103 */         Exceptions.throwIfFatal(e);
/* 104 */         RxJavaPlugins.onError(e);
/*     */       } 
/* 106 */       this.upstream.request(n);
/*     */     }
/*     */ 
/*     */     
/*     */     public void cancel() {
/* 111 */       Subscription s = this.upstream;
/* 112 */       if (s != SubscriptionHelper.CANCELLED) {
/* 113 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*     */         try {
/* 115 */           this.onCancel.run();
/* 116 */         } catch (Throwable e) {
/* 117 */           Exceptions.throwIfFatal(e);
/* 118 */           RxJavaPlugins.onError(e);
/*     */         } 
/* 120 */         s.cancel();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableDoOnLifecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */