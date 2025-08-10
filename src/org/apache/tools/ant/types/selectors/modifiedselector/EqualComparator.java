/*    */ package org.apache.tools.ant.types.selectors.modifiedselector;
/*    */ 
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EqualComparator
/*    */   implements Comparator<Object>
/*    */ {
/*    */   public int compare(Object o1, Object o2) {
/* 42 */     if (o1 == null) {
/* 43 */       if (o2 == null) {
/* 44 */         return 1;
/*    */       }
/* 46 */       return 0;
/*    */     } 
/* 48 */     return o1.equals(o2) ? 0 : 1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "EqualComparator";
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/selectors/modifiedselector/EqualComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */