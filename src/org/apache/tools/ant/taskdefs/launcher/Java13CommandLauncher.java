/*    */ package org.apache.tools.ant.taskdefs.launcher;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.types.Commandline;
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
/*    */ public class Java13CommandLauncher
/*    */   extends CommandLauncher
/*    */ {
/*    */   public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
/*    */     try {
/* 53 */       if (project != null) {
/* 54 */         project.log("Execute:Java13CommandLauncher: " + 
/* 55 */             Commandline.describeCommand(cmd), 4);
/*    */       }
/*    */       
/* 58 */       return Runtime.getRuntime().exec(cmd, env, workingDir);
/* 59 */     } catch (IOException ioex) {
/* 60 */       throw ioex;
/* 61 */     } catch (Exception exc) {
/*    */       
/* 63 */       throw new BuildException("Unable to execute command", exc);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/launcher/Java13CommandLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */