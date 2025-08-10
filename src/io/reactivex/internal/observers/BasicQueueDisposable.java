/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.internal.fuseable.QueueDisposable;
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
/*    */ public abstract class BasicQueueDisposable<T>
/*    */   implements QueueDisposable<T>
/*    */ {
/*    */   public final boolean offer(T e) {
/* 27 */     throw new UnsupportedOperationException("Should not be called");
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean offer(T v1, T v2) {
/* 32 */     throw new UnsupportedOperationException("Should not be called");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BasicQueueDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */