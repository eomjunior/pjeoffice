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
/*    */ public class PdfBorderArray
/*    */   extends PdfArray
/*    */ {
/*    */   public PdfBorderArray(float hRadius, float vRadius, float width) {
/* 61 */     this(hRadius, vRadius, width, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfBorderArray(float hRadius, float vRadius, float width, PdfDashPattern dash) {
/* 69 */     super(new PdfNumber(hRadius));
/* 70 */     add(new PdfNumber(vRadius));
/* 71 */     add(new PdfNumber(width));
/* 72 */     if (dash != null)
/* 73 */       add(dash); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfBorderArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */