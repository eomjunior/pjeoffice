/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.client5.http.async.HttpAsyncClient;
/*     */ import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
/*     */ import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
/*     */ import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
/*     */ import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.reactor.IOReactorStatus;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public abstract class CloseableHttpAsyncClient
/*     */   implements HttpAsyncClient, ModalCloseable
/*     */ {
/*     */   public abstract void start();
/*     */   
/*     */   public abstract IOReactorStatus getStatus();
/*     */   
/*     */   public abstract void awaitShutdown(TimeValue paramTimeValue) throws InterruptedException;
/*     */   
/*     */   public abstract void initiateShutdown();
/*     */   
/*     */   protected abstract <T> Future<T> doExecute(HttpHost paramHttpHost, AsyncRequestProducer paramAsyncRequestProducer, AsyncResponseConsumer<T> paramAsyncResponseConsumer, HandlerFactory<AsyncPushConsumer> paramHandlerFactory, HttpContext paramHttpContext, FutureCallback<T> paramFutureCallback);
/*     */   
/*     */   public final <T> Future<T> execute(HttpHost target, AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context, FutureCallback<T> callback) {
/*  83 */     Args.notNull(requestProducer, "Request producer");
/*  84 */     Args.notNull(responseConsumer, "Response consumer");
/*  85 */     return doExecute(target, requestProducer, responseConsumer, pushHandlerFactory, context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context, FutureCallback<T> callback) {
/*  95 */     Args.notNull(requestProducer, "Request producer");
/*  96 */     Args.notNull(responseConsumer, "Response consumer");
/*  97 */     return doExecute(null, requestProducer, responseConsumer, pushHandlerFactory, context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
/* 105 */     Args.notNull(requestProducer, "Request producer");
/* 106 */     Args.notNull(responseConsumer, "Response consumer");
/* 107 */     return execute(requestProducer, responseConsumer, null, context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
/* 114 */     Args.notNull(requestProducer, "Request producer");
/* 115 */     Args.notNull(responseConsumer, "Response consumer");
/* 116 */     return execute(requestProducer, responseConsumer, (HttpContext)HttpClientContext.create(), callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Future<SimpleHttpResponse> execute(SimpleHttpRequest request, HttpContext context, FutureCallback<SimpleHttpResponse> callback) {
/* 123 */     Args.notNull(request, "Request");
/* 124 */     return execute((AsyncRequestProducer)SimpleRequestProducer.create(request), (AsyncResponseConsumer<SimpleHttpResponse>)SimpleResponseConsumer.create(), context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Future<SimpleHttpResponse> execute(SimpleHttpRequest request, FutureCallback<SimpleHttpResponse> callback) {
/* 130 */     return execute(request, (HttpContext)HttpClientContext.create(), callback);
/*     */   }
/*     */   
/*     */   public abstract void register(String paramString1, String paramString2, Supplier<AsyncPushConsumer> paramSupplier);
/*     */   
/*     */   public final void register(String uriPattern, Supplier<AsyncPushConsumer> supplier) {
/* 136 */     register(null, uriPattern, supplier);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/CloseableHttpAsyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */