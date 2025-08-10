/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Vector;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.zip.ZipEntry;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Java;
/*     */ import org.apache.tools.ant.types.Environment;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WeblogicDeploymentTool
/*     */   extends GenericDeploymentTool
/*     */ {
/*     */   public static final String PUBLICID_EJB11 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";
/*     */   public static final String PUBLICID_EJB20 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN";
/*     */   public static final String PUBLICID_WEBLOGIC_EJB510 = "-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB//EN";
/*     */   public static final String PUBLICID_WEBLOGIC_EJB600 = "-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB//EN";
/*     */   public static final String PUBLICID_WEBLOGIC_EJB700 = "-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB//EN";
/*     */   protected static final String DEFAULT_WL51_EJB11_DTD_LOCATION = "/weblogic/ejb/deployment/xml/ejb-jar.dtd";
/*     */   protected static final String DEFAULT_WL60_EJB11_DTD_LOCATION = "/weblogic/ejb20/dd/xml/ejb11-jar.dtd";
/*     */   protected static final String DEFAULT_WL60_EJB20_DTD_LOCATION = "/weblogic/ejb20/dd/xml/ejb20-jar.dtd";
/*     */   protected static final String DEFAULT_WL51_DTD_LOCATION = "/weblogic/ejb/deployment/xml/weblogic-ejb-jar.dtd";
/*     */   protected static final String DEFAULT_WL60_51_DTD_LOCATION = "/weblogic/ejb20/dd/xml/weblogic510-ejb-jar.dtd";
/*     */   protected static final String DEFAULT_WL60_DTD_LOCATION = "/weblogic/ejb20/dd/xml/weblogic600-ejb-jar.dtd";
/*     */   protected static final String DEFAULT_WL70_DTD_LOCATION = "/weblogic/ejb20/dd/xml/weblogic700-ejb-jar.dtd";
/*     */   protected static final String DEFAULT_COMPILER = "default";
/*     */   protected static final String WL_DD = "weblogic-ejb-jar.xml";
/*     */   protected static final String WL_CMP_DD = "weblogic-cmp-rdbms-jar.xml";
/*     */   protected static final String COMPILER_EJB11 = "weblogic.ejbc";
/*     */   protected static final String COMPILER_EJB20 = "weblogic.ejbc20";
/* 104 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/* 107 */   private String jarSuffix = ".jar";
/*     */ 
/*     */ 
/*     */   
/*     */   private String weblogicDTD;
/*     */ 
/*     */ 
/*     */   
/*     */   private String ejb11DTD;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean keepgenerated = false;
/*     */ 
/*     */   
/* 122 */   private String ejbcClass = null;
/*     */   
/* 124 */   private String additionalArgs = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   private String additionalJvmArgs = "";
/*     */   
/*     */   private boolean keepGeneric = false;
/*     */   
/* 133 */   private String compiler = null;
/*     */ 
/*     */   
/*     */   private boolean alwaysRebuild = true;
/*     */ 
/*     */   
/*     */   private boolean noEJBC = false;
/*     */ 
/*     */   
/*     */   private boolean newCMP = false;
/*     */   
/* 144 */   private Path wlClasspath = null;
/*     */ 
/*     */   
/* 147 */   private List<Environment.Variable> sysprops = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   private Integer jvmDebugLevel = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private File outputDir;
/*     */ 
/*     */ 
/*     */   
/*     */   public void addSysproperty(Environment.Variable sysp) {
/* 163 */     this.sysprops.add(sysp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createWLClasspath() {
/* 171 */     if (this.wlClasspath == null) {
/* 172 */       this.wlClasspath = new Path(getTask().getProject());
/*     */     }
/* 174 */     return this.wlClasspath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputDir(File outputDir) {
/* 184 */     this.outputDir = outputDir;
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
/*     */   public void setWLClasspath(Path wlClasspath) {
/* 198 */     this.wlClasspath = wlClasspath;
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
/*     */   public void setCompiler(String compiler) {
/* 214 */     this.compiler = compiler;
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
/*     */   public void setRebuild(boolean rebuild) {
/* 229 */     this.alwaysRebuild = rebuild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJvmDebugLevel(Integer jvmDebugLevel) {
/* 239 */     this.jvmDebugLevel = jvmDebugLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getJvmDebugLevel() {
/* 247 */     return this.jvmDebugLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String inString) {
/* 256 */     this.jarSuffix = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepgeneric(boolean inValue) {
/* 266 */     this.keepGeneric = inValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepgenerated(String inValue) {
/* 277 */     this.keepgenerated = Boolean.parseBoolean(inValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgs(String args) {
/* 286 */     this.additionalArgs = args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJvmargs(String args) {
/* 294 */     this.additionalJvmArgs = args;
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
/*     */   public void setEjbcClass(String ejbcClass) {
/* 307 */     this.ejbcClass = ejbcClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEjbcClass() {
/* 315 */     return this.ejbcClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWeblogicdtd(String inString) {
/* 326 */     setEJBdtd(inString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWLdtd(String inString) {
/* 337 */     this.weblogicDTD = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEJBdtd(String inString) {
/* 348 */     this.ejb11DTD = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOldCMP(boolean oldCMP) {
/* 357 */     this.newCMP = !oldCMP;
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
/*     */   public void setNewCMP(boolean newCMP) {
/* 373 */     this.newCMP = newCMP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoEJBC(boolean noEJBC) {
/* 382 */     this.noEJBC = noEJBC;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerKnownDTDs(DescriptorHandler handler) {
/* 392 */     handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "/weblogic/ejb/deployment/xml/ejb-jar.dtd");
/* 393 */     handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", "/weblogic/ejb20/dd/xml/ejb11-jar.dtd");
/* 394 */     handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", this.ejb11DTD);
/* 395 */     handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", "/weblogic/ejb20/dd/xml/ejb20-jar.dtd");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DescriptorHandler getWeblogicDescriptorHandler(final File srcDir) {
/* 404 */     DescriptorHandler handler = new DescriptorHandler(getTask(), srcDir)
/*     */       {
/*     */         protected void processElement() {
/* 407 */           if ("type-storage".equals(this.currentElement))
/*     */           {
/*     */             
/* 410 */             this.ejbFiles.put(this.currentText, new File(srcDir, this.currentText
/* 411 */                   .substring("META-INF/".length())));
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 416 */     handler.registerDTD("-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB//EN", "/weblogic/ejb/deployment/xml/weblogic-ejb-jar.dtd");
/* 417 */     handler.registerDTD("-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB//EN", "/weblogic/ejb20/dd/xml/weblogic510-ejb-jar.dtd");
/* 418 */     handler.registerDTD("-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB//EN", "/weblogic/ejb20/dd/xml/weblogic600-ejb-jar.dtd");
/* 419 */     handler.registerDTD("-//BEA Systems, Inc.//DTD WebLogic 7.0.0 EJB//EN", "/weblogic/ejb20/dd/xml/weblogic700-ejb-jar.dtd");
/* 420 */     handler.registerDTD("-//BEA Systems, Inc.//DTD WebLogic 5.1.0 EJB//EN", this.weblogicDTD);
/* 421 */     handler.registerDTD("-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB//EN", this.weblogicDTD);
/*     */     
/* 423 */     (getConfig()).dtdLocations.forEach(l -> handler.registerDTD(l.getPublicId(), l.getLocation()));
/* 424 */     return handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
/* 434 */     File weblogicDD = new File((getConfig()).descriptorDir, ddPrefix + "weblogic-ejb-jar.xml");
/*     */     
/* 436 */     if (weblogicDD.exists()) {
/* 437 */       ejbFiles.put("META-INF/weblogic-ejb-jar.xml", weblogicDD);
/*     */     } else {
/*     */       
/* 440 */       log("Unable to locate weblogic deployment descriptor. It was expected to be in " + weblogicDD
/* 441 */           .getPath(), 1);
/*     */       
/*     */       return;
/*     */     } 
/* 445 */     if (!this.newCMP) {
/* 446 */       log("The old method for locating CMP files has been DEPRECATED.", 3);
/* 447 */       log("Please adjust your weblogic descriptor and set newCMP=\"true\" to use the new CMP descriptor inclusion mechanism. ", 3);
/*     */ 
/*     */       
/* 450 */       File weblogicCMPDD = new File((getConfig()).descriptorDir, ddPrefix + "weblogic-cmp-rdbms-jar.xml");
/*     */       
/* 452 */       if (weblogicCMPDD.exists()) {
/* 453 */         ejbFiles.put("META-INF/weblogic-cmp-rdbms-jar.xml", weblogicCMPDD);
/*     */       }
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 461 */         File ejbDescriptor = ejbFiles.get("META-INF/ejb-jar.xml");
/* 462 */         SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
/*     */         
/* 464 */         saxParserFactory.setValidating(true);
/*     */         
/* 466 */         SAXParser saxParser = saxParserFactory.newSAXParser();
/*     */         
/* 468 */         DescriptorHandler handler = getWeblogicDescriptorHandler(ejbDescriptor.getParentFile());
/*     */         
/* 470 */         InputStream in = Files.newInputStream(weblogicDD.toPath(), new java.nio.file.OpenOption[0]); 
/* 471 */         try { saxParser.parse(new InputSource(in), handler);
/* 472 */           if (in != null) in.close();  } catch (Throwable throwable) { if (in != null)
/* 473 */             try { in.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  ejbFiles.putAll(handler.getFiles());
/* 474 */       } catch (Exception e) {
/* 475 */         throw new BuildException("Exception while adding Vendor specific files: " + e
/*     */             
/* 477 */             .toString(), e);
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
/*     */   File getVendorOutputJarFile(String baseName) {
/* 490 */     return new File(getDestDir(), baseName + this.jarSuffix);
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
/*     */   private void buildWeblogicJar(File sourceJar, File destJar, String publicId) {
/* 504 */     if (this.noEJBC) {
/*     */       try {
/* 506 */         FILE_UTILS.copyFile(sourceJar, destJar);
/* 507 */         if (!this.keepgenerated) {
/* 508 */           sourceJar.delete();
/*     */         }
/*     */         return;
/* 511 */       } catch (IOException e) {
/* 512 */         throw new BuildException("Unable to write EJB jar", e);
/*     */       } 
/*     */     }
/*     */     
/* 516 */     String ejbcClassName = this.ejbcClass;
/*     */     
/*     */     try {
/* 519 */       Java javaTask = new Java(getTask());
/* 520 */       javaTask.setTaskName("ejbc");
/*     */       
/* 522 */       javaTask.createJvmarg().setLine(this.additionalJvmArgs);
/* 523 */       Objects.requireNonNull(javaTask); this.sysprops.forEach(javaTask::addSysproperty);
/*     */       
/* 525 */       if (getJvmDebugLevel() != null) {
/* 526 */         javaTask.createJvmarg().setLine(" -Dweblogic.StdoutSeverityLevel=" + this.jvmDebugLevel);
/*     */       }
/*     */       
/* 529 */       if (ejbcClassName == null)
/*     */       {
/* 531 */         if ("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN".equals(publicId)) {
/* 532 */           ejbcClassName = "weblogic.ejbc";
/* 533 */         } else if ("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN".equals(publicId)) {
/* 534 */           ejbcClassName = "weblogic.ejbc20";
/*     */         } else {
/* 536 */           log("Unrecognized publicId " + publicId + " - using EJB 1.1 compiler", 1);
/*     */           
/* 538 */           ejbcClassName = "weblogic.ejbc";
/*     */         } 
/*     */       }
/*     */       
/* 542 */       javaTask.setClassname(ejbcClassName);
/* 543 */       javaTask.createArg().setLine(this.additionalArgs);
/* 544 */       if (this.keepgenerated) {
/* 545 */         javaTask.createArg().setValue("-keepgenerated");
/*     */       }
/* 547 */       if (this.compiler == null) {
/*     */ 
/*     */ 
/*     */         
/* 551 */         String buildCompiler = getTask().getProject().getProperty("build.compiler");
/*     */         
/* 553 */         if ("jikes".equals(buildCompiler)) {
/* 554 */           javaTask.createArg().setValue("-compiler");
/* 555 */           javaTask.createArg().setValue("jikes");
/*     */         } 
/* 557 */       } else if (!"default".equals(this.compiler)) {
/* 558 */         javaTask.createArg().setValue("-compiler");
/* 559 */         javaTask.createArg().setLine(this.compiler);
/*     */       } 
/*     */       
/* 562 */       Path combinedClasspath = getCombinedClasspath();
/* 563 */       if (this.wlClasspath != null && combinedClasspath != null && 
/* 564 */         !combinedClasspath.toString().trim().isEmpty()) {
/* 565 */         javaTask.createArg().setValue("-classpath");
/* 566 */         javaTask.createArg().setPath(combinedClasspath);
/*     */       } 
/*     */       
/* 569 */       javaTask.createArg().setValue(sourceJar.getPath());
/* 570 */       if (this.outputDir == null) {
/* 571 */         javaTask.createArg().setValue(destJar.getPath());
/*     */       } else {
/* 573 */         javaTask.createArg().setValue(this.outputDir.getPath());
/*     */       } 
/*     */       
/* 576 */       Path classpath = this.wlClasspath;
/*     */       
/* 578 */       if (classpath == null) {
/* 579 */         classpath = getCombinedClasspath();
/*     */       }
/*     */       
/* 582 */       javaTask.setFork(true);
/* 583 */       if (classpath != null) {
/* 584 */         javaTask.setClasspath(classpath);
/*     */       }
/*     */       
/* 587 */       log("Calling " + ejbcClassName + " for " + sourceJar.toString(), 3);
/*     */ 
/*     */       
/* 590 */       if (javaTask.executeJava() != 0) {
/* 591 */         throw new BuildException("Ejbc reported an error");
/*     */       }
/* 593 */     } catch (Exception e) {
/*     */       
/* 595 */       throw new BuildException("Exception while calling " + ejbcClassName + ". Details: " + e
/* 596 */           .toString(), e);
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
/*     */   protected void writeJar(String baseName, File jarFile, Hashtable<String, File> files, String publicId) throws BuildException {
/* 614 */     File genericJarFile = super.getVendorOutputJarFile(baseName);
/*     */     
/* 616 */     super.writeJar(baseName, genericJarFile, files, publicId);
/*     */     
/* 618 */     if (this.alwaysRebuild || isRebuildRequired(genericJarFile, jarFile)) {
/* 619 */       buildWeblogicJar(genericJarFile, jarFile, publicId);
/*     */     }
/* 621 */     if (!this.keepGeneric) {
/* 622 */       log("deleting generic jar " + genericJarFile.toString(), 3);
/*     */       
/* 624 */       genericJarFile.delete();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateConfigured() throws BuildException {
/* 634 */     super.validateConfigured();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isRebuildRequired(File genericJarFile, File weblogicJarFile) {
/* 664 */     boolean rebuild = false;
/*     */     
/* 666 */     JarFile genericJar = null;
/* 667 */     JarFile wlJar = null;
/* 668 */     File newWLJarFile = null;
/* 669 */     JarOutputStream newJarStream = null;
/* 670 */     ClassLoader genericLoader = null;
/*     */     
/*     */     try {
/* 673 */       log("Checking if weblogic Jar needs to be rebuilt for jar " + weblogicJarFile.getName(), 3);
/*     */ 
/*     */       
/* 676 */       if (genericJarFile.exists() && genericJarFile.isFile() && weblogicJarFile
/* 677 */         .exists() && weblogicJarFile.isFile()) {
/*     */         
/* 679 */         genericJar = new JarFile(genericJarFile);
/* 680 */         wlJar = new JarFile(weblogicJarFile);
/*     */         
/* 682 */         Map<String, JarEntry> replaceEntries = new HashMap<>();
/*     */ 
/*     */ 
/*     */         
/* 686 */         Map<String, JarEntry> genericEntries = genericJar.stream().collect(Collectors.toMap(je -> je.getName().replace('\\', '/'), je -> je, (a, b) -> b));
/*     */ 
/*     */         
/* 689 */         Map<String, JarEntry> wlEntries = wlJar.stream().collect(Collectors.toMap(ZipEntry::getName, je -> je, (a, b) -> b));
/*     */ 
/*     */ 
/*     */         
/* 693 */         genericLoader = getClassLoaderFromJar(genericJarFile);
/*     */         
/* 695 */         for (String filepath : genericEntries.keySet()) {
/* 696 */           if (!wlEntries.containsKey(filepath)) {
/*     */             
/* 698 */             log("File " + filepath + " not present in weblogic jar", 3);
/*     */             
/* 700 */             rebuild = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 705 */           JarEntry genericEntry = genericEntries.get(filepath);
/* 706 */           JarEntry wlEntry = wlEntries.get(filepath);
/*     */           
/* 708 */           if (genericEntry.getCrc() != wlEntry.getCrc() || genericEntry
/* 709 */             .getSize() != wlEntry.getSize()) {
/*     */             
/* 711 */             if (genericEntry.getName().endsWith(".class")) {
/*     */ 
/*     */ 
/*     */               
/* 715 */               String classname = genericEntry.getName().replace(File.separatorChar, '.').replace('/', '.');
/*     */               
/* 717 */               classname = classname.substring(0, classname.lastIndexOf(".class"));
/*     */               
/* 719 */               Class<?> genclass = genericLoader.loadClass(classname);
/*     */               
/* 721 */               if (genclass.isInterface()) {
/*     */                 
/* 723 */                 log("Interface " + genclass.getName() + " has changed", 3);
/*     */                 
/* 725 */                 rebuild = true;
/*     */                 
/*     */                 break;
/*     */               } 
/* 729 */               replaceEntries.put(filepath, genericEntry); continue;
/* 730 */             }  if (!genericEntry.getName().equals("META-INF/MANIFEST.MF")) {
/*     */ 
/*     */               
/* 733 */               log("Non class file " + genericEntry.getName() + " has changed", 3);
/*     */               
/* 735 */               rebuild = true;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 741 */         if (!rebuild) {
/* 742 */           log("No rebuild needed - updating jar", 3);
/* 743 */           newWLJarFile = new File(weblogicJarFile.getAbsolutePath() + ".temp");
/* 744 */           if (newWLJarFile.exists()) {
/* 745 */             newWLJarFile.delete();
/*     */           }
/*     */           
/* 748 */           newJarStream = new JarOutputStream(Files.newOutputStream(newWLJarFile.toPath(), new java.nio.file.OpenOption[0]));
/* 749 */           newJarStream.setLevel(0);
/*     */ 
/*     */           
/* 752 */           for (JarEntry je : wlEntries.values()) {
/* 753 */             InputStream is; if (je.getCompressedSize() == -1L || je
/* 754 */               .getCompressedSize() == je.getSize()) {
/* 755 */               newJarStream.setLevel(0);
/*     */             } else {
/* 757 */               newJarStream.setLevel(9);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 762 */             if (replaceEntries.containsKey(je.getName())) {
/* 763 */               log("Updating Bean class from generic Jar " + je
/* 764 */                   .getName(), 3);
/*     */               
/* 766 */               je = replaceEntries.get(je.getName());
/* 767 */               is = genericJar.getInputStream(je);
/*     */             }
/*     */             else {
/*     */               
/* 771 */               is = wlJar.getInputStream(je);
/*     */             } 
/* 773 */             newJarStream.putNextEntry(new JarEntry(je.getName()));
/*     */             
/* 775 */             byte[] buffer = new byte[1024];
/*     */             int bytesRead;
/* 777 */             while ((bytesRead = is.read(buffer)) != -1) {
/* 778 */               newJarStream.write(buffer, 0, bytesRead);
/*     */             }
/* 780 */             is.close();
/*     */           } 
/*     */         } else {
/* 783 */           log("Weblogic Jar rebuild needed due to changed interface or XML", 3);
/*     */         } 
/*     */       } else {
/*     */         
/* 787 */         rebuild = true;
/*     */       } 
/* 789 */     } catch (ClassNotFoundException cnfe) {
/*     */ 
/*     */       
/* 792 */       String cnfmsg = "ClassNotFoundException while processing ejb-jar file. Details: " + cnfe.getMessage();
/*     */       
/* 794 */       throw new BuildException(cnfmsg, cnfe);
/* 795 */     } catch (IOException ioe) {
/*     */ 
/*     */       
/* 798 */       String msg = "IOException while processing ejb-jar file . Details: " + ioe.getMessage();
/*     */       
/* 800 */       throw new BuildException(msg, ioe);
/*     */     } finally {
/* 802 */       FileUtils.close(genericJar);
/* 803 */       FileUtils.close(wlJar);
/* 804 */       FileUtils.close(newJarStream);
/*     */       
/* 806 */       if (newJarStream != null) {
/*     */         try {
/* 808 */           FILE_UTILS.rename(newWLJarFile, weblogicJarFile);
/* 809 */         } catch (IOException renameException) {
/* 810 */           log(renameException.getMessage(), 1);
/* 811 */           rebuild = true;
/*     */         } 
/*     */       }
/* 814 */       if (genericLoader instanceof AntClassLoader) {
/*     */         
/* 816 */         AntClassLoader loader = (AntClassLoader)genericLoader;
/* 817 */         loader.cleanup();
/*     */       } 
/*     */     } 
/* 820 */     return rebuild;
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
/*     */   protected ClassLoader getClassLoaderFromJar(File classjar) throws IOException {
/* 833 */     Path lookupPath = new Path(getTask().getProject());
/*     */     
/* 835 */     lookupPath.setLocation(classjar);
/*     */     
/* 837 */     Path classpath = getCombinedClasspath();
/*     */     
/* 839 */     if (classpath != null) {
/* 840 */       lookupPath.append(classpath);
/*     */     }
/* 842 */     return (ClassLoader)getTask().getProject().createClassLoader(lookupPath);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/WeblogicDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */