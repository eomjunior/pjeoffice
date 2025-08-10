/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.collect.ForwardingObject;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.CheckReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingExecutorService
/*     */   extends ForwardingObject
/*     */   implements ExecutorService
/*     */ {
/*     */   @CheckReturnValue
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/*  58 */     return delegate().awaitTermination(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
/*  64 */     return delegate().invokeAll(tasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
/*  71 */     return delegate().invokeAll(tasks, timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
/*  77 */     return delegate().invokeAny(tasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  84 */     return delegate().invokeAny(tasks, timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/*  89 */     return delegate().isShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/*  94 */     return delegate().isTerminated();
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/*  99 */     delegate().shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public List<Runnable> shutdownNow() {
/* 105 */     return delegate().shutdownNow();
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable command) {
/* 110 */     delegate().execute(command);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 115 */     return delegate().submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 120 */     return delegate().submit(task);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Runnable task, @ParametricNullness T result) {
/* 126 */     return delegate().submit(task, result);
/*     */   }
/*     */   
/*     */   protected abstract ExecutorService delegate();
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ForwardingExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */