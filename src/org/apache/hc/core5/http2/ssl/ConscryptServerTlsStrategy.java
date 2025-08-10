/*     */ package org.apache.hc.core5.http2.ssl;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.nio.ssl.FixedPortStrategy;
/*     */ import org.apache.hc.core5.http.nio.ssl.SecurePortStrategy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConscryptServerTlsStrategy
/*     */   implements TlsStrategy
/*     */ {
/*     */   private final SSLContext sslContext;
/*     */   private final SecurePortStrategy securePortStrategy;
/*     */   private final SSLBufferMode sslBufferMode;
/*     */   private final SSLSessionInitializer initializer;
/*     */   private final SSLSessionVerifier verifier;
/*     */   
/*     */   @Deprecated
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext, SecurePortStrategy securePortStrategy, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  71 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/*  72 */     this.securePortStrategy = securePortStrategy;
/*  73 */     this.sslBufferMode = sslBufferMode;
/*  74 */     this.initializer = initializer;
/*  75 */     this.verifier = verifier;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext, SecurePortStrategy securePortStrategy, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  87 */     this(sslContext, securePortStrategy, null, initializer, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext, SecurePortStrategy securePortStrategy, SSLSessionVerifier verifier) {
/*  98 */     this(sslContext, securePortStrategy, null, null, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext, SecurePortStrategy securePortStrategy) {
/* 107 */     this(sslContext, securePortStrategy, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext, int... securePorts) {
/* 115 */     this(sslContext, (SecurePortStrategy)new FixedPortStrategy(securePorts));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/* 123 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/* 124 */     this.sslBufferMode = sslBufferMode;
/* 125 */     this.initializer = initializer;
/* 126 */     this.verifier = verifier;
/* 127 */     this.securePortStrategy = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/* 134 */     this(sslContext, (SSLBufferMode)null, initializer, verifier);
/*     */   }
/*     */   
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext, SSLSessionVerifier verifier) {
/* 138 */     this(sslContext, (SSLBufferMode)null, (SSLSessionInitializer)null, verifier);
/*     */   }
/*     */   
/*     */   public ConscryptServerTlsStrategy(SSLContext sslContext) {
/* 142 */     this(sslContext, (SSLBufferMode)null, (SSLSessionInitializer)null, (SSLSessionVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptServerTlsStrategy() {
/* 151 */     this(SSLContexts.createSystemDefault(), (SSLBufferMode)null, (SSLSessionInitializer)null, (SSLSessionVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConscryptServerTlsStrategy(SSLSessionVerifier verifier) {
/* 161 */     this(SSLContexts.createSystemDefault(), (SSLBufferMode)null, (SSLSessionInitializer)null, verifier);
/*     */   }
/*     */   
/*     */   private boolean isApplicable(SocketAddress localAddress) {
/* 165 */     return (this.securePortStrategy == null || this.securePortStrategy.isSecure(localAddress));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(TransportSecurityLayer tlsSession, NamedEndpoint endpoint, Object attachment, Timeout handshakeTimeout, FutureCallback<TransportSecurityLayer> callback) {
/* 176 */     tlsSession.startTls(this.sslContext, endpoint, this.sslBufferMode, 
/*     */ 
/*     */ 
/*     */         
/* 180 */         ConscryptSupport.initialize(attachment, this.initializer), 
/* 181 */         ConscryptSupport.verify(this.verifier), handshakeTimeout, callback);
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
/* 198 */     if (isApplicable(localAddress)) {
/* 199 */       upgrade(tlsSession, (NamedEndpoint)host, attachment, handshakeTimeout, null);
/* 200 */       return true;
/*     */     } 
/* 202 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/ssl/ConscryptServerTlsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */