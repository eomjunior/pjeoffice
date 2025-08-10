/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.RouteInfo;
/*     */ import org.apache.hc.client5.http.RouteTracker;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.ChallengeType;
/*     */ import org.apache.hc.client5.http.classic.ExecChain;
/*     */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*     */ import org.apache.hc.client5.http.classic.ExecRuntime;
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
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.io.entity.EntityUtils;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class ConnectExec
/*     */   implements ExecChainHandler
/*     */ {
/*  81 */   private static final Logger LOG = LoggerFactory.getLogger(ConnectExec.class);
/*     */   
/*     */   private final ConnectionReuseStrategy reuseStrategy;
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
/*     */   public ConnectExec(ConnectionReuseStrategy reuseStrategy, HttpProcessor proxyHttpProcessor, AuthenticationStrategy proxyAuthStrategy, SchemePortResolver schemePortResolver, boolean authCachingDisabled) {
/*  96 */     Args.notNull(reuseStrategy, "Connection reuse strategy");
/*  97 */     Args.notNull(proxyHttpProcessor, "Proxy HTTP processor");
/*  98 */     Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
/*  99 */     this.reuseStrategy = reuseStrategy;
/* 100 */     this.proxyHttpProcessor = proxyHttpProcessor;
/* 101 */     this.proxyAuthStrategy = proxyAuthStrategy;
/* 102 */     this.authenticator = new HttpAuthenticator();
/* 103 */     this.authCacheKeeper = authCachingDisabled ? null : new AuthCacheKeeper(schemePortResolver);
/* 104 */     this.routeDirector = (HttpRouteDirector)BasicRouteDirector.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(ClassicHttpRequest request, ExecChain.Scope scope, ExecChain chain) throws IOException, HttpException {
/* 112 */     Args.notNull(request, "HTTP request");
/* 113 */     Args.notNull(scope, "Scope");
/*     */     
/* 115 */     String exchangeId = scope.exchangeId;
/* 116 */     HttpRoute route = scope.route;
/* 117 */     HttpClientContext context = scope.clientContext;
/* 118 */     ExecRuntime execRuntime = scope.execRuntime;
/*     */     
/* 120 */     if (!execRuntime.isEndpointAcquired()) {
/* 121 */       Object userToken = context.getUserToken();
/* 122 */       if (LOG.isDebugEnabled()) {
/* 123 */         LOG.debug("{} acquiring connection with route {}", exchangeId, route);
/*     */       }
/* 125 */       execRuntime.acquireEndpoint(exchangeId, route, userToken, context);
/*     */     } 
/*     */     try {
/* 128 */       if (!execRuntime.isEndpointConnected()) {
/* 129 */         int step; if (LOG.isDebugEnabled()) {
/* 130 */           LOG.debug("{} opening connection {}", exchangeId, route);
/*     */         }
/*     */         
/* 133 */         RouteTracker tracker = new RouteTracker(route); do {
/*     */           HttpHost proxy; boolean secure; int hop;
/*     */           boolean bool1;
/* 136 */           HttpRoute fact = tracker.toRoute();
/* 137 */           step = this.routeDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
/*     */           
/* 139 */           switch (step) {
/*     */             
/*     */             case 1:
/* 142 */               execRuntime.connectEndpoint(context);
/* 143 */               tracker.connectTarget(route.isSecure());
/*     */               break;
/*     */             case 2:
/* 146 */               execRuntime.connectEndpoint(context);
/* 147 */               proxy = route.getProxyHost();
/* 148 */               tracker.connectProxy(proxy, (route.isSecure() && !route.isTunnelled()));
/*     */               break;
/*     */             case 3:
/* 151 */               secure = createTunnelToTarget(exchangeId, route, (HttpRequest)request, execRuntime, context);
/* 152 */               if (LOG.isDebugEnabled()) {
/* 153 */                 LOG.debug("{} tunnel to target created.", exchangeId);
/*     */               }
/* 155 */               tracker.tunnelTarget(secure);
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case 4:
/* 163 */               hop = fact.getHopCount() - 1;
/* 164 */               bool1 = createTunnelToProxy(route, hop, context);
/* 165 */               if (LOG.isDebugEnabled()) {
/* 166 */                 LOG.debug("{} tunnel to proxy created.", exchangeId);
/*     */               }
/* 168 */               tracker.tunnelProxy(route.getHopTarget(hop), bool1);
/*     */               break;
/*     */             
/*     */             case 5:
/* 172 */               execRuntime.upgradeTls(context);
/* 173 */               tracker.layerProtocol(route.isSecure());
/*     */               break;
/*     */             
/*     */             case -1:
/* 177 */               throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
/*     */             
/*     */             case 0:
/*     */               break;
/*     */             default:
/* 182 */               throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
/*     */           } 
/*     */ 
/*     */         
/* 186 */         } while (step > 0);
/*     */       } 
/* 188 */       return chain.proceed(request, scope);
/*     */     }
/* 190 */     catch (IOException|HttpException|RuntimeException ex) {
/* 191 */       execRuntime.discardEndpoint();
/* 192 */       throw ex;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean createTunnelToTarget(String exchangeId, HttpRoute route, HttpRequest request, ExecRuntime execRuntime, HttpClientContext context) throws HttpException, IOException {
/* 211 */     RequestConfig config = context.getRequestConfig();
/*     */     
/* 213 */     HttpHost target = route.getTargetHost();
/* 214 */     HttpHost proxy = route.getProxyHost();
/* 215 */     AuthExchange proxyAuthExchange = context.getAuthExchange(proxy);
/*     */     
/* 217 */     if (this.authCacheKeeper != null) {
/* 218 */       this.authCacheKeeper.loadPreemptively(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */     }
/*     */     
/* 221 */     ClassicHttpResponse response = null;
/*     */     
/* 223 */     String authority = target.toHostString();
/* 224 */     BasicClassicHttpRequest basicClassicHttpRequest = new BasicClassicHttpRequest(Method.CONNECT, target, authority);
/* 225 */     basicClassicHttpRequest.setVersion((ProtocolVersion)HttpVersion.HTTP_1_1);
/*     */     
/* 227 */     this.proxyHttpProcessor.process((HttpRequest)basicClassicHttpRequest, null, (HttpContext)context);
/*     */     
/* 229 */     while (response == null) {
/* 230 */       basicClassicHttpRequest.removeHeaders("Proxy-Authorization");
/* 231 */       this.authenticator.addAuthResponse(proxy, ChallengeType.PROXY, (HttpRequest)basicClassicHttpRequest, proxyAuthExchange, (HttpContext)context);
/*     */       
/* 233 */       response = execRuntime.execute(exchangeId, (ClassicHttpRequest)basicClassicHttpRequest, context);
/* 234 */       this.proxyHttpProcessor.process((HttpResponse)response, (EntityDetails)response.getEntity(), (HttpContext)context);
/*     */       
/* 236 */       int i = response.getCode();
/* 237 */       if (i < 200) {
/* 238 */         throw new HttpException("Unexpected response to CONNECT request: " + new StatusLine(response));
/*     */       }
/*     */       
/* 241 */       if (config.isAuthenticationEnabled()) {
/* 242 */         boolean proxyAuthRequested = this.authenticator.isChallenged(proxy, ChallengeType.PROXY, (HttpResponse)response, proxyAuthExchange, (HttpContext)context);
/*     */         
/* 244 */         if (this.authCacheKeeper != null) {
/* 245 */           if (proxyAuthRequested) {
/* 246 */             this.authCacheKeeper.updateOnChallenge(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */           } else {
/* 248 */             this.authCacheKeeper.updateOnNoChallenge(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */           } 
/*     */         }
/*     */         
/* 252 */         if (proxyAuthRequested) {
/* 253 */           boolean updated = this.authenticator.updateAuthState(proxy, ChallengeType.PROXY, (HttpResponse)response, this.proxyAuthStrategy, proxyAuthExchange, (HttpContext)context);
/*     */ 
/*     */           
/* 256 */           if (this.authCacheKeeper != null) {
/* 257 */             this.authCacheKeeper.updateOnResponse(proxy, null, proxyAuthExchange, (HttpContext)context);
/*     */           }
/* 259 */           if (updated) {
/*     */             
/* 261 */             if (this.reuseStrategy.keepAlive((HttpRequest)basicClassicHttpRequest, (HttpResponse)response, (HttpContext)context)) {
/* 262 */               if (LOG.isDebugEnabled()) {
/* 263 */                 LOG.debug("{} connection kept alive", exchangeId);
/*     */               }
/*     */               
/* 266 */               HttpEntity entity = response.getEntity();
/* 267 */               EntityUtils.consume(entity);
/*     */             } else {
/* 269 */               execRuntime.disconnectEndpoint();
/*     */             } 
/* 271 */             response = null;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 277 */     int status = response.getCode();
/* 278 */     if (status != 200) {
/*     */ 
/*     */       
/* 281 */       HttpEntity entity = response.getEntity();
/* 282 */       String responseMessage = (entity != null) ? EntityUtils.toString(entity) : null;
/* 283 */       execRuntime.disconnectEndpoint();
/* 284 */       throw new TunnelRefusedException("CONNECT refused by proxy: " + new StatusLine(response), responseMessage);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     return false;
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
/*     */   private boolean createTunnelToProxy(HttpRoute route, int hop, HttpClientContext context) throws HttpException {
/* 313 */     throw new HttpException("Proxy chains are not supported.");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/ConnectExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */