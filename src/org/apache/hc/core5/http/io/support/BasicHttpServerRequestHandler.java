/*    */ package org.apache.hc.core5.http.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestMapper;
/*    */ import org.apache.hc.core5.http.HttpResponseFactory;
/*    */ import org.apache.hc.core5.http.impl.io.DefaultClassicHttpResponseFactory;
/*    */ import org.apache.hc.core5.http.io.HttpRequestHandler;
/*    */ import org.apache.hc.core5.http.io.HttpServerRequestHandler;
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
/*    */ public class BasicHttpServerRequestHandler
/*    */   implements HttpServerRequestHandler
/*    */ {
/*    */   private final HttpRequestMapper<HttpRequestHandler> handlerMapper;
/*    */   private final HttpResponseFactory<ClassicHttpResponse> responseFactory;
/*    */   
/*    */   public BasicHttpServerRequestHandler(HttpRequestMapper<HttpRequestHandler> handlerMapper, HttpResponseFactory<ClassicHttpResponse> responseFactory) {
/* 59 */     this.handlerMapper = (HttpRequestMapper<HttpRequestHandler>)Args.notNull(handlerMapper, "Handler mapper");
/* 60 */     this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory<ClassicHttpResponse>)DefaultClassicHttpResponseFactory.INSTANCE;
/*    */   }
/*    */   
/*    */   public BasicHttpServerRequestHandler(HttpRequestMapper<HttpRequestHandler> handlerMapper) {
/* 64 */     this(handlerMapper, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(ClassicHttpRequest request, HttpServerRequestHandler.ResponseTrigger responseTrigger, HttpContext context) throws HttpException, IOException {
/* 72 */     ClassicHttpResponse response = (ClassicHttpResponse)this.responseFactory.newHttpResponse(200);
/* 73 */     HttpRequestHandler handler = (this.handlerMapper != null) ? (HttpRequestHandler)this.handlerMapper.resolve((HttpRequest)request, context) : null;
/* 74 */     if (handler != null) {
/* 75 */       handler.handle(request, response, context);
/*    */     } else {
/* 77 */       response.setCode(501);
/*    */     } 
/* 79 */     responseTrigger.submitResponse(response);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/BasicHttpServerRequestHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */