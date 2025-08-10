/*    */ package org.apache.hc.core5.http.io.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.hc.core5.http.ContentType;
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
/*    */ public class ByteBufferEntity
/*    */   extends AbstractHttpEntity
/*    */ {
/*    */   private final ByteBuffer buffer;
/*    */   private final long length;
/*    */   
/*    */   public ByteBufferEntity(ByteBuffer buffer, ContentType contentType, String contentEncoding) {
/* 46 */     super(contentType, contentEncoding);
/* 47 */     Args.notNull(buffer, "Source byte buffer");
/* 48 */     this.buffer = buffer;
/* 49 */     this.length = buffer.remaining();
/*    */   }
/*    */   
/*    */   public ByteBufferEntity(ByteBuffer buffer, ContentType contentType) {
/* 53 */     this(buffer, contentType, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isRepeatable() {
/* 58 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getContentLength() {
/* 63 */     return this.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public final InputStream getContent() throws IOException, UnsupportedOperationException {
/* 68 */     return new InputStream()
/*    */       {
/*    */         public int read() throws IOException
/*    */         {
/* 72 */           if (!ByteBufferEntity.this.buffer.hasRemaining()) {
/* 73 */             return -1;
/*    */           }
/* 75 */           return ByteBufferEntity.this.buffer.get() & 0xFF;
/*    */         }
/*    */ 
/*    */         
/*    */         public int read(byte[] bytes, int off, int len) throws IOException {
/* 80 */           if (!ByteBufferEntity.this.buffer.hasRemaining()) {
/* 81 */             return -1;
/*    */           }
/* 83 */           int chunk = Math.min(len, ByteBufferEntity.this.buffer.remaining());
/* 84 */           ByteBufferEntity.this.buffer.get(bytes, off, chunk);
/* 85 */           return chunk;
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public final boolean isStreaming() {
/* 92 */     return false;
/*    */   }
/*    */   
/*    */   public final void close() throws IOException {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/ByteBufferEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */