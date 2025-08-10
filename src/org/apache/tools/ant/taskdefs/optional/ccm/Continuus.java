/*     */ package org.apache.tools.ant.taskdefs.optional.ccm;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.LogStreamHandler;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.util.FileUtils;
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
/*     */ public abstract class Continuus
/*     */   extends Task
/*     */ {
/*     */   private static final String CCM_EXE = "ccm";
/*     */   public static final String COMMAND_CREATE_TASK = "create_task";
/*     */   public static final String COMMAND_CHECKOUT = "co";
/*     */   public static final String COMMAND_CHECKIN = "ci";
/*     */   public static final String COMMAND_RECONFIGURE = "reconfigure";
/*     */   public static final String COMMAND_DEFAULT_TASK = "default_task";
/*  71 */   private String ccmDir = "";
/*  72 */   private String ccmAction = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCcmAction() {
/*  79 */     return this.ccmAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCcmAction(String v) {
/*  88 */     this.ccmAction = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCcmDir(String dir) {
/*  97 */     this.ccmDir = FileUtils.translatePath(dir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String getCcmCommand() {
/* 105 */     String toReturn = this.ccmDir;
/* 106 */     if (!toReturn.isEmpty() && !toReturn.endsWith("/")) {
/* 107 */       toReturn = toReturn + "/";
/*     */     }
/*     */     
/* 110 */     toReturn = toReturn + "ccm";
/*     */     
/* 112 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int run(Commandline cmd, ExecuteStreamHandler handler) {
/*     */     try {
/* 123 */       Execute exe = new Execute(handler);
/* 124 */       exe.setAntRun(getProject());
/* 125 */       exe.setWorkingDirectory(getProject().getBaseDir());
/* 126 */       exe.setCommandline(cmd.getCommandline());
/* 127 */       return exe.execute();
/* 128 */     } catch (IOException e) {
/* 129 */       throw new BuildException(e, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int run(Commandline cmd) {
/* 139 */     return run(cmd, (ExecuteStreamHandler)new LogStreamHandler(this, 3, 1));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ccm/Continuus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */