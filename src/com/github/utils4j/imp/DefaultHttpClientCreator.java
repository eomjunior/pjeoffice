/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import java.net.ProxySelector;
/*    */ import org.apache.hc.client5.http.config.RequestConfig;
/*    */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
/*    */ import org.apache.hc.client5.http.impl.classic.HttpClients;
/*    */ import org.apache.hc.client5.http.impl.routing.SystemDefaultRoutePlanner;
/*    */ import org.apache.hc.client5.http.routing.HttpRoutePlanner;
/*    */ import org.apache.hc.core5.util.TimeValue;
/*    */ import org.apache.hc.core5.util.Timeout;
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
/*    */ public final class DefaultHttpClientCreator
/*    */ {
/* 40 */   private static final Timeout _1m = Timeout.ofMinutes(1L);
/* 41 */   private static final Timeout _3m = Timeout.ofMinutes(3L);
/* 42 */   private static final Timeout _30s = Timeout.ofSeconds(30L);
/*    */ 
/*    */ 
/*    */   
/*    */   public static CloseableHttpClient create(String userAgent) throws Exception {
/* 47 */     return HttpClients.custom()
/* 48 */       .setRoutePlanner((HttpRoutePlanner)new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
/* 49 */       .evictExpiredConnections()
/* 50 */       .evictIdleConnections((TimeValue)_1m)
/* 51 */       .setUserAgent(userAgent)
/* 52 */       .setDefaultRequestConfig(RequestConfig.custom()
/* 53 */         .setResponseTimeout(_30s)
/* 54 */         .setConnectTimeout(_3m)
/* 55 */         .setConnectionKeepAlive((TimeValue)_3m)
/* 56 */         .setConnectionRequestTimeout(_3m)
/* 57 */         .setCookieSpec("ignore").build())
/* 58 */       .build();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/DefaultHttpClientCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */