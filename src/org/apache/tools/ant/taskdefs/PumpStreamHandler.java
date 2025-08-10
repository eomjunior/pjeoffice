/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class PumpStreamHandler
/*     */   implements ExecuteStreamHandler
/*     */ {
/*     */   private Thread outputThread;
/*     */   private Thread errorThread;
/*     */   private Thread inputThread;
/*     */   private OutputStream out;
/*     */   private OutputStream err;
/*     */   private InputStream input;
/*     */   private final boolean nonBlockingRead;
/*     */   private static final long JOIN_TIMEOUT = 200L;
/*     */   
/*     */   public PumpStreamHandler(OutputStream out, OutputStream err, InputStream input, boolean nonBlockingRead) {
/*  55 */     if (out == null) {
/*  56 */       throw new NullPointerException("out must not be null");
/*     */     }
/*  58 */     if (err == null) {
/*  59 */       throw new NullPointerException("err must not be null");
/*     */     }
/*  61 */     this.out = out;
/*  62 */     this.err = err;
/*  63 */     this.input = input;
/*  64 */     this.nonBlockingRead = nonBlockingRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PumpStreamHandler(OutputStream out, OutputStream err, InputStream input) {
/*  75 */     this(out, err, input, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PumpStreamHandler(OutputStream out, OutputStream err) {
/*  84 */     this(out, err, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PumpStreamHandler(OutputStream outAndErr) {
/*  92 */     this(outAndErr, outAndErr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PumpStreamHandler() {
/*  99 */     this(System.out, System.err);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessOutputStream(InputStream is) {
/* 108 */     createProcessOutputPump(is, this.out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessErrorStream(InputStream is) {
/* 117 */     createProcessErrorPump(is, this.err);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessInputStream(OutputStream os) {
/* 126 */     if (this.input != null) {
/* 127 */       this.inputThread = createPump(this.input, os, true, this.nonBlockingRead);
/*     */     } else {
/* 129 */       FileUtils.close(os);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 137 */     start(this.outputThread);
/* 138 */     start(this.errorThread);
/* 139 */     start(this.inputThread);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/* 146 */     finish(this.inputThread);
/*     */     
/*     */     try {
/* 149 */       this.err.flush();
/* 150 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/*     */     try {
/* 154 */       this.out.flush();
/* 155 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 158 */     finish(this.outputThread);
/* 159 */     finish(this.errorThread);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void start(Thread t) {
/* 165 */     if (t != null) {
/* 166 */       t.start();
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
/*     */   protected final void finish(Thread t) {
/* 180 */     if (t == null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     try {
/* 185 */       StreamPumper s = null;
/* 186 */       if (t instanceof ThreadWithPumper) {
/* 187 */         s = ((ThreadWithPumper)t).getPumper();
/*     */       }
/* 189 */       if (s != null && s.isFinished()) {
/*     */         return;
/*     */       }
/* 192 */       if (!t.isAlive()) {
/*     */         return;
/*     */       }
/* 195 */       StreamPumper.PostStopHandle postStopHandle = null;
/* 196 */       if (s != null && !s.isFinished()) {
/* 197 */         postStopHandle = s.stop();
/*     */       }
/* 199 */       if (postStopHandle != null && postStopHandle.isInPostStopTasks())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 205 */         postStopHandle.awaitPostStopCompletion(2L, TimeUnit.SECONDS);
/*     */       }
/* 207 */       while ((s == null || !s.isFinished()) && t.isAlive()) {
/*     */ 
/*     */         
/* 210 */         t.interrupt();
/* 211 */         t.join(200L);
/*     */       } 
/* 213 */     } catch (InterruptedException interruptedException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream getErr() {
/* 223 */     return this.err;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OutputStream getOut() {
/* 231 */     return this.out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createProcessOutputPump(InputStream is, OutputStream os) {
/* 240 */     this.outputThread = createPump(is, os);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createProcessErrorPump(InputStream is, OutputStream os) {
/* 249 */     this.errorThread = createPump(is, os);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Thread createPump(InputStream is, OutputStream os) {
/* 260 */     return createPump(is, os, false);
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
/*     */   protected Thread createPump(InputStream is, OutputStream os, boolean closeWhenExhausted) {
/* 275 */     return createPump(is, os, closeWhenExhausted, true);
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
/*     */   protected Thread createPump(InputStream is, OutputStream os, boolean closeWhenExhausted, boolean nonBlockingIO) {
/* 293 */     StreamPumper pumper = new StreamPumper(is, os, closeWhenExhausted, nonBlockingIO);
/* 294 */     pumper.setAutoflush(true);
/* 295 */     Thread result = new ThreadWithPumper(pumper);
/* 296 */     result.setDaemon(true);
/* 297 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class ThreadWithPumper
/*     */     extends Thread
/*     */   {
/*     */     private final StreamPumper pumper;
/*     */ 
/*     */     
/*     */     public ThreadWithPumper(StreamPumper p) {
/* 308 */       super(p);
/* 309 */       this.pumper = p;
/*     */     }
/*     */     protected StreamPumper getPumper() {
/* 312 */       return this.pumper;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/PumpStreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */