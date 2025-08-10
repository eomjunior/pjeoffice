/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.launch.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LoaderUtils
/*     */ {
/*  34 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setContextClassLoader(ClassLoader loader) {
/*  43 */     Thread currentThread = Thread.currentThread();
/*  44 */     currentThread.setContextClassLoader(loader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassLoader getContextClassLoader() {
/*  55 */     Thread currentThread = Thread.currentThread();
/*  56 */     return currentThread.getContextClassLoader();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isContextLoaderAvailable() {
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static File normalizeSource(File source) {
/*  77 */     if (source != null) {
/*     */       try {
/*  79 */         source = FILE_UTILS.normalize(source.getAbsolutePath());
/*  80 */       } catch (BuildException buildException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  85 */     return source;
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
/*     */   public static File getClassSource(Class<?> c) {
/*  98 */     return normalizeSource(Locator.getClassSource(c));
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
/*     */   public static File getResourceSource(ClassLoader c, String resource) {
/* 113 */     if (c == null) {
/* 114 */       c = LoaderUtils.class.getClassLoader();
/*     */     }
/* 116 */     return normalizeSource(Locator.getResourceSource(c, resource));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String classNameToResource(String className) {
/* 126 */     return className.replace('.', '/') + ".class";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean classExists(ClassLoader loader, String className) {
/* 137 */     return (loader.getResource(classNameToResource(className)) != null);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/LoaderUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */