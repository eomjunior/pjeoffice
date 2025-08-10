/*     */ package org.apache.hc.client5.http.fluent;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URISyntaxException;
/*     */ import org.apache.hc.client5.http.auth.AuthCache;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthScope;
/*     */ import org.apache.hc.client5.http.auth.Credentials;
/*     */ import org.apache.hc.client5.http.auth.CredentialsStore;
/*     */ import org.apache.hc.client5.http.auth.NTCredentials;
/*     */ import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
/*     */ import org.apache.hc.client5.http.impl.auth.BasicScheme;
/*     */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
/*     */ import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
/*     */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Executor
/*     */ {
/*  63 */   static final CloseableHttpClient CLIENT = HttpClientBuilder.create()
/*  64 */     .setConnectionManager((HttpClientConnectionManager)PoolingHttpClientConnectionManagerBuilder.create()
/*  65 */       .useSystemProperties()
/*  66 */       .setMaxConnPerRoute(100)
/*  67 */       .setMaxConnTotal(200)
/*  68 */       .setDefaultConnectionConfig(ConnectionConfig.custom()
/*  69 */         .setValidateAfterInactivity(TimeValue.ofSeconds(10L))
/*  70 */         .build())
/*  71 */       .build())
/*  72 */     .useSystemProperties()
/*  73 */     .evictExpiredConnections()
/*  74 */     .evictIdleConnections(TimeValue.ofMinutes(1L))
/*  75 */     .build(); private final CloseableHttpClient httpclient;
/*     */   private final AuthCache authCache;
/*     */   
/*     */   public static Executor newInstance() {
/*  79 */     return new Executor(CLIENT);
/*     */   }
/*     */   private volatile CredentialsStore credentialsStore; private volatile CookieStore cookieStore;
/*     */   public static Executor newInstance(CloseableHttpClient httpclient) {
/*  83 */     return new Executor((httpclient != null) ? httpclient : CLIENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Executor(CloseableHttpClient httpclient) {
/*  93 */     this.httpclient = httpclient;
/*  94 */     this.authCache = (AuthCache)new BasicAuthCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor use(CredentialsStore credentialsStore) {
/* 101 */     this.credentialsStore = credentialsStore;
/* 102 */     return this;
/*     */   }
/*     */   public Executor auth(AuthScope authScope, Credentials credentials) {
/*     */     BasicCredentialsProvider basicCredentialsProvider;
/* 106 */     CredentialsStore credentialsStoreSnapshot = this.credentialsStore;
/* 107 */     if (credentialsStoreSnapshot == null) {
/* 108 */       basicCredentialsProvider = new BasicCredentialsProvider();
/* 109 */       this.credentialsStore = (CredentialsStore)basicCredentialsProvider;
/*     */     } 
/* 111 */     basicCredentialsProvider.setCredentials(authScope, credentials);
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public Executor auth(HttpHost host, Credentials credentials) {
/* 116 */     return auth(new AuthScope(host), credentials);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor auth(String host, Credentials credentials) {
/*     */     HttpHost httpHost;
/*     */     try {
/* 125 */       httpHost = HttpHost.create(host);
/* 126 */     } catch (URISyntaxException ex) {
/* 127 */       throw new IllegalArgumentException("Invalid host: " + host);
/*     */     } 
/* 129 */     return auth(httpHost, credentials);
/*     */   }
/*     */   
/*     */   public Executor authPreemptive(HttpHost host) {
/* 133 */     CredentialsStore credentialsStoreSnapshot = this.credentialsStore;
/* 134 */     if (credentialsStoreSnapshot != null) {
/* 135 */       Credentials credentials = credentialsStoreSnapshot.getCredentials(new AuthScope(host), null);
/* 136 */       if (credentials != null) {
/* 137 */         BasicScheme basicScheme = new BasicScheme();
/* 138 */         basicScheme.initPreemptive(credentials);
/* 139 */         this.authCache.put(host, (AuthScheme)basicScheme);
/*     */       } 
/*     */     } 
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor authPreemptive(String host) {
/*     */     HttpHost httpHost;
/*     */     try {
/* 151 */       httpHost = HttpHost.create(host);
/* 152 */     } catch (URISyntaxException ex) {
/* 153 */       throw new IllegalArgumentException("Invalid host: " + host);
/*     */     } 
/* 155 */     return authPreemptive(httpHost);
/*     */   }
/*     */   
/*     */   public Executor authPreemptiveProxy(HttpHost proxy) {
/* 159 */     CredentialsStore credentialsStoreSnapshot = this.credentialsStore;
/* 160 */     if (credentialsStoreSnapshot != null) {
/* 161 */       Credentials credentials = credentialsStoreSnapshot.getCredentials(new AuthScope(proxy), null);
/* 162 */       if (credentials != null) {
/* 163 */         BasicScheme basicScheme = new BasicScheme();
/* 164 */         basicScheme.initPreemptive(credentials);
/* 165 */         this.authCache.put(proxy, (AuthScheme)basicScheme);
/*     */       } 
/*     */     } 
/* 168 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor authPreemptiveProxy(String proxy) {
/*     */     HttpHost httpHost;
/*     */     try {
/* 177 */       httpHost = HttpHost.create(proxy);
/* 178 */     } catch (URISyntaxException ex) {
/* 179 */       throw new IllegalArgumentException("Invalid host: " + proxy);
/*     */     } 
/* 181 */     return authPreemptiveProxy(httpHost);
/*     */   }
/*     */ 
/*     */   
/*     */   public Executor auth(HttpHost host, String username, char[] password) {
/* 186 */     return auth(host, (Credentials)new UsernamePasswordCredentials(username, password));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor auth(HttpHost host, String username, char[] password, String workstation, String domain) {
/* 192 */     return auth(host, (Credentials)new NTCredentials(username, password, workstation, domain));
/*     */   }
/*     */   
/*     */   public Executor clearAuth() {
/* 196 */     CredentialsStore credentialsStoreSnapshot = this.credentialsStore;
/* 197 */     if (credentialsStoreSnapshot != null) {
/* 198 */       credentialsStoreSnapshot.clear();
/*     */     }
/* 200 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Executor use(CookieStore cookieStore) {
/* 207 */     this.cookieStore = cookieStore;
/* 208 */     return this;
/*     */   }
/*     */   
/*     */   public Executor clearCookies() {
/* 212 */     CookieStore cookieStoreSnapshot = this.cookieStore;
/* 213 */     if (cookieStoreSnapshot != null) {
/* 214 */       cookieStoreSnapshot.clear();
/*     */     }
/* 216 */     return this;
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
/*     */   public Response execute(Request request) throws IOException {
/* 229 */     HttpClientContext localContext = HttpClientContext.create();
/* 230 */     CredentialsStore credentialsStoreSnapshot = this.credentialsStore;
/* 231 */     if (credentialsStoreSnapshot != null) {
/* 232 */       localContext.setAttribute("http.auth.credentials-provider", credentialsStoreSnapshot);
/*     */     }
/* 234 */     if (this.authCache != null) {
/* 235 */       localContext.setAttribute("http.auth.auth-cache", this.authCache);
/*     */     }
/* 237 */     CookieStore cookieStoreSnapshot = this.cookieStore;
/* 238 */     if (cookieStoreSnapshot != null) {
/* 239 */       localContext.setAttribute("http.cookie-store", cookieStoreSnapshot);
/*     */     }
/* 241 */     return new Response(request.internalExecute(this.httpclient, localContext));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/fluent/Executor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */