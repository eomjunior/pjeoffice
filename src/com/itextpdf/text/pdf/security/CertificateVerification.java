/*     */ package com.itextpdf.text.pdf.security;
/*     */ 
/*     */ import java.security.KeyStore;
/*     */ import java.security.cert.CRL;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.List;
/*     */ import org.bouncycastle.cert.ocsp.BasicOCSPResp;
/*     */ import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
/*     */ import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
/*     */ import org.bouncycastle.tsp.TimeStampToken;
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
/*     */ public class CertificateVerification
/*     */ {
/*     */   public static String verifyCertificate(X509Certificate cert, Collection<CRL> crls, Calendar calendar) {
/*  77 */     if (calendar == null)
/*  78 */       calendar = new GregorianCalendar(); 
/*  79 */     if (cert.hasUnsupportedCriticalExtension()) {
/*  80 */       for (String oid : cert.getCriticalExtensionOIDs()) {
/*     */         
/*  82 */         if ("2.5.29.15".equals(oid) && cert.getKeyUsage()[0]) {
/*     */           continue;
/*     */         }
/*     */         
/*     */         try {
/*  87 */           if ("2.5.29.37".equals(oid) && cert.getExtendedKeyUsage().contains("1.3.6.1.5.5.7.3.8")) {
/*     */             continue;
/*     */           }
/*  90 */         } catch (CertificateParsingException certificateParsingException) {}
/*     */ 
/*     */         
/*  93 */         return "Has unsupported critical extension";
/*     */       } 
/*     */     }
/*     */     try {
/*  97 */       cert.checkValidity(calendar.getTime());
/*     */     }
/*  99 */     catch (Exception e) {
/* 100 */       return e.getMessage();
/*     */     } 
/* 102 */     if (crls != null)
/* 103 */       for (CRL crl : crls) {
/* 104 */         if (crl.isRevoked(cert)) {
/* 105 */           return "Certificate revoked";
/*     */         }
/*     */       }  
/* 108 */     return null;
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
/*     */   public static List<VerificationException> verifyCertificates(Certificate[] certs, KeyStore keystore, Collection<CRL> crls, Calendar calendar) {
/* 122 */     List<VerificationException> result = new ArrayList<VerificationException>();
/* 123 */     if (calendar == null)
/* 124 */       calendar = new GregorianCalendar(); 
/* 125 */     for (int k = 0; k < certs.length; k++) {
/* 126 */       X509Certificate cert = (X509Certificate)certs[k];
/* 127 */       String err = verifyCertificate(cert, crls, calendar);
/* 128 */       if (err != null)
/* 129 */         result.add(new VerificationException(cert, err)); 
/*     */       try {
/* 131 */         for (Enumeration<String> aliases = keystore.aliases(); aliases.hasMoreElements();) {
/*     */           try {
/* 133 */             String alias = aliases.nextElement();
/* 134 */             if (!keystore.isCertificateEntry(alias))
/*     */               continue; 
/* 136 */             X509Certificate certStoreX509 = (X509Certificate)keystore.getCertificate(alias);
/* 137 */             if (verifyCertificate(certStoreX509, crls, calendar) != null)
/*     */               continue; 
/*     */             try {
/* 140 */               cert.verify(certStoreX509.getPublicKey());
/* 141 */               return result;
/*     */             }
/* 143 */             catch (Exception e) {}
/*     */ 
/*     */           
/*     */           }
/* 147 */           catch (Exception exception) {}
/*     */         }
/*     */       
/*     */       }
/* 151 */       catch (Exception exception) {}
/*     */       
/*     */       int j;
/* 154 */       for (j = 0; j < certs.length; j++) {
/* 155 */         if (j != k) {
/*     */           
/* 157 */           X509Certificate certNext = (X509Certificate)certs[j];
/*     */           try {
/* 159 */             cert.verify(certNext.getPublicKey());
/*     */             
/*     */             break;
/* 162 */           } catch (Exception exception) {}
/*     */         } 
/*     */       } 
/* 165 */       if (j == certs.length) {
/* 166 */         result.add(new VerificationException(cert, "Cannot be verified against the KeyStore or the certificate chain"));
/*     */       }
/*     */     } 
/* 169 */     if (result.size() == 0)
/* 170 */       result.add(new VerificationException(null, "Invalid state. Possible circular certificate chain")); 
/* 171 */     return result;
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
/*     */   public static List<VerificationException> verifyCertificates(Certificate[] certs, KeyStore keystore, Calendar calendar) {
/* 184 */     return verifyCertificates(certs, keystore, null, calendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean verifyOcspCertificates(BasicOCSPResp ocsp, KeyStore keystore, String provider) {
/* 195 */     if (provider == null)
/* 196 */       provider = "BC"; 
/*     */     try {
/* 198 */       for (Enumeration<String> aliases = keystore.aliases(); aliases.hasMoreElements();) {
/*     */         try {
/* 200 */           String alias = aliases.nextElement();
/* 201 */           if (!keystore.isCertificateEntry(alias))
/*     */             continue; 
/* 203 */           X509Certificate certStoreX509 = (X509Certificate)keystore.getCertificate(alias);
/* 204 */           if (ocsp.isSignatureValid((new JcaContentVerifierProviderBuilder()).setProvider(provider).build(certStoreX509.getPublicKey()))) {
/* 205 */             return true;
/*     */           }
/* 207 */         } catch (Exception exception) {}
/*     */       }
/*     */     
/*     */     }
/* 211 */     catch (Exception exception) {}
/*     */     
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean verifyTimestampCertificates(TimeStampToken ts, KeyStore keystore, String provider) {
/* 224 */     if (provider == null)
/* 225 */       provider = "BC"; 
/*     */     try {
/* 227 */       for (Enumeration<String> aliases = keystore.aliases(); aliases.hasMoreElements();) {
/*     */         try {
/* 229 */           String alias = aliases.nextElement();
/* 230 */           if (!keystore.isCertificateEntry(alias))
/*     */             continue; 
/* 232 */           X509Certificate certStoreX509 = (X509Certificate)keystore.getCertificate(alias);
/* 233 */           ts.isSignatureValid((new JcaSimpleSignerInfoVerifierBuilder()).setProvider(provider).build(certStoreX509));
/* 234 */           return true;
/*     */         }
/* 236 */         catch (Exception exception) {}
/*     */       }
/*     */     
/*     */     }
/* 240 */     catch (Exception exception) {}
/*     */     
/* 242 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/security/CertificateVerification.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */