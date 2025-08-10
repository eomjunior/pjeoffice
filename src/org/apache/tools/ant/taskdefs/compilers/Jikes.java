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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jikes
/*     */   extends DefaultCompilerAdapter
/*     */ {
/*     */   public boolean execute() throws BuildException {
/*     */     Path sourcepath;
/*  48 */     this.attributes.log("Using jikes compiler", 3);
/*     */     
/*  50 */     Commandline cmd = new Commandline();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     if (this.compileSourcepath != null) {
/*  56 */       sourcepath = this.compileSourcepath;
/*     */     } else {
/*  58 */       sourcepath = this.src;
/*     */     } 
/*     */ 
/*     */     
/*  62 */     if (!sourcepath.isEmpty()) {
/*  63 */       cmd.createArgument().setValue("-sourcepath");
/*  64 */       cmd.createArgument().setPath(sourcepath);
/*     */     } 
/*     */     
/*  67 */     Path classpath = new Path(this.project);
/*     */     
/*  69 */     if (this.bootclasspath == null || this.bootclasspath.isEmpty())
/*     */     {
/*  71 */       this.includeJavaRuntime = true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     classpath.append(getCompileClasspath());
/*     */ 
/*     */     
/*  81 */     String jikesPath = System.getProperty("jikes.class.path");
/*  82 */     if (jikesPath != null) {
/*  83 */       classpath.append(new Path(this.project, jikesPath));
/*     */     }
/*     */     
/*  86 */     if (this.extdirs != null && !this.extdirs.isEmpty()) {
/*  87 */       cmd.createArgument().setValue("-extdirs");
/*  88 */       cmd.createArgument().setPath(this.extdirs);
/*     */     } 
/*     */     
/*  91 */     String exec = getJavac().getExecutable();
/*  92 */     cmd.setExecutable((exec == null) ? "jikes" : exec);
/*     */     
/*  94 */     if (this.deprecation) {
/*  95 */       cmd.createArgument().setValue("-deprecation");
/*     */     }
/*     */     
/*  98 */     if (this.destDir != null) {
/*  99 */       cmd.createArgument().setValue("-d");
/* 100 */       cmd.createArgument().setFile(this.destDir);
/*     */     } 
/*     */     
/* 103 */     cmd.createArgument().setValue("-classpath");
/* 104 */     cmd.createArgument().setPath(classpath);
/*     */     
/* 106 */     if (this.encoding != null) {
/* 107 */       cmd.createArgument().setValue("-encoding");
/* 108 */       cmd.createArgument().setValue(this.encoding);
/*     */     } 
/* 110 */     if (this.debug) {
/* 111 */       String debugLevel = this.attributes.getDebugLevel();
/* 112 */       if (debugLevel != null) {
/* 113 */         cmd.createArgument().setValue("-g:" + debugLevel);
/*     */       } else {
/* 115 */         cmd.createArgument().setValue("-g");
/*     */       } 
/*     */     } else {
/* 118 */       cmd.createArgument().setValue("-g:none");
/*     */     } 
/* 120 */     if (this.optimize) {
/* 121 */       cmd.createArgument().setValue("-O");
/*     */     }
/* 123 */     if (this.verbose) {
/* 124 */       cmd.createArgument().setValue("-verbose");
/*     */     }
/* 126 */     if (this.depend) {
/* 127 */       cmd.createArgument().setValue("-depend");
/*     */     }
/*     */     
/* 130 */     if (this.target != null) {
/* 131 */       cmd.createArgument().setValue("-target");
/* 132 */       cmd.createArgument().setValue(this.target);
/*     */     } 
/*     */     
/* 135 */     addPropertyParams(cmd);
/*     */     
/* 137 */     if (this.attributes.getSource() != null) {
/* 138 */       cmd.createArgument().setValue("-source");
/* 139 */       String source = this.attributes.getSource();
/* 140 */       if ("1.1".equals(source) || "1.2".equals(source)) {
/*     */ 
/*     */         
/* 143 */         this.attributes.log("Jikes doesn't support '-source " + source + "', will use '-source 1.3' instead");
/*     */         
/* 145 */         cmd.createArgument().setValue("1.3");
/*     */       } else {
/* 147 */         cmd.createArgument().setValue(source);
/*     */       } 
/*     */     } 
/* 150 */     addCurrentCompilerArgs(cmd);
/*     */     
/* 152 */     int firstFileName = cmd.size();
/*     */     
/* 154 */     Path boot = getBootClassPath();
/* 155 */     if (!boot.isEmpty()) {
/* 156 */       cmd.createArgument().setValue("-bootclasspath");
/* 157 */       cmd.createArgument().setPath(boot);
/*     */     } 
/* 159 */     logAndAddFilesToCompile(cmd);
/*     */     
/* 161 */     return (executeExternalCompile(cmd.getCommandline(), firstFileName) == 0);
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
/*     */   private void addPropertyParams(Commandline cmd) {
/* 178 */     String emacsProperty = this.project.getProperty("build.compiler.emacs");
/* 179 */     if (emacsProperty != null && Project.toBoolean(emacsProperty)) {
/* 180 */       cmd.createArgument().setValue("+E");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 189 */     String warningsProperty = this.project.getProperty("build.compiler.warnings");
/* 190 */     if (warningsProperty != null) {
/* 191 */       this.attributes.log("!! the build.compiler.warnings property is deprecated. !!", 1);
/*     */       
/* 193 */       this.attributes.log("!! Use the nowarn attribute instead. !!", 1);
/* 194 */       if (!Project.toBoolean(warningsProperty)) {
/* 195 */         cmd.createArgument().setValue("-nowarn");
/*     */       }
/*     */     } 
/* 198 */     if (this.attributes.getNowarn()) {
/* 199 */       cmd.createArgument().setValue("-nowarn");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     String pedanticProperty = this.project.getProperty("build.compiler.pedantic");
/* 206 */     if (pedanticProperty != null && Project.toBoolean(pedanticProperty)) {
/* 207 */       cmd.createArgument().setValue("+P");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 215 */     String fullDependProperty = this.project.getProperty("build.compiler.fulldepend");
/* 216 */     if (fullDependProperty != null && 
/* 217 */       Project.toBoolean(fullDependProperty))
/* 218 */       cmd.createArgument().setValue("+F"); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/Jikes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */