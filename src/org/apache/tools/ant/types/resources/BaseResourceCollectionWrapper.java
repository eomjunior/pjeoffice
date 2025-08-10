/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import org.apache.tools.ant.types.Resource;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BaseResourceCollectionWrapper
/*    */   extends AbstractResourceCollectionWrapper
/*    */ {
/* 33 */   private Collection<Resource> coll = null;
/*    */ 
/*    */   
/*    */   protected Iterator<Resource> createIterator() {
/* 37 */     return cacheCollection().iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSize() {
/* 42 */     return cacheCollection().size();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Collection<Resource> getCollection();
/*    */ 
/*    */ 
/*    */   
/*    */   private synchronized Collection<Resource> cacheCollection() {
/* 52 */     if (this.coll == null || !isCache()) {
/* 53 */       this.coll = getCollection();
/*    */     }
/* 55 */     return this.coll;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/BaseResourceCollectionWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */