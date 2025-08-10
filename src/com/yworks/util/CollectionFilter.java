/*    */ package com.yworks.util;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ public class CollectionFilter
/*    */   implements Filter
/*    */ {
/*    */   private Collection collection;
/*    */   
/*    */   public CollectionFilter(Collection col) {
/* 27 */     this.collection = col;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean accepts(Object o) {
/* 32 */     return (this.collection != null && this.collection.contains(o));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Collection getCollection() {
/* 42 */     return this.collection;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCollection(Collection collection) {
/* 52 */     this.collection = collection;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/util/CollectionFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */