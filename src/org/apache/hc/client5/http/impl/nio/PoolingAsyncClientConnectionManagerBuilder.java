/*     */ package org.apache.hc.client5.http.impl.nio;
/*     */ 
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.ssl.ConscryptClientTlsStrategy;
/*     */ import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
/*     */ import org.apache.hc.core5.pool.PoolReusePolicy;
/*     */ import org.apache.hc.core5.util.ReflectionUtils;
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
/*     */ public class PoolingAsyncClientConnectionManagerBuilder
/*     */ {
/*     */   private TlsStrategy tlsStrategy;
/*     */   private SchemePortResolver schemePortResolver;
/*     */   private DnsResolver dnsResolver;
/*     */   private PoolConcurrencyPolicy poolConcurrencyPolicy;
/*     */   private PoolReusePolicy poolReusePolicy;
/*     */   private boolean systemProperties;
/*     */   private int maxConnTotal;
/*     */   private int maxConnPerRoute;
/*     */   private Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver;
/*     */   private Resolver<HttpHost, TlsConfig> tlsConfigResolver;
/*     */   
/*     */   public static PoolingAsyncClientConnectionManagerBuilder create() {
/*  90 */     return new PoolingAsyncClientConnectionManagerBuilder();
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
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setTlsStrategy(TlsStrategy tlsStrategy) {
/* 102 */     this.tlsStrategy = tlsStrategy;
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setDnsResolver(DnsResolver dnsResolver) {
/* 110 */     this.dnsResolver = dnsResolver;
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setSchemePortResolver(SchemePortResolver schemePortResolver) {
/* 118 */     this.schemePortResolver = schemePortResolver;
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setPoolConcurrencyPolicy(PoolConcurrencyPolicy poolConcurrencyPolicy) {
/* 126 */     this.poolConcurrencyPolicy = poolConcurrencyPolicy;
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setConnPoolPolicy(PoolReusePolicy poolReusePolicy) {
/* 134 */     this.poolReusePolicy = poolReusePolicy;
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setMaxConnTotal(int maxConnTotal) {
/* 142 */     this.maxConnTotal = maxConnTotal;
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setMaxConnPerRoute(int maxConnPerRoute) {
/* 150 */     this.maxConnPerRoute = maxConnPerRoute;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setDefaultConnectionConfig(ConnectionConfig config) {
/* 160 */     this.connectionConfigResolver = (route -> config);
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setConnectionConfigResolver(Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver) {
/* 171 */     this.connectionConfigResolver = connectionConfigResolver;
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setDefaultTlsConfig(TlsConfig config) {
/* 181 */     this.tlsConfigResolver = (host -> config);
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setTlsConfigResolver(Resolver<HttpHost, TlsConfig> tlsConfigResolver) {
/* 192 */     this.tlsConfigResolver = tlsConfigResolver;
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setConnectionTimeToLive(TimeValue timeToLive) {
/* 203 */     setDefaultConnectionConfig(ConnectionConfig.custom()
/* 204 */         .setTimeToLive(timeToLive)
/* 205 */         .build());
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final PoolingAsyncClientConnectionManagerBuilder setValidateAfterInactivity(TimeValue validateAfterInactivity) {
/* 217 */     setDefaultConnectionConfig(ConnectionConfig.custom()
/* 218 */         .setValidateAfterInactivity(validateAfterInactivity)
/* 219 */         .build());
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final PoolingAsyncClientConnectionManagerBuilder useSystemProperties() {
/* 228 */     this.systemProperties = true;
/* 229 */     return this;
/*     */   }
/*     */   
/*     */   public PoolingAsyncClientConnectionManager build() {
/*     */     TlsStrategy tlsStrategyCopy;
/* 234 */     if (this.tlsStrategy != null) {
/* 235 */       tlsStrategyCopy = this.tlsStrategy;
/*     */     }
/* 237 */     else if (ReflectionUtils.determineJRELevel() <= 8 && ConscryptClientTlsStrategy.isSupported()) {
/* 238 */       if (this.systemProperties) {
/* 239 */         tlsStrategyCopy = ConscryptClientTlsStrategy.getSystemDefault();
/*     */       } else {
/* 241 */         tlsStrategyCopy = ConscryptClientTlsStrategy.getDefault();
/*     */       }
/*     */     
/* 244 */     } else if (this.systemProperties) {
/* 245 */       tlsStrategyCopy = DefaultClientTlsStrategy.getSystemDefault();
/*     */     } else {
/* 247 */       tlsStrategyCopy = DefaultClientTlsStrategy.getDefault();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     PoolingAsyncClientConnectionManager poolingmgr = new PoolingAsyncClientConnectionManager((Lookup<TlsStrategy>)RegistryBuilder.create().register(URIScheme.HTTPS.getId(), tlsStrategyCopy).build(), this.poolConcurrencyPolicy, this.poolReusePolicy, null, this.schemePortResolver, this.dnsResolver);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 260 */     poolingmgr.setConnectionConfigResolver(this.connectionConfigResolver);
/* 261 */     poolingmgr.setTlsConfigResolver(this.tlsConfigResolver);
/* 262 */     if (this.maxConnTotal > 0) {
/* 263 */       poolingmgr.setMaxTotal(this.maxConnTotal);
/*     */     }
/* 265 */     if (this.maxConnPerRoute > 0) {
/* 266 */       poolingmgr.setDefaultMaxPerRoute(this.maxConnPerRoute);
/*     */     }
/* 268 */     return poolingmgr;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/nio/PoolingAsyncClientConnectionManagerBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */