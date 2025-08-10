/*     */ package com.github.signer4j.cert.imp;
/*     */ 
/*     */ import com.github.signer4j.cert.ICertificatePJ;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_2;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_3;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_4;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_7;
/*     */ import com.github.signer4j.cert.oid.OID_2_16_76_1_3_8;
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
/*     */ class BrazilianCertificatePJ
/*     */   implements ICertificatePJ
/*     */ {
/*     */   private final OID_2_16_76_1_3_2 id_2_16_76_1_3_2;
/*     */   private final OID_2_16_76_1_3_3 id_2_16_76_1_3_3;
/*     */   private final OID_2_16_76_1_3_4 id_2_16_76_1_3_4;
/*     */   private final OID_2_16_76_1_3_7 id_2_16_76_1_3_7;
/*     */   private final OID_2_16_76_1_3_8 id_2_16_76_1_3_8;
/*     */   
/*     */   BrazilianCertificatePJ(OID_2_16_76_1_3_2 oid1, OID_2_16_76_1_3_3 oid2, OID_2_16_76_1_3_4 oid3, OID_2_16_76_1_3_7 oid4, OID_2_16_76_1_3_8 oid5) {
/*  53 */     this.id_2_16_76_1_3_2 = (OID_2_16_76_1_3_2)Args.requireNonNull(oid1, "oid1 is null");
/*  54 */     this.id_2_16_76_1_3_3 = (OID_2_16_76_1_3_3)Args.requireNonNull(oid2, "oid2 is null");
/*  55 */     this.id_2_16_76_1_3_4 = (OID_2_16_76_1_3_4)Args.requireNonNull(oid3, "oid3 is null");
/*  56 */     this.id_2_16_76_1_3_7 = (OID_2_16_76_1_3_7)Args.requireNonNull(oid4, "oid4 is null");
/*  57 */     this.id_2_16_76_1_3_8 = (OID_2_16_76_1_3_8)Args.requireNonNull(oid5, "oid5 is null");
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getResponsibleName() {
/*  62 */     return this.id_2_16_76_1_3_2.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getResponsibleCPF() {
/*  67 */     return this.id_2_16_76_1_3_4.getCPF();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getCNPJ() {
/*  72 */     return this.id_2_16_76_1_3_3.getCNPJ();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getBusinessName() {
/*  77 */     return this.id_2_16_76_1_3_8.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getNis() {
/*  82 */     return this.id_2_16_76_1_3_4.getNIS();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getRg() {
/*  87 */     return this.id_2_16_76_1_3_4.getRg();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getIssuingAgencyRg() {
/*  92 */     return this.id_2_16_76_1_3_4.getIssuingAgencyRg();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getUfIssuingAgencyRg() {
/*  97 */     return this.id_2_16_76_1_3_4.getUfIssuingAgencyRg();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<String> getCEI() {
/* 102 */     return this.id_2_16_76_1_3_7.getCEI();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Optional<LocalDate> getBirthDate() {
/* 107 */     Optional<String> date = this.id_2_16_76_1_3_4.getBirthDate();
/* 108 */     if (date.isPresent()) {
/* 109 */       String value = date.get();
/* 110 */       String day = value.substring(0, 2);
/* 111 */       String month = value.substring(2, 4);
/* 112 */       String year = value.substring(4, value.length());
/* 113 */       return Optional.of(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)));
/*     */     } 
/* 115 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/imp/BrazilianCertificatePJ.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */