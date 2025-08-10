/*    */ package org.apache.hc.client5.http.impl.async;
/*    */ 
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*    */ import org.apache.hc.core5.concurrent.Cancellable;
/*    */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*    */ import org.apache.hc.core5.concurrent.FutureCallback;
/*    */ import org.apache.hc.core5.http.HttpHost;
/*    */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*    */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*    */ import org.apache.hc.core5.http.nio.AsyncRequestProducer;
/*    */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*    */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*    */ import org.apache.hc.core5.http.nio.support.BasicClientExchangeHandler;
/*    */ import org.apache.hc.core5.http.protocol.HttpContext;
/*    */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractMinimalHttpAsyncClientBase
/*    */   extends AbstractHttpAsyncClientBase
/*    */ {
/*    */   AbstractMinimalHttpAsyncClientBase(DefaultConnectingIOReactor ioReactor, AsyncPushConsumerRegistry pushConsumerRegistry, ThreadFactory threadFactory) {
/* 52 */     super(ioReactor, pushConsumerRegistry, threadFactory);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected <T> Future<T> doExecute(HttpHost httpHost, AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context, FutureCallback<T> callback) {
/* 63 */     final ComplexFuture<T> future = new ComplexFuture(callback);
/* 64 */     future.setDependency(execute((AsyncClientExchangeHandler)new BasicClientExchangeHandler(requestProducer, responseConsumer, new FutureCallback<T>()
/*    */             {
/*    */ 
/*    */ 
/*    */               
/*    */               public void completed(T result)
/*    */               {
/* 71 */                 future.completed(result);
/*    */               }
/*    */ 
/*    */               
/*    */               public void failed(Exception ex) {
/* 76 */                 future.failed(ex);
/*    */               }
/*    */ 
/*    */               
/*    */               public void cancelled() {
/* 81 */                 future.cancel();
/*    */               }
/*    */             }), pushHandlerFactory, context));
/*    */     
/* 85 */     return (Future<T>)future;
/*    */   }
/*    */   
/*    */   public final Cancellable execute(AsyncClientExchangeHandler exchangeHandler) {
/* 89 */     return execute(exchangeHandler, null, (HttpContext)HttpClientContext.create());
/*    */   }
/*    */   
/*    */   public abstract Cancellable execute(AsyncClientExchangeHandler paramAsyncClientExchangeHandler, HandlerFactory<AsyncPushConsumer> paramHandlerFactory, HttpContext paramHttpContext);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/AbstractMinimalHttpAsyncClientBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */