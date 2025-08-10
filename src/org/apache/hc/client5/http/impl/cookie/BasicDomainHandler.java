/*     */ package org.apache.hc.client5.http.impl.cookie;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.hc.client5.http.cookie.Cookie;
/*     */ import org.apache.hc.client5.http.cookie.CookieOrigin;
/*     */ import org.apache.hc.client5.http.cookie.CookieRestrictionViolationException;
/*     */ import org.apache.hc.client5.http.cookie.MalformedCookieException;
/*     */ import org.apache.hc.client5.http.cookie.SetCookie;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.net.InetAddressUtils;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class BasicDomainHandler
/*     */   implements CommonCookieAttributeHandler
/*     */ {
/*  57 */   public static final BasicDomainHandler INSTANCE = new BasicDomainHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(SetCookie cookie, String value) throws MalformedCookieException {
/*  66 */     Args.notNull(cookie, "Cookie");
/*  67 */     if (TextUtils.isBlank(value)) {
/*  68 */       throw new MalformedCookieException("Blank or null value for domain attribute");
/*     */     }
/*     */     
/*  71 */     if (value.endsWith(".")) {
/*     */       return;
/*     */     }
/*  74 */     String domain = value;
/*  75 */     if (domain.startsWith(".")) {
/*  76 */       domain = domain.substring(1);
/*     */     }
/*  78 */     domain = domain.toLowerCase(Locale.ROOT);
/*  79 */     cookie.setDomain(domain);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/*  85 */     Args.notNull(cookie, "Cookie");
/*  86 */     Args.notNull(origin, "Cookie origin");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  92 */     String host = origin.getHost();
/*  93 */     String domain = cookie.getDomain();
/*  94 */     if (domain == null) {
/*  95 */       throw new CookieRestrictionViolationException("Cookie 'domain' may not be null");
/*     */     }
/*  97 */     if (!host.equals(domain) && !domainMatch(domain, host)) {
/*  98 */       throw new CookieRestrictionViolationException("Illegal 'domain' attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean domainMatch(String domain, String host) {
/* 104 */     if (InetAddressUtils.isIPv4Address(host) || InetAddressUtils.isIPv6Address(host)) {
/* 105 */       return false;
/*     */     }
/* 107 */     String normalizedDomain = domain.startsWith(".") ? domain.substring(1) : domain;
/* 108 */     if (host.endsWith(normalizedDomain)) {
/* 109 */       int prefix = host.length() - normalizedDomain.length();
/*     */       
/* 111 */       if (prefix == 0) {
/* 112 */         return true;
/*     */       }
/* 114 */       return (prefix > 1 && host.charAt(prefix - 1) == '.');
/*     */     } 
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(Cookie cookie, CookieOrigin origin) {
/* 121 */     Args.notNull(cookie, "Cookie");
/* 122 */     Args.notNull(origin, "Cookie origin");
/* 123 */     String host = origin.getHost();
/* 124 */     String domain = cookie.getDomain();
/* 125 */     if (domain == null) {
/* 126 */       return false;
/*     */     }
/* 128 */     if (domain.startsWith(".")) {
/* 129 */       domain = domain.substring(1);
/*     */     }
/* 131 */     domain = domain.toLowerCase(Locale.ROOT);
/* 132 */     if (host.equals(domain)) {
/* 133 */       return true;
/*     */     }
/* 135 */     if (cookie.containsAttribute("domain")) {
/* 136 */       return domainMatch(domain, host);
/*     */     }
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getAttributeName() {
/* 143 */     return "domain";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/BasicDomainHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */