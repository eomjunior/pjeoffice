/*    */ package org.apache.hc.core5.http.impl;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import org.apache.hc.core5.function.Resolver;
/*    */ import org.apache.hc.core5.http.HttpHost;
/*    */ import org.apache.hc.core5.http.URIScheme;
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
/*    */ public final class DefaultAddressResolver
/*    */   implements Resolver<HttpHost, InetSocketAddress>
/*    */ {
/* 43 */   public static final DefaultAddressResolver INSTANCE = new DefaultAddressResolver();
/*    */ 
/*    */   
/*    */   public InetSocketAddress resolve(HttpHost host) {
/* 47 */     if (host == null) {
/* 48 */       return null;
/*    */     }
/* 50 */     int port = host.getPort();
/* 51 */     if (port < 0) {
/* 52 */       String scheme = host.getSchemeName();
/* 53 */       if (URIScheme.HTTP.same(scheme)) {
/* 54 */         port = 80;
/* 55 */       } else if (URIScheme.HTTPS.same(scheme)) {
/* 56 */         port = 443;
/*    */       } 
/*    */     } 
/* 59 */     return new InetSocketAddress(host.getHostName(), port);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/DefaultAddressResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */