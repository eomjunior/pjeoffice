/*     */ package org.apache.tools.ant.taskdefs.optional.jsp.compilers;
/*     */ 
/*     */ import java.io.File;
/*     */ import org.apache.tools.ant.AntClassLoader;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Task;
/*     */ import org.apache.tools.ant.taskdefs.Java;
/*     */ import org.apache.tools.ant.taskdefs.optional.jsp.JspC;
/*     */ import org.apache.tools.ant.taskdefs.optional.jsp.JspMangler;
/*     */ import org.apache.tools.ant.types.CommandlineJava;
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
/*     */ public class JasperC
/*     */   extends DefaultJspCompilerAdapter
/*     */ {
/*     */   JspMangler mangler;
/*     */   
/*     */   public JasperC(JspMangler mangler) {
/*  54 */     this.mangler = mangler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute() throws BuildException {
/*  65 */     getJspc().log("Using jasper compiler", 3);
/*  66 */     CommandlineJava cmd = setupJasperCommand();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  71 */       Java java = new Java((Task)this.owner);
/*  72 */       Path p = getClasspath();
/*  73 */       if (getJspc().getClasspath() != null) {
/*  74 */         getProject().log("using user supplied classpath: " + p, 4);
/*     */       } else {
/*     */         
/*  77 */         getProject().log("using system classpath: " + p, 4);
/*     */       } 
/*     */       
/*  80 */       java.setClasspath(p);
/*  81 */       java.setDir(getProject().getBaseDir());
/*  82 */       java.setClassname("org.apache.jasper.JspC");
/*     */       
/*  84 */       for (String arg : cmd.getJavaCommand().getArguments()) {
/*  85 */         java.createArg().setValue(arg);
/*     */       }
/*  87 */       java.setFailonerror(getJspc().getFailonerror());
/*     */ 
/*     */       
/*  90 */       java.setFork(true);
/*  91 */       java.setTaskName("jasperc");
/*  92 */       java.execute();
/*  93 */       return true;
/*  94 */     } catch (Exception ex) {
/*  95 */       if (ex instanceof BuildException) {
/*  96 */         throw (BuildException)ex;
/*     */       }
/*  98 */       throw new BuildException("Error running jsp compiler: ", ex, 
/*  99 */           getJspc().getLocation());
/*     */     } finally {
/* 101 */       getJspc().deleteEmptyJavaFiles();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CommandlineJava setupJasperCommand() {
/* 110 */     CommandlineJava cmd = new CommandlineJava();
/* 111 */     JspC jspc = getJspc();
/* 112 */     addArg(cmd, "-d", jspc.getDestdir());
/* 113 */     addArg(cmd, "-p", jspc.getPackage());
/*     */     
/* 115 */     if (!isTomcat5x()) {
/* 116 */       addArg(cmd, "-v" + jspc.getVerbose());
/*     */     } else {
/* 118 */       getProject().log("this task doesn't support Tomcat 5.x properly, please use the Tomcat provided jspc task instead");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 123 */     addArg(cmd, "-uriroot", jspc.getUriroot());
/* 124 */     addArg(cmd, "-uribase", jspc.getUribase());
/* 125 */     addArg(cmd, "-ieplugin", jspc.getIeplugin());
/* 126 */     addArg(cmd, "-webinc", jspc.getWebinc());
/* 127 */     addArg(cmd, "-webxml", jspc.getWebxml());
/* 128 */     addArg(cmd, "-die9");
/*     */     
/* 130 */     if (jspc.isMapped()) {
/* 131 */       addArg(cmd, "-mapped");
/*     */     }
/* 133 */     if (jspc.getWebApp() != null) {
/* 134 */       File dir = jspc.getWebApp().getDirectory();
/* 135 */       addArg(cmd, "-webapp", dir);
/*     */     } 
/* 137 */     logAndAddFilesToCompile(getJspc(), getJspc().getCompileList(), cmd);
/* 138 */     return cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JspMangler createMangler() {
/* 146 */     return this.mangler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Path getClasspath() {
/* 153 */     Path p = getJspc().getClasspath();
/* 154 */     if (p == null) {
/* 155 */       p = new Path(getProject());
/* 156 */       return p.concatSystemClasspath("only");
/*     */     } 
/* 158 */     return p.concatSystemClasspath("ignore");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTomcat5x() {
/*     */     
/* 165 */     try { AntClassLoader l = getProject().createClassLoader(getClasspath()); 
/* 166 */       try { l.loadClass("org.apache.jasper.tagplugins.jstl.If");
/* 167 */         boolean bool = true;
/* 168 */         if (l != null) l.close();  return bool; } catch (Throwable throwable) { if (l != null) try { l.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (ClassNotFoundException e)
/* 169 */     { return false; }
/*     */   
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/optional/jsp/compilers/JasperC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */