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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PdfBorderDictionary
/*    */   extends PdfDictionary
/*    */ {
/*    */   public static final int STYLE_SOLID = 0;
/*    */   public static final int STYLE_DASHED = 1;
/*    */   public static final int STYLE_BEVELED = 2;
/*    */   public static final int STYLE_INSET = 3;
/*    */   public static final int STYLE_UNDERLINE = 4;
/*    */   
/*    */   public PdfBorderDictionary(float borderWidth, int borderStyle, PdfDashPattern dashes) {
/* 68 */     put(PdfName.W, new PdfNumber(borderWidth));
/* 69 */     switch (borderStyle) {
/*    */       case 0:
/* 71 */         put(PdfName.S, PdfName.S);
/*    */         return;
/*    */       case 1:
/* 74 */         if (dashes != null)
/* 75 */           put(PdfName.D, dashes); 
/* 76 */         put(PdfName.S, PdfName.D);
/*    */         return;
/*    */       case 2:
/* 79 */         put(PdfName.S, PdfName.B);
/*    */         return;
/*    */       case 3:
/* 82 */         put(PdfName.S, PdfName.I);
/*    */         return;
/*    */       case 4:
/* 85 */         put(PdfName.S, PdfName.U);
/*    */         return;
/*    */     } 
/* 88 */     throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.border.style", new Object[0]));
/*    */   }
/*    */ 
/*    */   
/*    */   public PdfBorderDictionary(float borderWidth, int borderStyle) {
/* 93 */     this(borderWidth, borderStyle, (PdfDashPattern)null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/PdfBorderDictionary.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */