/*    */ package com.itextpdf.text.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ArrayRandomAccessSource
/*    */   implements RandomAccessSource
/*    */ {
/*    */   private byte[] array;
/*    */   
/*    */   public ArrayRandomAccessSource(byte[] array) {
/* 54 */     if (array == null) throw new NullPointerException(); 
/* 55 */     this.array = array;
/*    */   }
/*    */   
/*    */   public int get(long offset) {
/* 59 */     if (offset >= this.array.length) return -1; 
/* 60 */     return 0xFF & this.array[(int)offset];
/*    */   }
/*    */   
/*    */   public int get(long offset, byte[] bytes, int off, int len) {
/* 64 */     if (this.array == null) throw new IllegalStateException("Already closed");
/*    */     
/* 66 */     if (offset >= this.array.length) {
/* 67 */       return -1;
/*    */     }
/* 69 */     if (offset + len > this.array.length) {
/* 70 */       len = (int)(this.array.length - offset);
/*    */     }
/* 72 */     System.arraycopy(this.array, (int)offset, bytes, off, len);
/*    */     
/* 74 */     return len;
/*    */   }
/*    */ 
/*    */   
/*    */   public long length() {
/* 79 */     return this.array.length;
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 83 */     this.array = null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/ArrayRandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */