/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.types.Commandline;
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
/*     */ public class Patch
/*     */   extends Task
/*     */ {
/*     */   private File originalFile;
/*     */   private File directory;
/*     */   private boolean havePatchfile = false;
/*  42 */   private Commandline cmd = new Commandline();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean failOnError = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PATCH = "patch";
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOriginalfile(File file) {
/*  55 */     this.originalFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestfile(File file) {
/*  65 */     if (file != null) {
/*  66 */       this.cmd.createArgument().setValue("-o");
/*  67 */       this.cmd.createArgument().setFile(file);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPatchfile(File file) {
/*  76 */     if (!file.exists()) {
/*  77 */       throw new BuildException("patchfile " + file + " doesn't exist", 
/*  78 */           getLocation());
/*     */     }
/*  80 */     this.cmd.createArgument().setValue("-i");
/*  81 */     this.cmd.createArgument().setFile(file);
/*  82 */     this.havePatchfile = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBackups(boolean backups) {
/*  90 */     if (backups) {
/*  91 */       this.cmd.createArgument().setValue("-b");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnorewhitespace(boolean ignore) {
/* 100 */     if (ignore) {
/* 101 */       this.cmd.createArgument().setValue("-l");
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
/*     */   public void setStrip(int num) throws BuildException {
/* 114 */     if (num < 0) {
/* 115 */       throw new BuildException("strip has to be >= 0", getLocation());
/*     */     }
/* 117 */     this.cmd.createArgument().setValue("-p" + num);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setQuiet(boolean q) {
/* 125 */     if (q) {
/* 126 */       this.cmd.createArgument().setValue("-s");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReverse(boolean r) {
/* 136 */     if (r) {
/* 137 */       this.cmd.createArgument().setValue("-R");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(File directory) {
/* 148 */     this.directory = directory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailOnError(boolean value) {
/* 159 */     this.failOnError = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 170 */     if (!this.havePatchfile) {
/* 171 */       throw new BuildException("patchfile argument is required", 
/* 172 */           getLocation());
/*     */     }
/* 174 */     Commandline toExecute = (Commandline)this.cmd.clone();
/* 175 */     toExecute.setExecutable("patch");
/*     */     
/* 177 */     if (this.originalFile != null) {
/* 178 */       toExecute.createArgument().setFile(this.originalFile);
/*     */     }
/*     */     
/* 181 */     Execute exe = new Execute(new LogStreamHandler(this, 2, 1), null);
/*     */ 
/*     */     
/* 184 */     exe.setCommandline(toExecute.getCommandline());
/*     */     
/* 186 */     if (this.directory == null) {
/* 187 */       exe.setWorkingDirectory(getProject().getBaseDir());
/*     */     } else {
/* 189 */       if (!this.directory.isDirectory()) {
/* 190 */         throw new BuildException(this.directory + " is not a directory.", 
/* 191 */             getLocation());
/*     */       }
/* 193 */       exe.setWorkingDirectory(this.directory);
/*     */     } 
/*     */     
/* 196 */     log(toExecute.describeCommand(), 3);
/*     */     try {
/* 198 */       int returncode = exe.execute();
/* 199 */       if (Execute.isFailure(returncode)) {
/* 200 */         String msg = "'patch' failed with exit code " + returncode;
/*     */         
/* 202 */         if (this.failOnError) {
/* 203 */           throw new BuildException(msg);
/*     */         }
/* 205 */         log(msg, 0);
/*     */       } 
/* 207 */     } catch (IOException e) {
/* 208 */       throw new BuildException(e, getLocation());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Patch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */