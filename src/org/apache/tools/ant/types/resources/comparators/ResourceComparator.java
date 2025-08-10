/*    */ package org.apache.tools.ant.types.resources.comparators;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import org.apache.tools.ant.types.DataType;
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
/*    */ 
/*    */ public abstract class ResourceComparator
/*    */   extends DataType
/*    */   implements Comparator<Resource>
/*    */ {
/*    */   public final int compare(Resource foo, Resource bar) {
/* 41 */     dieOnCircularReference();
/* 42 */     ResourceComparator c = isReference() ? getRef() : this;
/* 43 */     return c.resourceCompare(foo, bar);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 53 */     if (isReference()) {
/* 54 */       return getRef().equals(o);
/*    */     }
/* 56 */     return (o != null && (o == this || o.getClass().equals(getClass())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized int hashCode() {
/* 65 */     if (isReference()) {
/* 66 */       return getRef().hashCode();
/*    */     }
/* 68 */     return getClass().hashCode();
/*    */   }
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
/*    */   private ResourceComparator getRef() {
/* 81 */     return (ResourceComparator)getCheckedRef(ResourceComparator.class);
/*    */   }
/*    */   
/*    */   protected abstract int resourceCompare(Resource paramResource1, Resource paramResource2);
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/comparators/ResourceComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */