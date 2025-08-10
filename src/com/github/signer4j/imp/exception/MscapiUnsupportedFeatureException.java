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
/*    */ 
/*    */ 
/*    */ public class MscapiUnsupportedFeatureException
/*    */   extends MscapiException
/*    */ {
/*    */   private static final String DEFAULT_MESSAGE = "API de integração com 'Windows' falhou. Tente utilizar a opção nativa 'PJeOffice' no menu 'Configuração de certificado'.. O driver do seu certificado digital não suporta o recurso solicitado. Este erro costuma ocorrer quando o algorítmo de criptografia utilizado não está disponível no driver instalado do seu dispositivo.";
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public MscapiUnsupportedFeatureException(Exception cause) {
/* 39 */     super("API de integração com 'Windows' falhou. Tente utilizar a opção nativa 'PJeOffice' no menu 'Configuração de certificado'.. O driver do seu certificado digital não suporta o recurso solicitado. Este erro costuma ocorrer quando o algorítmo de criptografia utilizado não está disponível no driver instalado do seu dispositivo.", cause);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/MscapiUnsupportedFeatureException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */