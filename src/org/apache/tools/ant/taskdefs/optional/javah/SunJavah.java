/*     */ package org.apache.tools.ant.taskdefs.optional.javah;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.ProjectComponent;
/*     */ import org.apache.tools.ant.launch.Locator;
/*     */ import org.apache.tools.ant.taskdefs.ExecuteJava;
/*     */ import org.apache.tools.ant.taskdefs.optional.Javah;
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
/*     */ public class SunJavah
/*     */   implements JavahAdapter
/*     */ {
/*     */   public static final String IMPLEMENTATION_NAME = "sun";
/*     */   
/*     */   public boolean compile(Javah javah) throws BuildException {
/*     */     Class<?> c;
/*  49 */     Commandline cmd = setupJavahCommand(javah);
/*  50 */     ExecuteJava ej = new ExecuteJava();
/*     */ 
/*     */     
/*     */     try {
/*     */       try {
/*  55 */         c = Class.forName("com.sun.tools.javah.oldjavah.Main");
/*  56 */       } catch (ClassNotFoundException cnfe) {
/*     */         
/*  58 */         c = Class.forName("com.sun.tools.javah.Main");
/*     */       } 
/*  60 */     } catch (ClassNotFoundException ex) {
/*  61 */       throw new BuildException("Can't load javah", ex, javah
/*  62 */           .getLocation());
/*     */     } 
/*  64 */     cmd.setExecutable(c.getName());
/*  65 */     ej.setJavaCommand(cmd);
/*  66 */     File f = Locator.getClassSource(c);
/*  67 */     if (f != null) {
/*  68 */       ej.setClasspath(new Path(javah.getProject(), f.getPath()));
/*     */     }
/*  70 */     return (ej.fork((ProjectComponent)javah) == 0);
/*     */   }
/*     */   
/*     */   static Commandline setupJavahCommand(Javah javah) {
/*  74 */     Commandline cmd = new Commandline();
/*     */     
/*  76 */     if (javah.getDestdir() != null) {
/*  77 */       cmd.createArgument().setValue("-d");
/*  78 */       cmd.createArgument().setFile(javah.getDestdir());
/*     */     } 
/*     */     
/*  81 */     if (javah.getOutputfile() != null) {
/*  82 */       cmd.createArgument().setValue("-o");
/*  83 */       cmd.createArgument().setFile(javah.getOutputfile());
/*     */     } 
/*     */     
/*  86 */     if (javah.getClasspath() != null) {
/*  87 */       cmd.createArgument().setValue("-classpath");
/*  88 */       cmd.createArgument().setPath(javah.getClasspath());
/*     */     } 
/*     */     
/*  91 */     if (javah.getVerbose()) {
/*  92 */       cmd.createArgument().setValue("-verbose");
/*     */     }
/*  94 */     if (javah.getOld()) {
/*  95 */       cmd.createArgument().setValue("-old");
/*     */     }
/*  97 */     if (javah.getForce()) {
/*  98 */       cmd.createArgument().setValue("-force");
/*     */     }
/* 100 */     if (javah.getStubs() && !javah.getOld()) {
/* 101 */       throw new BuildException("stubs only available in old mode.", javah
/* 102 */           .getLocation());
/*     */     }
/*     */     
/* 105 */     if (javah.getStubs()) {
/* 106 */       cmd.createArgument().setValue("-stubs");
/*     */     }
/* 108 */     Path bcp = new Path(javah.getProject());
/* 109 */     if (javah.getBootclasspath() != null) {
/* 110 */       bcp.append(javah.getBootclasspath());
/*     */     }
/* 112 */     bcp = bcp.concatSystemBootClasspath("ignore");
/* 113 */     if (bcp.size() > 0) {
/* 114 */       cmd.createArgument().setValue("-bootclasspath");
/* 115 */       cmd.createArgument().setPath(bcp);
/*     */     } 
/*     */     
/* 118 */     cmd.addArguments(javah.getCurrentArgs());
/*     */     
/* 120 */     javah.logAndAddFiles(cmd);
/* 121 */     return cmd;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/javah/SunJavah.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */