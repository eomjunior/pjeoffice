/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Collectors;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AllButFirst
/*    */   extends SizeLimitCollection
/*    */ {
/*    */   protected Collection<Resource> getCollection() {
/* 39 */     return (Collection<Resource>)getResourceCollection().stream().skip(getValidCount())
/* 40 */       .collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized int size() {
/* 45 */     return Math.max(getResourceCollection().size() - getValidCount(), 0);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/AllButFirst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */