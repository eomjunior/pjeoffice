/*     */ package com.itextpdf.text.factories;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RomanNumberFactory
/*     */ {
/*     */   private static class RomanDigit
/*     */   {
/*     */     public char digit;
/*     */     public int value;
/*     */     public boolean pre;
/*     */     
/*     */     RomanDigit(char digit, int value, boolean pre) {
/*  70 */       this.digit = digit;
/*  71 */       this.value = value;
/*  72 */       this.pre = pre;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private static final RomanDigit[] roman = new RomanDigit[] { new RomanDigit('m', 1000, false), new RomanDigit('d', 500, false), new RomanDigit('c', 100, true), new RomanDigit('l', 50, false), new RomanDigit('x', 10, true), new RomanDigit('v', 5, false), new RomanDigit('i', 1, true) };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getString(int index) {
/*  95 */     StringBuffer buf = new StringBuffer();
/*     */ 
/*     */     
/*  98 */     if (index < 0) {
/*  99 */       buf.append('-');
/* 100 */       index = -index;
/*     */     } 
/*     */ 
/*     */     
/* 104 */     if (index > 3000) {
/* 105 */       buf.append('|');
/* 106 */       buf.append(getString(index / 1000));
/* 107 */       buf.append('|');
/*     */       
/* 109 */       index -= index / 1000 * 1000;
/*     */     } 
/*     */ 
/*     */     
/* 113 */     int pos = 0;
/*     */     
/*     */     while (true) {
/* 116 */       RomanDigit dig = roman[pos];
/*     */       
/* 118 */       while (index >= dig.value) {
/* 119 */         buf.append(dig.digit);
/* 120 */         index -= dig.value;
/*     */       } 
/*     */       
/* 123 */       if (index <= 0) {
/*     */         break;
/*     */       }
/*     */       
/* 127 */       int j = pos;
/* 128 */       while (!(roman[++j]).pre);
/*     */ 
/*     */       
/* 131 */       if (index + (roman[j]).value >= dig.value) {
/* 132 */         buf.append((roman[j]).digit).append(dig.digit);
/* 133 */         index -= dig.value - (roman[j]).value;
/*     */       } 
/* 135 */       pos++;
/*     */     } 
/* 137 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getLowerCaseString(int index) {
/* 146 */     return getString(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getUpperCaseString(int index) {
/* 155 */     return getString(index).toUpperCase();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getString(int index, boolean lowercase) {
/* 165 */     if (lowercase) {
/* 166 */       return getLowerCaseString(index);
/*     */     }
/*     */     
/* 169 */     return getUpperCaseString(index);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/factories/RomanNumberFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */