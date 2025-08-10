/*    */ package org.apache.hc.core5.http2.impl.nio;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*    */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class NoopH2StreamHandler
/*    */   implements H2StreamHandler
/*    */ {
/* 40 */   static final NoopH2StreamHandler INSTANCE = new NoopH2StreamHandler();
/*    */ 
/*    */   
/*    */   public boolean isOutputReady() {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void produceOutput() throws HttpException, IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void consumePromise(List<Header> headers) throws HttpException, IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void consumeHeader(List<Header> headers, boolean endStream) throws HttpException, IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateInputCapacity() throws IOException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void consumeData(ByteBuffer src, boolean endStream) throws HttpException, IOException {}
/*    */ 
/*    */   
/*    */   public HandlerFactory<AsyncPushConsumer> getPushHandlerFactory() {
/* 69 */     return null;
/*    */   }
/*    */   
/*    */   public void failed(Exception cause) {}
/*    */   
/*    */   public void handle(HttpException ex, boolean endStream) throws HttpException, IOException {}
/*    */   
/*    */   public void releaseResources() {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/NoopH2StreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */