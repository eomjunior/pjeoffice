/*     */ package org.apache.tools.ant.taskdefs.optional;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.ExecTask;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.taskdefs.LogOutputStream;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.taskdefs.StreamPumper;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.FileSet;
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
/*     */ public class Cab
/*     */   extends MatchingTask
/*     */ {
/*     */   private static final int DEFAULT_RESULT = -99;
/*     */   private File cabFile;
/*     */   private File baseDir;
/*     */   private boolean doCompress = true;
/*     */   private boolean doVerbose = false;
/*     */   private String cmdOptions;
/*     */   private boolean filesetAdded = false;
/*  58 */   protected String archiveType = "cab";
/*     */ 
/*     */   
/*  61 */   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCabfile(File cabFile) {
/*  68 */     this.cabFile = cabFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBasedir(File baseDir) {
/*  76 */     this.baseDir = baseDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompress(boolean compress) {
/*  84 */     this.doCompress = compress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(boolean verbose) {
/*  92 */     this.doVerbose = verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptions(String options) {
/* 100 */     this.cmdOptions = options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet fileset) {
/* 108 */     if (this.filesetAdded) {
/* 109 */       throw new BuildException("Only one nested fileset allowed");
/*     */     }
/* 111 */     this.filesetAdded = true;
/* 112 */     this.fileset = fileset;
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
/*     */   protected void checkConfiguration() throws BuildException {
/* 125 */     if (this.baseDir == null && !this.filesetAdded) {
/* 126 */       throw new BuildException("basedir attribute or one nested fileset is required!", 
/*     */           
/* 128 */           getLocation());
/*     */     }
/* 130 */     if (this.baseDir != null && !this.baseDir.exists()) {
/* 131 */       throw new BuildException("basedir does not exist!", getLocation());
/*     */     }
/* 133 */     if (this.baseDir != null && this.filesetAdded) {
/* 134 */       throw new BuildException("Both basedir attribute and a nested fileset is not allowed");
/*     */     }
/*     */     
/* 137 */     if (this.cabFile == null) {
/* 138 */       throw new BuildException("cabfile attribute must be set!", 
/* 139 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ExecTask createExec() throws BuildException {
/* 150 */     return new ExecTask((Task)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isUpToDate(Vector<String> files) {
/* 159 */     long cabModified = this.cabFile.lastModified();
/* 160 */     return files.stream().map(f -> FILE_UTILS.resolveFile(this.baseDir, f))
/* 161 */       .mapToLong(File::lastModified).allMatch(t -> (t < cabModified));
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
/*     */   
/*     */   protected File createListFile(Vector<String> files) throws IOException {
/* 176 */     File listFile = FILE_UTILS.createTempFile(getProject(), "ant", "", null, true, true);
/*     */     
/* 178 */     BufferedWriter writer = new BufferedWriter(new FileWriter(listFile));
/*     */     
/* 180 */     try { for (String f : files) {
/* 181 */         String s = String.format("\"%s\"", new Object[] { f });
/* 182 */         writer.write(s);
/* 183 */         writer.newLine();
/*     */       } 
/* 185 */       writer.close(); } catch (Throwable throwable) { try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 186 */      return listFile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void appendFiles(Vector<String> files, DirectoryScanner ds) {
/* 195 */     Collections.addAll(files, ds.getIncludedFiles());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vector<String> getFileList() throws BuildException {
/* 206 */     Vector<String> files = new Vector<>();
/*     */     
/* 208 */     if (this.baseDir != null) {
/*     */       
/* 210 */       appendFiles(files, getDirectoryScanner(this.baseDir));
/*     */     } else {
/* 212 */       this.baseDir = this.fileset.getDir();
/* 213 */       appendFiles(files, this.fileset.getDirectoryScanner(getProject()));
/*     */     } 
/* 215 */     return files;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 225 */     checkConfiguration();
/*     */     
/* 227 */     Vector<String> files = getFileList();
/*     */ 
/*     */     
/* 230 */     if (isUpToDate(files)) {
/*     */       return;
/*     */     }
/*     */     
/* 234 */     log("Building " + this.archiveType + ": " + this.cabFile.getAbsolutePath());
/*     */     
/* 236 */     if (!Os.isFamily("windows")) {
/* 237 */       log("Using listcab/libcabinet", 3);
/*     */       
/* 239 */       StringBuilder sb = new StringBuilder();
/*     */       
/* 241 */       files.forEach(f -> sb.append(f).append("\n"));
/*     */       
/* 243 */       sb.append("\n").append(this.cabFile.getAbsolutePath()).append("\n");
/*     */       
/*     */       try {
/* 246 */         Process p = Execute.launch(getProject(), new String[] { "listcab" }, null, 
/*     */             
/* 248 */             (this.baseDir != null) ? this.baseDir : getProject().getBaseDir(), true);
/*     */         
/* 250 */         OutputStream out = p.getOutputStream();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 255 */         LogOutputStream outLog = new LogOutputStream((Task)this, 3);
/* 256 */         LogOutputStream errLog = new LogOutputStream((Task)this, 0);
/* 257 */         StreamPumper outPump = new StreamPumper(p.getInputStream(), (OutputStream)outLog);
/* 258 */         StreamPumper errPump = new StreamPumper(p.getErrorStream(), (OutputStream)errLog);
/*     */ 
/*     */         
/* 261 */         (new Thread((Runnable)outPump)).start();
/* 262 */         (new Thread((Runnable)errPump)).start();
/*     */         
/* 264 */         out.write(sb.toString().getBytes());
/* 265 */         out.flush();
/* 266 */         out.close();
/*     */ 
/*     */         
/* 269 */         int result = -99;
/*     */ 
/*     */         
/*     */         try {
/* 273 */           result = p.waitFor();
/*     */ 
/*     */           
/* 276 */           outPump.waitFor();
/* 277 */           outLog.close();
/* 278 */           errPump.waitFor();
/* 279 */           errLog.close();
/* 280 */         } catch (InterruptedException ie) {
/* 281 */           log("Thread interrupted: " + ie);
/*     */         } 
/*     */ 
/*     */         
/* 285 */         if (Execute.isFailure(result)) {
/* 286 */           log("Error executing listcab; error code: " + result);
/*     */         }
/* 288 */       } catch (IOException ex) {
/* 289 */         throw new BuildException("Problem creating " + this.cabFile + " " + ex
/* 290 */             .getMessage(), 
/* 291 */             getLocation());
/*     */       } 
/*     */     } else {
/*     */       try {
/* 295 */         File listFile = createListFile(files);
/* 296 */         ExecTask exec = createExec();
/* 297 */         File outFile = null;
/*     */ 
/*     */         
/* 300 */         exec.setFailonerror(true);
/* 301 */         exec.setDir(this.baseDir);
/*     */         
/* 303 */         if (!this.doVerbose) {
/* 304 */           outFile = FILE_UTILS.createTempFile(getProject(), "ant", "", null, true, true);
/* 305 */           exec.setOutput(outFile);
/*     */         } 
/*     */         
/* 308 */         exec.setExecutable("cabarc");
/* 309 */         exec.createArg().setValue("-r");
/* 310 */         exec.createArg().setValue("-p");
/*     */         
/* 312 */         if (!this.doCompress) {
/* 313 */           exec.createArg().setValue("-m");
/* 314 */           exec.createArg().setValue("none");
/*     */         } 
/*     */         
/* 317 */         if (this.cmdOptions != null) {
/* 318 */           exec.createArg().setLine(this.cmdOptions);
/*     */         }
/*     */         
/* 321 */         exec.createArg().setValue("n");
/* 322 */         exec.createArg().setFile(this.cabFile);
/* 323 */         exec.createArg().setValue("@" + listFile.getAbsolutePath());
/*     */         
/* 325 */         exec.execute();
/*     */         
/* 327 */         if (outFile != null) {
/* 328 */           outFile.delete();
/*     */         }
/*     */         
/* 331 */         listFile.delete();
/* 332 */       } catch (IOException ioe) {
/* 333 */         throw new BuildException("Problem creating " + this.cabFile + " " + ioe
/* 334 */             .getMessage(), 
/* 335 */             getLocation());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/Cab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */