/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.Files;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.TreeMap;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.DirectoryScanner;
/*      */ import org.apache.tools.ant.taskdefs.compilers.CompilerAdapter;
/*      */ import org.apache.tools.ant.taskdefs.compilers.CompilerAdapterExtension;
/*      */ import org.apache.tools.ant.taskdefs.compilers.CompilerAdapterFactory;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.Reference;
/*      */ import org.apache.tools.ant.util.FileNameMapper;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.GlobPatternMapper;
/*      */ import org.apache.tools.ant.util.JavaEnvUtils;
/*      */ import org.apache.tools.ant.util.SourceFileScanner;
/*      */ import org.apache.tools.ant.util.facade.FacadeTaskHelper;
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
/*      */ public class Javac
/*      */   extends MatchingTask
/*      */ {
/*      */   private static final String FAIL_MSG = "Compile failed; see the compiler error output for details.";
/*      */   private static final char GROUP_START_MARK = '{';
/*      */   private static final char GROUP_END_MARK = '}';
/*      */   private static final char GROUP_SEP_MARK = ',';
/*      */   private static final String MODULE_MARKER = "*";
/*   93 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */   
/*      */   private Path src;
/*      */   private File destDir;
/*      */   private File nativeHeaderDir;
/*      */   private Path compileClasspath;
/*      */   private Path modulepath;
/*      */   private Path upgrademodulepath;
/*      */   private Path compileSourcepath;
/*      */   private Path moduleSourcepath;
/*      */   private String encoding;
/*      */   private boolean debug = false;
/*      */   private boolean optimize = false;
/*      */   private boolean deprecation = false;
/*      */   private boolean depend = false;
/*      */   private boolean verbose = false;
/*      */   private String targetAttribute;
/*      */   private String release;
/*      */   private Path bootclasspath;
/*      */   private Path extdirs;
/*      */   private Boolean includeAntRuntime;
/*      */   private boolean includeJavaRuntime = false;
/*      */   private boolean fork = false;
/*  116 */   private String forkedExecutable = null;
/*      */   private boolean nowarn = false;
/*      */   private String memoryInitialSize;
/*      */   private String memoryMaximumSize;
/*  120 */   private FacadeTaskHelper facade = null;
/*      */   
/*      */   protected boolean failOnError = true;
/*      */   
/*      */   protected boolean listFiles = false;
/*  125 */   protected File[] compileList = new File[0];
/*  126 */   private Map<String, Long> packageInfos = new HashMap<>();
/*      */   
/*      */   private String source;
/*      */   
/*      */   private String debugLevel;
/*      */   private File tmpDir;
/*      */   private String updatedProperty;
/*      */   private String errorProperty;
/*      */   private boolean taskSuccess = true;
/*      */   private boolean includeDestClasses = true;
/*  136 */   private CompilerAdapter nestedAdapter = null;
/*      */ 
/*      */   
/*      */   private boolean createMissingPackageInfoClass = true;
/*      */ 
/*      */ 
/*      */   
/*      */   public Javac() {
/*  144 */     this.facade = new FacadeTaskHelper(assumedJavaVersion());
/*      */   }
/*      */   
/*      */   private String assumedJavaVersion() {
/*  148 */     if (JavaEnvUtils.isJavaVersion("1.8")) {
/*  149 */       return "javac1.8";
/*      */     }
/*  151 */     if (JavaEnvUtils.isJavaVersion("9")) {
/*  152 */       return "javac9";
/*      */     }
/*  154 */     if (JavaEnvUtils.isAtLeastJavaVersion("10")) {
/*  155 */       return "javac10+";
/*      */     }
/*      */     
/*  158 */     return "modern";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDebugLevel() {
/*  166 */     return this.debugLevel;
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
/*      */   public void setDebugLevel(String v) {
/*  182 */     this.debugLevel = v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSource() {
/*  190 */     return (this.source != null) ? 
/*  191 */       this.source : getProject().getProperty("ant.build.javac.source");
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
/*      */   public void setSource(String v) {
/*  209 */     this.source = v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createSrc() {
/*  218 */     if (this.src == null) {
/*  219 */       this.src = new Path(getProject());
/*      */     }
/*  221 */     return this.src.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Path recreateSrc() {
/*  230 */     this.src = null;
/*  231 */     return createSrc();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSrcdir(Path srcDir) {
/*  239 */     if (this.src == null) {
/*  240 */       this.src = srcDir;
/*      */     } else {
/*  242 */       this.src.append(srcDir);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getSrcdir() {
/*  251 */     return this.src;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestdir(File destDir) {
/*  260 */     this.destDir = destDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getDestdir() {
/*  269 */     return this.destDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNativeHeaderDir(File nhDir) {
/*  279 */     this.nativeHeaderDir = nhDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getNativeHeaderDir() {
/*  289 */     return this.nativeHeaderDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSourcepath(Path sourcepath) {
/*  297 */     if (this.compileSourcepath == null) {
/*  298 */       this.compileSourcepath = sourcepath;
/*      */     } else {
/*  300 */       this.compileSourcepath.append(sourcepath);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getSourcepath() {
/*  309 */     return this.compileSourcepath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createSourcepath() {
/*  317 */     if (this.compileSourcepath == null) {
/*  318 */       this.compileSourcepath = new Path(getProject());
/*      */     }
/*  320 */     return this.compileSourcepath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSourcepathRef(Reference r) {
/*  328 */     createSourcepath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulesourcepath(Path msp) {
/*  337 */     if (this.moduleSourcepath == null) {
/*  338 */       this.moduleSourcepath = msp;
/*      */     } else {
/*  340 */       this.moduleSourcepath.append(msp);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getModulesourcepath() {
/*  350 */     return this.moduleSourcepath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createModulesourcepath() {
/*  359 */     if (this.moduleSourcepath == null) {
/*  360 */       this.moduleSourcepath = new Path(getProject());
/*      */     }
/*  362 */     return this.moduleSourcepath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulesourcepathRef(Reference r) {
/*  371 */     createModulesourcepath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspath(Path classpath) {
/*  380 */     if (this.compileClasspath == null) {
/*  381 */       this.compileClasspath = classpath;
/*      */     } else {
/*  383 */       this.compileClasspath.append(classpath);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getClasspath() {
/*  392 */     return this.compileClasspath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createClasspath() {
/*  400 */     if (this.compileClasspath == null) {
/*  401 */       this.compileClasspath = new Path(getProject());
/*      */     }
/*  403 */     return this.compileClasspath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspathRef(Reference r) {
/*  411 */     createClasspath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulepath(Path mp) {
/*  420 */     if (this.modulepath == null) {
/*  421 */       this.modulepath = mp;
/*      */     } else {
/*  423 */       this.modulepath.append(mp);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getModulepath() {
/*  433 */     return this.modulepath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createModulepath() {
/*  442 */     if (this.modulepath == null) {
/*  443 */       this.modulepath = new Path(getProject());
/*      */     }
/*  445 */     return this.modulepath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulepathRef(Reference r) {
/*  454 */     createModulepath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUpgrademodulepath(Path ump) {
/*  463 */     if (this.upgrademodulepath == null) {
/*  464 */       this.upgrademodulepath = ump;
/*      */     } else {
/*  466 */       this.upgrademodulepath.append(ump);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getUpgrademodulepath() {
/*  476 */     return this.upgrademodulepath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createUpgrademodulepath() {
/*  485 */     if (this.upgrademodulepath == null) {
/*  486 */       this.upgrademodulepath = new Path(getProject());
/*      */     }
/*  488 */     return this.upgrademodulepath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUpgrademodulepathRef(Reference r) {
/*  497 */     createUpgrademodulepath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBootclasspath(Path bootclasspath) {
/*  507 */     if (this.bootclasspath == null) {
/*  508 */       this.bootclasspath = bootclasspath;
/*      */     } else {
/*  510 */       this.bootclasspath.append(bootclasspath);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getBootclasspath() {
/*  520 */     return this.bootclasspath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createBootclasspath() {
/*  528 */     if (this.bootclasspath == null) {
/*  529 */       this.bootclasspath = new Path(getProject());
/*      */     }
/*  531 */     return this.bootclasspath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBootClasspathRef(Reference r) {
/*  539 */     createBootclasspath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtdirs(Path extdirs) {
/*  548 */     if (this.extdirs == null) {
/*  549 */       this.extdirs = extdirs;
/*      */     } else {
/*  551 */       this.extdirs.append(extdirs);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getExtdirs() {
/*  561 */     return this.extdirs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createExtdirs() {
/*  569 */     if (this.extdirs == null) {
/*  570 */       this.extdirs = new Path(getProject());
/*      */     }
/*  572 */     return this.extdirs.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setListfiles(boolean list) {
/*  580 */     this.listFiles = list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getListfiles() {
/*  588 */     return this.listFiles;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailonerror(boolean fail) {
/*  597 */     this.failOnError = fail;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProceed(boolean proceed) {
/*  605 */     this.failOnError = !proceed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getFailonerror() {
/*  613 */     return this.failOnError;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDeprecation(boolean deprecation) {
/*  622 */     this.deprecation = deprecation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getDeprecation() {
/*  630 */     return this.deprecation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMemoryInitialSize(String memoryInitialSize) {
/*  641 */     this.memoryInitialSize = memoryInitialSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMemoryInitialSize() {
/*  649 */     return this.memoryInitialSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMemoryMaximumSize(String memoryMaximumSize) {
/*  660 */     this.memoryMaximumSize = memoryMaximumSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMemoryMaximumSize() {
/*  668 */     return this.memoryMaximumSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEncoding(String encoding) {
/*  676 */     this.encoding = encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getEncoding() {
/*  684 */     return this.encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDebug(boolean debug) {
/*  693 */     this.debug = debug;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getDebug() {
/*  701 */     return this.debug;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOptimize(boolean optimize) {
/*  709 */     this.optimize = optimize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOptimize() {
/*  717 */     return this.optimize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDepend(boolean depend) {
/*  726 */     this.depend = depend;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getDepend() {
/*  734 */     return this.depend;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVerbose(boolean verbose) {
/*  742 */     this.verbose = verbose;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getVerbose() {
/*  750 */     return this.verbose;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTarget(String target) {
/*  760 */     this.targetAttribute = target;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getTarget() {
/*  768 */     return (this.targetAttribute != null) ? 
/*  769 */       this.targetAttribute : 
/*  770 */       getProject().getProperty("ant.build.javac.target");
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
/*      */   public void setRelease(String release) {
/*  784 */     this.release = release;
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
/*      */   public String getRelease() {
/*  796 */     return this.release;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIncludeantruntime(boolean include) {
/*  804 */     this.includeAntRuntime = Boolean.valueOf(include);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getIncludeantruntime() {
/*  812 */     return (this.includeAntRuntime == null || this.includeAntRuntime.booleanValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIncludejavaruntime(boolean include) {
/*  820 */     this.includeJavaRuntime = include;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getIncludejavaruntime() {
/*  829 */     return this.includeJavaRuntime;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFork(boolean f) {
/*  838 */     this.fork = f;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExecutable(String forkExec) {
/*  849 */     this.forkedExecutable = forkExec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getExecutable() {
/*  859 */     return this.forkedExecutable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isForkedJavac() {
/*  867 */     return (this.fork || CompilerAdapterFactory.isForkedJavac(getCompiler()));
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
/*      */   public String getJavacExecutable() {
/*  883 */     if (this.forkedExecutable == null && isForkedJavac()) {
/*  884 */       this.forkedExecutable = getSystemJavac();
/*  885 */     } else if (this.forkedExecutable != null && !isForkedJavac()) {
/*  886 */       this.forkedExecutable = null;
/*      */     } 
/*  888 */     return this.forkedExecutable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNowarn(boolean flag) {
/*  896 */     this.nowarn = flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getNowarn() {
/*  904 */     return this.nowarn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ImplementationSpecificArgument createCompilerArg() {
/*  912 */     ImplementationSpecificArgument arg = new ImplementationSpecificArgument();
/*      */     
/*  914 */     this.facade.addImplementationArgument(arg);
/*  915 */     return arg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getCurrentCompilerArgs() {
/*  923 */     String chosen = this.facade.getExplicitChoice();
/*      */     
/*      */     try {
/*  926 */       String appliedCompiler = getCompiler();
/*  927 */       this.facade.setImplementation(appliedCompiler);
/*      */       
/*  929 */       String[] result = this.facade.getArgs();
/*      */       
/*  931 */       String altCompilerName = getAltCompilerName(this.facade.getImplementation());
/*      */       
/*  933 */       if (result.length == 0 && altCompilerName != null) {
/*  934 */         this.facade.setImplementation(altCompilerName);
/*  935 */         result = this.facade.getArgs();
/*      */       } 
/*      */       
/*  938 */       return result;
/*      */     } finally {
/*      */       
/*  941 */       this.facade.setImplementation(chosen);
/*      */     } 
/*      */   }
/*      */   
/*      */   private String getAltCompilerName(String anImplementation) {
/*  946 */     if (CompilerAdapterFactory.isModernJdkCompiler(anImplementation)) {
/*  947 */       return "modern";
/*      */     }
/*  949 */     if (CompilerAdapterFactory.isClassicJdkCompiler(anImplementation)) {
/*  950 */       return "classic";
/*      */     }
/*  952 */     if ("modern".equalsIgnoreCase(anImplementation)) {
/*  953 */       String nextSelected = assumedJavaVersion();
/*  954 */       if (CompilerAdapterFactory.isModernJdkCompiler(nextSelected)) {
/*  955 */         return nextSelected;
/*      */       }
/*      */     } 
/*  958 */     if ("classic".equalsIgnoreCase(anImplementation)) {
/*  959 */       return assumedJavaVersion();
/*      */     }
/*  961 */     if (CompilerAdapterFactory.isForkedJavac(anImplementation)) {
/*  962 */       return assumedJavaVersion();
/*      */     }
/*  964 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTempdir(File tmpDir) {
/*  974 */     this.tmpDir = tmpDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getTempdir() {
/*  984 */     return this.tmpDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUpdatedProperty(String updatedProperty) {
/*  995 */     this.updatedProperty = updatedProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setErrorProperty(String errorProperty) {
/* 1006 */     this.errorProperty = errorProperty;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIncludeDestClasses(boolean includeDestClasses) {
/* 1017 */     this.includeDestClasses = includeDestClasses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIncludeDestClasses() {
/* 1025 */     return this.includeDestClasses;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getTaskSuccess() {
/* 1034 */     return this.taskSuccess;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createCompilerClasspath() {
/* 1045 */     return this.facade.getImplementationClasspath(getProject());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(CompilerAdapter adapter) {
/* 1055 */     if (this.nestedAdapter != null) {
/* 1056 */       throw new BuildException("Can't have more than one compiler adapter");
/*      */     }
/*      */     
/* 1059 */     this.nestedAdapter = adapter;
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
/*      */   public void setCreateMissingPackageInfoClass(boolean b) {
/* 1071 */     this.createMissingPackageInfoClass = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/* 1080 */     checkParameters();
/* 1081 */     resetFileLists();
/*      */ 
/*      */ 
/*      */     
/* 1085 */     if (hasPath(this.src)) {
/* 1086 */       collectFileListFromSourcePath();
/*      */     } else {
/* 1088 */       assert hasPath(this.moduleSourcepath) : "Either srcDir or moduleSourcepath must be given";
/* 1089 */       collectFileListFromModulePath();
/*      */     } 
/*      */     
/* 1092 */     compile();
/* 1093 */     if (this.updatedProperty != null && this.taskSuccess && this.compileList.length != 0)
/*      */     {
/*      */       
/* 1096 */       getProject().setNewProperty(this.updatedProperty, "true");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void resetFileLists() {
/* 1104 */     this.compileList = new File[0];
/* 1105 */     this.packageInfos = new HashMap<>();
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
/*      */   protected void scanDir(File srcDir, File destDir, String[] files) {
/* 1117 */     GlobPatternMapper m = new GlobPatternMapper();
/*      */     
/* 1119 */     for (String extension : findSupportedFileExtensions()) {
/* 1120 */       m.setFrom(extension);
/* 1121 */       m.setTo("*.class");
/* 1122 */       SourceFileScanner sfs = new SourceFileScanner(this);
/* 1123 */       File[] newFiles = sfs.restrictAsFiles(files, srcDir, destDir, (FileNameMapper)m);
/*      */       
/* 1125 */       if (newFiles.length > 0) {
/* 1126 */         lookForPackageInfos(srcDir, newFiles);
/* 1127 */         File[] newCompileList = new File[this.compileList.length + newFiles.length];
/*      */         
/* 1129 */         System.arraycopy(this.compileList, 0, newCompileList, 0, this.compileList.length);
/*      */         
/* 1131 */         System.arraycopy(newFiles, 0, newCompileList, this.compileList.length, newFiles.length);
/*      */         
/* 1133 */         this.compileList = newCompileList;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void collectFileListFromSourcePath() {
/* 1139 */     for (String filename : this.src.list()) {
/* 1140 */       File srcDir = getProject().resolveFile(filename);
/* 1141 */       if (!srcDir.exists()) {
/* 1142 */         throw new BuildException("srcdir \"" + srcDir
/* 1143 */             .getPath() + "\" does not exist!", 
/* 1144 */             getLocation());
/*      */       }
/*      */       
/* 1147 */       DirectoryScanner ds = getDirectoryScanner(srcDir);
/*      */       
/* 1149 */       scanDir(srcDir, (this.destDir != null) ? this.destDir : srcDir, ds.getIncludedFiles());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void collectFileListFromModulePath() {
/* 1154 */     FileUtils fu = FileUtils.getFileUtils();
/* 1155 */     for (String pathElement : this.moduleSourcepath.list()) {
/* 1156 */       boolean valid = false;
/* 1157 */       for (Map.Entry<String, Collection<File>> modules : resolveModuleSourcePathElement(
/* 1158 */           getProject().getBaseDir(), pathElement).entrySet()) {
/* 1159 */         String moduleName = modules.getKey();
/* 1160 */         for (File srcDir : modules.getValue()) {
/* 1161 */           if (srcDir.exists()) {
/* 1162 */             valid = true;
/* 1163 */             DirectoryScanner ds = getDirectoryScanner(srcDir);
/* 1164 */             String[] files = ds.getIncludedFiles();
/* 1165 */             scanDir(srcDir, fu.resolveFile(this.destDir, moduleName), files);
/*      */           } 
/*      */         } 
/*      */       } 
/* 1169 */       if (!valid) {
/* 1170 */         throw new BuildException("modulesourcepath \"" + pathElement + "\" does not exist!", 
/*      */             
/* 1172 */             getLocation());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private String[] findSupportedFileExtensions() {
/* 1178 */     String compilerImpl = getCompiler();
/*      */ 
/*      */     
/* 1181 */     CompilerAdapter adapter = (this.nestedAdapter != null) ? this.nestedAdapter : CompilerAdapterFactory.getCompiler(compilerImpl, this, 
/* 1182 */         createCompilerClasspath());
/* 1183 */     String[] extensions = null;
/* 1184 */     if (adapter instanceof CompilerAdapterExtension)
/*      */     {
/* 1186 */       extensions = ((CompilerAdapterExtension)adapter).getSupportedFileExtensions();
/*      */     }
/*      */     
/* 1189 */     if (extensions == null) {
/* 1190 */       extensions = new String[] { "java" };
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1195 */     for (int i = 0; i < extensions.length; i++) {
/* 1196 */       if (!extensions[i].startsWith("*.")) {
/* 1197 */         extensions[i] = "*." + extensions[i];
/*      */       }
/*      */     } 
/* 1200 */     return extensions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File[] getFileList() {
/* 1208 */     return this.compileList;
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
/*      */   protected boolean isJdkCompiler(String compilerImpl) {
/* 1220 */     return CompilerAdapterFactory.isJdkCompiler(compilerImpl);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getSystemJavac() {
/* 1227 */     return JavaEnvUtils.getJdkExecutable("javac");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCompiler(String compiler) {
/* 1236 */     this.facade.setImplementation(compiler);
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
/*      */   public String getCompiler() {
/* 1254 */     String compilerImpl = getCompilerVersion();
/* 1255 */     if (this.fork) {
/* 1256 */       if (isJdkCompiler(compilerImpl)) {
/* 1257 */         compilerImpl = "extJavac";
/*      */       } else {
/* 1259 */         log("Since compiler setting isn't classic or modern, ignoring fork setting.", 1);
/*      */       } 
/*      */     }
/*      */     
/* 1263 */     return compilerImpl;
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
/*      */   public String getCompilerVersion() {
/* 1281 */     this.facade.setMagicValue(getProject().getProperty("build.compiler"));
/* 1282 */     return this.facade.getImplementation();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkParameters() throws BuildException {
/* 1293 */     if (hasPath(this.src)) {
/* 1294 */       if (hasPath(this.moduleSourcepath)) {
/* 1295 */         throw new BuildException("modulesourcepath cannot be combined with srcdir attribute!", 
/* 1296 */             getLocation());
/*      */       }
/* 1298 */     } else if (hasPath(this.moduleSourcepath)) {
/* 1299 */       if (hasPath(this.src) || hasPath(this.compileSourcepath)) {
/* 1300 */         throw new BuildException("modulesourcepath cannot be combined with srcdir or sourcepath !", 
/* 1301 */             getLocation());
/*      */       }
/* 1303 */       if (this.destDir == null) {
/* 1304 */         throw new BuildException("modulesourcepath requires destdir attribute to be set!", 
/* 1305 */             getLocation());
/*      */       }
/*      */     } else {
/* 1308 */       throw new BuildException("either srcdir or modulesourcepath attribute must be set!", 
/* 1309 */           getLocation());
/*      */     } 
/*      */     
/* 1312 */     if (this.destDir != null && !this.destDir.isDirectory()) {
/* 1313 */       throw new BuildException("destination directory \"" + this.destDir + "\" does not exist or is not a directory", 
/*      */           
/* 1315 */           getLocation());
/*      */     }
/* 1317 */     if (this.includeAntRuntime == null && getProject().getProperty("build.sysclasspath") == null) {
/* 1318 */       log(getLocation() + "warning: 'includeantruntime' was not set, defaulting to " + "build.sysclasspath" + "=last; set to false for repeatable builds", 1);
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
/*      */   protected void compile() {
/* 1330 */     String compilerImpl = getCompiler();
/*      */     
/* 1332 */     if (this.compileList.length > 0) {
/* 1333 */       log("Compiling " + this.compileList.length + " source file" + (
/* 1334 */           (this.compileList.length == 1) ? "" : "s") + (
/* 1335 */           (this.destDir != null) ? (" to " + this.destDir) : ""));
/*      */       
/* 1337 */       if (this.listFiles) {
/* 1338 */         for (File element : this.compileList) {
/* 1339 */           log(element.getAbsolutePath());
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1345 */       CompilerAdapter adapter = (this.nestedAdapter != null) ? this.nestedAdapter : CompilerAdapterFactory.getCompiler(compilerImpl, this, 
/* 1346 */           createCompilerClasspath());
/*      */ 
/*      */       
/* 1349 */       adapter.setJavac(this);
/*      */ 
/*      */       
/* 1352 */       if (adapter.execute()) {
/*      */         
/* 1354 */         if (this.createMissingPackageInfoClass) {
/*      */           try {
/* 1356 */             generateMissingPackageInfoClasses((this.destDir != null) ? 
/* 1357 */                 this.destDir : 
/* 1358 */                 getProject()
/* 1359 */                 .resolveFile(this.src.list()[0]));
/* 1360 */           } catch (IOException x) {
/*      */             
/* 1362 */             throw new BuildException(x, getLocation());
/*      */           } 
/*      */         }
/*      */       } else {
/*      */         
/* 1367 */         this.taskSuccess = false;
/* 1368 */         if (this.errorProperty != null) {
/* 1369 */           getProject().setNewProperty(this.errorProperty, "true");
/*      */         }
/*      */         
/* 1372 */         if (this.failOnError) {
/* 1373 */           throw new BuildException("Compile failed; see the compiler error output for details.", getLocation());
/*      */         }
/* 1375 */         log("Compile failed; see the compiler error output for details.", 0);
/*      */       } 
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
/*      */   public class ImplementationSpecificArgument
/*      */     extends org.apache.tools.ant.util.facade.ImplementationSpecificArgument
/*      */   {
/*      */     public void setCompiler(String impl) {
/* 1392 */       setImplementation(impl);
/*      */     }
/*      */   }
/*      */   
/*      */   private void lookForPackageInfos(File srcDir, File[] newFiles) {
/* 1397 */     for (File f : newFiles) {
/* 1398 */       if ("package-info.java".equals(f.getName())) {
/*      */ 
/*      */ 
/*      */         
/* 1402 */         String path = FILE_UTILS.removeLeadingPath(srcDir, f).replace(File.separatorChar, '/');
/* 1403 */         String suffix = "/package-info.java";
/* 1404 */         if (!path.endsWith("/package-info.java")) {
/* 1405 */           log("anomalous package-info.java path: " + path, 1);
/*      */         } else {
/*      */           
/* 1408 */           String pkg = path.substring(0, path.length() - "/package-info.java".length());
/* 1409 */           this.packageInfos.put(pkg, Long.valueOf(f.lastModified()));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateMissingPackageInfoClasses(File dest) throws IOException {
/* 1419 */     for (Map.Entry<String, Long> entry : this.packageInfos.entrySet()) {
/* 1420 */       String pkg = entry.getKey();
/* 1421 */       Long sourceLastMod = entry.getValue();
/* 1422 */       File pkgBinDir = new File(dest, pkg.replace('/', File.separatorChar));
/* 1423 */       pkgBinDir.mkdirs();
/* 1424 */       File pkgInfoClass = new File(pkgBinDir, "package-info.class");
/* 1425 */       if (pkgInfoClass.isFile() && pkgInfoClass.lastModified() >= sourceLastMod.longValue()) {
/*      */         continue;
/*      */       }
/* 1428 */       log("Creating empty " + pkgInfoClass);
/* 1429 */       OutputStream os = Files.newOutputStream(pkgInfoClass.toPath(), new java.nio.file.OpenOption[0]); try {
/* 1430 */         os.write(PACKAGE_INFO_CLASS_HEADER);
/* 1431 */         byte[] name = pkg.getBytes(StandardCharsets.UTF_8);
/* 1432 */         int length = name.length + 13;
/* 1433 */         os.write((byte)length / 256);
/* 1434 */         os.write((byte)length % 256);
/* 1435 */         os.write(name);
/* 1436 */         os.write(PACKAGE_INFO_CLASS_FOOTER);
/* 1437 */         if (os != null) os.close(); 
/*      */       } catch (Throwable throwable) {
/*      */         if (os != null)
/*      */           try {
/*      */             os.close();
/*      */           } catch (Throwable throwable1) {
/*      */             throwable.addSuppressed(throwable1);
/*      */           }  
/*      */         throw throwable;
/*      */       } 
/*      */     }  } private static boolean hasPath(Path path) {
/* 1448 */     return (path != null && !path.isEmpty());
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
/*      */   private static Map<String, Collection<File>> resolveModuleSourcePathElement(File projectDir, String element) {
/* 1462 */     Map<String, Collection<File>> result = new TreeMap<>();
/* 1463 */     for (CharSequence resolvedElement : expandGroups(element)) {
/* 1464 */       findModules(projectDir, resolvedElement.toString(), result);
/*      */     }
/* 1466 */     return result;
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
/*      */   private static Collection<? extends CharSequence> expandGroups(CharSequence element) {
/* 1482 */     List<StringBuilder> result = new ArrayList<>();
/* 1483 */     result.add(new StringBuilder());
/* 1484 */     StringBuilder resolved = new StringBuilder();
/* 1485 */     for (int i = 0; i < element.length(); i++) {
/* 1486 */       int end; Collection<? extends CharSequence> parts; List<StringBuilder> oldRes; char c = element.charAt(i);
/* 1487 */       switch (c) {
/*      */         case '{':
/* 1489 */           end = getGroupEndIndex(element, i);
/* 1490 */           if (end < 0) {
/* 1491 */             throw new BuildException(String.format("Unclosed group %s, starting at: %d", new Object[] { element, 
/*      */ 
/*      */                     
/* 1494 */                     Integer.valueOf(i) }));
/*      */           }
/* 1496 */           parts = resolveGroup(element.subSequence(i + 1, end));
/* 1497 */           switch (parts.size()) {
/*      */             case 0:
/*      */               break;
/*      */             case 1:
/* 1501 */               resolved.append(parts.iterator().next());
/*      */               break;
/*      */             default:
/* 1504 */               oldRes = result;
/* 1505 */               result = new ArrayList<>(oldRes.size() * parts.size());
/* 1506 */               for (CharSequence part : parts) {
/* 1507 */                 for (CharSequence prefix : oldRes) {
/* 1508 */                   result.add((new StringBuilder(prefix)).append(resolved).append(part));
/*      */                 }
/*      */               } 
/* 1511 */               resolved = new StringBuilder(); break;
/*      */           } 
/* 1513 */           i = end;
/*      */           break;
/*      */         default:
/* 1516 */           resolved.append(c); break;
/*      */       } 
/*      */     } 
/* 1519 */     for (StringBuilder prefix : result) {
/* 1520 */       prefix.append(resolved);
/*      */     }
/* 1522 */     return (Collection)result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Collection<? extends CharSequence> resolveGroup(CharSequence group) {
/* 1532 */     Collection<CharSequence> result = new ArrayList<>();
/* 1533 */     int start = 0;
/* 1534 */     int depth = 0;
/* 1535 */     for (int i = 0; i < group.length(); i++) {
/* 1536 */       char c = group.charAt(i);
/* 1537 */       switch (c) {
/*      */         case '{':
/* 1539 */           depth++;
/*      */           break;
/*      */         case '}':
/* 1542 */           depth--;
/*      */           break;
/*      */         case ',':
/* 1545 */           if (depth == 0) {
/* 1546 */             result.addAll(expandGroups(group.subSequence(start, i)));
/* 1547 */             start = i + 1;
/*      */           } 
/*      */           break;
/*      */       } 
/*      */     } 
/* 1552 */     result.addAll(expandGroups(group.subSequence(start, group.length())));
/* 1553 */     return result;
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
/*      */   private static int getGroupEndIndex(CharSequence element, int start) {
/* 1566 */     int depth = 0;
/* 1567 */     for (int i = start; i < element.length(); i++) {
/* 1568 */       char c = element.charAt(i);
/* 1569 */       switch (c) {
/*      */         case '{':
/* 1571 */           depth++;
/*      */           break;
/*      */         case '}':
/* 1574 */           depth--;
/* 1575 */           if (depth == 0) {
/* 1576 */             return i;
/*      */           }
/*      */           break;
/*      */       } 
/*      */     } 
/* 1581 */     return -1;
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
/*      */   private static void findModules(File root, String pattern, Map<String, Collection<File>> collector) {
/* 1597 */     pattern = pattern.replace('/', File.separatorChar).replace('\\', File.separatorChar);
/* 1598 */     int startIndex = pattern.indexOf("*");
/* 1599 */     if (startIndex == -1) {
/* 1600 */       findModules(root, pattern, (String)null, collector);
/*      */       return;
/*      */     } 
/* 1603 */     if (startIndex == 0) {
/* 1604 */       throw new BuildException("The modulesourcepath entry must be a folder.");
/*      */     }
/* 1606 */     int endIndex = startIndex + "*".length();
/* 1607 */     if (pattern.charAt(startIndex - 1) != File.separatorChar) {
/* 1608 */       throw new BuildException("The module mark must be preceded by separator");
/*      */     }
/* 1610 */     if (endIndex < pattern.length() && pattern.charAt(endIndex) != File.separatorChar) {
/* 1611 */       throw new BuildException("The module mark must be followed by separator");
/*      */     }
/* 1613 */     if (pattern.indexOf("*", endIndex) != -1) {
/* 1614 */       throw new BuildException("The modulesourcepath entry must contain at most one module mark");
/*      */     }
/* 1616 */     String pathToModule = pattern.substring(0, startIndex);
/*      */     
/* 1618 */     String pathInModule = (endIndex == pattern.length()) ? null : pattern.substring(endIndex + 1);
/* 1619 */     findModules(root, pathToModule, pathInModule, collector);
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
/*      */   private static void findModules(File root, String pathToModule, String pathInModule, Map<String, Collection<File>> collector) {
/* 1635 */     File f = FileUtils.getFileUtils().resolveFile(root, pathToModule);
/* 1636 */     if (!f.isDirectory()) {
/*      */       return;
/*      */     }
/* 1639 */     for (File module : f.listFiles(File::isDirectory)) {
/* 1640 */       String moduleName = module.getName();
/*      */       
/* 1642 */       File moduleSourceRoot = (pathInModule == null) ? module : new File(module, pathInModule);
/* 1643 */       Collection<File> moduleRoots = collector.computeIfAbsent(moduleName, k -> new ArrayList());
/* 1644 */       moduleRoots.add(moduleSourceRoot);
/*      */     } 
/*      */   }
/*      */   
/* 1648 */   private static final byte[] PACKAGE_INFO_CLASS_HEADER = new byte[] { -54, -2, -70, -66, 0, 0, 0, 49, 0, 7, 7, 0, 5, 7, 0, 6, 1, 0, 10, 83, 111, 117, 114, 99, 101, 70, 105, 108, 101, 1, 0, 17, 112, 97, 99, 107, 97, 103, 101, 45, 105, 110, 102, 111, 46, 106, 97, 118, 97, 1 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1656 */   private static final byte[] PACKAGE_INFO_CLASS_FOOTER = new byte[] { 47, 112, 97, 99, 107, 97, 103, 101, 45, 105, 110, 102, 111, 1, 0, 16, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 2, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 3, 0, 0, 0, 2, 0, 4 };
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Javac.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */