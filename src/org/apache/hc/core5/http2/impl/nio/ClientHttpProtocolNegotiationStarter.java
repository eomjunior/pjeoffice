/*    */ package org.apache.hc.core5.http2.impl.nio;
/*    */ 
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.URIScheme;
/*    */ import org.apache.hc.core5.http.impl.nio.ClientHttp1IOEventHandler;
/*    */ import org.apache.hc.core5.http.impl.nio.ClientHttp1StreamDuplexerFactory;
/*    */ import org.apache.hc.core5.http.impl.nio.HttpConnectionEventHandler;
/*    */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*    */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*    */ import org.apache.hc.core5.http2.ssl.ApplicationProtocol;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*    */ @Internal
/*    */ public class ClientHttpProtocolNegotiationStarter
/*    */   implements IOEventHandlerFactory
/*    */ {
/*    */   private final ClientHttp1StreamDuplexerFactory http1StreamHandlerFactory;
/*    */   private final ClientH2StreamMultiplexerFactory http2StreamHandlerFactory;
/*    */   private final HttpVersionPolicy versionPolicy;
/*    */   private final TlsStrategy tlsStrategy;
/*    */   private final Timeout handshakeTimeout;
/*    */   
/*    */   public ClientHttpProtocolNegotiationStarter(ClientHttp1StreamDuplexerFactory http1StreamHandlerFactory, ClientH2StreamMultiplexerFactory http2StreamHandlerFactory, HttpVersionPolicy versionPolicy, TlsStrategy tlsStrategy, Timeout handshakeTimeout) {
/* 69 */     this.http1StreamHandlerFactory = (ClientHttp1StreamDuplexerFactory)Args.notNull(http1StreamHandlerFactory, "HTTP/1.1 stream handler factory");
/* 70 */     this.http2StreamHandlerFactory = (ClientH2StreamMultiplexerFactory)Args.notNull(http2StreamHandlerFactory, "HTTP/2 stream handler factory");
/* 71 */     this.versionPolicy = (versionPolicy != null) ? versionPolicy : HttpVersionPolicy.NEGOTIATE;
/* 72 */     this.tlsStrategy = tlsStrategy;
/* 73 */     this.handshakeTimeout = handshakeTimeout;
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpConnectionEventHandler createHandler(ProtocolIOSession ioSession, Object attachment) {
/* 78 */     HttpVersionPolicy endpointPolicy = this.versionPolicy;
/* 79 */     if (attachment instanceof EndpointParameters) {
/* 80 */       EndpointParameters params = (EndpointParameters)attachment;
/* 81 */       if (this.tlsStrategy != null && URIScheme.HTTPS.same(params.getScheme())) {
/* 82 */         this.tlsStrategy.upgrade((TransportSecurityLayer)ioSession, (NamedEndpoint)params, params.getAttachment(), this.handshakeTimeout, null);
/*    */       }
/* 84 */       if (params.getAttachment() instanceof HttpVersionPolicy) {
/* 85 */         endpointPolicy = (HttpVersionPolicy)params.getAttachment();
/*    */       }
/*    */     } 
/*    */     
/* 89 */     ioSession.registerProtocol(ApplicationProtocol.HTTP_1_1.id, new ClientHttp1UpgradeHandler(this.http1StreamHandlerFactory));
/* 90 */     ioSession.registerProtocol(ApplicationProtocol.HTTP_2.id, new ClientH2UpgradeHandler(this.http2StreamHandlerFactory));
/*    */     
/* 92 */     switch (endpointPolicy) {
/*    */       case FORCE_HTTP_2:
/* 94 */         return new ClientH2PrefaceHandler(ioSession, this.http2StreamHandlerFactory, false);
/*    */       case FORCE_HTTP_1:
/* 96 */         return (HttpConnectionEventHandler)new ClientHttp1IOEventHandler(this.http1StreamHandlerFactory.create(ioSession));
/*    */     } 
/* 98 */     return new HttpProtocolNegotiator(ioSession, null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ClientHttpProtocolNegotiationStarter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */