/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class Last
/*    */   extends SizeLimitCollection
/*    */ {
/*    */   protected Collection<Resource> getCollection() {
/* 42 */     int count = getValidCount();
/* 43 */     ResourceCollection rc = getResourceCollection();
/* 44 */     int size = rc.size();
/* 45 */     int skip = Math.max(0, size - count);
/*    */ 
/*    */     
/* 48 */     List<Resource> result = (List<Resource>)rc.stream().skip(skip).collect(Collectors.toList());
/*    */     
/* 50 */     int found = result.size();
/* 51 */     if (found == count || (size < count && found == size)) {
/* 52 */       return result;
/*    */     }
/*    */     
/* 55 */     String msg = String.format("Resource collection %s reports size %d but returns %d elements.", new Object[] { rc, 
/*    */           
/* 57 */           Integer.valueOf(size), Integer.valueOf(found + skip) });
/*    */ 
/*    */     
/* 60 */     if (found > count) {
/* 61 */       log(msg, 1);
/* 62 */       return result.subList(found - count, found);
/*    */     } 
/*    */     
/* 65 */     throw new BuildException(msg);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Last.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */