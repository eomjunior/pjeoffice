/*    */ package org.apache.hc.core5.http.impl.bootstrap;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import java.util.concurrent.Future;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*    */ import org.apache.hc.core5.reactor.IOSession;
/*    */ import org.apache.hc.core5.util.Timeout;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractConnectionInitiatorBase
/*    */   implements ConnectionInitiator
/*    */ {
/*    */   public final Future<IOSession> connect(NamedEndpoint remoteEndpoint, SocketAddress remoteAddress, SocketAddress localAddress, Timeout timeout, Object attachment, FutureCallback<IOSession> callback) {
/* 49 */     return getIOReactor().connect(remoteEndpoint, remoteAddress, localAddress, timeout, attachment, callback);
/*    */   }
/*    */   
/*    */   abstract ConnectionInitiator getIOReactor();
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/AbstractConnectionInitiatorBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */