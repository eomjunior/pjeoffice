/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileReader;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.Files;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.DirectoryScanner;
/*      */ import org.apache.tools.ant.ProjectComponent;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.types.Commandline;
/*      */ import org.apache.tools.ant.types.DirSet;
/*      */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*      */ import org.apache.tools.ant.types.FileSet;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.PatternSet;
/*      */ import org.apache.tools.ant.types.Reference;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.resources.FileProvider;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.JavaEnvUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Javadoc
/*      */   extends Task
/*      */ {
/*      */   private static final String LOAD_FRAME = "function loadFrames() {";
/*   91 */   private static final int LOAD_FRAME_LEN = "function loadFrames() {".length();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class DocletParam
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */     
/*      */     private String value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/*  109 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  118 */       return this.name;
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
/*  130 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getValue() {
/*  139 */       return this.value;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ExtensionInfo
/*      */     extends ProjectComponent
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Path path;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/*  161 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  170 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPath(Path path) {
/*  179 */       if (this.path == null) {
/*  180 */         this.path = path;
/*      */       } else {
/*  182 */         this.path.append(path);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Path getPath() {
/*  193 */       return this.path;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Path createPath() {
/*  203 */       if (this.path == null) {
/*  204 */         this.path = new Path(getProject());
/*      */       }
/*  206 */       return this.path.createPath();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPathRef(Reference r) {
/*  215 */       createPath().setRefid(r);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class DocletInfo
/*      */     extends ExtensionInfo
/*      */   {
/*  226 */     private final List<Javadoc.DocletParam> params = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Javadoc.DocletParam createParam() {
/*  234 */       Javadoc.DocletParam param = new Javadoc.DocletParam();
/*  235 */       this.params.add(param);
/*  236 */       return param;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Enumeration<Javadoc.DocletParam> getParams() {
/*  245 */       return Collections.enumeration(this.params);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class PackageName
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/*  262 */       this.name = name.trim();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/*  271 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  280 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class SourceFile
/*      */   {
/*      */     private File file;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SourceFile() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public SourceFile(File file) {
/*  304 */       this.file = file;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setFile(File file) {
/*  313 */       this.file = file;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public File getFile() {
/*  322 */       return this.file;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Html
/*      */   {
/*  334 */     private final StringBuffer text = new StringBuffer();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addText(String t) {
/*  342 */       this.text.append(t);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getText() {
/*  351 */       return this.text.substring(0);
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
/*      */   public static class AccessType
/*      */     extends EnumeratedAttribute
/*      */   {
/*      */     public String[] getValues() {
/*  367 */       return new String[] { "protected", "public", "package", "private" };
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
/*      */   public class ResourceCollectionContainer
/*      */     implements Iterable<ResourceCollection>
/*      */   {
/*  381 */     private final List<ResourceCollection> rcs = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void add(ResourceCollection rc) {
/*  388 */       this.rcs.add(rc);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<ResourceCollection> iterator() {
/*  397 */       return this.rcs.iterator();
/*      */     }
/*      */   }
/*      */   
/*  401 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */ 
/*      */   
/*  404 */   private final Commandline cmd = new Commandline();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addArgIf(boolean b, String arg) {
/*  414 */     if (b) {
/*  415 */       this.cmd.createArgument().setValue(arg);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addArgIfNotEmpty(String key, String value) {
/*  426 */     if (value == null || value.isEmpty()) {
/*  427 */       log("Warning: Leaving out empty argument '" + key + "'", 1);
/*      */     } else {
/*      */       
/*  430 */       this.cmd.createArgument().setValue(key);
/*  431 */       this.cmd.createArgument().setValue(value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean failOnError = false;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean failOnWarning = false;
/*      */ 
/*      */   
/*  445 */   private Path sourcePath = null;
/*  446 */   private File destDir = null;
/*  447 */   private final List<SourceFile> sourceFiles = new Vector<>();
/*  448 */   private final List<PackageName> packageNames = new Vector<>();
/*  449 */   private final List<PackageName> excludePackageNames = new Vector<>(1);
/*  450 */   private final List<PackageName> moduleNames = new ArrayList<>();
/*      */   private boolean author = true;
/*      */   private boolean version = true;
/*  453 */   private DocletInfo doclet = null;
/*  454 */   private Path classpath = null;
/*  455 */   private Path bootclasspath = null;
/*  456 */   private Path modulePath = null;
/*  457 */   private Path moduleSourcePath = null;
/*  458 */   private String group = null;
/*  459 */   private String packageList = null;
/*  460 */   private final List<LinkArgument> links = new Vector<>();
/*  461 */   private final List<GroupArgument> groups = new Vector<>();
/*  462 */   private final List<Object> tags = new Vector();
/*      */   private boolean useDefaultExcludes = true;
/*  464 */   private Html doctitle = null;
/*  465 */   private Html header = null;
/*  466 */   private Html footer = null;
/*  467 */   private Html bottom = null;
/*      */   private boolean useExternalFile = false;
/*  469 */   private String source = null;
/*      */   private boolean linksource = false;
/*      */   private boolean breakiterator = false;
/*      */   private String noqualifier;
/*      */   private boolean includeNoSourcePackages = false;
/*  474 */   private String executable = null;
/*      */   private boolean docFilesSubDirs = false;
/*  476 */   private String excludeDocFilesSubDir = null;
/*  477 */   private String docEncoding = null;
/*      */   
/*      */   private boolean postProcessGeneratedJavadocs = true;
/*  480 */   private final ResourceCollectionContainer nestedSourceFiles = new ResourceCollectionContainer();
/*      */   
/*  482 */   private final List<DirSet> packageSets = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUseExternalFile(boolean b) {
/*  491 */     this.useExternalFile = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefaultexcludes(boolean useDefaultExcludes) {
/*  502 */     this.useDefaultExcludes = useDefaultExcludes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMaxmemory(String max) {
/*  512 */     this.cmd.createArgument().setValue("-J-Xmx" + max);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAdditionalparam(String add) {
/*  521 */     this.cmd.createArgument().setLine(add);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Commandline.Argument createArg() {
/*  530 */     return this.cmd.createArgument();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSourcepath(Path src) {
/*  539 */     if (this.sourcePath == null) {
/*  540 */       this.sourcePath = src;
/*      */     } else {
/*  542 */       this.sourcePath.append(src);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createSourcepath() {
/*  553 */     if (this.sourcePath == null) {
/*  554 */       this.sourcePath = new Path(getProject());
/*      */     }
/*  556 */     return this.sourcePath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSourcepathRef(Reference r) {
/*  565 */     createSourcepath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulePath(Path mp) {
/*  576 */     if (this.modulePath == null) {
/*  577 */       this.modulePath = mp;
/*      */     } else {
/*  579 */       this.modulePath.append(mp);
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
/*      */   public Path createModulePath() {
/*  592 */     if (this.modulePath == null) {
/*  593 */       this.modulePath = new Path(getProject());
/*      */     }
/*  595 */     return this.modulePath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModulePathref(Reference r) {
/*  606 */     createModulePath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModuleSourcePath(Path mp) {
/*  617 */     if (this.moduleSourcePath == null) {
/*  618 */       this.moduleSourcePath = mp;
/*      */     } else {
/*  620 */       this.moduleSourcePath.append(mp);
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
/*      */   public Path createModuleSourcePath() {
/*  633 */     if (this.moduleSourcePath == null) {
/*  634 */       this.moduleSourcePath = new Path(getProject());
/*      */     }
/*  636 */     return this.moduleSourcePath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setModuleSourcePathref(Reference r) {
/*  647 */     createModuleSourcePath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestdir(File dir) {
/*  656 */     this.destDir = dir;
/*  657 */     this.cmd.createArgument().setValue("-d");
/*  658 */     this.cmd.createArgument().setFile(this.destDir);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSourcefiles(String src) {
/*  667 */     StringTokenizer tok = new StringTokenizer(src, ",");
/*  668 */     while (tok.hasMoreTokens()) {
/*  669 */       String f = tok.nextToken();
/*  670 */       SourceFile sf = new SourceFile();
/*  671 */       sf.setFile(getProject().resolveFile(f.trim()));
/*  672 */       addSource(sf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSource(SourceFile sf) {
/*  682 */     this.sourceFiles.add(sf);
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
/*      */   public void setPackagenames(String packages) {
/*  694 */     StringTokenizer tok = new StringTokenizer(packages, ",");
/*  695 */     while (tok.hasMoreTokens()) {
/*  696 */       String p = tok.nextToken();
/*  697 */       PackageName pn = new PackageName();
/*  698 */       pn.setName(p);
/*  699 */       addPackage(pn);
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
/*      */   public void setModulenames(String modules) {
/*  711 */     for (String m : modules.split(",")) {
/*  712 */       PackageName mn = new PackageName();
/*  713 */       mn.setName(m);
/*  714 */       addModule(mn);
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
/*      */   public void addPackage(PackageName pn) {
/*  727 */     this.packageNames.add(pn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addModule(PackageName mn) {
/*  738 */     this.moduleNames.add(mn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExcludePackageNames(String packages) {
/*  748 */     StringTokenizer tok = new StringTokenizer(packages, ",");
/*  749 */     while (tok.hasMoreTokens()) {
/*  750 */       String p = tok.nextToken();
/*  751 */       PackageName pn = new PackageName();
/*  752 */       pn.setName(p);
/*  753 */       addExcludePackage(pn);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addExcludePackage(PackageName pn) {
/*  763 */     this.excludePackageNames.add(pn);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOverview(File f) {
/*  773 */     this.cmd.createArgument().setValue("-overview");
/*  774 */     this.cmd.createArgument().setFile(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPublic(boolean b) {
/*  784 */     addArgIf(b, "-public");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProtected(boolean b) {
/*  794 */     addArgIf(b, "-protected");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPackage(boolean b) {
/*  804 */     addArgIf(b, "-package");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPrivate(boolean b) {
/*  814 */     addArgIf(b, "-private");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAccess(AccessType at) {
/*  825 */     this.cmd.createArgument().setValue("-" + at.getValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDoclet(String docletName) {
/*  835 */     if (this.doclet == null) {
/*  836 */       this.doclet = new DocletInfo();
/*  837 */       this.doclet.setProject(getProject());
/*      */     } 
/*  839 */     this.doclet.setName(docletName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDocletPath(Path docletPath) {
/*  848 */     if (this.doclet == null) {
/*  849 */       this.doclet = new DocletInfo();
/*  850 */       this.doclet.setProject(getProject());
/*      */     } 
/*  852 */     this.doclet.setPath(docletPath);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDocletPathRef(Reference r) {
/*  862 */     if (this.doclet == null) {
/*  863 */       this.doclet = new DocletInfo();
/*  864 */       this.doclet.setProject(getProject());
/*      */     } 
/*  866 */     this.doclet.createPath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DocletInfo createDoclet() {
/*  875 */     if (this.doclet == null) {
/*  876 */       this.doclet = new DocletInfo();
/*      */     }
/*  878 */     return this.doclet;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTaglet(ExtensionInfo tagletInfo) {
/*  887 */     this.tags.add(tagletInfo);
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
/*      */   public void setOld(boolean b) {
/*  899 */     log("Javadoc 1.4 doesn't support the -1.1 switch anymore", 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspath(Path path) {
/*  910 */     if (this.classpath == null) {
/*  911 */       this.classpath = path;
/*      */     } else {
/*  913 */       this.classpath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createClasspath() {
/*  923 */     if (this.classpath == null) {
/*  924 */       this.classpath = new Path(getProject());
/*      */     }
/*  926 */     return this.classpath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspathRef(Reference r) {
/*  935 */     createClasspath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBootclasspath(Path path) {
/*  944 */     if (this.bootclasspath == null) {
/*  945 */       this.bootclasspath = path;
/*      */     } else {
/*  947 */       this.bootclasspath.append(path);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createBootclasspath() {
/*  957 */     if (this.bootclasspath == null) {
/*  958 */       this.bootclasspath = new Path(getProject());
/*      */     }
/*  960 */     return this.bootclasspath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBootClasspathRef(Reference r) {
/*  969 */     createBootclasspath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setExtdirs(String path) {
/*  981 */     this.cmd.createArgument().setValue("-extdirs");
/*  982 */     this.cmd.createArgument().setValue(path);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtdirs(Path path) {
/*  991 */     this.cmd.createArgument().setValue("-extdirs");
/*  992 */     this.cmd.createArgument().setPath(path);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVerbose(boolean b) {
/* 1001 */     addArgIf(b, "-verbose");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLocale(String locale) {
/* 1012 */     this.cmd.createArgument(true).setValue(locale);
/* 1013 */     this.cmd.createArgument(true).setValue("-locale");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEncoding(String enc) {
/* 1022 */     this.cmd.createArgument().setValue("-encoding");
/* 1023 */     this.cmd.createArgument().setValue(enc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setVersion(boolean b) {
/* 1032 */     this.version = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUse(boolean b) {
/* 1041 */     addArgIf(b, "-use");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAuthor(boolean b) {
/* 1051 */     this.author = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSplitindex(boolean b) {
/* 1060 */     addArgIf(b, "-splitindex");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setWindowtitle(String title) {
/* 1070 */     addArgIfNotEmpty("-windowtitle", title);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDoctitle(String doctitle) {
/* 1079 */     Html h = new Html();
/* 1080 */     h.addText(doctitle);
/* 1081 */     addDoctitle(h);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addDoctitle(Html text) {
/* 1090 */     this.doctitle = text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHeader(String header) {
/* 1099 */     Html h = new Html();
/* 1100 */     h.addText(header);
/* 1101 */     addHeader(h);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addHeader(Html text) {
/* 1110 */     this.header = text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFooter(String footer) {
/* 1119 */     Html h = new Html();
/* 1120 */     h.addText(footer);
/* 1121 */     addFooter(h);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addFooter(Html text) {
/* 1130 */     this.footer = text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBottom(String bottom) {
/* 1139 */     Html h = new Html();
/* 1140 */     h.addText(bottom);
/* 1141 */     addBottom(h);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBottom(Html text) {
/* 1150 */     this.bottom = text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLinkoffline(String src) {
/* 1160 */     LinkArgument le = createLink();
/* 1161 */     le.setOffline(true);
/* 1162 */     String linkOfflineError = "The linkoffline attribute must include a URL and a package-list file location separated by a space";
/*      */ 
/*      */     
/* 1165 */     if (src.trim().isEmpty()) {
/* 1166 */       throw new BuildException("The linkoffline attribute must include a URL and a package-list file location separated by a space");
/*      */     }
/* 1168 */     StringTokenizer tok = new StringTokenizer(src, " ", false);
/* 1169 */     le.setHref(tok.nextToken());
/*      */     
/* 1171 */     if (!tok.hasMoreTokens()) {
/* 1172 */       throw new BuildException("The linkoffline attribute must include a URL and a package-list file location separated by a space");
/*      */     }
/* 1174 */     le.setPackagelistLoc(getProject().resolveFile(tok.nextToken()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGroup(String src) {
/* 1185 */     this.group = src;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLink(String src) {
/* 1193 */     createLink().setHref(src);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNodeprecated(boolean b) {
/* 1202 */     addArgIf(b, "-nodeprecated");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNodeprecatedlist(boolean b) {
/* 1211 */     addArgIf(b, "-nodeprecatedlist");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNotree(boolean b) {
/* 1220 */     addArgIf(b, "-notree");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoindex(boolean b) {
/* 1229 */     addArgIf(b, "-noindex");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNohelp(boolean b) {
/* 1238 */     addArgIf(b, "-nohelp");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNonavbar(boolean b) {
/* 1247 */     addArgIf(b, "-nonavbar");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSerialwarn(boolean b) {
/* 1256 */     addArgIf(b, "-serialwarn");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStylesheetfile(File f) {
/* 1265 */     this.cmd.createArgument().setValue("-stylesheetfile");
/* 1266 */     this.cmd.createArgument().setFile(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setHelpfile(File f) {
/* 1275 */     this.cmd.createArgument().setValue("-helpfile");
/* 1276 */     this.cmd.createArgument().setFile(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDocencoding(String enc) {
/* 1285 */     this.cmd.createArgument().setValue("-docencoding");
/* 1286 */     this.cmd.createArgument().setValue(enc);
/* 1287 */     this.docEncoding = enc;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPackageList(String src) {
/* 1296 */     this.packageList = src;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public LinkArgument createLink() {
/* 1305 */     LinkArgument la = new LinkArgument();
/* 1306 */     this.links.add(la);
/* 1307 */     return la;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class LinkArgument
/*      */   {
/*      */     private String href;
/*      */ 
/*      */     
/*      */     private boolean offline = false;
/*      */ 
/*      */     
/*      */     private File packagelistLoc;
/*      */ 
/*      */     
/*      */     private URL packagelistURL;
/*      */ 
/*      */     
/*      */     private boolean resolveLink = false;
/*      */ 
/*      */ 
/*      */     
/*      */     public void setHref(String hr) {
/* 1331 */       this.href = hr;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getHref() {
/* 1339 */       return this.href;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPackagelistLoc(File src) {
/* 1347 */       this.packagelistLoc = src;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public File getPackagelistLoc() {
/* 1355 */       return this.packagelistLoc;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPackagelistURL(URL src) {
/* 1363 */       this.packagelistURL = src;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public URL getPackagelistURL() {
/* 1371 */       return this.packagelistURL;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setOffline(boolean offline) {
/* 1379 */       this.offline = offline;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isLinkOffline() {
/* 1387 */       return this.offline;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setResolveLink(boolean resolve) {
/* 1396 */       this.resolveLink = resolve;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean shouldResolveLink() {
/* 1405 */       return this.resolveLink;
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
/*      */   public TagArgument createTag() {
/* 1418 */     TagArgument ta = new TagArgument();
/* 1419 */     this.tags.add(ta);
/* 1420 */     return ta;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1428 */   static final String[] SCOPE_ELEMENTS = new String[] { "overview", "packages", "types", "constructors", "methods", "fields" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class TagArgument
/*      */     extends FileSet
/*      */   {
/* 1438 */     private String name = null;
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean enabled = true;
/*      */ 
/*      */ 
/*      */     
/* 1446 */     private String scope = "a";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/* 1460 */       this.name = name;
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
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setScope(String verboseScope) throws BuildException {
/* 1480 */       verboseScope = verboseScope.toLowerCase(Locale.ENGLISH);
/*      */       
/* 1482 */       boolean[] elements = new boolean[Javadoc.SCOPE_ELEMENTS.length];
/*      */       
/* 1484 */       boolean gotAll = false;
/* 1485 */       boolean gotNotAll = false;
/*      */ 
/*      */ 
/*      */       
/* 1489 */       StringTokenizer tok = new StringTokenizer(verboseScope, ",");
/* 1490 */       while (tok.hasMoreTokens()) {
/* 1491 */         String next = tok.nextToken().trim();
/* 1492 */         if ("all".equals(next)) {
/* 1493 */           if (gotAll) {
/* 1494 */             getProject().log("Repeated tag scope element: all", 3);
/*      */           }
/*      */           
/* 1497 */           gotAll = true; continue;
/*      */         } 
/*      */         int i;
/* 1500 */         for (i = 0; i < Javadoc.SCOPE_ELEMENTS.length && 
/* 1501 */           !Javadoc.SCOPE_ELEMENTS[i].equals(next); i++);
/*      */ 
/*      */ 
/*      */         
/* 1505 */         if (i == Javadoc.SCOPE_ELEMENTS.length) {
/* 1506 */           throw new BuildException("Unrecognised scope element: %s", new Object[] { next });
/*      */         }
/*      */         
/* 1509 */         if (elements[i]) {
/* 1510 */           getProject().log("Repeated tag scope element: " + next, 3);
/*      */         }
/*      */         
/* 1513 */         elements[i] = true;
/* 1514 */         gotNotAll = true;
/*      */       } 
/*      */ 
/*      */       
/* 1518 */       if (gotNotAll && gotAll) {
/* 1519 */         throw new BuildException("Mixture of \"all\" and other scope elements in tag parameter.");
/*      */       }
/*      */       
/* 1522 */       if (!gotNotAll && !gotAll) {
/* 1523 */         throw new BuildException("No scope elements specified in tag parameter.");
/*      */       }
/*      */       
/* 1526 */       if (gotAll) {
/* 1527 */         this.scope = "a";
/*      */       } else {
/* 1529 */         StringBuilder buff = new StringBuilder(elements.length);
/* 1530 */         for (int i = 0; i < elements.length; i++) {
/* 1531 */           if (elements[i]) {
/* 1532 */             buff.append(Javadoc.SCOPE_ELEMENTS[i].charAt(0));
/*      */           }
/*      */         } 
/* 1535 */         this.scope = buff.toString();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setEnabled(boolean enabled) {
/* 1545 */       this.enabled = enabled;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getParameter() throws BuildException {
/* 1555 */       if (this.name == null || this.name.isEmpty()) {
/* 1556 */         throw new BuildException("No name specified for custom tag.");
/*      */       }
/* 1558 */       if (getDescription() != null) {
/* 1559 */         return this.name + ":" + (this.enabled ? "" : "X") + this.scope + ":" + 
/* 1560 */           getDescription();
/*      */       }
/* 1562 */       if (!this.enabled || !"a".equals(this.scope)) {
/* 1563 */         return this.name + ":" + (this.enabled ? "" : "X") + this.scope;
/*      */       }
/* 1565 */       return this.name;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GroupArgument createGroup() {
/* 1575 */     GroupArgument ga = new GroupArgument();
/* 1576 */     this.groups.add(ga);
/* 1577 */     return ga;
/*      */   }
/*      */ 
/*      */   
/*      */   public class GroupArgument
/*      */   {
/*      */     private Javadoc.Html title;
/*      */     
/* 1585 */     private final List<Javadoc.PackageName> packages = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setTitle(String src) {
/* 1592 */       Javadoc.Html h = new Javadoc.Html();
/* 1593 */       h.addText(src);
/* 1594 */       addTitle(h);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addTitle(Javadoc.Html text) {
/* 1602 */       this.title = text;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getTitle() {
/* 1610 */       return (this.title != null) ? this.title.getText() : null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPackages(String src) {
/* 1618 */       StringTokenizer tok = new StringTokenizer(src, ",");
/* 1619 */       while (tok.hasMoreTokens()) {
/* 1620 */         String p = tok.nextToken();
/* 1621 */         Javadoc.PackageName pn = new Javadoc.PackageName();
/* 1622 */         pn.setName(p);
/* 1623 */         addPackage(pn);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addPackage(Javadoc.PackageName pn) {
/* 1632 */       this.packages.add(pn);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getPackages() {
/* 1640 */       return this.packages.stream().map(Object::toString)
/* 1641 */         .collect(Collectors.joining(":"));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharset(String src) {
/* 1650 */     addArgIfNotEmpty("-charset", src);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailonerror(boolean b) {
/* 1661 */     this.failOnError = b;
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
/*      */   public void setFailonwarning(boolean b) {
/* 1673 */     this.failOnWarning = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSource(String source) {
/* 1683 */     this.source = source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExecutable(String executable) {
/* 1693 */     this.executable = executable;
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
/*      */   public void addPackageset(DirSet packageSet) {
/* 1705 */     this.packageSets.add(packageSet);
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
/*      */   public void addFileset(FileSet fs) {
/* 1719 */     createSourceFiles().add((ResourceCollection)fs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResourceCollectionContainer createSourceFiles() {
/* 1730 */     return this.nestedSourceFiles;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLinksource(boolean b) {
/* 1740 */     this.linksource = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBreakiterator(boolean b) {
/* 1750 */     this.breakiterator = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNoqualifier(String noqualifier) {
/* 1760 */     this.noqualifier = noqualifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIncludeNoSourcePackages(boolean b) {
/* 1770 */     this.includeNoSourcePackages = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDocFilesSubDirs(boolean b) {
/* 1780 */     this.docFilesSubDirs = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExcludeDocFilesSubDir(String s) {
/* 1791 */     this.excludeDocFilesSubDir = s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPostProcessGeneratedJavadocs(boolean b) {
/* 1801 */     this.postProcessGeneratedJavadocs = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/* 1810 */     checkTaskName();
/*      */     
/* 1812 */     List<String> packagesToDoc = new Vector<>();
/* 1813 */     Path sourceDirs = new Path(getProject());
/*      */     
/* 1815 */     checkPackageAndSourcePath();
/*      */     
/* 1817 */     if (this.sourcePath != null) {
/* 1818 */       sourceDirs.addExisting(this.sourcePath);
/*      */     }
/*      */     
/* 1821 */     parsePackages(packagesToDoc, sourceDirs);
/* 1822 */     checkPackages(packagesToDoc, sourceDirs);
/*      */     
/* 1824 */     List<SourceFile> sourceFilesToDoc = new ArrayList<>(this.sourceFiles);
/* 1825 */     addSourceFiles(sourceFilesToDoc);
/*      */     
/* 1827 */     checkPackagesToDoc(packagesToDoc, sourceFilesToDoc);
/*      */     
/* 1829 */     log("Generating Javadoc", 2);
/*      */     
/* 1831 */     Commandline toExecute = (Commandline)this.cmd.clone();
/* 1832 */     if (this.executable != null) {
/* 1833 */       toExecute.setExecutable(this.executable);
/*      */     } else {
/* 1835 */       toExecute.setExecutable(JavaEnvUtils.getJdkExecutable("javadoc"));
/*      */     } 
/*      */ 
/*      */     
/* 1839 */     generalJavadocArguments(toExecute);
/* 1840 */     doSourcePath(toExecute, sourceDirs);
/* 1841 */     doDoclet(toExecute);
/* 1842 */     doBootPath(toExecute);
/* 1843 */     doLinks(toExecute);
/* 1844 */     doGroup(toExecute);
/* 1845 */     doGroups(toExecute);
/* 1846 */     doDocFilesSubDirs(toExecute);
/* 1847 */     doModuleArguments(toExecute);
/*      */     
/* 1849 */     doTags(toExecute);
/* 1850 */     doSource(toExecute);
/* 1851 */     doLinkSource(toExecute);
/* 1852 */     doNoqualifier(toExecute);
/*      */     
/* 1854 */     if (this.breakiterator) {
/* 1855 */       toExecute.createArgument().setValue("-breakiterator");
/*      */     }
/*      */     
/* 1858 */     if (this.useExternalFile) {
/* 1859 */       writeExternalArgs(toExecute);
/*      */     }
/*      */     
/* 1862 */     File tmpList = null;
/* 1863 */     FileWriter wr = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 1869 */       BufferedWriter srcListWriter = null;
/* 1870 */       if (this.useExternalFile) {
/* 1871 */         tmpList = FILE_UTILS.createTempFile(getProject(), "javadoc", "", null, true, true);
/* 1872 */         toExecute.createArgument()
/* 1873 */           .setValue("@" + tmpList.getAbsolutePath());
/* 1874 */         wr = new FileWriter(tmpList.getAbsolutePath(), true);
/* 1875 */         srcListWriter = new BufferedWriter(wr);
/*      */       } 
/*      */       
/* 1878 */       doSourceAndPackageNames(toExecute, packagesToDoc, sourceFilesToDoc, this.useExternalFile, tmpList, srcListWriter);
/*      */ 
/*      */ 
/*      */       
/* 1882 */       if (this.useExternalFile) {
/* 1883 */         srcListWriter.flush();
/*      */       }
/* 1885 */     } catch (IOException e) {
/* 1886 */       if (tmpList != null) {
/* 1887 */         tmpList.delete();
/*      */       }
/* 1889 */       throw new BuildException("Error creating temporary file", e, 
/* 1890 */           getLocation());
/*      */     } finally {
/* 1892 */       FileUtils.close(wr);
/*      */     } 
/*      */     
/* 1895 */     if (this.packageList != null) {
/* 1896 */       toExecute.createArgument().setValue("@" + this.packageList);
/*      */     }
/* 1898 */     log(toExecute.describeCommand(), 3);
/*      */     
/* 1900 */     log("Javadoc execution", 2);
/*      */     
/* 1902 */     JavadocOutputStream out = new JavadocOutputStream(2);
/* 1903 */     JavadocOutputStream err = new JavadocOutputStream(1);
/* 1904 */     Execute exe = new Execute(new PumpStreamHandler((OutputStream)out, (OutputStream)err));
/* 1905 */     exe.setAntRun(getProject());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1913 */     exe.setWorkingDirectory(null);
/*      */     try {
/* 1915 */       exe.setCommandline(toExecute.getCommandline());
/* 1916 */       int ret = exe.execute();
/* 1917 */       if (ret != 0 && this.failOnError) {
/* 1918 */         throw new BuildException("Javadoc returned " + ret, 
/* 1919 */             getLocation());
/*      */       }
/* 1921 */       if (this.failOnWarning && (out.sawWarnings() || err.sawWarnings())) {
/* 1922 */         throw new BuildException("Javadoc issued warnings.", 
/* 1923 */             getLocation());
/*      */       }
/* 1925 */       postProcessGeneratedJavadocs();
/* 1926 */     } catch (IOException e) {
/* 1927 */       throw new BuildException("Javadoc failed: " + e, e, getLocation());
/*      */     } finally {
/* 1929 */       if (tmpList != null) {
/* 1930 */         tmpList.delete();
/* 1931 */         tmpList = null;
/*      */       } 
/*      */       
/* 1934 */       out.logFlush();
/* 1935 */       err.logFlush();
/* 1936 */       FileUtils.close((OutputStream)out);
/* 1937 */       FileUtils.close((OutputStream)err);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTaskName() {
/* 1942 */     if ("javadoc2".equals(getTaskType())) {
/* 1943 */       log("Warning: the task name <javadoc2> is deprecated. Use <javadoc> instead.", 1);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkPackageAndSourcePath() {
/* 1950 */     if (this.packageList != null && this.sourcePath == null) {
/* 1951 */       String msg = "sourcePath attribute must be set when specifying packagelist.";
/*      */       
/* 1953 */       throw new BuildException("sourcePath attribute must be set when specifying packagelist.");
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkPackages(List<String> packagesToDoc, Path sourceDirs) {
/* 1958 */     if (!packagesToDoc.isEmpty() && sourceDirs.isEmpty()) {
/* 1959 */       throw new BuildException("sourcePath attribute must be set when specifying package names.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkPackagesToDoc(List<String> packagesToDoc, List<SourceFile> sourceFilesToDoc) {
/* 1966 */     if (this.packageList == null && packagesToDoc.isEmpty() && sourceFilesToDoc
/* 1967 */       .isEmpty() && this.moduleNames.isEmpty()) {
/* 1968 */       throw new BuildException("No source files, no packages and no modules have been specified.");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void doSourcePath(Commandline toExecute, Path sourceDirs) {
/* 1974 */     if (!sourceDirs.isEmpty()) {
/* 1975 */       toExecute.createArgument().setValue("-sourcepath");
/* 1976 */       toExecute.createArgument().setPath(sourceDirs);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void generalJavadocArguments(Commandline toExecute) {
/* 1981 */     if (this.doctitle != null) {
/* 1982 */       toExecute.createArgument().setValue("-doctitle");
/* 1983 */       toExecute.createArgument().setValue(expand(this.doctitle.getText()));
/*      */     } 
/* 1985 */     if (this.header != null) {
/* 1986 */       toExecute.createArgument().setValue("-header");
/* 1987 */       toExecute.createArgument().setValue(expand(this.header.getText()));
/*      */     } 
/* 1989 */     if (this.footer != null) {
/* 1990 */       toExecute.createArgument().setValue("-footer");
/* 1991 */       toExecute.createArgument().setValue(expand(this.footer.getText()));
/*      */     } 
/* 1993 */     if (this.bottom != null) {
/* 1994 */       toExecute.createArgument().setValue("-bottom");
/* 1995 */       toExecute.createArgument().setValue(expand(this.bottom.getText()));
/*      */     } 
/*      */     
/* 1998 */     if (this.classpath == null) {
/* 1999 */       this.classpath = (new Path(getProject())).concatSystemClasspath("last");
/*      */     } else {
/* 2001 */       this.classpath = this.classpath.concatSystemClasspath("ignore");
/*      */     } 
/*      */     
/* 2004 */     if (this.classpath.size() > 0) {
/* 2005 */       toExecute.createArgument().setValue("-classpath");
/* 2006 */       toExecute.createArgument().setPath(this.classpath);
/*      */     } 
/*      */     
/* 2009 */     if (this.version && this.doclet == null) {
/* 2010 */       toExecute.createArgument().setValue("-version");
/*      */     }
/* 2012 */     if (this.author && this.doclet == null) {
/* 2013 */       toExecute.createArgument().setValue("-author");
/*      */     }
/*      */     
/* 2016 */     if (this.doclet == null && this.destDir == null) {
/* 2017 */       throw new BuildException("destdir attribute must be set!");
/*      */     }
/*      */   }
/*      */   
/*      */   private void doDoclet(Commandline toExecute) {
/* 2022 */     if (this.doclet != null) {
/* 2023 */       if (this.doclet.getName() == null) {
/* 2024 */         throw new BuildException("The doclet name must be specified.", getLocation());
/*      */       }
/* 2026 */       toExecute.createArgument().setValue("-doclet");
/* 2027 */       toExecute.createArgument().setValue(this.doclet.getName());
/* 2028 */       if (this.doclet.getPath() != null) {
/* 2029 */         Path docletPath = this.doclet.getPath().concatSystemClasspath("ignore");
/* 2030 */         if (docletPath.size() != 0) {
/* 2031 */           toExecute.createArgument().setValue("-docletpath");
/* 2032 */           toExecute.createArgument().setPath(docletPath);
/*      */         } 
/*      */       } 
/* 2035 */       for (DocletParam param : Collections.<DocletParam>list(this.doclet.getParams())) {
/* 2036 */         if (param.getName() == null) {
/* 2037 */           throw new BuildException("Doclet parameters must have a name");
/*      */         }
/* 2039 */         toExecute.createArgument().setValue(param.getName());
/* 2040 */         if (param.getValue() != null) {
/* 2041 */           toExecute.createArgument().setValue(param.getValue());
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void writeExternalArgs(Commandline toExecute) {
/* 2049 */     File optionsTmpFile = null;
/*      */     try {
/* 2051 */       optionsTmpFile = FILE_UTILS.createTempFile(
/* 2052 */           getProject(), "javadocOptions", "", null, true, true);
/* 2053 */       String[] listOpt = toExecute.getArguments();
/* 2054 */       toExecute.clearArgs();
/* 2055 */       toExecute.createArgument().setValue("@" + optionsTmpFile
/* 2056 */           .getAbsolutePath());
/*      */       
/* 2058 */       BufferedWriter optionsListWriter = new BufferedWriter(new FileWriter(optionsTmpFile.getAbsolutePath(), true)); 
/* 2059 */       try { for (String opt : listOpt) {
/* 2060 */           if (opt.startsWith("-J-")) {
/* 2061 */             toExecute.createArgument().setValue(opt);
/* 2062 */           } else if (opt.startsWith("-")) {
/* 2063 */             optionsListWriter.write(opt);
/* 2064 */             optionsListWriter.write(" ");
/*      */           } else {
/* 2066 */             optionsListWriter.write(quoteString(opt));
/* 2067 */             optionsListWriter.newLine();
/*      */           } 
/*      */         } 
/* 2070 */         optionsListWriter.close(); } catch (Throwable throwable) { try { optionsListWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; } 
/* 2071 */     } catch (IOException ex) {
/* 2072 */       if (optionsTmpFile != null) {
/* 2073 */         optionsTmpFile.delete();
/*      */       }
/* 2075 */       throw new BuildException("Error creating or writing temporary file for javadoc options", ex, 
/*      */           
/* 2077 */           getLocation());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void doBootPath(Commandline toExecute) {
/* 2082 */     Path bcp = new Path(getProject());
/* 2083 */     if (this.bootclasspath != null) {
/* 2084 */       bcp.append(this.bootclasspath);
/*      */     }
/* 2086 */     bcp = bcp.concatSystemBootClasspath("ignore");
/* 2087 */     if (bcp.size() > 0) {
/* 2088 */       toExecute.createArgument().setValue("-bootclasspath");
/* 2089 */       toExecute.createArgument().setPath(bcp);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void doLinks(Commandline toExecute) {
/* 2094 */     for (LinkArgument la : this.links) {
/* 2095 */       if (la.getHref() == null || la.getHref().isEmpty()) {
/* 2096 */         log("No href was given for the link - skipping", 3);
/*      */         
/*      */         continue;
/*      */       } 
/* 2100 */       String link = null;
/* 2101 */       if (la.shouldResolveLink()) {
/*      */         
/* 2103 */         File hrefAsFile = getProject().resolveFile(la.getHref());
/* 2104 */         if (hrefAsFile.exists()) {
/*      */           
/*      */           try {
/* 2107 */             link = FILE_UTILS.getFileURL(hrefAsFile).toExternalForm();
/* 2108 */           } catch (MalformedURLException ex) {
/*      */             
/* 2110 */             log("Warning: link location was invalid " + hrefAsFile, 1);
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 2115 */       if (link == null) {
/*      */         
/*      */         try {
/* 2118 */           URL base = new URL("file://.");
/*      */           
/* 2120 */           new URL(base, la.getHref());
/* 2121 */           link = la.getHref();
/* 2122 */         } catch (MalformedURLException mue) {
/*      */           
/* 2124 */           log("Link href \"" + la.getHref() + "\" is not a valid url - skipping link", 1);
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */       }
/*      */       
/* 2131 */       if (la.isLinkOffline()) {
/* 2132 */         File packageListLocation = la.getPackagelistLoc();
/* 2133 */         URL packageListURL = la.getPackagelistURL();
/* 2134 */         if (packageListLocation == null && packageListURL == null)
/*      */         {
/* 2136 */           throw new BuildException("The package list location for link " + la
/* 2137 */               .getHref() + " must be provided because the link is offline");
/*      */         }
/*      */         
/* 2140 */         if (packageListLocation != null) {
/* 2141 */           File packageListFile = new File(packageListLocation, "package-list");
/*      */           
/* 2143 */           if (packageListFile.exists()) {
/*      */             
/*      */             try {
/* 2146 */               packageListURL = FILE_UTILS.getFileURL(packageListLocation);
/* 2147 */             } catch (MalformedURLException ex) {
/* 2148 */               log("Warning: Package list location was invalid " + packageListLocation, 1);
/*      */             }
/*      */           
/*      */           } else {
/*      */             
/* 2153 */             log("Warning: No package list was found at " + packageListLocation, 3);
/*      */           } 
/*      */         } 
/*      */         
/* 2157 */         if (packageListURL != null) {
/* 2158 */           toExecute.createArgument().setValue("-linkoffline");
/* 2159 */           toExecute.createArgument().setValue(link);
/* 2160 */           toExecute.createArgument()
/* 2161 */             .setValue(packageListURL.toExternalForm());
/*      */         }  continue;
/*      */       } 
/* 2164 */       toExecute.createArgument().setValue("-link");
/* 2165 */       toExecute.createArgument().setValue(link);
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
/*      */   private void doGroup(Commandline toExecute) {
/* 2183 */     if (this.group != null) {
/* 2184 */       StringTokenizer tok = new StringTokenizer(this.group, ",", false);
/* 2185 */       while (tok.hasMoreTokens()) {
/* 2186 */         String grp = tok.nextToken().trim();
/* 2187 */         int space = grp.indexOf(' ');
/* 2188 */         if (space > 0) {
/* 2189 */           String name = grp.substring(0, space);
/* 2190 */           String pkgList = grp.substring(space + 1);
/* 2191 */           toExecute.createArgument().setValue("-group");
/* 2192 */           toExecute.createArgument().setValue(name);
/* 2193 */           toExecute.createArgument().setValue(pkgList);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void doGroups(Commandline toExecute) {
/* 2201 */     for (GroupArgument ga : this.groups) {
/* 2202 */       String title = ga.getTitle();
/* 2203 */       String packages = ga.getPackages();
/* 2204 */       if (title == null || packages == null) {
/* 2205 */         throw new BuildException("The title and packages must be specified for group elements.");
/*      */       }
/*      */       
/* 2208 */       toExecute.createArgument().setValue("-group");
/* 2209 */       toExecute.createArgument().setValue(expand(title));
/* 2210 */       toExecute.createArgument().setValue(packages);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void doNoqualifier(Commandline toExecute) {
/* 2215 */     if (this.noqualifier != null && this.doclet == null) {
/* 2216 */       toExecute.createArgument().setValue("-noqualifier");
/* 2217 */       toExecute.createArgument().setValue(this.noqualifier);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void doLinkSource(Commandline toExecute) {
/* 2222 */     if (this.linksource && this.doclet == null) {
/* 2223 */       toExecute.createArgument().setValue("-linksource");
/*      */     }
/*      */   }
/*      */   
/*      */   private void doSource(Commandline toExecute) {
/* 2228 */     String sourceArg = (this.source != null) ? this.source : getProject().getProperty("ant.build.javac.source");
/* 2229 */     if (sourceArg != null) {
/* 2230 */       toExecute.createArgument().setValue("-source");
/* 2231 */       toExecute.createArgument().setValue(sourceArg);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void doTags(Commandline toExecute) {
/* 2236 */     for (Object element : this.tags) {
/* 2237 */       if (element instanceof TagArgument) {
/* 2238 */         TagArgument ta = (TagArgument)element;
/* 2239 */         File tagDir = ta.getDir(getProject());
/* 2240 */         if (tagDir == null) {
/*      */ 
/*      */           
/* 2243 */           toExecute.createArgument().setValue("-tag");
/* 2244 */           toExecute.createArgument().setValue(ta.getParameter());
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/* 2249 */         DirectoryScanner tagDefScanner = ta.getDirectoryScanner(getProject());
/* 2250 */         for (String file : tagDefScanner.getIncludedFiles()) {
/* 2251 */           File tagDefFile = new File(tagDir, file); 
/* 2252 */           try { BufferedReader in = new BufferedReader(new FileReader(tagDefFile)); 
/* 2253 */             try { in.lines().forEach(line -> {
/*      */                     toExecute.createArgument().setValue("-tag");
/*      */                     toExecute.createArgument().setValue(line);
/*      */                   });
/* 2257 */               in.close(); } catch (Throwable throwable) { try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException ioe)
/* 2258 */           { throw new BuildException("Couldn't read tag file from " + tagDefFile.getAbsolutePath(), ioe); }
/*      */         
/*      */         } 
/*      */         
/*      */         continue;
/*      */       } 
/* 2264 */       ExtensionInfo tagletInfo = (ExtensionInfo)element;
/* 2265 */       toExecute.createArgument().setValue("-taglet");
/* 2266 */       toExecute.createArgument().setValue(tagletInfo.getName());
/* 2267 */       if (tagletInfo.getPath() != null) {
/* 2268 */         Path tagletPath = tagletInfo.getPath().concatSystemClasspath("ignore");
/* 2269 */         if (!tagletPath.isEmpty()) {
/* 2270 */           toExecute.createArgument().setValue("-tagletpath");
/* 2271 */           toExecute.createArgument().setPath(tagletPath);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void doDocFilesSubDirs(Commandline toExecute) {
/* 2279 */     if (this.docFilesSubDirs) {
/* 2280 */       toExecute.createArgument().setValue("-docfilessubdirs");
/* 2281 */       if (this.excludeDocFilesSubDir != null && !this.excludeDocFilesSubDir.trim().isEmpty()) {
/* 2282 */         toExecute.createArgument().setValue("-excludedocfilessubdir");
/* 2283 */         toExecute.createArgument().setValue(this.excludeDocFilesSubDir);
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
/*      */   private void doSourceAndPackageNames(Commandline toExecute, List<String> packagesToDoc, List<SourceFile> sourceFilesToDoc, boolean useExternalFile, File tmpList, BufferedWriter srcListWriter) throws IOException {
/* 2296 */     for (String packageName : packagesToDoc) {
/* 2297 */       if (useExternalFile) {
/* 2298 */         srcListWriter.write(packageName);
/* 2299 */         srcListWriter.newLine(); continue;
/*      */       } 
/* 2301 */       toExecute.createArgument().setValue(packageName);
/*      */     } 
/*      */ 
/*      */     
/* 2305 */     for (SourceFile sf : sourceFilesToDoc) {
/* 2306 */       String sourceFileName = sf.getFile().getAbsolutePath();
/* 2307 */       if (useExternalFile) {
/*      */ 
/*      */         
/* 2310 */         if (sourceFileName.contains(" ")) {
/* 2311 */           String name = sourceFileName;
/* 2312 */           if (File.separatorChar == '\\') {
/* 2313 */             name = sourceFileName.replace(File.separatorChar, '/');
/*      */           }
/* 2315 */           srcListWriter.write("\"" + name + "\"");
/*      */         } else {
/* 2317 */           srcListWriter.write(sourceFileName);
/*      */         } 
/* 2319 */         srcListWriter.newLine(); continue;
/*      */       } 
/* 2321 */       toExecute.createArgument().setValue(sourceFileName);
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
/*      */   private String quoteString(String str) {
/* 2333 */     if (!containsWhitespace(str) && 
/* 2334 */       !str.contains("'") && !str.contains("\"")) {
/* 2335 */       return str;
/*      */     }
/* 2337 */     if (!str.contains("'")) {
/* 2338 */       return quoteString(str, '\'');
/*      */     }
/* 2340 */     return quoteString(str, '"');
/*      */   }
/*      */   
/*      */   private boolean containsWhitespace(String s) {
/* 2344 */     for (char c : s.toCharArray()) {
/* 2345 */       if (Character.isWhitespace(c)) {
/* 2346 */         return true;
/*      */       }
/*      */     } 
/* 2349 */     return false;
/*      */   }
/*      */   
/*      */   private String quoteString(String str, char delim) {
/* 2353 */     StringBuilder buf = new StringBuilder(str.length() * 2);
/* 2354 */     buf.append(delim);
/* 2355 */     boolean lastCharWasCR = false;
/* 2356 */     for (char c : str.toCharArray()) {
/* 2357 */       if (c == delim) {
/* 2358 */         buf.append('\\').append(c);
/* 2359 */         lastCharWasCR = false;
/*      */       } else {
/* 2361 */         switch (c) {
/*      */           case '\\':
/* 2363 */             buf.append("\\\\");
/* 2364 */             lastCharWasCR = false;
/*      */             break;
/*      */           
/*      */           case '\r':
/* 2368 */             buf.append("\\\r");
/* 2369 */             lastCharWasCR = true;
/*      */             break;
/*      */ 
/*      */ 
/*      */           
/*      */           case '\n':
/* 2375 */             if (!lastCharWasCR) {
/* 2376 */               buf.append("\\\n");
/*      */             } else {
/* 2378 */               buf.append("\n");
/*      */             } 
/* 2380 */             lastCharWasCR = false;
/*      */             break;
/*      */           default:
/* 2383 */             buf.append(c);
/* 2384 */             lastCharWasCR = false;
/*      */             break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2389 */     buf.append(delim);
/* 2390 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addSourceFiles(List<SourceFile> sf) {
/* 2400 */     for (ResourceCollection rc : this.nestedSourceFiles) {
/* 2401 */       FileSet fileSet; if (!rc.isFilesystemOnly()) {
/* 2402 */         throw new BuildException("only file system based resources are supported by javadoc");
/*      */       }
/*      */       
/* 2405 */       if (rc instanceof FileSet) {
/* 2406 */         FileSet fs = (FileSet)rc;
/* 2407 */         if (!fs.hasPatterns() && !fs.hasSelectors()) {
/* 2408 */           FileSet fs2 = (FileSet)fs.clone();
/* 2409 */           fs2.createInclude().setName("**/*.java");
/* 2410 */           if (this.includeNoSourcePackages) {
/* 2411 */             fs2.createInclude().setName("**/package.html");
/*      */           }
/* 2413 */           fileSet = fs2;
/*      */         } 
/*      */       } 
/* 2416 */       for (Resource r : fileSet) {
/* 2417 */         sf.add(new SourceFile(((FileProvider)r.as(FileProvider.class)).getFile()));
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
/*      */   private void parsePackages(List<String> pn, Path sp) {
/* 2431 */     Set<String> addedPackages = new HashSet<>();
/* 2432 */     List<DirSet> dirSets = new ArrayList<>(this.packageSets);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2438 */     if (this.sourcePath != null) {
/* 2439 */       PatternSet ps = new PatternSet();
/* 2440 */       ps.setProject(getProject());
/* 2441 */       if (this.packageNames.isEmpty()) {
/* 2442 */         ps.createInclude().setName("**");
/*      */       } else {
/* 2444 */         this.packageNames.stream().map(PackageName::getName)
/* 2445 */           .map(s -> s.replace('.', '/').replaceFirst("\\*$", "**"))
/* 2446 */           .forEach(pkg -> ps.createInclude().setName(pkg));
/*      */       } 
/*      */       
/* 2449 */       this.excludePackageNames.stream().map(PackageName::getName)
/* 2450 */         .map(s -> s.replace('.', '/').replaceFirst("\\*$", "**"))
/* 2451 */         .forEach(pkg -> ps.createExclude().setName(pkg));
/*      */       
/* 2453 */       for (String pathElement : this.sourcePath.list()) {
/* 2454 */         File dir = new File(pathElement);
/* 2455 */         if (dir.isDirectory()) {
/* 2456 */           DirSet ds = new DirSet();
/* 2457 */           ds.setProject(getProject());
/* 2458 */           ds.setDefaultexcludes(this.useDefaultExcludes);
/* 2459 */           ds.setDir(dir);
/* 2460 */           ds.createPatternSet().addConfiguredPatternset(ps);
/* 2461 */           dirSets.add(ds);
/*      */         } else {
/* 2463 */           log("Skipping " + pathElement + " since it is no directory.", 1);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 2469 */     for (DirSet ds : dirSets) {
/* 2470 */       File baseDir = ds.getDir(getProject());
/* 2471 */       log("scanning " + baseDir + " for packages.", 4);
/* 2472 */       DirectoryScanner dsc = ds.getDirectoryScanner(getProject());
/* 2473 */       boolean containsPackages = false;
/* 2474 */       for (String dir : dsc.getIncludedDirectories()) {
/*      */         
/* 2476 */         File pd = new File(baseDir, dir);
/* 2477 */         String[] files = pd.list((directory, name) -> 
/* 2478 */             (name.endsWith(".java") || (this.includeNoSourcePackages && name.equals("package.html"))));
/*      */ 
/*      */         
/* 2481 */         if (files.length > 0) {
/* 2482 */           if (dir.isEmpty()) {
/* 2483 */             log(baseDir + " contains source files in the default package, you must specify them as source files not packages.", 1);
/*      */           }
/*      */           else {
/*      */             
/* 2487 */             containsPackages = true;
/*      */             
/* 2489 */             String packageName = dir.replace(File.separatorChar, '.');
/* 2490 */             if (!addedPackages.contains(packageName)) {
/* 2491 */               addedPackages.add(packageName);
/* 2492 */               pn.add(packageName);
/*      */             } 
/*      */           } 
/*      */         }
/*      */       } 
/* 2497 */       if (containsPackages) {
/*      */ 
/*      */         
/* 2500 */         sp.createPathElement().setLocation(baseDir); continue;
/*      */       } 
/* 2502 */       log(baseDir + " doesn't contain any packages, dropping it.", 3);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void postProcessGeneratedJavadocs() throws IOException {
/*      */     String fixData;
/* 2509 */     if (!this.postProcessGeneratedJavadocs) {
/*      */       return;
/*      */     }
/* 2512 */     if (this.destDir != null && !this.destDir.isDirectory()) {
/* 2513 */       log("No javadoc created, no need to post-process anything", 3);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 2518 */     InputStream in = Javadoc.class.getResourceAsStream("javadoc-frame-injections-fix.txt");
/* 2519 */     if (in == null) {
/* 2520 */       throw new FileNotFoundException("Missing resource 'javadoc-frame-injections-fix.txt' in classpath.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2528 */       fixData = fixLineFeeds(FileUtils.readFully(new InputStreamReader(in, StandardCharsets.US_ASCII))).trim();
/*      */     } finally {
/* 2530 */       FileUtils.close(in);
/*      */     } 
/*      */     
/* 2533 */     DirectoryScanner ds = new DirectoryScanner();
/* 2534 */     ds.setBasedir(this.destDir);
/* 2535 */     ds.setCaseSensitive(false);
/* 2536 */     ds.setIncludes(new String[] { "**/index.html", "**/index.htm", "**/toc.html", "**/toc.htm" });
/*      */ 
/*      */     
/* 2539 */     ds.addDefaultExcludes();
/* 2540 */     ds.scan();
/* 2541 */     int patched = 0;
/* 2542 */     for (String f : ds.getIncludedFiles()) {
/* 2543 */       patched += postProcess(new File(this.destDir, f), fixData);
/*      */     }
/* 2545 */     if (patched > 0) {
/* 2546 */       log("Patched " + patched + " link injection vulnerable javadocs", 2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int postProcess(File file, String fixData) throws IOException {
/* 2553 */     String fileContents, enc = (this.docEncoding != null) ? this.docEncoding : FILE_UTILS.getDefaultEncoding();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2558 */     InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]), enc); 
/* 2559 */     try { fileContents = fixLineFeeds(FileUtils.safeReadFully(reader));
/* 2560 */       reader.close(); }
/*      */     catch (Throwable throwable) { try { reader.close(); }
/*      */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*      */        throw throwable; }
/* 2564 */      if (!fileContents.contains("function validURL(url) {")) {
/*      */       
/* 2566 */       String patchedFileContents = patchContent(fileContents, fixData);
/* 2567 */       if (!patchedFileContents.equals(fileContents)) {
/*      */         
/* 2569 */         OutputStreamWriter w = new OutputStreamWriter(Files.newOutputStream(file.toPath(), new java.nio.file.OpenOption[0]), enc); 
/* 2570 */         try { w.write(patchedFileContents);
/* 2571 */           w.close();
/* 2572 */           boolean bool = true;
/* 2573 */           w.close(); return bool; } catch (Throwable throwable) { try { w.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/*      */       
/*      */       } 
/* 2576 */     }  return 0;
/*      */   }
/*      */   
/*      */   private String fixLineFeeds(String orig) {
/* 2580 */     return orig.replace("\r\n", "\n")
/* 2581 */       .replace("\n", System.lineSeparator());
/*      */   }
/*      */ 
/*      */   
/*      */   private String patchContent(String fileContents, String fixData) {
/* 2586 */     int start = fileContents.indexOf("function loadFrames() {");
/* 2587 */     if (start >= 0) {
/* 2588 */       return fileContents.substring(0, start) + fixData + fileContents
/* 2589 */         .substring(start + LOAD_FRAME_LEN);
/*      */     }
/* 2591 */     return fileContents;
/*      */   }
/*      */   
/*      */   private void doModuleArguments(Commandline toExecute) {
/* 2595 */     if (!this.moduleNames.isEmpty()) {
/* 2596 */       toExecute.createArgument().setValue("--module");
/* 2597 */       toExecute.createArgument()
/* 2598 */         .setValue(this.moduleNames.stream().map(PackageName::getName)
/* 2599 */           .collect(Collectors.joining(",")));
/*      */     } 
/* 2601 */     if (this.modulePath != null) {
/* 2602 */       toExecute.createArgument().setValue("--module-path");
/* 2603 */       toExecute.createArgument().setPath(this.modulePath);
/*      */     } 
/* 2605 */     if (this.moduleSourcePath != null) {
/* 2606 */       toExecute.createArgument().setValue("--module-source-path");
/* 2607 */       toExecute.createArgument().setPath(this.moduleSourcePath);
/*      */     } 
/*      */   }
/*      */   private class JavadocOutputStream extends LogOutputStream { private String queuedLine; private boolean sawWarnings;
/*      */     
/*      */     JavadocOutputStream(int level) {
/* 2613 */       super(Javadoc.this, level);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2621 */       this.queuedLine = null;
/* 2622 */       this.sawWarnings = false;
/*      */     }
/*      */     
/*      */     protected void processLine(String line, int messageLevel) {
/* 2626 */       if (line.matches("(\\d) warning[s]?$")) {
/* 2627 */         this.sawWarnings = true;
/*      */       }
/* 2629 */       if (messageLevel == 2 && line
/* 2630 */         .startsWith("Generating ")) {
/* 2631 */         if (this.queuedLine != null) {
/* 2632 */           super.processLine(this.queuedLine, 3);
/*      */         }
/* 2634 */         this.queuedLine = line;
/*      */       } else {
/* 2636 */         if (this.queuedLine != null) {
/* 2637 */           if (line.startsWith("Building ")) {
/* 2638 */             super.processLine(this.queuedLine, 3);
/*      */           } else {
/* 2640 */             super.processLine(this.queuedLine, 2);
/*      */           } 
/* 2642 */           this.queuedLine = null;
/*      */         } 
/* 2644 */         super.processLine(line, messageLevel);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void logFlush() {
/* 2649 */       if (this.queuedLine != null) {
/* 2650 */         super.processLine(this.queuedLine, 3);
/* 2651 */         this.queuedLine = null;
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean sawWarnings() {
/* 2656 */       return this.sawWarnings;
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String expand(String content) {
/* 2666 */     return getProject().replaceProperties(content);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Javadoc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */