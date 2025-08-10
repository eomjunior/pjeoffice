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
/*    */ public abstract class CallbackContribution<T>
/*    */   implements FutureCallback<T>
/*    */ {
/*    */   private final FutureCallback<?> callback;
/*    */   
/*    */   public CallbackContribution(FutureCallback<?> callback) {
/* 41 */     this.callback = callback;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void failed(Exception ex) {
/* 46 */     if (this.callback != null) {
/* 47 */       this.callback.failed(ex);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public final void cancelled() {
/* 53 */     if (this.callback != null)
/* 54 */       this.callback.cancelled(); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/concurrent/CallbackContribution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */