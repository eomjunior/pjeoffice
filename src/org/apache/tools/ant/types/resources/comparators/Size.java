/*    */ package org.apache.tools.ant.types.resources.comparators;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ public class Size
/*    */   extends ResourceComparator
/*    */ {
/*    */   protected int resourceCompare(Resource foo, Resource bar) {
/* 37 */     return Comparator.<Resource>comparingLong(Resource::getSize).compare(foo, bar);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/comparators/Size.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */