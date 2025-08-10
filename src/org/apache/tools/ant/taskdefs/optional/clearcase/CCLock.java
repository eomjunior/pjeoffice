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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CCLock
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_REPLACE = "-replace";
/*     */   public static final String FLAG_NUSERS = "-nusers";
/*     */   public static final String FLAG_OBSOLETE = "-obsolete";
/*     */   public static final String FLAG_COMMENT = "-comment";
/*     */   public static final String FLAG_PNAME = "-pname";
/*     */   private boolean mReplace = false;
/*     */   private boolean mObsolete = false;
/* 116 */   private String mComment = null;
/* 117 */   private String mNusers = null;
/* 118 */   private String mPname = null;
/* 119 */   private String mObjselect = null;
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
/* 130 */     Commandline commandLine = new Commandline();
/* 131 */     Project aProj = getProject();
/*     */ 
/*     */     
/* 134 */     if (getViewPath() == null) {
/* 135 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     commandLine.setExecutable(getClearToolCommand());
/* 142 */     commandLine.createArgument().setValue("lock");
/*     */ 
/*     */     
/* 145 */     checkOptions(commandLine);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (!getFailOnErr()) {
/* 151 */       getProject().log("Ignoring any errors that occur for: " + 
/* 152 */           getOpType(), 3);
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
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 166 */     if (getReplace())
/*     */     {
/* 168 */       cmd.createArgument().setValue("-replace");
/*     */     }
/* 170 */     if (getObsolete()) {
/*     */       
/* 172 */       cmd.createArgument().setValue("-obsolete");
/*     */     } else {
/* 174 */       getNusersCommand(cmd);
/*     */     } 
/* 176 */     getCommentCommand(cmd);
/*     */     
/* 178 */     if (getObjselect() == null && getPname() == null) {
/* 179 */       throw new BuildException("Should select either an element (pname) or an object (objselect)");
/*     */     }
/*     */     
/* 182 */     getPnameCommand(cmd);
/*     */     
/* 184 */     if (getObjselect() != null) {
/* 185 */       cmd.createArgument().setValue(getObjselect());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplace(boolean replace) {
/* 195 */     this.mReplace = replace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getReplace() {
/* 204 */     return this.mReplace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObsolete(boolean obsolete) {
/* 213 */     this.mObsolete = obsolete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getObsolete() {
/* 222 */     return this.mObsolete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNusers(String nusers) {
/* 232 */     this.mNusers = nusers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNusers() {
/* 241 */     return this.mNusers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String comment) {
/* 251 */     this.mComment = comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 260 */     return this.mComment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPname(String pname) {
/* 269 */     this.mPname = pname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPname() {
/* 278 */     return this.mPname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjSel(String objsel) {
/* 288 */     this.mObjselect = objsel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setObjselect(String objselect) {
/* 297 */     this.mObjselect = objselect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjselect() {
/* 306 */     return this.mObjselect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getNusersCommand(Commandline cmd) {
/* 316 */     if (getNusers() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 324 */     cmd.createArgument().setValue("-nusers");
/* 325 */     cmd.createArgument().setValue(getNusers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getCommentCommand(Commandline cmd) {
/* 335 */     if (getComment() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 343 */     cmd.createArgument().setValue("-comment");
/* 344 */     cmd.createArgument().setValue(getComment());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getPnameCommand(Commandline cmd) {
/* 354 */     if (getPname() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 362 */     cmd.createArgument().setValue("-pname");
/* 363 */     cmd.createArgument().setValue(getPname());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getOpType() {
/* 372 */     return Optional.<String>ofNullable(getPname()).orElseGet(this::getObjselect);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */