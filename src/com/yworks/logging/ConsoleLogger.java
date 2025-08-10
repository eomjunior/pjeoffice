/*    */ package com.yworks.logging;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConsoleLogger
/*    */   extends Logger
/*    */ {
/*    */   public ConsoleLogger() {
/* 14 */     register();
/*    */   }
/*    */ 
/*    */   
/*    */   public void doLog(String s) {}
/*    */ 
/*    */   
/*    */   public void doErr(String s) {
/* 22 */     System.err.println(s);
/*    */   }
/*    */   
/*    */   public void doWarn(String s) {
/* 26 */     System.out.println(s);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doWarnToLog(String s) {}
/*    */   
/*    */   public void doShrinkLog(String s) {
/* 33 */     System.out.println(s);
/*    */   }
/*    */   
/*    */   public void doErr(String s, Throwable ex) {
/* 37 */     System.out.println(s);
/* 38 */     ex.printStackTrace();
/*    */   }
/*    */   
/*    */   public void close() {
/* 42 */     unregister();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/logging/ConsoleLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */