/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.filters.LineContainsRegExp;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Environment;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.RedirectorElement;
/*     */ import org.apache.tools.ant.types.RegularExpression;
/*     */ import org.apache.tools.ant.types.ResourceCollection;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractJarSignerTask
/*     */   extends Task
/*     */ {
/*     */   public static final String ERROR_NO_SOURCE = "jar must be set through jar attribute or nested filesets";
/*     */   protected static final String JARSIGNER_COMMAND = "jarsigner";
/*     */   protected File jar;
/*     */   protected String alias;
/*     */   protected String keystore;
/*     */   protected String storepass;
/*     */   protected String storetype;
/*     */   protected String keypass;
/*     */   protected boolean verbose;
/*     */   protected boolean strict = false;
/*     */   protected String maxMemory;
/*  95 */   protected Vector<FileSet> filesets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RedirectorElement redirector;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   private Environment sysProperties = new Environment();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   private Path path = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private String executable;
/*     */ 
/*     */   
/*     */   private String providerName;
/*     */ 
/*     */   
/*     */   private String providerClass;
/*     */ 
/*     */   
/*     */   private String providerArg;
/*     */ 
/*     */   
/* 129 */   private List<Commandline.Argument> additionalArgs = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxmemory(String max) {
/* 138 */     this.maxMemory = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJar(File jar) {
/* 147 */     this.jar = jar;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlias(String alias) {
/* 156 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeystore(String keystore) {
/* 165 */     this.keystore = keystore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStorepass(String storepass) {
/* 174 */     this.storepass = storepass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStoretype(String storetype) {
/* 183 */     this.storetype = storetype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeypass(String keypass) {
/* 192 */     this.keypass = keypass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/* 201 */     this.verbose = verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStrict(boolean strict) {
/* 210 */     this.strict = strict;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 220 */     this.filesets.addElement(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSysproperty(Environment.Variable sysp) {
/* 229 */     this.sysProperties.addVariable(sysp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createPath() {
/* 239 */     if (this.path == null) {
/* 240 */       this.path = new Path(getProject());
/*     */     }
/* 242 */     return this.path.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProviderName(String providerName) {
/* 253 */     this.providerName = providerName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProviderClass(String providerClass) {
/* 264 */     this.providerClass = providerClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProviderArg(String providerArg) {
/* 275 */     this.providerArg = providerArg;
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
/*     */   public void addArg(Commandline.Argument arg) {
/* 287 */     this.additionalArgs.add(arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beginExecution() {
/* 295 */     this.redirector = createRedirector();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void endExecution() {
/* 302 */     this.redirector = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RedirectorElement createRedirector() {
/* 311 */     RedirectorElement result = new RedirectorElement();
/* 312 */     if (this.storepass != null) {
/* 313 */       StringBuilder input = (new StringBuilder(this.storepass)).append('\n');
/* 314 */       if (this.keypass != null) {
/* 315 */         input.append(this.keypass).append('\n');
/*     */       }
/* 317 */       result.setInputString(input.toString());
/* 318 */       result.setLogInputString(false);
/*     */       
/* 320 */       LineContainsRegExp filter = new LineContainsRegExp();
/* 321 */       RegularExpression rx = new RegularExpression();
/*     */       
/* 323 */       rx.setPattern("^(Enter Passphrase for keystore: |Enter key password for .+: )$");
/* 324 */       filter.addConfiguredRegexp(rx);
/* 325 */       filter.setNegate(true);
/* 326 */       result.createErrorFilterChain().addLineContainsRegExp(filter);
/*     */     } 
/* 328 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RedirectorElement getRedirector() {
/* 337 */     return this.redirector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutable(String executable) {
/* 347 */     this.executable = executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setCommonOptions(ExecTask cmd) {
/* 355 */     if (this.maxMemory != null) {
/* 356 */       addValue(cmd, "-J-Xmx" + this.maxMemory);
/*     */     }
/*     */     
/* 359 */     if (this.verbose) {
/* 360 */       addValue(cmd, "-verbose");
/*     */     }
/*     */     
/* 363 */     if (this.strict) {
/* 364 */       addValue(cmd, "-strict");
/*     */     }
/*     */ 
/*     */     
/* 368 */     for (Environment.Variable variable : this.sysProperties.getVariablesVector()) {
/* 369 */       declareSysProperty(cmd, variable);
/*     */     }
/*     */     
/* 372 */     for (Commandline.Argument arg : this.additionalArgs) {
/* 373 */       addArgument(cmd, arg);
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
/*     */   protected void declareSysProperty(ExecTask cmd, Environment.Variable property) throws BuildException {
/* 385 */     addValue(cmd, "-J-D" + property.getContent());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void bindToKeystore(ExecTask cmd) {
/* 393 */     if (null != this.keystore) {
/*     */       String loc;
/* 395 */       addValue(cmd, "-keystore");
/*     */       
/* 397 */       File keystoreFile = getProject().resolveFile(this.keystore);
/* 398 */       if (keystoreFile.exists()) {
/* 399 */         loc = keystoreFile.getPath();
/*     */       } else {
/*     */         
/* 402 */         loc = this.keystore;
/*     */       } 
/* 404 */       addValue(cmd, loc);
/*     */     } 
/* 406 */     if (null != this.storetype) {
/* 407 */       addValue(cmd, "-storetype");
/* 408 */       addValue(cmd, this.storetype);
/*     */     } 
/* 410 */     if (null != this.providerName) {
/* 411 */       addValue(cmd, "-providerName");
/* 412 */       addValue(cmd, this.providerName);
/*     */     } 
/* 414 */     if (null != this.providerClass) {
/* 415 */       addValue(cmd, "-providerClass");
/* 416 */       addValue(cmd, this.providerClass);
/* 417 */       if (null != this.providerArg) {
/* 418 */         addValue(cmd, "-providerArg");
/* 419 */         addValue(cmd, this.providerArg);
/*     */       } 
/* 421 */     } else if (null != this.providerArg) {
/* 422 */       log("Ignoring providerArg as providerClass has not been set");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecTask createJarSigner() {
/* 432 */     ExecTask cmd = new ExecTask(this);
/* 433 */     if (this.executable == null) {
/* 434 */       cmd.setExecutable(JavaEnvUtils.getJdkExecutable("jarsigner"));
/*     */     } else {
/* 436 */       cmd.setExecutable(this.executable);
/*     */     } 
/* 438 */     cmd.setTaskType("jarsigner");
/* 439 */     cmd.setFailonerror(true);
/* 440 */     cmd.addConfiguredRedirector(this.redirector);
/* 441 */     return cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vector<FileSet> createUnifiedSources() {
/* 450 */     Vector<FileSet> sources = new Vector<>(this.filesets);
/* 451 */     if (this.jar != null) {
/*     */ 
/*     */ 
/*     */       
/* 455 */       FileSet sourceJar = new FileSet();
/* 456 */       sourceJar.setProject(getProject());
/* 457 */       sourceJar.setFile(this.jar);
/* 458 */       sources.add(sourceJar);
/*     */     } 
/* 460 */     return sources;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path createUnifiedSourcePath() {
/* 471 */     Path p = (this.path == null) ? new Path(getProject()) : (Path)this.path.clone();
/* 472 */     for (FileSet fileSet : createUnifiedSources()) {
/* 473 */       p.add((ResourceCollection)fileSet);
/*     */     }
/* 475 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasResources() {
/* 484 */     return (this.path != null || !this.filesets.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addValue(ExecTask cmd, String value) {
/* 493 */     cmd.createArg().setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addArgument(ExecTask cmd, Commandline.Argument arg) {
/* 502 */     cmd.createArg().copyFrom(arg);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/AbstractJarSignerTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */