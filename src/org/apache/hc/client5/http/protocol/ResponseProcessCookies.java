/*     */ package org.apache.hc.client5.http.protocol;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.hc.client5.http.cookie.Cookie;
/*     */ import org.apache.hc.client5.http.cookie.CookieOrigin;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpec;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.client5.http.cookie.MalformedCookieException;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpResponseInterceptor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public class ResponseProcessCookies
/*     */   implements HttpResponseInterceptor
/*     */ {
/*  66 */   public static final ResponseProcessCookies INSTANCE = new ResponseProcessCookies();
/*     */   
/*  68 */   private static final Logger LOG = LoggerFactory.getLogger(ResponseProcessCookies.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void process(HttpResponse response, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/*  77 */     Args.notNull(response, "HTTP request");
/*  78 */     Args.notNull(context, "HTTP context");
/*     */     
/*  80 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/*  81 */     String exchangeId = clientContext.getExchangeId();
/*     */ 
/*     */     
/*  84 */     CookieSpec cookieSpec = clientContext.getCookieSpec();
/*  85 */     if (cookieSpec == null) {
/*  86 */       if (LOG.isDebugEnabled()) {
/*  87 */         LOG.debug("{} Cookie spec not specified in HTTP context", exchangeId);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  92 */     CookieStore cookieStore = clientContext.getCookieStore();
/*  93 */     if (cookieStore == null) {
/*  94 */       if (LOG.isDebugEnabled()) {
/*  95 */         LOG.debug("{} Cookie store not specified in HTTP context", exchangeId);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 100 */     CookieOrigin cookieOrigin = clientContext.getCookieOrigin();
/* 101 */     if (cookieOrigin == null) {
/* 102 */       if (LOG.isDebugEnabled()) {
/* 103 */         LOG.debug("{} Cookie origin not specified in HTTP context", exchangeId);
/*     */       }
/*     */       return;
/*     */     } 
/* 107 */     Iterator<Header> it = response.headerIterator("Set-Cookie");
/* 108 */     processCookies(exchangeId, it, cookieSpec, cookieOrigin, cookieStore);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processCookies(String exchangeId, Iterator<Header> iterator, CookieSpec cookieSpec, CookieOrigin cookieOrigin, CookieStore cookieStore) {
/* 117 */     while (iterator.hasNext()) {
/* 118 */       Header header = iterator.next();
/*     */       try {
/* 120 */         List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);
/* 121 */         for (Cookie cookie : cookies) {
/*     */           try {
/* 123 */             cookieSpec.validate(cookie, cookieOrigin);
/* 124 */             cookieStore.addCookie(cookie);
/*     */             
/* 126 */             if (LOG.isDebugEnabled()) {
/* 127 */               LOG.debug("{} Cookie accepted [{}]", exchangeId, formatCookie(cookie));
/*     */             }
/* 129 */           } catch (MalformedCookieException ex) {
/* 130 */             if (LOG.isWarnEnabled()) {
/* 131 */               LOG.warn("{} Cookie rejected [{}] {}", new Object[] { exchangeId, formatCookie(cookie), ex.getMessage() });
/*     */             }
/*     */           } 
/*     */         } 
/* 135 */       } catch (MalformedCookieException ex) {
/* 136 */         if (LOG.isWarnEnabled()) {
/* 137 */           LOG.warn("{} Invalid cookie header: \"{}\". {}", new Object[] { exchangeId, header, ex.getMessage() });
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String formatCookie(Cookie cookie) {
/* 144 */     StringBuilder buf = new StringBuilder();
/* 145 */     buf.append(cookie.getName());
/* 146 */     buf.append("=\"");
/* 147 */     String v = cookie.getValue();
/* 148 */     if (v != null) {
/* 149 */       if (v.length() > 100) {
/* 150 */         v = v.substring(0, 100) + "...";
/*     */       }
/* 152 */       buf.append(v);
/*     */     } 
/* 154 */     buf.append("\"");
/* 155 */     buf.append(", domain:");
/* 156 */     buf.append(cookie.getDomain());
/* 157 */     buf.append(", path:");
/* 158 */     buf.append(cookie.getPath());
/* 159 */     buf.append(", expiry:");
/* 160 */     buf.append(cookie.getExpiryInstant());
/* 161 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/protocol/ResponseProcessCookies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */