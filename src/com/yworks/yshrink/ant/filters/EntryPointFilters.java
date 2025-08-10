/*    */ package com.yworks.yshrink.ant.filters;
/*    */ 
/*    */ import com.yworks.common.ant.Exclude;
/*    */ import com.yworks.yshrink.model.ClassDescriptor;
/*    */ import com.yworks.yshrink.model.FieldDescriptor;
/*    */ import com.yworks.yshrink.model.MethodDescriptor;
/*    */ import com.yworks.yshrink.model.Model;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class EntryPointFilters
/*    */   extends AbstractEntryPointFilter
/*    */ {
/* 30 */   List<EntryPointFilter> filters = new ArrayList<>();
/*    */ 
/*    */ 
/*    */   
/*    */   private Exclude exclude;
/*    */ 
/*    */ 
/*    */   
/*    */   public void setExclude(Exclude exclude) {
/* 39 */     this.exclude = exclude;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addEntryPointFilter(EntryPointFilter entryPointFilter) {
/* 48 */     this.filters.add(entryPointFilter);
/*    */   }
/*    */   
/*    */   public boolean isEntryPointClass(Model model, ClassDescriptor cd) {
/* 52 */     for (EntryPointFilter entryPointFilter : this.filters) {
/* 53 */       if (entryPointFilter.isEntryPointClass(model, cd)) {
/* 54 */         return true;
/*    */       }
/*    */     } 
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isEntryPointMethod(Model model, ClassDescriptor cd, MethodDescriptor md) {
/* 61 */     for (EntryPointFilter entryPointFilter : this.filters) {
/* 62 */       if (entryPointFilter.isEntryPointMethod(model, cd, md)) {
/* 63 */         return true;
/*    */       }
/*    */     } 
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isEntryPointField(Model model, ClassDescriptor cd, FieldDescriptor fd) {
/* 70 */     for (EntryPointFilter entryPointFilter : this.filters) {
/* 71 */       if (entryPointFilter.isEntryPointField(model, cd, fd)) {
/* 72 */         return true;
/*    */       }
/*    */     } 
/* 75 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setRetainAttribute(ClassDescriptor cd) {
/* 80 */     for (EntryPointFilter entryPointFilter : this.filters)
/* 81 */       entryPointFilter.setRetainAttribute(cd); 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/filters/EntryPointFilters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */