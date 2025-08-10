/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.property.LocalProperties;
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
/*    */ public class Local
/*    */   extends Task
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public void setName(String name) {
/* 35 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() {
/* 42 */     if (this.name == null) {
/* 43 */       throw new BuildException("Missing attribute name");
/*    */     }
/* 45 */     LocalProperties.get(getProject()).addLocal(this.name);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Local.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */