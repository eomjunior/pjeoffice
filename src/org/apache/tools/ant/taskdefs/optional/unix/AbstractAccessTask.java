/*     */ package org.apache.tools.ant.taskdefs.optional.unix;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteOn;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAccessTask
/*     */   extends ExecuteOn
/*     */ {
/*     */   public AbstractAccessTask() {
/*  52 */     setParallel(true);
/*  53 */     super.setSkipEmptyFilesets(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File src) {
/*  61 */     FileSet fs = new FileSet();
/*  62 */     fs.setFile(src);
/*  63 */     addFileset(fs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommand(Commandline cmdl) {
/*  74 */     throw new BuildException(getTaskType() + " doesn't support the command attribute", 
/*     */         
/*  76 */         getLocation());
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
/*  87 */     throw new BuildException(getTaskType() + " doesn't support the skipemptyfileset attribute", 
/*     */         
/*  89 */         getLocation());
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
/* 100 */     throw new BuildException(getTaskType() + " doesn't support the addsourcefile attribute", 
/* 101 */         getLocation());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidOs() {
/* 111 */     return (getOs() == null && getOsFamily() == null) ? 
/* 112 */       Os.isFamily("unix") : super.isValidOs();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/unix/AbstractAccessTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */