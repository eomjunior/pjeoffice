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
/*    */ public class MscapiKeysetException
/*    */   extends MscapiException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MscapiKeysetException(Exception cause) {
/* 35 */     super("API de integração com 'Windows' falhou. Tente utilizar a opção nativa 'PJeOffice' no menu 'Configuração de certificado'.. Não foi possível ler as chaves do dispositivo!", cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/MscapiKeysetException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */