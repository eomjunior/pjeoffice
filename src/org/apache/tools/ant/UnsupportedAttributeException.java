/*    */ package org.apache.tools.ant;
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
/*    */ public class UnsupportedAttributeException
/*    */   extends BuildException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final String attribute;
/*    */   
/*    */   public UnsupportedAttributeException(String msg, String attribute) {
/* 36 */     super(msg);
/* 37 */     this.attribute = attribute;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAttribute() {
/* 46 */     return this.attribute;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/UnsupportedAttributeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */