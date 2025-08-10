/*    */ package org.apache.tools.ant.types.optional;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.taskdefs.condition.Condition;
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
/*    */ public class ScriptCondition
/*    */   extends AbstractScriptComponent
/*    */   implements Condition
/*    */ {
/*    */   private boolean value = false;
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 46 */     initScriptRunner();
/* 47 */     executeScript("ant_condition");
/* 48 */     return getValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getValue() {
/* 56 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(boolean value) {
/* 67 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/types/optional/ScriptCondition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */