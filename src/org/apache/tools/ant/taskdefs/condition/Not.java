/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class Not
/*    */   extends ConditionBase
/*    */   implements Condition
/*    */ {
/*    */   public boolean eval() throws BuildException {
/* 41 */     if (countConditions() > 1) {
/* 42 */       throw new BuildException("You must not nest more than one condition into <not>");
/*    */     }
/*    */     
/* 45 */     if (countConditions() < 1) {
/* 46 */       throw new BuildException("You must nest a condition into <not>");
/*    */     }
/* 48 */     return !((Condition)getConditions().nextElement()).eval();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/Not.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */