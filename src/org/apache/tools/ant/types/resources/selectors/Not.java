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
/*    */ public class Not
/*    */   implements ResourceSelector
/*    */ {
/*    */   private ResourceSelector sel;
/*    */   
/*    */   public Not() {}
/*    */   
/*    */   public Not(ResourceSelector s) {
/* 41 */     add(s);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(ResourceSelector s) {
/* 50 */     if (this.sel != null) {
/* 51 */       throw new IllegalStateException("The Not ResourceSelector accepts a single nested ResourceSelector");
/*    */     }
/*    */     
/* 54 */     this.sel = s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSelected(Resource r) {
/* 63 */     return !this.sel.isSelected(r);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/resources/selectors/Not.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */