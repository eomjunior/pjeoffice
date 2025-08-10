/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ final class TimeoutFuture<V>
/*     */   extends FluentFuture.TrustedFuture<V>
/*     */ {
/*     */   @CheckForNull
/*     */   private ListenableFuture<V> delegateRef;
/*     */   @CheckForNull
/*     */   private ScheduledFuture<?> timer;
/*     */   
/*     */   static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
/*  47 */     TimeoutFuture<V> result = new TimeoutFuture<>(delegate);
/*  48 */     Fire<V> fire = new Fire<>(result);
/*  49 */     result.timer = scheduledExecutor.schedule(fire, time, unit);
/*  50 */     delegate.addListener(fire, MoreExecutors.directExecutor());
/*  51 */     return result;
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
/*     */   private TimeoutFuture(ListenableFuture<V> delegate) {
/*  82 */     this.delegateRef = (ListenableFuture<V>)Preconditions.checkNotNull(delegate);
/*     */   }
/*     */   
/*     */   private static final class Fire<V> implements Runnable {
/*     */     @CheckForNull
/*     */     TimeoutFuture<V> timeoutFutureRef;
/*     */     
/*     */     Fire(TimeoutFuture<V> timeoutFuture) {
/*  90 */       this.timeoutFutureRef = timeoutFuture;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*  97 */       TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
/*  98 */       if (timeoutFuture == null) {
/*     */         return;
/*     */       }
/* 101 */       ListenableFuture<V> delegate = timeoutFuture.delegateRef;
/* 102 */       if (delegate == null) {
/*     */         return;
/*     */       }
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
/* 118 */       this.timeoutFutureRef = null;
/* 119 */       if (delegate.isDone()) {
/* 120 */         timeoutFuture.setFuture(delegate);
/*     */       } else {
/*     */         try {
/* 123 */           ScheduledFuture<?> timer = timeoutFuture.timer;
/* 124 */           timeoutFuture.timer = null;
/* 125 */           String message = "Timed out";
/*     */ 
/*     */           
/*     */           try {
/* 129 */             if (timer != null) {
/* 130 */               long overDelayMs = Math.abs(timer.getDelay(TimeUnit.MILLISECONDS));
/* 131 */               if (overDelayMs > 10L) {
/* 132 */                 message = message + " (timeout delayed by " + overDelayMs + " ms after scheduled time)";
/*     */               }
/*     */             } 
/* 135 */             message = message + ": " + delegate;
/*     */           } finally {
/* 137 */             timeoutFuture.setException(new TimeoutFuture.TimeoutFutureException(message));
/*     */           } 
/*     */         } finally {
/* 140 */           delegate.cancel(true);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TimeoutFutureException extends TimeoutException {
/*     */     private TimeoutFutureException(String message) {
/* 148 */       super(message);
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized Throwable fillInStackTrace() {
/* 153 */       setStackTrace(new StackTraceElement[0]);
/* 154 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @CheckForNull
/*     */   protected String pendingToString() {
/* 161 */     ListenableFuture<? extends V> localInputFuture = this.delegateRef;
/* 162 */     ScheduledFuture<?> localTimer = this.timer;
/* 163 */     if (localInputFuture != null) {
/* 164 */       String message = "inputFuture=[" + localInputFuture + "]";
/* 165 */       if (localTimer != null) {
/* 166 */         long delay = localTimer.getDelay(TimeUnit.MILLISECONDS);
/*     */         
/* 168 */         if (delay > 0L) {
/* 169 */           message = message + ", remaining delay=[" + delay + " ms]";
/*     */         }
/*     */       } 
/* 172 */       return message;
/*     */     } 
/* 174 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterDone() {
/* 179 */     maybePropagateCancellationTo(this.delegateRef);
/*     */     
/* 181 */     Future<?> localTimer = this.timer;
/*     */ 
/*     */ 
/*     */     
/* 185 */     if (localTimer != null) {
/* 186 */       localTimer.cancel(false);
/*     */     }
/*     */     
/* 189 */     this.delegateRef = null;
/* 190 */     this.timer = null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/util/concurrent/TimeoutFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */