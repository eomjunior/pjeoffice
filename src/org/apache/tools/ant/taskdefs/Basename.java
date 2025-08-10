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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Basename
/*     */   extends Task
/*     */ {
/*     */   private File file;
/*     */   private String property;
/*     */   private String suffix;
/*     */   
/*     */   public void setFile(File file) {
/*  64 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String property) {
/*  72 */     this.property = property;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String suffix) {
/*  80 */     this.suffix = suffix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  90 */     if (this.property == null) {
/*  91 */       throw new BuildException("property attribute required", getLocation());
/*     */     }
/*  93 */     if (this.file == null) {
/*  94 */       throw new BuildException("file attribute required", getLocation());
/*     */     }
/*  96 */     getProject().setNewProperty(this.property, 
/*  97 */         removeExtension(this.file.getName(), this.suffix));
/*     */   }
/*     */   
/*     */   private String removeExtension(String s, String ext) {
/* 101 */     if (ext == null || !s.endsWith(ext)) {
/* 102 */       return s;
/*     */     }
/* 104 */     int clipFrom = s.length() - ext.length();
/*     */ 
/*     */ 
/*     */     
/* 108 */     if (ext.charAt(0) != '.' && clipFrom > 0 && s.charAt(clipFrom - 1) == '.') {
/* 109 */       clipFrom--;
/*     */     }
/* 111 */     return s.substring(0, clipFrom);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Basename.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */