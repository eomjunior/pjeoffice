/*    */ package org.apache.hc.core5.concurrent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class FutureContribution<T>
/*    */   implements FutureCallback<T>
/*    */ {
/*    */   private final BasicFuture<?> future;
/*    */   
/*    */   public FutureContribution(BasicFuture<?> future) {
/* 41 */     this.future = future;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void failed(Exception ex) {
/* 46 */     if (this.future != null) {
/* 47 */       this.future.failed(ex);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final void cancelled() {
/* 53 */     if (this.future != null)
/* 54 */       this.future.cancel(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/concurrent/FutureContribution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */