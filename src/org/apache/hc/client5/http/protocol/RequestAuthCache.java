/*     */ package org.apache.hc.client5.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.client5.http.RouteInfo;
/*     */ import org.apache.hc.client5.http.auth.AuthCache;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.impl.RequestSupport;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
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
/*     */ @Deprecated
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class RequestAuthCache
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  63 */   private static final Logger LOG = LoggerFactory.getLogger(RequestAuthCache.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/*  72 */     Args.notNull(request, "HTTP request");
/*  73 */     Args.notNull(context, "HTTP context");
/*     */     
/*  75 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*  76 */     String exchangeId = clientContext.getExchangeId();
/*     */     
/*  78 */     AuthCache authCache = clientContext.getAuthCache();
/*  79 */     if (authCache == null) {
/*  80 */       if (LOG.isDebugEnabled()) {
/*  81 */         LOG.debug("{} Auth cache not set in the context", exchangeId);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  86 */     CredentialsProvider credsProvider = clientContext.getCredentialsProvider();
/*  87 */     if (credsProvider == null) {
/*  88 */       if (LOG.isDebugEnabled()) {
/*  89 */         LOG.debug("{} Credentials provider not set in the context", exchangeId);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  94 */     RouteInfo route = clientContext.getHttpRoute();
/*  95 */     if (route == null) {
/*  96 */       if (LOG.isDebugEnabled()) {
/*  97 */         LOG.debug("{} Route info not set in the context", exchangeId);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 102 */     HttpHost target = new HttpHost(request.getScheme(), (NamedEndpoint)request.getAuthority());
/* 103 */     AuthExchange targetAuthExchange = clientContext.getAuthExchange(target);
/* 104 */     if (targetAuthExchange.getState() == AuthExchange.State.UNCHALLENGED) {
/* 105 */       String pathPrefix = RequestSupport.extractPathPrefix(request);
/* 106 */       AuthScheme authScheme = authCache.get(target, pathPrefix);
/* 107 */       if (authScheme != null) {
/* 108 */         if (LOG.isDebugEnabled()) {
/* 109 */           LOG.debug("{} Re-using cached '{}' auth scheme for {}", new Object[] { exchangeId, authScheme.getName(), target });
/*     */         }
/* 111 */         targetAuthExchange.select(authScheme);
/*     */       } 
/*     */     } 
/*     */     
/* 115 */     HttpHost proxy = route.getProxyHost();
/* 116 */     if (proxy != null) {
/* 117 */       AuthExchange proxyAuthExchange = clientContext.getAuthExchange(proxy);
/* 118 */       if (proxyAuthExchange.getState() == AuthExchange.State.UNCHALLENGED) {
/* 119 */         AuthScheme authScheme = authCache.get(proxy, null);
/* 120 */         if (authScheme != null) {
/* 121 */           if (LOG.isDebugEnabled()) {
/* 122 */             LOG.debug("{} Re-using cached '{}' auth scheme for {}", new Object[] { exchangeId, authScheme.getName(), proxy });
/*     */           }
/* 124 */           proxyAuthExchange.select(authScheme);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/protocol/RequestAuthCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */