/*     */ package org.apache.tools.ant.taskdefs.optional.jlink;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JlinkTask
/*     */   extends MatchingTask
/*     */ {
/*  57 */   private File outfile = null;
/*     */   
/*  59 */   private Path mergefiles = null;
/*     */   
/*  61 */   private Path addfiles = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean compress = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutfile(File outfile) {
/*  70 */     this.outfile = outfile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createMergefiles() {
/*  79 */     if (this.mergefiles == null) {
/*  80 */       this.mergefiles = new Path(getProject());
/*     */     }
/*  82 */     return this.mergefiles.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMergefiles(Path mergefiles) {
/*  90 */     if (this.mergefiles == null) {
/*  91 */       this.mergefiles = mergefiles;
/*     */     } else {
/*  93 */       this.mergefiles.append(mergefiles);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createAddfiles() {
/* 103 */     if (this.addfiles == null) {
/* 104 */       this.addfiles = new Path(getProject());
/*     */     }
/* 106 */     return this.addfiles.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAddfiles(Path addfiles) {
/* 114 */     if (this.addfiles == null) {
/* 115 */       this.addfiles = addfiles;
/*     */     } else {
/* 117 */       this.addfiles.append(addfiles);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompress(boolean compress) {
/* 126 */     this.compress = compress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/* 136 */     if (this.outfile == null) {
/* 137 */       throw new BuildException("outfile attribute is required! Please set.");
/*     */     }
/*     */     
/* 140 */     if (!haveAddFiles() && !haveMergeFiles()) {
/* 141 */       throw new BuildException("addfiles or mergefiles required! Please set.");
/*     */     }
/*     */     
/* 144 */     log("linking:     " + this.outfile.getPath());
/* 145 */     log("compression: " + this.compress, 3);
/* 146 */     jlink linker = new jlink();
/* 147 */     linker.setOutfile(this.outfile.getPath());
/* 148 */     linker.setCompression(this.compress);
/* 149 */     if (haveMergeFiles()) {
/* 150 */       log("merge files: " + this.mergefiles.toString(), 3);
/* 151 */       linker.addMergeFiles(this.mergefiles.list());
/*     */     } 
/* 153 */     if (haveAddFiles()) {
/* 154 */       log("add files: " + this.addfiles.toString(), 3);
/* 155 */       linker.addAddFiles(this.addfiles.list());
/*     */     } 
/*     */     try {
/* 158 */       linker.link();
/* 159 */     } catch (Exception ex) {
/* 160 */       throw new BuildException(ex, getLocation());
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean haveAddFiles() {
/* 165 */     return haveEntries(this.addfiles);
/*     */   }
/*     */   
/*     */   private boolean haveMergeFiles() {
/* 169 */     return haveEntries(this.mergefiles);
/*     */   }
/*     */   
/*     */   private boolean haveEntries(Path p) {
/* 173 */     return (p != null && !p.isEmpty());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jlink/JlinkTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */