/*     */ package org.apache.hc.core5.http.nio;
/*     */ 
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.concurrent.FutureContribution;
/*     */ import org.apache.hc.core5.http.nio.support.BasicClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public abstract class AsyncClientEndpoint
/*     */ {
/*     */   public abstract void execute(AsyncClientExchangeHandler paramAsyncClientExchangeHandler, HandlerFactory<AsyncPushConsumer> paramHandlerFactory, HttpContext paramHttpContext);
/*     */   
/*     */   public void execute(AsyncClientExchangeHandler exchangeHandler, HttpContext context) {
/*  72 */     execute(exchangeHandler, (HandlerFactory<AsyncPushConsumer>)null, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void releaseAndReuse();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void releaseAndDiscard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isConnected();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context, FutureCallback<T> callback) {
/* 102 */     final BasicFuture<T> future = new BasicFuture(callback);
/* 103 */     execute((AsyncClientExchangeHandler)new BasicClientExchangeHandler(requestProducer, responseConsumer, (FutureCallback)new FutureContribution<T>(future)
/*     */           {
/*     */             
/*     */             public void completed(T result)
/*     */             {
/* 108 */               future.completed(result);
/*     */             }
/* 112 */           }), pushHandlerFactory, (context != null) ? context : (HttpContext)HttpCoreContext.create());
/* 113 */     return (Future<T>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
/* 127 */     return execute(requestProducer, responseConsumer, null, context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
/* 140 */     return execute(requestProducer, responseConsumer, null, null, callback);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/AsyncClientEndpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */