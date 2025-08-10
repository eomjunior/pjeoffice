/*     */ package org.apache.tools.ant.taskdefs.optional.clearcase;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CCMklbtype
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_REPLACE = "-replace";
/*     */   public static final String FLAG_GLOBAL = "-global";
/*     */   public static final String FLAG_ORDINARY = "-ordinary";
/*     */   public static final String FLAG_PBRANCH = "-pbranch";
/*     */   public static final String FLAG_SHARED = "-shared";
/*     */   public static final String FLAG_COMMENT = "-c";
/*     */   public static final String FLAG_COMMENTFILE = "-cfile";
/*     */   public static final String FLAG_NOCOMMENT = "-nc";
/* 131 */   private String mTypeName = null;
/* 132 */   private String mVOB = null;
/* 133 */   private String mComment = null;
/* 134 */   private String mCfile = null;
/*     */ 
/*     */   
/*     */   private boolean mReplace = false;
/*     */ 
/*     */   
/*     */   private boolean mGlobal = false;
/*     */   
/*     */   private boolean mOrdinary = true;
/*     */   
/*     */   private boolean mPbranch = false;
/*     */   
/*     */   private boolean mShared = false;
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 150 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */     
/* 153 */     if (getTypeName() == null) {
/* 154 */       throw new BuildException("Required attribute TypeName not specified");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     commandLine.setExecutable(getClearToolCommand());
/* 161 */     commandLine.createArgument().setValue("mklbtype");
/*     */     
/* 163 */     checkOptions(commandLine);
/*     */     
/* 165 */     if (!getFailOnErr()) {
/* 166 */       getProject().log("Ignoring any errors that occur for: " + 
/* 167 */           getTypeSpecifier(), 3);
/*     */     }
/* 169 */     int result = run(commandLine);
/* 170 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 171 */       throw new BuildException("Failed executing: " + commandLine, 
/* 172 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 180 */     if (getReplace())
/*     */     {
/* 182 */       cmd.createArgument().setValue("-replace");
/*     */     }
/*     */     
/* 185 */     if (getOrdinary()) {
/*     */       
/* 187 */       cmd.createArgument().setValue("-ordinary");
/* 188 */     } else if (getGlobal()) {
/*     */       
/* 190 */       cmd.createArgument().setValue("-global");
/*     */     } 
/*     */     
/* 193 */     if (getPbranch())
/*     */     {
/* 195 */       cmd.createArgument().setValue("-pbranch");
/*     */     }
/*     */     
/* 198 */     if (getShared())
/*     */     {
/* 200 */       cmd.createArgument().setValue("-shared");
/*     */     }
/*     */     
/* 203 */     if (getComment() != null) {
/*     */       
/* 205 */       getCommentCommand(cmd);
/* 206 */     } else if (getCommentFile() != null) {
/*     */       
/* 208 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 210 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/*     */ 
/*     */     
/* 214 */     cmd.createArgument().setValue(getTypeSpecifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeName(String tn) {
/* 223 */     this.mTypeName = tn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/* 232 */     return this.mTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVOB(String vob) {
/* 241 */     this.mVOB = vob;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVOB() {
/* 250 */     return this.mVOB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplace(boolean repl) {
/* 259 */     this.mReplace = repl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReplace() {
/* 268 */     return this.mReplace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGlobal(boolean glob) {
/* 277 */     this.mGlobal = glob;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getGlobal() {
/* 286 */     return this.mGlobal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrdinary(boolean ordinary) {
/* 295 */     this.mOrdinary = ordinary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOrdinary() {
/* 304 */     return this.mOrdinary;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPbranch(boolean pbranch) {
/* 313 */     this.mPbranch = pbranch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getPbranch() {
/* 322 */     return this.mPbranch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setShared(boolean shared) {
/* 331 */     this.mShared = shared;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShared() {
/* 340 */     return this.mShared;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 349 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 358 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentFile(String cfile) {
/* 367 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 376 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 386 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 392 */       cmd.createArgument().setValue("-c");
/* 393 */       cmd.createArgument().setValue(getComment());
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
/* 404 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 410 */       cmd.createArgument().setValue("-cfile");
/* 411 */       cmd.createArgument().setValue(getCommentFile());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getTypeSpecifier() {
/* 422 */     String typenm = getTypeName();
/* 423 */     if (getVOB() != null) {
/* 424 */       typenm = typenm + "@" + getVOB();
/*     */     }
/* 426 */     return typenm;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCMklbtype.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */