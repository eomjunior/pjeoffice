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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Kaffeh
/*    */   implements JavahAdapter
/*    */ {
/*    */   public static final String IMPLEMENTATION_NAME = "kaffeh";
/*    */   
/*    */   public boolean compile(Javah javah) throws BuildException {
/* 45 */     Commandline cmd = setupKaffehCommand(javah);
/*    */     try {
/* 47 */       Execute.runCommand((Task)javah, cmd.getCommandline());
/* 48 */       return true;
/* 49 */     } catch (BuildException e) {
/* 50 */       if (!e.getMessage().contains("failed with return code")) {
/* 51 */         throw e;
/*    */       }
/*    */       
/* 54 */       return false;
/*    */     } 
/*    */   }
/*    */   private Commandline setupKaffehCommand(Javah javah) {
/* 58 */     Commandline cmd = new Commandline();
/* 59 */     cmd.setExecutable(JavaEnvUtils.getJdkExecutable("kaffeh"));
/*    */     
/* 61 */     if (javah.getDestdir() != null) {
/* 62 */       cmd.createArgument().setValue("-d");
/* 63 */       cmd.createArgument().setFile(javah.getDestdir());
/*    */     } 
/*    */     
/* 66 */     if (javah.getOutputfile() != null) {
/* 67 */       cmd.createArgument().setValue("-o");
/* 68 */       cmd.createArgument().setFile(javah.getOutputfile());
/*    */     } 
/*    */     
/* 71 */     Path cp = new Path(javah.getProject());
/* 72 */     if (javah.getBootclasspath() != null) {
/* 73 */       cp.append(javah.getBootclasspath());
/*    */     }
/* 75 */     cp = cp.concatSystemBootClasspath("ignore");
/* 76 */     if (javah.getClasspath() != null) {
/* 77 */       cp.append(javah.getClasspath());
/*    */     }
/* 79 */     if (cp.size() > 0) {
/* 80 */       cmd.createArgument().setValue("-classpath");
/* 81 */       cmd.createArgument().setPath(cp);
/*    */     } 
/*    */     
/* 84 */     if (!javah.getOld()) {
/* 85 */       cmd.createArgument().setValue("-jni");
/*    */     }
/*    */     
/* 88 */     cmd.addArguments(javah.getCurrentArgs());
/*    */     
/* 90 */     javah.logAndAddFiles(cmd);
/* 91 */     return cmd;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/javah/Kaffeh.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */