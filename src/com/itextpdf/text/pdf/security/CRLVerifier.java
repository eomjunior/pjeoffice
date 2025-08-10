/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509CRL;
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
/*     */ public class CRLVerifier
/*     */   extends RootStoreVerifier
/*     */ {
/*  67 */   protected static final Logger LOGGER = LoggerFactory.getLogger(CRLVerifier.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<X509CRL> crls;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CRLVerifier(CertificateVerifier verifier, List<X509CRL> crls) {
/*  78 */     super(verifier);
/*  79 */     this.crls = crls;
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
/*     */   public List<VerificationOK> verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
/*  94 */     List<VerificationOK> result = new ArrayList<VerificationOK>();
/*  95 */     int validCrlsFound = 0;
/*     */     
/*  97 */     if (this.crls != null) {
/*  98 */       for (X509CRL crl : this.crls) {
/*  99 */         if (verify(crl, signCert, issuerCert, signDate)) {
/* 100 */           validCrlsFound++;
/*     */         }
/*     */       } 
/*     */     }
/* 104 */     boolean online = false;
/* 105 */     if (this.onlineCheckingAllowed && validCrlsFound == 0 && 
/* 106 */       verify(getCRL(signCert, issuerCert), signCert, issuerCert, signDate)) {
/* 107 */       validCrlsFound++;
/* 108 */       online = true;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     LOGGER.info("Valid CRLs found: " + validCrlsFound);
/* 113 */     if (validCrlsFound > 0) {
/* 114 */       result.add(new VerificationOK(signCert, (Class)getClass(), "Valid CRLs found: " + validCrlsFound + (online ? " (online)" : "")));
/*     */     }
/* 116 */     if (this.verifier != null) {
/* 117 */       result.addAll(this.verifier.verify(signCert, issuerCert, signDate));
/*     */     }
/* 119 */     return result;
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
/*     */   public boolean verify(X509CRL crl, X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException {
/* 132 */     if (crl == null || signDate == null) {
/* 133 */       return false;
/*     */     }
/* 135 */     if (crl.getIssuerX500Principal().equals(signCert.getIssuerX500Principal()) && signDate
/* 136 */       .after(crl.getThisUpdate()) && signDate.before(crl.getNextUpdate())) {
/*     */       
/* 138 */       if (isSignatureValid(crl, issuerCert) && crl.isRevoked(signCert)) {
/* 139 */         throw new VerificationException(signCert, "The certificate has been revoked.");
/*     */       }
/* 141 */       return true;
/*     */     } 
/* 143 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public X509CRL getCRL(X509Certificate signCert, X509Certificate issuerCert) {
/* 153 */     if (issuerCert == null) {
/* 154 */       issuerCert = signCert;
/*     */     }
/*     */     try {
/* 157 */       String crlurl = CertificateUtil.getCRLURL(signCert);
/* 158 */       if (crlurl == null)
/* 159 */         return null; 
/* 160 */       LOGGER.info("Getting CRL from " + crlurl);
/* 161 */       CertificateFactory cf = CertificateFactory.getInstance("X.509");
/*     */       
/* 163 */       return (X509CRL)cf.generateCRL((new URL(crlurl)).openStream());
/*     */     }
/* 165 */     catch (IOException e) {
/* 166 */       return null;
/*     */     }
/* 168 */     catch (GeneralSecurityException e) {
/* 169 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSignatureValid(X509CRL crl, X509Certificate crlIssuer) {
/* 181 */     if (crlIssuer != null) {
/*     */       try {
/* 183 */         crl.verify(crlIssuer.getPublicKey());
/* 184 */         return true;
/* 185 */       } catch (GeneralSecurityException e) {
/* 186 */         LOGGER.warn("CRL not issued by the same authority as the certificate that is being checked");
/*     */       } 
/*     */     }
/*     */     
/* 190 */     if (this.rootStore == null) {
/* 191 */       return false;
/*     */     }
/*     */     try {
/* 194 */       for (Enumeration<String> aliases = this.rootStore.aliases(); aliases.hasMoreElements(); ) {
/* 195 */         String alias = aliases.nextElement();
/*     */         try {
/* 197 */           if (!this.rootStore.isCertificateEntry(alias)) {
/*     */             continue;
/*     */           }
/* 200 */           X509Certificate anchor = (X509Certificate)this.rootStore.getCertificate(alias);
/* 201 */           crl.verify(anchor.getPublicKey());
/* 202 */           return true;
/* 203 */         } catch (GeneralSecurityException e) {}
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 208 */     catch (GeneralSecurityException e) {
/* 209 */       return false;
/*     */     } 
/* 211 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/CRLVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */