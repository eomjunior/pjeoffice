/*    */ package org.apache.tools.zip;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class FallbackZipEncoding
/*    */   implements ZipEncoding
/*    */ {
/*    */   private final String charset;
/*    */   
/*    */   public FallbackZipEncoding() {
/* 50 */     this.charset = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FallbackZipEncoding(String charset) {
/* 60 */     this.charset = charset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canEncode(String name) {
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ByteBuffer encode(String name) throws IOException {
/* 74 */     if (this.charset == null) {
/* 75 */       return ByteBuffer.wrap(name.getBytes());
/*    */     }
/* 77 */     return ByteBuffer.wrap(name.getBytes(this.charset));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String decode(byte[] data) throws IOException {
/* 85 */     if (this.charset == null) {
/* 86 */       return new String(data);
/*    */     }
/* 88 */     return new String(data, this.charset);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/zip/FallbackZipEncoding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */