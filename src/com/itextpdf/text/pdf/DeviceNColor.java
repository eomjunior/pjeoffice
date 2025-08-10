/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import com.itextpdf.text.error_messages.MessageLocalization;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeviceNColor
/*    */   extends ExtendedColor
/*    */ {
/*    */   PdfDeviceNColor pdfDeviceNColor;
/*    */   float[] tints;
/*    */   
/*    */   public DeviceNColor(PdfDeviceNColor pdfDeviceNColor, float[] tints) {
/* 52 */     super(6);
/* 53 */     if ((pdfDeviceNColor.getSpotColors()).length != tints.length)
/* 54 */       throw new RuntimeException(MessageLocalization.getComposedMessage("devicen.color.shall.have.the.same.number.of.colorants.as.the.destination.DeviceN.color.space", new Object[0])); 
/* 55 */     this.pdfDeviceNColor = pdfDeviceNColor;
/* 56 */     this.tints = tints;
/*    */   }
/*    */   
/*    */   public PdfDeviceNColor getPdfDeviceNColor() {
/* 60 */     return this.pdfDeviceNColor;
/*    */   }
/*    */   
/*    */   public float[] getTints() {
/* 64 */     return this.tints;
/*    */   }
/*    */   public boolean equals(Object obj) {
/* 67 */     if (obj instanceof DeviceNColor && ((DeviceNColor)obj).tints.length == this.tints.length) {
/* 68 */       int i = 0;
/* 69 */       for (float tint : this.tints) {
/* 70 */         if (tint != ((DeviceNColor)obj).tints[i])
/* 71 */           return false; 
/* 72 */         i++;
/*    */       } 
/* 74 */       return true;
/*    */     } 
/* 76 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 80 */     int hashCode = this.pdfDeviceNColor.hashCode(); float[] arrayOfFloat; int i; byte b;
/* 81 */     for (arrayOfFloat = this.tints, i = arrayOfFloat.length, b = 0; b < i; ) { Float tint = Float.valueOf(arrayOfFloat[b]);
/* 82 */       hashCode ^= tint.hashCode(); b++; }
/*    */     
/* 84 */     return hashCode;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/DeviceNColor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */