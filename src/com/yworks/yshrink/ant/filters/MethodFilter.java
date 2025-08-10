/*     */ package com.yworks.yshrink.ant.filters;
/*     */ 
/*     */ import com.yworks.common.ant.PatternMatchedSection;
/*     */ import com.yworks.common.ant.TypePatternSet;
/*     */ import com.yworks.yshrink.ant.MethodSection;
/*     */ import com.yworks.yshrink.model.ClassDescriptor;
/*     */ import com.yworks.yshrink.model.MethodDescriptor;
/*     */ import com.yworks.yshrink.model.Model;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.StringTokenizer;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ public class MethodFilter
/*     */   extends PatternMatchedFilter
/*     */ {
/*     */   private List<MethodSection> sections;
/*     */   
/*     */   public MethodFilter(Project project) {
/*  31 */     super(project);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMethodSection(MethodSection methodSection) {
/*  40 */     if (null == this.sections) {
/*  41 */       this.sections = new ArrayList<>(5);
/*     */     }
/*  43 */     this.sections.add(methodSection);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEntryPointMethod(Model model, ClassDescriptor cd, MethodDescriptor md) {
/*  49 */     String className = cd.getName();
/*  50 */     String methodName = md.getName();
/*     */     
/*  52 */     for (MethodSection ms : this.sections) {
/*     */       int i; boolean bool1;
/*  54 */       String entryMethodName = ms.getName();
/*  55 */       String entryMethodClass = ms.getClassName();
/*     */       
/*  57 */       boolean r = true;
/*     */ 
/*     */       
/*  60 */       if (null != ms.getReturnType()) {
/*  61 */         Type requiredReturnType = Type.getType(Util.verboseToNativeType(ms.getReturnType()));
/*  62 */         r &= requiredReturnType.equals(md.getReturnType());
/*     */       } 
/*     */ 
/*     */       
/*  66 */       if (null != ms.getArgs()) {
/*  67 */         String[] requiredArgTypes = ms.getArgs().split("\\s*,\\s*");
/*  68 */         if (requiredArgTypes.length == 1 && requiredArgTypes[0].length() == 0) {
/*  69 */           requiredArgTypes = new String[0];
/*     */         }
/*  71 */         Type[] argTypes = md.getArgumentTypes();
/*     */         
/*  73 */         if (requiredArgTypes.length == argTypes.length) {
/*  74 */           for (int j = 0; j < argTypes.length; j++) {
/*  75 */             Type argType = argTypes[j];
/*  76 */             Type requiredArgType = Type.getType(Util.verboseToNativeType(requiredArgTypes[j].trim()));
/*     */             
/*  78 */             r &= argType.equals(requiredArgType);
/*     */           } 
/*     */         } else {
/*  81 */           r = false;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  86 */       if (null != ms.getAccess()) {
/*  87 */         r &= ms.getAccess().isAccessLevel(md.getAccess());
/*     */       }
/*     */ 
/*     */       
/*  91 */       if (null == entryMethodClass || entryMethodClass.length() == 0) {
/*     */         
/*  93 */         i = r & ((match(TypePatternSet.Type.CLASS, className, (PatternMatchedSection)ms) || match(TypePatternSet.Type.CLASS, Util.toJavaClass(className), (PatternMatchedSection)ms)) ? 1 : 0);
/*     */       } else {
/*  95 */         bool1 = i & entryMethodClass.equals(className);
/*     */       } 
/*     */ 
/*     */       
/*  99 */       if (null != ms.getThrows()) {
/*     */         
/* 101 */         StringTokenizer tokenizer = new StringTokenizer(ms.getThrows(), ",");
/*     */         
/* 103 */         while (tokenizer.hasMoreTokens()) {
/* 104 */           String exception = Util.toInternalClass(tokenizer.nextToken().trim());
/*     */ 
/*     */ 
/*     */           
/* 108 */           boolean found = false;
/*     */           
/* 110 */           if (null != md.getExceptions()) {
/* 111 */             for (String exception2 : md.getExceptions()) {
/* 112 */               if (exception2.equals(exception)) {
/* 113 */                 found = true;
/*     */               }
/*     */             } 
/*     */           }
/*     */           
/* 118 */           bool1 &= found;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 124 */       if (null == entryMethodName || entryMethodName.length() == 0) {
/* 125 */         bool1 &= match(TypePatternSet.Type.NAME, methodName, (PatternMatchedSection)ms);
/*     */       } else {
/* 127 */         bool1 &= entryMethodName.equals(methodName);
/*     */       } 
/*     */       
/* 130 */       if (bool1) {
/* 131 */         return bool1;
/*     */       }
/*     */     } 
/*     */     
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/MethodFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */