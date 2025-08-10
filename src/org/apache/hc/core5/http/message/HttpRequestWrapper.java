/*    */ package org.apache.hc.core5.http.message;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import org.apache.hc.core5.http.HttpMessage;
/*    */ import org.apache.hc.core5.http.HttpRequest;
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
/*    */ public class HttpRequestWrapper
/*    */   extends AbstractMessageWrapper
/*    */   implements HttpRequest
/*    */ {
/*    */   private final HttpRequest message;
/*    */   
/*    */   public HttpRequestWrapper(HttpRequest message) {
/* 44 */     super((HttpMessage)message);
/* 45 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMethod() {
/* 50 */     return this.message.getMethod();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPath() {
/* 55 */     return this.message.getPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPath(String path) {
/* 60 */     this.message.setPath(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getScheme() {
/* 65 */     return this.message.getScheme();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setScheme(String scheme) {
/* 70 */     this.message.setScheme(scheme);
/*    */   }
/*    */ 
/*    */   
/*    */   public URIAuthority getAuthority() {
/* 75 */     return this.message.getAuthority();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setAuthority(URIAuthority authority) {
/* 80 */     this.message.setAuthority(authority);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getRequestUri() {
/* 85 */     return this.message.getRequestUri();
/*    */   }
/*    */ 
/*    */   
/*    */   public URI getUri() throws URISyntaxException {
/* 90 */     return this.message.getUri();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setUri(URI requestUri) {
/* 95 */     this.message.setUri(requestUri);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/message/HttpRequestWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */