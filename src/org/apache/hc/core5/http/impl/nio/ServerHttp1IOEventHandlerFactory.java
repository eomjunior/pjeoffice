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
/*    */ public class ServerHttp1IOEventHandlerFactory
/*    */   implements IOEventHandlerFactory
/*    */ {
/*    */   private final ServerHttp1StreamDuplexerFactory streamDuplexerFactory;
/*    */   private final TlsStrategy tlsStrategy;
/*    */   private final Timeout handshakeTimeout;
/*    */   
/*    */   public ServerHttp1IOEventHandlerFactory(ServerHttp1StreamDuplexerFactory streamDuplexerFactory, TlsStrategy tlsStrategy, Timeout handshakeTimeout) {
/* 57 */     this.streamDuplexerFactory = (ServerHttp1StreamDuplexerFactory)Args.notNull(streamDuplexerFactory, "Stream duplexer factory");
/* 58 */     this.tlsStrategy = tlsStrategy;
/* 59 */     this.handshakeTimeout = handshakeTimeout;
/*    */   }
/*    */ 
/*    */   
/*    */   public IOEventHandler createHandler(ProtocolIOSession ioSession, Object attachment) {
/* 64 */     String endpointScheme = URIScheme.HTTP.id;
/* 65 */     if (attachment instanceof EndpointParameters) {
/* 66 */       EndpointParameters params = (EndpointParameters)attachment;
/* 67 */       endpointScheme = params.getScheme();
/* 68 */       if (this.tlsStrategy != null && URIScheme.HTTPS.same(endpointScheme)) {
/* 69 */         this.tlsStrategy.upgrade((TransportSecurityLayer)ioSession, (NamedEndpoint)params, params
/*    */ 
/*    */             
/* 72 */             .getAttachment(), this.handshakeTimeout, null);
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 77 */       this.tlsStrategy.upgrade((TransportSecurityLayer)ioSession, null, attachment, this.handshakeTimeout, null);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 84 */     return new ServerHttp1IOEventHandler(this.streamDuplexerFactory.create(endpointScheme, ioSession));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ServerHttp1IOEventHandlerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */