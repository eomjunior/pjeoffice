/*     */ package org.apache.tools.ant.taskdefs.rmic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.LogOutputStream;
/*     */ import org.apache.tools.ant.types.Commandline;
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
/*     */ public class SunRmic
/*     */   extends DefaultRmicAdapter
/*     */ {
/*     */   public static final String RMIC_CLASSNAME = "sun.rmi.rmic.Main";
/*     */   public static final String COMPILER_NAME = "sun";
/*     */   public static final String RMIC_EXECUTABLE = "rmic";
/*     */   public static final String ERROR_NO_RMIC_ON_CLASSPATH = "Cannot use SUN rmic, as it is not available.  A common solution is to set the environment variable JAVA_HOME";
/*     */   public static final String ERROR_NO_RMIC_ON_CLASSPATH_JAVA_9 = "Cannot use SUN rmic, as it is not available.  The class we try to use is part of the jdk.rmic module which may not be. Please use the 'forking' compiler for JDK 9+";
/*     */   public static final String ERROR_RMIC_FAILED = "Error starting SUN rmic: ";
/*     */   
/*     */   public boolean execute() throws BuildException {
/*  68 */     getRmic().log("Using SUN rmic compiler", 3);
/*  69 */     Commandline cmd = setupRmicCommand();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  74 */     LogOutputStream logstr = new LogOutputStream((Task)getRmic(), 1);
/*     */     
/*  76 */     boolean success = false;
/*     */     try {
/*  78 */       Class<?> c = Class.forName("sun.rmi.rmic.Main");
/*     */       
/*  80 */       Constructor<?> cons = c.getConstructor(new Class[] { OutputStream.class, String.class });
/*  81 */       Object rmic = cons.newInstance(new Object[] { logstr, "rmic" });
/*     */       
/*  83 */       Method doRmic = c.getMethod("compile", new Class[] { String[].class });
/*     */       
/*  85 */       boolean ok = Boolean.TRUE.equals(doRmic.invoke(rmic, new Object[] { cmd.getArguments() }));
/*  86 */       success = true;
/*  87 */       return ok;
/*  88 */     } catch (ClassNotFoundException ex) {
/*  89 */       if (JavaEnvUtils.isAtLeastJavaVersion("9")) {
/*  90 */         throw new BuildException("Cannot use SUN rmic, as it is not available.  The class we try to use is part of the jdk.rmic module which may not be. Please use the 'forking' compiler for JDK 9+", 
/*  91 */             getRmic().getLocation());
/*     */       }
/*  93 */       throw new BuildException("Cannot use SUN rmic, as it is not available.  A common solution is to set the environment variable JAVA_HOME", 
/*  94 */           getRmic().getLocation());
/*  95 */     } catch (Exception ex) {
/*  96 */       if (ex instanceof BuildException) {
/*  97 */         throw (BuildException)ex;
/*     */       }
/*  99 */       throw new BuildException("Error starting SUN rmic: ", ex, 
/* 100 */           getRmic().getLocation());
/*     */     } finally {
/*     */       try {
/* 103 */         logstr.close();
/* 104 */       } catch (IOException e) {
/*     */ 
/*     */         
/* 107 */         if (success) {
/* 108 */           throw new BuildException(e);
/*     */         }
/*     */       } 
/*     */     } 
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
/* 122 */     return filterJvmCompilerArgs(compilerArgs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/rmic/SunRmic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */