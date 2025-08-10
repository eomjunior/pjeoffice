/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import org.apache.hc.core5.function.Factory;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.ssl.TLS;
/*     */ import org.apache.hc.core5.reactor.ssl.SSLBufferMode;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.ssl.SSLContexts;
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
/*     */ 
/*     */ 
/*     */ public class ClientTlsStrategyBuilder
/*     */ {
/*     */   private SSLContext sslContext;
/*     */   private String[] tlsVersions;
/*     */   private String[] ciphers;
/*     */   private SSLBufferMode sslBufferMode;
/*     */   private HostnameVerifier hostnameVerifier;
/*     */   @Deprecated
/*     */   private Factory<SSLEngine, TlsDetails> tlsDetailsFactory;
/*     */   private boolean systemProperties;
/*     */   
/*     */   public static ClientTlsStrategyBuilder create() {
/*  70 */     return new ClientTlsStrategyBuilder();
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
/*     */ 
/*     */   
/*     */   public ClientTlsStrategyBuilder setSslContext(SSLContext sslContext) {
/*  89 */     this.sslContext = sslContext;
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientTlsStrategyBuilder setTlsVersions(String... tlslVersions) {
/*  97 */     this.tlsVersions = tlslVersions;
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientTlsStrategyBuilder setTlsVersions(TLS... tlslVersions) {
/* 105 */     this.tlsVersions = new String[tlslVersions.length];
/* 106 */     for (int i = 0; i < tlslVersions.length; i++) {
/* 107 */       this.tlsVersions[i] = (tlslVersions[i]).id;
/*     */     }
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientTlsStrategyBuilder setCiphers(String... ciphers) {
/* 116 */     this.ciphers = ciphers;
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientTlsStrategyBuilder setSslBufferMode(SSLBufferMode sslBufferMode) {
/* 124 */     this.sslBufferMode = sslBufferMode;
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClientTlsStrategyBuilder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
/* 132 */     this.hostnameVerifier = hostnameVerifier;
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ClientTlsStrategyBuilder setTlsDetailsFactory(Factory<SSLEngine, TlsDetails> tlsDetailsFactory) {
/* 143 */     this.tlsDetailsFactory = tlsDetailsFactory;
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ClientTlsStrategyBuilder useSystemProperties() {
/* 152 */     this.systemProperties = true;
/* 153 */     return this;
/*     */   }
/*     */   public TlsStrategy build() {
/*     */     SSLContext sslContextCopy;
/*     */     String[] tlsVersionsCopy;
/*     */     String[] ciphersCopy;
/* 159 */     if (this.sslContext != null) {
/* 160 */       sslContextCopy = this.sslContext;
/*     */     } else {
/* 162 */       sslContextCopy = this.systemProperties ? SSLContexts.createSystemDefault() : SSLContexts.createDefault();
/*     */     } 
/*     */     
/* 165 */     if (this.tlsVersions != null) {
/* 166 */       tlsVersionsCopy = this.tlsVersions;
/*     */     } else {
/* 168 */       tlsVersionsCopy = this.systemProperties ? HttpsSupport.getSystemProtocols() : null;
/*     */     } 
/*     */     
/* 171 */     if (this.ciphers != null) {
/* 172 */       ciphersCopy = this.ciphers;
/*     */     } else {
/* 174 */       ciphersCopy = this.systemProperties ? HttpsSupport.getSystemCipherSuits() : null;
/*     */     } 
/* 176 */     return new DefaultClientTlsStrategy(sslContextCopy, tlsVersionsCopy, ciphersCopy, (this.sslBufferMode != null) ? this.sslBufferMode : SSLBufferMode.STATIC, (this.hostnameVerifier != null) ? this.hostnameVerifier : 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 181 */         HttpsSupport.getDefaultHostnameVerifier(), this.tlsDetailsFactory);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/ClientTlsStrategyBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */