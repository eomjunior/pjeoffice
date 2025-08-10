/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.client5.http.CircularRedirectException;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.RedirectException;
/*     */ import org.apache.hc.client5.http.async.AsyncExecCallback;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChain;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChainHandler;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.protocol.RedirectLocations;
/*     */ import org.apache.hc.client5.http.protocol.RedirectStrategy;
/*     */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*     */ import org.apache.hc.client5.http.utils.URIUtils;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.support.BasicRequestBuilder;
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
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class AsyncRedirectExec
/*     */   implements AsyncExecChainHandler
/*     */ {
/*  78 */   private static final Logger LOG = LoggerFactory.getLogger(AsyncRedirectExec.class);
/*     */   
/*     */   private final HttpRoutePlanner routePlanner;
/*     */   private final RedirectStrategy redirectStrategy;
/*     */   
/*     */   AsyncRedirectExec(HttpRoutePlanner routePlanner, RedirectStrategy redirectStrategy) {
/*  84 */     this.routePlanner = routePlanner;
/*  85 */     this.redirectStrategy = redirectStrategy;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class State
/*     */   {
/*     */     volatile URI redirectURI;
/*     */     
/*     */     volatile int maxRedirects;
/*     */     
/*     */     volatile int redirectCount;
/*     */     volatile HttpRequest currentRequest;
/*     */     volatile AsyncEntityProducer currentEntityProducer;
/*     */     volatile RedirectLocations redirectLocations;
/*     */     volatile AsyncExecChain.Scope currentScope;
/*     */     volatile boolean reroute;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */   
/*     */   private void internalExecute(final State state, final AsyncExecChain chain, final AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 106 */     final HttpRequest request = state.currentRequest;
/* 107 */     AsyncEntityProducer entityProducer = state.currentEntityProducer;
/* 108 */     final AsyncExecChain.Scope scope = state.currentScope;
/* 109 */     final HttpClientContext clientContext = scope.clientContext;
/* 110 */     final String exchangeId = scope.exchangeId;
/* 111 */     final HttpRoute currentRoute = scope.route;
/* 112 */     chain.proceed(request, entityProducer, scope, new AsyncExecCallback()
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public AsyncDataConsumer handleResponse(HttpResponse response, EntityDetails entityDetails) throws HttpException, IOException
/*     */           {
/* 119 */             state.redirectURI = null;
/* 120 */             RequestConfig config = clientContext.getRequestConfig();
/* 121 */             if (config.isRedirectsEnabled() && AsyncRedirectExec.this.redirectStrategy.isRedirected(request, response, (HttpContext)clientContext)) {
/* 122 */               BasicRequestBuilder redirectBuilder; if (state.redirectCount >= state.maxRedirects) {
/* 123 */                 throw new RedirectException("Maximum redirects (" + state.maxRedirects + ") exceeded");
/*     */               }
/*     */               
/* 126 */               state.redirectCount++;
/*     */               
/* 128 */               URI redirectUri = AsyncRedirectExec.this.redirectStrategy.getLocationURI(request, response, (HttpContext)clientContext);
/* 129 */               if (AsyncRedirectExec.LOG.isDebugEnabled()) {
/* 130 */                 AsyncRedirectExec.LOG.debug("{} redirect requested to location '{}'", exchangeId, redirectUri);
/*     */               }
/* 132 */               if (!config.isCircularRedirectsAllowed() && 
/* 133 */                 state.redirectLocations.contains(redirectUri)) {
/* 134 */                 throw new CircularRedirectException("Circular redirect to '" + redirectUri + "'");
/*     */               }
/*     */               
/* 137 */               state.redirectLocations.add(redirectUri);
/*     */               
/* 139 */               HttpHost newTarget = URIUtils.extractHost(redirectUri);
/* 140 */               if (newTarget == null) {
/* 141 */                 throw new ProtocolException("Redirect URI does not specify a valid host name: " + redirectUri);
/*     */               }
/*     */               
/* 144 */               int statusCode = response.getCode();
/*     */               
/* 146 */               switch (statusCode) {
/*     */                 case 301:
/*     */                 case 302:
/* 149 */                   if (Method.POST.isSame(request.getMethod())) {
/* 150 */                     BasicRequestBuilder basicRequestBuilder = BasicRequestBuilder.get();
/* 151 */                     state.currentEntityProducer = null; break;
/*     */                   } 
/* 153 */                   redirectBuilder = BasicRequestBuilder.copy(scope.originalRequest);
/*     */                   break;
/*     */                 
/*     */                 case 303:
/* 157 */                   if (!Method.GET.isSame(request.getMethod()) && !Method.HEAD.isSame(request.getMethod())) {
/* 158 */                     redirectBuilder = BasicRequestBuilder.get();
/* 159 */                     state.currentEntityProducer = null; break;
/*     */                   } 
/* 161 */                   redirectBuilder = BasicRequestBuilder.copy(scope.originalRequest);
/*     */                   break;
/*     */                 
/*     */                 default:
/* 165 */                   redirectBuilder = BasicRequestBuilder.copy(scope.originalRequest); break;
/*     */               } 
/* 167 */               redirectBuilder.setUri(redirectUri);
/* 168 */               state.reroute = false;
/* 169 */               state.redirectURI = redirectUri;
/* 170 */               state.currentRequest = (HttpRequest)redirectBuilder.build();
/*     */               
/* 172 */               if (!Objects.equals(currentRoute.getTargetHost(), newTarget)) {
/* 173 */                 HttpRoute newRoute = AsyncRedirectExec.this.routePlanner.determineRoute(newTarget, (HttpContext)clientContext);
/* 174 */                 if (!Objects.equals(currentRoute, newRoute)) {
/* 175 */                   state.reroute = true;
/* 176 */                   AuthExchange targetAuthExchange = clientContext.getAuthExchange(currentRoute.getTargetHost());
/* 177 */                   if (AsyncRedirectExec.LOG.isDebugEnabled()) {
/* 178 */                     AsyncRedirectExec.LOG.debug("{} resetting target auth state", exchangeId);
/*     */                   }
/* 180 */                   targetAuthExchange.reset();
/* 181 */                   if (currentRoute.getProxyHost() != null) {
/* 182 */                     AuthExchange proxyAuthExchange = clientContext.getAuthExchange(currentRoute.getProxyHost());
/* 183 */                     if (proxyAuthExchange.isConnectionBased()) {
/* 184 */                       if (AsyncRedirectExec.LOG.isDebugEnabled()) {
/* 185 */                         AsyncRedirectExec.LOG.debug("{} resetting proxy auth state", exchangeId);
/*     */                       }
/* 187 */                       proxyAuthExchange.reset();
/*     */                     } 
/*     */                   } 
/* 190 */                   state.currentScope = new AsyncExecChain.Scope(scope.exchangeId, newRoute, scope.originalRequest, scope.cancellableDependency, scope.clientContext, scope.execRuntime, scope.scheduler, scope.execCount);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 202 */             if (state.redirectURI != null) {
/* 203 */               if (AsyncRedirectExec.LOG.isDebugEnabled()) {
/* 204 */                 AsyncRedirectExec.LOG.debug("{} redirecting to '{}' via {}", new Object[] { this.val$exchangeId, this.val$state.redirectURI, this.val$currentRoute });
/*     */               }
/* 206 */               return null;
/*     */             } 
/* 208 */             return asyncExecCallback.handleResponse(response, entityDetails);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void handleInformationResponse(HttpResponse response) throws HttpException, IOException {
/* 214 */             asyncExecCallback.handleInformationResponse(response);
/*     */           }
/*     */ 
/*     */           
/*     */           public void completed() {
/* 219 */             if (state.redirectURI == null) {
/* 220 */               asyncExecCallback.completed();
/*     */             } else {
/* 222 */               AsyncEntityProducer entityProducer = state.currentEntityProducer;
/* 223 */               if (entityProducer != null) {
/* 224 */                 entityProducer.releaseResources();
/*     */               }
/* 226 */               if (entityProducer != null && !entityProducer.isRepeatable()) {
/* 227 */                 if (AsyncRedirectExec.LOG.isDebugEnabled()) {
/* 228 */                   AsyncRedirectExec.LOG.debug("{} cannot redirect non-repeatable request", exchangeId);
/*     */                 }
/* 230 */                 asyncExecCallback.completed();
/*     */               } else {
/*     */                 try {
/* 233 */                   if (state.reroute) {
/* 234 */                     scope.execRuntime.releaseEndpoint();
/*     */                   }
/* 236 */                   AsyncRedirectExec.this.internalExecute(state, chain, asyncExecCallback);
/* 237 */                 } catch (IOException|HttpException ex) {
/* 238 */                   asyncExecCallback.failed(ex);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception cause) {
/* 246 */             asyncExecCallback.failed(cause);
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
/*     */   public void execute(HttpRequest request, AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecChain chain, AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 260 */     HttpClientContext clientContext = scope.clientContext;
/* 261 */     RedirectLocations redirectLocations = clientContext.getRedirectLocations();
/* 262 */     if (redirectLocations == null) {
/* 263 */       redirectLocations = new RedirectLocations();
/* 264 */       clientContext.setAttribute("http.protocol.redirect-locations", redirectLocations);
/*     */     } 
/* 266 */     redirectLocations.clear();
/*     */     
/* 268 */     RequestConfig config = clientContext.getRequestConfig();
/*     */     
/* 270 */     State state = new State();
/* 271 */     state.maxRedirects = (config.getMaxRedirects() > 0) ? config.getMaxRedirects() : 50;
/* 272 */     state.redirectCount = 0;
/* 273 */     state.currentRequest = request;
/* 274 */     state.currentEntityProducer = entityProducer;
/* 275 */     state.redirectLocations = redirectLocations;
/* 276 */     state.currentScope = scope;
/*     */     
/* 278 */     internalExecute(state, chain, asyncExecCallback);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/AsyncRedirectExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */