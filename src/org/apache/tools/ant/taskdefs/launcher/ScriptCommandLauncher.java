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
/*    */ public class ScriptCommandLauncher
/*    */   extends CommandLauncherProxy
/*    */ {
/*    */   private final String myScript;
/*    */   
/*    */   public ScriptCommandLauncher(String script, CommandLauncher launcher) {
/* 34 */     super(launcher);
/* 35 */     this.myScript = script;
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
/* 57 */     if (project == null) {
/* 58 */       if (workingDir == null) {
/* 59 */         return exec(project, cmd, env);
/*    */       }
/* 61 */       throw new IOException("Cannot locate antRun script: No project provided");
/*    */     } 
/*    */ 
/*    */     
/* 65 */     String antHome = project.getProperty("ant.home");
/* 66 */     if (antHome == null) {
/* 67 */       throw new IOException("Cannot locate antRun script: Property 'ant.home' not found");
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 72 */     String antRun = FILE_UTILS.resolveFile(project.getBaseDir(), antHome + File.separator + this.myScript).toString();
/*    */ 
/*    */     
/* 75 */     File commandDir = workingDir;
/* 76 */     if (workingDir == null) {
/* 77 */       commandDir = project.getBaseDir();
/*    */     }
/* 79 */     String[] newcmd = new String[cmd.length + 2];
/* 80 */     newcmd[0] = antRun;
/* 81 */     newcmd[1] = commandDir.getAbsolutePath();
/* 82 */     System.arraycopy(cmd, 0, newcmd, 2, cmd.length);
/*    */     
/* 84 */     return exec(project, newcmd, env);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/launcher/ScriptCommandLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */