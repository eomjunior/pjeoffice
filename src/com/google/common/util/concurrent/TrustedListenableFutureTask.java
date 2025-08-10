/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RunnableFuture;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ class TrustedListenableFutureTask<V>
/*     */   extends FluentFuture.TrustedFuture<V>
/*     */   implements RunnableFuture<V>
/*     */ {
/*     */   @CheckForNull
/*     */   private volatile InterruptibleTask<?> task;
/*     */   
/*     */   static <V> TrustedListenableFutureTask<V> create(AsyncCallable<V> callable) {
/*  40 */     return new TrustedListenableFutureTask<>(callable);
/*     */   }
/*     */   
/*     */   static <V> TrustedListenableFutureTask<V> create(Callable<V> callable) {
/*  44 */     return new TrustedListenableFutureTask<>(callable);
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
/*     */   static <V> TrustedListenableFutureTask<V> create(Runnable runnable, @ParametricNullness V result) {
/*  58 */     return new TrustedListenableFutureTask<>(Executors.callable(runnable, result));
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
/*     */   TrustedListenableFutureTask(Callable<V> callable) {
/*  71 */     this.task = new TrustedFutureInterruptibleTask(callable);
/*     */   }
/*     */   
/*     */   TrustedListenableFutureTask(AsyncCallable<V> callable) {
/*  75 */     this.task = new TrustedFutureInterruptibleAsyncTask(callable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  80 */     InterruptibleTask<?> localTask = this.task;
/*  81 */     if (localTask != null) {
/*  82 */       localTask.run();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     this.task = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterDone() {
/*  93 */     super.afterDone();
/*     */     
/*  95 */     if (wasInterrupted()) {
/*  96 */       InterruptibleTask<?> localTask = this.task;
/*  97 */       if (localTask != null) {
/*  98 */         localTask.interruptTask();
/*     */       }
/*     */     } 
/*     */     
/* 102 */     this.task = null;
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected String pendingToString() {
/* 108 */     InterruptibleTask<?> localTask = this.task;
/* 109 */     if (localTask != null) {
/* 110 */       return "task=[" + localTask + "]";
/*     */     }
/* 112 */     return super.pendingToString();
/*     */   }
/*     */   
/*     */   private final class TrustedFutureInterruptibleTask
/*     */     extends InterruptibleTask<V> {
/*     */     private final Callable<V> callable;
/*     */     
/*     */     TrustedFutureInterruptibleTask(Callable<V> callable) {
/* 120 */       this.callable = (Callable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isDone() {
/* 125 */       return TrustedListenableFutureTask.this.isDone();
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     V runInterruptibly() throws Exception {
/* 131 */       return this.callable.call();
/*     */     }
/*     */ 
/*     */     
/*     */     void afterRanInterruptiblySuccess(@ParametricNullness V result) {
/* 136 */       TrustedListenableFutureTask.this.set(result);
/*     */     }
/*     */ 
/*     */     
/*     */     void afterRanInterruptiblyFailure(Throwable error) {
/* 141 */       TrustedListenableFutureTask.this.setException(error);
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 146 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class TrustedFutureInterruptibleAsyncTask
/*     */     extends InterruptibleTask<ListenableFuture<V>>
/*     */   {
/*     */     private final AsyncCallable<V> callable;
/*     */     
/*     */     TrustedFutureInterruptibleAsyncTask(AsyncCallable<V> callable) {
/* 156 */       this.callable = (AsyncCallable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isDone() {
/* 161 */       return TrustedListenableFutureTask.this.isDone();
/*     */     }
/*     */ 
/*     */     
/*     */     ListenableFuture<V> runInterruptibly() throws Exception {
/* 166 */       return (ListenableFuture<V>)Preconditions.checkNotNull(this.callable
/* 167 */           .call(), "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void afterRanInterruptiblySuccess(ListenableFuture<V> result) {
/* 175 */       TrustedListenableFutureTask.this.setFuture(result);
/*     */     }
/*     */ 
/*     */     
/*     */     void afterRanInterruptiblyFailure(Throwable error) {
/* 180 */       TrustedListenableFutureTask.this.setException(error);
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 185 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/TrustedListenableFutureTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */