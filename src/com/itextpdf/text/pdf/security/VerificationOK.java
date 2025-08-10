/*    */ package com.itextpdf.text.pdf.security;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VerificationOK
/*    */ {
/*    */   protected X509Certificate certificate;
/*    */   protected Class<? extends CertificateVerifier> verifierClass;
/*    */   protected String message;
/*    */   
/*    */   public VerificationOK(X509Certificate certificate, Class<? extends CertificateVerifier> verifierClass, String message) {
/* 71 */     this.certificate = certificate;
/* 72 */     this.verifierClass = verifierClass;
/* 73 */     this.message = message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 81 */     StringBuilder sb = new StringBuilder();
/* 82 */     if (this.certificate != null) {
/* 83 */       sb.append(this.certificate.getSubjectDN().getName());
/* 84 */       sb.append(" verified with ");
/*    */     } 
/* 86 */     sb.append(this.verifierClass.getName());
/* 87 */     sb.append(": ");
/* 88 */     sb.append(this.message);
/* 89 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/VerificationOK.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */