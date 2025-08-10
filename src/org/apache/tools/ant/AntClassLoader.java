/*      */ package org.apache.tools.ant;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.nio.file.Files;
/*      */ import java.security.CodeSource;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.security.cert.Certificate;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import java.util.jar.Attributes;
/*      */ import java.util.jar.JarEntry;
/*      */ import java.util.jar.JarFile;
/*      */ import java.util.jar.Manifest;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import org.apache.tools.ant.launch.Locator;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.JavaEnvUtils;
/*      */ import org.apache.tools.ant.util.LoaderUtils;
/*      */ import org.apache.tools.ant.util.ReflectUtil;
/*      */ import org.apache.tools.ant.util.StringUtils;
/*      */ import org.apache.tools.ant.util.VectorSet;
/*      */ import org.apache.tools.zip.ZipLong;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class AntClassLoader
/*      */   extends ClassLoader
/*      */   implements SubBuildListener, Closeable
/*      */ {
/*   79 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */   
/*   81 */   private static final boolean IS_ATLEAST_JAVA9 = JavaEnvUtils.isAtLeastJavaVersion("9"); private static final Class[] MR_JARFILE_CTOR_ARGS;
/*      */   private static final Object MR_JARFILE_CTOR_RUNTIME_VERSION_VAL;
/*      */   private static final int BUFFER_SIZE = 8192;
/*      */   private static final int NUMBER_OF_STRINGS = 256;
/*      */   
/*      */   static {
/*   87 */     registerAsParallelCapable();
/*   88 */     if (IS_ATLEAST_JAVA9) {
/*   89 */       Class[] ctorArgs = null;
/*   90 */       Object runtimeVersionVal = null;
/*      */       try {
/*   92 */         Class<?> runtimeVersionClass = Class.forName("java.lang.Runtime$Version");
/*   93 */         ctorArgs = new Class[] { File.class, boolean.class, int.class, runtimeVersionClass };
/*   94 */         runtimeVersionVal = Runtime.class.getDeclaredMethod("version", new Class[0]).invoke(null, new Object[0]);
/*   95 */       } catch (Exception exception) {}
/*      */ 
/*      */       
/*   98 */       MR_JARFILE_CTOR_ARGS = ctorArgs;
/*   99 */       MR_JARFILE_CTOR_RUNTIME_VERSION_VAL = runtimeVersionVal;
/*      */     } else {
/*  101 */       MR_JARFILE_CTOR_ARGS = null;
/*  102 */       MR_JARFILE_CTOR_RUNTIME_VERSION_VAL = null;
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
/*      */   private class ResourceEnumeration
/*      */     implements Enumeration<URL>
/*      */   {
/*      */     private final String resourceName;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int pathElementsIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private URL nextResource;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ResourceEnumeration(String name) {
/*  140 */       this.resourceName = name;
/*  141 */       this.pathElementsIndex = 0;
/*  142 */       findNextResource();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasMoreElements() {
/*  153 */       return (this.nextResource != null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public URL nextElement() {
/*  162 */       URL ret = this.nextResource;
/*  163 */       if (ret == null) {
/*  164 */         throw new NoSuchElementException();
/*      */       }
/*  166 */       findNextResource();
/*  167 */       return ret;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void findNextResource() {
/*  177 */       URL url = null;
/*  178 */       while (this.pathElementsIndex < AntClassLoader.this.pathComponents.size() && url == null) {
/*      */         try {
/*  180 */           File pathComponent = AntClassLoader.this.pathComponents.elementAt(this.pathElementsIndex);
/*  181 */           url = AntClassLoader.this.getResourceURL(pathComponent, this.resourceName);
/*  182 */           this.pathElementsIndex++;
/*  183 */         } catch (BuildException buildException) {}
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  188 */       this.nextResource = url;
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
/*  206 */   private final Vector<File> pathComponents = (Vector<File>)new VectorSet();
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
/*      */   
/*      */   private boolean parentFirst = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  224 */   private final Vector<String> systemPackages = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  231 */   private final Vector<String> loaderPackages = new Vector<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean ignoreBase = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  244 */   private ClassLoader parent = null;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  249 */   private Hashtable<File, JarFile> jarFiles = new Hashtable<>();
/*      */ 
/*      */ 
/*      */   
/*  253 */   private static Map<String, String> pathMap = Collections.synchronizedMap(new HashMap<>());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  259 */   private ClassLoader savedContextLoader = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isContextLoaderSaved = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AntClassLoader(ClassLoader parent, Project project, Path classpath) {
/*  276 */     setParent(parent);
/*  277 */     setClassPath(classpath);
/*  278 */     setProject(project);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AntClassLoader() {
/*  285 */     setParent((ClassLoader)null);
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
/*      */   public AntClassLoader(Project project, Path classpath) {
/*  300 */     setParent((ClassLoader)null);
/*  301 */     setProject(project);
/*  302 */     setClassPath(classpath);
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
/*      */   public AntClassLoader(ClassLoader parent, Project project, Path classpath, boolean parentFirst) {
/*  323 */     this(project, classpath);
/*  324 */     if (parent != null) {
/*  325 */       setParent(parent);
/*      */     }
/*  327 */     setParentFirst(parentFirst);
/*  328 */     addJavaLibraries();
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
/*      */   public AntClassLoader(Project project, Path classpath, boolean parentFirst) {
/*  345 */     this((ClassLoader)null, project, classpath, parentFirst);
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
/*      */   public AntClassLoader(ClassLoader parent, boolean parentFirst) {
/*  362 */     setParent(parent);
/*  363 */     this.project = null;
/*  364 */     this.parentFirst = parentFirst;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setProject(Project project) {
/*  373 */     this.project = project;
/*  374 */     if (project != null) {
/*  375 */       project.addBuildListener(this);
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
/*      */   public void setClassPath(Path classpath) {
/*  387 */     this.pathComponents.removeAllElements();
/*  388 */     if (classpath != null) {
/*  389 */       for (String pathElement : classpath.concatSystemClasspath("ignore").list()) {
/*      */         try {
/*  391 */           addPathElement(pathElement);
/*  392 */         } catch (BuildException e) {
/*      */ 
/*      */           
/*  395 */           log("Ignoring path element " + pathElement + " from classpath due to exception " + e, 4);
/*      */         } 
/*      */       } 
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
/*      */   public void setParent(ClassLoader parent) {
/*  409 */     this.parent = (parent == null) ? AntClassLoader.class.getClassLoader() : parent;
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
/*      */   public void setParentFirst(boolean parentFirst) {
/*  421 */     this.parentFirst = parentFirst;
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
/*      */   protected void log(String message, int priority) {
/*  433 */     if (this.project != null) {
/*  434 */       this.project.log(message, priority);
/*  435 */     } else if (priority < 2) {
/*  436 */       System.err.println(message);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThreadContextLoader() {
/*  445 */     if (this.isContextLoaderSaved) {
/*  446 */       throw new BuildException("Context loader has not been reset");
/*      */     }
/*  448 */     if (LoaderUtils.isContextLoaderAvailable()) {
/*  449 */       this.savedContextLoader = LoaderUtils.getContextClassLoader();
/*  450 */       ClassLoader loader = this;
/*  451 */       if (this.project != null && "only".equals(this.project.getProperty("build.sysclasspath"))) {
/*  452 */         loader = getClass().getClassLoader();
/*      */       }
/*  454 */       LoaderUtils.setContextClassLoader(loader);
/*  455 */       this.isContextLoaderSaved = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void resetThreadContextLoader() {
/*  463 */     if (LoaderUtils.isContextLoaderAvailable() && this.isContextLoaderSaved) {
/*  464 */       LoaderUtils.setContextClassLoader(this.savedContextLoader);
/*  465 */       this.savedContextLoader = null;
/*  466 */       this.isContextLoaderSaved = false;
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
/*      */   public void addPathElement(String pathElement) throws BuildException {
/*  481 */     File pathComponent = (this.project != null) ? this.project.resolveFile(pathElement) : new File(pathElement);
/*      */     
/*      */     try {
/*  484 */       addPathFile(pathComponent);
/*  485 */     } catch (IOException e) {
/*  486 */       throw new BuildException(e);
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
/*      */   public void addPathComponent(File file) {
/*  498 */     if (this.pathComponents.contains(file)) {
/*      */       return;
/*      */     }
/*  501 */     this.pathComponents.addElement(file);
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
/*      */   protected void addPathFile(File pathComponent) throws IOException {
/*  515 */     if (!this.pathComponents.contains(pathComponent)) {
/*  516 */       this.pathComponents.addElement(pathComponent);
/*      */     }
/*  518 */     if (pathComponent.isDirectory()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  523 */     String absPathPlusTimeAndLength = pathComponent.getAbsolutePath() + pathComponent.lastModified() + "-" + pathComponent.length();
/*  524 */     String classpath = pathMap.get(absPathPlusTimeAndLength);
/*  525 */     if (classpath == null) {
/*  526 */       JarFile jarFile = newJarFile(pathComponent); 
/*  527 */       try { Manifest manifest = jarFile.getManifest();
/*  528 */         if (manifest == null)
/*      */         
/*      */         { 
/*      */ 
/*      */           
/*  533 */           if (jarFile != null) jarFile.close();  return; }  classpath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH); if (jarFile != null) jarFile.close();  } catch (Throwable throwable) { if (jarFile != null)
/*  534 */           try { jarFile.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  if (classpath == null) {
/*  535 */         classpath = "";
/*      */       }
/*  537 */       pathMap.put(absPathPlusTimeAndLength, classpath);
/*      */     } 
/*      */     
/*  540 */     if (!classpath.isEmpty()) {
/*  541 */       URL baseURL = FILE_UTILS.getFileURL(pathComponent);
/*  542 */       StringTokenizer st = new StringTokenizer(classpath);
/*  543 */       while (st.hasMoreTokens()) {
/*  544 */         String classpathElement = st.nextToken();
/*  545 */         URL libraryURL = new URL(baseURL, classpathElement);
/*  546 */         if (!libraryURL.getProtocol().equals("file")) {
/*  547 */           log("Skipping jar library " + classpathElement + " since only relative URLs are supported by this loader", 3);
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/*  552 */         String decodedPath = Locator.decodeUri(libraryURL.getFile());
/*  553 */         File libraryFile = new File(decodedPath);
/*  554 */         if (libraryFile.exists() && !isInPath(libraryFile)) {
/*  555 */           addPathFile(libraryFile);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getClasspath() {
/*  568 */     StringBuilder sb = new StringBuilder();
/*  569 */     for (File component : this.pathComponents) {
/*  570 */       if (sb.length() > 0) {
/*  571 */         sb.append(File.pathSeparator);
/*      */       }
/*  573 */       sb.append(component.getAbsolutePath());
/*      */     } 
/*  575 */     return sb.toString();
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
/*      */   public synchronized void setIsolated(boolean isolated) {
/*  588 */     this.ignoreBase = isolated;
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
/*      */   @Deprecated
/*      */   public static void initializeClass(Class<?> theClass) {
/*  608 */     Constructor[] arrayOfConstructor = (Constructor[])theClass.getDeclaredConstructors();
/*      */     
/*  610 */     if (arrayOfConstructor != null && 
/*  611 */       arrayOfConstructor.length > 0 && arrayOfConstructor[0] != null) {
/*  612 */       String[] strs = new String[256];
/*      */       try {
/*  614 */         arrayOfConstructor[0].newInstance((Object[])strs);
/*      */       
/*      */       }
/*  617 */       catch (Exception exception) {}
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
/*      */   public void addSystemPackageRoot(String packageRoot) {
/*  645 */     this.systemPackages.addElement(packageRoot + (packageRoot.endsWith(".") ? "" : "."));
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
/*      */   public void addLoaderPackageRoot(String packageRoot) {
/*  658 */     this.loaderPackages.addElement(packageRoot + (packageRoot.endsWith(".") ? "" : "."));
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
/*      */   public Class<?> forceLoadClass(String classname) throws ClassNotFoundException {
/*  677 */     log("force loading " + classname, 4);
/*      */     
/*  679 */     Class<?> theClass = findLoadedClass(classname);
/*      */     
/*  681 */     if (theClass == null) {
/*  682 */       theClass = findClass(classname);
/*      */     }
/*  684 */     return theClass;
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
/*      */   public Class<?> forceLoadSystemClass(String classname) throws ClassNotFoundException {
/*  704 */     log("force system loading " + classname, 4);
/*      */     
/*  706 */     Class<?> theClass = findLoadedClass(classname);
/*      */     
/*  708 */     if (theClass == null) {
/*  709 */       theClass = findBaseClass(classname);
/*      */     }
/*  711 */     return theClass;
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
/*      */   public InputStream getResourceAsStream(String name) {
/*  725 */     InputStream resourceStream = null;
/*  726 */     if (isParentFirst(name)) {
/*  727 */       resourceStream = loadBaseResource(name);
/*      */     }
/*  729 */     if (resourceStream != null) {
/*  730 */       log("ResourceStream for " + name + " loaded from parent loader", 4);
/*      */     } else {
/*      */       
/*  733 */       resourceStream = loadResource(name);
/*  734 */       if (resourceStream != null) {
/*  735 */         log("ResourceStream for " + name + " loaded from ant loader", 4);
/*      */       }
/*      */     } 
/*      */     
/*  739 */     if (resourceStream == null && !isParentFirst(name)) {
/*  740 */       if (this.ignoreBase) {
/*      */ 
/*      */         
/*  743 */         resourceStream = (getRootLoader() == null) ? null : getRootLoader().getResourceAsStream(name);
/*      */       } else {
/*  745 */         resourceStream = loadBaseResource(name);
/*      */       } 
/*  747 */       if (resourceStream != null) {
/*  748 */         log("ResourceStream for " + name + " loaded from parent loader", 4);
/*      */       }
/*      */     } 
/*      */     
/*  752 */     if (resourceStream == null) {
/*  753 */       log("Couldn't load ResourceStream for " + name, 4);
/*      */     }
/*  755 */     return resourceStream;
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
/*      */   private InputStream loadResource(String name) {
/*  770 */     return this.pathComponents.stream().map(path -> getResourceStream(path, name))
/*  771 */       .filter(Objects::nonNull).findFirst().orElse(null);
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
/*      */   private InputStream loadBaseResource(String name) {
/*  785 */     return (this.parent == null) ? super.getResourceAsStream(name) : this.parent.getResourceAsStream(name);
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
/*      */   private InputStream getResourceStream(File file, String resourceName) {
/*      */     try {
/*  802 */       JarFile jarFile = this.jarFiles.get(file);
/*  803 */       if (jarFile == null && file.isDirectory()) {
/*  804 */         File resource = new File(file, resourceName);
/*  805 */         if (resource.exists()) {
/*  806 */           return Files.newInputStream(resource.toPath(), new java.nio.file.OpenOption[0]);
/*      */         }
/*      */       } else {
/*  809 */         if (jarFile == null) {
/*  810 */           if (file.exists()) {
/*  811 */             jarFile = newJarFile(file);
/*  812 */             this.jarFiles.put(file, jarFile);
/*      */           } else {
/*  814 */             return null;
/*      */           } 
/*      */ 
/*      */           
/*  818 */           jarFile = this.jarFiles.get(file);
/*      */         } 
/*  820 */         JarEntry entry = jarFile.getJarEntry(resourceName);
/*  821 */         if (entry != null) {
/*  822 */           return jarFile.getInputStream(entry);
/*      */         }
/*      */       } 
/*  825 */     } catch (Exception e) {
/*  826 */       log("Ignoring Exception " + e.getClass().getName() + ": " + e.getMessage() + " reading resource " + resourceName + " from " + file, 3);
/*      */     } 
/*      */     
/*  829 */     return null;
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
/*      */   private boolean isParentFirst(String resourceName) {
/*  853 */     Objects.requireNonNull(resourceName);
/*  854 */     Objects.requireNonNull(resourceName); return (this.loaderPackages.stream().noneMatch(resourceName::startsWith) && (this.systemPackages.stream().anyMatch(resourceName::startsWith) || this.parentFirst));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ClassLoader getRootLoader() {
/*  862 */     ClassLoader ret = getClass().getClassLoader();
/*  863 */     while (ret != null && ret.getParent() != null) {
/*  864 */       ret = ret.getParent();
/*      */     }
/*  866 */     return ret;
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
/*      */   public URL getResource(String name) {
/*  885 */     URL url = null;
/*  886 */     if (isParentFirst(name)) {
/*  887 */       url = (this.parent == null) ? super.getResource(name) : this.parent.getResource(name);
/*      */     }
/*  889 */     if (url != null) {
/*  890 */       log("Resource " + name + " loaded from parent loader", 4);
/*      */     } else {
/*  892 */       url = getUrl(name);
/*      */     } 
/*  894 */     if (url == null && !isParentFirst(name)) {
/*      */       
/*  896 */       if (this.ignoreBase) {
/*  897 */         url = (getRootLoader() == null) ? null : getRootLoader().getResource(name);
/*      */       } else {
/*  899 */         url = (this.parent == null) ? super.getResource(name) : this.parent.getResource(name);
/*      */       } 
/*  901 */       if (url != null) {
/*  902 */         log("Resource " + name + " loaded from parent loader", 4);
/*      */       }
/*      */     } 
/*  905 */     if (url == null) {
/*  906 */       log("Couldn't load Resource " + name, 4);
/*      */     }
/*  908 */     return url;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private URL getUrl(String name) {
/*  919 */     URL url = null;
/*      */ 
/*      */ 
/*      */     
/*  923 */     for (File pathComponent : this.pathComponents) {
/*  924 */       url = getResourceURL(pathComponent, name);
/*  925 */       if (url != null) {
/*  926 */         log("Resource " + name + " loaded from ant loader", 4);
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  931 */     return url;
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
/*      */   public Enumeration<URL> getNamedResources(String name) throws IOException {
/*  947 */     return findResources(name, false);
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
/*      */   protected URL findResource(String name) {
/*  959 */     return getUrl(name);
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
/*      */   protected Enumeration<URL> findResources(String name) throws IOException {
/*  973 */     return findResources(name, true);
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
/*      */   protected Enumeration<URL> findResources(String name, boolean skipParent) throws IOException {
/*  991 */     Enumeration<URL> base, mine = new ResourceEnumeration(name);
/*      */     
/*  993 */     if (this.parent != null && !skipParent) {
/*      */       
/*  995 */       base = this.parent.getResources(name);
/*      */     } else {
/*  997 */       base = Collections.emptyEnumeration();
/*      */     } 
/*  999 */     if (isParentFirst(name))
/*      */     {
/* 1001 */       return append(base, mine);
/*      */     }
/* 1003 */     if (this.ignoreBase) {
/* 1004 */       return (getRootLoader() == null) ? mine : 
/* 1005 */         append(mine, getRootLoader().getResources(name));
/*      */     }
/*      */     
/* 1008 */     return append(mine, base);
/*      */   }
/*      */   
/*      */   private static Enumeration<URL> append(Enumeration<URL> one, Enumeration<URL> two) {
/* 1012 */     return (Enumeration<URL>)Stream.concat(Collections.list(one).stream(), Collections.list(two).stream())
/* 1013 */       .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::enumeration));
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
/*      */   protected URL getResourceURL(File file, String resourceName) {
/*      */     try {
/* 1030 */       JarFile jarFile = this.jarFiles.get(file);
/* 1031 */       if (jarFile == null && file.isDirectory()) {
/* 1032 */         File resource = new File(file, resourceName);
/*      */         
/* 1034 */         if (resource.exists()) {
/*      */           try {
/* 1036 */             return FILE_UTILS.getFileURL(resource);
/* 1037 */           } catch (MalformedURLException ex) {
/* 1038 */             return null;
/*      */           } 
/*      */         }
/*      */       } else {
/* 1042 */         if (jarFile == null) {
/* 1043 */           if (file.exists()) {
/* 1044 */             if (!isZip(file)) {
/* 1045 */               String msg = "CLASSPATH element " + file + " is not a JAR.";
/*      */               
/* 1047 */               log(msg, 1);
/* 1048 */               return null;
/*      */             } 
/* 1050 */             jarFile = newJarFile(file);
/* 1051 */             this.jarFiles.put(file, jarFile);
/*      */           } else {
/* 1053 */             return null;
/*      */           } 
/*      */           
/* 1056 */           jarFile = this.jarFiles.get(file);
/*      */         } 
/* 1058 */         JarEntry entry = jarFile.getJarEntry(resourceName);
/* 1059 */         if (entry != null) {
/*      */           try {
/* 1061 */             return new URL("jar:" + FILE_UTILS.getFileURL(file) + "!/" + entry);
/* 1062 */           } catch (MalformedURLException ex) {
/* 1063 */             return null;
/*      */           } 
/*      */         }
/*      */       } 
/* 1067 */     } catch (Exception e) {
/* 1068 */       String msg = "Unable to obtain resource from " + file + ": ";
/* 1069 */       log(msg + e, 1);
/* 1070 */       log(StringUtils.getStackTrace(e), 1);
/*      */     } 
/* 1072 */     return null;
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
/*      */   protected synchronized Class<?> loadClass(String classname, boolean resolve) throws ClassNotFoundException {
/* 1102 */     Class<?> theClass = findLoadedClass(classname);
/* 1103 */     if (theClass != null) {
/* 1104 */       return theClass;
/*      */     }
/* 1106 */     if (isParentFirst(classname)) {
/*      */       try {
/* 1108 */         theClass = findBaseClass(classname);
/* 1109 */         log("Class " + classname + " loaded from parent loader (parentFirst)", 4);
/*      */       }
/* 1111 */       catch (ClassNotFoundException cnfe) {
/* 1112 */         theClass = findClass(classname);
/* 1113 */         log("Class " + classname + " loaded from ant loader (parentFirst)", 4);
/*      */       } 
/*      */     } else {
/*      */       
/*      */       try {
/* 1118 */         theClass = findClass(classname);
/* 1119 */         log("Class " + classname + " loaded from ant loader", 4);
/* 1120 */       } catch (ClassNotFoundException cnfe) {
/* 1121 */         if (this.ignoreBase) {
/* 1122 */           throw cnfe;
/*      */         }
/* 1124 */         theClass = findBaseClass(classname);
/* 1125 */         log("Class " + classname + " loaded from parent loader", 4);
/*      */       } 
/*      */     } 
/* 1128 */     if (resolve) {
/* 1129 */       resolveClass(theClass);
/*      */     }
/* 1131 */     return theClass;
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
/*      */   private String getClassFilename(String classname) {
/* 1144 */     return classname.replace('.', '/') + ".class";
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
/*      */   protected Class<?> defineClassFromData(File container, byte[] classData, String classname) throws IOException {
/* 1162 */     definePackage(container, classname);
/* 1163 */     ProtectionDomain currentPd = Project.class.getProtectionDomain();
/* 1164 */     String classResource = getClassFilename(classname);
/*      */     
/* 1166 */     CodeSource src = new CodeSource(FILE_UTILS.getFileURL(container), getCertificates(container, classResource));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1171 */     ProtectionDomain classesPd = new ProtectionDomain(src, currentPd.getPermissions(), this, currentPd.getPrincipals());
/* 1172 */     return defineClass(classname, classData, 0, classData.length, classesPd);
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
/*      */   protected void definePackage(File container, String className) throws IOException {
/* 1187 */     int classIndex = className.lastIndexOf('.');
/* 1188 */     if (classIndex == -1) {
/*      */       return;
/*      */     }
/* 1191 */     String packageName = className.substring(0, classIndex);
/* 1192 */     if (getPackage(packageName) != null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1197 */     Manifest manifest = getJarManifest(container);
/*      */     
/* 1199 */     if (manifest == null) {
/* 1200 */       definePackage(packageName, null, null, null, null, null, null, null);
/*      */     } else {
/* 1202 */       definePackage(container, packageName, manifest);
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
/*      */   private Manifest getJarManifest(File container) throws IOException {
/* 1218 */     if (container.isDirectory()) {
/* 1219 */       return null;
/*      */     }
/* 1221 */     JarFile jarFile = this.jarFiles.get(container);
/* 1222 */     if (jarFile == null) {
/* 1223 */       return null;
/*      */     }
/* 1225 */     return jarFile.getManifest();
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
/*      */   private Certificate[] getCertificates(File container, String entry) {
/* 1238 */     if (container.isDirectory()) {
/* 1239 */       return null;
/*      */     }
/* 1241 */     JarFile jarFile = this.jarFiles.get(container);
/* 1242 */     if (jarFile == null) {
/* 1243 */       return null;
/*      */     }
/* 1245 */     JarEntry ent = jarFile.getJarEntry(entry);
/* 1246 */     return (ent == null) ? null : ent.getCertificates();
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
/*      */   protected void definePackage(File container, String packageName, Manifest manifest) {
/* 1258 */     String sectionName = packageName.replace('.', '/') + "/";
/*      */     
/* 1260 */     String specificationTitle = null;
/* 1261 */     String specificationVendor = null;
/* 1262 */     String specificationVersion = null;
/* 1263 */     String implementationTitle = null;
/* 1264 */     String implementationVendor = null;
/* 1265 */     String implementationVersion = null;
/* 1266 */     String sealedString = null;
/* 1267 */     URL sealBase = null;
/*      */     
/* 1269 */     Attributes sectionAttributes = manifest.getAttributes(sectionName);
/* 1270 */     if (sectionAttributes != null) {
/* 1271 */       specificationTitle = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/* 1272 */       specificationVendor = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/* 1273 */       specificationVersion = sectionAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/* 1274 */       implementationTitle = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/* 1275 */       implementationVendor = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/* 1276 */       implementationVersion = sectionAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/* 1277 */       sealedString = sectionAttributes.getValue(Attributes.Name.SEALED);
/*      */     } 
/* 1279 */     Attributes mainAttributes = manifest.getMainAttributes();
/* 1280 */     if (mainAttributes != null) {
/* 1281 */       if (specificationTitle == null) {
/* 1282 */         specificationTitle = mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/*      */       }
/* 1284 */       if (specificationVendor == null) {
/* 1285 */         specificationVendor = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/*      */       }
/* 1287 */       if (specificationVersion == null) {
/* 1288 */         specificationVersion = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/*      */       }
/* 1290 */       if (implementationTitle == null) {
/* 1291 */         implementationTitle = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/*      */       }
/* 1293 */       if (implementationVendor == null) {
/* 1294 */         implementationVendor = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/*      */       }
/* 1296 */       if (implementationVersion == null) {
/* 1297 */         implementationVersion = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/*      */       }
/* 1299 */       if (sealedString == null) {
/* 1300 */         sealedString = mainAttributes.getValue(Attributes.Name.SEALED);
/*      */       }
/*      */     } 
/* 1303 */     if (sealedString != null && sealedString.equalsIgnoreCase("true")) {
/*      */       try {
/* 1305 */         sealBase = new URL(FileUtils.getFileUtils().toURI(container.getAbsolutePath()));
/* 1306 */       } catch (MalformedURLException malformedURLException) {}
/*      */     }
/*      */ 
/*      */     
/* 1310 */     definePackage(packageName, specificationTitle, specificationVersion, specificationVendor, implementationTitle, implementationVersion, implementationVendor, sealBase);
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
/*      */   private Class<?> getClassFromStream(InputStream stream, String classname, File container) throws IOException, SecurityException {
/* 1332 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 1333 */     int bytesRead = -1;
/* 1334 */     byte[] buffer = new byte[8192];
/*      */     
/* 1336 */     while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
/* 1337 */       baos.write(buffer, 0, bytesRead);
/*      */     }
/* 1339 */     byte[] classData = baos.toByteArray();
/* 1340 */     return defineClassFromData(container, classData, classname);
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
/*      */   public Class<?> findClass(String name) throws ClassNotFoundException {
/* 1356 */     log("Finding class " + name, 4);
/* 1357 */     return findClassInComponents(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isInPath(File component) {
/* 1368 */     return this.pathComponents.contains(component);
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
/*      */   private Class<?> findClassInComponents(String name) throws ClassNotFoundException {
/* 1386 */     String classFilename = getClassFilename(name);
/* 1387 */     for (File pathComponent : this.pathComponents) { 
/* 1388 */       try { InputStream stream = getResourceStream(pathComponent, classFilename); 
/* 1389 */         try { if (stream != null)
/* 1390 */           { log("Loaded from " + pathComponent + " " + classFilename, 4);
/*      */             
/* 1392 */             Class<?> clazz = getClassFromStream(stream, name, pathComponent);
/*      */             
/* 1394 */             if (stream != null) stream.close();  return clazz; }  if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (SecurityException se)
/* 1395 */       { throw se; }
/* 1396 */       catch (IOException ioe)
/*      */       
/* 1398 */       { log("Exception reading component " + pathComponent + " (reason: " + ioe
/* 1399 */             .getMessage() + ")", 3); }
/*      */        }
/*      */     
/* 1402 */     throw new ClassNotFoundException(name);
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
/*      */   private Class<?> findBaseClass(String name) throws ClassNotFoundException {
/* 1421 */     return (this.parent == null) ? findSystemClass(name) : this.parent.loadClass(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void cleanup() {
/* 1429 */     for (JarFile jarFile : this.jarFiles.values()) {
/* 1430 */       FileUtils.close(jarFile);
/*      */     }
/* 1432 */     this.jarFiles = new Hashtable<>();
/* 1433 */     if (this.project != null) {
/* 1434 */       this.project.removeBuildListener(this);
/*      */     }
/* 1436 */     this.project = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ClassLoader getConfiguredParent() {
/* 1447 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void buildStarted(BuildEvent event) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void buildFinished(BuildEvent event) {
/* 1466 */     cleanup();
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
/*      */   public void subBuildFinished(BuildEvent event) {
/* 1479 */     if (event.getProject() == this.project) {
/* 1480 */       cleanup();
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
/*      */   public void subBuildStarted(BuildEvent event) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void targetStarted(BuildEvent event) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void targetFinished(BuildEvent event) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void taskStarted(BuildEvent event) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void taskFinished(BuildEvent event) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void messageLogged(BuildEvent event) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addJavaLibraries() {
/* 1545 */     JavaEnvUtils.getJrePackages().forEach(this::addSystemPackageRoot);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1554 */     return "AntClassLoader[" + getClasspath() + "]";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Enumeration<URL> getResources(String name) throws IOException {
/* 1560 */     return getNamedResources(name);
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() {
/* 1565 */     cleanup();
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
/*      */   public static AntClassLoader newAntClassLoader(ClassLoader parent, Project project, Path path, boolean parentFirst) {
/* 1581 */     return new AntClassLoader(parent, project, path, parentFirst);
/*      */   }
/*      */   
/* 1584 */   private static final ZipLong EOCD_SIG = new ZipLong(101010256L);
/* 1585 */   private static final ZipLong SINGLE_SEGMENT_SPLIT_MARKER = new ZipLong(808471376L);
/*      */ 
/*      */   
/*      */   private static boolean isZip(File file) throws IOException {
/* 1589 */     byte[] sig = new byte[4];
/* 1590 */     if (readFully(file, sig)) {
/* 1591 */       ZipLong start = new ZipLong(sig);
/* 1592 */       return (ZipLong.LFH_SIG.equals(start) || EOCD_SIG
/* 1593 */         .equals(start) || ZipLong.DD_SIG
/* 1594 */         .equals(start) || SINGLE_SEGMENT_SPLIT_MARKER
/* 1595 */         .equals(start));
/*      */     } 
/* 1597 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean readFully(File f, byte[] b) throws IOException {
/* 1601 */     InputStream fis = Files.newInputStream(f.toPath(), new java.nio.file.OpenOption[0]); try {
/* 1602 */       int len = b.length;
/* 1603 */       int count = 0, x = 0;
/* 1604 */       while (count != len) {
/* 1605 */         x = fis.read(b, count, len - count);
/* 1606 */         if (x == -1) {
/*      */           break;
/*      */         }
/* 1609 */         count += x;
/*      */       } 
/* 1611 */       boolean bool = (count == len) ? true : false;
/* 1612 */       if (fis != null) fis.close(); 
/*      */       return bool;
/*      */     } catch (Throwable throwable) {
/*      */       if (fis != null)
/*      */         try {
/*      */           fis.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   } private static JarFile newJarFile(File file) throws IOException {
/* 1624 */     if (!IS_ATLEAST_JAVA9 || MR_JARFILE_CTOR_ARGS == null || MR_JARFILE_CTOR_RUNTIME_VERSION_VAL == null) {
/* 1625 */       return new JarFile(file);
/*      */     }
/* 1627 */     return (JarFile)ReflectUtil.newInstance(JarFile.class, MR_JARFILE_CTOR_ARGS, new Object[] { file, 
/* 1628 */           Boolean.valueOf(true), Integer.valueOf(1), MR_JARFILE_CTOR_RUNTIME_VERSION_VAL });
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/AntClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */