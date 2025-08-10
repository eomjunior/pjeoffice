/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public abstract class AbstractAsyncPushHandler<T>
/*     */   implements AsyncPushConsumer
/*     */ {
/*     */   private final AsyncResponseConsumer<T> responseConsumer;
/*     */   
/*     */   public AbstractAsyncPushHandler(AsyncResponseConsumer<T> responseConsumer) {
/*  57 */     this.responseConsumer = (AsyncResponseConsumer<T>)Args.notNull(responseConsumer, "Response consumer");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void handleResponse(HttpRequest paramHttpRequest, T paramT) throws IOException, HttpException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleError(HttpRequest promise, Exception cause) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void consumePromise(final HttpRequest promise, HttpResponse response, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
/*  84 */     this.responseConsumer.consumeResponse(response, entityDetails, httpContext, new FutureCallback<T>()
/*     */         {
/*     */           public void completed(T result)
/*     */           {
/*     */             try {
/*  89 */               AbstractAsyncPushHandler.this.handleResponse(promise, result);
/*  90 */             } catch (Exception ex) {
/*  91 */               failed(ex);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception cause) {
/*  97 */             AbstractAsyncPushHandler.this.handleError(promise, cause);
/*  98 */             AbstractAsyncPushHandler.this.releaseResources();
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 103 */             AbstractAsyncPushHandler.this.releaseResources();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 111 */     this.responseConsumer.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void consume(ByteBuffer src) throws IOException {
/* 116 */     this.responseConsumer.consume(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 121 */     this.responseConsumer.streamEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 126 */     this.responseConsumer.failed(cause);
/* 127 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void releaseResources() {
/* 132 */     if (this.responseConsumer != null)
/* 133 */       this.responseConsumer.releaseResources(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AbstractAsyncPushHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */