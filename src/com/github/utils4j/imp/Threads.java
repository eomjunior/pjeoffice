/*     */ package com.github.utils4j.imp;
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
/*     */ public class Threads
/*     */ {
/*     */   public static void sleep(long time) {
/*  37 */     if (time > 0L) {
/*     */       try {
/*  39 */         safeSleep(time);
/*  40 */       } catch (InterruptedException e) {
/*  41 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     }
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
/*     */   public static void safeSleep(long millis) throws InterruptedException {
/*  55 */     if (millis == 0L)
/*     */       return; 
/*  57 */     long done = 0L;
/*     */     do {
/*  59 */       long before = System.currentTimeMillis();
/*  60 */       Thread.sleep(millis - done);
/*  61 */       long after = System.currentTimeMillis();
/*  62 */       done += after - before;
/*  63 */     } while (done < millis);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Thread startAsync(Runnable runnable) {
/*  68 */     return startAsync(runnable, 0L);
/*     */   }
/*     */   
/*     */   public static Thread startAsync(String threadName, Runnable runnable) {
/*  72 */     return startAsync(threadName, runnable, 0L);
/*     */   }
/*     */   
/*     */   public static Thread startAsync(String threadName, Runnable runnable, long delay) {
/*  76 */     return startThread(threadName, runnable, delay, false);
/*     */   }
/*     */   
/*     */   public static Thread startAsync(Runnable runnable, long delay) {
/*  80 */     return startThread(Strings.empty(), runnable, delay, false);
/*     */   }
/*     */   
/*     */   public static Thread startDaemon(Runnable runnable) {
/*  84 */     return startDaemon(Strings.empty(), runnable);
/*     */   }
/*     */   
/*     */   public static Thread startDaemon(String threadName, Runnable runnable) {
/*  88 */     return startDaemon(threadName, runnable, 0L);
/*     */   }
/*     */   
/*     */   public static Thread startDaemon(Runnable runnable, long delay) {
/*  92 */     return startDaemon(Strings.empty(), runnable, delay);
/*     */   }
/*     */   
/*     */   public static Thread startDaemon(String threadName, Runnable runnable, long delay) {
/*  96 */     return startThread(threadName, runnable, delay, true);
/*     */   }
/*     */   
/*     */   public static Thread startDaemon(String threadName, Runnable runnable, long delay, boolean parentName) {
/* 100 */     return startThread(threadName, runnable, delay, true, parentName);
/*     */   }
/*     */   
/*     */   private static Thread startThread(String threadName, Runnable runnable, long delay, boolean deamon) {
/* 104 */     return startThread(threadName, runnable, delay, deamon, true);
/*     */   }
/*     */   
/*     */   private static Thread startThread(String threadName, Runnable runnable, long delay, boolean deamon, boolean parentName) {
/* 108 */     Args.requireNonNull(threadName, "threadName is empty");
/* 109 */     Args.requireNonNull(runnable, "runnable is null");
/* 110 */     Args.requireZeroPositive(delay, "delay is negative");
/*     */ 
/*     */ 
/*     */     
/* 114 */     Thread t = new Thread(() -> { sleep(delay); runnable.run(); }Strings.truncate(threadName + (parentName ? (" from parent: " + Thread.currentThread().getName()) : ""), 120));
/* 115 */     t.setDaemon(deamon);
/* 116 */     t.start();
/* 117 */     return t;
/*     */   }
/*     */   
/*     */   public static ShutdownHookThread shutdownHook(Runnable runnable) {
/* 121 */     return shutdownHookAdd(runnable, "shutdownhook:" + Dates.stringNow());
/*     */   }
/*     */   
/*     */   public static ShutdownHookThread shutdownHookAdd(Runnable runnable, String name) {
/* 125 */     Args.requireText(name, "threadName is empty");
/* 126 */     Args.requireNonNull(runnable, "runnable is null");
/* 127 */     ShutdownHookThread t = new ShutdownHookThread(name, runnable);
/* 128 */     Runtime.getRuntime().addShutdownHook(t);
/* 129 */     return t;
/*     */   }
/*     */   
/*     */   public static void shutdownHookRem(ShutdownHookThread jvmHook) {
/* 133 */     if (!isShutdownHook())
/* 134 */       Throwables.quietly(() -> Runtime.getRuntime().removeShutdownHook(jvmHook), true); 
/*     */   }
/*     */   
/*     */   public static class ShutdownHookThread
/*     */     extends Thread {
/*     */     ShutdownHookThread(String name, Runnable r) {
/* 140 */       super(r, name);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean isShutdownHook() {
/* 145 */     return Thread.currentThread() instanceof ShutdownHookThread;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Threads.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */