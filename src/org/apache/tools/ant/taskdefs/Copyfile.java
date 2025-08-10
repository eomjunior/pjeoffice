/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ @Deprecated
/*     */ public class Copyfile
/*     */   extends Task
/*     */ {
/*     */   private File srcFile;
/*     */   private File destFile;
/*     */   private boolean filtering = false;
/*     */   private boolean forceOverwrite = false;
/*     */   
/*     */   public void setSrc(File src) {
/*  50 */     this.srcFile = src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForceoverwrite(boolean force) {
/*  60 */     this.forceOverwrite = force;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File dest) {
/*  68 */     this.destFile = dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiltering(String filter) {
/*  77 */     this.filtering = Project.toBoolean(filter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  86 */     log("DEPRECATED - The copyfile task is deprecated.  Use copy instead.");
/*     */     
/*  88 */     if (this.srcFile == null) {
/*  89 */       throw new BuildException("The src attribute must be present.", 
/*  90 */           getLocation());
/*     */     }
/*     */     
/*  93 */     if (!this.srcFile.exists()) {
/*  94 */       throw new BuildException("src " + this.srcFile.toString() + " does not exist.", 
/*  95 */           getLocation());
/*     */     }
/*     */     
/*  98 */     if (this.destFile == null) {
/*  99 */       throw new BuildException("The dest attribute must be present.", 
/* 100 */           getLocation());
/*     */     }
/*     */     
/* 103 */     if (this.srcFile.equals(this.destFile)) {
/* 104 */       log("Warning: src == dest", 1);
/*     */     }
/*     */     
/* 107 */     if (this.forceOverwrite || this.srcFile
/* 108 */       .lastModified() > this.destFile.lastModified())
/*     */       try {
/* 110 */         getProject().copyFile(this.srcFile, this.destFile, this.filtering, this.forceOverwrite);
/* 111 */       } catch (IOException ioe) {
/* 112 */         throw new BuildException("Error copying file: " + this.srcFile
/* 113 */             .getAbsolutePath() + " due to " + ioe
/* 114 */             .getMessage());
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Copyfile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */