/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import com.itextpdf.text.BaseColor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PdfColor
/*    */   extends PdfArray
/*    */ {
/*    */   PdfColor(int red, int green, int blue) {
/* 67 */     super(new PdfNumber((red & 0xFF) / 255.0D));
/* 68 */     add(new PdfNumber((green & 0xFF) / 255.0D));
/* 69 */     add(new PdfNumber((blue & 0xFF) / 255.0D));
/*    */   }
/*    */   
/*    */   PdfColor(BaseColor color) {
/* 73 */     this(color.getRed(), color.getGreen(), color.getBlue());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */