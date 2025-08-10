/*     */ package org.apache.tools.ant.taskdefs.rmic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.LogStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.Rmic;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ForkingSunRmic
/*     */   extends DefaultRmicAdapter
/*     */ {
/*     */   public static final String COMPILER_NAME = "forking";
/*     */   
/*     */   protected boolean areIiopAndIdlSupported() {
/*  53 */     boolean supported = !JavaEnvUtils.isAtLeastJavaVersion("11");
/*  54 */     if (!supported && getRmic().getExecutable() != null) {
/*  55 */       getRmic().getProject()
/*  56 */         .log("Allowing -iiop and -idl for forked rmic even though this version of Java doesn't support it.", 2);
/*     */       
/*  58 */       return true;
/*     */     } 
/*  60 */     return supported;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws BuildException {
/*  70 */     Rmic owner = getRmic();
/*  71 */     Commandline cmd = setupRmicCommand();
/*  72 */     Project project = owner.getProject();
/*  73 */     String executable = owner.getExecutable();
/*  74 */     if (executable == null) {
/*  75 */       if (JavaEnvUtils.isAtLeastJavaVersion("15")) {
/*  76 */         throw new BuildException("rmic does not exist under Java 15 and higher, use rmic of an older JDK and explicitly set the executable attribute");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  82 */       executable = JavaEnvUtils.getJdkExecutable(getExecutableName());
/*     */     } 
/*  84 */     cmd.setExecutable(executable);
/*     */ 
/*     */     
/*  87 */     String[] args = cmd.getCommandline();
/*     */     
/*     */     try {
/*  90 */       Execute exe = new Execute((ExecuteStreamHandler)new LogStreamHandler((Task)owner, 2, 1));
/*     */ 
/*     */       
/*  93 */       exe.setAntRun(project);
/*  94 */       exe.setWorkingDirectory(project.getBaseDir());
/*  95 */       exe.setCommandline(args);
/*  96 */       exe.execute();
/*  97 */       return !exe.isFailure();
/*  98 */     } catch (IOException exception) {
/*  99 */       throw new BuildException("Error running " + getExecutableName() + " -maybe it is not on the path", exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getExecutableName() {
/* 109 */     return "rmic";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/rmic/ForkingSunRmic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */