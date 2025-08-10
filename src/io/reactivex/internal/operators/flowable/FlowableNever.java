/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.internal.subscriptions.EmptySubscription;
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
/*    */ public final class FlowableNever
/*    */   extends Flowable<Object>
/*    */ {
/* 21 */   public static final Flowable<Object> INSTANCE = new FlowableNever();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void subscribeActual(Subscriber<? super Object> s) {
/* 28 */     s.onSubscribe((Subscription)EmptySubscription.INSTANCE);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableNever.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */