/*    */ package org.apache.hc.core5.http.nio.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.hc.core5.http.EntityDetails;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.nio.AsyncRequestConsumer;
/*    */ import org.apache.hc.core5.http.nio.AsyncServerRequestHandler;
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
/*    */ public class BasicServerExchangeHandler<T>
/*    */   extends AbstractServerExchangeHandler<T>
/*    */ {
/*    */   private final AsyncServerRequestHandler<T> requestHandler;
/*    */   
/*    */   public BasicServerExchangeHandler(AsyncServerRequestHandler<T> requestHandler) {
/* 51 */     this.requestHandler = (AsyncServerRequestHandler<T>)Args.notNull(requestHandler, "Response handler");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected AsyncRequestConsumer<T> supplyConsumer(HttpRequest request, EntityDetails entityDetails, HttpContext context) throws HttpException {
/* 59 */     return this.requestHandler.prepare(request, entityDetails, context);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handle(T requestMessage, AsyncServerRequestHandler.ResponseTrigger responseTrigger, HttpContext context) throws HttpException, IOException {
/* 67 */     this.requestHandler.handle(requestMessage, responseTrigger, context);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/BasicServerExchangeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */