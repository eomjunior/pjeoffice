/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Environment;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCvsTask
/*     */   extends Task
/*     */ {
/*     */   public static final int DEFAULT_COMPRESSION_LEVEL = 3;
/*     */   private static final int MAXIMUM_COMRESSION_LEVEL = 9;
/*  58 */   private Commandline cmd = new Commandline();
/*     */   
/*  60 */   private List<Module> modules = new ArrayList<>();
/*     */ 
/*     */   
/*  63 */   private List<Commandline> commandlines = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String cvsRoot;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String cvsRsh;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String cvsPackage;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String tag;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_COMMAND = "checkout";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   private String command = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean quiet = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean reallyquiet = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   private int compression = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean noexec = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   private int port = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   private File passFile = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File dest;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean append = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File output;
/*     */ 
/*     */ 
/*     */   
/*     */   private File error;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failOnError = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private ExecuteStreamHandler executeStreamHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   private OutputStream outputStream;
/*     */ 
/*     */ 
/*     */   
/*     */   private OutputStream errorStream;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecuteStreamHandler(ExecuteStreamHandler handler) {
/* 164 */     this.executeStreamHandler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecuteStreamHandler getExecuteStreamHandler() {
/* 173 */     if (this.executeStreamHandler == null) {
/* 174 */       setExecuteStreamHandler(new PumpStreamHandler(getOutputStream(), 
/* 175 */             getErrorStream()));
/*     */     }
/*     */     
/* 178 */     return this.executeStreamHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setOutputStream(OutputStream outputStream) {
/* 187 */     this.outputStream = outputStream;
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
/*     */   protected OutputStream getOutputStream() {
/* 200 */     if (this.outputStream == null)
/*     */     {
/* 202 */       if (this.output != null) {
/*     */         try {
/* 204 */           setOutputStream(new PrintStream(new BufferedOutputStream(
/*     */                   
/* 206 */                   FileUtils.newOutputStream(Paths.get(this.output.getPath(), new String[0]), this.append))));
/*     */         }
/* 208 */         catch (IOException e) {
/* 209 */           throw new BuildException(e, getLocation());
/*     */         } 
/*     */       } else {
/* 212 */         setOutputStream((OutputStream)new LogOutputStream(this, 2));
/*     */       } 
/*     */     }
/*     */     
/* 216 */     return this.outputStream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setErrorStream(OutputStream errorStream) {
/* 225 */     this.errorStream = errorStream;
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
/*     */   protected OutputStream getErrorStream() {
/* 238 */     if (this.errorStream == null)
/*     */     {
/* 240 */       if (this.error != null) {
/*     */         
/*     */         try {
/* 243 */           setErrorStream(new PrintStream(new BufferedOutputStream(
/*     */                   
/* 245 */                   FileUtils.newOutputStream(Paths.get(this.error.getPath(), new String[0]), this.append))));
/*     */         }
/* 247 */         catch (IOException e) {
/* 248 */           throw new BuildException(e, getLocation());
/*     */         } 
/*     */       } else {
/* 251 */         setErrorStream((OutputStream)new LogOutputStream(this, 1));
/*     */       } 
/*     */     }
/*     */     
/* 255 */     return this.errorStream;
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
/*     */   protected void runCommand(Commandline toExecute) throws BuildException {
/* 271 */     Environment env = new Environment();
/*     */     
/* 273 */     if (this.port > 0) {
/* 274 */       Environment.Variable var = new Environment.Variable();
/* 275 */       var.setKey("CVS_CLIENT_PORT");
/* 276 */       var.setValue(String.valueOf(this.port));
/* 277 */       env.addVariable(var);
/*     */ 
/*     */ 
/*     */       
/* 281 */       var = new Environment.Variable();
/* 282 */       var.setKey("CVS_PSERVER_PORT");
/* 283 */       var.setValue(String.valueOf(this.port));
/* 284 */       env.addVariable(var);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     if (this.passFile == null) {
/*     */ 
/*     */       
/* 294 */       File defaultPassFile = new File(System.getProperty("cygwin.user.home", 
/* 295 */             System.getProperty("user.home")) + File.separatorChar + ".cvspass");
/*     */ 
/*     */       
/* 298 */       if (defaultPassFile.exists()) {
/* 299 */         setPassfile(defaultPassFile);
/*     */       }
/*     */     } 
/*     */     
/* 303 */     if (this.passFile != null) {
/* 304 */       if (this.passFile.isFile() && this.passFile.canRead()) {
/* 305 */         Environment.Variable var = new Environment.Variable();
/* 306 */         var.setKey("CVS_PASSFILE");
/* 307 */         var.setValue(String.valueOf(this.passFile));
/* 308 */         env.addVariable(var);
/* 309 */         log("Using cvs passfile: " + this.passFile, 3);
/*     */       }
/* 311 */       else if (!this.passFile.canRead()) {
/* 312 */         log("cvs passfile: " + this.passFile + " ignored as it is not readable", 1);
/*     */       }
/*     */       else {
/*     */         
/* 316 */         log("cvs passfile: " + this.passFile + " ignored as it is not a file", 1);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 322 */     if (this.cvsRsh != null) {
/* 323 */       Environment.Variable var = new Environment.Variable();
/* 324 */       var.setKey("CVS_RSH");
/* 325 */       var.setValue(String.valueOf(this.cvsRsh));
/* 326 */       env.addVariable(var);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 333 */     Execute exe = new Execute(getExecuteStreamHandler(), null);
/*     */     
/* 335 */     exe.setAntRun(getProject());
/* 336 */     if (this.dest == null) {
/* 337 */       this.dest = getProject().getBaseDir();
/*     */     }
/*     */     
/* 340 */     if (!this.dest.exists()) {
/* 341 */       this.dest.mkdirs();
/*     */     }
/*     */     
/* 344 */     exe.setWorkingDirectory(this.dest);
/* 345 */     exe.setCommandline(toExecute.getCommandline());
/* 346 */     exe.setEnvironment(env.getVariables());
/*     */     
/*     */     try {
/* 349 */       String actualCommandLine = executeToString(exe);
/*     */       
/* 351 */       log(actualCommandLine, 3);
/* 352 */       int retCode = exe.execute();
/* 353 */       log("retCode=" + retCode, 4);
/*     */       
/* 355 */       if (this.failOnError && Execute.isFailure(retCode)) {
/* 356 */         throw new BuildException(
/* 357 */             String.format("cvs exited with error code %s%nCommand line was [%s]", new Object[] {
/* 358 */                 Integer.valueOf(retCode), actualCommandLine }), getLocation());
/*     */       }
/* 360 */     } catch (IOException e) {
/* 361 */       if (this.failOnError) {
/* 362 */         throw new BuildException(e, getLocation());
/*     */       }
/* 364 */       log("Caught exception: " + e.getMessage(), 1);
/* 365 */     } catch (BuildException e) {
/* 366 */       BuildException buildException1; if (this.failOnError) {
/* 367 */         throw e;
/*     */       }
/* 369 */       Throwable t = e.getCause();
/* 370 */       if (t == null) {
/* 371 */         buildException1 = e;
/*     */       }
/* 373 */       log("Caught exception: " + buildException1.getMessage(), 1);
/* 374 */     } catch (Exception e) {
/* 375 */       if (this.failOnError) {
/* 376 */         throw new BuildException(e, getLocation());
/*     */       }
/* 378 */       log("Caught exception: " + e.getMessage(), 1);
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
/*     */   public void execute() throws BuildException {
/* 390 */     String savedCommand = getCommand();
/*     */     
/* 392 */     if (getCommand() == null && this.commandlines.isEmpty())
/*     */     {
/* 394 */       setCommand("checkout");
/*     */     }
/*     */     
/* 397 */     String c = getCommand();
/* 398 */     Commandline cloned = null;
/* 399 */     if (c != null) {
/* 400 */       cloned = (Commandline)this.cmd.clone();
/* 401 */       cloned.createArgument(true).setLine(c);
/* 402 */       addConfiguredCommandline(cloned, true);
/*     */     } 
/*     */     
/*     */     try {
/* 406 */       this.commandlines.forEach(this::runCommand);
/*     */     } finally {
/* 408 */       if (cloned != null) {
/* 409 */         removeCommandline(cloned);
/*     */       }
/* 411 */       setCommand(savedCommand);
/* 412 */       FileUtils.close(this.outputStream);
/* 413 */       FileUtils.close(this.errorStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String executeToString(Execute execute) {
/* 419 */     String cmdLine = Commandline.describeCommand(execute
/* 420 */         .getCommandline());
/* 421 */     StringBuilder buf = removeCvsPassword(cmdLine);
/*     */     
/* 423 */     String[] variableArray = execute.getEnvironment();
/* 424 */     if (variableArray != null) {
/* 425 */       buf.append(Arrays.<String>stream(variableArray).map(variable -> String.format("%n\t%s", new Object[] { variable
/* 426 */               })).collect(Collectors.joining("", String.format("%n%nenvironment:%n", new Object[0]), "")));
/*     */     }
/*     */     
/* 429 */     return buf.toString();
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
/*     */   private StringBuilder removeCvsPassword(String cmdLine) {
/* 441 */     StringBuilder buf = new StringBuilder(cmdLine);
/*     */     
/* 443 */     int start = cmdLine.indexOf("-d:");
/*     */     
/* 445 */     if (start >= 0) {
/* 446 */       int stop = cmdLine.indexOf('@', start);
/* 447 */       int startproto = cmdLine.indexOf(':', start);
/* 448 */       int startuser = cmdLine.indexOf(':', startproto + 1);
/* 449 */       int startpass = cmdLine.indexOf(':', startuser + 1);
/* 450 */       if (stop >= 0 && startpass > startproto && startpass < stop) {
/* 451 */         for (int i = startpass + 1; i < stop; i++) {
/* 452 */           buf.replace(i, i + 1, "*");
/*     */         }
/*     */       }
/*     */     } 
/* 456 */     return buf;
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
/*     */   public void setCvsRoot(String root) {
/* 468 */     if (root != null && root.trim().isEmpty()) {
/* 469 */       root = null;
/*     */     }
/*     */     
/* 472 */     this.cvsRoot = root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCvsRoot() {
/* 481 */     return this.cvsRoot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCvsRsh(String rsh) {
/* 490 */     if (rsh != null && rsh.trim().isEmpty()) {
/* 491 */       rsh = null;
/*     */     }
/*     */     
/* 494 */     this.cvsRsh = rsh;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCvsRsh() {
/* 503 */     return this.cvsRsh;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/* 512 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 520 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassfile(File passFile) {
/* 529 */     this.passFile = passFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getPassFile() {
/* 537 */     return this.passFile;
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
/*     */   public void setDest(File dest) {
/* 550 */     this.dest = dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDest() {
/* 559 */     return this.dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPackage(String p) {
/* 568 */     this.cvsPackage = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPackage() {
/* 577 */     return this.cvsPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTag() {
/* 585 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTag(String p) {
/* 594 */     if (p != null && !p.trim().isEmpty()) {
/* 595 */       this.tag = p;
/* 596 */       addCommandArgument("-r" + p);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addCommandArgument(String arg) {
/* 606 */     addCommandArgument(this.cmd, arg);
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
/*     */   public void addCommandArgument(Commandline c, String arg) {
/* 620 */     c.createArgument().setValue(arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDate(String p) {
/* 630 */     if (p != null && !p.trim().isEmpty()) {
/* 631 */       addCommandArgument("-D");
/* 632 */       addCommandArgument(p);
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
/*     */   public void setCommand(String c) {
/* 645 */     this.command = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommand() {
/* 656 */     return this.command;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuiet(boolean q) {
/* 664 */     this.quiet = q;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReallyquiet(boolean q) {
/* 673 */     this.reallyquiet = q;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoexec(boolean ne) {
/* 682 */     this.noexec = ne;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(File output) {
/* 690 */     this.output = output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setError(File error) {
/* 699 */     this.error = error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAppend(boolean value) {
/* 707 */     this.append = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean failOnError) {
/* 718 */     this.failOnError = failOnError;
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
/*     */   
/*     */   protected void configureCommandline(Commandline c) {
/* 741 */     if (c == null) {
/*     */       return;
/*     */     }
/* 744 */     c.setExecutable("cvs");
/* 745 */     if (this.cvsPackage != null) {
/* 746 */       c.createArgument().setLine(this.cvsPackage);
/*     */     }
/* 748 */     for (Module m : this.modules) {
/* 749 */       c.createArgument().setValue(m.getName());
/*     */     }
/* 751 */     if (this.compression > 0 && this.compression <= 9)
/*     */     {
/* 753 */       c.createArgument(true).setValue("-z" + this.compression);
/*     */     }
/* 755 */     if (this.quiet && !this.reallyquiet) {
/* 756 */       c.createArgument(true).setValue("-q");
/*     */     }
/* 758 */     if (this.reallyquiet) {
/* 759 */       c.createArgument(true).setValue("-Q");
/*     */     }
/* 761 */     if (this.noexec) {
/* 762 */       c.createArgument(true).setValue("-n");
/*     */     }
/* 764 */     if (this.cvsRoot != null) {
/* 765 */       c.createArgument(true).setLine("-d" + this.cvsRoot);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeCommandline(Commandline c) {
/* 774 */     this.commandlines.remove(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredCommandline(Commandline c) {
/* 782 */     addConfiguredCommandline(c, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredCommandline(Commandline c, boolean insertAtStart) {
/* 793 */     if (c == null) {
/*     */       return;
/*     */     }
/* 796 */     configureCommandline(c);
/* 797 */     if (insertAtStart) {
/* 798 */       this.commandlines.add(0, c);
/*     */     } else {
/* 800 */       this.commandlines.add(c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompressionLevel(int level) {
/* 810 */     this.compression = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompression(boolean usecomp) {
/* 820 */     setCompressionLevel(usecomp ? 
/* 821 */         3 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addModule(Module m) {
/* 831 */     this.modules.add(m);
/*     */   }
/*     */   
/*     */   protected List<Module> getModules() {
/* 835 */     return new ArrayList<>(this.modules);
/*     */   }
/*     */   
/*     */   public static final class Module {
/*     */     private String name;
/*     */     
/*     */     public void setName(String s) {
/* 842 */       this.name = s;
/*     */     }
/*     */     public String getName() {
/* 845 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/AbstractCvsTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */