/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Hashtable;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IPlanetDeploymentTool
/*     */   extends GenericDeploymentTool
/*     */ {
/*     */   private File iashome;
/*  89 */   private String jarSuffix = ".jar";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean keepgenerated = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean debug = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String descriptorName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String iasDescriptorName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String displayName;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String IAS_DD = "ias-ejb-jar.xml";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIashome(File iashome) {
/* 126 */     this.iashome = iashome;
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
/*     */   public void setKeepgenerated(boolean keepgenerated) {
/* 138 */     this.keepgenerated = keepgenerated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 148 */     this.debug = debug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSuffix(String jarSuffix) {
/* 158 */     this.jarSuffix = jarSuffix;
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
/*     */   public void setGenericJarSuffix(String inString) {
/* 170 */     log("Since a generic JAR file is not created during processing, the iPlanet Deployment Tool does not support the \"genericjarsuffix\" attribute.  It will be ignored.", 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processDescriptor(String descriptorName, SAXParser saxParser) {
/* 179 */     this.descriptorName = descriptorName;
/* 180 */     this.iasDescriptorName = null;
/*     */     
/* 182 */     log("iPlanet Deployment Tool processing: " + descriptorName + " (and " + 
/* 183 */         getIasDescriptorName() + ")", 3);
/*     */     
/* 185 */     super.processDescriptor(descriptorName, saxParser);
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
/*     */   protected void checkConfiguration(String descriptorFileName, SAXParser saxParser) throws BuildException {
/* 201 */     int startOfName = descriptorFileName.lastIndexOf(File.separatorChar) + 1;
/* 202 */     String stdXml = descriptorFileName.substring(startOfName);
/* 203 */     if (stdXml.equals("ejb-jar.xml") && (getConfig()).baseJarName == null) {
/* 204 */       throw new BuildException("No name specified for the completed JAR file.  The EJB descriptor should be prepended with the JAR name or it should be specified using the attribute \"basejarname\" in the \"ejbjar\" task.", 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 209 */           getLocation());
/*     */     }
/*     */ 
/*     */     
/* 213 */     File iasDescriptor = new File((getConfig()).descriptorDir, getIasDescriptorName());
/* 214 */     if (!iasDescriptor.exists() || !iasDescriptor.isFile()) {
/* 215 */       throw new BuildException("The iAS-specific EJB descriptor (" + iasDescriptor + ") was not found.", 
/* 216 */           getLocation());
/*     */     }
/*     */     
/* 219 */     if (this.iashome != null && !this.iashome.isDirectory()) {
/* 220 */       throw new BuildException("If \"iashome\" is specified, it must be a valid directory (it was set to " + this.iashome + ").", 
/*     */ 
/*     */           
/* 223 */           getLocation());
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
/*     */   
/*     */   protected Hashtable<String, File> parseEjbFiles(String descriptorFileName, SAXParser saxParser) throws IOException, SAXException {
/* 255 */     IPlanetEjbc ejbc = new IPlanetEjbc(new File((getConfig()).descriptorDir, descriptorFileName), new File((getConfig()).descriptorDir, getIasDescriptorName()), (getConfig()).srcDir, getCombinedClasspath().toString(), saxParser);
/*     */     
/* 257 */     ejbc.setRetainSource(this.keepgenerated);
/* 258 */     ejbc.setDebugOutput(this.debug);
/* 259 */     if (this.iashome != null) {
/* 260 */       ejbc.setIasHomeDir(this.iashome);
/*     */     }
/* 262 */     if ((getConfig()).dtdLocations != null) {
/* 263 */       for (EjbJar.DTDLocation dtdLocation : (getConfig()).dtdLocations) {
/* 264 */         ejbc.registerDTD(dtdLocation.getPublicId(), dtdLocation
/* 265 */             .getLocation());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 271 */       ejbc.execute();
/* 272 */     } catch (EjbcException e) {
/* 273 */       throw new BuildException("An error has occurred while trying to execute the iAS ejbc utility", e, 
/* 274 */           getLocation());
/*     */     } 
/*     */     
/* 277 */     this.displayName = ejbc.getDisplayName();
/* 278 */     Hashtable<String, File> files = ejbc.getEjbFiles();
/*     */ 
/*     */     
/* 281 */     String[] cmpDescriptors = ejbc.getCmpDescriptors();
/* 282 */     if (cmpDescriptors.length > 0) {
/* 283 */       File baseDir = (getConfig()).descriptorDir;
/*     */       
/* 285 */       int endOfPath = descriptorFileName.lastIndexOf(File.separator);
/* 286 */       String relativePath = descriptorFileName.substring(0, endOfPath + 1);
/*     */       
/* 288 */       for (String descriptor : cmpDescriptors) {
/* 289 */         int endOfCmp = descriptor.lastIndexOf('/');
/* 290 */         String cmpDescriptor = descriptor.substring(endOfCmp + 1);
/*     */         
/* 292 */         File cmpFile = new File(baseDir, relativePath + cmpDescriptor);
/* 293 */         if (!cmpFile.exists()) {
/* 294 */           throw new BuildException("The CMP descriptor file (" + cmpFile + ") could not be found.", 
/* 295 */               getLocation());
/*     */         }
/* 297 */         files.put(descriptor, cmpFile);
/*     */       } 
/*     */     } 
/* 300 */     return files;
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
/*     */   protected void addVendorFiles(Hashtable<String, File> ejbFiles, String ddPrefix) {
/* 313 */     ejbFiles.put("META-INF/ias-ejb-jar.xml", new File((getConfig()).descriptorDir, 
/* 314 */           getIasDescriptorName()));
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
/*     */   File getVendorOutputJarFile(String baseName) {
/* 328 */     File jarFile = new File(getDestDir(), baseName + this.jarSuffix);
/* 329 */     log("JAR file name: " + jarFile.toString(), 3);
/* 330 */     return jarFile;
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
/*     */   protected String getPublicId() {
/* 342 */     return null;
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
/*     */   private String getIasDescriptorName() {
/*     */     String basename, remainder;
/* 356 */     if (this.iasDescriptorName != null) {
/* 357 */       return this.iasDescriptorName;
/*     */     }
/*     */     
/* 360 */     String path = "";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 365 */     int startOfFileName = this.descriptorName.lastIndexOf(File.separatorChar);
/* 366 */     if (startOfFileName != -1) {
/* 367 */       path = this.descriptorName.substring(0, startOfFileName + 1);
/*     */     }
/*     */ 
/*     */     
/* 371 */     if (this.descriptorName.substring(startOfFileName + 1).equals("ejb-jar.xml")) {
/* 372 */       basename = "";
/* 373 */       remainder = "ejb-jar.xml";
/*     */     } else {
/*     */       
/* 376 */       int endOfBaseName = this.descriptorName.indexOf(
/* 377 */           (getConfig()).baseNameTerminator, startOfFileName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 384 */       if (endOfBaseName < 0) {
/* 385 */         endOfBaseName = this.descriptorName.lastIndexOf('.') - 1;
/* 386 */         if (endOfBaseName < 0) {
/* 387 */           endOfBaseName = this.descriptorName.length() - 1;
/*     */         }
/*     */       } 
/*     */       
/* 391 */       basename = this.descriptorName.substring(startOfFileName + 1, endOfBaseName + 1);
/*     */       
/* 393 */       remainder = this.descriptorName.substring(endOfBaseName + 1);
/*     */     } 
/*     */     
/* 396 */     this.iasDescriptorName = path + basename + "ias-" + remainder;
/* 397 */     return this.iasDescriptorName;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/IPlanetDeploymentTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */