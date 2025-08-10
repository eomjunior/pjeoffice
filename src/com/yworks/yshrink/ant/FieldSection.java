/*    */ package com.yworks.yshrink.ant;
/*    */ 
/*    */ import com.yworks.common.ant.PatternMatchedSection;
/*    */ import com.yworks.common.ant.TypePatternSet;
/*    */ import com.yworks.yshrink.util.Util;
/*    */ import org.apache.tools.ant.types.PatternSet;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FieldSection
/*    */   extends PatternMatchedSection
/*    */ {
/*    */   private String name;
/*    */   private String className;
/*    */   private String type;
/*    */   
/*    */   public String getName() {
/* 33 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setName(String name) {
/* 42 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 51 */     return this.className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setClass(String className) {
/* 60 */     this.className = Util.toInternalClass(className);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getType() {
/* 69 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setType(String type) {
/* 78 */     this.type = Util.toInternalClass(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypePatternSet createPatternSet() {
/* 83 */     TypePatternSet typePatternSet = newTypePatternSet();
/* 84 */     typePatternSet.setType("class");
/* 85 */     addPatternSet((PatternSet)typePatternSet, typePatternSet.getType());
/* 86 */     return typePatternSet;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected TypePatternSet newTypePatternSet() {
/* 96 */     return new TypePatternSet();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/FieldSection.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */