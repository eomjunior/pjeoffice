/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import com.github.utils4j.IConstants;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jvms
/*     */ {
/*  50 */   public static final String OS_NAME = System.getProperty("os.name").toLowerCase();
/*     */   
/*  52 */   public static final String OS_ARCH = System.getProperty("os.arch", "").toLowerCase();
/*     */   
/*  54 */   public static final String SYSTEM_ROOT = computeSystemRoot();
/*     */   
/*  56 */   public static final String JAVA_VERSION = computeJavaVersion();
/*     */   
/*     */   public static boolean isWindows() {
/*  59 */     return (OS_NAME.indexOf("win") >= 0);
/*     */   }
/*     */   
/*     */   public static boolean isMac() {
/*  63 */     return (OS_NAME.indexOf("mac") >= 0);
/*     */   }
/*     */   
/*     */   public static boolean is64Bits() {
/*  67 */     return OS_ARCH.contains("64");
/*     */   }
/*     */   
/*     */   public static boolean isArm() {
/*  71 */     return (OS_ARCH.contains("arm") || OS_ARCH.contains("aarch"));
/*     */   }
/*     */   
/*     */   public static boolean isUnix() {
/*  75 */     return (OS_NAME.indexOf("nix") >= 0 || OS_NAME.indexOf("nux") >= 0 || OS_NAME.indexOf("aix") > 0);
/*     */   }
/*     */   
/*     */   public static boolean isSolaris() {
/*  79 */     return (OS_NAME.indexOf("sunos") >= 0);
/*     */   }
/*     */   
/*     */   private static String computeSystemRoot() {
/*  83 */     String value = System.getenv("SystemRoot");
/*  84 */     if (value == null)
/*  85 */       return Strings.empty(); 
/*  86 */     return value.replaceAll("\\\\", "/");
/*     */   }
/*     */   
/*     */   private static String computeJavaVersion() {
/*  90 */     return System.getProperty("java.version", "");
/*     */   }
/*     */   
/*     */   public static String env(String paramName, File path) {
/*  94 */     Args.requireText(paramName, "paramName is empty");
/*  95 */     Args.requireNonNull(path, "path is null");
/*  96 */     return env(paramName, Directory.stringPath(path));
/*     */   }
/*     */   
/*     */   public static String env(String paramName, Path path) {
/* 100 */     Args.requireText(paramName, "paramName is empty");
/* 101 */     Args.requireNonNull(path, "path is null");
/* 102 */     return env(paramName, path.toFile());
/*     */   }
/*     */   
/*     */   public static String env(String paramName, String paramValue) {
/* 106 */     Args.requireText(paramName, "paramName is empty").trim();
/* 107 */     return String.format("-D%s=%s", new Object[] { Strings.trim(paramName), Strings.trim(paramValue) });
/*     */   }
/*     */   
/*     */   public static boolean isNative64bits() {
/* 111 */     if (is64Bits())
/* 112 */       return true; 
/* 113 */     if (isWindows())
/* 114 */       return ((new File("C:/Windows/SysWOW64")).exists() || System.getenv("ProgramFiles(X86)") != null); 
/* 115 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isMacArm() {
/* 119 */     if (!isMac())
/* 120 */       return false;  try {
/*     */       String output;
/* 122 */       Process process = (new ProcessBuilder(new String[] { "sysctl", "-n", "machdep.cpu.brand_string" })).start();
/*     */       
/* 124 */       try (InputStream input = process.getInputStream()) {
/* 125 */         output = Strings.trim(Streams.readOutStream(input, IConstants.CP_850).get()).toLowerCase();
/* 126 */         process.waitFor();
/*     */       } finally {
/* 128 */         Throwables.quietly(process.destroyForcibly()::waitFor);
/*     */       } 
/* 130 */       return (output.indexOf("intel") < 0);
/* 131 */     } catch (Exception e) {
/* 132 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Jvms.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */