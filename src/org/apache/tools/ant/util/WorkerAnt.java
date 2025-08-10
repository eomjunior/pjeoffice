/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorkerAnt
/*     */   extends Thread
/*     */ {
/*     */   private Task task;
/*     */   private Object notify;
/*     */   private volatile boolean finished = false;
/*     */   private volatile BuildException buildException;
/*     */   private volatile Throwable exception;
/*     */   public static final String ERROR_NO_TASK = "No task defined";
/*     */   
/*     */   public WorkerAnt(Task task, Object notify) {
/*  58 */     this.task = task;
/*  59 */     this.notify = (notify != null) ? notify : this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WorkerAnt(Task task) {
/*  69 */     this(task, null);
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
/*     */   public synchronized BuildException getBuildException() {
/*  81 */     return this.buildException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Throwable getException() {
/*  90 */     return this.exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Task getTask() {
/*  99 */     return this.task;
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
/*     */   public synchronized boolean isFinished() {
/* 111 */     return this.finished;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void waitUntilFinished(long timeout) throws InterruptedException {
/* 120 */     long start = System.currentTimeMillis();
/* 121 */     long end = start + timeout;
/* 122 */     synchronized (this.notify) {
/* 123 */       long now = System.currentTimeMillis();
/* 124 */       while (!this.finished && now < end) {
/* 125 */         this.notify.wait(end - now);
/* 126 */         now = System.currentTimeMillis();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rethrowAnyBuildException() {
/* 137 */     BuildException ex = getBuildException();
/* 138 */     if (ex != null) {
/* 139 */       throw ex;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized void caught(Throwable thrown) {
/* 150 */     this.exception = thrown;
/* 151 */     this
/*     */       
/* 153 */       .buildException = (thrown instanceof BuildException) ? (BuildException)thrown : new BuildException(thrown);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/* 162 */       if (this.task != null) {
/* 163 */         this.task.execute();
/*     */       }
/* 165 */     } catch (Throwable thrown) {
/* 166 */       caught(thrown);
/*     */     } finally {
/* 168 */       synchronized (this.notify) {
/* 169 */         this.finished = true;
/*     */ 
/*     */         
/* 172 */         this.notify.notifyAll();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/WorkerAnt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */