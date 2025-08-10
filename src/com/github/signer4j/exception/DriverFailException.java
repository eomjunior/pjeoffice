/*    */ package com.github.signer4j.exception;
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
/*    */ public class DriverFailException
/*    */   extends DriverException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public DriverFailException(String message) {
/* 35 */     super(message);
/*    */   }
/*    */   
/*    */   public DriverFailException(String message, Throwable cause) {
/* 39 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/exception/DriverFailException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */