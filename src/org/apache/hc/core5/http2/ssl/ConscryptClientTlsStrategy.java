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
/*     */ public class ConscryptClientTlsStrategy
/*     */   implements TlsStrategy
/*     */ {
/*     */   private final SSLContext sslContext;
/*     */   private final SSLBufferMode sslBufferMode;
/*     */   private final SSLSessionInitializer initializer;
/*     */   private final SSLSessionVerifier verifier;
/*     */   
/*     */   public ConscryptClientTlsStrategy(SSLContext sslContext, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  65 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/*  66 */     this.sslBufferMode = sslBufferMode;
/*  67 */     this.initializer = initializer;
/*  68 */     this.verifier = verifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptClientTlsStrategy(SSLContext sslContext, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  75 */     this(sslContext, null, initializer, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptClientTlsStrategy(SSLContext sslContext, SSLSessionVerifier verifier) {
/*  81 */     this(sslContext, null, null, verifier);
/*     */   }
/*     */   
/*     */   public ConscryptClientTlsStrategy(SSLContext sslContext) {
/*  85 */     this(sslContext, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptClientTlsStrategy() {
/*  94 */     this(SSLContexts.createSystemDefault(), null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptClientTlsStrategy(SSLSessionVerifier verifier) {
/* 103 */     this(SSLContexts.createSystemDefault(), verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(TransportSecurityLayer tlsSession, NamedEndpoint endpoint, Object attachment, Timeout handshakeTimeout, FutureCallback<TransportSecurityLayer> callback) {
/* 113 */     tlsSession.startTls(this.sslContext, endpoint, this.sslBufferMode, 
/*     */ 
/*     */ 
/*     */         
/* 117 */         ConscryptSupport.initialize(attachment, this.initializer), 
/* 118 */         ConscryptSupport.verify(this.verifier), handshakeTimeout, callback);
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
/*     */   @Deprecated
/*     */   public boolean upgrade(TransportSecurityLayer tlsSession, HttpHost host, SocketAddress localAddress, SocketAddress remoteAddress, Object attachment, Timeout handshakeTimeout) {
/* 135 */     String scheme = (host != null) ? host.getSchemeName() : null;
/* 136 */     if (URIScheme.HTTPS.same(scheme)) {
/* 137 */       upgrade(tlsSession, (NamedEndpoint)host, attachment, handshakeTimeout, null);
/* 138 */       return true;
/*     */     } 
/* 140 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/ssl/ConscryptClientTlsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */