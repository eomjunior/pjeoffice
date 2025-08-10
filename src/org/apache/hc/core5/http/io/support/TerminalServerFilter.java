/*    */ package org.apache.hc.core5.http.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.annotation.Contract;
/*    */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestMapper;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.impl.io.DefaultClassicHttpResponseFactory;
/*    */ import org.apache.hc.core5.http.io.HttpFilterChain;
/*    */ import org.apache.hc.core5.http.io.HttpFilterHandler;
/*    */ import org.apache.hc.core5.http.io.HttpRequestHandler;
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
/*    */ @Contract(threading = ThreadingBehavior.STATELESS)
/*    */ public final class TerminalServerFilter
/*    */   implements HttpFilterHandler
/*    */ {
/*    */   private final HttpRequestMapper<HttpRequestHandler> handlerMapper;
/*    */   private final HttpResponseFactory<ClassicHttpResponse> responseFactory;
/*    */   
/*    */   public TerminalServerFilter(HttpRequestMapper<HttpRequestHandler> handlerMapper, HttpResponseFactory<ClassicHttpResponse> responseFactory) {
/* 62 */     this.handlerMapper = (HttpRequestMapper<HttpRequestHandler>)Args.notNull(handlerMapper, "Handler mapper");
/* 63 */     this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory<ClassicHttpResponse>)DefaultClassicHttpResponseFactory.INSTANCE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(ClassicHttpRequest request, HttpFilterChain.ResponseTrigger responseTrigger, HttpContext context, HttpFilterChain chain) throws HttpException, IOException {
/* 72 */     ClassicHttpResponse response = (ClassicHttpResponse)this.responseFactory.newHttpResponse(200);
/* 73 */     HttpRequestHandler handler = (HttpRequestHandler)this.handlerMapper.resolve((HttpRequest)request, context);
/* 74 */     if (handler != null) {
/* 75 */       handler.handle(request, response, context);
/*    */     } else {
/* 77 */       response.setCode(501);
/*    */     } 
/* 79 */     responseTrigger.submitResponse(response);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/TerminalServerFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */