/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import org.slf4j.Logger;
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
/*     */ final class TlsSessionValidator
/*     */ {
/*     */   private final Logger log;
/*     */   
/*     */   TlsSessionValidator(Logger log) {
/*  50 */     this.log = log;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void verifySession(String hostname, SSLSession sslsession, HostnameVerifier hostnameVerifier) throws SSLException {
/*  58 */     if (this.log.isDebugEnabled()) {
/*  59 */       this.log.debug("Secure session established");
/*  60 */       this.log.debug(" negotiated protocol: {}", sslsession.getProtocol());
/*  61 */       this.log.debug(" negotiated cipher suite: {}", sslsession.getCipherSuite());
/*     */ 
/*     */       
/*     */       try {
/*  65 */         Certificate[] certs = sslsession.getPeerCertificates();
/*  66 */         Certificate cert = certs[0];
/*  67 */         if (cert instanceof X509Certificate) {
/*  68 */           X509Certificate x509 = (X509Certificate)cert;
/*  69 */           X500Principal peer = x509.getSubjectX500Principal();
/*     */           
/*  71 */           this.log.debug(" peer principal: {}", peer);
/*  72 */           Collection<List<?>> altNames1 = x509.getSubjectAlternativeNames();
/*  73 */           if (altNames1 != null) {
/*  74 */             List<String> altNames = new ArrayList<>();
/*  75 */             for (List<?> aC : altNames1) {
/*  76 */               if (!aC.isEmpty()) {
/*  77 */                 altNames.add(Objects.toString(aC.get(1), null));
/*     */               }
/*     */             } 
/*  80 */             this.log.debug(" peer alternative names: {}", altNames);
/*     */           } 
/*     */           
/*  83 */           X500Principal issuer = x509.getIssuerX500Principal();
/*  84 */           this.log.debug(" issuer principal: {}", issuer);
/*  85 */           Collection<List<?>> altNames2 = x509.getIssuerAlternativeNames();
/*  86 */           if (altNames2 != null) {
/*  87 */             List<String> altNames = new ArrayList<>();
/*  88 */             for (List<?> aC : altNames2) {
/*  89 */               if (!aC.isEmpty()) {
/*  90 */                 altNames.add(Objects.toString(aC.get(1), null));
/*     */               }
/*     */             } 
/*  93 */             this.log.debug(" issuer alternative names: {}", altNames);
/*     */           } 
/*     */         } 
/*  96 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 100 */     if (hostnameVerifier != null) {
/* 101 */       X509Certificate x509Certificate; Certificate[] certs = sslsession.getPeerCertificates();
/* 102 */       if (certs.length < 1) {
/* 103 */         throw new SSLPeerUnverifiedException("Peer certificate chain is empty");
/*     */       }
/* 105 */       Certificate peerCertificate = certs[0];
/*     */       
/* 107 */       if (peerCertificate instanceof X509Certificate) {
/* 108 */         x509Certificate = (X509Certificate)peerCertificate;
/*     */       } else {
/* 110 */         throw new SSLPeerUnverifiedException("Unexpected certificate type: " + peerCertificate.getType());
/*     */       } 
/* 112 */       if (hostnameVerifier instanceof HttpClientHostnameVerifier) {
/* 113 */         ((HttpClientHostnameVerifier)hostnameVerifier).verify(hostname, x509Certificate);
/* 114 */       } else if (!hostnameVerifier.verify(hostname, sslsession)) {
/* 115 */         List<SubjectName> subjectAlts = DefaultHostnameVerifier.getSubjectAltNames(x509Certificate);
/* 116 */         throw new SSLPeerUnverifiedException("Certificate for <" + hostname + "> doesn't match any of the subject alternative names: " + subjectAlts);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/TlsSessionValidator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */