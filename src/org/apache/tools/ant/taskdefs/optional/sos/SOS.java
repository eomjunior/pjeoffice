/*     */ package org.apache.tools.ant.taskdefs.optional.sos;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.LogStreamHandler;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SOS
/*     */   extends Task
/*     */   implements SOSCmd
/*     */ {
/*     */   private static final int ERROR_EXIT_STATUS = 255;
/*  42 */   private String sosCmdDir = null;
/*  43 */   private String sosUsername = null;
/*  44 */   private String sosPassword = null;
/*  45 */   private String projectPath = null;
/*  46 */   private String vssServerPath = null;
/*  47 */   private String sosServerPath = null;
/*  48 */   private String sosHome = null;
/*  49 */   private String localPath = null;
/*  50 */   private String version = null;
/*  51 */   private String label = null;
/*  52 */   private String comment = null;
/*  53 */   private String filename = null;
/*     */ 
/*     */   
/*     */   private boolean noCompress = false;
/*     */ 
/*     */   
/*     */   private boolean noCache = false;
/*     */ 
/*     */   
/*     */   private boolean recursive = false;
/*     */ 
/*     */   
/*     */   private boolean verbose = false;
/*     */ 
/*     */   
/*     */   protected Commandline commandLine;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setNoCache(boolean nocache) {
/*  73 */     this.noCache = nocache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setNoCompress(boolean nocompress) {
/*  82 */     this.noCompress = nocompress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSosCmd(String dir) {
/*  92 */     this.sosCmdDir = FileUtils.translatePath(dir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setUsername(String username) {
/* 103 */     this.sosUsername = username;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setPassword(String password) {
/* 112 */     this.sosPassword = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setProjectPath(String projectpath) {
/* 123 */     if (projectpath.startsWith("$")) {
/* 124 */       this.projectPath = projectpath;
/*     */     } else {
/* 126 */       this.projectPath = "$" + projectpath;
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
/*     */   public final void setVssServerPath(String vssServerPath) {
/* 138 */     this.vssServerPath = vssServerPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSosHome(String sosHome) {
/* 147 */     this.sosHome = sosHome;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSosServerPath(String sosServerPath) {
/* 159 */     this.sosServerPath = sosServerPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setLocalPath(Path path) {
/* 168 */     this.localPath = path.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 177 */     this.verbose = verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalFilename(String file) {
/* 187 */     this.filename = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalRecursive(boolean recurse) {
/* 195 */     this.recursive = recurse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalComment(String text) {
/* 203 */     this.comment = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalLabel(String text) {
/* 211 */     this.label = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInternalVersion(String text) {
/* 219 */     this.version = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSosCommand() {
/* 228 */     if (this.sosCmdDir == null) {
/* 229 */       return "soscmd";
/*     */     }
/* 231 */     return this.sosCmdDir + File.separator + "soscmd";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getComment() {
/* 240 */     return this.comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getVersion() {
/* 248 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getLabel() {
/* 256 */     return this.label;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUsername() {
/* 264 */     return this.sosUsername;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPassword() {
/* 272 */     return (this.sosPassword == null) ? "" : this.sosPassword;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getProjectPath() {
/* 280 */     return this.projectPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getVssServerPath() {
/* 288 */     return this.vssServerPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSosHome() {
/* 296 */     return this.sosHome;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSosServerPath() {
/* 304 */     return this.sosServerPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getFilename() {
/* 312 */     return this.filename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getNoCompress() {
/* 322 */     return this.noCompress ? "-nocompress" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getNoCache() {
/* 331 */     return this.noCache ? "-nocache" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getVerbose() {
/* 340 */     return this.verbose ? "-verbose" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getRecursive() {
/* 349 */     return this.recursive ? "-recursive" : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getLocalPath() {
/* 360 */     if (this.localPath == null) {
/* 361 */       return getProject().getBaseDir().getAbsolutePath();
/*     */     }
/*     */     
/* 364 */     File dir = getProject().resolveFile(this.localPath);
/* 365 */     if (!dir.exists()) {
/* 366 */       boolean done = (dir.mkdirs() || dir.isDirectory());
/* 367 */       if (!done) {
/* 368 */         String msg = "Directory " + this.localPath + " creation was not successful for an unknown reason";
/*     */         
/* 370 */         throw new BuildException(msg, getLocation());
/*     */       } 
/* 372 */       getProject().log("Created dir: " + dir.getAbsolutePath());
/*     */     } 
/* 374 */     return dir.getAbsolutePath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Commandline buildCmdLine();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 394 */     int result = 0;
/* 395 */     buildCmdLine();
/* 396 */     result = run(this.commandLine);
/* 397 */     if (result == 255) {
/* 398 */       String msg = "Failed executing: " + this.commandLine.toString();
/* 399 */       throw new BuildException(msg, getLocation());
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
/*     */   protected int run(Commandline cmd) {
/*     */     try {
/* 412 */       Execute exe = new Execute((ExecuteStreamHandler)new LogStreamHandler(this, 2, 1));
/*     */ 
/*     */ 
/*     */       
/* 416 */       exe.setAntRun(getProject());
/* 417 */       exe.setWorkingDirectory(getProject().getBaseDir());
/* 418 */       exe.setCommandline(cmd.getCommandline());
/* 419 */       exe.setVMLauncher(false);
/* 420 */       return exe.execute();
/* 421 */     } catch (IOException e) {
/* 422 */       throw new BuildException(e, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void getRequiredAttributes() {
/* 429 */     this.commandLine.setExecutable(getSosCommand());
/*     */     
/* 431 */     if (getSosServerPath() == null) {
/* 432 */       throw new BuildException("sosserverpath attribute must be set!", getLocation());
/*     */     }
/* 434 */     this.commandLine.createArgument().setValue("-server");
/* 435 */     this.commandLine.createArgument().setValue(getSosServerPath());
/*     */     
/* 437 */     if (getUsername() == null) {
/* 438 */       throw new BuildException("username attribute must be set!", getLocation());
/*     */     }
/* 440 */     this.commandLine.createArgument().setValue("-name");
/* 441 */     this.commandLine.createArgument().setValue(getUsername());
/*     */ 
/*     */     
/* 444 */     this.commandLine.createArgument().setValue("-password");
/* 445 */     this.commandLine.createArgument().setValue(getPassword());
/*     */     
/* 447 */     if (getVssServerPath() == null) {
/* 448 */       throw new BuildException("vssserverpath attribute must be set!", getLocation());
/*     */     }
/* 450 */     this.commandLine.createArgument().setValue("-database");
/* 451 */     this.commandLine.createArgument().setValue(getVssServerPath());
/*     */     
/* 453 */     if (getProjectPath() == null) {
/* 454 */       throw new BuildException("projectpath attribute must be set!", getLocation());
/*     */     }
/* 456 */     this.commandLine.createArgument().setValue("-project");
/* 457 */     this.commandLine.createArgument().setValue(getProjectPath());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void getOptionalAttributes() {
/* 463 */     this.commandLine.createArgument().setValue(getVerbose());
/*     */     
/* 465 */     this.commandLine.createArgument().setValue(getNoCompress());
/*     */     
/* 467 */     if (getSosHome() == null) {
/*     */       
/* 469 */       this.commandLine.createArgument().setValue(getNoCache());
/*     */     } else {
/* 471 */       this.commandLine.createArgument().setValue("-soshome");
/* 472 */       this.commandLine.createArgument().setValue(getSosHome());
/*     */     } 
/*     */     
/* 475 */     if (getLocalPath() != null) {
/* 476 */       this.commandLine.createArgument().setValue("-workdir");
/* 477 */       this.commandLine.createArgument().setValue(getLocalPath());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/sos/SOS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */