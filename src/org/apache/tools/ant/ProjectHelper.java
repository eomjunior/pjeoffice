/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Hashtable;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ import org.apache.tools.ant.types.resources.FileResource;
/*     */ import org.apache.tools.ant.util.LoaderUtils;
/*     */ import org.xml.sax.AttributeList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProjectHelper
/*     */ {
/*     */   public static final String ANT_CORE_URI = "antlib:org.apache.tools.ant";
/*     */   public static final String ANT_CURRENT_URI = "ant:current";
/*     */   public static final String ANT_ATTRIBUTE_URI = "ant:attribute";
/*     */   @Deprecated
/*     */   public static final String ANTLIB_URI = "antlib:";
/*     */   public static final String ANT_TYPE = "ant-type";
/*     */   @Deprecated
/*     */   public static final String HELPER_PROPERTY = "org.apache.tools.ant.ProjectHelper";
/*     */   @Deprecated
/*     */   public static final String SERVICE_ID = "META-INF/services/org.apache.tools.ant.ProjectHelper";
/*     */   @Deprecated
/*     */   public static final String PROJECTHELPER_REFERENCE = "ant.projectHelper";
/*     */   public static final String USE_PROJECT_NAME_AS_TARGET_PREFIX = "USE_PROJECT_NAME_AS_TARGET_PREFIX";
/*     */   
/*     */   public static void configureProject(Project project, File buildFile) throws BuildException {
/* 101 */     FileResource resource = new FileResource(buildFile);
/* 102 */     ProjectHelper helper = ProjectHelperRepository.getInstance().getProjectHelperForBuildFile((Resource)resource);
/* 103 */     project.addReference("ant.projectHelper", helper);
/* 104 */     helper.parse(project, buildFile);
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
/*     */   public static final class OnMissingExtensionPoint
/*     */   {
/* 118 */     public static final OnMissingExtensionPoint FAIL = new OnMissingExtensionPoint("fail");
/*     */ 
/*     */ 
/*     */     
/* 122 */     public static final OnMissingExtensionPoint WARN = new OnMissingExtensionPoint("warn");
/*     */ 
/*     */ 
/*     */     
/* 126 */     public static final OnMissingExtensionPoint IGNORE = new OnMissingExtensionPoint("ignore");
/*     */ 
/*     */     
/* 129 */     private static final OnMissingExtensionPoint[] values = new OnMissingExtensionPoint[] { FAIL, WARN, IGNORE };
/*     */     
/*     */     private final String name;
/*     */ 
/*     */     
/*     */     private OnMissingExtensionPoint(String name) {
/* 135 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String name() {
/* 139 */       return this.name;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 143 */       return this.name;
/*     */     }
/*     */     
/*     */     public static OnMissingExtensionPoint valueOf(String name) {
/* 147 */       if (name == null) {
/* 148 */         throw new NullPointerException();
/*     */       }
/* 150 */       for (OnMissingExtensionPoint value : values) {
/* 151 */         if (name.equals(value.name())) {
/* 152 */           return value;
/*     */         }
/*     */       } 
/* 155 */       throw new IllegalArgumentException("Unknown onMissingExtensionPoint " + name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   private Vector<Object> importStack = new Vector();
/* 165 */   private List<String[]> extensionStack = (List)new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<Object> getImportStack() {
/* 175 */     return this.importStack;
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
/*     */   public List<String[]> getExtensionStack() {
/* 188 */     return this.extensionStack;
/*     */   }
/*     */   
/* 191 */   private static final ThreadLocal<String> targetPrefix = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCurrentTargetPrefix() {
/* 203 */     return targetPrefix.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setCurrentTargetPrefix(String prefix) {
/* 213 */     targetPrefix.set(prefix);
/*     */   }
/*     */   
/* 216 */   private static final ThreadLocal<String> prefixSeparator = ThreadLocal.withInitial(() -> ".");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCurrentPrefixSeparator() {
/* 227 */     return prefixSeparator.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setCurrentPrefixSeparator(String sep) {
/* 237 */     prefixSeparator.set(sep);
/*     */   }
/*     */   
/* 240 */   private static final ThreadLocal<Boolean> inIncludeMode = ThreadLocal.withInitial(() -> Boolean.FALSE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInIncludeMode() {
/* 259 */     return Boolean.TRUE.equals(inIncludeMode.get());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setInIncludeMode(boolean includeMode) {
/* 270 */     inIncludeMode.set(Boolean.valueOf(includeMode));
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
/*     */   public void parse(Project project, Object source) throws BuildException {
/* 288 */     throw new BuildException("ProjectHelper.parse() must be implemented in a helper plugin " + 
/* 289 */         getClass().getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ProjectHelper getProjectHelper() {
/* 299 */     return ProjectHelperRepository.getInstance().getHelpers().next();
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
/*     */   @Deprecated
/*     */   public static ClassLoader getContextClassLoader() {
/* 313 */     return LoaderUtils.isContextLoaderAvailable() ? LoaderUtils.getContextClassLoader() : null;
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
/*     */   @Deprecated
/*     */   public static void configure(Object target, AttributeList attrs, Project project) throws BuildException {
/* 337 */     if (target instanceof TypeAdapter) {
/* 338 */       target = ((TypeAdapter)target).getProxy();
/*     */     }
/* 340 */     IntrospectionHelper ih = IntrospectionHelper.getHelper(project, target.getClass());
/*     */     
/* 342 */     for (int i = 0, length = attrs.getLength(); i < length; i++) {
/*     */       
/* 344 */       String value = replaceProperties(project, attrs.getValue(i), project.getProperties());
/*     */       try {
/* 346 */         ih.setAttribute(project, target, attrs.getName(i).toLowerCase(Locale.ENGLISH), value);
/* 347 */       } catch (BuildException be) {
/*     */         
/* 349 */         if (!attrs.getName(i).equals("id")) {
/* 350 */           throw be;
/*     */         }
/*     */       } 
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
/*     */   public static void addText(Project project, Object target, char[] buf, int start, int count) throws BuildException {
/* 372 */     addText(project, target, new String(buf, start, count));
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
/*     */   public static void addText(Project project, Object target, String text) throws BuildException {
/* 391 */     if (text == null) {
/*     */       return;
/*     */     }
/* 394 */     if (target instanceof TypeAdapter) {
/* 395 */       target = ((TypeAdapter)target).getProxy();
/*     */     }
/* 397 */     IntrospectionHelper.getHelper(project, target.getClass()).addText(project, target, text);
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
/*     */   public static void storeChild(Project project, Object parent, Object child, String tag) {
/* 414 */     IntrospectionHelper ih = IntrospectionHelper.getHelper(project, parent.getClass());
/* 415 */     ih.storeElement(project, parent, child, tag);
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
/*     */   @Deprecated
/*     */   public static String replaceProperties(Project project, String value) throws BuildException {
/* 441 */     return project.replaceProperties(value);
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
/*     */   @Deprecated
/*     */   public static String replaceProperties(Project project, String value, Hashtable<String, Object> keys) throws BuildException {
/* 467 */     PropertyHelper ph = PropertyHelper.getPropertyHelper(project);
/* 468 */     return ph.replaceProperties(null, value, keys);
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
/*     */   @Deprecated
/*     */   public static void parsePropertyString(String value, Vector<String> fragments, Vector<String> propertyRefs) throws BuildException {
/* 495 */     PropertyHelper.parsePropertyStringDefault(value, fragments, propertyRefs);
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
/*     */   public static String genComponentName(String uri, String name) {
/* 508 */     if (uri == null || uri.isEmpty() || uri.equals("antlib:org.apache.tools.ant")) {
/* 509 */       return name;
/*     */     }
/* 511 */     return uri + ":" + name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String extractUriFromComponentName(String componentName) {
/* 521 */     if (componentName == null) {
/* 522 */       return "";
/*     */     }
/* 524 */     int index = componentName.lastIndexOf(':');
/* 525 */     if (index == -1) {
/* 526 */       return "";
/*     */     }
/* 528 */     return componentName.substring(0, index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String extractNameFromComponentName(String componentName) {
/* 538 */     int index = componentName.lastIndexOf(':');
/* 539 */     if (index == -1) {
/* 540 */       return componentName;
/*     */     }
/* 542 */     return componentName.substring(index + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String nsToComponentName(String ns) {
/* 552 */     return "attribute namespace:" + ns;
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
/*     */   public static BuildException addLocationToBuildException(BuildException ex, Location newLocation) {
/* 566 */     if (ex.getLocation() == null || ex.getMessage() == null) {
/* 567 */       return ex;
/*     */     }
/*     */     
/* 570 */     String errorMessage = String.format("The following error occurred while executing this line:%n%s%s", new Object[] {
/* 571 */           ex.getLocation().toString(), ex.getMessage() });
/* 572 */     if (ex instanceof ExitStatusException) {
/* 573 */       int exitStatus = ((ExitStatusException)ex).getStatus();
/* 574 */       if (newLocation == null) {
/* 575 */         return new ExitStatusException(errorMessage, exitStatus);
/*     */       }
/* 577 */       return new ExitStatusException(errorMessage, exitStatus, newLocation);
/*     */     } 
/* 579 */     if (newLocation == null) {
/* 580 */       return new BuildException(errorMessage, ex);
/*     */     }
/* 582 */     return new BuildException(errorMessage, ex, newLocation);
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
/*     */   public boolean canParseAntlibDescriptor(Resource r) {
/* 606 */     return false;
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
/*     */   public UnknownElement parseAntlibDescriptor(Project containingProject, Resource source) {
/* 620 */     throw new BuildException("can't parse antlib descriptors");
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
/*     */   public boolean canParseBuildFile(Resource buildFile) {
/* 633 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultBuildFile() {
/* 643 */     return "build.xml";
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
/*     */   public void resolveExtensionOfAttributes(Project project) throws BuildException {
/* 667 */     for (String[] extensionInfo : getExtensionStack()) {
/* 668 */       String extPointName = extensionInfo[0];
/* 669 */       String targetName = extensionInfo[1];
/* 670 */       OnMissingExtensionPoint missingBehaviour = OnMissingExtensionPoint.valueOf(extensionInfo[2]);
/*     */ 
/*     */ 
/*     */       
/* 674 */       String prefixAndSep = (extensionInfo.length > 3) ? extensionInfo[3] : null;
/*     */ 
/*     */       
/* 677 */       Hashtable<String, Target> projectTargets = project.getTargets();
/* 678 */       Target extPoint = null;
/* 679 */       if (prefixAndSep == null) {
/*     */         
/* 681 */         extPoint = projectTargets.get(extPointName);
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 691 */         extPoint = projectTargets.get(prefixAndSep + extPointName);
/* 692 */         if (extPoint == null) {
/* 693 */           extPoint = projectTargets.get(extPointName);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 698 */       if (extPoint == null) {
/* 699 */         String message = "can't add target " + targetName + " to extension-point " + extPointName + " because the extension-point is unknown.";
/*     */ 
/*     */         
/* 702 */         if (missingBehaviour == OnMissingExtensionPoint.FAIL)
/* 703 */           throw new BuildException(message); 
/* 704 */         if (missingBehaviour == OnMissingExtensionPoint.WARN) {
/* 705 */           Target t = projectTargets.get(targetName);
/* 706 */           project.log(t, "Warning: " + message, 1);
/*     */         }  continue;
/*     */       } 
/* 709 */       if (!(extPoint instanceof ExtensionPoint)) {
/* 710 */         throw new BuildException("referenced target " + extPointName + " is not an extension-point");
/*     */       }
/*     */       
/* 713 */       extPoint.addDependency(targetName);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/ProjectHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */