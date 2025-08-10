/*     */ package com.github.signer4j.cert.oid;
/*     */ 
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
/*     */ public final class OID_2_16_76_1_3_5
/*     */   extends OIDBasic
/*     */ {
/*     */   public static final String OID = "2.16.76.1.3.5";
/*  36 */   public static final OID_2_16_76_1_3_5 EMPTY = new OID_2_16_76_1_3_5();
/*     */   
/*     */   private enum Fields implements OIDBasic.IMetadata {
/*  39 */     ELECTORAL_DOCUMENT(12),
/*  40 */     ZONE(3),
/*  41 */     SECTION(4),
/*  42 */     CITY_UF(22);
/*     */     
/*     */     private final int length;
/*     */     
/*     */     Fields(int length) {
/*  47 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int length() {
/*  52 */       return this.length;
/*     */     }
/*     */   }
/*     */   
/*     */   private OID_2_16_76_1_3_5() {
/*  57 */     this("");
/*     */   }
/*     */   
/*     */   protected OID_2_16_76_1_3_5(String content) {
/*  61 */     super("2.16.76.1.3.5", content);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setup() {
/*  66 */     setup((OIDBasic.IMetadata[])Fields.values());
/*     */   }
/*     */   
/*     */   public Optional<String> getElectoralDocument() {
/*  70 */     return get(Fields.ELECTORAL_DOCUMENT);
/*     */   }
/*     */   
/*     */   public Optional<String> getZone() {
/*  74 */     return get(Fields.ZONE);
/*     */   }
/*     */   
/*     */   public Optional<String> getSection() {
/*  78 */     return get(Fields.SECTION);
/*     */   }
/*     */   
/*     */   public Optional<String> getCityUF() {
/*  82 */     Optional<String> value = get(Fields.CITY_UF);
/*  83 */     if (value.isPresent()) {
/*  84 */       String s = value.get();
/*  85 */       int len = s.length();
/*  86 */       if (len > 2) {
/*  87 */         return Optional.of(s.substring(0, len - 2));
/*     */       }
/*     */     } 
/*  90 */     return value;
/*     */   }
/*     */   
/*     */   public Optional<String> getUFDocument() {
/*  94 */     Optional<String> value = get(Fields.CITY_UF);
/*  95 */     if (value.isPresent()) {
/*  96 */       String s = value.get();
/*  97 */       int len = s.length();
/*  98 */       if (len > 1) {
/*  99 */         return Optional.of(s.substring(len - 2, len));
/*     */       }
/*     */     } 
/* 102 */     return value;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/cert/oid/OID_2_16_76_1_3_5.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */