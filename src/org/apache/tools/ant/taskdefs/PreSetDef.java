/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import org.apache.tools.ant.AntTypeDefinition;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ComponentHelper;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.TaskContainer;
/*     */ import org.apache.tools.ant.UnknownElement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PreSetDef
/*     */   extends AntlibDefinition
/*     */   implements TaskContainer
/*     */ {
/*     */   private UnknownElement nestedTask;
/*     */   private String name;
/*     */   
/*     */   public void setName(String name) {
/*  52 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addTask(Task nestedTask) {
/*  61 */     if (this.nestedTask != null) {
/*  62 */       throw new BuildException("Only one nested element allowed");
/*     */     }
/*  64 */     if (!(nestedTask instanceof UnknownElement)) {
/*  65 */       throw new BuildException("addTask called with a task that is not an unknown element");
/*     */     }
/*     */     
/*  68 */     this.nestedTask = (UnknownElement)nestedTask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() {
/*  77 */     if (this.nestedTask == null) {
/*  78 */       throw new BuildException("Missing nested element");
/*     */     }
/*  80 */     if (this.name == null) {
/*  81 */       throw new BuildException("Name not specified");
/*     */     }
/*  83 */     this.name = ProjectHelper.genComponentName(getURI(), this.name);
/*     */     
/*  85 */     ComponentHelper helper = ComponentHelper.getComponentHelper(
/*  86 */         getProject());
/*     */     
/*  88 */     String componentName = ProjectHelper.genComponentName(this.nestedTask
/*  89 */         .getNamespace(), this.nestedTask.getTag());
/*     */     
/*  91 */     AntTypeDefinition def = helper.getDefinition(componentName);
/*  92 */     if (def == null) {
/*  93 */       throw new BuildException("Unable to find typedef %s", new Object[] { componentName });
/*     */     }
/*     */     
/*  96 */     PreSetDefinition newDef = new PreSetDefinition(def, this.nestedTask);
/*     */     
/*  98 */     newDef.setName(this.name);
/*     */     
/* 100 */     helper.addDataTypeDefinition(newDef);
/* 101 */     log("defining preset " + this.name, 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class PreSetDefinition
/*     */     extends AntTypeDefinition
/*     */   {
/*     */     private AntTypeDefinition parent;
/*     */ 
/*     */ 
/*     */     
/*     */     private UnknownElement element;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PreSetDefinition(AntTypeDefinition parent, UnknownElement el) {
/* 120 */       if (parent instanceof PreSetDefinition) {
/* 121 */         PreSetDefinition p = (PreSetDefinition)parent;
/* 122 */         el.applyPreSet(p.element);
/* 123 */         parent = p.parent;
/*     */       } 
/* 125 */       this.parent = parent;
/* 126 */       this.element = el;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClass(Class<?> clazz) {
/* 136 */       throw new BuildException("Not supported");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClassName(String className) {
/* 146 */       throw new BuildException("Not supported");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getClassName() {
/* 155 */       return this.parent.getClassName();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setAdapterClass(Class<?> adapterClass) {
/* 165 */       throw new BuildException("Not supported");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setAdaptToClass(Class<?> adaptToClass) {
/* 175 */       throw new BuildException("Not supported");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setClassLoader(ClassLoader classLoader) {
/* 186 */       throw new BuildException("Not supported");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ClassLoader getClassLoader() {
/* 195 */       return this.parent.getClassLoader();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> getExposedClass(Project project) {
/* 205 */       return this.parent.getExposedClass(project);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<?> getTypeClass(Project project) {
/* 215 */       return this.parent.getTypeClass(project);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void checkClass(Project project) {
/* 225 */       this.parent.checkClass(project);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object createObject(Project project) {
/* 236 */       return this.parent.create(project);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public UnknownElement getPreSets() {
/* 245 */       return this.element;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object create(Project project) {
/* 257 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean sameDefinition(AntTypeDefinition other, Project project) {
/* 269 */       return (other != null && other.getClass() == getClass() && this.parent != null && this.parent
/* 270 */         .sameDefinition(((PreSetDefinition)other).parent, project) && this.element
/* 271 */         .similar(((PreSetDefinition)other).element));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean similarDefinition(AntTypeDefinition other, Project project) {
/* 284 */       return (other != null && other.getClass().getName().equals(
/* 285 */           getClass().getName()) && this.parent != null && this.parent
/* 286 */         .similarDefinition(((PreSetDefinition)other).parent, project) && this.element
/* 287 */         .similar(((PreSetDefinition)other).element));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/PreSetDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */