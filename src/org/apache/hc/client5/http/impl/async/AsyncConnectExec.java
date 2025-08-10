/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.RouteInfo;
/*     */ import org.apache.hc.client5.http.RouteTracker;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.async.AsyncExecCallback;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChain;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChainHandler;
/*     */ import org.apache.hc.client5.http.async.AsyncExecRuntime;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.ChallengeType;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.TunnelRefusedException;
/*     */ import org.apache.hc.client5.http.impl.auth.AuthCacheKeeper;
/*     */ import org.apache.hc.client5.http.impl.auth.HttpAuthenticator;
/*     */ import org.apache.hc.client5.http.impl.routing.BasicRouteDirector;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.routing.HttpRouteDirector;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHttpRequest;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class AsyncConnectExec
/*     */   implements AsyncExecChainHandler
/*     */ {
/*  84 */   private static final Logger LOG = LoggerFactory.getLogger(AsyncConnectExec.class);
/*     */   
/*     */   private final HttpProcessor proxyHttpProcessor;
/*     */   
/*     */   private final AuthenticationStrategy proxyAuthStrategy;
/*     */   
/*     */   private final HttpAuthenticator authenticator;
/*     */   
/*     */   private final AuthCacheKeeper authCacheKeeper;
/*     */   
/*     */   private final HttpRouteDirector routeDirector;
/*     */   
/*     */   public AsyncConnectExec(HttpProcessor proxyHttpProcessor, AuthenticationStrategy proxyAuthStrategy, SchemePortResolver schemePortResolver, boolean authCachingDisabled) {
/*  97 */     Args.notNull(proxyHttpProcessor, "Proxy HTTP processor");
/*  98 */     Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
/*  99 */     this.proxyHttpProcessor = proxyHttpProcessor;
/* 100 */     this.proxyAuthStrategy = proxyAuthStrategy;
/* 101 */     this.authenticator = new HttpAuthenticator();
/* 102 */     this.authCacheKeeper = authCachingDisabled ? null : new AuthCacheKeeper(schemePortResolver);
/* 103 */     this.routeDirector = (HttpRouteDirector)BasicRouteDirector.INSTANCE;
/*     */   }
/*     */   
/*     */   static class State
/*     */   {
/*     */     State(HttpRoute route) {
/* 109 */       this.tracker = new RouteTracker(route);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     final RouteTracker tracker;
/*     */ 
/*     */     
/*     */     volatile boolean challenged;
/*     */ 
/*     */     
/*     */     volatile boolean tunnelRefused;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(final HttpRequest request, final AsyncEntityProducer entityProducer, final AsyncExecChain.Scope scope, final AsyncExecChain chain, final AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 126 */     Args.notNull(request, "HTTP request");
/* 127 */     Args.notNull(scope, "Scope");
/*     */     
/* 129 */     String exchangeId = scope.exchangeId;
/* 130 */     HttpRoute route = scope.route;
/* 131 */     CancellableDependency cancellableDependency = scope.cancellableDependency;
/* 132 */     HttpClientContext clientContext = scope.clientContext;
/* 133 */     AsyncExecRuntime execRuntime = scope.execRuntime;
/* 134 */     final State state = new State(route);
/*     */     
/* 136 */     if (!execRuntime.isEndpointAcquired()) {
/* 137 */       Object userToken = clientContext.getUserToken();
/* 138 */       if (LOG.isDebugEnabled()) {
/* 139 */         LOG.debug("{} acquiring connection with route {}", exchangeId, route);
/*     */       }
/* 141 */       cancellableDependency.setDependency(execRuntime.acquireEndpoint(exchangeId, route, userToken, clientContext, new FutureCallback<AsyncExecRuntime>()
/*     */             {
/*     */               
/*     */               public void completed(AsyncExecRuntime execRuntime)
/*     */               {
/* 146 */                 if (execRuntime.isEndpointConnected()) {
/*     */                   try {
/* 148 */                     chain.proceed(request, entityProducer, scope, asyncExecCallback);
/* 149 */                   } catch (HttpException|IOException ex) {
/* 150 */                     asyncExecCallback.failed(ex);
/*     */                   } 
/*     */                 } else {
/* 153 */                   AsyncConnectExec.this.proceedToNextHop(state, request, entityProducer, scope, chain, asyncExecCallback);
/*     */                 } 
/*     */               }
/*     */ 
/*     */               
/*     */               public void failed(Exception ex) {
/* 159 */                 asyncExecCallback.failed(ex);
/*     */               }
/*     */ 
/*     */               
/*     */               public void cancelled() {
/* 164 */                 asyncExecCallback.failed(new InterruptedIOException());
/*     */               }
/*     */             }));
/*     */     
/*     */     }
/* 169 */     else if (execRuntime.isEndpointConnected()) {
/*     */       try {
/* 171 */         chain.proceed(request, entityProducer, scope, asyncExecCallback);
/* 172 */       } catch (HttpException|IOException ex) {
/* 173 */         asyncExecCallback.failed(ex);
/*     */       } 
/*     */     } else {
/* 176 */       proceedToNextHop(state, request, entityProducer, scope, chain, asyncExecCallback);
/*     */     } 
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
/*     */   private void proceedToNextHop(final State state, final HttpRequest request, final AsyncEntityProducer entityProducer, final AsyncExecChain.Scope scope, final AsyncExecChain chain, final AsyncExecCallback asyncExecCallback) {
/* 189 */     final RouteTracker tracker = state.tracker;
/* 190 */     final String exchangeId = scope.exchangeId;
/* 191 */     final HttpRoute route = scope.route;
/* 192 */     final AsyncExecRuntime execRuntime = scope.execRuntime;
/* 193 */     CancellableDependency operation = scope.cancellableDependency;
/* 194 */     HttpClientContext clientContext = scope.clientContext;
/*     */     
/* 196 */     HttpRoute fact = tracker.toRoute();
/* 197 */     int step = this.routeDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
/*     */     
/* 199 */     switch (step) {
/*     */       case 1:
/* 201 */         operation.setDependency(execRuntime.connectEndpoint(clientContext, new FutureCallback<AsyncExecRuntime>()
/*     */               {
/*     */                 public void completed(AsyncExecRuntime execRuntime)
/*     */                 {
/* 205 */                   tracker.connectTarget(route.isSecure());
/* 206 */                   if (AsyncConnectExec.LOG.isDebugEnabled()) {
/* 207 */                     AsyncConnectExec.LOG.debug("{} connected to target", exchangeId);
/*     */                   }
/* 209 */                   AsyncConnectExec.this.proceedToNextHop(state, request, entityProducer, scope, chain, asyncExecCallback);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void failed(Exception ex) {
/* 214 */                   asyncExecCallback.failed(ex);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void cancelled() {
/* 219 */                   asyncExecCallback.failed(new InterruptedIOException());
/*     */                 }
/*     */               }));
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/* 226 */         operation.setDependency(execRuntime.connectEndpoint(clientContext, new FutureCallback<AsyncExecRuntime>()
/*     */               {
/*     */                 public void completed(AsyncExecRuntime execRuntime)
/*     */                 {
/* 230 */                   HttpHost proxy = route.getProxyHost();
/* 231 */                   tracker.connectProxy(proxy, (route.isSecure() && !route.isTunnelled()));
/* 232 */                   if (AsyncConnectExec.LOG.isDebugEnabled()) {
/* 233 */                     AsyncConnectExec.LOG.debug("{} connected to proxy", exchangeId);
/*     */                   }
/* 235 */                   AsyncConnectExec.this.proceedToNextHop(state, request, entityProducer, scope, chain, asyncExecCallback);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void failed(Exception ex) {
/* 240 */                   asyncExecCallback.failed(ex);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void cancelled() {
/* 245 */                   asyncExecCallback.failed(new InterruptedIOException());
/*     */                 }
/*     */               }));
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/*     */         try {
/* 253 */           HttpHost proxy = route.getProxyHost();
/* 254 */           HttpHost target = route.getTargetHost();
/* 255 */           if (LOG.isDebugEnabled()) {
/* 256 */             LOG.debug("{} create tunnel", exchangeId);
/*     */           }
/* 258 */           createTunnel(state, proxy, target, scope, chain, new AsyncExecCallback()
/*     */               {
/*     */ 
/*     */                 
/*     */                 public AsyncDataConsumer handleResponse(HttpResponse response, EntityDetails entityDetails) throws HttpException, IOException
/*     */                 {
/* 264 */                   return asyncExecCallback.handleResponse(response, entityDetails);
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void handleInformationResponse(HttpResponse response) throws HttpException, IOException {
/* 270 */                   asyncExecCallback.handleInformationResponse(response);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void completed() {
/* 275 */                   if (!execRuntime.isEndpointConnected()) {
/*     */                     
/* 277 */                     if (AsyncConnectExec.LOG.isDebugEnabled()) {
/* 278 */                       AsyncConnectExec.LOG.debug("{} proxy disconnected", exchangeId);
/*     */                     }
/* 280 */                     state.tracker.reset();
/*     */                   } 
/* 282 */                   if (state.challenged) {
/* 283 */                     if (AsyncConnectExec.LOG.isDebugEnabled()) {
/* 284 */                       AsyncConnectExec.LOG.debug("{} proxy authentication required", exchangeId);
/*     */                     }
/* 286 */                     AsyncConnectExec.this.proceedToNextHop(state, request, entityProducer, scope, chain, asyncExecCallback);
/*     */                   }
/* 288 */                   else if (state.tunnelRefused) {
/* 289 */                     if (AsyncConnectExec.LOG.isDebugEnabled()) {
/* 290 */                       AsyncConnectExec.LOG.debug("{} tunnel refused", exchangeId);
/*     */                     }
/* 292 */                     asyncExecCallback.failed((Exception)new TunnelRefusedException("Tunnel refused", null));
/*     */                   } else {
/* 294 */                     if (AsyncConnectExec.LOG.isDebugEnabled()) {
/* 295 */                       AsyncConnectExec.LOG.debug("{} tunnel to target created", exchangeId);
/*     */                     }
/* 297 */                     tracker.tunnelTarget(false);
/* 298 */                     AsyncConnectExec.this.proceedToNextHop(state, request, entityProducer, scope, chain, asyncExecCallback);
/*     */                   } 
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 public void failed(Exception cause) {
/* 305 */                   asyncExecCallback.failed(cause);
/*     */                 }
/*     */               });
/*     */         }
/* 309 */         catch (HttpException|IOException ex) {
/* 310 */           asyncExecCallback.failed(ex);
/*     */         } 
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 319 */         asyncExecCallback.failed((Exception)new HttpException("Proxy chains are not supported"));
/*     */         return;
/*     */       
/*     */       case 5:
/* 323 */         execRuntime.upgradeTls(clientContext, new FutureCallback<AsyncExecRuntime>()
/*     */             {
/*     */               public void completed(AsyncExecRuntime asyncExecRuntime)
/*     */               {
/* 327 */                 if (AsyncConnectExec.LOG.isDebugEnabled()) {
/* 328 */                   AsyncConnectExec.LOG.debug("{} upgraded to TLS", exchangeId);
/*     */                 }
/* 330 */                 tracker.layerProtocol(route.isSecure());
/* 331 */                 AsyncConnectExec.this.proceedToNextHop(state, request, entityProducer, scope, chain, asyncExecCallback);
/*     */               }
/*     */ 
/*     */               
/*     */               public void failed(Exception ex) {
/* 336 */                 asyncExecCallback.failed(ex);
/*     */               }
/*     */ 
/*     */               
/*     */               public void cancelled() {
/* 341 */                 asyncExecCallback.failed(new InterruptedIOException());
/*     */               }
/*     */             });
/*     */         return;
/*     */ 
/*     */       
/*     */       case -1:
/* 348 */         asyncExecCallback.failed((Exception)new HttpException("Unable to establish route: planned = " + route + "; current = " + fact));
/*     */         return;
/*     */ 
/*     */       
/*     */       case 0:
/* 353 */         if (LOG.isDebugEnabled()) {
/* 354 */           LOG.debug("{} route fully established", exchangeId);
/*     */         }
/*     */         try {
/* 357 */           chain.proceed(request, entityProducer, scope, asyncExecCallback);
/* 358 */         } catch (HttpException|IOException ex) {
/* 359 */           asyncExecCallback.failed(ex);
/*     */         } 
/*     */         return;
/*     */     } 
/*     */     
/* 364 */     throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
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
/*     */   private void createTunnel(final State state, final HttpHost proxy, HttpHost nextHop, AsyncExecChain.Scope scope, AsyncExecChain chain, final AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 376 */     final HttpClientContext clientContext = scope.clientContext;
/*     */     
/* 378 */     final AuthExchange proxyAuthExchange = (proxy != null) ? clientContext.getAuthExchange(proxy) : new AuthExchange();
/*     */     
/* 380 */     if (this.authCacheKeeper != null) {
/* 381 */       this.authCacheKeeper.loadPreemptively(proxy, null, proxyAuthExchange, (HttpContext)clientContext);
/*     */     }
/*     */     
/* 384 */     BasicHttpRequest basicHttpRequest = new BasicHttpRequest(Method.CONNECT, nextHop, nextHop.toHostString());
/* 385 */     basicHttpRequest.setVersion((ProtocolVersion)HttpVersion.HTTP_1_1);
/*     */     
/* 387 */     this.proxyHttpProcessor.process((HttpRequest)basicHttpRequest, null, (HttpContext)clientContext);
/* 388 */     this.authenticator.addAuthResponse(proxy, ChallengeType.PROXY, (HttpRequest)basicHttpRequest, proxyAuthExchange, (HttpContext)clientContext);
/*     */     
/* 390 */     chain.proceed((HttpRequest)basicHttpRequest, null, scope, new AsyncExecCallback()
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public AsyncDataConsumer handleResponse(HttpResponse response, EntityDetails entityDetails) throws HttpException, IOException
/*     */           {
/* 397 */             clientContext.setAttribute("http.response", response);
/* 398 */             AsyncConnectExec.this.proxyHttpProcessor.process(response, entityDetails, (HttpContext)clientContext);
/*     */             
/* 400 */             int status = response.getCode();
/* 401 */             if (status < 200) {
/* 402 */               throw new HttpException("Unexpected response to CONNECT request: " + new StatusLine(response));
/*     */             }
/*     */             
/* 405 */             if (AsyncConnectExec.this.needAuthentication(proxyAuthExchange, proxy, response, clientContext)) {
/* 406 */               state.challenged = true;
/* 407 */               return null;
/*     */             } 
/* 409 */             state.challenged = false;
/* 410 */             if (status >= 300) {
/* 411 */               state.tunnelRefused = true;
/* 412 */               return asyncExecCallback.handleResponse(response, entityDetails);
/*     */             } 
/* 414 */             return null;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void handleInformationResponse(HttpResponse response) throws HttpException, IOException {}
/*     */ 
/*     */           
/*     */           public void completed() {
/* 423 */             asyncExecCallback.completed();
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception cause) {
/* 428 */             asyncExecCallback.failed(cause);
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
/*     */   private boolean needAuthentication(AuthExchange proxyAuthExchange, HttpHost proxy, HttpResponse response, HttpClientContext context) {
/* 440 */     RequestConfig config = context.getRequestConfig();
/* 441 */     if (config.isAuthenticationEnabled()) {
/* 442 */       boolean proxyAuthRequested = this.authenticator.isChallenged(proxy, ChallengeType.PROXY, response, proxyAuthExchange, (HttpContext)context);
/*     */       
/* 444 */       if (this.authCacheKeeper != null) {
/* 445 */         if (proxyAuthRequested) {
/* 446 */           this.authCacheKeeper.updateOnChallenge(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         } else {
/* 448 */           this.authCacheKeeper.updateOnNoChallenge(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         } 
/*     */       }
/*     */       
/* 452 */       if (proxyAuthRequested) {
/* 453 */         boolean updated = this.authenticator.updateAuthState(proxy, ChallengeType.PROXY, response, this.proxyAuthStrategy, proxyAuthExchange, (HttpContext)context);
/*     */ 
/*     */         
/* 456 */         if (this.authCacheKeeper != null) {
/* 457 */           this.authCacheKeeper.updateOnResponse(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         }
/*     */         
/* 460 */         return updated;
/*     */       } 
/*     */     } 
/* 463 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/AsyncConnectExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */