/*    */ package org.apache.tools.ant.attribute;
/*    */ 
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.UnknownElement;
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
/*    */ public class IfTrueAttribute
/*    */   extends BaseIfAttribute
/*    */ {
/*    */   public static class Unless
/*    */     extends IfTrueAttribute
/*    */   {
/*    */     public Unless() {
/* 31 */       setPositive(false);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEnabled(UnknownElement el, String value) {
/* 39 */     return convertResult(Project.toBoolean(value));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/attribute/IfTrueAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */