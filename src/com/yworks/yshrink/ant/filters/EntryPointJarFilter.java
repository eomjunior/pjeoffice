/*    */ package com.yworks.yshrink.ant.filters;
/*    */ 
/*    */ import com.yworks.common.ant.EntryPointJar;
/*    */ import com.yworks.yshrink.model.ClassDescriptor;
/*    */ import com.yworks.yshrink.model.FieldDescriptor;
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
/*    */ 
/*    */ 
/*    */ public class EntryPointJarFilter
/*    */   extends AbstractEntryPointFilter
/*    */ {
/*    */   private final EntryPointJar entryPointJar;
/*    */   
/*    */   public EntryPointJarFilter(EntryPointJar entryPointJar) {
/* 24 */     this.entryPointJar = entryPointJar;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEntryPointClass(Model model, ClassDescriptor cd) {
/* 29 */     return cd.getSourceJar().equals(this.entryPointJar.getIn());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEntryPointMethod(Model model, ClassDescriptor cd, MethodDescriptor md) {
/* 34 */     return md.getSourceJar().equals(this.entryPointJar.getIn());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEntryPointField(Model model, ClassDescriptor cd, FieldDescriptor fd) {
/* 39 */     return fd.getSourceJar().equals(this.entryPointJar.getIn());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/EntryPointJarFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */