/*    */ package com.github.signer4j.cert.imp;
/*    */ 
/*    */ import com.github.signer4j.ICertificate;
/*    */ import com.github.signer4j.cert.ICertificateFactory;
/*    */ import com.github.utils4j.imp.Args;
/*    */ import java.io.InputStream;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.X509Certificate;
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
/*    */ public enum CertificateFactory
/*    */   implements ICertificateFactory
/*    */ {
/* 39 */   DEFAULT;
/*    */ 
/*    */   
/*    */   public ICertificate create(Object input, String aliasName) throws CertificateException {
/* 43 */     Args.requireNonNull(input, "input is null");
/* 44 */     if (input instanceof InputStream)
/* 45 */       return new BrazilianCertificate((InputStream)input, aliasName); 
/* 46 */     if (input instanceof X509Certificate)
/* 47 */       return new BrazilianCertificate((X509Certificate)input, aliasName); 
/* 48 */     throw new CertificateException("Incapaz de criar instância de 'BrazilianCertificate'. Tipo base desconhecido: " + input.getClass());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/imp/CertificateFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */