/*     */ package org.apache.tools.ant.taskdefs.rmic;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WLRmic
/*     */   extends DefaultRmicAdapter
/*     */ {
/*     */   public static final String WLRMIC_CLASSNAME = "weblogic.rmic";
/*     */   public static final String COMPILER_NAME = "weblogic";
/*     */   public static final String ERROR_NO_WLRMIC_ON_CLASSPATH = "Cannot use WebLogic rmic, as it is not available. Add it to Ant's classpath with the -lib option";
/*     */   public static final String ERROR_WLRMIC_FAILED = "Error starting WebLogic rmic: ";
/*     */   public static final String WL_RMI_STUB_SUFFIX = "_WLStub";
/*     */   public static final String WL_RMI_SKEL_SUFFIX = "_WLSkel";
/*     */   public static final String UNSUPPORTED_STUB_OPTION = "Unsupported stub option: ";
/*     */   
/*     */   protected boolean areIiopAndIdlSupported() {
/*  61 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws BuildException {
/*  71 */     getRmic().log("Using WebLogic rmic", 3);
/*  72 */     Commandline cmd = setupRmicCommand(new String[] { "-noexit" });
/*     */     
/*  74 */     AntClassLoader loader = null;
/*     */     
/*     */     try {
/*     */       Class<?> c;
/*  78 */       if (getRmic().getClasspath() == null) {
/*  79 */         c = Class.forName("weblogic.rmic");
/*     */       } else {
/*     */         
/*  82 */         loader = getRmic().getProject().createClassLoader(getRmic().getClasspath());
/*  83 */         c = Class.forName("weblogic.rmic", true, (ClassLoader)loader);
/*     */       } 
/*  85 */       Method doRmic = c.getMethod("main", new Class[] { String[].class });
/*  86 */       doRmic.invoke(null, new Object[] { cmd.getArguments() });
/*  87 */       return true;
/*  88 */     } catch (ClassNotFoundException ex) {
/*  89 */       throw new BuildException("Cannot use WebLogic rmic, as it is not available. Add it to Ant's classpath with the -lib option", getRmic().getLocation());
/*  90 */     } catch (Exception ex) {
/*  91 */       if (ex instanceof BuildException) {
/*  92 */         throw (BuildException)ex;
/*     */       }
/*  94 */       throw new BuildException("Error starting WebLogic rmic: ", ex, 
/*  95 */           getRmic().getLocation());
/*     */     } finally {
/*     */       
/*  98 */       if (loader != null) {
/*  99 */         loader.cleanup();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStubClassSuffix() {
/* 110 */     return "_WLStub";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSkelClassSuffix() {
/* 119 */     return "_WLSkel";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] preprocessCompilerArgs(String[] compilerArgs) {
/* 130 */     return filterJvmCompilerArgs(compilerArgs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String addStubVersionOptions() {
/* 141 */     String stubVersion = getRmic().getStubVersion();
/* 142 */     if (null != stubVersion) {
/* 143 */       getRmic().log("Unsupported stub option: " + stubVersion, 1);
/*     */     }
/*     */     
/* 146 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/rmic/WLRmic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */