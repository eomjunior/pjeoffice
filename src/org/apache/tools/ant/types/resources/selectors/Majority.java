/*    */ package org.apache.tools.ant.types.resources.selectors;
/*    */ 
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
/*    */ public class Majority
/*    */   extends ResourceSelectorContainer
/*    */   implements ResourceSelector
/*    */ {
/*    */   private boolean tie = true;
/*    */   
/*    */   public Majority() {}
/*    */   
/*    */   public Majority(ResourceSelector... r) {
/* 42 */     super(r);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void setAllowtie(boolean b) {
/* 50 */     this.tie = b;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized boolean isSelected(Resource r) {
/* 59 */     int passed = 0;
/* 60 */     int failed = 0;
/* 61 */     int count = selectorCount();
/* 62 */     boolean even = (count % 2 == 0);
/* 63 */     int threshold = count / 2;
/*    */     
/* 65 */     for (ResourceSelector rs : getResourceSelectors()) {
/* 66 */       if (rs.isSelected(r)) {
/* 67 */         passed++;
/* 68 */         if (passed > threshold || (even && this.tie && passed == threshold))
/* 69 */           return true; 
/*    */         continue;
/*    */       } 
/* 72 */       failed++;
/* 73 */       if (failed > threshold || (even && !this.tie && failed == threshold)) {
/* 74 */         return false;
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 79 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Majority.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */