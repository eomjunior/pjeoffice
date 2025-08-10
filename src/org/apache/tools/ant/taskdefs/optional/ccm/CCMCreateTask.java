/*     */ package org.apache.tools.ant.taskdefs.optional.ccm;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.util.StringUtils;
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
/*     */ public class CCMCreateTask
/*     */   extends Continuus
/*     */   implements ExecuteStreamHandler
/*     */ {
/*     */   public static final String FLAG_COMMENT = "/synopsis";
/*     */   public static final String FLAG_PLATFORM = "/plat";
/*     */   public static final String FLAG_RESOLVER = "/resolver";
/*     */   public static final String FLAG_RELEASE = "/release";
/*     */   public static final String FLAG_SUBSYSTEM = "/subsystem";
/*     */   public static final String FLAG_TASK = "/task";
/*  71 */   private String comment = null;
/*  72 */   private String platform = null;
/*  73 */   private String resolver = null;
/*  74 */   private String release = null;
/*  75 */   private String subSystem = null;
/*  76 */   private String task = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CCMCreateTask() {
/*  83 */     setCcmAction("create_task");
/*     */   }
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
/*     */   public void execute() throws BuildException {
/*  97 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */ 
/*     */     
/* 101 */     commandLine.setExecutable(getCcmCommand());
/* 102 */     commandLine.createArgument().setValue(getCcmAction());
/*     */     
/* 104 */     checkOptions(commandLine);
/*     */     
/* 106 */     if (Execute.isFailure(run(commandLine, this))) {
/* 107 */       throw new BuildException("Failed executing: " + commandLine, 
/* 108 */           getLocation());
/*     */     }
/*     */ 
/*     */     
/* 112 */     Commandline commandLine2 = new Commandline();
/* 113 */     commandLine2.setExecutable(getCcmCommand());
/* 114 */     commandLine2.createArgument().setValue("default_task");
/* 115 */     commandLine2.createArgument().setValue(getTask());
/*     */     
/* 117 */     log(commandLine.describeCommand(), 4);
/*     */     
/* 119 */     if (run(commandLine2) != 0) {
/* 120 */       throw new BuildException("Failed executing: " + commandLine2, 
/* 121 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 129 */     if (getComment() != null) {
/* 130 */       cmd.createArgument().setValue("/synopsis");
/* 131 */       cmd.createArgument().setValue("\"" + getComment() + "\"");
/*     */     } 
/*     */     
/* 134 */     if (getPlatform() != null) {
/* 135 */       cmd.createArgument().setValue("/plat");
/* 136 */       cmd.createArgument().setValue(getPlatform());
/*     */     } 
/*     */     
/* 139 */     if (getResolver() != null) {
/* 140 */       cmd.createArgument().setValue("/resolver");
/* 141 */       cmd.createArgument().setValue(getResolver());
/*     */     } 
/*     */     
/* 144 */     if (getSubSystem() != null) {
/* 145 */       cmd.createArgument().setValue("/subsystem");
/* 146 */       cmd.createArgument().setValue("\"" + getSubSystem() + "\"");
/*     */     } 
/*     */     
/* 149 */     if (getRelease() != null) {
/* 150 */       cmd.createArgument().setValue("/release");
/* 151 */       cmd.createArgument().setValue(getRelease());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/* 160 */     return this.comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String v) {
/* 169 */     this.comment = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPlatform() {
/* 177 */     return this.platform;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPlatform(String v) {
/* 186 */     this.platform = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getResolver() {
/* 194 */     return this.resolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setResolver(String v) {
/* 203 */     this.resolver = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRelease() {
/* 211 */     return this.release;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRelease(String v) {
/* 220 */     this.release = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubSystem() {
/* 228 */     return this.subSystem;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubSystem(String v) {
/* 237 */     this.subSystem = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTask() {
/* 245 */     return this.task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTask(String v) {
/* 255 */     this.task = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessInputStream(OutputStream param1) throws IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessErrorStream(InputStream is) throws IOException {
/* 291 */     BufferedReader reader = new BufferedReader(new InputStreamReader(is)); try {
/* 292 */       String s = reader.readLine();
/* 293 */       if (s != null) {
/* 294 */         log("err " + s, 4);
/*     */       }
/* 296 */       reader.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reader.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     }  } public void setProcessOutputStream(InputStream is) throws IOException {
/*     */     
/* 306 */     try { BufferedReader reader = new BufferedReader(new InputStreamReader(is));
/*     */       
/* 308 */       try { String buffer = reader.readLine();
/* 309 */         if (buffer != null) {
/* 310 */           log("buffer:" + buffer, 4);
/* 311 */           String taskstring = buffer.substring(buffer.indexOf(' ')).trim();
/* 312 */           taskstring = taskstring.substring(0, taskstring.lastIndexOf(' ')).trim();
/* 313 */           setTask(taskstring);
/* 314 */           log("task is " + getTask(), 4);
/*     */         } 
/* 316 */         reader.close(); } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (NullPointerException npe)
/* 317 */     { log("error procession stream, null pointer exception", 0);
/* 318 */       log(StringUtils.getStackTrace(npe), 0);
/* 319 */       throw new BuildException(npe); }
/* 320 */     catch (Exception e)
/* 321 */     { log("error procession stream " + e.getMessage(), 0);
/* 322 */       throw new BuildException(e.getMessage()); }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ccm/CCMCreateTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */