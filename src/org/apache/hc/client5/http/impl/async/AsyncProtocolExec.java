/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.async.AsyncExecCallback;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChain;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChainHandler;
/*     */ import org.apache.hc.client5.http.async.AsyncExecRuntime;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.ChallengeType;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.impl.RequestSupport;
/*     */ import org.apache.hc.client5.http.impl.auth.AuthCacheKeeper;
/*     */ import org.apache.hc.client5.http.impl.auth.HttpAuthenticator;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.message.BasicHttpRequest;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.support.BasicRequestBuilder;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.net.URIAuthority;
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
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class AsyncProtocolExec
/*     */   implements AsyncExecChainHandler
/*     */ {
/*  83 */   private static final Logger LOG = LoggerFactory.getLogger(AsyncProtocolExec.class);
/*     */   
/*     */   private final AuthenticationStrategy targetAuthStrategy;
/*     */   
/*     */   private final AuthenticationStrategy proxyAuthStrategy;
/*     */   
/*     */   private final HttpAuthenticator authenticator;
/*     */   
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   
/*     */   private final AuthCacheKeeper authCacheKeeper;
/*     */   
/*     */   AsyncProtocolExec(AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, SchemePortResolver schemePortResolver, boolean authCachingDisabled) {
/*  96 */     this.targetAuthStrategy = (AuthenticationStrategy)Args.notNull(targetAuthStrategy, "Target authentication strategy");
/*  97 */     this.proxyAuthStrategy = (AuthenticationStrategy)Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
/*  98 */     this.authenticator = new HttpAuthenticator();
/*  99 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/* 100 */     this.authCacheKeeper = authCachingDisabled ? null : new AuthCacheKeeper(this.schemePortResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(HttpRequest userRequest, AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecChain chain, AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/*     */     HttpRequest request;
/* 111 */     if (Method.CONNECT.isSame(userRequest.getMethod())) {
/* 112 */       throw new ProtocolException("Direct execution of CONNECT is not allowed");
/*     */     }
/*     */     
/* 115 */     HttpRoute route = scope.route;
/* 116 */     HttpHost routeTarget = route.getTargetHost();
/* 117 */     HttpHost proxy = route.getProxyHost();
/* 118 */     HttpClientContext clientContext = scope.clientContext;
/*     */ 
/*     */     
/* 121 */     if (proxy != null && !route.isTunnelled()) {
/* 122 */       BasicRequestBuilder requestBuilder = BasicRequestBuilder.copy(userRequest);
/* 123 */       if (requestBuilder.getAuthority() == null) {
/* 124 */         requestBuilder.setAuthority(new URIAuthority((NamedEndpoint)routeTarget));
/*     */       }
/* 126 */       requestBuilder.setAbsoluteRequestUri(true);
/* 127 */       BasicHttpRequest basicHttpRequest = requestBuilder.build();
/*     */     } else {
/* 129 */       request = userRequest;
/*     */     } 
/*     */ 
/*     */     
/* 133 */     if (request.getScheme() == null) {
/* 134 */       request.setScheme(routeTarget.getSchemeName());
/*     */     }
/* 136 */     if (request.getAuthority() == null) {
/* 137 */       request.setAuthority(new URIAuthority((NamedEndpoint)routeTarget));
/*     */     }
/*     */     
/* 140 */     URIAuthority authority = request.getAuthority();
/* 141 */     if (authority.getUserInfo() != null) {
/* 142 */       throw new ProtocolException("Request URI authority contains deprecated userinfo component");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 148 */     HttpHost target = new HttpHost(request.getScheme(), authority.getHostName(), this.schemePortResolver.resolve(request.getScheme(), (NamedEndpoint)authority));
/* 149 */     String pathPrefix = RequestSupport.extractPathPrefix(request);
/* 150 */     AuthExchange targetAuthExchange = clientContext.getAuthExchange(target);
/* 151 */     AuthExchange proxyAuthExchange = (proxy != null) ? clientContext.getAuthExchange(proxy) : new AuthExchange();
/*     */     
/* 153 */     if (!targetAuthExchange.isConnectionBased() && targetAuthExchange
/* 154 */       .getPathPrefix() != null && 
/* 155 */       !pathPrefix.startsWith(targetAuthExchange.getPathPrefix()))
/*     */     {
/*     */       
/* 158 */       targetAuthExchange.reset();
/*     */     }
/* 160 */     if (targetAuthExchange.getPathPrefix() == null) {
/* 161 */       targetAuthExchange.setPathPrefix(pathPrefix);
/*     */     }
/*     */     
/* 164 */     if (this.authCacheKeeper != null) {
/* 165 */       this.authCacheKeeper.loadPreemptively(target, pathPrefix, targetAuthExchange, (HttpContext)clientContext);
/* 166 */       if (proxy != null) {
/* 167 */         this.authCacheKeeper.loadPreemptively(proxy, null, proxyAuthExchange, (HttpContext)clientContext);
/*     */       }
/*     */     } 
/*     */     
/* 171 */     AtomicBoolean challenged = new AtomicBoolean(false);
/* 172 */     internalExecute(target, pathPrefix, targetAuthExchange, proxyAuthExchange, challenged, request, entityProducer, scope, chain, asyncExecCallback);
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
/*     */   private void internalExecute(final HttpHost target, final String pathPrefix, final AuthExchange targetAuthExchange, final AuthExchange proxyAuthExchange, final AtomicBoolean challenged, final HttpRequest request, final AsyncEntityProducer entityProducer, final AsyncExecChain.Scope scope, final AsyncExecChain chain, final AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 187 */     final String exchangeId = scope.exchangeId;
/* 188 */     HttpRoute route = scope.route;
/* 189 */     final HttpClientContext clientContext = scope.clientContext;
/* 190 */     final AsyncExecRuntime execRuntime = scope.execRuntime;
/*     */     
/* 192 */     final HttpHost proxy = route.getProxyHost();
/*     */     
/* 194 */     if (!request.containsHeader("Authorization")) {
/* 195 */       if (LOG.isDebugEnabled()) {
/* 196 */         LOG.debug("{} target auth state: {}", exchangeId, targetAuthExchange.getState());
/*     */       }
/* 198 */       this.authenticator.addAuthResponse(target, ChallengeType.TARGET, request, targetAuthExchange, (HttpContext)clientContext);
/*     */     } 
/* 200 */     if (!request.containsHeader("Proxy-Authorization") && !route.isTunnelled()) {
/* 201 */       if (LOG.isDebugEnabled()) {
/* 202 */         LOG.debug("{} proxy auth state: {}", exchangeId, proxyAuthExchange.getState());
/*     */       }
/* 204 */       this.authenticator.addAuthResponse(proxy, ChallengeType.PROXY, request, proxyAuthExchange, (HttpContext)clientContext);
/*     */     } 
/*     */     
/* 207 */     chain.proceed(request, entityProducer, scope, new AsyncExecCallback()
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public AsyncDataConsumer handleResponse(HttpResponse response, EntityDetails entityDetails) throws HttpException, IOException
/*     */           {
/* 214 */             if (Method.TRACE.isSame(request.getMethod()))
/*     */             {
/* 216 */               return asyncExecCallback.handleResponse(response, entityDetails);
/*     */             }
/* 218 */             if (AsyncProtocolExec.this.needAuthentication(targetAuthExchange, proxyAuthExchange, (proxy != null) ? proxy : target, target, pathPrefix, response, clientContext)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 226 */               challenged.set(true);
/* 227 */               return null;
/*     */             } 
/* 229 */             challenged.set(false);
/* 230 */             return asyncExecCallback.handleResponse(response, entityDetails);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void handleInformationResponse(HttpResponse response) throws HttpException, IOException {
/* 236 */             asyncExecCallback.handleInformationResponse(response);
/*     */           }
/*     */ 
/*     */           
/*     */           public void completed() {
/* 241 */             if (!execRuntime.isEndpointConnected()) {
/* 242 */               if (proxyAuthExchange.getState() == AuthExchange.State.SUCCESS && proxyAuthExchange
/* 243 */                 .isConnectionBased()) {
/* 244 */                 if (AsyncProtocolExec.LOG.isDebugEnabled()) {
/* 245 */                   AsyncProtocolExec.LOG.debug("{} resetting proxy auth state", exchangeId);
/*     */                 }
/* 247 */                 proxyAuthExchange.reset();
/*     */               } 
/* 249 */               if (targetAuthExchange.getState() == AuthExchange.State.SUCCESS && targetAuthExchange
/* 250 */                 .isConnectionBased()) {
/* 251 */                 if (AsyncProtocolExec.LOG.isDebugEnabled()) {
/* 252 */                   AsyncProtocolExec.LOG.debug("{} resetting target auth state", exchangeId);
/*     */                 }
/* 254 */                 targetAuthExchange.reset();
/*     */               } 
/*     */             } 
/*     */             
/* 258 */             if (challenged.get()) {
/* 259 */               if (entityProducer != null && !entityProducer.isRepeatable()) {
/* 260 */                 if (AsyncProtocolExec.LOG.isDebugEnabled()) {
/* 261 */                   AsyncProtocolExec.LOG.debug("{} cannot retry non-repeatable request", exchangeId);
/*     */                 }
/* 263 */                 asyncExecCallback.completed();
/*     */               } else {
/*     */                 
/* 266 */                 HttpRequest original = scope.originalRequest;
/* 267 */                 request.setHeaders(new Header[0]);
/* 268 */                 for (Iterator<Header> it = original.headerIterator(); it.hasNext();) {
/* 269 */                   request.addHeader(it.next());
/*     */                 }
/*     */                 try {
/* 272 */                   if (entityProducer != null) {
/* 273 */                     entityProducer.releaseResources();
/*     */                   }
/* 275 */                   AsyncProtocolExec.this.internalExecute(target, pathPrefix, targetAuthExchange, proxyAuthExchange, challenged, request, entityProducer, scope, chain, asyncExecCallback);
/*     */                 }
/* 277 */                 catch (HttpException|IOException ex) {
/* 278 */                   asyncExecCallback.failed(ex);
/*     */                 } 
/*     */               } 
/*     */             } else {
/* 282 */               asyncExecCallback.completed();
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception cause) {
/* 288 */             if (cause instanceof IOException || cause instanceof RuntimeException) {
/* 289 */               for (AuthExchange authExchange : clientContext.getAuthExchanges().values()) {
/* 290 */                 if (authExchange.isConnectionBased()) {
/* 291 */                   authExchange.reset();
/*     */                 }
/*     */               } 
/*     */             }
/* 295 */             asyncExecCallback.failed(cause);
/*     */           }
/*     */         });
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
/*     */   private boolean needAuthentication(AuthExchange targetAuthExchange, AuthExchange proxyAuthExchange, HttpHost proxy, HttpHost target, String pathPrefix, HttpResponse response, HttpClientContext context) {
/* 309 */     RequestConfig config = context.getRequestConfig();
/* 310 */     if (config.isAuthenticationEnabled()) {
/* 311 */       boolean targetAuthRequested = this.authenticator.isChallenged(target, ChallengeType.TARGET, response, targetAuthExchange, (HttpContext)context);
/*     */ 
/*     */       
/* 314 */       if (this.authCacheKeeper != null) {
/* 315 */         if (targetAuthRequested) {
/* 316 */           this.authCacheKeeper.updateOnChallenge(target, pathPrefix, targetAuthExchange, (HttpContext)context);
/*     */         } else {
/* 318 */           this.authCacheKeeper.updateOnNoChallenge(target, pathPrefix, targetAuthExchange, (HttpContext)context);
/*     */         } 
/*     */       }
/*     */       
/* 322 */       boolean proxyAuthRequested = this.authenticator.isChallenged(proxy, ChallengeType.PROXY, response, proxyAuthExchange, (HttpContext)context);
/*     */ 
/*     */       
/* 325 */       if (this.authCacheKeeper != null) {
/* 326 */         if (proxyAuthRequested) {
/* 327 */           this.authCacheKeeper.updateOnChallenge(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         } else {
/* 329 */           this.authCacheKeeper.updateOnNoChallenge(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         } 
/*     */       }
/*     */       
/* 333 */       if (targetAuthRequested) {
/* 334 */         boolean updated = this.authenticator.updateAuthState(target, ChallengeType.TARGET, response, this.targetAuthStrategy, targetAuthExchange, (HttpContext)context);
/*     */ 
/*     */         
/* 337 */         if (this.authCacheKeeper != null) {
/* 338 */           this.authCacheKeeper.updateOnResponse(target, pathPrefix, targetAuthExchange, (HttpContext)context);
/*     */         }
/*     */         
/* 341 */         return updated;
/*     */       } 
/* 343 */       if (proxyAuthRequested) {
/* 344 */         boolean updated = this.authenticator.updateAuthState(proxy, ChallengeType.PROXY, response, this.proxyAuthStrategy, proxyAuthExchange, (HttpContext)context);
/*     */ 
/*     */         
/* 347 */         if (this.authCacheKeeper != null) {
/* 348 */           this.authCacheKeeper.updateOnResponse(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         }
/*     */         
/* 351 */         return updated;
/*     */       } 
/*     */     } 
/* 354 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/AsyncProtocolExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */