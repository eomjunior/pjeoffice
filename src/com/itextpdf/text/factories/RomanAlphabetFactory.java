/*     */ package com.itextpdf.text.factories;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RomanAlphabetFactory
/*     */ {
/*     */   public static final String getString(int index) {
/*  63 */     if (index < 1) throw new NumberFormatException(MessageLocalization.getComposedMessage("you.can.t.translate.a.negative.number.into.an.alphabetical.value", new Object[0]));
/*     */     
/*  65 */     index--;
/*  66 */     int bytes = 1;
/*  67 */     int start = 0;
/*  68 */     int symbols = 26;
/*  69 */     while (index >= symbols + start) {
/*  70 */       bytes++;
/*  71 */       start += symbols;
/*  72 */       symbols *= 26;
/*     */     } 
/*     */     
/*  75 */     int c = index - start;
/*  76 */     char[] value = new char[bytes];
/*  77 */     while (bytes > 0) {
/*  78 */       value[--bytes] = (char)(97 + c % 26);
/*  79 */       c /= 26;
/*     */     } 
/*     */     
/*  82 */     return new String(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getLowerCaseString(int index) {
/*  93 */     return getString(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getUpperCaseString(int index) {
/* 104 */     return getString(index).toUpperCase();
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
/*     */   public static final String getString(int index, boolean lowercase) {
/* 117 */     if (lowercase) {
/* 118 */       return getLowerCaseString(index);
/*     */     }
/*     */     
/* 121 */     return getUpperCaseString(index);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/factories/RomanAlphabetFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */