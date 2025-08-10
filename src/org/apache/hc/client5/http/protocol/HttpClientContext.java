/*     */ package org.apache.hc.client5.http.protocol;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.RouteInfo;
/*     */ import org.apache.hc.client5.http.auth.AuthCache;
/*     */ import org.apache.hc.client5.http.auth.AuthExchange;
/*     */ import org.apache.hc.client5.http.auth.AuthScheme;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.cookie.CookieOrigin;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpec;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.protocol.BasicHttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpClientContext
/*     */   extends HttpCoreContext
/*     */ {
/*     */   public static final String HTTP_ROUTE = "http.route";
/*     */   public static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
/*     */   public static final String COOKIESPEC_REGISTRY = "http.cookiespec-registry";
/*     */   public static final String COOKIE_SPEC = "http.cookie-spec";
/*     */   public static final String COOKIE_ORIGIN = "http.cookie-origin";
/*     */   public static final String COOKIE_STORE = "http.cookie-store";
/*     */   public static final String CREDS_PROVIDER = "http.auth.credentials-provider";
/*     */   public static final String AUTH_CACHE = "http.auth.auth-cache";
/*     */   public static final String AUTH_EXCHANGE_MAP = "http.auth.exchanges";
/*     */   public static final String USER_TOKEN = "http.user-token";
/*     */   public static final String AUTHSCHEME_REGISTRY = "http.authscheme-registry";
/*     */   public static final String REQUEST_CONFIG = "http.request-config";
/*     */   public static final String EXCHANGE_ID = "http.exchange-id";
/*     */   
/*     */   public static HttpClientContext adapt(HttpContext context) {
/* 140 */     Args.notNull(context, "HTTP context");
/* 141 */     if (context instanceof HttpClientContext) {
/* 142 */       return (HttpClientContext)context;
/*     */     }
/* 144 */     return new HttpClientContext(context);
/*     */   }
/*     */   
/*     */   public static HttpClientContext create() {
/* 148 */     return new HttpClientContext((HttpContext)new BasicHttpContext());
/*     */   }
/*     */   
/*     */   public HttpClientContext(HttpContext context) {
/* 152 */     super(context);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpClientContext() {}
/*     */ 
/*     */   
/*     */   public RouteInfo getHttpRoute() {
/* 160 */     return (RouteInfo)getAttribute("http.route", HttpRoute.class);
/*     */   }
/*     */   
/*     */   public RedirectLocations getRedirectLocations() {
/* 164 */     return (RedirectLocations)getAttribute("http.protocol.redirect-locations", RedirectLocations.class);
/*     */   }
/*     */   
/*     */   public CookieStore getCookieStore() {
/* 168 */     return (CookieStore)getAttribute("http.cookie-store", CookieStore.class);
/*     */   }
/*     */   
/*     */   public void setCookieStore(CookieStore cookieStore) {
/* 172 */     setAttribute("http.cookie-store", cookieStore);
/*     */   }
/*     */   
/*     */   public CookieSpec getCookieSpec() {
/* 176 */     return (CookieSpec)getAttribute("http.cookie-spec", CookieSpec.class);
/*     */   }
/*     */   
/*     */   public CookieOrigin getCookieOrigin() {
/* 180 */     return (CookieOrigin)getAttribute("http.cookie-origin", CookieOrigin.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Lookup<T> getLookup(String name) {
/* 185 */     return (Lookup<T>)getAttribute(name, Lookup.class);
/*     */   }
/*     */   
/*     */   public Lookup<CookieSpecFactory> getCookieSpecRegistry() {
/* 189 */     return getLookup("http.cookiespec-registry");
/*     */   }
/*     */   
/*     */   public void setCookieSpecRegistry(Lookup<CookieSpecFactory> lookup) {
/* 193 */     setAttribute("http.cookiespec-registry", lookup);
/*     */   }
/*     */   
/*     */   public Lookup<AuthSchemeFactory> getAuthSchemeRegistry() {
/* 197 */     return getLookup("http.authscheme-registry");
/*     */   }
/*     */   
/*     */   public void setAuthSchemeRegistry(Lookup<AuthSchemeFactory> lookup) {
/* 201 */     setAttribute("http.authscheme-registry", lookup);
/*     */   }
/*     */   
/*     */   public CredentialsProvider getCredentialsProvider() {
/* 205 */     return (CredentialsProvider)getAttribute("http.auth.credentials-provider", CredentialsProvider.class);
/*     */   }
/*     */   
/*     */   public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
/* 209 */     setAttribute("http.auth.credentials-provider", credentialsProvider);
/*     */   }
/*     */   
/*     */   public AuthCache getAuthCache() {
/* 213 */     return (AuthCache)getAttribute("http.auth.auth-cache", AuthCache.class);
/*     */   }
/*     */   
/*     */   public void setAuthCache(AuthCache authCache) {
/* 217 */     setAttribute("http.auth.auth-cache", authCache);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<HttpHost, AuthExchange> getAuthExchanges() {
/* 225 */     Map<HttpHost, AuthExchange> map = (Map<HttpHost, AuthExchange>)getAttribute("http.auth.exchanges");
/* 226 */     if (map == null) {
/* 227 */       map = new HashMap<>();
/* 228 */       setAttribute("http.auth.exchanges", map);
/*     */     } 
/* 230 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AuthExchange getAuthExchange(HttpHost host) {
/* 237 */     Map<HttpHost, AuthExchange> authExchangeMap = getAuthExchanges();
/* 238 */     AuthExchange authExchange = authExchangeMap.get(host);
/* 239 */     if (authExchange == null) {
/* 240 */       authExchange = new AuthExchange();
/* 241 */       authExchangeMap.put(host, authExchange);
/*     */     } 
/* 243 */     return authExchange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAuthExchange(HttpHost host, AuthExchange authExchange) {
/* 250 */     Map<HttpHost, AuthExchange> authExchangeMap = getAuthExchanges();
/* 251 */     authExchangeMap.put(host, authExchange);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetAuthExchange(HttpHost host, AuthScheme authScheme) {
/* 258 */     AuthExchange authExchange = new AuthExchange();
/* 259 */     authExchange.select(authScheme);
/* 260 */     Map<HttpHost, AuthExchange> authExchangeMap = getAuthExchanges();
/* 261 */     authExchangeMap.put(host, authExchange);
/*     */   }
/*     */   
/*     */   public <T> T getUserToken(Class<T> clazz) {
/* 265 */     return (T)getAttribute("http.user-token", clazz);
/*     */   }
/*     */   
/*     */   public Object getUserToken() {
/* 269 */     return getAttribute("http.user-token");
/*     */   }
/*     */   
/*     */   public void setUserToken(Object obj) {
/* 273 */     setAttribute("http.user-token", obj);
/*     */   }
/*     */   
/*     */   public RequestConfig getRequestConfig() {
/* 277 */     RequestConfig config = (RequestConfig)getAttribute("http.request-config", RequestConfig.class);
/* 278 */     return (config != null) ? config : RequestConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   public void setRequestConfig(RequestConfig config) {
/* 282 */     setAttribute("http.request-config", config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExchangeId() {
/* 289 */     return (String)getAttribute("http.exchange-id", String.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExchangeId(String id) {
/* 296 */     setAttribute("http.exchange-id", id);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/protocol/HttpClientContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */