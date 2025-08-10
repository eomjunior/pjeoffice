/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.util.TimeoutObserver;
/*     */ import org.apache.tools.ant.util.Watchdog;
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
/*     */ public class ExecuteWatchdog
/*     */   implements TimeoutObserver
/*     */ {
/*     */   private Process process;
/*     */   private volatile boolean watch = false;
/*  51 */   private Exception caught = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean killedProcess = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private Watchdog watchdog;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecuteWatchdog(long timeout) {
/*  66 */     this.watchdog = new Watchdog(timeout);
/*  67 */     this.watchdog.addTimeoutObserver(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ExecuteWatchdog(int timeout) {
/*  79 */     this(timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start(Process process) {
/*  89 */     if (process == null) {
/*  90 */       throw new NullPointerException("process is null.");
/*     */     }
/*  92 */     if (this.process != null) {
/*  93 */       throw new IllegalStateException("Already running.");
/*     */     }
/*  95 */     this.caught = null;
/*  96 */     this.killedProcess = false;
/*  97 */     this.watch = true;
/*  98 */     this.process = process;
/*  99 */     this.watchdog.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() {
/* 107 */     this.watchdog.stop();
/* 108 */     cleanUp();
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
/*     */   public synchronized void timeoutOccured(Watchdog w) {
/*     */     try {
/*     */       try {
/* 122 */         this.process.exitValue();
/* 123 */       } catch (IllegalThreadStateException itse) {
/*     */ 
/*     */         
/* 126 */         if (this.watch) {
/* 127 */           this.killedProcess = true;
/* 128 */           this.process.destroy();
/*     */         } 
/*     */       } 
/* 131 */     } catch (Exception e) {
/* 132 */       this.caught = e;
/*     */     } finally {
/* 134 */       cleanUp();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected synchronized void cleanUp() {
/* 142 */     this.watch = false;
/* 143 */     this.process = null;
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
/*     */   public synchronized void checkException() throws BuildException {
/* 155 */     if (this.caught != null) {
/* 156 */       throw new BuildException("Exception in ExecuteWatchdog.run: " + this.caught
/* 157 */           .getMessage(), this.caught);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWatching() {
/* 167 */     return this.watch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean killedProcess() {
/* 176 */     return this.killedProcess;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ExecuteWatchdog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */