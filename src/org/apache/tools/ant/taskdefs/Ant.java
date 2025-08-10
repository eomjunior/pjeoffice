/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.file.Files;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.BuildListener;
/*     */ import org.apache.tools.ant.DefaultLogger;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.Target;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.PropertySet;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.VectorSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Ant
/*     */   extends Task
/*     */ {
/*  70 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */   
/*  73 */   private File dir = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  79 */   private String antFile = null;
/*     */ 
/*     */   
/*  82 */   private String output = null;
/*     */ 
/*     */   
/*     */   private boolean inheritAll = true;
/*     */ 
/*     */   
/*     */   private boolean inheritRefs = false;
/*     */ 
/*     */   
/*  91 */   private List<Property> properties = new Vector<>();
/*     */ 
/*     */   
/*  94 */   private List<Reference> references = new Vector<>();
/*     */ 
/*     */   
/*     */   private Project newProject;
/*     */ 
/*     */   
/* 100 */   private PrintStream out = null;
/*     */ 
/*     */   
/* 103 */   private List<PropertySet> propertySets = new Vector<>();
/*     */ 
/*     */   
/* 106 */   private List<String> targets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean targetAttributeSet = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useNativeBasedir = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Ant(Task owner) {
/* 132 */     bindToOwner(owner);
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
/*     */   public void setUseNativeBasedir(boolean b) {
/* 144 */     this.useNativeBasedir = b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInheritAll(boolean value) {
/* 153 */     this.inheritAll = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInheritRefs(boolean value) {
/* 162 */     this.inheritRefs = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 170 */     this.newProject = getProject().createSubProject();
/* 171 */     this.newProject.setJavaVersionProperty();
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
/*     */   private void reinit() {
/* 185 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeProject() {
/* 196 */     this.newProject.setInputHandler(getProject().getInputHandler());
/*     */     
/* 198 */     getProject().getBuildListeners().forEach(bl -> this.newProject.addBuildListener(bl));
/*     */     
/* 200 */     if (this.output != null) {
/*     */       File outfile;
/* 202 */       if (this.dir != null) {
/* 203 */         outfile = FILE_UTILS.resolveFile(this.dir, this.output);
/*     */       } else {
/* 205 */         outfile = getProject().resolveFile(this.output);
/*     */       } 
/*     */       try {
/* 208 */         this.out = new PrintStream(Files.newOutputStream(outfile.toPath(), new java.nio.file.OpenOption[0]));
/* 209 */         DefaultLogger logger = new DefaultLogger();
/* 210 */         logger.setMessageOutputLevel(2);
/* 211 */         logger.setOutputPrintStream(this.out);
/* 212 */         logger.setErrorPrintStream(this.out);
/* 213 */         this.newProject.addBuildListener((BuildListener)logger);
/* 214 */       } catch (IOException ex) {
/* 215 */         log("Ant: Can't set output to " + this.output);
/*     */       } 
/*     */     } 
/*     */     
/* 219 */     if (this.useNativeBasedir) {
/* 220 */       addAlmostAll(getProject().getUserProperties(), PropertyType.USER);
/*     */     } else {
/* 222 */       getProject().copyUserProperties(this.newProject);
/*     */     } 
/*     */     
/* 225 */     if (!this.inheritAll) {
/*     */ 
/*     */       
/* 228 */       this.newProject.initProperties();
/*     */     }
/*     */     else {
/*     */       
/* 232 */       addAlmostAll(getProject().getProperties(), PropertyType.PLAIN);
/*     */     } 
/*     */     
/* 235 */     for (PropertySet ps : this.propertySets) {
/* 236 */       addAlmostAll(ps.getProperties(), PropertyType.PLAIN);
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
/*     */   public void handleOutput(String outputToHandle) {
/* 250 */     if (this.newProject != null) {
/* 251 */       this.newProject.demuxOutput(outputToHandle, false);
/*     */     } else {
/* 253 */       super.handleOutput(outputToHandle);
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
/*     */   
/*     */   public int handleInput(byte[] buffer, int offset, int length) throws IOException {
/* 274 */     if (this.newProject != null) {
/* 275 */       return this.newProject.demuxInput(buffer, offset, length);
/*     */     }
/* 277 */     return super.handleInput(buffer, offset, length);
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
/*     */   public void handleFlush(String toFlush) {
/* 290 */     if (this.newProject != null) {
/* 291 */       this.newProject.demuxFlush(toFlush, false);
/*     */     } else {
/* 293 */       super.handleFlush(toFlush);
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
/*     */   public void handleErrorOutput(String errorOutputToHandle) {
/* 308 */     if (this.newProject != null) {
/* 309 */       this.newProject.demuxOutput(errorOutputToHandle, true);
/*     */     } else {
/* 311 */       super.handleErrorOutput(errorOutputToHandle);
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
/*     */   public void handleErrorFlush(String errorOutputToFlush) {
/* 325 */     if (this.newProject != null) {
/* 326 */       this.newProject.demuxFlush(errorOutputToFlush, true);
/*     */     } else {
/* 328 */       super.handleErrorFlush(errorOutputToFlush);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 339 */     File savedDir = this.dir;
/* 340 */     String savedAntFile = this.antFile;
/* 341 */     VectorSet<String> vectorSet = new VectorSet(this.targets);
/*     */     try {
/* 343 */       getNewProject();
/*     */       
/* 345 */       if (this.dir == null && this.inheritAll) {
/* 346 */         this.dir = getProject().getBaseDir();
/*     */       }
/*     */       
/* 349 */       initializeProject();
/*     */       
/* 351 */       if (this.dir != null) {
/* 352 */         if (!this.useNativeBasedir) {
/* 353 */           this.newProject.setBaseDir(this.dir);
/* 354 */           if (savedDir != null)
/*     */           {
/* 356 */             this.newProject.setInheritedProperty("basedir", this.dir
/* 357 */                 .getAbsolutePath());
/*     */           }
/*     */         } 
/*     */       } else {
/* 361 */         this.dir = getProject().getBaseDir();
/*     */       } 
/*     */       
/* 364 */       overrideProperties();
/*     */       
/* 366 */       if (this.antFile == null) {
/* 367 */         this.antFile = getDefaultBuildFile();
/*     */       }
/*     */       
/* 370 */       File file = FILE_UTILS.resolveFile(this.dir, this.antFile);
/* 371 */       this.antFile = file.getAbsolutePath();
/*     */       
/* 373 */       log("calling target(s) " + (
/* 374 */           vectorSet.isEmpty() ? "[default]" : vectorSet.toString()) + " in build file " + this.antFile, 3);
/*     */       
/* 376 */       this.newProject.setUserProperty("ant.file", this.antFile);
/*     */       
/* 378 */       String thisAntFile = getProject().getProperty("ant.file");
/*     */ 
/*     */       
/* 381 */       if (thisAntFile != null && file.equals(getProject().resolveFile(thisAntFile)) && 
/* 382 */         getOwningTarget() != null && getOwningTarget().getName().isEmpty()) {
/* 383 */         if ("antcall".equals(getTaskName())) {
/* 384 */           throw new BuildException("antcall must not be used at the top level.");
/*     */         }
/*     */         
/* 387 */         throw new BuildException("%s task at the top level must not invoke its own build file.", new Object[] {
/*     */               
/* 389 */               getTaskName()
/*     */             });
/*     */       } 
/*     */       try {
/* 393 */         ProjectHelper.configureProject(this.newProject, file);
/* 394 */       } catch (BuildException ex) {
/* 395 */         throw ProjectHelper.addLocationToBuildException(ex, 
/* 396 */             getLocation());
/*     */       } 
/*     */       
/* 399 */       if (vectorSet.isEmpty()) {
/* 400 */         String defaultTarget = this.newProject.getDefaultTarget();
/* 401 */         if (defaultTarget != null) {
/* 402 */           vectorSet.add(defaultTarget);
/*     */         }
/*     */       } 
/*     */       
/* 406 */       if (this.newProject.getProperty("ant.file")
/* 407 */         .equals(getProject().getProperty("ant.file")) && 
/* 408 */         getOwningTarget() != null) {
/*     */         
/* 410 */         String owningTargetName = getOwningTarget().getName();
/*     */         
/* 412 */         if (vectorSet.contains(owningTargetName)) {
/* 413 */           throw new BuildException("%s task calling its own parent target.", new Object[] {
/*     */                 
/* 415 */                 getTaskName()
/*     */               });
/*     */         }
/* 418 */         Map<String, Target> targetsMap = getProject().getTargets();
/*     */         
/* 420 */         Objects.requireNonNull(targetsMap); if (vectorSet.stream().map(targetsMap::get)
/* 421 */           .filter(Objects::nonNull)
/* 422 */           .anyMatch(other -> other.dependsOn(owningTargetName))) {
/* 423 */           throw new BuildException("%s task calling a target that depends on its parent target '%s'.", new Object[] {
/*     */                 
/* 425 */                 getTaskName(), owningTargetName
/*     */               });
/*     */         }
/*     */       } 
/* 429 */       addReferences();
/*     */       
/* 431 */       if (!vectorSet.isEmpty() && (vectorSet.size() != 1 || vectorSet
/* 432 */         .get(0) == null || !((String)vectorSet.get(0)).isEmpty())) {
/* 433 */         BuildException be = null;
/*     */         try {
/* 435 */           log("Entering " + this.antFile + "...", 3);
/* 436 */           this.newProject.fireSubBuildStarted();
/* 437 */           this.newProject.executeTargets((Vector)vectorSet);
/* 438 */         } catch (BuildException ex) {
/*     */           
/* 440 */           be = ProjectHelper.addLocationToBuildException(ex, getLocation());
/* 441 */           throw be;
/*     */         } finally {
/* 443 */           log("Exiting " + this.antFile + ".", 3);
/* 444 */           this.newProject.fireSubBuildFinished((Throwable)be);
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 449 */       this.newProject = null;
/* 450 */       for (Property p : this.properties) {
/* 451 */         p.setProject(null);
/*     */       }
/*     */       
/* 454 */       if (this.output != null && this.out != null) {
/* 455 */         FileUtils.close(this.out);
/*     */       }
/* 457 */       this.dir = savedDir;
/* 458 */       this.antFile = savedAntFile;
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
/*     */   protected String getDefaultBuildFile() {
/* 472 */     return "build.xml";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void overrideProperties() throws BuildException {
/* 483 */     Set<String> set = new HashSet<>();
/* 484 */     for (int i = this.properties.size() - 1; i >= 0; i--) {
/* 485 */       Property p = this.properties.get(i);
/* 486 */       if (p.getName() != null && !p.getName().isEmpty()) {
/* 487 */         if (set.contains(p.getName())) {
/* 488 */           this.properties.remove(i);
/*     */         } else {
/* 490 */           set.add(p.getName());
/*     */         } 
/*     */       }
/*     */     } 
/* 494 */     this.properties.stream().peek(p -> p.setProject(this.newProject))
/* 495 */       .forEach(Property::execute);
/*     */     
/* 497 */     if (this.useNativeBasedir) {
/* 498 */       addAlmostAll(getProject().getInheritedProperties(), PropertyType.INHERITED);
/*     */     } else {
/*     */       
/* 501 */       getProject().copyInheritedProperties(this.newProject);
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
/*     */   private void addReferences() throws BuildException {
/* 514 */     Map<String, Object> thisReferences = new HashMap<>(getProject().getReferences());
/* 515 */     for (Reference ref : this.references) {
/* 516 */       String refid = ref.getRefId();
/* 517 */       if (refid == null) {
/* 518 */         throw new BuildException("the refid attribute is required for reference elements");
/*     */       }
/*     */       
/* 521 */       if (!thisReferences.containsKey(refid)) {
/* 522 */         log("Parent project doesn't contain any reference '" + refid + "'", 1);
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 528 */       thisReferences.remove(refid);
/* 529 */       String toRefid = ref.getToRefid();
/* 530 */       if (toRefid == null) {
/* 531 */         toRefid = refid;
/*     */       }
/* 533 */       copyReference(refid, toRefid);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 538 */     if (this.inheritRefs) {
/* 539 */       Map<String, Object> newReferences = this.newProject.getReferences();
/* 540 */       for (String key : thisReferences.keySet()) {
/* 541 */         if (newReferences.containsKey(key)) {
/*     */           continue;
/*     */         }
/* 544 */         copyReference(key, key);
/* 545 */         this.newProject.inheritIDReferences(getProject());
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
/*     */   private void copyReference(String oldKey, String newKey) {
/* 560 */     Object orig = getProject().getReference(oldKey);
/* 561 */     if (orig == null) {
/* 562 */       log("No object referenced by " + oldKey + ". Can't copy to " + newKey, 1);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 568 */     Class<?> c = orig.getClass();
/* 569 */     Object copy = orig;
/*     */     try {
/* 571 */       Method cloneM = c.getMethod("clone", new Class[0]);
/* 572 */       if (cloneM != null) {
/* 573 */         copy = cloneM.invoke(orig, new Object[0]);
/* 574 */         log("Adding clone of reference " + oldKey, 4);
/*     */       } 
/* 576 */     } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */     
/* 580 */     if (copy instanceof ProjectComponent) {
/* 581 */       ((ProjectComponent)copy).setProject(this.newProject);
/*     */     } else {
/*     */       
/*     */       try {
/* 585 */         Method setProjectM = c.getMethod("setProject", new Class[] { Project.class });
/* 586 */         if (setProjectM != null) {
/* 587 */           setProjectM.invoke(copy, new Object[] { this.newProject });
/*     */         }
/* 589 */       } catch (NoSuchMethodException noSuchMethodException) {
/*     */ 
/*     */       
/* 592 */       } catch (Exception e2) {
/* 593 */         throw new BuildException("Error setting new project instance for reference with id " + oldKey, e2, 
/*     */ 
/*     */             
/* 596 */             getLocation());
/*     */       } 
/*     */     } 
/* 599 */     this.newProject.addReference(newKey, copy);
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
/*     */   private void addAlmostAll(Map<?, ?> props, PropertyType type) {
/* 613 */     props.forEach((k, v) -> {
/*     */           String key = k.toString();
/*     */           if ("basedir".equals(key) || "ant.file".equals(key)) {
/*     */             return;
/*     */           }
/*     */           String value = v.toString();
/*     */           switch (type) {
/*     */             case PLAIN:
/*     */               if (this.newProject.getProperty(key) == null) {
/*     */                 this.newProject.setNewProperty(key, value);
/*     */               }
/*     */               break;
/*     */             case USER:
/*     */               this.newProject.setUserProperty(key, value);
/*     */               break;
/*     */             case INHERITED:
/*     */               this.newProject.setInheritedProperty(key, value);
/*     */               break;
/*     */           } 
/*     */         });
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
/*     */   public void setDir(File dir) {
/* 647 */     this.dir = dir;
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
/*     */   public void setAntfile(String antFile) {
/* 659 */     this.antFile = antFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(String targetToAdd) {
/* 668 */     if (targetToAdd.isEmpty()) {
/* 669 */       throw new BuildException("target attribute must not be empty");
/*     */     }
/* 671 */     this.targets.add(targetToAdd);
/* 672 */     this.targetAttributeSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(String outputFile) {
/* 682 */     this.output = outputFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Property createProperty() {
/* 691 */     Property p = new Property(true, getProject());
/* 692 */     p.setProject(getNewProject());
/* 693 */     p.setTaskName("property");
/* 694 */     this.properties.add(p);
/* 695 */     return p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addReference(Reference ref) {
/* 704 */     this.references.add(ref);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConfiguredTarget(TargetElement t) {
/* 713 */     if (this.targetAttributeSet) {
/* 714 */       throw new BuildException("nested target is incompatible with the target attribute");
/*     */     }
/*     */     
/* 717 */     String name = t.getName();
/* 718 */     if (name.isEmpty()) {
/* 719 */       throw new BuildException("target name must not be empty");
/*     */     }
/* 721 */     this.targets.add(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPropertyset(PropertySet ps) {
/* 731 */     this.propertySets.add(ps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Project getNewProject() {
/* 740 */     if (this.newProject == null) {
/* 741 */       reinit();
/*     */     }
/* 743 */     return this.newProject;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Ant() {}
/*     */ 
/*     */   
/*     */   public static class Reference
/*     */     extends org.apache.tools.ant.types.Reference
/*     */   {
/* 754 */     private String targetid = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setToRefid(String targetid) {
/* 763 */       this.targetid = targetid;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getToRefid() {
/* 773 */       return this.targetid;
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
/*     */   public static class TargetElement
/*     */   {
/*     */     private String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setName(String name) {
/* 797 */       this.name = name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getName() {
/* 805 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum PropertyType {
/* 810 */     PLAIN, INHERITED, USER;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Ant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */