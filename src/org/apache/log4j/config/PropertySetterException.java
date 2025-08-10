/*    */ package org.apache.log4j.config;
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
/*    */ public class PropertySetterException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = -1352613734254235861L;
/*    */   protected Throwable rootCause;
/*    */   
/*    */   public PropertySetterException(String msg) {
/* 32 */     super(msg);
/*    */   }
/*    */ 
/*    */   
/*    */   public PropertySetterException(Throwable rootCause) {
/* 37 */     this.rootCause = rootCause;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 44 */     String msg = super.getMessage();
/* 45 */     if (msg == null && this.rootCause != null) {
/* 46 */       msg = this.rootCause.getMessage();
/*    */     }
/* 48 */     return msg;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/log4j/config/PropertySetterException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */