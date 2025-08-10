/*     */ package org.apache.tools.ant.taskdefs.optional.javah;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
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
/*     */ public class JavahAdapterFactory
/*     */ {
/*     */   public static String getDefault() {
/*  43 */     if (JavaEnvUtils.isKaffe()) {
/*  44 */       return "kaffeh";
/*     */     }
/*  46 */     if (JavaEnvUtils.isGij()) {
/*  47 */       return "gcjh";
/*     */     }
/*  49 */     return "forking";
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
/*     */   public static JavahAdapter getAdapter(String choice, ProjectComponent log) throws BuildException {
/*  65 */     return getAdapter(choice, log, null);
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
/*     */   public static JavahAdapter getAdapter(String choice, ProjectComponent log, Path classpath) throws BuildException {
/*  85 */     if ((JavaEnvUtils.isKaffe() && choice == null) || "kaffeh"
/*  86 */       .equals(choice)) {
/*  87 */       return new Kaffeh();
/*     */     }
/*  89 */     if ((JavaEnvUtils.isGij() && choice == null) || "gcjh"
/*  90 */       .equals(choice)) {
/*  91 */       return new Gcjh();
/*     */     }
/*  93 */     if (JavaEnvUtils.isAtLeastJavaVersion("10") && (choice == null || "forking"
/*  94 */       .equals(choice))) {
/*  95 */       throw new BuildException("javah does not exist under Java 10 and higher, use the javac task with nativeHeaderDir instead");
/*     */     }
/*     */     
/*  98 */     if ("forking".equals(choice)) {
/*  99 */       return new ForkingJavah();
/*     */     }
/* 101 */     if ("sun".equals(choice)) {
/* 102 */       return new SunJavah();
/*     */     }
/* 104 */     if (choice != null) {
/* 105 */       return resolveClassName(choice, (ClassLoader)log
/*     */           
/* 107 */           .getProject()
/* 108 */           .createClassLoader(classpath));
/*     */     }
/* 110 */     return new ForkingJavah();
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
/*     */   private static JavahAdapter resolveClassName(String className, ClassLoader loader) throws BuildException {
/* 125 */     return (JavahAdapter)ClasspathUtils.newInstance(className, 
/* 126 */         (loader != null) ? loader : 
/* 127 */         JavahAdapterFactory.class.getClassLoader(), JavahAdapter.class);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/javah/JavahAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */