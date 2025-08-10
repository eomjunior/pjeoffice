/*    */ package io.reactivex.internal.subscriptions;
/*    */ 
/*    */ import io.reactivex.internal.fuseable.QueueSubscription;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public abstract class BasicQueueSubscription<T>
/*    */   extends AtomicLong
/*    */   implements QueueSubscription<T>
/*    */ {
/*    */   private static final long serialVersionUID = -6671519529404341862L;
/*    */   
/*    */   public final boolean offer(T e) {
/* 31 */     throw new UnsupportedOperationException("Should not be called!");
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean offer(T v1, T v2) {
/* 36 */     throw new UnsupportedOperationException("Should not be called!");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/subscriptions/BasicQueueSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */