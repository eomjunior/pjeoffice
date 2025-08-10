/*     */ package org.apache.tools.ant.taskdefs.compilers;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Location;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.Javac;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DefaultCompilerAdapter
/*     */   implements CompilerAdapter, CompilerAdapterExtension
/*     */ {
/*     */   private static final int COMMAND_LINE_LIMIT;
/*     */   
/*     */   static {
/*  54 */     if (Os.isFamily("os/2")) {
/*     */       
/*  56 */       COMMAND_LINE_LIMIT = 1000;
/*     */     } else {
/*  58 */       COMMAND_LINE_LIMIT = 4096;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*  63 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  68 */   protected static final String lSep = StringUtils.LINE_SEP;
/*     */ 
/*     */   
/*  71 */   private static final Pattern JAVAC_ARG_FILE_CHARS_TO_QUOTE = Pattern.compile("[ #]");
/*     */   
/*     */   protected Path src;
/*     */   
/*     */   protected File destDir;
/*     */   
/*     */   protected String encoding;
/*     */   
/*     */   protected boolean debug = false;
/*     */   
/*     */   protected boolean optimize = false;
/*     */   
/*     */   protected boolean deprecation = false;
/*     */   
/*     */   protected boolean depend = false;
/*     */   
/*     */   protected boolean verbose = false;
/*     */   
/*     */   protected String target;
/*     */   
/*     */   protected String release;
/*     */   
/*     */   protected Path bootclasspath;
/*     */   protected Path extdirs;
/*     */   protected Path compileClasspath;
/*     */   protected Path modulepath;
/*     */   protected Path upgrademodulepath;
/*     */   protected Path compileSourcepath;
/*     */   protected Path moduleSourcepath;
/*     */   protected Project project;
/*     */   protected Location location;
/*     */   protected boolean includeAntRuntime;
/*     */   protected boolean includeJavaRuntime;
/*     */   protected String memoryInitialSize;
/*     */   protected String memoryMaximumSize;
/*     */   protected File[] compileList;
/*     */   protected Javac attributes;
/*     */   
/*     */   public void setJavac(Javac attributes) {
/* 110 */     this.attributes = attributes;
/* 111 */     this.src = attributes.getSrcdir();
/* 112 */     this.destDir = attributes.getDestdir();
/* 113 */     this.encoding = attributes.getEncoding();
/* 114 */     this.debug = attributes.getDebug();
/* 115 */     this.optimize = attributes.getOptimize();
/* 116 */     this.deprecation = attributes.getDeprecation();
/* 117 */     this.depend = attributes.getDepend();
/* 118 */     this.verbose = attributes.getVerbose();
/* 119 */     this.target = attributes.getTarget();
/* 120 */     this.release = attributes.getRelease();
/* 121 */     this.bootclasspath = attributes.getBootclasspath();
/* 122 */     this.extdirs = attributes.getExtdirs();
/* 123 */     this.compileList = attributes.getFileList();
/* 124 */     this.compileClasspath = attributes.getClasspath();
/* 125 */     this.modulepath = attributes.getModulepath();
/* 126 */     this.upgrademodulepath = attributes.getUpgrademodulepath();
/* 127 */     this.compileSourcepath = attributes.getSourcepath();
/* 128 */     this.moduleSourcepath = attributes.getModulesourcepath();
/* 129 */     this.project = attributes.getProject();
/* 130 */     this.location = attributes.getLocation();
/* 131 */     this.includeAntRuntime = attributes.getIncludeantruntime();
/* 132 */     this.includeJavaRuntime = attributes.getIncludejavaruntime();
/* 133 */     this.memoryInitialSize = attributes.getMemoryInitialSize();
/* 134 */     this.memoryMaximumSize = attributes.getMemoryMaximumSize();
/* 135 */     if (this.moduleSourcepath != null && this.src == null && this.compileSourcepath == null)
/*     */     {
/* 137 */       this.compileSourcepath = new Path(getProject());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Javac getJavac() {
/* 147 */     return this.attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getSupportedFileExtensions() {
/* 157 */     return new String[] { "java" };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Project getProject() {
/* 166 */     return this.project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path getCompileClasspath() {
/* 174 */     Path classpath = new Path(this.project);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 179 */     if (this.destDir != null && getJavac().isIncludeDestClasses()) {
/* 180 */       classpath.setLocation(this.destDir);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     Path cp = this.compileClasspath;
/* 187 */     if (cp == null) {
/* 188 */       cp = new Path(this.project);
/*     */     }
/* 190 */     if (this.includeAntRuntime) {
/* 191 */       classpath.addExisting(cp.concatSystemClasspath("last"));
/*     */     } else {
/* 193 */       classpath.addExisting(cp.concatSystemClasspath("ignore"));
/*     */     } 
/*     */     
/* 196 */     if (this.includeJavaRuntime) {
/* 197 */       classpath.addJavaRuntime();
/*     */     }
/*     */     
/* 200 */     return classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path getModulepath() {
/* 209 */     Path mp = new Path(getProject());
/* 210 */     if (this.modulepath != null) {
/* 211 */       mp.addExisting(this.modulepath);
/*     */     }
/* 213 */     return mp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path getUpgrademodulepath() {
/* 222 */     Path ump = new Path(getProject());
/* 223 */     if (this.upgrademodulepath != null) {
/* 224 */       ump.addExisting(this.upgrademodulepath);
/*     */     }
/* 226 */     return ump;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path getModulesourcepath() {
/* 235 */     Path msp = new Path(getProject());
/* 236 */     if (this.moduleSourcepath != null) {
/* 237 */       msp.add(this.moduleSourcepath);
/*     */     }
/* 239 */     return msp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupJavacCommandlineSwitches(Commandline cmd) {
/* 248 */     return setupJavacCommandlineSwitches(cmd, false);
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
/*     */   protected Commandline setupJavacCommandlineSwitches(Commandline cmd, boolean useDebugLevel) {
/* 260 */     Path sourcepath, classpath = getCompileClasspath();
/*     */ 
/*     */ 
/*     */     
/* 264 */     if (this.compileSourcepath != null) {
/* 265 */       sourcepath = this.compileSourcepath;
/*     */     } else {
/* 267 */       sourcepath = this.src;
/*     */     } 
/*     */     
/* 270 */     String memoryParameterPrefix = assumeJava1_2Plus() ? "-J-X" : "-J-";
/*     */     
/* 272 */     if (this.memoryInitialSize != null) {
/* 273 */       if (!this.attributes.isForkedJavac()) {
/* 274 */         this.attributes.log("Since fork is false, ignoring memoryInitialSize setting.", 1);
/*     */       }
/*     */       else {
/*     */         
/* 278 */         cmd.createArgument().setValue(memoryParameterPrefix + "ms" + this.memoryInitialSize);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 283 */     if (this.memoryMaximumSize != null) {
/* 284 */       if (!this.attributes.isForkedJavac()) {
/* 285 */         this.attributes.log("Since fork is false, ignoring memoryMaximumSize setting.", 1);
/*     */       }
/*     */       else {
/*     */         
/* 289 */         cmd.createArgument().setValue(memoryParameterPrefix + "mx" + this.memoryMaximumSize);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 294 */     if (this.attributes.getNowarn()) {
/* 295 */       cmd.createArgument().setValue("-nowarn");
/*     */     }
/*     */     
/* 298 */     if (this.deprecation) {
/* 299 */       cmd.createArgument().setValue("-deprecation");
/*     */     }
/*     */     
/* 302 */     if (this.destDir != null) {
/* 303 */       cmd.createArgument().setValue("-d");
/* 304 */       cmd.createArgument().setFile(this.destDir);
/*     */     } 
/*     */     
/* 307 */     cmd.createArgument().setValue("-classpath");
/*     */ 
/*     */ 
/*     */     
/* 311 */     if (!assumeJava1_2Plus()) {
/* 312 */       Path cp = new Path(this.project);
/* 313 */       Objects.requireNonNull(cp); Optional.<Path>ofNullable(getBootClassPath()).ifPresent(cp::append);
/*     */       
/* 315 */       if (this.extdirs != null) {
/* 316 */         cp.addExtdirs(this.extdirs);
/*     */       }
/* 318 */       cp.append(classpath);
/* 319 */       cp.append(sourcepath);
/* 320 */       cmd.createArgument().setPath(cp);
/*     */     } else {
/* 322 */       cmd.createArgument().setPath(classpath);
/*     */ 
/*     */       
/* 325 */       if (sourcepath.size() > 0) {
/* 326 */         cmd.createArgument().setValue("-sourcepath");
/* 327 */         cmd.createArgument().setPath(sourcepath);
/*     */       } 
/* 329 */       if (this.release == null || !assumeJava9Plus()) {
/* 330 */         if (this.target != null) {
/* 331 */           cmd.createArgument().setValue("-target");
/* 332 */           cmd.createArgument().setValue(this.target);
/*     */         } 
/*     */         
/* 335 */         Path bp = getBootClassPath();
/* 336 */         if (!bp.isEmpty()) {
/* 337 */           cmd.createArgument().setValue("-bootclasspath");
/* 338 */           cmd.createArgument().setPath(bp);
/*     */         } 
/*     */       } 
/*     */       
/* 342 */       if (this.extdirs != null && !this.extdirs.isEmpty()) {
/* 343 */         cmd.createArgument().setValue("-extdirs");
/* 344 */         cmd.createArgument().setPath(this.extdirs);
/*     */       } 
/*     */     } 
/*     */     
/* 348 */     if (this.encoding != null) {
/* 349 */       cmd.createArgument().setValue("-encoding");
/* 350 */       cmd.createArgument().setValue(this.encoding);
/*     */     } 
/* 352 */     if (this.debug) {
/* 353 */       if (useDebugLevel && assumeJava1_2Plus()) {
/* 354 */         String debugLevel = this.attributes.getDebugLevel();
/* 355 */         if (debugLevel != null) {
/* 356 */           cmd.createArgument().setValue("-g:" + debugLevel);
/*     */         } else {
/* 358 */           cmd.createArgument().setValue("-g");
/*     */         } 
/*     */       } else {
/* 361 */         cmd.createArgument().setValue("-g");
/*     */       } 
/* 363 */     } else if (getNoDebugArgument() != null) {
/* 364 */       cmd.createArgument().setValue(getNoDebugArgument());
/*     */     } 
/* 366 */     if (this.optimize) {
/* 367 */       cmd.createArgument().setValue("-O");
/*     */     }
/*     */     
/* 370 */     if (this.depend) {
/* 371 */       if (assumeJava1_3Plus()) {
/* 372 */         this.attributes.log("depend attribute is not supported by the modern compiler", 1);
/*     */       
/*     */       }
/* 375 */       else if (assumeJava1_2Plus()) {
/* 376 */         cmd.createArgument().setValue("-Xdepend");
/*     */       } else {
/* 378 */         cmd.createArgument().setValue("-depend");
/*     */       } 
/*     */     }
/*     */     
/* 382 */     if (this.verbose) {
/* 383 */       cmd.createArgument().setValue("-verbose");
/*     */     }
/*     */     
/* 386 */     addCurrentCompilerArgs(cmd);
/*     */     
/* 388 */     return cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupModernJavacCommandlineSwitches(Commandline cmd) {
/* 398 */     setupJavacCommandlineSwitches(cmd, true);
/* 399 */     if (assumeJava1_4Plus()) {
/* 400 */       String t = this.attributes.getTarget();
/* 401 */       String s = this.attributes.getSource();
/* 402 */       if (this.release == null || !assumeJava9Plus()) {
/* 403 */         if (this.release != null) {
/* 404 */           this.attributes.log("Support for javac --release has been added in Java9 ignoring it");
/*     */         }
/*     */         
/* 407 */         if (s != null) {
/* 408 */           cmd.createArgument().setValue("-source");
/* 409 */           cmd.createArgument().setValue(adjustSourceValue(s));
/*     */         }
/* 411 */         else if (t != null && mustSetSourceForTarget(t)) {
/* 412 */           setImplicitSourceSwitch(cmd, t, adjustSourceValue(t));
/*     */         } 
/*     */       } else {
/* 415 */         if (t != null || s != null || getBootClassPath().size() > 0) {
/* 416 */           this.attributes.log("Ignoring source, target and bootclasspath as release has been set", 1);
/*     */         }
/*     */ 
/*     */         
/* 420 */         cmd.createArgument().setValue("--release");
/* 421 */         cmd.createArgument().setValue(this.release);
/*     */       } 
/*     */     } 
/* 424 */     Path msp = getModulesourcepath();
/* 425 */     if (!msp.isEmpty()) {
/* 426 */       cmd.createArgument().setValue("--module-source-path");
/* 427 */       cmd.createArgument().setPath(msp);
/*     */     } 
/* 429 */     Path mp = getModulepath();
/* 430 */     if (!mp.isEmpty()) {
/* 431 */       cmd.createArgument().setValue("--module-path");
/* 432 */       cmd.createArgument().setPath(mp);
/*     */     } 
/* 434 */     Path ump = getUpgrademodulepath();
/* 435 */     if (!ump.isEmpty()) {
/* 436 */       cmd.createArgument().setValue("--upgrade-module-path");
/* 437 */       cmd.createArgument().setPath(ump);
/*     */     } 
/* 439 */     if (this.attributes.getNativeHeaderDir() != null) {
/* 440 */       if (!assumeJava1_8Plus()) {
/* 441 */         this.attributes.log("Support for javac -h has been added in Java8, ignoring it");
/*     */       } else {
/*     */         
/* 444 */         cmd.createArgument().setValue("-h");
/* 445 */         cmd.createArgument().setFile(this.attributes.getNativeHeaderDir());
/*     */       } 
/*     */     }
/* 448 */     return cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupModernJavacCommand() {
/* 457 */     Commandline cmd = new Commandline();
/* 458 */     setupModernJavacCommandlineSwitches(cmd);
/*     */     
/* 460 */     logAndAddFilesToCompile(cmd);
/* 461 */     return cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupJavacCommand() {
/* 469 */     return setupJavacCommand(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupJavacCommand(boolean debugLevelCheck) {
/* 479 */     Commandline cmd = new Commandline();
/* 480 */     setupJavacCommandlineSwitches(cmd, debugLevelCheck);
/* 481 */     logAndAddFilesToCompile(cmd);
/* 482 */     return cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logAndAddFilesToCompile(Commandline cmd) {
/* 491 */     this.attributes.log("Compilation " + cmd.describeArguments(), 3);
/*     */     
/* 493 */     this.attributes.log(String.format("%s to be compiled:", new Object[] {
/* 494 */             (this.compileList.length == 1) ? "File" : "Files"
/*     */           }), 3);
/* 496 */     this.attributes.log(Stream.<File>of(this.compileList).map(File::getAbsolutePath)
/* 497 */         .peek(arg -> cmd.createArgument().setValue(arg))
/* 498 */         .map(arg -> String.format("    %s%n", new Object[] { arg
/* 499 */             })).collect(Collectors.joining("")), 3);
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
/*     */   protected int executeExternalCompile(String[] args, int firstFileName) {
/* 512 */     return executeExternalCompile(args, firstFileName, true);
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
/*     */   
/*     */   protected int executeExternalCompile(String[] args, int firstFileName, boolean quoteFiles) {
/* 536 */     String[] commandArray = null;
/* 537 */     File tmpFile = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 547 */       if (Commandline.toString(args).length() > COMMAND_LINE_LIMIT && firstFileName >= 0) {
/*     */         
/*     */         try {
/* 550 */           tmpFile = FILE_UTILS.createTempFile(
/* 551 */               getProject(), "files", "", getJavac().getTempdir(), true, true);
/* 552 */           BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
/*     */           
/* 554 */           try { for (int i = firstFileName; i < args.length; i++) {
/* 555 */               if (quoteFiles && JAVAC_ARG_FILE_CHARS_TO_QUOTE.matcher(args[i]).find()) {
/* 556 */                 args[i] = args[i]
/* 557 */                   .replace(File.separatorChar, '/');
/* 558 */                 out.write("\"" + args[i] + "\"");
/*     */               } else {
/* 560 */                 out.write(args[i]);
/*     */               } 
/* 562 */               out.newLine();
/*     */             } 
/* 564 */             out.flush();
/* 565 */             commandArray = new String[firstFileName + 1];
/* 566 */             System.arraycopy(args, 0, commandArray, 0, firstFileName);
/*     */             
/* 568 */             commandArray[firstFileName] = "@" + tmpFile;
/* 569 */             out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; } 
/* 570 */         } catch (IOException e) {
/* 571 */           throw new BuildException("Error creating temporary file", e, this.location);
/*     */         } 
/*     */       } else {
/*     */         
/* 575 */         commandArray = args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     finally {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 598 */       if (tmpFile != null) {
/* 599 */         tmpFile.delete();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void addExtdirsToClasspath(Path classpath) {
/* 612 */     classpath.addExtdirs(this.extdirs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addCurrentCompilerArgs(Commandline cmd) {
/* 620 */     cmd.addArguments(getJavac().getCurrentCompilerArgs());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava11() {
/* 631 */     return (assumeJava1_1Plus() && !assumeJava1_2Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava1_1Plus() {
/* 640 */     return ("javac1.1"
/* 641 */       .equalsIgnoreCase(this.attributes.getCompilerVersion()) || 
/* 642 */       assumeJava1_2Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava12() {
/* 653 */     return (assumeJava1_2Plus() && !assumeJava1_3Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava1_2Plus() {
/* 662 */     return ("javac1.2"
/* 663 */       .equalsIgnoreCase(this.attributes.getCompilerVersion()) || 
/* 664 */       assumeJava1_3Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava13() {
/* 675 */     return (assumeJava1_3Plus() && !assumeJava1_4Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava1_3Plus() {
/* 684 */     return ("javac1.3"
/* 685 */       .equalsIgnoreCase(this.attributes.getCompilerVersion()) || 
/* 686 */       assumeJava1_4Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava14() {
/* 697 */     return (assumeJava1_4Plus() && !assumeJava1_5Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava1_4Plus() {
/* 706 */     return (assumeJavaXY("javac1.4", "1.4") || 
/* 707 */       assumeJava1_5Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava15() {
/* 718 */     return (assumeJava1_5Plus() && !assumeJava1_6Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava1_5Plus() {
/* 727 */     return (assumeJavaXY("javac1.5", "1.5") || 
/* 728 */       assumeJava1_6Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava16() {
/* 739 */     return (assumeJava1_6Plus() && !assumeJava1_7Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava1_6Plus() {
/* 748 */     return (assumeJavaXY("javac1.6", "1.6") || 
/* 749 */       assumeJava1_7Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava17() {
/* 760 */     return (assumeJava1_7Plus() && !assumeJava1_8Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava1_7Plus() {
/* 769 */     return (assumeJavaXY("javac1.7", "1.7") || 
/* 770 */       assumeJava1_8Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava18() {
/* 781 */     return (assumeJava1_8Plus() && !assumeJava9Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava1_8Plus() {
/* 790 */     return (assumeJavaXY("javac1.8", "1.8") || 
/* 791 */       assumeJava9Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava19() {
/* 802 */     return assumeJava9();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected boolean assumeJava9() {
/* 813 */     return (assumeJava9Plus() && !assumeJava10Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava9Plus() {
/* 822 */     return (assumeJavaXY("javac9", "9") || 
/* 823 */       assumeJavaXY("javac1.9", "9") || 
/* 824 */       assumeJava10Plus());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean assumeJava10Plus() {
/* 833 */     return ("javac10+".equalsIgnoreCase(this.attributes.getCompilerVersion()) || (
/* 834 */       JavaEnvUtils.isAtLeastJavaVersion("10") && 
/* 835 */       CompilerAdapterFactory.isJdkCompilerNickname(this.attributes.getCompilerVersion())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean assumeJavaXY(String javacXY, String javaEnvVersionXY) {
/* 843 */     String compilerVersion = this.attributes.getCompilerVersion();
/* 844 */     return (javacXY.equalsIgnoreCase(compilerVersion) || (
/* 845 */       JavaEnvUtils.isJavaVersion(javaEnvVersionXY) && 
/* 846 */       CompilerAdapterFactory.isJdkCompilerNickname(this.attributes.getCompilerVersion())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path getBootClassPath() {
/* 857 */     Path bp = new Path(this.project);
/* 858 */     if (this.bootclasspath != null) {
/* 859 */       bp.append(this.bootclasspath);
/*     */     }
/* 861 */     return bp.concatSystemBootClasspath("ignore");
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
/*     */   protected String getNoDebugArgument() {
/* 875 */     return assumeJava1_2Plus() ? "-g:none" : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setImplicitSourceSwitch(Commandline cmd, String target, String source) {
/* 880 */     this.attributes.log("", 1);
/* 881 */     this.attributes.log("          WARNING", 1);
/* 882 */     this.attributes.log("", 1);
/* 883 */     this.attributes.log("The -source switch defaults to " + getDefaultSource() + ".", 1);
/*     */ 
/*     */     
/* 886 */     this.attributes.log("If you specify -target " + target + " you now must also specify -source " + source + ".", 1);
/*     */ 
/*     */     
/* 889 */     this.attributes.log("Ant will implicitly add -source " + source + " for you.  Please change your build file.", 1);
/*     */ 
/*     */     
/* 892 */     cmd.createArgument().setValue("-source");
/* 893 */     cmd.createArgument().setValue(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getDefaultSource() {
/* 901 */     if (assumeJava9Plus()) {
/* 902 */       return "9 in JDK 9";
/*     */     }
/* 904 */     if (assumeJava1_8Plus()) {
/* 905 */       return "1.8 in JDK 1.8";
/*     */     }
/* 907 */     if (assumeJava1_7Plus()) {
/* 908 */       return "1.7 in JDK 1.7";
/*     */     }
/* 910 */     if (assumeJava1_5Plus()) {
/* 911 */       return "1.5 in JDK 1.5 and 1.6";
/*     */     }
/* 913 */     return "";
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
/*     */   private boolean mustSetSourceForTarget(String t) {
/* 926 */     if (!assumeJava1_5Plus()) {
/* 927 */       return false;
/*     */     }
/* 929 */     if (t.startsWith("1.")) {
/* 930 */       t = t.substring(2);
/*     */     }
/* 932 */     return ("1".equals(t) || "2".equals(t) || "3".equals(t) || "4".equals(t) || (("5"
/* 933 */       .equals(t) || "6".equals(t)) && assumeJava1_7Plus()) || ("7"
/* 934 */       .equals(t) && assumeJava1_8Plus()) || ("8"
/* 935 */       .equals(t) && assumeJava9Plus()) || ("9"
/* 936 */       .equals(t) && assumeJava10Plus()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String adjustSourceValue(String source) {
/* 947 */     return ("1.1".equals(source) || "1.2".equals(source)) ? "1.3" : source;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/DefaultCompilerAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */