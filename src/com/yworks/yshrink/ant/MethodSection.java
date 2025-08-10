/*     */ package com.yworks.yshrink.ant;
/*     */ 
/*     */ import com.yworks.common.ant.PatternMatchedSection;
/*     */ import com.yworks.common.ant.TypePatternSet;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import org.apache.tools.ant.types.PatternSet;
/*     */ import org.objectweb.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodSection
/*     */   extends PatternMatchedSection
/*     */ {
/*     */   private String signature;
/*     */   private String name;
/*     */   private String className;
/*     */   private String returnType;
/*     */   private String args;
/*     */   private String throwsClause;
/*     */   
/*     */   public String getSignature() {
/*  36 */     return this.signature;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSignature(String signature) {
/*  45 */     this.signature = signature;
/*  46 */     String[] methodArr = Util.toNativeMethod(signature);
/*  47 */     String methodName = methodArr[0];
/*  48 */     String methodDesc = methodArr[1];
/*  49 */     setName(methodName);
/*  50 */     setReturnType(Util.toJavaType(Type.getReturnType(methodDesc).getDescriptor()));
/*     */     
/*  52 */     setArgs(Util.getArgumentString(Type.getArgumentTypes(methodDesc)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getArgs() {
/*  61 */     return this.args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgs(String args) {
/*  70 */     this.args = args;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/*  81 */     if (name.trim().indexOf(' ') != -1) {
/*  82 */       setSignature(name);
/*     */     } else {
/*  84 */       this.name = name;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClass(String name) {
/*  94 */     this.className = Util.toInternalClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 103 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 112 */     return this.className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReturnType() {
/* 121 */     return this.returnType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReturnType(String returnType) {
/* 130 */     this.returnType = Util.toInternalClass(returnType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThrows() {
/* 139 */     return this.throwsClause;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThrows(String throwsClause) {
/* 148 */     this.throwsClause = throwsClause;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypePatternSet createPatternSet() {
/* 153 */     System.out.println("MethodSection.createPatternSet");
/* 154 */     TypePatternSet typePatternSet = newTypePatternSet();
/* 155 */     typePatternSet.setType("class");
/* 156 */     addPatternSet((PatternSet)typePatternSet, typePatternSet.getType());
/* 157 */     return typePatternSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TypePatternSet newTypePatternSet() {
/* 167 */     return new TypePatternSet();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/MethodSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */