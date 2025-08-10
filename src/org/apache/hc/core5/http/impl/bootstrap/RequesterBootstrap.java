/*     */ package org.apache.hc.core5.http.impl.bootstrap;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.hc.core5.annotation.Experimental;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.DefaultAddressResolver;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.impl.HttpProcessors;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultBHttpClientConnectionFactory;
/*     */ import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
/*     */ import org.apache.hc.core5.http.io.HttpClientConnection;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.SocketConfig;
/*     */ import org.apache.hc.core5.http.io.ssl.DefaultTlsSetupHandler;
/*     */ import org.apache.hc.core5.http.io.ssl.SSLSessionVerifier;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.pool.ConnPoolListener;
/*     */ import org.apache.hc.core5.pool.DefaultDisposalCallback;
/*     */ import org.apache.hc.core5.pool.DisposalCallback;
/*     */ import org.apache.hc.core5.pool.LaxConnPool;
/*     */ import org.apache.hc.core5.pool.ManagedConnPool;
/*     */ import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
/*     */ import org.apache.hc.core5.pool.PoolReusePolicy;
/*     */ import org.apache.hc.core5.pool.StrictConnPool;
/*     */ import org.apache.hc.core5.util.TimeValue;
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
/*     */ public class RequesterBootstrap
/*     */ {
/*     */   private HttpProcessor httpProcessor;
/*     */   private ConnectionReuseStrategy connReuseStrategy;
/*     */   private SocketConfig socketConfig;
/*     */   private HttpConnectionFactory<? extends HttpClientConnection> connectFactory;
/*     */   private SSLSocketFactory sslSocketFactory;
/*     */   private Callback<SSLParameters> sslSetupHandler;
/*     */   private SSLSessionVerifier sslSessionVerifier;
/*     */   private int defaultMaxPerRoute;
/*     */   private int maxTotal;
/*     */   private Timeout timeToLive;
/*     */   private PoolReusePolicy poolReusePolicy;
/*     */   private PoolConcurrencyPolicy poolConcurrencyPolicy;
/*     */   private Http1StreamListener streamListener;
/*     */   private ConnPoolListener<HttpHost> connPoolListener;
/*     */   
/*     */   public static RequesterBootstrap bootstrap() {
/*  86 */     return new RequesterBootstrap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final RequesterBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
/*  93 */     this.httpProcessor = httpProcessor;
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final RequesterBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy connStrategy) {
/* 101 */     this.connReuseStrategy = connStrategy;
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final RequesterBootstrap setSocketConfig(SocketConfig socketConfig) {
/* 109 */     this.socketConfig = socketConfig;
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setConnectionFactory(HttpConnectionFactory<? extends HttpClientConnection> connectFactory) {
/* 114 */     this.connectFactory = connectFactory;
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setSslContext(SSLContext sslContext) {
/* 119 */     this.sslSocketFactory = (sslContext != null) ? sslContext.getSocketFactory() : null;
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
/* 124 */     this.sslSocketFactory = sslSocketFactory;
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final RequesterBootstrap setSslSetupHandler(Callback<SSLParameters> sslSetupHandler) {
/* 132 */     this.sslSetupHandler = sslSetupHandler;
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final RequesterBootstrap setSslSessionVerifier(SSLSessionVerifier sslSessionVerifier) {
/* 140 */     this.sslSessionVerifier = sslSessionVerifier;
/* 141 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setDefaultMaxPerRoute(int defaultMaxPerRoute) {
/* 145 */     this.defaultMaxPerRoute = defaultMaxPerRoute;
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setMaxTotal(int maxTotal) {
/* 150 */     this.maxTotal = maxTotal;
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setTimeToLive(Timeout timeToLive) {
/* 155 */     this.timeToLive = timeToLive;
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setPoolReusePolicy(PoolReusePolicy poolReusePolicy) {
/* 160 */     this.poolReusePolicy = poolReusePolicy;
/* 161 */     return this;
/*     */   }
/*     */   
/*     */   @Experimental
/*     */   public final RequesterBootstrap setPoolConcurrencyPolicy(PoolConcurrencyPolicy poolConcurrencyPolicy) {
/* 166 */     this.poolConcurrencyPolicy = poolConcurrencyPolicy;
/* 167 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setStreamListener(Http1StreamListener streamListener) {
/* 171 */     this.streamListener = streamListener;
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public final RequesterBootstrap setConnPoolListener(ConnPoolListener<HttpHost> connPoolListener) {
/* 176 */     this.connPoolListener = connPoolListener;
/* 177 */     return this;
/*     */   } public HttpRequester create() {
/*     */     LaxConnPool laxConnPool;
/*     */     StrictConnPool strictConnPool;
/* 181 */     HttpRequestExecutor requestExecutor = new HttpRequestExecutor(HttpRequestExecutor.DEFAULT_WAIT_FOR_CONTINUE, (this.connReuseStrategy != null) ? this.connReuseStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE, this.streamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     switch ((this.poolConcurrencyPolicy != null) ? this.poolConcurrencyPolicy : PoolConcurrencyPolicy.STRICT) {
/*     */       case LAX:
/* 188 */         laxConnPool = new LaxConnPool((this.defaultMaxPerRoute > 0) ? this.defaultMaxPerRoute : 20, (TimeValue)this.timeToLive, this.poolReusePolicy, (DisposalCallback)new DefaultDisposalCallback(), this.connPoolListener);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 197 */         strictConnPool = new StrictConnPool((this.defaultMaxPerRoute > 0) ? this.defaultMaxPerRoute : 20, (this.maxTotal > 0) ? this.maxTotal : 50, (TimeValue)this.timeToLive, this.poolReusePolicy, (DisposalCallback)new DefaultDisposalCallback(), this.connPoolListener);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 206 */     return new HttpRequester(requestExecutor, (this.httpProcessor != null) ? this.httpProcessor : 
/*     */         
/* 208 */         HttpProcessors.client(), (ManagedConnPool<HttpHost, HttpClientConnection>)strictConnPool, (this.socketConfig != null) ? this.socketConfig : SocketConfig.DEFAULT, (this.connectFactory != null) ? this.connectFactory : (HttpConnectionFactory<? extends HttpClientConnection>)new DefaultBHttpClientConnectionFactory(Http1Config.DEFAULT, CharCodingConfig.DEFAULT), this.sslSocketFactory, (this.sslSetupHandler != null) ? this.sslSetupHandler : (Callback<SSLParameters>)new DefaultTlsSetupHandler(), this.sslSessionVerifier, (Resolver<HttpHost, InetSocketAddress>)DefaultAddressResolver.INSTANCE);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/RequesterBootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */