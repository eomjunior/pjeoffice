/*     */ package org.apache.tools.ant.types;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModuleVersion
/*     */ {
/*     */   private String number;
/*     */   private String preRelease;
/*     */   private String build;
/*     */   
/*     */   public String getNumber() {
/*  46 */     return this.number;
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
/*     */   public void setNumber(String number) {
/*  60 */     Objects.requireNonNull(number, "Version number cannot be null.");
/*  61 */     if (number.indexOf('-') >= 0 || number.indexOf('+') >= 0) {
/*  62 */       throw new IllegalArgumentException("Version number cannot contain '-' or '+'.");
/*     */     }
/*     */     
/*  65 */     this.number = number;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPreRelease() {
/*  74 */     return this.preRelease;
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
/*     */   public void setPreRelease(String pre) {
/*  86 */     if (pre != null && pre.indexOf('+') >= 0) {
/*  87 */       throw new IllegalArgumentException("Version's pre-release cannot contain '+'.");
/*     */     }
/*     */     
/*  90 */     this.preRelease = pre;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBuild() {
/*  99 */     return this.build;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBuild(String build) {
/* 109 */     this.build = build;
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
/*     */   public String toModuleVersionString() {
/* 122 */     if (this.number == null) {
/* 123 */       throw new IllegalStateException("Version number cannot be null.");
/*     */     }
/*     */     
/* 126 */     StringBuilder version = new StringBuilder(this.number);
/* 127 */     if (this.preRelease != null || this.build != null) {
/* 128 */       version.append('-').append(Objects.toString(this.preRelease, ""));
/*     */     }
/* 130 */     if (this.build != null) {
/* 131 */       version.append('+').append(this.build);
/*     */     }
/*     */     
/* 134 */     return version.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     return getClass().getName() + "[number=" + this.number + ", preRelease=" + this.preRelease + ", build=" + this.build + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/ModuleVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */