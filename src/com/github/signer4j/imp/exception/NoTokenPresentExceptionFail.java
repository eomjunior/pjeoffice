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
/*    */ public class NoTokenPresentExceptionFail
/*    */   extends NoTokenPresentException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public NoTokenPresentExceptionFail(Throwable cause) {
/* 35 */     super("O token não está conectado no dispositivo ou o certificado não possui uma chave privada associada. Possível incompatibilidade/bug de biblioteca PKCS11 com dispositivo criptográfico A3.", cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/NoTokenPresentExceptionFail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */