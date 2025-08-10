/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
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
/*     */ public class Mkdir
/*     */   extends Task
/*     */ {
/*     */   private static final int MKDIR_RETRY_SLEEP_MILLIS = 10;
/*     */   private File dir;
/*     */   
/*     */   public void execute() throws BuildException {
/*  51 */     if (this.dir == null) {
/*  52 */       throw new BuildException("dir attribute is required", getLocation());
/*     */     }
/*     */     
/*  55 */     if (this.dir.isFile()) {
/*  56 */       throw new BuildException("Unable to create directory as a file already exists with that name: %s", new Object[] { this.dir
/*     */             
/*  58 */             .getAbsolutePath() });
/*     */     }
/*     */     
/*  61 */     if (!this.dir.exists()) {
/*  62 */       boolean result = mkdirs(this.dir);
/*  63 */       if (!result) {
/*  64 */         if (this.dir.exists()) {
/*  65 */           log("A different process or task has already created dir " + this.dir
/*  66 */               .getAbsolutePath(), 3);
/*     */           return;
/*     */         } 
/*  69 */         throw new BuildException("Directory " + this.dir
/*  70 */             .getAbsolutePath() + " creation was not successful for an unknown reason", 
/*     */             
/*  72 */             getLocation());
/*     */       } 
/*  74 */       log("Created dir: " + this.dir.getAbsolutePath());
/*     */     } else {
/*  76 */       log("Skipping " + this.dir.getAbsolutePath() + " because it already exists.", 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File dir) {
/*  87 */     this.dir = dir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDir() {
/*  95 */     return this.dir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mkdirs(File f) {
/* 104 */     if (!f.mkdirs()) {
/*     */       try {
/* 106 */         Thread.sleep(10L);
/* 107 */         return f.mkdirs();
/* 108 */       } catch (InterruptedException ex) {
/* 109 */         return f.mkdirs();
/*     */       } 
/*     */     }
/* 112 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Mkdir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */