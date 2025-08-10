/*     */ package org.apache.hc.core5.http2.ssl;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
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
/*     */ public class H2ClientTlsStrategy
/*     */   implements TlsStrategy
/*     */ {
/*     */   private final SSLContext sslContext;
/*     */   private final SSLBufferMode sslBufferMode;
/*     */   private final SSLSessionInitializer initializer;
/*     */   private final SSLSessionVerifier verifier;
/*     */   
/*     */   public H2ClientTlsStrategy(SSLContext sslContext, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  65 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/*  66 */     this.sslBufferMode = sslBufferMode;
/*  67 */     this.initializer = initializer;
/*  68 */     this.verifier = verifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public H2ClientTlsStrategy(SSLContext sslContext, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  75 */     this(sslContext, null, initializer, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public H2ClientTlsStrategy(SSLContext sslContext, SSLSessionVerifier verifier) {
/*  81 */     this(sslContext, null, null, verifier);
/*     */   }
/*     */   
/*     */   public H2ClientTlsStrategy(SSLContext sslContext) {
/*  85 */     this(sslContext, null, null, null);
/*     */   }
/*     */   
/*     */   public H2ClientTlsStrategy() {
/*  89 */     this(SSLContexts.createSystemDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public H2ClientTlsStrategy(SSLSessionVerifier verifier) {
/*  99 */     this(SSLContexts.createSystemDefault(), null, null, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(TransportSecurityLayer tlsSession, NamedEndpoint endpoint, Object attachment, Timeout handshakeTimeout, FutureCallback<TransportSecurityLayer> callback) {
/* 109 */     tlsSession.startTls(this.sslContext, endpoint, this.sslBufferMode, 
/*     */ 
/*     */ 
/*     */         
/* 113 */         H2TlsSupport.enforceRequirements(attachment, this.initializer), this.verifier, handshakeTimeout, callback);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean upgrade(TransportSecurityLayer tlsSession, HttpHost host, SocketAddress localAddress, SocketAddress remoteAddress, Object attachment, Timeout handshakeTimeout) {
/* 131 */     String scheme = (host != null) ? host.getSchemeName() : null;
/* 132 */     if (URIScheme.HTTPS.same(scheme)) {
/* 133 */       upgrade(tlsSession, (NamedEndpoint)host, attachment, handshakeTimeout, null);
/* 134 */       return true;
/*     */     } 
/* 136 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/ssl/H2ClientTlsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */