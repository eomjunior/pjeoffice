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
/*    */ public class ShadingColor
/*    */   extends ExtendedColor
/*    */ {
/*    */   private static final long serialVersionUID = 4817929454941328671L;
/*    */   PdfShadingPattern shadingPattern;
/*    */   
/*    */   public ShadingColor(PdfShadingPattern shadingPattern) {
/* 60 */     super(5, 0.5F, 0.5F, 0.5F);
/* 61 */     this.shadingPattern = shadingPattern;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfShadingPattern getPdfShadingPattern() {
/* 69 */     return this.shadingPattern;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 73 */     return (obj instanceof ShadingColor && ((ShadingColor)obj).shadingPattern.equals(this.shadingPattern));
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 77 */     return this.shadingPattern.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/ShadingColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */