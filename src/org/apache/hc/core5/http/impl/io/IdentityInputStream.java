/*    */ package org.apache.hc.core5.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.apache.hc.core5.http.StreamClosedException;
/*    */ import org.apache.hc.core5.http.io.SessionInputBuffer;
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
/*    */ public class IdentityInputStream
/*    */   extends InputStream
/*    */ {
/*    */   private final SessionInputBuffer buffer;
/*    */   private final InputStream inputStream;
/*    */   private boolean closed;
/*    */   
/*    */   public IdentityInputStream(SessionInputBuffer buffer, InputStream inputStream) {
/* 64 */     this.buffer = (SessionInputBuffer)Args.notNull(buffer, "Session input buffer");
/* 65 */     this.inputStream = (InputStream)Args.notNull(inputStream, "Input stream");
/*    */   }
/*    */ 
/*    */   
/*    */   public int available() throws IOException {
/* 70 */     if (this.closed) {
/* 71 */       return 0;
/*    */     }
/* 73 */     int n = this.buffer.length();
/* 74 */     return (n > 0) ? n : this.inputStream.available();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 83 */     this.closed = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 88 */     if (this.closed) {
/* 89 */       throw new StreamClosedException();
/*    */     }
/* 91 */     return this.buffer.read(this.inputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 96 */     if (this.closed) {
/* 97 */       throw new StreamClosedException();
/*    */     }
/* 99 */     return this.buffer.read(b, off, len, this.inputStream);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/IdentityInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */