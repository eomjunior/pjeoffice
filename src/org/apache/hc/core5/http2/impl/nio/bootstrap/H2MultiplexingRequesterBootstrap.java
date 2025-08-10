/*     */ package org.apache.hc.core5.http2.impl.nio.bootstrap;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequestMapper;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.impl.DefaultAddressResolver;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.RequestHandlerRegistry;
/*     */ import org.apache.hc.core5.http.protocol.UriPatternType;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.impl.H2Processors;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientH2PrefaceHandler;
/*     */ import org.apache.hc.core5.http2.impl.nio.ClientH2StreamMultiplexerFactory;
/*     */ import org.apache.hc.core5.http2.impl.nio.H2StreamListener;
/*     */ import org.apache.hc.core5.http2.nio.support.DefaultAsyncPushConsumerFactory;
/*     */ import org.apache.hc.core5.http2.ssl.H2ClientTlsStrategy;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.IOSessionListener;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class H2MultiplexingRequesterBootstrap
/*     */ {
/*     */   private UriPatternType uriPatternType;
/*     */   private IOReactorConfig ioReactorConfig;
/*     */   private HttpProcessor httpProcessor;
/*     */   private CharCodingConfig charCodingConfig;
/*  75 */   private final List<HandlerEntry<Supplier<AsyncPushConsumer>>> pushConsumerList = new ArrayList<>(); private H2Config h2Config; private TlsStrategy tlsStrategy;
/*     */   private boolean strictALPNHandshake;
/*     */   
/*     */   public static H2MultiplexingRequesterBootstrap bootstrap() {
/*  79 */     return new H2MultiplexingRequesterBootstrap();
/*     */   }
/*     */ 
/*     */   
/*     */   private Decorator<IOSession> ioSessionDecorator;
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setIOReactorConfig(IOReactorConfig ioReactorConfig) {
/*  86 */     this.ioReactorConfig = ioReactorConfig;
/*  87 */     return this;
/*     */   }
/*     */   private Callback<Exception> exceptionCallback;
/*     */   private IOSessionListener sessionListener;
/*     */   private H2StreamListener streamListener;
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
/*  94 */     this.httpProcessor = httpProcessor;
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setH2Config(H2Config h2Config) {
/* 102 */     this.h2Config = h2Config;
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setCharCodingConfig(CharCodingConfig charCodingConfig) {
/* 110 */     this.charCodingConfig = charCodingConfig;
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setTlsStrategy(TlsStrategy tlsStrategy) {
/* 118 */     this.tlsStrategy = tlsStrategy;
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setStrictALPNHandshake(boolean strictALPNHandshake) {
/* 123 */     this.strictALPNHandshake = strictALPNHandshake;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setIOSessionDecorator(Decorator<IOSession> ioSessionDecorator) {
/* 131 */     this.ioSessionDecorator = ioSessionDecorator;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setExceptionCallback(Callback<Exception> exceptionCallback) {
/* 139 */     this.exceptionCallback = exceptionCallback;
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setIOSessionListener(IOSessionListener sessionListener) {
/* 147 */     this.sessionListener = sessionListener;
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setStreamListener(H2StreamListener streamListener) {
/* 155 */     this.streamListener = streamListener;
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap setUriPatternType(UriPatternType uriPatternType) {
/* 163 */     this.uriPatternType = uriPatternType;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2MultiplexingRequesterBootstrap register(String uriPattern, Supplier<AsyncPushConsumer> supplier) {
/* 175 */     Args.notBlank(uriPattern, "URI pattern");
/* 176 */     Args.notNull(supplier, "Supplier");
/* 177 */     this.pushConsumerList.add(new HandlerEntry<>(null, uriPattern, supplier));
/* 178 */     return this;
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
/*     */   public final H2MultiplexingRequesterBootstrap registerVirtual(String hostname, String uriPattern, Supplier<AsyncPushConsumer> supplier) {
/* 190 */     Args.notBlank(hostname, "Hostname");
/* 191 */     Args.notBlank(uriPattern, "URI pattern");
/* 192 */     Args.notNull(supplier, "Supplier");
/* 193 */     this.pushConsumerList.add(new HandlerEntry<>(hostname, uriPattern, supplier));
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public H2MultiplexingRequester create() {
/* 198 */     RequestHandlerRegistry<Supplier<AsyncPushConsumer>> registry = new RequestHandlerRegistry(this.uriPatternType);
/* 199 */     for (HandlerEntry<Supplier<AsyncPushConsumer>> entry : this.pushConsumerList) {
/* 200 */       registry.register(entry.hostname, entry.uriPattern, entry.handler);
/*     */     }
/*     */     
/* 203 */     ClientH2StreamMultiplexerFactory http2StreamHandlerFactory = new ClientH2StreamMultiplexerFactory((this.httpProcessor != null) ? this.httpProcessor : H2Processors.client(), (HandlerFactory)new DefaultAsyncPushConsumerFactory((HttpRequestMapper)registry), (this.h2Config != null) ? this.h2Config : H2Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT, this.streamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     return new H2MultiplexingRequester(this.ioReactorConfig, (ioSession, attachment) -> new ClientH2PrefaceHandler(ioSession, http2StreamHandlerFactory, this.strictALPNHandshake), this.ioSessionDecorator, this.exceptionCallback, this.sessionListener, (Resolver<HttpHost, InetSocketAddress>)DefaultAddressResolver.INSTANCE, (this.tlsStrategy != null) ? this.tlsStrategy : (TlsStrategy)new H2ClientTlsStrategy());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/bootstrap/H2MultiplexingRequesterBootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */