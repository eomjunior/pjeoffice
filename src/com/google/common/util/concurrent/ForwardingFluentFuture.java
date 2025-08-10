/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.TimeoutException;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ final class ForwardingFluentFuture<V>
/*    */   extends FluentFuture<V>
/*    */ {
/*    */   private final ListenableFuture<V> delegate;
/*    */   
/*    */   ForwardingFluentFuture(ListenableFuture<V> delegate) {
/* 42 */     this.delegate = (ListenableFuture<V>)Preconditions.checkNotNull(delegate);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addListener(Runnable listener, Executor executor) {
/* 47 */     this.delegate.addListener(listener, executor);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 52 */     return this.delegate.cancel(mayInterruptIfRunning);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 57 */     return this.delegate.isCancelled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 62 */     return this.delegate.isDone();
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   public V get() throws InterruptedException, ExecutionException {
/* 68 */     return this.delegate.get();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 75 */     return this.delegate.get(timeout, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 80 */     return this.delegate.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ForwardingFluentFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */