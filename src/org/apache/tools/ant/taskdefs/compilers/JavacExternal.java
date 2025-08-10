/*     */ package org.apache.tools.ant.taskdefs.compilers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.types.Commandline;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ import org.apache.tools.ant.util.JavaEnvUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavacExternal
/*     */   extends DefaultCompilerAdapter
/*     */ {
/*     */   public boolean execute() throws BuildException {
/*     */     int firstFileName;
/*  45 */     this.attributes.log("Using external javac compiler", 3);
/*     */     
/*  47 */     Commandline cmd = new Commandline();
/*  48 */     cmd.setExecutable(getJavac().getJavacExecutable());
/*  49 */     if (assumeJava1_3Plus()) {
/*  50 */       setupModernJavacCommandlineSwitches(cmd);
/*     */     } else {
/*  52 */       setupJavacCommandlineSwitches(cmd, true);
/*     */     } 
/*     */     
/*  55 */     int openVmsFirstFileName = assumeJava1_2Plus() ? cmd.size() : -1;
/*     */     
/*  57 */     logAndAddFilesToCompile(cmd);
/*     */ 
/*     */ 
/*     */     
/*  61 */     if (Os.isFamily("openvms")) {
/*  62 */       return execOnVMS(cmd, openVmsFirstFileName);
/*     */     }
/*     */     
/*  65 */     String[] commandLine = cmd.getCommandline();
/*     */ 
/*     */     
/*  68 */     if (assumeJava1_2Plus()) {
/*  69 */       firstFileName = moveArgFileEligibleOptionsToEnd(commandLine);
/*     */     } else {
/*  71 */       firstFileName = -1;
/*     */     } 
/*     */     
/*  74 */     return (executeExternalCompile(commandLine, firstFileName, true) == 0);
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
/*     */   private int moveArgFileEligibleOptionsToEnd(String[] commandLine) {
/*  88 */     int nonArgFileOptionIdx = 1;
/*  89 */     while (nonArgFileOptionIdx < commandLine.length && 
/*  90 */       !isArgFileEligible(commandLine[nonArgFileOptionIdx])) {
/*  91 */       nonArgFileOptionIdx++;
/*     */     }
/*     */     
/*  94 */     for (int i = nonArgFileOptionIdx + 1; i < commandLine.length; i++) {
/*  95 */       if (!isArgFileEligible(commandLine[i])) {
/*  96 */         String option = commandLine[i];
/*  97 */         if (i - nonArgFileOptionIdx >= 0) {
/*  98 */           System.arraycopy(commandLine, nonArgFileOptionIdx, commandLine, nonArgFileOptionIdx + 1, i - nonArgFileOptionIdx);
/*     */         }
/* 100 */         commandLine[nonArgFileOptionIdx] = option;
/* 101 */         nonArgFileOptionIdx++;
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     return nonArgFileOptionIdx;
/*     */   }
/*     */   
/*     */   private static boolean isArgFileEligible(String option) {
/* 109 */     return (!option.startsWith("-J") && !option.startsWith("@"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean execOnVMS(Commandline cmd, int firstFileName) {
/* 119 */     File vmsFile = null;
/*     */     try {
/* 121 */       vmsFile = JavaEnvUtils.createVmsJavaOptionFile(cmd.getArguments());
/*     */ 
/*     */       
/* 124 */       String[] commandLine = { cmd.getExecutable(), "-V", vmsFile.getPath() };
/* 125 */       return (0 == executeExternalCompile(commandLine, firstFileName, true));
/*     */ 
/*     */     
/*     */     }
/* 129 */     catch (IOException e) {
/* 130 */       throw new BuildException("Failed to create a temporary file for \"-V\" switch");
/*     */     } finally {
/*     */       
/* 133 */       FileUtils.delete(vmsFile);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/compilers/JavacExternal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */