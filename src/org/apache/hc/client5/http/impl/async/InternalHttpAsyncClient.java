/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.async.AsyncExecRuntime;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ @Internal
/*     */ public final class InternalHttpAsyncClient
/*     */   extends InternalAbstractHttpAsyncClient
/*     */ {
/*  71 */   private static final Logger LOG = LoggerFactory.getLogger(InternalHttpAsyncClient.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final AsyncClientConnectionManager manager;
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpRoutePlanner routePlanner;
/*     */ 
/*     */ 
/*     */   
/*     */   private final TlsConfig tlsConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InternalHttpAsyncClient(DefaultConnectingIOReactor ioReactor, AsyncExecChainElement execChain, AsyncPushConsumerRegistry pushConsumerRegistry, ThreadFactory threadFactory, AsyncClientConnectionManager manager, HttpRoutePlanner routePlanner, TlsConfig tlsConfig, Lookup<CookieSpecFactory> cookieSpecRegistry, Lookup<AuthSchemeFactory> authSchemeRegistry, CookieStore cookieStore, CredentialsProvider credentialsProvider, RequestConfig defaultConfig, List<Closeable> closeables) {
/*  90 */     super(ioReactor, pushConsumerRegistry, threadFactory, execChain, cookieSpecRegistry, authSchemeRegistry, cookieStore, credentialsProvider, defaultConfig, closeables);
/*     */     
/*  92 */     this.manager = manager;
/*  93 */     this.routePlanner = routePlanner;
/*  94 */     this.tlsConfig = tlsConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   AsyncExecRuntime createAsyncExecRuntime(HandlerFactory<AsyncPushConsumer> pushHandlerFactory) {
/*  99 */     return new InternalHttpAsyncExecRuntime(LOG, this.manager, getConnectionInitiator(), pushHandlerFactory, this.tlsConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   HttpRoute determineRoute(HttpHost httpHost, HttpClientContext clientContext) throws HttpException {
/* 104 */     return this.routePlanner.determineRoute(httpHost, (HttpContext)clientContext);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/InternalHttpAsyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */