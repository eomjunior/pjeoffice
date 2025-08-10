/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.io.IOException;
/*    */ import java.net.SocketAddress;
/*    */ import java.nio.channels.SelectionKey;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import org.apache.hc.core5.io.CloseMode;
/*    */ import org.apache.hc.core5.io.Closer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ListenerEndpointImpl
/*    */   implements ListenerEndpoint
/*    */ {
/*    */   private final SelectionKey key;
/*    */   final SocketAddress address;
/*    */   final Object attachment;
/*    */   private final AtomicBoolean closed;
/*    */   
/*    */   public ListenerEndpointImpl(SelectionKey key, Object attachment, SocketAddress address) {
/* 47 */     this.key = key;
/* 48 */     this.address = address;
/* 49 */     this.attachment = attachment;
/* 50 */     this.closed = new AtomicBoolean(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public SocketAddress getAddress() {
/* 55 */     return this.address;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "endpoint: " + this.address;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isClosed() {
/* 65 */     return this.closed.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 70 */     if (this.closed.compareAndSet(false, true)) {
/* 71 */       this.key.cancel();
/* 72 */       this.key.channel().close();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void close(CloseMode closeMode) {
/* 78 */     Closer.closeQuietly((Closeable)this);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ListenerEndpointImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */