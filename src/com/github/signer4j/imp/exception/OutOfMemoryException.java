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
/*    */ public class OutOfMemoryException
/*    */   extends Signer4JException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public OutOfMemoryException(OutOfMemoryError cause) {
/* 35 */     super("Arquivo muito grande.", cause);
/*    */   }
/*    */   
/*    */   public OutOfMemoryException(String message, OutOfMemoryError cause) {
/* 39 */     super("Out of memory: " + message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/OutOfMemoryException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */