/*    */ package org.apache.tools.ant.taskdefs.compilers;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
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
/*    */ public class Sj
/*    */   extends DefaultCompilerAdapter
/*    */ {
/*    */   public boolean execute() throws BuildException {
/* 40 */     this.attributes.log("Using symantec java compiler", 3);
/*    */     
/* 42 */     Commandline cmd = setupJavacCommand();
/* 43 */     String exec = getJavac().getExecutable();
/* 44 */     cmd.setExecutable((exec == null) ? "sj" : exec);
/*    */     
/* 46 */     int firstFileName = cmd.size() - this.compileList.length;
/*    */     
/* 48 */     return 
/* 49 */       (executeExternalCompile(cmd.getCommandline(), firstFileName) == 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getNoDebugArgument() {
/* 60 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/Sj.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */