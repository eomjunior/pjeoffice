/*    */ package com.yworks.yshrink.ant.filters;
/*    */ 
/*    */ import com.yworks.common.ant.AttributesSection;
/*    */ import com.yworks.common.ant.PatternMatchedSection;
/*    */ import com.yworks.common.ant.TypePatternSet;
/*    */ import com.yworks.yshrink.model.ClassDescriptor;
/*    */ import com.yworks.yshrink.util.Util;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.tools.ant.Project;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AttributeFilter
/*    */   extends PatternMatchedFilter
/*    */ {
/* 20 */   private List<AttributesSection> sections = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AttributeFilter(Project p) {
/* 28 */     super(p);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addAttributesSection(AttributesSection as) {
/* 37 */     this.sections.add(as);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRetainAttribute(ClassDescriptor cd) {
/* 42 */     String className = cd.getName();
/* 43 */     String javaClassName = Util.toJavaClass(cd.getName());
/*    */     
/* 45 */     for (AttributesSection section : this.sections) {
/* 46 */       if (match(TypePatternSet.Type.NAME, javaClassName, (PatternMatchedSection)section) || 
/* 47 */         match(TypePatternSet.Type.NAME, className, (PatternMatchedSection)section))
/*    */       {
/* 49 */         for (String attr : section.getAttributes())
/* 50 */           cd.setRetainAttribute(attr); 
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/AttributeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */