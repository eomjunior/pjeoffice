/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
/*    */ import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
/*    */ import io.reactivex.internal.subscriptions.SubscriptionHelper;
/*    */ import org.reactivestreams.Subscriber;
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
/*    */ 
/*    */ public final class FlowableCount<T>
/*    */   extends AbstractFlowableWithUpstream<T, Long>
/*    */ {
/*    */   public FlowableCount(Flowable<T> source) {
/* 24 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super Long> s) {
/* 29 */     this.source.subscribe(new CountSubscriber(s));
/*    */   }
/*    */ 
/*    */   
/*    */   static final class CountSubscriber
/*    */     extends DeferredScalarSubscription<Long>
/*    */     implements FlowableSubscriber<Object>
/*    */   {
/*    */     private static final long serialVersionUID = 4973004223787171406L;
/*    */     Subscription upstream;
/*    */     long count;
/*    */     
/*    */     CountSubscriber(Subscriber<? super Long> downstream) {
/* 42 */       super(downstream);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 47 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 48 */         this.upstream = s;
/* 49 */         this.downstream.onSubscribe((Subscription)this);
/* 50 */         s.request(Long.MAX_VALUE);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(Object t) {
/* 56 */       this.count++;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 61 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 66 */       complete(Long.valueOf(this.count));
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 71 */       super.cancel();
/* 72 */       this.upstream.cancel();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableCount.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */