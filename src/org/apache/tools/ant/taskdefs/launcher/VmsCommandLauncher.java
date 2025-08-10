/*     */ package org.apache.tools.ant.taskdefs.launcher;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import org.apache.tools.ant.Project;
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
/*     */ public class VmsCommandLauncher
/*     */   extends Java13CommandLauncher
/*     */ {
/*     */   public Process exec(Project project, String[] cmd, String[] env) throws IOException {
/*  51 */     File cmdFile = createCommandFile(project, cmd, env);
/*     */     
/*  53 */     Process p = super.exec(project, new String[] { cmdFile.getPath() }, env);
/*  54 */     deleteAfter(cmdFile, p);
/*  55 */     return p;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Process exec(Project project, String[] cmd, String[] env, File workingDir) throws IOException {
/*  79 */     File cmdFile = createCommandFile(project, cmd, env);
/*  80 */     Process p = super.exec(project, new String[] { cmdFile.getPath() }, env, workingDir);
/*     */     
/*  82 */     deleteAfter(cmdFile, p);
/*  83 */     return p;
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
/*     */   private File createCommandFile(Project project, String[] cmd, String[] env) throws IOException {
/*  96 */     File script = FILE_UTILS.createTempFile(project, "ANT", ".COM", null, true, true);
/*  97 */     BufferedWriter out = new BufferedWriter(new FileWriter(script));
/*     */ 
/*     */     
/* 100 */     try { if (env != null)
/*     */       {
/* 102 */         for (String variable : env) {
/* 103 */           int eqIndex = variable.indexOf('=');
/* 104 */           if (eqIndex != -1) {
/* 105 */             out.write("$ DEFINE/NOLOG ");
/* 106 */             out.write(variable.substring(0, eqIndex));
/* 107 */             out.write(" \"");
/* 108 */             out.write(variable.substring(eqIndex + 1));
/* 109 */             out.write(34);
/* 110 */             out.newLine();
/*     */           } 
/*     */         } 
/*     */       }
/* 114 */       out.write("$ " + cmd[0]);
/* 115 */       for (int i = 1; i < cmd.length; i++) {
/* 116 */         out.write(" -");
/* 117 */         out.newLine();
/* 118 */         out.write(cmd[i]);
/*     */       } 
/* 120 */       out.close(); } catch (Throwable throwable) { try { out.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
/* 121 */      return script;
/*     */   }
/*     */   
/*     */   private void deleteAfter(File f, Process p) {
/* 125 */     (new Thread(() -> {
/*     */           try {
/*     */             p.waitFor();
/* 128 */           } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */           
/*     */           FileUtils.delete(f);
/* 132 */         })).start();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/launcher/VmsCommandLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */