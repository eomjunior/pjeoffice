/*    */ package org.apache.hc.client5.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.client5.http.config.RequestConfig;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestInterceptor;
/*    */ import org.apache.hc.core5.http.HttpVersion;
/*    */ import org.apache.hc.core5.http.ProtocolVersion;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public class RequestExpectContinue
/*    */   implements HttpRequestInterceptor
/*    */ {
/*    */   public void process(HttpRequest request, EntityDetails entity, HttpContext context) throws HttpException, IOException {
/* 66 */     Args.notNull(request, "HTTP request");
/*    */     
/* 68 */     if (!request.containsHeader("Expect")) {
/* 69 */       ProtocolVersion version = (request.getVersion() != null) ? request.getVersion() : (ProtocolVersion)HttpVersion.HTTP_1_1;
/*    */       
/* 71 */       if (entity != null && entity
/* 72 */         .getContentLength() != 0L && !version.lessEquals((ProtocolVersion)HttpVersion.HTTP_1_0)) {
/* 73 */         HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 74 */         RequestConfig config = clientContext.getRequestConfig();
/* 75 */         if (config.isExpectContinueEnabled())
/* 76 */           request.addHeader("Expect", "100-continue"); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/protocol/RequestExpectContinue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */