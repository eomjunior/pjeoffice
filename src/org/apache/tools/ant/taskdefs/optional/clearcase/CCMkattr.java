/*     */ package org.apache.tools.ant.taskdefs.optional.clearcase;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
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
/*     */ public class CCMkattr
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
/* 114 */   private String mVersion = null;
/* 115 */   private String mTypeName = null;
/* 116 */   private String mTypeValue = null;
/* 117 */   private String mComment = null;
/* 118 */   private String mCfile = null;
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
/* 129 */     Commandline commandLine = new Commandline();
/* 130 */     Project aProj = getProject();
/*     */ 
/*     */     
/* 133 */     if (getTypeName() == null) {
/* 134 */       throw new BuildException("Required attribute TypeName not specified");
/*     */     }
/* 136 */     if (getTypeValue() == null) {
/* 137 */       throw new BuildException("Required attribute TypeValue not specified");
/*     */     }
/*     */     
/* 140 */     if (getViewPath() == null) {
/* 141 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     commandLine.setExecutable(getClearToolCommand());
/* 148 */     commandLine.createArgument().setValue("mkattr");
/*     */     
/* 150 */     checkOptions(commandLine);
/*     */     
/* 152 */     if (!getFailOnErr()) {
/* 153 */       getProject().log("Ignoring any errors that occur for: " + 
/* 154 */           getViewPathBasename(), 3);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     int result = run(commandLine);
/* 161 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 162 */       throw new BuildException("Failed executing: " + commandLine, 
/* 163 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 172 */     if (getReplace())
/*     */     {
/* 174 */       cmd.createArgument().setValue("-replace");
/*     */     }
/*     */     
/* 177 */     if (getRecurse())
/*     */     {
/* 179 */       cmd.createArgument().setValue("-recurse");
/*     */     }
/*     */     
/* 182 */     if (getVersion() != null)
/*     */     {
/* 184 */       getVersionCommand(cmd);
/*     */     }
/*     */     
/* 187 */     if (getComment() != null) {
/*     */       
/* 189 */       getCommentCommand(cmd);
/* 190 */     } else if (getCommentFile() != null) {
/*     */       
/* 192 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 194 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/*     */     
/* 197 */     if (getTypeName() != null)
/*     */     {
/* 199 */       getTypeCommand(cmd);
/*     */     }
/* 201 */     if (getTypeValue() != null)
/*     */     {
/* 203 */       getTypeValueCommand(cmd);
/*     */     }
/*     */     
/* 206 */     cmd.createArgument().setValue(getViewPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplace(boolean replace) {
/* 215 */     this.mReplace = replace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReplace() {
/* 224 */     return this.mReplace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecurse(boolean recurse) {
/* 233 */     this.mRecurse = recurse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRecurse() {
/* 242 */     return this.mRecurse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(String version) {
/* 251 */     this.mVersion = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/* 260 */     return this.mVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 269 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 278 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentFile(String cfile) {
/* 287 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 296 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeName(String tn) {
/* 305 */     this.mTypeName = tn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/* 314 */     return this.mTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeValue(String tv) {
/* 323 */     this.mTypeValue = tv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeValue() {
/* 332 */     return this.mTypeValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getVersionCommand(Commandline cmd) {
/* 342 */     if (getVersion() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 348 */       cmd.createArgument().setValue("-version");
/* 349 */       cmd.createArgument().setValue(getVersion());
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
/* 360 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 366 */       cmd.createArgument().setValue("-c");
/* 367 */       cmd.createArgument().setValue(getComment());
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
/* 378 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 384 */       cmd.createArgument().setValue("-cfile");
/* 385 */       cmd.createArgument().setValue(getCommentFile());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getTypeCommand(Commandline cmd) {
/* 396 */     String typenm = getTypeName();
/*     */     
/* 398 */     if (typenm != null) {
/* 399 */       cmd.createArgument().setValue(typenm);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getTypeValueCommand(Commandline cmd) {
/* 410 */     String typevl = getTypeValue();
/*     */     
/* 412 */     if (typevl != null) {
/* 413 */       if (Os.isFamily("windows")) {
/* 414 */         typevl = "\\\"" + typevl + "\\\"";
/*     */       } else {
/* 416 */         typevl = "\"" + typevl + "\"";
/*     */       } 
/* 418 */       cmd.createArgument().setValue(typevl);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCMkattr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */