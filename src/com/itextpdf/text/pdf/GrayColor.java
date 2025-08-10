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
/*    */ public class GrayColor
/*    */   extends ExtendedColor
/*    */ {
/*    */   private static final long serialVersionUID = -6571835680819282746L;
/*    */   private float gray;
/* 56 */   public static final GrayColor GRAYBLACK = new GrayColor(0.0F);
/* 57 */   public static final GrayColor GRAYWHITE = new GrayColor(1.0F);
/*    */   
/*    */   public GrayColor(int intGray) {
/* 60 */     this(intGray / 255.0F);
/*    */   }
/*    */   
/*    */   public GrayColor(float floatGray) {
/* 64 */     super(1, floatGray, floatGray, floatGray);
/* 65 */     this.gray = normalize(floatGray);
/*    */   }
/*    */   
/*    */   public float getGray() {
/* 69 */     return this.gray;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 73 */     return (obj instanceof GrayColor && ((GrayColor)obj).gray == this.gray);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 77 */     return Float.floatToIntBits(this.gray);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/GrayColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */