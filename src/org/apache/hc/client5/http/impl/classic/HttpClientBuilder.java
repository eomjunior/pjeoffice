/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.ProxySelector;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
/*     */ import org.apache.hc.client5.http.HttpRequestRetryStrategy;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.UserTokenHandler;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.classic.BackoffManager;
/*     */ import org.apache.hc.client5.http.classic.ConnectionBackoffStrategy;
/*     */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.cookie.BasicCookieStore;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.client5.http.entity.InputStreamFactory;
/*     */ import org.apache.hc.client5.http.impl.ChainElement;
/*     */ import org.apache.hc.client5.http.impl.CookieSpecSupport;
/*     */ import org.apache.hc.client5.http.impl.DefaultAuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultClientConnectionReuseStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.impl.DefaultUserTokenHandler;
/*     */ import org.apache.hc.client5.http.impl.IdleConnectionEvictor;
/*     */ import org.apache.hc.client5.http.impl.NoopUserTokenHandler;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.DigestSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.KerberosSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.NTLMSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.SPNegoSchemeFactory;
/*     */ import org.apache.hc.client5.http.impl.auth.SystemDefaultCredentialsProvider;
/*     */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
/*     */ import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
/*     */ import org.apache.hc.client5.http.impl.routing.DefaultRoutePlanner;
/*     */ import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.protocol.RedirectStrategy;
/*     */ import org.apache.hc.client5.http.protocol.RequestAddCookies;
/*     */ import org.apache.hc.client5.http.protocol.RequestClientConnControl;
/*     */ import org.apache.hc.client5.http.protocol.RequestDefaultHeaders;
/*     */ import org.apache.hc.client5.http.protocol.RequestExpectContinue;
/*     */ import org.apache.hc.client5.http.protocol.ResponseProcessCookies;
/*     */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpResponseInterceptor;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.config.NamedElementChain;
/*     */ import org.apache.hc.core5.http.config.Registry;
/*     */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*     */ import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
/*     */ import org.apache.hc.core5.http.protocol.DefaultHttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessorBuilder;
/*     */ import org.apache.hc.core5.http.protocol.RequestContent;
/*     */ import org.apache.hc.core5.http.protocol.RequestTargetHost;
/*     */ import org.apache.hc.core5.http.protocol.RequestUserAgent;
/*     */ import org.apache.hc.core5.pool.ConnPoolControl;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ import org.apache.hc.core5.util.VersionInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpClientBuilder
/*     */ {
/*     */   private HttpRequestExecutor requestExec;
/*     */   private HttpClientConnectionManager connManager;
/*     */   private boolean connManagerShared;
/*     */   private SchemePortResolver schemePortResolver;
/*     */   private ConnectionReuseStrategy reuseStrategy;
/*     */   private ConnectionKeepAliveStrategy keepAliveStrategy;
/*     */   private AuthenticationStrategy targetAuthStrategy;
/*     */   private AuthenticationStrategy proxyAuthStrategy;
/*     */   private UserTokenHandler userTokenHandler;
/*     */   private LinkedList<RequestInterceptorEntry> requestInterceptors;
/*     */   private LinkedList<ResponseInterceptorEntry> responseInterceptors;
/*     */   private LinkedList<ExecInterceptorEntry> execInterceptors;
/*     */   private HttpRequestRetryStrategy retryStrategy;
/*     */   private HttpRoutePlanner routePlanner;
/*     */   private RedirectStrategy redirectStrategy;
/*     */   private ConnectionBackoffStrategy connectionBackoffStrategy;
/*     */   private BackoffManager backoffManager;
/*     */   private Lookup<AuthSchemeFactory> authSchemeRegistry;
/*     */   private Lookup<CookieSpecFactory> cookieSpecRegistry;
/*     */   private LinkedHashMap<String, InputStreamFactory> contentDecoderMap;
/*     */   private CookieStore cookieStore;
/*     */   private CredentialsProvider credentialsProvider;
/*     */   private String userAgent;
/*     */   private HttpHost proxy;
/*     */   private Collection<? extends Header> defaultHeaders;
/*     */   private RequestConfig defaultRequestConfig;
/*     */   private boolean evictExpiredConnections;
/*     */   private boolean evictIdleConnections;
/*     */   private TimeValue maxIdleTime;
/*     */   private boolean systemProperties;
/*     */   private boolean redirectHandlingDisabled;
/*     */   private boolean automaticRetriesDisabled;
/*     */   private boolean contentCompressionDisabled;
/*     */   private boolean cookieManagementDisabled;
/*     */   private boolean authCachingDisabled;
/*     */   private boolean connectionStateDisabled;
/*     */   private boolean defaultUserAgentDisabled;
/*     */   private List<Closeable> closeables;
/*     */   
/*     */   private static class RequestInterceptorEntry
/*     */   {
/*     */     final Position position;
/*     */     final HttpRequestInterceptor interceptor;
/*     */     
/*     */     enum Position
/*     */     {
/* 142 */       FIRST, LAST;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private RequestInterceptorEntry(Position position, HttpRequestInterceptor interceptor) {
/* 148 */       this.position = position;
/* 149 */       this.interceptor = interceptor;
/*     */     } }
/*     */   enum Position { FIRST, LAST; }
/*     */   private static class ResponseInterceptorEntry {
/*     */     final Position position; final HttpResponseInterceptor interceptor;
/*     */     
/* 155 */     enum Position { FIRST, LAST; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ResponseInterceptorEntry(Position position, HttpResponseInterceptor interceptor) {
/* 161 */       this.position = position;
/* 162 */       this.interceptor = interceptor;
/*     */     }
/*     */   }
/*     */   enum Position { FIRST, LAST; }
/*     */   private static class ExecInterceptorEntry { final Position position; final String name; final ExecChainHandler interceptor; final String existing;
/*     */     
/* 168 */     enum Position { BEFORE, AFTER, REPLACE, FIRST, LAST; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ExecInterceptorEntry(Position position, String name, ExecChainHandler interceptor, String existing) {
/* 180 */       this.position = position;
/* 181 */       this.name = name;
/* 182 */       this.interceptor = interceptor;
/* 183 */       this.existing = existing;
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
/*     */   public static HttpClientBuilder create() {
/* 232 */     return new HttpClientBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setRequestExecutor(HttpRequestExecutor requestExec) {
/* 243 */     this.requestExec = requestExec;
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setConnectionManager(HttpClientConnectionManager connManager) {
/* 252 */     this.connManager = connManager;
/* 253 */     return this;
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
/*     */   public final HttpClientBuilder setConnectionManagerShared(boolean shared) {
/* 272 */     this.connManagerShared = shared;
/* 273 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy reuseStrategy) {
/* 281 */     this.reuseStrategy = reuseStrategy;
/* 282 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy keepAliveStrategy) {
/* 290 */     this.keepAliveStrategy = keepAliveStrategy;
/* 291 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy targetAuthStrategy) {
/* 300 */     this.targetAuthStrategy = targetAuthStrategy;
/* 301 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy proxyAuthStrategy) {
/* 310 */     this.proxyAuthStrategy = proxyAuthStrategy;
/* 311 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setUserTokenHandler(UserTokenHandler userTokenHandler) {
/* 322 */     this.userTokenHandler = userTokenHandler;
/* 323 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder disableConnectionState() {
/* 330 */     this.connectionStateDisabled = true;
/* 331 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setSchemePortResolver(SchemePortResolver schemePortResolver) {
/* 339 */     this.schemePortResolver = schemePortResolver;
/* 340 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setUserAgent(String userAgent) {
/* 347 */     this.userAgent = userAgent;
/* 348 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setDefaultHeaders(Collection<? extends Header> defaultHeaders) {
/* 355 */     this.defaultHeaders = defaultHeaders;
/* 356 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder addResponseInterceptorFirst(HttpResponseInterceptor interceptor) {
/* 363 */     Args.notNull(interceptor, "Interceptor");
/* 364 */     if (this.responseInterceptors == null) {
/* 365 */       this.responseInterceptors = new LinkedList<>();
/*     */     }
/* 367 */     this.responseInterceptors.add(new ResponseInterceptorEntry(ResponseInterceptorEntry.Position.FIRST, interceptor));
/* 368 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder addResponseInterceptorLast(HttpResponseInterceptor interceptor) {
/* 375 */     Args.notNull(interceptor, "Interceptor");
/* 376 */     if (this.responseInterceptors == null) {
/* 377 */       this.responseInterceptors = new LinkedList<>();
/*     */     }
/* 379 */     this.responseInterceptors.add(new ResponseInterceptorEntry(ResponseInterceptorEntry.Position.LAST, interceptor));
/* 380 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder addRequestInterceptorFirst(HttpRequestInterceptor interceptor) {
/* 387 */     Args.notNull(interceptor, "Interceptor");
/* 388 */     if (this.requestInterceptors == null) {
/* 389 */       this.requestInterceptors = new LinkedList<>();
/*     */     }
/* 391 */     this.requestInterceptors.add(new RequestInterceptorEntry(RequestInterceptorEntry.Position.FIRST, interceptor));
/* 392 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder addRequestInterceptorLast(HttpRequestInterceptor interceptor) {
/* 399 */     Args.notNull(interceptor, "Interceptor");
/* 400 */     if (this.requestInterceptors == null) {
/* 401 */       this.requestInterceptors = new LinkedList<>();
/*     */     }
/* 403 */     this.requestInterceptors.add(new RequestInterceptorEntry(RequestInterceptorEntry.Position.LAST, interceptor));
/* 404 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder addExecInterceptorBefore(String existing, String name, ExecChainHandler interceptor) {
/* 411 */     Args.notBlank(existing, "Existing");
/* 412 */     Args.notBlank(name, "Name");
/* 413 */     Args.notNull(interceptor, "Interceptor");
/* 414 */     if (this.execInterceptors == null) {
/* 415 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 417 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.BEFORE, name, interceptor, existing));
/* 418 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder addExecInterceptorAfter(String existing, String name, ExecChainHandler interceptor) {
/* 425 */     Args.notBlank(existing, "Existing");
/* 426 */     Args.notBlank(name, "Name");
/* 427 */     Args.notNull(interceptor, "Interceptor");
/* 428 */     if (this.execInterceptors == null) {
/* 429 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 431 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.AFTER, name, interceptor, existing));
/* 432 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder replaceExecInterceptor(String existing, ExecChainHandler interceptor) {
/* 439 */     Args.notBlank(existing, "Existing");
/* 440 */     Args.notNull(interceptor, "Interceptor");
/* 441 */     if (this.execInterceptors == null) {
/* 442 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 444 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.REPLACE, existing, interceptor, existing));
/* 445 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder addExecInterceptorFirst(String name, ExecChainHandler interceptor) {
/* 452 */     Args.notNull(name, "Name");
/* 453 */     Args.notNull(interceptor, "Interceptor");
/* 454 */     if (this.execInterceptors == null) {
/* 455 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 457 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.FIRST, name, interceptor, null));
/* 458 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder addExecInterceptorLast(String name, ExecChainHandler interceptor) {
/* 465 */     Args.notNull(name, "Name");
/* 466 */     Args.notNull(interceptor, "Interceptor");
/* 467 */     if (this.execInterceptors == null) {
/* 468 */       this.execInterceptors = new LinkedList<>();
/*     */     }
/* 470 */     this.execInterceptors.add(new ExecInterceptorEntry(ExecInterceptorEntry.Position.LAST, name, interceptor, null));
/* 471 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder disableCookieManagement() {
/* 478 */     this.cookieManagementDisabled = true;
/* 479 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder disableContentCompression() {
/* 486 */     this.contentCompressionDisabled = true;
/* 487 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder disableAuthCaching() {
/* 494 */     this.authCachingDisabled = true;
/* 495 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setRetryStrategy(HttpRequestRetryStrategy retryStrategy) {
/* 505 */     this.retryStrategy = retryStrategy;
/* 506 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder disableAutomaticRetries() {
/* 513 */     this.automaticRetriesDisabled = true;
/* 514 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setProxy(HttpHost proxy) {
/* 524 */     this.proxy = proxy;
/* 525 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setRoutePlanner(HttpRoutePlanner routePlanner) {
/* 532 */     this.routePlanner = routePlanner;
/* 533 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setRedirectStrategy(RedirectStrategy redirectStrategy) {
/* 544 */     this.redirectStrategy = redirectStrategy;
/* 545 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder disableRedirectHandling() {
/* 552 */     this.redirectHandlingDisabled = true;
/* 553 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setConnectionBackoffStrategy(ConnectionBackoffStrategy connectionBackoffStrategy) {
/* 561 */     this.connectionBackoffStrategy = connectionBackoffStrategy;
/* 562 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setBackoffManager(BackoffManager backoffManager) {
/* 569 */     this.backoffManager = backoffManager;
/* 570 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setDefaultCookieStore(CookieStore cookieStore) {
/* 578 */     this.cookieStore = cookieStore;
/* 579 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setDefaultCredentialsProvider(CredentialsProvider credentialsProvider) {
/* 589 */     this.credentialsProvider = credentialsProvider;
/* 590 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setDefaultAuthSchemeRegistry(Lookup<AuthSchemeFactory> authSchemeRegistry) {
/* 600 */     this.authSchemeRegistry = authSchemeRegistry;
/* 601 */     return this;
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
/*     */   public final HttpClientBuilder setDefaultCookieSpecRegistry(Lookup<CookieSpecFactory> cookieSpecRegistry) {
/* 614 */     this.cookieSpecRegistry = cookieSpecRegistry;
/* 615 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setContentDecoderRegistry(LinkedHashMap<String, InputStreamFactory> contentDecoderMap) {
/* 625 */     this.contentDecoderMap = contentDecoderMap;
/* 626 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder setDefaultRequestConfig(RequestConfig config) {
/* 635 */     this.defaultRequestConfig = config;
/* 636 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder useSystemProperties() {
/* 644 */     this.systemProperties = true;
/* 645 */     return this;
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
/*     */   public final HttpClientBuilder evictExpiredConnections() {
/* 664 */     this.evictExpiredConnections = true;
/* 665 */     return this;
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
/*     */   public final HttpClientBuilder evictIdleConnections(TimeValue maxIdleTime) {
/* 688 */     this.evictIdleConnections = true;
/* 689 */     this.maxIdleTime = maxIdleTime;
/* 690 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final HttpClientBuilder disableDefaultUserAgent() {
/* 699 */     this.defaultUserAgentDisabled = true;
/* 700 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Internal
/*     */   protected void customizeExecChain(NamedElementChain<ExecChainHandler> execChainDefinition) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Internal
/*     */   protected void addCloseable(Closeable closeable) {
/* 719 */     if (closeable == null) {
/*     */       return;
/*     */     }
/* 722 */     if (this.closeables == null) {
/* 723 */       this.closeables = new ArrayList<>();
/*     */     }
/* 725 */     this.closeables.add(closeable); } public CloseableHttpClient build() { PoolingHttpClientConnectionManager poolingHttpClientConnectionManager; DefaultClientConnectionReuseStrategy defaultClientConnectionReuseStrategy; DefaultConnectionKeepAliveStrategy defaultConnectionKeepAliveStrategy; DefaultAuthenticationStrategy defaultAuthenticationStrategy1, defaultAuthenticationStrategy2;
/*     */     NoopUserTokenHandler noopUserTokenHandler;
/*     */     DefaultRoutePlanner defaultRoutePlanner;
/*     */     Registry registry;
/*     */     BasicCookieStore basicCookieStore;
/*     */     BasicCredentialsProvider basicCredentialsProvider;
/* 731 */     HttpRequestExecutor requestExecCopy = this.requestExec;
/* 732 */     if (requestExecCopy == null) {
/* 733 */       requestExecCopy = new HttpRequestExecutor();
/*     */     }
/* 735 */     HttpClientConnectionManager connManagerCopy = this.connManager;
/* 736 */     if (connManagerCopy == null) {
/* 737 */       poolingHttpClientConnectionManager = PoolingHttpClientConnectionManagerBuilder.create().build();
/*     */     }
/* 739 */     ConnectionReuseStrategy reuseStrategyCopy = this.reuseStrategy;
/* 740 */     if (reuseStrategyCopy == null) {
/* 741 */       if (this.systemProperties) {
/* 742 */         String s = System.getProperty("http.keepAlive", "true");
/* 743 */         if ("true".equalsIgnoreCase(s)) {
/* 744 */           defaultClientConnectionReuseStrategy = DefaultClientConnectionReuseStrategy.INSTANCE;
/*     */         } else {
/* 746 */           reuseStrategyCopy = ((request, response, context) -> false);
/*     */         } 
/*     */       } else {
/* 749 */         defaultClientConnectionReuseStrategy = DefaultClientConnectionReuseStrategy.INSTANCE;
/*     */       } 
/*     */     }
/*     */     
/* 753 */     ConnectionKeepAliveStrategy keepAliveStrategyCopy = this.keepAliveStrategy;
/* 754 */     if (keepAliveStrategyCopy == null) {
/* 755 */       defaultConnectionKeepAliveStrategy = DefaultConnectionKeepAliveStrategy.INSTANCE;
/*     */     }
/* 757 */     AuthenticationStrategy targetAuthStrategyCopy = this.targetAuthStrategy;
/* 758 */     if (targetAuthStrategyCopy == null) {
/* 759 */       defaultAuthenticationStrategy1 = DefaultAuthenticationStrategy.INSTANCE;
/*     */     }
/* 761 */     AuthenticationStrategy proxyAuthStrategyCopy = this.proxyAuthStrategy;
/* 762 */     if (proxyAuthStrategyCopy == null) {
/* 763 */       defaultAuthenticationStrategy2 = DefaultAuthenticationStrategy.INSTANCE;
/*     */     }
/* 765 */     UserTokenHandler userTokenHandlerCopy = this.userTokenHandler;
/* 766 */     if (userTokenHandlerCopy == null) {
/* 767 */       if (!this.connectionStateDisabled) {
/* 768 */         DefaultUserTokenHandler defaultUserTokenHandler = DefaultUserTokenHandler.INSTANCE;
/*     */       } else {
/* 770 */         noopUserTokenHandler = NoopUserTokenHandler.INSTANCE;
/*     */       } 
/*     */     }
/*     */     
/* 774 */     String userAgentCopy = this.userAgent;
/* 775 */     if (userAgentCopy == null) {
/* 776 */       if (this.systemProperties) {
/* 777 */         userAgentCopy = System.getProperty("http.agent");
/*     */       }
/* 779 */       if (userAgentCopy == null && !this.defaultUserAgentDisabled) {
/* 780 */         userAgentCopy = VersionInfo.getSoftwareInfo("Apache-HttpClient", "org.apache.hc.client5", 
/* 781 */             getClass());
/*     */       }
/*     */     } 
/*     */     
/* 785 */     HttpProcessorBuilder b = HttpProcessorBuilder.create();
/* 786 */     if (this.requestInterceptors != null) {
/* 787 */       for (RequestInterceptorEntry entry : this.requestInterceptors) {
/* 788 */         if (entry.position == RequestInterceptorEntry.Position.FIRST) {
/* 789 */           b.addFirst(entry.interceptor);
/*     */         }
/*     */       } 
/*     */     }
/* 793 */     if (this.responseInterceptors != null) {
/* 794 */       for (ResponseInterceptorEntry entry : this.responseInterceptors) {
/* 795 */         if (entry.position == ResponseInterceptorEntry.Position.FIRST) {
/* 796 */           b.addFirst(entry.interceptor);
/*     */         }
/*     */       } 
/*     */     }
/* 800 */     b.addAll(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestDefaultHeaders(this.defaultHeaders), (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy), (HttpRequestInterceptor)new RequestExpectContinue() });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 807 */     if (!this.cookieManagementDisabled) {
/* 808 */       b.add((HttpRequestInterceptor)RequestAddCookies.INSTANCE);
/*     */     }
/* 810 */     if (!this.cookieManagementDisabled) {
/* 811 */       b.add((HttpResponseInterceptor)ResponseProcessCookies.INSTANCE);
/*     */     }
/* 813 */     if (this.requestInterceptors != null) {
/* 814 */       for (RequestInterceptorEntry entry : this.requestInterceptors) {
/* 815 */         if (entry.position == RequestInterceptorEntry.Position.LAST) {
/* 816 */           b.addLast(entry.interceptor);
/*     */         }
/*     */       } 
/*     */     }
/* 820 */     if (this.responseInterceptors != null) {
/* 821 */       for (ResponseInterceptorEntry entry : this.responseInterceptors) {
/* 822 */         if (entry.position == ResponseInterceptorEntry.Position.LAST) {
/* 823 */           b.addLast(entry.interceptor);
/*     */         }
/*     */       } 
/*     */     }
/* 827 */     HttpProcessor httpProcessor = b.build();
/*     */     
/* 829 */     NamedElementChain<ExecChainHandler> execChainDefinition = new NamedElementChain();
/* 830 */     execChainDefinition.addLast(new MainClientExec((HttpClientConnectionManager)poolingHttpClientConnectionManager, httpProcessor, (ConnectionReuseStrategy)defaultClientConnectionReuseStrategy, (ConnectionKeepAliveStrategy)defaultConnectionKeepAliveStrategy, (UserTokenHandler)noopUserTokenHandler), ChainElement.MAIN_TRANSPORT
/*     */         
/* 832 */         .name());
/* 833 */     execChainDefinition.addFirst(new ConnectExec((ConnectionReuseStrategy)defaultClientConnectionReuseStrategy, (HttpProcessor)new DefaultHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestUserAgent(userAgentCopy) }, ), (AuthenticationStrategy)defaultAuthenticationStrategy2, (this.schemePortResolver != null) ? this.schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE, this.authCachingDisabled), ChainElement.CONNECT
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 840 */         .name());
/*     */     
/* 842 */     execChainDefinition.addFirst(new ProtocolExec((AuthenticationStrategy)defaultAuthenticationStrategy1, (AuthenticationStrategy)defaultAuthenticationStrategy2, (this.schemePortResolver != null) ? this.schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE, this.authCachingDisabled), ChainElement.PROTOCOL
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 848 */         .name());
/*     */ 
/*     */     
/* 851 */     if (!this.automaticRetriesDisabled) {
/* 852 */       DefaultHttpRequestRetryStrategy defaultHttpRequestRetryStrategy; HttpRequestRetryStrategy retryStrategyCopy = this.retryStrategy;
/* 853 */       if (retryStrategyCopy == null) {
/* 854 */         defaultHttpRequestRetryStrategy = DefaultHttpRequestRetryStrategy.INSTANCE;
/*     */       }
/* 856 */       execChainDefinition.addFirst(new HttpRequestRetryExec((HttpRequestRetryStrategy)defaultHttpRequestRetryStrategy), ChainElement.RETRY
/*     */           
/* 858 */           .name());
/*     */     } 
/*     */     
/* 861 */     HttpRoutePlanner routePlannerCopy = this.routePlanner;
/* 862 */     if (routePlannerCopy == null) {
/* 863 */       DefaultSchemePortResolver defaultSchemePortResolver; SchemePortResolver schemePortResolverCopy = this.schemePortResolver;
/* 864 */       if (schemePortResolverCopy == null) {
/* 865 */         defaultSchemePortResolver = DefaultSchemePortResolver.INSTANCE;
/*     */       }
/* 867 */       if (this.proxy != null) {
/* 868 */         DefaultProxyRoutePlanner defaultProxyRoutePlanner = new DefaultProxyRoutePlanner(this.proxy, (SchemePortResolver)defaultSchemePortResolver);
/* 869 */       } else if (this.systemProperties) {
/*     */         
/* 871 */         SystemDefaultRoutePlanner systemDefaultRoutePlanner = new SystemDefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver, ProxySelector.getDefault());
/*     */       } else {
/* 873 */         defaultRoutePlanner = new DefaultRoutePlanner((SchemePortResolver)defaultSchemePortResolver);
/*     */       } 
/*     */     } 
/*     */     
/* 877 */     if (!this.contentCompressionDisabled) {
/* 878 */       if (this.contentDecoderMap != null) {
/* 879 */         List<String> encodings = new ArrayList<>(this.contentDecoderMap.keySet());
/* 880 */         RegistryBuilder<InputStreamFactory> b2 = RegistryBuilder.create();
/* 881 */         for (Map.Entry<String, InputStreamFactory> entry : this.contentDecoderMap.entrySet()) {
/* 882 */           b2.register(entry.getKey(), entry.getValue());
/*     */         }
/* 884 */         registry = b2.build();
/* 885 */         execChainDefinition.addFirst(new ContentCompressionExec(encodings, (Lookup<InputStreamFactory>)registry, true), ChainElement.COMPRESS
/*     */             
/* 887 */             .name());
/*     */       } else {
/* 889 */         execChainDefinition.addFirst(new ContentCompressionExec(true), ChainElement.COMPRESS.name());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 894 */     if (!this.redirectHandlingDisabled) {
/* 895 */       DefaultRedirectStrategy defaultRedirectStrategy; RedirectStrategy redirectStrategyCopy = this.redirectStrategy;
/* 896 */       if (redirectStrategyCopy == null) {
/* 897 */         defaultRedirectStrategy = DefaultRedirectStrategy.INSTANCE;
/*     */       }
/* 899 */       execChainDefinition.addFirst(new RedirectExec((HttpRoutePlanner)defaultRoutePlanner, (RedirectStrategy)defaultRedirectStrategy), ChainElement.REDIRECT
/*     */           
/* 901 */           .name());
/*     */     } 
/*     */ 
/*     */     
/* 905 */     if (this.backoffManager != null && this.connectionBackoffStrategy != null) {
/* 906 */       execChainDefinition.addFirst(new BackoffStrategyExec(this.connectionBackoffStrategy, this.backoffManager), ChainElement.BACK_OFF
/* 907 */           .name());
/*     */     }
/*     */     
/* 910 */     if (this.execInterceptors != null) {
/* 911 */       for (ExecInterceptorEntry entry : this.execInterceptors) {
/* 912 */         switch (entry.position) {
/*     */           case AFTER:
/* 914 */             execChainDefinition.addAfter(entry.existing, entry.interceptor, entry.name);
/*     */           
/*     */           case BEFORE:
/* 917 */             execChainDefinition.addBefore(entry.existing, entry.interceptor, entry.name);
/*     */           
/*     */           case REPLACE:
/* 920 */             execChainDefinition.replace(entry.existing, entry.interceptor);
/*     */           
/*     */           case FIRST:
/* 923 */             execChainDefinition.addFirst(entry.interceptor, entry.name);
/*     */ 
/*     */ 
/*     */           
/*     */           case LAST:
/* 928 */             execChainDefinition.addBefore(ChainElement.MAIN_TRANSPORT.name(), entry.interceptor, entry.name);
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/*     */     }
/* 934 */     customizeExecChain(execChainDefinition);
/*     */     
/* 936 */     NamedElementChain<ExecChainHandler>.Node current = execChainDefinition.getLast();
/* 937 */     ExecChainElement execChain = null;
/* 938 */     while (current != null) {
/* 939 */       execChain = new ExecChainElement((ExecChainHandler)current.getValue(), execChain);
/* 940 */       current = current.getPrevious();
/*     */     } 
/*     */     
/* 943 */     Lookup<AuthSchemeFactory> authSchemeRegistryCopy = this.authSchemeRegistry;
/* 944 */     if (authSchemeRegistryCopy == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 951 */       registry = RegistryBuilder.create().register("Basic", BasicSchemeFactory.INSTANCE).register("Digest", DigestSchemeFactory.INSTANCE).register("NTLM", NTLMSchemeFactory.INSTANCE).register("Negotiate", SPNegoSchemeFactory.DEFAULT).register("Kerberos", KerberosSchemeFactory.DEFAULT).build();
/*     */     }
/* 953 */     Lookup<CookieSpecFactory> cookieSpecRegistryCopy = this.cookieSpecRegistry;
/* 954 */     if (cookieSpecRegistryCopy == null) {
/* 955 */       cookieSpecRegistryCopy = CookieSpecSupport.createDefault();
/*     */     }
/*     */     
/* 958 */     CookieStore defaultCookieStore = this.cookieStore;
/* 959 */     if (defaultCookieStore == null) {
/* 960 */       basicCookieStore = new BasicCookieStore();
/*     */     }
/*     */     
/* 963 */     CredentialsProvider defaultCredentialsProvider = this.credentialsProvider;
/* 964 */     if (defaultCredentialsProvider == null) {
/* 965 */       if (this.systemProperties) {
/* 966 */         SystemDefaultCredentialsProvider systemDefaultCredentialsProvider = new SystemDefaultCredentialsProvider();
/*     */       } else {
/* 968 */         basicCredentialsProvider = new BasicCredentialsProvider();
/*     */       } 
/*     */     }
/*     */     
/* 972 */     List<Closeable> closeablesCopy = (this.closeables != null) ? new ArrayList<>(this.closeables) : null;
/* 973 */     if (!this.connManagerShared) {
/* 974 */       if (closeablesCopy == null) {
/* 975 */         closeablesCopy = new ArrayList<>(1);
/*     */       }
/* 977 */       if ((this.evictExpiredConnections || this.evictIdleConnections) && 
/* 978 */         poolingHttpClientConnectionManager instanceof ConnPoolControl) {
/* 979 */         IdleConnectionEvictor connectionEvictor = new IdleConnectionEvictor((ConnPoolControl)poolingHttpClientConnectionManager, this.maxIdleTime, this.maxIdleTime);
/*     */         
/* 981 */         closeablesCopy.add(() -> {
/*     */               connectionEvictor.shutdown();
/*     */               try {
/*     */                 connectionEvictor.awaitTermination(Timeout.ofSeconds(1L));
/* 985 */               } catch (InterruptedException interrupted) {
/*     */                 Thread.currentThread().interrupt();
/*     */               } 
/*     */             });
/* 989 */         connectionEvictor.start();
/*     */       } 
/*     */       
/* 992 */       closeablesCopy.add(poolingHttpClientConnectionManager);
/*     */     } 
/*     */     
/* 995 */     return new InternalHttpClient((HttpClientConnectionManager)poolingHttpClientConnectionManager, requestExecCopy, execChain, (HttpRoutePlanner)defaultRoutePlanner, cookieSpecRegistryCopy, (Lookup<AuthSchemeFactory>)registry, (CookieStore)basicCookieStore, (CredentialsProvider)basicCredentialsProvider, (this.defaultRequestConfig != null) ? this.defaultRequestConfig : RequestConfig.DEFAULT, closeablesCopy); }
/*     */ 
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/HttpClientBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */