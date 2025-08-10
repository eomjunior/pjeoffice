/*     */ package org.apache.tools.ant.taskdefs.optional.jsp.compilers;
/*     */ 
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.optional.jsp.Jasper41Mangler;
/*     */ import org.apache.tools.ant.taskdefs.optional.jsp.JspMangler;
/*     */ import org.apache.tools.ant.taskdefs.optional.jsp.JspNameMangler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JspCompilerAdapterFactory
/*     */ {
/*     */   public static JspCompilerAdapter getCompiler(String compilerType, Task task) throws BuildException {
/*  56 */     return getCompiler(compilerType, task, task
/*     */         
/*  58 */         .getProject().createClassLoader(null));
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
/*     */   public static JspCompilerAdapter getCompiler(String compilerType, Task task, AntClassLoader loader) throws BuildException {
/*  83 */     if ("jasper".equalsIgnoreCase(compilerType))
/*     */     {
/*  85 */       return new JasperC((JspMangler)new JspNameMangler());
/*     */     }
/*  87 */     if ("jasper41".equalsIgnoreCase(compilerType))
/*     */     {
/*  89 */       return new JasperC((JspMangler)new Jasper41Mangler());
/*     */     }
/*  91 */     return resolveClassName(compilerType, loader);
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
/*     */   private static JspCompilerAdapter resolveClassName(String className, AntClassLoader classloader) throws BuildException {
/*     */     try {
/* 107 */       Class<? extends JspCompilerAdapter> c = classloader.findClass(className).asSubclass(JspCompilerAdapter.class);
/* 108 */       return c.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 109 */     } catch (ClassNotFoundException cnfe) {
/* 110 */       throw new BuildException(className + " can't be found.", cnfe);
/* 111 */     } catch (ClassCastException cce) {
/* 112 */       throw new BuildException(className + " isn't the classname of a compiler adapter.", cce);
/*     */     }
/* 114 */     catch (Throwable t) {
/*     */       
/* 116 */       throw new BuildException(className + " caused an interesting exception.", t);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jsp/compilers/JspCompilerAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */