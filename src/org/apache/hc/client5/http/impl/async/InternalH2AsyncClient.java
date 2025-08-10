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
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ @Internal
/*     */ public final class InternalH2AsyncClient
/*     */   extends InternalAbstractHttpAsyncClient
/*     */ {
/*  68 */   private static final Logger LOG = LoggerFactory.getLogger(InternalH2AsyncClient.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final HttpRoutePlanner routePlanner;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final InternalH2ConnPool connPool;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InternalH2AsyncClient(DefaultConnectingIOReactor ioReactor, AsyncExecChainElement execChain, AsyncPushConsumerRegistry pushConsumerRegistry, ThreadFactory threadFactory, InternalH2ConnPool connPool, HttpRoutePlanner routePlanner, Lookup<CookieSpecFactory> cookieSpecRegistry, Lookup<AuthSchemeFactory> authSchemeRegistry, CookieStore cookieStore, CredentialsProvider credentialsProvider, RequestConfig defaultConfig, List<Closeable> closeables) {
/*  85 */     super(ioReactor, pushConsumerRegistry, threadFactory, execChain, cookieSpecRegistry, authSchemeRegistry, cookieStore, credentialsProvider, defaultConfig, closeables);
/*     */     
/*  87 */     this.connPool = connPool;
/*  88 */     this.routePlanner = routePlanner;
/*     */   }
/*     */ 
/*     */   
/*     */   AsyncExecRuntime createAsyncExecRuntime(HandlerFactory<AsyncPushConsumer> pushHandlerFactory) {
/*  93 */     return new InternalH2AsyncExecRuntime(LOG, this.connPool, pushHandlerFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   HttpRoute determineRoute(HttpHost httpHost, HttpClientContext clientContext) throws HttpException {
/*  98 */     HttpRoute route = this.routePlanner.determineRoute(httpHost, (HttpContext)clientContext);
/*  99 */     if (route.isTunnelled()) {
/* 100 */       throw new HttpException("HTTP/2 tunneling not supported");
/*     */     }
/* 102 */     return route;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/InternalH2AsyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */