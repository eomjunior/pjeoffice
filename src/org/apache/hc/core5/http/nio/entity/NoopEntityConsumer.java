/*    */ package org.apache.hc.core5.http.nio.entity;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
/*    */ import org.apache.hc.core5.http.nio.CapacityChannel;
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
/*    */ @Deprecated
/*    */ public final class NoopEntityConsumer
/*    */   implements AsyncEntityConsumer<Void>
/*    */ {
/*    */   private volatile FutureCallback<Void> resultCallback;
/*    */   
/*    */   public void streamStart(EntityDetails entityDetails, FutureCallback<Void> resultCallback) throws IOException, HttpException {
/* 56 */     this.resultCallback = resultCallback;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 61 */     capacityChannel.update(2147483647);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void consume(ByteBuffer src) throws IOException {}
/*    */ 
/*    */   
/*    */   public void streamEnd(List<? extends Header> trailers) throws IOException {
/* 70 */     if (this.resultCallback != null) {
/* 71 */       this.resultCallback.completed(null);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void failed(Exception cause) {
/* 77 */     if (this.resultCallback != null) {
/* 78 */       this.resultCallback.failed(cause);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Void getContent() {
/* 84 */     return null;
/*    */   }
/*    */   
/*    */   public void releaseResources() {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/NoopEntityConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */