/*    */ package org.apache.tools.ant.taskdefs.condition;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.ProjectComponent;
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
/*    */ public class IsFalse
/*    */   extends ProjectComponent
/*    */   implements Condition
/*    */ {
/* 33 */   private Boolean value = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(boolean value) {
/* 40 */     this.value = value ? Boolean.TRUE : Boolean.FALSE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 48 */     if (this.value == null) {
/* 49 */       throw new BuildException("Nothing to test for falsehood");
/*    */     }
/* 51 */     return !this.value.booleanValue();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsFalse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */