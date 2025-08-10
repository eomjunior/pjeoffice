/*     */ package com.yworks.yguard.ant;
/*     */ 
/*     */ import com.yworks.common.ant.YGuardBaseTask;
/*     */ import com.yworks.yguard.ObfuscatorTask;
/*     */ import com.yworks.yguard.obf.YGuardRule;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.apache.tools.ant.types.ZipFileSet;
/*     */ 
/*     */ 
/*     */ public class ClassSection
/*     */   extends PatternMatchedClassesSection
/*     */   implements Mappable
/*     */ {
/*     */   private String name;
/*     */   private String mapTo;
/*  18 */   private int methodMode = 0;
/*  19 */   private int fieldMode = 0;
/*  20 */   private int classMode = 0;
/*     */   
/*     */   private boolean classesSet = false;
/*     */   
/*     */   private String extendsType;
/*     */   
/*     */   private String implementsType;
/*     */   
/*     */   protected final YGuardBaseTask task;
/*     */ 
/*     */   
/*     */   public ClassSection() {
/*  32 */     this.task = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassSection(YGuardBaseTask task) {
/*  41 */     this.task = task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  50 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasses(ObfuscatorTask.Modifiers m) {
/*  59 */     this.classMode = m.getModifierValue();
/*  60 */     this.classesSet = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethods(ObfuscatorTask.Modifiers m) {
/*  69 */     this.methodMode = m.getModifierValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFields(ObfuscatorTask.Modifiers m) {
/*  78 */     this.fieldMode = m.getModifierValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMap(String mapTo) {
/*  87 */     this.mapTo = mapTo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtends(String extendsType) {
/*  96 */     this.extendsType = ObfuscatorTask.toNativeClass(extendsType);
/*  97 */     if (this.task instanceof ObfuscatorTask) {
/*  98 */       ((ObfuscatorTask)this.task).setNeedYShrinkModel(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtends() {
/* 109 */     return this.extendsType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplements(String implementsType) {
/* 118 */     this.implementsType = ObfuscatorTask.toNativeClass(implementsType);
/* 119 */     if (this.task instanceof ObfuscatorTask) {
/* 120 */       ((ObfuscatorTask)this.task).setNeedYShrinkModel(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplements() {
/* 130 */     return this.implementsType;
/*     */   }
/*     */   
/*     */   public void addEntries(Collection entries, ZipFileSet zf) throws IOException {
/* 134 */     if (this.classesSet && this.patternSets.size() < 1) {
/* 135 */       PatternSet ps = new PatternSet();
/* 136 */       ps.setProject(zf.getProject());
/* 137 */       ps.setIncludes("**.*");
/* 138 */       this.patternSets.add(ps);
/*     */     } 
/* 140 */     super.addEntries(entries, zf);
/*     */   }
/*     */   
/*     */   public void addEntries(Collection<YGuardRule> entries, String name) {
/* 144 */     String className = ObfuscatorTask.toNativeClass(name);
/* 145 */     YGuardRule centry = new YGuardRule(1, className);
/* 146 */     centry.retainFields = this.fieldMode;
/* 147 */     centry.retainMethods = this.methodMode;
/* 148 */     if (this.classesSet) {
/* 149 */       centry.retainClasses = this.classMode;
/*     */     }
/* 151 */     entries.add(centry);
/*     */   }
/*     */   
/*     */   public void addMapEntries(Collection<YGuardRule> entries) {
/* 155 */     YGuardRule entry = new YGuardRule(5, ObfuscatorTask.toNativeClass(this.name));
/* 156 */     entry.obfName = ObfuscatorTask.toNativeClass(this.mapTo);
/* 157 */     entries.add(entry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getClassMode() {
/* 166 */     return this.classMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFieldMode() {
/* 175 */     return this.fieldMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMethodMode() {
/* 184 */     return this.methodMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 193 */     return this.name;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yguard/ant/ClassSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */