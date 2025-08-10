/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import com.itextpdf.text.log.Level;
/*     */ import com.itextpdf.text.log.Logger;
/*     */ import com.itextpdf.text.log.LoggerFactory;
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.cert.CRL;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509CRL;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
/*     */ import org.bouncycastle.cert.X509CertificateHolder;
/*     */ import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
/*     */ import org.bouncycastle.cert.ocsp.BasicOCSPResp;
/*     */ import org.bouncycastle.cert.ocsp.CertificateStatus;
/*     */ import org.bouncycastle.cert.ocsp.OCSPException;
/*     */ import org.bouncycastle.cert.ocsp.SingleResp;
/*     */ import org.bouncycastle.operator.ContentVerifierProvider;
/*     */ import org.bouncycastle.operator.DigestCalculatorProvider;
/*     */ import org.bouncycastle.operator.OperatorCreationException;
/*     */ import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
/*     */ import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
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
/*     */ public class OCSPVerifier
/*     */   extends RootStoreVerifier
/*     */ {
/*  82 */   protected static final Logger LOGGER = LoggerFactory.getLogger(OCSPVerifier.class);
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final String id_kp_OCSPSigning = "1.3.6.1.5.5.7.3.9";
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<BasicOCSPResp> ocsps;
/*     */ 
/*     */ 
/*     */   
/*     */   public OCSPVerifier(CertificateVerifier verifier, List<BasicOCSPResp> ocsps) {
/*  95 */     super(verifier);
/*  96 */     this.ocsps = ocsps;
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
/*     */   public List<VerificationOK> verify(X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
/* 112 */     List<VerificationOK> result = new ArrayList<VerificationOK>();
/* 113 */     int validOCSPsFound = 0;
/*     */     
/* 115 */     if (this.ocsps != null) {
/* 116 */       for (BasicOCSPResp ocspResp : this.ocsps) {
/* 117 */         if (verify(ocspResp, signCert, issuerCert, signDate)) {
/* 118 */           validOCSPsFound++;
/*     */         }
/*     */       } 
/*     */     }
/* 122 */     boolean online = false;
/* 123 */     if (this.onlineCheckingAllowed && validOCSPsFound == 0 && 
/* 124 */       verify(getOcspResponse(signCert, issuerCert), signCert, issuerCert, signDate)) {
/* 125 */       validOCSPsFound++;
/* 126 */       online = true;
/*     */     } 
/*     */ 
/*     */     
/* 130 */     LOGGER.info("Valid OCSPs found: " + validOCSPsFound);
/* 131 */     if (validOCSPsFound > 0)
/* 132 */       result.add(new VerificationOK(signCert, (Class)getClass(), "Valid OCSPs Found: " + validOCSPsFound + (online ? " (online)" : ""))); 
/* 133 */     if (this.verifier != null) {
/* 134 */       result.addAll(this.verifier.verify(signCert, issuerCert, signDate));
/*     */     }
/* 136 */     return result;
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
/*     */   public boolean verify(BasicOCSPResp ocspResp, X509Certificate signCert, X509Certificate issuerCert, Date signDate) throws GeneralSecurityException, IOException {
/* 151 */     if (ocspResp == null) {
/* 152 */       return false;
/*     */     }
/* 154 */     SingleResp[] resp = ocspResp.getResponses();
/* 155 */     for (int i = 0; i < resp.length; i++) {
/*     */       
/* 157 */       if (signCert.getSerialNumber().equals(resp[i].getCertID().getSerialNumber()))
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 162 */           if (issuerCert == null) issuerCert = signCert; 
/* 163 */           if (!resp[i].getCertID().matchesIssuer(new X509CertificateHolder(issuerCert.getEncoded()), (DigestCalculatorProvider)new BcDigestCalculatorProvider())) {
/* 164 */             LOGGER.info("OCSP: Issuers doesn't match.");
/*     */ 
/*     */           
/*     */           }
/*     */           else {
/*     */ 
/*     */             
/* 171 */             Date date = resp[i].getNextUpdate();
/* 172 */             if (date == null) {
/* 173 */               date = new Date(resp[i].getThisUpdate().getTime() + 180000L);
/* 174 */               if (LOGGER.isLogging(Level.INFO)) {
/* 175 */                 LOGGER.info(String.format("No 'next update' for OCSP Response; assuming %s", new Object[] { date }));
/*     */               }
/*     */             } 
/* 178 */             if (signDate.after(date))
/* 179 */             { if (LOGGER.isLogging(Level.INFO)) {
/* 180 */                 LOGGER.info(String.format("OCSP no longer valid: %s after %s", new Object[] { signDate, date }));
/*     */               } }
/*     */             
/*     */             else
/*     */             
/* 185 */             { Object status = resp[i].getCertStatus();
/* 186 */               if (status == CertificateStatus.GOOD)
/*     */               
/* 188 */               { isValidResponse(ocspResp, issuerCert);
/* 189 */                 return true; }  } 
/*     */           } 
/*     */         } catch (OCSPException oCSPException) {} 
/* 192 */     }  return false;
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
/*     */   public void isValidResponse(BasicOCSPResp ocspResp, X509Certificate issuerCert) throws GeneralSecurityException, IOException {
/* 207 */     X509Certificate responderCert = null;
/*     */ 
/*     */ 
/*     */     
/* 211 */     if (isSignatureValid(ocspResp, issuerCert)) {
/* 212 */       responderCert = issuerCert;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 217 */     if (responderCert == null) {
/* 218 */       if (ocspResp.getCerts() != null) {
/*     */         
/* 220 */         X509CertificateHolder[] certs = ocspResp.getCerts();
/* 221 */         for (X509CertificateHolder cert : certs) {
/*     */           X509Certificate tempCert;
/*     */           try {
/* 224 */             tempCert = (new JcaX509CertificateConverter()).getCertificate(cert);
/* 225 */           } catch (Exception ex) {}
/*     */ 
/*     */           
/* 228 */           List<String> keyPurposes = null;
/*     */           try {
/* 230 */             keyPurposes = tempCert.getExtendedKeyUsage();
/* 231 */             if (keyPurposes != null && keyPurposes.contains("1.3.6.1.5.5.7.3.9") && isSignatureValid(ocspResp, tempCert)) {
/* 232 */               responderCert = tempCert;
/*     */               break;
/*     */             } 
/* 235 */           } catch (CertificateParsingException certificateParsingException) {}
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 240 */         if (responderCert == null) {
/* 241 */           throw new VerificationException(issuerCert, "OCSP response could not be verified");
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 246 */         if (this.rootStore != null) {
/*     */           try {
/* 248 */             for (Enumeration<String> aliases = this.rootStore.aliases(); aliases.hasMoreElements(); ) {
/* 249 */               String alias = aliases.nextElement();
/*     */               try {
/* 251 */                 if (!this.rootStore.isCertificateEntry(alias))
/*     */                   continue; 
/* 253 */                 X509Certificate anchor = (X509Certificate)this.rootStore.getCertificate(alias);
/* 254 */                 if (isSignatureValid(ocspResp, anchor)) {
/* 255 */                   responderCert = anchor;
/*     */                   break;
/*     */                 } 
/* 258 */               } catch (GeneralSecurityException generalSecurityException) {}
/*     */             }
/*     */           
/* 261 */           } catch (KeyStoreException e) {
/* 262 */             responderCert = null;
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 268 */         if (responderCert == null) {
/* 269 */           throw new VerificationException(issuerCert, "OCSP response could not be verified");
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 275 */     responderCert.verify(issuerCert.getPublicKey());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 280 */     if (responderCert.getExtensionValue(OCSPObjectIdentifiers.id_pkix_ocsp_nocheck.getId()) == null) {
/*     */       CRL crl;
/*     */       try {
/* 283 */         crl = CertificateUtil.getCRL(responderCert);
/* 284 */       } catch (Exception ignored) {
/* 285 */         crl = null;
/*     */       } 
/* 287 */       if (crl != null && crl instanceof X509CRL) {
/* 288 */         CRLVerifier crlVerifier = new CRLVerifier(null, null);
/* 289 */         crlVerifier.setRootStore(this.rootStore);
/* 290 */         crlVerifier.setOnlineCheckingAllowed(this.onlineCheckingAllowed);
/* 291 */         crlVerifier.verify((X509CRL)crl, responderCert, issuerCert, new Date());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*     */     
/* 297 */     responderCert.checkValidity();
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
/*     */   @Deprecated
/*     */   public boolean verifyResponse(BasicOCSPResp ocspResp, X509Certificate issuerCert) {
/*     */     try {
/* 312 */       isValidResponse(ocspResp, issuerCert);
/* 313 */       return true;
/* 314 */     } catch (Exception e) {
/* 315 */       return false;
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
/*     */   public boolean isSignatureValid(BasicOCSPResp ocspResp, Certificate responderCert) {
/*     */     try {
/* 328 */       ContentVerifierProvider verifierProvider = (new JcaContentVerifierProviderBuilder()).setProvider("BC").build(responderCert.getPublicKey());
/* 329 */       return ocspResp.isSignatureValid(verifierProvider);
/* 330 */     } catch (OperatorCreationException e) {
/* 331 */       return false;
/* 332 */     } catch (OCSPException e) {
/* 333 */       return false;
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
/*     */   public BasicOCSPResp getOcspResponse(X509Certificate signCert, X509Certificate issuerCert) {
/* 345 */     if (signCert == null && issuerCert == null) {
/* 346 */       return null;
/*     */     }
/* 348 */     OcspClientBouncyCastle ocsp = new OcspClientBouncyCastle();
/* 349 */     BasicOCSPResp ocspResp = ocsp.getBasicOCSPResp(signCert, issuerCert, null);
/* 350 */     if (ocspResp == null) {
/* 351 */       return null;
/*     */     }
/* 353 */     SingleResp[] resp = ocspResp.getResponses();
/* 354 */     for (int i = 0; i < resp.length; i++) {
/* 355 */       Object status = resp[i].getCertStatus();
/* 356 */       if (status == CertificateStatus.GOOD) {
/* 357 */         return ocspResp;
/*     */       }
/*     */     } 
/* 360 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/OCSPVerifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */