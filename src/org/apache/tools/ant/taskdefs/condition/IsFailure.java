/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import org.apache.tools.ant.taskdefs.Execute;
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
/*    */ public class IsFailure
/*    */   implements Condition
/*    */ {
/*    */   private int code;
/*    */   
/*    */   public void setCode(int c) {
/* 35 */     this.code = c;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCode() {
/* 43 */     return this.code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() {
/* 51 */     return Execute.isFailure(this.code);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsFailure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */