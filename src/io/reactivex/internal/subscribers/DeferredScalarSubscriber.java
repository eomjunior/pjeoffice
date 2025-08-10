/*    */ package io.reactivex.internal.subscribers;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DeferredScalarSubscriber<T, R>
/*    */   extends DeferredScalarSubscription<R>
/*    */   implements FlowableSubscriber<T>
/*    */ {
/*    */   private static final long serialVersionUID = 2984505488220891551L;
/*    */   protected Subscription upstream;
/*    */   protected boolean hasValue;
/*    */   
/*    */   public DeferredScalarSubscriber(Subscriber<? super R> downstream) {
/* 43 */     super(downstream);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onSubscribe(Subscription s) {
/* 48 */     if (SubscriptionHelper.validate(this.upstream, s)) {
/* 49 */       this.upstream = s;
/*    */       
/* 51 */       this.downstream.onSubscribe((Subscription)this);
/*    */       
/* 53 */       s.request(Long.MAX_VALUE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onError(Throwable t) {
/* 59 */     this.value = null;
/* 60 */     this.downstream.onError(t);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onComplete() {
/* 65 */     if (this.hasValue) {
/* 66 */       complete(this.value);
/*    */     } else {
/* 68 */       this.downstream.onComplete();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 74 */     super.cancel();
/* 75 */     this.upstream.cancel();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscribers/DeferredScalarSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */