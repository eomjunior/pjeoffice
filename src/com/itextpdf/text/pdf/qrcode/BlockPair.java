/*    */ package com.itextpdf.text.pdf.qrcode;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class BlockPair
/*    */ {
/*    */   private final ByteArray dataBytes;
/*    */   private final ByteArray errorCorrectionBytes;
/*    */   
/*    */   BlockPair(ByteArray data, ByteArray errorCorrection) {
/* 27 */     this.dataBytes = data;
/* 28 */     this.errorCorrectionBytes = errorCorrection;
/*    */   }
/*    */   
/*    */   public ByteArray getDataBytes() {
/* 32 */     return this.dataBytes;
/*    */   }
/*    */   
/*    */   public ByteArray getErrorCorrectionBytes() {
/* 36 */     return this.errorCorrectionBytes;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/BlockPair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */