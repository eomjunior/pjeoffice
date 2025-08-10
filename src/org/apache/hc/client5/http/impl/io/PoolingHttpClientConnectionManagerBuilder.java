/*     */ package org.apache.hc.client5.http.impl.io;
/*     */ 
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
/*     */ import org.apache.hc.client5.http.socket.LayeredConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.SocketConfig;
/*     */ import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
/*     */ import org.apache.hc.core5.pool.PoolReusePolicy;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PoolingHttpClientConnectionManagerBuilder
/*     */ {
/*     */   private HttpConnectionFactory<ManagedHttpClientConnection> connectionFactory;
/*     */   private LayeredConnectionSocketFactory sslSocketFactory;
/*     */   private SchemePortResolver schemePortResolver;
/*     */   private DnsResolver dnsResolver;
/*     */   private PoolConcurrencyPolicy poolConcurrencyPolicy;
/*     */   private PoolReusePolicy poolReusePolicy;
/*     */   private Resolver<HttpRoute, SocketConfig> socketConfigResolver;
/*     */   private Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver;
/*     */   private Resolver<HttpHost, TlsConfig> tlsConfigResolver;
/*     */   private boolean systemProperties;
/*     */   private int maxConnTotal;
/*     */   private int maxConnPerRoute;
/*     */   
/*     */   public static PoolingHttpClientConnectionManagerBuilder create() {
/*  94 */     return new PoolingHttpClientConnectionManagerBuilder();
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
/*     */   public final PoolingHttpClientConnectionManagerBuilder setConnectionFactory(HttpConnectionFactory<ManagedHttpClientConnection> connectionFactory) {
/* 106 */     this.connectionFactory = connectionFactory;
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setSSLSocketFactory(LayeredConnectionSocketFactory sslSocketFactory) {
/* 115 */     this.sslSocketFactory = sslSocketFactory;
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setDnsResolver(DnsResolver dnsResolver) {
/* 123 */     this.dnsResolver = dnsResolver;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setSchemePortResolver(SchemePortResolver schemePortResolver) {
/* 131 */     this.schemePortResolver = schemePortResolver;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setPoolConcurrencyPolicy(PoolConcurrencyPolicy poolConcurrencyPolicy) {
/* 139 */     this.poolConcurrencyPolicy = poolConcurrencyPolicy;
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setConnPoolPolicy(PoolReusePolicy poolReusePolicy) {
/* 147 */     this.poolReusePolicy = poolReusePolicy;
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setMaxConnTotal(int maxConnTotal) {
/* 155 */     this.maxConnTotal = maxConnTotal;
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setMaxConnPerRoute(int maxConnPerRoute) {
/* 163 */     this.maxConnPerRoute = maxConnPerRoute;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setDefaultSocketConfig(SocketConfig config) {
/* 171 */     this.socketConfigResolver = (route -> config);
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setSocketConfigResolver(Resolver<HttpRoute, SocketConfig> socketConfigResolver) {
/* 182 */     this.socketConfigResolver = socketConfigResolver;
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setDefaultConnectionConfig(ConnectionConfig config) {
/* 192 */     this.connectionConfigResolver = (route -> config);
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setConnectionConfigResolver(Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver) {
/* 203 */     this.connectionConfigResolver = connectionConfigResolver;
/* 204 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setDefaultTlsConfig(TlsConfig config) {
/* 213 */     this.tlsConfigResolver = (host -> config);
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder setTlsConfigResolver(Resolver<HttpHost, TlsConfig> tlsConfigResolver) {
/* 224 */     this.tlsConfigResolver = tlsConfigResolver;
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final PoolingHttpClientConnectionManagerBuilder setConnectionTimeToLive(TimeValue timeToLive) {
/* 235 */     setDefaultConnectionConfig(ConnectionConfig.custom()
/* 236 */         .setTimeToLive(timeToLive)
/* 237 */         .build());
/* 238 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final PoolingHttpClientConnectionManagerBuilder setValidateAfterInactivity(TimeValue validateAfterInactivity) {
/* 249 */     setDefaultConnectionConfig(ConnectionConfig.custom()
/* 250 */         .setValidateAfterInactivity(validateAfterInactivity)
/* 251 */         .build());
/* 252 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingHttpClientConnectionManagerBuilder useSystemProperties() {
/* 260 */     this.systemProperties = true;
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager build() {
/* 272 */     PoolingHttpClientConnectionManager poolingmgr = new PoolingHttpClientConnectionManager(RegistryBuilder.create().register(URIScheme.HTTP.id, PlainConnectionSocketFactory.getSocketFactory()).register(URIScheme.HTTPS.id, (this.sslSocketFactory != null) ? this.sslSocketFactory : (this.systemProperties ? SSLConnectionSocketFactory.getSystemSocketFactory() : SSLConnectionSocketFactory.getSocketFactory())).build(), this.poolConcurrencyPolicy, this.poolReusePolicy, null, this.schemePortResolver, this.dnsResolver, this.connectionFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     poolingmgr.setSocketConfigResolver(this.socketConfigResolver);
/* 280 */     poolingmgr.setConnectionConfigResolver(this.connectionConfigResolver);
/* 281 */     poolingmgr.setTlsConfigResolver(this.tlsConfigResolver);
/* 282 */     if (this.maxConnTotal > 0) {
/* 283 */       poolingmgr.setMaxTotal(this.maxConnTotal);
/*     */     }
/* 285 */     if (this.maxConnPerRoute > 0) {
/* 286 */       poolingmgr.setDefaultMaxPerRoute(this.maxConnPerRoute);
/*     */     }
/* 288 */     return poolingmgr;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/PoolingHttpClientConnectionManagerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */