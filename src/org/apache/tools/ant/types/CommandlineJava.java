/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ public class CommandlineJava
/*     */   implements Cloneable
/*     */ {
/*  43 */   private Commandline vmCommand = new Commandline();
/*     */ 
/*     */ 
/*     */   
/*  47 */   private Commandline javaCommand = new Commandline();
/*     */ 
/*     */ 
/*     */   
/*  51 */   private SysProperties sysProperties = new SysProperties();
/*  52 */   private Path classpath = null;
/*  53 */   private Path bootclasspath = null;
/*  54 */   private Path modulepath = null;
/*  55 */   private Path upgrademodulepath = null;
/*     */   private String vmVersion;
/*  57 */   private String maxMemory = null;
/*     */ 
/*     */ 
/*     */   
/*  61 */   private Assertions assertions = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ExecutableType executableType;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cloneVm = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SysProperties
/*     */     extends Environment
/*     */     implements Cloneable
/*     */   {
/*  82 */     Properties sys = null;
/*     */     
/*  84 */     private Vector<PropertySet> propertySets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getVariables() throws BuildException {
/*  95 */       List<String> definitions = new LinkedList<>();
/*  96 */       addDefinitionsToList(definitions.listIterator());
/*  97 */       if (definitions.isEmpty()) {
/*  98 */         return null;
/*     */       }
/* 100 */       return definitions.<String>toArray(new String[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addDefinitionsToList(ListIterator<String> listIt) {
/* 108 */       String[] props = super.getVariables();
/* 109 */       if (props != null) {
/* 110 */         for (String prop : props) {
/* 111 */           listIt.add("-D" + prop);
/*     */         }
/*     */       }
/* 114 */       Properties propertySetProperties = mergePropertySets();
/* 115 */       for (String key : propertySetProperties.stringPropertyNames()) {
/* 116 */         listIt.add("-D" + key + "=" + propertySetProperties.getProperty(key));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int size() {
/* 126 */       Properties p = mergePropertySets();
/* 127 */       return this.variables.size() + p.size();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setSystem() throws BuildException {
/*     */       try {
/* 137 */         this.sys = System.getProperties();
/* 138 */         Properties p = new Properties();
/* 139 */         for (String name : this.sys.stringPropertyNames()) {
/* 140 */           String value = this.sys.getProperty(name);
/* 141 */           if (value != null) {
/* 142 */             p.put(name, value);
/*     */           }
/*     */         } 
/* 145 */         p.putAll(mergePropertySets());
/* 146 */         for (Environment.Variable v : this.variables) {
/* 147 */           v.validate();
/* 148 */           p.put(v.getKey(), v.getValue());
/*     */         } 
/* 150 */         System.setProperties(p);
/* 151 */       } catch (SecurityException e) {
/* 152 */         throw new BuildException("Cannot modify system properties", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void restoreSystem() throws BuildException {
/* 162 */       if (this.sys == null) {
/* 163 */         throw new BuildException("Unbalanced nesting of SysProperties");
/*     */       }
/*     */       
/*     */       try {
/* 167 */         System.setProperties(this.sys);
/* 168 */         this.sys = null;
/* 169 */       } catch (SecurityException e) {
/* 170 */         throw new BuildException("Cannot modify system properties", e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object clone() throws CloneNotSupportedException {
/*     */       try {
/* 183 */         SysProperties c = (SysProperties)super.clone();
/* 184 */         c.variables = (Vector<Environment.Variable>)this.variables.clone();
/* 185 */         c.propertySets = (Vector<PropertySet>)this.propertySets.clone();
/* 186 */         return c;
/* 187 */       } catch (CloneNotSupportedException e) {
/* 188 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addSyspropertyset(PropertySet ps) {
/* 197 */       this.propertySets.addElement(ps);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addSysproperties(SysProperties ps) {
/* 206 */       this.variables.addAll(ps.variables);
/* 207 */       this.propertySets.addAll(ps.propertySets);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Properties mergePropertySets() {
/* 215 */       Properties p = new Properties();
/* 216 */       for (PropertySet ps : this.propertySets) {
/* 217 */         p.putAll(ps.getProperties());
/*     */       }
/* 219 */       return p;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandlineJava() {
/* 228 */     setVm(JavaEnvUtils.getJreExecutable("java"));
/* 229 */     setVmversion(JavaEnvUtils.getJavaVersion());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline.Argument createArgument() {
/* 237 */     return this.javaCommand.createArgument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline.Argument createVmArgument() {
/* 245 */     return this.vmCommand.createArgument();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSysproperty(Environment.Variable sysp) {
/* 253 */     this.sysProperties.addVariable(sysp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSyspropertyset(PropertySet sysp) {
/* 261 */     this.sysProperties.addSyspropertyset(sysp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSysproperties(SysProperties sysp) {
/* 270 */     this.sysProperties.addSysproperties(sysp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVm(String vm) {
/* 278 */     this.vmCommand.setExecutable(vm);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVmversion(String value) {
/* 286 */     this.vmVersion = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCloneVm(boolean cloneVm) {
/* 297 */     this.cloneVm = cloneVm;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Assertions getAssertions() {
/* 305 */     return this.assertions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAssertions(Assertions assertions) {
/* 313 */     this.assertions = assertions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJar(String jarpathname) {
/* 321 */     this.javaCommand.setExecutable(jarpathname);
/* 322 */     this.executableType = ExecutableType.JAR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJar() {
/* 332 */     if (this.executableType == ExecutableType.JAR) {
/* 333 */       return this.javaCommand.getExecutable();
/*     */     }
/* 335 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassname(String classname) {
/* 343 */     if (this.executableType == ExecutableType.MODULE) {
/* 344 */       this.javaCommand.setExecutable(createModuleClassPair(
/* 345 */             parseModuleFromModuleClassPair(this.javaCommand.getExecutable()), classname), false);
/*     */     } else {
/*     */       
/* 348 */       this.javaCommand.setExecutable(classname);
/* 349 */       this.executableType = ExecutableType.CLASS;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassname() {
/* 359 */     if (this.executableType != null) {
/* 360 */       switch (this.executableType) {
/*     */         case CLASS:
/* 362 */           return this.javaCommand.getExecutable();
/*     */         case MODULE:
/* 364 */           return parseClassFromModuleClassPair(this.javaCommand.getExecutable());
/*     */       } 
/*     */     
/*     */     }
/* 368 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceFile(String sourceFile) {
/* 379 */     this.executableType = ExecutableType.SOURCE_FILE;
/* 380 */     this.javaCommand.setExecutable(sourceFile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSourceFile() {
/* 390 */     return (this.executableType == ExecutableType.SOURCE_FILE) ? this.javaCommand.getExecutable() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModule(String module) {
/* 399 */     if (this.executableType == null) {
/* 400 */       this.javaCommand.setExecutable(module);
/*     */     } else {
/* 402 */       switch (this.executableType) {
/*     */         case JAR:
/* 404 */           this.javaCommand.setExecutable(module, false);
/*     */           break;
/*     */         case CLASS:
/* 407 */           this.javaCommand.setExecutable(createModuleClassPair(module, this.javaCommand
/* 408 */                 .getExecutable()), false);
/*     */           break;
/*     */         case MODULE:
/* 411 */           this.javaCommand.setExecutable(createModuleClassPair(module, 
/* 412 */                 parseClassFromModuleClassPair(this.javaCommand.getExecutable())), false);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 417 */     this.executableType = ExecutableType.MODULE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getModule() {
/* 428 */     if (this.executableType == ExecutableType.MODULE) {
/* 429 */       return parseModuleFromModuleClassPair(this.javaCommand.getExecutable());
/*     */     }
/* 431 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath(Project p) {
/* 440 */     if (this.classpath == null) {
/* 441 */       this.classpath = new Path(p);
/*     */     }
/* 443 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createBootclasspath(Project p) {
/* 453 */     if (this.bootclasspath == null) {
/* 454 */       this.bootclasspath = new Path(p);
/*     */     }
/* 456 */     return this.bootclasspath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createModulepath(Project p) {
/* 466 */     if (this.modulepath == null) {
/* 467 */       this.modulepath = new Path(p);
/*     */     }
/* 469 */     return this.modulepath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createUpgrademodulepath(Project p) {
/* 479 */     if (this.upgrademodulepath == null) {
/* 480 */       this.upgrademodulepath = new Path(p);
/*     */     }
/* 482 */     return this.upgrademodulepath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVmversion() {
/* 490 */     return this.vmVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCommandline() {
/* 499 */     List<String> commands = new LinkedList<>();
/*     */     
/* 501 */     addCommandsToList(commands.listIterator());
/*     */     
/* 503 */     return commands.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addCommandsToList(ListIterator<String> listIterator) {
/* 513 */     getActualVMCommand().addCommandToList(listIterator);
/*     */     
/* 515 */     this.sysProperties.addDefinitionsToList(listIterator);
/*     */     
/* 517 */     if (isCloneVm()) {
/* 518 */       SysProperties clonedSysProperties = new SysProperties();
/* 519 */       PropertySet ps = new PropertySet();
/* 520 */       PropertySet.BuiltinPropertySetName sys = new PropertySet.BuiltinPropertySetName();
/* 521 */       sys.setValue("system");
/* 522 */       ps.appendBuiltin(sys);
/* 523 */       clonedSysProperties.addSyspropertyset(ps);
/* 524 */       clonedSysProperties.addDefinitionsToList(listIterator);
/*     */     } 
/*     */     
/* 527 */     Path bcp = calculateBootclasspath(true);
/* 528 */     if (bcp.size() > 0) {
/* 529 */       listIterator.add("-Xbootclasspath:" + bcp.toString());
/*     */     }
/*     */     
/* 532 */     if (haveClasspath()) {
/* 533 */       listIterator.add("-classpath");
/* 534 */       listIterator.add(this.classpath.concatSystemClasspath("ignore").toString());
/*     */     } 
/*     */     
/* 537 */     if (haveModulepath()) {
/* 538 */       listIterator.add("--module-path");
/* 539 */       listIterator.add(this.modulepath.concatSystemClasspath("ignore").toString());
/*     */     } 
/*     */     
/* 542 */     if (haveUpgrademodulepath()) {
/* 543 */       listIterator.add("--upgrade-module-path");
/* 544 */       listIterator.add(this.upgrademodulepath.concatSystemClasspath("ignore").toString());
/*     */     } 
/*     */     
/* 547 */     if (getAssertions() != null) {
/* 548 */       getAssertions().applyAssertions(listIterator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 554 */     if (this.executableType == ExecutableType.JAR) {
/* 555 */       listIterator.add("-jar");
/* 556 */     } else if (this.executableType == ExecutableType.MODULE) {
/* 557 */       listIterator.add("-m");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 564 */     this.javaCommand.addCommandToList(listIterator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxmemory(String max) {
/* 573 */     this.maxMemory = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 582 */     return Commandline.toString(getCommandline());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String describeCommand() {
/* 592 */     return Commandline.describeCommand(getCommandline());
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
/*     */   public String describeJavaCommand() {
/* 604 */     return Commandline.describeCommand(getJavaCommand());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline getActualVMCommand() {
/* 612 */     Commandline actualVMCommand = (Commandline)this.vmCommand.clone();
/* 613 */     if (this.maxMemory != null) {
/* 614 */       if (this.vmVersion.startsWith("1.1")) {
/* 615 */         actualVMCommand.createArgument().setValue("-mx" + this.maxMemory);
/*     */       } else {
/* 617 */         actualVMCommand.createArgument().setValue("-Xmx" + this.maxMemory);
/*     */       } 
/*     */     }
/* 620 */     return actualVMCommand;
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
/*     */   @Deprecated
/*     */   public int size() {
/* 635 */     int size = getActualVMCommand().size() + this.javaCommand.size() + this.sysProperties.size();
/*     */     
/* 637 */     if (isCloneVm()) {
/* 638 */       size += System.getProperties().size();
/*     */     }
/*     */     
/* 641 */     if (haveClasspath()) {
/* 642 */       size += 2;
/*     */     }
/*     */     
/* 645 */     if (calculateBootclasspath(true).size() > 0) {
/* 646 */       size++;
/*     */     }
/*     */     
/* 649 */     if (this.executableType == ExecutableType.JAR || this.executableType == ExecutableType.MODULE) {
/* 650 */       size++;
/*     */     }
/*     */     
/* 653 */     if (getAssertions() != null) {
/* 654 */       size += getAssertions().size();
/*     */     }
/* 656 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline getJavaCommand() {
/* 664 */     return this.javaCommand;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Commandline getVmCommand() {
/* 672 */     return getActualVMCommand();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 680 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getBootclasspath() {
/* 688 */     return this.bootclasspath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getModulepath() {
/* 697 */     return this.modulepath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getUpgrademodulepath() {
/* 706 */     return this.upgrademodulepath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSystemProperties() throws BuildException {
/* 715 */     this.sysProperties.setSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void restoreSystemProperties() throws BuildException {
/* 724 */     this.sysProperties.restoreSystem();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SysProperties getSystemProperties() {
/* 732 */     return this.sysProperties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/*     */     try {
/* 744 */       CommandlineJava c = (CommandlineJava)super.clone();
/* 745 */       c.vmCommand = (Commandline)this.vmCommand.clone();
/* 746 */       c.javaCommand = (Commandline)this.javaCommand.clone();
/* 747 */       c.sysProperties = (SysProperties)this.sysProperties.clone();
/* 748 */       if (this.classpath != null) {
/* 749 */         c.classpath = (Path)this.classpath.clone();
/*     */       }
/* 751 */       if (this.bootclasspath != null) {
/* 752 */         c.bootclasspath = (Path)this.bootclasspath.clone();
/*     */       }
/* 754 */       if (this.modulepath != null) {
/* 755 */         c.modulepath = (Path)this.modulepath.clone();
/*     */       }
/* 757 */       if (this.upgrademodulepath != null) {
/* 758 */         c.upgrademodulepath = (Path)this.upgrademodulepath.clone();
/*     */       }
/* 760 */       if (this.assertions != null) {
/* 761 */         c.assertions = (Assertions)this.assertions.clone();
/*     */       }
/* 763 */       return c;
/* 764 */     } catch (CloneNotSupportedException e) {
/* 765 */       throw new BuildException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearJavaArgs() {
/* 773 */     this.javaCommand.clearArgs();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean haveClasspath() {
/* 783 */     Path fullClasspath = (this.classpath == null) ? null : this.classpath.concatSystemClasspath("ignore");
/* 784 */     return (fullClasspath != null && !fullClasspath.toString().trim().isEmpty());
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
/*     */   protected boolean haveBootclasspath(boolean log) {
/* 798 */     return (calculateBootclasspath(log).size() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean haveModulepath() {
/* 808 */     Path fullClasspath = (this.modulepath != null) ? this.modulepath.concatSystemClasspath("ignore") : null;
/* 809 */     return (fullClasspath != null && 
/* 810 */       !fullClasspath.toString().trim().isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean haveUpgrademodulepath() {
/* 820 */     Path fullClasspath = (this.upgrademodulepath != null) ? this.upgrademodulepath.concatSystemClasspath("ignore") : null;
/* 821 */     return (fullClasspath != null && !fullClasspath.toString().trim().isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path calculateBootclasspath(boolean log) {
/* 832 */     if (this.vmVersion.startsWith("1.1")) {
/* 833 */       if (this.bootclasspath != null && log) {
/* 834 */         this.bootclasspath.log("Ignoring bootclasspath as the target VM doesn't support it.");
/*     */       }
/*     */     } else {
/* 837 */       Path b = this.bootclasspath;
/* 838 */       if (b == null) {
/* 839 */         b = new Path(null);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 844 */       return b.concatSystemBootClasspath(isCloneVm() ? "last" : "ignore");
/*     */     } 
/* 846 */     return new Path(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCloneVm() {
/* 856 */     return (this.cloneVm || Boolean.parseBoolean(System.getProperty("ant.build.clonevm")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String createModuleClassPair(String module, String classname) {
/* 867 */     return (classname == null) ? module : String.format("%s/%s", new Object[] { module, classname });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String parseModuleFromModuleClassPair(String moduleClassPair) {
/* 877 */     if (moduleClassPair == null) {
/* 878 */       return null;
/*     */     }
/* 880 */     String[] moduleAndClass = moduleClassPair.split("/");
/* 881 */     return moduleAndClass[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String parseClassFromModuleClassPair(String moduleClassPair) {
/* 891 */     if (moduleClassPair == null) {
/* 892 */       return null;
/*     */     }
/* 894 */     String[] moduleAndClass = moduleClassPair.split("/");
/* 895 */     return (moduleAndClass.length == 2) ? moduleAndClass[1] : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum ExecutableType
/*     */   {
/* 906 */     CLASS,
/*     */ 
/*     */ 
/*     */     
/* 910 */     JAR,
/*     */ 
/*     */ 
/*     */     
/* 914 */     MODULE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 919 */     SOURCE_FILE;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/CommandlineJava.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */