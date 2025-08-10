/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.FlowableSubscriber;
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
/*    */ public final class FlowableHide<T>
/*    */   extends AbstractFlowableWithUpstream<T, T>
/*    */ {
/*    */   public FlowableHide(Flowable<T> source) {
/* 30 */     super(source);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void subscribeActual(Subscriber<? super T> s) {
/* 35 */     this.source.subscribe(new HideSubscriber<T>(s));
/*    */   }
/*    */   
/*    */   static final class HideSubscriber<T>
/*    */     implements FlowableSubscriber<T>, Subscription
/*    */   {
/*    */     final Subscriber<? super T> downstream;
/*    */     Subscription upstream;
/*    */     
/*    */     HideSubscriber(Subscriber<? super T> downstream) {
/* 45 */       this.downstream = downstream;
/*    */     }
/*    */ 
/*    */     
/*    */     public void request(long n) {
/* 50 */       this.upstream.request(n);
/*    */     }
/*    */ 
/*    */     
/*    */     public void cancel() {
/* 55 */       this.upstream.cancel();
/*    */     }
/*    */ 
/*    */     
/*    */     public void onSubscribe(Subscription s) {
/* 60 */       if (SubscriptionHelper.validate(this.upstream, s)) {
/* 61 */         this.upstream = s;
/* 62 */         this.downstream.onSubscribe(this);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/*    */     public void onNext(T t) {
/* 68 */       this.downstream.onNext(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onError(Throwable t) {
/* 73 */       this.downstream.onError(t);
/*    */     }
/*    */ 
/*    */     
/*    */     public void onComplete() {
/* 78 */       this.downstream.onComplete();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableHide.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */