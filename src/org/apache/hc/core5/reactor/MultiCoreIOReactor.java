/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ class MultiCoreIOReactor
/*     */   implements IOReactor
/*     */ {
/*     */   private final IOReactor[] ioReactors;
/*     */   private final Thread[] threads;
/*     */   private final AtomicReference<IOReactorStatus> status;
/*     */   private final AtomicBoolean terminated;
/*     */   
/*     */   MultiCoreIOReactor(IOReactor[] ioReactors, Thread[] threads) {
/*  49 */     this.ioReactors = (IOReactor[])ioReactors.clone();
/*  50 */     this.threads = (Thread[])threads.clone();
/*  51 */     this.status = new AtomicReference<>(IOReactorStatus.INACTIVE);
/*  52 */     this.terminated = new AtomicBoolean();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOReactorStatus getStatus() {
/*  57 */     return this.status.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void start() {
/*  68 */     if (this.status.compareAndSet(IOReactorStatus.INACTIVE, IOReactorStatus.ACTIVE)) {
/*  69 */       for (int i = 0; i < this.threads.length; i++) {
/*  70 */         this.threads[i].start();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void initiateShutdown() {
/*  77 */     if (this.status.compareAndSet(IOReactorStatus.INACTIVE, IOReactorStatus.SHUT_DOWN) || this.status
/*  78 */       .compareAndSet(IOReactorStatus.ACTIVE, IOReactorStatus.SHUTTING_DOWN)) {
/*  79 */       for (int i = 0; i < this.ioReactors.length; i++) {
/*  80 */         IOReactor ioReactor = this.ioReactors[i];
/*  81 */         ioReactor.initiateShutdown();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitShutdown(TimeValue waitTime) throws InterruptedException {
/*  88 */     Args.notNull(waitTime, "Wait time");
/*  89 */     long deadline = System.currentTimeMillis() + waitTime.toMilliseconds();
/*  90 */     long remaining = waitTime.toMilliseconds(); int i;
/*  91 */     for (i = 0; i < this.ioReactors.length; i++) {
/*  92 */       IOReactor ioReactor = this.ioReactors[i];
/*  93 */       if (ioReactor.getStatus().compareTo(IOReactorStatus.SHUT_DOWN) < 0) {
/*  94 */         ioReactor.awaitShutdown(TimeValue.of(remaining, TimeUnit.MILLISECONDS));
/*  95 */         remaining = deadline - System.currentTimeMillis();
/*  96 */         if (remaining <= 0L) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/* 101 */     for (i = 0; i < this.threads.length; i++) {
/* 102 */       Thread thread = this.threads[i];
/* 103 */       thread.join(remaining);
/* 104 */       remaining = deadline - System.currentTimeMillis();
/* 105 */       if (remaining <= 0L) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close(CloseMode closeMode) {
/* 113 */     close(closeMode, Timeout.ofSeconds(5L));
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
/*     */   public void close(CloseMode closeMode, Timeout timeout) {
/* 128 */     if (closeMode == CloseMode.GRACEFUL) {
/* 129 */       initiateShutdown();
/*     */       try {
/* 131 */         awaitShutdown((TimeValue)timeout);
/* 132 */       } catch (InterruptedException e) {
/* 133 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/* 136 */     this.status.set(IOReactorStatus.SHUT_DOWN);
/* 137 */     if (this.terminated.compareAndSet(false, true)) {
/* 138 */       int i; for (i = 0; i < this.ioReactors.length; i++) {
/* 139 */         Closer.close(this.ioReactors[i], CloseMode.IMMEDIATE);
/*     */       }
/* 141 */       for (i = 0; i < this.threads.length; i++) {
/* 142 */         this.threads[i].interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/* 149 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     return getClass().getSimpleName() + " [status=" + this.status + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/MultiCoreIOReactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */