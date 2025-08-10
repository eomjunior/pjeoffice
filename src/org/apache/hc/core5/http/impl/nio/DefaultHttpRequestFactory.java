/*    */ package org.apache.hc.core5.http.impl.nio;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestFactory;
/*    */ import org.apache.hc.core5.http.message.BasicHttpRequest;
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
/*    */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*    */ public class DefaultHttpRequestFactory
/*    */   implements HttpRequestFactory<HttpRequest>
/*    */ {
/* 46 */   public static final DefaultHttpRequestFactory INSTANCE = new DefaultHttpRequestFactory();
/*    */ 
/*    */   
/*    */   public HttpRequest newHttpRequest(String method, URI uri) {
/* 50 */     return (HttpRequest)new BasicHttpRequest(method, uri);
/*    */   }
/*    */ 
/*    */   
/*    */   public HttpRequest newHttpRequest(String method, String uri) {
/* 55 */     return (HttpRequest)new BasicHttpRequest(method, uri);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/DefaultHttpRequestFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */