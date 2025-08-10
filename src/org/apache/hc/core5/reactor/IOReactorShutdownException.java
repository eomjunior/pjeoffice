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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IOReactorShutdownException
/*    */   extends IllegalStateException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public IOReactorShutdownException(String message) {
/* 40 */     super(message);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOReactorShutdownException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */