/*     */ package org.apache.tools.ant;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringReader;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.tools.ant.util.DateUtils;
/*     */ import org.apache.tools.ant.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultLogger
/*     */   implements BuildLogger
/*     */ {
/*     */   public static final int LEFT_COLUMN_SIZE = 12;
/*     */   protected PrintStream out;
/*     */   protected PrintStream err;
/*  54 */   protected int msgOutputLevel = 0;
/*     */ 
/*     */   
/*  57 */   private long startTime = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  62 */   protected static final String lSep = StringUtils.LINE_SEP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean emacsMode = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessageOutputLevel(int level) {
/*  93 */     this.msgOutputLevel = level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutputPrintStream(PrintStream output) {
/* 103 */     this.out = new PrintStream(output, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorPrintStream(PrintStream err) {
/* 113 */     this.err = new PrintStream(err, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEmacsMode(boolean emacsMode) {
/* 123 */     this.emacsMode = emacsMode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void buildStarted(BuildEvent event) {
/* 132 */     this.startTime = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   static void throwableMessage(StringBuffer m, Throwable error, boolean verbose) {
/* 136 */     while (error instanceof BuildException) {
/* 137 */       Throwable cause = error.getCause();
/* 138 */       if (cause == null) {
/*     */         break;
/*     */       }
/* 141 */       String msg1 = error.toString();
/* 142 */       String msg2 = cause.toString();
/* 143 */       if (msg1.endsWith(msg2)) {
/* 144 */         m.append(msg1, 0, msg1.length() - msg2.length());
/* 145 */         error = cause;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 150 */     if (verbose || !(error instanceof BuildException)) {
/* 151 */       m.append(StringUtils.getStackTrace(error));
/*     */     } else {
/* 153 */       m.append(String.format("%s%n", new Object[] { error }));
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
/*     */   public void buildFinished(BuildEvent event) {
/* 166 */     Throwable error = event.getException();
/* 167 */     StringBuffer message = new StringBuffer();
/* 168 */     if (error == null) {
/* 169 */       message.append(String.format("%n%s", new Object[] { getBuildSuccessfulMessage() }));
/*     */     } else {
/* 171 */       message.append(String.format("%n%s%n", new Object[] { getBuildFailedMessage() }));
/* 172 */       throwableMessage(message, error, (3 <= this.msgOutputLevel));
/*     */     } 
/* 174 */     message.append(String.format("%nTotal time: %s", new Object[] {
/* 175 */             formatTime(System.currentTimeMillis() - this.startTime)
/*     */           }));
/* 177 */     String msg = message.toString();
/* 178 */     if (error == null) {
/* 179 */       printMessage(msg, this.out, 3);
/*     */     } else {
/* 181 */       printMessage(msg, this.err, 0);
/*     */     } 
/* 183 */     log(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getBuildFailedMessage() {
/* 192 */     return "BUILD FAILED";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getBuildSuccessfulMessage() {
/* 201 */     return "BUILD SUCCESSFUL";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void targetStarted(BuildEvent event) {
/* 212 */     if (2 <= this.msgOutputLevel && 
/* 213 */       !event.getTarget().getName().isEmpty()) {
/* 214 */       String msg = String.format("%n%s:", new Object[] { event.getTarget().getName() });
/* 215 */       printMessage(msg, this.out, event.getPriority());
/* 216 */       log(msg);
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
/*     */   public void targetFinished(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskStarted(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void taskFinished(BuildEvent event) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void messageLogged(BuildEvent event) {
/* 253 */     int priority = event.getPriority();
/*     */     
/* 255 */     if (priority <= this.msgOutputLevel) {
/*     */       
/* 257 */       StringBuilder message = new StringBuilder();
/* 258 */       if (event.getTask() == null || this.emacsMode) {
/*     */         
/* 260 */         message.append(event.getMessage());
/*     */       } else {
/*     */         
/* 263 */         String name = event.getTask().getTaskName();
/* 264 */         String label = "[" + name + "] ";
/* 265 */         int size = 12 - label.length();
/*     */         
/* 267 */         String prefix = (size > 0) ? ((String)Stream.<CharSequence>generate(() -> " ").limit(size).collect(Collectors.joining()) + label) : label;
/*     */ 
/*     */         
/* 270 */         try { BufferedReader r = new BufferedReader(new StringReader(event.getMessage())); 
/* 271 */           try { message.append(r.lines()
/* 272 */                 .collect(Collectors.joining(System.lineSeparator() + prefix, prefix, "")));
/* 273 */             r.close(); } catch (Throwable throwable) { try { r.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/*     */         
/* 275 */         { message.append(label).append(event.getMessage()); }
/*     */       
/*     */       } 
/* 278 */       Throwable ex = event.getException();
/* 279 */       if (4 <= this.msgOutputLevel && ex != null) {
/* 280 */         message.append(String.format("%n%s: ", new Object[] { ex.getClass().getSimpleName()
/* 281 */               })).append(StringUtils.getStackTrace(ex));
/*     */       }
/*     */       
/* 284 */       String msg = message.toString();
/* 285 */       if (priority != 0) {
/* 286 */         printMessage(msg, this.out, priority);
/*     */       } else {
/* 288 */         printMessage(msg, this.err, priority);
/*     */       } 
/* 290 */       log(msg);
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
/*     */   protected static String formatTime(long millis) {
/* 304 */     return DateUtils.formatElapsedTime(millis);
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
/*     */   protected void printMessage(String message, PrintStream stream, int priority) {
/* 320 */     stream.println(message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void log(String message) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getTimestamp() {
/* 338 */     Date date = new Date(System.currentTimeMillis());
/* 339 */     DateFormat formatter = DateFormat.getDateTimeInstance(3, 3);
/* 340 */     return formatter.format(date);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String extractProjectName(BuildEvent event) {
/* 350 */     Project project = event.getProject();
/* 351 */     return (project != null) ? project.getName() : null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/DefaultLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */