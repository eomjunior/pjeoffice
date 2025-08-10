/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.impl.nio.HttpConnectionEventHandler;
/*     */ import org.apache.hc.core5.http.impl.nio.ServerHttp1IOEventHandler;
/*     */ import org.apache.hc.core5.http.impl.nio.ServerHttp1StreamDuplexerFactory;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*     */ import org.apache.hc.core5.http2.ssl.ApplicationProtocol;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.EndpointParameters;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ @Internal
/*     */ public class ServerHttpProtocolNegotiationStarter
/*     */   implements IOEventHandlerFactory
/*     */ {
/*     */   private final ServerHttp1StreamDuplexerFactory http1StreamHandlerFactory;
/*     */   private final ServerH2StreamMultiplexerFactory http2StreamHandlerFactory;
/*     */   private final HttpVersionPolicy versionPolicy;
/*     */   private final TlsStrategy tlsStrategy;
/*     */   private final Timeout handshakeTimeout;
/*     */   
/*     */   public ServerHttpProtocolNegotiationStarter(ServerHttp1StreamDuplexerFactory http1StreamHandlerFactory, ServerH2StreamMultiplexerFactory http2StreamHandlerFactory, HttpVersionPolicy versionPolicy, TlsStrategy tlsStrategy, Timeout handshakeTimeout) {
/*  69 */     this.http1StreamHandlerFactory = (ServerHttp1StreamDuplexerFactory)Args.notNull(http1StreamHandlerFactory, "HTTP/1.1 stream handler factory");
/*  70 */     this.http2StreamHandlerFactory = (ServerH2StreamMultiplexerFactory)Args.notNull(http2StreamHandlerFactory, "HTTP/2 stream handler factory");
/*  71 */     this.versionPolicy = (versionPolicy != null) ? versionPolicy : HttpVersionPolicy.NEGOTIATE;
/*  72 */     this.tlsStrategy = tlsStrategy;
/*  73 */     this.handshakeTimeout = handshakeTimeout;
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpConnectionEventHandler createHandler(ProtocolIOSession ioSession, Object attachment) {
/*  78 */     HttpVersionPolicy endpointPolicy = this.versionPolicy;
/*  79 */     URIScheme uriScheme = URIScheme.HTTP;
/*  80 */     if (attachment instanceof EndpointParameters) {
/*  81 */       EndpointParameters params = (EndpointParameters)attachment;
/*  82 */       if (this.tlsStrategy != null && URIScheme.HTTPS.same(params.getScheme())) {
/*  83 */         uriScheme = URIScheme.HTTPS;
/*  84 */         this.tlsStrategy.upgrade((TransportSecurityLayer)ioSession, (NamedEndpoint)params, params.getAttachment(), this.handshakeTimeout, null);
/*     */       } 
/*  86 */       if (params.getAttachment() instanceof HttpVersionPolicy) {
/*  87 */         endpointPolicy = (HttpVersionPolicy)params.getAttachment();
/*     */       }
/*     */     } 
/*     */     
/*  91 */     ioSession.registerProtocol(ApplicationProtocol.HTTP_1_1.id, new ServerHttp1UpgradeHandler(this.http1StreamHandlerFactory));
/*  92 */     ioSession.registerProtocol(ApplicationProtocol.HTTP_2.id, new ServerH2UpgradeHandler(this.http2StreamHandlerFactory));
/*     */     
/*  94 */     switch (endpointPolicy) {
/*     */       case FORCE_HTTP_2:
/*  96 */         return new ServerH2PrefaceHandler(ioSession, this.http2StreamHandlerFactory);
/*     */       case FORCE_HTTP_1:
/*  98 */         return (HttpConnectionEventHandler)new ServerHttp1IOEventHandler(this.http1StreamHandlerFactory.create(uriScheme.id, ioSession));
/*     */     } 
/* 100 */     return new HttpProtocolNegotiator(ioSession, null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ServerHttpProtocolNegotiationStarter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */