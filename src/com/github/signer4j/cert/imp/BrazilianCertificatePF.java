/*     */ package com.github.signer4j.cert.imp;
/*     */ 
/*     */ import com.github.signer4j.cert.ICertificatePF;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_1;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_5;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_6;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import java.time.LocalDate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class BrazilianCertificatePF
/*     */   implements ICertificatePF
/*     */ {
/*     */   private final OID_2_16_76_1_3_1 id_2_16_76_1_3_1;
/*     */   private final OID_2_16_76_1_3_5 id_2_16_76_1_3_5;
/*     */   private final OID_2_16_76_1_3_6 id_2_16_76_1_3_6;
/*     */   
/*     */   BrazilianCertificatePF(OID_2_16_76_1_3_1 oid1, OID_2_16_76_1_3_5 oid2, OID_2_16_76_1_3_6 oid3) {
/*  49 */     this.id_2_16_76_1_3_1 = (OID_2_16_76_1_3_1)Args.requireNonNull(oid1, "oid1 is null");
/*  50 */     this.id_2_16_76_1_3_5 = (OID_2_16_76_1_3_5)Args.requireNonNull(oid2, "oid2 is null");
/*  51 */     this.id_2_16_76_1_3_6 = (OID_2_16_76_1_3_6)Args.requireNonNull(oid3, "oid3 is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getCPF() {
/*  56 */     return this.id_2_16_76_1_3_1.getCPF();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getNis() {
/*  61 */     return this.id_2_16_76_1_3_1.getNIS();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getRg() {
/*  66 */     return this.id_2_16_76_1_3_1.getRg();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getIssuingAgencyRg() {
/*  71 */     return this.id_2_16_76_1_3_1.getIssuingAgencyRg();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getUfIssuingAgencyRg() {
/*  76 */     return this.id_2_16_76_1_3_1.getUfIssuingAgencyRg();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getElectoralDocument() {
/*  81 */     return this.id_2_16_76_1_3_5.getElectoralDocument();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getSectionElectoralDocument() {
/*  86 */     return this.id_2_16_76_1_3_5.getSection();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getZoneElectoralDocument() {
/*  91 */     return this.id_2_16_76_1_3_5.getZone();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getCityElectoralDocument() {
/*  96 */     return this.id_2_16_76_1_3_5.getCityUF();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getUFElectoralDocument() {
/* 101 */     return this.id_2_16_76_1_3_5.getUFDocument();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getCEI() {
/* 106 */     return this.id_2_16_76_1_3_6.getCEI();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<LocalDate> getBirthDate() {
/* 111 */     Optional<String> date = this.id_2_16_76_1_3_1.getBirthDate();
/* 112 */     if (date.isPresent()) {
/* 113 */       String value = date.get();
/* 114 */       String day = value.substring(0, 2);
/* 115 */       String month = value.substring(2, 4);
/* 116 */       String year = value.substring(4, value.length());
/* 117 */       return Optional.of(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)));
/*     */     } 
/* 119 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/imp/BrazilianCertificatePF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */