/*     */ package org.apache.hc.client5.http.impl.cookie;
/*     */ 
/*     */ import org.apache.hc.client5.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.hc.client5.http.cookie.Cookie;
/*     */ import org.apache.hc.client5.http.cookie.CookieOrigin;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpec;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.MalformedCookieException;
/*     */ import org.apache.hc.client5.http.psl.PublicSuffixMatcher;
/*     */ import org.apache.hc.client5.http.utils.DateUtils;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public class RFC6265CookieSpecFactory
/*     */   implements CookieSpecFactory
/*     */ {
/*     */   private final CompatibilityLevel compatibilityLevel;
/*     */   private final PublicSuffixMatcher publicSuffixMatcher;
/*     */   private volatile CookieSpec cookieSpec;
/*     */   
/*     */   public enum CompatibilityLevel
/*     */   {
/*  52 */     STRICT,
/*  53 */     RELAXED,
/*  54 */     IE_MEDIUM_SECURITY;
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
/*     */   public RFC6265CookieSpecFactory(CompatibilityLevel compatibilityLevel, PublicSuffixMatcher publicSuffixMatcher) {
/*  66 */     this.compatibilityLevel = (compatibilityLevel != null) ? compatibilityLevel : CompatibilityLevel.RELAXED;
/*  67 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*     */   }
/*     */   
/*     */   public RFC6265CookieSpecFactory(PublicSuffixMatcher publicSuffixMatcher) {
/*  71 */     this(CompatibilityLevel.RELAXED, publicSuffixMatcher);
/*     */   }
/*     */   
/*     */   public RFC6265CookieSpecFactory() {
/*  75 */     this(CompatibilityLevel.RELAXED, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public CookieSpec create(HttpContext context) {
/*  80 */     if (this.cookieSpec == null) {
/*  81 */       synchronized (this) {
/*  82 */         if (this.cookieSpec == null) {
/*  83 */           switch (this.compatibilityLevel) {
/*     */             case STRICT:
/*  85 */               this
/*     */                 
/*  87 */                 .cookieSpec = new RFC6265StrictSpec(new CommonCookieAttributeHandler[] { BasicPathHandler.INSTANCE, PublicSuffixDomainFilter.decorate(BasicDomainHandler.INSTANCE, this.publicSuffixMatcher), BasicMaxAgeHandler.INSTANCE, BasicSecureHandler.INSTANCE, BasicHttpOnlyHandler.INSTANCE, new BasicExpiresHandler(DateUtils.STANDARD_PATTERNS) });
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             case IE_MEDIUM_SECURITY:
/*  95 */               this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 104 */                 .cookieSpec = new RFC6265LaxSpec(new CommonCookieAttributeHandler[] { new BasicPathHandler() { public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {} }, PublicSuffixDomainFilter.decorate(BasicDomainHandler.INSTANCE, this.publicSuffixMatcher), BasicMaxAgeHandler.INSTANCE, BasicSecureHandler.INSTANCE, BasicHttpOnlyHandler.INSTANCE, new BasicExpiresHandler(DateUtils.STANDARD_PATTERNS) });
/*     */               break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             default:
/* 112 */               this
/*     */                 
/* 114 */                 .cookieSpec = new RFC6265LaxSpec(new CommonCookieAttributeHandler[] { BasicPathHandler.INSTANCE, PublicSuffixDomainFilter.decorate(BasicDomainHandler.INSTANCE, this.publicSuffixMatcher), LaxMaxAgeHandler.INSTANCE, BasicSecureHandler.INSTANCE, LaxExpiresHandler.INSTANCE });
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */       } 
/*     */     }
/* 123 */     return this.cookieSpec;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/cookie/RFC6265CookieSpecFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */