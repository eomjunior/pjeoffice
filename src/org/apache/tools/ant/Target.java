/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.property.LocalProperties;
/*     */ import org.apache.tools.ant.taskdefs.condition.And;
/*     */ import org.apache.tools.ant.taskdefs.condition.Condition;
/*     */ import org.apache.tools.ant.taskdefs.condition.Or;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Target
/*     */   implements TaskContainer
/*     */ {
/*     */   private String name;
/*  45 */   private String ifString = "";
/*     */ 
/*     */   
/*  48 */   private String unlessString = "";
/*     */ 
/*     */   
/*     */   private Condition ifCondition;
/*     */   
/*     */   private Condition unlessCondition;
/*     */   
/*  55 */   private List<String> dependencies = null;
/*     */ 
/*     */   
/*  58 */   private List<Object> children = new ArrayList();
/*     */ 
/*     */   
/*  61 */   private Location location = Location.UNKNOWN_LOCATION;
/*     */ 
/*     */   
/*     */   private Project project;
/*     */ 
/*     */   
/*  67 */   private String description = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Target(Target other) {
/*  79 */     this.name = other.name;
/*  80 */     this.ifString = other.ifString;
/*  81 */     this.unlessString = other.unlessString;
/*  82 */     this.ifCondition = other.ifCondition;
/*  83 */     this.unlessCondition = other.unlessCondition;
/*  84 */     this.dependencies = other.dependencies;
/*  85 */     this.location = other.location;
/*  86 */     this.project = other.project;
/*  87 */     this.description = other.description;
/*     */     
/*  89 */     this.children = other.children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProject(Project project) {
/*  99 */     this.project = project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/* 109 */     return this.project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocation(Location location) {
/* 119 */     this.location = location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Location getLocation() {
/* 129 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDepends(String depS) {
/* 140 */     for (String dep : parseDepends(depS, getName(), "depends")) {
/* 141 */       addDependency(dep);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> parseDepends(String depends, String targetName, String attributeName) {
/* 148 */     if (depends.isEmpty()) {
/* 149 */       return new ArrayList<>();
/*     */     }
/*     */     
/* 152 */     List<String> list = new ArrayList<>();
/* 153 */     StringTokenizer tok = new StringTokenizer(depends, ",", true);
/* 154 */     while (tok.hasMoreTokens()) {
/* 155 */       String token = tok.nextToken().trim();
/*     */ 
/*     */       
/* 158 */       if (token.isEmpty() || ",".equals(token)) {
/* 159 */         throw new BuildException("Syntax Error: " + attributeName + " attribute of target \"" + targetName + "\" contains an empty string.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 166 */       list.add(token);
/*     */ 
/*     */ 
/*     */       
/* 170 */       if (tok.hasMoreTokens()) {
/* 171 */         token = tok.nextToken();
/* 172 */         if (!tok.hasMoreTokens() || !",".equals(token)) {
/* 173 */           throw new BuildException("Syntax Error: " + attributeName + " attribute for target \"" + targetName + "\" ends with a \",\" character");
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 182 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 191 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 201 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTask(Task task) {
/* 210 */     this.children.add(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDataType(RuntimeConfigurable r) {
/* 220 */     this.children.add(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Task[] getTasks() {
/* 229 */     List<Task> tasks = new ArrayList<>(this.children.size());
/* 230 */     for (Object o : this.children) {
/* 231 */       if (o instanceof Task) {
/* 232 */         tasks.add((Task)o);
/*     */       }
/*     */     } 
/* 235 */     return tasks.<Task>toArray(new Task[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addDependency(String dependency) {
/* 245 */     if (this.dependencies == null) {
/* 246 */       this.dependencies = new ArrayList<>(2);
/*     */     }
/* 248 */     this.dependencies.add(dependency);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Enumeration<String> getDependencies() {
/* 257 */     return (this.dependencies == null) ? Collections.<String>emptyEnumeration() : 
/* 258 */       Collections.<String>enumeration(this.dependencies);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean dependsOn(String other) {
/* 268 */     Project p = getProject();
/* 269 */     Hashtable<String, Target> t = (p == null) ? null : p.getTargets();
/* 270 */     return (p != null && p.topoSort(getName(), t, false).contains(t.get(other)));
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
/*     */   public void setIf(String property) {
/* 287 */     this.ifString = (property == null) ? "" : property;
/* 288 */     setIf(() -> {
/*     */           PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(getProject());
/*     */           Object o = propertyHelper.parseProperties(this.ifString);
/*     */           return propertyHelper.testIfCondition(o);
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
/*     */   public String getIf() {
/* 304 */     return this.ifString.isEmpty() ? null : this.ifString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIf(Condition condition) {
/* 314 */     if (this.ifCondition == null) {
/* 315 */       this.ifCondition = condition;
/*     */     } else {
/* 317 */       And andCondition = new And();
/* 318 */       andCondition.setProject(getProject());
/* 319 */       andCondition.setLocation(getLocation());
/* 320 */       andCondition.add(this.ifCondition);
/* 321 */       andCondition.add(condition);
/* 322 */       this.ifCondition = (Condition)andCondition;
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
/*     */   public void setUnless(String property) {
/* 340 */     this.unlessString = (property == null) ? "" : property;
/* 341 */     setUnless(() -> {
/*     */           PropertyHelper propertyHelper = PropertyHelper.getPropertyHelper(getProject());
/*     */           Object o = propertyHelper.parseProperties(this.unlessString);
/*     */           return !propertyHelper.testUnlessCondition(o);
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
/*     */   public String getUnless() {
/* 357 */     return this.unlessString.isEmpty() ? null : this.unlessString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnless(Condition condition) {
/* 367 */     if (this.unlessCondition == null) {
/* 368 */       this.unlessCondition = condition;
/*     */     } else {
/* 370 */       Or orCondition = new Or();
/* 371 */       orCondition.setProject(getProject());
/* 372 */       orCondition.setLocation(getLocation());
/* 373 */       orCondition.add(this.unlessCondition);
/* 374 */       orCondition.add(condition);
/* 375 */       this.unlessCondition = (Condition)orCondition;
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
/*     */   public void setDescription(String description) {
/* 387 */     this.description = description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 397 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 408 */     return this.name;
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
/*     */   public void execute() throws BuildException {
/* 428 */     if (this.ifCondition != null && !this.ifCondition.eval()) {
/* 429 */       this.project.log(this, "Skipped because property '" + this.project.replaceProperties(this.ifString) + "' not set.", 3);
/*     */       
/*     */       return;
/*     */     } 
/* 433 */     if (this.unlessCondition != null && this.unlessCondition.eval()) {
/* 434 */       this.project.log(this, "Skipped because property '" + this.project
/* 435 */           .replaceProperties(this.unlessString) + "' set.", 3);
/*     */       return;
/*     */     } 
/* 438 */     LocalProperties localProperties = LocalProperties.get(getProject());
/* 439 */     localProperties.enterScope();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 445 */       for (int i = 0; i < this.children.size(); i++) {
/* 446 */         Object o = this.children.get(i);
/* 447 */         if (o instanceof Task) {
/* 448 */           Task task = (Task)o;
/* 449 */           task.perform();
/*     */         } else {
/* 451 */           ((RuntimeConfigurable)o).maybeConfigure(this.project);
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 455 */       localProperties.exitScope();
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
/*     */   public final void performTasks() {
/* 467 */     RuntimeException thrown = null;
/* 468 */     this.project.fireTargetStarted(this);
/*     */     try {
/* 470 */       execute();
/* 471 */     } catch (RuntimeException exc) {
/* 472 */       thrown = exc;
/* 473 */       throw exc;
/*     */     } finally {
/* 475 */       this.project.fireTargetFinished(this, thrown);
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
/*     */   void replaceChild(Task el, RuntimeConfigurable o) {
/*     */     int index;
/* 489 */     while ((index = this.children.indexOf(el)) >= 0) {
/* 490 */       this.children.set(index, o);
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
/*     */   void replaceChild(Task el, Task o) {
/*     */     int index;
/* 504 */     while ((index = this.children.indexOf(el)) >= 0)
/* 505 */       this.children.set(index, o); 
/*     */   }
/*     */   
/*     */   public Target() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/Target.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */