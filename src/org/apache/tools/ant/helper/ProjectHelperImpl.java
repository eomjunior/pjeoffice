/*      */ package org.apache.tools.ant.helper;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.nio.file.Files;
/*      */ import java.util.Locale;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.IntrospectionHelper;
/*      */ import org.apache.tools.ant.Location;
/*      */ import org.apache.tools.ant.Project;
/*      */ import org.apache.tools.ant.ProjectHelper;
/*      */ import org.apache.tools.ant.RuntimeConfigurable;
/*      */ import org.apache.tools.ant.Target;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.TaskContainer;
/*      */ import org.apache.tools.ant.TypeAdapter;
/*      */ import org.apache.tools.ant.UnknownElement;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.JAXPUtils;
/*      */ import org.xml.sax.AttributeList;
/*      */ import org.xml.sax.DocumentHandler;
/*      */ import org.xml.sax.HandlerBase;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.Parser;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.helpers.XMLReaderAdapter;
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
/*      */ public class ProjectHelperImpl
/*      */   extends ProjectHelper
/*      */ {
/*   61 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Parser parser;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Project project;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private File buildFile;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private File buildFileParent;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Locator locator;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   93 */   private Target implicitTarget = new Target();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ProjectHelperImpl() {
/*   99 */     this.implicitTarget.setName("");
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
/*      */   public void parse(Project project, Object source) throws BuildException {
/*  111 */     if (!(source instanceof File)) {
/*  112 */       throw new BuildException("Only File source supported by default plugin");
/*      */     }
/*      */     
/*  115 */     File bFile = (File)source;
/*      */     
/*  117 */     this.project = project;
/*  118 */     this.buildFile = new File(bFile.getAbsolutePath());
/*  119 */     this.buildFileParent = new File(this.buildFile.getParent());
/*      */     
/*      */     try {
/*  122 */       this.parser = JAXPUtils.getParser();
/*  123 */     } catch (BuildException e) {
/*  124 */       this.parser = new XMLReaderAdapter(JAXPUtils.getXMLReader());
/*      */     } 
/*      */     
/*  127 */     try { InputStream inputStream = Files.newInputStream(bFile.toPath(), new java.nio.file.OpenOption[0]); 
/*  128 */       try { String uri = FILE_UTILS.toURI(bFile.getAbsolutePath());
/*  129 */         InputSource inputSource = new InputSource(inputStream);
/*  130 */         inputSource.setSystemId(uri);
/*  131 */         project.log("parsing buildfile " + bFile + " with URI = " + uri, 3);
/*  132 */         HandlerBase hb = new RootHandler(this);
/*  133 */         this.parser.setDocumentHandler(hb);
/*  134 */         this.parser.setEntityResolver(hb);
/*  135 */         this.parser.setErrorHandler(hb);
/*  136 */         this.parser.setDTDHandler(hb);
/*  137 */         this.parser.parse(inputSource);
/*  138 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SAXParseException exc)
/*      */     
/*  140 */     { Location location = new Location(exc.getSystemId(), exc.getLineNumber(), exc.getColumnNumber());
/*      */       
/*  142 */       Throwable t = exc.getException();
/*  143 */       if (t instanceof BuildException) {
/*  144 */         BuildException be = (BuildException)t;
/*  145 */         if (be.getLocation() == Location.UNKNOWN_LOCATION) {
/*  146 */           be.setLocation(location);
/*      */         }
/*  148 */         throw be;
/*      */       } 
/*  150 */       throw new BuildException(exc.getMessage(), t, location); }
/*  151 */     catch (SAXException exc)
/*  152 */     { Throwable t = exc.getException();
/*  153 */       if (t instanceof BuildException) {
/*  154 */         throw (BuildException)t;
/*      */       }
/*  156 */       throw new BuildException(exc.getMessage(), t); }
/*  157 */     catch (FileNotFoundException exc)
/*  158 */     { throw new BuildException(exc); }
/*  159 */     catch (UnsupportedEncodingException exc)
/*  160 */     { throw new BuildException("Encoding of project file is invalid.", exc); }
/*  161 */     catch (IOException exc)
/*  162 */     { throw new BuildException("Error reading project file: " + exc.getMessage(), exc); }
/*      */   
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
/*      */   static class AbstractHandler
/*      */     extends HandlerBase
/*      */   {
/*      */     protected DocumentHandler parentHandler;
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
/*      */     ProjectHelperImpl helperImpl;
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
/*      */     public AbstractHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
/*  208 */       this.parentHandler = parentHandler;
/*  209 */       this.helperImpl = helperImpl;
/*      */ 
/*      */       
/*  212 */       helperImpl.parser.setDocumentHandler(this);
/*      */     }
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
/*      */     public void startElement(String tag, AttributeList attrs) throws SAXParseException {
/*  228 */       throw new SAXParseException("Unexpected element \"" + tag + "\"", this.helperImpl.locator);
/*      */     }
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
/*      */     public void characters(char[] buf, int start, int count) throws SAXParseException {
/*  244 */       String s = (new String(buf, start, count)).trim();
/*      */       
/*  246 */       if (!s.isEmpty()) {
/*  247 */         throw new SAXParseException("Unexpected text \"" + s + "\"", this.helperImpl.locator);
/*      */       }
/*      */     }
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
/*      */     public void endElement(String name) throws SAXException {
/*  264 */       this.helperImpl.parser.setDocumentHandler(this.parentHandler);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class RootHandler
/*      */     extends HandlerBase
/*      */   {
/*      */     ProjectHelperImpl helperImpl;
/*      */ 
/*      */     
/*      */     public RootHandler(ProjectHelperImpl helperImpl) {
/*  277 */       this.helperImpl = helperImpl;
/*      */     }
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
/*      */     public InputSource resolveEntity(String publicId, String systemId) {
/*  291 */       this.helperImpl.project.log("resolving systemId: " + systemId, 3);
/*      */       
/*  293 */       if (systemId.startsWith("file:")) {
/*  294 */         String path = ProjectHelperImpl.FILE_UTILS.fromURI(systemId);
/*      */         
/*  296 */         File file = new File(path);
/*  297 */         if (!file.isAbsolute()) {
/*  298 */           file = ProjectHelperImpl.FILE_UTILS.resolveFile(this.helperImpl.buildFileParent, path);
/*  299 */           this.helperImpl.project.log("Warning: '" + systemId + "' in " + this.helperImpl.buildFile + " should be expressed simply as '" + path
/*  300 */               .replace('\\', '/') + "' for compliance with other XML tools", 1);
/*      */         } 
/*      */         
/*      */         try {
/*  304 */           InputSource inputSource = new InputSource(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]));
/*  305 */           inputSource.setSystemId(ProjectHelperImpl.FILE_UTILS.toURI(file.getAbsolutePath()));
/*  306 */           return inputSource;
/*  307 */         } catch (IOException fne) {
/*  308 */           this.helperImpl.project.log(file.getAbsolutePath() + " could not be found", 1);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  313 */       return null;
/*      */     }
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
/*      */     public void startElement(String tag, AttributeList attrs) throws SAXParseException {
/*  329 */       if ("project".equals(tag)) {
/*  330 */         (new ProjectHelperImpl.ProjectHandler(this.helperImpl, this)).init(tag, attrs);
/*      */       } else {
/*  332 */         throw new SAXParseException("Config file is not of expected XML type", this.helperImpl
/*  333 */             .locator);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setDocumentLocator(Locator locator) {
/*  344 */       this.helperImpl.locator = locator;
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
/*      */   static class ProjectHandler
/*      */     extends AbstractHandler
/*      */   {
/*      */     public ProjectHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
/*  361 */       super(helperImpl, parentHandler);
/*      */     }
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
/*      */     public void init(String tag, AttributeList attrs) throws SAXParseException {
/*  381 */       String def = null;
/*  382 */       String name = null;
/*  383 */       String id = null;
/*  384 */       String baseDir = null;
/*      */       
/*  386 */       for (int i = 0; i < attrs.getLength(); i++) {
/*  387 */         String key = attrs.getName(i);
/*  388 */         String value = attrs.getValue(i);
/*  389 */         switch (key) {
/*      */           case "default":
/*  391 */             def = value;
/*      */             break;
/*      */           case "name":
/*  394 */             name = value;
/*      */             break;
/*      */           case "id":
/*  397 */             id = value;
/*      */             break;
/*      */           case "basedir":
/*  400 */             baseDir = value;
/*      */             break;
/*      */           default:
/*  403 */             throw new SAXParseException("Unexpected attribute \"" + key + "\"", this.helperImpl
/*  404 */                 .locator);
/*      */         } 
/*      */       
/*      */       } 
/*  408 */       if (def != null && !def.isEmpty()) {
/*  409 */         this.helperImpl.project.setDefault(def);
/*      */       } else {
/*  411 */         throw new BuildException("The default attribute is required");
/*      */       } 
/*      */       
/*  414 */       if (name != null) {
/*  415 */         this.helperImpl.project.setName(name);
/*  416 */         this.helperImpl.project.addReference(name, this.helperImpl.project);
/*      */       } 
/*      */       
/*  419 */       if (id != null) {
/*  420 */         this.helperImpl.project.addReference(id, this.helperImpl.project);
/*      */       }
/*      */       
/*  423 */       if (this.helperImpl.project.getProperty("basedir") != null) {
/*  424 */         this.helperImpl.project.setBasedir(this.helperImpl.project.getProperty("basedir"));
/*      */       }
/*  426 */       else if (baseDir == null) {
/*  427 */         this.helperImpl.project.setBasedir(this.helperImpl.buildFileParent.getAbsolutePath());
/*      */       
/*      */       }
/*  430 */       else if ((new File(baseDir)).isAbsolute()) {
/*  431 */         this.helperImpl.project.setBasedir(baseDir);
/*      */       } else {
/*  433 */         File resolvedBaseDir = ProjectHelperImpl.FILE_UTILS.resolveFile(this.helperImpl.buildFileParent, baseDir);
/*      */         
/*  435 */         this.helperImpl.project.setBaseDir(resolvedBaseDir);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  440 */       this.helperImpl.project.addTarget("", this.helperImpl.implicitTarget);
/*      */     }
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
/*      */     public void startElement(String name, AttributeList attrs) throws SAXParseException {
/*  459 */       if ("target".equals(name)) {
/*  460 */         handleTarget(name, attrs);
/*      */       } else {
/*  462 */         ProjectHelperImpl.handleElement(this.helperImpl, this, this.helperImpl.implicitTarget, name, attrs);
/*      */       } 
/*      */     }
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
/*      */     private void handleTarget(String tag, AttributeList attrs) throws SAXParseException {
/*  479 */       (new ProjectHelperImpl.TargetHandler(this.helperImpl, this)).init(tag, attrs);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class TargetHandler
/*      */     extends AbstractHandler
/*      */   {
/*      */     private Target target;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public TargetHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
/*  497 */       super(helperImpl, parentHandler);
/*      */     }
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
/*      */     public void init(String tag, AttributeList attrs) throws SAXParseException {
/*  518 */       String name = null;
/*  519 */       String depends = "";
/*  520 */       String ifCond = null;
/*  521 */       String unlessCond = null;
/*  522 */       String id = null;
/*  523 */       String description = null;
/*      */       
/*  525 */       for (int i = 0; i < attrs.getLength(); i++) {
/*  526 */         String key = attrs.getName(i);
/*  527 */         String value = attrs.getValue(i);
/*  528 */         switch (key) {
/*      */           case "name":
/*  530 */             name = value;
/*  531 */             if (name.isEmpty()) {
/*  532 */               throw new BuildException("name attribute must not be empty", new Location(this.helperImpl
/*  533 */                     .locator));
/*      */             }
/*      */             break;
/*      */           case "depends":
/*  537 */             depends = value;
/*      */             break;
/*      */           case "if":
/*  540 */             ifCond = value;
/*      */             break;
/*      */           case "unless":
/*  543 */             unlessCond = value;
/*      */             break;
/*      */           case "id":
/*  546 */             id = value;
/*      */             break;
/*      */           case "description":
/*  549 */             description = value;
/*      */             break;
/*      */           default:
/*  552 */             throw new SAXParseException("Unexpected attribute \"" + key + "\"", this.helperImpl
/*  553 */                 .locator);
/*      */         } 
/*      */       
/*      */       } 
/*  557 */       if (name == null) {
/*  558 */         throw new SAXParseException("target element appears without a name attribute", this.helperImpl
/*  559 */             .locator);
/*      */       }
/*      */       
/*  562 */       this.target = new Target();
/*      */ 
/*      */       
/*  565 */       this.target.addDependency("");
/*      */       
/*  567 */       this.target.setName(name);
/*  568 */       this.target.setIf(ifCond);
/*  569 */       this.target.setUnless(unlessCond);
/*  570 */       this.target.setDescription(description);
/*  571 */       this.helperImpl.project.addTarget(name, this.target);
/*      */       
/*  573 */       if (id != null && !id.isEmpty()) {
/*  574 */         this.helperImpl.project.addReference(id, this.target);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  579 */       if (!depends.isEmpty()) {
/*  580 */         this.target.setDepends(depends);
/*      */       }
/*      */     }
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
/*      */     public void startElement(String name, AttributeList attrs) throws SAXParseException {
/*  596 */       ProjectHelperImpl.handleElement(this.helperImpl, this, this.target, name, attrs);
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
/*      */   private static void handleElement(ProjectHelperImpl helperImpl, DocumentHandler parent, Target target, String elementName, AttributeList attrs) throws SAXParseException {
/*  610 */     if ("description".equals(elementName)) {
/*      */       
/*  612 */       new DescriptionHandler(helperImpl, parent);
/*  613 */     } else if (helperImpl.project.getDataTypeDefinitions().get(elementName) != null) {
/*  614 */       (new DataTypeHandler(helperImpl, parent, target)).init(elementName, attrs);
/*      */     } else {
/*  616 */       (new TaskHandler(helperImpl, parent, (TaskContainer)target, null, target)).init(elementName, attrs);
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
/*      */   static class DescriptionHandler
/*      */     extends AbstractHandler
/*      */   {
/*      */     public DescriptionHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler) {
/*  634 */       super(helperImpl, parentHandler);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void characters(char[] buf, int start, int count) {
/*  646 */       String text = new String(buf, start, count);
/*  647 */       String currentDescription = this.helperImpl.project.getDescription();
/*  648 */       if (currentDescription == null) {
/*  649 */         this.helperImpl.project.setDescription(text);
/*      */       } else {
/*  651 */         this.helperImpl.project.setDescription(currentDescription + text);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class TaskHandler
/*      */     extends AbstractHandler
/*      */   {
/*      */     private Target target;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private TaskContainer container;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Task task;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private RuntimeConfigurable parentWrapper;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  687 */     private RuntimeConfigurable wrapper = null;
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
/*      */     public TaskHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, TaskContainer container, RuntimeConfigurable parentWrapper, Target target) {
/*  708 */       super(helperImpl, parentHandler);
/*  709 */       this.container = container;
/*  710 */       this.parentWrapper = parentWrapper;
/*  711 */       this.target = target;
/*      */     }
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
/*      */     public void init(String tag, AttributeList attrs) throws SAXParseException {
/*      */       try {
/*  732 */         this.task = this.helperImpl.project.createTask(tag);
/*  733 */       } catch (BuildException buildException) {}
/*      */ 
/*      */ 
/*      */       
/*  737 */       if (this.task == null) {
/*  738 */         this.task = (Task)new UnknownElement(tag);
/*  739 */         this.task.setProject(this.helperImpl.project);
/*      */         
/*  741 */         this.task.setTaskName(tag);
/*      */       } 
/*  743 */       this.task.setLocation(new Location(this.helperImpl.locator));
/*  744 */       this.helperImpl.configureId(this.task, attrs);
/*      */       
/*  746 */       this.task.setOwningTarget(this.target);
/*  747 */       this.container.addTask(this.task);
/*  748 */       this.task.init();
/*  749 */       this.wrapper = this.task.getRuntimeConfigurableWrapper();
/*  750 */       this.wrapper.setAttributes(attrs);
/*  751 */       if (this.parentWrapper != null) {
/*  752 */         this.parentWrapper.addChild(this.wrapper);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void characters(char[] buf, int start, int count) {
/*  765 */       this.wrapper.addText(buf, start, count);
/*      */     }
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
/*      */     public void startElement(String name, AttributeList attrs) throws SAXParseException {
/*  782 */       if (this.task instanceof TaskContainer) {
/*      */         
/*  784 */         (new TaskHandler(this.helperImpl, this, (TaskContainer)this.task, this.wrapper, this.target)).init(name, attrs);
/*      */       } else {
/*      */         
/*  787 */         (new ProjectHelperImpl.NestedElementHandler(this.helperImpl, this, this.task, this.wrapper, this.target)).init(name, attrs);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class NestedElementHandler
/*      */     extends AbstractHandler
/*      */   {
/*      */     private Object parent;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object child;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private RuntimeConfigurable parentWrapper;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  814 */     private RuntimeConfigurable childWrapper = null;
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
/*      */     private Target target;
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
/*      */     public NestedElementHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, Object parent, RuntimeConfigurable parentWrapper, Target target) {
/*  840 */       super(helperImpl, parentHandler);
/*      */       
/*  842 */       if (parent instanceof TypeAdapter) {
/*  843 */         this.parent = ((TypeAdapter)parent).getProxy();
/*      */       } else {
/*  845 */         this.parent = parent;
/*      */       } 
/*  847 */       this.parentWrapper = parentWrapper;
/*  848 */       this.target = target;
/*      */     }
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
/*      */     public void init(String propType, AttributeList attrs) throws SAXParseException {
/*  868 */       Class<?> parentClass = this.parent.getClass();
/*  869 */       IntrospectionHelper ih = IntrospectionHelper.getHelper(this.helperImpl.project, parentClass);
/*      */       
/*      */       try {
/*  872 */         String elementName = propType.toLowerCase(Locale.ENGLISH);
/*  873 */         if (this.parent instanceof UnknownElement) {
/*  874 */           UnknownElement uc = new UnknownElement(elementName);
/*  875 */           uc.setProject(this.helperImpl.project);
/*  876 */           ((UnknownElement)this.parent).addChild(uc);
/*  877 */           this.child = uc;
/*      */         } else {
/*  879 */           this.child = ih.createElement(this.helperImpl.project, this.parent, elementName);
/*      */         } 
/*  881 */         this.helperImpl.configureId(this.child, attrs);
/*      */         
/*  883 */         this.childWrapper = new RuntimeConfigurable(this.child, propType);
/*  884 */         this.childWrapper.setAttributes(attrs);
/*  885 */         this.parentWrapper.addChild(this.childWrapper);
/*  886 */       } catch (BuildException exc) {
/*  887 */         throw new SAXParseException(exc.getMessage(), this.helperImpl.locator, exc);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void characters(char[] buf, int start, int count) {
/*  900 */       this.childWrapper.addText(buf, start, count);
/*      */     }
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
/*      */     public void startElement(String name, AttributeList attrs) throws SAXParseException {
/*  917 */       if (this.child instanceof TaskContainer) {
/*      */ 
/*      */         
/*  920 */         (new ProjectHelperImpl.TaskHandler(this.helperImpl, this, (TaskContainer)this.child, this.childWrapper, this.target))
/*  921 */           .init(name, attrs);
/*      */       } else {
/*  923 */         (new NestedElementHandler(this.helperImpl, this, this.child, this.childWrapper, this.target)).init(name, attrs);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class DataTypeHandler
/*      */     extends AbstractHandler
/*      */   {
/*      */     private Target target;
/*      */ 
/*      */     
/*      */     private Object element;
/*      */ 
/*      */     
/*  940 */     private RuntimeConfigurable wrapper = null;
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
/*      */     public DataTypeHandler(ProjectHelperImpl helperImpl, DocumentHandler parentHandler, Target target) {
/*  954 */       super(helperImpl, parentHandler);
/*  955 */       this.target = target;
/*      */     }
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
/*      */     public void init(String propType, AttributeList attrs) throws SAXParseException {
/*      */       try {
/*  976 */         this.element = this.helperImpl.project.createDataType(propType);
/*  977 */         if (this.element == null) {
/*  978 */           throw new BuildException("Unknown data type " + propType);
/*      */         }
/*  980 */         this.wrapper = new RuntimeConfigurable(this.element, propType);
/*  981 */         this.wrapper.setAttributes(attrs);
/*  982 */         this.target.addDataType(this.wrapper);
/*  983 */       } catch (BuildException exc) {
/*  984 */         throw new SAXParseException(exc.getMessage(), this.helperImpl.locator, exc);
/*      */       } 
/*      */     }
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
/*      */     public void characters(char[] buf, int start, int count) {
/*  999 */       this.wrapper.addText(buf, start, count);
/*      */     }
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
/*      */     public void startElement(String name, AttributeList attrs) throws SAXParseException {
/* 1015 */       (new ProjectHelperImpl.NestedElementHandler(this.helperImpl, this, this.element, this.wrapper, this.target)).init(name, attrs);
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
/*      */   private void configureId(Object target, AttributeList attr) {
/* 1030 */     String id = attr.getValue("id");
/* 1031 */     if (id != null)
/* 1032 */       this.project.addReference(id, target); 
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/helper/ProjectHelperImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */