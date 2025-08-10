/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.Objects;
/*     */ import org.apache.hc.client5.http.CircularRedirectException;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.RedirectException;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.classic.ExecChain;
/*     */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.protocol.RedirectLocations;
/*     */ import org.apache.hc.client5.http.protocol.RedirectStrategy;
/*     */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*     */ import org.apache.hc.client5.http.utils.URIUtils;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*     */ import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class RedirectExec
/*     */   implements ExecChainHandler
/*     */ {
/*  78 */   private static final Logger LOG = LoggerFactory.getLogger(RedirectExec.class);
/*     */ 
/*     */   
/*     */   private final RedirectStrategy redirectStrategy;
/*     */   
/*     */   private final HttpRoutePlanner routePlanner;
/*     */ 
/*     */   
/*     */   public RedirectExec(HttpRoutePlanner routePlanner, RedirectStrategy redirectStrategy) {
/*  87 */     Args.notNull(routePlanner, "HTTP route planner");
/*  88 */     Args.notNull(redirectStrategy, "HTTP redirect strategy");
/*  89 */     this.routePlanner = routePlanner;
/*  90 */     this.redirectStrategy = redirectStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(ClassicHttpRequest request, ExecChain.Scope scope, ExecChain chain) throws IOException, HttpException {
/*     */     String exchangeId;
/*     */     ClassicHttpResponse response;
/*  98 */     Args.notNull(request, "HTTP request");
/*  99 */     Args.notNull(scope, "Scope");
/*     */     
/* 101 */     HttpClientContext context = scope.clientContext;
/* 102 */     RedirectLocations redirectLocations = context.getRedirectLocations();
/* 103 */     if (redirectLocations == null) {
/* 104 */       redirectLocations = new RedirectLocations();
/* 105 */       context.setAttribute("http.protocol.redirect-locations", redirectLocations);
/*     */     } 
/* 107 */     redirectLocations.clear();
/*     */     
/* 109 */     RequestConfig config = context.getRequestConfig();
/* 110 */     int maxRedirects = (config.getMaxRedirects() > 0) ? config.getMaxRedirects() : 50;
/* 111 */     ClassicHttpRequest currentRequest = request;
/* 112 */     ExecChain.Scope currentScope = scope;
/* 113 */     int redirectCount = 0; while (true) {
/* 114 */       exchangeId = currentScope.exchangeId;
/* 115 */       response = chain.proceed(currentRequest, currentScope);
/*     */       try {
/* 117 */         if (config.isRedirectsEnabled() && this.redirectStrategy.isRedirected((HttpRequest)request, (HttpResponse)response, (HttpContext)context)) {
/* 118 */           ClassicRequestBuilder redirectBuilder; HttpEntity requestEntity = request.getEntity();
/* 119 */           if (requestEntity != null && !requestEntity.isRepeatable()) {
/* 120 */             if (LOG.isDebugEnabled()) {
/* 121 */               LOG.debug("{} cannot redirect non-repeatable request", exchangeId);
/*     */             }
/* 123 */             return response;
/*     */           } 
/* 125 */           if (redirectCount >= maxRedirects) {
/* 126 */             throw new RedirectException("Maximum redirects (" + maxRedirects + ") exceeded");
/*     */           }
/* 128 */           redirectCount++;
/*     */           
/* 130 */           URI redirectUri = this.redirectStrategy.getLocationURI((HttpRequest)currentRequest, (HttpResponse)response, (HttpContext)context);
/* 131 */           if (LOG.isDebugEnabled()) {
/* 132 */             LOG.debug("{} redirect requested to location '{}'", exchangeId, redirectUri);
/*     */           }
/*     */           
/* 135 */           HttpHost newTarget = URIUtils.extractHost(redirectUri);
/* 136 */           if (newTarget == null) {
/* 137 */             throw new ProtocolException("Redirect URI does not specify a valid host name: " + redirectUri);
/*     */           }
/*     */ 
/*     */           
/* 141 */           if (!config.isCircularRedirectsAllowed() && 
/* 142 */             redirectLocations.contains(redirectUri)) {
/* 143 */             throw new CircularRedirectException("Circular redirect to '" + redirectUri + "'");
/*     */           }
/*     */           
/* 146 */           redirectLocations.add(redirectUri);
/*     */           
/* 148 */           int statusCode = response.getCode();
/*     */           
/* 150 */           switch (statusCode) {
/*     */             case 301:
/*     */             case 302:
/* 153 */               if (Method.POST.isSame(request.getMethod())) {
/* 154 */                 ClassicRequestBuilder classicRequestBuilder = ClassicRequestBuilder.get(); break;
/*     */               } 
/* 156 */               redirectBuilder = ClassicRequestBuilder.copy(scope.originalRequest);
/*     */               break;
/*     */             
/*     */             case 303:
/* 160 */               if (!Method.GET.isSame(request.getMethod()) && !Method.HEAD.isSame(request.getMethod())) {
/* 161 */                 redirectBuilder = ClassicRequestBuilder.get(); break;
/*     */               } 
/* 163 */               redirectBuilder = ClassicRequestBuilder.copy(scope.originalRequest);
/*     */               break;
/*     */             
/*     */             default:
/* 167 */               redirectBuilder = ClassicRequestBuilder.copy(scope.originalRequest); break;
/*     */           } 
/* 169 */           redirectBuilder.setUri(redirectUri);
/*     */           
/* 171 */           HttpRoute currentRoute = currentScope.route;
/* 172 */           if (!Objects.equals(currentRoute.getTargetHost(), newTarget)) {
/* 173 */             HttpRoute newRoute = this.routePlanner.determineRoute(newTarget, (HttpContext)context);
/* 174 */             if (!Objects.equals(currentRoute, newRoute)) {
/* 175 */               if (LOG.isDebugEnabled()) {
/* 176 */                 LOG.debug("{} new route required", exchangeId);
/*     */               }
/* 178 */               AuthExchange targetAuthExchange = context.getAuthExchange(currentRoute.getTargetHost());
/* 179 */               if (LOG.isDebugEnabled()) {
/* 180 */                 LOG.debug("{} resetting target auth state", exchangeId);
/*     */               }
/* 182 */               targetAuthExchange.reset();
/* 183 */               if (currentRoute.getProxyHost() != null) {
/* 184 */                 AuthExchange proxyAuthExchange = context.getAuthExchange(currentRoute.getProxyHost());
/* 185 */                 if (proxyAuthExchange.isConnectionBased()) {
/* 186 */                   if (LOG.isDebugEnabled()) {
/* 187 */                     LOG.debug("{} resetting proxy auth state", exchangeId);
/*     */                   }
/* 189 */                   proxyAuthExchange.reset();
/*     */                 } 
/*     */               } 
/* 192 */               currentScope = new ExecChain.Scope(currentScope.exchangeId, newRoute, currentScope.originalRequest, currentScope.execRuntime, currentScope.clientContext);
/*     */             } 
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 201 */           if (LOG.isDebugEnabled()) {
/* 202 */             LOG.debug("{} redirecting to '{}' via {}", new Object[] { exchangeId, redirectUri, currentRoute });
/*     */           }
/* 204 */           currentRequest = redirectBuilder.build();
/* 205 */           RequestEntityProxy.enhance(currentRequest);
/*     */           
/* 207 */           EntityUtils.consume(response.getEntity());
/* 208 */           response.close(); continue;
/*     */         } 
/* 210 */         return response;
/*     */       }
/* 212 */       catch (RuntimeException|IOException exception) {
/* 213 */         response.close();
/* 214 */         throw exception;
/* 215 */       } catch (HttpException ex) {
/*     */         break;
/*     */       } 
/*     */     }  try {
/* 219 */       EntityUtils.consume(response.getEntity());
/* 220 */     } catch (IOException ioex) {
/* 221 */       if (LOG.isDebugEnabled()) {
/* 222 */         LOG.debug("{} I/O error while releasing connection", exchangeId, ioex);
/*     */       }
/*     */     } finally {
/* 225 */       response.close();
/*     */     } 
/* 227 */     throw ex;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/RedirectExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */