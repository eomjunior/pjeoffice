/*    */ package com.yworks.common.ant;
/*    */ 
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
/*    */ public class TypePatternSet
/*    */   extends PatternSet
/*    */ {
/*    */   public enum Type
/*    */   {
/* 19 */     CLASS,
/*    */ 
/*    */ 
/*    */     
/* 23 */     NAME,
/*    */ 
/*    */ 
/*    */     
/* 27 */     PACKAGE,
/*    */ 
/*    */ 
/*    */     
/* 31 */     EXTENDS,
/*    */ 
/*    */ 
/*    */     
/* 35 */     IMPLEMENTS;
/*    */   }
/*    */   
/* 38 */   private Type type = Type.NAME;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Type getType() {
/* 46 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setType(String type) {
/* 55 */     this.type = Type.valueOf(type.toUpperCase());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/common/ant/TypePatternSet.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */