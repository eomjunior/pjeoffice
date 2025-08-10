/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Vector;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.TaskContainer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Sequential
/*    */   extends Task
/*    */   implements TaskContainer
/*    */ {
/* 44 */   private List<Task> nestedTasks = new Vector<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addTask(Task nestedTask) {
/* 53 */     this.nestedTasks.add(nestedTask);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void execute() throws BuildException {
/* 64 */     LocalProperties localProperties = LocalProperties.get(getProject());
/* 65 */     localProperties.enterScope();
/*    */     try {
/* 67 */       this.nestedTasks.forEach(Task::perform);
/*    */     } finally {
/* 69 */       localProperties.exitScope();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Sequential.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */