/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.util.concurrent.FutureTask;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HttpRequestFutureTask<V>
/*     */   extends FutureTask<V>
/*     */ {
/*     */   private final ClassicHttpRequest request;
/*     */   private final HttpRequestTaskCallable<V> callable;
/*     */   
/*     */   HttpRequestFutureTask(ClassicHttpRequest request, HttpRequestTaskCallable<V> httpCallable) {
/*  42 */     super(httpCallable);
/*  43 */     this.request = request;
/*  44 */     this.callable = httpCallable;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  49 */     this.callable.cancel();
/*  50 */     if (mayInterruptIfRunning && this.request instanceof Cancellable) {
/*  51 */       ((Cancellable)this.request).cancel();
/*     */     }
/*  53 */     return super.cancel(mayInterruptIfRunning);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long scheduledTime() {
/*  60 */     return this.callable.getScheduled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long startedTime() {
/*  67 */     return this.callable.getStarted();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long endedTime() {
/*  74 */     if (isDone()) {
/*  75 */       return this.callable.getEnded();
/*     */     }
/*  77 */     throw new IllegalStateException("Task is not done yet");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long requestDuration() {
/*  86 */     if (isDone()) {
/*  87 */       return endedTime() - startedTime();
/*     */     }
/*  89 */     throw new IllegalStateException("Task is not done yet");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long taskDuration() {
/*  97 */     if (isDone()) {
/*  98 */       return endedTime() - scheduledTime();
/*     */     }
/* 100 */     throw new IllegalStateException("Task is not done yet");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     return this.request.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/HttpRequestFutureTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */