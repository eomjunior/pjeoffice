/*    */ package org.apache.hc.client5.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*    */ import org.apache.hc.core5.http.Method;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class RequestDefaultHeaders
/*    */   implements HttpRequestInterceptor
/*    */ {
/* 57 */   public static final RequestDefaultHeaders INSTANCE = new RequestDefaultHeaders();
/*    */ 
/*    */ 
/*    */   
/*    */   private final Collection<? extends Header> defaultHeaders;
/*    */ 
/*    */ 
/*    */   
/*    */   public RequestDefaultHeaders(Collection<? extends Header> defaultHeaders) {
/* 66 */     this.defaultHeaders = defaultHeaders;
/*    */   }
/*    */   
/*    */   public RequestDefaultHeaders() {
/* 70 */     this(null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 76 */     Args.notNull(request, "HTTP request");
/*    */     
/* 78 */     String method = request.getMethod();
/* 79 */     if (Method.CONNECT.isSame(method)) {
/*    */       return;
/*    */     }
/*    */     
/* 83 */     if (this.defaultHeaders != null)
/* 84 */       for (Header defHeader : this.defaultHeaders) {
/* 85 */         if (!request.containsHeader(defHeader.getName()))
/* 86 */           request.addHeader(defHeader); 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/protocol/RequestDefaultHeaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */