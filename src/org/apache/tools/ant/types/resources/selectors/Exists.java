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
/*    */ public class Exists
/*    */   implements ResourceSelector
/*    */ {
/*    */   public boolean isSelected(Resource r) {
/* 34 */     return r.isExists();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Exists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */