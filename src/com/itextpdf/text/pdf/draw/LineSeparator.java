/*     */ package com.itextpdf.text.pdf.draw;
/*     */ 
/*     */ import com.itextpdf.text.BaseColor;
/*     */ import com.itextpdf.text.Font;
/*     */ import com.itextpdf.text.pdf.PdfContentByte;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LineSeparator
/*     */   extends VerticalPositionMark
/*     */ {
/*  62 */   protected float lineWidth = 1.0F;
/*     */   
/*  64 */   protected float percentage = 100.0F;
/*     */   
/*     */   protected BaseColor lineColor;
/*     */   
/*  68 */   protected int alignment = 6;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineSeparator(float lineWidth, float percentage, BaseColor lineColor, int align, float offset) {
/*  79 */     this.lineWidth = lineWidth;
/*  80 */     this.percentage = percentage;
/*  81 */     this.lineColor = lineColor;
/*  82 */     this.alignment = align;
/*  83 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineSeparator(Font font) {
/*  91 */     this.lineWidth = 0.06666667F * font.getSize();
/*  92 */     this.offset = -0.33333334F * font.getSize();
/*  93 */     this.percentage = 100.0F;
/*  94 */     this.lineColor = font.getColor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LineSeparator() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
/* 108 */     canvas.saveState();
/* 109 */     drawLine(canvas, llx, urx, y);
/* 110 */     canvas.restoreState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawLine(PdfContentByte canvas, float leftX, float rightX, float y) {
/*     */     float w, s;
/* 122 */     if (getPercentage() < 0.0F) {
/* 123 */       w = -getPercentage();
/*     */     } else {
/* 125 */       w = (rightX - leftX) * getPercentage() / 100.0F;
/*     */     } 
/* 127 */     switch (getAlignment()) {
/*     */       case 0:
/* 129 */         s = 0.0F;
/*     */         break;
/*     */       case 2:
/* 132 */         s = rightX - leftX - w;
/*     */         break;
/*     */       default:
/* 135 */         s = (rightX - leftX - w) / 2.0F;
/*     */         break;
/*     */     } 
/* 138 */     canvas.setLineWidth(getLineWidth());
/* 139 */     if (getLineColor() != null)
/* 140 */       canvas.setColorStroke(getLineColor()); 
/* 141 */     canvas.moveTo(s + leftX, y + this.offset);
/* 142 */     canvas.lineTo(s + w + leftX, y + this.offset);
/* 143 */     canvas.stroke();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getLineWidth() {
/* 151 */     return this.lineWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineWidth(float lineWidth) {
/* 159 */     this.lineWidth = lineWidth;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPercentage() {
/* 167 */     return this.percentage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPercentage(float percentage) {
/* 175 */     this.percentage = percentage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseColor getLineColor() {
/* 183 */     return this.lineColor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineColor(BaseColor color) {
/* 191 */     this.lineColor = color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAlignment() {
/* 199 */     return this.alignment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlignment(int align) {
/* 207 */     this.alignment = align;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/draw/LineSeparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */