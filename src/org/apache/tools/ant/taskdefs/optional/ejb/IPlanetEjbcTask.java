/*     */ package org.apache.tools.ant.taskdefs.optional.ejb;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IPlanetEjbcTask
/*     */   extends Task
/*     */ {
/*     */   private File ejbdescriptor;
/*     */   private File iasdescriptor;
/*     */   private File dest;
/*     */   private Path classpath;
/*     */   private boolean keepgenerated = false;
/*     */   private boolean debug = false;
/*     */   private File iashome;
/*     */   
/*     */   public void setEjbdescriptor(File ejbdescriptor) {
/* 107 */     this.ejbdescriptor = ejbdescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIasdescriptor(File iasdescriptor) {
/* 118 */     this.iasdescriptor = iasdescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDest(File dest) {
/* 129 */     this.dest = dest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 138 */     if (this.classpath == null) {
/* 139 */       this.classpath = classpath;
/*     */     } else {
/* 141 */       this.classpath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 150 */     if (this.classpath == null) {
/* 151 */       this.classpath = new Path(getProject());
/*     */     }
/* 153 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKeepgenerated(boolean keepgenerated) {
/* 163 */     this.keepgenerated = keepgenerated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/* 173 */     this.debug = debug;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIashome(File iashome) {
/* 184 */     this.iashome = iashome;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 193 */     checkConfiguration();
/*     */     
/* 195 */     executeEjbc(getParser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkConfiguration() throws BuildException {
/* 205 */     if (this.ejbdescriptor == null) {
/* 206 */       String msg = "The standard EJB descriptor must be specified using the \"ejbdescriptor\" attribute.";
/*     */       
/* 208 */       throw new BuildException(msg, getLocation());
/*     */     } 
/* 210 */     if (!this.ejbdescriptor.exists() || !this.ejbdescriptor.isFile()) {
/* 211 */       String msg = "The standard EJB descriptor (" + this.ejbdescriptor + ") was not found or isn't a file.";
/*     */       
/* 213 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */     
/* 216 */     if (this.iasdescriptor == null) {
/* 217 */       String msg = "The iAS-speific XML descriptor must be specified using the \"iasdescriptor\" attribute.";
/*     */       
/* 219 */       throw new BuildException(msg, getLocation());
/*     */     } 
/* 221 */     if (!this.iasdescriptor.exists() || !this.iasdescriptor.isFile()) {
/* 222 */       String msg = "The iAS-specific XML descriptor (" + this.iasdescriptor + ") was not found or isn't a file.";
/*     */       
/* 224 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */     
/* 227 */     if (this.dest == null) {
/* 228 */       String msg = "The destination directory must be specified using the \"dest\" attribute.";
/*     */       
/* 230 */       throw new BuildException(msg, getLocation());
/*     */     } 
/* 232 */     if (!this.dest.exists() || !this.dest.isDirectory()) {
/* 233 */       String msg = "The destination directory (" + this.dest + ") was not found or isn't a directory.";
/*     */       
/* 235 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */     
/* 238 */     if (this.iashome != null && !this.iashome.isDirectory()) {
/* 239 */       String msg = "If \"iashome\" is specified, it must be a valid directory (it was set to " + this.iashome + ").";
/*     */ 
/*     */       
/* 242 */       throw new BuildException(msg, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SAXParser getParser() throws BuildException {
/*     */     try {
/* 254 */       SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
/* 255 */       saxParserFactory.setValidating(true);
/* 256 */       return saxParserFactory.newSAXParser();
/* 257 */     } catch (SAXException|javax.xml.parsers.ParserConfigurationException e) {
/* 258 */       throw new BuildException("Unable to create a SAXParser: " + e.getMessage(), e, getLocation());
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
/*     */   private void executeEjbc(SAXParser saxParser) throws BuildException {
/* 274 */     IPlanetEjbc ejbc = new IPlanetEjbc(this.ejbdescriptor, this.iasdescriptor, this.dest, getClasspath().toString(), saxParser);
/*     */     
/* 276 */     ejbc.setRetainSource(this.keepgenerated);
/* 277 */     ejbc.setDebugOutput(this.debug);
/* 278 */     if (this.iashome != null) {
/* 279 */       ejbc.setIasHomeDir(this.iashome);
/*     */     }
/*     */     try {
/* 282 */       ejbc.execute();
/* 283 */     } catch (IOException e) {
/* 284 */       throw new BuildException("An IOException occurred while trying to read the XML descriptor file: " + e
/*     */           
/* 286 */           .getMessage(), e, 
/* 287 */           getLocation());
/* 288 */     } catch (SAXException e) {
/* 289 */       throw new BuildException("A SAXException occurred while trying to read the XML descriptor file: " + e
/*     */           
/* 291 */           .getMessage(), e, 
/* 292 */           getLocation());
/* 293 */     } catch (EjbcException e) {
/* 294 */       throw new BuildException("An exception occurred while trying to run the ejbc utility: " + e
/*     */           
/* 296 */           .getMessage(), e, 
/* 297 */           getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path getClasspath() {
/* 308 */     if (this.classpath == null) {
/* 309 */       return (new Path(getProject())).concatSystemClasspath("last");
/*     */     }
/* 311 */     return this.classpath.concatSystemClasspath("ignore");
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ejb/IPlanetEjbcTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */