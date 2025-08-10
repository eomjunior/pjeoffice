/*     */ package com.github.signer4j.imp;
/*     */ 
/*     */ import com.github.signer4j.IAuthStrategy;
/*     */ import com.github.signer4j.IToken;
/*     */ import com.github.signer4j.ITokenCycle;
/*     */ import com.github.signer4j.imp.exception.Signer4JException;
/*     */ import com.github.utils4j.imp.Args;
/*     */ import com.github.utils4j.imp.Threads;
/*     */ import java.util.concurrent.locks.ReentrantLock;
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
/*     */ public abstract class TokenCycle
/*     */   extends TokenWrapper
/*     */   implements ITokenCycle
/*     */ {
/*  42 */   private static long LOGOUT_BATCH_TIMEOUT = 2000L;
/*     */   
/*     */   private final ReentrantLock lock;
/*     */   
/*     */   protected volatile IAuthStrategy strategy;
/*     */   
/*  48 */   private volatile long deadline = -1L;
/*     */   
/*  50 */   private volatile int refCount = 0;
/*     */   
/*     */   public TokenCycle(IToken token, IAuthStrategy strategy, ReentrantLock lock) {
/*  53 */     super(token);
/*  54 */     this.strategy = (IAuthStrategy)Args.requireNonNull(strategy, "strategy is null");
/*  55 */     this.lock = (ReentrantLock)Args.requireNonNull(lock, "lock is null");
/*     */   }
/*     */   
/*     */   private boolean hasDeadline() {
/*  59 */     return (this.deadline > 0L);
/*     */   }
/*     */   
/*     */   private boolean isBeforeDeadline() {
/*  63 */     return (hasDeadline() && System.currentTimeMillis() < this.deadline);
/*     */   }
/*     */   
/*     */   private boolean isPastDeadline() {
/*  67 */     return (hasDeadline() && System.currentTimeMillis() >= this.deadline);
/*     */   }
/*     */   
/*     */   private boolean hasTimeout() {
/*  71 */     return isPastDeadline();
/*     */   }
/*     */   
/*     */   private boolean isUsing() {
/*  75 */     return (this.refCount > 0 || isBeforeDeadline());
/*     */   }
/*     */   
/*     */   public final void dispose() {
/*  79 */     logout(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public IToken login() throws Signer4JException {
/*     */     try {
/*  85 */       this.lock.lock();
/*  86 */       this.strategy.login(this.token, isUsing());
/*  87 */       this.refCount++;
/*     */     } finally {
/*  89 */       this.lock.unlock();
/*     */     } 
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void logout() {
/*     */     try {
/*  97 */       this.lock.lock();
/*  98 */       if (this.refCount > 0 && --this.refCount == 0) {
/*  99 */         logoutAsync();
/*     */       }
/*     */     } finally {
/* 102 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void logout(boolean force) {
/* 108 */     if (force) {
/*     */       try {
/* 110 */         this.lock.lock();
/* 111 */         super.logout();
/* 112 */         this.refCount = 0;
/* 113 */         this.deadline = -1L;
/*     */       } finally {
/* 115 */         this.lock.unlock();
/*     */       } 
/*     */       return;
/*     */     } 
/* 119 */     logout();
/*     */   }
/*     */ 
/*     */   
/*     */   private void logoutAsync() {
/* 124 */     this.deadline = System.currentTimeMillis() + LOGOUT_BATCH_TIMEOUT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     Runnable code = () -> {
/*     */         while (true) {
/*     */           long waitingTime;
/*     */           
/*     */           try {
/*     */             this.lock.lock();
/*     */             if (this.refCount > 0 || !hasDeadline()) {
/*     */               return;
/*     */             }
/*     */             if (hasTimeout()) {
/*     */               try {
/*     */                 this.strategy.logout(this.token);
/*     */               } finally {
/*     */                 this.deadline = -1L;
/*     */               } 
/*     */               return;
/*     */             } 
/*     */             waitingTime = this.deadline - System.currentTimeMillis();
/*     */           } finally {
/*     */             this.lock.unlock();
/*     */           } 
/*     */           Threads.sleep(waitingTime + 10L);
/*     */         } 
/*     */       };
/* 155 */     Threads.startDaemon("batch-logout", code, LOGOUT_BATCH_TIMEOUT + 10L);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/signer4j/imp/TokenCycle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */