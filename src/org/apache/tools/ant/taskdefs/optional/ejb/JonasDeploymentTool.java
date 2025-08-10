/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Arrays;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.Java;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JonasDeploymentTool
/*     */   extends GenericDeploymentTool
/*     */ {
/*     */   protected static final String EJB_JAR_1_1_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN";
/*     */   protected static final String EJB_JAR_2_0_PUBLIC_ID = "-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN";
/*     */   protected static final String JONAS_EJB_JAR_2_4_PUBLIC_ID = "-//ObjectWeb//DTD JOnAS 2.4//EN";
/*     */   protected static final String JONAS_EJB_JAR_2_5_PUBLIC_ID = "-//ObjectWeb//DTD JOnAS 2.5//EN";
/*     */   protected static final String RMI_ORB = "RMI";
/*     */   protected static final String JEREMIE_ORB = "JEREMIE";
/*     */   protected static final String DAVID_ORB = "DAVID";
/*     */   protected static final String EJB_JAR_1_1_DTD = "ejb-jar_1_1.dtd";
/*     */   protected static final String EJB_JAR_2_0_DTD = "ejb-jar_2_0.dtd";
/*     */   protected static final String JONAS_EJB_JAR_2_4_DTD = "jonas-ejb-jar_2_4.dtd";
/*     */   protected static final String JONAS_EJB_JAR_2_5_DTD = "jonas-ejb-jar_2_5.dtd";
/*     */   protected static final String JONAS_DD = "jonas-ejb-jar.xml";
/*     */   protected static final String GENIC_CLASS = "org.objectweb.jonas_ejb.genic.GenIC";
/*     */   protected static final String OLD_GENIC_CLASS_1 = "org.objectweb.jonas_ejb.tools.GenWholeIC";
/*     */   protected static final String OLD_GENIC_CLASS_2 = "org.objectweb.jonas_ejb.tools.GenIC";
/*     */   private String descriptorName;
/*     */   private String jonasDescriptorName;
/*     */   private File outputdir;
/*     */   private boolean keepgenerated = false;
/*     */   private boolean nocompil = false;
/*     */   private boolean novalidation = false;
/*     */   private String javac;
/*     */   private String javacopts;
/*     */   private String rmicopts;
/*     */   private boolean secpropag = false;
/*     */   private boolean verbose = false;
/*     */   private String additionalargs;
/*     */   private File jonasroot;
/*     */   private boolean keepgeneric = false;
/* 179 */   private String suffix = ".jar";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String orb;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean nogenic = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepgenerated(boolean aBoolean) {
/* 204 */     this.keepgenerated = aBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAdditionalargs(String aString) {
/* 213 */     this.additionalargs = aString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNocompil(boolean aBoolean) {
/* 222 */     this.nocompil = aBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNovalidation(boolean aBoolean) {
/* 231 */     this.novalidation = aBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavac(String aString) {
/* 240 */     this.javac = aString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavacopts(String aString) {
/* 249 */     this.javacopts = aString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRmicopts(String aString) {
/* 258 */     this.rmicopts = aString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSecpropag(boolean aBoolean) {
/* 267 */     this.secpropag = aBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean aBoolean) {
/* 276 */     this.verbose = aBoolean;
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
/*     */   public void setJonasroot(File aFile) {
/* 289 */     this.jonasroot = aFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepgeneric(boolean aBoolean) {
/* 298 */     this.keepgeneric = aBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJarsuffix(String aString) {
/* 307 */     this.suffix = aString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOrb(String aString) {
/* 316 */     this.orb = aString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNogenic(boolean aBoolean) {
/* 325 */     this.nogenic = aBoolean;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processDescriptor(String aDescriptorName, SAXParser saxParser) {
/* 336 */     this.descriptorName = aDescriptorName;
/*     */     
/* 338 */     log("JOnAS Deployment Tool processing: " + this.descriptorName, 3);
/*     */ 
/*     */     
/* 341 */     super.processDescriptor(this.descriptorName, saxParser);
/*     */     
/* 343 */     if (this.outputdir != null) {
/*     */       
/* 345 */       log("Deleting temp output directory '" + this.outputdir + "'.", 3);
/* 346 */       deleteAllFiles(this.outputdir);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeJar(String baseName, File jarfile, Hashtable<String, File> ejbFiles, String publicId) throws BuildException {
/* 356 */     File genericJarFile = super.getVendorOutputJarFile(baseName);
/* 357 */     super.writeJar(baseName, genericJarFile, ejbFiles, publicId);
/*     */ 
/*     */     
/* 360 */     addGenICGeneratedFiles(genericJarFile, ejbFiles);
/*     */ 
/*     */     
/* 363 */     super.writeJar(baseName, getVendorOutputJarFile(baseName), ejbFiles, publicId);
/*     */     
/* 365 */     if (!this.keepgeneric) {
/* 366 */       log("Deleting generic JAR " + genericJarFile.toString(), 3);
/* 367 */       genericJarFile.delete();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
/* 376 */     this.jonasDescriptorName = getJonasDescriptorName();
/* 377 */     File jonasDD = new File((getConfig()).descriptorDir, this.jonasDescriptorName);
/*     */     
/* 379 */     if (jonasDD.exists()) {
/* 380 */       ejbFiles.put("META-INF/jonas-ejb-jar.xml", jonasDD);
/*     */     } else {
/* 382 */       log("Unable to locate the JOnAS deployment descriptor. It was expected to be in: " + jonasDD
/* 383 */           .getPath() + ".", 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected File getVendorOutputJarFile(String baseName) {
/* 390 */     return new File(getDestDir(), baseName + this.suffix);
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
/*     */   private String getJonasDescriptorName() {
/*     */     String jonasDN, path, fileName;
/* 408 */     boolean jonasConvention = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 414 */     int startOfFileName = this.descriptorName.lastIndexOf(File.separatorChar);
/* 415 */     if (startOfFileName != -1) {
/*     */       
/* 417 */       path = this.descriptorName.substring(0, startOfFileName + 1);
/* 418 */       fileName = this.descriptorName.substring(startOfFileName + 1);
/*     */     } else {
/*     */       
/* 421 */       path = "";
/* 422 */       fileName = this.descriptorName;
/*     */     } 
/*     */     
/* 425 */     if (fileName.startsWith("ejb-jar.xml")) {
/* 426 */       return path + "jonas-ejb-jar.xml";
/*     */     }
/*     */     
/* 429 */     int endOfBaseName = this.descriptorName.indexOf((getConfig()).baseNameTerminator, startOfFileName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 436 */     if (endOfBaseName < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 441 */       endOfBaseName = this.descriptorName.lastIndexOf('.') - 1;
/* 442 */       if (endOfBaseName < 0)
/*     */       {
/* 444 */         endOfBaseName = this.descriptorName.length() - 1;
/*     */       }
/*     */       
/* 447 */       jonasConvention = true;
/*     */     } 
/*     */     
/* 450 */     String baseName = this.descriptorName.substring(startOfFileName + 1, endOfBaseName + 1);
/* 451 */     String remainder = this.descriptorName.substring(endOfBaseName + 1);
/*     */     
/* 453 */     if (jonasConvention) {
/* 454 */       jonasDN = path + "jonas-" + baseName + ".xml";
/*     */     } else {
/* 456 */       jonasDN = path + baseName + "jonas-" + remainder;
/*     */     } 
/*     */     
/* 459 */     log("Standard EJB descriptor name: " + this.descriptorName, 3);
/* 460 */     log("JOnAS-specific descriptor name: " + jonasDN, 3);
/*     */     
/* 462 */     return jonasDN;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getJarBaseName(String descriptorFileName) {
/* 469 */     String baseName = null;
/*     */     
/* 471 */     if ((getConfig()).namingScheme.getValue().equals("descriptor"))
/*     */     {
/*     */       
/* 474 */       if (!descriptorFileName.contains((getConfig()).baseNameTerminator)) {
/*     */         int endOfBaseName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 481 */         String aCanonicalDescriptor = descriptorFileName.replace('\\', '/');
/* 482 */         int lastSeparatorIndex = aCanonicalDescriptor.lastIndexOf('/');
/*     */ 
/*     */         
/* 485 */         if (lastSeparatorIndex != -1) {
/* 486 */           endOfBaseName = descriptorFileName.indexOf(".xml", lastSeparatorIndex);
/*     */         } else {
/* 488 */           endOfBaseName = descriptorFileName.indexOf(".xml");
/*     */         } 
/*     */         
/* 491 */         if (endOfBaseName != -1) {
/* 492 */           baseName = descriptorFileName.substring(0, endOfBaseName);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 497 */     if (baseName == null)
/*     */     {
/* 499 */       baseName = super.getJarBaseName(descriptorFileName);
/*     */     }
/*     */     
/* 502 */     log("JAR base name: " + baseName, 3);
/*     */     
/* 504 */     return baseName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerKnownDTDs(DescriptorHandler handler) {
/* 510 */     handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN", this.jonasroot + File.separator + "xml" + File.separator + "ejb-jar_1_1.dtd");
/*     */     
/* 512 */     handler.registerDTD("-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN", this.jonasroot + File.separator + "xml" + File.separator + "ejb-jar_2_0.dtd");
/*     */ 
/*     */     
/* 515 */     handler.registerDTD("-//ObjectWeb//DTD JOnAS 2.4//EN", this.jonasroot + File.separator + "xml" + File.separator + "jonas-ejb-jar_2_4.dtd");
/*     */     
/* 517 */     handler.registerDTD("-//ObjectWeb//DTD JOnAS 2.5//EN", this.jonasroot + File.separator + "xml" + File.separator + "jonas-ejb-jar_2_5.dtd");
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
/*     */   private void addGenICGeneratedFiles(File genericJarFile, Hashtable<String, File> ejbFiles) {
/* 529 */     if (this.nogenic) {
/*     */       return;
/*     */     }
/*     */     
/* 533 */     Java genicTask = new Java(getTask());
/* 534 */     genicTask.setTaskName("genic");
/* 535 */     genicTask.setFork(true);
/*     */ 
/*     */     
/* 538 */     genicTask.createJvmarg().setValue("-Dinstall.root=" + this.jonasroot);
/*     */ 
/*     */     
/* 541 */     String jonasConfigDir = this.jonasroot + File.separator + "config";
/* 542 */     File javaPolicyFile = new File(jonasConfigDir, "java.policy");
/* 543 */     if (javaPolicyFile.exists()) {
/* 544 */       genicTask.createJvmarg().setValue("-Djava.security.policy=" + javaPolicyFile
/* 545 */           .toString());
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 550 */       this.outputdir = createTempDir();
/* 551 */     } catch (IOException aIOException) {
/* 552 */       String msg = "Cannot create temp dir: " + aIOException.getMessage();
/* 553 */       throw new BuildException(msg, aIOException);
/*     */     } 
/* 555 */     log("Using temporary output directory: " + this.outputdir, 3);
/*     */     
/* 557 */     genicTask.createArg().setValue("-d");
/* 558 */     genicTask.createArg().setFile(this.outputdir);
/*     */     
/* 560 */     for (String key : ejbFiles.keySet()) {
/* 561 */       File f = new File(this.outputdir + File.separator + key);
/* 562 */       f.getParentFile().mkdirs();
/*     */     } 
/* 564 */     log("Worked around a bug of GenIC 2.5.", 3);
/*     */ 
/*     */     
/* 567 */     Path classpath = getCombinedClasspath();
/* 568 */     if (classpath == null) {
/* 569 */       classpath = new Path(getTask().getProject());
/*     */     }
/* 571 */     classpath.append(new Path(classpath.getProject(), jonasConfigDir));
/* 572 */     classpath.append(new Path(classpath.getProject(), this.outputdir.toString()));
/*     */ 
/*     */     
/* 575 */     if (this.orb != null) {
/* 576 */       String orbJar = this.jonasroot + File.separator + "lib" + File.separator + this.orb + "_jonas.jar";
/*     */       
/* 578 */       classpath.append(new Path(classpath.getProject(), orbJar));
/*     */     } 
/* 580 */     log("Using classpath: " + classpath.toString(), 3);
/* 581 */     genicTask.setClasspath(classpath);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 588 */     String genicClass = getGenicClassName(classpath);
/* 589 */     if (genicClass == null) {
/* 590 */       log("Cannot find GenIC class in classpath.", 0);
/* 591 */       throw new BuildException("GenIC class not found, please check the classpath.");
/*     */     } 
/* 593 */     log("Using '" + genicClass + "' GenIC class.", 3);
/* 594 */     genicTask.setClassname(genicClass);
/*     */ 
/*     */     
/* 597 */     if (this.keepgenerated) {
/* 598 */       genicTask.createArg().setValue("-keepgenerated");
/*     */     }
/*     */ 
/*     */     
/* 602 */     if (this.nocompil) {
/* 603 */       genicTask.createArg().setValue("-nocompil");
/*     */     }
/*     */ 
/*     */     
/* 607 */     if (this.novalidation) {
/* 608 */       genicTask.createArg().setValue("-novalidation");
/*     */     }
/*     */ 
/*     */     
/* 612 */     if (this.javac != null) {
/* 613 */       genicTask.createArg().setValue("-javac");
/* 614 */       genicTask.createArg().setLine(this.javac);
/*     */     } 
/*     */ 
/*     */     
/* 618 */     if (this.javacopts != null && !this.javacopts.isEmpty()) {
/* 619 */       genicTask.createArg().setValue("-javacopts");
/* 620 */       genicTask.createArg().setLine(this.javacopts);
/*     */     } 
/*     */ 
/*     */     
/* 624 */     if (this.rmicopts != null && !this.rmicopts.isEmpty()) {
/* 625 */       genicTask.createArg().setValue("-rmicopts");
/* 626 */       genicTask.createArg().setLine(this.rmicopts);
/*     */     } 
/*     */ 
/*     */     
/* 630 */     if (this.secpropag) {
/* 631 */       genicTask.createArg().setValue("-secpropag");
/*     */     }
/*     */ 
/*     */     
/* 635 */     if (this.verbose) {
/* 636 */       genicTask.createArg().setValue("-verbose");
/*     */     }
/*     */ 
/*     */     
/* 640 */     if (this.additionalargs != null) {
/* 641 */       genicTask.createArg().setValue(this.additionalargs);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 646 */     genicTask.createArg().setValue("-noaddinjar");
/*     */ 
/*     */     
/* 649 */     genicTask.createArg().setValue(genericJarFile.getPath());
/*     */ 
/*     */     
/* 652 */     log("Calling " + genicClass + " for " + (getConfig()).descriptorDir + File.separator + this.descriptorName + ".", 3);
/*     */ 
/*     */     
/* 655 */     if (genicTask.executeJava() != 0) {
/*     */ 
/*     */       
/* 658 */       log("Deleting temp output directory '" + this.outputdir + "'.", 3);
/* 659 */       deleteAllFiles(this.outputdir);
/*     */       
/* 661 */       if (!this.keepgeneric) {
/* 662 */         log("Deleting generic JAR " + genericJarFile.toString(), 3);
/*     */         
/* 664 */         genericJarFile.delete();
/*     */       } 
/*     */       
/* 667 */       throw new BuildException("GenIC reported an error.");
/*     */     } 
/*     */ 
/*     */     
/* 671 */     addAllFiles(this.outputdir, "", ejbFiles);
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
/*     */   String getGenicClassName(Path classpath) {
/* 683 */     log("Looking for GenIC class in classpath: " + classpath
/* 684 */         .toString(), 3);
/*     */     
/* 686 */     AntClassLoader cl = classpath.getProject().createClassLoader(classpath);
/*     */ 
/*     */     
/* 689 */     try { cl.loadClass("org.objectweb.jonas_ejb.genic.GenIC");
/* 690 */       log("Found GenIC class 'org.objectweb.jonas_ejb.genic.GenIC' in classpath.", 3);
/*     */       
/* 692 */       String str = "org.objectweb.jonas_ejb.genic.GenIC";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 725 */       if (cl != null) cl.close();  return str; } catch (ClassNotFoundException cnf1) { log("GenIC class 'org.objectweb.jonas_ejb.genic.GenIC' not found in classpath.", 3); try { cl.loadClass("org.objectweb.jonas_ejb.tools.GenWholeIC"); log("Found GenIC class 'org.objectweb.jonas_ejb.tools.GenWholeIC' in classpath.", 3); String str = "org.objectweb.jonas_ejb.tools.GenWholeIC"; if (cl != null) cl.close();  return str; } catch (ClassNotFoundException cnf2) { log("GenIC class 'org.objectweb.jonas_ejb.tools.GenWholeIC' not found in classpath.", 3); try { cl.loadClass("org.objectweb.jonas_ejb.tools.GenIC"); log("Found GenIC class 'org.objectweb.jonas_ejb.tools.GenIC' in classpath.", 3); String str = "org.objectweb.jonas_ejb.tools.GenIC"; if (cl != null) cl.close();  return str; } catch (ClassNotFoundException cnf3) { log("GenIC class 'org.objectweb.jonas_ejb.tools.GenIC' not found in classpath.", 3); if (cl != null) cl.close();  }  }  } catch (Throwable throwable) { if (cl != null)
/* 726 */         try { cl.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  return null;
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
/*     */   protected void checkConfiguration(String descriptorFileName, SAXParser saxParser) throws BuildException {
/* 740 */     if (this.jonasroot == null) {
/* 741 */       throw new BuildException("The jonasroot attribute is not set.");
/*     */     }
/* 743 */     if (!this.jonasroot.isDirectory()) {
/* 744 */       throw new BuildException("The jonasroot attribute '%s' is not a valid directory.", new Object[] { this.jonasroot });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 751 */     List<String> validOrbs = Arrays.asList(new String[] { "RMI", "JEREMIE", "DAVID" });
/*     */     
/* 753 */     if (this.orb != null && !validOrbs.contains(this.orb)) {
/* 754 */       throw new BuildException("The orb attribute '%s' is not valid (must be one of %s.", new Object[] { this.orb, validOrbs });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 760 */     if (this.additionalargs != null && this.additionalargs.isEmpty()) {
/* 761 */       throw new BuildException("Empty additionalargs attribute.");
/*     */     }
/*     */ 
/*     */     
/* 765 */     if (this.javac != null && this.javac.isEmpty()) {
/* 766 */       throw new BuildException("Empty javac attribute.");
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
/*     */   private File createTempDir() throws IOException {
/* 781 */     return Files.createTempDirectory("genic", (FileAttribute<?>[])new FileAttribute[0]).toFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deleteAllFiles(File aFile) {
/* 791 */     if (aFile.isDirectory()) {
/* 792 */       for (File child : aFile.listFiles()) {
/* 793 */         deleteAllFiles(child);
/*     */       }
/*     */     }
/* 796 */     aFile.delete();
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
/*     */   private void addAllFiles(File file, String rootDir, Hashtable<String, File> hashtable) {
/* 808 */     if (!file.exists()) {
/* 809 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 812 */     if (file.isDirectory()) {
/* 813 */       for (File child : file.listFiles()) {
/* 814 */         String newRootDir; if (rootDir.isEmpty()) {
/* 815 */           newRootDir = child.getName();
/*     */         } else {
/* 817 */           newRootDir = rootDir + File.separator + child.getName();
/*     */         } 
/* 819 */         addAllFiles(child, newRootDir, hashtable);
/*     */       } 
/*     */     } else {
/* 822 */       hashtable.put(rootDir, file);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/JonasDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */