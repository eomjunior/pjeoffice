/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.security.cert.Certificate;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.security.cert.CertificateFactory;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.List;
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
/*    */ public class Certificates
/*    */ {
/*    */   private static final String X509_CERTIFICATE_TYPE = "X.509";
/*    */   private static final String CERTIFICATION_CHAIN_ENCODING = "PkiPath";
/*    */   private static CertificateFactory FACTORY;
/*    */   
/*    */   private static CertificateFactory getFactory() throws CertificateException {
/* 45 */     return (FACTORY == null) ? (FACTORY = CertificateFactory.getInstance("X.509")) : FACTORY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static X509Certificate create(InputStream is) throws CertificateException {
/* 51 */     Args.requireNonNull(is, "inputstream is null");
/* 52 */     return (X509Certificate)getFactory().generateCertificate(is);
/*    */   }
/*    */   
/*    */   public static byte[] toByteArray(List<Certificate> chain) throws CertificateException {
/* 56 */     Args.requireNonEmpty(chain, "chain is empty");
/* 57 */     return getFactory().generateCertPath(chain).getEncoded("PkiPath");
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Certificates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */