/*     */ package org.apache.tools.ant.taskdefs.optional.ccm;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.taskdefs.Execute;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.types.FileSet;
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
/*     */ public class CCMCheck
/*     */   extends Continuus
/*     */ {
/*     */   public static final String FLAG_COMMENT = "/comment";
/*     */   public static final String FLAG_TASK = "/task";
/*  47 */   private File file = null;
/*  48 */   private String comment = null;
/*  49 */   private String task = null;
/*     */ 
/*     */ 
/*     */   
/*  53 */   protected Vector<FileSet> filesets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/*  62 */     return this.file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File v) {
/*  70 */     log("working file " + v, 3);
/*  71 */     this.file = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getComment() {
/*  79 */     return this.comment;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setComment(String v) {
/*  87 */     this.comment = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTask() {
/*  95 */     return this.task;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTask(String v) {
/* 104 */     this.task = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet set) {
/* 112 */     this.filesets.addElement(set);
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
/*     */   public void execute() throws BuildException {
/* 127 */     if (this.file == null && this.filesets.isEmpty()) {
/* 128 */       throw new BuildException("Specify at least one source - a file or a fileset.");
/*     */     }
/*     */ 
/*     */     
/* 132 */     if (this.file != null && this.file.exists() && this.file.isDirectory()) {
/* 133 */       throw new BuildException("CCMCheck cannot be generated for directories");
/*     */     }
/*     */     
/* 136 */     if (this.file != null && !this.filesets.isEmpty()) {
/* 137 */       throw new BuildException("Choose between file and fileset !");
/*     */     }
/*     */     
/* 140 */     if (getFile() != null) {
/* 141 */       doit();
/*     */       
/*     */       return;
/*     */     } 
/* 145 */     for (FileSet fs : this.filesets) {
/* 146 */       File basedir = fs.getDir(getProject());
/* 147 */       DirectoryScanner ds = fs.getDirectoryScanner(getProject());
/* 148 */       for (String srcFile : ds.getIncludedFiles()) {
/* 149 */         setFile(new File(basedir, srcFile));
/* 150 */         doit();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doit() {
/* 159 */     Commandline commandLine = new Commandline();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 165 */     commandLine.setExecutable(getCcmCommand());
/* 166 */     commandLine.createArgument().setValue(getCcmAction());
/*     */     
/* 168 */     checkOptions(commandLine);
/*     */     
/* 170 */     int result = run(commandLine);
/* 171 */     if (Execute.isFailure(result)) {
/* 172 */       throw new BuildException("Failed executing: " + commandLine, 
/* 173 */           getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkOptions(Commandline cmd) {
/* 181 */     if (getComment() != null) {
/* 182 */       cmd.createArgument().setValue("/comment");
/* 183 */       cmd.createArgument().setValue(getComment());
/*     */     } 
/*     */     
/* 186 */     if (getTask() != null) {
/* 187 */       cmd.createArgument().setValue("/task");
/* 188 */       cmd.createArgument().setValue(getTask());
/*     */     } 
/*     */     
/* 191 */     if (getFile() != null)
/* 192 */       cmd.createArgument().setValue(this.file.getAbsolutePath()); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/ccm/CCMCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */