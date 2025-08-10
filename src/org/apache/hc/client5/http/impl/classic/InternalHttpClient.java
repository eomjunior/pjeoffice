/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import org.apache.hc.client5.http.ClientProtocolException;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.classic.ExecChain;
/*     */ import org.apache.hc.client5.http.classic.ExecRuntime;
/*     */ import org.apache.hc.client5.http.config.Configurable;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.client5.http.impl.ExecSupport;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*     */ import org.apache.hc.client5.http.routing.RoutingSupport;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
/*     */ import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
/*     */ import org.apache.hc.core5.http.protocol.BasicHttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ class InternalHttpClient
/*     */   extends CloseableHttpClient
/*     */   implements Configurable
/*     */ {
/*  82 */   private static final Logger LOG = LoggerFactory.getLogger(InternalHttpClient.class);
/*     */ 
/*     */   
/*     */   private final HttpClientConnectionManager connManager;
/*     */   
/*     */   private final HttpRequestExecutor requestExecutor;
/*     */   
/*     */   private final ExecChainElement execChain;
/*     */   
/*     */   private final HttpRoutePlanner routePlanner;
/*     */   
/*     */   private final Lookup<CookieSpecFactory> cookieSpecRegistry;
/*     */   
/*     */   private final Lookup<AuthSchemeFactory> authSchemeRegistry;
/*     */   
/*     */   private final CookieStore cookieStore;
/*     */   
/*     */   private final CredentialsProvider credentialsProvider;
/*     */   
/*     */   private final RequestConfig defaultConfig;
/*     */   
/*     */   private final ConcurrentLinkedQueue<Closeable> closeables;
/*     */ 
/*     */   
/*     */   public InternalHttpClient(HttpClientConnectionManager connManager, HttpRequestExecutor requestExecutor, ExecChainElement execChain, HttpRoutePlanner routePlanner, Lookup<CookieSpecFactory> cookieSpecRegistry, Lookup<AuthSchemeFactory> authSchemeRegistry, CookieStore cookieStore, CredentialsProvider credentialsProvider, RequestConfig defaultConfig, List<Closeable> closeables) {
/* 107 */     this.connManager = (HttpClientConnectionManager)Args.notNull(connManager, "Connection manager");
/* 108 */     this.requestExecutor = (HttpRequestExecutor)Args.notNull(requestExecutor, "Request executor");
/* 109 */     this.execChain = (ExecChainElement)Args.notNull(execChain, "Execution chain");
/* 110 */     this.routePlanner = (HttpRoutePlanner)Args.notNull(routePlanner, "Route planner");
/* 111 */     this.cookieSpecRegistry = cookieSpecRegistry;
/* 112 */     this.authSchemeRegistry = authSchemeRegistry;
/* 113 */     this.cookieStore = cookieStore;
/* 114 */     this.credentialsProvider = credentialsProvider;
/* 115 */     this.defaultConfig = defaultConfig;
/* 116 */     this.closeables = (closeables != null) ? new ConcurrentLinkedQueue<>(closeables) : null;
/*     */   }
/*     */   
/*     */   private HttpRoute determineRoute(HttpHost target, HttpContext context) throws HttpException {
/* 120 */     return this.routePlanner.determineRoute(target, context);
/*     */   }
/*     */   
/*     */   private void setupContext(HttpClientContext context) {
/* 124 */     if (context.getAttribute("http.authscheme-registry") == null) {
/* 125 */       context.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
/*     */     }
/* 127 */     if (context.getAttribute("http.cookiespec-registry") == null) {
/* 128 */       context.setAttribute("http.cookiespec-registry", this.cookieSpecRegistry);
/*     */     }
/* 130 */     if (context.getAttribute("http.cookie-store") == null) {
/* 131 */       context.setAttribute("http.cookie-store", this.cookieStore);
/*     */     }
/* 133 */     if (context.getAttribute("http.auth.credentials-provider") == null) {
/* 134 */       context.setAttribute("http.auth.credentials-provider", this.credentialsProvider);
/*     */     }
/* 136 */     if (context.getAttribute("http.request-config") == null) {
/* 137 */       context.setAttribute("http.request-config", this.defaultConfig);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CloseableHttpResponse doExecute(HttpHost target, ClassicHttpRequest request, HttpContext context) throws IOException {
/* 146 */     Args.notNull(request, "HTTP request");
/*     */     try {
/* 148 */       HttpClientContext localcontext = HttpClientContext.adapt((context != null) ? context : (HttpContext)new BasicHttpContext());
/*     */       
/* 150 */       RequestConfig config = null;
/* 151 */       if (request instanceof Configurable) {
/* 152 */         config = ((Configurable)request).getConfig();
/*     */       }
/* 154 */       if (config != null) {
/* 155 */         localcontext.setRequestConfig(config);
/*     */       }
/* 157 */       setupContext(localcontext);
/* 158 */       HttpRoute route = determineRoute((target != null) ? target : 
/* 159 */           RoutingSupport.determineHost((HttpRequest)request), (HttpContext)localcontext);
/*     */       
/* 161 */       String exchangeId = ExecSupport.getNextExchangeId();
/* 162 */       localcontext.setExchangeId(exchangeId);
/* 163 */       if (LOG.isDebugEnabled()) {
/* 164 */         LOG.debug("{} preparing request execution", exchangeId);
/*     */       }
/*     */       
/* 167 */       ExecRuntime execRuntime = new InternalExecRuntime(LOG, this.connManager, this.requestExecutor, (request instanceof CancellableDependency) ? (CancellableDependency)request : null);
/*     */       
/* 169 */       ExecChain.Scope scope = new ExecChain.Scope(exchangeId, route, request, execRuntime, localcontext);
/* 170 */       ClassicHttpResponse response = this.execChain.execute(ClassicRequestBuilder.copy(request).build(), scope);
/* 171 */       return CloseableHttpResponse.adapt(response);
/* 172 */     } catch (HttpException httpException) {
/* 173 */       throw new ClientProtocolException(httpException.getMessage(), httpException);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public RequestConfig getConfig() {
/* 179 */     return this.defaultConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 184 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 189 */     if (this.closeables != null) {
/*     */       Closeable closeable;
/* 191 */       while ((closeable = this.closeables.poll()) != null) {
/*     */         try {
/* 193 */           if (closeable instanceof ModalCloseable) {
/* 194 */             ((ModalCloseable)closeable).close(closeMode); continue;
/*     */           } 
/* 196 */           closeable.close();
/*     */         }
/* 198 */         catch (IOException ex) {
/* 199 */           LOG.error(ex.getMessage(), ex);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/InternalHttpClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */