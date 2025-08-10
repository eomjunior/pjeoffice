/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.cert.ICertificateFactory;
/*    */ import com.github.signer4j.imp.exception.Signer4JException;
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
/*    */ class PKCS12Certificates
/*    */   extends KeyStoreCertificates
/*    */ {
/*    */   PKCS12Certificates(PKCS12Token token, IKeyStore keyStore, ICertificateFactory factory) throws Signer4JException {
/* 35 */     super(token, keyStore, factory);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/PKCS12Certificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */