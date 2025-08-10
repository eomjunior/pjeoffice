/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public final class SizeMonitorOutputStream
/*    */   extends OutputStream {
/*  8 */   private long size = 0L;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final long currentSize() {
/* 15 */     return this.size;
/*    */   }
/*    */   
/*    */   public final void reset() {
/* 19 */     this.size = 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void write(int b) throws IOException {
/* 24 */     update(1L);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void write(byte[] b) throws IOException {
/* 29 */     update(b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void write(byte[] b, int off, int len) throws IOException {
/* 34 */     update(len);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void flush() throws IOException {}
/*    */ 
/*    */   
/*    */   public final void close() throws IOException {}
/*    */ 
/*    */   
/*    */   public void update(long diff) {
/* 45 */     this.size += diff;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/SizeMonitorOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */