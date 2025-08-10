/*    */ package com.yworks.yshrink.ant.filters;
/*    */ 
/*    */ import com.yworks.common.ant.PatternMatchedSection;
/*    */ import com.yworks.common.ant.TypePatternSet;
/*    */ import com.yworks.yshrink.ant.FieldSection;
/*    */ import com.yworks.yshrink.model.ClassDescriptor;
/*    */ import com.yworks.yshrink.model.FieldDescriptor;
/*    */ import com.yworks.yshrink.model.Model;
/*    */ import com.yworks.yshrink.util.Util;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.objectweb.asm.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldFilter
/*    */   extends PatternMatchedFilter
/*    */ {
/*    */   private List<FieldSection> sections;
/*    */   
/*    */   public FieldFilter(Project project) {
/* 30 */     super(project);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addFieldSection(FieldSection fieldSection) {
/* 39 */     if (null == this.sections) {
/* 40 */       this.sections = new ArrayList<>(5);
/*    */     }
/* 42 */     this.sections.add(fieldSection);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEntryPointField(Model model, ClassDescriptor cd, FieldDescriptor fd) {
/* 48 */     String className = cd.getName();
/* 49 */     String fieldName = fd.getName();
/*    */     
/* 51 */     for (FieldSection fs : this.sections) {
/*    */       int i;
/* 53 */       boolean bool1, r = true;
/*    */       
/* 55 */       String entryFieldClass = fs.getClassName();
/* 56 */       String entryFieldName = fs.getName();
/*    */ 
/*    */       
/* 59 */       if (null != fs.getType()) {
/* 60 */         Type requiredType = Type.getType(Util.verboseToNativeType(fs.getType()));
/* 61 */         r &= requiredType.equals(fd.getDesc());
/*    */       } 
/*    */ 
/*    */       
/* 65 */       if (null != fs.getAccess()) {
/* 66 */         r &= fs.getAccess().isAccessLevel(fd.getAccess());
/*    */       }
/*    */ 
/*    */       
/* 70 */       if (null == entryFieldClass || entryFieldClass.length() == 0) {
/*    */         
/* 72 */         i = r & ((match(TypePatternSet.Type.CLASS, className, (PatternMatchedSection)fs) || match(TypePatternSet.Type.CLASS, Util.toJavaClass(className), (PatternMatchedSection)fs)) ? 1 : 0);
/*    */       } else {
/* 74 */         bool1 = i & entryFieldClass.equals(className);
/*    */       } 
/*    */ 
/*    */       
/* 78 */       if (null == entryFieldName || entryFieldName.length() == 0) {
/* 79 */         bool1 &= match(TypePatternSet.Type.NAME, fieldName, (PatternMatchedSection)fs);
/*    */       } else {
/* 81 */         bool1 &= entryFieldName.equals(fieldName);
/*    */       } 
/*    */       
/* 84 */       if (bool1) {
/* 85 */         return bool1;
/*    */       }
/*    */     } 
/*    */     
/* 89 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/FieldFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */