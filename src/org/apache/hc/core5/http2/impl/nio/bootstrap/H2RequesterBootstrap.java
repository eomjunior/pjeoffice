/*     */ package org.apache.hc.core5.http2.impl.nio.bootstrap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.annotation.Experimental;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequestMapper;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.impl.HttpProcessors;
/*     */ import org.apache.hc.core5.http.impl.nio.ClientHttp1StreamDuplexerFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.DefaultHttpRequestWriterFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.DefaultHttpResponseParserFactory;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.RequestHandlerRegistry;
/*     */ import org.apache.hc.core5.http.protocol.UriPatternType;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.impl.H2Processors;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientH2StreamMultiplexerFactory;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientHttpProtocolNegotiationStarter;
/*     */ import org.apache.hc.core5.http2.impl.nio.H2StreamListener;
/*     */ import org.apache.hc.core5.http2.nio.support.DefaultAsyncPushConsumerFactory;
/*     */ import org.apache.hc.core5.http2.ssl.H2ClientTlsStrategy;
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
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class H2RequesterBootstrap
/*     */ {
/*     */   private UriPatternType uriPatternType;
/*     */   private IOReactorConfig ioReactorConfig;
/*     */   private HttpProcessor httpProcessor;
/*     */   private CharCodingConfig charCodingConfig;
/*     */   private HttpVersionPolicy versionPolicy;
/*     */   private H2Config h2Config;
/*     */   private Http1Config http1Config;
/*     */   private int defaultMaxPerRoute;
/* 104 */   private final List<HandlerEntry<Supplier<AsyncPushConsumer>>> pushConsumerList = new ArrayList<>(); private int maxTotal; private TimeValue timeToLive; private PoolReusePolicy poolReusePolicy; private PoolConcurrencyPolicy poolConcurrencyPolicy;
/*     */   private TlsStrategy tlsStrategy;
/*     */   
/*     */   public static H2RequesterBootstrap bootstrap() {
/* 108 */     return new H2RequesterBootstrap();
/*     */   }
/*     */   
/*     */   private Timeout handshakeTimeout;
/*     */   private Decorator<IOSession> ioSessionDecorator;
/*     */   
/*     */   public final H2RequesterBootstrap setIOReactorConfig(IOReactorConfig ioReactorConfig) {
/* 115 */     this.ioReactorConfig = ioReactorConfig;
/* 116 */     return this;
/*     */   }
/*     */   private Callback<Exception> exceptionCallback; private IOSessionListener sessionListener; private H2StreamListener streamListener;
/*     */   private Http1StreamListener http1StreamListener;
/*     */   private ConnPoolListener<HttpHost> connPoolListener;
/*     */   
/*     */   public final H2RequesterBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
/* 123 */     this.httpProcessor = httpProcessor;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setVersionPolicy(HttpVersionPolicy versionPolicy) {
/* 131 */     this.versionPolicy = versionPolicy;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setH2Config(H2Config h2Config) {
/* 139 */     this.h2Config = h2Config;
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setHttp1Config(Http1Config http1Config) {
/* 147 */     this.http1Config = http1Config;
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setCharCodingConfig(CharCodingConfig charCodingConfig) {
/* 155 */     this.charCodingConfig = charCodingConfig;
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public final H2RequesterBootstrap setDefaultMaxPerRoute(int defaultMaxPerRoute) {
/* 160 */     this.defaultMaxPerRoute = defaultMaxPerRoute;
/* 161 */     return this;
/*     */   }
/*     */   
/*     */   public final H2RequesterBootstrap setMaxTotal(int maxTotal) {
/* 165 */     this.maxTotal = maxTotal;
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public final H2RequesterBootstrap setTimeToLive(TimeValue timeToLive) {
/* 170 */     this.timeToLive = timeToLive;
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setPoolReusePolicy(PoolReusePolicy poolReusePolicy) {
/* 178 */     this.poolReusePolicy = poolReusePolicy;
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Experimental
/*     */   public final H2RequesterBootstrap setPoolConcurrencyPolicy(PoolConcurrencyPolicy poolConcurrencyPolicy) {
/* 187 */     this.poolConcurrencyPolicy = poolConcurrencyPolicy;
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setTlsStrategy(TlsStrategy tlsStrategy) {
/* 195 */     this.tlsStrategy = tlsStrategy;
/* 196 */     return this;
/*     */   }
/*     */   
/*     */   public final H2RequesterBootstrap setHandshakeTimeout(Timeout handshakeTimeout) {
/* 200 */     this.handshakeTimeout = handshakeTimeout;
/* 201 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setIOSessionDecorator(Decorator<IOSession> ioSessionDecorator) {
/* 208 */     this.ioSessionDecorator = ioSessionDecorator;
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setExceptionCallback(Callback<Exception> exceptionCallback) {
/* 216 */     this.exceptionCallback = exceptionCallback;
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setIOSessionListener(IOSessionListener sessionListener) {
/* 224 */     this.sessionListener = sessionListener;
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setStreamListener(H2StreamListener streamListener) {
/* 232 */     this.streamListener = streamListener;
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setStreamListener(Http1StreamListener http1StreamListener) {
/* 240 */     this.http1StreamListener = http1StreamListener;
/* 241 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setConnPoolListener(ConnPoolListener<HttpHost> connPoolListener) {
/* 248 */     this.connPoolListener = connPoolListener;
/* 249 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap setUriPatternType(UriPatternType uriPatternType) {
/* 256 */     this.uriPatternType = uriPatternType;
/* 257 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2RequesterBootstrap register(String uriPattern, Supplier<AsyncPushConsumer> supplier) {
/* 268 */     Args.notBlank(uriPattern, "URI pattern");
/* 269 */     Args.notNull(supplier, "Supplier");
/* 270 */     this.pushConsumerList.add(new HandlerEntry<>(null, uriPattern, supplier));
/* 271 */     return this;
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
/*     */   public final H2RequesterBootstrap registerVirtual(String hostname, String uriPattern, Supplier<AsyncPushConsumer> supplier) {
/* 283 */     Args.notBlank(hostname, "Hostname");
/* 284 */     Args.notBlank(uriPattern, "URI pattern");
/* 285 */     Args.notNull(supplier, "Supplier");
/* 286 */     this.pushConsumerList.add(new HandlerEntry<>(hostname, uriPattern, supplier));
/* 287 */     return this;
/*     */   }
/*     */   public H2AsyncRequester create() {
/*     */     LaxConnPool laxConnPool;
/*     */     StrictConnPool strictConnPool;
/* 292 */     switch ((this.poolConcurrencyPolicy != null) ? this.poolConcurrencyPolicy : PoolConcurrencyPolicy.STRICT) {
/*     */       case LAX:
/* 294 */         laxConnPool = new LaxConnPool((this.defaultMaxPerRoute > 0) ? this.defaultMaxPerRoute : 20, this.timeToLive, this.poolReusePolicy, (DisposalCallback)new DefaultDisposalCallback(), this.connPoolListener);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 303 */         strictConnPool = new StrictConnPool((this.defaultMaxPerRoute > 0) ? this.defaultMaxPerRoute : 20, (this.maxTotal > 0) ? this.maxTotal : 50, this.timeToLive, this.poolReusePolicy, (DisposalCallback)new DefaultDisposalCallback(), this.connPoolListener);
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     RequestHandlerRegistry<Supplier<AsyncPushConsumer>> registry = new RequestHandlerRegistry(this.uriPatternType);
/* 313 */     for (HandlerEntry<Supplier<AsyncPushConsumer>> entry : this.pushConsumerList) {
/* 314 */       registry.register(entry.hostname, entry.uriPattern, entry.handler);
/*     */     }
/*     */ 
/*     */     
/* 318 */     ClientH2StreamMultiplexerFactory http2StreamHandlerFactory = new ClientH2StreamMultiplexerFactory((this.httpProcessor != null) ? this.httpProcessor : H2Processors.client(), (HandlerFactory)new DefaultAsyncPushConsumerFactory((HttpRequestMapper)registry), (this.h2Config != null) ? this.h2Config : H2Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT, this.streamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     TlsStrategy actualTlsStrategy = (this.tlsStrategy != null) ? this.tlsStrategy : (TlsStrategy)new H2ClientTlsStrategy();
/*     */ 
/*     */     
/* 327 */     ClientHttp1StreamDuplexerFactory http1StreamHandlerFactory = new ClientHttp1StreamDuplexerFactory((this.httpProcessor != null) ? this.httpProcessor : HttpProcessors.client(), (this.http1Config != null) ? this.http1Config : Http1Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT, (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE, (NHttpMessageParserFactory)new DefaultHttpResponseParserFactory(this.http1Config), (NHttpMessageWriterFactory)DefaultHttpRequestWriterFactory.INSTANCE, (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE, (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE, this.http1StreamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 337 */     ClientHttpProtocolNegotiationStarter clientHttpProtocolNegotiationStarter = new ClientHttpProtocolNegotiationStarter(http1StreamHandlerFactory, http2StreamHandlerFactory, (this.versionPolicy != null) ? this.versionPolicy : HttpVersionPolicy.NEGOTIATE, actualTlsStrategy, this.handshakeTimeout);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 344 */     return new H2AsyncRequester((this.versionPolicy != null) ? this.versionPolicy : HttpVersionPolicy.NEGOTIATE, this.ioReactorConfig, (IOEventHandlerFactory)clientHttpProtocolNegotiationStarter, this.ioSessionDecorator, this.exceptionCallback, this.sessionListener, (ManagedConnPool<HttpHost, IOSession>)strictConnPool, actualTlsStrategy, this.handshakeTimeout);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/bootstrap/H2RequesterBootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */