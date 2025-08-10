/*     */ package org.apache.hc.client5.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.hc.client5.http.RouteInfo;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.cookie.Cookie;
/*     */ import org.apache.hc.client5.http.cookie.CookieOrigin;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpec;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TextUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class RequestAddCookies
/*     */   implements HttpRequestInterceptor
/*     */ {
/*  74 */   public static final RequestAddCookies INSTANCE = new RequestAddCookies();
/*     */   
/*  76 */   private static final Logger LOG = LoggerFactory.getLogger(RequestAddCookies.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/*  85 */     Args.notNull(request, "HTTP request");
/*  86 */     Args.notNull(context, "HTTP context");
/*     */     
/*  88 */     String method = request.getMethod();
/*  89 */     if (Method.CONNECT.isSame(method) || Method.TRACE.isSame(method)) {
/*     */       return;
/*     */     }
/*     */     
/*  93 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*  94 */     String exchangeId = clientContext.getExchangeId();
/*     */ 
/*     */     
/*  97 */     CookieStore cookieStore = clientContext.getCookieStore();
/*  98 */     if (cookieStore == null) {
/*  99 */       if (LOG.isDebugEnabled()) {
/* 100 */         LOG.debug("{} Cookie store not specified in HTTP context", exchangeId);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 106 */     Lookup<CookieSpecFactory> registry = clientContext.getCookieSpecRegistry();
/* 107 */     if (registry == null) {
/* 108 */       if (LOG.isDebugEnabled()) {
/* 109 */         LOG.debug("{} CookieSpec registry not specified in HTTP context", exchangeId);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 115 */     RouteInfo route = clientContext.getHttpRoute();
/* 116 */     if (route == null) {
/* 117 */       if (LOG.isDebugEnabled()) {
/* 118 */         LOG.debug("{} Connection route not set in the context", exchangeId);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 123 */     RequestConfig config = clientContext.getRequestConfig();
/* 124 */     String cookieSpecName = config.getCookieSpec();
/* 125 */     if (cookieSpecName == null) {
/* 126 */       cookieSpecName = "strict";
/*     */     }
/* 128 */     if (LOG.isDebugEnabled()) {
/* 129 */       LOG.debug("{} Cookie spec selected: {}", exchangeId, cookieSpecName);
/*     */     }
/*     */     
/* 132 */     URIAuthority authority = request.getAuthority();
/* 133 */     String path = request.getPath();
/* 134 */     if (TextUtils.isEmpty(path)) {
/* 135 */       path = "/";
/*     */     }
/* 137 */     String hostName = (authority != null) ? authority.getHostName() : null;
/* 138 */     if (hostName == null) {
/* 139 */       hostName = route.getTargetHost().getHostName();
/*     */     }
/* 141 */     int port = (authority != null) ? authority.getPort() : -1;
/* 142 */     if (port < 0) {
/* 143 */       port = route.getTargetHost().getPort();
/*     */     }
/* 145 */     CookieOrigin cookieOrigin = new CookieOrigin(hostName, port, path, route.isSecure());
/*     */ 
/*     */     
/* 148 */     CookieSpecFactory factory = (CookieSpecFactory)registry.lookup(cookieSpecName);
/* 149 */     if (factory == null) {
/* 150 */       if (LOG.isDebugEnabled()) {
/* 151 */         LOG.debug("{} Unsupported cookie spec: {}", exchangeId, cookieSpecName);
/*     */       }
/*     */       return;
/*     */     } 
/* 155 */     CookieSpec cookieSpec = factory.create((HttpContext)clientContext);
/*     */     
/* 157 */     List<Cookie> cookies = cookieStore.getCookies();
/*     */     
/* 159 */     List<Cookie> matchedCookies = new ArrayList<>();
/* 160 */     Instant now = Instant.now();
/* 161 */     boolean expired = false;
/* 162 */     for (Cookie cookie : cookies) {
/* 163 */       if (!cookie.isExpired(now)) {
/* 164 */         if (cookieSpec.match(cookie, cookieOrigin)) {
/* 165 */           if (LOG.isDebugEnabled()) {
/* 166 */             LOG.debug("{} Cookie {} match {}", new Object[] { exchangeId, cookie, cookieOrigin });
/*     */           }
/* 168 */           matchedCookies.add(cookie);
/*     */         }  continue;
/*     */       } 
/* 171 */       if (LOG.isDebugEnabled()) {
/* 172 */         LOG.debug("{} Cookie {} expired", exchangeId, cookie);
/*     */       }
/* 174 */       expired = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 180 */     if (expired) {
/* 181 */       cookieStore.clearExpired(now);
/*     */     }
/*     */     
/* 184 */     if (!matchedCookies.isEmpty()) {
/* 185 */       List<Header> headers = cookieSpec.formatCookies(matchedCookies);
/* 186 */       for (Header header : headers) {
/* 187 */         request.addHeader(header);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 193 */     context.setAttribute("http.cookie-spec", cookieSpec);
/* 194 */     context.setAttribute("http.cookie-origin", cookieOrigin);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/protocol/RequestAddCookies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */