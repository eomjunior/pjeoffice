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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CCMkdir
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_COMMENT = "-c";
/*     */   public static final String FLAG_COMMENTFILE = "-cfile";
/*     */   public static final String FLAG_NOCOMMENT = "-nc";
/*     */   public static final String FLAG_NOCHECKOUT = "-nco";
/*  84 */   private String mComment = null;
/*  85 */   private String mCfile = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mNoco = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  97 */     Commandline commandLine = new Commandline();
/*  98 */     Project aProj = getProject();
/*     */ 
/*     */     
/* 101 */     if (getViewPath() == null) {
/* 102 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 108 */     commandLine.setExecutable(getClearToolCommand());
/* 109 */     commandLine.createArgument().setValue("mkdir");
/*     */     
/* 111 */     checkOptions(commandLine);
/*     */     
/* 113 */     if (!getFailOnErr()) {
/* 114 */       getProject().log("Ignoring any errors that occur for: " + 
/* 115 */           getViewPathBasename(), 3);
/*     */     }
/* 117 */     int result = run(commandLine);
/* 118 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 119 */       throw new BuildException("Failed executing: " + commandLine, 
/* 120 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 128 */     if (getComment() != null) {
/*     */       
/* 130 */       getCommentCommand(cmd);
/* 131 */     } else if (getCommentFile() != null) {
/*     */       
/* 133 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 135 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/* 137 */     if (getNoCheckout())
/*     */     {
/* 139 */       cmd.createArgument().setValue("-nco");
/*     */     }
/*     */     
/* 142 */     cmd.createArgument().setValue(getViewPath());
/*     */   }
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
/*     */   public void setCommentFile(String cfile) {
/* 169 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 178 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoCheckout(boolean co) {
/* 187 */     this.mNoco = co;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNoCheckout() {
/* 196 */     return this.mNoco;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 206 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 212 */       cmd.createArgument().setValue("-c");
/* 213 */       cmd.createArgument().setValue(getComment());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentFileCommand(Commandline cmd) {
/* 224 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 230 */       cmd.createArgument().setValue("-cfile");
/* 231 */       cmd.createArgument().setValue(getCommentFile());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCMkdir.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */