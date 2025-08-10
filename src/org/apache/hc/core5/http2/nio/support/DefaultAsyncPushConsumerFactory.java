/*    */ package org.apache.hc.core5.http2.nio.support;
/*    */ 
/*    */ import org.apache.hc.core5.function.Supplier;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestMapper;
/*    */ import org.apache.hc.core5.http.MisdirectedRequestException;
/*    */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*    */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*    */ import org.apache.hc.core5.http.nio.ResourceHolder;
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
/*    */ public final class DefaultAsyncPushConsumerFactory
/*    */   implements HandlerFactory<AsyncPushConsumer>
/*    */ {
/*    */   private final HttpRequestMapper<Supplier<AsyncPushConsumer>> mapper;
/*    */   
/*    */   public DefaultAsyncPushConsumerFactory(HttpRequestMapper<Supplier<AsyncPushConsumer>> mapper) {
/* 51 */     this.mapper = (HttpRequestMapper<Supplier<AsyncPushConsumer>>)Args.notNull(mapper, "Request handler mapper");
/*    */   }
/*    */ 
/*    */   
/*    */   public AsyncPushConsumer create(HttpRequest request, HttpContext context) throws HttpException {
/*    */     try {
/* 57 */       Supplier<AsyncPushConsumer> supplier = (Supplier<AsyncPushConsumer>)this.mapper.resolve(request, context);
/* 58 */       return (supplier != null) ? (AsyncPushConsumer)supplier.get() : null;
/* 59 */     } catch (MisdirectedRequestException ex) {
/* 60 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/nio/support/DefaultAsyncPushConsumerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */