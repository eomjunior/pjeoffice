/*    */ package org.apache.hc.client5.http.async.methods;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.hc.client5.http.config.Configurable;
/*    */ import org.apache.hc.client5.http.config.RequestConfig;
/*    */ import org.apache.hc.core5.http.HttpHost;
/*    */ import org.apache.hc.core5.http.message.BasicHttpRequest;
/*    */ import org.apache.hc.core5.net.URIAuthority;
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
/*    */ public class ConfigurableHttpRequest
/*    */   extends BasicHttpRequest
/*    */   implements Configurable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private RequestConfig requestConfig;
/*    */   
/*    */   public ConfigurableHttpRequest(String method, String path) {
/* 49 */     super(method, path);
/*    */   }
/*    */   
/*    */   public ConfigurableHttpRequest(String method, HttpHost host, String path) {
/* 53 */     super(method, host, path);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ConfigurableHttpRequest(String method, String scheme, URIAuthority authority, String path) {
/* 60 */     super(method, scheme, authority, path);
/*    */   }
/*    */   
/*    */   public ConfigurableHttpRequest(String method, URI requestUri) {
/* 64 */     super(method, requestUri);
/*    */   }
/*    */ 
/*    */   
/*    */   public RequestConfig getConfig() {
/* 69 */     return this.requestConfig;
/*    */   }
/*    */   
/*    */   public void setConfig(RequestConfig requestConfig) {
/* 73 */     this.requestConfig = requestConfig;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/ConfigurableHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */