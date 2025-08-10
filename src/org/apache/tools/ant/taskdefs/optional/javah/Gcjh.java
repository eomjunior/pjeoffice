/*    */ package org.apache.tools.ant.taskdefs.optional.javah;
/*    */ 
/*    */ import org.apache.tools.ant.BuildException;
/*    */ import org.apache.tools.ant.Task;
/*    */ import org.apache.tools.ant.taskdefs.Execute;
/*    */ import org.apache.tools.ant.taskdefs.optional.Javah;
/*    */ import org.apache.tools.ant.types.Commandline;
/*    */ import org.apache.tools.ant.types.Path;
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
/*    */ public class Gcjh
/*    */   implements JavahAdapter
/*    */ {
/*    */   public static final String IMPLEMENTATION_NAME = "gcjh";
/*    */   
/*    */   public boolean compile(Javah javah) throws BuildException {
/* 40 */     Commandline cmd = setupGcjhCommand(javah);
/*    */     try {
/* 42 */       Execute.runCommand((Task)javah, cmd.getCommandline());
/* 43 */       return true;
/* 44 */     } catch (BuildException e) {
/* 45 */       if (!e.getMessage().contains("failed with return code")) {
/* 46 */         throw e;
/*    */       }
/*    */       
/* 49 */       return false;
/*    */     } 
/*    */   }
/*    */   private Commandline setupGcjhCommand(Javah javah) {
/* 53 */     Commandline cmd = new Commandline();
/* 54 */     cmd.setExecutable(JavaEnvUtils.getJdkExecutable("gcjh"));
/*    */     
/* 56 */     if (javah.getDestdir() != null) {
/* 57 */       cmd.createArgument().setValue("-d");
/* 58 */       cmd.createArgument().setFile(javah.getDestdir());
/*    */     } 
/*    */     
/* 61 */     if (javah.getOutputfile() != null) {
/* 62 */       cmd.createArgument().setValue("-o");
/* 63 */       cmd.createArgument().setFile(javah.getOutputfile());
/*    */     } 
/*    */     
/* 66 */     Path cp = new Path(javah.getProject());
/* 67 */     if (javah.getBootclasspath() != null) {
/* 68 */       cp.append(javah.getBootclasspath());
/*    */     }
/* 70 */     cp = cp.concatSystemBootClasspath("ignore");
/* 71 */     if (javah.getClasspath() != null) {
/* 72 */       cp.append(javah.getClasspath());
/*    */     }
/* 74 */     if (cp.size() > 0) {
/* 75 */       cmd.createArgument().setValue("--classpath");
/* 76 */       cmd.createArgument().setPath(cp);
/*    */     } 
/*    */     
/* 79 */     if (!javah.getOld()) {
/* 80 */       cmd.createArgument().setValue("-jni");
/*    */     }
/*    */     
/* 83 */     cmd.addArguments(javah.getCurrentArgs());
/*    */     
/* 85 */     javah.logAndAddFiles(cmd);
/* 86 */     return cmd;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/javah/Gcjh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */