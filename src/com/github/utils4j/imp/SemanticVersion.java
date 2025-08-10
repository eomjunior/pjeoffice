/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SemanticVersion
/*     */ {
/*     */   private final int major;
/*     */   private final int minor;
/*     */   private final int patch;
/*     */   
/*     */   public static SemanticVersion from(int major, int minor, int patch) {
/*  37 */     return new SemanticVersion(major, minor, patch);
/*     */   }
/*     */   
/*     */   public static SemanticVersion from(String version) throws IOException {
/*  41 */     checkFormat(!Strings.hasText(version), version);
/*     */     
/*  43 */     String[] members = version.split("\\.");
/*  44 */     checkFormat((members.length != 3), version);
/*     */     
/*  46 */     int major = Strings.toInt(members[0], -1);
/*  47 */     checkFormat((major <= 0), version);
/*     */     
/*  49 */     int minor = Strings.toInt(members[1], -1);
/*  50 */     checkFormat((minor < 0), version);
/*     */     
/*  52 */     int patch = Strings.toInt(members[2], -1);
/*  53 */     checkFormat((patch < 0), version);
/*  54 */     return from(major, minor, patch);
/*     */   }
/*     */   
/*     */   private static void checkFormat(boolean condition, String version) throws IOException {
/*  58 */     if (condition) {
/*  59 */       throw new IOException("Versão em formato inválido '" + version + "'");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SemanticVersion(int major, int minor, int patch) {
/*  66 */     this.major = Args.requirePositive(major, "major is <= 0");
/*  67 */     this.minor = Args.requireZeroPositive(minor, "minor is < 0");
/*  68 */     this.patch = Args.requireZeroPositive(patch, "patch is < 0");
/*     */   }
/*     */ 
/*     */   
/*     */   public final String toString() {
/*  73 */     return this.major + "." + this.minor + "." + this.patch;
/*     */   }
/*     */   
/*     */   public final String fullString() {
/*  77 */     return toString() + ".0";
/*     */   }
/*     */   
/*     */   public final int major() {
/*  81 */     return this.major;
/*     */   }
/*     */   
/*     */   public final int minor() {
/*  85 */     return this.minor;
/*     */   }
/*     */   
/*     */   public final int patch() {
/*  89 */     return this.patch;
/*     */   }
/*     */   
/*     */   public final int match(SemanticVersion version) {
/*  93 */     if (this.major > version.major())
/*  94 */       return 1; 
/*  95 */     if (this.major < version.major()) {
/*  96 */       return -1;
/*     */     }
/*  98 */     if (this.minor > version.minor())
/*  99 */       return 1; 
/* 100 */     if (this.minor < version.minor()) {
/* 101 */       return -1;
/*     */     }
/* 103 */     return this.patch - version.patch;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/SemanticVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */