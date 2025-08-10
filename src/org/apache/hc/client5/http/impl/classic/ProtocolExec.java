/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.ChallengeType;
/*     */ import org.apache.hc.client5.http.classic.ExecChain;
/*     */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*     */ import org.apache.hc.client5.http.classic.ExecRuntime;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.impl.RequestSupport;
/*     */ import org.apache.hc.client5.http.impl.auth.AuthCacheKeeper;
/*     */ import org.apache.hc.client5.http.impl.auth.HttpAuthenticator;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.Header;
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
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class ProtocolExec
/*     */   implements ExecChainHandler
/*     */ {
/*  82 */   private static final Logger LOG = LoggerFactory.getLogger(ProtocolExec.class);
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
/*     */   public ProtocolExec(AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, SchemePortResolver schemePortResolver, boolean authCachingDisabled) {
/*  95 */     this.targetAuthStrategy = (AuthenticationStrategy)Args.notNull(targetAuthStrategy, "Target authentication strategy");
/*  96 */     this.proxyAuthStrategy = (AuthenticationStrategy)Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
/*  97 */     this.authenticator = new HttpAuthenticator();
/*  98 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/*  99 */     this.authCacheKeeper = authCachingDisabled ? null : new AuthCacheKeeper(this.schemePortResolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(ClassicHttpRequest userRequest, ExecChain.Scope scope, ExecChain chain) throws IOException, HttpException {
/* 107 */     Args.notNull(userRequest, "HTTP request");
/* 108 */     Args.notNull(scope, "Scope");
/*     */     
/* 110 */     if (Method.CONNECT.isSame(userRequest.getMethod())) {
/* 111 */       throw new ProtocolException("Direct execution of CONNECT is not allowed");
/*     */     }
/*     */     
/* 114 */     String exchangeId = scope.exchangeId;
/* 115 */     HttpRoute route = scope.route;
/* 116 */     HttpClientContext context = scope.clientContext;
/* 117 */     ExecRuntime execRuntime = scope.execRuntime;
/*     */     
/* 119 */     HttpHost routeTarget = route.getTargetHost();
/* 120 */     HttpHost proxy = route.getProxyHost();
/*     */     try {
/*     */       ClassicHttpRequest request;
/*     */       ClassicHttpResponse response;
/* 124 */       if (proxy != null && !route.isTunnelled()) {
/* 125 */         ClassicRequestBuilder requestBuilder = ClassicRequestBuilder.copy(userRequest);
/* 126 */         if (requestBuilder.getAuthority() == null) {
/* 127 */           requestBuilder.setAuthority(new URIAuthority((NamedEndpoint)routeTarget));
/*     */         }
/* 129 */         requestBuilder.setAbsoluteRequestUri(true);
/* 130 */         request = requestBuilder.build();
/*     */       } else {
/* 132 */         request = userRequest;
/*     */       } 
/*     */ 
/*     */       
/* 136 */       if (request.getScheme() == null) {
/* 137 */         request.setScheme(routeTarget.getSchemeName());
/*     */       }
/* 139 */       if (request.getAuthority() == null) {
/* 140 */         request.setAuthority(new URIAuthority((NamedEndpoint)routeTarget));
/*     */       }
/*     */       
/* 143 */       URIAuthority authority = request.getAuthority();
/* 144 */       if (authority.getUserInfo() != null) {
/* 145 */         throw new ProtocolException("Request URI authority contains deprecated userinfo component");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 151 */       HttpHost target = new HttpHost(request.getScheme(), authority.getHostName(), this.schemePortResolver.resolve(request.getScheme(), (NamedEndpoint)authority));
/* 152 */       String pathPrefix = RequestSupport.extractPathPrefix((HttpRequest)request);
/*     */       
/* 154 */       AuthExchange targetAuthExchange = context.getAuthExchange(target);
/* 155 */       AuthExchange proxyAuthExchange = (proxy != null) ? context.getAuthExchange(proxy) : new AuthExchange();
/*     */       
/* 157 */       if (!targetAuthExchange.isConnectionBased() && targetAuthExchange
/* 158 */         .getPathPrefix() != null && 
/* 159 */         !pathPrefix.startsWith(targetAuthExchange.getPathPrefix()))
/*     */       {
/*     */         
/* 162 */         targetAuthExchange.reset();
/*     */       }
/* 164 */       if (targetAuthExchange.getPathPrefix() == null) {
/* 165 */         targetAuthExchange.setPathPrefix(pathPrefix);
/*     */       }
/*     */       
/* 168 */       if (this.authCacheKeeper != null) {
/* 169 */         this.authCacheKeeper.loadPreemptively(target, pathPrefix, targetAuthExchange, (HttpContext)context);
/* 170 */         if (proxy != null) {
/* 171 */           this.authCacheKeeper.loadPreemptively(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         }
/*     */       } 
/*     */       
/* 175 */       RequestEntityProxy.enhance(request);
/*     */ 
/*     */       
/*     */       while (true) {
/* 179 */         if (!request.containsHeader("Authorization")) {
/* 180 */           if (LOG.isDebugEnabled()) {
/* 181 */             LOG.debug("{} target auth state: {}", exchangeId, targetAuthExchange.getState());
/*     */           }
/* 183 */           this.authenticator.addAuthResponse(target, ChallengeType.TARGET, (HttpRequest)request, targetAuthExchange, (HttpContext)context);
/*     */         } 
/* 185 */         if (!request.containsHeader("Proxy-Authorization") && !route.isTunnelled()) {
/* 186 */           if (LOG.isDebugEnabled()) {
/* 187 */             LOG.debug("{} proxy auth state: {}", exchangeId, proxyAuthExchange.getState());
/*     */           }
/* 189 */           this.authenticator.addAuthResponse(proxy, ChallengeType.PROXY, (HttpRequest)request, proxyAuthExchange, (HttpContext)context);
/*     */         } 
/*     */         
/* 192 */         response = chain.proceed(request, scope);
/*     */         
/* 194 */         if (Method.TRACE.isSame(request.getMethod())) {
/*     */           
/* 196 */           ResponseEntityProxy.enhance(response, execRuntime);
/* 197 */           return response;
/*     */         } 
/* 199 */         HttpEntity requestEntity = request.getEntity();
/* 200 */         if (requestEntity != null && !requestEntity.isRepeatable()) {
/* 201 */           if (LOG.isDebugEnabled()) {
/* 202 */             LOG.debug("{} Cannot retry non-repeatable request", exchangeId);
/*     */           }
/* 204 */           ResponseEntityProxy.enhance(response, execRuntime);
/* 205 */           return response;
/*     */         } 
/* 207 */         if (needAuthentication(targetAuthExchange, proxyAuthExchange, (proxy != null) ? proxy : target, target, pathPrefix, (HttpResponse)response, context)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 216 */           HttpEntity responseEntity = response.getEntity();
/* 217 */           if (execRuntime.isConnectionReusable()) {
/* 218 */             EntityUtils.consume(responseEntity);
/*     */           } else {
/* 220 */             execRuntime.disconnectEndpoint();
/* 221 */             if (proxyAuthExchange.getState() == AuthExchange.State.SUCCESS && proxyAuthExchange
/* 222 */               .isConnectionBased()) {
/* 223 */               if (LOG.isDebugEnabled()) {
/* 224 */                 LOG.debug("{} resetting proxy auth state", exchangeId);
/*     */               }
/* 226 */               proxyAuthExchange.reset();
/*     */             } 
/* 228 */             if (targetAuthExchange.getState() == AuthExchange.State.SUCCESS && targetAuthExchange
/* 229 */               .isConnectionBased()) {
/* 230 */               if (LOG.isDebugEnabled()) {
/* 231 */                 LOG.debug("{} resetting target auth state", exchangeId);
/*     */               }
/* 233 */               targetAuthExchange.reset();
/*     */             } 
/*     */           } 
/*     */           
/* 237 */           ClassicHttpRequest original = scope.originalRequest;
/* 238 */           request.setHeaders(new Header[0]);
/* 239 */           for (Iterator<Header> it = original.headerIterator(); it.hasNext();)
/* 240 */             request.addHeader(it.next());  continue;
/*     */         }  break;
/*     */       } 
/* 243 */       ResponseEntityProxy.enhance(response, execRuntime);
/* 244 */       return response;
/*     */     
/*     */     }
/* 247 */     catch (HttpException ex) {
/* 248 */       execRuntime.discardEndpoint();
/* 249 */       throw ex;
/* 250 */     } catch (RuntimeException|IOException ex) {
/* 251 */       execRuntime.discardEndpoint();
/* 252 */       for (AuthExchange authExchange : context.getAuthExchanges().values()) {
/* 253 */         if (authExchange.isConnectionBased()) {
/* 254 */           authExchange.reset();
/*     */         }
/*     */       } 
/* 257 */       throw ex;
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
/*     */   private boolean needAuthentication(AuthExchange targetAuthExchange, AuthExchange proxyAuthExchange, HttpHost proxy, HttpHost target, String pathPrefix, HttpResponse response, HttpClientContext context) {
/* 269 */     RequestConfig config = context.getRequestConfig();
/* 270 */     if (config.isAuthenticationEnabled()) {
/* 271 */       boolean targetAuthRequested = this.authenticator.isChallenged(target, ChallengeType.TARGET, response, targetAuthExchange, (HttpContext)context);
/*     */ 
/*     */       
/* 274 */       if (this.authCacheKeeper != null) {
/* 275 */         if (targetAuthRequested) {
/* 276 */           this.authCacheKeeper.updateOnChallenge(target, pathPrefix, targetAuthExchange, (HttpContext)context);
/*     */         } else {
/* 278 */           this.authCacheKeeper.updateOnNoChallenge(target, pathPrefix, targetAuthExchange, (HttpContext)context);
/*     */         } 
/*     */       }
/*     */       
/* 282 */       boolean proxyAuthRequested = this.authenticator.isChallenged(proxy, ChallengeType.PROXY, response, proxyAuthExchange, (HttpContext)context);
/*     */ 
/*     */       
/* 285 */       if (this.authCacheKeeper != null) {
/* 286 */         if (proxyAuthRequested) {
/* 287 */           this.authCacheKeeper.updateOnChallenge(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         } else {
/* 289 */           this.authCacheKeeper.updateOnNoChallenge(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         } 
/*     */       }
/*     */       
/* 293 */       if (targetAuthRequested) {
/* 294 */         boolean updated = this.authenticator.updateAuthState(target, ChallengeType.TARGET, response, this.targetAuthStrategy, targetAuthExchange, (HttpContext)context);
/*     */ 
/*     */         
/* 297 */         if (this.authCacheKeeper != null) {
/* 298 */           this.authCacheKeeper.updateOnResponse(target, pathPrefix, targetAuthExchange, (HttpContext)context);
/*     */         }
/*     */         
/* 301 */         return updated;
/*     */       } 
/* 303 */       if (proxyAuthRequested) {
/* 304 */         boolean updated = this.authenticator.updateAuthState(proxy, ChallengeType.PROXY, response, this.proxyAuthStrategy, proxyAuthExchange, (HttpContext)context);
/*     */ 
/*     */         
/* 307 */         if (this.authCacheKeeper != null) {
/* 308 */           this.authCacheKeeper.updateOnResponse(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */         }
/*     */         
/* 311 */         return updated;
/*     */       } 
/*     */     } 
/* 314 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/ProtocolExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */