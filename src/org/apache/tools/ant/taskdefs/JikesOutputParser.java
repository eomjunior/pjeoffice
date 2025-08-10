/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.tools.ant.Task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class JikesOutputParser
/*     */   implements ExecuteStreamHandler
/*     */ {
/*     */   protected Task task;
/*     */   protected boolean errorFlag = false;
/*     */   protected int errors;
/*     */   protected int warnings;
/*     */   protected boolean error = false;
/*     */   protected boolean emacsMode;
/*     */   protected BufferedReader br;
/*     */   
/*     */   public void setProcessInputStream(OutputStream os) {}
/*     */   
/*     */   public void setProcessErrorStream(InputStream is) {}
/*     */   
/*     */   public void setProcessOutputStream(InputStream is) throws IOException {
/*  74 */     this.br = new BufferedReader(new InputStreamReader(is));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() throws IOException {
/*  82 */     parseOutput(this.br);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JikesOutputParser(Task task, boolean emacsMode) {
/*  99 */     System.err.println("As of Ant 1.2 released in October 2000, the JikesOutputParser class");
/*     */     
/* 101 */     System.err.println("is considered to be dead code by the Ant developers and is unmaintained.");
/*     */     
/* 103 */     System.err.println("Don't use it!");
/*     */     
/* 105 */     this.task = task;
/* 106 */     this.emacsMode = emacsMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseOutput(BufferedReader reader) throws IOException {
/* 115 */     if (this.emacsMode) {
/* 116 */       parseEmacsOutput(reader);
/*     */     } else {
/* 118 */       parseStandardOutput(reader);
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
/*     */   private void parseStandardOutput(BufferedReader reader) throws IOException {
/*     */     String line;
/* 134 */     while ((line = reader.readLine()) != null) {
/* 135 */       String lower = line.toLowerCase();
/* 136 */       if (line.trim().isEmpty()) {
/*     */         continue;
/*     */       }
/* 139 */       if (lower.contains("error")) {
/* 140 */         setError(true);
/* 141 */       } else if (lower.contains("warning")) {
/* 142 */         setError(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 150 */       else if (this.emacsMode) {
/* 151 */         setError(true);
/*     */       } 
/*     */       
/* 154 */       log(line);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void parseEmacsOutput(BufferedReader reader) throws IOException {
/* 160 */     parseStandardOutput(reader);
/*     */   }
/*     */   
/*     */   private void setError(boolean err) {
/* 164 */     this.error = err;
/* 165 */     if (this.error) {
/* 166 */       this.errorFlag = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void log(String line) {
/* 171 */     if (!this.emacsMode) {
/* 172 */       this.task.log("", this.error ? 0 : 1);
/*     */     }
/* 174 */     this.task.log(line, this.error ? 0 : 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean getErrorFlag() {
/* 182 */     return this.errorFlag;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/JikesOutputParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */