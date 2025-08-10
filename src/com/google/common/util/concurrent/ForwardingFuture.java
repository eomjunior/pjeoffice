/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.ForwardingObject;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.Future;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingFuture<V>
/*    */   extends ForwardingObject
/*    */   implements Future<V>
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 50 */     return delegate().cancel(mayInterruptIfRunning);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCancelled() {
/* 55 */     return delegate().isCancelled();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDone() {
/* 60 */     return delegate().isDone();
/*    */   }
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   @CanIgnoreReturnValue
/*    */   public V get() throws InterruptedException, ExecutionException {
/* 67 */     return delegate().get();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   @CanIgnoreReturnValue
/*    */   public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 75 */     return delegate().get(timeout, unit);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Future<? extends V> delegate();
/*    */ 
/*    */   
/*    */   public static abstract class SimpleForwardingFuture<V>
/*    */     extends ForwardingFuture<V>
/*    */   {
/*    */     private final Future<V> delegate;
/*    */ 
/*    */     
/*    */     protected SimpleForwardingFuture(Future<V> delegate) {
/* 90 */       this.delegate = (Future<V>)Preconditions.checkNotNull(delegate);
/*    */     }
/*    */ 
/*    */     
/*    */     protected final Future<V> delegate() {
/* 95 */       return this.delegate;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/ForwardingFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */