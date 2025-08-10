/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.nio.entity.AbstractBinAsyncEntityConsumer;
/*    */ import org.apache.hc.core5.util.ByteArrayBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class SimpleAsyncEntityConsumer
/*    */   extends AbstractBinAsyncEntityConsumer<byte[]>
/*    */ {
/* 44 */   private final ByteArrayBuffer buffer = new ByteArrayBuffer(1024);
/*    */ 
/*    */ 
/*    */   
/*    */   protected void streamStart(ContentType contentType) throws HttpException, IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected int capacityIncrement() {
/* 53 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void data(ByteBuffer src, boolean endOfStream) throws IOException {
/* 58 */     if (src == null) {
/*    */       return;
/*    */     }
/* 61 */     if (src.hasArray()) {
/* 62 */       this.buffer.append(src.array(), src.arrayOffset() + src.position(), src.remaining());
/*    */     } else {
/* 64 */       while (src.hasRemaining()) {
/* 65 */         this.buffer.append(src.get());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] generateContent() throws IOException {
/* 72 */     return this.buffer.toByteArray();
/*    */   }
/*    */ 
/*    */   
/*    */   public void releaseResources() {
/* 77 */     this.buffer.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleAsyncEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */