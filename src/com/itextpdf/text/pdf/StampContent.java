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
/*    */ public class StampContent
/*    */   extends PdfContentByte
/*    */ {
/*    */   PdfStamperImp.PageStamp ps;
/*    */   PageResources pageResources;
/*    */   
/*    */   StampContent(PdfStamperImp stamper, PdfStamperImp.PageStamp ps) {
/* 52 */     super(stamper);
/* 53 */     this.ps = ps;
/* 54 */     this.pageResources = ps.pageResources;
/*    */   }
/*    */   
/*    */   public void setAction(PdfAction action, float llx, float lly, float urx, float ury) {
/* 58 */     ((PdfStamperImp)this.writer).addAnnotation(this.writer.createAnnotation(llx, lly, urx, ury, action, null), this.ps.pageN);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfContentByte getDuplicate() {
/* 68 */     return new StampContent((PdfStamperImp)this.writer, this.ps);
/*    */   }
/*    */   
/*    */   PageResources getPageResources() {
/* 72 */     return this.pageResources;
/*    */   }
/*    */   
/*    */   void addAnnotation(PdfAnnotation annot) {
/* 76 */     ((PdfStamperImp)this.writer).addAnnotation(annot, this.ps.pageN);
/*    */   }
/*    */ 
/*    */   
/*    */   protected PdfIndirectReference getCurrentPage() {
/* 81 */     return this.ps.pageN.getIndRef();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/StampContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */