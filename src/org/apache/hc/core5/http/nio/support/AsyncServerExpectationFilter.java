/*    */ package org.apache.hc.core5.http.nio.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpResponse;
/*    */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*    */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*    */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*    */ import org.apache.hc.core5.http.nio.AsyncFilterChain;
/*    */ import org.apache.hc.core5.http.nio.AsyncFilterHandler;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class AsyncServerExpectationFilter
/*    */   implements AsyncFilterHandler
/*    */ {
/*    */   protected boolean verify(HttpRequest request, HttpContext context) throws HttpException {
/* 55 */     return true;
/*    */   }
/*    */   
/*    */   protected AsyncEntityProducer generateResponseContent(HttpResponse expectationFailed) throws HttpException {
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final AsyncDataConsumer handle(HttpRequest request, EntityDetails entityDetails, HttpContext context, AsyncFilterChain.ResponseTrigger responseTrigger, AsyncFilterChain chain) throws HttpException, IOException {
/* 69 */     if (entityDetails != null) {
/* 70 */       Header h = request.getFirstHeader("Expect");
/* 71 */       if (h != null && "100-continue".equalsIgnoreCase(h.getValue())) {
/* 72 */         boolean verified = verify(request, context);
/* 73 */         if (verified) {
/* 74 */           responseTrigger.sendInformation((HttpResponse)new BasicHttpResponse(100));
/*    */         } else {
/* 76 */           BasicHttpResponse basicHttpResponse = new BasicHttpResponse(417);
/* 77 */           AsyncEntityProducer responseContentProducer = generateResponseContent((HttpResponse)basicHttpResponse);
/* 78 */           responseTrigger.submitResponse((HttpResponse)basicHttpResponse, responseContentProducer);
/* 79 */           return null;
/*    */         } 
/*    */       } 
/*    */     } 
/* 83 */     return chain.proceed(request, entityDetails, context, responseTrigger);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AsyncServerExpectationFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */