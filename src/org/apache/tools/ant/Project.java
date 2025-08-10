/*      */ package org.apache.tools.ant;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.Stack;
/*      */ import java.util.Vector;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.tools.ant.helper.DefaultExecutor;
/*      */ import org.apache.tools.ant.input.DefaultInputHandler;
/*      */ import org.apache.tools.ant.input.InputHandler;
/*      */ import org.apache.tools.ant.launch.Locator;
/*      */ import org.apache.tools.ant.types.Description;
/*      */ import org.apache.tools.ant.types.FilterSet;
/*      */ import org.apache.tools.ant.types.FilterSetCollection;
/*      */ import org.apache.tools.ant.types.Path;
/*      */ import org.apache.tools.ant.types.Resource;
/*      */ import org.apache.tools.ant.types.ResourceFactory;
/*      */ import org.apache.tools.ant.types.resources.FileResource;
/*      */ import org.apache.tools.ant.util.FileUtils;
/*      */ import org.apache.tools.ant.util.JavaEnvUtils;
/*      */ import org.apache.tools.ant.util.VectorSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Project
/*      */   implements ResourceFactory
/*      */ {
/*      */   public static final int MSG_ERR = 0;
/*      */   public static final int MSG_WARN = 1;
/*      */   public static final int MSG_INFO = 2;
/*      */   public static final int MSG_VERBOSE = 3;
/*      */   public static final int MSG_DEBUG = 4;
/*      */   private static final String VISITING = "VISITING";
/*      */   private static final String VISITED = "VISITED";
/*      */   @Deprecated
/*      */   public static final String JAVA_1_0 = "1.0";
/*      */   @Deprecated
/*      */   public static final String JAVA_1_1 = "1.1";
/*      */   @Deprecated
/*      */   public static final String JAVA_1_2 = "1.2";
/*      */   @Deprecated
/*      */   public static final String JAVA_1_3 = "1.3";
/*      */   @Deprecated
/*      */   public static final String JAVA_1_4 = "1.4";
/*      */   public static final String TOKEN_START = "@";
/*      */   public static final String TOKEN_END = "@";
/*  135 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*      */ 
/*      */   
/*      */   private String name;
/*      */ 
/*      */   
/*      */   private String description;
/*      */ 
/*      */   
/*  144 */   private final Object referencesLock = new Object();
/*      */ 
/*      */   
/*  147 */   private final Hashtable<String, Object> references = new AntRefTable();
/*      */ 
/*      */   
/*  150 */   private final HashMap<String, Object> idReferences = new HashMap<>();
/*      */ 
/*      */   
/*      */   private String defaultTarget;
/*      */ 
/*      */   
/*  156 */   private final Hashtable<String, Target> targets = new Hashtable<>(); private final FilterSetCollection globalFilters; private File baseDir; private final Object listenersLock; private volatile BuildListener[] listeners;
/*      */   private final ThreadLocal<Boolean> isLoggingMessage;
/*      */   private ClassLoader coreLoader;
/*  159 */   private final FilterSet globalFilterSet = new FilterSet(); private final Map<Thread, Task> threadTasks; private final Map<ThreadGroup, Task> threadGroupTasks;
/*      */   
/*      */   public Project() {
/*  162 */     this.globalFilterSet.setProject(this);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  170 */     this.globalFilters = new FilterSetCollection(this.globalFilterSet);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  177 */     this.listenersLock = new Object();
/*      */ 
/*      */     
/*  180 */     this.listeners = new BuildListener[0];
/*      */ 
/*      */ 
/*      */     
/*  184 */     this.isLoggingMessage = ThreadLocal.withInitial(() -> Boolean.FALSE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  190 */     this.coreLoader = null;
/*      */ 
/*      */     
/*  193 */     this
/*  194 */       .threadTasks = Collections.synchronizedMap(new WeakHashMap<>());
/*      */ 
/*      */     
/*  197 */     this
/*  198 */       .threadGroupTasks = Collections.synchronizedMap(new WeakHashMap<>());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  203 */     this.inputHandler = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  208 */     this.defaultInputStream = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  213 */     this.keepGoingMode = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  261 */     this.inputHandler = (InputHandler)new DefaultInputHandler();
/*      */   }
/*      */   private InputHandler inputHandler; private InputStream defaultInputStream; private boolean keepGoingMode;
/*      */   public void setInputHandler(InputHandler handler) {
/*      */     this.inputHandler = handler;
/*      */   }
/*      */   public void setDefaultInputStream(InputStream defaultInputStream) {
/*      */     this.defaultInputStream = defaultInputStream;
/*      */   }
/*      */   
/*      */   public Project createSubProject() {
/*  272 */     Project subProject = null;
/*      */     try {
/*  274 */       subProject = getClass().getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/*  275 */     } catch (Exception e) {
/*  276 */       subProject = new Project();
/*      */     } 
/*  278 */     initSubProject(subProject);
/*  279 */     return subProject;
/*      */   } public InputStream getDefaultInputStream() {
/*      */     return this.defaultInputStream;
/*      */   }
/*      */   public InputHandler getInputHandler() {
/*      */     return this.inputHandler;
/*      */   }
/*      */   public void initSubProject(Project subProject) {
/*  287 */     ComponentHelper.getComponentHelper(subProject)
/*  288 */       .initSubProject(ComponentHelper.getComponentHelper(this));
/*  289 */     subProject.setDefaultInputStream(getDefaultInputStream());
/*  290 */     subProject.setKeepGoingMode(isKeepGoingMode());
/*  291 */     subProject.setExecutor(getExecutor().getSubProjectExecutor());
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
/*      */   public void init() throws BuildException {
/*  303 */     initProperties();
/*      */     
/*  305 */     ComponentHelper.getComponentHelper(this).initDefaultDefinitions();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void initProperties() throws BuildException {
/*  314 */     setJavaVersionProperty();
/*  315 */     setSystemProperties();
/*  316 */     setPropertyInternal("ant.version", Main.getAntVersion());
/*  317 */     setAntLib();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void setAntLib() {
/*  327 */     File antlib = Locator.getClassSource(Project.class);
/*      */     
/*  329 */     if (antlib != null) {
/*  330 */       setPropertyInternal("ant.core.lib", antlib.getAbsolutePath());
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
/*      */   public AntClassLoader createClassLoader(Path path) {
/*  342 */     return 
/*  343 */       AntClassLoader.newAntClassLoader(getClass().getClassLoader(), this, path, true);
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
/*      */   public AntClassLoader createClassLoader(ClassLoader parent, Path path) {
/*  357 */     return AntClassLoader.newAntClassLoader(parent, this, path, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCoreLoader(ClassLoader coreLoader) {
/*  368 */     this.coreLoader = coreLoader;
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
/*      */   public ClassLoader getCoreLoader() {
/*  380 */     return this.coreLoader;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBuildListener(BuildListener listener) {
/*  391 */     synchronized (this.listenersLock) {
/*      */       
/*  393 */       for (BuildListener buildListener : this.listeners) {
/*  394 */         if (buildListener == listener) {
/*      */           return;
/*      */         }
/*      */       } 
/*      */       
/*  399 */       BuildListener[] newListeners = new BuildListener[this.listeners.length + 1];
/*      */       
/*  401 */       System.arraycopy(this.listeners, 0, newListeners, 0, this.listeners.length);
/*  402 */       newListeners[this.listeners.length] = listener;
/*  403 */       this.listeners = newListeners;
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
/*      */   public void removeBuildListener(BuildListener listener) {
/*  415 */     synchronized (this.listenersLock) {
/*      */       
/*  417 */       for (int i = 0; i < this.listeners.length; i++) {
/*  418 */         if (this.listeners[i] == listener) {
/*  419 */           BuildListener[] newListeners = new BuildListener[this.listeners.length - 1];
/*      */           
/*  421 */           System.arraycopy(this.listeners, 0, newListeners, 0, i);
/*  422 */           System.arraycopy(this.listeners, i + 1, newListeners, i, this.listeners.length - i - 1);
/*      */           
/*  424 */           this.listeners = newListeners;
/*      */           break;
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
/*      */   public Vector<BuildListener> getBuildListeners() {
/*  437 */     synchronized (this.listenersLock) {
/*  438 */       Vector<BuildListener> r = new Vector<>(this.listeners.length);
/*  439 */       Collections.addAll(r, this.listeners);
/*  440 */       return r;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(String message) {
/*  451 */     log(message, 2);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(String message, int msgLevel) {
/*  460 */     log(message, (Throwable)null, msgLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(String message, Throwable throwable, int msgLevel) {
/*  471 */     fireMessageLogged(this, message, throwable, msgLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Task task, String message, int msgLevel) {
/*  481 */     fireMessageLogged(task, message, (Throwable)null, msgLevel);
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
/*      */   public void log(Task task, String message, Throwable throwable, int msgLevel) {
/*  493 */     fireMessageLogged(task, message, throwable, msgLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void log(Target target, String message, int msgLevel) {
/*  504 */     log(target, message, (Throwable)null, msgLevel);
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
/*      */   public void log(Target target, String message, Throwable throwable, int msgLevel) {
/*  518 */     fireMessageLogged(target, message, throwable, msgLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FilterSet getGlobalFilterSet() {
/*  527 */     return this.globalFilterSet;
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
/*      */   public void setProperty(String name, String value) {
/*  539 */     PropertyHelper.getPropertyHelper(this).setProperty(name, value, true);
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
/*      */   public void setNewProperty(String name, String value) {
/*  554 */     PropertyHelper.getPropertyHelper(this).setNewProperty(name, value);
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
/*      */   public void setUserProperty(String name, String value) {
/*  567 */     PropertyHelper.getPropertyHelper(this).setUserProperty(name, value);
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
/*      */   public void setInheritedProperty(String name, String value) {
/*  583 */     PropertyHelper.getPropertyHelper(this).setInheritedProperty(name, value);
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
/*      */   private void setPropertyInternal(String name, String value) {
/*  595 */     PropertyHelper.getPropertyHelper(this).setProperty(name, value, false);
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
/*      */   public String getProperty(String propertyName) {
/*  608 */     Object value = PropertyHelper.getPropertyHelper(this).getProperty(propertyName);
/*  609 */     return (value == null) ? null : String.valueOf(value);
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
/*      */   public String replaceProperties(String value) throws BuildException {
/*  627 */     return PropertyHelper.getPropertyHelper(this).replaceProperties(null, value, null);
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
/*      */   public String getUserProperty(String propertyName) {
/*  640 */     return (String)PropertyHelper.getPropertyHelper(this).getUserProperty(propertyName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable<String, Object> getProperties() {
/*  650 */     return PropertyHelper.getPropertyHelper(this).getProperties();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> getPropertyNames() {
/*  659 */     return PropertyHelper.getPropertyHelper(this).getPropertyNames();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable<String, Object> getUserProperties() {
/*  667 */     return PropertyHelper.getPropertyHelper(this).getUserProperties();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable<String, Object> getInheritedProperties() {
/*  676 */     return PropertyHelper.getPropertyHelper(this).getInheritedProperties();
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
/*      */   public void copyUserProperties(Project other) {
/*  692 */     PropertyHelper.getPropertyHelper(this).copyUserProperties(other);
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
/*      */   public void copyInheritedProperties(Project other) {
/*  708 */     PropertyHelper.getPropertyHelper(this).copyInheritedProperties(other);
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
/*      */   @Deprecated
/*      */   public void setDefaultTarget(String defaultTarget) {
/*  724 */     setDefault(defaultTarget);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDefaultTarget() {
/*  733 */     return this.defaultTarget;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDefault(String defaultTarget) {
/*  744 */     if (defaultTarget != null) {
/*  745 */       setUserProperty("ant.project.default-target", defaultTarget);
/*      */     }
/*  747 */     this.defaultTarget = defaultTarget;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setName(String name) {
/*  758 */     setUserProperty("ant.project.name", name);
/*  759 */     this.name = name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  768 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDescription(String description) {
/*  778 */     this.description = description;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getDescription() {
/*  788 */     if (this.description == null) {
/*  789 */       this.description = Description.getDescription(this);
/*      */     }
/*  791 */     return this.description;
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
/*      */   @Deprecated
/*      */   public void addFilter(String token, String value) {
/*  809 */     if (token == null) {
/*      */       return;
/*      */     }
/*  812 */     this.globalFilterSet.addFilter(new FilterSet.Filter(token, value));
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
/*      */   @Deprecated
/*      */   public Hashtable<String, String> getFilters() {
/*  830 */     return this.globalFilterSet.getFilterHash();
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
/*      */   public void setBasedir(String baseD) throws BuildException {
/*  843 */     setBaseDir(new File(baseD));
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
/*      */   public void setBaseDir(File baseDir) throws BuildException {
/*  856 */     baseDir = FILE_UTILS.normalize(baseDir.getAbsolutePath());
/*  857 */     if (!baseDir.exists()) {
/*  858 */       throw new BuildException("Basedir " + baseDir.getAbsolutePath() + " does not exist");
/*      */     }
/*      */     
/*  861 */     if (!baseDir.isDirectory()) {
/*  862 */       throw new BuildException("Basedir " + baseDir.getAbsolutePath() + " is not a directory");
/*      */     }
/*      */     
/*  865 */     this.baseDir = baseDir;
/*  866 */     setPropertyInternal("basedir", this.baseDir.getPath());
/*  867 */     String msg = "Project base dir set to: " + this.baseDir;
/*  868 */     log(msg, 3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File getBaseDir() {
/*  878 */     if (this.baseDir == null) {
/*      */       try {
/*  880 */         setBasedir(".");
/*  881 */       } catch (BuildException ex) {
/*  882 */         ex.printStackTrace();
/*      */       } 
/*      */     }
/*  885 */     return this.baseDir;
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
/*      */   public void setKeepGoingMode(boolean keepGoingMode) {
/*  898 */     this.keepGoingMode = keepGoingMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isKeepGoingMode() {
/*  909 */     return this.keepGoingMode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String getJavaVersion() {
/*  921 */     return JavaEnvUtils.getJavaVersion();
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
/*      */   public void setJavaVersionProperty() throws BuildException {
/*  935 */     String javaVersion = JavaEnvUtils.getJavaVersion();
/*  936 */     setPropertyInternal("ant.java.version", javaVersion);
/*      */ 
/*      */     
/*  939 */     if (!JavaEnvUtils.isAtLeastJavaVersion("1.8")) {
/*  940 */       throw new BuildException("Ant cannot work on Java prior to 1.8");
/*      */     }
/*  942 */     log("Detected Java version: " + javaVersion + " in: " + 
/*  943 */         System.getProperty("java.home"), 3);
/*      */     
/*  945 */     log("Detected OS: " + System.getProperty("os.name"), 3);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSystemProperties() {
/*  953 */     Properties systemP = System.getProperties();
/*  954 */     for (String propertyName : systemP.stringPropertyNames()) {
/*  955 */       String value = systemP.getProperty(propertyName);
/*  956 */       if (value != null) {
/*  957 */         setPropertyInternal(propertyName, value);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTaskDefinition(String taskName, Class<?> taskClass) throws BuildException {
/*  984 */     ComponentHelper.getComponentHelper(this).addTaskDefinition(taskName, taskClass);
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
/*      */   public void checkTaskClass(Class<?> taskClass) throws BuildException {
/* 1001 */     ComponentHelper.getComponentHelper(this).checkTaskClass(taskClass);
/*      */     
/* 1003 */     if (!Modifier.isPublic(taskClass.getModifiers())) {
/* 1004 */       String message = taskClass + " is not public";
/* 1005 */       log(message, 0);
/* 1006 */       throw new BuildException(message);
/*      */     } 
/* 1008 */     if (Modifier.isAbstract(taskClass.getModifiers())) {
/* 1009 */       String message = taskClass + " is abstract";
/* 1010 */       log(message, 0);
/* 1011 */       throw new BuildException(message);
/*      */     } 
/*      */     try {
/* 1014 */       taskClass.getConstructor(new Class[0]);
/*      */     
/*      */     }
/* 1017 */     catch (NoSuchMethodException e) {
/* 1018 */       String message = "No public no-arg constructor in " + taskClass;
/*      */       
/* 1020 */       log(message, 0);
/* 1021 */       throw new BuildException(message);
/* 1022 */     } catch (LinkageError e) {
/* 1023 */       String message = "Could not load " + taskClass + ": " + e;
/* 1024 */       log(message, 0);
/* 1025 */       throw new BuildException(message, e);
/*      */     } 
/* 1027 */     if (!Task.class.isAssignableFrom(taskClass)) {
/* 1028 */       TaskAdapter.checkTaskClass(taskClass, this);
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
/*      */   public Hashtable<String, Class<?>> getTaskDefinitions() {
/* 1040 */     return ComponentHelper.getComponentHelper(this).getTaskDefinitions();
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
/*      */   public Map<String, Class<?>> getCopyOfTaskDefinitions() {
/* 1053 */     return new HashMap<>(getTaskDefinitions());
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
/*      */   public void addDataTypeDefinition(String typeName, Class<?> typeClass) {
/* 1070 */     ComponentHelper.getComponentHelper(this).addDataTypeDefinition(typeName, typeClass);
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
/*      */   public Hashtable<String, Class<?>> getDataTypeDefinitions() {
/* 1082 */     return ComponentHelper.getComponentHelper(this).getDataTypeDefinitions();
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
/*      */   public Map<String, Class<?>> getCopyOfDataTypeDefinitions() {
/* 1095 */     return new HashMap<>(getDataTypeDefinitions());
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
/*      */   public void addTarget(Target target) throws BuildException {
/* 1109 */     addTarget(target.getName(), target);
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
/*      */   public void addTarget(String targetName, Target target) throws BuildException {
/* 1126 */     if (this.targets.get(targetName) != null) {
/* 1127 */       throw new BuildException("Duplicate target: `" + targetName + "'");
/*      */     }
/* 1129 */     addOrReplaceTarget(targetName, target);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addOrReplaceTarget(Target target) {
/* 1140 */     addOrReplaceTarget(target.getName(), target);
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
/*      */   public void addOrReplaceTarget(String targetName, Target target) {
/* 1153 */     String msg = " +Target: " + targetName;
/* 1154 */     log(msg, 4);
/* 1155 */     target.setProject(this);
/* 1156 */     this.targets.put(targetName, target);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable<String, Target> getTargets() {
/* 1165 */     return this.targets;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Target> getCopyOfTargets() {
/* 1175 */     return new HashMap<>(this.targets);
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
/*      */   public Task createTask(String taskType) throws BuildException {
/* 1192 */     return ComponentHelper.getComponentHelper(this).createTask(taskType);
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
/*      */   public Object createDataType(String typeName) throws BuildException {
/* 1208 */     return ComponentHelper.getComponentHelper(this).createDataType(typeName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExecutor(Executor e) {
/* 1216 */     addReference("ant.executor", e);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Executor getExecutor() {
/* 1224 */     Object o = getReference("ant.executor");
/* 1225 */     if (o == null) {
/* 1226 */       String classname = getProperty("ant.executor.class");
/* 1227 */       if (classname == null) {
/* 1228 */         classname = DefaultExecutor.class.getName();
/*      */       }
/* 1230 */       log("Attempting to create object of type " + classname, 4);
/*      */       try {
/* 1232 */         o = Class.forName(classname, true, this.coreLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 1233 */       } catch (ClassNotFoundException seaEnEfEx) {
/*      */         
/*      */         try {
/* 1236 */           o = Class.forName(classname).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 1237 */         } catch (Exception ex) {
/* 1238 */           log(ex.toString(), 0);
/*      */         } 
/* 1240 */       } catch (Exception ex) {
/* 1241 */         log(ex.toString(), 0);
/*      */       } 
/* 1243 */       if (o == null) {
/* 1244 */         throw new BuildException("Unable to obtain a Target Executor instance.");
/*      */       }
/*      */       
/* 1247 */       setExecutor((Executor)o);
/*      */     } 
/* 1249 */     return (Executor)o;
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
/*      */   public void executeTargets(Vector<String> names) throws BuildException {
/* 1262 */     setUserProperty("ant.project.invoked-targets", 
/* 1263 */         String.join(",", (Iterable)names));
/* 1264 */     getExecutor().executeTargets(this, names.<String>toArray(new String[0]));
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
/*      */   public void demuxOutput(String output, boolean isWarning) {
/* 1277 */     Task task = getThreadTask(Thread.currentThread());
/* 1278 */     if (task == null) {
/* 1279 */       log(output, isWarning ? 1 : 2);
/* 1280 */     } else if (isWarning) {
/* 1281 */       task.handleErrorOutput(output);
/*      */     } else {
/* 1283 */       task.handleOutput(output);
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
/*      */   public int defaultInput(byte[] buffer, int offset, int length) throws IOException {
/* 1302 */     if (this.defaultInputStream == null) {
/* 1303 */       throw new EOFException("No input provided for project");
/*      */     }
/* 1305 */     System.out.flush();
/* 1306 */     return this.defaultInputStream.read(buffer, offset, length);
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
/*      */   public int demuxInput(byte[] buffer, int offset, int length) throws IOException {
/* 1323 */     Task task = getThreadTask(Thread.currentThread());
/* 1324 */     if (task == null) {
/* 1325 */       return defaultInput(buffer, offset, length);
/*      */     }
/* 1327 */     return task.handleInput(buffer, offset, length);
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
/*      */   public void demuxFlush(String output, boolean isError) {
/* 1342 */     Task task = getThreadTask(Thread.currentThread());
/* 1343 */     if (task == null) {
/* 1344 */       fireMessageLogged(this, output, isError ? 0 : 2);
/* 1345 */     } else if (isError) {
/* 1346 */       task.handleErrorFlush(output);
/*      */     } else {
/* 1348 */       task.handleFlush(output);
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
/*      */   public void executeTarget(String targetName) throws BuildException {
/* 1365 */     if (targetName == null) {
/* 1366 */       String msg = "No target specified";
/* 1367 */       throw new BuildException("No target specified");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1374 */     executeSortedTargets(topoSort(targetName, this.targets, false));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void executeSortedTargets(Vector<Target> sortedTargets) throws BuildException {
/* 1384 */     Set<String> succeededTargets = new HashSet<>();
/* 1385 */     BuildException buildException = null;
/* 1386 */     for (Target curtarget : sortedTargets) {
/* 1387 */       boolean canExecute = true;
/* 1388 */       for (String dependencyName : Collections.<String>list(curtarget.getDependencies())) {
/* 1389 */         if (!succeededTargets.contains(dependencyName)) {
/* 1390 */           canExecute = false;
/* 1391 */           log(curtarget, "Cannot execute '" + curtarget
/* 1392 */               .getName() + "' - '" + dependencyName + "' failed or was not executed.", 0);
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */       
/* 1398 */       if (canExecute) {
/* 1399 */         Throwable thrownException = null;
/*      */         try {
/* 1401 */           curtarget.performTasks();
/* 1402 */           succeededTargets.add(curtarget.getName());
/* 1403 */         } catch (RuntimeException ex) {
/* 1404 */           if (!this.keepGoingMode) {
/* 1405 */             throw ex;
/*      */           }
/* 1407 */           thrownException = ex;
/* 1408 */         } catch (Throwable ex) {
/* 1409 */           if (!this.keepGoingMode) {
/* 1410 */             throw new BuildException(ex);
/*      */           }
/* 1412 */           thrownException = ex;
/*      */         } 
/* 1414 */         if (thrownException != null) {
/* 1415 */           if (thrownException instanceof BuildException) {
/* 1416 */             log(curtarget, "Target '" + curtarget
/* 1417 */                 .getName() + "' failed with message '" + thrownException
/*      */                 
/* 1419 */                 .getMessage() + "'.", 0);
/*      */             
/* 1421 */             if (buildException == null)
/* 1422 */               buildException = (BuildException)thrownException; 
/*      */             continue;
/*      */           } 
/* 1425 */           log(curtarget, "Target '" + curtarget
/* 1426 */               .getName() + "' failed with message '" + thrownException
/*      */               
/* 1428 */               .getMessage() + "'.", 0);
/* 1429 */           thrownException.printStackTrace(System.err);
/* 1430 */           if (buildException == null) {
/* 1431 */             buildException = new BuildException(thrownException);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1438 */     if (buildException != null) {
/* 1439 */       throw buildException;
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
/*      */   @Deprecated
/*      */   public File resolveFile(String fileName, File rootDir) {
/* 1462 */     return FILE_UTILS.resolveFile(rootDir, fileName);
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
/*      */   public File resolveFile(String fileName) {
/* 1478 */     return FILE_UTILS.resolveFile(this.baseDir, fileName);
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
/*      */   @Deprecated
/*      */   public static String translatePath(String toProcess) {
/* 1502 */     return FileUtils.translatePath(toProcess);
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
/*      */   @Deprecated
/*      */   public void copyFile(String sourceFile, String destFile) throws IOException {
/* 1521 */     FILE_UTILS.copyFile(sourceFile, destFile);
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
/*      */   @Deprecated
/*      */   public void copyFile(String sourceFile, String destFile, boolean filtering) throws IOException {
/* 1542 */     FILE_UTILS.copyFile(sourceFile, destFile, 
/* 1543 */         filtering ? this.globalFilters : null);
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
/*      */   @Deprecated
/*      */   public void copyFile(String sourceFile, String destFile, boolean filtering, boolean overwrite) throws IOException {
/* 1567 */     FILE_UTILS.copyFile(sourceFile, destFile, 
/* 1568 */         filtering ? this.globalFilters : null, overwrite);
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
/*      */   @Deprecated
/*      */   public void copyFile(String sourceFile, String destFile, boolean filtering, boolean overwrite, boolean preserveLastModified) throws IOException {
/* 1598 */     FILE_UTILS.copyFile(sourceFile, destFile, 
/* 1599 */         filtering ? this.globalFilters : null, overwrite, preserveLastModified);
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
/*      */   @Deprecated
/*      */   public void copyFile(File sourceFile, File destFile) throws IOException {
/* 1617 */     FILE_UTILS.copyFile(sourceFile, destFile);
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
/*      */   @Deprecated
/*      */   public void copyFile(File sourceFile, File destFile, boolean filtering) throws IOException {
/* 1638 */     FILE_UTILS.copyFile(sourceFile, destFile, 
/* 1639 */         filtering ? this.globalFilters : null);
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
/*      */   @Deprecated
/*      */   public void copyFile(File sourceFile, File destFile, boolean filtering, boolean overwrite) throws IOException {
/* 1663 */     FILE_UTILS.copyFile(sourceFile, destFile, 
/* 1664 */         filtering ? this.globalFilters : null, overwrite);
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
/*      */   @Deprecated
/*      */   public void copyFile(File sourceFile, File destFile, boolean filtering, boolean overwrite, boolean preserveLastModified) throws IOException {
/* 1694 */     FILE_UTILS.copyFile(sourceFile, destFile, 
/* 1695 */         filtering ? this.globalFilters : null, overwrite, preserveLastModified);
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
/*      */   @Deprecated
/*      */   public void setFileLastModified(File file, long time) throws BuildException {
/* 1716 */     FILE_UTILS.setFileLastModified(file, time);
/* 1717 */     log("Setting modification time for " + file, 3);
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
/*      */   public static boolean toBoolean(String s) {
/* 1732 */     return ("on".equalsIgnoreCase(s) || "true"
/* 1733 */       .equalsIgnoreCase(s) || "yes"
/* 1734 */       .equalsIgnoreCase(s));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Project getProject(Object o) {
/* 1744 */     if (o instanceof ProjectComponent) {
/* 1745 */       return ((ProjectComponent)o).getProject();
/*      */     }
/*      */     try {
/* 1748 */       Method m = o.getClass().getMethod("getProject", new Class[0]);
/* 1749 */       if (Project.class.equals(m.getReturnType())) {
/* 1750 */         return (Project)m.invoke(o, new Object[0]);
/*      */       }
/* 1752 */     } catch (Exception exception) {}
/*      */ 
/*      */     
/* 1755 */     return null;
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
/*      */   public final Vector<Target> topoSort(String root, Hashtable<String, Target> targetTable) throws BuildException {
/* 1774 */     return topoSort(new String[] { root }, targetTable, true);
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
/*      */   public final Vector<Target> topoSort(String root, Hashtable<String, Target> targetTable, boolean returnAll) throws BuildException {
/* 1796 */     return topoSort(new String[] { root }, targetTable, returnAll);
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
/*      */   public final Vector<Target> topoSort(String[] roots, Hashtable<String, Target> targetTable, boolean returnAll) throws BuildException {
/* 1818 */     VectorSet vectorSet = new VectorSet();
/* 1819 */     Hashtable<String, String> state = new Hashtable<>();
/* 1820 */     Stack<String> visiting = new Stack<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1830 */     for (String root : roots) {
/* 1831 */       String st = state.get(root);
/* 1832 */       if (st == null) {
/* 1833 */         tsort(root, targetTable, state, visiting, (Vector<Target>)vectorSet);
/* 1834 */       } else if (st == "VISITING") {
/* 1835 */         throw new BuildException("Unexpected node in visiting state: " + root);
/*      */       } 
/*      */     } 
/*      */     
/* 1839 */     log("Build sequence for target(s)" + 
/* 1840 */         (String)Arrays.<String>stream(roots).map(root -> String.format(" `%s'", new Object[] { root
/* 1841 */             })).collect(Collectors.joining(",")) + " is " + vectorSet, 3);
/*      */ 
/*      */     
/* 1844 */     Vector<Target> complete = returnAll ? (Vector<Target>)vectorSet : new Vector<>((Collection<? extends Target>)vectorSet);
/* 1845 */     for (String curTarget : targetTable.keySet()) {
/* 1846 */       String st = state.get(curTarget);
/* 1847 */       if (st == null) {
/* 1848 */         tsort(curTarget, targetTable, state, visiting, complete); continue;
/* 1849 */       }  if (st == "VISITING") {
/* 1850 */         throw new BuildException("Unexpected node in visiting state: " + curTarget);
/*      */       }
/*      */     } 
/*      */     
/* 1854 */     log("Complete build sequence is " + complete, 3);
/* 1855 */     return (Vector<Target>)vectorSet;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void tsort(String root, Hashtable<String, Target> targetTable, Hashtable<String, String> state, Stack<String> visiting, Vector<Target> ret) throws BuildException {
/* 1902 */     state.put(root, "VISITING");
/* 1903 */     visiting.push(root);
/*      */     
/* 1905 */     Target target = targetTable.get(root);
/*      */ 
/*      */     
/* 1908 */     if (target == null) {
/* 1909 */       StringBuilder sb = new StringBuilder("Target \"");
/* 1910 */       sb.append(root);
/* 1911 */       sb.append("\" does not exist in the project \"");
/* 1912 */       sb.append(this.name);
/* 1913 */       sb.append("\". ");
/* 1914 */       visiting.pop();
/* 1915 */       if (!visiting.empty()) {
/* 1916 */         String parent = visiting.peek();
/* 1917 */         sb.append("It is used from target \"");
/* 1918 */         sb.append(parent);
/* 1919 */         sb.append("\".");
/*      */       } 
/* 1921 */       throw new BuildException(new String(sb));
/*      */     } 
/* 1923 */     for (String cur : Collections.<String>list(target.getDependencies())) {
/* 1924 */       String m = state.get(cur);
/* 1925 */       if (m == null) {
/*      */         
/* 1927 */         tsort(cur, targetTable, state, visiting, ret); continue;
/* 1928 */       }  if (m == "VISITING")
/*      */       {
/* 1930 */         throw makeCircularException(cur, visiting);
/*      */       }
/*      */     } 
/* 1933 */     String p = visiting.pop();
/* 1934 */     if (root != p) {
/* 1935 */       throw new BuildException("Unexpected internal error: expected to pop " + root + " but got " + p);
/*      */     }
/*      */     
/* 1938 */     state.put(root, "VISITED");
/* 1939 */     ret.addElement(target);
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
/*      */   private static BuildException makeCircularException(String end, Stack<String> stk) {
/* 1952 */     StringBuilder sb = new StringBuilder("Circular dependency: ");
/* 1953 */     sb.append(end);
/*      */     
/*      */     while (true) {
/* 1956 */       String c = stk.pop();
/* 1957 */       sb.append(" <- ");
/* 1958 */       sb.append(c);
/* 1959 */       if (c.equals(end)) {
/* 1960 */         return new BuildException(sb.toString());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void inheritIDReferences(Project parent) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addIdReference(String id, Object value) {
/* 1977 */     this.idReferences.put(id, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addReference(String referenceName, Object value) {
/* 1987 */     synchronized (this.referencesLock) {
/* 1988 */       Object old = ((AntRefTable)this.references).getReal(referenceName);
/* 1989 */       if (old == value) {
/*      */         return;
/*      */       }
/*      */       
/* 1993 */       if (old != null && !(old instanceof UnknownElement)) {
/* 1994 */         log("Overriding previous definition of reference to " + referenceName, 3);
/*      */       }
/*      */       
/* 1997 */       log("Adding reference: " + referenceName, 4);
/* 1998 */       this.references.put(referenceName, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hashtable<String, Object> getReferences() {
/* 2009 */     return this.references;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasReference(String key) {
/* 2020 */     synchronized (this.referencesLock) {
/* 2021 */       return this.references.containsKey(key);
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
/*      */   public Map<String, Object> getCopyOfReferences() {
/* 2035 */     synchronized (this.referencesLock) {
/* 2036 */       return new HashMap<>(this.references);
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
/*      */   public <T> T getReference(String key) {
/* 2051 */     synchronized (this.referencesLock) {
/*      */       
/* 2053 */       T ret = (T)this.references.get(key);
/* 2054 */       if (ret != null) {
/* 2055 */         return ret;
/*      */       }
/*      */     } 
/*      */     
/* 2059 */     if (!key.equals("ant.PropertyHelper")) {
/*      */       try {
/* 2061 */         if (PropertyHelper.getPropertyHelper(this).containsProperties(key)) {
/* 2062 */           log("Unresolvable reference " + key + " might be a misuse of property expansion syntax.", 1);
/*      */         }
/*      */       }
/* 2065 */       catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */     
/* 2069 */     return null;
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
/*      */   public String getElementName(Object element) {
/* 2086 */     return ComponentHelper.getComponentHelper(this).getElementName(element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fireBuildStarted() {
/* 2094 */     BuildEvent event = new BuildEvent(this);
/* 2095 */     for (BuildListener currListener : this.listeners) {
/* 2096 */       currListener.buildStarted(event);
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
/*      */   public void fireBuildFinished(Throwable exception) {
/* 2108 */     BuildEvent event = new BuildEvent(this);
/* 2109 */     event.setException(exception);
/* 2110 */     for (BuildListener currListener : this.listeners) {
/* 2111 */       currListener.buildFinished(event);
/*      */     }
/*      */     
/* 2114 */     IntrospectionHelper.clearCache();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void fireSubBuildStarted() {
/* 2124 */     BuildEvent event = new BuildEvent(this);
/* 2125 */     for (BuildListener currListener : this.listeners) {
/* 2126 */       if (currListener instanceof SubBuildListener) {
/* 2127 */         ((SubBuildListener)currListener).subBuildStarted(event);
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
/*      */ 
/*      */   
/*      */   public void fireSubBuildFinished(Throwable exception) {
/* 2142 */     BuildEvent event = new BuildEvent(this);
/* 2143 */     event.setException(exception);
/* 2144 */     for (BuildListener currListener : this.listeners) {
/* 2145 */       if (currListener instanceof SubBuildListener) {
/* 2146 */         ((SubBuildListener)currListener).subBuildFinished(event);
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
/*      */   protected void fireTargetStarted(Target target) {
/* 2159 */     BuildEvent event = new BuildEvent(target);
/* 2160 */     for (BuildListener currListener : this.listeners) {
/* 2161 */       currListener.targetStarted(event);
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
/*      */   protected void fireTargetFinished(Target target, Throwable exception) {
/* 2177 */     BuildEvent event = new BuildEvent(target);
/* 2178 */     event.setException(exception);
/* 2179 */     for (BuildListener currListener : this.listeners) {
/* 2180 */       currListener.targetFinished(event);
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
/*      */   protected void fireTaskStarted(Task task) {
/* 2194 */     registerThreadTask(Thread.currentThread(), task);
/* 2195 */     BuildEvent event = new BuildEvent(task);
/* 2196 */     for (BuildListener currListener : this.listeners) {
/* 2197 */       currListener.taskStarted(event);
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
/*      */   protected void fireTaskFinished(Task task, Throwable exception) {
/* 2212 */     registerThreadTask(Thread.currentThread(), null);
/* 2213 */     System.out.flush();
/* 2214 */     System.err.flush();
/* 2215 */     BuildEvent event = new BuildEvent(task);
/* 2216 */     event.setException(exception);
/* 2217 */     for (BuildListener currListener : this.listeners) {
/* 2218 */       currListener.taskFinished(event);
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
/*      */   private void fireMessageLoggedEvent(BuildEvent event, String message, int priority) {
/* 2237 */     if (message == null) {
/* 2238 */       message = String.valueOf(message);
/*      */     }
/* 2240 */     if (message.endsWith(System.lineSeparator())) {
/* 2241 */       int endIndex = message.length() - System.lineSeparator().length();
/* 2242 */       event.setMessage(message.substring(0, endIndex), priority);
/*      */     } else {
/* 2244 */       event.setMessage(message, priority);
/*      */     } 
/* 2246 */     if (this.isLoggingMessage.get() != Boolean.FALSE) {
/*      */       return;
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
/*      */     try {
/* 2263 */       this.isLoggingMessage.set(Boolean.TRUE);
/* 2264 */       for (BuildListener currListener : this.listeners) {
/* 2265 */         currListener.messageLogged(event);
/*      */       }
/*      */     } finally {
/* 2268 */       this.isLoggingMessage.set(Boolean.FALSE);
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
/*      */   protected void fireMessageLogged(Project project, String message, int priority) {
/* 2283 */     fireMessageLogged(project, message, (Throwable)null, priority);
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
/*      */   protected void fireMessageLogged(Project project, String message, Throwable throwable, int priority) {
/* 2299 */     BuildEvent event = new BuildEvent(project);
/* 2300 */     event.setException(throwable);
/* 2301 */     fireMessageLoggedEvent(event, message, priority);
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
/*      */   protected void fireMessageLogged(Target target, String message, int priority) {
/* 2315 */     fireMessageLogged(target, message, (Throwable)null, priority);
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
/*      */   protected void fireMessageLogged(Target target, String message, Throwable throwable, int priority) {
/* 2331 */     BuildEvent event = new BuildEvent(target);
/* 2332 */     event.setException(throwable);
/* 2333 */     fireMessageLoggedEvent(event, message, priority);
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
/*      */   protected void fireMessageLogged(Task task, String message, int priority) {
/* 2346 */     fireMessageLogged(task, message, (Throwable)null, priority);
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
/*      */   protected void fireMessageLogged(Task task, String message, Throwable throwable, int priority) {
/* 2362 */     BuildEvent event = new BuildEvent(task);
/* 2363 */     event.setException(throwable);
/* 2364 */     fireMessageLoggedEvent(event, message, priority);
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
/*      */   public void registerThreadTask(Thread thread, Task task) {
/* 2376 */     synchronized (this.threadTasks) {
/* 2377 */       if (task != null) {
/* 2378 */         this.threadTasks.put(thread, task);
/* 2379 */         this.threadGroupTasks.put(thread.getThreadGroup(), task);
/*      */       } else {
/* 2381 */         this.threadTasks.remove(thread);
/* 2382 */         this.threadGroupTasks.remove(thread.getThreadGroup());
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
/*      */   public Task getThreadTask(Thread thread) {
/* 2395 */     synchronized (this.threadTasks) {
/* 2396 */       Task task = this.threadTasks.get(thread);
/* 2397 */       if (task == null) {
/* 2398 */         ThreadGroup group = thread.getThreadGroup();
/* 2399 */         while (task == null && group != null) {
/* 2400 */           task = this.threadGroupTasks.get(group);
/* 2401 */           group = group.getParent();
/*      */         } 
/*      */       } 
/* 2404 */       return task;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AntRefTable
/*      */     extends Hashtable<String, Object>
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object getReal(Object key) {
/* 2426 */       return super.get(key);
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
/*      */     public Object get(Object key) {
/* 2442 */       Object o = getReal(key);
/* 2443 */       if (o instanceof UnknownElement) {
/*      */         
/* 2445 */         UnknownElement ue = (UnknownElement)o;
/* 2446 */         ue.maybeConfigure();
/* 2447 */         o = ue.getRealThing();
/*      */       } 
/* 2449 */       return o;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setProjectReference(Object obj) {
/* 2460 */     if (obj instanceof ProjectComponent) {
/* 2461 */       ((ProjectComponent)obj).setProject(this);
/*      */       return;
/*      */     } 
/*      */     try {
/* 2465 */       Method method = obj.getClass().getMethod("setProject", new Class[] { Project.class });
/* 2466 */       if (method != null) {
/* 2467 */         method.invoke(obj, new Object[] { this });
/*      */       }
/* 2469 */     } catch (Throwable throwable) {}
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
/*      */   public Resource getResource(String name) {
/* 2485 */     return (Resource)new FileResource(getBaseDir(), name);
/*      */   }
/*      */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/Project.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */