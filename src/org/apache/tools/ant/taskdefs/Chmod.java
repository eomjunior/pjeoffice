/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Chmod
/*     */   extends ExecuteOn
/*     */ {
/*  42 */   private FileSet defaultSet = new FileSet();
/*     */   
/*     */   private boolean defaultSetDefined = false;
/*     */   
/*     */   private boolean havePerm = false;
/*     */ 
/*     */   
/*     */   public Chmod() {
/*  50 */     super.setExecutable("chmod");
/*  51 */     setParallel(true);
/*  52 */     super.setSkipEmptyFilesets(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/*  63 */     super.setProject(project);
/*  64 */     this.defaultSet.setProject(project);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File src) {
/*  72 */     FileSet fs = new FileSet();
/*  73 */     fs.setFile(src);
/*  74 */     addFileset(fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File src) {
/*  83 */     this.defaultSet.setDir(src);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPerm(String perm) {
/*  91 */     createArg().setValue(perm);
/*  92 */     this.havePerm = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createInclude() {
/* 100 */     this.defaultSetDefined = true;
/* 101 */     return this.defaultSet.createInclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet.NameEntry createExclude() {
/* 109 */     this.defaultSetDefined = true;
/* 110 */     return this.defaultSet.createExclude();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternSet createPatternSet() {
/* 118 */     this.defaultSetDefined = true;
/* 119 */     return this.defaultSet.createPatternSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludes(String includes) {
/* 129 */     this.defaultSetDefined = true;
/* 130 */     this.defaultSet.setIncludes(includes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExcludes(String excludes) {
/* 140 */     this.defaultSetDefined = true;
/* 141 */     this.defaultSet.setExcludes(excludes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultexcludes(boolean useDefaultExcludes) {
/* 152 */     this.defaultSetDefined = true;
/* 153 */     this.defaultSet.setDefaultexcludes(useDefaultExcludes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkConfiguration() {
/* 161 */     if (!this.havePerm) {
/* 162 */       throw new BuildException("Required attribute perm not set in chmod", 
/* 163 */           getLocation());
/*     */     }
/*     */     
/* 166 */     if (this.defaultSetDefined && this.defaultSet.getDir(getProject()) != null) {
/* 167 */       addFileset(this.defaultSet);
/*     */     }
/* 169 */     super.checkConfiguration();
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
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 184 */     if (this.defaultSetDefined || this.defaultSet.getDir(getProject()) == null) {
/*     */       try {
/* 186 */         super.execute();
/*     */       } finally {
/* 188 */         if (this.defaultSetDefined && this.defaultSet.getDir(getProject()) != null) {
/* 189 */           this.filesets.removeElement(this.defaultSet);
/*     */         }
/*     */       } 
/* 192 */     } else if (isValidOs()) {
/*     */       
/* 194 */       Execute execute = prepareExec();
/* 195 */       Commandline cloned = (Commandline)this.cmdl.clone();
/* 196 */       cloned.createArgument().setValue(this.defaultSet.getDir(getProject())
/* 197 */           .getPath());
/*     */       try {
/* 199 */         execute.setCommandline(cloned.getCommandline());
/* 200 */         runExecute(execute);
/* 201 */       } catch (IOException e) {
/* 202 */         throw new BuildException("Execute failed: " + e, e, getLocation());
/*     */       } finally {
/*     */         
/* 205 */         logFlush();
/*     */       } 
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
/*     */   
/*     */   public void setExecutable(String e) {
/* 219 */     throw new BuildException(getTaskType() + " doesn't support the executable attribute", 
/* 220 */         getLocation());
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
/*     */   public void setCommand(Commandline cmdl) {
/* 232 */     throw new BuildException(getTaskType() + " doesn't support the command attribute", 
/* 233 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSkipEmptyFilesets(boolean skip) {
/* 244 */     throw new BuildException(getTaskType() + " doesn't support the skipemptyfileset attribute", 
/* 245 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddsourcefile(boolean b) {
/* 256 */     throw new BuildException(getTaskType() + " doesn't support the addsourcefile attribute", 
/* 257 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidOs() {
/* 267 */     return (getOs() == null && getOsFamily() == null) ? 
/* 268 */       Os.isFamily("unix") : super.isValidOs();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Chmod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */