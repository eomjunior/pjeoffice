/*     */ package org.apache.tools.ant.taskdefs.rmic;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.util.ClasspathUtils;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RmicAdapterFactory
/*     */ {
/*     */   public static final String ERROR_UNKNOWN_COMPILER = "Class not found: ";
/*     */   public static final String ERROR_NOT_RMIC_ADAPTER = "Class of unexpected Type: ";
/*     */   public static final String DEFAULT_COMPILER = "default";
/*     */   
/*     */   public static RmicAdapter getRmic(String rmicType, Task task) throws BuildException {
/*  68 */     return getRmic(rmicType, task, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RmicAdapter getRmic(String rmicType, Task task, Path classpath) throws BuildException {
/*  98 */     if ("default".equalsIgnoreCase(rmicType) || rmicType.isEmpty()) {
/*  99 */       if (KaffeRmic.isAvailable()) {
/* 100 */         rmicType = "kaffe";
/* 101 */       } else if (JavaEnvUtils.isAtLeastJavaVersion("9")) {
/* 102 */         rmicType = "forking";
/*     */       } else {
/* 104 */         rmicType = "sun";
/*     */       } 
/*     */     }
/* 107 */     if ("sun".equalsIgnoreCase(rmicType)) {
/* 108 */       return new SunRmic();
/*     */     }
/* 110 */     if ("kaffe".equalsIgnoreCase(rmicType)) {
/* 111 */       return new KaffeRmic();
/*     */     }
/* 113 */     if ("weblogic".equalsIgnoreCase(rmicType)) {
/* 114 */       return new WLRmic();
/*     */     }
/* 116 */     if ("forking".equalsIgnoreCase(rmicType)) {
/* 117 */       return new ForkingSunRmic();
/*     */     }
/* 119 */     if ("xnew".equalsIgnoreCase(rmicType)) {
/* 120 */       return new XNewRmic();
/*     */     }
/*     */     
/* 123 */     return resolveClassName(rmicType, (ClassLoader)task
/*     */         
/* 125 */         .getProject().createClassLoader(classpath));
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
/*     */   private static RmicAdapter resolveClassName(String className, ClassLoader loader) throws BuildException {
/* 140 */     return (RmicAdapter)ClasspathUtils.newInstance(className, 
/* 141 */         (loader != null) ? loader : 
/* 142 */         RmicAdapterFactory.class.getClassLoader(), RmicAdapter.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/rmic/RmicAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */