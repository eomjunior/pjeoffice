/*      */ package org.apache.tools.ant.taskdefs.modules;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.FileVisitResult;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.SimpleFileVisitor;
/*      */ import java.nio.file.attribute.BasicFileAttributes;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Properties;
/*      */ import java.util.spi.ToolProvider;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.ProjectComponent;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.LogLevel;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.Reference;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.ResourceFactory;
/*      */ import org.apache.tools.ant.util.CompositeMapper;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Link
/*      */   extends Task
/*      */ {
/*      */   private static final String INVALID_LAUNCHER_STRING = "Launcher command must take the form name=module or name=module/mainclass";
/*      */   private Path modulePath;
/*  249 */   private final List<ModuleSpec> modules = new ArrayList<>();
/*      */ 
/*      */   
/*  252 */   private final List<ModuleSpec> observableModules = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  258 */   private final List<Launcher> launchers = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  264 */   private final List<LocaleSpec> locales = new ArrayList<>();
/*      */ 
/*      */   
/*  267 */   private final List<PatternListEntry> ordering = new ArrayList<>();
/*      */ 
/*      */   
/*  270 */   private final List<PatternListEntry> excludedFiles = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  275 */   private final List<PatternListEntry> excludedResources = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean bindServices;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean ignoreSigning;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean includeHeaders = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean includeManPages = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean includeNativeCommands = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean debug = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private LogLevel verboseLevel;
/*      */ 
/*      */ 
/*      */   
/*      */   private File outputDir;
/*      */ 
/*      */ 
/*      */   
/*      */   private Endianness endianness;
/*      */ 
/*      */ 
/*      */   
/*      */   private CompressionLevel compressionLevel;
/*      */ 
/*      */ 
/*      */   
/*      */   private Compression compression;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDuplicateLegal;
/*      */ 
/*      */ 
/*      */   
/*      */   private VMType vmType;
/*      */ 
/*      */ 
/*      */   
/*  338 */   private final List<ReleaseInfo> releaseInfo = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createModulePath() {
/*  348 */     if (this.modulePath == null) {
/*  349 */       this.modulePath = new Path(getProject());
/*      */     }
/*  351 */     return this.modulePath.createPath();
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
/*      */   public Path getModulePath() {
/*  363 */     return this.modulePath;
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
/*  377 */     if (this.modulePath == null) {
/*  378 */       this.modulePath = path;
/*      */     } else {
/*  380 */       this.modulePath.append(path);
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
/*      */   public void setModulePathRef(Reference ref) {
/*  393 */     createModulePath().setRefid(ref);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ModuleSpec createModule() {
/*  404 */     ModuleSpec module = new ModuleSpec();
/*  405 */     this.modules.add(module);
/*  406 */     return module;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModules(String moduleList) {
/*  415 */     for (String moduleName : moduleList.split(",")) {
/*  416 */       this.modules.add(new ModuleSpec(moduleName));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ModuleSpec createObservableModule() {
/*  427 */     ModuleSpec module = new ModuleSpec();
/*  428 */     this.observableModules.add(module);
/*  429 */     return module;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObservableModules(String moduleList) {
/*  438 */     for (String moduleName : moduleList.split(",")) {
/*  439 */       this.observableModules.add(new ModuleSpec(moduleName));
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
/*      */   public Launcher createLauncher() {
/*  452 */     Launcher command = new Launcher();
/*  453 */     this.launchers.add(command);
/*  454 */     return command;
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
/*      */   public void setLaunchers(String launcherList) {
/*  466 */     for (String launcherSpec : launcherList.split(",")) {
/*  467 */       this.launchers.add(new Launcher(launcherSpec));
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
/*      */   public LocaleSpec createLocale() {
/*  479 */     LocaleSpec locale = new LocaleSpec();
/*  480 */     this.locales.add(locale);
/*  481 */     return locale;
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
/*      */   public void setLocales(String localeList) {
/*  493 */     for (String localeName : localeList.split(",")) {
/*  494 */       this.locales.add(new LocaleSpec(localeName));
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
/*      */   public PatternListEntry createExcludeFiles() {
/*  507 */     PatternListEntry entry = new PatternListEntry();
/*  508 */     this.excludedFiles.add(entry);
/*  509 */     return entry;
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
/*      */   public void setExcludeFiles(String patternList) {
/*  521 */     for (String pattern : patternList.split(",")) {
/*  522 */       this.excludedFiles.add(new PatternListEntry(pattern));
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
/*      */   public PatternListEntry createExcludeResources() {
/*  535 */     PatternListEntry entry = new PatternListEntry();
/*  536 */     this.excludedResources.add(entry);
/*  537 */     return entry;
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
/*      */   public void setExcludeResources(String patternList) {
/*  550 */     for (String pattern : patternList.split(",")) {
/*  551 */       this.excludedResources.add(new PatternListEntry(pattern));
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
/*      */   public PatternListEntry createResourceOrder() {
/*  564 */     PatternListEntry order = new PatternListEntry();
/*  565 */     this.ordering.add(order);
/*  566 */     return order;
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
/*      */   public void setResourceOrder(String patternList) {
/*  581 */     List<PatternListEntry> orderList = new ArrayList<>();
/*      */     
/*  583 */     for (String pattern : patternList.split(",")) {
/*  584 */       orderList.add(new PatternListEntry(pattern));
/*      */     }
/*      */ 
/*      */     
/*  588 */     this.ordering.addAll(0, orderList);
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
/*      */   public boolean getBindServices() {
/*  600 */     return this.bindServices;
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
/*      */   public void setBindServices(boolean bind) {
/*  612 */     this.bindServices = bind;
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
/*      */   public boolean getIgnoreSigning() {
/*  625 */     return this.ignoreSigning;
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
/*      */   public void setIgnoreSigning(boolean ignore) {
/*  642 */     this.ignoreSigning = ignore;
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
/*      */   public boolean getIncludeHeaders() {
/*  654 */     return this.includeHeaders;
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
/*      */   public void setIncludeHeaders(boolean include) {
/*  667 */     this.includeHeaders = include;
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
/*      */   public boolean getIncludeManPages() {
/*  679 */     return this.includeManPages;
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
/*      */   public void setIncludeManPages(boolean include) {
/*  692 */     this.includeManPages = include;
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
/*      */   public boolean getIncludeNativeCommands() {
/*  704 */     return this.includeNativeCommands;
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
/*      */   public void setIncludeNativeCommands(boolean include) {
/*  717 */     this.includeNativeCommands = include;
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
/*      */   public boolean getDebug() {
/*  730 */     return this.debug;
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
/*      */   public void setDebug(boolean debug) {
/*  743 */     this.debug = debug;
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
/*      */   public LogLevel getVerboseLevel() {
/*  756 */     return this.verboseLevel;
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
/*      */   public void setVerboseLevel(LogLevel level) {
/*  769 */     this.verboseLevel = level;
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
/*      */   public File getDestDir() {
/*  781 */     return this.outputDir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestDir(File dir) {
/*  792 */     this.outputDir = dir;
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
/*      */   public CompressionLevel getCompress() {
/*  806 */     return this.compressionLevel;
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
/*      */   public void setCompress(CompressionLevel level) {
/*  820 */     this.compressionLevel = level;
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
/*      */   public Compression createCompress() {
/*  835 */     if (this.compression != null) {
/*  836 */       throw new BuildException("Only one nested compression element is permitted.", 
/*      */           
/*  838 */           getLocation());
/*      */     }
/*  840 */     this.compression = new Compression();
/*  841 */     return this.compression;
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
/*      */   public Endianness getEndianness() {
/*  854 */     return this.endianness;
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
/*      */   public void setEndianness(Endianness endianness) {
/*  868 */     this.endianness = endianness;
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
/*      */   public boolean getCheckDuplicateLegal() {
/*  882 */     return this.checkDuplicateLegal;
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
/*      */   public void setCheckDuplicateLegal(boolean check) {
/*  896 */     this.checkDuplicateLegal = check;
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
/*      */   public VMType getVmType() {
/*  908 */     return this.vmType;
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
/*      */   public void setVmType(VMType type) {
/*  920 */     this.vmType = type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReleaseInfo createReleaseInfo() {
/*  930 */     ReleaseInfo info = new ReleaseInfo();
/*  931 */     this.releaseInfo.add(info);
/*  932 */     return info;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class ModuleSpec
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ModuleSpec() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ModuleSpec(String name) {
/*  953 */       setName(name);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  962 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/*  972 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void validate() {
/*  981 */       if (this.name == null) {
/*  982 */         throw new BuildException("name is required for module.", Link.this
/*  983 */             .getLocation());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class LocaleSpec
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public LocaleSpec() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public LocaleSpec(String name) {
/* 1006 */       setName(name);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/* 1017 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/* 1028 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void validate() {
/* 1037 */       if (this.name == null) {
/* 1038 */         throw new BuildException("name is required for locale.", Link.this
/* 1039 */             .getLocation());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class PatternListEntry
/*      */   {
/*      */     private String pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private File file;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PatternListEntry() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PatternListEntry(String pattern) {
/* 1071 */       if (pattern.startsWith("@")) {
/* 1072 */         setListFile(new File(pattern.substring(1)));
/*      */       } else {
/* 1074 */         setPattern(pattern);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getPattern() {
/* 1084 */       return this.pattern;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPattern(String pattern) {
/* 1095 */       this.pattern = pattern;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public File getListFile() {
/* 1106 */       return this.file;
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
/*      */     public void setListFile(File file) {
/* 1118 */       this.file = file;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void validate() {
/* 1128 */       if ((this.pattern == null && this.file == null) || (this.pattern != null && this.file != null))
/*      */       {
/*      */         
/* 1131 */         throw new BuildException("Each entry in a pattern list must specify exactly one of pattern or file.", Link.this
/*      */             
/* 1133 */             .getLocation());
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toOptionValue() {
/* 1145 */       return (this.pattern != null) ? this.pattern : ("@" + this.file);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class Launcher
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String module;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String mainClass;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Launcher() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Launcher(String textSpec) {
/* 1187 */       Objects.requireNonNull(textSpec, "Text cannot be null");
/*      */       
/* 1189 */       int equals = textSpec.lastIndexOf('=');
/* 1190 */       if (equals < 1) {
/* 1191 */         throw new BuildException("Launcher command must take the form name=module or name=module/mainclass");
/*      */       }
/*      */       
/* 1194 */       setName(textSpec.substring(0, equals));
/*      */       
/* 1196 */       int slash = textSpec.indexOf('/', equals);
/* 1197 */       if (slash < 0) {
/* 1198 */         setModule(textSpec.substring(equals + 1));
/* 1199 */       } else if (slash > equals + 1 && slash < textSpec.length() - 1) {
/* 1200 */         setModule(textSpec.substring(equals + 1, slash));
/* 1201 */         setMainClass(textSpec.substring(slash + 1));
/*      */       } else {
/* 1203 */         throw new BuildException("Launcher command must take the form name=module or name=module/mainclass");
/*      */       } 
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
/*      */     public String getName() {
/* 1216 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/* 1227 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getModule() {
/* 1237 */       return this.module;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setModule(String module) {
/* 1247 */       this.module = module;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMainClass() {
/* 1258 */       return this.mainClass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setMainClass(String className) {
/* 1269 */       this.mainClass = className;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void validate() {
/* 1278 */       if (this.name == null || this.name.isEmpty()) {
/* 1279 */         throw new BuildException("Launcher must have a name", Link.this
/* 1280 */             .getLocation());
/*      */       }
/* 1282 */       if (this.module == null || this.module.isEmpty()) {
/* 1283 */         throw new BuildException("Launcher must have specify a module", Link.this
/* 1284 */             .getLocation());
/*      */       }
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
/*      */     public String toString() {
/* 1297 */       if (this.mainClass != null) {
/* 1298 */         return this.name + "=" + this.module + "/" + this.mainClass;
/*      */       }
/* 1300 */       return this.name + "=" + this.module;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Endianness
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public String[] getValues() {
/* 1313 */       return new String[] { "little", "big" };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class VMType
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public String[] getValues() {
/* 1327 */       return new String[] { "client", "server", "minimal", "all" };
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
/*      */   public static class CompressionLevel
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     private static final Map<String, String> KEYWORDS;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/* 1352 */       Map<String, String> map = new LinkedHashMap<>();
/* 1353 */       map.put("0", "0");
/* 1354 */       map.put("1", "1");
/* 1355 */       map.put("2", "2");
/* 1356 */       map.put("none", "0");
/* 1357 */       map.put("strings", "1");
/* 1358 */       map.put("zip", "2");
/*      */       
/* 1360 */       KEYWORDS = Collections.unmodifiableMap(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public String[] getValues() {
/* 1365 */       return (String[])KEYWORDS.keySet().toArray((Object[])new String[0]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String toCommandLineOption() {
/* 1375 */       return KEYWORDS.get(getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class Compression
/*      */   {
/*      */     private Link.CompressionLevel level;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1391 */     private final List<Link.PatternListEntry> patterns = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Link.CompressionLevel getLevel() {
/* 1399 */       return this.level;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setLevel(Link.CompressionLevel level) {
/* 1408 */       this.level = level;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Link.PatternListEntry createFiles() {
/* 1418 */       Link.PatternListEntry pattern = new Link.PatternListEntry();
/* 1419 */       this.patterns.add(pattern);
/* 1420 */       return pattern;
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
/*      */     public void setFiles(String patternList) {
/* 1433 */       this.patterns.clear();
/* 1434 */       for (String pattern : patternList.split(",")) {
/* 1435 */         this.patterns.add(new Link.PatternListEntry(pattern));
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void validate() {
/* 1446 */       if (this.level == null) {
/* 1447 */         throw new BuildException("Compression level must be specified.", Link.this
/* 1448 */             .getLocation());
/*      */       }
/* 1450 */       this.patterns.forEach(Link.PatternListEntry::validate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toCommandLineOption() {
/* 1460 */       StringBuilder option = new StringBuilder(this.level.toCommandLineOption());
/*      */       
/* 1462 */       if (!this.patterns.isEmpty()) {
/* 1463 */         String separator = ":filter=";
/* 1464 */         for (Link.PatternListEntry entry : this.patterns) {
/* 1465 */           option.append(separator).append(entry.toOptionValue());
/* 1466 */           separator = ",";
/*      */         } 
/*      */       } 
/*      */       
/* 1470 */       return option.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class ReleaseInfoKey
/*      */   {
/*      */     private String key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ReleaseInfoKey() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ReleaseInfoKey(String key) {
/* 1493 */       setKey(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getKey() {
/* 1502 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setKey(String key) {
/* 1512 */       this.key = key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void validate() {
/* 1521 */       if (this.key == null) {
/* 1522 */         throw new BuildException("Release info key must define a 'key' attribute.", Link.this
/*      */             
/* 1524 */             .getLocation());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class ReleaseInfoEntry
/*      */   {
/*      */     private String key;
/*      */ 
/*      */ 
/*      */     
/*      */     private String value;
/*      */ 
/*      */ 
/*      */     
/*      */     private File file;
/*      */ 
/*      */     
/* 1545 */     private String charset = StandardCharsets.ISO_8859_1.name();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ReleaseInfoEntry() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ReleaseInfoEntry(String key, String value) {
/* 1560 */       setKey(key);
/* 1561 */       setValue(value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getKey() {
/* 1572 */       return this.key;
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
/*      */     public void setKey(String key) {
/* 1584 */       this.key = key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getValue() {
/* 1595 */       return this.value;
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
/*      */     public void setValue(String value) {
/* 1607 */       this.value = value;
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
/*      */     public File getFile() {
/* 1620 */       return this.file;
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
/*      */     public void setFile(File file) {
/* 1633 */       this.file = file;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getCharset() {
/* 1644 */       return this.charset;
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
/*      */     public void setCharset(String charset) {
/* 1656 */       this.charset = charset;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void validate() {
/* 1667 */       if (this.file == null && (this.key == null || this.value == null)) {
/* 1668 */         throw new BuildException("Release info must define 'key' and 'value' attributes, or a 'file' attribute.", Link.this
/*      */             
/* 1670 */             .getLocation());
/*      */       }
/* 1672 */       if (this.file != null && (this.key != null || this.value != null)) {
/* 1673 */         throw new BuildException("Release info cannot define both a file attribute and key/value attributes.", Link.this
/*      */             
/* 1675 */             .getLocation());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1680 */       if (this.charset == null) {
/* 1681 */         throw new BuildException("Charset cannot be null.", Link.this
/* 1682 */             .getLocation());
/*      */       }
/*      */       
/*      */       try {
/* 1686 */         Charset.forName(this.charset);
/* 1687 */       } catch (IllegalArgumentException e) {
/* 1688 */         throw new BuildException(e, Link.this.getLocation());
/*      */       } 
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
/*      */     
/*      */     public Properties toProperties() {
/* 1705 */       Properties props = new Properties();
/* 1706 */       if (this.file != null) { 
/* 1707 */         try { Reader reader = Files.newBufferedReader(this.file
/* 1708 */               .toPath(), Charset.forName(this.charset));
/*      */           
/* 1710 */           try { props.load(reader);
/* 1711 */             if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 1712 */         { throw new BuildException("Cannot read release info file \"" + this.file + "\": " + e, e, Link.this
/*      */               
/* 1714 */               .getLocation()); }
/*      */          }
/*      */       else
/* 1717 */       { props.setProperty(this.key, this.value); }
/*      */ 
/*      */       
/* 1720 */       return props;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class ReleaseInfo
/*      */   {
/*      */     private File file;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1737 */     private final List<Link.ReleaseInfoEntry> propertiesToAdd = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1742 */     private final List<Link.ReleaseInfoKey> propertiesToDelete = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public File getFile() {
/* 1751 */       return this.file;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setFile(File file) {
/* 1761 */       this.file = file;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Link.ReleaseInfoEntry createAdd() {
/* 1771 */       Link.ReleaseInfoEntry property = new Link.ReleaseInfoEntry();
/* 1772 */       this.propertiesToAdd.add(property);
/* 1773 */       return property;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Link.ReleaseInfoKey createDelete() {
/* 1784 */       Link.ReleaseInfoKey key = new Link.ReleaseInfoKey();
/* 1785 */       this.propertiesToDelete.add(key);
/* 1786 */       return key;
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
/*      */     public void setDelete(String keyList) {
/* 1799 */       for (String key : keyList.split(",")) {
/* 1800 */         this.propertiesToDelete.add(new Link.ReleaseInfoKey(key));
/*      */       }
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
/*      */     public void validate() {
/* 1813 */       this.propertiesToAdd.forEach(Link.ReleaseInfoEntry::validate);
/* 1814 */       this.propertiesToDelete.forEach(Link.ReleaseInfoKey::validate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<String> toCommandLineOptions() {
/* 1825 */       Collection<String> options = new ArrayList<>();
/*      */       
/* 1827 */       if (this.file != null) {
/* 1828 */         options.add("--release-info=" + this.file);
/*      */       }
/* 1830 */       if (!this.propertiesToAdd.isEmpty()) {
/* 1831 */         StringBuilder option = new StringBuilder("--release-info=add");
/*      */         
/* 1833 */         for (Link.ReleaseInfoEntry entry : this.propertiesToAdd) {
/* 1834 */           Properties props = entry.toProperties();
/* 1835 */           for (String key : props.stringPropertyNames()) {
/* 1836 */             option.append(":").append(key).append("=");
/* 1837 */             option.append(props.getProperty(key));
/*      */           } 
/*      */         } 
/*      */         
/* 1841 */         options.add(option.toString());
/*      */       } 
/* 1843 */       if (!this.propertiesToDelete.isEmpty()) {
/* 1844 */         StringBuilder option = new StringBuilder("--release-info=del:keys=");
/*      */ 
/*      */         
/* 1847 */         String separator = "";
/* 1848 */         for (Link.ReleaseInfoKey key : this.propertiesToDelete) {
/* 1849 */           option.append(separator).append(key.getKey());
/*      */ 
/*      */           
/* 1852 */           separator = ",";
/*      */         } 
/*      */         
/* 1855 */         options.add(option.toString());
/*      */       } 
/*      */       
/* 1858 */       return options;
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
/*      */   public void execute() throws BuildException {
/*      */     int exitCode;
/* 1876 */     if (this.outputDir == null) {
/* 1877 */       throw new BuildException("Destination directory is required.", 
/* 1878 */           getLocation());
/*      */     }
/*      */     
/* 1881 */     if (this.modulePath == null || this.modulePath.isEmpty()) {
/* 1882 */       throw new BuildException("Module path is required.", getLocation());
/*      */     }
/*      */     
/* 1885 */     if (this.modules.isEmpty()) {
/* 1886 */       throw new BuildException("At least one module must be specified.", 
/* 1887 */           getLocation());
/*      */     }
/*      */     
/* 1890 */     if (this.outputDir.exists()) {
/* 1891 */       CompositeMapper imageMapper = new CompositeMapper();
/*      */       
/* 1893 */       try { Stream<Path> imageTree = Files.walk(this.outputDir.toPath(), new java.nio.file.FileVisitOption[0]);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1903 */         try { imageTree.forEach(p -> imageMapper.add((FileNameMapper)new MergingMapper(p.toString())));
/*      */ 
/*      */ 
/*      */           
/* 1907 */           ResourceCollection outOfDate = ResourceUtils.selectOutOfDateSources((ProjectComponent)this, (ResourceCollection)this.modulePath, (FileNameMapper)imageMapper, (ResourceFactory)
/* 1908 */               getProject(), 
/* 1909 */               FileUtils.getFileUtils().getFileTimestampGranularity());
/* 1910 */           if (outOfDate.isEmpty())
/* 1911 */           { log("Skipping image creation, since \"" + this.outputDir + "\" is already newer than all constituent modules.", 3);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1916 */             if (imageTree != null) imageTree.close();  return; }  if (imageTree != null) imageTree.close();  } catch (Throwable throwable) { if (imageTree != null) try { imageTree.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 1917 */       { throw new BuildException("Could not scan \"" + this.outputDir + "\" for being up-to-date: " + e, e, 
/*      */             
/* 1919 */             getLocation()); }
/*      */     
/*      */     } 
/*      */     
/* 1923 */     this.modules.forEach(ModuleSpec::validate);
/* 1924 */     this.observableModules.forEach(ModuleSpec::validate);
/* 1925 */     this.launchers.forEach(Launcher::validate);
/* 1926 */     this.locales.forEach(LocaleSpec::validate);
/* 1927 */     this.ordering.forEach(PatternListEntry::validate);
/* 1928 */     this.excludedFiles.forEach(PatternListEntry::validate);
/* 1929 */     this.excludedResources.forEach(PatternListEntry::validate);
/*      */     
/* 1931 */     Collection<String> args = buildJlinkArgs();
/*      */     
/* 1933 */     ToolProvider jlink = ToolProvider.findFirst("jlink").<Throwable>orElseThrow(() -> new BuildException("jlink tool not found in JDK.", getLocation()));
/*      */ 
/*      */ 
/*      */     
/* 1937 */     if (this.outputDir.exists()) {
/* 1938 */       log("Deleting existing " + this.outputDir, 3);
/* 1939 */       deleteTree(this.outputDir.toPath());
/*      */     } 
/*      */     
/* 1942 */     log("Executing: jlink " + String.join(" ", (Iterable)args), 3);
/*      */     
/* 1944 */     ByteArrayOutputStream stdout = new ByteArrayOutputStream();
/* 1945 */     ByteArrayOutputStream stderr = new ByteArrayOutputStream();
/*      */ 
/*      */     
/* 1948 */     PrintStream out = new PrintStream(stdout); 
/* 1949 */     try { PrintStream err = new PrintStream(stderr);
/*      */       
/* 1951 */       try { exitCode = jlink.run(out, err, args.<String>toArray(new String[0]));
/* 1952 */         err.close(); } catch (Throwable throwable) { try { err.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */        throw throwable; }
/* 1954 */      if (exitCode != 0) {
/* 1955 */       StringBuilder message = new StringBuilder();
/* 1956 */       message.append("jlink failed (exit code ").append(exitCode).append(")");
/* 1957 */       if (stdout.size() > 0) {
/* 1958 */         message.append(", output is: ").append(stdout);
/*      */       }
/* 1960 */       if (stderr.size() > 0) {
/* 1961 */         message.append(", error output is: ").append(stderr);
/*      */       }
/*      */       
/* 1964 */       throw new BuildException(message.toString(), getLocation());
/*      */     } 
/*      */     
/* 1967 */     if (this.verboseLevel != null) {
/* 1968 */       int level = this.verboseLevel.getLevel();
/*      */       
/* 1970 */       if (stdout.size() > 0) {
/* 1971 */         log(stdout.toString(), level);
/*      */       }
/* 1973 */       if (stderr.size() > 0) {
/* 1974 */         log(stderr.toString(), level);
/*      */       }
/*      */     } 
/*      */     
/* 1978 */     log("Created " + this.outputDir.getAbsolutePath(), 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void deleteTree(Path dir) {
/*      */     try {
/* 1990 */       Files.walkFileTree(dir, new SimpleFileVisitor<Path>()
/*      */           {
/*      */             
/*      */             public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException
/*      */             {
/* 1995 */               Files.delete(file);
/* 1996 */               return FileVisitResult.CONTINUE;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
/* 2003 */               if (e == null) {
/* 2004 */                 Files.delete(dir);
/*      */               }
/* 2006 */               return super.postVisitDirectory(dir, e);
/*      */             }
/*      */           });
/* 2009 */     } catch (IOException e) {
/* 2010 */       throw new BuildException("Could not delete \"" + dir + "\": " + e, e, 
/* 2011 */           getLocation());
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
/*      */   private Collection<String> buildJlinkArgs() {
/* 2025 */     Collection<String> args = new ArrayList<>();
/*      */     
/* 2027 */     args.add("--output");
/* 2028 */     args.add(this.outputDir.toString());
/*      */     
/* 2030 */     args.add("--module-path");
/* 2031 */     args.add(this.modulePath.toString());
/*      */     
/* 2033 */     args.add("--add-modules");
/* 2034 */     args.add(this.modules.stream().map(ModuleSpec::getName).collect(
/* 2035 */           Collectors.joining(",")));
/*      */     
/* 2037 */     if (!this.observableModules.isEmpty()) {
/* 2038 */       args.add("--limit-modules");
/* 2039 */       args.add(this.observableModules.stream().map(ModuleSpec::getName).collect(
/* 2040 */             Collectors.joining(",")));
/*      */     } 
/*      */     
/* 2043 */     if (!this.locales.isEmpty()) {
/* 2044 */       args.add("--include-locales=" + (String)this.locales
/* 2045 */           .stream().map(LocaleSpec::getName).collect(
/* 2046 */             Collectors.joining(",")));
/*      */     }
/*      */     
/* 2049 */     for (Launcher launcher : this.launchers) {
/* 2050 */       args.add("--launcher");
/* 2051 */       args.add(launcher.toString());
/*      */     } 
/*      */     
/* 2054 */     if (!this.ordering.isEmpty()) {
/* 2055 */       args.add("--order-resources=" + (String)this.ordering
/* 2056 */           .stream().map(PatternListEntry::toOptionValue).collect(
/* 2057 */             Collectors.joining(",")));
/*      */     }
/* 2059 */     if (!this.excludedFiles.isEmpty()) {
/* 2060 */       args.add("--exclude-files=" + (String)this.excludedFiles
/* 2061 */           .stream().map(PatternListEntry::toOptionValue).collect(
/* 2062 */             Collectors.joining(",")));
/*      */     }
/* 2064 */     if (!this.excludedResources.isEmpty()) {
/* 2065 */       args.add("--exclude-resources=" + (String)this.excludedResources
/* 2066 */           .stream().map(PatternListEntry::toOptionValue).collect(
/* 2067 */             Collectors.joining(",")));
/*      */     }
/*      */     
/* 2070 */     if (this.bindServices) {
/* 2071 */       args.add("--bind-services");
/*      */     }
/* 2073 */     if (this.ignoreSigning) {
/* 2074 */       args.add("--ignore-signing-information");
/*      */     }
/* 2076 */     if (!this.includeHeaders) {
/* 2077 */       args.add("--no-header-files");
/*      */     }
/* 2079 */     if (!this.includeManPages) {
/* 2080 */       args.add("--no-man-pages");
/*      */     }
/* 2082 */     if (!this.includeNativeCommands) {
/* 2083 */       args.add("--strip-native-commands");
/*      */     }
/* 2085 */     if (!this.debug) {
/* 2086 */       args.add("--strip-debug");
/*      */     }
/* 2088 */     if (this.verboseLevel != null) {
/* 2089 */       args.add("--verbose");
/*      */     }
/*      */     
/* 2092 */     if (this.endianness != null) {
/* 2093 */       args.add("--endian");
/* 2094 */       args.add(this.endianness.getValue());
/*      */     } 
/*      */     
/* 2097 */     if (this.compressionLevel != null) {
/* 2098 */       if (this.compression != null) {
/* 2099 */         throw new BuildException("compressionLevel attribute and <compression> child element cannot both be present.", 
/*      */             
/* 2101 */             getLocation());
/*      */       }
/* 2103 */       args.add("--compress=" + this.compressionLevel.toCommandLineOption());
/*      */     } 
/* 2105 */     if (this.compression != null) {
/* 2106 */       this.compression.validate();
/* 2107 */       args.add("--compress=" + this.compression.toCommandLineOption());
/*      */     } 
/* 2109 */     if (this.vmType != null) {
/* 2110 */       args.add("--vm=" + this.vmType.getValue());
/*      */     }
/* 2112 */     if (this.checkDuplicateLegal) {
/* 2113 */       args.add("--dedup-legal-notices=error-if-not-same-content");
/*      */     }
/* 2115 */     for (ReleaseInfo info : this.releaseInfo) {
/* 2116 */       info.validate();
/* 2117 */       args.addAll(info.toCommandLineOptions());
/*      */     } 
/*      */     
/* 2120 */     return args;
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/modules/Link.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */