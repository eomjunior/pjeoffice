/*    */ package org.apache.tools.ant.taskdefs.optional.javah;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Project;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.taskdefs.Execute;
/*    */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*    */ import org.apache.tools.ant.taskdefs.LogStreamHandler;
/*    */ import org.apache.tools.ant.taskdefs.optional.Javah;
/*    */ import org.apache.tools.ant.types.Commandline;
/*    */ import org.apache.tools.ant.util.JavaEnvUtils;
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
/*    */ public class ForkingJavah
/*    */   implements JavahAdapter
/*    */ {
/*    */   public static final String IMPLEMENTATION_NAME = "forking";
/*    */   
/*    */   public boolean compile(Javah javah) throws BuildException {
/* 50 */     Commandline cmd = SunJavah.setupJavahCommand(javah);
/* 51 */     Project project = javah.getProject();
/* 52 */     String executable = JavaEnvUtils.getJdkExecutable("javah");
/* 53 */     javah.log("Running " + executable, 3);
/* 54 */     cmd.setExecutable(executable);
/*    */ 
/*    */     
/* 57 */     String[] args = cmd.getCommandline();
/*    */     
/*    */     try {
/* 60 */       Execute exe = new Execute((ExecuteStreamHandler)new LogStreamHandler((Task)javah, 2, 1));
/*    */ 
/*    */       
/* 63 */       exe.setAntRun(project);
/* 64 */       exe.setWorkingDirectory(project.getBaseDir());
/* 65 */       exe.setCommandline(args);
/* 66 */       exe.execute();
/* 67 */       return !exe.isFailure();
/* 68 */     } catch (IOException exception) {
/* 69 */       throw new BuildException("Error running " + executable + " -maybe it is not on the path", exception);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/javah/ForkingJavah.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */