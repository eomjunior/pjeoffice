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
/*    */ public class PdfTransparencyGroup
/*    */   extends PdfDictionary
/*    */ {
/*    */   public PdfTransparencyGroup() {
/* 57 */     put(PdfName.S, PdfName.TRANSPARENCY);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setIsolated(boolean isolated) {
/* 65 */     if (isolated) {
/* 66 */       put(PdfName.I, PdfBoolean.PDFTRUE);
/*    */     } else {
/* 68 */       remove(PdfName.I);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setKnockout(boolean knockout) {
/* 76 */     if (knockout) {
/* 77 */       put(PdfName.K, PdfBoolean.PDFTRUE);
/*    */     } else {
/* 79 */       remove(PdfName.K);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfTransparencyGroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */