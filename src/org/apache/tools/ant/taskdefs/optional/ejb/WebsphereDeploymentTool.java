/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.zip.ZipEntry;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Java;
/*     */ import org.apache.tools.ant.types.Environment;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebsphereDeploymentTool
/*     */   extends GenericDeploymentTool
/*     */ {
/*     */   public static final String PUBLICID_EJB11 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";
/*     */   public static final String PUBLICID_EJB20 = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN";
/*     */   protected static final String SCHEMA_DIR = "Schema/";
/*     */   protected static final String WAS_EXT = "ibm-ejb-jar-ext.xmi";
/*     */   protected static final String WAS_BND = "ibm-ejb-jar-bnd.xmi";
/*     */   protected static final String WAS_CMP_MAP = "Map.mapxmi";
/*     */   protected static final String WAS_CMP_SCHEMA = "Schema.dbxmi";
/*  81 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/*  84 */   private String jarSuffix = ".jar";
/*     */ 
/*     */   
/*     */   private String ejb11DTD;
/*     */ 
/*     */   
/*     */   private boolean keepGeneric = false;
/*     */ 
/*     */   
/*     */   private boolean alwaysRebuild = true;
/*     */ 
/*     */   
/*     */   private boolean ejbdeploy = true;
/*     */ 
/*     */   
/*     */   private boolean newCMP = false;
/*     */   
/* 101 */   private Path wasClasspath = null;
/*     */ 
/*     */   
/*     */   private String dbVendor;
/*     */ 
/*     */   
/*     */   private String dbName;
/*     */ 
/*     */   
/*     */   private String dbSchema;
/*     */ 
/*     */   
/*     */   private boolean codegen;
/*     */ 
/*     */   
/*     */   private boolean quiet = true;
/*     */ 
/*     */   
/*     */   private boolean novalidate;
/*     */ 
/*     */   
/*     */   private boolean nowarn;
/*     */ 
/*     */   
/*     */   private boolean noinform;
/*     */ 
/*     */   
/*     */   private boolean trace;
/*     */ 
/*     */   
/*     */   private String rmicOptions;
/*     */ 
/*     */   
/*     */   private boolean use35MappingRules;
/*     */ 
/*     */   
/* 137 */   private String tempdir = "_ejbdeploy_temp";
/*     */ 
/*     */ 
/*     */   
/*     */   private File websphereHome;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createWASClasspath() {
/* 147 */     if (this.wasClasspath == null) {
/* 148 */       this.wasClasspath = new Path(getTask().getProject());
/*     */     }
/* 150 */     return this.wasClasspath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWASClasspath(Path wasClasspath) {
/* 158 */     this.wasClasspath = wasClasspath;
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
/*     */   public void setDbvendor(String dbvendor) {
/* 177 */     this.dbVendor = dbvendor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDbname(String dbName) {
/* 186 */     this.dbName = dbName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDbschema(String dbSchema) {
/* 195 */     this.dbSchema = dbSchema;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCodegen(boolean codegen) {
/* 205 */     this.codegen = codegen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuiet(boolean quiet) {
/* 214 */     this.quiet = quiet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNovalidate(boolean novalidate) {
/* 223 */     this.novalidate = novalidate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNowarn(boolean nowarn) {
/* 232 */     this.nowarn = nowarn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoinform(boolean noinform) {
/* 241 */     this.noinform = noinform;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrace(boolean trace) {
/* 250 */     this.trace = trace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRmicoptions(String options) {
/* 259 */     this.rmicOptions = options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUse35(boolean attr) {
/* 268 */     this.use35MappingRules = attr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRebuild(boolean rebuild) {
/* 277 */     this.alwaysRebuild = rebuild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String inString) {
/* 287 */     this.jarSuffix = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepgeneric(boolean inValue) {
/* 296 */     this.keepGeneric = inValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEjbdeploy(boolean ejbdeploy) {
/* 306 */     this.ejbdeploy = ejbdeploy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEJBdtd(String inString) {
/* 316 */     this.ejb11DTD = inString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOldCMP(boolean oldCMP) {
/* 325 */     this.newCMP = !oldCMP;
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
/*     */   public void setNewCMP(boolean newCMP) {
/* 338 */     this.newCMP = newCMP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTempdir(String tempdir) {
/* 347 */     this.tempdir = tempdir;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected DescriptorHandler getDescriptorHandler(File srcDir) {
/* 353 */     DescriptorHandler handler = new DescriptorHandler(getTask(), srcDir);
/*     */ 
/*     */     
/* 356 */     handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", this.ejb11DTD);
/*     */     
/* 358 */     (getConfig()).dtdLocations.forEach(l -> handler.registerDTD(l.getPublicId(), l.getLocation()));
/* 359 */     return handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DescriptorHandler getWebsphereDescriptorHandler(File srcDir) {
/* 369 */     DescriptorHandler handler = new DescriptorHandler(getTask(), srcDir)
/*     */       {
/*     */         protected void processElement() {}
/*     */       };
/*     */ 
/*     */     
/* 375 */     (getConfig()).dtdLocations.forEach(l -> handler.registerDTD(l.getPublicId(), l.getLocation()));
/* 376 */     return handler;
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
/*     */   protected void addVendorFiles(Hashtable<String, File> ejbFiles, String baseName) {
/* 388 */     String ddPrefix = usingBaseJarName() ? "" : baseName;
/* 389 */     String dbPrefix = (this.dbVendor == null) ? "" : (this.dbVendor + "-");
/*     */ 
/*     */     
/* 392 */     File websphereEXT = new File((getConfig()).descriptorDir, ddPrefix + "ibm-ejb-jar-ext.xmi");
/*     */     
/* 394 */     if (websphereEXT.exists()) {
/* 395 */       ejbFiles.put("META-INF/ibm-ejb-jar-ext.xmi", websphereEXT);
/*     */     } else {
/*     */       
/* 398 */       log("Unable to locate websphere extensions. It was expected to be in " + websphereEXT
/* 399 */           .getPath(), 3);
/*     */     } 
/*     */     
/* 402 */     File websphereBND = new File((getConfig()).descriptorDir, ddPrefix + "ibm-ejb-jar-bnd.xmi");
/*     */     
/* 404 */     if (websphereBND.exists()) {
/* 405 */       ejbFiles.put("META-INF/ibm-ejb-jar-bnd.xmi", websphereBND);
/*     */     } else {
/*     */       
/* 408 */       log("Unable to locate websphere bindings. It was expected to be in " + websphereBND
/* 409 */           .getPath(), 3);
/*     */     } 
/*     */     
/* 412 */     if (!this.newCMP) {
/* 413 */       log("The old method for locating CMP files has been DEPRECATED.", 3);
/*     */       
/* 415 */       log("Please adjust your websphere descriptor and set newCMP=\"true\" to use the new CMP descriptor inclusion mechanism. ", 3);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 421 */         File websphereMAP = new File((getConfig()).descriptorDir, ddPrefix + dbPrefix + "Map.mapxmi");
/*     */ 
/*     */         
/* 424 */         if (websphereMAP.exists()) {
/* 425 */           ejbFiles.put("META-INF/Map.mapxmi", websphereMAP);
/*     */         } else {
/*     */           
/* 428 */           log("Unable to locate the websphere Map: " + websphereMAP
/* 429 */               .getPath(), 3);
/*     */         } 
/*     */         
/* 432 */         File websphereSchema = new File((getConfig()).descriptorDir, ddPrefix + dbPrefix + "Schema.dbxmi");
/*     */ 
/*     */         
/* 435 */         if (websphereSchema.exists()) {
/* 436 */           ejbFiles.put("META-INF/Schema/Schema.dbxmi", websphereSchema);
/*     */         } else {
/*     */           
/* 439 */           log("Unable to locate the websphere Schema: " + websphereSchema
/* 440 */               .getPath(), 3);
/*     */         }
/*     */       
/* 443 */       } catch (Exception e) {
/* 444 */         throw new BuildException("Exception while adding Vendor specific files: " + e
/*     */             
/* 446 */             .toString(), e);
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
/* 459 */     return new File(getDestDir(), baseName + this.jarSuffix);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getOptions() {
/* 469 */     StringBuilder options = new StringBuilder();
/*     */     
/* 471 */     if (this.dbVendor != null) {
/* 472 */       options.append(" -dbvendor ").append(this.dbVendor);
/*     */     }
/* 474 */     if (this.dbName != null) {
/* 475 */       options.append(" -dbname \"").append(this.dbName).append("\"");
/*     */     }
/*     */     
/* 478 */     if (this.dbSchema != null) {
/* 479 */       options.append(" -dbschema \"").append(this.dbSchema).append("\"");
/*     */     }
/*     */     
/* 482 */     if (this.codegen) {
/* 483 */       options.append(" -codegen");
/*     */     }
/*     */     
/* 486 */     if (this.quiet) {
/* 487 */       options.append(" -quiet");
/*     */     }
/*     */     
/* 490 */     if (this.novalidate) {
/* 491 */       options.append(" -novalidate");
/*     */     }
/*     */     
/* 494 */     if (this.nowarn) {
/* 495 */       options.append(" -nowarn");
/*     */     }
/*     */     
/* 498 */     if (this.noinform) {
/* 499 */       options.append(" -noinform");
/*     */     }
/*     */     
/* 502 */     if (this.trace) {
/* 503 */       options.append(" -trace");
/*     */     }
/*     */     
/* 506 */     if (this.use35MappingRules) {
/* 507 */       options.append(" -35");
/*     */     }
/*     */     
/* 510 */     if (this.rmicOptions != null) {
/* 511 */       options.append(" -rmic \"").append(this.rmicOptions).append("\"");
/*     */     }
/*     */     
/* 514 */     return options.toString();
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
/*     */   private void buildWebsphereJar(File sourceJar, File destJar) {
/*     */     try {
/* 528 */       if (this.ejbdeploy) {
/* 529 */         Java javaTask = new Java(getTask());
/*     */         
/* 531 */         javaTask.createJvmarg().setValue("-Xms64m");
/* 532 */         javaTask.createJvmarg().setValue("-Xmx128m");
/*     */ 
/*     */         
/* 535 */         Environment.Variable var = new Environment.Variable();
/*     */         
/* 537 */         var.setKey("websphere.lib.dir");
/* 538 */         File libdir = new File(this.websphereHome, "lib");
/* 539 */         var.setValue(libdir.getAbsolutePath());
/* 540 */         javaTask.addSysproperty(var);
/*     */ 
/*     */         
/* 543 */         javaTask.setDir(this.websphereHome);
/*     */ 
/*     */         
/* 546 */         javaTask.setTaskName("ejbdeploy");
/* 547 */         javaTask.setClassname("com.ibm.etools.ejbdeploy.EJBDeploy");
/*     */         
/* 549 */         javaTask.createArg().setValue(sourceJar.getPath());
/* 550 */         javaTask.createArg().setValue(this.tempdir);
/* 551 */         javaTask.createArg().setValue(destJar.getPath());
/* 552 */         javaTask.createArg().setLine(getOptions());
/* 553 */         if (getCombinedClasspath() != null && 
/* 554 */           !getCombinedClasspath().toString().isEmpty()) {
/* 555 */           javaTask.createArg().setValue("-cp");
/* 556 */           javaTask.createArg().setValue(getCombinedClasspath().toString());
/*     */         } 
/*     */         
/* 559 */         Path classpath = this.wasClasspath;
/*     */         
/* 561 */         if (classpath == null) {
/* 562 */           classpath = getCombinedClasspath();
/*     */         }
/*     */         
/* 565 */         javaTask.setFork(true);
/* 566 */         if (classpath != null) {
/* 567 */           javaTask.setClasspath(classpath);
/*     */         }
/*     */         
/* 570 */         log("Calling websphere.ejbdeploy for " + sourceJar.toString(), 3);
/*     */ 
/*     */         
/* 573 */         javaTask.execute();
/*     */       } 
/* 575 */     } catch (Exception e) {
/*     */       
/* 577 */       throw new BuildException("Exception while calling ejbdeploy. Details: " + e
/* 578 */           .toString(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeJar(String baseName, File jarFile, Hashtable<String, File> files, String publicId) throws BuildException {
/* 587 */     if (this.ejbdeploy) {
/*     */       
/* 589 */       File genericJarFile = super.getVendorOutputJarFile(baseName);
/*     */       
/* 591 */       super.writeJar(baseName, genericJarFile, files, publicId);
/*     */ 
/*     */       
/* 594 */       if (this.alwaysRebuild || isRebuildRequired(genericJarFile, jarFile)) {
/* 595 */         buildWebsphereJar(genericJarFile, jarFile);
/*     */       }
/* 597 */       if (!this.keepGeneric) {
/* 598 */         log("deleting generic jar " + genericJarFile.toString(), 3);
/*     */         
/* 600 */         genericJarFile.delete();
/*     */       } 
/*     */     } else {
/*     */       
/* 604 */       super.writeJar(baseName, jarFile, files, publicId);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateConfigured() throws BuildException {
/* 614 */     super.validateConfigured();
/* 615 */     if (this.ejbdeploy) {
/* 616 */       String home = getTask().getProject().getProperty("websphere.home");
/* 617 */       if (home == null) {
/* 618 */         throw new BuildException("The 'websphere.home' property must be set when 'ejbdeploy=true'");
/*     */       }
/*     */       
/* 621 */       this.websphereHome = getTask().getProject().resolveFile(home);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isRebuildRequired(File genericJarFile, File websphereJarFile) {
/* 652 */     boolean rebuild = false;
/*     */     
/* 654 */     JarFile genericJar = null;
/* 655 */     JarFile wasJar = null;
/* 656 */     File newwasJarFile = null;
/* 657 */     JarOutputStream newJarStream = null;
/* 658 */     ClassLoader genericLoader = null;
/*     */     
/*     */     try {
/* 661 */       log("Checking if websphere Jar needs to be rebuilt for jar " + websphereJarFile
/* 662 */           .getName(), 3);
/*     */       
/* 664 */       if (genericJarFile.exists() && genericJarFile.isFile() && websphereJarFile
/* 665 */         .exists() && websphereJarFile.isFile()) {
/*     */         
/* 667 */         genericJar = new JarFile(genericJarFile);
/* 668 */         wasJar = new JarFile(websphereJarFile);
/*     */ 
/*     */ 
/*     */         
/* 672 */         Map<String, JarEntry> genericEntries = genericJar.stream().collect(Collectors.toMap(je -> je.getName().replace('\\', '/'), je -> je, (a, b) -> b));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 677 */         Map<String, JarEntry> wasEntries = wasJar.stream().collect(Collectors.toMap(ZipEntry::getName, je -> je, (a, b) -> b));
/*     */ 
/*     */         
/* 680 */         genericLoader = getClassLoaderFromJar(genericJarFile);
/*     */         
/* 682 */         Map<String, JarEntry> replaceEntries = new HashMap<>();
/* 683 */         for (String filepath : genericEntries.keySet()) {
/* 684 */           if (!wasEntries.containsKey(filepath)) {
/*     */             
/* 686 */             log("File " + filepath + " not present in websphere jar", 3);
/*     */             
/* 688 */             rebuild = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 693 */           JarEntry genericEntry = genericEntries.get(filepath);
/* 694 */           JarEntry wasEntry = wasEntries.get(filepath);
/*     */           
/* 696 */           if (genericEntry.getCrc() != wasEntry.getCrc() || genericEntry
/* 697 */             .getSize() != wasEntry.getSize()) {
/*     */             
/* 699 */             if (genericEntry.getName().endsWith(".class")) {
/*     */ 
/*     */               
/* 702 */               String classname = genericEntry.getName().replace(File.separatorChar, '.');
/*     */               
/* 704 */               classname = classname.substring(0, classname.lastIndexOf(".class"));
/*     */               
/* 706 */               Class<?> genclass = genericLoader.loadClass(classname);
/*     */               
/* 708 */               if (genclass.isInterface()) {
/*     */                 
/* 710 */                 log("Interface " + genclass.getName() + " has changed", 3);
/*     */                 
/* 712 */                 rebuild = true;
/*     */                 
/*     */                 break;
/*     */               } 
/* 716 */               replaceEntries.put(filepath, genericEntry);
/*     */               continue;
/*     */             } 
/* 719 */             if (!genericEntry.getName().equals("META-INF/MANIFEST.MF")) {
/*     */               
/* 721 */               log("Non class file " + genericEntry.getName() + " has changed", 3);
/*     */               
/* 723 */               rebuild = true;
/*     */             } 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/* 730 */         if (!rebuild) {
/* 731 */           log("No rebuild needed - updating jar", 3);
/* 732 */           newwasJarFile = new File(websphereJarFile.getAbsolutePath() + ".temp");
/* 733 */           if (newwasJarFile.exists()) {
/* 734 */             newwasJarFile.delete();
/*     */           }
/*     */           
/* 737 */           newJarStream = new JarOutputStream(Files.newOutputStream(newwasJarFile.toPath(), new java.nio.file.OpenOption[0]));
/* 738 */           newJarStream.setLevel(0);
/*     */ 
/*     */           
/* 741 */           for (JarEntry je : wasEntries.values()) {
/* 742 */             InputStream is; if (je.getCompressedSize() == -1L || je
/* 743 */               .getCompressedSize() == je.getSize()) {
/* 744 */               newJarStream.setLevel(0);
/*     */             } else {
/* 746 */               newJarStream.setLevel(9);
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 751 */             if (replaceEntries.containsKey(je.getName())) {
/* 752 */               log("Updating Bean class from generic Jar " + je.getName(), 3);
/*     */ 
/*     */               
/* 755 */               je = replaceEntries.get(je.getName());
/* 756 */               is = genericJar.getInputStream(je);
/*     */             }
/*     */             else {
/*     */               
/* 760 */               is = wasJar.getInputStream(je);
/*     */             } 
/* 762 */             newJarStream.putNextEntry(new JarEntry(je.getName()));
/*     */             
/* 764 */             byte[] buffer = new byte[1024];
/*     */             int bytesRead;
/* 766 */             while ((bytesRead = is.read(buffer)) != -1) {
/* 767 */               newJarStream.write(buffer, 0, bytesRead);
/*     */             }
/* 769 */             is.close();
/*     */           } 
/*     */         } else {
/* 772 */           log("websphere Jar rebuild needed due to changed interface or XML", 3);
/*     */         } 
/*     */       } else {
/*     */         
/* 776 */         rebuild = true;
/*     */       } 
/* 778 */     } catch (ClassNotFoundException cnfe) {
/* 779 */       throw new BuildException("ClassNotFoundException while processing ejb-jar file. Details: " + cnfe
/*     */           
/* 781 */           .getMessage(), cnfe);
/*     */     }
/* 783 */     catch (IOException ioe) {
/* 784 */       throw new BuildException("IOException while processing ejb-jar file . Details: " + ioe
/*     */           
/* 786 */           .getMessage(), ioe);
/*     */     }
/*     */     finally {
/*     */       
/* 790 */       FileUtils.close(genericJar);
/* 791 */       FileUtils.close(wasJar);
/* 792 */       FileUtils.close(newJarStream);
/*     */       
/* 794 */       if (newJarStream != null) {
/*     */         try {
/* 796 */           FILE_UTILS.rename(newwasJarFile, websphereJarFile);
/* 797 */         } catch (IOException renameException) {
/* 798 */           log(renameException.getMessage(), 1);
/* 799 */           rebuild = true;
/*     */         } 
/*     */       }
/* 802 */       if (genericLoader instanceof AntClassLoader) {
/*     */         
/* 804 */         AntClassLoader loader = (AntClassLoader)genericLoader;
/* 805 */         loader.cleanup();
/*     */       } 
/*     */     } 
/* 808 */     return rebuild;
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
/*     */   protected ClassLoader getClassLoaderFromJar(File classjar) throws IOException {
/* 820 */     Path lookupPath = new Path(getTask().getProject());
/*     */     
/* 822 */     lookupPath.setLocation(classjar);
/*     */     
/* 824 */     Path classpath = getCombinedClasspath();
/*     */     
/* 826 */     if (classpath != null) {
/* 827 */       lookupPath.append(classpath);
/*     */     }
/*     */     
/* 830 */     return (ClassLoader)getTask().getProject().createClassLoader(lookupPath);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/WebsphereDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */