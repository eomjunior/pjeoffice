/*     */ package org.apache.hc.client5.http.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.client5.http.AuthenticationStrategy;
/*     */ import org.apache.hc.client5.http.auth.AuthChallenge;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.ChallengeType;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.config.Lookup;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class DefaultAuthenticationStrategy
/*     */   implements AuthenticationStrategy
/*     */ {
/*  62 */   private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticationStrategy.class);
/*     */   
/*  64 */   public static final DefaultAuthenticationStrategy INSTANCE = new DefaultAuthenticationStrategy();
/*     */ 
/*     */   
/*  67 */   private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList(new String[] { "Negotiate", "Kerberos", "NTLM", "Digest", "Basic" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<AuthScheme> select(ChallengeType challengeType, Map<String, AuthChallenge> challenges, HttpContext context) {
/*  79 */     Args.notNull(challengeType, "ChallengeType");
/*  80 */     Args.notNull(challenges, "Map of auth challenges");
/*  81 */     Args.notNull(context, "HTTP context");
/*  82 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*  83 */     String exchangeId = clientContext.getExchangeId();
/*     */     
/*  85 */     List<AuthScheme> options = new ArrayList<>();
/*  86 */     Lookup<AuthSchemeFactory> registry = clientContext.getAuthSchemeRegistry();
/*  87 */     if (registry == null) {
/*  88 */       if (LOG.isDebugEnabled()) {
/*  89 */         LOG.debug("{} Auth scheme registry not set in the context", exchangeId);
/*     */       }
/*  91 */       return options;
/*     */     } 
/*  93 */     RequestConfig config = clientContext.getRequestConfig();
/*     */     
/*  95 */     Collection<String> authPrefs = (challengeType == ChallengeType.TARGET) ? config.getTargetPreferredAuthSchemes() : config.getProxyPreferredAuthSchemes();
/*  96 */     if (authPrefs == null) {
/*  97 */       authPrefs = DEFAULT_SCHEME_PRIORITY;
/*     */     }
/*  99 */     if (LOG.isDebugEnabled()) {
/* 100 */       LOG.debug("{} Authentication schemes in the order of preference: {}", exchangeId, authPrefs);
/*     */     }
/*     */     
/* 103 */     for (String schemeName : authPrefs) {
/* 104 */       AuthChallenge challenge = challenges.get(schemeName.toLowerCase(Locale.ROOT));
/* 105 */       if (challenge != null) {
/* 106 */         AuthSchemeFactory authSchemeFactory = (AuthSchemeFactory)registry.lookup(schemeName);
/* 107 */         if (authSchemeFactory == null) {
/* 108 */           if (LOG.isWarnEnabled()) {
/* 109 */             LOG.warn("{} Authentication scheme {} not supported", exchangeId, schemeName);
/*     */           }
/*     */           
/*     */           continue;
/*     */         } 
/* 114 */         AuthScheme authScheme = authSchemeFactory.create(context);
/* 115 */         options.add(authScheme); continue;
/*     */       } 
/* 117 */       if (LOG.isDebugEnabled()) {
/* 118 */         LOG.debug("{} Challenge for {} authentication scheme not available", exchangeId, schemeName);
/*     */       }
/*     */     } 
/*     */     
/* 122 */     return options;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/DefaultAuthenticationStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */