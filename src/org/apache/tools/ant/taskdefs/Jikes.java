/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.taskdefs.condition.Os;
/*     */ import org.apache.tools.ant.util.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class Jikes
/*     */ {
/*     */   private static final int MAX_FILES_ON_COMMAND_LINE = 250;
/*     */   protected JikesOutputParser jop;
/*     */   protected String command;
/*     */   protected Project project;
/*     */   
/*     */   protected Jikes(JikesOutputParser jop, String command, Project project) {
/*  61 */     System.err.println("As of Ant 1.2 released in October 2000, the Jikes class");
/*     */     
/*  63 */     System.err.println("is considered to be dead code by the Ant developers and is unmaintained.");
/*     */     
/*  65 */     System.err.println("Don't use it!");
/*     */     
/*  67 */     this.jop = jop;
/*  68 */     this.command = command;
/*  69 */     this.project = project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void compile(String[] args) {
/*  77 */     String[] commandArray = null;
/*  78 */     File tmpFile = null;
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  83 */       if (Os.isFamily("windows") && args.length > 250) {
/*  84 */         tmpFile = FileUtils.getFileUtils().createTempFile(this.project, "jikes", "tmp", null, false, true);
/*     */         
/*  86 */         try { BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile)); 
/*  87 */           try { for (String arg : args) {
/*  88 */               out.write(arg);
/*  89 */               out.newLine();
/*     */             } 
/*  91 */             out.flush();
/*     */             
/*  93 */             commandArray = new String[] { this.command, "@" + tmpFile.getAbsolutePath() };
/*  94 */             out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/*  95 */         { throw new BuildException("Error creating temporary file", e); }
/*     */       
/*     */       } else {
/*  98 */         commandArray = new String[args.length + 1];
/*  99 */         commandArray[0] = this.command;
/* 100 */         System.arraycopy(args, 0, commandArray, 1, args.length);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 108 */         Execute exe = new Execute(this.jop);
/* 109 */         exe.setAntRun(this.project);
/* 110 */         exe.setWorkingDirectory(this.project.getBaseDir());
/* 111 */         exe.setCommandline(commandArray);
/* 112 */         exe.execute();
/* 113 */       } catch (IOException e) {
/* 114 */         throw new BuildException("Error running Jikes compiler", e);
/*     */       } 
/*     */     } finally {
/* 117 */       if (tmpFile != null && !tmpFile.delete())
/* 118 */         tmpFile.deleteOnExit(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/Jikes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */