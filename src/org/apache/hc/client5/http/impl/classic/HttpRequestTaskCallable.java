/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.client5.http.classic.HttpClient;
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
/*     */ class HttpRequestTaskCallable<V>
/*     */   implements Callable<V>
/*     */ {
/*     */   private final ClassicHttpRequest request;
/*     */   private final HttpClient httpclient;
/*  43 */   private final AtomicBoolean cancelled = new AtomicBoolean(false);
/*     */   
/*  45 */   private final long scheduled = System.currentTimeMillis();
/*  46 */   private long started = -1L;
/*  47 */   private long ended = -1L;
/*     */ 
/*     */   
/*     */   private final HttpContext context;
/*     */ 
/*     */   
/*     */   private final HttpClientResponseHandler<V> responseHandler;
/*     */ 
/*     */   
/*     */   private final FutureCallback<V> callback;
/*     */   
/*     */   private final FutureRequestExecutionMetrics metrics;
/*     */ 
/*     */   
/*     */   HttpRequestTaskCallable(HttpClient httpClient, ClassicHttpRequest request, HttpContext context, HttpClientResponseHandler<V> responseHandler, FutureCallback<V> callback, FutureRequestExecutionMetrics metrics) {
/*  62 */     this.httpclient = httpClient;
/*  63 */     this.responseHandler = responseHandler;
/*  64 */     this.request = request;
/*  65 */     this.context = context;
/*  66 */     this.callback = callback;
/*  67 */     this.metrics = metrics;
/*     */   }
/*     */   
/*     */   public long getScheduled() {
/*  71 */     return this.scheduled;
/*     */   }
/*     */   
/*     */   public long getStarted() {
/*  75 */     return this.started;
/*     */   }
/*     */   
/*     */   public long getEnded() {
/*  79 */     return this.ended;
/*     */   }
/*     */ 
/*     */   
/*     */   public V call() throws Exception {
/*  84 */     if (!this.cancelled.get()) {
/*     */       
/*     */       try {
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
/*     */       } finally {
/*     */         
/* 106 */         this.metrics.getRequests().increment(this.started);
/* 107 */         this.metrics.getTasks().increment(this.started);
/* 108 */         this.metrics.getActiveConnections().decrementAndGet();
/*     */       } 
/*     */     }
/* 111 */     throw new CancellationException();
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 115 */     this.cancelled.set(true);
/* 116 */     if (this.callback != null)
/* 117 */       this.callback.cancelled(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/HttpRequestTaskCallable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */