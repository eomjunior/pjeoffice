/*     */ package org.apache.hc.core5.http.nio.ssl;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.HttpHost;
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
/*     */ 
/*     */ 
/*     */ public class BasicServerTlsStrategy
/*     */   implements TlsStrategy
/*     */ {
/*     */   private final SSLContext sslContext;
/*     */   private final SecurePortStrategy securePortStrategy;
/*     */   private final SSLBufferMode sslBufferMode;
/*     */   private final SSLSessionInitializer initializer;
/*     */   private final SSLSessionVerifier verifier;
/*     */   
/*     */   @Deprecated
/*     */   public BasicServerTlsStrategy(SSLContext sslContext, SecurePortStrategy securePortStrategy, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  70 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/*  71 */     this.securePortStrategy = securePortStrategy;
/*  72 */     this.sslBufferMode = sslBufferMode;
/*  73 */     this.initializer = initializer;
/*  74 */     this.verifier = verifier;
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
/*     */   public BasicServerTlsStrategy(SSLContext sslContext, SecurePortStrategy securePortStrategy, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/*  86 */     this(sslContext, securePortStrategy, null, initializer, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BasicServerTlsStrategy(SSLContext sslContext, SecurePortStrategy securePortStrategy, SSLSessionVerifier verifier) {
/*  97 */     this(sslContext, securePortStrategy, null, null, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BasicServerTlsStrategy(SSLContext sslContext, SecurePortStrategy securePortStrategy) {
/* 105 */     this(sslContext, securePortStrategy, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public BasicServerTlsStrategy(SecurePortStrategy securePortStrategy) {
/* 113 */     this(SSLContexts.createSystemDefault(), securePortStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicServerTlsStrategy(SSLSessionVerifier verifier) {
/* 122 */     this(SSLContexts.createSystemDefault(), verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicServerTlsStrategy(SSLContext sslContext, SSLBufferMode sslBufferMode, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/* 130 */     this.sslContext = (SSLContext)Args.notNull(sslContext, "SSL context");
/* 131 */     this.sslBufferMode = sslBufferMode;
/* 132 */     this.initializer = initializer;
/* 133 */     this.verifier = verifier;
/* 134 */     this.securePortStrategy = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicServerTlsStrategy(SSLContext sslContext, SSLSessionInitializer initializer, SSLSessionVerifier verifier) {
/* 141 */     this(sslContext, (SSLBufferMode)null, initializer, verifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicServerTlsStrategy(SSLContext sslContext, SSLSessionVerifier verifier) {
/* 147 */     this(sslContext, (SSLBufferMode)null, (SSLSessionInitializer)null, verifier);
/*     */   }
/*     */   
/*     */   public BasicServerTlsStrategy(SSLContext sslContext) {
/* 151 */     this(sslContext, null, null, null, null);
/*     */   }
/*     */   
/*     */   public BasicServerTlsStrategy() {
/* 155 */     this(SSLContexts.createSystemDefault());
/*     */   }
/*     */   
/*     */   private boolean isApplicable(SocketAddress localAddress) {
/* 159 */     return (this.securePortStrategy == null || this.securePortStrategy.isSecure(localAddress));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(TransportSecurityLayer tlsSession, NamedEndpoint endpoint, Object attachment, Timeout handshakeTimeout, FutureCallback<TransportSecurityLayer> callback) {
/* 169 */     tlsSession.startTls(this.sslContext, endpoint, this.sslBufferMode, 
/* 170 */         TlsSupport.enforceStrongSecurity(this.initializer), this.verifier, handshakeTimeout, callback);
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
/* 185 */     if (isApplicable(localAddress)) {
/* 186 */       upgrade(tlsSession, (NamedEndpoint)host, attachment, handshakeTimeout, null);
/* 187 */       return true;
/*     */     } 
/* 189 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/ssl/BasicServerTlsStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */