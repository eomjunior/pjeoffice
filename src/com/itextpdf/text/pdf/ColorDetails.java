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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ColorDetails
/*    */ {
/*    */   PdfIndirectReference indirectReference;
/*    */   PdfName colorSpaceName;
/*    */   ICachedColorSpace colorSpace;
/*    */   
/*    */   ColorDetails(PdfName colorName, PdfIndirectReference indirectReference, ICachedColorSpace scolor) {
/* 68 */     this.colorSpaceName = colorName;
/* 69 */     this.indirectReference = indirectReference;
/* 70 */     this.colorSpace = scolor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfIndirectReference getIndirectReference() {
/* 77 */     return this.indirectReference;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   PdfName getColorSpaceName() {
/* 84 */     return this.colorSpaceName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfObject getPdfObject(PdfWriter writer) {
/* 91 */     return this.colorSpace.getPdfObject(writer);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ColorDetails.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */