/*     */ package org.apache.tools.ant.taskdefs.optional.clearcase;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CCUnCheckout
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_KEEPCOPY = "-keep";
/*     */   public static final String FLAG_RM = "-rm";
/*     */   private boolean mKeep = false;
/*     */   
/*     */   public void execute() throws BuildException {
/*  77 */     Commandline commandLine = new Commandline();
/*  78 */     Project aProj = getProject();
/*     */ 
/*     */     
/*  81 */     if (getViewPath() == null) {
/*  82 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     commandLine.setExecutable(getClearToolCommand());
/*  89 */     commandLine.createArgument().setValue("uncheckout");
/*     */     
/*  91 */     checkOptions(commandLine);
/*     */     
/*  93 */     if (!getFailOnErr()) {
/*  94 */       getProject().log("Ignoring any errors that occur for: " + 
/*  95 */           getViewPathBasename(), 3);
/*     */     }
/*  97 */     int result = run(commandLine);
/*  98 */     if (Execute.isFailure(result) && getFailOnErr()) {
/*  99 */       throw new BuildException("Failed executing: " + commandLine, 
/* 100 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 109 */     if (getKeepCopy()) {
/*     */       
/* 111 */       cmd.createArgument().setValue("-keep");
/*     */     } else {
/*     */       
/* 114 */       cmd.createArgument().setValue("-rm");
/*     */     } 
/*     */ 
/*     */     
/* 118 */     cmd.createArgument().setValue(getViewPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepCopy(boolean keep) {
/* 127 */     this.mKeep = keep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getKeepCopy() {
/* 136 */     return this.mKeep;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCUnCheckout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */