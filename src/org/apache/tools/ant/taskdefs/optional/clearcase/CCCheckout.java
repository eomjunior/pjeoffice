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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CCCheckout
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_RESERVED = "-reserved";
/*     */   public static final String FLAG_UNRESERVED = "-unreserved";
/*     */   public static final String FLAG_OUT = "-out";
/*     */   public static final String FLAG_NODATA = "-ndata";
/*     */   public static final String FLAG_BRANCH = "-branch";
/*     */   public static final String FLAG_VERSION = "-version";
/*     */   public static final String FLAG_NOWARN = "-nwarn";
/*     */   public static final String FLAG_COMMENT = "-c";
/*     */   public static final String FLAG_COMMENTFILE = "-cfile";
/*     */   public static final String FLAG_NOCOMMENT = "-nc";
/*     */   private boolean mReserved = true;
/* 149 */   private String mOut = null;
/*     */   private boolean mNdata = false;
/* 151 */   private String mBranch = null;
/*     */   private boolean mVersion = false;
/*     */   private boolean mNwarn = false;
/* 154 */   private String mComment = null;
/* 155 */   private String mCfile = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mNotco = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 167 */     Commandline commandLine = new Commandline();
/* 168 */     Project aProj = getProject();
/*     */ 
/*     */     
/* 171 */     if (getViewPath() == null) {
/* 172 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 178 */     commandLine.setExecutable(getClearToolCommand());
/* 179 */     commandLine.createArgument().setValue("checkout");
/*     */     
/* 181 */     checkOptions(commandLine);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 187 */     if (!getNotco() && lsCheckout()) {
/* 188 */       getProject().log("Already checked out in this view: " + 
/* 189 */           getViewPathBasename(), 3);
/*     */       return;
/*     */     } 
/* 192 */     if (!getFailOnErr()) {
/* 193 */       getProject().log("Ignoring any errors that occur for: " + 
/* 194 */           getViewPathBasename(), 3);
/*     */     }
/* 196 */     int result = run(commandLine);
/* 197 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 198 */       throw new BuildException("Failed executing: " + commandLine, 
/* 199 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean lsCheckout() {
/* 207 */     Commandline cmdl = new Commandline();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     cmdl.setExecutable(getClearToolCommand());
/* 213 */     cmdl.createArgument().setValue("lsco");
/* 214 */     cmdl.createArgument().setValue("-cview");
/* 215 */     cmdl.createArgument().setValue("-short");
/* 216 */     cmdl.createArgument().setValue("-d");
/*     */     
/* 218 */     cmdl.createArgument().setValue(getViewPath());
/*     */     
/* 220 */     String result = runS(cmdl, getFailOnErr());
/*     */     
/* 222 */     return (result != null && !result.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 230 */     if (getReserved()) {
/*     */       
/* 232 */       cmd.createArgument().setValue("-reserved");
/*     */     } else {
/*     */       
/* 235 */       cmd.createArgument().setValue("-unreserved");
/*     */     } 
/*     */     
/* 238 */     if (getOut() != null) {
/*     */       
/* 240 */       getOutCommand(cmd);
/* 241 */     } else if (getNoData()) {
/*     */       
/* 243 */       cmd.createArgument().setValue("-ndata");
/*     */     } 
/*     */     
/* 246 */     if (getBranch() != null) {
/*     */       
/* 248 */       getBranchCommand(cmd);
/* 249 */     } else if (getVersion()) {
/*     */       
/* 251 */       cmd.createArgument().setValue("-version");
/*     */     } 
/*     */     
/* 254 */     if (getNoWarn())
/*     */     {
/* 256 */       cmd.createArgument().setValue("-nwarn");
/*     */     }
/*     */     
/* 259 */     if (getComment() != null) {
/*     */       
/* 261 */       getCommentCommand(cmd);
/* 262 */     } else if (getCommentFile() != null) {
/*     */       
/* 264 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 266 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/*     */ 
/*     */     
/* 270 */     cmd.createArgument().setValue(getViewPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReserved(boolean reserved) {
/* 279 */     this.mReserved = reserved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReserved() {
/* 288 */     return this.mReserved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNotco(boolean notco) {
/* 298 */     this.mNotco = notco;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNotco() {
/* 308 */     return this.mNotco;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOut(String outf) {
/* 317 */     this.mOut = outf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getOut() {
/* 326 */     return this.mOut;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoData(boolean ndata) {
/* 336 */     this.mNdata = ndata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNoData() {
/* 345 */     return this.mNdata;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBranch(String branch) {
/* 354 */     this.mBranch = branch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBranch() {
/* 363 */     return this.mBranch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(boolean version) {
/* 372 */     this.mVersion = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getVersion() {
/* 381 */     return this.mVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoWarn(boolean nwarn) {
/* 390 */     this.mNwarn = nwarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNoWarn() {
/* 399 */     return this.mNwarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 408 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 417 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentFile(String cfile) {
/* 426 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 435 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getOutCommand(Commandline cmd) {
/* 445 */     if (getOut() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 451 */       cmd.createArgument().setValue("-out");
/* 452 */       cmd.createArgument().setValue(getOut());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getBranchCommand(Commandline cmd) {
/* 463 */     if (getBranch() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 469 */       cmd.createArgument().setValue("-branch");
/* 470 */       cmd.createArgument().setValue(getBranch());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 481 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 487 */       cmd.createArgument().setValue("-c");
/* 488 */       cmd.createArgument().setValue(getComment());
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
/* 499 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 505 */       cmd.createArgument().setValue("-cfile");
/* 506 */       cmd.createArgument().setValue(getCommentFile());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCCheckout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */