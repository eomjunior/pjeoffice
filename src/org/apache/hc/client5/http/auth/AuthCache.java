/*     */ package org.apache.hc.client5.http.auth;
/*     */ 
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface AuthCache
/*     */ {
/*     */   void put(HttpHost paramHttpHost, AuthScheme paramAuthScheme);
/*     */   
/*     */   AuthScheme get(HttpHost paramHttpHost);
/*     */   
/*     */   void remove(HttpHost paramHttpHost);
/*     */   
/*     */   void clear();
/*     */   
/*     */   default void put(HttpHost host, String pathPrefix, AuthScheme authScheme) {
/*  77 */     put(host, authScheme);
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
/*     */   
/*     */   default AuthScheme get(HttpHost host, String pathPrefix) {
/*  91 */     return get(host);
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
/*     */   
/*     */   default void remove(HttpHost host, String pathPrefix) {
/* 105 */     remove(host);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/auth/AuthCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */