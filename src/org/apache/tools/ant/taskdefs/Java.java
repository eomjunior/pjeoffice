/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.Vector;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.ExitException;
/*      */ import org.apache.tools.ant.ExitStatusException;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.taskdefs.condition.Os;
/*      */ import org.apache.tools.ant.types.Assertions;
/*      */ import org.apache.tools.ant.types.Commandline;
/*      */ import org.apache.tools.ant.types.CommandlineJava;
/*      */ import org.apache.tools.ant.types.Environment;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.Permissions;
/*      */ import org.apache.tools.ant.types.PropertySet;
/*      */ import org.apache.tools.ant.types.RedirectorElement;
/*      */ import org.apache.tools.ant.types.Reference;
/*      */ import org.apache.tools.ant.util.KeepAliveInputStream;
/*      */ import org.apache.tools.ant.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Java
/*      */   extends Task
/*      */ {
/*      */   private static final String TIMEOUT_MESSAGE = "Timeout: killed the sub-process";
/*   56 */   private CommandlineJava cmdl = new CommandlineJava();
/*   57 */   private Environment env = new Environment();
/*      */   private boolean fork = false;
/*      */   private boolean newEnvironment = false;
/*   60 */   private File dir = null;
/*      */   private boolean failOnError = false;
/*   62 */   private Long timeout = null;
/*      */   
/*      */   private String inputString;
/*      */   
/*      */   private File input;
/*      */   
/*      */   private File output;
/*      */   
/*      */   private File error;
/*   71 */   protected Redirector redirector = new Redirector(this);
/*      */   
/*      */   protected RedirectorElement redirectorElement;
/*      */   
/*      */   private String resultProperty;
/*   76 */   private Permissions perm = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean spawn = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean incompatibleWithSpawn = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Java(Task owner) {
/*   92 */     bindToOwner(owner);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/*  102 */     File savedDir = this.dir;
/*  103 */     Permissions savedPermissions = this.perm;
/*      */     
/*  105 */     int err = -1;
/*      */     try {
/*  107 */       checkConfiguration();
/*  108 */       err = executeJava();
/*  109 */       if (err != 0) {
/*  110 */         if (this.failOnError) {
/*  111 */           throw new ExitStatusException("Java returned: " + err, err, 
/*      */               
/*  113 */               getLocation());
/*      */         }
/*  115 */         log("Java Result: " + err, 0);
/*      */       } 
/*      */       
/*  118 */       maybeSetResultPropertyValue(err);
/*      */     } finally {
/*  120 */       this.dir = savedDir;
/*  121 */       this.perm = savedPermissions;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int executeJava() throws BuildException {
/*  135 */     return executeJava(getCommandLine());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkConfiguration() throws BuildException {
/*  143 */     String classname = getCommandLine().getClassname();
/*  144 */     String module = getCommandLine().getModule();
/*  145 */     String sourceFile = getCommandLine().getSourceFile();
/*  146 */     if (classname == null && getCommandLine().getJar() == null && module == null && sourceFile == null) {
/*  147 */       throw new BuildException("Classname must not be null.");
/*      */     }
/*  149 */     if (!this.fork && getCommandLine().getJar() != null) {
/*  150 */       throw new BuildException("Cannot execute a jar in non-forked mode. Please set fork='true'. ");
/*      */     }
/*      */     
/*  153 */     if (!this.fork && getCommandLine().getModule() != null) {
/*  154 */       throw new BuildException("Cannot execute a module in non-forked mode. Please set fork='true'. ");
/*      */     }
/*      */     
/*  157 */     if (!this.fork && sourceFile != null) {
/*  158 */       throw new BuildException("Cannot execute sourcefile in non-forked mode. Please set fork='true'");
/*      */     }
/*  160 */     if (this.spawn && !this.fork) {
/*  161 */       throw new BuildException("Cannot spawn a java process in non-forked mode. Please set fork='true'. ");
/*      */     }
/*      */     
/*  164 */     if (getCommandLine().getClasspath() != null && 
/*  165 */       getCommandLine().getJar() != null) {
/*  166 */       log("When using 'jar' attribute classpath-settings are ignored. See the manual for more information.", 3);
/*      */     }
/*      */     
/*  169 */     if (this.spawn && this.incompatibleWithSpawn) {
/*  170 */       getProject().log("spawn does not allow attributes related to input, output, error, result", 0);
/*      */ 
/*      */       
/*  173 */       getProject().log("spawn also does not allow timeout", 0);
/*  174 */       getProject().log("finally, spawn is not compatible with a nested I/O <redirector>", 0);
/*      */ 
/*      */       
/*  177 */       throw new BuildException("You have used an attribute or nested element which is not compatible with spawn");
/*      */     } 
/*      */     
/*  180 */     if (getCommandLine().getAssertions() != null && !this.fork) {
/*  181 */       log("Assertion statements are currently ignored in non-forked mode");
/*      */     }
/*  183 */     if (this.fork) {
/*  184 */       if (this.perm != null) {
/*  185 */         log("Permissions can not be set this way in forked mode.", 1);
/*      */       }
/*  187 */       log(getCommandLine().describeCommand(), 3);
/*      */     } else {
/*  189 */       if (getCommandLine().getVmCommand().size() > 1) {
/*  190 */         log("JVM args ignored when same JVM is used.", 1);
/*      */       }
/*      */       
/*  193 */       if (this.dir != null) {
/*  194 */         log("Working directory ignored when same JVM is used.", 1);
/*      */       }
/*      */       
/*  197 */       if (this.newEnvironment || null != this.env.getVariables()) {
/*  198 */         log("Changes to environment variables are ignored when same JVM is used.", 1);
/*      */       }
/*      */       
/*  201 */       if (getCommandLine().getBootclasspath() != null) {
/*  202 */         log("bootclasspath ignored when same JVM is used.", 1);
/*      */       }
/*      */       
/*  205 */       if (this.perm == null) {
/*  206 */         this.perm = new Permissions(true);
/*  207 */         log("running " + getCommandLine().getClassname() + " with default permissions (exit forbidden)", 3);
/*      */       } 
/*      */       
/*  210 */       log("Running in same VM " + getCommandLine().describeJavaCommand(), 3);
/*      */     } 
/*      */     
/*  213 */     setupRedirector();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int executeJava(CommandlineJava commandLine) {
/*      */     try {
/*  223 */       if (this.fork) {
/*  224 */         if (this.spawn) {
/*  225 */           spawn(commandLine.getCommandline());
/*  226 */           return 0;
/*      */         } 
/*  228 */         return fork(commandLine.getCommandline());
/*      */       } 
/*      */       try {
/*  231 */         run(commandLine);
/*  232 */         return 0;
/*  233 */       } catch (ExitException ex) {
/*  234 */         return ex.getStatus();
/*      */       } 
/*  236 */     } catch (BuildException e) {
/*  237 */       if (e.getLocation() == null && getLocation() != null) {
/*  238 */         e.setLocation(getLocation());
/*      */       }
/*  240 */       if (this.failOnError) {
/*  241 */         throw e;
/*      */       }
/*  243 */       if ("Timeout: killed the sub-process".equals(e.getMessage())) {
/*  244 */         log("Timeout: killed the sub-process");
/*      */       } else {
/*  246 */         log((Throwable)e);
/*      */       } 
/*  248 */       return -1;
/*  249 */     } catch (ThreadDeath t) {
/*  250 */       throw t;
/*  251 */     } catch (Throwable t) {
/*  252 */       if (this.failOnError) {
/*  253 */         throw new BuildException(t, getLocation());
/*      */       }
/*  255 */       log(t);
/*  256 */       return -1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSpawn(boolean spawn) {
/*  267 */     this.spawn = spawn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspath(Path s) {
/*  276 */     createClasspath().append(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createClasspath() {
/*  285 */     return getCommandLine().createClasspath(getProject()).createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createBootclasspath() {
/*  295 */     return getCommandLine().createBootclasspath(getProject()).createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulepath(Path mp) {
/*  305 */     createModulepath().append(mp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createModulepath() {
/*  315 */     return getCommandLine().createModulepath(getProject()).createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulepathRef(Reference r) {
/*  325 */     createModulepath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createUpgrademodulepath() {
/*  335 */     return getCommandLine().createUpgrademodulepath(getProject()).createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Permissions createPermissions() {
/*  344 */     this.perm = (this.perm == null) ? new Permissions() : this.perm;
/*  345 */     return this.perm;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspathRef(Reference r) {
/*  354 */     createClasspath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJar(File jarfile) throws BuildException {
/*  366 */     if (getCommandLine().getClassname() != null || getCommandLine().getModule() != null || 
/*  367 */       getCommandLine().getSourceFile() != null) {
/*  368 */       throw new BuildException("Cannot use combination of 'jar', 'sourcefile', 'classname', 'module' attributes in same command");
/*      */     }
/*      */     
/*  371 */     getCommandLine().setJar(jarfile.getAbsolutePath());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClassname(String s) throws BuildException {
/*  382 */     if (getCommandLine().getJar() != null || getCommandLine().getSourceFile() != null) {
/*  383 */       throw new BuildException("Cannot use combination of 'jar', 'classname', sourcefile attributes in same command");
/*      */     }
/*      */     
/*  386 */     getCommandLine().setClassname(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModule(String module) throws BuildException {
/*  398 */     if (getCommandLine().getJar() != null || getCommandLine().getSourceFile() != null) {
/*  399 */       throw new BuildException("Cannot use combination of 'jar', 'module', sourcefile attributes in same command");
/*      */     }
/*      */     
/*  402 */     getCommandLine().setModule(module);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSourceFile(String sourceFile) throws BuildException {
/*  415 */     String jar = getCommandLine().getJar();
/*  416 */     String className = getCommandLine().getClassname();
/*  417 */     String module = getCommandLine().getModule();
/*  418 */     if (jar != null || className != null || module != null) {
/*  419 */       throw new BuildException("Cannot use 'sourcefile' in combination with 'jar' or 'module' or 'classname'");
/*      */     }
/*      */     
/*  422 */     getCommandLine().setSourceFile(sourceFile);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArgs(String s) {
/*  434 */     log("The args attribute is deprecated. Please use nested arg elements.", 1);
/*      */     
/*  436 */     getCommandLine().createArgument().setLine(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCloneVm(boolean cloneVm) {
/*  449 */     getCommandLine().setCloneVm(cloneVm);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Commandline.Argument createArg() {
/*  458 */     return getCommandLine().createArgument();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResultProperty(String resultProperty) {
/*  470 */     this.resultProperty = resultProperty;
/*  471 */     this.incompatibleWithSpawn = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void maybeSetResultPropertyValue(int result) {
/*  481 */     String res = Integer.toString(result);
/*  482 */     if (this.resultProperty != null) {
/*  483 */       getProject().setNewProperty(this.resultProperty, res);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFork(boolean s) {
/*  493 */     this.fork = s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJvmargs(String s) {
/*  502 */     log("The jvmargs attribute is deprecated. Please use nested jvmarg elements.", 1);
/*      */     
/*  504 */     getCommandLine().createVmArgument().setLine(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Commandline.Argument createJvmarg() {
/*  513 */     return getCommandLine().createVmArgument();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJvm(String s) {
/*  522 */     getCommandLine().setVm(s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSysproperty(Environment.Variable sysp) {
/*  531 */     getCommandLine().addSysproperty(sysp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSyspropertyset(PropertySet sysp) {
/*  542 */     getCommandLine().addSyspropertyset(sysp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailonerror(boolean fail) {
/*  553 */     this.failOnError = fail;
/*  554 */     this.incompatibleWithSpawn |= fail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDir(File d) {
/*  564 */     this.dir = d;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutput(File out) {
/*  573 */     this.output = out;
/*  574 */     this.incompatibleWithSpawn = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInput(File input) {
/*  583 */     if (this.inputString != null) {
/*  584 */       throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
/*      */     }
/*      */     
/*  587 */     this.input = input;
/*  588 */     this.incompatibleWithSpawn = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInputString(String inputString) {
/*  597 */     if (this.input != null) {
/*  598 */       throw new BuildException("The \"input\" and \"inputstring\" attributes cannot both be specified");
/*      */     }
/*      */     
/*  601 */     this.inputString = inputString;
/*  602 */     this.incompatibleWithSpawn = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLogError(boolean logError) {
/*  614 */     this.redirector.setLogError(logError);
/*  615 */     this.incompatibleWithSpawn |= logError;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setError(File error) {
/*  626 */     this.error = error;
/*  627 */     this.incompatibleWithSpawn = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOutputproperty(String outputProp) {
/*  638 */     this.redirector.setOutputProperty(outputProp);
/*  639 */     this.incompatibleWithSpawn = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setErrorProperty(String errorProperty) {
/*  651 */     this.redirector.setErrorProperty(errorProperty);
/*  652 */     this.incompatibleWithSpawn = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxmemory(String max) {
/*  661 */     getCommandLine().setMaxmemory(max);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setJVMVersion(String value) {
/*  669 */     getCommandLine().setVmversion(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addEnv(Environment.Variable var) {
/*  682 */     this.env.addVariable(var);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNewenvironment(boolean newenv) {
/*  695 */     this.newEnvironment = newenv;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAppend(boolean append) {
/*  706 */     this.redirector.setAppend(append);
/*  707 */     this.incompatibleWithSpawn |= append;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDiscardOutput(boolean discard) {
/*  722 */     this.redirector.setDiscardOutput(discard);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDiscardError(boolean discard) {
/*  737 */     this.redirector.setDiscardError(discard);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimeout(Long value) {
/*  748 */     this.timeout = value;
/*  749 */     this.incompatibleWithSpawn |= (this.timeout != null) ? 1 : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addAssertions(Assertions asserts) {
/*  758 */     if (getCommandLine().getAssertions() != null) {
/*  759 */       throw new BuildException("Only one assertion declaration is allowed");
/*      */     }
/*  761 */     getCommandLine().setAssertions(asserts);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredRedirector(RedirectorElement redirectorElement) {
/*  769 */     if (this.redirectorElement != null) {
/*  770 */       throw new BuildException("cannot have > 1 nested redirectors");
/*      */     }
/*  772 */     this.redirectorElement = redirectorElement;
/*  773 */     this.incompatibleWithSpawn = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleOutput(String output) {
/*  785 */     if (this.redirector.getOutputStream() != null) {
/*  786 */       this.redirector.handleOutput(output);
/*      */     } else {
/*  788 */       super.handleOutput(output);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int handleInput(byte[] buffer, int offset, int length) throws IOException {
/*  808 */     return this.redirector.handleInput(buffer, offset, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleFlush(String output) {
/*  820 */     if (this.redirector.getOutputStream() != null) {
/*  821 */       this.redirector.handleFlush(output);
/*      */     } else {
/*  823 */       super.handleFlush(output);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleErrorOutput(String output) {
/*  836 */     if (this.redirector.getErrorStream() != null) {
/*  837 */       this.redirector.handleErrorOutput(output);
/*      */     } else {
/*  839 */       super.handleErrorOutput(output);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleErrorFlush(String output) {
/*  852 */     if (this.redirector.getErrorStream() != null) {
/*  853 */       this.redirector.handleErrorFlush(output);
/*      */     } else {
/*  855 */       super.handleErrorFlush(output);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setupRedirector() {
/*  863 */     this.redirector.setInput(this.input);
/*  864 */     this.redirector.setInputString(this.inputString);
/*  865 */     this.redirector.setOutput(this.output);
/*  866 */     this.redirector.setError(this.error);
/*  867 */     if (this.redirectorElement != null) {
/*  868 */       this.redirectorElement.configure(this.redirector);
/*      */     }
/*  870 */     if (!this.spawn && this.input == null && this.inputString == null)
/*      */     {
/*  872 */       this.redirector.setInputStream((InputStream)new KeepAliveInputStream(
/*  873 */             getProject().getDefaultInputStream()));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void run(CommandlineJava command) throws BuildException {
/*      */     try {
/*  884 */       ExecuteJava exe = new ExecuteJava();
/*  885 */       exe.setJavaCommand(command.getJavaCommand());
/*  886 */       exe.setClasspath(command.getClasspath());
/*  887 */       exe.setSystemProperties(command.getSystemProperties());
/*  888 */       exe.setPermissions(this.perm);
/*  889 */       exe.setTimeout(this.timeout);
/*  890 */       this.redirector.createStreams();
/*  891 */       exe.execute(getProject());
/*  892 */       this.redirector.complete();
/*  893 */       if (exe.killedProcess()) {
/*  894 */         throw new BuildException("Timeout: killed the sub-process");
/*      */       }
/*  896 */     } catch (IOException e) {
/*  897 */       throw new BuildException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int fork(String[] command) throws BuildException {
/*  907 */     Execute exe = new Execute(this.redirector.createHandler(), createWatchdog());
/*  908 */     setupExecutable(exe, command);
/*      */     
/*      */     try {
/*  911 */       int rc = exe.execute();
/*  912 */       this.redirector.complete();
/*  913 */       if (exe.killedProcess()) {
/*  914 */         throw new BuildException("Timeout: killed the sub-process");
/*      */       }
/*  916 */       return rc;
/*  917 */     } catch (IOException e) {
/*  918 */       throw new BuildException(e, getLocation());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void spawn(String[] command) throws BuildException {
/*  927 */     Execute exe = new Execute();
/*  928 */     setupExecutable(exe, command);
/*      */     try {
/*  930 */       exe.spawn();
/*  931 */     } catch (IOException e) {
/*  932 */       throw new BuildException(e, getLocation());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setupExecutable(Execute exe, String[] command) {
/*  944 */     exe.setAntRun(getProject());
/*  945 */     setupWorkingDir(exe);
/*  946 */     setupEnvironment(exe);
/*  947 */     setupCommandLine(exe, command);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setupEnvironment(Execute exe) {
/*  955 */     String[] environment = this.env.getVariables();
/*  956 */     if (environment != null) {
/*  957 */       for (String element : environment) {
/*  958 */         log("Setting environment variable: " + element, 3);
/*      */       }
/*      */     }
/*      */     
/*  962 */     exe.setNewenvironment(this.newEnvironment);
/*  963 */     exe.setEnvironment(environment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setupWorkingDir(Execute exe) {
/*  972 */     if (this.dir == null) {
/*  973 */       this.dir = getProject().getBaseDir();
/*  974 */     } else if (!this.dir.isDirectory()) {
/*  975 */       throw new BuildException(this.dir.getAbsolutePath() + " is not a valid directory", 
/*      */           
/*  977 */           getLocation());
/*      */     } 
/*  979 */     exe.setWorkingDirectory(this.dir);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setupCommandLine(Execute exe, String[] command) {
/*  992 */     if (Os.isFamily("openvms")) {
/*  993 */       setupCommandLineForVMS(exe, command);
/*      */     } else {
/*  995 */       exe.setCommandline(command);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setupCommandLineForVMS(Execute exe, String[] command) {
/* 1008 */     ExecuteJava.setupCommandLineForVMS(exe, command);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void run(String classname, Vector<String> args) throws BuildException {
/* 1020 */     CommandlineJava cmdj = new CommandlineJava();
/* 1021 */     cmdj.setClassname(classname);
/* 1022 */     args.forEach(arg -> cmdj.createArgument().setValue(arg));
/* 1023 */     run(cmdj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearArgs() {
/* 1030 */     getCommandLine().clearJavaArgs();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ExecuteWatchdog createWatchdog() throws BuildException {
/* 1043 */     if (this.timeout == null) {
/* 1044 */       return null;
/*      */     }
/* 1046 */     return new ExecuteWatchdog(this.timeout.longValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void log(Throwable t) {
/* 1055 */     log(StringUtils.getStackTrace(t), 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandlineJava getCommandLine() {
/* 1065 */     return this.cmdl;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandlineJava.SysProperties getSysProperties() {
/* 1075 */     return getCommandLine().getSystemProperties();
/*      */   }
/*      */   
/*      */   public Java() {}
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Java.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */