/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.Single;
/*    */ import io.reactivex.SingleObserver;
/*    */ import io.reactivex.disposables.Disposable;
/*    */ import io.reactivex.internal.fuseable.FuseToFlowable;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import io.reactivex.plugins.RxJavaPlugins;
/*    */ import org.reactivestreams.Subscription;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class FlowableCountSingle<T>
/*    */   extends Single<Long>
/*    */   implements FuseToFlowable<Long>
/*    */ {
/*    */   final Flowable<T> source;
/*    */   
/*    */   public FlowableCountSingle(Flowable<T> source) {
/* 29 */     this.source = source;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(SingleObserver<? super Long> observer) {
/* 34 */     this.source.subscribe(new CountSubscriber(observer));
/*    */   }
/*    */ 
/*    */   
/*    */   public Flowable<Long> fuseToFlowable() {
/* 39 */     return RxJavaPlugins.onAssembly(new FlowableCount<T>(this.source));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class CountSubscriber
/*    */     implements FlowableSubscriber<Object>, Disposable
/*    */   {
/*    */     final SingleObserver<? super Long> downstream;
/*    */     Subscription upstream;
/*    */     long count;
/*    */     
/*    */     CountSubscriber(SingleObserver<? super Long> downstream) {
/* 51 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 56 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 57 */         this.upstream = s;
/* 58 */         this.downstream.onSubscribe(this);
/* 59 */         s.request(Long.MAX_VALUE);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(Object t) {
/* 65 */       this.count++;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 70 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 71 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 76 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/* 77 */       this.downstream.onSuccess(Long.valueOf(this.count));
/*    */     }
/*    */ 
/*    */     
/*    */     public void dispose() {
/* 82 */       this.upstream.cancel();
/* 83 */       this.upstream = (Subscription)SubscriptionHelper.CANCELLED;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean isDisposed() {
/* 88 */       return (this.upstream == SubscriptionHelper.CANCELLED);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableCountSingle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */