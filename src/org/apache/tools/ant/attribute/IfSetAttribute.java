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
/*    */ public class IfSetAttribute
/*    */   extends BaseIfAttribute
/*    */ {
/*    */   public static class Unless
/*    */     extends IfSetAttribute
/*    */   {
/*    */     public Unless() {
/* 30 */       setPositive(false);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isEnabled(UnknownElement el, String value) {
/* 37 */     return convertResult((getProject().getProperty(value) != null));
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/attribute/IfSetAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */