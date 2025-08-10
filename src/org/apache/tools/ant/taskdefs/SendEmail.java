/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import org.apache.tools.ant.taskdefs.email.EmailTask;
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
/*    */ public class SendEmail
/*    */   extends EmailTask
/*    */ {
/*    */   @Deprecated
/*    */   public void setMailport(Integer value) {
/* 44 */     setMailport(value.intValue());
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/SendEmail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */