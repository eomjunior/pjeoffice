/*     */ package org.apache.hc.client5.http;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.client5.http.auth.AuthCache;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.Credentials;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicScheme;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.routing.RoutingSupport;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.protocol.BasicHttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextBuilder
/*     */ {
/*     */   private final SchemePortResolver schemePortResolver;
/*     */   private Lookup<CookieSpecFactory> cookieSpecRegistry;
/*     */   private Lookup<AuthSchemeFactory> authSchemeRegistry;
/*     */   private CookieStore cookieStore;
/*     */   private CredentialsProvider credentialsProvider;
/*     */   private AuthCache authCache;
/*     */   private Map<HttpHost, AuthScheme> authSchemeMap;
/*     */   
/*     */   ContextBuilder(SchemePortResolver schemePortResolver) {
/*  67 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/*     */   }
/*     */   
/*     */   public static ContextBuilder create(SchemePortResolver schemePortResolver) {
/*  71 */     return new ContextBuilder(schemePortResolver);
/*     */   }
/*     */   
/*     */   public static ContextBuilder create() {
/*  75 */     return new ContextBuilder((SchemePortResolver)DefaultSchemePortResolver.INSTANCE);
/*     */   }
/*     */   
/*     */   public ContextBuilder useCookieSpecRegistry(Lookup<CookieSpecFactory> cookieSpecRegistry) {
/*  79 */     this.cookieSpecRegistry = cookieSpecRegistry;
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public ContextBuilder useAuthSchemeRegistry(Lookup<AuthSchemeFactory> authSchemeRegistry) {
/*  84 */     this.authSchemeRegistry = authSchemeRegistry;
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public ContextBuilder useCookieStore(CookieStore cookieStore) {
/*  89 */     this.cookieStore = cookieStore;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public ContextBuilder useCredentialsProvider(CredentialsProvider credentialsProvider) {
/*  94 */     this.credentialsProvider = credentialsProvider;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public ContextBuilder useAuthCache(AuthCache authCache) {
/*  99 */     this.authCache = authCache;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public ContextBuilder preemptiveAuth(HttpHost host, AuthScheme authScheme) {
/* 104 */     Args.notNull(host, "HTTP host");
/* 105 */     if (this.authSchemeMap == null) {
/* 106 */       this.authSchemeMap = new HashMap<>();
/*     */     }
/* 108 */     this.authSchemeMap.put(RoutingSupport.normalize(host, this.schemePortResolver), authScheme);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public ContextBuilder preemptiveBasicAuth(HttpHost host, UsernamePasswordCredentials credentials) {
/* 113 */     Args.notNull(host, "HTTP host");
/* 114 */     BasicScheme authScheme = new BasicScheme(StandardCharsets.UTF_8);
/* 115 */     authScheme.initPreemptive((Credentials)credentials);
/* 116 */     preemptiveAuth(host, (AuthScheme)authScheme);
/* 117 */     return this;
/*     */   }
/*     */   public HttpClientContext build() {
/* 120 */     HttpClientContext context = new HttpClientContext((HttpContext)new BasicHttpContext());
/* 121 */     context.setCookieSpecRegistry(this.cookieSpecRegistry);
/* 122 */     context.setAuthSchemeRegistry(this.authSchemeRegistry);
/* 123 */     context.setCookieStore(this.cookieStore);
/* 124 */     context.setCredentialsProvider(this.credentialsProvider);
/* 125 */     context.setAuthCache(this.authCache);
/* 126 */     if (this.authSchemeMap != null) {
/* 127 */       for (Map.Entry<HttpHost, AuthScheme> entry : this.authSchemeMap.entrySet()) {
/* 128 */         context.resetAuthExchange(entry.getKey(), entry.getValue());
/*     */       }
/*     */     }
/* 131 */     return context;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/ContextBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */