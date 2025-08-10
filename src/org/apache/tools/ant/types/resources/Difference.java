/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
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
/*    */ public class Difference
/*    */   extends BaseResourceCollectionContainer
/*    */ {
/*    */   protected Collection<Resource> getCollection() {
/* 43 */     List<ResourceCollection> rcs = getResourceCollections();
/* 44 */     int size = rcs.size();
/* 45 */     if (size < 2)
/* 46 */       throw new BuildException("The difference of %d resource %s is undefined.", new Object[] {
/* 47 */             Integer.valueOf(size), 
/* 48 */             (size == 1) ? "collection" : "collections"
/*    */           }); 
/* 50 */     Set<Resource> hs = new HashSet<>();
/* 51 */     List<Resource> al = new ArrayList<>();
/* 52 */     for (ResourceCollection rc : rcs) {
/* 53 */       for (Resource r : rc) {
/* 54 */         if (hs.add(r)) {
/* 55 */           al.add(r); continue;
/*    */         } 
/* 57 */         al.remove(r);
/*    */       } 
/*    */     } 
/*    */     
/* 61 */     return al;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Difference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */