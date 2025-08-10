/*     */ package org.apache.hc.client5.http.impl;
/*     */ 
/*     */ import java.security.Principal;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.UserTokenHandler;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultUserTokenHandler
/*     */   implements UserTokenHandler
/*     */ {
/*  62 */   public static final DefaultUserTokenHandler INSTANCE = new DefaultUserTokenHandler();
/*     */ 
/*     */   
/*     */   public Object getUserToken(HttpRoute route, HttpContext context) {
/*  66 */     return getUserToken(route, null, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getUserToken(HttpRoute route, HttpRequest request, HttpContext context) {
/*  72 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*     */     
/*  74 */     HttpHost target = (request != null) ? new HttpHost(request.getScheme(), (NamedEndpoint)request.getAuthority()) : route.getTargetHost();
/*     */     
/*  76 */     AuthExchange targetAuthExchange = clientContext.getAuthExchange(target);
/*  77 */     if (targetAuthExchange != null) {
/*  78 */       Principal authPrincipal = getAuthPrincipal(targetAuthExchange);
/*  79 */       if (authPrincipal != null) {
/*  80 */         return authPrincipal;
/*     */       }
/*     */     } 
/*  83 */     HttpHost proxy = route.getProxyHost();
/*  84 */     if (proxy != null) {
/*  85 */       AuthExchange proxyAuthExchange = clientContext.getAuthExchange(proxy);
/*  86 */       if (proxyAuthExchange != null) {
/*  87 */         Principal authPrincipal = getAuthPrincipal(proxyAuthExchange);
/*  88 */         if (authPrincipal != null) {
/*  89 */           return authPrincipal;
/*     */         }
/*     */       } 
/*     */     } 
/*  93 */     SSLSession sslSession = clientContext.getSSLSession();
/*  94 */     if (sslSession != null) {
/*  95 */       return sslSession.getLocalPrincipal();
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */   
/*     */   private static Principal getAuthPrincipal(AuthExchange authExchange) {
/* 101 */     AuthScheme scheme = authExchange.getAuthScheme();
/* 102 */     if (scheme != null && scheme.isConnectionBased()) {
/* 103 */       return scheme.getPrincipal();
/*     */     }
/* 105 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/DefaultUserTokenHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */