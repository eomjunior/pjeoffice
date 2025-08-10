/*    */ package com.github.signer4j.imp.exception;
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
/*    */ public class InterruptedOperationException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public InterruptedOperationException() {
/* 35 */     super("A operação foi interrompida/cancelada!");
/*    */   }
/*    */   
/*    */   public InterruptedOperationException(Throwable cause) {
/* 39 */     super(cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/InterruptedOperationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */