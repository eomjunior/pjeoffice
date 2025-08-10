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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ErrorCorrectionLevel
/*    */ {
/* 33 */   public static final ErrorCorrectionLevel L = new ErrorCorrectionLevel(0, 1, "L");
/*    */ 
/*    */ 
/*    */   
/* 37 */   public static final ErrorCorrectionLevel M = new ErrorCorrectionLevel(1, 0, "M");
/*    */ 
/*    */ 
/*    */   
/* 41 */   public static final ErrorCorrectionLevel Q = new ErrorCorrectionLevel(2, 3, "Q");
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static final ErrorCorrectionLevel H = new ErrorCorrectionLevel(3, 2, "H");
/*    */   
/* 47 */   private static final ErrorCorrectionLevel[] FOR_BITS = new ErrorCorrectionLevel[] { M, L, H, Q };
/*    */   
/*    */   private final int ordinal;
/*    */   private final int bits;
/*    */   private final String name;
/*    */   
/*    */   private ErrorCorrectionLevel(int ordinal, int bits, String name) {
/* 54 */     this.ordinal = ordinal;
/* 55 */     this.bits = bits;
/* 56 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int ordinal() {
/* 60 */     return this.ordinal;
/*    */   }
/*    */   
/*    */   public int getBits() {
/* 64 */     return this.bits;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 68 */     return this.name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 72 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ErrorCorrectionLevel forBits(int bits) {
/* 80 */     if (bits < 0 || bits >= FOR_BITS.length) {
/* 81 */       throw new IllegalArgumentException();
/*    */     }
/* 83 */     return FOR_BITS[bits];
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/ErrorCorrectionLevel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */