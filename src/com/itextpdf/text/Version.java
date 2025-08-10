/*     */ package com.itextpdf.text;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Version
/*     */ {
/*  58 */   private static final Object staticLock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   public static String AGPL = " (AGPL-version)";
/*     */ 
/*     */   
/*  66 */   private static volatile Version version = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   private final String iText = "iText®";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private final String release = "5.5.13.3";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private String iTextVersion = "iText® 5.5.13.3 ©2000-2022 iText Group NV";
/*     */ 
/*     */ 
/*     */   
/*  88 */   private String key = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Version getInstance() {
/*  96 */     synchronized (staticLock) {
/*  97 */       if (version != null) {
/*  98 */         return version;
/*     */       }
/*     */     } 
/* 101 */     Version localVersion = new Version();
/*     */     try {
/* 103 */       Class<?> klass = Class.forName("com.itextpdf.licensekey.LicenseKey");
/* 104 */       Class[] cArg = { String.class };
/* 105 */       Method m = klass.getMethod("getLicenseeInfoForVersion", cArg);
/* 106 */       localVersion.getClass(); Object[] args = { "5.5.13.3" };
/* 107 */       String[] info = (String[])m.invoke(klass.newInstance(), args);
/* 108 */       if (info[3] != null && info[3].trim().length() > 0) {
/* 109 */         localVersion.key = info[3];
/*     */       } else {
/* 111 */         localVersion.key = "Trial version ";
/* 112 */         if (info[5] == null) {
/* 113 */           localVersion.key += "unauthorised";
/*     */         } else {
/* 115 */           localVersion.key += info[5];
/*     */         } 
/*     */       } 
/*     */       
/* 119 */       if (info[4] != null && info[4].trim().length() > 0) {
/* 120 */         localVersion.iTextVersion = info[4];
/* 121 */       } else if (info[2] != null && info[2].trim().length() > 0) {
/* 122 */         localVersion.iTextVersion += " (" + info[2];
/* 123 */         if (!localVersion.key.toLowerCase().startsWith("trial")) {
/* 124 */           localVersion.iTextVersion += "; licensed version)";
/*     */         } else {
/* 126 */           localVersion.iTextVersion += "; " + localVersion.key + ")";
/*     */         } 
/* 128 */       } else if (info[0] != null && info[0].trim().length() > 0) {
/*     */         
/* 130 */         localVersion.iTextVersion += " (" + info[0];
/* 131 */         if (!localVersion.key.toLowerCase().startsWith("trial")) {
/*     */ 
/*     */           
/* 134 */           localVersion.iTextVersion += "; licensed version)";
/*     */         } else {
/* 136 */           localVersion.iTextVersion += "; " + localVersion.key + ")";
/*     */         } 
/*     */       } else {
/* 139 */         throw new Exception();
/*     */       } 
/* 141 */     } catch (Exception e) {
/* 142 */       if (dependsOnTheOldLicense()) {
/* 143 */         throw new RuntimeException("iText License Library 1.0.* has been deprecated. Please, update to the latest version.");
/*     */       }
/* 145 */       localVersion.iTextVersion += AGPL;
/*     */     } 
/* 147 */     return atomicSetVersion(localVersion);
/*     */   }
/*     */   
/*     */   private static boolean dependsOnTheOldLicense() {
/*     */     try {
/* 152 */       Class<?> klass = Class.forName("com.itextpdf.license.LicenseKey");
/* 153 */       return (klass.getField("PRODUCT_NAME") != null);
/* 154 */     } catch (Exception e) {
/* 155 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getProduct() {
/* 166 */     return "iText®";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRelease() {
/* 176 */     return "5.5.13.3";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getVersion() {
/* 187 */     return this.iTextVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/* 195 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAGPLVersion() {
/* 203 */     return (getInstance().getVersion().indexOf(AGPL) > 0);
/*     */   }
/*     */   
/*     */   private static Version atomicSetVersion(Version newVersion) {
/* 207 */     synchronized (staticLock) {
/* 208 */       version = newVersion;
/* 209 */       return version;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/text/Version.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */