/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.pdf.hyphenation.Hyphenation;
/*     */ import com.itextpdf.text.pdf.hyphenation.Hyphenator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HyphenationAuto
/*     */   implements HyphenationEvent
/*     */ {
/*     */   protected Hyphenator hyphenator;
/*     */   protected String post;
/*     */   
/*     */   public HyphenationAuto(String lang, String country, int leftMin, int rightMin) {
/*  71 */     this.hyphenator = new Hyphenator(lang, country, leftMin, rightMin);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHyphenSymbol() {
/*  78 */     return "-";
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
/*     */   public String getHyphenatedWordPre(String word, BaseFont font, float fontSize, float remainingWidth) {
/*  91 */     this.post = word;
/*  92 */     String hyphen = getHyphenSymbol();
/*  93 */     float hyphenWidth = font.getWidthPoint(hyphen, fontSize);
/*  94 */     if (hyphenWidth > remainingWidth)
/*  95 */       return ""; 
/*  96 */     Hyphenation hyphenation = this.hyphenator.hyphenate(word);
/*  97 */     if (hyphenation == null) {
/*  98 */       return "";
/*     */     }
/* 100 */     int len = hyphenation.length();
/*     */     int k;
/* 102 */     for (k = 0; k < len && 
/* 103 */       font.getWidthPoint(hyphenation.getPreHyphenText(k), fontSize) + hyphenWidth <= remainingWidth; k++);
/*     */ 
/*     */     
/* 106 */     k--;
/* 107 */     if (k < 0)
/* 108 */       return ""; 
/* 109 */     this.post = hyphenation.getPostHyphenText(k);
/* 110 */     return hyphenation.getPreHyphenText(k) + hyphen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHyphenatedWordPost() {
/* 118 */     return this.post;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/HyphenationAuto.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */