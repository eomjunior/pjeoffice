/*     */ package org.apache.hc.core5.http.impl.bootstrap;
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
/*     */ import org.apache.hc.core5.http.impl.nio.DefaultHttpRequestParserFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.DefaultHttpResponseWriterFactory;
/*     */ import org.apache.hc.core5.http.impl.nio.ServerHttp1IOEventHandlerFactory;
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
/*     */ public class AsyncServerBootstrap
/*     */ {
/*  95 */   private final List<HandlerEntry<Supplier<AsyncServerExchangeHandler>>> handlerList = new ArrayList<>(); private String canonicalHostName; private LookupRegistry<Supplier<AsyncServerExchangeHandler>> lookupRegistry; private IOReactorConfig ioReactorConfig;
/*  96 */   private final List<FilterEntry<AsyncFilterHandler>> filters = new ArrayList<>(); private Http1Config http1Config; private CharCodingConfig charCodingConfig;
/*     */   private HttpProcessor httpProcessor;
/*     */   
/*     */   public static AsyncServerBootstrap bootstrap() {
/* 100 */     return new AsyncServerBootstrap();
/*     */   }
/*     */   private ConnectionReuseStrategy connStrategy; private TlsStrategy tlsStrategy; private Timeout handshakeTimeout; private Decorator<IOSession> ioSessionDecorator; private Callback<Exception> exceptionCallback;
/*     */   private IOSessionListener sessionListener;
/*     */   private Http1StreamListener streamListener;
/*     */   
/*     */   public final AsyncServerBootstrap setCanonicalHostName(String canonicalHostName) {
/* 107 */     this.canonicalHostName = canonicalHostName;
/* 108 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setIOReactorConfig(IOReactorConfig ioReactorConfig) {
/* 115 */     this.ioReactorConfig = ioReactorConfig;
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setHttp1Config(Http1Config http1Config) {
/* 123 */     this.http1Config = http1Config;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setCharCodingConfig(CharCodingConfig charCodingConfig) {
/* 131 */     this.charCodingConfig = charCodingConfig;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
/* 139 */     this.httpProcessor = httpProcessor;
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy connStrategy) {
/* 147 */     this.connStrategy = connStrategy;
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setTlsStrategy(TlsStrategy tlsStrategy) {
/* 155 */     this.tlsStrategy = tlsStrategy;
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setTlsHandshakeTimeout(Timeout handshakeTimeout) {
/* 163 */     this.handshakeTimeout = handshakeTimeout;
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setIOSessionDecorator(Decorator<IOSession> ioSessionDecorator) {
/* 171 */     this.ioSessionDecorator = ioSessionDecorator;
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setExceptionCallback(Callback<Exception> exceptionCallback) {
/* 179 */     this.exceptionCallback = exceptionCallback;
/* 180 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setIOSessionListener(IOSessionListener sessionListener) {
/* 187 */     this.sessionListener = sessionListener;
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setLookupRegistry(LookupRegistry<Supplier<AsyncServerExchangeHandler>> lookupRegistry) {
/* 195 */     this.lookupRegistry = lookupRegistry;
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap setStreamListener(Http1StreamListener streamListener) {
/* 205 */     this.streamListener = streamListener;
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap register(String uriPattern, Supplier<AsyncServerExchangeHandler> supplier) {
/* 217 */     Args.notBlank(uriPattern, "URI pattern");
/* 218 */     Args.notNull(supplier, "Supplier");
/* 219 */     this.handlerList.add(new HandlerEntry<>(null, uriPattern, supplier));
/* 220 */     return this;
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
/*     */   public final AsyncServerBootstrap registerVirtual(String hostname, String uriPattern, Supplier<AsyncServerExchangeHandler> supplier) {
/* 232 */     Args.notBlank(hostname, "Hostname");
/* 233 */     Args.notBlank(uriPattern, "URI pattern");
/* 234 */     Args.notNull(supplier, "Supplier");
/* 235 */     this.handlerList.add(new HandlerEntry<>(hostname, uriPattern, supplier));
/* 236 */     return this;
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
/*     */   public final <T> AsyncServerBootstrap register(String uriPattern, AsyncServerRequestHandler<T> requestHandler) {
/* 249 */     register(uriPattern, () -> new BasicServerExchangeHandler(requestHandler));
/* 250 */     return this;
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
/*     */   public final <T> AsyncServerBootstrap registerVirtual(String hostname, String uriPattern, AsyncServerRequestHandler<T> requestHandler) {
/* 265 */     registerVirtual(hostname, uriPattern, () -> new BasicServerExchangeHandler(requestHandler));
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap addFilterBefore(String existing, String name, AsyncFilterHandler filterHandler) {
/* 273 */     Args.notBlank(existing, "Existing");
/* 274 */     Args.notBlank(name, "Name");
/* 275 */     Args.notNull(filterHandler, "Filter handler");
/* 276 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.BEFORE, name, filterHandler, existing));
/* 277 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap addFilterAfter(String existing, String name, AsyncFilterHandler filterHandler) {
/* 284 */     Args.notBlank(existing, "Existing");
/* 285 */     Args.notBlank(name, "Name");
/* 286 */     Args.notNull(filterHandler, "Filter handler");
/* 287 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.AFTER, name, filterHandler, existing));
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap replaceFilter(String existing, AsyncFilterHandler filterHandler) {
/* 295 */     Args.notBlank(existing, "Existing");
/* 296 */     Args.notNull(filterHandler, "Filter handler");
/* 297 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.REPLACE, existing, filterHandler, existing));
/* 298 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap addFilterFirst(String name, AsyncFilterHandler filterHandler) {
/* 305 */     Args.notNull(name, "Name");
/* 306 */     Args.notNull(filterHandler, "Filter handler");
/* 307 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.FIRST, name, filterHandler, null));
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final AsyncServerBootstrap addFilterLast(String name, AsyncFilterHandler filterHandler) {
/* 315 */     Args.notNull(name, "Name");
/* 316 */     Args.notNull(filterHandler, "Filter handler");
/* 317 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.LAST, name, filterHandler, null));
/* 318 */     return this;
/*     */   }
/*     */   
/*     */   public HttpAsyncServer create() {
/*     */     DefaultAsyncResponseExchangeHandlerFactory defaultAsyncResponseExchangeHandlerFactory;
/* 323 */     RequestHandlerRegistry<Supplier<AsyncServerExchangeHandler>> registry = new RequestHandlerRegistry((this.canonicalHostName != null) ? this.canonicalHostName : InetAddressUtils.getCanonicalLocalHostName(), () -> (this.lookupRegistry != null) ? this.lookupRegistry : UriPatternType.newMatcher(UriPatternType.URI_PATTERN));
/*     */ 
/*     */     
/* 326 */     for (HandlerEntry<Supplier<AsyncServerExchangeHandler>> entry : this.handlerList) {
/* 327 */       registry.register(entry.hostname, entry.uriPattern, entry.handler);
/*     */     }
/*     */ 
/*     */     
/* 331 */     if (!this.filters.isEmpty()) {
/* 332 */       NamedElementChain<AsyncFilterHandler> filterChainDefinition = new NamedElementChain();
/* 333 */       filterChainDefinition.addLast(new TerminalAsyncServerFilter((HandlerFactory)new DefaultAsyncResponseExchangeHandlerFactory((HttpRequestMapper)registry)), StandardFilter.MAIN_HANDLER
/*     */           
/* 335 */           .name());
/* 336 */       filterChainDefinition.addFirst(new AsyncServerExpectationFilter(), StandardFilter.EXPECT_CONTINUE
/*     */           
/* 338 */           .name());
/*     */       
/* 340 */       for (FilterEntry<AsyncFilterHandler> entry : this.filters) {
/* 341 */         switch (entry.position) {
/*     */           case AFTER:
/* 343 */             filterChainDefinition.addAfter(entry.existing, entry.filterHandler, entry.name);
/*     */           
/*     */           case BEFORE:
/* 346 */             filterChainDefinition.addBefore(entry.existing, entry.filterHandler, entry.name);
/*     */           
/*     */           case REPLACE:
/* 349 */             filterChainDefinition.replace(entry.existing, entry.filterHandler);
/*     */           
/*     */           case FIRST:
/* 352 */             filterChainDefinition.addFirst(entry.filterHandler, entry.name);
/*     */ 
/*     */ 
/*     */           
/*     */           case LAST:
/* 357 */             filterChainDefinition.addBefore(StandardFilter.MAIN_HANDLER.name(), entry.filterHandler, entry.name);
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 362 */       NamedElementChain<AsyncFilterHandler>.Node current = filterChainDefinition.getLast();
/* 363 */       AsyncServerFilterChainElement execChain = null;
/* 364 */       while (current != null) {
/* 365 */         execChain = new AsyncServerFilterChainElement((AsyncFilterHandler)current.getValue(), execChain);
/* 366 */         current = current.getPrevious();
/*     */       } 
/*     */       
/* 369 */       AsyncServerFilterChainExchangeHandlerFactory asyncServerFilterChainExchangeHandlerFactory = new AsyncServerFilterChainExchangeHandlerFactory(execChain, this.exceptionCallback);
/*     */     } else {
/* 371 */       defaultAsyncResponseExchangeHandlerFactory = new DefaultAsyncResponseExchangeHandlerFactory((HttpRequestMapper)registry, handler -> new BasicAsyncServerExpectationDecorator(handler, this.exceptionCallback));
/*     */     } 
/*     */ 
/*     */     
/* 375 */     ServerHttp1StreamDuplexerFactory streamHandlerFactory = new ServerHttp1StreamDuplexerFactory((this.httpProcessor != null) ? this.httpProcessor : HttpProcessors.server(), (HandlerFactory)defaultAsyncResponseExchangeHandlerFactory, (this.http1Config != null) ? this.http1Config : Http1Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT, (this.connStrategy != null) ? this.connStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE, (NHttpMessageParserFactory)DefaultHttpRequestParserFactory.INSTANCE, (NHttpMessageWriterFactory)DefaultHttpResponseWriterFactory.INSTANCE, (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE, (ContentLengthStrategy)DefaultContentLengthStrategy.INSTANCE, this.streamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 385 */     ServerHttp1IOEventHandlerFactory serverHttp1IOEventHandlerFactory = new ServerHttp1IOEventHandlerFactory(streamHandlerFactory, this.tlsStrategy, this.handshakeTimeout);
/*     */ 
/*     */ 
/*     */     
/* 389 */     return new HttpAsyncServer((IOEventHandlerFactory)serverHttp1IOEventHandlerFactory, this.ioReactorConfig, this.ioSessionDecorator, this.exceptionCallback, this.sessionListener);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/AsyncServerBootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */