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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnsupportedElementException
/*    */   extends BuildException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final String element;
/*    */   
/*    */   public UnsupportedElementException(String msg, String element) {
/* 45 */     super(msg);
/* 46 */     this.element = element;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getElement() {
/* 55 */     return this.element;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/UnsupportedElementException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */