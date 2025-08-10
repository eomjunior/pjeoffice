/*     */ package org.apache.tools.ant.taskdefs;
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
/*     */ public class Sleep
/*     */   extends Task
/*     */ {
/*     */   private boolean failOnError = true;
/*  51 */   private int seconds = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private int hours = 0;
/*     */ 
/*     */ 
/*     */   
/*  60 */   private int minutes = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private int milliseconds = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSeconds(int seconds) {
/*  79 */     this.seconds = seconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHours(int hours) {
/*  88 */     this.hours = hours;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMinutes(int minutes) {
/*  97 */     this.minutes = minutes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMilliseconds(int milliseconds) {
/* 106 */     this.milliseconds = milliseconds;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doSleep(long millis) {
/*     */     try {
/* 116 */       Thread.sleep(millis);
/* 117 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean failOnError) {
/* 128 */     this.failOnError = failOnError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long getSleepTime() {
/* 139 */     return ((this.hours * 60L + this.minutes) * 60L + this.seconds) * 1000L + this.milliseconds;
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
/*     */   public void validate() throws BuildException {
/* 151 */     if (getSleepTime() < 0L) {
/* 152 */       throw new BuildException("Negative sleep periods are not supported");
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
/*     */   public void execute() throws BuildException {
/*     */     try {
/* 165 */       validate();
/* 166 */       long sleepTime = getSleepTime();
/* 167 */       log("sleeping for " + sleepTime + " milliseconds", 3);
/*     */       
/* 169 */       doSleep(sleepTime);
/* 170 */     } catch (Exception e) {
/* 171 */       if (this.failOnError) {
/* 172 */         throw new BuildException(e);
/*     */       }
/* 174 */       log(e.toString(), 0);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Sleep.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */