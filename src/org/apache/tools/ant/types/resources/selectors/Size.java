/*    */ package org.apache.tools.ant.types.resources.selectors;
/*    */ 
/*    */ import org.apache.tools.ant.types.Comparison;
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
/*    */ public class Size
/*    */   implements ResourceSelector
/*    */ {
/* 28 */   private long size = -1L;
/* 29 */   private Comparison when = Comparison.EQUAL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSize(long l) {
/* 36 */     this.size = l;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getSize() {
/* 44 */     return this.size;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setWhen(Comparison c) {
/* 52 */     this.when = c;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Comparison getWhen() {
/* 60 */     return this.when;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSelected(Resource r) {
/* 69 */     long diff = r.getSize() - this.size;
/* 70 */     return this.when.evaluate((diff == 0L) ? 0 : (int)(diff / Math.abs(diff)));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Size.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */