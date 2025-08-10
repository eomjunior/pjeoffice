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
/*     */ public class CCUpdate
/*     */   extends ClearCase
/*     */ {
/*     */   public static final String FLAG_GRAPHICAL = "-graphical";
/*     */   public static final String FLAG_LOG = "-log";
/*     */   public static final String FLAG_OVERWRITE = "-overwrite";
/*     */   public static final String FLAG_NOVERWRITE = "-noverwrite";
/*     */   public static final String FLAG_RENAME = "-rename";
/*     */   public static final String FLAG_CURRENTTIME = "-ctime";
/*     */   public static final String FLAG_PRESERVETIME = "-ptime";
/*     */   private boolean mGraphical = false;
/*     */   private boolean mOverwrite = false;
/*     */   private boolean mRename = false;
/*     */   private boolean mCtime = false;
/*     */   private boolean mPtime = false;
/* 118 */   private String mLog = null;
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
/* 133 */     if (getViewPath() == null) {
/* 134 */       setViewPath(aProj.getBaseDir().getPath());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     commandLine.setExecutable(getClearToolCommand());
/* 141 */     commandLine.createArgument().setValue("update");
/*     */ 
/*     */     
/* 144 */     checkOptions(commandLine);
/*     */ 
/*     */     
/* 147 */     getProject().log(commandLine.toString(), 4);
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
/* 165 */     if (getGraphical()) {
/*     */       
/* 167 */       cmd.createArgument().setValue("-graphical");
/*     */     } else {
/* 169 */       if (getOverwrite()) {
/*     */         
/* 171 */         cmd.createArgument().setValue("-overwrite");
/* 172 */       } else if (getRename()) {
/*     */         
/* 174 */         cmd.createArgument().setValue("-rename");
/*     */       } else {
/*     */         
/* 177 */         cmd.createArgument().setValue("-noverwrite");
/*     */       } 
/*     */       
/* 180 */       if (getCurrentTime()) {
/*     */         
/* 182 */         cmd.createArgument().setValue("-ctime");
/* 183 */       } else if (getPreserveTime()) {
/*     */         
/* 185 */         cmd.createArgument().setValue("-ptime");
/*     */       } 
/*     */ 
/*     */       
/* 189 */       getLogCommand(cmd);
/*     */     } 
/*     */ 
/*     */     
/* 193 */     cmd.createArgument().setValue(getViewPath());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGraphical(boolean graphical) {
/* 202 */     this.mGraphical = graphical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getGraphical() {
/* 211 */     return this.mGraphical;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOverwrite(boolean ow) {
/* 220 */     this.mOverwrite = ow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getOverwrite() {
/* 229 */     return this.mOverwrite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRename(boolean ren) {
/* 238 */     this.mRename = ren;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRename() {
/* 247 */     return this.mRename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentTime(boolean ct) {
/* 257 */     this.mCtime = ct;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCurrentTime() {
/* 266 */     return this.mCtime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPreserveTime(boolean pt) {
/* 276 */     this.mPtime = pt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getPreserveTime() {
/* 285 */     return this.mPtime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLog(String log) {
/* 295 */     this.mLog = log;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLog() {
/* 304 */     return this.mLog;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void getLogCommand(Commandline cmd) {
/* 313 */     if (getLog() == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     cmd.createArgument().setValue("-log");
/* 322 */     cmd.createArgument().setValue(getLog());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/CCUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */