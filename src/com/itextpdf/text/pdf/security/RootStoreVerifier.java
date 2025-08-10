/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
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
/*     */ public class RootStoreVerifier
/*     */   extends CertificateVerifier
/*     */ {
/*  65 */   protected static final Logger LOGGER = LoggerFactory.getLogger(RootStoreVerifier.class);
/*     */ 
/*     */   
/*  68 */   protected KeyStore rootStore = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RootStoreVerifier(CertificateVerifier verifier) {
/*  77 */     super(verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRootStore(KeyStore keyStore) {
/*  87 */     this.rootStore = keyStore;
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
/* 104 */     LOGGER.info("Root store verification: " + signCert.getSubjectDN().getName());
/*     */     
/* 106 */     if (this.rootStore == null)
/* 107 */       return super.verify(signCert, issuerCert, signDate); 
/*     */     try {
/* 109 */       List<VerificationOK> result = new ArrayList<VerificationOK>();
/*     */       
/* 111 */       for (Enumeration<String> aliases = this.rootStore.aliases(); aliases.hasMoreElements(); ) {
/* 112 */         String alias = aliases.nextElement();
/*     */         try {
/* 114 */           if (!this.rootStore.isCertificateEntry(alias)) {
/*     */             continue;
/*     */           }
/* 117 */           X509Certificate anchor = (X509Certificate)this.rootStore.getCertificate(alias);
/* 118 */           signCert.verify(anchor.getPublicKey());
/* 119 */           LOGGER.info("Certificate verified against root store");
/* 120 */           result.add(new VerificationOK(signCert, (Class)getClass(), "Certificate verified against root store."));
/* 121 */           result.addAll(super.verify(signCert, issuerCert, signDate));
/* 122 */           return result;
/* 123 */         } catch (GeneralSecurityException e) {}
/*     */       } 
/*     */ 
/*     */       
/* 127 */       result.addAll(super.verify(signCert, issuerCert, signDate));
/* 128 */       return result;
/* 129 */     } catch (GeneralSecurityException e) {
/* 130 */       return super.verify(signCert, issuerCert, signDate);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/RootStoreVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */