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
/*    */ public class ExitException
/*    */   extends SecurityException
/*    */ {
/*    */   private static final long serialVersionUID = 2772487854280543363L;
/*    */   private int status;
/*    */   
/*    */   public ExitException(int status) {
/* 39 */     super("ExitException: status " + status);
/* 40 */     this.status = status;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExitException(String msg, int status) {
/* 49 */     super(msg);
/* 50 */     this.status = status;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getStatus() {
/* 59 */     return this.status;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/ExitException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */