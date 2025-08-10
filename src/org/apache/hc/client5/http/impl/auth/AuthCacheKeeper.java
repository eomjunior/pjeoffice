/*     */ package org.apache.hc.client5.http.impl.auth;
/*     */ 
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.auth.AuthCache;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthStateCacheable;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*     */ @Internal
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public final class AuthCacheKeeper
/*     */ {
/*  53 */   private static final Logger LOG = LoggerFactory.getLogger(AuthCacheKeeper.class);
/*     */   
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   
/*     */   public AuthCacheKeeper(SchemePortResolver schemePortResolver) {
/*  58 */     this.schemePortResolver = schemePortResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateOnChallenge(HttpHost host, String pathPrefix, AuthExchange authExchange, HttpContext context) {
/*  65 */     clearCache(host, pathPrefix, HttpClientContext.adapt(context));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateOnNoChallenge(HttpHost host, String pathPrefix, AuthExchange authExchange, HttpContext context) {
/*  72 */     if (authExchange.getState() == AuthExchange.State.SUCCESS) {
/*  73 */       updateCache(host, pathPrefix, authExchange.getAuthScheme(), HttpClientContext.adapt(context));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateOnResponse(HttpHost host, String pathPrefix, AuthExchange authExchange, HttpContext context) {
/*  81 */     if (authExchange.getState() == AuthExchange.State.FAILURE) {
/*  82 */       clearCache(host, pathPrefix, HttpClientContext.adapt(context));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadPreemptively(HttpHost host, String pathPrefix, AuthExchange authExchange, HttpContext context) {
/*  90 */     if (authExchange.getState() == AuthExchange.State.UNCHALLENGED) {
/*  91 */       AuthScheme authScheme = loadFromCache(host, pathPrefix, HttpClientContext.adapt(context));
/*  92 */       if (authScheme == null && pathPrefix != null) {
/*  93 */         authScheme = loadFromCache(host, null, HttpClientContext.adapt(context));
/*     */       }
/*  95 */       if (authScheme != null) {
/*  96 */         authExchange.select(authScheme);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AuthScheme loadFromCache(HttpHost host, String pathPrefix, HttpClientContext clientContext) {
/* 104 */     AuthCache authCache = clientContext.getAuthCache();
/* 105 */     if (authCache != null) {
/* 106 */       AuthScheme authScheme = authCache.get(host, pathPrefix);
/* 107 */       if (authScheme != null) {
/* 108 */         if (LOG.isDebugEnabled()) {
/* 109 */           String exchangeId = clientContext.getExchangeId();
/* 110 */           LOG.debug("{} Re-using cached '{}' auth scheme for {}{}", new Object[] { exchangeId, authScheme.getName(), host, (pathPrefix != null) ? pathPrefix : "" });
/*     */         } 
/*     */         
/* 113 */         return authScheme;
/*     */       } 
/*     */     } 
/* 116 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateCache(HttpHost host, String pathPrefix, AuthScheme authScheme, HttpClientContext clientContext) {
/* 123 */     boolean cacheable = (authScheme.getClass().getAnnotation(AuthStateCacheable.class) != null);
/* 124 */     if (cacheable) {
/* 125 */       AuthCache authCache = clientContext.getAuthCache();
/* 126 */       if (authCache == null) {
/* 127 */         authCache = new BasicAuthCache(this.schemePortResolver);
/* 128 */         clientContext.setAuthCache(authCache);
/*     */       } 
/* 130 */       if (LOG.isDebugEnabled()) {
/* 131 */         String exchangeId = clientContext.getExchangeId();
/* 132 */         LOG.debug("{} Caching '{}' auth scheme for {}{}", new Object[] { exchangeId, authScheme.getName(), host, (pathPrefix != null) ? pathPrefix : "" });
/*     */       } 
/*     */       
/* 135 */       authCache.put(host, pathPrefix, authScheme);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void clearCache(HttpHost host, String pathPrefix, HttpClientContext clientContext) {
/* 142 */     AuthCache authCache = clientContext.getAuthCache();
/* 143 */     if (authCache != null) {
/* 144 */       if (LOG.isDebugEnabled()) {
/* 145 */         String exchangeId = clientContext.getExchangeId();
/* 146 */         LOG.debug("{} Clearing cached auth scheme for {}{}", new Object[] { exchangeId, host, (pathPrefix != null) ? pathPrefix : "" });
/*     */       } 
/*     */       
/* 149 */       authCache.remove(host, pathPrefix);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/auth/AuthCacheKeeper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */