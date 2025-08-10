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
/*    */ public class SpotColor
/*    */   extends ExtendedColor
/*    */ {
/*    */   private static final long serialVersionUID = -6257004582113248079L;
/*    */   PdfSpotColor spot;
/*    */   float tint;
/*    */   
/*    */   public SpotColor(PdfSpotColor spot, float tint) {
/* 57 */     super(3, (spot
/* 58 */         .getAlternativeCS().getRed() / 255.0F - 1.0F) * tint + 1.0F, (spot
/* 59 */         .getAlternativeCS().getGreen() / 255.0F - 1.0F) * tint + 1.0F, (spot
/* 60 */         .getAlternativeCS().getBlue() / 255.0F - 1.0F) * tint + 1.0F);
/* 61 */     this.spot = spot;
/* 62 */     this.tint = tint;
/*    */   }
/*    */   
/*    */   public PdfSpotColor getPdfSpotColor() {
/* 66 */     return this.spot;
/*    */   }
/*    */   
/*    */   public float getTint() {
/* 70 */     return this.tint;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 74 */     return (obj instanceof SpotColor && ((SpotColor)obj).spot.equals(this.spot) && ((SpotColor)obj).tint == this.tint);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 78 */     return this.spot.hashCode() ^ Float.floatToIntBits(this.tint);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/SpotColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */