/*     */ package org.apache.tools.ant.taskdefs.compilers;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ public final class CompilerAdapterFactory
/*     */ {
/*     */   private static final String MODERN_COMPILER = "com.sun.tools.javac.Main";
/*     */   public static final String COMPILER_JIKES = "jikes";
/*     */   public static final String COMPILER_GCJ = "gcj";
/*     */   public static final String COMPILER_SYMANTEC_ALIAS = "sj";
/*     */   public static final String COMPILER_SYMANTEC = "symantec";
/*     */   public static final String COMPILER_JVC_ALIAS = "microsoft";
/*     */   public static final String COMPILER_JVC = "jvc";
/*     */   public static final String COMPILER_KJC = "kjc";
/*     */   public static final String COMPILER_JAVAC_1_1 = "javac1.1";
/*     */   public static final String COMPILER_JAVAC_1_2 = "javac1.2";
/*     */   public static final String COMPILER_JAVAC_1_3 = "javac1.3";
/*     */   public static final String COMPILER_JAVAC_1_4 = "javac1.4";
/*     */   public static final String COMPILER_JAVAC_1_5 = "javac1.5";
/*     */   public static final String COMPILER_JAVAC_1_6 = "javac1.6";
/*     */   public static final String COMPILER_JAVAC_1_7 = "javac1.7";
/*     */   public static final String COMPILER_JAVAC_1_8 = "javac1.8";
/*     */   public static final String COMPILER_JAVAC_9_ALIAS = "javac1.9";
/*     */   public static final String COMPILER_JAVAC_9 = "javac9";
/*     */   public static final String COMPILER_JAVAC_10_PLUS = "javac10+";
/*     */   public static final String COMPILER_CLASSIC = "classic";
/*     */   public static final String COMPILER_MODERN = "modern";
/*     */   public static final String COMPILER_EXTJAVAC = "extJavac";
/*  63 */   public static final String COMPILER_MODERN_CLASSNAME = Javac13.class.getName();
/*  64 */   public static final String COMPILER_EXTJAVAC_CLASSNAME = JavacExternal.class.getName();
/*     */   
/*  66 */   private static final List<String> JDK_COMPILERS = Arrays.asList(new String[] { "javac1.1", "javac1.2", "javac1.3", "javac1.4", "javac1.5", "javac1.6", "javac1.7", "javac1.8", "javac1.9", "javac9", "javac10+", "classic", "modern", "extJavac", COMPILER_MODERN_CLASSNAME, COMPILER_EXTJAVAC_CLASSNAME });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private static final List<String> FORKED_JDK_COMPILERS = Arrays.asList(new String[] { "extJavac", COMPILER_EXTJAVAC_CLASSNAME });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   private static final List<String> JDK_COMPILER_NICKNAMES = Arrays.asList(new String[] { "classic", "modern", "extJavac", COMPILER_MODERN_CLASSNAME, COMPILER_EXTJAVAC_CLASSNAME });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   private static final List<String> CLASSIC_JDK_COMPILERS = Arrays.asList(new String[] { "javac1.1", "javac1.2" });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   private static final List<String> MODERN_JDK_COMPILERS = Arrays.asList(new String[] { "javac1.3", "javac1.4", "javac1.5", "javac1.6", "javac1.7", "javac1.8", "javac1.9", "javac9", "javac10+", COMPILER_MODERN_CLASSNAME });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompilerAdapter getCompiler(String compilerType, Task task) throws BuildException {
/* 147 */     return getCompiler(compilerType, task, null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static CompilerAdapter getCompiler(String compilerType, Task task, Path classpath) throws BuildException {
/* 180 */     if ("jikes".equalsIgnoreCase(compilerType)) {
/* 181 */       return new Jikes();
/*     */     }
/* 183 */     if (isForkedJavac(compilerType)) {
/* 184 */       return new JavacExternal();
/*     */     }
/* 186 */     if ("classic".equalsIgnoreCase(compilerType) || 
/* 187 */       isClassicJdkCompiler(compilerType)) {
/* 188 */       task.log("This version of java does not support the classic compiler; upgrading to modern", 1);
/*     */ 
/*     */       
/* 191 */       compilerType = "modern";
/*     */     } 
/* 193 */     if ("modern".equalsIgnoreCase(compilerType) || 
/* 194 */       isModernJdkCompiler(compilerType)) {
/*     */       
/* 196 */       if (doesModernCompilerExist()) {
/* 197 */         return new Javac13();
/*     */       }
/* 199 */       throw new BuildException("Unable to find a javac compiler;\n%s is not on the classpath.\nPerhaps JAVA_HOME does not point to the JDK.\nIt is currently set to \"%s\"", new Object[] { "com.sun.tools.javac.Main", 
/*     */             
/* 201 */             JavaEnvUtils.getJavaHome() });
/*     */     } 
/*     */     
/* 204 */     if ("jvc".equalsIgnoreCase(compilerType) || "microsoft"
/* 205 */       .equalsIgnoreCase(compilerType)) {
/* 206 */       return new Jvc();
/*     */     }
/* 208 */     if ("kjc".equalsIgnoreCase(compilerType)) {
/* 209 */       return new Kjc();
/*     */     }
/* 211 */     if ("gcj".equalsIgnoreCase(compilerType)) {
/* 212 */       return new Gcj();
/*     */     }
/* 214 */     if ("sj".equalsIgnoreCase(compilerType) || "symantec"
/* 215 */       .equalsIgnoreCase(compilerType)) {
/* 216 */       return new Sj();
/*     */     }
/* 218 */     return resolveClassName(compilerType, (ClassLoader)task
/*     */         
/* 220 */         .getProject().createClassLoader(classpath));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean doesModernCompilerExist() {
/*     */     try {
/* 229 */       Class.forName("com.sun.tools.javac.Main");
/* 230 */       return true;
/* 231 */     } catch (ClassNotFoundException cnfe) {
/*     */       try {
/* 233 */         ClassLoader cl = CompilerAdapterFactory.class.getClassLoader();
/* 234 */         if (cl != null) {
/* 235 */           cl.loadClass("com.sun.tools.javac.Main");
/* 236 */           return true;
/*     */         } 
/* 238 */       } catch (ClassNotFoundException classNotFoundException) {}
/*     */ 
/*     */ 
/*     */       
/* 242 */       return false;
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static CompilerAdapter resolveClassName(String className, ClassLoader loader) throws BuildException {
/* 257 */     return (CompilerAdapter)ClasspathUtils.newInstance(className, 
/* 258 */         (loader != null) ? loader : 
/* 259 */         CompilerAdapterFactory.class.getClassLoader(), CompilerAdapter.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isForkedJavac(String compilerName) {
/* 270 */     return containsIgnoreCase(FORKED_JDK_COMPILERS, compilerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJdkCompiler(String compilerName) {
/* 280 */     return containsIgnoreCase(JDK_COMPILERS, compilerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isJdkCompilerNickname(String compilerName) {
/* 290 */     return containsIgnoreCase(JDK_COMPILER_NICKNAMES, compilerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isClassicJdkCompiler(String compilerName) {
/* 300 */     return containsIgnoreCase(CLASSIC_JDK_COMPILERS, compilerName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isModernJdkCompiler(String compilerName) {
/* 310 */     return containsIgnoreCase(MODERN_JDK_COMPILERS, compilerName);
/*     */   }
/*     */   
/*     */   private static boolean containsIgnoreCase(List<String> compilers, String compilerName) {
/* 314 */     Objects.requireNonNull(compilerName); return (compilerName != null && compilers.stream().anyMatch(compilerName::equalsIgnoreCase));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/CompilerAdapterFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */