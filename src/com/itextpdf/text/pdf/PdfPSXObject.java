/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public class PdfPSXObject
/*    */   extends PdfTemplate
/*    */ {
/*    */   protected PdfPSXObject() {}
/*    */   
/*    */   public PdfPSXObject(PdfWriter wr) {
/* 63 */     super(wr);
/*    */   }
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
/*    */   public PdfStream getFormXObject(int compressionLevel) throws IOException {
/* 76 */     PdfStream s = new PdfStream(this.content.toByteArray());
/* 77 */     s.put(PdfName.TYPE, PdfName.XOBJECT);
/* 78 */     s.put(PdfName.SUBTYPE, PdfName.PS);
/* 79 */     s.flateCompress(compressionLevel);
/* 80 */     return s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfContentByte getDuplicate() {
/* 90 */     PdfPSXObject tpl = new PdfPSXObject();
/* 91 */     tpl.writer = this.writer;
/* 92 */     tpl.pdf = this.pdf;
/* 93 */     tpl.thisReference = this.thisReference;
/* 94 */     tpl.pageResources = this.pageResources;
/* 95 */     tpl.separator = this.separator;
/* 96 */     return tpl;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPSXObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */