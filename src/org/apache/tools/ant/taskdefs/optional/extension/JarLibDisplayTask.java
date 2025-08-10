/*     */ package org.apache.tools.ant.taskdefs.optional.extension;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JarLibDisplayTask
/*     */   extends Task
/*     */ {
/*     */   private File libraryFile;
/*  52 */   private final List<FileSet> libraryFileSets = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFile(File file) {
/*  60 */     this.libraryFile = file;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileset(FileSet fileSet) {
/*  69 */     this.libraryFileSets.add(fileSet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute() throws BuildException {
/*  79 */     validate();
/*     */     
/*  81 */     LibraryDisplayer displayer = new LibraryDisplayer();
/*     */     
/*  83 */     if (this.libraryFileSets.isEmpty()) {
/*  84 */       displayer.displayLibrary(this.libraryFile);
/*     */     } else {
/*  86 */       for (FileSet fileSet : this.libraryFileSets) {
/*     */         
/*  88 */         DirectoryScanner scanner = fileSet.getDirectoryScanner(getProject());
/*  89 */         File basedir = scanner.getBasedir();
/*  90 */         for (String filename : scanner.getIncludedFiles()) {
/*  91 */           displayer.displayLibrary(new File(basedir, filename));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validate() throws BuildException {
/* 103 */     if (null == this.libraryFile) {
/* 104 */       if (this.libraryFileSets.isEmpty())
/* 105 */         throw new BuildException("File attribute not specified."); 
/*     */     } else {
/* 107 */       if (!this.libraryFile.exists())
/* 108 */         throw new BuildException("File '%s' does not exist.", new Object[] { this.libraryFile }); 
/* 109 */       if (!this.libraryFile.isFile())
/* 110 */         throw new BuildException("'%s' is not a file.", new Object[] { this.libraryFile }); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/extension/JarLibDisplayTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */