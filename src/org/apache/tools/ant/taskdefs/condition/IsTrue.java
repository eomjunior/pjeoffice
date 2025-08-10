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
/*    */ public class IsTrue
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
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 49 */     if (this.value == null) {
/* 50 */       throw new BuildException("Nothing to test for truth");
/*    */     }
/* 52 */     return this.value.booleanValue();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsTrue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */