/*    */ package com.yworks.yshrink.ant;
/*    */ 
/*    */ import com.yworks.logging.Logger;
/*    */ import org.apache.tools.ant.Project;
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
/*    */ public class AntLogger
/*    */   extends Logger
/*    */ {
/*    */   private final Task task;
/*    */   private final Project project;
/*    */   
/*    */   public AntLogger(Project project, Task task) {
/* 24 */     this.project = project;
/* 25 */     this.task = task;
/* 26 */     register();
/*    */   }
/*    */   
/*    */   public void doLog(String s) {
/* 30 */     this.project.log(this.task, s, 2);
/*    */   }
/*    */   
/*    */   public void doErr(String s) {
/* 34 */     this.project.log(this.task, "ERROR: " + s, 0);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void doWarn(String s) {}
/*    */ 
/*    */   
/*    */   public void doWarnToLog(String s) {}
/*    */ 
/*    */   
/*    */   public void doShrinkLog(String s) {}
/*    */ 
/*    */   
/*    */   public void doErr(String s, Throwable ex) {
/* 49 */     this.project.log(this.task, "ERROR: " + s + "\n" + ex.getMessage(), 0);
/*    */   }
/*    */   
/*    */   public void close() {
/* 53 */     unregister();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/yworks/yshrink/ant/AntLogger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */