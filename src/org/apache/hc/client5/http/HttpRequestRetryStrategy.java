/*    */ package org.apache.hc.client5.http;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.util.TimeValue;
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
/*    */ public interface HttpRequestRetryStrategy
/*    */ {
/*    */   boolean retryRequest(HttpRequest paramHttpRequest, IOException paramIOException, int paramInt, HttpContext paramHttpContext);
/*    */   
/*    */   boolean retryRequest(HttpResponse paramHttpResponse, int paramInt, HttpContext paramHttpContext);
/*    */   
/*    */   default TimeValue getRetryInterval(HttpRequest request, IOException exception, int execCount, HttpContext context) {
/* 91 */     return TimeValue.ZERO_MILLISECONDS;
/*    */   }
/*    */   
/*    */   TimeValue getRetryInterval(HttpResponse paramHttpResponse, int paramInt, HttpContext paramHttpContext);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/HttpRequestRetryStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */