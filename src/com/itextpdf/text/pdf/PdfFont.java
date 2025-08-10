/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.ExceptionConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PdfFont
/*     */   implements Comparable<PdfFont>
/*     */ {
/*     */   private BaseFont font;
/*     */   private float size;
/*  73 */   protected float hScale = 1.0F;
/*     */ 
/*     */ 
/*     */   
/*     */   PdfFont(BaseFont bf, float size) {
/*  78 */     this.size = size;
/*  79 */     this.font = bf;
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
/*     */   public int compareTo(PdfFont pdfFont) {
/*  92 */     if (pdfFont == null) {
/*  93 */       return -1;
/*     */     }
/*     */     try {
/*  96 */       if (this.font != pdfFont.font) {
/*  97 */         return 1;
/*     */       }
/*  99 */       if (size() != pdfFont.size()) {
/* 100 */         return 2;
/*     */       }
/* 102 */       return 0;
/*     */     }
/* 104 */     catch (ClassCastException cce) {
/* 105 */       return -2;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float size() {
/* 116 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float width() {
/* 126 */     return width(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float width(int character) {
/* 137 */     return this.font.getWidthPoint(character, this.size) * this.hScale;
/*     */   }
/*     */   
/*     */   float width(String s) {
/* 141 */     return this.font.getWidthPoint(s, this.size) * this.hScale;
/*     */   }
/*     */   
/*     */   BaseFont getFont() {
/* 145 */     return this.font;
/*     */   }
/*     */   
/*     */   static PdfFont getDefaultFont() {
/*     */     try {
/* 150 */       BaseFont bf = BaseFont.createFont("Helvetica", "Cp1252", false);
/* 151 */       return new PdfFont(bf, 12.0F);
/*     */     }
/* 153 */     catch (Exception ee) {
/* 154 */       throw new ExceptionConverter(ee);
/*     */     } 
/*     */   }
/*     */   void setHorizontalScaling(float hScale) {
/* 158 */     this.hScale = hScale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   float getHorizontalScaling() {
/* 165 */     return this.hScale;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfFont.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */