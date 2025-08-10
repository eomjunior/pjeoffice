/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ import org.apache.tools.ant.util.ClasspathUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DefBase
/*     */   extends AntlibDefinition
/*     */ {
/*     */   private ClassLoader createdLoader;
/*     */   private ClasspathUtils.Delegate cpDelegate;
/*     */   
/*     */   protected boolean hasCpDelegate() {
/*  45 */     return (this.cpDelegate != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setReverseLoader(boolean reverseLoader) {
/*  57 */     getDelegate().setReverseLoader(reverseLoader);
/*  58 */     log("The reverseloader attribute is DEPRECATED. It will be removed", 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/*  66 */     return getDelegate().getClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReverseLoader() {
/*  73 */     return getDelegate().isReverseLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLoaderId() {
/*  81 */     return getDelegate().getClassLoadId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClasspathId() {
/*  89 */     return getDelegate().getClassLoadId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path classpath) {
/*  98 */     getDelegate().setClasspath(classpath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 107 */     return getDelegate().createClasspath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 116 */     getDelegate().setClasspathref(r);
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
/*     */   public void setLoaderRef(Reference r) {
/* 132 */     getDelegate().setLoaderRef(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ClassLoader createLoader() {
/* 140 */     if (getAntlibClassLoader() != null && this.cpDelegate == null) {
/* 141 */       return getAntlibClassLoader();
/*     */     }
/* 143 */     if (this.createdLoader == null) {
/* 144 */       this.createdLoader = getDelegate().getClassLoader();
/*     */ 
/*     */ 
/*     */       
/* 148 */       ((AntClassLoader)this.createdLoader)
/* 149 */         .addSystemPackageRoot("org.apache.tools.ant");
/*     */     } 
/* 151 */     return this.createdLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() throws BuildException {
/* 160 */     super.init();
/*     */   }
/*     */   
/*     */   private ClasspathUtils.Delegate getDelegate() {
/* 164 */     if (this.cpDelegate == null) {
/* 165 */       this.cpDelegate = ClasspathUtils.getDelegate((ProjectComponent)this);
/*     */     }
/* 167 */     return this.cpDelegate;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/DefBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */