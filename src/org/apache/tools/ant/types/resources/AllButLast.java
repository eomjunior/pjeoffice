/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.stream.Collectors;
/*    */ import org.apache.tools.ant.types.Resource;
/*    */ import org.apache.tools.ant.types.ResourceCollection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AllButLast
/*    */   extends SizeLimitCollection
/*    */ {
/*    */   protected Collection<Resource> getCollection() {
/* 41 */     int ct = getValidCount();
/* 42 */     ResourceCollection nested = getResourceCollection();
/* 43 */     if (ct > nested.size()) {
/* 44 */       return Collections.emptyList();
/*    */     }
/* 46 */     return (Collection<Resource>)nested.stream().limit(nested.size() - ct)
/* 47 */       .collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized int size() {
/* 52 */     return Math.max(getResourceCollection().size() - getValidCount(), 0);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/AllButLast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */