/*   */ package com.github.signer4j.imp.exception;
/*   */ 
/*   */ public class CertificateAliasNotFoundException
/*   */   extends RuntimeException {
/*   */   private static final long serialVersionUID = 1L;
/*   */   
/*   */   public CertificateAliasNotFoundException() {
/* 8 */     super("Alias não definido para o certificado escolhido! Certifique-se de que o certificado possua uma chave privada associada a ele.");
/*   */   }
/*   */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/exception/CertificateAliasNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */