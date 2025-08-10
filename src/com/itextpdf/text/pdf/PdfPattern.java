/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import com.itextpdf.text.ExceptionConverter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PdfPattern
/*    */   extends PdfStream
/*    */ {
/*    */   PdfPattern(PdfPatternPainter painter) {
/* 61 */     this(painter, -1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   PdfPattern(PdfPatternPainter painter, int compressionLevel) {
/* 72 */     PdfNumber one = new PdfNumber(1);
/* 73 */     PdfArray matrix = painter.getMatrix();
/* 74 */     if (matrix != null) {
/* 75 */       put(PdfName.MATRIX, matrix);
/*    */     }
/* 77 */     put(PdfName.TYPE, PdfName.PATTERN);
/* 78 */     put(PdfName.BBOX, new PdfRectangle(painter.getBoundingBox()));
/* 79 */     put(PdfName.RESOURCES, painter.getResources());
/* 80 */     put(PdfName.TILINGTYPE, one);
/* 81 */     put(PdfName.PATTERNTYPE, one);
/* 82 */     if (painter.isStencil()) {
/* 83 */       put(PdfName.PAINTTYPE, new PdfNumber(2));
/*    */     } else {
/* 85 */       put(PdfName.PAINTTYPE, one);
/* 86 */     }  put(PdfName.XSTEP, new PdfNumber(painter.getXStep()));
/* 87 */     put(PdfName.YSTEP, new PdfNumber(painter.getYStep()));
/* 88 */     this.bytes = painter.toPdf(null);
/* 89 */     put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/*    */     try {
/* 91 */       flateCompress(compressionLevel);
/* 92 */     } catch (Exception e) {
/* 93 */       throw new ExceptionConverter(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPattern.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */