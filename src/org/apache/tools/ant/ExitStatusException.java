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
/*    */ public class ExitStatusException
/*    */   extends BuildException
/*    */ {
/*    */   private static final long serialVersionUID = 7760846806886585968L;
/*    */   private int status;
/*    */   
/*    */   public ExitStatusException(int status) {
/* 38 */     this.status = status;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExitStatusException(String msg, int status) {
/* 47 */     super(msg);
/* 48 */     this.status = status;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExitStatusException(String message, int status, Location location) {
/* 58 */     super(message, location);
/* 59 */     this.status = status;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getStatus() {
/* 67 */     return this.status;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/ExitStatusException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */