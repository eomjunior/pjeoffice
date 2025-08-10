/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Environment;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.RedirectorElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExecTask
/*     */   extends Task
/*     */ {
/*  45 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private String os;
/*     */   
/*     */   private String osFamily;
/*     */   
/*     */   private File dir;
/*     */   protected boolean failOnError = false;
/*     */   protected boolean newEnvironment = false;
/*  54 */   private Long timeout = null;
/*  55 */   private Environment env = new Environment();
/*  56 */   protected Commandline cmdl = new Commandline();
/*     */   
/*     */   private String resultProperty;
/*     */   
/*     */   private boolean failIfExecFails = true;
/*     */   
/*     */   private String executable;
/*     */   private boolean resolveExecutable = false;
/*     */   private boolean searchPath = false;
/*     */   private boolean spawn = false;
/*     */   private boolean incompatibleWithSpawn = false;
/*     */   private String inputString;
/*     */   private File input;
/*     */   private File output;
/*     */   private File error;
/*  71 */   protected Redirector redirector = new Redirector(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected RedirectorElement redirectorElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean vmLauncher = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExecTask(Task owner) {
/*  96 */     bindToOwner(owner);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpawn(boolean spawn) {
/* 106 */     this.spawn = spawn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeout(Long value) {
/* 117 */     this.timeout = value;
/* 118 */     this.incompatibleWithSpawn |= (this.timeout != null) ? 1 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeout(Integer value) {
/* 127 */     setTimeout(
/* 128 */         (value == null) ? null : Long.valueOf(value.intValue()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutable(String value) {
/* 136 */     this.executable = value;
/* 137 */     this.cmdl.setExecutable(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File d) {
/* 145 */     this.dir = d;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOs(String os) {
/* 153 */     this.os = os;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getOs() {
/* 162 */     return this.os;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommand(Commandline cmdl) {
/* 171 */     log("The command attribute is deprecated.\nPlease use the executable attribute and nested arg elements.", 1);
/*     */     
/* 173 */     this.cmdl = cmdl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(File out) {
/* 183 */     this.output = out;
/* 184 */     this.incompatibleWithSpawn = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInput(File input) {
/* 193 */     if (this.inputString != null) {
/* 194 */       throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
/*     */     }
/*     */     
/* 197 */     this.input = input;
/* 198 */     this.incompatibleWithSpawn = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputString(String inputString) {
/* 207 */     if (this.input != null) {
/* 208 */       throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
/*     */     }
/*     */     
/* 211 */     this.inputString = inputString;
/* 212 */     this.incompatibleWithSpawn = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogError(boolean logError) {
/* 222 */     this.redirector.setLogError(logError);
/* 223 */     this.incompatibleWithSpawn |= logError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setError(File error) {
/* 234 */     this.error = error;
/* 235 */     this.incompatibleWithSpawn = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputproperty(String outputProp) {
/* 245 */     this.redirector.setOutputProperty(outputProp);
/* 246 */     this.incompatibleWithSpawn = true;
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
/*     */   public void setErrorProperty(String errorProperty) {
/* 258 */     this.redirector.setErrorProperty(errorProperty);
/* 259 */     this.incompatibleWithSpawn = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailonerror(boolean fail) {
/* 268 */     this.failOnError = fail;
/* 269 */     this.incompatibleWithSpawn |= fail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNewenvironment(boolean newenv) {
/* 279 */     this.newEnvironment = newenv;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResolveExecutable(boolean resolveExecutable) {
/* 289 */     this.resolveExecutable = resolveExecutable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSearchPath(boolean searchPath) {
/* 299 */     this.searchPath = searchPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getResolveExecutable() {
/* 310 */     return this.resolveExecutable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addEnv(Environment.Variable var) {
/* 319 */     this.env.addVariable(var);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline.Argument createArg() {
/* 328 */     return this.cmdl.createArgument();
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
/*     */   public void setResultProperty(String resultProperty) {
/* 340 */     this.resultProperty = resultProperty;
/* 341 */     this.incompatibleWithSpawn = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void maybeSetResultPropertyValue(int result) {
/* 351 */     if (this.resultProperty != null) {
/* 352 */       String res = Integer.toString(result);
/* 353 */       getProject().setNewProperty(this.resultProperty, res);
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
/*     */   
/*     */   public void setFailIfExecutionFails(boolean flag) {
/* 366 */     this.failIfExecFails = flag;
/* 367 */     this.incompatibleWithSpawn |= flag;
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
/*     */   public void setAppend(boolean append) {
/* 379 */     this.redirector.setAppend(append);
/* 380 */     this.incompatibleWithSpawn |= append;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDiscardOutput(boolean discard) {
/* 395 */     this.redirector.setDiscardOutput(discard);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDiscardError(boolean discard) {
/* 410 */     this.redirector.setDiscardError(discard);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredRedirector(RedirectorElement redirectorElement) {
/* 420 */     if (this.redirectorElement != null) {
/* 421 */       throw new BuildException("cannot have > 1 nested <redirector>s");
/*     */     }
/* 423 */     this.redirectorElement = redirectorElement;
/* 424 */     this.incompatibleWithSpawn = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOsFamily(String osFamily) {
/* 433 */     this.osFamily = osFamily.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getOsFamily() {
/* 442 */     return this.osFamily;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveExecutable(String exec, boolean mustSearchPath) {
/* 459 */     if (!this.resolveExecutable) {
/* 460 */       return exec;
/*     */     }
/*     */     
/* 463 */     File executableFile = getProject().resolveFile(exec);
/* 464 */     if (executableFile.exists()) {
/* 465 */       return executableFile.getAbsolutePath();
/*     */     }
/*     */     
/* 468 */     if (this.dir != null) {
/* 469 */       executableFile = FILE_UTILS.resolveFile(this.dir, exec);
/* 470 */       if (executableFile.exists()) {
/* 471 */         return executableFile.getAbsolutePath();
/*     */       }
/*     */     } 
/*     */     
/* 475 */     if (mustSearchPath) {
/* 476 */       Path p = null;
/* 477 */       String[] environment = this.env.getVariables();
/* 478 */       if (environment != null) {
/* 479 */         for (String variable : environment) {
/* 480 */           if (isPath(variable)) {
/* 481 */             p = new Path(getProject(), getPath(variable));
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/* 486 */       if (p == null) {
/* 487 */         String path = getPath(Execute.getEnvironmentVariables());
/* 488 */         if (path != null) {
/* 489 */           p = new Path(getProject(), path);
/*     */         }
/*     */       } 
/* 492 */       if (p != null) {
/* 493 */         for (String pathname : p.list()) {
/*     */           
/* 495 */           executableFile = FILE_UTILS.resolveFile(new File(pathname), exec);
/* 496 */           if (executableFile.exists()) {
/* 497 */             return executableFile.getAbsolutePath();
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 504 */     return exec;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 520 */     if (!isValidOs()) {
/*     */       return;
/*     */     }
/* 523 */     File savedDir = this.dir;
/* 524 */     this.cmdl.setExecutable(resolveExecutable(this.executable, this.searchPath));
/* 525 */     checkConfiguration();
/*     */     try {
/* 527 */       runExec(prepareExec());
/*     */     } finally {
/* 529 */       this.dir = savedDir;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkConfiguration() throws BuildException {
/* 538 */     if (this.cmdl.getExecutable() == null) {
/* 539 */       throw new BuildException("no executable specified", getLocation());
/*     */     }
/* 541 */     if (this.dir != null && !this.dir.exists()) {
/* 542 */       throw new BuildException("The directory " + this.dir + " does not exist");
/*     */     }
/* 544 */     if (this.dir != null && !this.dir.isDirectory()) {
/* 545 */       throw new BuildException(this.dir + " is not a directory");
/*     */     }
/* 547 */     if (this.spawn && this.incompatibleWithSpawn) {
/* 548 */       getProject().log("spawn does not allow attributes related to input, output, error, result", 0);
/*     */       
/* 550 */       getProject().log("spawn also does not allow timeout", 0);
/* 551 */       getProject().log("finally, spawn is not compatible with a nested I/O <redirector>", 0);
/*     */       
/* 553 */       throw new BuildException("You have used an attribute or nested element which is not compatible with spawn");
/*     */     } 
/*     */     
/* 556 */     setupRedirector();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setupRedirector() {
/* 563 */     this.redirector.setInput(this.input);
/* 564 */     this.redirector.setInputString(this.inputString);
/* 565 */     this.redirector.setOutput(this.output);
/* 566 */     this.redirector.setError(this.error);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidOs() {
/* 588 */     if (this.osFamily != null && !Os.isFamily(this.osFamily)) {
/* 589 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 594 */     String myos = System.getProperty("os.name");
/* 595 */     log("Current OS is " + myos, 3);
/* 596 */     if (this.os != null && !this.os.contains(myos)) {
/*     */       
/* 598 */       log("This OS, " + myos + " was not found in the specified list of valid OSes: " + this.os, 3);
/*     */ 
/*     */       
/* 601 */       return false;
/*     */     } 
/* 603 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVMLauncher(boolean vmLauncher) {
/* 613 */     this.vmLauncher = vmLauncher;
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
/*     */   protected Execute prepareExec() throws BuildException {
/* 625 */     if (this.dir == null) {
/* 626 */       this.dir = getProject().getBaseDir();
/*     */     }
/* 628 */     if (this.redirectorElement != null) {
/* 629 */       this.redirectorElement.configure(this.redirector);
/*     */     }
/* 631 */     Execute exe = new Execute(createHandler(), createWatchdog());
/* 632 */     exe.setAntRun(getProject());
/* 633 */     exe.setWorkingDirectory(this.dir);
/* 634 */     exe.setVMLauncher(this.vmLauncher);
/* 635 */     String[] environment = this.env.getVariables();
/* 636 */     if (environment != null) {
/* 637 */       for (String variable : environment) {
/* 638 */         log("Setting environment variable: " + variable, 3);
/*     */       }
/*     */     }
/*     */     
/* 642 */     exe.setNewenvironment(this.newEnvironment);
/* 643 */     exe.setEnvironment(environment);
/* 644 */     return exe;
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
/*     */   
/*     */   protected final void runExecute(Execute exe) throws IOException {
/* 657 */     int returnCode = -1;
/*     */     
/* 659 */     if (!this.spawn) {
/* 660 */       returnCode = exe.execute();
/*     */ 
/*     */       
/* 663 */       if (exe.killedProcess()) {
/* 664 */         String msg = "Timeout: killed the sub-process";
/* 665 */         if (this.failOnError) {
/* 666 */           throw new BuildException(msg);
/*     */         }
/* 668 */         log(msg, 1);
/*     */       } 
/* 670 */       maybeSetResultPropertyValue(returnCode);
/* 671 */       this.redirector.complete();
/* 672 */       if (Execute.isFailure(returnCode)) {
/* 673 */         if (this.failOnError) {
/* 674 */           throw new BuildException(getTaskType() + " returned: " + returnCode, 
/* 675 */               getLocation());
/*     */         }
/* 677 */         log("Result: " + returnCode, 0);
/*     */       } 
/*     */     } else {
/* 680 */       exe.spawn();
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected void runExec(Execute exe) throws BuildException {
/* 695 */     log(this.cmdl.describeCommand(), 3);
/*     */     
/* 697 */     exe.setCommandline(this.cmdl.getCommandline());
/*     */     try {
/* 699 */       runExecute(exe);
/* 700 */     } catch (IOException e) {
/* 701 */       if (this.failIfExecFails) {
/* 702 */         throw new BuildException("Execute failed: " + e.toString(), e, 
/* 703 */             getLocation());
/*     */       }
/* 705 */       log("Execute failed: " + e.toString(), 0);
/*     */     } finally {
/*     */       
/* 708 */       logFlush();
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
/*     */   protected ExecuteStreamHandler createHandler() throws BuildException {
/* 720 */     return this.redirector.createHandler();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecuteWatchdog createWatchdog() throws BuildException {
/* 731 */     return (this.timeout == null) ? 
/* 732 */       null : new ExecuteWatchdog(this.timeout.longValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logFlush() {}
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPath(String line) {
/* 742 */     return (line.startsWith("PATH=") || line
/* 743 */       .startsWith("Path="));
/*     */   }
/*     */   
/*     */   private String getPath(String line) {
/* 747 */     return line.substring("PATH=".length());
/*     */   }
/*     */   
/*     */   private String getPath(Map<String, String> map) {
/* 751 */     String p = map.get("PATH");
/* 752 */     return (p != null) ? p : map.get("Path");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/ExecTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */