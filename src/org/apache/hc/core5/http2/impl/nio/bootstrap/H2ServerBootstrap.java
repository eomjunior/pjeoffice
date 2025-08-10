/*     */ package org.apache.hc.core5.http2.impl.nio.bootstrap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.ContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.HttpRequestMapper;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.config.NamedElementChain;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.impl.HttpProcessors;
/*     */ import org.apache.hc.core5.http.impl.bootstrap.HttpAsyncServer;
/*     */ import org.apache.hc.core5.http.impl.bootstrap.StandardFilter;
/*     */ import org.apache.hc.core5.http.impl.nio.DefaultHttpRequestParserFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.DefaultHttpResponseWriterFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.ServerHttp1StreamDuplexerFactory;
/*     */ import org.apache.hc.core5.http.nio.AsyncFilterHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerRequestHandler;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageParserFactory;
/*     */ import org.apache.hc.core5.http.nio.NHttpMessageWriterFactory;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.nio.support.AsyncServerExpectationFilter;
/*     */ import org.apache.hc.core5.http.nio.support.AsyncServerFilterChainElement;
/*     */ import org.apache.hc.core5.http.nio.support.AsyncServerFilterChainExchangeHandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.support.BasicAsyncServerExpectationDecorator;
/*     */ import org.apache.hc.core5.http.nio.support.BasicServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.support.DefaultAsyncResponseExchangeHandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.support.TerminalAsyncServerFilter;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.LookupRegistry;
/*     */ import org.apache.hc.core5.http.protocol.RequestHandlerRegistry;
/*     */ import org.apache.hc.core5.http.protocol.UriPatternType;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.impl.H2Processors;
/*     */ import org.apache.hc.core5.http2.impl.nio.H2StreamListener;
/*     */ import org.apache.hc.core5.http2.impl.nio.ServerH2StreamMultiplexerFactory;
/*     */ import org.apache.hc.core5.http2.impl.nio.ServerHttpProtocolNegotiationStarter;
/*     */ import org.apache.hc.core5.http2.ssl.H2ServerTlsStrategy;
/*     */ import org.apache.hc.core5.net.InetAddressUtils;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.IOSessionListener;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class H2ServerBootstrap
/*     */ {
/* 104 */   private final List<HandlerEntry<Supplier<AsyncServerExchangeHandler>>> handlerList = new ArrayList<>(); private String canonicalHostName; private LookupRegistry<Supplier<AsyncServerExchangeHandler>> lookupRegistry; private IOReactorConfig ioReactorConfig; private HttpProcessor httpProcessor;
/* 105 */   private final List<FilterEntry<AsyncFilterHandler>> filters = new ArrayList<>(); private CharCodingConfig charCodingConfig; private HttpVersionPolicy versionPolicy;
/*     */   private H2Config h2Config;
/*     */   
/*     */   public static H2ServerBootstrap bootstrap() {
/* 109 */     return new H2ServerBootstrap();
/*     */   }
/*     */   private Http1Config http1Config; private TlsStrategy tlsStrategy; private Timeout handshakeTimeout; private Decorator<IOSession> ioSessionDecorator;
/*     */   private Callback<Exception> exceptionCallback;
/*     */   private IOSessionListener sessionListener;
/*     */   private H2StreamListener h2StreamListener;
/*     */   private Http1StreamListener http1StreamListener;
/*     */   
/*     */   public final H2ServerBootstrap setCanonicalHostName(String canonicalHostName) {
/* 118 */     this.canonicalHostName = canonicalHostName;
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setIOReactorConfig(IOReactorConfig ioReactorConfig) {
/* 126 */     this.ioReactorConfig = ioReactorConfig;
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
/* 134 */     this.httpProcessor = httpProcessor;
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setVersionPolicy(HttpVersionPolicy versionPolicy) {
/* 142 */     this.versionPolicy = versionPolicy;
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setH2Config(H2Config h2Config) {
/* 150 */     this.h2Config = h2Config;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setHttp1Config(Http1Config http1Config) {
/* 158 */     this.http1Config = http1Config;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setCharset(CharCodingConfig charCodingConfig) {
/* 166 */     this.charCodingConfig = charCodingConfig;
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setTlsStrategy(TlsStrategy tlsStrategy) {
/* 174 */     this.tlsStrategy = tlsStrategy;
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public final H2ServerBootstrap setHandshakeTimeout(Timeout handshakeTimeout) {
/* 179 */     this.handshakeTimeout = handshakeTimeout;
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setIOSessionDecorator(Decorator<IOSession> ioSessionDecorator) {
/* 187 */     this.ioSessionDecorator = ioSessionDecorator;
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setExceptionCallback(Callback<Exception> exceptionCallback) {
/* 195 */     this.exceptionCallback = exceptionCallback;
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setIOSessionListener(IOSessionListener sessionListener) {
/* 203 */     this.sessionListener = sessionListener;
/* 204 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setStreamListener(H2StreamListener h2StreamListener) {
/* 211 */     this.h2StreamListener = h2StreamListener;
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setStreamListener(Http1StreamListener http1StreamListener) {
/* 219 */     this.http1StreamListener = http1StreamListener;
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap setLookupRegistry(LookupRegistry<Supplier<AsyncServerExchangeHandler>> lookupRegistry) {
/* 227 */     this.lookupRegistry = lookupRegistry;
/* 228 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap register(String uriPattern, Supplier<AsyncServerExchangeHandler> supplier) {
/* 239 */     Args.notBlank(uriPattern, "URI pattern");
/* 240 */     Args.notNull(supplier, "Supplier");
/* 241 */     this.handlerList.add(new HandlerEntry<>(null, uriPattern, supplier));
/* 242 */     return this;
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
/*     */   public final H2ServerBootstrap registerVirtual(String hostname, String uriPattern, Supplier<AsyncServerExchangeHandler> supplier) {
/* 254 */     Args.notBlank(hostname, "Hostname");
/* 255 */     Args.notBlank(uriPattern, "URI pattern");
/* 256 */     Args.notNull(supplier, "Supplier");
/* 257 */     this.handlerList.add(new HandlerEntry<>(hostname, uriPattern, supplier));
/* 258 */     return this;
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
/*     */   public final <T> H2ServerBootstrap register(String uriPattern, AsyncServerRequestHandler<T> requestHandler) {
/* 271 */     register(uriPattern, () -> new BasicServerExchangeHandler(requestHandler));
/* 272 */     return this;
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
/*     */   public final <T> H2ServerBootstrap registerVirtual(String hostname, String uriPattern, AsyncServerRequestHandler<T> requestHandler) {
/* 287 */     registerVirtual(hostname, uriPattern, () -> new BasicServerExchangeHandler(requestHandler));
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap addFilterBefore(String existing, String name, AsyncFilterHandler filterHandler) {
/* 295 */     Args.notBlank(existing, "Existing");
/* 296 */     Args.notBlank(name, "Name");
/* 297 */     Args.notNull(filterHandler, "Filter handler");
/* 298 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.BEFORE, name, filterHandler, existing));
/* 299 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap addFilterAfter(String existing, String name, AsyncFilterHandler filterHandler) {
/* 306 */     Args.notBlank(existing, "Existing");
/* 307 */     Args.notBlank(name, "Name");
/* 308 */     Args.notNull(filterHandler, "Filter handler");
/* 309 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.AFTER, name, filterHandler, existing));
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap replaceFilter(String existing, AsyncFilterHandler filterHandler) {
/* 317 */     Args.notBlank(existing, "Existing");
/* 318 */     Args.notNull(filterHandler, "Filter handler");
/* 319 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.REPLACE, existing, filterHandler, existing));
/* 320 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap addFilterFirst(String name, AsyncFilterHandler filterHandler) {
/* 327 */     Args.notNull(name, "Name");
/* 328 */     Args.notNull(filterHandler, "Filter handler");
/* 329 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.FIRST, name, filterHandler, null));
/* 330 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2ServerBootstrap addFilterLast(String name, AsyncFilterHandler filterHandler) {
/* 337 */     Args.notNull(name, "Name");
/* 338 */     Args.notNull(filterHandler, "Filter handler");
/* 339 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.LAST, name, filterHandler, null));
/* 340 */     return this;
/*     */   }
/*     */   public HttpAsyncServer create() {
/*     */     DefaultAsyncResponseExchangeHandlerFactory defaultAsyncResponseExchangeHandlerFactory;
/* 344 */     String actualCanonicalHostName = (this.canonicalHostName != null) ? this.canonicalHostName : InetAddressUtils.getCanonicalLocalHostName();
/* 345 */     RequestHandlerRegistry<Supplier<AsyncServerExchangeHandler>> registry = new RequestHandlerRegistry(actualCanonicalHostName, () -> (this.lookupRegistry != null) ? this.lookupRegistry : UriPatternType.newMatcher(UriPatternType.URI_PATTERN));
/*     */ 
/*     */ 
/*     */     
/* 349 */     for (HandlerEntry<Supplier<AsyncServerExchangeHandler>> entry : this.handlerList) {
/* 350 */       registry.register(entry.hostname, entry.uriPattern, entry.handler);
/*     */     }
/*     */ 
/*     */     
/* 354 */     if (!this.filters.isEmpty()) {
/* 355 */       NamedElementChain<AsyncFilterHandler> filterChainDefinition = new NamedElementChain();
/* 356 */       filterChainDefinition.addLast(new TerminalAsyncServerFilter((HandlerFactory)new DefaultAsyncResponseExchangeHandlerFactory((HttpRequestMapper)registry)), StandardFilter.MAIN_HANDLER
/*     */           
/* 358 */           .name());
/* 359 */       filterChainDefinition.addFirst(new AsyncServerExpectationFilter(), StandardFilter.EXPECT_CONTINUE
/*     */           
/* 361 */           .name());
/*     */       
/* 363 */       for (FilterEntry<AsyncFilterHandler> entry : this.filters) {
/* 364 */         switch (entry.position) {
/*     */           case AFTER:
/* 366 */             filterChainDefinition.addAfter(entry.existing, entry.filterHandler, entry.name);
/*     */           
/*     */           case BEFORE:
/* 369 */             filterChainDefinition.addBefore(entry.existing, entry.filterHandler, entry.name);
/*     */           
/*     */           case REPLACE:
/* 372 */             filterChainDefinition.replace(entry.existing, entry.filterHandler);
/*     */           
/*     */           case FIRST:
/* 375 */             filterChainDefinition.addFirst(entry.filterHandler, entry.name);
/*     */ 
/*     */ 
/*     */           
/*     */           case LAST:
/* 380 */             filterChainDefinition.addBefore(StandardFilter.MAIN_HANDLER.name(), entry.filterHandler, entry.name);
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 385 */       NamedElementChain<AsyncFilterHandler>.Node current = filterChainDefinition.getLast();
/* 386 */       AsyncServerFilterChainElement execChain = null;
/* 387 */       while (current != null) {
/* 388 */         execChain = new AsyncServerFilterChainElement((AsyncFilterHandler)current.getValue(), execChain);
/* 389 */         current = current.getPrevious();
/*     */       } 
/*     */       
/* 392 */       AsyncServerFilterChainExchangeHandlerFactory asyncServerFilterChainExchangeHandlerFactory = new AsyncServerFilterChainExchangeHandlerFactory(execChain, this.exceptionCallback);
/*     */     } else {
/* 394 */       defaultAsyncResponseExchangeHandlerFactory = new DefaultAsyncResponseExchangeHandlerFactory((HttpRequestMapper)registry, handler -> new BasicAsyncServerExpectationDecorator(handler, this.exceptionCallback));
/*     */     } 
/*     */ 
/*     */     
/* 398 */     ServerH2StreamMultiplexerFactory http2StreamHandlerFactory = new ServerH2StreamMultiplexerFactory((this.httpProcessor != null) ? this.httpProcessor : H2Processors.server(), (HandlerFactory)defaultAsyncResponseExchangeHandlerFactory, (this.h2Config != null) ? this.h2Config : H2Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT, this.h2StreamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 404 */     TlsStrategy actualTlsStrategy = (this.tlsStrategy != null) ? this.tlsStrategy : (TlsStrategy)new H2ServerTlsStrategy();
/*     */ 
/*     */     
/* 407 */     ServerHttp1StreamDuplexerFactory http1StreamHandlerFactory = new ServerHttp1StreamDuplexerFactory((this.httpProcessor != null) ? this.httpProcessor : HttpProcessors.server(), (HandlerFactory)defaultAsyncResponseExchangeHandlerFactory, (this.http1Config != null) ? this.http1Config : Http1Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT, (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE, (NHttpMessageParserFactory)DefaultHttpRequestParserFactory.INSTANCE, (NHttpMessageWriterFactory)DefaultHttpResponseWriterFactory.INSTANCE, (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE, (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE, this.http1StreamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 418 */     ServerHttpProtocolNegotiationStarter serverHttpProtocolNegotiationStarter = new ServerHttpProtocolNegotiationStarter(http1StreamHandlerFactory, http2StreamHandlerFactory, (this.versionPolicy != null) ? this.versionPolicy : HttpVersionPolicy.NEGOTIATE, actualTlsStrategy, this.handshakeTimeout);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 425 */     return new HttpAsyncServer((IOEventHandlerFactory)serverHttpProtocolNegotiationStarter, this.ioReactorConfig, this.ioSessionDecorator, this.exceptionCallback, this.sessionListener, actualCanonicalHostName);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/bootstrap/H2ServerBootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */