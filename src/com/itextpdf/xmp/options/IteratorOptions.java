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
/*     */ public final class IteratorOptions
/*     */   extends Options
/*     */ {
/*     */   public static final int JUST_CHILDREN = 256;
/*     */   public static final int JUST_LEAFNODES = 512;
/*     */   public static final int JUST_LEAFNAME = 1024;
/*     */   public static final int OMIT_QUALIFIERS = 4096;
/*     */   
/*     */   public boolean isJustChildren() {
/*  61 */     return getOption(256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJustLeafname() {
/*  70 */     return getOption(1024);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isJustLeafnodes() {
/*  79 */     return getOption(512);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isOmitQualifiers() {
/*  88 */     return getOption(4096);
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
/*     */   public IteratorOptions setJustChildren(boolean value) {
/* 100 */     setOption(256, value);
/* 101 */     return this;
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
/*     */   public IteratorOptions setJustLeafname(boolean value) {
/* 113 */     setOption(1024, value);
/* 114 */     return this;
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
/*     */   public IteratorOptions setJustLeafnodes(boolean value) {
/* 126 */     setOption(512, value);
/* 127 */     return this;
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
/*     */   public IteratorOptions setOmitQualifiers(boolean value) {
/* 139 */     setOption(4096, value);
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String defineOptionName(int option) {
/* 149 */     switch (option) {
/*     */       case 256:
/* 151 */         return "JUST_CHILDREN";
/* 152 */       case 512: return "JUST_LEAFNODES";
/* 153 */       case 1024: return "JUST_LEAFNAME";
/* 154 */       case 4096: return "OMIT_QUALIFIERS";
/* 155 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getValidOptions() {
/* 165 */     return 5888;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/options/IteratorOptions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */