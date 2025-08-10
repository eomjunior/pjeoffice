/*    */ package com.github.utils4j.imp;
/*    */ 
/*    */ import org.apache.hc.client5.http.config.RequestConfig;
/*    */ import org.apache.hc.client5.http.fluent.Request;
/*    */ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
/*    */ import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
/*    */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
/*    */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpToucher
/*    */ {
/*    */   public static void touch(String uri) {
/* 46 */     Args.requireNonNull(uri, "uri is null");
/* 47 */     Timeout timeout = Timeout.ofSeconds(5L);
/* 48 */     Throwables.quietly(() -> {
/*    */           try (CloseableHttpClient client = HttpClientBuilder.create().disableAuthCaching().disableAutomaticRetries().disableConnectionState().disableContentCompression().disableCookieManagement().disableRedirectHandling().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec("ignore").build()).setConnectionManager((HttpClientConnectionManager)PoolingHttpClientConnectionManagerBuilder.create().setMaxConnPerRoute(2).setMaxConnTotal(2).setValidateAfterInactivity(TimeValue.ofSeconds(10L)).build()).build()) {
/*    */             Request.get(uri).connectTimeout(timeout).responseTimeout(timeout).execute(client).discardContent();
/*    */           } 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/HttpToucher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */