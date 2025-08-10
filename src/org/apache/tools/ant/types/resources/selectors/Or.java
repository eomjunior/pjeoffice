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
/*    */ public class Or
/*    */   extends ResourceSelectorContainer
/*    */   implements ResourceSelector
/*    */ {
/*    */   public Or() {}
/*    */   
/*    */   public Or(ResourceSelector... r) {
/* 39 */     super(r);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSelected(Resource r) {
/* 48 */     return getResourceSelectors().stream().anyMatch(s -> s.isSelected(r));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Or.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */