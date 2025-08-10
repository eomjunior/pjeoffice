/*    */ package org.apache.hc.core5.http.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.Header;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.io.HttpServerRequestHandler;
/*    */ import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
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
/*    */ public class BasicHttpServerExpectationDecorator
/*    */   implements HttpServerRequestHandler
/*    */ {
/*    */   private final HttpServerRequestHandler requestHandler;
/*    */   
/*    */   public BasicHttpServerExpectationDecorator(HttpServerRequestHandler requestHandler) {
/* 56 */     this.requestHandler = (HttpServerRequestHandler)Args.notNull(requestHandler, "Request handler");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected ClassicHttpResponse verify(ClassicHttpRequest request, HttpContext context) {
/* 69 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final void handle(ClassicHttpRequest request, HttpServerRequestHandler.ResponseTrigger responseTrigger, HttpContext context) throws HttpException, IOException {
/* 77 */     Header expect = request.getFirstHeader("Expect");
/* 78 */     if (expect != null && "100-continue".equalsIgnoreCase(expect.getValue())) {
/* 79 */       ClassicHttpResponse response = verify(request, context);
/* 80 */       if (response == null) {
/* 81 */         responseTrigger.sendInformation((ClassicHttpResponse)new BasicClassicHttpResponse(100));
/*    */       } else {
/* 83 */         responseTrigger.submitResponse(response);
/*    */         return;
/*    */       } 
/*    */     } 
/* 87 */     this.requestHandler.handle(request, responseTrigger, context);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/BasicHttpServerExpectationDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */