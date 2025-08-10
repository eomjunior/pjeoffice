/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.types.EnumeratedAttribute;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
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
/*     */ public class EjbJar
/*     */   extends MatchingTask
/*     */ {
/*     */   public static class DTDLocation
/*     */     extends org.apache.tools.ant.types.DTDLocation {}
/*     */   
/*     */   static class Config
/*     */   {
/*     */     public File srcDir;
/*     */     public File descriptorDir;
/*  86 */     public String baseNameTerminator = "-";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String baseJarName;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean flatDestDir = false;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Path classpath;
/*     */ 
/*     */ 
/*     */     
/* 105 */     public List<FileSet> supportFileSets = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     public ArrayList<EjbJar.DTDLocation> dtdLocations = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EjbJar.NamingScheme namingScheme;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public File manifest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String analyzer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class NamingScheme
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public static final String EJB_NAME = "ejb-name";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DIRECTORY = "directory";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DESCRIPTOR = "descriptor";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String BASEJARNAME = "basejarname";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 166 */       return new String[] { "ejb-name", "directory", "descriptor", "basejarname" };
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class CMPVersion
/*     */     extends EnumeratedAttribute
/*     */   {
/*     */     public static final String CMP1_0 = "1.0";
/*     */ 
/*     */     
/*     */     public static final String CMP2_0 = "2.0";
/*     */ 
/*     */ 
/*     */     
/*     */     public String[] getValues() {
/* 183 */       return new String[] { "1.0", "2.0" };
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
/* 194 */   private Config config = new Config();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File destDir;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 205 */   private String genericJarSuffix = "-generic.jar";
/*     */ 
/*     */   
/* 208 */   private String cmpVersion = "1.0";
/*     */ 
/*     */   
/* 211 */   private List<EJBDeploymentTool> deploymentTools = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addDeploymentTool(EJBDeploymentTool deploymentTool) {
/* 221 */     deploymentTool.setTask((Task)this);
/* 222 */     this.deploymentTools.add(deploymentTool);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrionDeploymentTool createOrion() {
/* 233 */     OrionDeploymentTool tool = new OrionDeploymentTool();
/* 234 */     addDeploymentTool(tool);
/* 235 */     return tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WeblogicDeploymentTool createWeblogic() {
/* 244 */     WeblogicDeploymentTool tool = new WeblogicDeploymentTool();
/* 245 */     addDeploymentTool(tool);
/* 246 */     return tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebsphereDeploymentTool createWebsphere() {
/* 255 */     WebsphereDeploymentTool tool = new WebsphereDeploymentTool();
/* 256 */     addDeploymentTool(tool);
/* 257 */     return tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BorlandDeploymentTool createBorland() {
/* 266 */     log("Borland deployment tools", 3);
/*     */     
/* 268 */     BorlandDeploymentTool tool = new BorlandDeploymentTool();
/* 269 */     tool.setTask((Task)this);
/* 270 */     this.deploymentTools.add(tool);
/* 271 */     return tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IPlanetDeploymentTool createIplanet() {
/* 280 */     log("iPlanet Application Server deployment tools", 3);
/*     */     
/* 282 */     IPlanetDeploymentTool tool = new IPlanetDeploymentTool();
/* 283 */     addDeploymentTool(tool);
/* 284 */     return tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JbossDeploymentTool createJboss() {
/* 293 */     JbossDeploymentTool tool = new JbossDeploymentTool();
/* 294 */     addDeploymentTool(tool);
/* 295 */     return tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JonasDeploymentTool createJonas() {
/* 304 */     log("JOnAS deployment tools", 3);
/*     */     
/* 306 */     JonasDeploymentTool tool = new JonasDeploymentTool();
/* 307 */     addDeploymentTool(tool);
/* 308 */     return tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WeblogicTOPLinkDeploymentTool createWeblogictoplink() {
/* 318 */     log("The <weblogictoplink> element is no longer required. Please use the <weblogic> element and set newCMP=\"true\"", 2);
/*     */ 
/*     */     
/* 321 */     WeblogicTOPLinkDeploymentTool tool = new WeblogicTOPLinkDeploymentTool();
/*     */     
/* 323 */     addDeploymentTool(tool);
/* 324 */     return tool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 334 */     if (this.config.classpath == null) {
/* 335 */       this.config.classpath = new Path(getProject());
/*     */     }
/* 337 */     return this.config.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DTDLocation createDTD() {
/* 348 */     DTDLocation dtdLocation = new DTDLocation();
/* 349 */     this.config.dtdLocations.add(dtdLocation);
/*     */     
/* 351 */     return dtdLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FileSet createSupport() {
/* 360 */     FileSet supportFileSet = new FileSet();
/* 361 */     this.config.supportFileSets.add(supportFileSet);
/* 362 */     return supportFileSet;
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
/*     */   public void setManifest(File manifest) {
/* 377 */     this.config.manifest = manifest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcdir(File inDir) {
/* 388 */     this.config.srcDir = inDir;
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
/*     */   public void setDescriptordir(File inDir) {
/* 401 */     this.config.descriptorDir = inDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDependency(String analyzer) {
/* 410 */     this.config.analyzer = analyzer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBasejarname(String inValue) {
/* 421 */     this.config.baseJarName = inValue;
/* 422 */     if (this.config.namingScheme == null) {
/* 423 */       this.config.namingScheme = new NamingScheme();
/* 424 */       this.config.namingScheme.setValue("basejarname");
/* 425 */     } else if (!"basejarname".equals(this.config.namingScheme.getValue())) {
/* 426 */       throw new BuildException("The basejarname attribute is not compatible with the %s naming scheme", new Object[] { this.config.namingScheme
/*     */             
/* 428 */             .getValue() });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNaming(NamingScheme namingScheme) {
/* 439 */     this.config.namingScheme = namingScheme;
/* 440 */     if (!"basejarname".equals(this.config.namingScheme.getValue()) && this.config.baseJarName != null)
/*     */     {
/* 442 */       throw new BuildException("The basejarname attribute is not compatible with the %s naming scheme", new Object[] { this.config.namingScheme
/*     */             
/* 444 */             .getValue() });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDestdir() {
/* 455 */     return this.destDir;
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
/*     */   public void setDestdir(File inDir) {
/* 469 */     this.destDir = inDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCmpversion() {
/* 479 */     return this.cmpVersion;
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
/*     */   public void setCmpversion(CMPVersion version) {
/* 492 */     this.cmpVersion = version.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 501 */     this.config.classpath = classpath;
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
/*     */   public void setFlatdestdir(boolean inValue) {
/* 516 */     this.config.flatDestDir = inValue;
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
/*     */   public void setGenericjarsuffix(String inString) {
/* 529 */     this.genericJarSuffix = inString;
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
/*     */   public void setBasenameterminator(String inValue) {
/* 542 */     this.config.baseNameTerminator = inValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateConfig() throws BuildException {
/* 551 */     if (this.config.srcDir == null) {
/* 552 */       throw new BuildException("The srcDir attribute must be specified");
/*     */     }
/*     */     
/* 555 */     if (this.config.descriptorDir == null) {
/* 556 */       this.config.descriptorDir = this.config.srcDir;
/*     */     }
/*     */     
/* 559 */     if (this.config.namingScheme == null) {
/* 560 */       this.config.namingScheme = new NamingScheme();
/* 561 */       this.config.namingScheme.setValue("descriptor");
/* 562 */     } else if ("basejarname".equals(this.config.namingScheme.getValue()) && this.config.baseJarName == null) {
/*     */       
/* 564 */       throw new BuildException("The basejarname attribute must be specified with the basejarname naming scheme");
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
/*     */   public void execute() throws BuildException {
/* 586 */     validateConfig();
/*     */     
/* 588 */     if (this.deploymentTools.isEmpty()) {
/* 589 */       GenericDeploymentTool genericTool = new GenericDeploymentTool();
/* 590 */       genericTool.setTask((Task)this);
/* 591 */       genericTool.setDestdir(this.destDir);
/* 592 */       genericTool.setGenericJarSuffix(this.genericJarSuffix);
/* 593 */       this.deploymentTools.add(genericTool);
/*     */     } 
/*     */     
/* 596 */     for (EJBDeploymentTool tool : this.deploymentTools) {
/* 597 */       tool.configure(this.config);
/* 598 */       tool.validateConfigured();
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 603 */       SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
/* 604 */       saxParserFactory.setValidating(true);
/* 605 */       SAXParser saxParser = saxParserFactory.newSAXParser();
/*     */       
/* 607 */       DirectoryScanner ds = getDirectoryScanner(this.config.descriptorDir);
/* 608 */       ds.scan();
/* 609 */       String[] files = ds.getIncludedFiles();
/*     */       
/* 611 */       log(files.length + " deployment descriptors located.", 3);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 616 */       for (String file : files) {
/*     */         
/* 618 */         for (EJBDeploymentTool tool : this.deploymentTools) {
/* 619 */           tool.processDescriptor(file, saxParser);
/*     */         }
/*     */       } 
/* 622 */     } catch (SAXException se) {
/* 623 */       throw new BuildException("SAXException while creating parser.", se);
/* 624 */     } catch (ParserConfigurationException pce) {
/* 625 */       throw new BuildException("ParserConfigurationException while creating parser. ", pce);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/EjbJar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */