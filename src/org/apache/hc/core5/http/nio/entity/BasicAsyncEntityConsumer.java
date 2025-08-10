/*    */ package org.apache.hc.core5.http.nio.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.hc.core5.http.ContentType;
/*    */ import org.apache.hc.core5.http.HttpException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BasicAsyncEntityConsumer
/*    */   extends AbstractBinAsyncEntityConsumer<byte[]>
/*    */ {
/* 49 */   private final ByteArrayBuffer buffer = new ByteArrayBuffer(1024);
/*    */ 
/*    */ 
/*    */   
/*    */   protected void streamStart(ContentType contentType) throws HttpException, IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected int capacityIncrement() {
/* 58 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void data(ByteBuffer src, boolean endOfStream) throws IOException {
/* 63 */     this.buffer.append(src);
/*    */   }
/*    */ 
/*    */   
/*    */   protected byte[] generateContent() throws IOException {
/* 68 */     return this.buffer.toByteArray();
/*    */   }
/*    */ 
/*    */   
/*    */   public void releaseResources() {
/* 73 */     this.buffer.clear();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/BasicAsyncEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */