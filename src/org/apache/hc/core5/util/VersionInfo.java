/*     */ package org.apache.hc.core5.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionInfo
/*     */ {
/*     */   public static final String UNAVAILABLE = "UNAVAILABLE";
/*     */   public static final String VERSION_PROPERTY_FILE = "version.properties";
/*     */   public static final String PROPERTY_MODULE = "info.module";
/*     */   public static final String PROPERTY_RELEASE = "info.release";
/*     */   @Deprecated
/*     */   public static final String PROPERTY_TIMESTAMP = "info.timestamp";
/*     */   private final String infoPackage;
/*     */   private final String infoModule;
/*     */   private final String infoRelease;
/*     */   private final String infoTimestamp;
/*     */   private final String infoClassloader;
/*  86 */   private static final VersionInfo[] EMPTY_VERSION_INFO_ARRAY = new VersionInfo[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VersionInfo(String pckg, String module, String release, String time, String clsldr) {
/* 100 */     Args.notNull(pckg, "Package identifier");
/* 101 */     this.infoPackage = pckg;
/* 102 */     this.infoModule = (module != null) ? module : "UNAVAILABLE";
/* 103 */     this.infoRelease = (release != null) ? release : "UNAVAILABLE";
/* 104 */     this.infoTimestamp = (time != null) ? time : "UNAVAILABLE";
/* 105 */     this.infoClassloader = (clsldr != null) ? clsldr : "UNAVAILABLE";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getPackage() {
/* 116 */     return this.infoPackage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getModule() {
/* 126 */     return this.infoModule;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getRelease() {
/* 136 */     return this.infoRelease;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final String getTimestamp() {
/* 148 */     return this.infoTimestamp;
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
/*     */   public final String getClassloader() {
/* 160 */     return this.infoClassloader;
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
/*     */   public String toString() {
/* 174 */     StringBuilder sb = new StringBuilder(20 + this.infoPackage.length() + this.infoModule.length() + this.infoRelease.length() + this.infoTimestamp.length() + this.infoClassloader.length());
/*     */     
/* 176 */     sb.append("VersionInfo(")
/* 177 */       .append(this.infoPackage).append(':').append(this.infoModule);
/*     */ 
/*     */ 
/*     */     
/* 181 */     if (!"UNAVAILABLE".equals(this.infoRelease)) {
/* 182 */       sb.append(':').append(this.infoRelease);
/*     */     }
/*     */     
/* 185 */     sb.append(')');
/*     */     
/* 187 */     if (!"UNAVAILABLE".equals(this.infoClassloader)) {
/* 188 */       sb.append('@').append(this.infoClassloader);
/*     */     }
/*     */     
/* 191 */     return sb.toString();
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
/*     */   public static VersionInfo[] loadVersionInfo(String[] pckgs, ClassLoader clsldr) {
/* 207 */     Args.notNull(pckgs, "Package identifier array");
/* 208 */     List<VersionInfo> vil = new ArrayList<>(pckgs.length);
/* 209 */     for (String pckg : pckgs) {
/* 210 */       VersionInfo vi = loadVersionInfo(pckg, clsldr);
/* 211 */       if (vi != null) {
/* 212 */         vil.add(vi);
/*     */       }
/*     */     } 
/*     */     
/* 216 */     return vil.<VersionInfo>toArray(EMPTY_VERSION_INFO_ARRAY);
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
/*     */   public static VersionInfo loadVersionInfo(String pckg, ClassLoader clsldr) {
/* 233 */     Args.notNull(pckg, "Package identifier");
/* 234 */     ClassLoader cl = (clsldr != null) ? clsldr : Thread.currentThread().getContextClassLoader();
/*     */     
/* 236 */     Properties vip = null;
/*     */ 
/*     */ 
/*     */     
/* 240 */     try (InputStream is = cl.getResourceAsStream(pckg.replace('.', '/') + "/" + "version.properties")) {
/* 241 */       if (is != null) {
/* 242 */         Properties props = new Properties();
/* 243 */         props.load(is);
/* 244 */         vip = props;
/*     */       }
/*     */     
/* 247 */     } catch (IOException iOException) {}
/*     */ 
/*     */ 
/*     */     
/* 251 */     VersionInfo result = null;
/* 252 */     if (vip != null) {
/* 253 */       result = fromMap(pckg, vip, cl);
/*     */     }
/*     */     
/* 256 */     return result;
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
/*     */   protected static VersionInfo fromMap(String pckg, Map<?, ?> info, ClassLoader clsldr) {
/* 271 */     Args.notNull(pckg, "Package identifier");
/* 272 */     String module = null;
/* 273 */     String release = null;
/*     */     
/* 275 */     if (info != null) {
/* 276 */       module = (String)info.get("info.module");
/* 277 */       if (module != null && module.length() < 1) {
/* 278 */         module = null;
/*     */       }
/*     */       
/* 281 */       release = (String)info.get("info.release");
/* 282 */       if (release != null && (release.length() < 1 || release
/* 283 */         .equals("${project.version}"))) {
/* 284 */         release = null;
/*     */       }
/*     */     } 
/*     */     
/* 288 */     String clsldrstr = null;
/* 289 */     if (clsldr != null) {
/* 290 */       clsldrstr = clsldr.toString();
/*     */     }
/*     */     
/* 293 */     return new VersionInfo(pckg, module, release, null, clsldrstr);
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
/*     */   public static String getSoftwareInfo(String name, String pkg, Class<?> cls) {
/* 313 */     VersionInfo vi = loadVersionInfo(pkg, cls.getClassLoader());
/* 314 */     String release = (vi != null) ? vi.getRelease() : "UNAVAILABLE";
/* 315 */     String javaVersion = System.getProperty("java.version");
/*     */     
/* 317 */     String nameAndRelease = name;
/* 318 */     if (!"UNAVAILABLE".equals(release)) {
/* 319 */       nameAndRelease = nameAndRelease + "/" + release;
/*     */     }
/*     */     
/* 322 */     return String.format("%s (Java/%s)", new Object[] { nameAndRelease, javaVersion });
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/util/VersionInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */