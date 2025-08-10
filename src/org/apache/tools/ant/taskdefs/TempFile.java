/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TempFile
/*     */   extends Task
/*     */ {
/*  45 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String property;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private File destDir = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String prefix;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   private String suffix = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean deleteOnExit;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean createFile;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/*  80 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestDir(File destDir) {
/*  90 */     this.destDir = destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrefix(String prefix) {
/*  99 */     this.prefix = prefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String suffix) {
/* 108 */     this.suffix = suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeleteOnExit(boolean deleteOnExit) {
/* 117 */     this.deleteOnExit = deleteOnExit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeleteOnExit() {
/* 125 */     return this.deleteOnExit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCreateFile(boolean createFile) {
/* 133 */     this.createFile = createFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCreateFile() {
/* 141 */     return this.createFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 151 */     if (this.property == null || this.property.isEmpty()) {
/* 152 */       throw new BuildException("no property specified");
/*     */     }
/* 154 */     if (this.destDir == null) {
/* 155 */       this.destDir = getProject().resolveFile(".");
/*     */     }
/* 157 */     File tfile = FILE_UTILS.createTempFile(getProject(), this.prefix, this.suffix, this.destDir, this.deleteOnExit, this.createFile);
/*     */     
/* 159 */     getProject().setNewProperty(this.property, tfile.toString());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/TempFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */