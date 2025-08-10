/*     */ package org.apache.tools.ant.types.resources.selectors;
/*     */ 
/*     */ import org.apache.tools.ant.AntTypeDefinition;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ComponentHelper;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.ProjectHelper;
/*     */ import org.apache.tools.ant.types.Resource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstanceOf
/*     */   implements ResourceSelector
/*     */ {
/*     */   private static final String ONE_ONLY = "Exactly one of class|type must be set.";
/*     */   private Project project;
/*     */   private Class<?> clazz;
/*     */   private String type;
/*     */   private String uri;
/*     */   
/*     */   public void setProject(Project p) {
/*  45 */     this.project = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClass(Class<?> c) {
/*  53 */     if (this.clazz != null) {
/*  54 */       throw new BuildException("The class attribute has already been set.");
/*     */     }
/*  56 */     this.clazz = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(String s) {
/*  64 */     this.type = s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setURI(String u) {
/*  72 */     this.uri = u;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getCheckClass() {
/*  80 */     return this.clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getType() {
/*  88 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getURI() {
/*  96 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelected(Resource r) {
/* 106 */     if (((this.clazz == null) ? true : false) == ((this.type == null) ? true : false)) {
/* 107 */       throw new BuildException("Exactly one of class|type must be set.");
/*     */     }
/* 109 */     Class<?> c = this.clazz;
/* 110 */     if (this.type != null) {
/* 111 */       if (this.project == null) {
/* 112 */         throw new BuildException("No project set for InstanceOf ResourceSelector; the type attribute is invalid.");
/*     */       }
/*     */ 
/*     */       
/* 116 */       AntTypeDefinition d = ComponentHelper.getComponentHelper(this.project).getDefinition(ProjectHelper.genComponentName(this.uri, this.type));
/* 117 */       if (d == null) {
/* 118 */         throw new BuildException("type %s not found.", new Object[] { this.type });
/*     */       }
/*     */       try {
/* 121 */         c = d.innerGetTypeClass();
/* 122 */       } catch (ClassNotFoundException e) {
/* 123 */         throw new BuildException(e);
/*     */       } 
/*     */     } 
/* 126 */     return c.isAssignableFrom(r.getClass());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/InstanceOf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */