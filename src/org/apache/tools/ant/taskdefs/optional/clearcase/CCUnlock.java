/*     */ package org.apache.tools.ant.taskdefs.optional.clearcase;
/*     */ 
/*     */ import java.util.Optional;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CCUnlock
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_COMMENT = "-comment";
/*     */   public static final String FLAG_PNAME = "-pname";
/*  83 */   private String mComment = null;
/*  84 */   private String mPname = null;
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
/*  95 */     Commandline commandLine = new Commandline();
/*  96 */     Project aProj = getProject();
/*     */ 
/*     */     
/*  99 */     if (getViewPath() == null) {
/* 100 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     commandLine.setExecutable(getClearToolCommand());
/* 107 */     commandLine.createArgument().setValue("unlock");
/*     */ 
/*     */     
/* 110 */     checkOptions(commandLine);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     if (!getFailOnErr()) {
/* 116 */       getProject().log("Ignoring any errors that occur for: " + 
/* 117 */           getOpType(), 3);
/*     */     }
/* 119 */     int result = run(commandLine);
/* 120 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 121 */       throw new BuildException("Failed executing: " + commandLine, 
/* 122 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 131 */     getCommentCommand(cmd);
/*     */     
/* 133 */     if (getObjSelect() == null && getPname() == null) {
/* 134 */       throw new BuildException("Should select either an element (pname) or an object (objselect)");
/*     */     }
/*     */     
/* 137 */     getPnameCommand(cmd);
/*     */     
/* 139 */     if (getObjSelect() != null) {
/* 140 */       cmd.createArgument().setValue(getObjSelect());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 151 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 160 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPname(String pname) {
/* 169 */     this.mPname = pname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPname() {
/* 178 */     return this.mPname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjselect(String objselect) {
/* 187 */     setObjSelect(objselect);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjSel(String objsel) {
/* 197 */     setObjSelect(objsel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjselect() {
/* 206 */     return getObjSelect();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 216 */     if (getComment() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 224 */     cmd.createArgument().setValue("-comment");
/* 225 */     cmd.createArgument().setValue(getComment());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getPnameCommand(Commandline cmd) {
/* 235 */     if (getPname() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     cmd.createArgument().setValue("-pname");
/* 244 */     cmd.createArgument().setValue(getPname());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getOpType() {
/* 253 */     return Optional.<String>ofNullable(getPname()).orElseGet(this::getObjSelect);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCUnlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */