/*     */ package org.apache.tools.ant.helper;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Location;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.RuntimeConfigurable;
/*     */ import org.apache.tools.ant.Target;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntXMLContext
/*     */ {
/*     */   private Project project;
/*     */   private File buildFile;
/*     */   private URL buildFileURL;
/*  56 */   private Vector<Target> targetVector = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File buildFileParent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private URL buildFileParentURL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String currentProjectName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Locator locator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private Target implicitTarget = new Target();
/*     */ 
/*     */ 
/*     */   
/*  89 */   private Target currentTarget = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   private Vector<RuntimeConfigurable> wStack = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean ignoreProjectTag = false;
/*     */ 
/*     */ 
/*     */   
/* 103 */   private Map<String, List<String>> prefixMapping = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/* 107 */   private Map<String, Target> currentTargets = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AntXMLContext(Project project) {
/* 114 */     this.project = project;
/* 115 */     this.implicitTarget.setProject(project);
/* 116 */     this.implicitTarget.setName("");
/* 117 */     this.targetVector.addElement(this.implicitTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBuildFile(File buildFile) {
/* 125 */     this.buildFile = buildFile;
/* 126 */     if (buildFile != null) {
/* 127 */       this.buildFileParent = new File(buildFile.getParent());
/* 128 */       this.implicitTarget.setLocation(new Location(buildFile.getAbsolutePath()));
/*     */       try {
/* 130 */         setBuildFile(FileUtils.getFileUtils().getFileURL(buildFile));
/* 131 */       } catch (MalformedURLException ex) {
/* 132 */         throw new BuildException(ex);
/*     */       } 
/*     */     } else {
/* 135 */       this.buildFileParent = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBuildFile(URL buildFile) throws MalformedURLException {
/* 146 */     this.buildFileURL = buildFile;
/* 147 */     this.buildFileParentURL = new URL(buildFile, ".");
/* 148 */     if (this.implicitTarget.getLocation() == null) {
/* 149 */       this.implicitTarget.setLocation(new Location(buildFile.toString()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getBuildFile() {
/* 158 */     return this.buildFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getBuildFileParent() {
/* 166 */     return this.buildFileParent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getBuildFileURL() {
/* 175 */     return this.buildFileURL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URL getBuildFileParentURL() {
/* 184 */     return this.buildFileParentURL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/* 192 */     return this.project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCurrentProjectName() {
/* 200 */     return this.currentProjectName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentProjectName(String name) {
/* 208 */     this.currentProjectName = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RuntimeConfigurable currentWrapper() {
/* 217 */     if (this.wStack.size() < 1) {
/* 218 */       return null;
/*     */     }
/* 220 */     return this.wStack.elementAt(this.wStack.size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RuntimeConfigurable parentWrapper() {
/* 229 */     if (this.wStack.size() < 2) {
/* 230 */       return null;
/*     */     }
/* 232 */     return this.wStack.elementAt(this.wStack.size() - 2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pushWrapper(RuntimeConfigurable wrapper) {
/* 240 */     this.wStack.addElement(wrapper);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void popWrapper() {
/* 247 */     if (this.wStack.size() > 0) {
/* 248 */       this.wStack.removeElementAt(this.wStack.size() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<RuntimeConfigurable> getWrapperStack() {
/* 257 */     return this.wStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTarget(Target target) {
/* 265 */     this.targetVector.addElement(target);
/* 266 */     this.currentTarget = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Target getCurrentTarget() {
/* 274 */     return this.currentTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Target getImplicitTarget() {
/* 282 */     return this.implicitTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentTarget(Target target) {
/* 290 */     this.currentTarget = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplicitTarget(Target target) {
/* 298 */     this.implicitTarget = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<Target> getTargets() {
/* 306 */     return this.targetVector;
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
/*     */   public void configureId(Object element, Attributes attr) {
/* 320 */     String id = attr.getValue("id");
/* 321 */     if (id != null) {
/* 322 */       this.project.addIdReference(id, element);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locator getLocator() {
/* 331 */     return this.locator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocator(Locator locator) {
/* 339 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIgnoringProjectTag() {
/* 347 */     return this.ignoreProjectTag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreProjectTag(boolean flag) {
/* 355 */     this.ignoreProjectTag = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startPrefixMapping(String prefix, String uri) {
/* 365 */     List<String> list = this.prefixMapping.computeIfAbsent(prefix, k -> new ArrayList());
/* 366 */     list.add(uri);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void endPrefixMapping(String prefix) {
/* 375 */     List<String> list = this.prefixMapping.get(prefix);
/* 376 */     if (list == null || list.isEmpty()) {
/*     */       return;
/*     */     }
/* 379 */     list.remove(list.size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrefixMapping(String prefix) {
/* 389 */     List<String> list = this.prefixMapping.get(prefix);
/* 390 */     if (list == null || list.isEmpty()) {
/* 391 */       return null;
/*     */     }
/* 393 */     return list.get(list.size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Target> getCurrentTargets() {
/* 401 */     return this.currentTargets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCurrentTargets(Map<String, Target> currentTargets) {
/* 409 */     this.currentTargets = currentTargets;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/helper/AntXMLContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */