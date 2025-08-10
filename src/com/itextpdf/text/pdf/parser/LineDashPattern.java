/*     */ package com.itextpdf.text.pdf.parser;
/*     */ 
/*     */ import com.itextpdf.text.pdf.PdfArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LineDashPattern
/*     */ {
/*     */   private PdfArray dashArray;
/*     */   private float dashPhase;
/*     */   private int currentIndex;
/*  59 */   private int elemOrdinalNumber = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   private DashArrayElem currentElem;
/*     */ 
/*     */ 
/*     */   
/*     */   public LineDashPattern(PdfArray dashArray, float dashPhase) {
/*  68 */     this.dashArray = new PdfArray(dashArray);
/*  69 */     this.dashPhase = dashPhase;
/*  70 */     initFirst(dashPhase);
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
/*     */   public PdfArray getDashArray() {
/*  83 */     return this.dashArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDashArray(PdfArray dashArray) {
/*  91 */     this.dashArray = dashArray;
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
/*     */   public float getDashPhase() {
/* 103 */     return this.dashPhase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDashPhase(float dashPhase) {
/* 111 */     this.dashPhase = dashPhase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DashArrayElem next() {
/* 119 */     DashArrayElem ret = this.currentElem;
/*     */     
/* 121 */     if (this.dashArray.size() > 0) {
/* 122 */       this.currentIndex = (this.currentIndex + 1) % this.dashArray.size();
/* 123 */       this
/* 124 */         .currentElem = new DashArrayElem(this.dashArray.getAsNumber(this.currentIndex).floatValue(), isEven(++this.elemOrdinalNumber));
/*     */     } 
/*     */     
/* 127 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 135 */     this.currentIndex = 0;
/* 136 */     this.elemOrdinalNumber = 1;
/* 137 */     initFirst(this.dashPhase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSolid() {
/* 147 */     if (this.dashArray.size() % 2 != 0) {
/* 148 */       return false;
/*     */     }
/*     */     
/* 151 */     float unitsOffSum = 0.0F;
/*     */     
/* 153 */     for (int i = 1; i < this.dashArray.size(); i += 2) {
/* 154 */       unitsOffSum += this.dashArray.getAsNumber(i).floatValue();
/*     */     }
/*     */     
/* 157 */     return (Float.compare(unitsOffSum, 0.0F) == 0);
/*     */   }
/*     */   
/*     */   private void initFirst(float phase) {
/* 161 */     if (this.dashArray.size() > 0) {
/* 162 */       while (phase > 0.0F) {
/* 163 */         phase -= this.dashArray.getAsNumber(this.currentIndex).floatValue();
/* 164 */         this.currentIndex = (this.currentIndex + 1) % this.dashArray.size();
/* 165 */         this.elemOrdinalNumber++;
/*     */       } 
/*     */       
/* 168 */       if (phase < 0.0F) {
/* 169 */         this.elemOrdinalNumber--;
/* 170 */         this.currentIndex--;
/* 171 */         this.currentElem = new DashArrayElem(-phase, isEven(this.elemOrdinalNumber));
/*     */       } else {
/* 173 */         this
/* 174 */           .currentElem = new DashArrayElem(this.dashArray.getAsNumber(this.currentIndex).floatValue(), isEven(this.elemOrdinalNumber));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isEven(int num) {
/* 180 */     return (num % 2 == 0);
/*     */   }
/*     */   
/*     */   public class DashArrayElem
/*     */   {
/*     */     private float val;
/*     */     private boolean isGap;
/*     */     
/*     */     public DashArrayElem(float val, boolean isGap) {
/* 189 */       this.val = val;
/* 190 */       this.isGap = isGap;
/*     */     }
/*     */     
/*     */     public float getVal() {
/* 194 */       return this.val;
/*     */     }
/*     */     
/*     */     public void setVal(float val) {
/* 198 */       this.val = val;
/*     */     }
/*     */     
/*     */     public boolean isGap() {
/* 202 */       return this.isGap;
/*     */     }
/*     */     
/*     */     public void setGap(boolean isGap) {
/* 206 */       this.isGap = isGap;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/parser/LineDashPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */