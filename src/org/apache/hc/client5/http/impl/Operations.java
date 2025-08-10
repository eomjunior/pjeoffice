/*     */ package org.apache.hc.client5.http.impl;
/*     */ 
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Operations
/*     */ {
/*     */   private static final Cancellable NOOP_CANCELLABLE = () -> false;
/*     */   
/*     */   public static class CompletedFuture<T>
/*     */     implements Future<T>
/*     */   {
/*     */     private final T result;
/*     */     
/*     */     public CompletedFuture(T result) {
/*  57 */       this.result = result;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get() throws InterruptedException, ExecutionException {
/*  62 */       return this.result;
/*     */     }
/*     */ 
/*     */     
/*     */     public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  67 */       return this.result;
/*     */     }
/*     */     
/*     */     public boolean cancel(boolean mayInterruptIfRunning) {
/*  71 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCancelled() {
/*  76 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDone() {
/*  81 */       return true;
/*     */     }
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
/*     */   public static Cancellable nonCancellable() {
/*  94 */     return NOOP_CANCELLABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Cancellable cancellable(Future<?> future) {
/* 105 */     if (future == null) {
/* 106 */       return NOOP_CANCELLABLE;
/*     */     }
/* 108 */     if (future instanceof Cancellable) {
/* 109 */       return (Cancellable)future;
/*     */     }
/* 111 */     return () -> future.cancel(true);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/Operations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */