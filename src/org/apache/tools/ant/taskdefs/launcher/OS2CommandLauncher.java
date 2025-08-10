/*    */ package org.apache.tools.ant.taskdefs.launcher;
/*    */ 
/*    */ import java.io.File;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OS2CommandLauncher
/*    */   extends CommandLauncherProxy
/*    */ {
/*    */   public OS2CommandLauncher(CommandLauncher launcher) {
/* 34 */     super(launcher);
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
/*    */ 
/*    */   
/*    */   public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
/* 56 */     File commandDir = workingDir;
/* 57 */     if (workingDir == null) {
/* 58 */       if (project != null) {
/* 59 */         commandDir = project.getBaseDir();
/*    */       } else {
/* 61 */         return exec(project, cmd, env);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 66 */     int preCmdLength = 7;
/* 67 */     String cmdDir = commandDir.getAbsolutePath();
/* 68 */     String[] newcmd = new String[cmd.length + 7];
/*    */     
/* 70 */     newcmd[0] = "cmd";
/* 71 */     newcmd[1] = "/c";
/* 72 */     newcmd[2] = cmdDir.substring(0, 2);
/* 73 */     newcmd[3] = "&&";
/* 74 */     newcmd[4] = "cd";
/* 75 */     newcmd[5] = cmdDir.substring(2);
/* 76 */     newcmd[6] = "&&";
/*    */     
/* 78 */     System.arraycopy(cmd, 0, newcmd, 7, cmd.length);
/*    */     
/* 80 */     return exec(project, newcmd, env);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/launcher/OS2CommandLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */