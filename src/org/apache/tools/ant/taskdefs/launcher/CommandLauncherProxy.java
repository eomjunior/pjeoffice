/*    */ package org.apache.tools.ant.taskdefs.launcher;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.tools.ant.Project;
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
/*    */ public class CommandLauncherProxy
/*    */   extends CommandLauncher
/*    */ {
/*    */   private final CommandLauncher myLauncher;
/*    */   
/*    */   protected CommandLauncherProxy(CommandLauncher launcher) {
/* 32 */     this.myLauncher = launcher;
/*    */   }
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
/*    */   public Process exec(Project project, String[] cmd, String[] env) throws IOException {
/* 52 */     return this.myLauncher.exec(project, cmd, env);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/launcher/CommandLauncherProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */