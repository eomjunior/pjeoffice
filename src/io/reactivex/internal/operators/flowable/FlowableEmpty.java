/*    */ package io.reactivex.internal.operators.flowable;
/*    */ 
/*    */ import io.reactivex.Flowable;
/*    */ import io.reactivex.internal.fuseable.ScalarCallable;
/*    */ import io.reactivex.internal.subscriptions.EmptySubscription;
/*    */ import org.reactivestreams.Subscriber;
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
/*    */ public final class FlowableEmpty
/*    */   extends Flowable<Object>
/*    */   implements ScalarCallable<Object>
/*    */ {
/* 27 */   public static final Flowable<Object> INSTANCE = new FlowableEmpty();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void subscribeActual(Subscriber<? super Object> s) {
/* 34 */     EmptySubscription.complete(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object call() {
/* 39 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/operators/flowable/FlowableEmpty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */