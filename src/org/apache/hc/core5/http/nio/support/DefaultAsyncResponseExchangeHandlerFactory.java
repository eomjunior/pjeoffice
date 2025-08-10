/*    */ package org.apache.hc.core5.http.nio.support;
/*    */ 
/*    */ import org.apache.hc.core5.function.Decorator;
/*    */ import org.apache.hc.core5.function.Supplier;
/*    */ import org.apache.hc.core5.http.HttpException;
/*    */ import org.apache.hc.core5.http.HttpRequest;
/*    */ import org.apache.hc.core5.http.HttpRequestMapper;
/*    */ import org.apache.hc.core5.http.MisdirectedRequestException;
/*    */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DefaultAsyncResponseExchangeHandlerFactory
/*    */   implements HandlerFactory<AsyncServerExchangeHandler>
/*    */ {
/*    */   private final HttpRequestMapper<Supplier<AsyncServerExchangeHandler>> mapper;
/*    */   private final Decorator<AsyncServerExchangeHandler> decorator;
/*    */   
/*    */   public DefaultAsyncResponseExchangeHandlerFactory(HttpRequestMapper<Supplier<AsyncServerExchangeHandler>> mapper, Decorator<AsyncServerExchangeHandler> decorator) {
/* 56 */     this.mapper = (HttpRequestMapper<Supplier<AsyncServerExchangeHandler>>)Args.notNull(mapper, "Request handler mapper");
/* 57 */     this.decorator = decorator;
/*    */   }
/*    */   
/*    */   public DefaultAsyncResponseExchangeHandlerFactory(HttpRequestMapper<Supplier<AsyncServerExchangeHandler>> mapper) {
/* 61 */     this(mapper, null);
/*    */   }
/*    */ 
/*    */   
/*    */   private AsyncServerExchangeHandler createHandler(HttpRequest request, HttpContext context) throws HttpException {
/*    */     try {
/* 67 */       Supplier<AsyncServerExchangeHandler> supplier = (Supplier<AsyncServerExchangeHandler>)this.mapper.resolve(request, context);
/* 68 */       return (supplier != null) ? (AsyncServerExchangeHandler)supplier
/* 69 */         .get() : new ImmediateResponseExchangeHandler(404, "Resource not found");
/*    */     }
/* 71 */     catch (MisdirectedRequestException ex) {
/* 72 */       return new ImmediateResponseExchangeHandler(421, "Not authoritative");
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public AsyncServerExchangeHandler create(HttpRequest request, HttpContext context) throws HttpException {
/* 79 */     AsyncServerExchangeHandler handler = createHandler(request, context);
/* 80 */     if (handler != null) {
/* 81 */       return (this.decorator != null) ? (AsyncServerExchangeHandler)this.decorator.decorate(handler) : handler;
/*    */     }
/* 83 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/DefaultAsyncResponseExchangeHandlerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */