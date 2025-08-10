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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IndependentRandomAccessSource
/*    */   implements RandomAccessSource
/*    */ {
/*    */   private final RandomAccessSource source;
/*    */   
/*    */   public IndependentRandomAccessSource(RandomAccessSource source) {
/* 63 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int get(long position) throws IOException {
/* 70 */     return this.source.get(position);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int get(long position, byte[] bytes, int off, int len) throws IOException {
/* 77 */     return this.source.get(position, bytes, off, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long length() {
/* 84 */     return this.source.length();
/*    */   }
/*    */   
/*    */   public void close() throws IOException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/io/IndependentRandomAccessSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */