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
/*    */ public class First
/*    */   extends SizeLimitCollection
/*    */ {
/*    */   protected Collection<Resource> getCollection() {
/* 38 */     return (Collection<Resource>)getResourceCollection().stream().limit(getValidCount())
/* 39 */       .collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/First.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */