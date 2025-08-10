/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.rmi.Remote;
/*     */ import java.util.Objects;
/*     */ import java.util.Vector;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.taskdefs.rmic.RmicAdapter;
/*     */ import org.apache.tools.ant.taskdefs.rmic.RmicAdapterFactory;
/*     */ import org.apache.tools.ant.types.FilterSetCollection;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.util.FileNameMapper;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.SourceFileScanner;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ import org.apache.tools.ant.util.facade.FacadeTaskHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rmic
/*     */   extends MatchingTask
/*     */ {
/*     */   public static final String ERROR_RMIC_FAILED = "Rmic failed; see the compiler error output for details.";
/*     */   public static final String ERROR_UNABLE_TO_VERIFY_CLASS = "Unable to verify class ";
/*     */   public static final String ERROR_NOT_FOUND = ". It could not be found.";
/*     */   public static final String ERROR_NOT_DEFINED = ". It is not defined.";
/*     */   public static final String ERROR_LOADING_CAUSED_EXCEPTION = ". Loading caused Exception: ";
/*     */   public static final String ERROR_NO_BASE_EXISTS = "base or destdir does not exist: ";
/*     */   public static final String ERROR_NOT_A_DIR = "base or destdir is not a directory:";
/*     */   public static final String ERROR_BASE_NOT_SET = "base or destdir attribute must be set!";
/* 110 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */   
/*     */   private File baseDir;
/*     */   
/*     */   private File destDir;
/*     */   
/*     */   private String classname;
/*     */   private File sourceBase;
/*     */   private String stubVersion;
/*     */   private Path compileClasspath;
/*     */   private Path extDirs;
/*     */   private boolean verify = false;
/*     */   private boolean filtering = false;
/*     */   private boolean iiop = false;
/*     */   private String iiopOpts;
/*     */   private boolean idl = false;
/*     */   private String idlOpts;
/*     */   private boolean debug = false;
/*     */   private boolean includeAntRuntime = true;
/*     */   private boolean includeJavaRuntime = false;
/* 130 */   private Vector<String> compileList = new Vector<>();
/*     */   
/* 132 */   private AntClassLoader loader = null;
/*     */   
/*     */   private FacadeTaskHelper facade;
/* 135 */   private String executable = null;
/*     */   
/*     */   private boolean listFiles = false;
/*     */   
/* 139 */   private RmicAdapter nestedAdapter = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rmic() {
/* 145 */     this.facade = new FacadeTaskHelper("default");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBase(File base) {
/* 153 */     this.baseDir = base;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestdir(File destdir) {
/* 162 */     this.destDir = destdir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDestdir() {
/* 171 */     return this.destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getOutputDir() {
/* 181 */     if (getDestdir() != null) {
/* 182 */       return getDestdir();
/*     */     }
/* 184 */     return getBase();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getBase() {
/* 192 */     return this.baseDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassname(String classname) {
/* 201 */     this.classname = classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassname() {
/* 209 */     return this.classname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSourceBase(File sourceBase) {
/* 217 */     this.sourceBase = sourceBase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getSourceBase() {
/* 225 */     return this.sourceBase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStubVersion(String stubVersion) {
/* 234 */     this.stubVersion = stubVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStubVersion() {
/* 242 */     return this.stubVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFiltering(boolean filter) {
/* 250 */     this.filtering = filter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFiltering() {
/* 258 */     return this.filtering;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 267 */     this.debug = debug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getDebug() {
/* 275 */     return this.debug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setClasspath(Path classpath) {
/* 283 */     if (this.compileClasspath == null) {
/* 284 */       this.compileClasspath = classpath;
/*     */     } else {
/* 286 */       this.compileClasspath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Path createClasspath() {
/* 295 */     if (this.compileClasspath == null) {
/* 296 */       this.compileClasspath = new Path(getProject());
/*     */     }
/* 298 */     return this.compileClasspath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference pathRef) {
/* 307 */     createClasspath().setRefid(pathRef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 315 */     return this.compileClasspath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerify(boolean verify) {
/* 326 */     this.verify = verify;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getVerify() {
/* 334 */     return this.verify;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIiop(boolean iiop) {
/* 344 */     this.iiop = iiop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIiop() {
/* 352 */     return this.iiop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIiopopts(String iiopOpts) {
/* 360 */     this.iiopOpts = iiopOpts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIiopopts() {
/* 368 */     return this.iiopOpts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdl(boolean idl) {
/* 378 */     this.idl = idl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIdl() {
/* 386 */     return this.idl;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdlopts(String idlOpts) {
/* 394 */     this.idlOpts = idlOpts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIdlopts() {
/* 402 */     return this.idlOpts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<String> getFileList() {
/* 410 */     return this.compileList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludeantruntime(boolean include) {
/* 420 */     this.includeAntRuntime = include;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIncludeantruntime() {
/* 429 */     return this.includeAntRuntime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIncludejavaruntime(boolean include) {
/* 440 */     this.includeJavaRuntime = include;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getIncludejavaruntime() {
/* 449 */     return this.includeJavaRuntime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setExtdirs(Path extDirs) {
/* 458 */     if (this.extDirs == null) {
/* 459 */       this.extDirs = extDirs;
/*     */     } else {
/* 461 */       this.extDirs.append(extDirs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Path createExtdirs() {
/* 470 */     if (this.extDirs == null) {
/* 471 */       this.extDirs = new Path(getProject());
/*     */     }
/* 473 */     return this.extDirs.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getExtdirs() {
/* 482 */     return this.extDirs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<String> getCompileList() {
/* 489 */     return this.compileList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompiler(String compiler) {
/* 500 */     if (!compiler.isEmpty()) {
/* 501 */       this.facade.setImplementation(compiler);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCompiler() {
/* 511 */     this.facade.setMagicValue(getProject().getProperty("build.rmic"));
/* 512 */     return this.facade.getImplementation();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImplementationSpecificArgument createCompilerArg() {
/* 521 */     ImplementationSpecificArgument arg = new ImplementationSpecificArgument();
/* 522 */     this.facade.addImplementationArgument(arg);
/* 523 */     return arg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getCurrentCompilerArgs() {
/* 532 */     getCompiler();
/* 533 */     return this.facade.getArgs();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExecutable(String ex) {
/* 543 */     this.executable = ex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExecutable() {
/* 554 */     return this.executable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createCompilerClasspath() {
/* 565 */     return this.facade.getImplementationClasspath(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setListfiles(boolean list) {
/* 575 */     this.listFiles = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(RmicAdapter adapter) {
/* 585 */     if (this.nestedAdapter != null) {
/* 586 */       throw new BuildException("Can't have more than one rmic adapter");
/*     */     }
/* 588 */     this.nestedAdapter = adapter;
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
/*     */     try {
/* 600 */       this.compileList.clear();
/*     */       
/* 602 */       File outputDir = getOutputDir();
/* 603 */       if (outputDir == null) {
/* 604 */         throw new BuildException("base or destdir attribute must be set!", getLocation());
/*     */       }
/* 606 */       if (!outputDir.exists()) {
/* 607 */         throw new BuildException("base or destdir does not exist: " + outputDir, 
/* 608 */             getLocation());
/*     */       }
/* 610 */       if (!outputDir.isDirectory()) {
/* 611 */         throw new BuildException("base or destdir is not a directory:" + outputDir, getLocation());
/*     */       }
/* 613 */       if (this.verify) {
/* 614 */         log("Verify has been turned on.", 3);
/*     */       }
/*     */       
/* 617 */       RmicAdapter adapter = (this.nestedAdapter != null) ? this.nestedAdapter : RmicAdapterFactory.getRmic(getCompiler(), this, 
/* 618 */           createCompilerClasspath());
/*     */ 
/*     */       
/* 621 */       adapter.setRmic(this);
/*     */       
/* 623 */       Path classpath = adapter.getClasspath();
/* 624 */       this.loader = getProject().createClassLoader(classpath);
/*     */ 
/*     */ 
/*     */       
/* 628 */       if (this.classname == null) {
/* 629 */         DirectoryScanner ds = getDirectoryScanner(this.baseDir);
/* 630 */         scanDir(this.baseDir, ds.getIncludedFiles(), adapter.getMapper());
/*     */       } else {
/*     */         
/* 633 */         String path = this.classname.replace('.', File.separatorChar) + ".class";
/*     */         
/* 635 */         File f = new File(this.baseDir, path);
/* 636 */         if (f.isFile()) {
/* 637 */           scanDir(this.baseDir, new String[] { path }, adapter.getMapper());
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 643 */           this.compileList.add(this.classname);
/*     */         } 
/*     */       } 
/* 646 */       int fileCount = this.compileList.size();
/* 647 */       if (fileCount > 0) {
/* 648 */         log("RMI Compiling " + fileCount + " class" + (
/* 649 */             (fileCount > 1) ? "es" : "") + " to " + outputDir, 2);
/*     */ 
/*     */         
/* 652 */         if (this.listFiles) {
/* 653 */           this.compileList.forEach(this::log);
/*     */         }
/*     */ 
/*     */         
/* 657 */         if (!adapter.execute()) {
/* 658 */           throw new BuildException("Rmic failed; see the compiler error output for details.", getLocation());
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 666 */       if (null != this.sourceBase && !outputDir.equals(this.sourceBase) && fileCount > 0)
/*     */       {
/* 668 */         if (this.idl) {
/* 669 */           log("Cannot determine sourcefiles in idl mode, ", 1);
/*     */           
/* 671 */           log("sourcebase attribute will be ignored.", 1);
/*     */         } else {
/*     */           
/* 674 */           this.compileList.forEach(f -> moveGeneratedFile(outputDir, this.sourceBase, f, adapter));
/*     */         } 
/*     */       }
/*     */     } finally {
/*     */       
/* 679 */       cleanup();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanup() {
/* 689 */     if (this.loader != null) {
/* 690 */       this.loader.cleanup();
/* 691 */       this.loader = null;
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
/*     */   private void moveGeneratedFile(File baseDir, File sourceBaseFile, String classname, RmicAdapter adapter) throws BuildException {
/* 703 */     String classFileName = classname.replace('.', File.separatorChar) + ".class";
/*     */     
/* 705 */     String[] generatedFiles = adapter.getMapper().mapFileName(classFileName);
/* 706 */     if (generatedFiles == null) {
/*     */       return;
/*     */     }
/*     */     
/* 710 */     for (String generatedFile : generatedFiles) {
/* 711 */       if (generatedFile.endsWith(".class")) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 717 */         String sourceFileName = StringUtils.removeSuffix(generatedFile, ".class") + ".java";
/*     */         
/* 719 */         File oldFile = new File(baseDir, sourceFileName);
/* 720 */         if (oldFile.exists()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 725 */           File newFile = new File(sourceBaseFile, sourceFileName);
/*     */           try {
/* 727 */             if (this.filtering) {
/* 728 */               FILE_UTILS.copyFile(oldFile, newFile, new FilterSetCollection(
/* 729 */                     getProject().getGlobalFilterSet()));
/*     */             } else {
/* 731 */               FILE_UTILS.copyFile(oldFile, newFile);
/*     */             } 
/* 733 */             oldFile.delete();
/* 734 */           } catch (IOException ioe) {
/* 735 */             throw new BuildException("Failed to copy " + oldFile + " to " + newFile + " due to " + ioe
/* 736 */                 .getMessage(), ioe, 
/* 737 */                 getLocation());
/*     */           } 
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
/*     */   protected void scanDir(File baseDir, String[] files, FileNameMapper mapper) {
/* 750 */     String[] newFiles = files;
/* 751 */     if (this.idl) {
/* 752 */       log("will leave uptodate test to rmic implementation in idl mode.", 3);
/*     */     }
/* 754 */     else if (this.iiop && this.iiopOpts != null && this.iiopOpts.contains("-always")) {
/* 755 */       log("no uptodate test as -always option has been specified", 3);
/*     */     } else {
/*     */       
/* 758 */       SourceFileScanner sfs = new SourceFileScanner(this);
/* 759 */       newFiles = sfs.restrict(files, baseDir, getOutputDir(), mapper);
/*     */     } 
/*     */ 
/*     */     
/* 763 */     Objects.requireNonNull(this.compileList); Stream.<String>of(newFiles).map(s -> s.replace(File.separatorChar, '.')).map(s -> s.substring(0, s.lastIndexOf(".class"))).forEach(this.compileList::add);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidRmiRemote(String classname) {
/*     */     try {
/* 773 */       Class<?> testClass = this.loader.loadClass(classname);
/*     */       
/* 775 */       return ((!testClass.isInterface() || this.iiop || this.idl) && isValidRmiRemote(testClass));
/* 776 */     } catch (ClassNotFoundException e) {
/* 777 */       log("Unable to verify class " + classname + ". It could not be found.", 1);
/*     */     }
/* 779 */     catch (NoClassDefFoundError e) {
/* 780 */       log("Unable to verify class " + classname + ". It is not defined.", 1);
/*     */     }
/* 782 */     catch (Throwable t) {
/* 783 */       log("Unable to verify class " + classname + ". Loading caused Exception: " + t
/* 784 */           .getMessage(), 1);
/*     */     } 
/*     */ 
/*     */     
/* 788 */     return false;
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
/*     */   public Class<?> getRemoteInterface(Class<?> testClass) {
/* 800 */     Objects.requireNonNull(Remote.class); return Stream.<Class<?>>of(testClass.getInterfaces()).filter(Remote.class::isAssignableFrom).findFirst().orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isValidRmiRemote(Class<?> testClass) {
/* 808 */     return Remote.class.isAssignableFrom(testClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getLoader() {
/* 816 */     return (ClassLoader)this.loader;
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
/*     */   public class ImplementationSpecificArgument
/*     */     extends org.apache.tools.ant.util.facade.ImplementationSpecificArgument
/*     */   {
/*     */     public void setCompiler(String impl) {
/* 835 */       setImplementation(impl);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Rmic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */