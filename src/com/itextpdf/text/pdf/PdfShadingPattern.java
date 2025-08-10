/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import com.itextpdf.text.error_messages.MessageLocalization;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfShadingPattern
/*     */   extends PdfDictionary
/*     */ {
/*     */   protected PdfShading shading;
/*     */   protected PdfWriter writer;
/*  58 */   protected float[] matrix = new float[] { 1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F };
/*     */   
/*     */   protected PdfName patternName;
/*     */   
/*     */   protected PdfIndirectReference patternReference;
/*     */ 
/*     */   
/*     */   public PdfShadingPattern(PdfShading shading) {
/*  66 */     this.writer = shading.getWriter();
/*  67 */     put(PdfName.PATTERNTYPE, new PdfNumber(2));
/*  68 */     this.shading = shading;
/*     */   }
/*     */   
/*     */   PdfName getPatternName() {
/*  72 */     return this.patternName;
/*     */   }
/*     */   
/*     */   PdfName getShadingName() {
/*  76 */     return this.shading.getShadingName();
/*     */   }
/*     */   
/*     */   PdfIndirectReference getPatternReference() {
/*  80 */     if (this.patternReference == null)
/*  81 */       this.patternReference = this.writer.getPdfIndirectReference(); 
/*  82 */     return this.patternReference;
/*     */   }
/*     */   
/*     */   PdfIndirectReference getShadingReference() {
/*  86 */     return this.shading.getShadingReference();
/*     */   }
/*     */   
/*     */   void setName(int number) {
/*  90 */     this.patternName = new PdfName("P" + number);
/*     */   }
/*     */   
/*     */   public void addToBody() throws IOException {
/*  94 */     put(PdfName.SHADING, getShadingReference());
/*  95 */     put(PdfName.MATRIX, new PdfArray(this.matrix));
/*  96 */     this.writer.addToBody(this, getPatternReference());
/*     */   }
/*     */   
/*     */   public void setMatrix(float[] matrix) {
/* 100 */     if (matrix.length != 6)
/* 101 */       throw new RuntimeException(MessageLocalization.getComposedMessage("the.matrix.size.must.be.6", new Object[0])); 
/* 102 */     this.matrix = matrix;
/*     */   }
/*     */   
/*     */   public float[] getMatrix() {
/* 106 */     return this.matrix;
/*     */   }
/*     */   
/*     */   public PdfShading getShading() {
/* 110 */     return this.shading;
/*     */   }
/*     */   
/*     */   ColorDetails getColorDetails() {
/* 114 */     return this.shading.getColorDetails();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfShadingPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */