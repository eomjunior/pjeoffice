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
/*     */ public class CCMkbl
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_COMMENT = "-c";
/*     */   public static final String FLAG_COMMENTFILE = "-cfile";
/*     */   public static final String FLAG_NOCOMMENT = "-nc";
/*     */   public static final String FLAG_IDENTICAL = "-identical";
/*     */   public static final String FLAG_INCREMENTAL = "-incremental";
/*     */   public static final String FLAG_FULL = "-full";
/*     */   public static final String FLAG_NLABEL = "-nlabel";
/* 114 */   private String mComment = null;
/* 115 */   private String mCfile = null;
/* 116 */   private String mBaselineRootName = null;
/*     */ 
/*     */   
/*     */   private boolean mNwarn = false;
/*     */ 
/*     */   
/*     */   private boolean mIdentical = true;
/*     */ 
/*     */   
/*     */   private boolean mFull = false;
/*     */   
/*     */   private boolean mNlabel = false;
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 131 */     Commandline commandLine = new Commandline();
/* 132 */     Project aProj = getProject();
/*     */ 
/*     */     
/* 135 */     if (getViewPath() == null) {
/* 136 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     commandLine.setExecutable(getClearToolCommand());
/* 143 */     commandLine.createArgument().setValue("mkbl");
/*     */     
/* 145 */     checkOptions(commandLine);
/*     */     
/* 147 */     if (!getFailOnErr()) {
/* 148 */       getProject().log("Ignoring any errors that occur for: " + 
/* 149 */           getBaselineRootName(), 3);
/*     */     }
/* 151 */     int result = run(commandLine);
/* 152 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 153 */       throw new BuildException("Failed executing: " + commandLine, 
/* 154 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 162 */     if (getComment() != null) {
/*     */       
/* 164 */       getCommentCommand(cmd);
/* 165 */     } else if (getCommentFile() != null) {
/*     */       
/* 167 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 169 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/*     */     
/* 172 */     if (getIdentical())
/*     */     {
/* 174 */       cmd.createArgument().setValue("-identical");
/*     */     }
/*     */     
/* 177 */     if (getFull()) {
/*     */       
/* 179 */       cmd.createArgument().setValue("-full");
/*     */     } else {
/*     */       
/* 182 */       cmd.createArgument().setValue("-incremental");
/*     */     } 
/*     */     
/* 185 */     if (getNlabel())
/*     */     {
/* 187 */       cmd.createArgument().setValue("-nlabel");
/*     */     }
/*     */ 
/*     */     
/* 191 */     cmd.createArgument().setValue(getBaselineRootName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 200 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 209 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentFile(String cfile) {
/* 218 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 227 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBaselineRootName(String baselineRootName) {
/* 236 */     this.mBaselineRootName = baselineRootName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBaselineRootName() {
/* 245 */     return this.mBaselineRootName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoWarn(boolean nwarn) {
/* 254 */     this.mNwarn = nwarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNoWarn() {
/* 263 */     return this.mNwarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdentical(boolean identical) {
/* 272 */     this.mIdentical = identical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIdentical() {
/* 281 */     return this.mIdentical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFull(boolean full) {
/* 290 */     this.mFull = full;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFull() {
/* 299 */     return this.mFull;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNlabel(boolean nlabel) {
/* 308 */     this.mNlabel = nlabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getNlabel() {
/* 317 */     return this.mNlabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 327 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 333 */       cmd.createArgument().setValue("-c");
/* 334 */       cmd.createArgument().setValue(getComment());
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
/* 345 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 351 */       cmd.createArgument().setValue("-cfile");
/* 352 */       cmd.createArgument().setValue(getCommentFile());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCMkbl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */