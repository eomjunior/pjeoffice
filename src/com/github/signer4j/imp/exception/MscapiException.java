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
/*    */ public class MscapiException
/*    */   extends Signer4JException
/*    */ {
/*    */   protected static final String DEFAULT_MESSAGE = "API de integração com 'Windows' falhou. Tente utilizar a opção nativa 'PJeOffice' no menu 'Configuração de certificado'.";
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MscapiException(String message, Throwable cause) {
/* 37 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public MscapiException(Exception cause) {
/* 41 */     super("API de integração com 'Windows' falhou. Tente utilizar a opção nativa 'PJeOffice' no menu 'Configuração de certificado'.", cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/MscapiException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */