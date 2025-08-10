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
/*    */ public class WinNTCommandLauncher
/*    */   extends CommandLauncherProxy
/*    */ {
/*    */   public WinNTCommandLauncher(CommandLauncher launcher) {
/* 32 */     super(launcher);
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
/* 54 */     File commandDir = workingDir;
/* 55 */     if (workingDir == null) {
/* 56 */       if (project != null) {
/* 57 */         commandDir = project.getBaseDir();
/*    */       } else {
/* 59 */         return exec(project, cmd, env);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 64 */     int preCmdLength = 6;
/* 65 */     String[] newcmd = new String[cmd.length + 6];
/*    */     
/* 67 */     newcmd[0] = "cmd";
/* 68 */     newcmd[1] = "/c";
/* 69 */     newcmd[2] = "cd";
/* 70 */     newcmd[3] = "/d";
/* 71 */     newcmd[4] = commandDir.getAbsolutePath();
/* 72 */     newcmd[5] = "&&";
/*    */     
/* 74 */     System.arraycopy(cmd, 0, newcmd, 6, cmd.length);
/*    */     
/* 76 */     return exec(project, newcmd, env);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/launcher/WinNTCommandLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */