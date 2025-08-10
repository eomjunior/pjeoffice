/*    */ package org.apache.tools.ant.attribute;
/*    */ 
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
/*    */ public class IfBlankAttribute
/*    */   extends BaseIfAttribute
/*    */ {
/*    */   public static class Unless
/*    */     extends IfBlankAttribute
/*    */   {
/*    */     public Unless() {
/* 30 */       setPositive(false);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEnabled(UnknownElement el, String value) {
/* 37 */     return convertResult((value == null || value.isEmpty()));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/attribute/IfBlankAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */