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
/*    */ 
/*    */ public class NoTokenPresentException
/*    */   extends Signer4JException
/*    */ {
/*    */   protected static final String DEFAULT_CAUSE = "O token não está conectado no dispositivo ou o certificado não possui uma chave privada associada.";
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public NoTokenPresentException(Throwable cause) {
/* 37 */     this("O token não está conectado no dispositivo ou o certificado não possui uma chave privada associada.", cause);
/*    */   }
/*    */   
/*    */   public NoTokenPresentException(String message, Throwable cause) {
/* 41 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/NoTokenPresentException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */