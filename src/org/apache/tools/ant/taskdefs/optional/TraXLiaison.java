/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import javax.xml.transform.Templates;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.XSLTLiaison4;
/*     */ import org.apache.tools.ant.taskdefs.XSLTLogger;
/*     */ import org.apache.tools.ant.taskdefs.XSLTLoggerAware;
/*     */ import org.apache.tools.ant.taskdefs.XSLTProcess;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.XMLCatalog;
/*     */ import org.apache.tools.ant.types.resources.FileProvider;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.types.resources.URLProvider;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.JAXPUtils;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ import org.apache.tools.ant.util.StreamUtils;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TraXLiaison
/*     */   implements XSLTLiaison4, ErrorListener, XSLTLoggerAware
/*     */ {
/*  84 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Project project;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   private String factoryName = null;
/*     */ 
/*     */   
/*  98 */   private TransformerFactory tfactory = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private Resource stylesheet;
/*     */ 
/*     */ 
/*     */   
/*     */   private XSLTLogger logger;
/*     */ 
/*     */   
/*     */   private EntityResolver entityResolver;
/*     */ 
/*     */   
/*     */   private Transformer transformer;
/*     */ 
/*     */   
/*     */   private Templates templates;
/*     */ 
/*     */   
/*     */   private long templatesModTime;
/*     */ 
/*     */   
/*     */   private URIResolver uriResolver;
/*     */ 
/*     */   
/* 124 */   private final Vector<String[]> outputProperties = (Vector)new Vector<>();
/*     */ 
/*     */   
/* 127 */   private final Hashtable<String, Object> params = new Hashtable<>();
/*     */ 
/*     */   
/* 130 */   private final List<Object[]> attributes = new ArrayList();
/*     */ 
/*     */   
/* 133 */   private final Map<String, Boolean> features = new HashMap<>();
/*     */ 
/*     */   
/*     */   private boolean suppressWarnings = false;
/*     */ 
/*     */   
/* 139 */   private XSLTProcess.TraceConfiguration traceConfiguration = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStylesheet(File stylesheet) throws Exception {
/* 154 */     FileResource fr = new FileResource();
/* 155 */     fr.setProject(this.project);
/* 156 */     fr.setFile(stylesheet);
/* 157 */     setStylesheet((Resource)fr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStylesheet(Resource stylesheet) throws Exception {
/* 166 */     if (this.stylesheet != null) {
/*     */       
/* 168 */       this.transformer = null;
/*     */ 
/*     */       
/* 171 */       if (!this.stylesheet.equals(stylesheet) || stylesheet
/* 172 */         .getLastModified() != this.templatesModTime) {
/* 173 */         this.templates = null;
/*     */       }
/*     */     } 
/* 176 */     this.stylesheet = stylesheet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void transform(File infile, File outfile) throws Exception {
/* 186 */     if (this.transformer == null) {
/* 187 */       createTransformer();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 192 */     InputStream fis = new BufferedInputStream(Files.newInputStream(infile.toPath(), new java.nio.file.OpenOption[0])); try {
/* 193 */       OutputStream fos = new BufferedOutputStream(Files.newOutputStream(outfile.toPath(), new java.nio.file.OpenOption[0])); 
/* 194 */       try { StreamResult res = new StreamResult(fos);
/*     */         
/* 196 */         res.setSystemId(JAXPUtils.getSystemId(outfile));
/*     */ 
/*     */         
/* 199 */         setTransformationParameters();
/*     */         
/* 201 */         this.transformer.transform(getSource(fis, infile), res);
/* 202 */         fos.close(); } catch (Throwable throwable) { try { fos.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  fis.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         fis.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Source getSource(InputStream is, File infile) throws ParserConfigurationException, SAXException {
/* 219 */     Source src = null;
/* 220 */     if (this.entityResolver != null) {
/* 221 */       if (getFactory().getFeature("http://javax.xml.transform.sax.SAXSource/feature")) {
/* 222 */         SAXParserFactory spFactory = SAXParserFactory.newInstance();
/* 223 */         spFactory.setNamespaceAware(true);
/* 224 */         XMLReader reader = spFactory.newSAXParser().getXMLReader();
/* 225 */         reader.setEntityResolver(this.entityResolver);
/* 226 */         src = new SAXSource(reader, new InputSource(is));
/*     */       } else {
/* 228 */         throw new IllegalStateException("xcatalog specified, but parser doesn't support SAX");
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 234 */       src = new StreamSource(is);
/*     */     } 
/* 236 */     src.setSystemId(JAXPUtils.getSystemId(infile));
/* 237 */     return src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Source getSource(InputStream is, Resource resource) throws ParserConfigurationException, SAXException {
/* 245 */     Source src = null;
/* 246 */     if (this.entityResolver != null) {
/* 247 */       if (getFactory().getFeature("http://javax.xml.transform.sax.SAXSource/feature")) {
/* 248 */         SAXParserFactory spFactory = SAXParserFactory.newInstance();
/* 249 */         spFactory.setNamespaceAware(true);
/* 250 */         XMLReader reader = spFactory.newSAXParser().getXMLReader();
/* 251 */         reader.setEntityResolver(this.entityResolver);
/* 252 */         src = new SAXSource(reader, new InputSource(is));
/*     */       } else {
/* 254 */         throw new IllegalStateException("xcatalog specified, but parser doesn't support SAX");
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 260 */       src = new StreamSource(is);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 265 */     src.setSystemId(resourceToURI(resource));
/* 266 */     return src;
/*     */   }
/*     */   
/*     */   private String resourceToURI(Resource resource) {
/* 270 */     FileProvider fp = (FileProvider)resource.as(FileProvider.class);
/* 271 */     if (fp != null) {
/* 272 */       return FILE_UTILS.toURI(fp.getFile().getAbsolutePath());
/*     */     }
/* 274 */     URLProvider up = (URLProvider)resource.as(URLProvider.class);
/* 275 */     if (up != null) {
/* 276 */       URL u = up.getURL();
/* 277 */       return String.valueOf(u);
/*     */     } 
/* 279 */     return resource.getName();
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
/*     */   private void readTemplates() throws IOException, TransformerConfigurationException, ParserConfigurationException, SAXException {
/* 295 */     InputStream xslStream = new BufferedInputStream(this.stylesheet.getInputStream()); try {
/* 296 */       this.templatesModTime = this.stylesheet.getLastModified();
/* 297 */       Source src = getSource(xslStream, this.stylesheet);
/* 298 */       this.templates = getFactory().newTemplates(src);
/* 299 */       xslStream.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         xslStream.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   private void createTransformer() throws IOException, ParserConfigurationException, SAXException, TransformerException {
/* 310 */     if (this.templates == null) {
/* 311 */       readTemplates();
/*     */     }
/*     */     
/* 314 */     this.transformer = this.templates.newTransformer();
/*     */ 
/*     */     
/* 317 */     this.transformer.setErrorListener(this);
/* 318 */     if (this.uriResolver != null) {
/* 319 */       this.transformer.setURIResolver(this.uriResolver);
/*     */     }
/* 321 */     for (String[] pair : this.outputProperties) {
/* 322 */       this.transformer.setOutputProperty(pair[0], pair[1]);
/*     */     }
/*     */     
/* 325 */     if (this.traceConfiguration != null) {
/* 326 */       if ("org.apache.xalan.transformer.TransformerImpl"
/* 327 */         .equals(this.transformer.getClass().getName())) {
/*     */         
/*     */         try {
/* 330 */           Class<?> traceSupport = Class.forName("org.apache.tools.ant.taskdefs.optional.Xalan2TraceSupport", true, 
/*     */               
/* 332 */               Thread.currentThread()
/* 333 */               .getContextClassLoader());
/*     */           
/* 335 */           XSLTTraceSupport ts = traceSupport.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 336 */           ts.configureTrace(this.transformer, this.traceConfiguration);
/* 337 */         } catch (Exception e) {
/* 338 */           String msg = "Failed to enable tracing because of " + e;
/* 339 */           if (this.project != null) {
/* 340 */             this.project.log(msg, 1);
/*     */           } else {
/* 342 */             System.err.println(msg);
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 347 */         String msg = "Not enabling trace support for transformer implementation" + this.transformer.getClass().getName();
/* 348 */         if (this.project != null) {
/* 349 */           this.project.log(msg, 1);
/*     */         } else {
/* 351 */           System.err.println(msg);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setTransformationParameters() {
/* 361 */     this.params.forEach((key, value) -> this.transformer.setParameter(key, value));
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
/*     */   private TransformerFactory getFactory() throws BuildException {
/* 373 */     if (this.tfactory != null) {
/* 374 */       return this.tfactory;
/*     */     }
/*     */     
/* 377 */     if (this.factoryName == null) {
/* 378 */       this.tfactory = TransformerFactory.newInstance();
/*     */     } else {
/*     */       try {
/* 381 */         Class<?> clazz = null;
/*     */         
/*     */         try {
/* 384 */           clazz = Class.forName(this.factoryName, true, 
/* 385 */               Thread.currentThread()
/* 386 */               .getContextClassLoader());
/* 387 */         } catch (ClassNotFoundException cnfe) {
/* 388 */           String msg = "Failed to load " + this.factoryName + " via the configured classpath, will try Ant's classpath instead.";
/*     */ 
/*     */           
/* 391 */           if (this.logger != null) {
/* 392 */             this.logger.log(msg);
/* 393 */           } else if (this.project != null) {
/* 394 */             this.project.log(msg, 1);
/*     */           } else {
/* 396 */             System.err.println(msg);
/*     */           } 
/*     */         } 
/*     */         
/* 400 */         if (clazz == null) {
/* 401 */           clazz = Class.forName(this.factoryName);
/*     */         }
/* 403 */         this.tfactory = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 404 */       } catch (Exception e) {
/* 405 */         throw new BuildException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 409 */     applyReflectionHackForExtensionMethods();
/*     */     
/* 411 */     this.tfactory.setErrorListener(this);
/*     */ 
/*     */     
/* 414 */     for (Object[] pair : this.attributes) {
/* 415 */       this.tfactory.setAttribute((String)pair[0], pair[1]);
/*     */     }
/*     */     
/* 418 */     for (Map.Entry<String, Boolean> feature : this.features.entrySet()) {
/*     */       try {
/* 420 */         this.tfactory.setFeature(feature.getKey(), ((Boolean)feature.getValue()).booleanValue());
/* 421 */       } catch (TransformerConfigurationException ex) {
/* 422 */         throw new BuildException(ex);
/*     */       } 
/*     */     } 
/*     */     
/* 426 */     if (this.uriResolver != null) {
/* 427 */       this.tfactory.setURIResolver(this.uriResolver);
/*     */     }
/* 429 */     return this.tfactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFactory(String name) {
/* 439 */     this.factoryName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttribute(String name, Object value) {
/* 450 */     Object[] pair = { name, value };
/* 451 */     this.attributes.add(pair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) {
/* 461 */     this.features.put(name, Boolean.valueOf(value));
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
/*     */   public void setOutputProperty(String name, String value) {
/* 474 */     String[] pair = { name, value };
/* 475 */     this.outputProperties.addElement(pair);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver aResolver) {
/* 483 */     this.entityResolver = aResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setURIResolver(URIResolver aResolver) {
/* 491 */     this.uriResolver = aResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(String name, String value) {
/* 500 */     this.params.put(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(String name, Object value) {
/* 510 */     this.params.put(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogger(XSLTLogger l) {
/* 518 */     this.logger = l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(TransformerException e) {
/* 526 */     logError(e, "Error");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fatalError(TransformerException e) {
/* 534 */     logError(e, "Fatal Error");
/* 535 */     throw new BuildException("Fatal error during transformation using " + this.stylesheet + ": " + e.getMessageAndLocation(), e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warning(TransformerException e) {
/* 543 */     if (!this.suppressWarnings) {
/* 544 */       logError(e, "Warning");
/*     */     }
/*     */   }
/*     */   
/*     */   private void logError(TransformerException e, String type) {
/* 549 */     if (this.logger == null) {
/*     */       return;
/*     */     }
/*     */     
/* 553 */     StringBuilder msg = new StringBuilder();
/* 554 */     SourceLocator locator = e.getLocator();
/* 555 */     if (locator != null) {
/* 556 */       String systemid = locator.getSystemId();
/* 557 */       if (systemid != null) {
/* 558 */         String url = systemid;
/* 559 */         if (url.startsWith("file:")) {
/* 560 */           url = FileUtils.getFileUtils().fromURI(url);
/*     */         }
/* 562 */         msg.append(url);
/*     */       } else {
/* 564 */         msg.append("Unknown file");
/*     */       } 
/* 566 */       int line = locator.getLineNumber();
/* 567 */       if (line != -1) {
/* 568 */         msg.append(":");
/* 569 */         msg.append(line);
/* 570 */         int column = locator.getColumnNumber();
/* 571 */         if (column != -1) {
/* 572 */           msg.append(":");
/* 573 */           msg.append(column);
/*     */         } 
/*     */       } 
/*     */     } 
/* 577 */     msg.append(": ");
/* 578 */     msg.append(type);
/* 579 */     msg.append("! ");
/* 580 */     msg.append(e.getMessage());
/* 581 */     if (e.getCause() != null) {
/* 582 */       msg.append(" Cause: ");
/* 583 */       msg.append(e.getCause());
/*     */     } 
/*     */     
/* 586 */     this.logger.log(msg.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected String getSystemId(File file) {
/* 598 */     return JAXPUtils.getSystemId(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(XSLTProcess xsltTask) {
/* 608 */     this.project = xsltTask.getProject();
/* 609 */     XSLTProcess.Factory factory = xsltTask.getFactory();
/* 610 */     if (factory != null) {
/* 611 */       setFactory(factory.getName());
/*     */       
/* 613 */       StreamUtils.enumerationAsStream(factory.getAttributes())
/* 614 */         .forEach(attr -> setAttribute(attr.getName(), attr.getValue()));
/* 615 */       factory.getFeatures()
/* 616 */         .forEach(feature -> setFeature(feature.getName(), feature.getValue()));
/*     */     } 
/*     */     
/* 619 */     XMLCatalog xmlCatalog = xsltTask.getXMLCatalog();
/*     */     
/* 621 */     if (xmlCatalog != null) {
/* 622 */       setEntityResolver((EntityResolver)xmlCatalog);
/* 623 */       setURIResolver((URIResolver)xmlCatalog);
/*     */     } 
/*     */ 
/*     */     
/* 627 */     StreamUtils.enumerationAsStream(xsltTask.getOutputProperties())
/* 628 */       .forEach(prop -> setOutputProperty(prop.getName(), prop.getValue()));
/*     */     
/* 630 */     this.suppressWarnings = xsltTask.getSuppressWarnings();
/*     */     
/* 632 */     this.traceConfiguration = xsltTask.getTraceConfiguration();
/*     */   }
/*     */ 
/*     */   
/*     */   private void applyReflectionHackForExtensionMethods() {
/* 637 */     if (!JavaEnvUtils.isAtLeastJavaVersion("9"))
/*     */       try {
/* 639 */         Field _isNotSecureProcessing = this.tfactory.getClass().getDeclaredField("_isNotSecureProcessing");
/* 640 */         _isNotSecureProcessing.setAccessible(true);
/* 641 */         _isNotSecureProcessing.set(this.tfactory, Boolean.TRUE);
/* 642 */       } catch (Exception x) {
/* 643 */         if (this.project != null)
/* 644 */           this.project.log(x.toString(), 4); 
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/TraXLiaison.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */