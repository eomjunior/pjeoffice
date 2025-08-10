/*    */ package org.apache.tools.ant.taskdefs.optional.net;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ @Deprecated
/*    */ public class MimeMail
/*    */   extends EmailTask
/*    */ {
/*    */   public void execute() throws BuildException {
/* 41 */     log("DEPRECATED - The " + getTaskName() + " task is deprecated. Use the mail task instead.");
/*    */     
/* 43 */     super.execute();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/net/MimeMail.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */