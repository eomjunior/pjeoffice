/*      */ package org.apache.tools.ant.taskdefs.modules;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.nio.file.Files;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.spi.ToolProvider;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.ProjectComponent;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.FileSet;
/*      */ import org.apache.tools.ant.types.ModuleVersion;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.Reference;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.ResourceFactory;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.types.resources.Union;
/*      */ import org.apache.tools.ant.util.FileNameMapper;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.MergingMapper;
/*      */ import org.apache.tools.ant.util.ResourceUtils;
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
/*      */ public class Jmod
/*      */   extends Task
/*      */ {
/*      */   private File jmodFile;
/*      */   private Path classpath;
/*      */   private Path modulePath;
/*      */   private Path commandPath;
/*      */   private Path configPath;
/*      */   private Path headerPath;
/*      */   private Path legalPath;
/*      */   private Path nativeLibPath;
/*      */   private Path manPath;
/*      */   private String version;
/*      */   private ModuleVersion moduleVersion;
/*      */   private String mainClass;
/*      */   private String platform;
/*      */   private String hashModulesPattern;
/*      */   private boolean resolveByDefault = true;
/*  249 */   private final List<ResolutionWarningSpec> moduleWarnings = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getDestFile() {
/*  260 */     return this.jmodFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestFile(File file) {
/*  270 */     this.jmodFile = file;
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
/*      */   public Path createClasspath() {
/*  282 */     if (this.classpath == null) {
/*  283 */       this.classpath = new Path(getProject());
/*      */     }
/*  285 */     return this.classpath.createPath();
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
/*      */   public Path getClasspath() {
/*  297 */     return this.classpath;
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
/*      */   public void setClasspath(Path path) {
/*  311 */     if (this.classpath == null) {
/*  312 */       this.classpath = path;
/*      */     } else {
/*  314 */       this.classpath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspathRef(Reference ref) {
/*  325 */     createClasspath().setRefid(ref);
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
/*      */   public Path createModulePath() {
/*  338 */     if (this.modulePath == null) {
/*  339 */       this.modulePath = new Path(getProject());
/*      */     }
/*  341 */     return this.modulePath.createPath();
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
/*      */   public Path getModulePath() {
/*  355 */     return this.modulePath;
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
/*      */   public void setModulePath(Path path) {
/*  369 */     if (this.modulePath == null) {
/*  370 */       this.modulePath = path;
/*      */     } else {
/*  372 */       this.modulePath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulePathRef(Reference ref) {
/*  383 */     createModulePath().setRefid(ref);
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
/*      */   public Path createCommandPath() {
/*  395 */     if (this.commandPath == null) {
/*  396 */       this.commandPath = new Path(getProject());
/*      */     }
/*  398 */     return this.commandPath.createPath();
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
/*      */   public Path getCommandPath() {
/*  410 */     return this.commandPath;
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
/*      */   public void setCommandPath(Path path) {
/*  422 */     if (this.commandPath == null) {
/*  423 */       this.commandPath = path;
/*      */     } else {
/*  425 */       this.commandPath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCommandPathRef(Reference ref) {
/*  436 */     createCommandPath().setRefid(ref);
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
/*      */   public Path createConfigPath() {
/*  448 */     if (this.configPath == null) {
/*  449 */       this.configPath = new Path(getProject());
/*      */     }
/*  451 */     return this.configPath.createPath();
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
/*      */   public Path getConfigPath() {
/*  463 */     return this.configPath;
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
/*      */   public void setConfigPath(Path path) {
/*  475 */     if (this.configPath == null) {
/*  476 */       this.configPath = path;
/*      */     } else {
/*  478 */       this.configPath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConfigPathRef(Reference ref) {
/*  489 */     createConfigPath().setRefid(ref);
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
/*      */   public Path createHeaderPath() {
/*  502 */     if (this.headerPath == null) {
/*  503 */       this.headerPath = new Path(getProject());
/*      */     }
/*  505 */     return this.headerPath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getHeaderPath() {
/*  516 */     return this.headerPath;
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
/*      */   public void setHeaderPath(Path path) {
/*  529 */     if (this.headerPath == null) {
/*  530 */       this.headerPath = path;
/*      */     } else {
/*  532 */       this.headerPath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHeaderPathRef(Reference ref) {
/*  543 */     createHeaderPath().setRefid(ref);
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
/*      */   public Path createLegalPath() {
/*  555 */     if (this.legalPath == null) {
/*  556 */       this.legalPath = new Path(getProject());
/*      */     }
/*  558 */     return this.legalPath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getLegalPath() {
/*  568 */     return this.legalPath;
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
/*      */   public void setLegalPath(Path path) {
/*  580 */     if (this.legalPath == null) {
/*  581 */       this.legalPath = path;
/*      */     } else {
/*  583 */       this.legalPath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLegalPathRef(Reference ref) {
/*  594 */     createLegalPath().setRefid(ref);
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
/*      */   public Path createNativeLibPath() {
/*  606 */     if (this.nativeLibPath == null) {
/*  607 */       this.nativeLibPath = new Path(getProject());
/*      */     }
/*  609 */     return this.nativeLibPath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getNativeLibPath() {
/*  619 */     return this.nativeLibPath;
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
/*      */   public void setNativeLibPath(Path path) {
/*  631 */     if (this.nativeLibPath == null) {
/*  632 */       this.nativeLibPath = path;
/*      */     } else {
/*  634 */       this.nativeLibPath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNativeLibPathRef(Reference ref) {
/*  645 */     createNativeLibPath().setRefid(ref);
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
/*      */   public Path createManPath() {
/*  658 */     if (this.manPath == null) {
/*  659 */       this.manPath = new Path(getProject());
/*      */     }
/*  661 */     return this.manPath.createPath();
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
/*      */   public Path getManPath() {
/*  673 */     return this.manPath;
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
/*      */   public void setManPath(Path path) {
/*  687 */     if (this.manPath == null) {
/*  688 */       this.manPath = path;
/*      */     } else {
/*  690 */       this.manPath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setManPathRef(Reference ref) {
/*  701 */     createManPath().setRefid(ref);
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
/*      */   public ModuleVersion createVersion() {
/*  713 */     if (this.moduleVersion != null) {
/*  714 */       throw new BuildException("No more than one <moduleVersion> element is allowed.", 
/*      */           
/*  716 */           getLocation());
/*      */     }
/*  718 */     this.moduleVersion = new ModuleVersion();
/*  719 */     return this.moduleVersion;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getVersion() {
/*  730 */     return this.version;
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
/*      */   public void setVersion(String version) {
/*  742 */     this.version = version;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getMainClass() {
/*  752 */     return this.mainClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMainClass(String className) {
/*  762 */     this.mainClass = className;
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
/*      */   public String getPlatform() {
/*  775 */     return this.platform;
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
/*      */   public void setPlatform(String platform) {
/*  791 */     this.platform = platform;
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
/*      */   public String getHashModulesPattern() {
/*  803 */     return this.hashModulesPattern;
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
/*      */   public void setHashModulesPattern(String pattern) {
/*  815 */     this.hashModulesPattern = pattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getResolveByDefault() {
/*  825 */     return this.resolveByDefault;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setResolveByDefault(boolean resolve) {
/*  835 */     this.resolveByDefault = resolve;
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
/*      */   public ResolutionWarningSpec createModuleWarning() {
/*  847 */     ResolutionWarningSpec warningSpec = new ResolutionWarningSpec();
/*  848 */     this.moduleWarnings.add(warningSpec);
/*  849 */     return warningSpec;
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
/*      */   public void setModuleWarnings(String warningList) {
/*  864 */     for (String warning : warningList.split(",")) {
/*  865 */       this.moduleWarnings.add(new ResolutionWarningSpec(warning));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ResolutionWarningReason
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public static final String DEPRECATED = "deprecated";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String LEAVING = "leaving";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final String INCUBATING = "incubating";
/*      */ 
/*      */ 
/*      */     
/*      */     private static final Map<String, String> VALUES_TO_OPTIONS;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/*  896 */       Map<String, String> map = new LinkedHashMap<>();
/*  897 */       map.put("deprecated", "deprecated");
/*  898 */       map.put("leaving", "deprecated-for-removal");
/*  899 */       map.put("incubating", "incubating");
/*      */       
/*  901 */       VALUES_TO_OPTIONS = Collections.unmodifiableMap(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/*  906 */       return (String[])VALUES_TO_OPTIONS.keySet().toArray((Object[])new String[0]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String toCommandLineOption() {
/*  916 */       return VALUES_TO_OPTIONS.get(getValue());
/*      */     }
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
/*      */     public static ResolutionWarningReason valueOf(String s) {
/*  931 */       return 
/*  932 */         (ResolutionWarningReason)getInstance(ResolutionWarningReason.class, s);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class ResolutionWarningSpec
/*      */   {
/*      */     private Jmod.ResolutionWarningReason reason;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ResolutionWarningSpec() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ResolutionWarningSpec(String reason) {
/*  961 */       setReason(Jmod.ResolutionWarningReason.valueOf(reason));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Jmod.ResolutionWarningReason getReason() {
/*  970 */       return this.reason;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setReason(Jmod.ResolutionWarningReason reason) {
/*  979 */       this.reason = reason;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void validate() {
/*  988 */       if (this.reason == null) {
/*  989 */         throw new BuildException("reason attribute is required", Jmod.this
/*  990 */             .getLocation());
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
/*      */   
/*      */   private static boolean isRegularFile(Resource resource) {
/* 1005 */     return (resource.isExists() && !resource.isDirectory());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkDirPaths() {
/* 1016 */     if (this.modulePath != null && this.modulePath
/* 1017 */       .stream().anyMatch(Jmod::isRegularFile))
/*      */     {
/* 1019 */       throw new BuildException("ModulePath must contain only directories.", 
/* 1020 */           getLocation());
/*      */     }
/* 1022 */     if (this.commandPath != null && this.commandPath
/* 1023 */       .stream().anyMatch(Jmod::isRegularFile))
/*      */     {
/* 1025 */       throw new BuildException("CommandPath must contain only directories.", 
/* 1026 */           getLocation());
/*      */     }
/* 1028 */     if (this.configPath != null && this.configPath
/* 1029 */       .stream().anyMatch(Jmod::isRegularFile))
/*      */     {
/* 1031 */       throw new BuildException("ConfigPath must contain only directories.", 
/* 1032 */           getLocation());
/*      */     }
/* 1034 */     if (this.headerPath != null && this.headerPath
/* 1035 */       .stream().anyMatch(Jmod::isRegularFile))
/*      */     {
/* 1037 */       throw new BuildException("HeaderPath must contain only directories.", 
/* 1038 */           getLocation());
/*      */     }
/* 1040 */     if (this.legalPath != null && this.legalPath
/* 1041 */       .stream().anyMatch(Jmod::isRegularFile))
/*      */     {
/* 1043 */       throw new BuildException("LegalPath must contain only directories.", 
/* 1044 */           getLocation());
/*      */     }
/* 1046 */     if (this.nativeLibPath != null && this.nativeLibPath
/* 1047 */       .stream().anyMatch(Jmod::isRegularFile))
/*      */     {
/* 1049 */       throw new BuildException("NativeLibPath must contain only directories.", 
/* 1050 */           getLocation());
/*      */     }
/* 1052 */     if (this.manPath != null && this.manPath
/* 1053 */       .stream().anyMatch(Jmod::isRegularFile))
/*      */     {
/* 1055 */       throw new BuildException("ManPath must contain only directories.", 
/* 1056 */           getLocation());
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
/*      */   public void execute() throws BuildException {
/*      */     int exitCode;
/* 1077 */     if (this.jmodFile == null) {
/* 1078 */       throw new BuildException("Destination file is required.", 
/* 1079 */           getLocation());
/*      */     }
/*      */     
/* 1082 */     if (this.classpath == null) {
/* 1083 */       throw new BuildException("Classpath is required.", 
/* 1084 */           getLocation());
/*      */     }
/*      */     
/* 1087 */     if (this.classpath.stream().noneMatch(Resource::isExists)) {
/* 1088 */       throw new BuildException("Classpath must contain at least one entry which exists.", 
/*      */           
/* 1090 */           getLocation());
/*      */     }
/*      */     
/* 1093 */     if (this.version != null && this.moduleVersion != null) {
/* 1094 */       throw new BuildException("version attribute and nested <version> element cannot both be present.", 
/*      */ 
/*      */           
/* 1097 */           getLocation());
/*      */     }
/*      */     
/* 1100 */     if (this.hashModulesPattern != null && !this.hashModulesPattern.isEmpty() && this.modulePath == null)
/*      */     {
/*      */       
/* 1103 */       throw new BuildException("hashModulesPattern requires a module path, since it will generate hashes of the other modules which depend on the module being created.", 
/*      */ 
/*      */ 
/*      */           
/* 1107 */           getLocation());
/*      */     }
/*      */     
/* 1110 */     checkDirPaths();
/*      */     
/* 1112 */     Path[] dependentPaths = { this.classpath, this.modulePath, this.commandPath, this.configPath, this.headerPath, this.legalPath, this.nativeLibPath, this.manPath };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1122 */     Union allResources = new Union(getProject());
/* 1123 */     for (Path path : dependentPaths) {
/* 1124 */       if (path != null) {
/* 1125 */         for (String entry : path.list()) {
/* 1126 */           File entryFile = new File(entry);
/* 1127 */           if (entryFile.isDirectory()) {
/* 1128 */             log("Will compare timestamp of all files in \"" + entryFile + "\" with timestamp of " + this.jmodFile, 3);
/*      */ 
/*      */             
/* 1131 */             FileSet fileSet = new FileSet();
/* 1132 */             fileSet.setDir(entryFile);
/* 1133 */             allResources.add((ResourceCollection)fileSet);
/*      */           } else {
/* 1135 */             log("Will compare timestamp of \"" + entryFile + "\" with timestamp of " + this.jmodFile, 3);
/*      */ 
/*      */             
/* 1138 */             allResources.add((ResourceCollection)new FileResource(entryFile));
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1145 */     ResourceCollection outOfDate = ResourceUtils.selectOutOfDateSources((ProjectComponent)this, (ResourceCollection)allResources, (FileNameMapper)new MergingMapper(this.jmodFile
/* 1146 */           .toString()), (ResourceFactory)
/* 1147 */         getProject(), 
/* 1148 */         FileUtils.getFileUtils().getFileTimestampGranularity());
/*      */     
/* 1150 */     if (outOfDate.isEmpty()) {
/* 1151 */       log("Skipping jmod creation, since \"" + this.jmodFile + "\" is already newer than all files in paths.", 3);
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1157 */     Collection<String> args = buildJmodArgs();
/*      */     
/*      */     try {
/* 1160 */       log("Deleting " + this.jmodFile + " if it exists.", 3);
/* 1161 */       Files.deleteIfExists(this.jmodFile.toPath());
/* 1162 */     } catch (IOException e) {
/* 1163 */       throw new BuildException("Could not remove old file \"" + this.jmodFile + "\": " + e, e, 
/*      */           
/* 1165 */           getLocation());
/*      */     } 
/*      */     
/* 1168 */     ToolProvider jmod = ToolProvider.findFirst("jmod").<Throwable>orElseThrow(() -> new BuildException("jmod tool not found in JDK.", getLocation()));
/*      */ 
/*      */ 
/*      */     
/* 1172 */     log("Executing: jmod " + String.join(" ", (Iterable)args), 3);
/*      */     
/* 1174 */     ByteArrayOutputStream stdout = new ByteArrayOutputStream();
/* 1175 */     ByteArrayOutputStream stderr = new ByteArrayOutputStream();
/*      */ 
/*      */     
/* 1178 */     PrintStream out = new PrintStream(stdout); 
/* 1179 */     try { PrintStream err = new PrintStream(stderr);
/*      */       
/* 1181 */       try { exitCode = jmod.run(out, err, args.<String>toArray(new String[0]));
/* 1182 */         err.close(); } catch (Throwable throwable) { try { err.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */        throw throwable; }
/* 1184 */      if (exitCode != 0) {
/* 1185 */       StringBuilder message = new StringBuilder();
/* 1186 */       message.append("jmod failed (exit code ").append(exitCode).append(")");
/* 1187 */       if (stdout.size() > 0) {
/* 1188 */         message.append(", output is: ").append(stdout);
/*      */       }
/* 1190 */       if (stderr.size() > 0) {
/* 1191 */         message.append(", error output is: ").append(stderr);
/*      */       }
/*      */       
/* 1194 */       throw new BuildException(message.toString(), getLocation());
/*      */     } 
/*      */     
/* 1197 */     log("Created " + this.jmodFile.getAbsolutePath(), 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Collection<String> buildJmodArgs() {
/* 1207 */     Collection<String> args = new ArrayList<>();
/*      */     
/* 1209 */     args.add("create");
/*      */     
/* 1211 */     args.add("--class-path");
/* 1212 */     args.add(this.classpath.toString());
/*      */ 
/*      */ 
/*      */     
/* 1216 */     if (this.modulePath != null && !this.modulePath.isEmpty()) {
/* 1217 */       args.add("--module-path");
/* 1218 */       args.add(this.modulePath.toString());
/*      */     } 
/* 1220 */     if (this.commandPath != null && !this.commandPath.isEmpty()) {
/* 1221 */       args.add("--cmds");
/* 1222 */       args.add(this.commandPath.toString());
/*      */     } 
/* 1224 */     if (this.configPath != null && !this.configPath.isEmpty()) {
/* 1225 */       args.add("--config");
/* 1226 */       args.add(this.configPath.toString());
/*      */     } 
/* 1228 */     if (this.headerPath != null && !this.headerPath.isEmpty()) {
/* 1229 */       args.add("--header-files");
/* 1230 */       args.add(this.headerPath.toString());
/*      */     } 
/* 1232 */     if (this.legalPath != null && !this.legalPath.isEmpty()) {
/* 1233 */       args.add("--legal-notices");
/* 1234 */       args.add(this.legalPath.toString());
/*      */     } 
/* 1236 */     if (this.nativeLibPath != null && !this.nativeLibPath.isEmpty()) {
/* 1237 */       args.add("--libs");
/* 1238 */       args.add(this.nativeLibPath.toString());
/*      */     } 
/* 1240 */     if (this.manPath != null && !this.manPath.isEmpty()) {
/* 1241 */       args.add("--man-pages");
/* 1242 */       args.add(this.manPath.toString());
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1248 */     String versionStr = (this.moduleVersion != null) ? this.moduleVersion.toModuleVersionString() : this.version;
/* 1249 */     if (versionStr != null && !versionStr.isEmpty()) {
/* 1250 */       args.add("--module-version");
/* 1251 */       args.add(versionStr);
/*      */     } 
/*      */     
/* 1254 */     if (this.mainClass != null && !this.mainClass.isEmpty()) {
/* 1255 */       args.add("--main-class");
/* 1256 */       args.add(this.mainClass);
/*      */     } 
/* 1258 */     if (this.platform != null && !this.platform.isEmpty()) {
/* 1259 */       args.add("--target-platform");
/* 1260 */       args.add(this.platform);
/*      */     } 
/* 1262 */     if (this.hashModulesPattern != null && !this.hashModulesPattern.isEmpty()) {
/* 1263 */       args.add("--hash-modules");
/* 1264 */       args.add(this.hashModulesPattern);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1269 */     if (!this.resolveByDefault) {
/* 1270 */       args.add("--do-not-resolve-by-default");
/*      */     }
/* 1272 */     for (ResolutionWarningSpec moduleWarning : this.moduleWarnings) {
/* 1273 */       moduleWarning.validate();
/* 1274 */       args.add("--warn-if-resolved");
/* 1275 */       args.add(moduleWarning.getReason().toCommandLineOption());
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1280 */     args.add(this.jmodFile.toString());
/*      */     
/* 1282 */     return args;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/modules/Jmod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */