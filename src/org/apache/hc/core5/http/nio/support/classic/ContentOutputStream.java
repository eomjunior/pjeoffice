/*    */ package org.apache.hc.core5.http.nio.support.classic;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public class ContentOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   private final ContentOutputBuffer buffer;
/*    */   
/*    */   public ContentOutputStream(ContentOutputBuffer buffer) {
/* 46 */     Args.notNull(buffer, "Output buffer");
/* 47 */     this.buffer = buffer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 52 */     this.buffer.writeCompleted();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void flush() throws IOException {}
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 61 */     this.buffer.write(b, off, len);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b) throws IOException {
/* 66 */     if (b == null) {
/*    */       return;
/*    */     }
/* 69 */     this.buffer.write(b, 0, b.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 74 */     this.buffer.write(b);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/ContentOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */