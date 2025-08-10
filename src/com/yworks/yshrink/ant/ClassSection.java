/*     */ package com.yworks.yshrink.ant;
/*     */ 
/*     */ import com.yworks.common.ant.PatternMatchedSection;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ClassSection
/*     */   extends PatternMatchedSection
/*     */ {
/*     */   private String name;
/*     */   private String extendsType;
/*     */   private String implementsType;
/*  19 */   private PatternMatchedSection.Access classAccess = PatternMatchedSection.Access.NONE;
/*  20 */   private PatternMatchedSection.Access methodAccess = PatternMatchedSection.Access.NONE;
/*  21 */   private PatternMatchedSection.Access fieldAccess = PatternMatchedSection.Access.NONE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  36 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  45 */     this.name = Util.toInternalClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtends() {
/*  54 */     return this.extendsType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExtends(String extendsType) {
/*  63 */     this.extendsType = Util.toInternalClass(extendsType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getImplements() {
/*  72 */     return this.implementsType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setImplements(String implementsType) {
/*  81 */     this.implementsType = Util.toInternalClass(implementsType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAccess(String access) {
/*  86 */     super.setAccess(access);
/*  87 */     setClassAccess(access);
/*  88 */     setMethodAccess(access);
/*  89 */     setFieldAccess(access);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternMatchedSection.Access getClassAccess() {
/*  98 */     return this.classAccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClassAccess(String classAccessStr) {
/* 107 */     PatternMatchedSection.Access acc = accessValue(classAccessStr);
/*     */     
/* 109 */     if (null != acc) {
/* 110 */       this.classAccess = acc;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternMatchedSection.Access getMethodAccess() {
/* 120 */     return this.methodAccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodAccess(String methodAccessStr) {
/* 129 */     PatternMatchedSection.Access acc = accessValue(methodAccessStr);
/*     */     
/* 131 */     if (null != acc) {
/* 132 */       this.methodAccess = acc;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasses(String classAccess) {
/* 142 */     setClassAccess(classAccess);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethods(String methodAccess) {
/* 151 */     setMethodAccess(methodAccess);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternMatchedSection.Access getFieldAccess() {
/* 160 */     return this.fieldAccess;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFieldAccess(String fieldAccessStr) {
/* 170 */     PatternMatchedSection.Access acc = accessValue(fieldAccessStr);
/*     */     
/* 172 */     if (null != acc) {
/* 173 */       this.fieldAccess = acc;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFields(String fieldAccess) {
/* 183 */     setFieldAccess(fieldAccess);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/ClassSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */