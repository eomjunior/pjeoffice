/*    */ package com.yworks.yshrink.model;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AnnotationUsage
/*    */ {
/*    */   private String descriptor;
/* 14 */   private List<String> fieldUsages = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AnnotationUsage(String descriptor) {
/* 22 */     this.descriptor = descriptor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addFieldUsage(String name) {
/* 31 */     if (name != null) {
/* 32 */       this.fieldUsages.add(name);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDescriptor() {
/* 42 */     return this.descriptor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> getFieldUsages() {
/* 51 */     return this.fieldUsages;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/model/AnnotationUsage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */