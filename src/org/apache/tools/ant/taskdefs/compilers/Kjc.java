/*     */ package org.apache.tools.ant.taskdefs.compilers;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteJava;
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
/*     */ public class Kjc
/*     */   extends DefaultCompilerAdapter
/*     */ {
/*     */   public boolean execute() throws BuildException {
/*  43 */     this.attributes.log("Using kjc compiler", 3);
/*  44 */     Commandline cmd = setupKjcCommand();
/*  45 */     cmd.setExecutable("at.dms.kjc.Main");
/*  46 */     ExecuteJava ej = new ExecuteJava();
/*  47 */     ej.setJavaCommand(cmd);
/*  48 */     return (ej.fork((ProjectComponent)getJavac()) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupKjcCommand() {
/*  56 */     Commandline cmd = new Commandline();
/*     */ 
/*     */     
/*  59 */     Path classpath = getCompileClasspath();
/*     */     
/*  61 */     if (this.deprecation) {
/*  62 */       cmd.createArgument().setValue("-deprecation");
/*     */     }
/*     */     
/*  65 */     if (this.destDir != null) {
/*  66 */       cmd.createArgument().setValue("-d");
/*  67 */       cmd.createArgument().setFile(this.destDir);
/*     */     } 
/*     */ 
/*     */     
/*  71 */     cmd.createArgument().setValue("-classpath");
/*     */     
/*  73 */     Path cp = new Path(this.project);
/*     */ 
/*     */     
/*  76 */     Path p = getBootClassPath();
/*  77 */     if (!p.isEmpty()) {
/*  78 */       cp.append(p);
/*     */     }
/*     */     
/*  81 */     if (this.extdirs != null) {
/*  82 */       cp.addExtdirs(this.extdirs);
/*     */     }
/*     */     
/*  85 */     cp.append(classpath);
/*  86 */     if (this.compileSourcepath != null) {
/*  87 */       cp.append(this.compileSourcepath);
/*     */     } else {
/*  89 */       cp.append(this.src);
/*     */     } 
/*     */     
/*  92 */     cmd.createArgument().setPath(cp);
/*     */ 
/*     */ 
/*     */     
/*  96 */     if (this.encoding != null) {
/*  97 */       cmd.createArgument().setValue("-encoding");
/*  98 */       cmd.createArgument().setValue(this.encoding);
/*     */     } 
/*     */     
/* 101 */     if (this.debug) {
/* 102 */       cmd.createArgument().setValue("-g");
/*     */     }
/*     */     
/* 105 */     if (this.optimize) {
/* 106 */       cmd.createArgument().setValue("-O2");
/*     */     }
/*     */     
/* 109 */     if (this.verbose) {
/* 110 */       cmd.createArgument().setValue("-verbose");
/*     */     }
/*     */     
/* 113 */     addCurrentCompilerArgs(cmd);
/*     */     
/* 115 */     logAndAddFilesToCompile(cmd);
/* 116 */     return cmd;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/Kjc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */