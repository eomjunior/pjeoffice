/*    */ package org.apache.hc.core5.http.nio.support.classic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ public class ContentInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private final ContentInputBuffer buffer;
/*    */   
/*    */   public ContentInputStream(ContentInputBuffer buffer) {
/* 46 */     Args.notNull(buffer, "Input buffer");
/* 47 */     this.buffer = buffer;
/*    */   }
/*    */ 
/*    */   
/*    */   public int available() throws IOException {
/* 52 */     return this.buffer.length();
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 57 */     return this.buffer.read(b, off, len);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b) throws IOException {
/* 62 */     if (b == null) {
/* 63 */       return 0;
/*    */     }
/* 65 */     return this.buffer.read(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 70 */     return this.buffer.read();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 76 */     byte[] tmp = new byte[1024];
/* 77 */     while (this.buffer.read(tmp, 0, tmp.length) >= 0);
/*    */     
/* 79 */     super.close();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/ContentInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */