/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class Nice
/*    */   extends Task
/*    */ {
/*    */   private Integer newPriority;
/*    */   private String currentPriority;
/*    */   
/*    */   public void execute() throws BuildException {
/* 58 */     Thread self = Thread.currentThread();
/* 59 */     int priority = self.getPriority();
/* 60 */     if (this.currentPriority != null) {
/* 61 */       String current = Integer.toString(priority);
/* 62 */       getProject().setNewProperty(this.currentPriority, current);
/*    */     } 
/*    */     
/* 65 */     if (this.newPriority != null && priority != this.newPriority.intValue()) {
/*    */       try {
/* 67 */         self.setPriority(this.newPriority.intValue());
/* 68 */       } catch (SecurityException e) {
/*    */         
/* 70 */         log("Unable to set new priority -a security manager is in the way", 1);
/*    */       }
/* 72 */       catch (IllegalArgumentException iae) {
/* 73 */         throw new BuildException("Priority out of range", iae);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCurrentPriority(String currentPriority) {
/* 84 */     this.currentPriority = currentPriority;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNewPriority(int newPriority) {
/* 92 */     if (newPriority < 1 || newPriority > 10) {
/* 93 */       throw new BuildException("The thread priority is out of the range 1-10");
/*    */     }
/* 95 */     this.newPriority = Integer.valueOf(newPriority);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Nice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */