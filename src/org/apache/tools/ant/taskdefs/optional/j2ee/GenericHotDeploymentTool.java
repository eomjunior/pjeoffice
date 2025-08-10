/*     */ package org.apache.tools.ant.taskdefs.optional.j2ee;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.Java;
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
/*     */ public class GenericHotDeploymentTool
/*     */   extends AbstractHotDeploymentTool
/*     */ {
/*     */   private Java java;
/*     */   private String className;
/*  42 */   private static final String[] VALID_ACTIONS = new String[] { "deploy" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline.Argument createArg() {
/*  51 */     return this.java.createArg();
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
/*     */   public Commandline.Argument createJvmarg() {
/*  63 */     return this.java.createJvmarg();
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
/*     */   protected boolean isActionValid() {
/*  75 */     return getTask().getAction().equals(VALID_ACTIONS[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTask(ServerDeploy task) {
/*  85 */     super.setTask(task);
/*  86 */     this.java = new Java(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deploy() throws BuildException {
/*  97 */     this.java.setClassname(this.className);
/*  98 */     this.java.setClasspath(getClasspath());
/*  99 */     this.java.setFork(true);
/* 100 */     this.java.setFailonerror(true);
/* 101 */     this.java.execute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateAttributes() throws BuildException {
/* 111 */     super.validateAttributes();
/*     */     
/* 113 */     if (this.className == null) {
/* 114 */       throw new BuildException("The classname attribute must be set");
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
/*     */   public void setClassName(String className) {
/* 126 */     this.className = className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Java getJava() {
/* 134 */     return this.java;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 142 */     return this.className;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/j2ee/GenericHotDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */