/*    */ package org.apache.tools.ant.types.resources;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
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
/*    */ public class Intersect
/*    */   extends BaseResourceCollectionContainer
/*    */ {
/*    */   protected Collection<Resource> getCollection() {
/* 46 */     List<ResourceCollection> rcs = getResourceCollections();
/* 47 */     int size = rcs.size();
/* 48 */     if (size < 2) {
/* 49 */       throw new BuildException("The intersection of %d resource %s is undefined.", new Object[] {
/* 50 */             Integer.valueOf(size), 
/* 51 */             (size == 1) ? "collection" : "collections"
/*    */           });
/*    */     }
/* 54 */     Function<ResourceCollection, Set<Resource>> toSet = c -> (Set)c.stream().collect(Collectors.toSet());
/*    */ 
/*    */     
/* 57 */     Iterator<ResourceCollection> rc = rcs.iterator();
/* 58 */     Set<Resource> s = new LinkedHashSet<>(toSet.apply(rc.next()));
/* 59 */     rc.forEachRemaining(c -> s.retainAll(toSet.apply(c)));
/* 60 */     return s;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/Intersect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */