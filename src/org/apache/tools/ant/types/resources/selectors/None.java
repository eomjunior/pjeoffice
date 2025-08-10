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
/*    */ public class None
/*    */   extends ResourceSelectorContainer
/*    */   implements ResourceSelector
/*    */ {
/*    */   public None() {}
/*    */   
/*    */   public None(ResourceSelector... r) {
/* 40 */     super(r);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSelected(Resource r) {
/* 49 */     return getResourceSelectors().stream().noneMatch(s -> s.isSelected(r));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/None.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */