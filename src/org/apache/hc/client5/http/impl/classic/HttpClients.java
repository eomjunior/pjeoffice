/*    */ package org.apache.hc.client5.http.impl.classic;
/*    */ 
/*    */ import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
/*    */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class HttpClients
/*    */ {
/*    */   public static HttpClientBuilder custom() {
/* 49 */     return HttpClientBuilder.create();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CloseableHttpClient createDefault() {
/* 57 */     return HttpClientBuilder.create().build();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CloseableHttpClient createSystem() {
/* 65 */     return HttpClientBuilder.create().useSystemProperties().build();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MinimalHttpClient createMinimal() {
/* 73 */     return new MinimalHttpClient((HttpClientConnectionManager)new PoolingHttpClientConnectionManager());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MinimalHttpClient createMinimal(HttpClientConnectionManager connManager) {
/* 81 */     return new MinimalHttpClient(connManager);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/HttpClients.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */