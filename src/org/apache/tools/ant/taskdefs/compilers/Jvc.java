/*     */ package org.apache.tools.ant.taskdefs.compilers;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.types.Commandline;
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
/*     */ public class Jvc
/*     */   extends DefaultCompilerAdapter
/*     */ {
/*     */   public boolean execute() throws BuildException {
/*  42 */     this.attributes.log("Using jvc compiler", 3);
/*     */     
/*  44 */     Path classpath = new Path(this.project);
/*     */ 
/*     */ 
/*     */     
/*  48 */     Path p = getBootClassPath();
/*  49 */     if (!p.isEmpty()) {
/*  50 */       classpath.append(p);
/*     */     }
/*     */     
/*  53 */     if (this.includeJavaRuntime)
/*     */     {
/*     */       
/*  56 */       classpath.addExtdirs(this.extdirs);
/*     */     }
/*     */     
/*  59 */     classpath.append(getCompileClasspath());
/*     */ 
/*     */ 
/*     */     
/*  63 */     if (this.compileSourcepath != null) {
/*  64 */       classpath.append(this.compileSourcepath);
/*     */     } else {
/*  66 */       classpath.append(this.src);
/*     */     } 
/*     */     
/*  69 */     Commandline cmd = new Commandline();
/*  70 */     String exec = getJavac().getExecutable();
/*  71 */     cmd.setExecutable((exec == null) ? "jvc" : exec);
/*     */     
/*  73 */     if (this.destDir != null) {
/*  74 */       cmd.createArgument().setValue("/d");
/*  75 */       cmd.createArgument().setFile(this.destDir);
/*     */     } 
/*     */ 
/*     */     
/*  79 */     cmd.createArgument().setValue("/cp:p");
/*  80 */     cmd.createArgument().setPath(classpath);
/*     */     
/*  82 */     boolean msExtensions = true;
/*  83 */     String mse = getProject().getProperty("build.compiler.jvc.extensions");
/*  84 */     if (mse != null) {
/*  85 */       msExtensions = Project.toBoolean(mse);
/*     */     }
/*     */     
/*  88 */     if (msExtensions) {
/*     */       
/*  90 */       cmd.createArgument().setValue("/x-");
/*     */       
/*  92 */       cmd.createArgument().setValue("/nomessage");
/*     */     } 
/*     */ 
/*     */     
/*  96 */     cmd.createArgument().setValue("/nologo");
/*     */     
/*  98 */     if (this.debug) {
/*  99 */       cmd.createArgument().setValue("/g");
/*     */     }
/* 101 */     if (this.optimize) {
/* 102 */       cmd.createArgument().setValue("/O");
/*     */     }
/* 104 */     if (this.verbose) {
/* 105 */       cmd.createArgument().setValue("/verbose");
/*     */     }
/*     */     
/* 108 */     addCurrentCompilerArgs(cmd);
/*     */     
/* 110 */     int firstFileName = cmd.size();
/* 111 */     logAndAddFilesToCompile(cmd);
/*     */     
/* 113 */     return 
/* 114 */       (executeExternalCompile(cmd.getCommandline(), firstFileName, false) == 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/Jvc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */