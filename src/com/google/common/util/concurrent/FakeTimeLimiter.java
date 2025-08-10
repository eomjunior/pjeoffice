/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ExecutionException;
/*    */ import java.util.concurrent.TimeUnit;
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
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ public final class FakeTimeLimiter
/*    */   implements TimeLimiter
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   public <T> T newProxy(T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit) {
/* 46 */     Preconditions.checkNotNull(target);
/* 47 */     Preconditions.checkNotNull(interfaceType);
/* 48 */     Preconditions.checkNotNull(timeoutUnit);
/* 49 */     return target;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   @CanIgnoreReturnValue
/*    */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws ExecutionException {
/* 57 */     Preconditions.checkNotNull(callable);
/* 58 */     Preconditions.checkNotNull(timeoutUnit);
/*    */     try {
/* 60 */       return callable.call();
/* 61 */     } catch (RuntimeException e) {
/* 62 */       throw new UncheckedExecutionException(e);
/* 63 */     } catch (Exception e) {
/* 64 */       Platform.restoreInterruptIfIsInterruptedException(e);
/* 65 */       throw new ExecutionException(e);
/* 66 */     } catch (Error e) {
/* 67 */       throw new ExecutionError(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @ParametricNullness
/*    */   @CanIgnoreReturnValue
/*    */   public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws ExecutionException {
/* 76 */     return callWithTimeout(callable, timeoutDuration, timeoutUnit);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) {
/* 82 */     Preconditions.checkNotNull(runnable);
/* 83 */     Preconditions.checkNotNull(timeoutUnit);
/*    */     try {
/* 85 */       runnable.run();
/* 86 */     } catch (Exception e) {
/* 87 */       throw new UncheckedExecutionException(e);
/* 88 */     } catch (Error e) {
/* 89 */       throw new ExecutionError(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void runUninterruptiblyWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) {
/* 96 */     runWithTimeout(runnable, timeoutDuration, timeoutUnit);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/FakeTimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */