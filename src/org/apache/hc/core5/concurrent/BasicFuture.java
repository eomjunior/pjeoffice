/*     */ package org.apache.hc.core5.concurrent;
/*     */ 
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeoutValueException;
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
/*     */ 
/*     */ 
/*     */ public class BasicFuture<T>
/*     */   implements Future<T>, Cancellable
/*     */ {
/*     */   private final FutureCallback<T> callback;
/*     */   private volatile boolean completed;
/*     */   private volatile boolean cancelled;
/*     */   private volatile T result;
/*     */   private volatile Exception ex;
/*     */   
/*     */   public BasicFuture(FutureCallback<T> callback) {
/*  57 */     this.callback = callback;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  62 */     return this.cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  67 */     return this.completed;
/*     */   }
/*     */   
/*     */   private T getResult() throws ExecutionException {
/*  71 */     if (this.ex != null) {
/*  72 */       throw new ExecutionException(this.ex);
/*     */     }
/*  74 */     if (this.cancelled) {
/*  75 */       throw new CancellationException();
/*     */     }
/*  77 */     return this.result;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized T get() throws InterruptedException, ExecutionException {
/*  82 */     while (!this.completed) {
/*  83 */       wait();
/*     */     }
/*  85 */     return getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  91 */     Args.notNull(unit, "Time unit");
/*  92 */     long msecs = unit.toMillis(timeout);
/*  93 */     long startTime = (msecs <= 0L) ? 0L : System.currentTimeMillis();
/*  94 */     long waitTime = msecs;
/*  95 */     if (this.completed)
/*  96 */       return getResult(); 
/*  97 */     if (waitTime <= 0L) {
/*  98 */       throw TimeoutValueException.fromMilliseconds(msecs, msecs + Math.abs(waitTime));
/*     */     }
/*     */     while (true) {
/* 101 */       wait(waitTime);
/* 102 */       if (this.completed) {
/* 103 */         return getResult();
/*     */       }
/* 105 */       waitTime = msecs - System.currentTimeMillis() - startTime;
/* 106 */       if (waitTime <= 0L) {
/* 107 */         throw TimeoutValueException.fromMilliseconds(msecs, msecs + Math.abs(waitTime));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean completed(T result) {
/* 114 */     synchronized (this) {
/* 115 */       if (this.completed) {
/* 116 */         return false;
/*     */       }
/* 118 */       this.completed = true;
/* 119 */       this.result = result;
/* 120 */       notifyAll();
/*     */     } 
/* 122 */     if (this.callback != null) {
/* 123 */       this.callback.completed(result);
/*     */     }
/* 125 */     return true;
/*     */   }
/*     */   
/*     */   public boolean failed(Exception exception) {
/* 129 */     synchronized (this) {
/* 130 */       if (this.completed) {
/* 131 */         return false;
/*     */       }
/* 133 */       this.completed = true;
/* 134 */       this.ex = exception;
/* 135 */       notifyAll();
/*     */     } 
/* 137 */     if (this.callback != null) {
/* 138 */       this.callback.failed(exception);
/*     */     }
/* 140 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 145 */     synchronized (this) {
/* 146 */       if (this.completed) {
/* 147 */         return false;
/*     */       }
/* 149 */       this.completed = true;
/* 150 */       this.cancelled = true;
/* 151 */       notifyAll();
/*     */     } 
/* 153 */     if (this.callback != null) {
/* 154 */       this.callback.cancelled();
/*     */     }
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel() {
/* 161 */     return cancel(true);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/concurrent/BasicFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */