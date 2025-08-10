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
/*    */ public class PdfPTableHeader
/*    */   extends PdfPTableBody
/*    */ {
/* 48 */   protected PdfName role = PdfName.THEAD;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfName getRole() {
/* 55 */     return this.role;
/*    */   }
/*    */   
/*    */   public void setRole(PdfName role) {
/* 59 */     this.role = role;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfPTableHeader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */