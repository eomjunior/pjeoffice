/*    */ package com.itextpdf.text.pdf.draw;
/*    */ 
/*    */ import com.itextpdf.text.pdf.PdfContentByte;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DottedLineSeparator
/*    */   extends LineSeparator
/*    */ {
/* 57 */   protected float gap = 5.0F;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y) {
/* 63 */     canvas.saveState();
/* 64 */     canvas.setLineWidth(this.lineWidth);
/* 65 */     canvas.setLineCap(1);
/* 66 */     canvas.setLineDash(0.0F, this.gap, this.gap / 2.0F);
/* 67 */     drawLine(canvas, llx, urx, y);
/* 68 */     canvas.restoreState();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public float getGap() {
/* 76 */     return this.gap;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setGap(float gap) {
/* 84 */     this.gap = gap;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/draw/DottedLineSeparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */