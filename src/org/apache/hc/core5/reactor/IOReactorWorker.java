/*    */ package org.apache.hc.core5.reactor;
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
/*    */ final class IOReactorWorker
/*    */   implements Runnable
/*    */ {
/*    */   private final AbstractSingleCoreIOReactor ioReactor;
/*    */   private volatile Throwable throwable;
/*    */   
/*    */   public IOReactorWorker(AbstractSingleCoreIOReactor ioReactor) {
/* 38 */     this.ioReactor = ioReactor;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/*    */     try {
/* 44 */       this.ioReactor.execute();
/* 45 */     } catch (Error ex) {
/* 46 */       this.throwable = ex;
/* 47 */       throw ex;
/* 48 */     } catch (Exception ex) {
/* 49 */       this.throwable = ex;
/*    */     } 
/*    */   }
/*    */   
/*    */   public Throwable getThrowable() {
/* 54 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOReactorWorker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */