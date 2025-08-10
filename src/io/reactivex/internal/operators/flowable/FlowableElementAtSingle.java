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
/*     */ public final class FlowableElementAtSingle<T>
/*     */   extends Single<T>
/*     */   implements FuseToFlowable<T>
/*     */ {
/*     */   final Flowable<T> source;
/*     */   final long index;
/*     */   final T defaultValue;
/*     */   
/*     */   public FlowableElementAtSingle(Flowable<T> source, long index, T defaultValue) {
/*  34 */     this.source = source;
/*  35 */     this.index = index;
/*  36 */     this.defaultValue = defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void subscribeActual(SingleObserver<? super T> observer) {
/*  41 */     this.source.subscribe(new ElementAtSubscriber<T>(observer, this.index, this.defaultValue));
/*     */   }
/*     */ 
/*     */   
/*     */   public Flowable<T> fuseToFlowable() {
/*  46 */     return RxJavaPlugins.onAssembly(new FlowableElementAt<T>(this.source, this.index, this.defaultValue, true));
/*     */   }
/*     */ 
/*     */   
/*     */   static final class ElementAtSubscriber<T>
/*     */     implements FlowableSubscriber<T>, Disposable
/*     */   {
/*     */     final SingleObserver<? super T> downstream;
/*     */     
/*     */     final long index;
/*     */     
/*     */     final T defaultValue;
/*     */     Subscription upstream;
/*     */     long count;
/*     */     boolean done;
/*     */     
/*     */     ElementAtSubscriber(SingleObserver<? super T> actual, long index, T defaultValue) {
/*  63 */       this.downstream = actual;
/*  64 */       this.index = index;
/*  65 */       this.defaultValue = defaultValue;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onSubscribe(Subscription s) {
/*  70 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/*  71 */         this.upstream = s;
/*  72 */         this.downstream.onSubscribe(this);
/*  73 */         s.request(Long.MAX_VALUE);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onNext(T t) {
/*  79 */       if (this.done) {
/*     */         return;
/*     */       }
/*  82 */       long c = this.count;
/*  83 */       if (c == this.index) {
/*  84 */         this.done = true;
/*  85 */         this.upstream.cancel();
/*  86 */         this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*  87 */         this.downstream.onSuccess(t);
/*     */         return;
/*     */       } 
/*  90 */       this.count = c + 1L;
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
/* 106 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 107 */       if (!this.done) {
/* 108 */         this.done = true;
/*     */         
/* 110 */         T v = this.defaultValue;
/*     */         
/* 112 */         if (v != null) {
/* 113 */           this.downstream.onSuccess(v);
/*     */         } else {
/* 115 */           this.downstream.onError(new NoSuchElementException());
/*     */         } 
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


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableElementAtSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */