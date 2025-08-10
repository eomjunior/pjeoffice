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
/*     */ public class CCMkelem
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_COMMENT = "-c";
/*     */   public static final String FLAG_COMMENTFILE = "-cfile";
/*     */   public static final String FLAG_NOCOMMENT = "-nc";
/*     */   public static final String FLAG_NOWARN = "-nwarn";
/*     */   public static final String FLAG_PRESERVETIME = "-ptime";
/*     */   public static final String FLAG_NOCHECKOUT = "-nco";
/*     */   public static final String FLAG_CHECKIN = "-ci";
/*     */   public static final String FLAG_MASTER = "-master";
/*     */   public static final String FLAG_ELTYPE = "-eltype";
/* 129 */   private String mComment = null;
/* 130 */   private String mCfile = null;
/*     */   private boolean mNwarn = false;
/*     */   private boolean mPtime = false;
/*     */   private boolean mNoco = false;
/*     */   private boolean mCheckin = false;
/*     */   private boolean mMaster = false;
/* 136 */   private String mEltype = null;
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
/* 147 */     Commandline commandLine = new Commandline();
/* 148 */     Project aProj = getProject();
/*     */ 
/*     */     
/* 151 */     if (getViewPath() == null) {
/* 152 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     commandLine.setExecutable(getClearToolCommand());
/* 159 */     commandLine.createArgument().setValue("mkelem");
/*     */     
/* 161 */     checkOptions(commandLine);
/*     */     
/* 163 */     if (!getFailOnErr()) {
/* 164 */       getProject().log("Ignoring any errors that occur for: " + 
/* 165 */           getViewPathBasename(), 3);
/*     */     }
/* 167 */     int result = run(commandLine);
/* 168 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 169 */       throw new BuildException("Failed executing: " + commandLine, 
/* 170 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 178 */     if (getComment() != null) {
/*     */       
/* 180 */       getCommentCommand(cmd);
/* 181 */     } else if (getCommentFile() != null) {
/*     */       
/* 183 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 185 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/*     */     
/* 188 */     if (getNoWarn())
/*     */     {
/* 190 */       cmd.createArgument().setValue("-nwarn");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 195 */     if (getNoCheckout() && getCheckin()) {
/* 196 */       throw new BuildException("Should choose either [nocheckout | checkin]");
/*     */     }
/* 198 */     if (getNoCheckout())
/*     */     {
/* 200 */       cmd.createArgument().setValue("-nco");
/*     */     }
/* 202 */     if (getCheckin()) {
/*     */       
/* 204 */       cmd.createArgument().setValue("-ci");
/* 205 */       if (getPreserveTime())
/*     */       {
/* 207 */         cmd.createArgument().setValue("-ptime");
/*     */       }
/*     */     } 
/* 210 */     if (getMaster())
/*     */     {
/* 212 */       cmd.createArgument().setValue("-master");
/*     */     }
/* 214 */     if (getEltype() != null)
/*     */     {
/* 216 */       getEltypeCommand(cmd);
/*     */     }
/*     */     
/* 219 */     cmd.createArgument().setValue(getViewPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 228 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 237 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentFile(String cfile) {
/* 246 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 255 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoWarn(boolean nwarn) {
/* 264 */     this.mNwarn = nwarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNoWarn() {
/* 273 */     return this.mNwarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveTime(boolean ptime) {
/* 282 */     this.mPtime = ptime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getPreserveTime() {
/* 291 */     return this.mPtime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoCheckout(boolean co) {
/* 300 */     this.mNoco = co;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNoCheckout() {
/* 309 */     return this.mNoco;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCheckin(boolean ci) {
/* 318 */     this.mCheckin = ci;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCheckin() {
/* 327 */     return this.mCheckin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaster(boolean master) {
/* 337 */     this.mMaster = master;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMaster() {
/* 346 */     return this.mMaster;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEltype(String eltype) {
/* 355 */     this.mEltype = eltype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEltype() {
/* 364 */     return this.mEltype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 374 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 380 */       cmd.createArgument().setValue("-c");
/* 381 */       cmd.createArgument().setValue(getComment());
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
/* 392 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 398 */       cmd.createArgument().setValue("-cfile");
/* 399 */       cmd.createArgument().setValue(getCommentFile());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getEltypeCommand(Commandline cmd) {
/* 410 */     if (getEltype() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 416 */       cmd.createArgument().setValue("-eltype");
/* 417 */       cmd.createArgument().setValue(getEltype());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCMkelem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */