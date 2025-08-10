/*    */ package io.reactivex.internal.observers;
/*    */ 
/*    */ import io.reactivex.internal.fuseable.QueueDisposable;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
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
/*    */ public abstract class BasicIntQueueDisposable<T>
/*    */   extends AtomicInteger
/*    */   implements QueueDisposable<T>
/*    */ {
/*    */   private static final long serialVersionUID = -1001730202384742097L;
/*    */   
/*    */   public final boolean offer(T e) {
/* 33 */     throw new UnsupportedOperationException("Should not be called");
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean offer(T v1, T v2) {
/* 38 */     throw new UnsupportedOperationException("Should not be called");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/io/reactivex/internal/observers/BasicIntQueueDisposable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */