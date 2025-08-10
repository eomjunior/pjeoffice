/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Map;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.LogOutputStream;
/*     */ import org.apache.tools.ant.taskdefs.LogStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.PumpStreamHandler;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.Path;
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
/*     */ public class Rpm
/*     */   extends Task
/*     */ {
/*     */   private static final String PATH1 = "PATH";
/*     */   private static final String PATH2 = "Path";
/*     */   private static final String PATH3 = "path";
/*     */   private String specFile;
/*     */   private File topDir;
/*  64 */   private String command = "-bb";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private String rpmBuildCommand = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean cleanBuildDir = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean removeSpec = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean removeSource = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File output;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File error;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failOnError = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean quiet = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*     */     PumpStreamHandler pumpStreamHandler;
/*     */     LogOutputStream logOutputStream1, logOutputStream2;
/* 116 */     Commandline toExecute = new Commandline();
/*     */     
/* 118 */     toExecute.setExecutable((this.rpmBuildCommand == null) ? guessRpmBuildCommand() : 
/* 119 */         this.rpmBuildCommand);
/* 120 */     if (this.topDir != null) {
/* 121 */       toExecute.createArgument().setValue("--define");
/* 122 */       toExecute.createArgument().setValue("_topdir " + this.topDir);
/*     */     } 
/*     */     
/* 125 */     toExecute.createArgument().setLine(this.command);
/*     */     
/* 127 */     if (this.cleanBuildDir) {
/* 128 */       toExecute.createArgument().setValue("--clean");
/*     */     }
/* 130 */     if (this.removeSpec) {
/* 131 */       toExecute.createArgument().setValue("--rmspec");
/*     */     }
/* 133 */     if (this.removeSource) {
/* 134 */       toExecute.createArgument().setValue("--rmsource");
/*     */     }
/*     */     
/* 137 */     toExecute.createArgument().setValue("SPECS/" + this.specFile);
/*     */     
/* 139 */     ExecuteStreamHandler streamhandler = null;
/* 140 */     OutputStream outputstream = null;
/* 141 */     OutputStream errorstream = null;
/* 142 */     if (this.error == null && this.output == null) {
/* 143 */       if (!this.quiet) {
/* 144 */         LogStreamHandler logStreamHandler = new LogStreamHandler(this, 2, 1);
/*     */       } else {
/*     */         
/* 147 */         LogStreamHandler logStreamHandler = new LogStreamHandler(this, 4, 4);
/*     */       } 
/*     */     } else {
/*     */       
/* 151 */       if (this.output != null) {
/* 152 */         OutputStream fos = null;
/*     */         try {
/* 154 */           fos = Files.newOutputStream(this.output.toPath(), new java.nio.file.OpenOption[0]);
/* 155 */           BufferedOutputStream bos = new BufferedOutputStream(fos);
/* 156 */           outputstream = new PrintStream(bos);
/* 157 */         } catch (IOException e) {
/* 158 */           FileUtils.close(fos);
/* 159 */           throw new BuildException(e, getLocation());
/*     */         } 
/* 161 */       } else if (!this.quiet) {
/* 162 */         logOutputStream1 = new LogOutputStream(this, 2);
/*     */       } else {
/* 164 */         logOutputStream1 = new LogOutputStream(this, 4);
/*     */       } 
/* 166 */       if (this.error != null) {
/* 167 */         OutputStream fos = null;
/*     */         try {
/* 169 */           fos = Files.newOutputStream(this.error.toPath(), new java.nio.file.OpenOption[0]);
/* 170 */           BufferedOutputStream bos = new BufferedOutputStream(fos);
/* 171 */           errorstream = new PrintStream(bos);
/* 172 */         } catch (IOException e) {
/* 173 */           FileUtils.close(fos);
/* 174 */           throw new BuildException(e, getLocation());
/*     */         } 
/* 176 */       } else if (!this.quiet) {
/* 177 */         logOutputStream2 = new LogOutputStream(this, 1);
/*     */       } else {
/* 179 */         logOutputStream2 = new LogOutputStream(this, 4);
/*     */       } 
/* 181 */       pumpStreamHandler = new PumpStreamHandler((OutputStream)logOutputStream1, (OutputStream)logOutputStream2);
/*     */     } 
/*     */     
/* 184 */     Execute exe = getExecute(toExecute, (ExecuteStreamHandler)pumpStreamHandler);
/*     */     try {
/* 186 */       log("Building the RPM based on the " + this.specFile + " file");
/* 187 */       int returncode = exe.execute();
/* 188 */       if (Execute.isFailure(returncode)) {
/* 189 */         String msg = "'" + toExecute.getExecutable() + "' failed with exit code " + returncode;
/*     */         
/* 191 */         if (this.failOnError) {
/* 192 */           throw new BuildException(msg);
/*     */         }
/* 194 */         log(msg, 0);
/*     */       } 
/* 196 */     } catch (IOException e) {
/* 197 */       throw new BuildException(e, getLocation());
/*     */     } finally {
/* 199 */       FileUtils.close((OutputStream)logOutputStream1);
/* 200 */       FileUtils.close((OutputStream)logOutputStream2);
/*     */     } 
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
/*     */   public void setTopDir(File td) {
/* 213 */     this.topDir = td;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCommand(String c) {
/* 222 */     this.command = c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSpecFile(String sf) {
/* 230 */     if (sf == null || sf.trim().isEmpty()) {
/* 231 */       throw new BuildException("You must specify a spec file", getLocation());
/*     */     }
/* 233 */     this.specFile = sf;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCleanBuildDir(boolean cbd) {
/* 242 */     this.cleanBuildDir = cbd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveSpec(boolean rs) {
/* 250 */     this.removeSpec = rs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveSource(boolean rs) {
/* 260 */     this.removeSource = rs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(File output) {
/* 268 */     this.output = output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setError(File error) {
/* 276 */     this.error = error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRpmBuildCommand(String c) {
/* 287 */     this.rpmBuildCommand = c;
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
/*     */   public void setFailOnError(boolean value) {
/* 299 */     this.failOnError = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuiet(boolean value) {
/* 310 */     this.quiet = value;
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
/*     */   protected String guessRpmBuildCommand() {
/* 323 */     Map<String, String> env = Execute.getEnvironmentVariables();
/* 324 */     String path = env.get("PATH");
/* 325 */     if (path == null) {
/* 326 */       path = env.get("Path");
/* 327 */       if (path == null) {
/* 328 */         path = env.get("path");
/*     */       }
/*     */     } 
/*     */     
/* 332 */     if (path != null) {
/* 333 */       Path p = new Path(getProject(), path);
/* 334 */       String[] pElements = p.list();
/* 335 */       for (String pElement : pElements) {
/*     */ 
/*     */         
/* 338 */         File f = new File(pElement, "rpmbuild" + (Os.isFamily("dos") ? ".exe" : ""));
/* 339 */         if (f.canRead()) {
/* 340 */           return f.getAbsolutePath();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 345 */     return "rpm";
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
/*     */   protected Execute getExecute(Commandline toExecute, ExecuteStreamHandler streamhandler) {
/* 357 */     Execute exe = new Execute(streamhandler, null);
/*     */     
/* 359 */     exe.setAntRun(getProject());
/* 360 */     if (this.topDir == null) {
/* 361 */       this.topDir = getProject().getBaseDir();
/*     */     }
/* 363 */     exe.setWorkingDirectory(this.topDir);
/*     */     
/* 365 */     exe.setCommandline(toExecute.getCommandline());
/* 366 */     return exe;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/Rpm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */