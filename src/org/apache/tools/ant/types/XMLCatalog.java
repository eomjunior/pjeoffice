/*      */ package org.apache.tools.ant.types;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLConnection;
/*      */ import java.nio.file.Files;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ import javax.xml.transform.Source;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import javax.xml.transform.URIResolver;
/*      */ import javax.xml.transform.sax.SAXSource;
/*      */ import org.apache.tools.ant.AntClassLoader;
/*      */ import org.apache.tools.ant.BuildException;
/*      */ import org.apache.tools.ant.Project;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.JAXPUtils;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.XMLReader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class XMLCatalog
/*      */   extends DataType
/*      */   implements EntityResolver, URIResolver
/*      */ {
/*  122 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  127 */   private Vector<ResourceLocation> elements = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Path classpath;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Path catalogPath;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String APACHE_RESOLVER = "org.apache.tools.ant.types.resolver.ApacheCatalogResolver";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String CATALOG_RESOLVER = "org.apache.xml.resolver.tools.CatalogResolver";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CatalogResolver catalogResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Vector<ResourceLocation> getElements() {
/*  168 */     return (getRef()).elements;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Path getClasspath() {
/*  177 */     return (getRef()).classpath;
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
/*      */   public Path createClasspath() {
/*  189 */     if (isReference()) {
/*  190 */       throw noChildrenAllowed();
/*      */     }
/*  192 */     if (this.classpath == null) {
/*  193 */       this.classpath = new Path(getProject());
/*      */     }
/*  195 */     setChecked(false);
/*  196 */     return this.classpath.createPath();
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
/*      */   public void setClasspath(Path classpath) {
/*  208 */     if (isReference()) {
/*  209 */       throw tooManyAttributes();
/*      */     }
/*  211 */     if (this.classpath == null) {
/*  212 */       this.classpath = classpath;
/*      */     } else {
/*  214 */       this.classpath.append(classpath);
/*      */     } 
/*  216 */     setChecked(false);
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
/*      */   public void setClasspathRef(Reference r) {
/*  228 */     if (isReference()) {
/*  229 */       throw tooManyAttributes();
/*      */     }
/*  231 */     createClasspath().setRefid(r);
/*  232 */     setChecked(false);
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
/*      */   public Path createCatalogPath() {
/*  245 */     if (isReference()) {
/*  246 */       throw noChildrenAllowed();
/*      */     }
/*  248 */     if (this.catalogPath == null) {
/*  249 */       this.catalogPath = new Path(getProject());
/*      */     }
/*  251 */     setChecked(false);
/*  252 */     return this.catalogPath.createPath();
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
/*      */   public void setCatalogPathRef(Reference r) {
/*  265 */     if (isReference()) {
/*  266 */       throw tooManyAttributes();
/*      */     }
/*  268 */     createCatalogPath().setRefid(r);
/*  269 */     setChecked(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Path getCatalogPath() {
/*  279 */     return (getRef()).catalogPath;
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
/*      */   public void addDTD(ResourceLocation dtd) throws BuildException {
/*  295 */     if (isReference()) {
/*  296 */       throw noChildrenAllowed();
/*      */     }
/*      */     
/*  299 */     getElements().addElement(dtd);
/*  300 */     setChecked(false);
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
/*      */   public void addEntity(ResourceLocation entity) throws BuildException {
/*  315 */     addDTD(entity);
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
/*      */   public void addConfiguredXMLCatalog(XMLCatalog catalog) {
/*  327 */     if (isReference()) {
/*  328 */       throw noChildrenAllowed();
/*      */     }
/*      */ 
/*      */     
/*  332 */     getElements().addAll(catalog.getElements());
/*      */ 
/*      */     
/*  335 */     Path nestedClasspath = catalog.getClasspath();
/*  336 */     createClasspath().append(nestedClasspath);
/*      */ 
/*      */     
/*  339 */     Path nestedCatalogPath = catalog.getCatalogPath();
/*  340 */     createCatalogPath().append(nestedCatalogPath);
/*  341 */     setChecked(false);
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
/*      */   public void setRefid(Reference r) throws BuildException {
/*  358 */     if (!this.elements.isEmpty()) {
/*  359 */       throw tooManyAttributes();
/*      */     }
/*  361 */     super.setRefid(r);
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
/*      */   public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/*  377 */     if (isReference()) {
/*  378 */       return getRef().resolveEntity(publicId, systemId);
/*      */     }
/*      */     
/*  381 */     dieOnCircularReference();
/*      */     
/*  383 */     log("resolveEntity: '" + publicId + "': '" + systemId + "'", 4);
/*      */ 
/*      */ 
/*      */     
/*  387 */     InputSource inputSource = getCatalogResolver().resolveEntity(publicId, systemId);
/*      */     
/*  389 */     if (inputSource == null) {
/*  390 */       log("No matching catalog entry found, parser will use: '" + systemId + "'", 4);
/*      */     }
/*      */ 
/*      */     
/*  394 */     return inputSource;
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
/*      */   public Source resolve(String href, String base) throws TransformerException {
/*  409 */     if (isReference()) {
/*  410 */       return getRef().resolve(href, base);
/*      */     }
/*      */     
/*  413 */     dieOnCircularReference();
/*      */     
/*  415 */     SAXSource source = null;
/*      */     
/*  417 */     String uri = removeFragment(href);
/*      */     
/*  419 */     log("resolve: '" + uri + "' with base: '" + base + "'", 4);
/*      */     
/*  421 */     source = (SAXSource)getCatalogResolver().resolve(uri, base);
/*      */     
/*  423 */     if (source == null) {
/*  424 */       log("No matching catalog entry found, parser will use: '" + href + "'", 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  430 */       source = new SAXSource();
/*      */       try {
/*      */         URL baseURL;
/*  433 */         if (base == null) {
/*  434 */           baseURL = FILE_UTILS.getFileURL(getProject().getBaseDir());
/*      */         } else {
/*  436 */           baseURL = new URL(base);
/*      */         } 
/*  438 */         URL url = uri.isEmpty() ? baseURL : new URL(baseURL, uri);
/*  439 */         source.setInputSource(new InputSource(url.toString()));
/*  440 */       } catch (MalformedURLException ex) {
/*      */ 
/*      */         
/*  443 */         source.setInputSource(new InputSource(uri));
/*      */       } 
/*      */     } 
/*      */     
/*  447 */     setEntityResolver(source);
/*  448 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
/*  454 */     if (isChecked()) {
/*      */       return;
/*      */     }
/*  457 */     if (isReference()) {
/*  458 */       super.dieOnCircularReference(stk, p);
/*      */     } else {
/*  460 */       if (this.classpath != null) {
/*  461 */         pushAndInvokeCircularReferenceCheck(this.classpath, stk, p);
/*      */       }
/*  463 */       if (this.catalogPath != null) {
/*  464 */         pushAndInvokeCircularReferenceCheck(this.catalogPath, stk, p);
/*      */       }
/*  466 */       setChecked(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private XMLCatalog getRef() {
/*  474 */     if (!isReference()) {
/*  475 */       return this;
/*      */     }
/*  477 */     return getCheckedRef(XMLCatalog.class);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public XMLCatalog() {
/*  483 */     this.catalogResolver = null;
/*      */     setChecked(false);
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
/*      */   private CatalogResolver getCatalogResolver() {
/*  497 */     if (this.catalogResolver == null) {
/*      */ 
/*      */ 
/*      */       
/*  501 */       AntClassLoader loader = getProject().createClassLoader(Path.systemClasspath);
/*      */       
/*      */       try {
/*  504 */         Class<?> clazz = Class.forName("org.apache.tools.ant.types.resolver.ApacheCatalogResolver", true, (ClassLoader)loader);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  509 */         ClassLoader apacheResolverLoader = clazz.getClassLoader();
/*      */ 
/*      */ 
/*      */         
/*  513 */         Class<?> baseResolverClass = Class.forName("org.apache.xml.resolver.tools.CatalogResolver", true, apacheResolverLoader);
/*      */ 
/*      */ 
/*      */         
/*  517 */         ClassLoader baseResolverLoader = baseResolverClass.getClassLoader();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  523 */         clazz = Class.forName("org.apache.tools.ant.types.resolver.ApacheCatalogResolver", true, baseResolverLoader);
/*      */         
/*  525 */         Object obj = clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  530 */         this.catalogResolver = new ExternalResolver(clazz, obj);
/*  531 */       } catch (Throwable ex) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  536 */         this.catalogResolver = new InternalResolver();
/*  537 */         if (getCatalogPath() != null && (
/*  538 */           getCatalogPath().list()).length != 0) {
/*  539 */           log("Warning: XML resolver not found; external catalogs will be ignored", 1);
/*      */         }
/*      */         
/*  542 */         log("Failed to load Apache resolver: " + ex, 4);
/*      */       } 
/*      */     } 
/*  545 */     return this.catalogResolver;
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
/*      */   private void setEntityResolver(SAXSource source) throws TransformerException {
/*  569 */     XMLReader reader = source.getXMLReader();
/*  570 */     if (reader == null) {
/*  571 */       SAXParserFactory spFactory = SAXParserFactory.newInstance();
/*  572 */       spFactory.setNamespaceAware(true);
/*      */       try {
/*  574 */         reader = spFactory.newSAXParser().getXMLReader();
/*  575 */       } catch (ParserConfigurationException|SAXException ex) {
/*  576 */         throw new TransformerException(ex);
/*      */       } 
/*      */     } 
/*  579 */     reader.setEntityResolver(this);
/*  580 */     source.setXMLReader(reader);
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
/*      */   private ResourceLocation findMatchingEntry(String publicId) {
/*  592 */     return getElements().stream()
/*  593 */       .filter(e -> e.getPublicId().equals(publicId)).findFirst()
/*  594 */       .orElse(null);
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
/*      */   private String removeFragment(String uri) {
/*  608 */     String result = uri;
/*  609 */     int hashPos = uri.indexOf("#");
/*  610 */     if (hashPos >= 0) {
/*  611 */       result = uri.substring(0, hashPos);
/*      */     }
/*  613 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private InputSource filesystemLookup(ResourceLocation matchingEntry) {
/*      */     URL baseURL;
/*  624 */     String uri = matchingEntry.getLocation();
/*      */     
/*  626 */     uri = uri.replace(File.separatorChar, '/');
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  634 */     if (matchingEntry.getBase() != null) {
/*  635 */       baseURL = matchingEntry.getBase();
/*      */     } else {
/*      */       try {
/*  638 */         baseURL = FILE_UTILS.getFileURL(getProject().getBaseDir());
/*  639 */       } catch (MalformedURLException ex) {
/*  640 */         throw new BuildException("Project basedir cannot be converted to a URL");
/*      */       } 
/*      */     } 
/*      */     
/*  644 */     URL url = null;
/*      */     try {
/*  646 */       url = new URL(baseURL, uri);
/*  647 */     } catch (MalformedURLException ex) {
/*      */ 
/*      */ 
/*      */       
/*  651 */       File testFile = new File(uri);
/*  652 */       if (testFile.exists() && testFile.canRead()) {
/*  653 */         log("uri : '" + uri + "' matches a readable file", 4);
/*      */         
/*      */         try {
/*  656 */           url = FILE_UTILS.getFileURL(testFile);
/*  657 */         } catch (MalformedURLException ex1) {
/*  658 */           throw new BuildException("could not find an URL for :" + testFile
/*  659 */               .getAbsolutePath());
/*      */         } 
/*      */       } else {
/*  662 */         log("uri : '" + uri + "' does not match a readable file", 4);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  668 */     InputSource source = null;
/*  669 */     if (url != null && "file".equals(url.getProtocol())) {
/*  670 */       String fileName = FILE_UTILS.fromURI(url.toString());
/*  671 */       if (fileName != null) {
/*  672 */         log("fileName " + fileName, 4);
/*  673 */         File resFile = new File(fileName);
/*  674 */         if (resFile.exists() && resFile.canRead()) {
/*      */           try {
/*  676 */             source = new InputSource(Files.newInputStream(resFile.toPath(), new java.nio.file.OpenOption[0]));
/*  677 */             String sysid = JAXPUtils.getSystemId(resFile);
/*  678 */             source.setSystemId(sysid);
/*  679 */             log("catalog entry matched a readable file: '" + sysid + "'", 4);
/*      */           }
/*  681 */           catch (IOException iOException) {}
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  687 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private InputSource classpathLookup(ResourceLocation matchingEntry) {
/*  698 */     InputSource source = null;
/*      */     
/*  700 */     Path cp = this.classpath;
/*  701 */     if (cp != null) {
/*  702 */       cp = this.classpath.concatSystemClasspath("ignore");
/*      */     } else {
/*  704 */       cp = (new Path(getProject())).concatSystemClasspath("last");
/*      */     } 
/*  706 */     AntClassLoader loader = getProject().createClassLoader(cp);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  712 */     InputStream is = loader.getResourceAsStream(matchingEntry.getLocation());
/*      */     
/*  714 */     if (is != null) {
/*  715 */       source = new InputSource(is);
/*  716 */       URL entryURL = loader.getResource(matchingEntry.getLocation());
/*  717 */       String sysid = entryURL.toExternalForm();
/*  718 */       source.setSystemId(sysid);
/*  719 */       log("catalog entry matched a resource in the classpath: '" + sysid + "'", 4);
/*      */     } 
/*      */ 
/*      */     
/*  723 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private InputSource urlLookup(ResourceLocation matchingEntry) {
/*      */     URL baseURL, url;
/*  734 */     String uri = matchingEntry.getLocation();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  742 */     if (matchingEntry.getBase() != null) {
/*  743 */       baseURL = matchingEntry.getBase();
/*      */     } else {
/*      */       try {
/*  746 */         baseURL = FILE_UTILS.getFileURL(getProject().getBaseDir());
/*  747 */       } catch (MalformedURLException ex) {
/*  748 */         throw new BuildException("Project basedir cannot be converted to a URL");
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  755 */       url = new URL(baseURL, uri);
/*  756 */     } catch (MalformedURLException ex) {
/*  757 */       url = null;
/*      */     } 
/*      */     
/*  760 */     InputSource source = null;
/*  761 */     if (url != null) {
/*      */       try {
/*  763 */         InputStream is = null;
/*  764 */         URLConnection conn = url.openConnection();
/*  765 */         if (conn != null) {
/*  766 */           conn.setUseCaches(false);
/*  767 */           is = conn.getInputStream();
/*      */         } 
/*  769 */         if (is != null) {
/*  770 */           source = new InputSource(is);
/*  771 */           String sysid = url.toExternalForm();
/*  772 */           source.setSystemId(sysid);
/*  773 */           log("catalog entry matched as a URL: '" + sysid + "'", 4);
/*      */         }
/*      */       
/*  776 */       } catch (IOException iOException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  781 */     return source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface CatalogResolver
/*      */     extends URIResolver, EntityResolver
/*      */   {
/*      */     InputSource resolveEntity(String param1String1, String param1String2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class InternalResolver
/*      */     implements CatalogResolver
/*      */   {
/*      */     public InternalResolver() {
/*  804 */       XMLCatalog.this.log("Apache resolver library not found, internal resolver will be used", 3);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public InputSource resolveEntity(String publicId, String systemId) {
/*  811 */       InputSource result = null;
/*  812 */       ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(publicId);
/*      */       
/*  814 */       if (matchingEntry != null) {
/*      */         
/*  816 */         XMLCatalog.this.log("Matching catalog entry found for publicId: '" + matchingEntry
/*  817 */             .getPublicId() + "' location: '" + matchingEntry
/*  818 */             .getLocation() + "'", 4);
/*      */ 
/*      */         
/*  821 */         result = XMLCatalog.this.filesystemLookup(matchingEntry);
/*      */         
/*  823 */         if (result == null) {
/*  824 */           result = XMLCatalog.this.classpathLookup(matchingEntry);
/*      */         }
/*      */         
/*  827 */         if (result == null) {
/*  828 */           result = XMLCatalog.this.urlLookup(matchingEntry);
/*      */         }
/*      */       } 
/*  831 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Source resolve(String href, String base) throws TransformerException {
/*  838 */       SAXSource result = null;
/*  839 */       InputSource source = null;
/*      */       
/*  841 */       ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(href);
/*      */       
/*  843 */       if (matchingEntry != null) {
/*      */         
/*  845 */         XMLCatalog.this.log("Matching catalog entry found for uri: '" + matchingEntry
/*  846 */             .getPublicId() + "' location: '" + matchingEntry
/*  847 */             .getLocation() + "'", 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  860 */         ResourceLocation entryCopy = matchingEntry;
/*  861 */         if (base != null) {
/*      */           try {
/*  863 */             URL baseURL = new URL(base);
/*  864 */             entryCopy = new ResourceLocation();
/*  865 */             entryCopy.setBase(baseURL);
/*  866 */           } catch (MalformedURLException malformedURLException) {}
/*      */         }
/*      */ 
/*      */         
/*  870 */         entryCopy.setPublicId(matchingEntry.getPublicId());
/*  871 */         entryCopy.setLocation(matchingEntry.getLocation());
/*      */         
/*  873 */         source = XMLCatalog.this.filesystemLookup(entryCopy);
/*      */         
/*  875 */         if (source == null) {
/*  876 */           source = XMLCatalog.this.classpathLookup(entryCopy);
/*      */         }
/*      */         
/*  879 */         if (source == null) {
/*  880 */           source = XMLCatalog.this.urlLookup(entryCopy);
/*      */         }
/*      */         
/*  883 */         if (source != null) {
/*  884 */           result = new SAXSource(source);
/*      */         }
/*      */       } 
/*  887 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class ExternalResolver
/*      */     implements CatalogResolver
/*      */   {
/*  900 */     private Method setXMLCatalog = null;
/*  901 */     private Method parseCatalog = null;
/*  902 */     private Method resolveEntity = null;
/*  903 */     private Method resolve = null;
/*      */ 
/*      */     
/*  906 */     private Object resolverImpl = null;
/*      */ 
/*      */     
/*      */     private boolean externalCatalogsProcessed = false;
/*      */ 
/*      */     
/*      */     public ExternalResolver(Class<?> resolverImplClass, Object resolverImpl) {
/*  913 */       this.resolverImpl = resolverImpl;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/*  923 */         this.setXMLCatalog = resolverImplClass.getMethod("setXMLCatalog", new Class[] { XMLCatalog.class });
/*      */ 
/*      */         
/*  926 */         this.parseCatalog = resolverImplClass.getMethod("parseCatalog", new Class[] { String.class });
/*      */ 
/*      */         
/*  929 */         this.resolveEntity = resolverImplClass.getMethod("resolveEntity", new Class[] { String.class, String.class });
/*      */ 
/*      */         
/*  932 */         this.resolve = resolverImplClass.getMethod("resolve", new Class[] { String.class, String.class });
/*      */       }
/*  934 */       catch (NoSuchMethodException ex) {
/*  935 */         throw new BuildException(ex);
/*      */       } 
/*      */       
/*  938 */       XMLCatalog.this.log("Apache resolver library found, xml-commons resolver will be used", 3);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public InputSource resolveEntity(String publicId, String systemId) {
/*      */       InputSource result;
/*  946 */       processExternalCatalogs();
/*      */       
/*  948 */       ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(publicId);
/*      */ 
/*      */       
/*  951 */       if (matchingEntry != null) {
/*      */         
/*  953 */         XMLCatalog.this.log("Matching catalog entry found for publicId: '" + matchingEntry
/*  954 */             .getPublicId() + "' location: '" + matchingEntry
/*  955 */             .getLocation() + "'", 4);
/*      */ 
/*      */         
/*  958 */         result = XMLCatalog.this.filesystemLookup(matchingEntry);
/*      */         
/*  960 */         if (result == null) {
/*  961 */           result = XMLCatalog.this.classpathLookup(matchingEntry);
/*      */         }
/*      */         
/*  964 */         if (result == null) {
/*      */           
/*      */           try {
/*  967 */             result = (InputSource)this.resolveEntity.invoke(this.resolverImpl, new Object[] { publicId, systemId });
/*      */           }
/*  969 */           catch (Exception ex) {
/*  970 */             throw new BuildException(ex);
/*      */           } 
/*      */         }
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  983 */           result = (InputSource)this.resolveEntity.invoke(this.resolverImpl, new Object[] { publicId, systemId });
/*      */         }
/*  985 */         catch (Exception ex) {
/*  986 */           throw new BuildException(ex);
/*      */         } 
/*      */       } 
/*      */       
/*  990 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Source resolve(String href, String base) throws TransformerException {
/*      */       SAXSource result;
/* 1000 */       processExternalCatalogs();
/*      */       
/* 1002 */       ResourceLocation matchingEntry = XMLCatalog.this.findMatchingEntry(href);
/*      */       
/* 1004 */       if (matchingEntry != null) {
/*      */         
/* 1006 */         XMLCatalog.this.log("Matching catalog entry found for uri: '" + matchingEntry
/* 1007 */             .getPublicId() + "' location: '" + matchingEntry
/* 1008 */             .getLocation() + "'", 4);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1023 */         ResourceLocation entryCopy = matchingEntry;
/* 1024 */         if (base != null) {
/*      */           try {
/* 1026 */             URL baseURL = new URL(base);
/* 1027 */             entryCopy = new ResourceLocation();
/* 1028 */             entryCopy.setBase(baseURL);
/* 1029 */           } catch (MalformedURLException malformedURLException) {}
/*      */         }
/*      */ 
/*      */         
/* 1033 */         entryCopy.setPublicId(matchingEntry.getPublicId());
/* 1034 */         entryCopy.setLocation(matchingEntry.getLocation());
/*      */         
/* 1036 */         InputSource source = XMLCatalog.this.filesystemLookup(entryCopy);
/*      */         
/* 1038 */         if (source == null) {
/* 1039 */           source = XMLCatalog.this.classpathLookup(entryCopy);
/*      */         }
/*      */         
/* 1042 */         if (source != null) {
/* 1043 */           result = new SAXSource(source);
/*      */         } else {
/*      */           try {
/* 1046 */             result = (SAXSource)this.resolve.invoke(this.resolverImpl, new Object[] { href, base });
/*      */           }
/* 1048 */           catch (Exception ex) {
/* 1049 */             throw new BuildException(ex);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1061 */         if (base == null) {
/*      */           try {
/* 1063 */             base = XMLCatalog.FILE_UTILS.getFileURL(XMLCatalog.this.getProject().getBaseDir()).toString();
/* 1064 */           } catch (MalformedURLException x) {
/* 1065 */             throw new TransformerException(x);
/*      */           } 
/*      */         }
/*      */         try {
/* 1069 */           result = (SAXSource)this.resolve.invoke(this.resolverImpl, new Object[] { href, base });
/*      */         }
/* 1071 */         catch (Exception ex) {
/* 1072 */           throw new BuildException(ex);
/*      */         } 
/*      */       } 
/* 1075 */       return result;
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
/*      */     private void processExternalCatalogs() {
/* 1087 */       if (!this.externalCatalogsProcessed) {
/*      */         
/*      */         try {
/* 1090 */           this.setXMLCatalog.invoke(this.resolverImpl, new Object[] { this.this$0 });
/* 1091 */         } catch (Exception ex) {
/* 1092 */           throw new BuildException(ex);
/*      */         } 
/*      */ 
/*      */         
/* 1096 */         Path catPath = XMLCatalog.this.getCatalogPath();
/* 1097 */         if (catPath != null) {
/* 1098 */           XMLCatalog.this.log("Using catalogpath '" + XMLCatalog.this.getCatalogPath() + "'", 4);
/*      */ 
/*      */           
/* 1101 */           for (String catFileName : XMLCatalog.this.getCatalogPath().list()) {
/* 1102 */             File catFile = new File(catFileName);
/* 1103 */             XMLCatalog.this.log("Parsing " + catFile, 4);
/*      */             try {
/* 1105 */               this.parseCatalog.invoke(this.resolverImpl, new Object[] { catFile.getPath() });
/* 1106 */             } catch (Exception ex) {
/* 1107 */               throw new BuildException(ex);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1112 */       this.externalCatalogsProcessed = true;
/*      */     }
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/XMLCatalog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */