/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.TaskAdapter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Taskdef
/*    */   extends Typedef
/*    */ {
/*    */   public Taskdef() {
/* 47 */     setAdapterClass(TaskAdapter.class);
/* 48 */     setAdaptToClass(Task.class);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Taskdef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */