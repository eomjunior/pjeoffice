/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CertificateVerifier
/*     */ {
/*     */   protected CertificateVerifier verifier;
/*     */   protected boolean onlineCheckingAllowed = true;
/*     */   
/*     */   public CertificateVerifier(CertificateVerifier verifier) {
/*  72 */     this.verifier = verifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnlineCheckingAllowed(boolean onlineCheckingAllowed) {
/*  80 */     this.onlineCheckingAllowed = onlineCheckingAllowed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<VerificationOK> verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
/*  97 */     if (signDate != null) {
/*  98 */       signCert.checkValidity(signDate);
/*     */     }
/* 100 */     if (issuerCert != null) {
/* 101 */       signCert.verify(issuerCert.getPublicKey());
/*     */     }
/*     */     else {
/*     */       
/* 105 */       signCert.verify(signCert.getPublicKey());
/*     */     } 
/* 107 */     List<VerificationOK> result = new ArrayList<VerificationOK>();
/* 108 */     if (this.verifier != null)
/* 109 */       result.addAll(this.verifier.verify(signCert, issuerCert, signDate)); 
/* 110 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/CertificateVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */