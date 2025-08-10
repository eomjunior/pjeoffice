/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class BooleanTimeout
/*     */ {
/*  75 */   private static final Logger LOGGER = LoggerFactory.getLogger(BooleanTimeout.class);
/*     */   
/*     */   private static final int DEFAULT_TIMEOUT = 2000;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final long timeout;
/*     */   
/*     */   private Thread rollbackThread;
/*     */   
/*     */   private volatile long lastTime;
/*     */   
/*     */   private final Runnable timeoutCode;
/*     */   
/*  89 */   private final AtomicBoolean value = new AtomicBoolean(false);
/*     */   
/*  91 */   private final Set<Runnable> ephemeralTimeoutCodes = new HashSet<>(4);
/*     */   
/*     */   public BooleanTimeout(String name) {
/*  94 */     this(name, 2000L);
/*     */   }
/*     */   
/*     */   public BooleanTimeout(String name, Runnable timeoutCode) {
/*  98 */     this(name, 2000L, timeoutCode);
/*     */   }
/*     */   
/*     */   public BooleanTimeout(String name, long timeout) {
/* 102 */     this(name, timeout, () -> {
/*     */         
/*     */         });
/*     */   } public BooleanTimeout(String name, long timeout, Runnable timeoutCode) {
/* 106 */     this.name = Args.requireText(name, "name is empty");
/* 107 */     this.timeout = Args.requireZeroPositive(timeout, "timeout is negative");
/* 108 */     this.timeoutCode = Args.<Runnable>requireNonNull(timeoutCode, "timeoutCode is null");
/*     */   }
/*     */   
/*     */   public final long getTimeout() {
/* 112 */     return this.timeout;
/*     */   }
/*     */   
/*     */   private void reset() {
/* 116 */     this.lastTime = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   private long deadline() {
/* 120 */     return this.lastTime + this.timeout;
/*     */   }
/*     */   
/*     */   private long deadlineRemaining() {
/* 124 */     return deadline() - System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   private boolean hasTimeout() {
/* 128 */     return (deadline() <= System.currentTimeMillis());
/*     */   }
/*     */   
/*     */   private synchronized void start() {
/* 132 */     if (this.rollbackThread == null) {
/* 133 */       this.rollbackThread = Threads.startDaemon(this.name + ": rollback boolean to false timeout", this::roolbackToFalse);
/*     */     }
/*     */   }
/*     */   
/*     */   public final void setTrue() {
/* 138 */     this.value.set(true);
/* 139 */     isTrue();
/* 140 */     synchronized (this.value) {
/* 141 */       this.value.notifyAll();
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void setTrue(Runnable timeoutCode) {
/* 146 */     synchronized (this.ephemeralTimeoutCodes) {
/* 147 */       setTrue();
/* 148 */       addEphemeralTimeout(timeoutCode);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addEphemeralTimeout(Runnable timeoutCode) {
/* 153 */     if (timeoutCode != null) {
/* 154 */       this.ephemeralTimeoutCodes.add(timeoutCode);
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean isTrue() {
/* 159 */     reset();
/* 160 */     boolean state = this.value.get();
/* 161 */     if (state) {
/* 162 */       start();
/*     */     }
/* 164 */     return state;
/*     */   }
/*     */   
/*     */   public synchronized void shutdown() {
/* 168 */     if (this.rollbackThread != null) {
/* 169 */       this.rollbackThread.interrupt();
/* 170 */       this.rollbackThread = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void roolbackToFalse() {
/*     */     label34: do {
/* 177 */       while (!this.value.get()) {
/* 178 */         synchronized (this.value) {
/*     */           try {
/* 180 */             this.value.wait();
/* 181 */           } catch (InterruptedException e) {
/* 182 */             Thread.currentThread().interrupt();
/*     */             
/*     */             break label34;
/*     */           } 
/*     */         } 
/*     */       } 
/* 188 */       while (!hasTimeout()) {
/* 189 */         long waitingTime = deadlineRemaining();
/* 190 */         if (waitingTime > 0L) {
/* 191 */           synchronized (this.value) {
/*     */             try {
/* 193 */               this.value.wait(waitingTime + 20L);
/* 194 */             } catch (InterruptedException e) {
/* 195 */               Thread.currentThread().interrupt();
/*     */               
/*     */               break label34;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/* 202 */       this.value.set(false);
/*     */       
/* 204 */       LOGGER.debug(this.name + ": reset to false");
/*     */       
/* 206 */       runTimeout();
/*     */     }
/* 208 */     while (!Thread.currentThread().isInterrupted());
/*     */   }
/*     */   private void runTimeout() {
/*     */     List<Runnable> codes;
/* 212 */     Throwables.quietly(this.timeoutCode::run);
/*     */ 
/*     */     
/* 215 */     synchronized (this.ephemeralTimeoutCodes) {
/* 216 */       codes = new ArrayList<>(this.ephemeralTimeoutCodes.size());
/* 217 */       codes.addAll(this.ephemeralTimeoutCodes);
/* 218 */       this.ephemeralTimeoutCodes.clear();
/*     */     } 
/*     */     
/* 221 */     for (Runnable r : codes)
/* 222 */       Throwables.quietly(r::run); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/BooleanTimeout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */