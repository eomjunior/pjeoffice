/*     */ package org.apache.tools.ant.taskdefs.optional.javacc;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.CommandlineJava;
/*     */ import org.apache.tools.ant.types.Path;
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
/*     */ public class JavaCC
/*     */   extends Task
/*     */ {
/*     */   private static final String LOOKAHEAD = "LOOKAHEAD";
/*     */   private static final String CHOICE_AMBIGUITY_CHECK = "CHOICE_AMBIGUITY_CHECK";
/*     */   private static final String OTHER_AMBIGUITY_CHECK = "OTHER_AMBIGUITY_CHECK";
/*     */   private static final String STATIC = "STATIC";
/*     */   private static final String DEBUG_PARSER = "DEBUG_PARSER";
/*     */   private static final String DEBUG_LOOKAHEAD = "DEBUG_LOOKAHEAD";
/*     */   private static final String DEBUG_TOKEN_MANAGER = "DEBUG_TOKEN_MANAGER";
/*     */   private static final String OPTIMIZE_TOKEN_MANAGER = "OPTIMIZE_TOKEN_MANAGER";
/*     */   private static final String ERROR_REPORTING = "ERROR_REPORTING";
/*     */   private static final String JAVA_UNICODE_ESCAPE = "JAVA_UNICODE_ESCAPE";
/*     */   private static final String UNICODE_INPUT = "UNICODE_INPUT";
/*     */   private static final String IGNORE_CASE = "IGNORE_CASE";
/*     */   private static final String COMMON_TOKEN_ACTION = "COMMON_TOKEN_ACTION";
/*     */   private static final String USER_TOKEN_MANAGER = "USER_TOKEN_MANAGER";
/*     */   private static final String USER_CHAR_STREAM = "USER_CHAR_STREAM";
/*     */   private static final String BUILD_PARSER = "BUILD_PARSER";
/*     */   private static final String BUILD_TOKEN_MANAGER = "BUILD_TOKEN_MANAGER";
/*     */   private static final String SANITY_CHECK = "SANITY_CHECK";
/*     */   private static final String FORCE_LA_CHECK = "FORCE_LA_CHECK";
/*     */   private static final String CACHE_TOKENS = "CACHE_TOKENS";
/*     */   private static final String KEEP_LINE_COLUMN = "KEEP_LINE_COLUMN";
/*     */   private static final String JDK_VERSION = "JDK_VERSION";
/*  67 */   private final Map<String, Object> optionalAttrs = new Hashtable<>();
/*     */ 
/*     */   
/*  70 */   private File outputDirectory = null;
/*  71 */   private File targetFile = null;
/*  72 */   private File javaccHome = null;
/*     */   
/*  74 */   private CommandlineJava cmdl = new CommandlineJava();
/*     */   
/*     */   protected static final int TASKDEF_TYPE_JAVACC = 1;
/*     */   
/*     */   protected static final int TASKDEF_TYPE_JJTREE = 2;
/*     */   protected static final int TASKDEF_TYPE_JJDOC = 3;
/*  80 */   protected static final String[] ARCHIVE_LOCATIONS = new String[] { "JavaCC.zip", "bin/lib/JavaCC.zip", "bin/lib/javacc.jar", "javacc.jar" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  88 */   protected static final int[] ARCHIVE_LOCATIONS_VS_MAJOR_VERSION = new int[] { 1, 2, 3, 3 };
/*     */   
/*     */   protected static final String COM_PACKAGE = "COM.sun.labs.";
/*     */   
/*     */   protected static final String COM_JAVACC_CLASS = "javacc.Main";
/*     */   
/*     */   protected static final String COM_JJTREE_CLASS = "jjtree.Main";
/*     */   
/*     */   protected static final String COM_JJDOC_CLASS = "jjdoc.JJDocMain";
/*     */   
/*     */   protected static final String ORG_PACKAGE_3_0 = "org.netbeans.javacc.";
/*     */   
/*     */   protected static final String ORG_PACKAGE_3_1 = "org.javacc.";
/*     */   
/*     */   protected static final String ORG_JAVACC_CLASS = "parser.Main";
/*     */   
/*     */   protected static final String ORG_JJTREE_CLASS = "jjtree.Main";
/*     */   
/*     */   protected static final String ORG_JJDOC_CLASS = "jjdoc.JJDocMain";
/* 107 */   private String maxMemory = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLookahead(int lookahead) {
/* 114 */     this.optionalAttrs.put("LOOKAHEAD", Integer.valueOf(lookahead));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setChoiceambiguitycheck(int choiceAmbiguityCheck) {
/* 122 */     this.optionalAttrs.put("CHOICE_AMBIGUITY_CHECK", Integer.valueOf(choiceAmbiguityCheck));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOtherambiguityCheck(int otherAmbiguityCheck) {
/* 130 */     this.optionalAttrs.put("OTHER_AMBIGUITY_CHECK", Integer.valueOf(otherAmbiguityCheck));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStatic(boolean staticParser) {
/* 138 */     this.optionalAttrs.put("STATIC", staticParser ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebugparser(boolean debugParser) {
/* 146 */     this.optionalAttrs.put("DEBUG_PARSER", debugParser ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebuglookahead(boolean debugLookahead) {
/* 154 */     this.optionalAttrs.put("DEBUG_LOOKAHEAD", debugLookahead ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebugtokenmanager(boolean debugTokenManager) {
/* 162 */     this.optionalAttrs.put("DEBUG_TOKEN_MANAGER", debugTokenManager ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptimizetokenmanager(boolean optimizeTokenManager) {
/* 170 */     this.optionalAttrs.put("OPTIMIZE_TOKEN_MANAGER", 
/* 171 */         optimizeTokenManager ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorreporting(boolean errorReporting) {
/* 179 */     this.optionalAttrs.put("ERROR_REPORTING", errorReporting ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavaunicodeescape(boolean javaUnicodeEscape) {
/* 187 */     this.optionalAttrs.put("JAVA_UNICODE_ESCAPE", javaUnicodeEscape ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnicodeinput(boolean unicodeInput) {
/* 195 */     this.optionalAttrs.put("UNICODE_INPUT", unicodeInput ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnorecase(boolean ignoreCase) {
/* 203 */     this.optionalAttrs.put("IGNORE_CASE", ignoreCase ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommontokenaction(boolean commonTokenAction) {
/* 211 */     this.optionalAttrs.put("COMMON_TOKEN_ACTION", commonTokenAction ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUsertokenmanager(boolean userTokenManager) {
/* 219 */     this.optionalAttrs.put("USER_TOKEN_MANAGER", userTokenManager ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUsercharstream(boolean userCharStream) {
/* 227 */     this.optionalAttrs.put("USER_CHAR_STREAM", userCharStream ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBuildparser(boolean buildParser) {
/* 235 */     this.optionalAttrs.put("BUILD_PARSER", buildParser ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBuildtokenmanager(boolean buildTokenManager) {
/* 243 */     this.optionalAttrs.put("BUILD_TOKEN_MANAGER", buildTokenManager ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSanitycheck(boolean sanityCheck) {
/* 251 */     this.optionalAttrs.put("SANITY_CHECK", sanityCheck ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setForcelacheck(boolean forceLACheck) {
/* 259 */     this.optionalAttrs.put("FORCE_LA_CHECK", forceLACheck ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCachetokens(boolean cacheTokens) {
/* 267 */     this.optionalAttrs.put("CACHE_TOKENS", cacheTokens ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeeplinecolumn(boolean keepLineColumn) {
/* 275 */     this.optionalAttrs.put("KEEP_LINE_COLUMN", keepLineColumn ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJDKversion(String jdkVersion) {
/* 284 */     this.optionalAttrs.put("JDK_VERSION", jdkVersion);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputdirectory(File outputDirectory) {
/* 294 */     this.outputDirectory = outputDirectory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(File targetFile) {
/* 302 */     this.targetFile = targetFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavacchome(File javaccHome) {
/* 310 */     this.javaccHome = javaccHome;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxmemory(String max) {
/* 320 */     this.maxMemory = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaCC() {
/* 327 */     this.cmdl.setVm(JavaEnvUtils.getJreExecutable("java"));
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
/* 338 */     this.optionalAttrs.forEach((name, value) -> this.cmdl.createArgument().setValue("-" + name + ":" + value));
/*     */ 
/*     */ 
/*     */     
/* 342 */     if (this.targetFile == null || !this.targetFile.isFile()) {
/* 343 */       throw new BuildException("Invalid target: %s", new Object[] { this.targetFile });
/*     */     }
/*     */ 
/*     */     
/* 347 */     if (this.outputDirectory == null) {
/* 348 */       this.outputDirectory = new File(this.targetFile.getParent());
/* 349 */     } else if (!this.outputDirectory.isDirectory()) {
/* 350 */       throw new BuildException("Outputdir not a directory.");
/*     */     } 
/* 352 */     this.cmdl.createArgument().setValue("-OUTPUT_DIRECTORY:" + this.outputDirectory
/* 353 */         .getAbsolutePath());
/*     */ 
/*     */     
/* 356 */     File javaFile = getOutputJavaFile(this.outputDirectory, this.targetFile);
/* 357 */     if (javaFile.exists() && this.targetFile
/* 358 */       .lastModified() < javaFile.lastModified()) {
/* 359 */       log("Target is already built - skipping (" + this.targetFile + ")", 3);
/*     */       
/*     */       return;
/*     */     } 
/* 363 */     this.cmdl.createArgument().setValue(this.targetFile.getAbsolutePath());
/*     */     
/* 365 */     Path classpath = this.cmdl.createClasspath(getProject());
/* 366 */     File javaccJar = getArchiveFile(this.javaccHome);
/* 367 */     classpath.createPathElement().setPath(javaccJar.getAbsolutePath());
/* 368 */     classpath.addJavaRuntime();
/*     */     
/* 370 */     this.cmdl.setClassname(getMainClass(classpath, 1));
/*     */ 
/*     */     
/* 373 */     this.cmdl.setMaxmemory(this.maxMemory);
/* 374 */     Commandline.Argument arg = this.cmdl.createVmArgument();
/* 375 */     arg.setValue("-Dinstall.root=" + this.javaccHome.getAbsolutePath());
/*     */     
/* 377 */     Execute.runCommand(this, this.cmdl.getCommandline());
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
/*     */   protected static File getArchiveFile(File home) throws BuildException {
/* 390 */     return new File(home, ARCHIVE_LOCATIONS[
/* 391 */           getArchiveLocationIndex(home)]);
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
/*     */   protected static String getMainClass(File home, int type) throws BuildException {
/* 405 */     Path p = new Path(null);
/* 406 */     p.createPathElement().setLocation(getArchiveFile(home));
/* 407 */     p.addJavaRuntime();
/* 408 */     return getMainClass(p, type);
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
/*     */   protected static String getMainClass(Path path, int type) throws BuildException {
/* 422 */     String packagePrefix = null;
/* 423 */     String mainClass = null;
/*     */ 
/*     */     
/* 426 */     AntClassLoader l = AntClassLoader.newAntClassLoader(null, null, path
/*     */         
/* 428 */         .concatSystemClasspath("ignore"), true);
/*     */     try {
/* 430 */       String javaccClass = "COM.sun.labs.javacc.Main";
/* 431 */       InputStream is = l.getResourceAsStream(javaccClass.replace('.', '/') + ".class");
/*     */       
/* 433 */       if (is != null) {
/* 434 */         packagePrefix = "COM.sun.labs.";
/* 435 */         switch (type) {
/*     */           case 1:
/* 437 */             mainClass = "javacc.Main";
/*     */             break;
/*     */ 
/*     */           
/*     */           case 2:
/* 442 */             mainClass = "jjtree.Main";
/*     */             break;
/*     */ 
/*     */           
/*     */           case 3:
/* 447 */             mainClass = "jjdoc.JJDocMain";
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       } else {
/* 454 */         javaccClass = "org.javacc.parser.Main";
/* 455 */         is = l.getResourceAsStream(javaccClass.replace('.', '/') + ".class");
/*     */         
/* 457 */         if (is != null) {
/* 458 */           packagePrefix = "org.javacc.";
/*     */         } else {
/* 460 */           javaccClass = "org.netbeans.javacc.parser.Main";
/* 461 */           is = l.getResourceAsStream(javaccClass.replace('.', '/') + ".class");
/*     */           
/* 463 */           if (is != null) {
/* 464 */             packagePrefix = "org.netbeans.javacc.";
/*     */           }
/*     */         } 
/*     */         
/* 468 */         if (is != null) {
/* 469 */           switch (type) {
/*     */             case 1:
/* 471 */               mainClass = "parser.Main";
/*     */               break;
/*     */ 
/*     */             
/*     */             case 2:
/* 476 */               mainClass = "jjtree.Main";
/*     */               break;
/*     */ 
/*     */             
/*     */             case 3:
/* 481 */               mainClass = "jjdoc.JJDocMain";
/*     */               break;
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */       } 
/* 490 */       if (packagePrefix == null) {
/* 491 */         throw new BuildException("failed to load JavaCC");
/*     */       }
/* 493 */       if (mainClass == null) {
/* 494 */         throw new BuildException("unknown task type " + type);
/*     */       }
/* 496 */       String str1 = packagePrefix + mainClass;
/* 497 */       if (l != null) l.close(); 
/*     */       return str1;
/*     */     } catch (Throwable throwable) {
/*     */       if (l != null)
/*     */         try {
/*     */           l.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getArchiveLocationIndex(File home) throws BuildException {
/* 511 */     if (home == null || !home.isDirectory()) {
/* 512 */       throw new BuildException("JavaCC home must be a valid directory.");
/*     */     }
/*     */     
/* 515 */     for (int i = 0; i < ARCHIVE_LOCATIONS.length; i++) {
/* 516 */       File f = new File(home, ARCHIVE_LOCATIONS[i]);
/*     */       
/* 518 */       if (f.exists()) {
/* 519 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 523 */     throw new BuildException("Could not find a path to JavaCC.zip or javacc.jar from '%s'.", new Object[] { home });
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
/*     */   protected static int getMajorVersionNumber(File home) throws BuildException {
/* 539 */     return ARCHIVE_LOCATIONS_VS_MAJOR_VERSION[
/* 540 */         getArchiveLocationIndex(home)];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File getOutputJavaFile(File outputdir, File srcfile) {
/* 549 */     String path = srcfile.getPath();
/*     */ 
/*     */     
/* 552 */     int startBasename = path.lastIndexOf(File.separator);
/* 553 */     if (startBasename != -1) {
/* 554 */       path = path.substring(startBasename + 1);
/*     */     }
/*     */ 
/*     */     
/* 558 */     int startExtn = path.lastIndexOf('.');
/* 559 */     if (startExtn != -1) {
/* 560 */       path = path.substring(0, startExtn) + ".java";
/*     */     } else {
/* 562 */       path = path + ".java";
/*     */     } 
/*     */ 
/*     */     
/* 566 */     if (outputdir != null) {
/* 567 */       path = outputdir + File.separator + path;
/*     */     }
/*     */     
/* 570 */     return new File(path);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/javacc/JavaCC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */