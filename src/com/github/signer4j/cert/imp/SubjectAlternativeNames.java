/*     */ package com.github.signer4j.cert.imp;
/*     */ 
/*     */ import com.github.signer4j.cert.ICertificatePF;
/*     */ import com.github.signer4j.cert.ICertificatePJ;
/*     */ import com.github.signer4j.cert.ISubjectAlternativeNames;
/*     */ import com.github.signer4j.cert.oid.OIDReader;
/*     */ import com.github.signer4j.cert.oid.OID_1_3_6_1_4_1_311_20_2_3;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_1;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_2;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_3;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_4;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_5;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_6;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_7;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_8;
/*     */ import com.github.utils4j.imp.Strings;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Optional;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SubjectAlternativeNames
/*     */   implements ISubjectAlternativeNames
/*     */ {
/*  50 */   private String email = null;
/*  51 */   private BrazilianCertificatePF certificatePF = null;
/*  52 */   private BrazilianCertificatePJ certificatePJ = null;
/*     */   
/*     */   public SubjectAlternativeNames(X509Certificate certificate) {
/*  55 */     setup(certificate);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasCertificatePF() {
/*  60 */     return (this.certificatePF != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<ICertificatePF> getCertificatePF() {
/*  65 */     return Optional.ofNullable(this.certificatePF);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasCertificatePJ() {
/*  70 */     return (this.certificatePJ != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<ICertificatePJ> getCertificatePJ() {
/*  75 */     return Optional.ofNullable(this.certificatePJ);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getEmail() {
/*  80 */     return Strings.optional(this.email);
/*     */   }
/*     */   
/*     */   private void setup(X509Certificate certificate) {
/*  84 */     OIDReader reader = new OIDReader(certificate);
/*  85 */     if (reader.isCertificatePF()) {
/*  86 */       this
/*     */ 
/*     */         
/*  89 */         .certificatePF = new BrazilianCertificatePF(reader.getOID_2_16_76_1_3_1().orElse(OID_2_16_76_1_3_1.EMPTY), reader.getOID_2_16_76_1_3_5().orElse(OID_2_16_76_1_3_5.EMPTY), reader.getOID_2_16_76_1_3_6().orElse(OID_2_16_76_1_3_6.EMPTY));
/*     */     }
/*  91 */     else if (reader.isCertificatePJ()) {
/*  92 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  97 */         .certificatePJ = new BrazilianCertificatePJ(reader.getOID_2_16_76_1_3_2().orElse(OID_2_16_76_1_3_2.EMPTY), reader.getOID_2_16_76_1_3_3().orElse(OID_2_16_76_1_3_3.EMPTY), reader.getOID_2_16_76_1_3_4().orElse(OID_2_16_76_1_3_4.EMPTY), reader.getOID_2_16_76_1_3_7().orElse(OID_2_16_76_1_3_7.EMPTY), reader.getOID_2_16_76_1_3_8().orElse(OID_2_16_76_1_3_8.EMPTY));
/*     */     } 
/*     */     
/* 100 */     Optional<OID_1_3_6_1_4_1_311_20_2_3> oid = reader.getOID_1_3_6_1_4_1_311_20_2_3();
/* 101 */     if (oid.isPresent()) {
/* 102 */       Optional<String> upn = ((OID_1_3_6_1_4_1_311_20_2_3)oid.get()).getUPN();
/* 103 */       if (upn.isPresent()) {
/* 104 */         this.email = upn.get();
/*     */       }
/*     */     } 
/* 107 */     if (!Strings.hasText(this.email)) {
/* 108 */       Optional<String> mail = reader.getEmail();
/* 109 */       if (mail.isPresent())
/* 110 */         this.email = mail.get(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/imp/SubjectAlternativeNames.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */