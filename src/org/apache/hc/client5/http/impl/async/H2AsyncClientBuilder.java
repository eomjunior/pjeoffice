/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.HttpRequestRetryStrategy;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChainHandler;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.cookie.BasicCookieStore;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.client5.http.impl.ChainElement;
/*     */ import org.apache.hc.client5.http.impl.CookieSpecSupport;
/*     */ import org.apache.hc.client5.http.impl.DefaultAuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.DigestSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.KerberosSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.NTLMSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.SPNegoSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.SystemDefaultCredentialsProvider;
/*     */ import org.apache.hc.client5.http.impl.nio.MultihomeConnectionInitiator;
/*     */ import org.apache.hc.client5.http.impl.routing.DefaultRoutePlanner;
/*     */ import org.apache.hc.client5.http.protocol.RedirectStrategy;
/*     */ import org.apache.hc.client5.http.protocol.RequestAddCookies;
/*     */ import org.apache.hc.client5.http.protocol.RequestDefaultHeaders;
/*     */ import org.apache.hc.client5.http.protocol.RequestExpectContinue;
/*     */ import org.apache.hc.client5.http.protocol.ResponseProcessCookies;
/*     */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*     */ import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.HttpResponseInterceptor;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.config.NamedElementChain;
/*     */ import org.apache.hc.core5.http.config.Registry;
/*     */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.protocol.DefaultHttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessorBuilder;
/*     */ import org.apache.hc.core5.http.protocol.RequestTargetHost;
/*     */ import org.apache.hc.core5.http.protocol.RequestUserAgent;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.protocol.H2RequestConnControl;
/*     */ import org.apache.hc.core5.http2.protocol.H2RequestContent;
/*     */ import org.apache.hc.core5.http2.protocol.H2RequestTargetHost;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.IOSessionListener;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.VersionInfo;
/*     */ 
/*     */ public class H2AsyncClientBuilder {
/*     */   private IOReactorConfig ioReactorConfig;
/*     */   private IOSessionListener ioSessionListener;
/*     */   private H2Config h2Config;
/*     */   private CharCodingConfig charCodingConfig;
/*     */   private SchemePortResolver schemePortResolver;
/*     */   private AuthenticationStrategy targetAuthStrategy;
/*     */   private AuthenticationStrategy proxyAuthStrategy;
/*     */   private LinkedList<RequestInterceptorEntry> requestInterceptors;
/*     */   private LinkedList<ResponseInterceptorEntry> responseInterceptors;
/*     */   private LinkedList<ExecInterceptorEntry> execInterceptors;
/*     */   private HttpRoutePlanner routePlanner;
/*     */   private RedirectStrategy redirectStrategy;
/*     */   private HttpRequestRetryStrategy retryStrategy;
/*     */   private Lookup<AuthSchemeFactory> authSchemeRegistry;
/*     */   private Lookup<CookieSpecFactory> cookieSpecRegistry;
/*     */   private CookieStore cookieStore;
/*     */   private CredentialsProvider credentialsProvider;
/*     */   private String userAgent;
/*     */   private Collection<? extends Header> defaultHeaders;
/*     */   private RequestConfig defaultRequestConfig;
/*     */   private Resolver<HttpHost, ConnectionConfig> connectionConfigResolver;
/*     */   private boolean evictIdleConnections;
/*     */   private TimeValue maxIdleTime;
/*     */   private boolean systemProperties;
/*     */   private boolean automaticRetriesDisabled;
/*     */   private boolean redirectHandlingDisabled;
/*     */   private boolean cookieManagementDisabled;
/*     */   private boolean authCachingDisabled;
/*     */   private DnsResolver dnsResolver;
/*     */   private TlsStrategy tlsStrategy;
/*     */   private ThreadFactory threadFactory;
/*     */   private List<Closeable> closeables;
/*     */   private Callback<Exception> ioReactorExceptionCallback;
/*     */   private Decorator<IOSession> ioSessionDecorator;
/*     */   
/*     */   private static class RequestInterceptorEntry {
/*     */     final Position position;
/*     */     final HttpRequestInterceptor interceptor;
/*     */     
/*     */     enum Position {
/* 128 */       FIRST, LAST;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private RequestInterceptorEntry(Position position, HttpRequestInterceptor interceptor) {
/* 134 */       this.position = position;
/* 135 */       this.interceptor = interceptor;
/*     */     } }
/*     */   enum Position { FIRST, LAST; }
/*     */   private static class ResponseInterceptorEntry {
/*     */     final Position position; final HttpResponseInterceptor interceptor;
/*     */     
/* 141 */     enum Position { FIRST, LAST; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ResponseInterceptorEntry(Position position, HttpResponseInterceptor interceptor) {
/* 147 */       this.position = position;
/* 148 */       this.interceptor = interceptor;
/*     */     }
/*     */   }
/*     */   enum Position { FIRST, LAST; }
/*     */   private static class ExecInterceptorEntry { final Position position; final String name; final AsyncExecChainHandler interceptor; final String existing;
/*     */     
/* 154 */     enum Position { BEFORE, AFTER, REPLACE, FIRST, LAST; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ExecInterceptorEntry(Position position, String name, AsyncExecChainHandler interceptor, String existing) {
/* 166 */       this.position = position;
/* 167 */       this.name = name;
/* 168 */       this.interceptor = interceptor;
/* 169 */       this.existing = existing;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum Position
/*     */   {
/*     */     BEFORE, AFTER, REPLACE, FIRST, LAST;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static H2AsyncClientBuilder create() {
/* 221 */     return new H2AsyncClientBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setH2Config(H2Config h2Config) {
/* 232 */     this.h2Config = h2Config;
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setIOReactorConfig(IOReactorConfig ioReactorConfig) {
/* 240 */     this.ioReactorConfig = ioReactorConfig;
/* 241 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setIOSessionListener(IOSessionListener ioSessionListener) {
/* 250 */     this.ioSessionListener = ioSessionListener;
/* 251 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setCharCodingConfig(CharCodingConfig charCodingConfig) {
/* 258 */     this.charCodingConfig = charCodingConfig;
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy) {
/* 268 */     this.targetAuthStrategy = targetAuthStrategy;
/* 269 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy) {
/* 278 */     this.proxyAuthStrategy = proxyAuthStrategy;
/* 279 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setIoReactorExceptionCallback(Callback<Exception> ioReactorExceptionCallback) {
/* 288 */     this.ioReactorExceptionCallback = ioReactorExceptionCallback;
/* 289 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setIoSessionDecorator(Decorator<IOSession> ioSessionDecorator) {
/* 299 */     this.ioSessionDecorator = ioSessionDecorator;
/* 300 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder addResponseInterceptorFirst(HttpResponseInterceptor interceptor) {
/* 307 */     Args.notNull(interceptor, "Interceptor");
/* 308 */     if (this.responseInterceptors == null) {
/* 309 */       this.responseInterceptors = new LinkedList<>();
/*     */     }
/* 311 */     this.responseInterceptors.add(new ResponseInterceptorEntry(ResponseInterceptorEntry.Position.FIRST, interceptor));
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder addResponseInterceptorLast(HttpResponseInterceptor interceptor) {
/* 319 */     Args.notNull(interceptor, "Interceptor");
/* 320 */     if (this.responseInterceptors == null) {
/* 321 */       this.responseInterceptors = new LinkedList<>();
/*     */     }
/* 323 */     this.responseInterceptors.add(new ResponseInterceptorEntry(ResponseInterceptorEntry.Position.LAST, interceptor));
/* 324 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder addExecInterceptorBefore(String existing, String name, AsyncExecChainHandler interceptor) {
/* 331 */     Args.notBlank(existing, "Existing");
/* 332 */     Args.notBlank(name, "Name");
/* 333 */     Args.notNull(interceptor, "Interceptor");
/* 334 */     if (this.execInterceptors == null) {
/* 335 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 337 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.BEFORE, name, interceptor, existing));
/* 338 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder addExecInterceptorAfter(String existing, String name, AsyncExecChainHandler interceptor) {
/* 345 */     Args.notBlank(existing, "Existing");
/* 346 */     Args.notBlank(name, "Name");
/* 347 */     Args.notNull(interceptor, "Interceptor");
/* 348 */     if (this.execInterceptors == null) {
/* 349 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 351 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.AFTER, name, interceptor, existing));
/* 352 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder replaceExecInterceptor(String existing, AsyncExecChainHandler interceptor) {
/* 359 */     Args.notBlank(existing, "Existing");
/* 360 */     Args.notNull(interceptor, "Interceptor");
/* 361 */     if (this.execInterceptors == null) {
/* 362 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 364 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.REPLACE, existing, interceptor, existing));
/* 365 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder addExecInterceptorFirst(String name, AsyncExecChainHandler interceptor) {
/* 372 */     Args.notNull(name, "Name");
/* 373 */     Args.notNull(interceptor, "Interceptor");
/* 374 */     if (this.execInterceptors == null) {
/* 375 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 377 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.FIRST, name, interceptor, null));
/* 378 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder addExecInterceptorLast(String name, AsyncExecChainHandler interceptor) {
/* 385 */     Args.notNull(name, "Name");
/* 386 */     Args.notNull(interceptor, "Interceptor");
/* 387 */     if (this.execInterceptors == null) {
/* 388 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 390 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.LAST, name, interceptor, null));
/* 391 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder addRequestInterceptorFirst(HttpRequestInterceptor interceptor) {
/* 398 */     Args.notNull(interceptor, "Interceptor");
/* 399 */     if (this.requestInterceptors == null) {
/* 400 */       this.requestInterceptors = new LinkedList<>();
/*     */     }
/* 402 */     this.requestInterceptors.add(new RequestInterceptorEntry(RequestInterceptorEntry.Position.FIRST, interceptor));
/* 403 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder addRequestInterceptorLast(HttpRequestInterceptor interceptor) {
/* 410 */     Args.notNull(interceptor, "Interceptor");
/* 411 */     if (this.requestInterceptors == null) {
/* 412 */       this.requestInterceptors = new LinkedList<>();
/*     */     }
/* 414 */     this.requestInterceptors.add(new RequestInterceptorEntry(RequestInterceptorEntry.Position.LAST, interceptor));
/* 415 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setRetryStrategy(HttpRequestRetryStrategy retryStrategy) {
/* 425 */     this.retryStrategy = retryStrategy;
/* 426 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public H2AsyncClientBuilder setRedirectStrategy(RedirectStrategy redirectStrategy) {
/* 437 */     this.redirectStrategy = redirectStrategy;
/* 438 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setSchemePortResolver(SchemePortResolver schemePortResolver) {
/* 445 */     this.schemePortResolver = schemePortResolver;
/* 446 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setDnsResolver(DnsResolver dnsResolver) {
/* 453 */     this.dnsResolver = dnsResolver;
/* 454 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setTlsStrategy(TlsStrategy tlsStrategy) {
/* 461 */     this.tlsStrategy = tlsStrategy;
/* 462 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setThreadFactory(ThreadFactory threadFactory) {
/* 469 */     this.threadFactory = threadFactory;
/* 470 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setUserAgent(String userAgent) {
/* 477 */     this.userAgent = userAgent;
/* 478 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setDefaultHeaders(Collection<? extends Header> defaultHeaders) {
/* 485 */     this.defaultHeaders = defaultHeaders;
/* 486 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setRoutePlanner(HttpRoutePlanner routePlanner) {
/* 493 */     this.routePlanner = routePlanner;
/* 494 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setDefaultCredentialsProvider(CredentialsProvider credentialsProvider) {
/* 503 */     this.credentialsProvider = credentialsProvider;
/* 504 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setDefaultAuthSchemeRegistry(Lookup<AuthSchemeFactory> authSchemeRegistry) {
/* 513 */     this.authSchemeRegistry = authSchemeRegistry;
/* 514 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setDefaultCookieSpecRegistry(Lookup<CookieSpecFactory> cookieSpecRegistry) {
/* 523 */     this.cookieSpecRegistry = cookieSpecRegistry;
/* 524 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setDefaultCookieStore(CookieStore cookieStore) {
/* 532 */     this.cookieStore = cookieStore;
/* 533 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setDefaultRequestConfig(RequestConfig config) {
/* 542 */     this.defaultRequestConfig = config;
/* 543 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setConnectionConfigResolver(Resolver<HttpHost, ConnectionConfig> connectionConfigResolver) {
/* 552 */     this.connectionConfigResolver = connectionConfigResolver;
/* 553 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder setDefaultConnectionConfig(ConnectionConfig connectionConfig) {
/* 562 */     this.connectionConfigResolver = (host -> connectionConfig);
/* 563 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder useSystemProperties() {
/* 571 */     this.systemProperties = true;
/* 572 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder disableRedirectHandling() {
/* 579 */     this.redirectHandlingDisabled = true;
/* 580 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder disableAutomaticRetries() {
/* 587 */     this.automaticRetriesDisabled = true;
/* 588 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder disableCookieManagement() {
/* 595 */     this.cookieManagementDisabled = true;
/* 596 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder disableAuthCaching() {
/* 603 */     this.authCachingDisabled = true;
/* 604 */     return this;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final H2AsyncClientBuilder evictIdleConnections(TimeValue maxIdleTime) {
/* 622 */     this.evictIdleConnections = true;
/* 623 */     this.maxIdleTime = maxIdleTime;
/* 624 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Internal
/*     */   protected void customizeExecChain(NamedElementChain<AsyncExecChainHandler> execChainDefinition) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Internal
/*     */   protected void addCloseable(Closeable closeable) {
/* 643 */     if (closeable == null) {
/*     */       return;
/*     */     }
/* 646 */     if (this.closeables == null) {
/* 647 */       this.closeables = new ArrayList<>();
/*     */     }
/* 649 */     this.closeables.add(closeable); } public CloseableHttpAsyncClient build() { DefaultAuthenticationStrategy defaultAuthenticationStrategy1, defaultAuthenticationStrategy2; DefaultRoutePlanner defaultRoutePlanner;
/*     */     Registry registry;
/*     */     BasicCookieStore basicCookieStore;
/*     */     BasicCredentialsProvider basicCredentialsProvider;
/* 653 */     AuthenticationStrategy targetAuthStrategyCopy = this.targetAuthStrategy;
/* 654 */     if (targetAuthStrategyCopy == null) {
/* 655 */       defaultAuthenticationStrategy1 = DefaultAuthenticationStrategy.INSTANCE;
/*     */     }
/* 657 */     AuthenticationStrategy proxyAuthStrategyCopy = this.proxyAuthStrategy;
/* 658 */     if (proxyAuthStrategyCopy == null) {
/* 659 */       defaultAuthenticationStrategy2 = DefaultAuthenticationStrategy.INSTANCE;
/*     */     }
/*     */     
/* 662 */     String userAgentCopy = this.userAgent;
/* 663 */     if (userAgentCopy == null) {
/* 664 */       if (this.systemProperties) {
/* 665 */         userAgentCopy = getProperty("http.agent", null);
/*     */       }
/* 667 */       if (userAgentCopy == null) {
/* 668 */         userAgentCopy = VersionInfo.getSoftwareInfo("Apache-HttpAsyncClient", "org.apache.hc.client5", 
/* 669 */             getClass());
/*     */       }
/*     */     } 
/*     */     
/* 673 */     HttpProcessorBuilder b = HttpProcessorBuilder.create();
/* 674 */     if (this.requestInterceptors != null) {
/* 675 */       for (RequestInterceptorEntry entry : this.requestInterceptors) {
/* 676 */         if (entry.position == RequestInterceptorEntry.Position.FIRST) {
/* 677 */           b.addFirst(entry.interceptor);
/*     */         }
/*     */       } 
/*     */     }
/* 681 */     if (this.responseInterceptors != null) {
/* 682 */       for (ResponseInterceptorEntry entry : this.responseInterceptors) {
/* 683 */         if (entry.position == ResponseInterceptorEntry.Position.FIRST) {
/* 684 */           b.addFirst(entry.interceptor);
/*     */         }
/*     */       } 
/*     */     }
/* 688 */     b.addAll(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestDefaultHeaders(this.defaultHeaders), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy), (HttpRequestInterceptor)new RequestExpectContinue(), (HttpRequestInterceptor)new H2RequestContent(), (HttpRequestInterceptor)new H2RequestTargetHost(), (HttpRequestInterceptor)new H2RequestConnControl() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 695 */     if (!this.cookieManagementDisabled) {
/* 696 */       b.add((HttpRequestInterceptor)RequestAddCookies.INSTANCE);
/*     */     }
/* 698 */     if (!this.cookieManagementDisabled) {
/* 699 */       b.add((HttpResponseInterceptor)ResponseProcessCookies.INSTANCE);
/*     */     }
/* 701 */     if (this.requestInterceptors != null) {
/* 702 */       for (RequestInterceptorEntry entry : this.requestInterceptors) {
/* 703 */         if (entry.position == RequestInterceptorEntry.Position.LAST) {
/* 704 */           b.addLast(entry.interceptor);
/*     */         }
/*     */       } 
/*     */     }
/* 708 */     if (this.responseInterceptors != null) {
/* 709 */       for (ResponseInterceptorEntry entry : this.responseInterceptors) {
/* 710 */         if (entry.position == ResponseInterceptorEntry.Position.LAST) {
/* 711 */           b.addLast(entry.interceptor);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 716 */     HttpProcessor httpProcessor = b.build();
/*     */     
/* 718 */     NamedElementChain<AsyncExecChainHandler> execChainDefinition = new NamedElementChain();
/* 719 */     execChainDefinition.addLast(new H2AsyncMainClientExec(httpProcessor), ChainElement.MAIN_TRANSPORT
/*     */         
/* 721 */         .name());
/*     */     
/* 723 */     execChainDefinition.addFirst(new AsyncConnectExec((HttpProcessor)new DefaultHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy) }, ), (AuthenticationStrategy)defaultAuthenticationStrategy2, (this.schemePortResolver != null) ? this.schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE, this.authCachingDisabled), ChainElement.CONNECT
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 729 */         .name());
/*     */     
/* 731 */     execChainDefinition.addFirst(new AsyncProtocolExec((AuthenticationStrategy)defaultAuthenticationStrategy1, (AuthenticationStrategy)defaultAuthenticationStrategy2, (this.schemePortResolver != null) ? this.schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE, this.authCachingDisabled), ChainElement.PROTOCOL
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 737 */         .name());
/*     */ 
/*     */     
/* 740 */     if (!this.automaticRetriesDisabled) {
/* 741 */       DefaultHttpRequestRetryStrategy defaultHttpRequestRetryStrategy; HttpRequestRetryStrategy retryStrategyCopy = this.retryStrategy;
/* 742 */       if (retryStrategyCopy == null) {
/* 743 */         defaultHttpRequestRetryStrategy = DefaultHttpRequestRetryStrategy.INSTANCE;
/*     */       }
/* 745 */       execChainDefinition.addFirst(new AsyncHttpRequestRetryExec((HttpRequestRetryStrategy)defaultHttpRequestRetryStrategy), ChainElement.RETRY
/*     */           
/* 747 */           .name());
/*     */     } 
/*     */     
/* 750 */     HttpRoutePlanner routePlannerCopy = this.routePlanner;
/* 751 */     if (routePlannerCopy == null) {
/* 752 */       DefaultSchemePortResolver defaultSchemePortResolver; SchemePortResolver schemePortResolverCopy = this.schemePortResolver;
/* 753 */       if (schemePortResolverCopy == null) {
/* 754 */         defaultSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
/*     */       }
/* 756 */       defaultRoutePlanner = new DefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver);
/*     */     } 
/*     */ 
/*     */     
/* 760 */     if (!this.redirectHandlingDisabled) {
/* 761 */       DefaultRedirectStrategy defaultRedirectStrategy; RedirectStrategy redirectStrategyCopy = this.redirectStrategy;
/* 762 */       if (redirectStrategyCopy == null) {
/* 763 */         defaultRedirectStrategy = DefaultRedirectStrategy.INSTANCE;
/*     */       }
/* 765 */       execChainDefinition.addFirst(new AsyncRedirectExec((HttpRoutePlanner)defaultRoutePlanner, (RedirectStrategy)defaultRedirectStrategy), ChainElement.REDIRECT
/*     */           
/* 767 */           .name());
/*     */     } 
/*     */     
/* 770 */     AsyncPushConsumerRegistry pushConsumerRegistry = new AsyncPushConsumerRegistry();
/*     */     
/* 772 */     IOEventHandlerFactory ioEventHandlerFactory = new H2AsyncClientProtocolStarter(HttpProcessorBuilder.create().build(), (request, context) -> pushConsumerRegistry.get(request), (this.h2Config != null) ? this.h2Config : H2Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT);
/*     */ 
/*     */ 
/*     */     
/* 776 */     DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioEventHandlerFactory, (this.ioReactorConfig != null) ? this.ioReactorConfig : IOReactorConfig.DEFAULT, (this.threadFactory != null) ? this.threadFactory : (ThreadFactory)new DefaultThreadFactory("httpclient-dispatch", true), (this.ioSessionDecorator != null) ? this.ioSessionDecorator : LoggingIOSessionDecorator.INSTANCE, (this.ioReactorExceptionCallback != null) ? this.ioReactorExceptionCallback : LoggingExceptionCallback.INSTANCE, this.ioSessionListener, ioSession -> ioSession.enqueue((Command)new ShutdownCommand(CloseMode.GRACEFUL), Command.Priority.IMMEDIATE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 785 */     if (this.execInterceptors != null) {
/* 786 */       for (ExecInterceptorEntry entry : this.execInterceptors) {
/* 787 */         switch (entry.position) {
/*     */           case AFTER:
/* 789 */             execChainDefinition.addAfter(entry.existing, entry.interceptor, entry.name);
/*     */           
/*     */           case BEFORE:
/* 792 */             execChainDefinition.addBefore(entry.existing, entry.interceptor, entry.name);
/*     */           
/*     */           case REPLACE:
/* 795 */             execChainDefinition.replace(entry.existing, entry.interceptor);
/*     */           
/*     */           case FIRST:
/* 798 */             execChainDefinition.addFirst(entry.interceptor, entry.name);
/*     */ 
/*     */ 
/*     */           
/*     */           case LAST:
/* 803 */             execChainDefinition.addBefore(ChainElement.MAIN_TRANSPORT.name(), entry.interceptor, entry.name);
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*     */     }
/* 809 */     customizeExecChain(execChainDefinition);
/*     */     
/* 811 */     NamedElementChain<AsyncExecChainHandler>.Node current = execChainDefinition.getLast();
/* 812 */     AsyncExecChainElement execChain = null;
/* 813 */     while (current != null) {
/* 814 */       execChain = new AsyncExecChainElement((AsyncExecChainHandler)current.getValue(), execChain);
/* 815 */       current = current.getPrevious();
/*     */     } 
/*     */     
/* 818 */     Lookup<AuthSchemeFactory> authSchemeRegistryCopy = this.authSchemeRegistry;
/* 819 */     if (authSchemeRegistryCopy == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 826 */       registry = RegistryBuilder.create().register("Basic", BasicSchemeFactory.INSTANCE).register("Digest", DigestSchemeFactory.INSTANCE).register("NTLM", NTLMSchemeFactory.INSTANCE).register("Negotiate", SPNegoSchemeFactory.DEFAULT).register("Kerberos", KerberosSchemeFactory.DEFAULT).build();
/*     */     }
/* 828 */     Lookup<CookieSpecFactory> cookieSpecRegistryCopy = this.cookieSpecRegistry;
/* 829 */     if (cookieSpecRegistryCopy == null) {
/* 830 */       cookieSpecRegistryCopy = CookieSpecSupport.createDefault();
/*     */     }
/*     */     
/* 833 */     CookieStore cookieStoreCopy = this.cookieStore;
/* 834 */     if (cookieStoreCopy == null) {
/* 835 */       basicCookieStore = new BasicCookieStore();
/*     */     }
/*     */     
/* 838 */     CredentialsProvider credentialsProviderCopy = this.credentialsProvider;
/* 839 */     if (credentialsProviderCopy == null) {
/* 840 */       if (this.systemProperties) {
/* 841 */         SystemDefaultCredentialsProvider systemDefaultCredentialsProvider = new SystemDefaultCredentialsProvider();
/*     */       } else {
/* 843 */         basicCredentialsProvider = new BasicCredentialsProvider();
/*     */       } 
/*     */     }
/*     */     
/* 847 */     TlsStrategy tlsStrategyCopy = this.tlsStrategy;
/* 848 */     if (tlsStrategyCopy == null) {
/* 849 */       if (this.systemProperties) {
/* 850 */         tlsStrategyCopy = DefaultClientTlsStrategy.getSystemDefault();
/*     */       } else {
/* 852 */         tlsStrategyCopy = DefaultClientTlsStrategy.getDefault();
/*     */       } 
/*     */     }
/*     */     
/* 856 */     MultihomeConnectionInitiator connectionInitiator = new MultihomeConnectionInitiator((ConnectionInitiator)ioReactor, this.dnsResolver);
/* 857 */     InternalH2ConnPool connPool = new InternalH2ConnPool((ConnectionInitiator)connectionInitiator, host -> null, tlsStrategyCopy);
/* 858 */     connPool.setConnectionConfigResolver(this.connectionConfigResolver);
/*     */     
/* 860 */     List<Closeable> closeablesCopy = (this.closeables != null) ? new ArrayList<>(this.closeables) : null;
/* 861 */     if (closeablesCopy == null) {
/* 862 */       closeablesCopy = new ArrayList<>(1);
/*     */     }
/* 864 */     if (this.evictIdleConnections) {
/*     */       
/* 866 */       IdleConnectionEvictor connectionEvictor = new IdleConnectionEvictor(connPool, (this.maxIdleTime != null) ? this.maxIdleTime : TimeValue.ofSeconds(30L));
/* 867 */       closeablesCopy.add(connectionEvictor::shutdown);
/* 868 */       connectionEvictor.start();
/*     */     } 
/* 870 */     closeablesCopy.add(connPool);
/*     */     
/* 872 */     return new InternalH2AsyncClient(ioReactor, execChain, pushConsumerRegistry, (this.threadFactory != null) ? this.threadFactory : (ThreadFactory)new DefaultThreadFactory("httpclient-main", true), connPool, (HttpRoutePlanner)defaultRoutePlanner, cookieSpecRegistryCopy, (Lookup<AuthSchemeFactory>)registry, (CookieStore)basicCookieStore, (CredentialsProvider)basicCredentialsProvider, this.defaultRequestConfig, closeablesCopy); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getProperty(String key, String defaultValue) {
/* 888 */     return AccessController.<String>doPrivileged(() -> System.getProperty(key, defaultValue));
/*     */   }
/*     */   
/*     */   static class IdleConnectionEvictor
/*     */     implements Closeable {
/*     */     private final Thread thread;
/*     */     
/*     */     public IdleConnectionEvictor(InternalH2ConnPool connPool, TimeValue maxIdleTime) {
/* 896 */       this.thread = (new DefaultThreadFactory("idle-connection-evictor", true)).newThread(() -> {
/*     */             try {
/*     */               while (!Thread.currentThread().isInterrupted()) {
/*     */                 maxIdleTime.sleep();
/*     */                 connPool.closeIdle(maxIdleTime);
/*     */               } 
/* 902 */             } catch (InterruptedException ex) {
/*     */               Thread.currentThread().interrupt();
/* 904 */             } catch (Exception exception) {}
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {
/* 911 */       this.thread.start();
/*     */     }
/*     */     
/*     */     public void shutdown() {
/* 915 */       this.thread.interrupt();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 920 */       shutdown();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/H2AsyncClientBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */