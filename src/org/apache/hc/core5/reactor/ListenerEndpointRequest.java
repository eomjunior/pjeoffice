/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.net.SocketAddress;
/*    */ import org.apache.hc.core5.concurrent.BasicFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ListenerEndpointRequest
/*    */   implements Closeable
/*    */ {
/*    */   final SocketAddress address;
/*    */   final Object attachment;
/*    */   final BasicFuture<ListenerEndpoint> future;
/*    */   
/*    */   ListenerEndpointRequest(SocketAddress address, Object attachment, BasicFuture<ListenerEndpoint> future) {
/* 42 */     this.address = address;
/* 43 */     this.attachment = attachment;
/* 44 */     this.future = future;
/*    */   }
/*    */   
/*    */   public void completed(ListenerEndpoint endpoint) {
/* 48 */     if (this.future != null) {
/* 49 */       this.future.completed(endpoint);
/*    */     }
/*    */   }
/*    */   
/*    */   public void failed(Exception cause) {
/* 54 */     if (this.future != null) {
/* 55 */       this.future.failed(cause);
/*    */     }
/*    */   }
/*    */   
/*    */   public void cancel() {
/* 60 */     if (this.future != null) {
/* 61 */       this.future.cancel();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isCancelled() {
/* 66 */     return (this.future != null && this.future.isCancelled());
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 71 */     cancel();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ListenerEndpointRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */