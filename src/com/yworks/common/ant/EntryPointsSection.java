/*    */ package com.yworks.common.ant;
/*    */ 
/*    */ import com.yworks.yshrink.ant.ClassSection;
/*    */ import com.yworks.yshrink.ant.FieldSection;
/*    */ import com.yworks.yshrink.ant.MethodSection;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntryPointsSection
/*    */   extends Exclude
/*    */ {
/* 15 */   private List<MethodSection> methodSections = new ArrayList<>(5);
/* 16 */   private List<ClassSection> classSections = new ArrayList<>(5);
/* 17 */   private List<FieldSection> fieldSections = new ArrayList<>(5);
/* 18 */   private List<AttributesSection> attributesSections = new ArrayList<>(1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public EntryPointsSection(YGuardBaseTask task) {
/* 26 */     super(task);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addConfiguredMethod(MethodSection ms) {
/* 35 */     this.methodSections.add(ms);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addConfiguredClass(ClassSection cs) {
/* 44 */     this.classSections.add(cs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addConfiguredField(FieldSection fs) {
/* 53 */     this.fieldSections.add(fs);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addConfiguredAttribute(AttributesSection as) {
/* 62 */     this.attributesSections.add(as);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MethodSection> getMethodSections() {
/* 71 */     return this.methodSections;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<ClassSection> getClassSections() {
/* 80 */     return this.classSections;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<FieldSection> getFieldSections() {
/* 89 */     return this.fieldSections;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<AttributesSection> getAttributesSections() {
/* 98 */     return this.attributesSections;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/EntryPointsSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */