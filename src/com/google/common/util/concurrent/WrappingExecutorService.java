/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ abstract class WrappingExecutorService
/*     */   implements ExecutorService
/*     */ {
/*     */   private final ExecutorService delegate;
/*     */   
/*     */   protected WrappingExecutorService(ExecutorService delegate) {
/*  53 */     this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
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
/*     */   protected Runnable wrapTask(Runnable command) {
/*  67 */     Callable<Object> wrapped = wrapTask(Executors.callable(command, null));
/*  68 */     return () -> {
/*     */         try {
/*     */           wrapped.call();
/*  71 */         } catch (Exception e) {
/*     */           Platform.restoreInterruptIfIsInterruptedException(e);
/*     */           Throwables.throwIfUnchecked(e);
/*     */           throw new RuntimeException(e);
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> ImmutableList<Callable<T>> wrapTasks(Collection<? extends Callable<T>> tasks) {
/*  86 */     ImmutableList.Builder<Callable<T>> builder = ImmutableList.builder();
/*  87 */     for (Callable<T> task : tasks) {
/*  88 */       builder.add(wrapTask(task));
/*     */     }
/*  90 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void execute(Runnable command) {
/*  96 */     this.delegate.execute(wrapTask(command));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> Future<T> submit(Callable<T> task) {
/* 101 */     return this.delegate.submit(wrapTask((Callable<T>)Preconditions.checkNotNull(task)));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Future<?> submit(Runnable task) {
/* 106 */     return this.delegate.submit(wrapTask(task));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> submit(Runnable task, @ParametricNullness T result) {
/* 112 */     return this.delegate.submit(wrapTask(task), result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
/* 118 */     return this.delegate.invokeAll((Collection<? extends Callable<T>>)wrapTasks(tasks));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
/* 125 */     return this.delegate.invokeAll((Collection<? extends Callable<T>>)wrapTasks(tasks), timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
/* 131 */     return this.delegate.invokeAny((Collection<? extends Callable<T>>)wrapTasks(tasks));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 138 */     return this.delegate.invokeAny((Collection<? extends Callable<T>>)wrapTasks(tasks), timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void shutdown() {
/* 145 */     this.delegate.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final List<Runnable> shutdownNow() {
/* 151 */     return this.delegate.shutdownNow();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isShutdown() {
/* 156 */     return this.delegate.isShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isTerminated() {
/* 161 */     return this.delegate.isTerminated();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 166 */     return this.delegate.awaitTermination(timeout, unit);
/*     */   }
/*     */   
/*     */   protected abstract <T> Callable<T> wrapTask(Callable<T> paramCallable);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/WrappingExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */