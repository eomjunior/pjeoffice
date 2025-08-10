/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class Watchdog
/*     */   implements Runnable
/*     */ {
/*     */   public static final String ERROR_INVALID_TIMEOUT = "timeout less than 1.";
/*  42 */   private List<TimeoutObserver> observers = Collections.synchronizedList(new ArrayList<>(1));
/*  43 */   private long timeout = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean stopped = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Watchdog(long timeout) {
/*  56 */     if (timeout < 1L) {
/*  57 */       throw new IllegalArgumentException("timeout less than 1.");
/*     */     }
/*  59 */     this.timeout = timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTimeoutObserver(TimeoutObserver to) {
/*  67 */     this.observers.add(to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTimeoutObserver(TimeoutObserver to) {
/*  75 */     this.observers.remove(to);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void fireTimeoutOccured() {
/*  83 */     this.observers.forEach(o -> o.timeoutOccured(this));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/*  90 */     this.stopped = false;
/*  91 */     Thread t = new Thread(this, "WATCHDOG");
/*  92 */     t.setDaemon(true);
/*  93 */     t.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() {
/* 100 */     this.stopped = true;
/* 101 */     notifyAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void run() {
/* 112 */     long now = System.currentTimeMillis();
/* 113 */     long until = now + this.timeout;
/*     */     
/*     */     try {
/* 116 */       while (!this.stopped && until > now) {
/* 117 */         wait(until - now);
/* 118 */         now = System.currentTimeMillis();
/*     */       } 
/* 120 */     } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */     
/* 123 */     if (!this.stopped)
/* 124 */       fireTimeoutOccured(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/Watchdog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */