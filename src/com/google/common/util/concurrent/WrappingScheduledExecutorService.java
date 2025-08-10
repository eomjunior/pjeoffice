/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.annotations.J2ktIncompatible;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.ScheduledFuture;
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
/*    */ @ElementTypesAreNonnullByDefault
/*    */ @J2ktIncompatible
/*    */ @GwtIncompatible
/*    */ abstract class WrappingScheduledExecutorService
/*    */   extends WrappingExecutorService
/*    */   implements ScheduledExecutorService
/*    */ {
/*    */   final ScheduledExecutorService delegate;
/*    */   
/*    */   protected WrappingScheduledExecutorService(ScheduledExecutorService delegate) {
/* 41 */     super(delegate);
/* 42 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */   
/*    */   public final ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 47 */     return this.delegate.schedule(wrapTask(command), delay, unit);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final <V> ScheduledFuture<V> schedule(Callable<V> task, long delay, TimeUnit unit) {
/* 53 */     return this.delegate.schedule(wrapTask(task), delay, unit);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 59 */     return this.delegate.scheduleAtFixedRate(wrapTask(command), initialDelay, period, unit);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 65 */     return this.delegate.scheduleWithFixedDelay(wrapTask(command), initialDelay, delay, unit);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/WrappingScheduledExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */