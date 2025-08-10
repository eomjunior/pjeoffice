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
/*    */ public final class FlowableTakeLastOne<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   public FlowableTakeLastOne(Flowable<T> source) {
/* 23 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 28 */     this.source.subscribe(new TakeLastOneSubscriber<T>(s));
/*    */   }
/*    */   
/*    */   static final class TakeLastOneSubscriber<T>
/*    */     extends DeferredScalarSubscription<T>
/*    */     implements FlowableSubscriber<T>
/*    */   {
/*    */     private static final long serialVersionUID = -5467847744262967226L;
/*    */     Subscription upstream;
/*    */     
/*    */     TakeLastOneSubscriber(Subscriber<? super T> downstream) {
/* 39 */       super(downstream);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 44 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 45 */         this.upstream = s;
/* 46 */         this.downstream.onSubscribe((Subscription)this);
/* 47 */         s.request(Long.MAX_VALUE);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 53 */       this.value = t;
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 58 */       this.value = null;
/* 59 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 64 */       T v = (T)this.value;
/* 65 */       if (v != null) {
/* 66 */         complete(v);
/*    */       } else {
/* 68 */         this.downstream.onComplete();
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 74 */       super.cancel();
/* 75 */       this.upstream.cancel();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableTakeLastOne.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */