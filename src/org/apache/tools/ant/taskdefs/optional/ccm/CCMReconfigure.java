/*     */ package org.apache.tools.ant.taskdefs.optional.ccm;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class CCMReconfigure
/*     */   extends Continuus
/*     */ {
/*     */   public static final String FLAG_RECURSE = "/recurse";
/*     */   public static final String FLAG_VERBOSE = "/verbose";
/*     */   public static final String FLAG_PROJECT = "/project";
/*  47 */   private String ccmProject = null;
/*     */   
/*     */   private boolean recurse = false;
/*     */   
/*     */   private boolean verbose = false;
/*     */   
/*     */   public CCMReconfigure() {
/*  54 */     setCcmAction("reconfigure");
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
/*     */   public void execute() throws BuildException {
/*  67 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */ 
/*     */     
/*  71 */     commandLine.setExecutable(getCcmCommand());
/*  72 */     commandLine.createArgument().setValue(getCcmAction());
/*     */     
/*  74 */     checkOptions(commandLine);
/*     */     
/*  76 */     int result = run(commandLine);
/*  77 */     if (Execute.isFailure(result)) {
/*  78 */       throw new BuildException("Failed executing: " + commandLine, 
/*  79 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/*  88 */     if (isRecurse()) {
/*  89 */       cmd.createArgument().setValue("/recurse");
/*     */     }
/*     */     
/*  92 */     if (isVerbose()) {
/*  93 */       cmd.createArgument().setValue("/verbose");
/*     */     }
/*     */     
/*  96 */     if (getCcmProject() != null) {
/*  97 */       cmd.createArgument().setValue("/project");
/*  98 */       cmd.createArgument().setValue(getCcmProject());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCcmProject() {
/* 108 */     return this.ccmProject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCcmProject(String v) {
/* 116 */     this.ccmProject = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRecurse() {
/* 124 */     return this.recurse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecurse(boolean v) {
/* 133 */     this.recurse = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVerbose() {
/* 141 */     return this.verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean v) {
/* 149 */     this.verbose = v;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ccm/CCMReconfigure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */