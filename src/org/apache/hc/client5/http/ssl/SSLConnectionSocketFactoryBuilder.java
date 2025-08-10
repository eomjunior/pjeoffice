/*     */ package org.apache.hc.client5.http.ssl;
/*     */ 
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.hc.core5.http.ssl.TLS;
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
/*     */ 
/*     */ 
/*     */ public class SSLConnectionSocketFactoryBuilder
/*     */ {
/*     */   private SSLContext sslContext;
/*     */   private String[] tlsVersions;
/*     */   private String[] ciphers;
/*     */   private HostnameVerifier hostnameVerifier;
/*     */   private boolean systemProperties;
/*     */   
/*     */   public static SSLConnectionSocketFactoryBuilder create() {
/*  65 */     return new SSLConnectionSocketFactoryBuilder();
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
/*     */   public SSLConnectionSocketFactoryBuilder setSslContext(SSLContext sslContext) {
/*  78 */     this.sslContext = sslContext;
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SSLConnectionSocketFactoryBuilder setTlsVersions(String... tlslVersions) {
/*  86 */     this.tlsVersions = tlslVersions;
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SSLConnectionSocketFactoryBuilder setTlsVersions(TLS... tlslVersions) {
/*  94 */     this.tlsVersions = new String[tlslVersions.length];
/*  95 */     for (int i = 0; i < tlslVersions.length; i++) {
/*  96 */       this.tlsVersions[i] = (tlslVersions[i]).id;
/*     */     }
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SSLConnectionSocketFactoryBuilder setCiphers(String... ciphers) {
/* 105 */     this.ciphers = ciphers;
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SSLConnectionSocketFactoryBuilder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
/* 114 */     this.hostnameVerifier = hostnameVerifier;
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final SSLConnectionSocketFactoryBuilder useSystemProperties() {
/* 123 */     this.systemProperties = true;
/* 124 */     return this;
/*     */   } public SSLConnectionSocketFactory build() {
/*     */     SSLSocketFactory socketFactory;
/*     */     String[] tlsVersionsCopy;
/*     */     String[] ciphersCopy;
/* 129 */     if (this.sslContext != null) {
/* 130 */       socketFactory = this.sslContext.getSocketFactory();
/*     */     }
/* 132 */     else if (this.systemProperties) {
/* 133 */       socketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */     } else {
/* 135 */       socketFactory = SSLContexts.createDefault().getSocketFactory();
/*     */     } 
/*     */ 
/*     */     
/* 139 */     if (this.tlsVersions != null) {
/* 140 */       tlsVersionsCopy = this.tlsVersions;
/*     */     } else {
/* 142 */       tlsVersionsCopy = this.systemProperties ? HttpsSupport.getSystemProtocols() : null;
/*     */     } 
/*     */     
/* 145 */     if (this.ciphers != null) {
/* 146 */       ciphersCopy = this.ciphers;
/*     */     } else {
/* 148 */       ciphersCopy = this.systemProperties ? HttpsSupport.getSystemCipherSuits() : null;
/*     */     } 
/* 150 */     return new SSLConnectionSocketFactory(socketFactory, tlsVersionsCopy, ciphersCopy, (this.hostnameVerifier != null) ? this.hostnameVerifier : 
/*     */ 
/*     */ 
/*     */         
/* 154 */         HttpsSupport.getDefaultHostnameVerifier());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ssl/SSLConnectionSocketFactoryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */