/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.DTDLocation;
/*     */ import org.apache.tools.ant.types.FileSet;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.types.ResourceLocation;
/*     */ import org.apache.tools.ant.types.XMLCatalog;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.JAXPUtils;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.ParserAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLValidateTask
/*     */   extends Task
/*     */ {
/*  62 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/*     */   protected static final String INIT_FAILED_MSG = "Could not start xml validation: ";
/*     */   
/*     */   protected boolean failOnError = true;
/*     */   
/*     */   protected boolean warn = true;
/*     */   
/*     */   protected boolean lenient = false;
/*     */   
/*  73 */   protected String readerClassName = null;
/*     */ 
/*     */   
/*  76 */   protected File file = null;
/*     */   
/*  78 */   protected Vector<FileSet> filesets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Path classpath;
/*     */ 
/*     */ 
/*     */   
/*  87 */   protected XMLReader xmlReader = null;
/*     */   
/*  89 */   protected ValidatorErrorHandler errorHandler = new ValidatorErrorHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private Vector<Attribute> attributeList = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  99 */   private final Vector<Property> propertyList = new Vector<>();
/*     */   
/* 101 */   private XMLCatalog xmlCatalog = new XMLCatalog();
/*     */ 
/*     */   
/*     */   public static final String MESSAGE_FILES_VALIDATED = " file(s) have been successfully validated.";
/*     */   
/* 106 */   private AntClassLoader readerLoader = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean fail) {
/* 117 */     this.failOnError = fail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWarn(boolean bool) {
/* 127 */     this.warn = bool;
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
/*     */   public void setLenient(boolean bool) {
/* 142 */     this.lenient = bool;
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
/*     */   public void setClassName(String className) {
/* 157 */     this.readerClassName = className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/* 165 */     if (this.classpath == null) {
/* 166 */       this.classpath = classpath;
/*     */     } else {
/* 168 */       this.classpath.append(classpath);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 177 */     if (this.classpath == null) {
/* 178 */       this.classpath = new Path(getProject());
/*     */     }
/* 180 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 189 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/* 197 */     this.file = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredXMLCatalog(XMLCatalog catalog) {
/* 205 */     this.xmlCatalog.addConfiguredXMLCatalog(catalog);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 213 */     this.filesets.addElement(set);
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
/*     */   public Attribute createAttribute() {
/* 227 */     Attribute feature = new Attribute();
/* 228 */     this.attributeList.addElement(feature);
/* 229 */     return feature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Property createProperty() {
/* 239 */     Property prop = new Property();
/* 240 */     this.propertyList.addElement(prop);
/* 241 */     return prop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() throws BuildException {
/* 250 */     super.init();
/* 251 */     this.xmlCatalog.setProject(getProject());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DTDLocation createDTD() {
/* 261 */     DTDLocation dtdLocation = new DTDLocation();
/* 262 */     this.xmlCatalog.addDTD((ResourceLocation)dtdLocation);
/* 263 */     return dtdLocation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EntityResolver getEntityResolver() {
/* 270 */     return (EntityResolver)this.xmlCatalog;
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
/*     */   protected XMLReader getXmlReader() {
/* 282 */     return this.xmlReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*     */     try {
/* 291 */       int fileProcessed = 0;
/* 292 */       if (this.file == null && this.filesets.isEmpty()) {
/* 293 */         throw new BuildException("Specify at least one source - a file or a fileset.");
/*     */       }
/*     */ 
/*     */       
/* 297 */       if (this.file != null) {
/* 298 */         if (this.file.exists() && this.file.canRead() && this.file.isFile()) {
/* 299 */           doValidate(this.file);
/* 300 */           fileProcessed++;
/*     */         } else {
/* 302 */           String errorMsg = "File " + this.file + " cannot be read";
/* 303 */           if (this.failOnError) {
/* 304 */             throw new BuildException(errorMsg);
/*     */           }
/* 306 */           log(errorMsg, 0);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 311 */       for (FileSet fs : this.filesets) {
/* 312 */         DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/* 313 */         for (String fileName : ds.getIncludedFiles()) {
/* 314 */           File srcFile = new File(fs.getDir(getProject()), fileName);
/* 315 */           doValidate(srcFile);
/* 316 */           fileProcessed++;
/*     */         } 
/*     */       } 
/* 319 */       onSuccessfulValidation(fileProcessed);
/*     */     } finally {
/* 321 */       cleanup();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onSuccessfulValidation(int fileProcessed) {
/* 330 */     log(fileProcessed + " file(s) have been successfully validated.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initValidator() {
/* 341 */     this.xmlReader = createXmlReader();
/*     */     
/* 343 */     this.xmlReader.setEntityResolver(getEntityResolver());
/* 344 */     this.xmlReader.setErrorHandler(this.errorHandler);
/*     */     
/* 346 */     if (!isSax1Parser()) {
/*     */       
/* 348 */       if (!this.lenient) {
/* 349 */         setFeature("http://xml.org/sax/features/validation", true);
/*     */       }
/*     */       
/* 352 */       for (Attribute feature : this.attributeList) {
/* 353 */         setFeature(feature.getName(), feature.getValue());
/*     */       }
/*     */ 
/*     */       
/* 357 */       for (Property prop : this.propertyList) {
/* 358 */         setProperty(prop.getName(), prop.getValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSax1Parser() {
/* 368 */     return this.xmlReader instanceof ParserAdapter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XMLReader createXmlReader() {
/*     */     XMLReader newReader;
/* 380 */     Object reader = null;
/* 381 */     if (this.readerClassName == null) {
/* 382 */       reader = createDefaultReaderOrParser();
/*     */     } else {
/*     */       
/* 385 */       Class<?> readerClass = null;
/*     */       
/*     */       try {
/* 388 */         if (this.classpath != null) {
/* 389 */           this.readerLoader = getProject().createClassLoader(this.classpath);
/* 390 */           readerClass = Class.forName(this.readerClassName, true, (ClassLoader)this.readerLoader);
/*     */         } else {
/*     */           
/* 393 */           readerClass = Class.forName(this.readerClassName);
/*     */         } 
/*     */         
/* 396 */         reader = readerClass.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 397 */       } catch (ClassNotFoundException|IllegalAccessException|InstantiationException|NoSuchMethodException|java.lang.reflect.InvocationTargetException e) {
/*     */         
/* 399 */         throw new BuildException("Could not start xml validation: " + this.readerClassName, e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 405 */     if (reader instanceof XMLReader) {
/* 406 */       newReader = (XMLReader)reader;
/* 407 */       log("Using SAX2 reader " + reader
/* 408 */           .getClass().getName(), 3);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 413 */     else if (reader instanceof Parser) {
/* 414 */       newReader = new ParserAdapter((Parser)reader);
/* 415 */       log("Using SAX1 parser " + reader
/* 416 */           .getClass().getName(), 3);
/*     */     } else {
/*     */       
/* 419 */       throw new BuildException("Could not start xml validation: " + reader
/*     */           
/* 421 */           .getClass().getName() + " implements nor SAX1 Parser nor SAX2 XMLReader.");
/*     */     } 
/*     */ 
/*     */     
/* 425 */     return newReader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanup() {
/* 434 */     if (this.readerLoader != null) {
/* 435 */       this.readerLoader.cleanup();
/* 436 */       this.readerLoader = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object createDefaultReaderOrParser() {
/*     */     Object reader;
/*     */     try {
/* 447 */       reader = createDefaultReader();
/* 448 */     } catch (BuildException exc) {
/* 449 */       reader = JAXPUtils.getParser();
/*     */     } 
/* 451 */     return reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected XMLReader createDefaultReader() {
/* 462 */     return JAXPUtils.getXMLReader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setFeature(String feature, boolean value) throws BuildException {
/* 473 */     log("Setting feature " + feature + "=" + value, 4);
/*     */     try {
/* 475 */       this.xmlReader.setFeature(feature, value);
/* 476 */     } catch (SAXNotRecognizedException e) {
/* 477 */       throw new BuildException("Parser " + this.xmlReader
/*     */           
/* 479 */           .getClass().getName() + " doesn't recognize feature " + feature, e, 
/*     */ 
/*     */ 
/*     */           
/* 483 */           getLocation());
/* 484 */     } catch (SAXNotSupportedException e) {
/* 485 */       throw new BuildException("Parser " + this.xmlReader
/*     */           
/* 487 */           .getClass().getName() + " doesn't support feature " + feature, e, 
/*     */ 
/*     */ 
/*     */           
/* 491 */           getLocation());
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
/*     */   protected void setProperty(String name, String value) throws BuildException {
/* 505 */     if (name == null || value == null) {
/* 506 */       throw new BuildException("Property name and value must be specified.");
/*     */     }
/*     */     
/*     */     try {
/* 510 */       this.xmlReader.setProperty(name, value);
/* 511 */     } catch (SAXNotRecognizedException e) {
/* 512 */       throw new BuildException("Parser " + this.xmlReader
/*     */           
/* 514 */           .getClass().getName() + " doesn't recognize property " + name, e, 
/*     */ 
/*     */ 
/*     */           
/* 518 */           getLocation());
/* 519 */     } catch (SAXNotSupportedException e) {
/* 520 */       throw new BuildException("Parser " + this.xmlReader
/*     */           
/* 522 */           .getClass().getName() + " doesn't support property " + name, e, 
/*     */ 
/*     */ 
/*     */           
/* 526 */           getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean doValidate(File afile) {
/* 537 */     initValidator();
/* 538 */     boolean result = true;
/*     */     try {
/* 540 */       log("Validating " + afile.getName() + "... ", 3);
/* 541 */       this.errorHandler.init(afile);
/* 542 */       InputSource is = new InputSource(Files.newInputStream(afile.toPath(), new java.nio.file.OpenOption[0]));
/* 543 */       String uri = FILE_UTILS.toURI(afile.getAbsolutePath());
/* 544 */       is.setSystemId(uri);
/* 545 */       this.xmlReader.parse(is);
/* 546 */     } catch (SAXException ex) {
/* 547 */       log("Caught when validating: " + ex.toString(), 4);
/* 548 */       if (this.failOnError) {
/* 549 */         throw new BuildException("Could not validate document " + afile);
/*     */       }
/*     */       
/* 552 */       log("Could not validate document " + afile + ": " + ex.toString());
/* 553 */       result = false;
/* 554 */     } catch (IOException ex) {
/* 555 */       throw new BuildException("Could not validate document " + afile, ex);
/*     */     } 
/*     */ 
/*     */     
/* 559 */     if (this.errorHandler.getFailure()) {
/* 560 */       if (this.failOnError) {
/* 561 */         throw new BuildException(afile + " is not a valid XML document.");
/*     */       }
/*     */       
/* 564 */       result = false;
/* 565 */       log(afile + " is not a valid XML document", 0);
/*     */     } 
/* 567 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ValidatorErrorHandler
/*     */     implements ErrorHandler
/*     */   {
/* 580 */     protected File currentFile = null;
/* 581 */     protected String lastErrorMessage = null;
/*     */ 
/*     */     
/*     */     protected boolean failed = false;
/*     */ 
/*     */ 
/*     */     
/*     */     public void init(File file) {
/* 589 */       this.currentFile = file;
/* 590 */       this.failed = false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getFailure() {
/* 597 */       return this.failed;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void fatalError(SAXParseException exception) {
/* 605 */       this.failed = true;
/* 606 */       doLog(exception, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void error(SAXParseException exception) {
/* 613 */       this.failed = true;
/* 614 */       doLog(exception, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void warning(SAXParseException exception) {
/* 623 */       if (XMLValidateTask.this.warn) {
/* 624 */         doLog(exception, 1);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     private void doLog(SAXParseException e, int logLevel) {
/* 630 */       XMLValidateTask.this.log(getMessage(e), logLevel);
/*     */     }
/*     */     
/*     */     private String getMessage(SAXParseException e) {
/* 634 */       String sysID = e.getSystemId();
/* 635 */       if (sysID != null) {
/* 636 */         String name = sysID;
/* 637 */         if (sysID.startsWith("file:")) {
/*     */           try {
/* 639 */             name = XMLValidateTask.FILE_UTILS.fromURI(sysID);
/* 640 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */         
/* 644 */         int line = e.getLineNumber();
/* 645 */         int col = e.getColumnNumber();
/* 646 */         return name + (
/* 647 */           (line == -1) ? 
/* 648 */           "" : (
/* 649 */           ":" + line + ((col == -1) ? "" : (":" + col)))) + ": " + e
/*     */           
/* 651 */           .getMessage();
/*     */       } 
/* 653 */       return e.getMessage();
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
/*     */   public static class Attribute
/*     */   {
/* 667 */     private String attributeName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean attributeValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 679 */       this.attributeName = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(boolean value) {
/* 686 */       this.attributeValue = value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 694 */       return this.attributeName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getValue() {
/* 702 */       return this.attributeValue;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Property
/*     */   {
/*     */     private String name;
/*     */ 
/*     */ 
/*     */     
/*     */     private String value;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 721 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 728 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue() {
/* 736 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setValue(String value) {
/* 743 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/XMLValidateTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */