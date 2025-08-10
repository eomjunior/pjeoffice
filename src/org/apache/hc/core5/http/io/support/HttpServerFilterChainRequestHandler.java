/*    */ package org.apache.hc.core5.http.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*    */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.io.HttpFilterChain;
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
/*    */ public class HttpServerFilterChainRequestHandler
/*    */   implements HttpServerRequestHandler
/*    */ {
/*    */   private final HttpServerFilterChainElement filterChain;
/*    */   
/*    */   public HttpServerFilterChainRequestHandler(HttpServerFilterChainElement filterChain) {
/* 51 */     this.filterChain = (HttpServerFilterChainElement)Args.notNull(filterChain, "Filter chain");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(ClassicHttpRequest request, final HttpServerRequestHandler.ResponseTrigger trigger, HttpContext context) throws HttpException, IOException {
/* 59 */     this.filterChain.handle(request, new HttpFilterChain.ResponseTrigger()
/*    */         {
/*    */           public void sendInformation(ClassicHttpResponse response) throws HttpException, IOException
/*    */           {
/* 63 */             trigger.sendInformation(response);
/*    */           }
/*    */ 
/*    */           
/*    */           public void submitResponse(ClassicHttpResponse response) throws HttpException, IOException {
/* 68 */             trigger.submitResponse(response);
/*    */           }
/*    */         },  context);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/HttpServerFilterChainRequestHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */