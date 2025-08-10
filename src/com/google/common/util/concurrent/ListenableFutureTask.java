/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.FutureTask;
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
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public class ListenableFutureTask<V>
/*     */   extends FutureTask<V>
/*     */   implements ListenableFuture<V>
/*     */ {
/*  54 */   private final ExecutionList executionList = new ExecutionList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <V> ListenableFutureTask<V> create(Callable<V> callable) {
/*  64 */     return new ListenableFutureTask<>(callable);
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
/*     */   public static <V> ListenableFutureTask<V> create(Runnable runnable, @ParametricNullness V result) {
/*  79 */     return new ListenableFutureTask<>(runnable, result);
/*     */   }
/*     */   
/*     */   ListenableFutureTask(Callable<V> callable) {
/*  83 */     super(callable);
/*     */   }
/*     */   
/*     */   ListenableFutureTask(Runnable runnable, @ParametricNullness V result) {
/*  87 */     super(runnable, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addListener(Runnable listener, Executor exec) {
/*  92 */     this.executionList.add(listener, exec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ParametricNullness
/*     */   @CanIgnoreReturnValue
/*     */   public V get(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException, ExecutionException {
/* 101 */     long timeoutNanos = unit.toNanos(timeout);
/* 102 */     if (timeoutNanos <= 2147483647999999999L) {
/* 103 */       return super.get(timeout, unit);
/*     */     }
/*     */     
/* 106 */     return super.get(
/* 107 */         Math.min(timeoutNanos, 2147483647999999999L), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void done() {
/* 113 */     this.executionList.execute();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ListenableFutureTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */