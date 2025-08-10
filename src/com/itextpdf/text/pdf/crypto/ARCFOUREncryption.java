/*    */ package com.itextpdf.text.pdf.crypto;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ARCFOUREncryption
/*    */ {
/* 47 */   private byte[] state = new byte[256];
/*    */ 
/*    */   
/*    */   private int x;
/*    */   
/*    */   private int y;
/*    */ 
/*    */   
/*    */   public void prepareARCFOURKey(byte[] key) {
/* 56 */     prepareARCFOURKey(key, 0, key.length);
/*    */   }
/*    */   
/*    */   public void prepareARCFOURKey(byte[] key, int off, int len) {
/* 60 */     int index1 = 0;
/* 61 */     int index2 = 0;
/* 62 */     for (int k = 0; k < 256; k++)
/* 63 */       this.state[k] = (byte)k; 
/* 64 */     this.x = 0;
/* 65 */     this.y = 0;
/*    */     
/* 67 */     for (int i = 0; i < 256; i++) {
/* 68 */       index2 = key[index1 + off] + this.state[i] + index2 & 0xFF;
/* 69 */       byte tmp = this.state[i];
/* 70 */       this.state[i] = this.state[index2];
/* 71 */       this.state[index2] = tmp;
/* 72 */       index1 = (index1 + 1) % len;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void encryptARCFOUR(byte[] dataIn, int off, int len, byte[] dataOut, int offOut) {
/* 77 */     int length = len + off;
/*    */     
/* 79 */     for (int k = off; k < length; k++) {
/* 80 */       this.x = this.x + 1 & 0xFF;
/* 81 */       this.y = this.state[this.x] + this.y & 0xFF;
/* 82 */       byte tmp = this.state[this.x];
/* 83 */       this.state[this.x] = this.state[this.y];
/* 84 */       this.state[this.y] = tmp;
/* 85 */       dataOut[k - off + offOut] = (byte)(dataIn[k] ^ this.state[this.state[this.x] + this.state[this.y] & 0xFF]);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void encryptARCFOUR(byte[] data, int off, int len) {
/* 90 */     encryptARCFOUR(data, off, len, data, off);
/*    */   }
/*    */   
/*    */   public void encryptARCFOUR(byte[] dataIn, byte[] dataOut) {
/* 94 */     encryptARCFOUR(dataIn, 0, dataIn.length, dataOut, 0);
/*    */   }
/*    */   
/*    */   public void encryptARCFOUR(byte[] data) {
/* 98 */     encryptARCFOUR(data, 0, data.length, data, 0);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/crypto/ARCFOUREncryption.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */