/*     */ package com.itextpdf.xmp.options;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ParseOptions
/*     */   extends Options
/*     */ {
/*     */   public static final int REQUIRE_XMP_META = 1;
/*     */   public static final int STRICT_ALIASING = 4;
/*     */   public static final int FIX_CONTROL_CHARS = 8;
/*     */   public static final int ACCEPT_LATIN_1 = 16;
/*     */   public static final int OMIT_NORMALIZATION = 32;
/*     */   
/*     */   public ParseOptions() {
/*  62 */     setOption(24, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRequireXMPMeta() {
/*  71 */     return getOption(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseOptions setRequireXMPMeta(boolean value) {
/*  81 */     setOption(1, value);
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getStrictAliasing() {
/*  91 */     return getOption(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseOptions setStrictAliasing(boolean value) {
/* 101 */     setOption(4, value);
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFixControlChars() {
/* 111 */     return getOption(8);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseOptions setFixControlChars(boolean value) {
/* 121 */     setOption(8, value);
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getAcceptLatin1() {
/* 131 */     return getOption(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseOptions setOmitNormalization(boolean value) {
/* 141 */     setOption(32, value);
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOmitNormalization() {
/* 151 */     return getOption(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseOptions setAcceptLatin1(boolean value) {
/* 161 */     setOption(16, value);
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String defineOptionName(int option) {
/* 171 */     switch (option) {
/*     */       case 1:
/* 173 */         return "REQUIRE_XMP_META";
/* 174 */       case 4: return "STRICT_ALIASING";
/* 175 */       case 8: return "FIX_CONTROL_CHARS";
/* 176 */       case 16: return "ACCEPT_LATIN_1";
/* 177 */       case 32: return "OMIT_NORMALIZATION";
/* 178 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getValidOptions() {
/* 188 */     return 61;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/options/ParseOptions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */