/*    */ package com.itextpdf.text.pdf;
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
/*    */ public class PdfFormXObject
/*    */   extends PdfStream
/*    */ {
/* 55 */   public static final PdfNumber ZERO = new PdfNumber(0);
/*    */ 
/*    */   
/* 58 */   public static final PdfNumber ONE = new PdfNumber(1);
/*    */ 
/*    */   
/* 61 */   public static final PdfLiteral MATRIX = new PdfLiteral("[1 0 0 1 0 0]");
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
/*    */   PdfFormXObject(PdfTemplate template, int compressionLevel) {
/* 74 */     put(PdfName.TYPE, PdfName.XOBJECT);
/* 75 */     put(PdfName.SUBTYPE, PdfName.FORM);
/* 76 */     put(PdfName.RESOURCES, template.getResources());
/* 77 */     put(PdfName.BBOX, new PdfRectangle(template.getBoundingBox()));
/* 78 */     put(PdfName.FORMTYPE, ONE);
/* 79 */     if (template.getLayer() != null)
/* 80 */       put(PdfName.OC, template.getLayer().getRef()); 
/* 81 */     if (template.getGroup() != null)
/* 82 */       put(PdfName.GROUP, template.getGroup()); 
/* 83 */     PdfArray matrix = template.getMatrix();
/* 84 */     if (matrix == null) {
/* 85 */       put(PdfName.MATRIX, MATRIX);
/*    */     } else {
/* 87 */       put(PdfName.MATRIX, matrix);
/* 88 */     }  this.bytes = template.toPdf(null);
/* 89 */     put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/* 90 */     if (template.getAdditional() != null) {
/* 91 */       putAll(template.getAdditional());
/*    */     }
/* 93 */     flateCompress(compressionLevel);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfFormXObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */