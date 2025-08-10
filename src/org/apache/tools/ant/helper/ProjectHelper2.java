/*      */ package org.apache.tools.ant.helper;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.file.Files;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Stack;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.ExtensionPoint;
/*      */ import org.apache.tools.ant.Location;
/*      */ import org.apache.tools.ant.Project;
/*      */ import org.apache.tools.ant.ProjectHelper;
/*      */ import org.apache.tools.ant.RuntimeConfigurable;
/*      */ import org.apache.tools.ant.Target;
/*      */ import org.apache.tools.ant.Task;
/*      */ import org.apache.tools.ant.UnknownElement;
/*      */ import org.apache.tools.ant.launch.Locator;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.resources.FileProvider;
/*      */ import org.apache.tools.ant.types.resources.URLProvider;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.JAXPUtils;
/*      */ import org.apache.tools.zip.ZipFile;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.XMLReader;
/*      */ import org.xml.sax.helpers.DefaultHandler;
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
/*      */ 
/*      */ public class ProjectHelper2
/*      */   extends ProjectHelper
/*      */ {
/*      */   public static final String REFID_TARGETS = "ant.targets";
/*   70 */   private static AntHandler elementHandler = new ElementHandler();
/*   71 */   private static AntHandler targetHandler = new TargetHandler();
/*   72 */   private static AntHandler mainHandler = new MainHandler();
/*   73 */   private static AntHandler projectHandler = new ProjectHandler();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String REFID_CONTEXT = "ant.parsing.context";
/*      */ 
/*      */ 
/*      */   
/*   81 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
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
/*      */   public boolean canParseAntlibDescriptor(Resource resource) {
/*   94 */     return true;
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
/*      */   public UnknownElement parseAntlibDescriptor(Project containingProject, Resource resource) {
/*  110 */     URLProvider up = (URLProvider)resource.as(URLProvider.class);
/*  111 */     if (up == null) {
/*  112 */       throw new BuildException("Unsupported resource type: " + resource);
/*      */     }
/*  114 */     return parseUnknownElement(containingProject, up.getURL());
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
/*      */   public UnknownElement parseUnknownElement(Project project, URL source) throws BuildException {
/*  127 */     Target dummyTarget = new Target();
/*  128 */     dummyTarget.setProject(project);
/*      */     
/*  130 */     AntXMLContext context = new AntXMLContext(project);
/*  131 */     context.addTarget(dummyTarget);
/*  132 */     context.setImplicitTarget(dummyTarget);
/*      */     
/*  134 */     parse(context.getProject(), source, new RootHandler(context, elementHandler));
/*  135 */     Task[] tasks = dummyTarget.getTasks();
/*  136 */     if (tasks.length != 1) {
/*  137 */       throw new BuildException("No tasks defined");
/*      */     }
/*  139 */     return (UnknownElement)tasks[0];
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
/*  151 */     getImportStack().addElement(source);
/*  152 */     AntXMLContext context = null;
/*  153 */     context = (AntXMLContext)project.getReference("ant.parsing.context");
/*  154 */     if (context == null) {
/*  155 */       context = new AntXMLContext(project);
/*  156 */       project.addReference("ant.parsing.context", context);
/*  157 */       project.addReference("ant.targets", context.getTargets());
/*      */     } 
/*  159 */     if (getImportStack().size() > 1) {
/*      */       
/*  161 */       context.setIgnoreProjectTag(true);
/*  162 */       Target currentTarget = context.getCurrentTarget();
/*  163 */       Target currentImplicit = context.getImplicitTarget();
/*  164 */       Map<String, Target> currentTargets = context.getCurrentTargets();
/*      */       try {
/*  166 */         Target newCurrent = new Target();
/*  167 */         newCurrent.setProject(project);
/*  168 */         newCurrent.setName("");
/*  169 */         context.setCurrentTarget(newCurrent);
/*  170 */         context.setCurrentTargets(new HashMap<>());
/*  171 */         context.setImplicitTarget(newCurrent);
/*  172 */         parse(project, source, new RootHandler(context, mainHandler));
/*  173 */         newCurrent.execute();
/*      */       } finally {
/*  175 */         context.setCurrentTarget(currentTarget);
/*  176 */         context.setImplicitTarget(currentImplicit);
/*  177 */         context.setCurrentTargets(currentTargets);
/*      */       } 
/*      */     } else {
/*      */       
/*  181 */       context.setCurrentTargets(new HashMap<>());
/*  182 */       parse(project, source, new RootHandler(context, mainHandler));
/*      */       
/*  184 */       context.getImplicitTarget().execute();
/*      */ 
/*      */       
/*  187 */       resolveExtensionOfAttributes(project);
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
/*      */   public void parse(Project project, Object source, RootHandler handler) throws BuildException {
/*  202 */     AntXMLContext context = handler.context;
/*      */     
/*  204 */     File buildFile = null;
/*  205 */     URL url = null;
/*  206 */     String buildFileName = null;
/*      */     
/*  208 */     if (source instanceof File) {
/*  209 */       buildFile = (File)source;
/*  210 */     } else if (source instanceof URL) {
/*  211 */       url = (URL)source;
/*  212 */     } else if (source instanceof Resource) {
/*  213 */       FileProvider fp = (FileProvider)((Resource)source).as(FileProvider.class);
/*  214 */       if (fp != null) {
/*  215 */         buildFile = fp.getFile();
/*      */       } else {
/*  217 */         URLProvider up = (URLProvider)((Resource)source).as(URLProvider.class);
/*  218 */         if (up != null) {
/*  219 */           url = up.getURL();
/*      */         }
/*      */       } 
/*      */     } 
/*  223 */     if (buildFile != null) {
/*  224 */       buildFile = FILE_UTILS.normalize(buildFile.getAbsolutePath());
/*  225 */       context.setBuildFile(buildFile);
/*  226 */       buildFileName = buildFile.toString();
/*  227 */     } else if (url != null) {
/*      */       try {
/*  229 */         context.setBuildFile((File)null);
/*  230 */         context.setBuildFile(url);
/*  231 */       } catch (MalformedURLException ex) {
/*  232 */         throw new BuildException(ex);
/*      */       } 
/*  234 */       buildFileName = url.toString();
/*      */     } else {
/*  236 */       throw new BuildException("Source " + source.getClass().getName() + " not supported by this plugin");
/*      */     } 
/*      */     
/*  239 */     InputStream inputStream = null;
/*  240 */     InputSource inputSource = null;
/*  241 */     ZipFile zf = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  247 */       XMLReader parser = JAXPUtils.getNamespaceXMLReader();
/*      */       
/*  249 */       String uri = null;
/*  250 */       if (buildFile != null) {
/*  251 */         uri = FILE_UTILS.toURI(buildFile.getAbsolutePath());
/*  252 */         inputStream = Files.newInputStream(buildFile.toPath(), new java.nio.file.OpenOption[0]);
/*      */       } else {
/*  254 */         uri = url.toString();
/*  255 */         int pling = uri.indexOf("!/");
/*  256 */         if (uri.startsWith("jar:file") && pling > -1) {
/*      */           
/*  258 */           zf = new ZipFile(Locator.fromJarURI(uri), "UTF-8");
/*      */           
/*  260 */           inputStream = zf.getInputStream(zf.getEntry(uri.substring(pling + 2)));
/*      */         } else {
/*  262 */           URLConnection conn = url.openConnection();
/*  263 */           conn.setUseCaches(false);
/*  264 */           inputStream = conn.getInputStream();
/*      */         } 
/*      */       } 
/*      */       
/*  268 */       inputSource = new InputSource(inputStream);
/*  269 */       if (uri != null) {
/*  270 */         inputSource.setSystemId(uri);
/*      */       }
/*  272 */       project.log("parsing buildfile " + buildFileName + " with URI = " + uri + (
/*  273 */           (zf != null) ? " from a zip file" : ""), 3);
/*      */ 
/*      */       
/*  276 */       parser.setContentHandler(handler);
/*  277 */       parser.setEntityResolver(handler);
/*  278 */       parser.setErrorHandler(handler);
/*  279 */       parser.setDTDHandler(handler);
/*  280 */       parser.parse(inputSource);
/*  281 */     } catch (SAXParseException exc) {
/*      */       
/*  283 */       Location location = new Location(exc.getSystemId(), exc.getLineNumber(), exc.getColumnNumber());
/*      */       
/*  285 */       Throwable t = exc.getException();
/*  286 */       if (t instanceof BuildException) {
/*  287 */         BuildException be = (BuildException)t;
/*  288 */         if (be.getLocation() == Location.UNKNOWN_LOCATION) {
/*  289 */           be.setLocation(location);
/*      */         }
/*  291 */         throw be;
/*      */       } 
/*  293 */       throw new BuildException(exc.getMessage(), (t == null) ? exc : t, location);
/*  294 */     } catch (SAXException exc) {
/*  295 */       Throwable t = exc.getException();
/*  296 */       if (t instanceof BuildException) {
/*  297 */         throw (BuildException)t;
/*      */       }
/*  299 */       throw new BuildException(exc.getMessage(), (t == null) ? exc : t);
/*  300 */     } catch (FileNotFoundException exc) {
/*  301 */       throw new BuildException(exc);
/*  302 */     } catch (UnsupportedEncodingException exc) {
/*  303 */       throw new BuildException("Encoding of project file " + buildFileName + " is invalid.", exc);
/*      */     }
/*  305 */     catch (IOException exc) {
/*  306 */       throw new BuildException("Error reading project file " + buildFileName + ": " + exc
/*  307 */           .getMessage(), exc);
/*      */     } finally {
/*  309 */       FileUtils.close(inputStream);
/*  310 */       ZipFile.closeQuietly(zf);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static AntHandler getMainHandler() {
/*  319 */     return mainHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void setMainHandler(AntHandler handler) {
/*  327 */     mainHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static AntHandler getProjectHandler() {
/*  335 */     return projectHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void setProjectHandler(AntHandler handler) {
/*  343 */     projectHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static AntHandler getTargetHandler() {
/*  351 */     return targetHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void setTargetHandler(AntHandler handler) {
/*  359 */     targetHandler = handler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static AntHandler getElementHandler() {
/*  367 */     return elementHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static void setElementHandler(AntHandler handler) {
/*  375 */     elementHandler = handler;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class AntHandler
/*      */   {
/*      */     public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {}
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
/*      */     public AntHandler onStartChild(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
/*  425 */       throw new SAXParseException("Unexpected element \"" + qname + " \"", context
/*  426 */           .getLocator());
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
/*      */     public void onEndChild(String uri, String tag, String qname, AntXMLContext context) throws SAXParseException {}
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
/*      */     public void onEndElement(String uri, String tag, AntXMLContext context) {}
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
/*      */     public void characters(char[] buf, int start, int count, AntXMLContext context) throws SAXParseException {
/*  467 */       String s = (new String(buf, start, count)).trim();
/*      */       
/*  469 */       if (!s.isEmpty()) {
/*  470 */         throw new SAXParseException("Unexpected text \"" + s + "\"", context.getLocator());
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void checkNamespace(String uri) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class RootHandler
/*      */     extends DefaultHandler
/*      */   {
/*  489 */     private Stack<ProjectHelper2.AntHandler> antHandlers = new Stack<>();
/*  490 */     private ProjectHelper2.AntHandler currentHandler = null;
/*      */ 
/*      */ 
/*      */     
/*      */     private AntXMLContext context;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public RootHandler(AntXMLContext context, ProjectHelper2.AntHandler rootHandler) {
/*  500 */       this.currentHandler = rootHandler;
/*  501 */       this.antHandlers.push(this.currentHandler);
/*  502 */       this.context = context;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ProjectHelper2.AntHandler getCurrentAntHandler() {
/*  510 */       return this.currentHandler;
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
/*      */     public InputSource resolveEntity(String publicId, String systemId) {
/*  526 */       this.context.getProject().log("resolving systemId: " + systemId, 3);
/*      */       
/*  528 */       if (systemId.startsWith("file:")) {
/*  529 */         String path = ProjectHelper2.FILE_UTILS.fromURI(systemId);
/*      */         
/*  531 */         File file = new File(path);
/*  532 */         if (!file.isAbsolute()) {
/*  533 */           file = ProjectHelper2.FILE_UTILS.resolveFile(this.context.getBuildFileParent(), path);
/*  534 */           this.context.getProject().log("Warning: '" + systemId + "' in " + this.context
/*  535 */               .getBuildFile() + " should be expressed simply as '" + path
/*  536 */               .replace('\\', '/') + "' for compliance with other XML tools", 1);
/*      */         } 
/*      */         
/*  539 */         this.context.getProject().log("file=" + file, 4);
/*      */         try {
/*  541 */           InputSource inputSource = new InputSource(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]));
/*  542 */           inputSource.setSystemId(ProjectHelper2.FILE_UTILS.toURI(file.getAbsolutePath()));
/*  543 */           return inputSource;
/*  544 */         } catch (IOException fne) {
/*  545 */           this.context.getProject().log(file.getAbsolutePath() + " could not be found", 1);
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  551 */       this.context.getProject().log("could not resolve systemId", 4);
/*  552 */       return null;
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
/*      */     public void startElement(String uri, String tag, String qname, Attributes attrs) throws SAXParseException {
/*  572 */       ProjectHelper2.AntHandler next = this.currentHandler.onStartChild(uri, tag, qname, attrs, this.context);
/*  573 */       this.antHandlers.push(this.currentHandler);
/*  574 */       this.currentHandler = next;
/*  575 */       this.currentHandler.onStartElement(uri, tag, qname, attrs, this.context);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setDocumentLocator(Locator locator) {
/*  586 */       this.context.setLocator(locator);
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
/*      */     public void endElement(String uri, String name, String qName) throws SAXException {
/*  602 */       this.currentHandler.onEndElement(uri, name, this.context);
/*  603 */       this.currentHandler = this.antHandlers.pop();
/*  604 */       if (this.currentHandler != null) {
/*  605 */         this.currentHandler.onEndChild(uri, name, qName, this.context);
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
/*      */     public void characters(char[] buf, int start, int count) throws SAXParseException {
/*  619 */       this.currentHandler.characters(buf, start, count, this.context);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void startPrefixMapping(String prefix, String uri) {
/*  630 */       this.context.startPrefixMapping(prefix, uri);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void endPrefixMapping(String prefix) {
/*  640 */       this.context.endPrefixMapping(prefix);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class MainHandler
/*      */     extends AntHandler
/*      */   {
/*      */     public ProjectHelper2.AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
/*  665 */       if ("project".equals(name) && (uri
/*  666 */         .isEmpty() || uri.equals("antlib:org.apache.tools.ant"))) {
/*  667 */         return ProjectHelper2.projectHandler;
/*      */       }
/*  669 */       if (name.equals(qname)) {
/*  670 */         throw new SAXParseException("Unexpected element \"{" + uri + "}" + name + "\" {" + "antlib:org.apache.tools.ant" + "}" + name, context
/*  671 */             .getLocator());
/*      */       }
/*  673 */       throw new SAXParseException("Unexpected element \"" + qname + "\" " + name, context
/*  674 */           .getLocator());
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
/*      */   public static class ProjectHandler
/*      */     extends AntHandler
/*      */   {
/*      */     public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
/*  705 */       String baseDir = null;
/*  706 */       boolean nameAttributeSet = false;
/*      */       
/*  708 */       Project project = context.getProject();
/*      */       
/*  710 */       context.getImplicitTarget().setLocation(new Location(context.getLocator()));
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
/*  723 */       for (int i = 0; i < attrs.getLength(); i++) {
/*  724 */         String attrUri = attrs.getURI(i);
/*  725 */         if (attrUri == null || attrUri.isEmpty() || attrUri.equals(uri)) {
/*      */ 
/*      */           
/*  728 */           String value = attrs.getValue(i);
/*  729 */           switch (attrs.getLocalName(i)) {
/*      */             case "default":
/*  731 */               if (value != null && !value.isEmpty() && 
/*  732 */                 !context.isIgnoringProjectTag()) {
/*  733 */                 project.setDefault(value);
/*      */               }
/*      */               break;
/*      */             
/*      */             case "name":
/*  738 */               if (value != null) {
/*  739 */                 context.setCurrentProjectName(value);
/*  740 */                 nameAttributeSet = true;
/*  741 */                 if (!context.isIgnoringProjectTag()) {
/*  742 */                   project.setName(value);
/*  743 */                   project.addReference(value, project); break;
/*  744 */                 }  if (ProjectHelper.isInIncludeMode() && 
/*  745 */                   !value.isEmpty() && ProjectHelper.getCurrentTargetPrefix() != null && 
/*  746 */                   ProjectHelper.getCurrentTargetPrefix().endsWith("USE_PROJECT_NAME_AS_TARGET_PREFIX")) {
/*  747 */                   String newTargetPrefix = ProjectHelper.getCurrentTargetPrefix().replace("USE_PROJECT_NAME_AS_TARGET_PREFIX", value);
/*      */                   
/*  749 */                   ProjectHelper.setCurrentTargetPrefix(newTargetPrefix);
/*      */                 } 
/*      */               } 
/*      */               break;
/*      */             
/*      */             case "id":
/*  755 */               if (value != null)
/*      */               {
/*  757 */                 if (!context.isIgnoringProjectTag()) {
/*  758 */                   project.addReference(value, project);
/*      */                 }
/*      */               }
/*      */               break;
/*      */             case "basedir":
/*  763 */               if (!context.isIgnoringProjectTag()) {
/*  764 */                 baseDir = value;
/*      */               }
/*      */               break;
/*      */             
/*      */             default:
/*  769 */               throw new SAXParseException("Unexpected attribute \"" + attrs.getQName(i) + "\"", context
/*  770 */                   .getLocator());
/*      */           } 
/*      */ 
/*      */         
/*      */         } 
/*      */       } 
/*  776 */       String antFileProp = "ant.file." + context.getCurrentProjectName();
/*  777 */       String dup = project.getProperty(antFileProp);
/*      */       
/*  779 */       String typeProp = "ant.file.type." + context.getCurrentProjectName();
/*  780 */       String dupType = project.getProperty(typeProp);
/*  781 */       if (dup != null && nameAttributeSet) {
/*  782 */         Object dupFile = null;
/*  783 */         Object contextFile = null;
/*  784 */         if ("url".equals(dupType)) {
/*      */           try {
/*  786 */             dupFile = new URL(dup);
/*  787 */           } catch (MalformedURLException mue) {
/*  788 */             throw new BuildException("failed to parse " + dup + " as URL while looking at a duplicate project name.", mue);
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  793 */           contextFile = context.getBuildFileURL();
/*      */         } else {
/*  795 */           dupFile = new File(dup);
/*  796 */           contextFile = context.getBuildFile();
/*      */         } 
/*      */         
/*  799 */         if (context.isIgnoringProjectTag() && !dupFile.equals(contextFile)) {
/*  800 */           project.log("Duplicated project name in import. Project " + context
/*  801 */               .getCurrentProjectName() + " defined first in " + dup + " and again in " + contextFile, 1);
/*      */         }
/*      */       } 
/*      */       
/*  805 */       if (nameAttributeSet) {
/*  806 */         if (context.getBuildFile() != null) {
/*  807 */           project.setUserProperty(antFileProp, context
/*  808 */               .getBuildFile().toString());
/*  809 */           project.setUserProperty(typeProp, "file");
/*      */         }
/*  811 */         else if (context.getBuildFileURL() != null) {
/*  812 */           project.setUserProperty(antFileProp, context
/*  813 */               .getBuildFileURL().toString());
/*  814 */           project.setUserProperty(typeProp, "url");
/*      */         } 
/*      */       }
/*      */       
/*  818 */       if (context.isIgnoringProjectTag()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  823 */       if (project.getProperty("basedir") != null) {
/*  824 */         project.setBasedir(project.getProperty("basedir"));
/*      */       
/*      */       }
/*  827 */       else if (baseDir == null) {
/*  828 */         project.setBasedir(context.getBuildFileParent().getAbsolutePath());
/*      */       
/*      */       }
/*  831 */       else if ((new File(baseDir)).isAbsolute()) {
/*  832 */         project.setBasedir(baseDir);
/*      */       } else {
/*  834 */         project.setBaseDir(ProjectHelper2.FILE_UTILS.resolveFile(context.getBuildFileParent(), baseDir));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  839 */       project.addTarget("", context.getImplicitTarget());
/*  840 */       context.setCurrentTarget(context.getImplicitTarget());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ProjectHelper2.AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
/*  866 */       return 
/*  867 */         (("target".equals(name) || "extension-point".equals(name)) && (uri.isEmpty() || uri.equals("antlib:org.apache.tools.ant"))) ? ProjectHelper2
/*  868 */         .targetHandler : ProjectHelper2.elementHandler;
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
/*      */   public static class TargetHandler
/*      */     extends AntHandler
/*      */   {
/*      */     public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
/*  900 */       String name = null;
/*  901 */       String depends = "";
/*  902 */       String extensionPoint = null;
/*  903 */       ProjectHelper.OnMissingExtensionPoint extensionPointMissing = null;
/*      */       
/*  905 */       Project project = context.getProject();
/*      */       
/*  907 */       Target target = "target".equals(tag) ? new Target() : (Target)new ExtensionPoint();
/*  908 */       target.setProject(project);
/*  909 */       target.setLocation(new Location(context.getLocator()));
/*  910 */       context.addTarget(target);
/*      */       
/*  912 */       for (int i = 0; i < attrs.getLength(); i++) {
/*  913 */         String attrUri = attrs.getURI(i);
/*  914 */         if (attrUri == null || attrUri.isEmpty() || attrUri.equals(uri)) {
/*      */ 
/*      */           
/*  917 */           String value = attrs.getValue(i);
/*  918 */           switch (attrs.getLocalName(i)) {
/*      */             case "name":
/*  920 */               name = value;
/*  921 */               if (name.isEmpty()) {
/*  922 */                 throw new BuildException("name attribute must not be empty");
/*      */               }
/*      */               break;
/*      */             case "depends":
/*  926 */               depends = value;
/*      */               break;
/*      */             case "if":
/*  929 */               target.setIf(value);
/*      */               break;
/*      */             case "unless":
/*  932 */               target.setUnless(value);
/*      */               break;
/*      */             case "id":
/*  935 */               if (value != null && !value.isEmpty()) {
/*  936 */                 context.getProject().addReference(value, target);
/*      */               }
/*      */               break;
/*      */             case "description":
/*  940 */               target.setDescription(value);
/*      */               break;
/*      */             case "extensionOf":
/*  943 */               extensionPoint = value;
/*      */               break;
/*      */             case "onMissingExtensionPoint":
/*      */               try {
/*  947 */                 extensionPointMissing = ProjectHelper.OnMissingExtensionPoint.valueOf(value);
/*  948 */               } catch (IllegalArgumentException e) {
/*  949 */                 throw new BuildException("Invalid onMissingExtensionPoint " + value);
/*      */               } 
/*      */               break;
/*      */             default:
/*  953 */               throw new SAXParseException("Unexpected attribute \"" + attrs.getQName(i) + "\"", context
/*  954 */                   .getLocator());
/*      */           } 
/*      */         } 
/*      */       } 
/*  958 */       if (name == null) {
/*  959 */         throw new SAXParseException("target element appears without a name attribute", context
/*  960 */             .getLocator());
/*      */       }
/*      */       
/*  963 */       String prefix = null;
/*      */       
/*  965 */       boolean isInIncludeMode = (context.isIgnoringProjectTag() && ProjectHelper.isInIncludeMode());
/*  966 */       String sep = ProjectHelper.getCurrentPrefixSeparator();
/*      */       
/*  968 */       if (isInIncludeMode) {
/*  969 */         prefix = getTargetPrefix(context);
/*  970 */         if (prefix == null) {
/*  971 */           throw new BuildException("can't include build file " + context
/*  972 */               .getBuildFileURL() + ", no as attribute has been given and the project tag doesn't specify a name attribute");
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  977 */         name = prefix + sep + name;
/*      */       } 
/*      */ 
/*      */       
/*  981 */       if (context.getCurrentTargets().get(name) != null) {
/*  982 */         throw new BuildException("Duplicate target '" + name + "'", target
/*  983 */             .getLocation());
/*      */       }
/*  985 */       Hashtable<String, Target> projectTargets = project.getTargets();
/*  986 */       boolean usedTarget = false;
/*      */       
/*  988 */       if (projectTargets.containsKey(name)) {
/*  989 */         project.log("Already defined in main or a previous import, ignore " + name, 3);
/*      */       } else {
/*      */         
/*  992 */         target.setName(name);
/*  993 */         context.getCurrentTargets().put(name, target);
/*  994 */         project.addOrReplaceTarget(name, target);
/*  995 */         usedTarget = true;
/*      */       } 
/*      */       
/*  998 */       if (!depends.isEmpty()) {
/*  999 */         if (!isInIncludeMode) {
/* 1000 */           target.setDepends(depends);
/*      */         } else {
/* 1002 */           for (String string : Target.parseDepends(depends, name, "depends")) {
/* 1003 */             target.addDependency(prefix + sep + string);
/*      */           }
/*      */         } 
/*      */       }
/* 1007 */       if (!isInIncludeMode && context.isIgnoringProjectTag() && (
/* 1008 */         prefix = getTargetPrefix(context)) != null) {
/*      */ 
/*      */         
/* 1011 */         String newName = prefix + sep + name;
/* 1012 */         Target newTarget = target;
/* 1013 */         if (usedTarget)
/*      */         {
/* 1015 */           newTarget = "target".equals(tag) ? new Target(target) : (Target)new ExtensionPoint(target);
/*      */         }
/* 1017 */         newTarget.setName(newName);
/* 1018 */         context.getCurrentTargets().put(newName, newTarget);
/* 1019 */         project.addOrReplaceTarget(newName, newTarget);
/*      */       } 
/* 1021 */       if (extensionPointMissing != null && extensionPoint == null) {
/* 1022 */         throw new BuildException("onMissingExtensionPoint attribute cannot be specified unless extensionOf is specified", target
/*      */             
/* 1024 */             .getLocation());
/*      */       }
/*      */       
/* 1027 */       if (extensionPoint != null) {
/*      */         
/* 1029 */         ProjectHelper helper = (ProjectHelper)context.getProject().getReference("ant.projectHelper");
/* 1030 */         for (String extPointName : Target.parseDepends(extensionPoint, name, "extensionOf")) {
/* 1031 */           if (extensionPointMissing == null) {
/* 1032 */             extensionPointMissing = ProjectHelper.OnMissingExtensionPoint.FAIL;
/*      */           }
/*      */ 
/*      */           
/* 1036 */           if (ProjectHelper.isInIncludeMode()) {
/*      */ 
/*      */ 
/*      */             
/* 1040 */             helper.getExtensionStack().add(new String[] { extPointName, target
/* 1041 */                   .getName(), extensionPointMissing
/* 1042 */                   .name(), prefix + sep }); continue;
/*      */           } 
/* 1044 */           helper.getExtensionStack().add(new String[] { extPointName, target
/* 1045 */                 .getName(), extensionPointMissing
/* 1046 */                 .name() });
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private String getTargetPrefix(AntXMLContext context) {
/* 1053 */       String configuredValue = ProjectHelper.getCurrentTargetPrefix();
/* 1054 */       if (configuredValue != null && configuredValue.isEmpty()) {
/* 1055 */         configuredValue = null;
/*      */       }
/* 1057 */       if (configuredValue != null) {
/* 1058 */         return configuredValue;
/*      */       }
/*      */       
/* 1061 */       String projectName = context.getCurrentProjectName();
/* 1062 */       if (projectName != null && projectName.isEmpty()) {
/* 1063 */         projectName = null;
/*      */       }
/*      */       
/* 1066 */       return projectName;
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
/*      */     public ProjectHelper2.AntHandler onStartChild(String uri, String name, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
/* 1087 */       return ProjectHelper2.elementHandler;
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
/*      */     public void onEndElement(String uri, String tag, AntXMLContext context) {
/* 1100 */       context.setCurrentTarget(context.getImplicitTarget());
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
/*      */   public static class ElementHandler
/*      */     extends AntHandler
/*      */   {
/*      */     public void onStartElement(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
/* 1136 */       RuntimeConfigurable parentWrapper = context.currentWrapper();
/* 1137 */       Object parent = null;
/*      */       
/* 1139 */       if (parentWrapper != null) {
/* 1140 */         parent = parentWrapper.getProxy();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1145 */       UnknownElement task = new UnknownElement(tag);
/* 1146 */       task.setProject(context.getProject());
/* 1147 */       task.setNamespace(uri);
/* 1148 */       task.setQName(qname);
/* 1149 */       task.setTaskType(ProjectHelper.genComponentName(task.getNamespace(), tag));
/* 1150 */       task.setTaskName(qname);
/*      */ 
/*      */       
/* 1153 */       Location location = new Location(context.getLocator().getSystemId(), context.getLocator().getLineNumber(), context.getLocator().getColumnNumber());
/* 1154 */       task.setLocation(location);
/* 1155 */       task.setOwningTarget(context.getCurrentTarget());
/*      */       
/* 1157 */       if (parent != null) {
/*      */         
/* 1159 */         ((UnknownElement)parent).addChild(task);
/*      */       } else {
/*      */         
/* 1162 */         context.getCurrentTarget().addTask((Task)task);
/*      */       } 
/*      */       
/* 1165 */       context.configureId(task, attrs);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1170 */       RuntimeConfigurable wrapper = new RuntimeConfigurable(task, task.getTaskName());
/*      */       
/* 1172 */       for (int i = 0; i < attrs.getLength(); i++) {
/* 1173 */         String name = attrs.getLocalName(i);
/* 1174 */         String attrUri = attrs.getURI(i);
/* 1175 */         if (attrUri != null && !attrUri.isEmpty() && !attrUri.equals(uri)) {
/* 1176 */           name = attrUri + ":" + attrs.getQName(i);
/*      */         }
/* 1178 */         String value = attrs.getValue(i);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1183 */         if ("ant-type".equals(name) || ("antlib:org.apache.tools.ant"
/* 1184 */           .equals(attrUri) && "ant-type"
/* 1185 */           .equals(attrs.getLocalName(i)))) {
/* 1186 */           name = "ant-type";
/* 1187 */           int index = value.indexOf(":");
/* 1188 */           if (index >= 0) {
/* 1189 */             String prefix = value.substring(0, index);
/* 1190 */             String mappedUri = context.getPrefixMapping(prefix);
/* 1191 */             if (mappedUri == null) {
/* 1192 */               throw new BuildException("Unable to find XML NS prefix \"" + prefix + "\"");
/*      */             }
/*      */             
/* 1195 */             value = ProjectHelper.genComponentName(mappedUri, value
/* 1196 */                 .substring(index + 1));
/*      */           } 
/*      */         } 
/* 1199 */         wrapper.setAttribute(name, value);
/*      */       } 
/* 1201 */       if (parentWrapper != null) {
/* 1202 */         parentWrapper.addChild(wrapper);
/*      */       }
/* 1204 */       context.pushWrapper(wrapper);
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
/*      */     public void characters(char[] buf, int start, int count, AntXMLContext context) throws SAXParseException {
/* 1223 */       RuntimeConfigurable wrapper = context.currentWrapper();
/* 1224 */       wrapper.addText(buf, start, count);
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
/*      */ 
/*      */     
/*      */     public ProjectHelper2.AntHandler onStartChild(String uri, String tag, String qname, Attributes attrs, AntXMLContext context) throws SAXParseException {
/* 1247 */       return ProjectHelper2.elementHandler;
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
/*      */     public void onEndElement(String uri, String tag, AntXMLContext context) {
/* 1260 */       context.popWrapper();
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/helper/ProjectHelper2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */