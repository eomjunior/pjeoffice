/*     */ package com.itextpdf.text.pdf.languages;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class IndicLigaturizer
/*     */   implements LanguageProcessor
/*     */ {
/*     */   public static final int MATRA_AA = 0;
/*     */   public static final int MATRA_I = 1;
/*     */   public static final int MATRA_E = 2;
/*     */   public static final int MATRA_AI = 3;
/*     */   public static final int MATRA_HLR = 4;
/*     */   public static final int MATRA_HLRR = 5;
/*     */   public static final int LETTER_A = 6;
/*     */   public static final int LETTER_AU = 7;
/*     */   public static final int LETTER_KA = 8;
/*     */   public static final int LETTER_HA = 9;
/*     */   public static final int HALANTA = 10;
/*     */   protected char[] langTable;
/*     */   
/*     */   public String process(String s) {
/*  80 */     if (s == null || s.length() == 0)
/*  81 */       return ""; 
/*  82 */     StringBuilder res = new StringBuilder();
/*     */     
/*  84 */     for (int i = 0; i < s.length(); i++) {
/*  85 */       char letter = s.charAt(i);
/*     */       
/*  87 */       if (IsVyanjana(letter) || IsSwaraLetter(letter)) {
/*  88 */         res.append(letter);
/*  89 */       } else if (IsSwaraMatra(letter)) {
/*  90 */         int prevCharIndex = res.length() - 1;
/*     */         
/*  92 */         if (prevCharIndex >= 0) {
/*     */ 
/*     */           
/*  95 */           if (res.charAt(prevCharIndex) == this.langTable[10]) {
/*  96 */             res.deleteCharAt(prevCharIndex);
/*     */           }
/*  98 */           res.append(letter);
/*  99 */           int prevPrevCharIndex = res.length() - 2;
/*     */           
/* 101 */           if (letter == this.langTable[1] && prevPrevCharIndex >= 0)
/* 102 */             swap(res, prevPrevCharIndex, res.length() - 1); 
/*     */         } else {
/* 104 */           res.append(letter);
/*     */         } 
/*     */       } else {
/* 107 */         res.append(letter);
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     return res.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRTL() {
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean IsSwaraLetter(char ch) {
/* 132 */     return (ch >= this.langTable[6] && ch <= this.langTable[7]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean IsSwaraMatra(char ch) {
/* 143 */     return ((ch >= this.langTable[0] && ch <= this.langTable[3]) || ch == this.langTable[4] || ch == this.langTable[5]);
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
/*     */   protected boolean IsVyanjana(char ch) {
/* 155 */     return (ch >= this.langTable[8] && ch <= this.langTable[9]);
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
/*     */   
/*     */   private static void swap(StringBuilder s, int i, int j) {
/* 169 */     char temp = s.charAt(i);
/* 170 */     s.setCharAt(i, s.charAt(j));
/* 171 */     s.setCharAt(j, temp);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/languages/IndicLigaturizer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */