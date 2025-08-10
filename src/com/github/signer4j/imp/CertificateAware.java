/*    */ package com.github.signer4j.imp;
/*    */ 
/*    */ import com.github.signer4j.IPersonalData;
/*    */ import com.github.utils4j.imp.Base64;
/*    */ import java.security.cert.Certificate;
/*    */ import java.security.cert.CertificateException;
/*    */ import java.util.Arrays;
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
/*    */ abstract class CertificateAware
/*    */   implements IPersonalData
/*    */ {
/*    */   private String certificate64Cache;
/*    */   private String certificateChain64Cache;
/*    */   
/*    */   public final String getCertificate64() throws CertificateException {
/* 45 */     return (this.certificate64Cache != null) ? this.certificate64Cache : (this
/* 46 */       .certificate64Cache = Base64.base64Encode(Arrays.asList(new Certificate[] { getCertificate() })));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCertificateChain64() throws CertificateException {
/* 51 */     return (this.certificateChain64Cache != null) ? this.certificateChain64Cache : (this
/* 52 */       .certificateChain64Cache = Base64.base64Encode(getCertificateChain()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/CertificateAware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */