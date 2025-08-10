/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.SystemDefaultDnsResolver;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.impl.DefaultClientConnectionReuseStrategy;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
/*     */ import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
/*     */ import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
/*     */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.config.CharCodingConfig;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.protocol.DefaultHttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http.protocol.RequestUserAgent;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*     */ import org.apache.hc.core5.http2.config.H2Config;
/*     */ import org.apache.hc.core5.http2.protocol.H2RequestConnControl;
/*     */ import org.apache.hc.core5.http2.protocol.H2RequestContent;
/*     */ import org.apache.hc.core5.http2.protocol.H2RequestTargetHost;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HttpAsyncClients
/*     */ {
/*     */   public static HttpAsyncClientBuilder custom() {
/*  71 */     return HttpAsyncClientBuilder.create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CloseableHttpAsyncClient createDefault() {
/*  78 */     return HttpAsyncClientBuilder.create().build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CloseableHttpAsyncClient createSystem() {
/*  86 */     return HttpAsyncClientBuilder.create().useSystemProperties().build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static H2AsyncClientBuilder customHttp2() {
/*  95 */     return H2AsyncClientBuilder.create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CloseableHttpAsyncClient createHttp2Default() {
/* 103 */     return H2AsyncClientBuilder.create().build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CloseableHttpAsyncClient createHttp2System() {
/* 111 */     return H2AsyncClientBuilder.create().useSystemProperties().build();
/*     */   }
/*     */   
/*     */   private static HttpProcessor createMinimalProtocolProcessor() {
/* 115 */     return (HttpProcessor)new DefaultHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new H2RequestContent(), (HttpRequestInterceptor)new H2RequestTargetHost(), (HttpRequestInterceptor)new H2RequestConnControl(), (HttpRequestInterceptor)new RequestUserAgent(
/*     */ 
/*     */ 
/*     */             
/* 119 */             VersionInfo.getSoftwareInfo("Apache-HttpAsyncClient", "org.apache.hc.client5", HttpAsyncClients.class)) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MinimalHttpAsyncClient createMinimalHttpAsyncClientImpl(IOEventHandlerFactory eventHandlerFactory, AsyncPushConsumerRegistry pushConsumerRegistry, IOReactorConfig ioReactorConfig, AsyncClientConnectionManager connmgr, SchemePortResolver schemePortResolver, TlsConfig tlsConfig) {
/* 130 */     return new MinimalHttpAsyncClient(eventHandlerFactory, pushConsumerRegistry, ioReactorConfig, (ThreadFactory)new DefaultThreadFactory("httpclient-main", true), (ThreadFactory)new DefaultThreadFactory("httpclient-dispatch", true), connmgr, schemePortResolver, tlsConfig);
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
/*     */   @Deprecated
/*     */   public static MinimalHttpAsyncClient createMinimal(HttpVersionPolicy versionPolicy, H2Config h2Config, Http1Config h1Config, IOReactorConfig ioReactorConfig, AsyncClientConnectionManager connmgr) {
/* 155 */     AsyncPushConsumerRegistry pushConsumerRegistry = new AsyncPushConsumerRegistry();
/* 156 */     return createMinimalHttpAsyncClientImpl(new HttpAsyncClientProtocolNegotiationStarter(
/*     */           
/* 158 */           createMinimalProtocolProcessor(), (request, context) -> pushConsumerRegistry.get(request), h2Config, h1Config, CharCodingConfig.DEFAULT, (ConnectionReuseStrategy)DefaultClientConnectionReuseStrategy.INSTANCE), pushConsumerRegistry, ioReactorConfig, connmgr, (SchemePortResolver)DefaultSchemePortResolver.INSTANCE, (versionPolicy != null) ? 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 168 */         TlsConfig.custom().setVersionPolicy(versionPolicy).build() : null);
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
/*     */   public static MinimalHttpAsyncClient createMinimal(H2Config h2Config, Http1Config h1Config, IOReactorConfig ioReactorConfig, AsyncClientConnectionManager connmgr) {
/* 183 */     AsyncPushConsumerRegistry pushConsumerRegistry = new AsyncPushConsumerRegistry();
/* 184 */     return createMinimalHttpAsyncClientImpl(new HttpAsyncClientProtocolNegotiationStarter(
/*     */           
/* 186 */           createMinimalProtocolProcessor(), (request, context) -> pushConsumerRegistry.get(request), h2Config, h1Config, CharCodingConfig.DEFAULT, (ConnectionReuseStrategy)DefaultClientConnectionReuseStrategy.INSTANCE), pushConsumerRegistry, ioReactorConfig, connmgr, (SchemePortResolver)DefaultSchemePortResolver.INSTANCE, null);
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
/*     */   @Deprecated
/*     */   public static MinimalHttpAsyncClient createMinimal(HttpVersionPolicy versionPolicy, H2Config h2Config, Http1Config h1Config, IOReactorConfig ioReactorConfig) {
/* 212 */     return createMinimal(versionPolicy, h2Config, h1Config, ioReactorConfig, 
/* 213 */         (AsyncClientConnectionManager)PoolingAsyncClientConnectionManagerBuilder.create().build());
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
/*     */   public static MinimalHttpAsyncClient createMinimal(H2Config h2Config, Http1Config h1Config, IOReactorConfig ioReactorConfig) {
/* 225 */     return createMinimal(h2Config, h1Config, ioReactorConfig, 
/* 226 */         (AsyncClientConnectionManager)PoolingAsyncClientConnectionManagerBuilder.create().build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MinimalHttpAsyncClient createMinimal(H2Config h2Config, Http1Config h1Config) {
/* 235 */     return createMinimal(HttpVersionPolicy.NEGOTIATE, h2Config, h1Config, IOReactorConfig.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MinimalHttpAsyncClient createMinimal() {
/* 244 */     return createMinimal(H2Config.DEFAULT, Http1Config.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MinimalHttpAsyncClient createMinimal(AsyncClientConnectionManager connManager) {
/* 253 */     return createMinimal(HttpVersionPolicy.NEGOTIATE, H2Config.DEFAULT, Http1Config.DEFAULT, IOReactorConfig.DEFAULT, connManager);
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
/*     */   private static MinimalH2AsyncClient createMinimalHttp2AsyncClientImpl(IOEventHandlerFactory eventHandlerFactory, AsyncPushConsumerRegistry pushConsumerRegistry, IOReactorConfig ioReactorConfig, DnsResolver dnsResolver, TlsStrategy tlsStrategy) {
/* 267 */     return new MinimalH2AsyncClient(eventHandlerFactory, pushConsumerRegistry, ioReactorConfig, (ThreadFactory)new DefaultThreadFactory("httpclient-main", true), (ThreadFactory)new DefaultThreadFactory("httpclient-dispatch", true), dnsResolver, tlsStrategy);
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
/*     */   public static MinimalH2AsyncClient createHttp2Minimal(H2Config h2Config, IOReactorConfig ioReactorConfig, DnsResolver dnsResolver, TlsStrategy tlsStrategy) {
/* 286 */     AsyncPushConsumerRegistry pushConsumerRegistry = new AsyncPushConsumerRegistry();
/* 287 */     return createMinimalHttp2AsyncClientImpl(new H2AsyncClientProtocolStarter(
/*     */           
/* 289 */           createMinimalProtocolProcessor(), (request, context) -> pushConsumerRegistry.get(request), h2Config, CharCodingConfig.DEFAULT), pushConsumerRegistry, ioReactorConfig, dnsResolver, tlsStrategy);
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
/*     */   public static MinimalH2AsyncClient createHttp2Minimal(H2Config h2Config, IOReactorConfig ioReactorConfig, TlsStrategy tlsStrategy) {
/* 307 */     return createHttp2Minimal(h2Config, ioReactorConfig, (DnsResolver)SystemDefaultDnsResolver.INSTANCE, tlsStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MinimalH2AsyncClient createHttp2Minimal(H2Config h2Config, IOReactorConfig ioReactorConfig) {
/* 317 */     return createHttp2Minimal(h2Config, ioReactorConfig, DefaultClientTlsStrategy.getDefault());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MinimalH2AsyncClient createHttp2Minimal(H2Config h2Config) {
/* 325 */     return createHttp2Minimal(h2Config, IOReactorConfig.DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MinimalH2AsyncClient createHttp2Minimal() {
/* 333 */     return createHttp2Minimal(H2Config.DEFAULT);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/HttpAsyncClients.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */