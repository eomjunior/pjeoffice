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
/*    */ 
/*    */ public class IsSet
/*    */   extends ProjectComponent
/*    */   implements Condition
/*    */ {
/*    */   private String property;
/*    */   
/*    */   public void setProperty(String p) {
/* 37 */     this.property = p;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean eval() throws BuildException {
/* 46 */     if (this.property == null) {
/* 47 */       throw new BuildException("No property specified for isset condition");
/*    */     }
/*    */     
/* 50 */     return (getProject().getProperty(this.property) != null);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/condition/IsSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */