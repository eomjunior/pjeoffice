/*     */ package com.itextpdf.text.pdf.qrcode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Mode
/*     */ {
/*  30 */   public static final Mode TERMINATOR = new Mode(new int[] { 0, 0, 0 }, 0, "TERMINATOR");
/*  31 */   public static final Mode NUMERIC = new Mode(new int[] { 10, 12, 14 }, 1, "NUMERIC");
/*  32 */   public static final Mode ALPHANUMERIC = new Mode(new int[] { 9, 11, 13 }, 2, "ALPHANUMERIC");
/*  33 */   public static final Mode STRUCTURED_APPEND = new Mode(new int[] { 0, 0, 0 }, 3, "STRUCTURED_APPEND");
/*  34 */   public static final Mode BYTE = new Mode(new int[] { 8, 16, 16 }, 4, "BYTE");
/*  35 */   public static final Mode ECI = new Mode(null, 7, "ECI");
/*  36 */   public static final Mode KANJI = new Mode(new int[] { 8, 10, 12 }, 8, "KANJI");
/*  37 */   public static final Mode FNC1_FIRST_POSITION = new Mode(null, 5, "FNC1_FIRST_POSITION");
/*  38 */   public static final Mode FNC1_SECOND_POSITION = new Mode(null, 9, "FNC1_SECOND_POSITION");
/*     */   
/*     */   private final int[] characterCountBitsForVersions;
/*     */   private final int bits;
/*     */   private final String name;
/*     */   
/*     */   private Mode(int[] characterCountBitsForVersions, int bits, String name) {
/*  45 */     this.characterCountBitsForVersions = characterCountBitsForVersions;
/*  46 */     this.bits = bits;
/*  47 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Mode forBits(int bits) {
/*  56 */     switch (bits) {
/*     */       case 0:
/*  58 */         return TERMINATOR;
/*     */       case 1:
/*  60 */         return NUMERIC;
/*     */       case 2:
/*  62 */         return ALPHANUMERIC;
/*     */       case 3:
/*  64 */         return STRUCTURED_APPEND;
/*     */       case 4:
/*  66 */         return BYTE;
/*     */       case 5:
/*  68 */         return FNC1_FIRST_POSITION;
/*     */       case 7:
/*  70 */         return ECI;
/*     */       case 8:
/*  72 */         return KANJI;
/*     */       case 9:
/*  74 */         return FNC1_SECOND_POSITION;
/*     */     } 
/*  76 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCharacterCountBits(Version version) {
/*     */     int offset;
/*  86 */     if (this.characterCountBitsForVersions == null) {
/*  87 */       throw new IllegalArgumentException("Character count doesn't apply to this mode");
/*     */     }
/*  89 */     int number = version.getVersionNumber();
/*     */     
/*  91 */     if (number <= 9) {
/*  92 */       offset = 0;
/*  93 */     } else if (number <= 26) {
/*  94 */       offset = 1;
/*     */     } else {
/*  96 */       offset = 2;
/*     */     } 
/*  98 */     return this.characterCountBitsForVersions[offset];
/*     */   }
/*     */   
/*     */   public int getBits() {
/* 102 */     return this.bits;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 106 */     return this.name;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 110 */     return this.name;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/Mode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */