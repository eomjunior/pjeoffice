/*     */ package com.yworks.yshrink;
/*     */ 
/*     */ import com.yworks.common.ShrinkBag;
/*     */ import com.yworks.common.ant.EntryPointsSection;
/*     */ import com.yworks.common.ant.PatternMatchedSection;
/*     */ import com.yworks.common.ant.TypePatternSet;
/*     */ import com.yworks.common.ant.YGuardBaseTask;
/*     */ import com.yworks.yguard.ant.ClassSection;
/*     */ import com.yworks.yguard.ant.FieldSection;
/*     */ import com.yworks.yguard.ant.MethodSection;
/*     */ import com.yworks.yguard.ant.PatternMatchedClassesSection;
/*     */ import com.yworks.yshrink.ant.ClassSection;
/*     */ import com.yworks.yshrink.ant.FieldSection;
/*     */ import com.yworks.yshrink.ant.MethodSection;
/*     */ import com.yworks.yshrink.ant.ShrinkTask;
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.PatternSet;
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
/*     */ public class YShrinkInvokerImpl
/*     */   implements YShrinkInvoker
/*     */ {
/*  40 */   final ShrinkTask shrinkTask = new ShrinkTask();
/*  41 */   EntryPointsSection eps = new EntryPointsSection((YGuardBaseTask)this.shrinkTask);
/*     */ 
/*     */   
/*     */   public void setEntyPoints(EntryPointsSection eps) {
/*  45 */     this.eps = eps;
/*     */   }
/*     */   
/*     */   public void setLogFile(File shrinkLog) {
/*  49 */     this.shrinkTask.setLogFile(shrinkLog);
/*     */   }
/*     */   
/*     */   public void setContext(Task task) {
/*  53 */     this.shrinkTask.setProject(task.getProject());
/*  54 */     this.shrinkTask.setOwningTarget(task.getOwningTarget());
/*  55 */     this.shrinkTask.setTaskName(task.getTaskName());
/*  56 */     this.shrinkTask.setLocation(task.getLocation());
/*  57 */     this.shrinkTask.setDescription(task.getDescription());
/*  58 */     this.shrinkTask.init();
/*     */   }
/*     */   
/*     */   public void execute() {
/*  62 */     this.shrinkTask.setEntryPointsExternally(this.eps);
/*  63 */     this.shrinkTask.execute();
/*     */   }
/*     */   
/*     */   public void addPair(ShrinkBag pair) {
/*  67 */     this.shrinkTask.addConfiguredInOutPair(pair);
/*     */   }
/*     */   
/*     */   public void setResourceClassPath(Path path) {
/*  71 */     this.shrinkTask.setResourceClassPath(path);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addClassSection(ClassSection cs) {
/*  76 */     ClassSection yShrinkCS = new ClassSection();
/*     */     
/*  78 */     addPatternSets((PatternMatchedClassesSection)cs, (PatternMatchedSection)yShrinkCS, "name");
/*     */     
/*  80 */     yShrinkCS.setClasses(convertAccess(cs.getClassMode()).name());
/*  81 */     yShrinkCS.setFields(convertAccess(cs.getFieldMode()).name());
/*  82 */     yShrinkCS.setMethods(convertAccess(cs.getMethodMode()).name());
/*     */     
/*  84 */     if (null != cs.getName()) {
/*  85 */       yShrinkCS.setName(cs.getName());
/*     */     }
/*  87 */     this.eps.addConfiguredClass(yShrinkCS);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMethodSection(MethodSection ms) {
/*  92 */     MethodSection yShrinkMS = new MethodSection();
/*     */     
/*  94 */     addPatternSets((PatternMatchedClassesSection)ms, (PatternMatchedSection)yShrinkMS, "class");
/*     */     
/*  96 */     yShrinkMS.setName(ms.getName());
/*  97 */     yShrinkMS.setClass(ms.getClassName());
/*     */     
/*  99 */     this.eps.addConfiguredMethod(yShrinkMS);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFieldSection(FieldSection fs) {
/* 104 */     FieldSection yShrinkFS = new FieldSection();
/*     */     
/* 106 */     addPatternSets((PatternMatchedClassesSection)fs, (PatternMatchedSection)yShrinkFS, "class");
/*     */     
/* 108 */     yShrinkFS.setName(fs.getName());
/* 109 */     yShrinkFS.setClass(fs.getClassName());
/*     */     
/* 111 */     this.eps.addConfiguredField(yShrinkFS);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addPatternSets(PatternMatchedClassesSection yGuardSection, PatternMatchedSection yShrinkSection, String type) {
/* 116 */     if (null != yGuardSection.getPatternSets()) {
/* 117 */       for (PatternSet ps : yGuardSection.getPatternSets()) {
/* 118 */         TypePatternSet tps = new TypePatternSet();
/* 119 */         tps.append(ps, this.shrinkTask.getProject());
/* 120 */         tps.setType(type);
/* 121 */         yShrinkSection.addPatternSet((PatternSet)tps, tps.getType());
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static PatternMatchedSection.Access convertAccess(int yGuardAccess) {
/* 128 */     switch (yGuardAccess) {
/*     */       case 4103:
/* 130 */         return PatternMatchedSection.Access.PRIVATE;
/*     */       case 4101:
/* 132 */         return PatternMatchedSection.Access.FRIENDLY;
/*     */       case 5:
/* 134 */         return PatternMatchedSection.Access.PROTECTED;
/*     */       case 1:
/* 136 */         return PatternMatchedSection.Access.PUBLIC;
/*     */     } 
/* 138 */     return PatternMatchedSection.Access.NONE;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/YShrinkInvokerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */