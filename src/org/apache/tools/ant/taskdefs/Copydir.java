/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class Copydir
/*     */   extends MatchingTask
/*     */ {
/*     */   private File srcDir;
/*     */   private File destDir;
/*     */   private boolean filtering = false;
/*     */   private boolean flatten = false;
/*     */   private boolean forceOverwrite = false;
/*  46 */   private Map<String, String> filecopyList = new Hashtable<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrc(File src) {
/*  54 */     this.srcDir = src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File dest) {
/*  63 */     this.destDir = dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiltering(boolean filter) {
/*  72 */     this.filtering = filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFlatten(boolean flatten) {
/*  81 */     this.flatten = flatten;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForceoverwrite(boolean force) {
/*  91 */     this.forceOverwrite = force;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 100 */     log("DEPRECATED - The copydir task is deprecated.  Use copy instead.");
/*     */     
/* 102 */     if (this.srcDir == null) {
/* 103 */       throw new BuildException("src attribute must be set!", 
/* 104 */           getLocation());
/*     */     }
/*     */     
/* 107 */     if (!this.srcDir.exists()) {
/* 108 */       throw new BuildException("srcdir " + this.srcDir.toString() + " does not exist!", 
/* 109 */           getLocation());
/*     */     }
/*     */     
/* 112 */     if (this.destDir == null) {
/* 113 */       throw new BuildException("The dest attribute must be set.", 
/* 114 */           getLocation());
/*     */     }
/*     */     
/* 117 */     if (this.srcDir.equals(this.destDir)) {
/* 118 */       log("Warning: src == dest", 1);
/*     */     }
/*     */     
/* 121 */     DirectoryScanner ds = getDirectoryScanner(this.srcDir);
/*     */     
/*     */     try {
/* 124 */       scanDir(this.srcDir, this.destDir, ds.getIncludedFiles());
/* 125 */       if (this.filecopyList.size() > 0) {
/* 126 */         log("Copying " + this.filecopyList.size() + " file" + (
/* 127 */             (this.filecopyList.size() == 1) ? "" : "s") + " to " + this.destDir
/* 128 */             .getAbsolutePath());
/* 129 */         for (Map.Entry<String, String> e : this.filecopyList.entrySet()) {
/* 130 */           String fromFile = e.getKey();
/* 131 */           String toFile = e.getValue();
/*     */           try {
/* 133 */             getProject().copyFile(fromFile, toFile, this.filtering, this.forceOverwrite);
/*     */           }
/* 135 */           catch (IOException ioe) {
/*     */             
/* 137 */             String msg = "Failed to copy " + fromFile + " to " + toFile + " due to " + ioe.getMessage();
/* 138 */             throw new BuildException(msg, ioe, getLocation());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 143 */       this.filecopyList.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scanDir(File from, File to, String[] files) {
/* 148 */     for (String filename : files) {
/* 149 */       File destFile, srcFile = new File(from, filename);
/*     */       
/* 151 */       if (this.flatten) {
/* 152 */         destFile = new File(to, (new File(filename)).getName());
/*     */       } else {
/* 154 */         destFile = new File(to, filename);
/*     */       } 
/* 156 */       if (this.forceOverwrite || srcFile
/* 157 */         .lastModified() > destFile.lastModified())
/* 158 */         this.filecopyList.put(srcFile.getAbsolutePath(), destFile
/* 159 */             .getAbsolutePath()); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Copydir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */