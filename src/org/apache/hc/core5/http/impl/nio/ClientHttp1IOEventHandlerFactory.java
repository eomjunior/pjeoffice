/*    */ package org.apache.hc.core5.http.impl.nio;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.URIScheme;
/*    */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.reactor.EndpointParameters;
/*    */ import org.apache.hc.core5.reactor.IOEventHandler;
/*    */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*    */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*    */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ public class ClientHttp1IOEventHandlerFactory
/*    */   implements IOEventHandlerFactory
/*    */ {
/*    */   private final ClientHttp1StreamDuplexerFactory streamDuplexerFactory;
/*    */   private final TlsStrategy tlsStrategy;
/*    */   private final Timeout handshakeTimeout;
/*    */   
/*    */   public ClientHttp1IOEventHandlerFactory(ClientHttp1StreamDuplexerFactory streamDuplexerFactory, TlsStrategy tlsStrategy, Timeout handshakeTimeout) {
/* 57 */     this.streamDuplexerFactory = (ClientHttp1StreamDuplexerFactory)Args.notNull(streamDuplexerFactory, "Stream duplexer factory");
/* 58 */     this.tlsStrategy = tlsStrategy;
/* 59 */     this.handshakeTimeout = handshakeTimeout;
/*    */   }
/*    */ 
/*    */   
/*    */   public IOEventHandler createHandler(ProtocolIOSession ioSession, Object attachment) {
/* 64 */     if (attachment instanceof EndpointParameters) {
/* 65 */       EndpointParameters params = (EndpointParameters)attachment;
/* 66 */       if (this.tlsStrategy != null && URIScheme.HTTPS.same(params.getScheme())) {
/* 67 */         this.tlsStrategy.upgrade((TransportSecurityLayer)ioSession, (NamedEndpoint)params, params.getAttachment(), this.handshakeTimeout, null);
/*    */       }
/*    */     } 
/* 70 */     return new ClientHttp1IOEventHandler(this.streamDuplexerFactory.create(ioSession));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ClientHttp1IOEventHandlerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */