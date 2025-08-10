/*    */ package com.github.utils4j.imp;
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
/*    */ public class DaemonThreadFactory
/*    */   extends CustomThreadFactory
/*    */ {
/*    */   public DaemonThreadFactory(String baseName) {
/* 33 */     super(baseName);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void customize(Thread thread) {
/* 38 */     thread.setDaemon(true);
/* 39 */     if (thread.getPriority() != 5)
/* 40 */       thread.setPriority(5); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/DaemonThreadFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */