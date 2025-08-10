/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*    */ import org.apache.hc.client5.http.impl.cookie.IgnoreCookieSpecFactory;
/*    */ import org.apache.hc.client5.http.impl.cookie.RFC6265CookieSpecFactory;
/*    */ import org.apache.hc.client5.http.psl.PublicSuffixMatcher;
/*    */ import org.apache.hc.client5.http.psl.PublicSuffixMatcherLoader;
/*    */ import org.apache.hc.core5.annotation.Internal;
/*    */ import org.apache.hc.core5.http.config.Lookup;
/*    */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Internal
/*    */ public final class CookieSpecSupport
/*    */ {
/*    */   public static RegistryBuilder<CookieSpecFactory> createDefaultBuilder(PublicSuffixMatcher publicSuffixMatcher) {
/* 52 */     return RegistryBuilder.create()
/* 53 */       .register("relaxed", new RFC6265CookieSpecFactory(RFC6265CookieSpecFactory.CompatibilityLevel.RELAXED, publicSuffixMatcher))
/*    */       
/* 55 */       .register("strict", new RFC6265CookieSpecFactory(RFC6265CookieSpecFactory.CompatibilityLevel.STRICT, publicSuffixMatcher))
/*    */       
/* 57 */       .register("ignore", new IgnoreCookieSpecFactory());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RegistryBuilder<CookieSpecFactory> createDefaultBuilder() {
/* 64 */     return createDefaultBuilder(PublicSuffixMatcherLoader.getDefault());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Lookup<CookieSpecFactory> createDefault() {
/* 71 */     return createDefault(PublicSuffixMatcherLoader.getDefault());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Lookup<CookieSpecFactory> createDefault(PublicSuffixMatcher publicSuffixMatcher) {
/* 78 */     return (Lookup<CookieSpecFactory>)createDefaultBuilder(publicSuffixMatcher).build();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/CookieSpecSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */