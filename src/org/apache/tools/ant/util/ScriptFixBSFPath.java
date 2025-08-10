/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptFixBSFPath
/*     */ {
/*     */   private static final String UTIL_OPTIONAL_PACKAGE = "org.apache.tools.ant.util.optional";
/*     */   private static final String BSF_PACKAGE = "org.apache.bsf";
/*     */   private static final String BSF_MANAGER = "org.apache.bsf.BSFManager";
/*     */   private static final String BSF_SCRIPT_RUNNER = "org.apache.tools.ant.util.optional.ScriptRunner";
/*  48 */   private static final String[] BSF_LANGUAGES = new String[] { "js", "org.mozilla.javascript.Scriptable", "javascript", "org.mozilla.javascript.Scriptable", "jacl", "tcl.lang.Interp", "netrexx", "netrexx.lang.Rexx", "nrx", "netrexx.lang.Rexx", "jython", "org.python.core.Py", "py", "org.python.core.Py", "xslt", "org.apache.xpath.objects.XObject" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private static final Map<String, String> BSF_LANGUAGE_MAP = new HashMap<>();
/*     */   static {
/*  62 */     for (int i = 0; i < BSF_LANGUAGES.length; i += 2) {
/*  63 */       BSF_LANGUAGE_MAP.put(BSF_LANGUAGES[i], BSF_LANGUAGES[i + 1]);
/*     */     }
/*     */   }
/*     */   
/*     */   private File getClassSource(ClassLoader loader, String className) {
/*  68 */     return LoaderUtils.getResourceSource(loader, 
/*     */         
/*  70 */         LoaderUtils.classNameToResource(className));
/*     */   }
/*     */   
/*     */   private File getClassSource(String className) {
/*  74 */     return getClassSource(getClass().getClassLoader(), className);
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
/*     */   public void fixClassLoader(ClassLoader loader, String language) {
/*  98 */     if (loader == getClass().getClassLoader() || !(loader instanceof AntClassLoader)) {
/*     */       return;
/*     */     }
/*     */     
/* 102 */     ClassLoader myLoader = getClass().getClassLoader();
/* 103 */     AntClassLoader fixLoader = (AntClassLoader)loader;
/*     */ 
/*     */     
/* 106 */     File bsfSource = getClassSource("org.apache.bsf.BSFManager");
/*     */ 
/*     */ 
/*     */     
/* 110 */     boolean needMoveRunner = (bsfSource == null);
/*     */ 
/*     */     
/* 113 */     String languageClassName = BSF_LANGUAGE_MAP.get(language);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     boolean needMoveBsf = (bsfSource != null && languageClassName != null && !LoaderUtils.classExists(myLoader, languageClassName) && LoaderUtils.classExists(loader, languageClassName));
/*     */ 
/*     */     
/* 123 */     needMoveRunner = (needMoveRunner || needMoveBsf);
/*     */ 
/*     */     
/* 126 */     if (bsfSource == null) {
/* 127 */       bsfSource = getClassSource(loader, "org.apache.bsf.BSFManager");
/*     */     }
/*     */     
/* 130 */     if (bsfSource == null) {
/* 131 */       throw new BuildException("Unable to find BSF classes for scripting");
/*     */     }
/*     */ 
/*     */     
/* 135 */     if (needMoveBsf) {
/* 136 */       fixLoader.addPathComponent(bsfSource);
/* 137 */       fixLoader.addLoaderPackageRoot("org.apache.bsf");
/*     */     } 
/*     */     
/* 140 */     if (needMoveRunner) {
/* 141 */       fixLoader.addPathComponent(
/* 142 */           LoaderUtils.getResourceSource((ClassLoader)fixLoader, 
/*     */             
/* 144 */             LoaderUtils.classNameToResource("org.apache.tools.ant.util.optional.ScriptRunner")));
/* 145 */       fixLoader.addLoaderPackageRoot("org.apache.tools.ant.util.optional");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ScriptFixBSFPath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */