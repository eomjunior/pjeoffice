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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CCCheckin
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_COMMENT = "-c";
/*     */   public static final String FLAG_COMMENTFILE = "-cfile";
/*     */   public static final String FLAG_NOCOMMENT = "-nc";
/*     */   public static final String FLAG_NOWARN = "-nwarn";
/*     */   public static final String FLAG_PRESERVETIME = "-ptime";
/*     */   public static final String FLAG_KEEPCOPY = "-keep";
/*     */   public static final String FLAG_IDENTICAL = "-identical";
/* 117 */   private String mComment = null;
/* 118 */   private String mCfile = null;
/*     */ 
/*     */   
/*     */   private boolean mNwarn = false;
/*     */ 
/*     */   
/*     */   private boolean mPtime = false;
/*     */ 
/*     */   
/*     */   private boolean mKeep = false;
/*     */   
/*     */   private boolean mIdentical = true;
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 133 */     Commandline commandLine = new Commandline();
/* 134 */     Project aProj = getProject();
/*     */ 
/*     */     
/* 137 */     if (getViewPath() == null) {
/* 138 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     commandLine.setExecutable(getClearToolCommand());
/* 145 */     commandLine.createArgument().setValue("checkin");
/*     */     
/* 147 */     checkOptions(commandLine);
/*     */     
/* 149 */     if (!getFailOnErr()) {
/* 150 */       getProject().log("Ignoring any errors that occur for: " + 
/* 151 */           getViewPathBasename(), 3);
/*     */     }
/* 153 */     int result = run(commandLine);
/* 154 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 155 */       throw new BuildException("Failed executing: " + commandLine, getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 163 */     if (getComment() != null) {
/*     */       
/* 165 */       getCommentCommand(cmd);
/*     */     }
/* 167 */     else if (getCommentFile() != null) {
/*     */       
/* 169 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 171 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/*     */ 
/*     */     
/* 175 */     if (getNoWarn())
/*     */     {
/* 177 */       cmd.createArgument().setValue("-nwarn");
/*     */     }
/*     */     
/* 180 */     if (getPreserveTime())
/*     */     {
/* 182 */       cmd.createArgument().setValue("-ptime");
/*     */     }
/*     */     
/* 185 */     if (getKeepCopy())
/*     */     {
/* 187 */       cmd.createArgument().setValue("-keep");
/*     */     }
/*     */     
/* 190 */     if (getIdentical())
/*     */     {
/* 192 */       cmd.createArgument().setValue("-identical");
/*     */     }
/*     */ 
/*     */     
/* 196 */     cmd.createArgument().setValue(getViewPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 206 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 215 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentFile(String cfile) {
/* 224 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 233 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoWarn(boolean nwarn) {
/* 242 */     this.mNwarn = nwarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNoWarn() {
/* 251 */     return this.mNwarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveTime(boolean ptime) {
/* 260 */     this.mPtime = ptime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getPreserveTime() {
/* 269 */     return this.mPtime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepCopy(boolean keep) {
/* 278 */     this.mKeep = keep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getKeepCopy() {
/* 287 */     return this.mKeep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdentical(boolean identical) {
/* 297 */     this.mIdentical = identical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIdentical() {
/* 306 */     return this.mIdentical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 316 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 322 */       cmd.createArgument().setValue("-c");
/* 323 */       cmd.createArgument().setValue(getComment());
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
/* 334 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 340 */       cmd.createArgument().setValue("-cfile");
/* 341 */       cmd.createArgument().setValue(getCommentFile());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCCheckin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */