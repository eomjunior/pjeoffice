/*    */ package com.yworks.yshrink.ant.filters;
/*    */ 
/*    */ import com.yworks.yshrink.model.ClassDescriptor;
/*    */ import com.yworks.yshrink.model.FieldDescriptor;
/*    */ import com.yworks.yshrink.model.MethodDescriptor;
/*    */ import com.yworks.yshrink.model.Model;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AbstractEntryPointFilter
/*    */   implements EntryPointFilter
/*    */ {
/*    */   public boolean isEntryPointClass(Model model, ClassDescriptor cd) {
/* 16 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isEntryPointMethod(Model model, ClassDescriptor cd, MethodDescriptor md) {
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isEntryPointField(Model model, ClassDescriptor cd, FieldDescriptor fd) {
/* 24 */     return false;
/*    */   }
/*    */   
/*    */   public void setRetainAttribute(ClassDescriptor cd) {}
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/AbstractEntryPointFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */