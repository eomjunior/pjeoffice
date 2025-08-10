/*     */ package com.yworks.yshrink.ant.filters;
/*     */ 
/*     */ import com.yworks.common.ant.PatternMatchedSection;
/*     */ import com.yworks.common.ant.TypePatternSet;
/*     */ import com.yworks.yshrink.ant.ClassSection;
/*     */ import com.yworks.yshrink.model.ClassDescriptor;
/*     */ import com.yworks.yshrink.model.FieldDescriptor;
/*     */ import com.yworks.yshrink.model.MethodDescriptor;
/*     */ import com.yworks.yshrink.model.Model;
/*     */ import com.yworks.yshrink.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ public class ClassFilter
/*     */   extends PatternMatchedFilter
/*     */ {
/*     */   private List<ClassSection> sections;
/*     */   
/*     */   public ClassFilter(Project project) {
/*  32 */     super(project);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEntryPointClass(Model model, ClassDescriptor cd) {
/*  37 */     boolean r = false;
/*  38 */     for (ClassSection cs : this.sections) {
/*  39 */       if (matches(cs, model, cd)) {
/*  40 */         return true;
/*     */       }
/*     */     } 
/*  43 */     return false;
/*     */   }
/*     */   private boolean matches(ClassSection cs, Model model, ClassDescriptor cd) {
/*     */     int i;
/*     */     boolean bool1;
/*  48 */     String className = cd.getName();
/*     */     
/*  50 */     boolean r = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     if (null != cs.getClassAccess() && (cs.getName() == null || cs.getName() == "") && !cs.getClassAccess().equals(PatternMatchedSection.Access.NONE))
/*     */     {
/*  58 */       r &= cs.getClassAccess().isAccessLevel(cd.getAccess());
/*     */     }
/*     */ 
/*     */     
/*  62 */     String entryClassName = cs.getName();
/*  63 */     if (null == entryClassName || entryClassName.length() == 0) {
/*     */ 
/*     */       
/*  66 */       i = r & ((match(TypePatternSet.Type.NAME, Util.toJavaClass(className), (PatternMatchedSection)cs) || match(TypePatternSet.Type.NAME, className, (PatternMatchedSection)cs)) ? 1 : 0);
/*     */     } else {
/*  68 */       bool1 = i & entryClassName.equals(className);
/*     */     } 
/*     */ 
/*     */     
/*  72 */     if (null != cs.getExtends()) {
/*  73 */       boolean self = cs.getExtends().equals(cd.getName());
/*  74 */       if (!self) {
/*  75 */         Collection<String> ancestors = cd.getAllAncestorClasses(model);
/*  76 */         bool1 &= ancestors.contains(cs.getExtends());
/*     */       } else {
/*  78 */         bool1 &= self;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  83 */     if (null != cs.getImplements()) {
/*  84 */       boolean self = cs.getImplements().equals(cd.getName());
/*  85 */       if (!self) {
/*  86 */         Collection<String> interfaces = cd.getAllImplementedInterfaces(model);
/*  87 */         bool1 &= interfaces.contains(cs.getImplements());
/*     */       } else {
/*  89 */         bool1 &= self;
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     return bool1;
/*     */   }
/*     */ 
/*     */   
/*     */   private List<ClassSection> getAllMatchingClassSections(Model model, ClassDescriptor cd) {
/*  98 */     List<ClassSection> matchingSections = new ArrayList<>();
/*     */     
/* 100 */     for (ClassSection cs : this.sections) {
/* 101 */       if (matches(cs, model, cd)) {
/* 102 */         matchingSections.add(cs);
/*     */       }
/*     */     } 
/*     */     
/* 106 */     return matchingSections;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEntryPointField(Model model, ClassDescriptor cd, FieldDescriptor fd) {
/* 112 */     for (ClassSection cs : getAllMatchingClassSections(model, cd)) {
/*     */       
/* 114 */       boolean r = false;
/*     */       
/* 116 */       PatternMatchedSection.Access acc = cs.getFieldAccess();
/* 117 */       if (null != acc) {
/* 118 */         r = acc.isAccessLevel(fd.getAccess());
/*     */       }
/*     */       
/* 121 */       if (r) {
/* 122 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEntryPointMethod(Model model, ClassDescriptor cd, MethodDescriptor md) {
/* 132 */     for (ClassSection cs : getAllMatchingClassSections(model, cd)) {
/*     */       
/* 134 */       boolean r = true;
/*     */       
/* 136 */       PatternMatchedSection.Access acc = cs.getMethodAccess();
/* 137 */       if (null != acc) {
/* 138 */         r = (r && acc.isAccessLevel(md.getAccess()));
/*     */       }
/*     */       
/* 141 */       if (r) {
/* 142 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 146 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addClassSection(ClassSection cs) {
/* 155 */     if (null == this.sections) {
/* 156 */       this.sections = new ArrayList<>(5);
/*     */     }
/* 158 */     this.sections.add(cs);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/ClassFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */