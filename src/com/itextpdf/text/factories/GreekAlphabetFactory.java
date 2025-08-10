/*     */ package com.itextpdf.text.factories;
/*     */ 
/*     */ import com.itextpdf.text.SpecialSymbol;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GreekAlphabetFactory
/*     */ {
/*     */   public static final String getString(int index) {
/*  65 */     return getString(index, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getLowerCaseString(int index) {
/*  74 */     return getString(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getUpperCaseString(int index) {
/*  83 */     return getString(index).toUpperCase();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getString(int index, boolean lowercase) {
/*  93 */     if (index < 1) return ""; 
/*  94 */     index--;
/*     */     
/*  96 */     int bytes = 1;
/*  97 */     int start = 0;
/*  98 */     int symbols = 24;
/*  99 */     while (index >= symbols + start) {
/* 100 */       bytes++;
/* 101 */       start += symbols;
/* 102 */       symbols *= 24;
/*     */     } 
/*     */     
/* 105 */     int c = index - start;
/* 106 */     char[] value = new char[bytes];
/* 107 */     while (bytes > 0) {
/* 108 */       bytes--;
/* 109 */       value[bytes] = (char)(c % 24);
/* 110 */       if (value[bytes] > '\020') value[bytes] = (char)(value[bytes] + 1); 
/* 111 */       value[bytes] = (char)(value[bytes] + (lowercase ? 945 : 913));
/* 112 */       value[bytes] = SpecialSymbol.getCorrespondingSymbol(value[bytes]);
/* 113 */       c /= 24;
/*     */     } 
/*     */     
/* 116 */     return String.valueOf(value);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/factories/GreekAlphabetFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */