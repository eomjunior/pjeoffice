/*    */ package org.apache.hc.core5.reactor;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.SocketAddress;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.Future;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
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
/*    */ public interface ConnectionAcceptor
/*    */ {
/*    */   default Future<ListenerEndpoint> listen(SocketAddress address, Object attachment, FutureCallback<ListenerEndpoint> callback) {
/* 59 */     return listen(address, callback);
/*    */   }
/*    */   
/*    */   Future<ListenerEndpoint> listen(SocketAddress paramSocketAddress, FutureCallback<ListenerEndpoint> paramFutureCallback);
/*    */   
/*    */   void pause() throws IOException;
/*    */   
/*    */   void resume() throws IOException;
/*    */   
/*    */   Set<ListenerEndpoint> getEndpoints();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/ConnectionAcceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */