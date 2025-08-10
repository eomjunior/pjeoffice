/*     */ package org.apache.tools.ant.taskdefs.optional.clearcase;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.ExecTask;
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
/*     */ public abstract class ClearCase
/*     */   extends Task
/*     */ {
/*     */   private static final String CLEARTOOL_EXE = "cleartool";
/*     */   public static final String COMMAND_UPDATE = "update";
/*     */   public static final String COMMAND_CHECKOUT = "checkout";
/*     */   public static final String COMMAND_CHECKIN = "checkin";
/*     */   public static final String COMMAND_UNCHECKOUT = "uncheckout";
/*     */   public static final String COMMAND_LOCK = "lock";
/*     */   public static final String COMMAND_UNLOCK = "unlock";
/*     */   public static final String COMMAND_MKBL = "mkbl";
/*     */   public static final String COMMAND_MKLABEL = "mklabel";
/*     */   public static final String COMMAND_MKLBTYPE = "mklbtype";
/*     */   public static final String COMMAND_RMTYPE = "rmtype";
/*     */   public static final String COMMAND_LSCO = "lsco";
/*     */   public static final String COMMAND_MKELEM = "mkelem";
/*     */   public static final String COMMAND_MKATTR = "mkattr";
/*     */   public static final String COMMAND_MKDIR = "mkdir";
/* 109 */   private String mClearToolDir = "";
/* 110 */   private String mviewPath = null;
/* 111 */   private String mobjSelect = null;
/* 112 */   private int pcnt = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean mFailonerr = true;
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setClearToolDir(String dir) {
/* 121 */     this.mClearToolDir = FileUtils.translatePath(dir);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String getClearToolCommand() {
/* 130 */     String toReturn = this.mClearToolDir;
/* 131 */     if (!toReturn.isEmpty() && !toReturn.endsWith("/")) {
/* 132 */       toReturn = toReturn + "/";
/*     */     }
/*     */     
/* 135 */     toReturn = toReturn + "cleartool";
/*     */     
/* 137 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setViewPath(String viewPath) {
/* 146 */     this.mviewPath = viewPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getViewPath() {
/* 155 */     return this.mviewPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getViewPathBasename() {
/* 164 */     return (new File(this.mviewPath)).getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setObjSelect(String objSelect) {
/* 173 */     this.mobjSelect = objSelect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getObjSelect() {
/* 182 */     return this.mobjSelect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int run(Commandline cmd) {
/*     */     try {
/* 192 */       Project aProj = getProject();
/* 193 */       Execute exe = new Execute((ExecuteStreamHandler)new LogStreamHandler(this, 2, 1));
/*     */       
/* 195 */       exe.setAntRun(aProj);
/* 196 */       exe.setWorkingDirectory(aProj.getBaseDir());
/* 197 */       exe.setCommandline(cmd.getCommandline());
/* 198 */       return exe.execute();
/* 199 */     } catch (IOException e) {
/* 200 */       throw new BuildException(e, getLocation());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected String runS(Commandline cmdline) {
/* 212 */     return runS(cmdline, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String runS(Commandline cmdline, boolean failOnError) {
/* 223 */     String outV = "opts.cc.runS.output" + this.pcnt++;
/* 224 */     ExecTask exe = new ExecTask(this);
/* 225 */     Commandline.Argument arg = exe.createArg();
/*     */     
/* 227 */     exe.setExecutable(cmdline.getExecutable());
/* 228 */     arg.setLine(Commandline.toString(cmdline.getArguments()));
/* 229 */     exe.setOutputproperty(outV);
/* 230 */     exe.setFailonerror(failOnError);
/* 231 */     exe.execute();
/*     */     
/* 233 */     return getProject().getProperty(outV);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnErr(boolean failonerr) {
/* 243 */     this.mFailonerr = failonerr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFailOnErr() {
/* 253 */     return this.mFailonerr;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/clearcase/ClearCase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */