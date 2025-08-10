/*     */ package org.apache.tools.ant.taskdefs.compilers;
/*     */ 
/*     */ import org.apache.tools.ant.BuildException;
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
/*     */ public class Gcj
/*     */   extends DefaultCompilerAdapter
/*     */ {
/*  34 */   private static final String[] CONFLICT_WITH_DASH_C = new String[] { "-o", "--main=", "-D", "-fjni", "-L" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws BuildException {
/*  43 */     this.attributes.log("Using gcj compiler", 3);
/*  44 */     Commandline cmd = setupGCJCommand();
/*     */     
/*  46 */     int firstFileName = cmd.size();
/*  47 */     logAndAddFilesToCompile(cmd);
/*     */     
/*  49 */     return 
/*  50 */       (executeExternalCompile(cmd.getCommandline(), firstFileName) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Commandline setupGCJCommand() {
/*  58 */     Commandline cmd = new Commandline();
/*  59 */     Path classpath = new Path(this.project);
/*     */ 
/*     */ 
/*     */     
/*  63 */     Path p = getBootClassPath();
/*  64 */     if (!p.isEmpty()) {
/*  65 */       classpath.append(p);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  70 */     if (this.extdirs != null || this.includeJavaRuntime) {
/*  71 */       classpath.addExtdirs(this.extdirs);
/*     */     }
/*     */     
/*  74 */     classpath.append(getCompileClasspath());
/*     */ 
/*     */ 
/*     */     
/*  78 */     if (this.compileSourcepath != null) {
/*  79 */       classpath.append(this.compileSourcepath);
/*     */     } else {
/*  81 */       classpath.append(this.src);
/*     */     } 
/*     */     
/*  84 */     String exec = getJavac().getExecutable();
/*  85 */     cmd.setExecutable((exec == null) ? "gcj" : exec);
/*     */     
/*  87 */     if (this.destDir != null) {
/*  88 */       cmd.createArgument().setValue("-d");
/*  89 */       cmd.createArgument().setFile(this.destDir);
/*     */       
/*  91 */       if (!this.destDir.exists() && 
/*  92 */         !this.destDir.mkdirs() && !this.destDir.isDirectory()) {
/*  93 */         throw new BuildException("Can't make output directories. Maybe permission is wrong.");
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  98 */     cmd.createArgument().setValue("-classpath");
/*  99 */     cmd.createArgument().setPath(classpath);
/*     */     
/* 101 */     if (this.encoding != null) {
/* 102 */       cmd.createArgument().setValue("--encoding=" + this.encoding);
/*     */     }
/* 104 */     if (this.debug) {
/* 105 */       cmd.createArgument().setValue("-g1");
/*     */     }
/* 107 */     if (this.optimize) {
/* 108 */       cmd.createArgument().setValue("-O");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     if (!isNativeBuild()) {
/* 116 */       cmd.createArgument().setValue("-C");
/*     */     }
/*     */     
/* 119 */     if (this.attributes.getSource() != null) {
/* 120 */       String source = this.attributes.getSource();
/* 121 */       cmd.createArgument().setValue("-fsource=" + source);
/*     */     } 
/*     */     
/* 124 */     if (this.attributes.getTarget() != null) {
/* 125 */       String target = this.attributes.getTarget();
/* 126 */       cmd.createArgument().setValue("-ftarget=" + target);
/*     */     } 
/*     */     
/* 129 */     addCurrentCompilerArgs(cmd);
/*     */     
/* 131 */     return cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNativeBuild() {
/* 141 */     boolean nativeBuild = false;
/* 142 */     String[] additionalArguments = getJavac().getCurrentCompilerArgs();
/* 143 */     int argsLength = 0;
/* 144 */     while (!nativeBuild && argsLength < additionalArguments.length) {
/* 145 */       int conflictLength = 0;
/* 146 */       while (!nativeBuild && conflictLength < CONFLICT_WITH_DASH_C.length) {
/*     */         
/* 148 */         nativeBuild = additionalArguments[argsLength].startsWith(CONFLICT_WITH_DASH_C[conflictLength]);
/* 149 */         conflictLength++;
/*     */       } 
/* 151 */       argsLength++;
/*     */     } 
/* 153 */     return nativeBuild;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/Gcj.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */