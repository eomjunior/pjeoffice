/*    */ package org.apache.hc.client5.http.impl.nio;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import java.util.concurrent.Future;
/*    */ import org.apache.hc.client5.http.DnsResolver;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*    */ import org.apache.hc.core5.reactor.IOSession;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*    */ public final class MultihomeConnectionInitiator
/*    */   implements ConnectionInitiator
/*    */ {
/*    */   private final ConnectionInitiator connectionInitiator;
/*    */   private final MultihomeIOSessionRequester sessionRequester;
/*    */   
/*    */   public MultihomeConnectionInitiator(ConnectionInitiator connectionInitiator, DnsResolver dnsResolver) {
/* 57 */     this.connectionInitiator = (ConnectionInitiator)Args.notNull(connectionInitiator, "Connection initiator");
/* 58 */     this.sessionRequester = new MultihomeIOSessionRequester(dnsResolver);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Future<IOSession> connect(NamedEndpoint remoteEndpoint, SocketAddress remoteAddress, SocketAddress localAddress, Timeout connectTimeout, Object attachment, FutureCallback<IOSession> callback) {
/* 69 */     Args.notNull(remoteEndpoint, "Remote endpoint");
/* 70 */     return this.sessionRequester.connect(this.connectionInitiator, remoteEndpoint, remoteAddress, localAddress, connectTimeout, attachment, callback);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Future<IOSession> connect(NamedEndpoint remoteEndpoint, SocketAddress localAddress, Timeout connectTimeout, Object attachment, FutureCallback<IOSession> callback) {
/* 79 */     Args.notNull(remoteEndpoint, "Remote endpoint");
/* 80 */     return this.sessionRequester.connect(this.connectionInitiator, remoteEndpoint, localAddress, connectTimeout, attachment, callback);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/nio/MultihomeConnectionInitiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */