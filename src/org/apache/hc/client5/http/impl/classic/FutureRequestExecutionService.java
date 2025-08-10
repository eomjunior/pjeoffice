/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.client5.http.classic.HttpClient;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.io.HttpClientResponseHandler;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public class FutureRequestExecutionService
/*     */   implements Closeable
/*     */ {
/*     */   private final HttpClient httpclient;
/*     */   private final ExecutorService executorService;
/*  52 */   private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();
/*  53 */   private final AtomicBoolean closed = new AtomicBoolean(false);
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
/*     */   public FutureRequestExecutionService(HttpClient httpclient, ExecutorService executorService) {
/*  71 */     this.httpclient = httpclient;
/*  72 */     this.executorService = executorService;
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
/*     */   public <T> FutureTask<T> execute(ClassicHttpRequest request, HttpContext context, HttpClientResponseHandler<T> httpClientResponseHandler) {
/*  90 */     return execute(request, context, httpClientResponseHandler, null);
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
/*     */ 
/*     */   
/*     */   public <T> FutureTask<T> execute(ClassicHttpRequest request, HttpContext context, HttpClientResponseHandler<T> httpClientResponseHandler, FutureCallback<T> callback) {
/* 114 */     if (this.closed.get()) {
/* 115 */       throw new IllegalStateException("Close has been called on this httpclient instance.");
/*     */     }
/* 117 */     this.metrics.getScheduledConnections().incrementAndGet();
/* 118 */     HttpRequestTaskCallable<T> callable = new HttpRequestTaskCallable<>(this.httpclient, request, context, httpClientResponseHandler, callback, this.metrics);
/*     */     
/* 120 */     HttpRequestFutureTask<T> httpRequestFutureTask = new HttpRequestFutureTask<>(request, callable);
/*     */     
/* 122 */     this.executorService.execute(httpRequestFutureTask);
/*     */     
/* 124 */     return httpRequestFutureTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FutureRequestExecutionMetrics metrics() {
/* 132 */     return this.metrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 137 */     this.closed.set(true);
/* 138 */     this.executorService.shutdownNow();
/* 139 */     if (this.httpclient instanceof Closeable)
/* 140 */       ((Closeable)this.httpclient).close(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/FutureRequestExecutionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */