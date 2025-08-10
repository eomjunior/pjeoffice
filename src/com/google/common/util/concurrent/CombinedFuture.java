/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
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
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ final class CombinedFuture<V>
/*     */   extends AggregateFuture<Object, V>
/*     */ {
/*     */   @CheckForNull
/*     */   private CombinedFutureInterruptibleTask<?> task;
/*     */   
/*     */   CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, AsyncCallable<V> callable) {
/*  43 */     super(futures, allMustSucceed, false);
/*  44 */     this.task = new AsyncCallableInterruptibleTask(callable, listenerExecutor);
/*  45 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, Callable<V> callable) {
/*  53 */     super(futures, allMustSucceed, false);
/*  54 */     this.task = new CallableInterruptibleTask(callable, listenerExecutor);
/*  55 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   void collectOneValue(int index, @CheckForNull Object returnValue) {}
/*     */ 
/*     */   
/*     */   void handleAllCompleted() {
/*  63 */     CombinedFutureInterruptibleTask<?> localTask = this.task;
/*  64 */     if (localTask != null) {
/*  65 */       localTask.execute();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void releaseResources(AggregateFuture.ReleaseResourcesReason reason) {
/*  71 */     super.releaseResources(reason);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     if (reason == AggregateFuture.ReleaseResourcesReason.OUTPUT_FUTURE_DONE) {
/*  80 */       this.task = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void interruptTask() {
/*  86 */     CombinedFutureInterruptibleTask<?> localTask = this.task;
/*  87 */     if (localTask != null) {
/*  88 */       localTask.interruptTask();
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class CombinedFutureInterruptibleTask<T>
/*     */     extends InterruptibleTask<T>
/*     */   {
/*     */     private final Executor listenerExecutor;
/*     */     
/*     */     CombinedFutureInterruptibleTask(Executor listenerExecutor) {
/*  98 */       this.listenerExecutor = (Executor)Preconditions.checkNotNull(listenerExecutor);
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean isDone() {
/* 103 */       return CombinedFuture.this.isDone();
/*     */     }
/*     */     
/*     */     final void execute() {
/*     */       try {
/* 108 */         this.listenerExecutor.execute(this);
/* 109 */       } catch (RejectedExecutionException e) {
/* 110 */         CombinedFuture.this.setException(e);
/*     */       } 
/*     */     }
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
/*     */     final void afterRanInterruptiblySuccess(@ParametricNullness T result) {
/* 127 */       CombinedFuture.this.task = null;
/*     */       
/* 129 */       setValue(result);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     final void afterRanInterruptiblyFailure(Throwable error) {
/* 135 */       CombinedFuture.this.task = null;
/*     */       
/* 137 */       if (error instanceof ExecutionException) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 142 */         CombinedFuture.this.setException(((ExecutionException)error).getCause());
/* 143 */       } else if (error instanceof java.util.concurrent.CancellationException) {
/* 144 */         CombinedFuture.this.cancel(false);
/*     */       } else {
/* 146 */         CombinedFuture.this.setException(error);
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void setValue(@ParametricNullness T param1T);
/*     */   }
/*     */   
/*     */   private final class AsyncCallableInterruptibleTask
/*     */     extends CombinedFutureInterruptibleTask<ListenableFuture<V>>
/*     */   {
/*     */     private final AsyncCallable<V> callable;
/*     */     
/*     */     AsyncCallableInterruptibleTask(AsyncCallable<V> callable, Executor listenerExecutor) {
/* 159 */       super(listenerExecutor);
/* 160 */       this.callable = (AsyncCallable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     ListenableFuture<V> runInterruptibly() throws Exception {
/* 165 */       ListenableFuture<V> result = this.callable.call();
/* 166 */       return (ListenableFuture<V>)Preconditions.checkNotNull(result, "AsyncCallable.call returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", this.callable);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setValue(ListenableFuture<V> value) {
/* 175 */       CombinedFuture.this.setFuture(value);
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 180 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private final class CallableInterruptibleTask
/*     */     extends CombinedFutureInterruptibleTask<V> {
/*     */     private final Callable<V> callable;
/*     */     
/*     */     CallableInterruptibleTask(Callable<V> callable, Executor listenerExecutor) {
/* 189 */       super(listenerExecutor);
/* 190 */       this.callable = (Callable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     @ParametricNullness
/*     */     V runInterruptibly() throws Exception {
/* 196 */       return this.callable.call();
/*     */     }
/*     */ 
/*     */     
/*     */     void setValue(@ParametricNullness V value) {
/* 201 */       CombinedFuture.this.set(value);
/*     */     }
/*     */ 
/*     */     
/*     */     String toPendingString() {
/* 206 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/CombinedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */