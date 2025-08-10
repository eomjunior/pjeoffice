/*     */ package org.apache.tools.ant.util;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScriptRunnerCreator
/*     */ {
/*     */   private static final String AUTO = "auto";
/*     */   private static final String UTIL_OPT = "org.apache.tools.ant.util.optional";
/*     */   private static final String BSF = "bsf";
/*     */   private static final String BSF_PACK = "org.apache.bsf";
/*     */   private static final String BSF_MANAGER = "org.apache.bsf.BSFManager";
/*     */   private static final String BSF_RUNNER = "org.apache.tools.ant.util.optional.ScriptRunner";
/*     */   private static final String JAVAX = "javax";
/*     */   private static final String JAVAX_MANAGER = "javax.script.ScriptEngineManager";
/*     */   private static final String JAVAX_RUNNER = "org.apache.tools.ant.util.optional.JavaxScriptRunner";
/*     */   private Project project;
/*     */   private String manager;
/*     */   private String language;
/*  44 */   private ClassLoader scriptLoader = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptRunnerCreator(Project project) {
/*  51 */     this.project = project;
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
/*     */   public synchronized ScriptRunnerBase createRunner(String manager, String language, ClassLoader classLoader) {
/*  64 */     this.manager = manager;
/*  65 */     this.language = language;
/*  66 */     this.scriptLoader = classLoader;
/*     */     
/*  68 */     if (language == null) {
/*  69 */       throw new BuildException("script language must be specified");
/*     */     }
/*  71 */     if (!manager.equals("auto") && !manager.equals("javax") && !manager.equals("bsf")) {
/*  72 */       throw new BuildException("Unsupported language prefix " + manager);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     ScriptRunnerBase ret = null;
/*  80 */     ret = createRunner("bsf", "org.apache.bsf.BSFManager", "org.apache.tools.ant.util.optional.ScriptRunner");
/*  81 */     if (ret == null) {
/*  82 */       ret = createRunner("javax", "javax.script.ScriptEngineManager", "org.apache.tools.ant.util.optional.JavaxScriptRunner");
/*     */     }
/*  84 */     if (ret != null) {
/*  85 */       return ret;
/*     */     }
/*  87 */     if ("javax".equals(manager)) {
/*  88 */       throw new BuildException("Unable to load the script engine manager (javax.script.ScriptEngineManager)");
/*     */     }
/*     */     
/*  91 */     if ("bsf".equals(manager)) {
/*  92 */       throw new BuildException("Unable to load the BSF script engine manager (org.apache.bsf.BSFManager)");
/*     */     }
/*     */     
/*  95 */     throw new BuildException("Unable to load a script engine manager (org.apache.bsf.BSFManager or javax.script.ScriptEngineManager)");
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
/*     */   private ScriptRunnerBase createRunner(String checkManager, String managerClass, String runnerClass) {
/* 112 */     ScriptRunnerBase runner = null;
/* 113 */     if (!this.manager.equals("auto") && !this.manager.equals(checkManager)) {
/* 114 */       return null;
/*     */     }
/* 116 */     if (managerClass.equals("org.apache.bsf.BSFManager")) {
/* 117 */       if (this.scriptLoader.getResource(LoaderUtils.classNameToResource(managerClass)) == null) {
/* 118 */         return null;
/*     */       }
/* 120 */       (new ScriptFixBSFPath()).fixClassLoader(this.scriptLoader, this.language);
/*     */     } else {
/*     */       try {
/* 123 */         Class.forName(managerClass, true, this.scriptLoader);
/* 124 */       } catch (Exception ex) {
/* 125 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 130 */       runner = Class.forName(runnerClass, true, this.scriptLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
/* 131 */       runner.setProject(this.project);
/* 132 */     } catch (Exception ex) {
/* 133 */       throw ReflectUtil.toBuildException(ex);
/*     */     } 
/* 135 */     runner.setLanguage(this.language);
/* 136 */     runner.setScriptClassLoader(this.scriptLoader);
/* 137 */     return runner;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/ScriptRunnerCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */