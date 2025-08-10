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
/*    */ public final class ByteArray
/*    */ {
/*    */   private static final int INITIAL_SIZE = 32;
/*    */   private byte[] bytes;
/*    */   private int size;
/*    */   
/*    */   public ByteArray() {
/* 33 */     this.bytes = null;
/* 34 */     this.size = 0;
/*    */   }
/*    */   
/*    */   public ByteArray(int size) {
/* 38 */     this.bytes = new byte[size];
/* 39 */     this.size = size;
/*    */   }
/*    */   
/*    */   public ByteArray(byte[] byteArray) {
/* 43 */     this.bytes = byteArray;
/* 44 */     this.size = this.bytes.length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int at(int index) {
/* 53 */     return this.bytes[index] & 0xFF;
/*    */   }
/*    */   
/*    */   public void set(int index, int value) {
/* 57 */     this.bytes[index] = (byte)value;
/*    */   }
/*    */   
/*    */   public int size() {
/* 61 */     return this.size;
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 65 */     return (this.size == 0);
/*    */   }
/*    */   
/*    */   public void appendByte(int value) {
/* 69 */     if (this.size == 0 || this.size >= this.bytes.length) {
/* 70 */       int newSize = Math.max(32, this.size << 1);
/* 71 */       reserve(newSize);
/*    */     } 
/* 73 */     this.bytes[this.size] = (byte)value;
/* 74 */     this.size++;
/*    */   }
/*    */   
/*    */   public void reserve(int capacity) {
/* 78 */     if (this.bytes == null || this.bytes.length < capacity) {
/* 79 */       byte[] newArray = new byte[capacity];
/* 80 */       if (this.bytes != null) {
/* 81 */         System.arraycopy(this.bytes, 0, newArray, 0, this.bytes.length);
/*    */       }
/* 83 */       this.bytes = newArray;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(byte[] source, int offset, int count) {
/* 89 */     this.bytes = new byte[count];
/* 90 */     this.size = count;
/* 91 */     for (int x = 0; x < count; x++)
/* 92 */       this.bytes[x] = source[offset + x]; 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/pdf/qrcode/ByteArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */