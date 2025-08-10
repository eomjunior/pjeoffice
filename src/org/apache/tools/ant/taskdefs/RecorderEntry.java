/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.file.Paths;
/*     */ import org.apache.tools.ant.BuildEvent;
/*     */ import org.apache.tools.ant.BuildException;
/*     */ import org.apache.tools.ant.BuildListener;
/*     */ import org.apache.tools.ant.BuildLogger;
/*     */ import org.apache.tools.ant.Project;
/*     */ import org.apache.tools.ant.SubBuildListener;
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
/*     */ public class RecorderEntry
/*     */   implements BuildLogger, SubBuildListener
/*     */ {
/*  44 */   private String filename = null;
/*     */   
/*     */   private boolean record = true;
/*     */   
/*  48 */   private int loglevel = 2;
/*     */   
/*  50 */   private PrintStream out = null;
/*     */   
/*  52 */   private long targetStartTime = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean emacsMode = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private Project project;
/*     */ 
/*     */ 
/*     */   
/*     */   protected RecorderEntry(String name) {
/*  65 */     this.targetStartTime = System.currentTimeMillis();
/*  66 */     this.filename = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFilename() {
/*  76 */     return this.filename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRecordState(Boolean state) {
/*  85 */     if (state != null) {
/*  86 */       flush();
/*  87 */       this.record = state.booleanValue();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildStarted(BuildEvent event) {
/*  96 */     log("> BUILD STARTED", 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildFinished(BuildEvent event) {
/* 104 */     log("< BUILD FINISHED", 4);
/*     */     
/* 106 */     if (this.record && this.out != null) {
/* 107 */       Throwable error = event.getException();
/*     */       
/* 109 */       if (error == null) {
/* 110 */         this.out.printf("%nBUILD SUCCESSFUL%n", new Object[0]);
/*     */       } else {
/* 112 */         this.out.printf("%nBUILD FAILED%n%n", new Object[0]);
/* 113 */         error.printStackTrace(this.out);
/*     */       } 
/*     */     } 
/* 116 */     cleanup();
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
/*     */   public void subBuildFinished(BuildEvent event) {
/* 129 */     if (event.getProject() == this.project) {
/* 130 */       cleanup();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void subBuildStarted(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void targetStarted(BuildEvent event) {
/* 149 */     log(">> TARGET STARTED -- " + event.getTarget(), 4);
/* 150 */     log(String.format("%n%s:", new Object[] { event.getTarget().getName() }), 2);
/* 151 */     this.targetStartTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void targetFinished(BuildEvent event) {
/* 159 */     log("<< TARGET FINISHED -- " + event.getTarget(), 4);
/*     */     
/* 161 */     String time = formatTime(System.currentTimeMillis() - this.targetStartTime);
/*     */     
/* 163 */     log(event.getTarget() + ":  duration " + time, 3);
/* 164 */     flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskStarted(BuildEvent event) {
/* 172 */     log(">>> TASK STARTED -- " + event.getTask(), 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskFinished(BuildEvent event) {
/* 180 */     log("<<< TASK FINISHED -- " + event.getTask(), 4);
/* 181 */     flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageLogged(BuildEvent event) {
/* 189 */     log("--- MESSAGE LOGGED", 4);
/*     */     
/* 191 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 193 */     if (event.getTask() != null) {
/* 194 */       String name = event.getTask().getTaskName();
/*     */       
/* 196 */       if (!this.emacsMode) {
/* 197 */         String label = "[" + name + "] ";
/* 198 */         int size = 12 - label.length();
/*     */         
/* 200 */         for (int i = 0; i < size; i++) {
/* 201 */           buf.append(" ");
/*     */         }
/* 203 */         buf.append(label);
/*     */       } 
/*     */     } 
/* 206 */     buf.append(event.getMessage());
/*     */     
/* 208 */     log(buf.toString(), event.getPriority());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void log(String mesg, int level) {
/* 219 */     if (this.record && level <= this.loglevel && this.out != null) {
/* 220 */       this.out.println(mesg);
/*     */     }
/*     */   }
/*     */   
/*     */   private void flush() {
/* 225 */     if (this.record && this.out != null) {
/* 226 */       this.out.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageOutputLevel(int level) {
/* 235 */     if (level >= 0 && level <= 4) {
/* 236 */       this.loglevel = level;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputPrintStream(PrintStream output) {
/* 245 */     closeFile();
/* 246 */     this.out = output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmacsMode(boolean emacsMode) {
/* 255 */     this.emacsMode = emacsMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorPrintStream(PrintStream err) {
/* 264 */     setOutputPrintStream(err);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String formatTime(long millis) {
/* 270 */     long seconds = millis / 1000L;
/* 271 */     long minutes = seconds / 60L;
/*     */ 
/*     */     
/* 274 */     if (minutes > 0L) {
/* 275 */       return minutes + " minute" + (
/* 276 */         (minutes == 1L) ? " " : "s ") + (seconds % 60L) + " second" + (
/*     */         
/* 278 */         (seconds % 60L == 1L) ? "" : "s");
/*     */     }
/* 280 */     return seconds + " second" + (
/* 281 */       (seconds % 60L == 1L) ? "" : "s");
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
/*     */   public void setProject(Project project) {
/* 294 */     this.project = project;
/* 295 */     if (project != null) {
/* 296 */       project.addBuildListener((BuildListener)this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Project getProject() {
/* 307 */     return this.project;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void cleanup() {
/* 314 */     closeFile();
/* 315 */     if (this.project != null) {
/* 316 */       this.project.removeBuildListener((BuildListener)this);
/*     */     }
/* 318 */     this.project = null;
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
/*     */   void openFile(boolean append) throws BuildException {
/* 330 */     openFileImpl(append);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void closeFile() {
/* 339 */     if (this.out != null) {
/* 340 */       this.out.close();
/* 341 */       this.out = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void reopenFile() throws BuildException {
/* 352 */     openFileImpl(true);
/*     */   }
/*     */   
/*     */   private void openFileImpl(boolean append) throws BuildException {
/* 356 */     if (this.out == null)
/*     */       try {
/* 358 */         this.out = new PrintStream(FileUtils.newOutputStream(Paths.get(this.filename, new String[0]), append));
/* 359 */       } catch (IOException ioe) {
/* 360 */         throw new BuildException("Problems opening file using a recorder entry", ioe);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/RecorderEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */