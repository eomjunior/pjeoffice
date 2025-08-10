/*    */ package org.apache.tools.ant.dispatch;
/*    */ 
/*    */ import org.apache.tools.ant.Task;
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
/*    */ public abstract class DispatchTask
/*    */   extends Task
/*    */   implements Dispatchable
/*    */ {
/*    */   private String action;
/*    */   
/*    */   public String getActionParameterName() {
/* 41 */     return "action";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAction(String action) {
/* 49 */     this.action = action;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getAction() {
/* 57 */     return this.action;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/dispatch/DispatchTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */