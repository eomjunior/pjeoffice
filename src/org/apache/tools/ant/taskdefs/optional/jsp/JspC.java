/*     */ package org.apache.tools.ant.taskdefs.optional.jsp;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.time.Instant;
/*     */ import java.util.Vector;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.DirectoryScanner;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.MatchingTask;
/*     */ import org.apache.tools.ant.taskdefs.optional.jsp.compilers.JspCompilerAdapter;
/*     */ import org.apache.tools.ant.taskdefs.optional.jsp.compilers.JspCompilerAdapterFactory;
/*     */ import org.apache.tools.ant.types.Path;
/*     */ import org.apache.tools.ant.types.Reference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JspC
/*     */   extends MatchingTask
/*     */ {
/*     */   private Path classpath;
/*     */   private Path compilerClasspath;
/*     */   private Path src;
/*     */   private File destDir;
/*     */   private String packageName;
/*  73 */   private String compilerName = "jasper";
/*     */ 
/*     */   
/*     */   private String iepluginid;
/*     */   
/*     */   private boolean mapped;
/*     */   
/*  80 */   private int verbose = 0;
/*     */   
/*  82 */   protected Vector<String> compileList = new Vector<>();
/*  83 */   Vector<File> javaFiles = new Vector<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean failOnError = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File uriroot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File webinc;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private File webxml;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebAppParameter webApp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String FAIL_MSG = "Compile failed, messages should have been provided.";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSrcDir(Path srcDir) {
/* 124 */     if (this.src == null) {
/* 125 */       this.src = srcDir;
/*     */     } else {
/* 127 */       this.src.append(srcDir);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getSrcDir() {
/* 136 */     return this.src;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestdir(File destDir) {
/* 145 */     this.destDir = destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getDestdir() {
/* 153 */     return this.destDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPackage(String pkg) {
/* 161 */     this.packageName = pkg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPackage() {
/* 169 */     return this.packageName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVerbose(int i) {
/* 177 */     this.verbose = i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVerbose() {
/* 185 */     return this.verbose;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFailonerror(boolean fail) {
/* 194 */     this.failOnError = fail;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFailonerror() {
/* 201 */     return this.failOnError;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIeplugin() {
/* 209 */     return this.iepluginid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIeplugin(String iepluginid) {
/* 217 */     this.iepluginid = iepluginid;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMapped() {
/* 226 */     return this.mapped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMapped(boolean mapped) {
/* 235 */     this.mapped = mapped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUribase(File uribase) {
/* 246 */     log("Uribase is currently an unused parameter", 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getUribase() {
/* 254 */     return this.uriroot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUriroot(File uriroot) {
/* 264 */     this.uriroot = uriroot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getUriroot() {
/* 272 */     return this.uriroot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspath(Path cp) {
/* 280 */     if (this.classpath == null) {
/* 281 */       this.classpath = cp;
/*     */     } else {
/* 283 */       this.classpath.append(cp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createClasspath() {
/* 292 */     if (this.classpath == null) {
/* 293 */       this.classpath = new Path(getProject());
/*     */     }
/* 295 */     return this.classpath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setClasspathRef(Reference r) {
/* 303 */     createClasspath().setRefid(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getClasspath() {
/* 311 */     return this.classpath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompilerclasspath(Path cp) {
/* 319 */     if (this.compilerClasspath == null) {
/* 320 */       this.compilerClasspath = cp;
/*     */     } else {
/* 322 */       this.compilerClasspath.append(cp);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path getCompilerclasspath() {
/* 331 */     return this.compilerClasspath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Path createCompilerclasspath() {
/* 339 */     if (this.compilerClasspath == null) {
/* 340 */       this.compilerClasspath = new Path(getProject());
/*     */     }
/* 342 */     return this.compilerClasspath.createPath();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWebxml(File webxml) {
/* 351 */     this.webxml = webxml;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getWebxml() {
/* 359 */     return this.webxml;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWebinc(File webinc) {
/* 368 */     this.webinc = webinc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getWebinc() {
/* 376 */     return this.webinc;
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
/*     */   public void addWebApp(WebAppParameter webappParam) throws BuildException {
/* 388 */     if (this.webApp == null) {
/* 389 */       this.webApp = webappParam;
/*     */     } else {
/* 391 */       throw new BuildException("Only one webapp can be specified");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebAppParameter getWebApp() {
/* 400 */     return this.webApp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCompiler(String compiler) {
/* 408 */     this.compilerName = compiler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector<String> getCompileList() {
/* 416 */     return this.compileList;
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
/*     */   public void execute() throws BuildException {
/* 429 */     if (this.destDir == null) {
/* 430 */       throw new BuildException("destdir attribute must be set!", 
/* 431 */           getLocation());
/*     */     }
/*     */     
/* 434 */     if (!this.destDir.isDirectory()) {
/* 435 */       throw new BuildException("destination directory \"" + this.destDir + "\" does not exist or is not a directory", 
/*     */           
/* 437 */           getLocation());
/*     */     }
/*     */     
/* 440 */     File dest = getActualDestDir();
/*     */     
/* 442 */     AntClassLoader al = getProject().createClassLoader(this.compilerClasspath);
/*     */     
/*     */     try {
/* 445 */       JspCompilerAdapter compiler = JspCompilerAdapterFactory.getCompiler(this.compilerName, (Task)this, al);
/*     */ 
/*     */ 
/*     */       
/* 449 */       if (this.webApp != null)
/* 450 */       { doCompilation(compiler);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 512 */         if (al != null) al.close();  return; }  if (this.src == null) throw new BuildException("srcdir attribute must be set!", getLocation());  String[] list = this.src.list(); if (list.length == 0) throw new BuildException("srcdir attribute must be set!", getLocation());  if (compiler.implementsOwnDependencyChecking()) { doCompilation(compiler); if (al != null) al.close();  return; }  JspMangler mangler = compiler.createMangler(); resetFileLists(); int filecount = 0; for (String fileName : list) { File srcDir = getProject().resolveFile(fileName); if (!srcDir.exists()) throw new BuildException("srcdir \"" + srcDir.getPath() + "\" does not exist!", getLocation());  DirectoryScanner ds = getDirectoryScanner(srcDir); String[] files = ds.getIncludedFiles(); filecount = files.length; scanDir(srcDir, dest, mangler, files); }  log("compiling " + this.compileList.size() + " files", 3); if (!this.compileList.isEmpty()) { log("Compiling " + this.compileList.size() + " source file" + ((this.compileList.size() == 1) ? "" : "s") + " to " + dest); doCompilation(compiler); } else if (filecount == 0) { log("there were no files to compile", 2); } else { log("all files are up to date", 3); }  if (al != null) al.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (al != null)
/*     */         try {
/*     */           al.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }   throw throwable;
/* 520 */     }  } private File getActualDestDir() { if (this.packageName == null) {
/* 521 */       return this.destDir;
/*     */     }
/* 523 */     return new File(this.destDir.getPath() + File.separatorChar + this.packageName
/* 524 */         .replace('.', File.separatorChar)); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doCompilation(JspCompilerAdapter compiler) throws BuildException {
/* 533 */     compiler.setJspc(this);
/*     */ 
/*     */     
/* 536 */     if (!compiler.execute()) {
/* 537 */       if (this.failOnError) {
/* 538 */         throw new BuildException("Compile failed, messages should have been provided.", getLocation());
/*     */       }
/* 540 */       log("Compile failed, messages should have been provided.", 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void resetFileLists() {
/* 548 */     this.compileList.removeAllElements();
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
/*     */   protected void scanDir(File srcDir, File dest, JspMangler mangler, String[] files) {
/* 562 */     long now = Instant.now().toEpochMilli();
/*     */     
/* 564 */     for (String filename : files) {
/* 565 */       File srcFile = new File(srcDir, filename);
/* 566 */       File javaFile = mapToJavaFile(mangler, srcFile, srcDir, dest);
/* 567 */       if (javaFile != null) {
/*     */ 
/*     */         
/* 570 */         if (srcFile.lastModified() > now) {
/* 571 */           log("Warning: file modified in the future: " + filename, 1);
/*     */         }
/*     */         
/* 574 */         if (isCompileNeeded(srcFile, javaFile)) {
/* 575 */           this.compileList.addElement(srcFile.getAbsolutePath());
/* 576 */           this.javaFiles.addElement(javaFile);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isCompileNeeded(File srcFile, File javaFile) {
/* 598 */     boolean shouldCompile = false;
/* 599 */     if (!javaFile.exists()) {
/* 600 */       shouldCompile = true;
/* 601 */       log("Compiling " + srcFile.getPath() + " because java file " + javaFile
/* 602 */           .getPath() + " does not exist", 3);
/*     */     }
/* 604 */     else if (srcFile.lastModified() > javaFile.lastModified()) {
/* 605 */       shouldCompile = true;
/* 606 */       log("Compiling " + srcFile.getPath() + " because it is out of date with respect to " + javaFile
/*     */           
/* 608 */           .getPath(), 3);
/*     */     }
/* 610 */     else if (javaFile.length() == 0L) {
/* 611 */       shouldCompile = true;
/* 612 */       log("Compiling " + srcFile.getPath() + " because java file " + javaFile
/* 613 */           .getPath() + " is empty", 3);
/*     */     } 
/*     */     
/* 616 */     return shouldCompile;
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
/*     */   protected File mapToJavaFile(JspMangler mangler, File srcFile, File srcDir, File dest) {
/* 630 */     if (!srcFile.getName().endsWith(".jsp")) {
/* 631 */       return null;
/*     */     }
/* 633 */     String javaFileName = mangler.mapJspToJavaName(srcFile);
/* 634 */     return new File(dest, javaFileName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deleteEmptyJavaFiles() {
/* 643 */     if (this.javaFiles != null) {
/* 644 */       for (File file : this.javaFiles) {
/* 645 */         if (file.exists() && file.length() == 0L) {
/* 646 */           log("deleting empty output file " + file);
/* 647 */           file.delete();
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
/*     */   public static class WebAppParameter
/*     */   {
/*     */     private File directory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public File getDirectory() {
/* 668 */       return this.directory;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setBaseDir(File directory) {
/* 676 */       this.directory = directory;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jsp/JspC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */