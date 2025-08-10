/*     */ package org.apache.tools.ant.taskdefs.optional.native2ascii;
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
/*     */ public class Native2AsciiAdapterFactory
/*     */ {
/*     */   public static String getDefault() {
/*  43 */     if (shouldUseKaffe()) {
/*  44 */       return "kaffe";
/*     */     }
/*  46 */     return "builtin";
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
/*     */   public static Native2AsciiAdapter getAdapter(String choice, ProjectComponent log) throws BuildException {
/*  62 */     return getAdapter(choice, log, null);
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
/*     */   public static Native2AsciiAdapter getAdapter(String choice, ProjectComponent log, Path classpath) throws BuildException {
/*  82 */     if ((shouldUseKaffe() && choice == null) || "kaffe"
/*  83 */       .equals(choice))
/*  84 */       return new KaffeNative2Ascii(); 
/*  85 */     if ("sun".equals(choice))
/*  86 */       return new SunNative2Ascii(); 
/*  87 */     if ("builtin".equals(choice))
/*  88 */       return new BuiltinNative2Ascii(); 
/*  89 */     if (choice != null) {
/*  90 */       return resolveClassName(choice, (ClassLoader)log
/*     */           
/*  92 */           .getProject()
/*  93 */           .createClassLoader(classpath));
/*     */     }
/*     */     
/*  96 */     return new BuiltinNative2Ascii();
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
/*     */   private static Native2AsciiAdapter resolveClassName(String className, ClassLoader loader) throws BuildException {
/* 111 */     return (Native2AsciiAdapter)ClasspathUtils.newInstance(className, 
/* 112 */         (loader != null) ? loader : 
/* 113 */         Native2AsciiAdapterFactory.class.getClassLoader(), Native2AsciiAdapter.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean shouldUseKaffe() {
/* 118 */     return (JavaEnvUtils.isKaffe() || JavaEnvUtils.isClasspathBased());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/native2ascii/Native2AsciiAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */