/*    */ package io.reactivex.internal.subscriptions;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
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
/*    */ public final class BooleanSubscription
/*    */   extends AtomicBoolean
/*    */   implements Subscription
/*    */ {
/*    */   private static final long serialVersionUID = -8127758972444290902L;
/*    */   
/*    */   public void request(long n) {
/* 29 */     SubscriptionHelper.validate(n);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cancel() {
/* 34 */     lazySet(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 42 */     return get();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return "BooleanSubscription(cancelled=" + get() + ")";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/BooleanSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */