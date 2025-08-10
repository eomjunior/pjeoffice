/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileWatchdog
/*     */   extends Thread
/*     */ {
/*     */   public static final long DEFAULT_DELAY = 60000L;
/*     */   protected String filename;
/*  47 */   protected long delay = 60000L;
/*     */   
/*     */   File file;
/*  50 */   long lastModif = 0L;
/*     */   boolean warnedAlready = false;
/*     */   boolean interrupted = false;
/*     */   
/*     */   protected FileWatchdog(String filename) {
/*  55 */     super("FileWatchdog");
/*  56 */     this.filename = filename;
/*  57 */     this.file = new File(filename);
/*  58 */     setDaemon(true);
/*  59 */     checkAndConfigure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelay(long delay) {
/*  66 */     this.delay = delay;
/*     */   }
/*     */   
/*     */   protected abstract void doOnChange();
/*     */   
/*     */   protected void checkAndConfigure() {
/*     */     boolean fileExists;
/*     */     try {
/*  74 */       fileExists = this.file.exists();
/*  75 */     } catch (SecurityException e) {
/*  76 */       LogLog.warn("Was not allowed to read check file existance, file:[" + this.filename + "].");
/*  77 */       this.interrupted = true;
/*     */       
/*     */       return;
/*     */     } 
/*  81 */     if (fileExists) {
/*  82 */       long l = this.file.lastModified();
/*  83 */       if (l > this.lastModif) {
/*  84 */         this.lastModif = l;
/*  85 */         doOnChange();
/*  86 */         this.warnedAlready = false;
/*     */       }
/*     */     
/*  89 */     } else if (!this.warnedAlready) {
/*  90 */       LogLog.debug("[" + this.filename + "] does not exist.");
/*  91 */       this.warnedAlready = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  97 */     while (!this.interrupted) {
/*     */       try {
/*  99 */         Thread.sleep(this.delay);
/* 100 */       } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */       
/* 103 */       checkAndConfigure();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/helpers/FileWatchdog.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */