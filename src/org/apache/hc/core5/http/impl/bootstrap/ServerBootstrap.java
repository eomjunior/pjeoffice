/*     */ package org.apache.hc.core5.http.impl.bootstrap;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.ExceptionListener;
/*     */ import org.apache.hc.core5.http.HttpRequestMapper;
/*     */ import org.apache.hc.core5.http.HttpResponseFactory;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.config.NamedElementChain;
/*     */ import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.impl.Http1StreamListener;
/*     */ import org.apache.hc.core5.http.impl.HttpProcessors;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultBHttpServerConnection;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultBHttpServerConnectionFactory;
/*     */ import org.apache.hc.core5.http.impl.io.DefaultClassicHttpResponseFactory;
/*     */ import org.apache.hc.core5.http.impl.io.HttpService;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.HttpFilterHandler;
/*     */ import org.apache.hc.core5.http.io.HttpRequestHandler;
/*     */ import org.apache.hc.core5.http.io.HttpServerRequestHandler;
/*     */ import org.apache.hc.core5.http.io.SocketConfig;
/*     */ import org.apache.hc.core5.http.io.ssl.DefaultTlsSetupHandler;
/*     */ import org.apache.hc.core5.http.io.support.BasicHttpServerExpectationDecorator;
/*     */ import org.apache.hc.core5.http.io.support.BasicHttpServerRequestHandler;
/*     */ import org.apache.hc.core5.http.io.support.HttpServerExpectationFilter;
/*     */ import org.apache.hc.core5.http.io.support.HttpServerFilterChainElement;
/*     */ import org.apache.hc.core5.http.io.support.HttpServerFilterChainRequestHandler;
/*     */ import org.apache.hc.core5.http.io.support.TerminalServerFilter;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.LookupRegistry;
/*     */ import org.apache.hc.core5.http.protocol.RequestHandlerRegistry;
/*     */ import org.apache.hc.core5.http.protocol.UriPatternType;
/*     */ import org.apache.hc.core5.net.InetAddressUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerBootstrap
/*     */ {
/* 100 */   private final List<HandlerEntry<HttpRequestHandler>> handlerList = new ArrayList<>(); private String canonicalHostName; private LookupRegistry<HttpRequestHandler> lookupRegistry; private int listenerPort; private InetAddress localAddress;
/* 101 */   private final List<FilterEntry<HttpFilterHandler>> filters = new ArrayList<>(); private SocketConfig socketConfig; private Http1Config http1Config; private CharCodingConfig charCodingConfig;
/*     */   private HttpProcessor httpProcessor;
/*     */   
/*     */   public static ServerBootstrap bootstrap() {
/* 105 */     return new ServerBootstrap();
/*     */   }
/*     */   private ConnectionReuseStrategy connStrategy; private HttpResponseFactory<ClassicHttpResponse> responseFactory; private ServerSocketFactory serverSocketFactory; private SSLContext sslContext;
/*     */   private Callback<SSLParameters> sslSetupHandler;
/*     */   private HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory;
/*     */   private ExceptionListener exceptionListener;
/*     */   private Http1StreamListener streamListener;
/*     */   
/*     */   public final ServerBootstrap setCanonicalHostName(String canonicalHostName) {
/* 114 */     this.canonicalHostName = canonicalHostName;
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setListenerPort(int listenerPort) {
/* 122 */     this.listenerPort = listenerPort;
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setLocalAddress(InetAddress localAddress) {
/* 130 */     this.localAddress = localAddress;
/* 131 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setSocketConfig(SocketConfig socketConfig) {
/* 138 */     this.socketConfig = socketConfig;
/* 139 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setHttp1Config(Http1Config http1Config) {
/* 146 */     this.http1Config = http1Config;
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setCharCodingConfig(CharCodingConfig charCodingConfig) {
/* 154 */     this.charCodingConfig = charCodingConfig;
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setHttpProcessor(HttpProcessor httpProcessor) {
/* 162 */     this.httpProcessor = httpProcessor;
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setConnectionReuseStrategy(ConnectionReuseStrategy connStrategy) {
/* 170 */     this.connStrategy = connStrategy;
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setResponseFactory(HttpResponseFactory<ClassicHttpResponse> responseFactory) {
/* 178 */     this.responseFactory = responseFactory;
/* 179 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setLookupRegistry(LookupRegistry<HttpRequestHandler> lookupRegistry) {
/* 186 */     this.lookupRegistry = lookupRegistry;
/* 187 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap register(String uriPattern, HttpRequestHandler requestHandler) {
/* 198 */     Args.notBlank(uriPattern, "URI pattern");
/* 199 */     Args.notNull(requestHandler, "Supplier");
/* 200 */     this.handlerList.add(new HandlerEntry<>(null, uriPattern, requestHandler));
/* 201 */     return this;
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
/*     */   public final ServerBootstrap registerVirtual(String hostname, String uriPattern, HttpRequestHandler requestHandler) {
/* 213 */     Args.notBlank(hostname, "Hostname");
/* 214 */     Args.notBlank(uriPattern, "URI pattern");
/* 215 */     Args.notNull(requestHandler, "Supplier");
/* 216 */     this.handlerList.add(new HandlerEntry<>(hostname, uriPattern, requestHandler));
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setConnectionFactory(HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactory) {
/* 225 */     this.connectionFactory = connectionFactory;
/* 226 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setServerSocketFactory(ServerSocketFactory serverSocketFactory) {
/* 233 */     this.serverSocketFactory = serverSocketFactory;
/* 234 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setSslContext(SSLContext sslContext) {
/* 244 */     this.sslContext = sslContext;
/* 245 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setSslSetupHandler(Callback<SSLParameters> sslSetupHandler) {
/* 252 */     this.sslSetupHandler = sslSetupHandler;
/* 253 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setExceptionListener(ExceptionListener exceptionListener) {
/* 260 */     this.exceptionListener = exceptionListener;
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap setStreamListener(Http1StreamListener streamListener) {
/* 268 */     this.streamListener = streamListener;
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap addFilterBefore(String existing, String name, HttpFilterHandler filterHandler) {
/* 276 */     Args.notBlank(existing, "Existing");
/* 277 */     Args.notBlank(name, "Name");
/* 278 */     Args.notNull(filterHandler, "Filter handler");
/* 279 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.BEFORE, name, filterHandler, existing));
/* 280 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap addFilterAfter(String existing, String name, HttpFilterHandler filterHandler) {
/* 287 */     Args.notBlank(existing, "Existing");
/* 288 */     Args.notBlank(name, "Name");
/* 289 */     Args.notNull(filterHandler, "Filter handler");
/* 290 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.AFTER, name, filterHandler, existing));
/* 291 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap replaceFilter(String existing, HttpFilterHandler filterHandler) {
/* 298 */     Args.notBlank(existing, "Existing");
/* 299 */     Args.notNull(filterHandler, "Filter handler");
/* 300 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.REPLACE, existing, filterHandler, existing));
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap addFilterFirst(String name, HttpFilterHandler filterHandler) {
/* 308 */     Args.notNull(name, "Name");
/* 309 */     Args.notNull(filterHandler, "Filter handler");
/* 310 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.FIRST, name, filterHandler, null));
/* 311 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ServerBootstrap addFilterLast(String name, HttpFilterHandler filterHandler) {
/* 318 */     Args.notNull(name, "Name");
/* 319 */     Args.notNull(filterHandler, "Filter handler");
/* 320 */     this.filters.add(new FilterEntry<>(FilterEntry.Position.LAST, name, filterHandler, null));
/* 321 */     return this;
/*     */   }
/*     */   public HttpServer create() {
/*     */     BasicHttpServerExpectationDecorator basicHttpServerExpectationDecorator;
/*     */     DefaultBHttpServerConnectionFactory defaultBHttpServerConnectionFactory;
/* 326 */     RequestHandlerRegistry<HttpRequestHandler> handlerRegistry = new RequestHandlerRegistry((this.canonicalHostName != null) ? this.canonicalHostName : InetAddressUtils.getCanonicalLocalHostName(), () -> (this.lookupRegistry != null) ? this.lookupRegistry : UriPatternType.newMatcher(UriPatternType.URI_PATTERN));
/*     */ 
/*     */     
/* 329 */     for (HandlerEntry<HttpRequestHandler> entry : this.handlerList) {
/* 330 */       handlerRegistry.register(entry.hostname, entry.uriPattern, entry.handler);
/*     */     }
/*     */ 
/*     */     
/* 334 */     if (!this.filters.isEmpty()) {
/* 335 */       NamedElementChain<HttpFilterHandler> filterChainDefinition = new NamedElementChain();
/* 336 */       filterChainDefinition.addLast(new TerminalServerFilter((HttpRequestMapper)handlerRegistry, (this.responseFactory != null) ? this.responseFactory : (HttpResponseFactory)DefaultClassicHttpResponseFactory.INSTANCE), StandardFilter.MAIN_HANDLER
/*     */ 
/*     */ 
/*     */           
/* 340 */           .name());
/* 341 */       filterChainDefinition.addFirst(new HttpServerExpectationFilter(), StandardFilter.EXPECT_CONTINUE
/*     */           
/* 343 */           .name());
/*     */       
/* 345 */       for (FilterEntry<HttpFilterHandler> entry : this.filters) {
/* 346 */         switch (entry.position) {
/*     */           case AFTER:
/* 348 */             filterChainDefinition.addAfter(entry.existing, entry.filterHandler, entry.name);
/*     */           
/*     */           case BEFORE:
/* 351 */             filterChainDefinition.addBefore(entry.existing, entry.filterHandler, entry.name);
/*     */           
/*     */           case REPLACE:
/* 354 */             filterChainDefinition.replace(entry.existing, entry.filterHandler);
/*     */           
/*     */           case FIRST:
/* 357 */             filterChainDefinition.addFirst(entry.filterHandler, entry.name);
/*     */ 
/*     */ 
/*     */           
/*     */           case LAST:
/* 362 */             filterChainDefinition.addBefore(StandardFilter.MAIN_HANDLER.name(), entry.filterHandler, entry.name);
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 367 */       NamedElementChain<HttpFilterHandler>.Node current = filterChainDefinition.getLast();
/* 368 */       HttpServerFilterChainElement filterChain = null;
/* 369 */       while (current != null) {
/* 370 */         filterChain = new HttpServerFilterChainElement((HttpFilterHandler)current.getValue(), filterChain);
/* 371 */         current = current.getPrevious();
/*     */       } 
/* 373 */       HttpServerFilterChainRequestHandler httpServerFilterChainRequestHandler = new HttpServerFilterChainRequestHandler(filterChain);
/*     */     } else {
/* 375 */       basicHttpServerExpectationDecorator = new BasicHttpServerExpectationDecorator((HttpServerRequestHandler)new BasicHttpServerRequestHandler((HttpRequestMapper)handlerRegistry, (this.responseFactory != null) ? this.responseFactory : (HttpResponseFactory)DefaultClassicHttpResponseFactory.INSTANCE));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 381 */     HttpService httpService = new HttpService((this.httpProcessor != null) ? this.httpProcessor : HttpProcessors.server(), (HttpServerRequestHandler)basicHttpServerExpectationDecorator, (this.connStrategy != null) ? this.connStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE, this.streamListener);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 386 */     ServerSocketFactory serverSocketFactoryCopy = this.serverSocketFactory;
/* 387 */     if (serverSocketFactoryCopy == null) {
/* 388 */       if (this.sslContext != null) {
/* 389 */         serverSocketFactoryCopy = this.sslContext.getServerSocketFactory();
/*     */       } else {
/* 391 */         serverSocketFactoryCopy = ServerSocketFactory.getDefault();
/*     */       } 
/*     */     }
/*     */     
/* 395 */     HttpConnectionFactory<? extends DefaultBHttpServerConnection> connectionFactoryCopy = this.connectionFactory;
/* 396 */     if (connectionFactoryCopy == null) {
/* 397 */       String scheme = (serverSocketFactoryCopy instanceof javax.net.ssl.SSLServerSocketFactory) ? URIScheme.HTTPS.id : URIScheme.HTTP.id;
/* 398 */       defaultBHttpServerConnectionFactory = new DefaultBHttpServerConnectionFactory(scheme, this.http1Config, this.charCodingConfig);
/*     */     } 
/*     */     
/* 401 */     return new HttpServer(
/* 402 */         Math.max(this.listenerPort, 0), httpService, this.localAddress, (this.socketConfig != null) ? this.socketConfig : SocketConfig.DEFAULT, serverSocketFactoryCopy, (HttpConnectionFactory<? extends DefaultBHttpServerConnection>)defaultBHttpServerConnectionFactory, (this.sslSetupHandler != null) ? this.sslSetupHandler : (Callback<SSLParameters>)new DefaultTlsSetupHandler(), (this.exceptionListener != null) ? this.exceptionListener : ExceptionListener.NO_OP);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/ServerBootstrap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */