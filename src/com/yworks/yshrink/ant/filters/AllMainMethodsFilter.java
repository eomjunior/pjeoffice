/*    */ package com.yworks.yshrink.ant.filters;
/*    */ 
/*    */ import com.yworks.logging.Logger;
/*    */ import com.yworks.yshrink.model.ClassDescriptor;
/*    */ import com.yworks.yshrink.model.MethodDescriptor;
/*    */ import com.yworks.yshrink.model.Model;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AllMainMethodsFilter
/*    */   extends AbstractEntryPointFilter
/*    */ {
/* 18 */   static String MAIN_DESC = "([Ljava/lang/String;)V";
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEntryPointMethod(Model model, ClassDescriptor cd, MethodDescriptor md) {
/* 23 */     if ("main".equals(md.getName())) {
/* 24 */       Logger.log("MainMethodFilter: main found in " + cd.getName());
/* 25 */       return true;
/*    */     } 
/* 27 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/AllMainMethodsFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */