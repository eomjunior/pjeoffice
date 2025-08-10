/*     */ package com.itextpdf.text.pdf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PdfDashPattern
/*     */   extends PdfArray
/*     */ {
/*  61 */   private float dash = -1.0F;
/*     */ 
/*     */   
/*  64 */   private float gap = -1.0F;
/*     */ 
/*     */   
/*  67 */   private float phase = -1.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDashPattern() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDashPattern(float dash) {
/*  84 */     super(new PdfNumber(dash));
/*  85 */     this.dash = dash;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDashPattern(float dash, float gap) {
/*  93 */     super(new PdfNumber(dash));
/*  94 */     add(new PdfNumber(gap));
/*  95 */     this.dash = dash;
/*  96 */     this.gap = gap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PdfDashPattern(float dash, float gap, float phase) {
/* 104 */     super(new PdfNumber(dash));
/* 105 */     add(new PdfNumber(gap));
/* 106 */     this.dash = dash;
/* 107 */     this.gap = gap;
/* 108 */     this.phase = phase;
/*     */   }
/*     */   
/*     */   public void add(float n) {
/* 112 */     add(new PdfNumber(n));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
/* 120 */     os.write(91);
/*     */     
/* 122 */     if (this.dash >= 0.0F) {
/* 123 */       (new PdfNumber(this.dash)).toPdf(writer, os);
/* 124 */       if (this.gap >= 0.0F) {
/* 125 */         os.write(32);
/* 126 */         (new PdfNumber(this.gap)).toPdf(writer, os);
/*     */       } 
/*     */     } 
/* 129 */     os.write(93);
/* 130 */     if (this.phase >= 0.0F) {
/* 131 */       os.write(32);
/* 132 */       (new PdfNumber(this.phase)).toPdf(writer, os);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfDashPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */