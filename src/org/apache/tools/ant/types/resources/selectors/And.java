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
/*    */ public class And
/*    */   extends ResourceSelectorContainer
/*    */   implements ResourceSelector
/*    */ {
/*    */   public And() {}
/*    */   
/*    */   public And(ResourceSelector... r) {
/* 39 */     super(r);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSelected(Resource r) {
/* 48 */     return getResourceSelectors().stream().allMatch(s -> s.isSelected(r));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/And.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */