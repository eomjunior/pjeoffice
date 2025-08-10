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
/*    */ public class PatternColor
/*    */   extends ExtendedColor
/*    */ {
/*    */   private static final long serialVersionUID = -1185448552860615964L;
/*    */   PdfPatternPainter painter;
/*    */   
/*    */   public PatternColor(PdfPatternPainter painter) {
/* 59 */     super(4, 0.5F, 0.5F, 0.5F);
/* 60 */     this.painter = painter;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfPatternPainter getPainter() {
/* 67 */     return this.painter;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 71 */     return (obj instanceof PatternColor && ((PatternColor)obj).painter.equals(this.painter));
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 75 */     return this.painter.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PatternColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */