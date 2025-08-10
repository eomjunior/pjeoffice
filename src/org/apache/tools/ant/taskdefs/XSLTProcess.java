/*      */ package org.apache.tools.ant.taskdefs;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.OutputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Enumeration;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.xpath.XPath;
/*      */ import javax.xml.xpath.XPathConstants;
/*      */ import javax.xml.xpath.XPathExpression;
/*      */ import javax.xml.xpath.XPathExpressionException;
/*      */ import javax.xml.xpath.XPathFactory;
/*      */ import org.apache.tools.ant.AntClassLoader;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.DirectoryScanner;
/*      */ import org.apache.tools.ant.DynamicConfigurator;
/*      */ import org.apache.tools.ant.Project;
/*      */ import org.apache.tools.ant.ProjectComponent;
/*      */ import org.apache.tools.ant.PropertyHelper;
/*      */ import org.apache.tools.ant.taskdefs.optional.TraXLiaison;
/*      */ import org.apache.tools.ant.types.CommandlineJava;
/*      */ import org.apache.tools.ant.types.Environment;
/*      */ import org.apache.tools.ant.types.Mapper;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.PropertySet;
/*      */ import org.apache.tools.ant.types.Reference;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceCollection;
/*      */ import org.apache.tools.ant.types.XMLCatalog;
/*      */ import org.apache.tools.ant.types.resources.FileProvider;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.types.resources.Resources;
/*      */ import org.apache.tools.ant.types.resources.Union;
/*      */ import org.apache.tools.ant.util.ClasspathUtils;
/*      */ import org.apache.tools.ant.util.FileNameMapper;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.ResourceUtils;
/*      */ import org.apache.tools.ant.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class XSLTProcess
/*      */   extends MatchingTask
/*      */   implements XSLTLogger
/*      */ {
/*      */   public static final String PROCESSOR_TRAX = "trax";
/*   82 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */ 
/*      */   
/*   85 */   private File destDir = null;
/*      */ 
/*      */   
/*   88 */   private File baseDir = null;
/*      */ 
/*      */   
/*   91 */   private String xslFile = null;
/*      */ 
/*      */   
/*   94 */   private Resource xslResource = null;
/*      */ 
/*      */   
/*   97 */   private String targetExtension = ".html";
/*      */ 
/*      */   
/*  100 */   private String fileNameParameter = null;
/*      */ 
/*      */   
/*  103 */   private String fileDirParameter = null;
/*      */ 
/*      */   
/*  106 */   private final List<Param> params = new ArrayList<>();
/*      */ 
/*      */   
/*  109 */   private File inFile = null;
/*      */ 
/*      */   
/*  112 */   private File outFile = null;
/*      */ 
/*      */   
/*      */   private String processor;
/*      */ 
/*      */   
/*  118 */   private Path classpath = null;
/*      */ 
/*      */ 
/*      */   
/*      */   private XSLTLiaison liaison;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean stylesheetLoaded = false;
/*      */ 
/*      */   
/*      */   private boolean force = false;
/*      */ 
/*      */   
/*  132 */   private final List<OutputProperty> outputProperties = new Vector<>();
/*      */ 
/*      */   
/*  135 */   private final XMLCatalog xmlCatalog = new XMLCatalog();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean performDirectoryScan = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  148 */   private Factory factory = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean reuseLoadedStylesheet = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  166 */   private AntClassLoader loader = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  173 */   private Mapper mapperElement = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  180 */   private final Union resources = new Union();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean useImplicitFileset = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean suppressWarnings = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean failOnTransformationError = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean failOnError = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean failOnNoResources = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private XPathFactory xpathFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private XPath xpath;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  236 */   private final CommandlineJava.SysProperties sysProperties = new CommandlineJava.SysProperties();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TraceConfiguration traceConfiguration;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setScanIncludedDirectories(boolean b) {
/*  254 */     this.performDirectoryScan = b;
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
/*      */   public void setReloadStylesheet(boolean b) {
/*  266 */     this.reuseLoadedStylesheet = !b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addMapper(Mapper mapper) {
/*  276 */     if (this.mapperElement != null) {
/*  277 */       handleError("Cannot define more than one mapper");
/*      */     } else {
/*  279 */       this.mapperElement = mapper;
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
/*      */   public void add(ResourceCollection rc) {
/*  291 */     this.resources.add(rc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredStyle(Resources rc) {
/*  300 */     if (rc.size() != 1) {
/*  301 */       handleError("The style element must be specified with exactly one nested resource.");
/*      */     } else {
/*      */       
/*  304 */       setXslResource(rc.iterator().next());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setXslResource(Resource xslResource) {
/*  314 */     this.xslResource = xslResource;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void add(FileNameMapper fileNameMapper) throws BuildException {
/*  324 */     Mapper mapper = new Mapper(getProject());
/*  325 */     mapper.add(fileNameMapper);
/*  326 */     addMapper(mapper);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void execute() throws BuildException {
/*  337 */     if ("style".equals(getTaskType())) {
/*  338 */       log("Warning: the task name <style> is deprecated. Use <xslt> instead.", 1);
/*      */     }
/*      */     
/*  341 */     File savedBaseDir = this.baseDir;
/*      */     
/*  343 */     String baseMessage = "specify the stylesheet either as a filename in style attribute or as a nested resource";
/*      */ 
/*      */     
/*  346 */     if (this.xslResource == null && this.xslFile == null) {
/*  347 */       handleError("specify the stylesheet either as a filename in style attribute or as a nested resource");
/*      */       return;
/*      */     } 
/*  350 */     if (this.xslResource != null && this.xslFile != null) {
/*  351 */       handleError("specify the stylesheet either as a filename in style attribute or as a nested resource but not as both");
/*      */       return;
/*      */     } 
/*  354 */     if (this.inFile != null && !this.inFile.exists()) {
/*  355 */       handleError("input file " + this.inFile + " does not exist"); return;
/*      */     } 
/*      */     try {
/*      */       Resource styleResource;
/*  359 */       setupLoader();
/*      */       
/*  361 */       if (this.sysProperties.size() > 0) {
/*  362 */         this.sysProperties.setSystem();
/*      */       }
/*      */ 
/*      */       
/*  366 */       if (this.baseDir == null) {
/*  367 */         this.baseDir = getProject().getBaseDir();
/*      */       }
/*  369 */       this.liaison = getLiaison();
/*      */ 
/*      */       
/*  372 */       if (this.liaison instanceof XSLTLoggerAware) {
/*  373 */         ((XSLTLoggerAware)this.liaison).setLogger(this);
/*      */       }
/*  375 */       log("Using " + this.liaison.getClass().toString(), 3);
/*      */       
/*  377 */       if (this.xslFile != null) {
/*      */ 
/*      */         
/*  380 */         File stylesheet = getProject().resolveFile(this.xslFile);
/*  381 */         if (!stylesheet.exists()) {
/*  382 */           File alternative = FILE_UTILS.resolveFile(this.baseDir, this.xslFile);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  387 */           if (alternative.exists()) {
/*  388 */             log("DEPRECATED - the 'style' attribute should be relative to the project's");
/*  389 */             log("             basedir, not the tasks's basedir.");
/*  390 */             stylesheet = alternative;
/*      */           } 
/*      */         } 
/*  393 */         FileResource fr = new FileResource();
/*  394 */         fr.setProject(getProject());
/*  395 */         fr.setFile(stylesheet);
/*  396 */         FileResource fileResource1 = fr;
/*      */       } else {
/*  398 */         styleResource = this.xslResource;
/*      */       } 
/*      */       
/*  401 */       if (!styleResource.isExists()) {
/*  402 */         handleError("stylesheet " + styleResource + " doesn't exist.");
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  407 */       if (this.inFile != null && this.outFile != null) {
/*  408 */         process(this.inFile, this.outFile, styleResource);
/*      */ 
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  417 */       checkDest();
/*      */ 
/*      */       
/*  420 */       if (this.useImplicitFileset) {
/*  421 */         DirectoryScanner scanner = getDirectoryScanner(this.baseDir);
/*  422 */         log("Transforming into " + this.destDir, 2);
/*      */ 
/*      */         
/*  425 */         for (String element : scanner.getIncludedFiles()) {
/*  426 */           process(this.baseDir, element, this.destDir, styleResource);
/*      */         }
/*  428 */         if (this.performDirectoryScan)
/*      */         {
/*  430 */           for (String dir : scanner.getIncludedDirectories()) {
/*  431 */             for (String element : (new File(this.baseDir, dir)).list()) {
/*  432 */               process(this.baseDir, dir + File.separator + element, this.destDir, styleResource);
/*      */             }
/*      */           }
/*      */         
/*      */         }
/*  437 */       } else if (this.resources.isEmpty()) {
/*      */         
/*  439 */         if (this.failOnNoResources) {
/*  440 */           handleError("no resources specified");
/*      */         }
/*      */         return;
/*      */       } 
/*  444 */       processResources(styleResource);
/*      */     } finally {
/*  446 */       if (this.loader != null) {
/*  447 */         this.loader.resetThreadContextLoader();
/*  448 */         this.loader.cleanup();
/*  449 */         this.loader = null;
/*      */       } 
/*  451 */       if (this.sysProperties.size() > 0) {
/*  452 */         this.sysProperties.restoreSystem();
/*      */       }
/*  454 */       this.liaison = null;
/*  455 */       this.stylesheetLoaded = false;
/*  456 */       this.baseDir = savedBaseDir;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setForce(boolean force) {
/*  467 */     this.force = force;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBasedir(File dir) {
/*  477 */     this.baseDir = dir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDestdir(File dir) {
/*  488 */     this.destDir = dir;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExtension(String name) {
/*  497 */     this.targetExtension = name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setStyle(String xslFile) {
/*  507 */     this.xslFile = xslFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspath(Path classpath) {
/*  516 */     createClasspath().append(classpath);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path createClasspath() {
/*  525 */     if (this.classpath == null) {
/*  526 */       this.classpath = new Path(getProject());
/*      */     }
/*  528 */     return this.classpath.createPath();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClasspathRef(Reference r) {
/*  538 */     createClasspath().setRefid(r);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProcessor(String processor) {
/*  547 */     this.processor = processor;
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
/*      */   public void setUseImplicitFileset(boolean useimplicitfileset) {
/*  559 */     this.useImplicitFileset = useimplicitfileset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addConfiguredXMLCatalog(XMLCatalog xmlCatalog) {
/*  568 */     this.xmlCatalog.addConfiguredXMLCatalog(xmlCatalog);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFileNameParameter(String fileNameParameter) {
/*  579 */     this.fileNameParameter = fileNameParameter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFileDirParameter(String fileDirParameter) {
/*  590 */     this.fileDirParameter = fileDirParameter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSuppressWarnings(boolean b) {
/*  600 */     this.suppressWarnings = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getSuppressWarnings() {
/*  610 */     return this.suppressWarnings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailOnTransformationError(boolean b) {
/*  620 */     this.failOnTransformationError = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailOnError(boolean b) {
/*  630 */     this.failOnError = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFailOnNoResources(boolean b) {
/*  640 */     this.failOnNoResources = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSysproperty(Environment.Variable sysp) {
/*  650 */     this.sysProperties.addVariable(sysp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addSyspropertyset(PropertySet sysp) {
/*  660 */     this.sysProperties.addSyspropertyset(sysp);
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
/*      */   public TraceConfiguration createTrace() {
/*  674 */     if (this.traceConfiguration != null) {
/*  675 */       throw new BuildException("can't have more than one trace configuration");
/*      */     }
/*  677 */     this.traceConfiguration = new TraceConfiguration();
/*  678 */     return this.traceConfiguration;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TraceConfiguration getTraceConfiguration() {
/*  688 */     return this.traceConfiguration;
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
/*      */   private void resolveProcessor(String proc) throws Exception {
/*  700 */     if ("trax".equals(proc)) {
/*  701 */       this.liaison = (XSLTLiaison)new TraXLiaison();
/*      */     } else {
/*      */       
/*  704 */       Class<? extends XSLTLiaison> clazz = loadClass(proc).asSubclass(XSLTLiaison.class);
/*  705 */       this.liaison = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
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
/*      */   private Class<?> loadClass(String classname) throws ClassNotFoundException {
/*  718 */     setupLoader();
/*  719 */     if (this.loader == null) {
/*  720 */       return Class.forName(classname);
/*      */     }
/*  722 */     return Class.forName(classname, true, (ClassLoader)this.loader);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setupLoader() {
/*  731 */     if (this.classpath != null && this.loader == null) {
/*  732 */       this.loader = getProject().createClassLoader(this.classpath);
/*  733 */       this.loader.setThreadContextLoader();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOut(File outFile) {
/*  744 */     this.outFile = outFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIn(File inFile) {
/*  754 */     this.inFile = inFile;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkDest() {
/*  763 */     if (this.destDir == null) {
/*  764 */       handleError("destdir attributes must be set!");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processResources(Resource stylesheet) {
/*  775 */     for (Resource r : this.resources) {
/*  776 */       if (!r.isExists()) {
/*      */         continue;
/*      */       }
/*  779 */       File base = this.baseDir;
/*  780 */       String name = r.getName();
/*  781 */       FileProvider fp = (FileProvider)r.as(FileProvider.class);
/*  782 */       if (fp != null) {
/*  783 */         FileResource f = ResourceUtils.asFileResource(fp);
/*  784 */         base = f.getBaseDir();
/*  785 */         if (base == null) {
/*  786 */           name = f.getFile().getAbsolutePath();
/*      */         }
/*      */       } 
/*  789 */       process(base, name, this.destDir, stylesheet);
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
/*      */   private void process(File baseDir, String xmlFile, File destDir, Resource stylesheet) throws BuildException {
/*  806 */     File outF = null;
/*      */     
/*      */     try {
/*  809 */       long styleSheetLastModified = stylesheet.getLastModified();
/*  810 */       File inF = new File(baseDir, xmlFile);
/*      */       
/*  812 */       if (inF.isDirectory()) {
/*  813 */         log("Skipping " + inF + " it is a directory.", 3);
/*      */         
/*      */         return;
/*      */       } 
/*  817 */       FileNameMapper mapper = (this.mapperElement == null) ? new StyleMapper() : this.mapperElement.getImplementation();
/*      */       
/*  819 */       String[] outFileName = mapper.mapFileName(xmlFile);
/*  820 */       if (outFileName == null || outFileName.length == 0) {
/*  821 */         log("Skipping " + this.inFile + " it cannot get mapped to output.", 3);
/*      */         return;
/*      */       } 
/*  824 */       if (outFileName.length > 1) {
/*  825 */         log("Skipping " + this.inFile + " its mapping is ambiguous.", 3);
/*      */         return;
/*      */       } 
/*  828 */       outF = new File(destDir, outFileName[0]);
/*      */       
/*  830 */       if (this.force || inF.lastModified() > outF.lastModified() || styleSheetLastModified > outF
/*  831 */         .lastModified()) {
/*  832 */         ensureDirectoryFor(outF);
/*  833 */         log("Processing " + inF + " to " + outF);
/*  834 */         configureLiaison(stylesheet);
/*  835 */         setLiaisonDynamicFileParameters(this.liaison, inF);
/*  836 */         this.liaison.transform(inF, outF);
/*      */       } 
/*  838 */     } catch (Exception ex) {
/*      */ 
/*      */       
/*  841 */       log("Failed to process " + this.inFile, 2);
/*  842 */       if (outF != null) {
/*  843 */         outF.delete();
/*      */       }
/*  845 */       handleTransformationError(ex);
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
/*      */   private void process(File inFile, File outFile, Resource stylesheet) throws BuildException {
/*      */     try {
/*  860 */       long styleSheetLastModified = stylesheet.getLastModified();
/*  861 */       log("In file " + inFile + " time: " + inFile.lastModified(), 4);
/*  862 */       log("Out file " + outFile + " time: " + outFile.lastModified(), 4);
/*  863 */       log("Style file " + this.xslFile + " time: " + styleSheetLastModified, 4);
/*  864 */       if (this.force || inFile.lastModified() >= outFile.lastModified() || styleSheetLastModified >= outFile
/*  865 */         .lastModified()) {
/*  866 */         ensureDirectoryFor(outFile);
/*  867 */         log("Processing " + inFile + " to " + outFile, 2);
/*  868 */         configureLiaison(stylesheet);
/*  869 */         setLiaisonDynamicFileParameters(this.liaison, inFile);
/*  870 */         this.liaison.transform(inFile, outFile);
/*      */       } else {
/*  872 */         log("Skipping input file " + inFile + " because it is older than output file " + outFile + " and so is the stylesheet " + stylesheet, 4);
/*      */       }
/*      */     
/*  875 */     } catch (Exception ex) {
/*  876 */       log("Failed to process " + inFile, 2);
/*  877 */       if (outFile != null) {
/*  878 */         outFile.delete();
/*      */       }
/*  880 */       handleTransformationError(ex);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void ensureDirectoryFor(File targetFile) throws BuildException {
/*  891 */     File directory = targetFile.getParentFile();
/*  892 */     if (!directory.exists() && !directory.mkdirs() && !directory.isDirectory()) {
/*  893 */       handleError("Unable to create directory: " + directory
/*  894 */           .getAbsolutePath());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Factory getFactory() {
/*  904 */     return this.factory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public XMLCatalog getXMLCatalog() {
/*  913 */     this.xmlCatalog.setProject(getProject());
/*  914 */     return this.xmlCatalog;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration<OutputProperty> getOutputProperties() {
/*  922 */     return Collections.enumeration(this.outputProperties);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected XSLTLiaison getLiaison() {
/*  932 */     if (this.liaison == null) {
/*  933 */       if (this.processor != null) {
/*      */         try {
/*  935 */           resolveProcessor(this.processor);
/*  936 */         } catch (Exception e) {
/*  937 */           handleError(e);
/*      */         } 
/*      */       } else {
/*      */         try {
/*  941 */           resolveProcessor("trax");
/*  942 */         } catch (Throwable e1) {
/*  943 */           log(StringUtils.getStackTrace(e1), 0);
/*  944 */           handleError(e1);
/*      */         } 
/*      */       } 
/*      */     }
/*  948 */     return this.liaison;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Param createParam() {
/*  957 */     Param p = new Param();
/*  958 */     this.params.add(p);
/*  959 */     return p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Param
/*      */   {
/*  967 */     private String name = null;
/*      */ 
/*      */     
/*  970 */     private String expression = null;
/*      */ 
/*      */ 
/*      */     
/*      */     private String type;
/*      */ 
/*      */     
/*      */     private Object ifCond;
/*      */ 
/*      */     
/*      */     private Object unlessCond;
/*      */ 
/*      */     
/*      */     private Project project;
/*      */ 
/*      */ 
/*      */     
/*      */     public void setProject(Project project) {
/*  988 */       this.project = project;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/*  997 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setExpression(String expression) {
/* 1007 */       this.expression = expression;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setType(String type) {
/* 1016 */       this.type = type;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() throws BuildException {
/* 1026 */       if (this.name == null) {
/* 1027 */         throw new BuildException("Name attribute is missing.");
/*      */       }
/* 1029 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getExpression() throws BuildException {
/* 1040 */       if (this.expression == null) {
/* 1041 */         throw new BuildException("Expression attribute is missing.");
/*      */       }
/* 1043 */       return this.expression;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getType() {
/* 1052 */       return this.type;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setIf(Object ifCond) {
/* 1063 */       this.ifCond = ifCond;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setIf(String ifProperty) {
/* 1073 */       setIf(ifProperty);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setUnless(Object unlessCond) {
/* 1084 */       this.unlessCond = unlessCond;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setUnless(String unlessProperty) {
/* 1094 */       setUnless(unlessProperty);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean shouldUse() {
/* 1103 */       PropertyHelper ph = PropertyHelper.getPropertyHelper(this.project);
/* 1104 */       return (ph.testIfCondition(this.ifCond) && ph
/* 1105 */         .testUnlessCondition(this.unlessCond));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum ParamType
/*      */   {
/* 1136 */     STRING,
/* 1137 */     BOOLEAN,
/* 1138 */     INT,
/* 1139 */     LONG,
/* 1140 */     DOUBLE,
/* 1141 */     XPATH_STRING,
/* 1142 */     XPATH_BOOLEAN,
/* 1143 */     XPATH_NUMBER,
/* 1144 */     XPATH_NODE,
/* 1145 */     XPATH_NODESET;
/*      */     
/*      */     public static final Map<ParamType, QName> XPATH_TYPES;
/*      */     
/*      */     static {
/* 1150 */       Map<ParamType, QName> m = new EnumMap<>(ParamType.class);
/* 1151 */       m.put(XPATH_STRING, XPathConstants.STRING);
/* 1152 */       m.put(XPATH_BOOLEAN, XPathConstants.BOOLEAN);
/* 1153 */       m.put(XPATH_NUMBER, XPathConstants.NUMBER);
/* 1154 */       m.put(XPATH_NODE, XPathConstants.NODE);
/* 1155 */       m.put(XPATH_NODESET, XPathConstants.NODESET);
/* 1156 */       XPATH_TYPES = Collections.unmodifiableMap(m);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OutputProperty createOutputProperty() {
/* 1166 */     OutputProperty p = new OutputProperty();
/* 1167 */     this.outputProperties.add(p);
/* 1168 */     return p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class OutputProperty
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String value;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/* 1188 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/* 1197 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getValue() {
/* 1204 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValue(String value) {
/* 1212 */       this.value = value;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void init() throws BuildException {
/* 1223 */     super.init();
/* 1224 */     this.xmlCatalog.setProject(getProject());
/*      */     
/* 1226 */     this.xpathFactory = XPathFactory.newInstance();
/* 1227 */     this.xpath = this.xpathFactory.newXPath();
/* 1228 */     this.xpath.setXPathVariableResolver(variableName -> getProject().getProperty(variableName.toString()));
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
/*      */   @Deprecated
/*      */   protected void configureLiaison(File stylesheet) throws BuildException {
/* 1241 */     FileResource fr = new FileResource();
/* 1242 */     fr.setProject(getProject());
/* 1243 */     fr.setFile(stylesheet);
/* 1244 */     configureLiaison((Resource)fr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void configureLiaison(Resource stylesheet) throws BuildException {
/* 1255 */     if (this.stylesheetLoaded && this.reuseLoadedStylesheet) {
/*      */       return;
/*      */     }
/* 1258 */     this.stylesheetLoaded = true;
/*      */     
/*      */     try {
/* 1261 */       log("Loading stylesheet " + stylesheet, 2);
/*      */ 
/*      */       
/* 1264 */       if (this.liaison instanceof XSLTLiaison2) {
/* 1265 */         ((XSLTLiaison2)this.liaison).configure(this);
/*      */       }
/* 1267 */       if (this.liaison instanceof XSLTLiaison3) {
/*      */ 
/*      */         
/* 1270 */         ((XSLTLiaison3)this.liaison).setStylesheet(stylesheet);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1276 */         FileProvider fp = (FileProvider)stylesheet.as(FileProvider.class);
/* 1277 */         if (fp != null) {
/* 1278 */           this.liaison.setStylesheet(fp.getFile());
/*      */         } else {
/* 1280 */           handleError(this.liaison.getClass().toString() + " accepts the stylesheet only as a file");
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 1285 */       for (Param p : this.params) {
/* 1286 */         if (p.shouldUse()) {
/* 1287 */           Object evaluatedParam = evaluateParam(p);
/* 1288 */           if (this.liaison instanceof XSLTLiaison4) {
/* 1289 */             ((XSLTLiaison4)this.liaison).addParam(p.getName(), evaluatedParam); continue;
/*      */           } 
/* 1291 */           if (evaluatedParam == null || evaluatedParam instanceof String) {
/* 1292 */             this.liaison.addParam(p.getName(), (String)evaluatedParam); continue;
/*      */           } 
/* 1294 */           log("XSLTLiaison '" + this.liaison.getClass().getName() + "' supports only String parameters. Converting parameter '" + p
/* 1295 */               .getName() + "' to its String value '" + evaluatedParam, 1);
/*      */           
/* 1297 */           this.liaison.addParam(p.getName(), String.valueOf(evaluatedParam));
/*      */         }
/*      */       
/*      */       } 
/* 1301 */     } catch (Exception ex) {
/* 1302 */       log("Failed to transform using stylesheet " + stylesheet, 2);
/* 1303 */       handleTransformationError(ex);
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
/*      */   private Object evaluateParam(Param param) throws XPathExpressionException {
/*      */     ParamType type;
/* 1319 */     String typeName = param.getType();
/* 1320 */     String expression = param.getExpression();
/*      */ 
/*      */ 
/*      */     
/* 1324 */     if (typeName == null || typeName.isEmpty()) {
/* 1325 */       type = ParamType.STRING;
/*      */     } else {
/*      */       try {
/* 1328 */         type = ParamType.valueOf(typeName);
/* 1329 */       } catch (IllegalArgumentException e) {
/* 1330 */         throw new IllegalArgumentException("Invalid XSLT parameter type: " + typeName, e);
/*      */       } 
/*      */     } 
/*      */     
/* 1334 */     switch (type) {
/*      */       case STRING:
/* 1336 */         return expression;
/*      */       case BOOLEAN:
/* 1338 */         return Boolean.valueOf(Boolean.parseBoolean(expression));
/*      */       case DOUBLE:
/* 1340 */         return Double.valueOf(Double.parseDouble(expression));
/*      */       case INT:
/* 1342 */         return Integer.valueOf(Integer.parseInt(expression));
/*      */       case LONG:
/* 1344 */         return Long.valueOf(Long.parseLong(expression));
/*      */     } 
/* 1346 */     QName xpathType = ParamType.XPATH_TYPES.get(type);
/* 1347 */     if (xpathType == null) {
/* 1348 */       throw new IllegalArgumentException("Invalid XSLT parameter type: " + typeName);
/*      */     }
/* 1350 */     XPathExpression xpe = this.xpath.compile(expression);
/*      */     
/* 1352 */     return xpe.evaluate((Object)null, xpathType);
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
/*      */   private void setLiaisonDynamicFileParameters(XSLTLiaison liaison, File inFile) throws Exception {
/* 1368 */     if (this.fileNameParameter != null) {
/* 1369 */       liaison.addParam(this.fileNameParameter, inFile.getName());
/*      */     }
/* 1371 */     if (this.fileDirParameter != null) {
/* 1372 */       String fileName = FileUtils.getRelativePath(this.baseDir, inFile);
/* 1373 */       File file = new File(fileName);
/*      */ 
/*      */       
/* 1376 */       liaison.addParam(this.fileDirParameter, (file.getParent() != null) ? file.getParent().replace('\\', '/') : 
/* 1377 */           ".");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Factory createFactory() throws BuildException {
/* 1387 */     if (this.factory != null) {
/* 1388 */       handleError("'factory' element must be unique");
/*      */     } else {
/* 1390 */       this.factory = new Factory();
/*      */     } 
/* 1392 */     return this.factory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleError(String msg) {
/* 1403 */     if (this.failOnError) {
/* 1404 */       throw new BuildException(msg, getLocation());
/*      */     }
/* 1406 */     log(msg, 1);
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
/*      */   protected void handleError(Throwable ex) {
/* 1419 */     if (this.failOnError) {
/* 1420 */       throw new BuildException(ex);
/*      */     }
/* 1422 */     log("Caught an exception: " + ex, 1);
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
/*      */   protected void handleTransformationError(Exception ex) {
/* 1434 */     if (this.failOnError && this.failOnTransformationError) {
/* 1435 */       throw new BuildException(ex);
/*      */     }
/* 1437 */     log("Caught an error during transformation: " + ex, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Factory
/*      */   {
/*      */     private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1453 */     private final List<Attribute> attributes = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1458 */     private final List<Feature> features = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getName() {
/* 1464 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setName(String name) {
/* 1472 */       this.name = name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addAttribute(Attribute attr) {
/* 1480 */       this.attributes.add(attr);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Enumeration<Attribute> getAttributes() {
/* 1488 */       return Collections.enumeration(this.attributes);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addFeature(Feature feature) {
/* 1497 */       this.features.add(feature);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterable<Feature> getFeatures() {
/* 1507 */       return this.features;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static class Attribute
/*      */       extends ProjectComponent
/*      */       implements DynamicConfigurator
/*      */     {
/*      */       private String name;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       private Object value;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public String getName() {
/* 1531 */         return this.name;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public Object getValue() {
/* 1538 */         return this.value;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public Object createDynamicElement(String name) throws BuildException {
/* 1549 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setDynamicAttribute(String name, String value) throws BuildException {
/* 1562 */         if ("name".equalsIgnoreCase(name)) {
/* 1563 */           this.name = value;
/* 1564 */         } else if ("value".equalsIgnoreCase(name)) {
/*      */ 
/*      */           
/* 1567 */           if ("true".equalsIgnoreCase(value)) {
/* 1568 */             this.value = Boolean.TRUE;
/* 1569 */           } else if ("false".equalsIgnoreCase(value)) {
/* 1570 */             this.value = Boolean.FALSE;
/*      */           } else {
/*      */             try {
/* 1573 */               this.value = Integer.valueOf(value);
/* 1574 */             } catch (NumberFormatException e) {
/* 1575 */               this.value = value;
/*      */             } 
/*      */           } 
/* 1578 */         } else if ("valueref".equalsIgnoreCase(name)) {
/* 1579 */           this.value = getProject().getReference(value);
/* 1580 */         } else if ("classloaderforpath".equalsIgnoreCase(name)) {
/* 1581 */           this
/* 1582 */             .value = ClasspathUtils.getClassLoaderForPath(getProject(), new Reference(
/* 1583 */                 getProject(), value));
/*      */         } else {
/*      */           
/* 1586 */           throw new BuildException("Unsupported attribute: %s", new Object[] { name });
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public static class Feature
/*      */     {
/*      */       private String name;
/*      */       
/*      */       private boolean value;
/*      */ 
/*      */       
/*      */       public Feature() {}
/*      */ 
/*      */       
/*      */       public Feature(String name, boolean value) {
/* 1603 */         this.name = name;
/* 1604 */         this.value = value;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setName(String name) {
/* 1611 */         this.name = name;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(boolean value) {
/* 1618 */         this.value = value;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public String getName() {
/* 1625 */         return this.name;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean getValue() {
/* 1632 */         return this.value;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class StyleMapper
/*      */     implements FileNameMapper
/*      */   {
/*      */     private StyleMapper() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setFrom(String from) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void setTo(String to) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public String[] mapFileName(String xmlFile) {
/* 1657 */       int dotPos = xmlFile.lastIndexOf('.');
/* 1658 */       if (dotPos > 0) {
/* 1659 */         xmlFile = xmlFile.substring(0, dotPos);
/*      */       }
/* 1661 */       return new String[] { xmlFile + XSLTProcess.access$100(this.this$0) };
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final class TraceConfiguration
/*      */   {
/*      */     private boolean elements;
/*      */     
/*      */     private boolean extension;
/*      */     
/*      */     private boolean generation;
/*      */     
/*      */     private boolean selection;
/*      */     
/*      */     private boolean templates;
/*      */ 
/*      */     
/*      */     public void setElements(boolean b) {
/* 1680 */       this.elements = b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getElements() {
/* 1690 */       return this.elements;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setExtension(boolean b) {
/* 1700 */       this.extension = b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getExtension() {
/* 1710 */       return this.extension;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setGeneration(boolean b) {
/* 1720 */       this.generation = b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getGeneration() {
/* 1730 */       return this.generation;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setSelection(boolean b) {
/* 1740 */       this.selection = b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getSelection() {
/* 1750 */       return this.selection;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setTemplates(boolean b) {
/* 1760 */       this.templates = b;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getTemplates() {
/* 1770 */       return this.templates;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public OutputStream getOutputStream() {
/* 1779 */       return (OutputStream)new LogOutputStream((ProjectComponent)XSLTProcess.this);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/XSLTProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */