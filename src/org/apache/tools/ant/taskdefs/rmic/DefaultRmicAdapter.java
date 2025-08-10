/*     */ package org.apache.tools.ant.taskdefs.rmic;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.Rmic;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
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
/*     */ 
/*     */ public abstract class DefaultRmicAdapter
/*     */   implements RmicAdapter
/*     */ {
/*  46 */   private static final Random RAND = new Random();
/*     */ 
/*     */   
/*     */   public static final String RMI_STUB_SUFFIX = "_Stub";
/*     */ 
/*     */   
/*     */   public static final String RMI_SKEL_SUFFIX = "_Skel";
/*     */ 
/*     */   
/*     */   public static final String RMI_TIE_SUFFIX = "_Tie";
/*     */ 
/*     */   
/*     */   public static final String STUB_COMPAT = "-vcompat";
/*     */ 
/*     */   
/*     */   public static final String STUB_1_1 = "-v1.1";
/*     */ 
/*     */   
/*     */   public static final String STUB_1_2 = "-v1.2";
/*     */ 
/*     */   
/*     */   public static final String STUB_OPTION_1_1 = "1.1";
/*     */ 
/*     */   
/*     */   public static final String STUB_OPTION_1_2 = "1.2";
/*     */ 
/*     */   
/*     */   public static final String STUB_OPTION_COMPAT = "compat";
/*     */ 
/*     */   
/*     */   private Rmic attributes;
/*     */   
/*     */   private FileNameMapper mapper;
/*     */ 
/*     */   
/*     */   public void setRmic(Rmic attributes) {
/*  82 */     this.attributes = attributes;
/*  83 */     this.mapper = new RmicFileNameMapper();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rmic getRmic() {
/*  91 */     return this.attributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getStubClassSuffix() {
/*  99 */     return "_Stub";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getSkelClassSuffix() {
/* 107 */     return "_Skel";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getTieClassSuffix() {
/* 115 */     return "_Tie";
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
/*     */   public FileNameMapper getMapper() {
/* 137 */     return this.mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 146 */     return getCompileClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path getCompileClasspath() {
/* 154 */     Path classpath = new Path(this.attributes.getProject());
/*     */ 
/*     */     
/* 157 */     classpath.setLocation(this.attributes.getBase());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 162 */     Path cp = this.attributes.getClasspath();
/* 163 */     if (cp == null) {
/* 164 */       cp = new Path(this.attributes.getProject());
/*     */     }
/* 166 */     if (this.attributes.getIncludeantruntime()) {
/* 167 */       classpath.addExisting(cp.concatSystemClasspath("last"));
/*     */     } else {
/* 169 */       classpath.addExisting(cp.concatSystemClasspath("ignore"));
/*     */     } 
/*     */     
/* 172 */     if (this.attributes.getIncludejavaruntime()) {
/* 173 */       classpath.addJavaRuntime();
/*     */     }
/* 175 */     return classpath;
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
/*     */   protected boolean areIiopAndIdlSupported() {
/* 187 */     return !JavaEnvUtils.isAtLeastJavaVersion("11");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupRmicCommand() {
/* 195 */     return setupRmicCommand(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupRmicCommand(String[] options) {
/* 205 */     Commandline cmd = new Commandline();
/*     */     
/* 207 */     if (options != null) {
/* 208 */       for (String option : options) {
/* 209 */         cmd.createArgument().setValue(option);
/*     */       }
/*     */     }
/*     */     
/* 213 */     Path classpath = getCompileClasspath();
/*     */     
/* 215 */     cmd.createArgument().setValue("-d");
/* 216 */     cmd.createArgument().setFile(this.attributes.getOutputDir());
/*     */     
/* 218 */     if (this.attributes.getExtdirs() != null) {
/* 219 */       cmd.createArgument().setValue("-extdirs");
/* 220 */       cmd.createArgument().setPath(this.attributes.getExtdirs());
/*     */     } 
/*     */     
/* 223 */     cmd.createArgument().setValue("-classpath");
/* 224 */     cmd.createArgument().setPath(classpath);
/* 225 */     String stubOption = addStubVersionOptions();
/* 226 */     if (stubOption != null)
/*     */     {
/* 228 */       cmd.createArgument().setValue(stubOption);
/*     */     }
/*     */ 
/*     */     
/* 232 */     if (null != this.attributes.getSourceBase()) {
/* 233 */       cmd.createArgument().setValue("-keepgenerated");
/*     */     }
/*     */     
/* 236 */     if (this.attributes.getIiop()) {
/* 237 */       if (!areIiopAndIdlSupported()) {
/* 238 */         throw new BuildException("this rmic implementation doesn't support the -iiop switch");
/*     */       }
/* 240 */       this.attributes.log("IIOP has been turned on.", 2);
/* 241 */       cmd.createArgument().setValue("-iiop");
/* 242 */       if (this.attributes.getIiopopts() != null) {
/* 243 */         this.attributes.log("IIOP Options: " + this.attributes.getIiopopts(), 2);
/*     */         
/* 245 */         cmd.createArgument().setValue(this.attributes.getIiopopts());
/*     */       } 
/*     */     } 
/*     */     
/* 249 */     if (this.attributes.getIdl()) {
/* 250 */       if (!areIiopAndIdlSupported()) {
/* 251 */         throw new BuildException("this rmic implementation doesn't support the -idl switch");
/*     */       }
/* 253 */       cmd.createArgument().setValue("-idl");
/* 254 */       this.attributes.log("IDL has been turned on.", 2);
/* 255 */       if (this.attributes.getIdlopts() != null) {
/* 256 */         cmd.createArgument().setValue(this.attributes.getIdlopts());
/* 257 */         this.attributes.log("IDL Options: " + this.attributes.getIdlopts(), 2);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 262 */     if (this.attributes.getDebug()) {
/* 263 */       cmd.createArgument().setValue("-g");
/*     */     }
/*     */     
/* 266 */     String[] compilerArgs = this.attributes.getCurrentCompilerArgs();
/* 267 */     compilerArgs = preprocessCompilerArgs(compilerArgs);
/* 268 */     cmd.addArguments(compilerArgs);
/*     */     
/* 270 */     verifyArguments(cmd);
/*     */     
/* 272 */     logAndAddFilesToCompile(cmd);
/* 273 */     return cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String addStubVersionOptions() {
/* 284 */     String stubVersion = this.attributes.getStubVersion();
/*     */     
/* 286 */     String stubOption = null;
/* 287 */     if (null != stubVersion) {
/* 288 */       if ("1.1".equals(stubVersion)) {
/* 289 */         stubOption = "-v1.1";
/* 290 */       } else if ("1.2".equals(stubVersion)) {
/* 291 */         stubOption = "-v1.2";
/* 292 */       } else if ("compat".equals(stubVersion)) {
/* 293 */         stubOption = "-vcompat";
/*     */       } else {
/*     */         
/* 296 */         this.attributes.log("Unknown stub option " + stubVersion);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 302 */     if (stubOption == null && 
/* 303 */       !this.attributes.getIiop() && 
/* 304 */       !this.attributes.getIdl()) {
/* 305 */       stubOption = "-vcompat";
/*     */     }
/* 307 */     return stubOption;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] preprocessCompilerArgs(String[] compilerArgs) {
/* 318 */     return compilerArgs;
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
/*     */   protected String[] filterJvmCompilerArgs(String[] compilerArgs) {
/* 330 */     int len = compilerArgs.length;
/* 331 */     List<String> args = new ArrayList<>(len);
/* 332 */     for (String arg : compilerArgs) {
/* 333 */       if (arg.startsWith("-J")) {
/* 334 */         this.attributes.log("Dropping " + arg + " from compiler arguments");
/*     */       } else {
/* 336 */         args.add(arg);
/*     */       } 
/*     */     } 
/* 339 */     return args.<String>toArray(new String[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void logAndAddFilesToCompile(Commandline cmd) {
/* 349 */     Vector<String> compileList = this.attributes.getCompileList();
/*     */     
/* 351 */     this.attributes.log("Compilation " + cmd.describeArguments(), 3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 357 */     String niceSourceList = ((compileList.size() == 1) ? "File" : "Files") + " to be compiled:" + (String)compileList.stream().peek(arg -> cmd.createArgument().setValue(arg)).collect(Collectors.joining("    "));
/* 358 */     this.attributes.log(niceSourceList, 3);
/*     */   }
/*     */   
/*     */   private void verifyArguments(Commandline cmd) {
/* 362 */     if (JavaEnvUtils.isAtLeastJavaVersion("9")) {
/* 363 */       for (String arg : cmd.getArguments()) {
/* 364 */         if ("-Xnew".equals(arg)) {
/* 365 */           throw new BuildException("JDK9 has removed support for -Xnew");
/*     */         }
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
/*     */   private class RmicFileNameMapper
/*     */     implements FileNameMapper
/*     */   {
/*     */     private RmicFileNameMapper() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setFrom(String s) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setTo(String s) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] mapFileName(String name) {
/* 403 */       if (name == null || 
/* 404 */         !name.endsWith(".class") || name
/* 405 */         .endsWith(DefaultRmicAdapter.this.getStubClassSuffix() + ".class") || name
/* 406 */         .endsWith(DefaultRmicAdapter.this.getSkelClassSuffix() + ".class") || name
/* 407 */         .endsWith(DefaultRmicAdapter.this.getTieClassSuffix() + ".class"))
/*     */       {
/* 409 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 413 */       String base = StringUtils.removeSuffix(name, ".class");
/*     */       
/* 415 */       String classname = base.replace(File.separatorChar, '.');
/* 416 */       if (DefaultRmicAdapter.this.attributes.getVerify() && 
/* 417 */         !DefaultRmicAdapter.this.attributes.isValidRmiRemote(classname)) {
/* 418 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 429 */       String[] target = { name + ".tmp." + DefaultRmicAdapter.RAND.nextLong() };
/*     */       
/* 431 */       if (!DefaultRmicAdapter.this.attributes.getIiop() && !DefaultRmicAdapter.this.attributes.getIdl()) {
/*     */         
/* 433 */         if ("1.2".equals(DefaultRmicAdapter.this.attributes.getStubVersion()))
/*     */         {
/* 435 */           target = new String[] { base + this.this$0.getStubClassSuffix() + ".class" };
/*     */         
/*     */         }
/*     */         else
/*     */         {
/* 440 */           target = new String[] { base + this.this$0.getStubClassSuffix() + ".class", base + this.this$0.getSkelClassSuffix() + ".class" };
/*     */         }
/*     */       
/* 443 */       } else if (!DefaultRmicAdapter.this.attributes.getIdl()) {
/* 444 */         String dirname; int lastSlash = base.lastIndexOf(File.separatorChar);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 450 */         int index = -1;
/* 451 */         if (lastSlash == -1) {
/*     */           
/* 453 */           index = 0;
/* 454 */           dirname = "";
/*     */         } else {
/* 456 */           index = lastSlash + 1;
/* 457 */           dirname = base.substring(0, index);
/*     */         } 
/*     */         
/* 460 */         String filename = base.substring(index);
/*     */         
/*     */         try {
/* 463 */           Class<?> c = DefaultRmicAdapter.this.attributes.getLoader().loadClass(classname);
/*     */           
/* 465 */           if (c.isInterface()) {
/*     */ 
/*     */             
/* 468 */             target = new String[] { dirname + "_" + filename + this.this$0.getStubClassSuffix() + ".class" };
/*     */           } else {
/*     */             String iDir;
/*     */ 
/*     */             
/*     */             int iIndex;
/*     */ 
/*     */             
/* 476 */             Class<?> interf = DefaultRmicAdapter.this.attributes.getRemoteInterface(c);
/* 477 */             String iName = interf.getName();
/*     */ 
/*     */             
/* 480 */             int lastDot = iName.lastIndexOf('.');
/* 481 */             if (lastDot == -1) {
/*     */               
/* 483 */               iIndex = 0;
/* 484 */               iDir = "";
/*     */             } else {
/* 486 */               iIndex = lastDot + 1;
/* 487 */               iDir = iName.substring(0, iIndex);
/* 488 */               iDir = iDir.replace('.', File.separatorChar);
/*     */             } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 495 */             target = new String[] { dirname + "_" + filename + this.this$0.getTieClassSuffix() + ".class", iDir + "_" + iName.substring(iIndex) + this.this$0.getStubClassSuffix() + ".class" };
/*     */           }
/*     */         
/* 498 */         } catch (ClassNotFoundException e) {
/* 499 */           DefaultRmicAdapter.this.attributes.log("Unable to verify class " + classname + ". It could not be found.", 1);
/*     */         
/*     */         }
/* 502 */         catch (NoClassDefFoundError e) {
/* 503 */           DefaultRmicAdapter.this.attributes.log("Unable to verify class " + classname + ". It is not defined.", 1);
/*     */         }
/* 505 */         catch (Throwable t) {
/* 506 */           DefaultRmicAdapter.this.attributes.log("Unable to verify class " + classname + ". Loading caused Exception: " + t
/*     */               
/* 508 */               .getMessage(), 1);
/*     */         } 
/*     */       } 
/* 511 */       return target;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/rmic/DefaultRmicAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */