/*     */ package org.apache.hc.core5.http.impl.bootstrap;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Experimental;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.impl.HttpProcessors;
/*     */ import org.apache.hc.core5.http.impl.nio.ClientHttp1IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.ClientHttp1StreamDuplexerFactory;
/*     */ import org.apache.hc.core5.http.nio.ssl.BasicClientTlsStrategy;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.pool.ConnPoolListener;
/*     */ import org.apache.hc.core5.pool.DefaultDisposalCallback;
/*     */ import org.apache.hc.core5.pool.DisposalCallback;
/*     */ import org.apache.hc.core5.pool.LaxConnPool;
/*     */ import org.apache.hc.core5.pool.ManagedConnPool;
/*     */ import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
/*     */ import org.apache.hc.core5.pool.PoolReusePolicy;
/*     */ import org.apache.hc.core5.pool.StrictConnPool;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.IOSessionListener;
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
/*     */ 
/*     */ public class AsyncRequesterBootstrap
/*     */ {
/*     */   private IOReactorConfig ioReactorConfig;
/*     */   private Http1Config http1Config;
/*     */   private CharCodingConfig charCodingConfig;
/*     */   private HttpProcessor httpProcessor;
/*     */   private ConnectionReuseStrategy connStrategy;
/*     */   private int defaultMaxPerRoute;
/*     */   private int maxTotal;
/*     */   private Timeout timeToLive;
/*     */   private PoolReusePolicy poolReusePolicy;
/*     */   private PoolConcurrencyPolicy poolConcurrencyPolicy;
/*     */   private TlsStrategy tlsStrategy;
/*     */   private Timeout handshakeTimeout;
/*     */   private Decorator<IOSession> ioSessionDecorator;
/*     */   private Callback<Exception> exceptionCallback;
/*     */   private IOSessionListener sessionListener;
/*     */   private Http1StreamListener streamListener;
/*     */   private ConnPoolListener<HttpHost> connPoolListener;
/*     */   
/*     */   public static AsyncRequesterBootstrap bootstrap() {
/*  85 */     return new AsyncRequesterBootstrap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setIOReactorConfig(IOReactorConfig ioReactorConfig) {
/*  92 */     this.ioReactorConfig = ioReactorConfig;
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setHttp1Config(Http1Config http1Config) {
/* 100 */     this.http1Config = http1Config;
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setCharCodingConfig(CharCodingConfig charCodingConfig) {
/* 108 */     this.charCodingConfig = charCodingConfig;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
/* 116 */     this.httpProcessor = httpProcessor;
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy connStrategy) {
/* 124 */     this.connStrategy = connStrategy;
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public final AsyncRequesterBootstrap setDefaultMaxPerRoute(int defaultMaxPerRoute) {
/* 129 */     this.defaultMaxPerRoute = defaultMaxPerRoute;
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public final AsyncRequesterBootstrap setMaxTotal(int maxTotal) {
/* 134 */     this.maxTotal = maxTotal;
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public final AsyncRequesterBootstrap setTimeToLive(Timeout timeToLive) {
/* 139 */     this.timeToLive = timeToLive;
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setPoolReusePolicy(PoolReusePolicy poolReusePolicy) {
/* 147 */     this.poolReusePolicy = poolReusePolicy;
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Experimental
/*     */   public final AsyncRequesterBootstrap setPoolConcurrencyPolicy(PoolConcurrencyPolicy poolConcurrencyPolicy) {
/* 156 */     this.poolConcurrencyPolicy = poolConcurrencyPolicy;
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setTlsStrategy(TlsStrategy tlsStrategy) {
/* 164 */     this.tlsStrategy = tlsStrategy;
/* 165 */     return this;
/*     */   }
/*     */   
/*     */   public final AsyncRequesterBootstrap setTlsHandshakeTimeout(Timeout handshakeTimeout) {
/* 169 */     this.handshakeTimeout = handshakeTimeout;
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setIOSessionDecorator(Decorator<IOSession> ioSessionDecorator) {
/* 177 */     this.ioSessionDecorator = ioSessionDecorator;
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setExceptionCallback(Callback<Exception> exceptionCallback) {
/* 185 */     this.exceptionCallback = exceptionCallback;
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setIOSessionListener(IOSessionListener sessionListener) {
/* 193 */     this.sessionListener = sessionListener;
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setStreamListener(Http1StreamListener streamListener) {
/* 201 */     this.streamListener = streamListener;
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncRequesterBootstrap setConnPoolListener(ConnPoolListener<HttpHost> connPoolListener) {
/* 209 */     this.connPoolListener = connPoolListener;
/* 210 */     return this;
/*     */   }
/*     */   public HttpAsyncRequester create() {
/*     */     LaxConnPool laxConnPool;
/*     */     StrictConnPool strictConnPool;
/* 215 */     switch ((this.poolConcurrencyPolicy != null) ? this.poolConcurrencyPolicy : PoolConcurrencyPolicy.STRICT) {
/*     */       case LAX:
/* 217 */         laxConnPool = new LaxConnPool((this.defaultMaxPerRoute > 0) ? this.defaultMaxPerRoute : 20, (TimeValue)this.timeToLive, this.poolReusePolicy, (DisposalCallback)new DefaultDisposalCallback(), this.connPoolListener);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 226 */         strictConnPool = new StrictConnPool((this.defaultMaxPerRoute > 0) ? this.defaultMaxPerRoute : 20, (this.maxTotal > 0) ? this.maxTotal : 50, (TimeValue)this.timeToLive, this.poolReusePolicy, (DisposalCallback)new DefaultDisposalCallback(), this.connPoolListener);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     ClientHttp1StreamDuplexerFactory streamDuplexerFactory = new ClientHttp1StreamDuplexerFactory((this.httpProcessor != null) ? this.httpProcessor : HttpProcessors.client(), (this.http1Config != null) ? this.http1Config : Http1Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT, this.connStrategy, null, null, this.streamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     TlsStrategy tlsStrategyCopy = (this.tlsStrategy != null) ? this.tlsStrategy : (TlsStrategy)new BasicClientTlsStrategy();
/* 244 */     ClientHttp1IOEventHandlerFactory clientHttp1IOEventHandlerFactory = new ClientHttp1IOEventHandlerFactory(streamDuplexerFactory, tlsStrategyCopy, this.handshakeTimeout);
/*     */ 
/*     */ 
/*     */     
/* 248 */     return new HttpAsyncRequester(this.ioReactorConfig, (IOEventHandlerFactory)clientHttp1IOEventHandlerFactory, this.ioSessionDecorator, this.exceptionCallback, this.sessionListener, (ManagedConnPool<HttpHost, IOSession>)strictConnPool, tlsStrategyCopy, this.handshakeTimeout);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/AsyncRequesterBootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */