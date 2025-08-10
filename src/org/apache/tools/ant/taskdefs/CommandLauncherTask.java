/*    */ package org.apache.tools.ant.taskdefs;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.taskdefs.launcher.CommandLauncher;
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
/*    */ public class CommandLauncherTask
/*    */   extends Task
/*    */ {
/*    */   private boolean vmLauncher;
/*    */   private CommandLauncher commandLauncher;
/*    */   
/*    */   public synchronized void addConfigured(CommandLauncher commandLauncher) {
/* 35 */     if (this.commandLauncher != null) {
/* 36 */       throw new BuildException("Only one CommandLauncher can be installed");
/*    */     }
/* 38 */     this.commandLauncher = commandLauncher;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute() {
/* 43 */     if (this.commandLauncher != null) {
/* 44 */       if (this.vmLauncher) {
/* 45 */         CommandLauncher.setVMLauncher(getProject(), this.commandLauncher);
/*    */       } else {
/* 47 */         CommandLauncher.setShellLauncher(getProject(), this.commandLauncher);
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   public void setVmLauncher(boolean vmLauncher) {
/* 53 */     this.vmLauncher = vmLauncher;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/CommandLauncherTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */