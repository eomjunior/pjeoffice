/*    */ package com.itextpdf.text.pdf;
/*    */ 
/*    */ import com.itextpdf.text.ExceptionConverter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PdfICCBased
/*    */   extends PdfStream
/*    */ {
/*    */   public PdfICCBased(ICC_Profile profile) {
/* 63 */     this(profile, -1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PdfICCBased(ICC_Profile profile, int compressionLevel) {
/*    */     try {
/* 77 */       int numberOfComponents = profile.getNumComponents();
/* 78 */       switch (numberOfComponents) {
/*    */         case 1:
/* 80 */           put(PdfName.ALTERNATE, PdfName.DEVICEGRAY);
/*    */           break;
/*    */         case 3:
/* 83 */           put(PdfName.ALTERNATE, PdfName.DEVICERGB);
/*    */           break;
/*    */         case 4:
/* 86 */           put(PdfName.ALTERNATE, PdfName.DEVICECMYK);
/*    */           break;
/*    */         default:
/* 89 */           throw new PdfException(MessageLocalization.getComposedMessage("1.component.s.is.not.supported", numberOfComponents));
/*    */       } 
/* 91 */       put(PdfName.N, new PdfNumber(numberOfComponents));
/* 92 */       this.bytes = profile.getData();
/* 93 */       put(PdfName.LENGTH, new PdfNumber(this.bytes.length));
/* 94 */       flateCompress(compressionLevel);
/* 95 */     } catch (Exception e) {
/* 96 */       throw new ExceptionConverter(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfICCBased.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */