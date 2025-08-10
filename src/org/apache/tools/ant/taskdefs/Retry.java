/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.TaskContainer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Retry
/*     */   extends Task
/*     */   implements TaskContainer
/*     */ {
/*     */   private Task nestedTask;
/*  39 */   private int retryCount = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   private int retryDelay = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addTask(Task t) {
/*  52 */     if (this.nestedTask != null) {
/*  53 */       throw new BuildException("The retry task container accepts a single nested task (which may be a sequential task container)");
/*     */     }
/*     */     
/*  56 */     this.nestedTask = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRetryCount(int n) {
/*  64 */     this.retryCount = n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRetryDelay(int retryDelay) {
/*  73 */     if (retryDelay < 0) {
/*  74 */       throw new BuildException("retryDelay must be a non-negative number");
/*     */     }
/*  76 */     this.retryDelay = retryDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  85 */     StringBuilder errorMessages = new StringBuilder();
/*  86 */     for (int i = 0; i <= this.retryCount; i++) {
/*     */       try {
/*  88 */         this.nestedTask.perform();
/*     */         break;
/*  90 */       } catch (Exception e) {
/*  91 */         String msg; errorMessages.append(e.getMessage());
/*  92 */         if (i >= this.retryCount) {
/*  93 */           throw new BuildException(String.format("Task [%s] failed after [%d] attempts; giving up.%nError messages:%n%s", new Object[] { this.nestedTask
/*     */                   
/*  95 */                   .getTaskName(), Integer.valueOf(this.retryCount), errorMessages }), getLocation());
/*     */         }
/*     */         
/*  98 */         if (this.retryDelay > 0) {
/*  99 */           msg = "Attempt [" + i + "]: error occurred; retrying after " + this.retryDelay + " ms...";
/*     */         } else {
/* 101 */           msg = "Attempt [" + i + "]: error occurred; retrying...";
/*     */         } 
/* 103 */         log(msg, e, 2);
/* 104 */         errorMessages.append(System.lineSeparator());
/* 105 */         if (this.retryDelay > 0)
/*     */           try {
/* 107 */             Thread.sleep(this.retryDelay);
/* 108 */           } catch (InterruptedException interruptedException) {} 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Retry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */