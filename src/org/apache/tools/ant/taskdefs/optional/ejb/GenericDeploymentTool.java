/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.zip.ZipEntry;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Location;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.depend.DependencyAnalyzer;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
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
/*     */ public class GenericDeploymentTool
/*     */   implements EJBDeploymentTool
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 1024;
/*     */   public static final int JAR_COMPRESS_LEVEL = 9;
/*     */   protected static final String META_DIR = "META-INF/";
/*     */   protected static final String MANIFEST = "META-INF/MANIFEST.MF";
/*     */   protected static final String EJB_DD = "ejb-jar.xml";
/*     */   public static final String ANALYZER_SUPER = "super";
/*     */   public static final String ANALYZER_FULL = "full";
/*     */   public static final String ANALYZER_NONE = "none";
/*     */   public static final String DEFAULT_ANALYZER = "super";
/*     */   public static final String ANALYZER_CLASS_SUPER = "org.apache.tools.ant.util.depend.bcel.AncestorAnalyzer";
/*     */   public static final String ANALYZER_CLASS_FULL = "org.apache.tools.ant.util.depend.bcel.FullAnalyzer";
/*     */   private EjbJar.Config config;
/*     */   private File destDir;
/*     */   private Path classpath;
/* 104 */   private String genericJarSuffix = "-generic.jar";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Task task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   private ClassLoader classpathLoader = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Set<String> addedfiles;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DescriptorHandler handler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DependencyAnalyzer dependencyAnalyzer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestdir(File inDir) {
/* 138 */     this.destDir = inDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getDestDir() {
/* 147 */     return this.destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTask(Task task) {
/* 157 */     this.task = task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Task getTask() {
/* 166 */     return this.task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EjbJar.Config getConfig() {
/* 175 */     return this.config;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean usingBaseJarName() {
/* 185 */     return (this.config.baseJarName != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGenericJarSuffix(String inString) {
/* 193 */     this.genericJarSuffix = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 202 */     if (this.classpath == null) {
/* 203 */       this.classpath = new Path(this.task.getProject());
/*     */     }
/* 205 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 214 */     this.classpath = classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path getCombinedClasspath() {
/* 224 */     Path combinedPath = this.classpath;
/* 225 */     if (this.config.classpath != null) {
/* 226 */       if (combinedPath == null) {
/* 227 */         combinedPath = this.config.classpath;
/*     */       } else {
/* 229 */         combinedPath.append(this.config.classpath);
/*     */       } 
/*     */     }
/* 232 */     return combinedPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(String message, int level) {
/* 242 */     getTask().log(message, level);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Location getLocation() {
/* 251 */     return getTask().getLocation();
/*     */   }
/*     */   
/*     */   private void createAnalyzer() {
/* 255 */     String analyzer = this.config.analyzer;
/* 256 */     if (analyzer == null) {
/* 257 */       analyzer = "super";
/*     */     }
/*     */     
/* 260 */     if (analyzer.equals("none")) {
/*     */       return;
/*     */     }
/*     */     
/* 264 */     String analyzerClassName = null;
/* 265 */     switch (analyzer) {
/*     */       case "super":
/* 267 */         analyzerClassName = "org.apache.tools.ant.util.depend.bcel.AncestorAnalyzer";
/*     */         break;
/*     */       case "full":
/* 270 */         analyzerClassName = "org.apache.tools.ant.util.depend.bcel.FullAnalyzer";
/*     */         break;
/*     */       default:
/* 273 */         analyzerClassName = analyzer;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 279 */       Class<? extends DependencyAnalyzer> analyzerClass = Class.forName(analyzerClassName).asSubclass(DependencyAnalyzer.class);
/* 280 */       this.dependencyAnalyzer = analyzerClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 281 */       this.dependencyAnalyzer.addClassPath(new Path(this.task.getProject(), this.config.srcDir
/* 282 */             .getPath()));
/* 283 */       this.dependencyAnalyzer.addClassPath(this.config.classpath);
/* 284 */     } catch (NoClassDefFoundError e) {
/* 285 */       this.dependencyAnalyzer = null;
/* 286 */       this.task.log("Unable to load dependency analyzer: " + analyzerClassName + " - dependent class not found: " + e
/* 287 */           .getMessage(), 1);
/*     */     }
/* 289 */     catch (Exception e) {
/* 290 */       this.dependencyAnalyzer = null;
/* 291 */       this.task.log("Unable to load dependency analyzer: " + analyzerClassName + " - exception: " + e
/* 292 */           .getMessage(), 1);
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
/*     */   public void configure(EjbJar.Config config) {
/* 304 */     this.config = config;
/*     */     
/* 306 */     createAnalyzer();
/* 307 */     this.classpathLoader = null;
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
/*     */   protected void addFileToJar(JarOutputStream jStream, File inputFile, String logicalFilename) throws BuildException {
/* 327 */     if (!this.addedfiles.contains(logicalFilename)) {
/* 328 */       try { InputStream iStream = Files.newInputStream(inputFile.toPath(), new java.nio.file.OpenOption[0]);
/*     */ 
/*     */         
/* 331 */         try { ZipEntry zipEntry = new ZipEntry(logicalFilename.replace('\\', '/'));
/* 332 */           jStream.putNextEntry(zipEntry);
/*     */ 
/*     */ 
/*     */           
/* 336 */           byte[] byteBuffer = new byte[2048];
/* 337 */           int count = 0;
/*     */           do {
/* 339 */             jStream.write(byteBuffer, 0, count);
/* 340 */             count = iStream.read(byteBuffer, 0, byteBuffer.length);
/* 341 */           } while (count != -1);
/*     */ 
/*     */           
/* 344 */           this.addedfiles.add(logicalFilename);
/* 345 */           if (iStream != null) iStream.close();  } catch (Throwable throwable) { if (iStream != null) try { iStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ioe)
/* 346 */       { log("WARNING: IOException while adding entry " + logicalFilename + " to jarfile from " + inputFile
/* 347 */             .getPath() + " " + ioe
/* 348 */             .getClass().getName() + "-" + ioe.getMessage(), 1); }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DescriptorHandler getDescriptorHandler(File srcDir) {
/* 360 */     DescriptorHandler h = new DescriptorHandler(getTask(), srcDir);
/*     */     
/* 362 */     registerKnownDTDs(h);
/*     */ 
/*     */     
/* 365 */     for (EjbJar.DTDLocation dtdLocation : (getConfig()).dtdLocations) {
/* 366 */       h.registerDTD(dtdLocation.getPublicId(), dtdLocation.getLocation());
/*     */     }
/* 368 */     return h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerKnownDTDs(DescriptorHandler handler) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processDescriptor(String descriptorFileName, SAXParser saxParser) {
/* 386 */     checkConfiguration(descriptorFileName, saxParser);
/*     */     
/*     */     try {
/* 389 */       this.handler = getDescriptorHandler(this.config.srcDir);
/*     */ 
/*     */       
/* 392 */       Hashtable<String, File> ejbFiles = parseEjbFiles(descriptorFileName, saxParser);
/*     */ 
/*     */       
/* 395 */       addSupportClasses(ejbFiles);
/*     */ 
/*     */       
/* 398 */       String baseName = getJarBaseName(descriptorFileName);
/*     */       
/* 400 */       String ddPrefix = getVendorDDPrefix(baseName, descriptorFileName);
/*     */       
/* 402 */       File manifestFile = getManifestFile(ddPrefix);
/* 403 */       if (manifestFile != null) {
/* 404 */         ejbFiles.put("META-INF/MANIFEST.MF", manifestFile);
/*     */       }
/*     */ 
/*     */       
/* 408 */       ejbFiles.put("META-INF/ejb-jar.xml", new File(this.config.descriptorDir, descriptorFileName));
/*     */ 
/*     */ 
/*     */       
/* 412 */       addVendorFiles(ejbFiles, ddPrefix);
/*     */ 
/*     */       
/* 415 */       checkAndAddDependants(ejbFiles);
/*     */ 
/*     */ 
/*     */       
/* 419 */       if (this.config.flatDestDir && !baseName.isEmpty()) {
/* 420 */         int startName = baseName.lastIndexOf(File.separator);
/* 421 */         if (startName == -1) {
/* 422 */           startName = 0;
/*     */         }
/*     */         
/* 425 */         int endName = baseName.length();
/* 426 */         baseName = baseName.substring(startName, endName);
/*     */       } 
/*     */       
/* 429 */       File jarFile = getVendorOutputJarFile(baseName);
/*     */ 
/*     */       
/* 432 */       if (needToRebuild(ejbFiles, jarFile)) {
/*     */         
/* 434 */         log("building " + jarFile
/* 435 */             .getName() + " with " + ejbFiles
/*     */             
/* 437 */             .size() + " files", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 442 */         String publicId = getPublicId();
/* 443 */         writeJar(baseName, jarFile, ejbFiles, publicId);
/*     */       } else {
/*     */         
/* 446 */         log(jarFile.toString() + " is up to date.", 3);
/*     */       }
/*     */     
/* 449 */     } catch (SAXException se) {
/* 450 */       throw new BuildException("SAXException while parsing '" + descriptorFileName + "'. This probably indicates badly-formed XML.  Details: " + se
/*     */ 
/*     */           
/* 453 */           .getMessage(), se);
/*     */     }
/* 455 */     catch (IOException ioe) {
/* 456 */       throw new BuildException("IOException while parsing'" + descriptorFileName + "'.  This probably indicates that the descriptor doesn't exist. Details: " + ioe
/*     */ 
/*     */           
/* 459 */           .getMessage(), ioe);
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
/*     */   protected void checkConfiguration(String descriptorFileName, SAXParser saxParser) throws BuildException {}
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
/*     */   protected Hashtable<String, File> parseEjbFiles(String descriptorFileName, SAXParser saxParser) throws IOException, SAXException {
/* 506 */     InputStream descriptorStream = Files.newInputStream((new File(this.config.descriptorDir, descriptorFileName))
/* 507 */         .toPath(), new java.nio.file.OpenOption[0]); try {
/* 508 */       saxParser.parse(new InputSource(descriptorStream), this.handler);
/* 509 */       Hashtable<String, File> hashtable = this.handler.getFiles();
/* 510 */       if (descriptorStream != null) descriptorStream.close(); 
/*     */       return hashtable;
/*     */     } catch (Throwable throwable) {
/*     */       if (descriptorStream != null)
/*     */         try {
/*     */           descriptorStream.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   } protected void addSupportClasses(Hashtable<String, File> ejbFiles) {
/* 522 */     Project project = this.task.getProject();
/* 523 */     for (FileSet supportFileSet : this.config.supportFileSets) {
/* 524 */       File supportBaseDir = supportFileSet.getDir(project);
/* 525 */       DirectoryScanner supportScanner = supportFileSet.getDirectoryScanner(project);
/* 526 */       for (String supportFile : supportScanner.getIncludedFiles()) {
/* 527 */         ejbFiles.put(supportFile, new File(supportBaseDir, supportFile));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getJarBaseName(String descriptorFileName) {
/* 544 */     String baseName = "";
/*     */ 
/*     */     
/* 547 */     if ("basejarname".equals(this.config.namingScheme.getValue())) {
/* 548 */       String canonicalDescriptor = descriptorFileName.replace('\\', '/');
/* 549 */       int index = canonicalDescriptor.lastIndexOf('/');
/* 550 */       if (index != -1) {
/* 551 */         baseName = descriptorFileName.substring(0, index + 1);
/*     */       }
/* 553 */       baseName = baseName + this.config.baseJarName;
/* 554 */     } else if ("descriptor".equals(this.config.namingScheme.getValue())) {
/* 555 */       int lastSeparatorIndex = descriptorFileName.lastIndexOf(File.separator);
/* 556 */       int endBaseName = -1;
/* 557 */       if (lastSeparatorIndex != -1) {
/* 558 */         endBaseName = descriptorFileName.indexOf(this.config.baseNameTerminator, lastSeparatorIndex);
/*     */       } else {
/*     */         
/* 561 */         endBaseName = descriptorFileName.indexOf(this.config.baseNameTerminator);
/*     */       } 
/*     */       
/* 564 */       if (endBaseName != -1) {
/* 565 */         baseName = descriptorFileName.substring(0, endBaseName);
/*     */       } else {
/* 567 */         throw new BuildException("Unable to determine jar name from descriptor \"%s\"", new Object[] { descriptorFileName });
/*     */       }
/*     */     
/*     */     }
/* 571 */     else if ("directory".equals(this.config.namingScheme.getValue())) {
/* 572 */       File descriptorFile = new File(this.config.descriptorDir, descriptorFileName);
/* 573 */       String path = descriptorFile.getAbsolutePath();
/*     */       
/* 575 */       int lastSeparatorIndex = path.lastIndexOf(File.separator);
/* 576 */       if (lastSeparatorIndex == -1) {
/* 577 */         throw new BuildException("Unable to determine directory name holding descriptor");
/*     */       }
/* 579 */       String dirName = path.substring(0, lastSeparatorIndex);
/* 580 */       int dirSeparatorIndex = dirName.lastIndexOf(File.separator);
/* 581 */       if (dirSeparatorIndex != -1) {
/* 582 */         dirName = dirName.substring(dirSeparatorIndex + 1);
/*     */       }
/* 584 */       baseName = dirName;
/* 585 */     } else if ("ejb-name".equals(this.config.namingScheme.getValue())) {
/* 586 */       baseName = this.handler.getEjbName();
/*     */     } 
/* 588 */     return baseName;
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
/*     */   public String getVendorDDPrefix(String baseName, String descriptorFileName) {
/* 601 */     String ddPrefix = null;
/*     */     
/* 603 */     if (this.config.namingScheme.getValue().equals("descriptor")) {
/* 604 */       ddPrefix = baseName + this.config.baseNameTerminator;
/* 605 */     } else if (this.config.namingScheme.getValue().equals("basejarname") || this.config.namingScheme
/* 606 */       .getValue().equals("ejb-name") || this.config.namingScheme
/* 607 */       .getValue().equals("directory")) {
/* 608 */       String canonicalDescriptor = descriptorFileName.replace('\\', '/');
/* 609 */       int index = canonicalDescriptor.lastIndexOf('/');
/* 610 */       if (index == -1) {
/* 611 */         ddPrefix = "";
/*     */       } else {
/* 613 */         ddPrefix = descriptorFileName.substring(0, index + 1);
/*     */       } 
/*     */     } 
/* 616 */     return ddPrefix;
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
/*     */   protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   File getVendorOutputJarFile(String baseName) {
/* 637 */     return new File(this.destDir, baseName + this.genericJarSuffix);
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
/*     */   protected boolean needToRebuild(Hashtable<String, File> ejbFiles, File jarFile) {
/* 656 */     if (jarFile.exists()) {
/* 657 */       long lastBuild = jarFile.lastModified();
/*     */ 
/*     */ 
/*     */       
/* 661 */       for (File currentFile : ejbFiles.values()) {
/* 662 */         if (lastBuild < currentFile.lastModified()) {
/* 663 */           log("Build needed because " + currentFile.getPath() + " is out of date", 3);
/*     */           
/* 665 */           return true;
/*     */         } 
/*     */       } 
/* 668 */       return false;
/*     */     } 
/* 670 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPublicId() {
/* 681 */     return this.handler.getPublicId();
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
/*     */   protected File getManifestFile(String prefix) {
/* 697 */     File manifestFile = new File((getConfig()).descriptorDir, prefix + "manifest.mf");
/* 698 */     if (manifestFile.exists()) {
/* 699 */       return manifestFile;
/*     */     }
/* 701 */     if (this.config.manifest != null) {
/* 702 */       return this.config.manifest;
/*     */     }
/* 704 */     return null;
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
/*     */   protected void writeJar(String baseName, File jarfile, Hashtable<String, File> files, String publicId) throws BuildException {
/* 720 */     if (this.addedfiles == null) {
/* 721 */       this.addedfiles = new HashSet<>();
/*     */     } else {
/* 723 */       this.addedfiles.clear();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 732 */       if (jarfile.exists()) {
/* 733 */         jarfile.delete();
/*     */       }
/* 735 */       jarfile.getParentFile().mkdirs();
/* 736 */       jarfile.createNewFile();
/*     */       
/* 738 */       InputStream in = null;
/* 739 */       Manifest manifest = null;
/*     */       try {
/* 741 */         File manifestFile = files.get("META-INF/MANIFEST.MF");
/* 742 */         if (manifestFile != null && manifestFile.exists()) {
/* 743 */           in = Files.newInputStream(manifestFile.toPath(), new java.nio.file.OpenOption[0]);
/*     */         } else {
/* 745 */           String defaultManifest = "/org/apache/tools/ant/defaultManifest.mf";
/* 746 */           in = getClass().getResourceAsStream(defaultManifest);
/* 747 */           if (in == null) {
/* 748 */             throw new BuildException("Could not find default manifest: %s", new Object[] { defaultManifest });
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 753 */         manifest = new Manifest(in);
/* 754 */       } catch (IOException e) {
/* 755 */         throw new BuildException("Unable to read manifest", e, getLocation());
/*     */       } finally {
/* 757 */         if (in != null) {
/* 758 */           in.close();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 765 */       JarOutputStream jarStream = new JarOutputStream(Files.newOutputStream(jarfile.toPath(), new java.nio.file.OpenOption[0]), manifest); 
/* 766 */       try { jarStream.setMethod(8);
/*     */ 
/*     */         
/* 769 */         for (Map.Entry<String, File> entryFiles : files.entrySet()) {
/* 770 */           String entryName = entryFiles.getKey();
/* 771 */           if (entryName.equals("META-INF/MANIFEST.MF")) {
/*     */             continue;
/*     */           }
/* 774 */           File entryFile = entryFiles.getValue();
/* 775 */           log("adding file '" + entryName + "'", 3);
/* 776 */           addFileToJar(jarStream, entryFile, entryName);
/*     */ 
/*     */ 
/*     */           
/* 780 */           InnerClassFilenameFilter flt = new InnerClassFilenameFilter(entryFile.getName());
/* 781 */           File entryDir = entryFile.getParentFile();
/* 782 */           String[] innerfiles = entryDir.list(flt);
/* 783 */           if (innerfiles != null) {
/* 784 */             for (String innerfile : innerfiles) {
/*     */ 
/*     */               
/* 787 */               int entryIndex = entryName.lastIndexOf(entryFile.getName()) - 1;
/* 788 */               if (entryIndex < 0) {
/* 789 */                 entryName = innerfile;
/*     */               } else {
/* 791 */                 entryName = entryName.substring(0, entryIndex) + File.separatorChar + innerfile;
/*     */               } 
/*     */ 
/*     */               
/* 795 */               entryFile = new File(this.config.srcDir, entryName);
/*     */               
/* 797 */               log("adding innerclass file '" + entryName + "'", 3);
/*     */ 
/*     */               
/* 800 */               addFileToJar(jarStream, entryFile, entryName);
/*     */             } 
/*     */           }
/*     */         } 
/* 804 */         jarStream.close(); } catch (Throwable throwable) { try { jarStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; } 
/* 805 */     } catch (IOException ioe) {
/*     */ 
/*     */ 
/*     */       
/* 809 */       String msg = "IOException while processing ejb-jar file '" + jarfile.toString() + "'. Details: " + ioe.getMessage();
/* 810 */       throw new BuildException(msg, ioe);
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
/*     */   
/*     */   protected void checkAndAddDependants(Hashtable<String, File> checkEntries) throws BuildException {
/* 823 */     if (this.dependencyAnalyzer == null) {
/*     */       return;
/*     */     }
/*     */     
/* 827 */     this.dependencyAnalyzer.reset();
/*     */     
/* 829 */     for (String entryName : checkEntries.keySet()) {
/* 830 */       if (entryName.endsWith(".class")) {
/* 831 */         String className = entryName.substring(0, entryName
/* 832 */             .length() - ".class".length());
/* 833 */         className = className.replace(File.separatorChar, '/');
/* 834 */         className = className.replace('/', '.');
/*     */         
/* 836 */         this.dependencyAnalyzer.addRootClass(className);
/*     */       } 
/*     */     } 
/*     */     
/* 840 */     for (String classname : Collections.list(this.dependencyAnalyzer.getClassDependencies())) {
/* 841 */       String location = classname.replace('.', File.separatorChar) + ".class";
/* 842 */       File classFile = new File(this.config.srcDir, location);
/* 843 */       if (classFile.exists()) {
/* 844 */         checkEntries.put(location, classFile);
/* 845 */         log("dependent class: " + classname + " - " + classFile, 3);
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
/*     */   
/*     */   protected ClassLoader getClassLoaderForBuild() {
/* 858 */     if (this.classpathLoader != null) {
/* 859 */       return this.classpathLoader;
/*     */     }
/* 861 */     Path combinedClasspath = getCombinedClasspath();
/*     */ 
/*     */     
/* 864 */     if (combinedClasspath == null) {
/* 865 */       this.classpathLoader = getClass().getClassLoader();
/*     */     } else {
/*     */       
/* 868 */       this
/* 869 */         .classpathLoader = (ClassLoader)getTask().getProject().createClassLoader(combinedClasspath);
/*     */     } 
/* 871 */     return this.classpathLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateConfigured() throws BuildException {
/* 882 */     if (this.destDir == null || !this.destDir.isDirectory())
/* 883 */       throw new BuildException("A valid destination directory must be specified using the \"destdir\" attribute.", 
/*     */           
/* 885 */           getLocation()); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/GenericDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */