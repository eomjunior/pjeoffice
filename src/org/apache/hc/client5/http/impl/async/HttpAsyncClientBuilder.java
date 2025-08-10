/*      */ package org.apache.hc.client5.http.impl.async;
/*      */ 
/*      */ import java.io.Closeable;
/*      */ import java.net.ProxySelector;
/*      */ import java.security.AccessController;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*      */ import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
/*      */ import org.apache.hc.client5.http.HttpRequestRetryStrategy;
/*      */ import org.apache.hc.client5.http.SchemePortResolver;
/*      */ import org.apache.hc.client5.http.UserTokenHandler;
/*      */ import org.apache.hc.client5.http.async.AsyncExecChainHandler;
/*      */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*      */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*      */ import org.apache.hc.client5.http.config.RequestConfig;
/*      */ import org.apache.hc.client5.http.config.TlsConfig;
/*      */ import org.apache.hc.client5.http.cookie.BasicCookieStore;
/*      */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*      */ import org.apache.hc.client5.http.cookie.CookieStore;
/*      */ import org.apache.hc.client5.http.impl.ChainElement;
/*      */ import org.apache.hc.client5.http.impl.CookieSpecSupport;
/*      */ import org.apache.hc.client5.http.impl.DefaultAuthenticationStrategy;
/*      */ import org.apache.hc.client5.http.impl.DefaultClientConnectionReuseStrategy;
/*      */ import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
/*      */ import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
/*      */ import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
/*      */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*      */ import org.apache.hc.client5.http.impl.DefaultUserTokenHandler;
/*      */ import org.apache.hc.client5.http.impl.IdleConnectionEvictor;
/*      */ import org.apache.hc.client5.http.impl.NoopUserTokenHandler;
/*      */ import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
/*      */ import org.apache.hc.client5.http.impl.auth.BasicSchemeFactory;
/*      */ import org.apache.hc.client5.http.impl.auth.DigestSchemeFactory;
/*      */ import org.apache.hc.client5.http.impl.auth.KerberosSchemeFactory;
/*      */ import org.apache.hc.client5.http.impl.auth.NTLMSchemeFactory;
/*      */ import org.apache.hc.client5.http.impl.auth.SPNegoSchemeFactory;
/*      */ import org.apache.hc.client5.http.impl.auth.SystemDefaultCredentialsProvider;
/*      */ import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManager;
/*      */ import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
/*      */ import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
/*      */ import org.apache.hc.client5.http.impl.routing.DefaultRoutePlanner;
/*      */ import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner;
/*      */ import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
/*      */ import org.apache.hc.client5.http.protocol.RedirectStrategy;
/*      */ import org.apache.hc.client5.http.protocol.RequestAddCookies;
/*      */ import org.apache.hc.client5.http.protocol.RequestDefaultHeaders;
/*      */ import org.apache.hc.client5.http.protocol.RequestExpectContinue;
/*      */ import org.apache.hc.client5.http.protocol.ResponseProcessCookies;
/*      */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*      */ import org.apache.hc.core5.annotation.Internal;
/*      */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*      */ import org.apache.hc.core5.function.Callback;
/*      */ import org.apache.hc.core5.function.Decorator;
/*      */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*      */ import org.apache.hc.core5.http.Header;
/*      */ import org.apache.hc.core5.http.HttpException;
/*      */ import org.apache.hc.core5.http.HttpHost;
/*      */ import org.apache.hc.core5.http.HttpRequest;
/*      */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*      */ import org.apache.hc.core5.http.HttpResponse;
/*      */ import org.apache.hc.core5.http.HttpResponseInterceptor;
/*      */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*      */ import org.apache.hc.core5.http.config.Http1Config;
/*      */ import org.apache.hc.core5.http.config.Lookup;
/*      */ import org.apache.hc.core5.http.config.NamedElementChain;
/*      */ import org.apache.hc.core5.http.config.Registry;
/*      */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*      */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*      */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*      */ import org.apache.hc.core5.http.protocol.DefaultHttpProcessor;
/*      */ import org.apache.hc.core5.http.protocol.HttpContext;
/*      */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*      */ import org.apache.hc.core5.http.protocol.HttpProcessorBuilder;
/*      */ import org.apache.hc.core5.http.protocol.RequestTargetHost;
/*      */ import org.apache.hc.core5.http.protocol.RequestUserAgent;
/*      */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*      */ import org.apache.hc.core5.http2.config.H2Config;
/*      */ import org.apache.hc.core5.http2.protocol.H2RequestConnControl;
/*      */ import org.apache.hc.core5.http2.protocol.H2RequestContent;
/*      */ import org.apache.hc.core5.http2.protocol.H2RequestTargetHost;
/*      */ import org.apache.hc.core5.io.CloseMode;
/*      */ import org.apache.hc.core5.pool.ConnPoolControl;
/*      */ import org.apache.hc.core5.reactor.Command;
/*      */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
/*      */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*      */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*      */ import org.apache.hc.core5.reactor.IOSession;
/*      */ import org.apache.hc.core5.reactor.IOSessionListener;
/*      */ import org.apache.hc.core5.util.Args;
/*      */ import org.apache.hc.core5.util.TimeValue;
/*      */ import org.apache.hc.core5.util.VersionInfo;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HttpAsyncClientBuilder
/*      */ {
/*      */   @Deprecated
/*      */   private TlsConfig tlsConfig;
/*      */   private AsyncClientConnectionManager connManager;
/*      */   private boolean connManagerShared;
/*      */   private IOReactorConfig ioReactorConfig;
/*      */   private IOSessionListener ioSessionListener;
/*      */   private Callback<Exception> ioReactorExceptionCallback;
/*      */   private Http1Config h1Config;
/*      */   private H2Config h2Config;
/*      */   private CharCodingConfig charCodingConfig;
/*      */   private SchemePortResolver schemePortResolver;
/*      */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*      */   private UserTokenHandler userTokenHandler;
/*      */   private AuthenticationStrategy targetAuthStrategy;
/*      */   private AuthenticationStrategy proxyAuthStrategy;
/*      */   private Decorator<IOSession> ioSessionDecorator;
/*      */   private LinkedList<RequestInterceptorEntry> requestInterceptors;
/*      */   private LinkedList<ResponseInterceptorEntry> responseInterceptors;
/*      */   private LinkedList<ExecInterceptorEntry> execInterceptors;
/*      */   private HttpRoutePlanner routePlanner;
/*      */   private RedirectStrategy redirectStrategy;
/*      */   private HttpRequestRetryStrategy retryStrategy;
/*      */   private ConnectionReuseStrategy reuseStrategy;
/*      */   private Lookup<AuthSchemeFactory> authSchemeRegistry;
/*      */   private Lookup<CookieSpecFactory> cookieSpecRegistry;
/*      */   private CookieStore cookieStore;
/*      */   private CredentialsProvider credentialsProvider;
/*      */   private String userAgent;
/*      */   private HttpHost proxy;
/*      */   private Collection<? extends Header> defaultHeaders;
/*      */   private RequestConfig defaultRequestConfig;
/*      */   private boolean evictExpiredConnections;
/*      */   private boolean evictIdleConnections;
/*      */   private TimeValue maxIdleTime;
/*      */   private boolean systemProperties;
/*      */   private boolean automaticRetriesDisabled;
/*      */   private boolean redirectHandlingDisabled;
/*      */   private boolean cookieManagementDisabled;
/*      */   private boolean authCachingDisabled;
/*      */   private boolean connectionStateDisabled;
/*      */   private ThreadFactory threadFactory;
/*      */   private List<Closeable> closeables;
/*      */   
/*      */   private static class RequestInterceptorEntry
/*      */   {
/*      */     final Position position;
/*      */     final HttpRequestInterceptor interceptor;
/*      */     
/*      */     enum Position
/*      */     {
/*  157 */       FIRST, LAST;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private RequestInterceptorEntry(Position position, HttpRequestInterceptor interceptor) {
/*  163 */       this.position = position;
/*  164 */       this.interceptor = interceptor;
/*      */     } }
/*      */   enum Position { FIRST, LAST; }
/*      */   private static class ResponseInterceptorEntry {
/*      */     final Position position; final HttpResponseInterceptor interceptor;
/*      */     
/*  170 */     enum Position { FIRST, LAST; }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ResponseInterceptorEntry(Position position, HttpResponseInterceptor interceptor) {
/*  176 */       this.position = position;
/*  177 */       this.interceptor = interceptor;
/*      */     }
/*      */   }
/*      */   enum Position { FIRST, LAST; }
/*      */   private static class ExecInterceptorEntry { final Position position; final String name; final AsyncExecChainHandler interceptor; final String existing;
/*      */     
/*  183 */     enum Position { BEFORE, AFTER, REPLACE, FIRST, LAST; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ExecInterceptorEntry(Position position, String name, AsyncExecChainHandler interceptor, String existing) {
/*  195 */       this.position = position;
/*  196 */       this.name = name;
/*  197 */       this.interceptor = interceptor;
/*  198 */       this.existing = existing;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   enum Position
/*      */   {
/*      */     BEFORE, AFTER, REPLACE, FIRST, LAST;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static HttpAsyncClientBuilder create() {
/*  258 */     return new HttpAsyncClientBuilder();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final HttpAsyncClientBuilder setVersionPolicy(HttpVersionPolicy versionPolicy) {
/*  272 */     this.tlsConfig = (versionPolicy != null) ? TlsConfig.custom().setVersionPolicy(versionPolicy).build() : null;
/*  273 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setHttp1Config(Http1Config h1Config) {
/*  280 */     this.h1Config = h1Config;
/*  281 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setH2Config(H2Config h2Config) {
/*  288 */     this.h2Config = h2Config;
/*  289 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setConnectionManager(AsyncClientConnectionManager connManager) {
/*  296 */     this.connManager = connManager;
/*  297 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setConnectionManagerShared(boolean shared) {
/*  312 */     this.connManagerShared = shared;
/*  313 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setIOReactorConfig(IOReactorConfig ioReactorConfig) {
/*  320 */     this.ioReactorConfig = ioReactorConfig;
/*  321 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setIOSessionListener(IOSessionListener ioSessionListener) {
/*  330 */     this.ioSessionListener = ioSessionListener;
/*  331 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setIoReactorExceptionCallback(Callback<Exception> ioReactorExceptionCallback) {
/*  340 */     this.ioReactorExceptionCallback = ioReactorExceptionCallback;
/*  341 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setCharCodingConfig(CharCodingConfig charCodingConfig) {
/*  348 */     this.charCodingConfig = charCodingConfig;
/*  349 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy reuseStrategy) {
/*  358 */     this.reuseStrategy = reuseStrategy;
/*  359 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy) {
/*  366 */     this.keepAliveStrategy = keepAliveStrategy;
/*  367 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setUserTokenHandler(UserTokenHandler userTokenHandler) {
/*  378 */     this.userTokenHandler = userTokenHandler;
/*  379 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy) {
/*  388 */     this.targetAuthStrategy = targetAuthStrategy;
/*  389 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy) {
/*  398 */     this.proxyAuthStrategy = proxyAuthStrategy;
/*  399 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setIoSessionDecorator(Decorator<IOSession> ioSessionDecorator) {
/*  408 */     this.ioSessionDecorator = ioSessionDecorator;
/*  409 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder addResponseInterceptorFirst(HttpResponseInterceptor interceptor) {
/*  416 */     Args.notNull(interceptor, "Interceptor");
/*  417 */     if (this.responseInterceptors == null) {
/*  418 */       this.responseInterceptors = new LinkedList<>();
/*      */     }
/*  420 */     this.responseInterceptors.add(new ResponseInterceptorEntry(ResponseInterceptorEntry.Position.FIRST, interceptor));
/*  421 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder addResponseInterceptorLast(HttpResponseInterceptor interceptor) {
/*  428 */     Args.notNull(interceptor, "Interceptor");
/*  429 */     if (this.responseInterceptors == null) {
/*  430 */       this.responseInterceptors = new LinkedList<>();
/*      */     }
/*  432 */     this.responseInterceptors.add(new ResponseInterceptorEntry(ResponseInterceptorEntry.Position.LAST, interceptor));
/*  433 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder addExecInterceptorBefore(String existing, String name, AsyncExecChainHandler interceptor) {
/*  440 */     Args.notBlank(existing, "Existing");
/*  441 */     Args.notBlank(name, "Name");
/*  442 */     Args.notNull(interceptor, "Interceptor");
/*  443 */     if (this.execInterceptors == null) {
/*  444 */       this.execInterceptors = new LinkedList<>();
/*      */     }
/*  446 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.BEFORE, name, interceptor, existing));
/*  447 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder addExecInterceptorAfter(String existing, String name, AsyncExecChainHandler interceptor) {
/*  454 */     Args.notBlank(existing, "Existing");
/*  455 */     Args.notBlank(name, "Name");
/*  456 */     Args.notNull(interceptor, "Interceptor");
/*  457 */     if (this.execInterceptors == null) {
/*  458 */       this.execInterceptors = new LinkedList<>();
/*      */     }
/*  460 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.AFTER, name, interceptor, existing));
/*  461 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder replaceExecInterceptor(String existing, AsyncExecChainHandler interceptor) {
/*  468 */     Args.notBlank(existing, "Existing");
/*  469 */     Args.notNull(interceptor, "Interceptor");
/*  470 */     if (this.execInterceptors == null) {
/*  471 */       this.execInterceptors = new LinkedList<>();
/*      */     }
/*  473 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.REPLACE, existing, interceptor, existing));
/*  474 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder addExecInterceptorFirst(String name, AsyncExecChainHandler interceptor) {
/*  481 */     Args.notNull(name, "Name");
/*  482 */     Args.notNull(interceptor, "Interceptor");
/*  483 */     if (this.execInterceptors == null) {
/*  484 */       this.execInterceptors = new LinkedList<>();
/*      */     }
/*  486 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.FIRST, name, interceptor, null));
/*  487 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder addExecInterceptorLast(String name, AsyncExecChainHandler interceptor) {
/*  494 */     Args.notNull(name, "Name");
/*  495 */     Args.notNull(interceptor, "Interceptor");
/*  496 */     if (this.execInterceptors == null) {
/*  497 */       this.execInterceptors = new LinkedList<>();
/*      */     }
/*  499 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.LAST, name, interceptor, null));
/*  500 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder addRequestInterceptorFirst(HttpRequestInterceptor interceptor) {
/*  507 */     Args.notNull(interceptor, "Interceptor");
/*  508 */     if (this.requestInterceptors == null) {
/*  509 */       this.requestInterceptors = new LinkedList<>();
/*      */     }
/*  511 */     this.requestInterceptors.add(new RequestInterceptorEntry(RequestInterceptorEntry.Position.FIRST, interceptor));
/*  512 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder addRequestInterceptorLast(HttpRequestInterceptor interceptor) {
/*  519 */     Args.notNull(interceptor, "Interceptor");
/*  520 */     if (this.requestInterceptors == null) {
/*  521 */       this.requestInterceptors = new LinkedList<>();
/*      */     }
/*  523 */     this.requestInterceptors.add(new RequestInterceptorEntry(RequestInterceptorEntry.Position.LAST, interceptor));
/*  524 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setRetryStrategy(HttpRequestRetryStrategy retryStrategy) {
/*  534 */     this.retryStrategy = retryStrategy;
/*  535 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpAsyncClientBuilder setRedirectStrategy(RedirectStrategy redirectStrategy) {
/*  546 */     this.redirectStrategy = redirectStrategy;
/*  547 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setSchemePortResolver(SchemePortResolver schemePortResolver) {
/*  554 */     this.schemePortResolver = schemePortResolver;
/*  555 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setThreadFactory(ThreadFactory threadFactory) {
/*  562 */     this.threadFactory = threadFactory;
/*  563 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setUserAgent(String userAgent) {
/*  570 */     this.userAgent = userAgent;
/*  571 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setDefaultHeaders(Collection<? extends Header> defaultHeaders) {
/*  578 */     this.defaultHeaders = defaultHeaders;
/*  579 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setProxy(HttpHost proxy) {
/*  589 */     this.proxy = proxy;
/*  590 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setRoutePlanner(HttpRoutePlanner routePlanner) {
/*  597 */     this.routePlanner = routePlanner;
/*  598 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setDefaultCredentialsProvider(CredentialsProvider credentialsProvider) {
/*  607 */     this.credentialsProvider = credentialsProvider;
/*  608 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setDefaultAuthSchemeRegistry(Lookup<AuthSchemeFactory> authSchemeRegistry) {
/*  617 */     this.authSchemeRegistry = authSchemeRegistry;
/*  618 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setDefaultCookieSpecRegistry(Lookup<CookieSpecFactory> cookieSpecRegistry) {
/*  627 */     this.cookieSpecRegistry = cookieSpecRegistry;
/*  628 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setDefaultCookieStore(CookieStore cookieStore) {
/*  636 */     this.cookieStore = cookieStore;
/*  637 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder setDefaultRequestConfig(RequestConfig config) {
/*  646 */     this.defaultRequestConfig = config;
/*  647 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder useSystemProperties() {
/*  655 */     this.systemProperties = true;
/*  656 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder disableConnectionState() {
/*  663 */     this.connectionStateDisabled = true;
/*  664 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder disableRedirectHandling() {
/*  671 */     this.redirectHandlingDisabled = true;
/*  672 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder disableAutomaticRetries() {
/*  679 */     this.automaticRetriesDisabled = true;
/*  680 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder disableCookieManagement() {
/*  687 */     this.cookieManagementDisabled = true;
/*  688 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder disableAuthCaching() {
/*  695 */     this.authCachingDisabled = true;
/*  696 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder evictExpiredConnections() {
/*  713 */     this.evictExpiredConnections = true;
/*  714 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final HttpAsyncClientBuilder evictIdleConnections(TimeValue maxIdleTime) {
/*  735 */     this.evictIdleConnections = true;
/*  736 */     this.maxIdleTime = maxIdleTime;
/*  737 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Internal
/*      */   protected void customizeExecChain(NamedElementChain<AsyncExecChainHandler> execChainDefinition) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Internal
/*      */   protected void addCloseable(Closeable closeable) {
/*  756 */     if (closeable == null) {
/*      */       return;
/*      */     }
/*  759 */     if (this.closeables == null) {
/*  760 */       this.closeables = new ArrayList<>();
/*      */     }
/*  762 */     this.closeables.add(closeable); } public CloseableHttpAsyncClient build() { PoolingAsyncClientConnectionManager poolingAsyncClientConnectionManager; DefaultConnectionKeepAliveStrategy defaultConnectionKeepAliveStrategy; NoopUserTokenHandler noopUserTokenHandler; DefaultAuthenticationStrategy defaultAuthenticationStrategy1, defaultAuthenticationStrategy2; DefaultRoutePlanner defaultRoutePlanner; DefaultClientConnectionReuseStrategy defaultClientConnectionReuseStrategy;
/*      */     Registry registry;
/*      */     BasicCookieStore basicCookieStore;
/*      */     BasicCredentialsProvider basicCredentialsProvider;
/*  766 */     AsyncClientConnectionManager connManagerCopy = this.connManager;
/*  767 */     if (connManagerCopy == null) {
/*  768 */       poolingAsyncClientConnectionManager = PoolingAsyncClientConnectionManagerBuilder.create().build();
/*      */     }
/*      */     
/*  771 */     ConnectionKeepAliveStrategy keepAliveStrategyCopy = this.keepAliveStrategy;
/*  772 */     if (keepAliveStrategyCopy == null) {
/*  773 */       defaultConnectionKeepAliveStrategy = DefaultConnectionKeepAliveStrategy.INSTANCE;
/*      */     }
/*      */     
/*  776 */     UserTokenHandler userTokenHandlerCopy = this.userTokenHandler;
/*  777 */     if (userTokenHandlerCopy == null) {
/*  778 */       if (!this.connectionStateDisabled) {
/*  779 */         DefaultUserTokenHandler defaultUserTokenHandler = DefaultUserTokenHandler.INSTANCE;
/*      */       } else {
/*  781 */         noopUserTokenHandler = NoopUserTokenHandler.INSTANCE;
/*      */       } 
/*      */     }
/*      */     
/*  785 */     AuthenticationStrategy targetAuthStrategyCopy = this.targetAuthStrategy;
/*  786 */     if (targetAuthStrategyCopy == null) {
/*  787 */       defaultAuthenticationStrategy1 = DefaultAuthenticationStrategy.INSTANCE;
/*      */     }
/*  789 */     AuthenticationStrategy proxyAuthStrategyCopy = this.proxyAuthStrategy;
/*  790 */     if (proxyAuthStrategyCopy == null) {
/*  791 */       defaultAuthenticationStrategy2 = DefaultAuthenticationStrategy.INSTANCE;
/*      */     }
/*      */     
/*  794 */     String userAgentCopy = this.userAgent;
/*  795 */     if (userAgentCopy == null) {
/*  796 */       if (this.systemProperties) {
/*  797 */         userAgentCopy = getProperty("http.agent", null);
/*      */       }
/*  799 */       if (userAgentCopy == null) {
/*  800 */         userAgentCopy = VersionInfo.getSoftwareInfo("Apache-HttpAsyncClient", "org.apache.hc.client5", 
/*  801 */             getClass());
/*      */       }
/*      */     } 
/*      */     
/*  805 */     HttpProcessorBuilder b = HttpProcessorBuilder.create();
/*  806 */     if (this.requestInterceptors != null) {
/*  807 */       for (RequestInterceptorEntry entry : this.requestInterceptors) {
/*  808 */         if (entry.position == RequestInterceptorEntry.Position.FIRST) {
/*  809 */           b.addFirst(entry.interceptor);
/*      */         }
/*      */       } 
/*      */     }
/*  813 */     if (this.responseInterceptors != null) {
/*  814 */       for (ResponseInterceptorEntry entry : this.responseInterceptors) {
/*  815 */         if (entry.position == ResponseInterceptorEntry.Position.FIRST) {
/*  816 */           b.addFirst(entry.interceptor);
/*      */         }
/*      */       } 
/*      */     }
/*  820 */     b.addAll(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestDefaultHeaders(this.defaultHeaders), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy), (HttpRequestInterceptor)new RequestExpectContinue(), (HttpRequestInterceptor)new H2RequestContent(), (HttpRequestInterceptor)new H2RequestTargetHost(), (HttpRequestInterceptor)new H2RequestConnControl() });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  827 */     if (!this.cookieManagementDisabled) {
/*  828 */       b.add((HttpRequestInterceptor)RequestAddCookies.INSTANCE);
/*      */     }
/*  830 */     if (!this.cookieManagementDisabled) {
/*  831 */       b.add((HttpResponseInterceptor)ResponseProcessCookies.INSTANCE);
/*      */     }
/*  833 */     if (this.requestInterceptors != null) {
/*  834 */       for (RequestInterceptorEntry entry : this.requestInterceptors) {
/*  835 */         if (entry.position == RequestInterceptorEntry.Position.LAST) {
/*  836 */           b.addLast(entry.interceptor);
/*      */         }
/*      */       } 
/*      */     }
/*  840 */     if (this.responseInterceptors != null) {
/*  841 */       for (ResponseInterceptorEntry entry : this.responseInterceptors) {
/*  842 */         if (entry.position == ResponseInterceptorEntry.Position.LAST) {
/*  843 */           b.addLast(entry.interceptor);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*  848 */     HttpProcessor httpProcessor = b.build();
/*      */     
/*  850 */     NamedElementChain<AsyncExecChainHandler> execChainDefinition = new NamedElementChain();
/*  851 */     execChainDefinition.addLast(new HttpAsyncMainClientExec(httpProcessor, (ConnectionKeepAliveStrategy)defaultConnectionKeepAliveStrategy, (UserTokenHandler)noopUserTokenHandler), ChainElement.MAIN_TRANSPORT
/*      */         
/*  853 */         .name());
/*      */     
/*  855 */     execChainDefinition.addFirst(new AsyncConnectExec((HttpProcessor)new DefaultHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy) }, ), (AuthenticationStrategy)defaultAuthenticationStrategy2, (this.schemePortResolver != null) ? this.schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE, this.authCachingDisabled), ChainElement.CONNECT
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  861 */         .name());
/*      */     
/*  863 */     execChainDefinition.addFirst(new AsyncProtocolExec((AuthenticationStrategy)defaultAuthenticationStrategy1, (AuthenticationStrategy)defaultAuthenticationStrategy2, (this.schemePortResolver != null) ? this.schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE, this.authCachingDisabled), ChainElement.PROTOCOL
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  869 */         .name());
/*      */ 
/*      */     
/*  872 */     if (!this.automaticRetriesDisabled) {
/*  873 */       DefaultHttpRequestRetryStrategy defaultHttpRequestRetryStrategy; HttpRequestRetryStrategy retryStrategyCopy = this.retryStrategy;
/*  874 */       if (retryStrategyCopy == null) {
/*  875 */         defaultHttpRequestRetryStrategy = DefaultHttpRequestRetryStrategy.INSTANCE;
/*      */       }
/*  877 */       execChainDefinition.addFirst(new AsyncHttpRequestRetryExec((HttpRequestRetryStrategy)defaultHttpRequestRetryStrategy), ChainElement.RETRY
/*      */           
/*  879 */           .name());
/*      */     } 
/*      */     
/*  882 */     HttpRoutePlanner routePlannerCopy = this.routePlanner;
/*  883 */     if (routePlannerCopy == null) {
/*  884 */       DefaultSchemePortResolver defaultSchemePortResolver; SchemePortResolver schemePortResolverCopy = this.schemePortResolver;
/*  885 */       if (schemePortResolverCopy == null) {
/*  886 */         defaultSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
/*      */       }
/*  888 */       if (this.proxy != null) {
/*  889 */         DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(this.proxy, (SchemePortResolver)defaultSchemePortResolver);
/*  890 */       } else if (this.systemProperties) {
/*  891 */         ProxySelector defaultProxySelector = AccessController.<ProxySelector>doPrivileged(ProxySelector::getDefault);
/*  892 */         SystemDefaultRoutePlanner systemDefaultRoutePlanner = new SystemDefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver, defaultProxySelector);
/*      */       } else {
/*      */         
/*  895 */         defaultRoutePlanner = new DefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  900 */     if (!this.redirectHandlingDisabled) {
/*  901 */       DefaultRedirectStrategy defaultRedirectStrategy; RedirectStrategy redirectStrategyCopy = this.redirectStrategy;
/*  902 */       if (redirectStrategyCopy == null) {
/*  903 */         defaultRedirectStrategy = DefaultRedirectStrategy.INSTANCE;
/*      */       }
/*  905 */       execChainDefinition.addFirst(new AsyncRedirectExec((HttpRoutePlanner)defaultRoutePlanner, (RedirectStrategy)defaultRedirectStrategy), ChainElement.REDIRECT
/*      */           
/*  907 */           .name());
/*      */     } 
/*      */     
/*  910 */     List<Closeable> closeablesCopy = (this.closeables != null) ? new ArrayList<>(this.closeables) : null;
/*  911 */     if (!this.connManagerShared) {
/*  912 */       if (closeablesCopy == null) {
/*  913 */         closeablesCopy = new ArrayList<>(1);
/*      */       }
/*  915 */       if ((this.evictExpiredConnections || this.evictIdleConnections) && 
/*  916 */         poolingAsyncClientConnectionManager instanceof ConnPoolControl) {
/*  917 */         IdleConnectionEvictor connectionEvictor = new IdleConnectionEvictor((ConnPoolControl)poolingAsyncClientConnectionManager, this.maxIdleTime, this.maxIdleTime);
/*      */         
/*  919 */         closeablesCopy.add(connectionEvictor::shutdown);
/*  920 */         connectionEvictor.start();
/*      */       } 
/*      */       
/*  923 */       closeablesCopy.add(poolingAsyncClientConnectionManager);
/*      */     } 
/*  925 */     ConnectionReuseStrategy reuseStrategyCopy = this.reuseStrategy;
/*  926 */     if (reuseStrategyCopy == null) {
/*  927 */       if (this.systemProperties) {
/*  928 */         String s = getProperty("http.keepAlive", "true");
/*  929 */         if ("true".equalsIgnoreCase(s)) {
/*  930 */           defaultClientConnectionReuseStrategy = DefaultClientConnectionReuseStrategy.INSTANCE;
/*      */         } else {
/*  932 */           reuseStrategyCopy = ((request, response, context) -> false);
/*      */         } 
/*      */       } else {
/*  935 */         defaultClientConnectionReuseStrategy = DefaultClientConnectionReuseStrategy.INSTANCE;
/*      */       } 
/*      */     }
/*  938 */     AsyncPushConsumerRegistry pushConsumerRegistry = new AsyncPushConsumerRegistry();
/*      */     
/*  940 */     IOEventHandlerFactory ioEventHandlerFactory = new HttpAsyncClientProtocolNegotiationStarter(HttpProcessorBuilder.create().build(), (request, context) -> pushConsumerRegistry.get(request), (this.h2Config != null) ? this.h2Config : H2Config.DEFAULT, (this.h1Config != null) ? this.h1Config : Http1Config.DEFAULT, (this.charCodingConfig != null) ? this.charCodingConfig : CharCodingConfig.DEFAULT, (ConnectionReuseStrategy)defaultClientConnectionReuseStrategy);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  946 */     DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioEventHandlerFactory, (this.ioReactorConfig != null) ? this.ioReactorConfig : IOReactorConfig.DEFAULT, (this.threadFactory != null) ? this.threadFactory : (ThreadFactory)new DefaultThreadFactory("httpclient-dispatch", true), (this.ioSessionDecorator != null) ? this.ioSessionDecorator : LoggingIOSessionDecorator.INSTANCE, (this.ioReactorExceptionCallback != null) ? this.ioReactorExceptionCallback : LoggingExceptionCallback.INSTANCE, this.ioSessionListener, ioSession -> ioSession.enqueue((Command)new ShutdownCommand(CloseMode.GRACEFUL), Command.Priority.IMMEDIATE));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  955 */     if (this.execInterceptors != null) {
/*  956 */       for (ExecInterceptorEntry entry : this.execInterceptors) {
/*  957 */         switch (entry.position) {
/*      */           case AFTER:
/*  959 */             execChainDefinition.addAfter(entry.existing, entry.interceptor, entry.name);
/*      */           
/*      */           case BEFORE:
/*  962 */             execChainDefinition.addBefore(entry.existing, entry.interceptor, entry.name);
/*      */           
/*      */           case REPLACE:
/*  965 */             execChainDefinition.replace(entry.existing, entry.interceptor);
/*      */           
/*      */           case FIRST:
/*  968 */             execChainDefinition.addFirst(entry.interceptor, entry.name);
/*      */ 
/*      */ 
/*      */           
/*      */           case LAST:
/*  973 */             execChainDefinition.addBefore(ChainElement.MAIN_TRANSPORT.name(), entry.interceptor, entry.name);
/*      */         } 
/*      */ 
/*      */       
/*      */       } 
/*      */     }
/*  979 */     customizeExecChain(execChainDefinition);
/*      */     
/*  981 */     NamedElementChain<AsyncExecChainHandler>.Node current = execChainDefinition.getLast();
/*  982 */     AsyncExecChainElement execChain = null;
/*  983 */     while (current != null) {
/*  984 */       execChain = new AsyncExecChainElement((AsyncExecChainHandler)current.getValue(), execChain);
/*  985 */       current = current.getPrevious();
/*      */     } 
/*      */     
/*  988 */     Lookup<AuthSchemeFactory> authSchemeRegistryCopy = this.authSchemeRegistry;
/*  989 */     if (authSchemeRegistryCopy == null)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  996 */       registry = RegistryBuilder.create().register("Basic", BasicSchemeFactory.INSTANCE).register("Digest", DigestSchemeFactory.INSTANCE).register("NTLM", NTLMSchemeFactory.INSTANCE).register("Negotiate", SPNegoSchemeFactory.DEFAULT).register("Kerberos", KerberosSchemeFactory.DEFAULT).build();
/*      */     }
/*  998 */     Lookup<CookieSpecFactory> cookieSpecRegistryCopy = this.cookieSpecRegistry;
/*  999 */     if (cookieSpecRegistryCopy == null) {
/* 1000 */       cookieSpecRegistryCopy = CookieSpecSupport.createDefault();
/*      */     }
/*      */     
/* 1003 */     CookieStore cookieStoreCopy = this.cookieStore;
/* 1004 */     if (cookieStoreCopy == null) {
/* 1005 */       basicCookieStore = new BasicCookieStore();
/*      */     }
/*      */     
/* 1008 */     CredentialsProvider credentialsProviderCopy = this.credentialsProvider;
/* 1009 */     if (credentialsProviderCopy == null) {
/* 1010 */       if (this.systemProperties) {
/* 1011 */         SystemDefaultCredentialsProvider systemDefaultCredentialsProvider = new SystemDefaultCredentialsProvider();
/*      */       } else {
/* 1013 */         basicCredentialsProvider = new BasicCredentialsProvider();
/*      */       } 
/*      */     }
/*      */     
/* 1017 */     return new InternalHttpAsyncClient(ioReactor, execChain, pushConsumerRegistry, (this.threadFactory != null) ? this.threadFactory : (ThreadFactory)new DefaultThreadFactory("httpclient-main", true), (AsyncClientConnectionManager)poolingAsyncClientConnectionManager, (HttpRoutePlanner)defaultRoutePlanner, this.tlsConfig, cookieSpecRegistryCopy, (Lookup<AuthSchemeFactory>)registry, (CookieStore)basicCookieStore, (CredentialsProvider)basicCredentialsProvider, this.defaultRequestConfig, closeablesCopy); }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getProperty(String key, String defaultValue) {
/* 1034 */     return AccessController.<String>doPrivileged(() -> System.getProperty(key, defaultValue));
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/HttpAsyncClientBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */