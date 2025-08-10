/*     */ package org.apache.hc.client5.http.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.support.BasicClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ public abstract class AsyncConnectionEndpoint
/*     */   implements ModalCloseable
/*     */ {
/*     */   public abstract void execute(String paramString, AsyncClientExchangeHandler paramAsyncClientExchangeHandler, HandlerFactory<AsyncPushConsumer> paramHandlerFactory, HttpContext paramHttpContext);
/*     */   
/*     */   public abstract boolean isConnected();
/*     */   
/*     */   public abstract void setSocketTimeout(Timeout paramTimeout);
/*     */   
/*     */   public final void close() throws IOException {
/*  85 */     close(CloseMode.GRACEFUL);
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
/*     */   public void execute(String id, AsyncClientExchangeHandler exchangeHandler, HttpContext context) {
/*  99 */     execute(id, exchangeHandler, (HandlerFactory<AsyncPushConsumer>)null, context);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(String id, AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context, FutureCallback<T> callback) {
/* 121 */     final BasicFuture<T> future = new BasicFuture(callback);
/* 122 */     execute(id, (AsyncClientExchangeHandler)new BasicClientExchangeHandler(requestProducer, responseConsumer, new FutureCallback<T>()
/*     */           {
/*     */             
/*     */             public void completed(T result)
/*     */             {
/* 127 */               future.completed(result);
/*     */             }
/*     */ 
/*     */             
/*     */             public void failed(Exception ex) {
/* 132 */               future.failed(ex);
/*     */             }
/*     */ 
/*     */             
/*     */             public void cancelled() {
/* 137 */               future.cancel();
/*     */             }
/* 142 */           }), pushHandlerFactory, (context != null) ? context : (HttpContext)HttpCoreContext.create());
/* 143 */     return (Future<T>)future;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(String id, AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
/* 163 */     return execute(id, requestProducer, responseConsumer, (HandlerFactory<AsyncPushConsumer>)null, context, callback);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(String id, AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, FutureCallback<T> callback) {
/* 183 */     return execute(id, requestProducer, responseConsumer, pushHandlerFactory, (HttpContext)null, callback);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(String id, AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
/* 201 */     return execute(id, requestProducer, responseConsumer, (HandlerFactory<AsyncPushConsumer>)null, (HttpContext)null, callback);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/nio/AsyncConnectionEndpoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */