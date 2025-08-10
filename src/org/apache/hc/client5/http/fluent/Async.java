/*     */ package org.apache.hc.client5.http.fluent;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.io.HttpClientResponseHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Async
/*     */ {
/*     */   private Executor executor;
/*     */   private Executor concurrentExec;
/*     */   
/*     */   public static Async newInstance() {
/*  46 */     return new Async();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Async use(Executor executor) {
/*  54 */     this.executor = executor;
/*  55 */     return this;
/*     */   }
/*     */   
/*     */   public Async use(Executor concurrentExec) {
/*  59 */     this.concurrentExec = concurrentExec;
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   static class ExecRunnable<T>
/*     */     implements Runnable
/*     */   {
/*     */     private final BasicFuture<T> future;
/*     */     
/*     */     private final Request request;
/*     */     
/*     */     private final Executor executor;
/*     */     
/*     */     private final HttpClientResponseHandler<T> handler;
/*     */     
/*     */     ExecRunnable(BasicFuture<T> future, Request request, Executor executor, HttpClientResponseHandler<T> handler) {
/*  76 */       this.future = future;
/*  77 */       this.request = request;
/*  78 */       this.executor = executor;
/*  79 */       this.handler = handler;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/*  85 */         Response response = this.executor.execute(this.request);
/*  86 */         T result = response.handleResponse(this.handler);
/*  87 */         this.future.completed(result);
/*  88 */       } catch (Exception ex) {
/*  89 */         this.future.failed(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> execute(Request request, HttpClientResponseHandler<T> handler, FutureCallback<T> callback) {
/*  97 */     BasicFuture<T> future = new BasicFuture(callback);
/*     */ 
/*     */ 
/*     */     
/* 101 */     ExecRunnable<T> runnable = new ExecRunnable<>(future, request, (this.executor != null) ? this.executor : Executor.newInstance(), handler);
/*     */     
/* 103 */     if (this.concurrentExec != null) {
/* 104 */       this.concurrentExec.execute(runnable);
/*     */     } else {
/* 106 */       Thread t = new Thread(runnable);
/* 107 */       t.setDaemon(true);
/* 108 */       t.start();
/*     */     } 
/* 110 */     return (Future<T>)future;
/*     */   }
/*     */   
/*     */   public <T> Future<T> execute(Request request, HttpClientResponseHandler<T> handler) {
/* 114 */     return execute(request, handler, null);
/*     */   }
/*     */   
/*     */   public Future<Content> execute(Request request, FutureCallback<Content> callback) {
/* 118 */     return execute(request, (HttpClientResponseHandler<Content>)new ContentResponseHandler(), callback);
/*     */   }
/*     */   
/*     */   public Future<Content> execute(Request request) {
/* 122 */     return execute(request, (HttpClientResponseHandler<Content>)new ContentResponseHandler(), null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/fluent/Async.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */