/*     */ package org.apache.tools.ant.taskdefs.condition;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Locale;
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
/*     */ public class Os
/*     */   implements Condition
/*     */ {
/*  33 */   private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
/*     */   
/*  35 */   private static final String OS_ARCH = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);
/*     */   
/*  37 */   private static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.ENGLISH);
/*  38 */   private static final String PATH_SEP = File.pathSeparator;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_WINDOWS = "windows";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_9X = "win9x";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_NT = "winnt";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_OS2 = "os/2";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_NETWARE = "netware";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_DOS = "dos";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_MAC = "mac";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_TANDEM = "tandem";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_UNIX = "unix";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_VMS = "openvms";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_ZOS = "z/os";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String FAMILY_OS400 = "os/400";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String DARWIN = "darwin";
/*     */ 
/*     */ 
/*     */   
/*     */   private String family;
/*     */ 
/*     */ 
/*     */   
/*     */   private String name;
/*     */ 
/*     */ 
/*     */   
/*     */   private String version;
/*     */ 
/*     */ 
/*     */   
/*     */   private String arch;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Os() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Os(String family) {
/* 124 */     setFamily(family);
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
/*     */   public void setFamily(String f) {
/* 146 */     this.family = f.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 155 */     this.name = name.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArch(String arch) {
/* 164 */     this.arch = arch.toLowerCase(Locale.ENGLISH);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVersion(String version) {
/* 173 */     this.version = version.toLowerCase(Locale.ENGLISH);
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
/*     */   public boolean eval() throws BuildException {
/* 185 */     return isOs(this.family, this.name, this.arch, this.version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFamily(String family) {
/* 196 */     return isOs(family, null, null, null);
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
/*     */   public static boolean isName(String name) {
/* 208 */     return isOs(null, name, null, null);
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
/*     */   public static boolean isArch(String arch) {
/* 220 */     return isOs(null, null, arch, null);
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
/*     */   public static boolean isVersion(String version) {
/* 232 */     return isOs(null, null, null, version);
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
/*     */   public static boolean isOs(String family, String name, String arch, String version) {
/* 248 */     boolean retValue = false;
/*     */     
/* 250 */     if (family != null || name != null || arch != null || version != null) {
/*     */ 
/*     */       
/* 253 */       boolean isFamily = true;
/* 254 */       boolean isName = true;
/* 255 */       boolean isArch = true;
/* 256 */       boolean isVersion = true;
/*     */       
/* 258 */       if (family != null) {
/*     */ 
/*     */ 
/*     */         
/* 262 */         boolean isWindows = OS_NAME.contains("windows");
/* 263 */         boolean is9x = false;
/* 264 */         boolean isNT = false;
/* 265 */         if (isWindows) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 272 */           is9x = (OS_NAME.contains("95") || OS_NAME.contains("98") || OS_NAME.contains("me") || OS_NAME.contains("ce"));
/* 273 */           isNT = !is9x;
/*     */         } 
/* 275 */         switch (family) {
/*     */           case "windows":
/* 277 */             isFamily = isWindows;
/*     */             break;
/*     */           case "win9x":
/* 280 */             isFamily = (isWindows && is9x);
/*     */             break;
/*     */           case "winnt":
/* 283 */             isFamily = (isWindows && isNT);
/*     */             break;
/*     */           case "os/2":
/* 286 */             isFamily = OS_NAME.contains("os/2");
/*     */             break;
/*     */           case "netware":
/* 289 */             isFamily = OS_NAME.contains("netware");
/*     */             break;
/*     */           case "dos":
/* 292 */             isFamily = (PATH_SEP.equals(";") && !isFamily("netware"));
/*     */             break;
/*     */           
/*     */           case "mac":
/* 296 */             isFamily = (OS_NAME.contains("mac") || OS_NAME.contains("darwin"));
/*     */             break;
/*     */           case "tandem":
/* 299 */             isFamily = OS_NAME.contains("nonstop_kernel");
/*     */             break;
/*     */ 
/*     */ 
/*     */           
/*     */           case "unix":
/* 305 */             isFamily = (PATH_SEP.equals(":") && !isFamily("openvms") && (!isFamily("mac") || OS_NAME.endsWith("x") || OS_NAME.contains("darwin")));
/*     */             break;
/*     */           
/*     */           case "z/os":
/* 309 */             isFamily = (OS_NAME.contains("z/os") || OS_NAME.contains("os/390"));
/*     */             break;
/*     */           case "os/400":
/* 312 */             isFamily = OS_NAME.contains("os/400");
/*     */             break;
/*     */           case "openvms":
/* 315 */             isFamily = OS_NAME.contains("openvms");
/*     */             break;
/*     */           default:
/* 318 */             throw new BuildException("Don't know how to detect os family \"" + family + "\"");
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 323 */       if (name != null) {
/* 324 */         isName = name.equals(OS_NAME);
/*     */       }
/* 326 */       if (arch != null) {
/* 327 */         isArch = arch.equals(OS_ARCH);
/*     */       }
/* 329 */       if (version != null) {
/* 330 */         isVersion = version.equals(OS_VERSION);
/*     */       }
/* 332 */       retValue = (isFamily && isName && isArch && isVersion);
/*     */     } 
/* 334 */     return retValue;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/Os.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */