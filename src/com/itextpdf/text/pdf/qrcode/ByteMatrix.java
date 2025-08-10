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
/*    */ public final class ByteMatrix
/*    */ {
/*    */   private final byte[][] bytes;
/*    */   private final int width;
/*    */   private final int height;
/*    */   
/*    */   public ByteMatrix(int width, int height) {
/* 36 */     this.bytes = new byte[height][width];
/* 37 */     this.width = width;
/* 38 */     this.height = height;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 42 */     return this.height;
/*    */   }
/*    */   
/*    */   public int getWidth() {
/* 46 */     return this.width;
/*    */   }
/*    */   
/*    */   public byte get(int x, int y) {
/* 50 */     return this.bytes[y][x];
/*    */   }
/*    */   
/*    */   public byte[][] getArray() {
/* 54 */     return this.bytes;
/*    */   }
/*    */   
/*    */   public void set(int x, int y, byte value) {
/* 58 */     this.bytes[y][x] = value;
/*    */   }
/*    */   
/*    */   public void set(int x, int y, int value) {
/* 62 */     this.bytes[y][x] = (byte)value;
/*    */   }
/*    */   
/*    */   public void clear(byte value) {
/* 66 */     for (int y = 0; y < this.height; y++) {
/* 67 */       for (int x = 0; x < this.width; x++) {
/* 68 */         this.bytes[y][x] = value;
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 74 */     StringBuffer result = new StringBuffer(2 * this.width * this.height + 2);
/* 75 */     for (int y = 0; y < this.height; y++) {
/* 76 */       for (int x = 0; x < this.width; x++) {
/* 77 */         switch (this.bytes[y][x]) {
/*    */           case 0:
/* 79 */             result.append(" 0");
/*    */             break;
/*    */           case 1:
/* 82 */             result.append(" 1");
/*    */             break;
/*    */           default:
/* 85 */             result.append("  ");
/*    */             break;
/*    */         } 
/*    */       } 
/* 89 */       result.append('\n');
/*    */     } 
/* 91 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/ByteMatrix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */