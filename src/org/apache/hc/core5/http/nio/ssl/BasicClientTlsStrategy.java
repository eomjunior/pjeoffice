/*     */ package org.apache.hc.core5.http.nio.ssl;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLSessionInitializer;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLSessionVerifier;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
/*     */ import org.apache.hc.core5.ssl.SSLContexts;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicClientTlsStrategy
/*     */   implements TlsStrategy
/*     */ {
/*     */   private final SSLContext sslContext;
/*     */   private final SSLBufferMode sslBufferMode;
/*     */   private final SSLSessionInitializer initializer;
/*     */   private final SSLSessionVerifier verifier;
/*     */   
/*     */   public BasicClientTlsStrategy(SSLContext sslContext, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  64 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/*  65 */     this.sslBufferMode = sslBufferMode;
/*  66 */     this.initializer = initializer;
/*  67 */     this.verifier = verifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClientTlsStrategy(SSLContext sslContext, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  74 */     this(sslContext, null, initializer, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClientTlsStrategy(SSLContext sslContext, SSLSessionVerifier verifier) {
/*  80 */     this(sslContext, null, null, verifier);
/*     */   }
/*     */   
/*     */   public BasicClientTlsStrategy(SSLContext sslContext) {
/*  84 */     this(sslContext, null, null, null);
/*     */   }
/*     */   
/*     */   public BasicClientTlsStrategy() {
/*  88 */     this(SSLContexts.createSystemDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicClientTlsStrategy(SSLSessionVerifier verifier) {
/*  98 */     this(SSLContexts.createSystemDefault(), verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(TransportSecurityLayer tlsSession, NamedEndpoint endpoint, Object attachment, Timeout handshakeTimeout, FutureCallback<TransportSecurityLayer> callback) {
/* 108 */     tlsSession.startTls(this.sslContext, endpoint, this.sslBufferMode, 
/* 109 */         TlsSupport.enforceStrongSecurity(this.initializer), this.verifier, handshakeTimeout, callback);
/*     */   }
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
/*     */   @Deprecated
/*     */   public boolean upgrade(TransportSecurityLayer tlsSession, HttpHost host, SocketAddress localAddress, SocketAddress remoteAddress, Object attachment, Timeout handshakeTimeout) {
/* 124 */     String scheme = (host != null) ? host.getSchemeName() : null;
/* 125 */     if (URIScheme.HTTPS.same(scheme)) {
/* 126 */       upgrade(tlsSession, (NamedEndpoint)host, attachment, handshakeTimeout, null);
/* 127 */       return true;
/*     */     } 
/* 129 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/ssl/BasicClientTlsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */