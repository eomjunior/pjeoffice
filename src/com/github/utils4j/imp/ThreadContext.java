/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.ILifeCycle;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ public abstract class ThreadContext<E extends Exception>
/*     */   implements ILifeCycle<E>
/*     */ {
/*  38 */   private final AtomicReference<Thread> context = new AtomicReference<>();
/*     */   private final String name;
/*     */   private final boolean deamon;
/*     */   
/*     */   public ThreadContext(String name) {
/*  43 */     this(name, false);
/*     */   }
/*     */   
/*     */   public ThreadContext(String name, boolean deamon) {
/*  47 */     this.deamon = deamon;
/*  48 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStarted() {
/*  53 */     return (this.context.get() != null);
/*     */   }
/*     */   
/*     */   protected void checkIsAlive() {
/*  57 */     States.requireTrue(isStarted(), "context not available");
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized void start() throws E {
/*  62 */     stop();
/*  63 */     this.context.set(new Thread(this.name)
/*     */         {
/*     */           public void run() {
/*     */             try {
/*  67 */               ThreadContext.this.beforeRun();
/*  68 */             } catch (Exception e) {
/*  69 */               ThreadContext.this.context.set(null);
/*  70 */               ThreadContext.this.handleException(e);
/*     */               return;
/*     */             } 
/*     */             try {
/*  74 */               ThreadContext.this.doRun();
/*  75 */             } catch (InterruptedException e) {
/*  76 */               interrupt();
/*  77 */             } catch (Exception e) {
/*  78 */               Throwables.quietly(() -> ThreadContext.this.handleException(e));
/*     */             } finally {
/*  80 */               Throwables.quietly(ThreadContext.this::afterRun);
/*  81 */               ThreadContext.this.context.set(null);
/*     */             } 
/*     */           }
/*     */         });
/*  85 */     ((Thread)this.context.get()).setDaemon(this.deamon);
/*  86 */     ((Thread)this.context.get()).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void stop() throws E {
/*  91 */     stop(0L);
/*     */   }
/*     */ 
/*     */   
/*     */   public final synchronized void stop(long timeout) throws E {
/*     */     Thread thread;
/*  97 */     if ((thread = this.context.getAndSet(null)) != null) {
/*  98 */       thread.interrupt();
/*     */       try {
/* 100 */         thread.join(timeout);
/* 101 */       } catch (InterruptedException e) {
/* 102 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void beforeRun() throws Exception {}
/*     */   
/*     */   protected void afterRun() throws Exception {}
/*     */   
/*     */   protected void handleException(Throwable e) {
/* 112 */     e.printStackTrace();
/*     */   }
/*     */   
/*     */   protected abstract void doRun() throws Exception;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/ThreadContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */