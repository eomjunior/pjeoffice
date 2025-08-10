/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import com.google.errorprone.annotations.CheckReturnValue;
/*    */ import java.util.concurrent.AbstractExecutorService;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.Future;
/*    */ import java.util.concurrent.RunnableFuture;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CheckReturnValue
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtIncompatible
/*    */ @J2ktIncompatible
/*    */ public abstract class AbstractListeningExecutorService
/*    */   extends AbstractExecutorService
/*    */   implements ListeningExecutorService
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   protected final <T> RunnableFuture<T> newTaskFor(Runnable runnable, @ParametricNullness T value) {
/* 51 */     return TrustedListenableFutureTask.create(runnable, value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   protected final <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
/* 60 */     return TrustedListenableFutureTask.create(callable);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public ListenableFuture<?> submit(Runnable task) {
/* 66 */     return (ListenableFuture)super.submit(task);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public <T> ListenableFuture<T> submit(Runnable task, @ParametricNullness T result) {
/* 73 */     return (ListenableFuture<T>)super.<T>submit(task, result);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public <T> ListenableFuture<T> submit(Callable<T> task) {
/* 79 */     return (ListenableFuture<T>)super.<T>submit(task);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/AbstractListeningExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */