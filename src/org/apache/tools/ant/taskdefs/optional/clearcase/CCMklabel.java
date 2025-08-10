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
/*     */ public class CCMklabel
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_REPLACE = "-replace";
/*     */   public static final String FLAG_RECURSE = "-recurse";
/*     */   public static final String FLAG_VERSION = "-version";
/*     */   public static final String FLAG_COMMENT = "-c";
/*     */   public static final String FLAG_COMMENTFILE = "-cfile";
/*     */   public static final String FLAG_NOCOMMENT = "-nc";
/*     */   private boolean mReplace = false;
/*     */   private boolean mRecurse = false;
/* 113 */   private String mVersion = null;
/* 114 */   private String mTypeName = null;
/* 115 */   private String mVOB = null;
/* 116 */   private String mComment = null;
/* 117 */   private String mCfile = null;
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
/* 128 */     Commandline commandLine = new Commandline();
/* 129 */     Project aProj = getProject();
/*     */ 
/*     */     
/* 132 */     if (getTypeName() == null) {
/* 133 */       throw new BuildException("Required attribute TypeName not specified");
/*     */     }
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
/* 145 */     commandLine.createArgument().setValue("mklabel");
/*     */     
/* 147 */     checkOptions(commandLine);
/*     */     
/* 149 */     if (!getFailOnErr()) {
/* 150 */       getProject().log("Ignoring any errors that occur for: " + 
/* 151 */           getViewPathBasename(), 3);
/*     */     }
/* 153 */     int result = run(commandLine);
/* 154 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 155 */       throw new BuildException("Failed executing: " + commandLine, 
/* 156 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 165 */     if (getReplace())
/*     */     {
/* 167 */       cmd.createArgument().setValue("-replace");
/*     */     }
/*     */     
/* 170 */     if (getRecurse())
/*     */     {
/* 172 */       cmd.createArgument().setValue("-recurse");
/*     */     }
/*     */     
/* 175 */     if (getVersion() != null)
/*     */     {
/* 177 */       getVersionCommand(cmd);
/*     */     }
/*     */     
/* 180 */     if (getComment() != null) {
/*     */       
/* 182 */       getCommentCommand(cmd);
/* 183 */     } else if (getCommentFile() != null) {
/*     */       
/* 185 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 187 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/*     */     
/* 190 */     if (getTypeName() != null)
/*     */     {
/* 192 */       getTypeCommand(cmd);
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
/*     */   public void setReplace(boolean replace) {
/* 205 */     this.mReplace = replace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReplace() {
/* 214 */     return this.mReplace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecurse(boolean recurse) {
/* 223 */     this.mRecurse = recurse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRecurse() {
/* 232 */     return this.mRecurse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(String version) {
/* 241 */     this.mVersion = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/* 250 */     return this.mVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 259 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 268 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentFile(String cfile) {
/* 277 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 286 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeName(String tn) {
/* 295 */     this.mTypeName = tn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/* 304 */     return this.mTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVOB(String vob) {
/* 313 */     this.mVOB = vob;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVOB() {
/* 322 */     return this.mVOB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getVersionCommand(Commandline cmd) {
/* 332 */     if (getVersion() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 338 */       cmd.createArgument().setValue("-version");
/* 339 */       cmd.createArgument().setValue(getVersion());
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
/* 350 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 356 */       cmd.createArgument().setValue("-c");
/* 357 */       cmd.createArgument().setValue(getComment());
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
/* 368 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 374 */       cmd.createArgument().setValue("-cfile");
/* 375 */       cmd.createArgument().setValue(getCommentFile());
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
/*     */   private void getTypeCommand(Commandline cmd) {
/* 387 */     if (getTypeName() != null) {
/* 388 */       String typenm = getTypeName();
/* 389 */       if (getVOB() != null) {
/* 390 */         typenm = typenm + "@" + getVOB();
/*     */       }
/* 392 */       cmd.createArgument().setValue(typenm);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCMklabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */