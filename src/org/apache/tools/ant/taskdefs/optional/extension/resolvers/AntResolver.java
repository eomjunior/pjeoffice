/*     */ package org.apache.tools.ant.taskdefs.optional.extension.resolvers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.Ant;
/*     */ import org.apache.tools.ant.taskdefs.optional.extension.Extension;
/*     */ import org.apache.tools.ant.taskdefs.optional.extension.ExtensionResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntResolver
/*     */   implements ExtensionResolver
/*     */ {
/*     */   private File antfile;
/*     */   private File destfile;
/*     */   private String target;
/*     */   
/*     */   public void setAntfile(File antfile) {
/*  43 */     this.antfile = antfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestfile(File destfile) {
/*  51 */     this.destfile = destfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(String target) {
/*  59 */     this.target = target;
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
/*     */   
/*     */   public File resolve(Extension extension, Project project) throws BuildException {
/*  72 */     validate();
/*     */     
/*  74 */     Ant ant = new Ant();
/*  75 */     ant.setProject(project);
/*  76 */     ant.setInheritAll(false);
/*  77 */     ant.setAntfile(this.antfile.getName());
/*     */ 
/*     */     
/*     */     try {
/*  81 */       File dir = this.antfile.getParentFile().getCanonicalFile();
/*  82 */       ant.setDir(dir);
/*  83 */     } catch (IOException ioe) {
/*  84 */       throw new BuildException(ioe.getMessage(), ioe);
/*     */     } 
/*     */     
/*  87 */     if (null != this.target) {
/*  88 */       ant.setTarget(this.target);
/*     */     }
/*     */     
/*  91 */     ant.execute();
/*     */     
/*  93 */     return this.destfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() {
/* 100 */     if (null == this.antfile) {
/* 101 */       String message = "Must specify Buildfile";
/* 102 */       throw new BuildException("Must specify Buildfile");
/*     */     } 
/*     */     
/* 105 */     if (null == this.destfile) {
/* 106 */       String message = "Must specify destination file";
/* 107 */       throw new BuildException("Must specify destination file");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     return "Ant[" + this.antfile + "==>" + this.destfile + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/resolvers/AntResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */