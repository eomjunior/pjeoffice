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
/*     */ public class CCRmtype
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_IGNORE = "-ignore";
/*     */   public static final String FLAG_RMALL = "-rmall";
/*     */   public static final String FLAG_FORCE = "-force";
/*     */   public static final String FLAG_COMMENT = "-c";
/*     */   public static final String FLAG_COMMENTFILE = "-cfile";
/*     */   public static final String FLAG_NOCOMMENT = "-nc";
/* 115 */   private String mTypeKind = null;
/* 116 */   private String mTypeName = null;
/* 117 */   private String mVOB = null;
/* 118 */   private String mComment = null;
/* 119 */   private String mCfile = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mRmall = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mIgnore = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 132 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */     
/* 135 */     if (getTypeKind() == null) {
/* 136 */       throw new BuildException("Required attribute TypeKind not specified");
/*     */     }
/* 138 */     if (getTypeName() == null) {
/* 139 */       throw new BuildException("Required attribute TypeName not specified");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     commandLine.setExecutable(getClearToolCommand());
/* 146 */     commandLine.createArgument().setValue("rmtype");
/*     */     
/* 148 */     checkOptions(commandLine);
/*     */     
/* 150 */     if (!getFailOnErr()) {
/* 151 */       getProject().log("Ignoring any errors that occur for: " + 
/* 152 */           getTypeSpecifier(), 3);
/*     */     }
/* 154 */     int result = run(commandLine);
/* 155 */     if (Execute.isFailure(result) && getFailOnErr()) {
/* 156 */       throw new BuildException("Failed executing: " + commandLine, 
/* 157 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 165 */     if (getIgnore())
/*     */     {
/* 167 */       cmd.createArgument().setValue("-ignore");
/*     */     }
/* 169 */     if (getRmAll()) {
/*     */       
/* 171 */       cmd.createArgument().setValue("-rmall");
/* 172 */       cmd.createArgument().setValue("-force");
/*     */     } 
/* 174 */     if (getComment() != null) {
/*     */       
/* 176 */       getCommentCommand(cmd);
/* 177 */     } else if (getCommentFile() != null) {
/*     */       
/* 179 */       getCommentFileCommand(cmd);
/*     */     } else {
/* 181 */       cmd.createArgument().setValue("-nc");
/*     */     } 
/*     */ 
/*     */     
/* 185 */     cmd.createArgument().setValue(getTypeSpecifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnore(boolean ignore) {
/* 194 */     this.mIgnore = ignore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIgnore() {
/* 203 */     return this.mIgnore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRmAll(boolean rmall) {
/* 212 */     this.mRmall = rmall;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRmAll() {
/* 221 */     return this.mRmall;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 230 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 239 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommentFile(String cfile) {
/* 248 */     this.mCfile = cfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommentFile() {
/* 257 */     return this.mCfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeKind(String tk) {
/* 266 */     this.mTypeKind = tk;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeKind() {
/* 275 */     return this.mTypeKind;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTypeName(String tn) {
/* 284 */     this.mTypeName = tn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeName() {
/* 293 */     return this.mTypeName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVOB(String vob) {
/* 302 */     this.mVOB = vob;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVOB() {
/* 311 */     return this.mVOB;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getTypeSpecifier() {
/* 321 */     String tkind = getTypeKind();
/* 322 */     String tname = getTypeName();
/*     */ 
/*     */     
/* 325 */     String typeSpec = tkind + ":" + tname;
/* 326 */     if (getVOB() != null) {
/* 327 */       typeSpec = typeSpec + "@" + getVOB();
/*     */     }
/* 329 */     return typeSpec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 339 */     if (getComment() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 345 */       cmd.createArgument().setValue("-c");
/* 346 */       cmd.createArgument().setValue(getComment());
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
/* 357 */     if (getCommentFile() != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 363 */       cmd.createArgument().setValue("-cfile");
/* 364 */       cmd.createArgument().setValue(getCommentFile());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCRmtype.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */