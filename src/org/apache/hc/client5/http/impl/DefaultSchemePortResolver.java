/*    */ package org.apache.hc.client5.http.impl;
/*    */ 
/*    */ import org.apache.hc.client5.http.SchemePortResolver;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpHost;
/*    */ import org.apache.hc.core5.http.URIScheme;
/*    */ import org.apache.hc.core5.net.NamedEndpoint;
/*    */ import org.apache.hc.core5.util.Args;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class DefaultSchemePortResolver
/*    */   implements SchemePortResolver
/*    */ {
/* 45 */   public static final DefaultSchemePortResolver INSTANCE = new DefaultSchemePortResolver();
/*    */ 
/*    */   
/*    */   public int resolve(HttpHost host) {
/* 49 */     Args.notNull(host, "HTTP host");
/* 50 */     return resolve(host.getSchemeName(), (NamedEndpoint)host);
/*    */   }
/*    */ 
/*    */   
/*    */   public int resolve(String scheme, NamedEndpoint endpoint) {
/* 55 */     Args.notNull(endpoint, "Endpoint");
/* 56 */     int port = endpoint.getPort();
/* 57 */     if (port > 0) {
/* 58 */       return port;
/*    */     }
/* 60 */     if (URIScheme.HTTP.same(scheme))
/* 61 */       return 80; 
/* 62 */     if (URIScheme.HTTPS.same(scheme)) {
/* 63 */       return 443;
/*    */     }
/* 65 */     return -1;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/DefaultSchemePortResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */