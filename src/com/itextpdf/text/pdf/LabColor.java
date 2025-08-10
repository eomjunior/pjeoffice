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
/*    */ public class LabColor
/*    */   extends ExtendedColor
/*    */ {
/*    */   PdfLabColor labColorSpace;
/*    */   private float l;
/*    */   private float a;
/*    */   private float b;
/*    */   
/*    */   public LabColor(PdfLabColor labColorSpace, float l, float a, float b) {
/* 55 */     super(7);
/* 56 */     this.labColorSpace = labColorSpace;
/* 57 */     this.l = l;
/* 58 */     this.a = a;
/* 59 */     this.b = b;
/* 60 */     BaseColor altRgbColor = labColorSpace.lab2Rgb(l, a, b);
/* 61 */     setValue(altRgbColor.getRed(), altRgbColor.getGreen(), altRgbColor.getBlue(), 255);
/*    */   }
/*    */   
/*    */   public PdfLabColor getLabColorSpace() {
/* 65 */     return this.labColorSpace;
/*    */   }
/*    */   
/*    */   public float getL() {
/* 69 */     return this.l;
/*    */   }
/*    */   
/*    */   public float getA() {
/* 73 */     return this.a;
/*    */   }
/*    */   
/*    */   public float getB() {
/* 77 */     return this.b;
/*    */   }
/*    */   
/*    */   public BaseColor toRgb() {
/* 81 */     return this.labColorSpace.lab2Rgb(this.l, this.a, this.b);
/*    */   }
/*    */   
/*    */   CMYKColor toCmyk() {
/* 85 */     return this.labColorSpace.lab2Cmyk(this.l, this.a, this.b);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/LabColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */