/*     */ package org.apache.tools.ant.taskdefs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.concurrent.CountDownLatch;
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
/*     */ public class StreamPumper
/*     */   implements Runnable
/*     */ {
/*     */   private static final int SMALL_BUFFER_SIZE = 128;
/*     */   private final InputStream is;
/*     */   private final OutputStream os;
/*     */   private volatile boolean askedToStop;
/*     */   private volatile boolean finished;
/*     */   private final boolean closeWhenExhausted;
/*     */   private boolean autoflush = false;
/*  43 */   private Exception exception = null;
/*  44 */   private int bufferSize = 128;
/*     */ 
/*     */   
/*     */   private boolean started = false;
/*     */ 
/*     */   
/*     */   private final boolean useAvailable;
/*     */   
/*     */   private PostStopHandle postStopHandle;
/*     */   
/*     */   private static final long POLL_INTERVAL = 100L;
/*     */ 
/*     */   
/*     */   public StreamPumper(InputStream is, OutputStream os, boolean closeWhenExhausted) {
/*  58 */     this(is, os, closeWhenExhausted, false);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamPumper(InputStream is, OutputStream os, boolean closeWhenExhausted, boolean useAvailable) {
/*  86 */     this.is = is;
/*  87 */     this.os = os;
/*  88 */     this.closeWhenExhausted = closeWhenExhausted;
/*  89 */     this.useAvailable = useAvailable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamPumper(InputStream is, OutputStream os) {
/*  99 */     this(is, os, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setAutoflush(boolean autoflush) {
/* 108 */     this.autoflush = autoflush;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 118 */     synchronized (this) {
/* 119 */       this.started = true;
/*     */     } 
/* 121 */     this.finished = false;
/*     */     
/* 123 */     byte[] buf = new byte[this.bufferSize];
/*     */ 
/*     */     
/*     */     try {
/* 127 */       while (!this.askedToStop && !Thread.interrupted()) {
/* 128 */         waitForInput(this.is);
/*     */         
/* 130 */         if (this.askedToStop || Thread.interrupted()) {
/*     */           break;
/*     */         }
/*     */         
/* 134 */         int length = this.is.read(buf);
/* 135 */         if (length < 0) {
/*     */           break;
/*     */         }
/*     */         
/* 139 */         if (length > 0) {
/*     */           
/* 141 */           this.os.write(buf, 0, length);
/* 142 */           if (this.autoflush) {
/* 143 */             this.os.flush();
/*     */           }
/*     */         } 
/*     */       } 
/* 147 */       doPostStop();
/* 148 */     } catch (InterruptedException interruptedException) {
/*     */     
/* 150 */     } catch (Exception e) {
/* 151 */       synchronized (this) {
/* 152 */         this.exception = e;
/*     */       } 
/*     */     } finally {
/* 155 */       if (this.closeWhenExhausted) {
/* 156 */         FileUtils.close(this.os);
/*     */       }
/* 158 */       this.finished = true;
/* 159 */       this.askedToStop = false;
/* 160 */       synchronized (this) {
/* 161 */         notifyAll();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinished() {
/* 171 */     return this.finished;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void waitFor() throws InterruptedException {
/* 180 */     while (!isFinished()) {
/* 181 */       wait();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setBufferSize(int bufferSize) {
/* 191 */     if (this.started) {
/* 192 */       throw new IllegalStateException("Cannot set buffer size on a running StreamPumper");
/*     */     }
/* 194 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getBufferSize() {
/* 203 */     return this.bufferSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Exception getException() {
/* 211 */     return this.exception;
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
/*     */   synchronized PostStopHandle stop() {
/* 227 */     this.askedToStop = true;
/* 228 */     this.postStopHandle = new PostStopHandle();
/* 229 */     notifyAll();
/* 230 */     return this.postStopHandle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void waitForInput(InputStream is) throws IOException, InterruptedException {
/* 237 */     if (this.useAvailable) {
/* 238 */       while (!this.askedToStop && is.available() == 0) {
/* 239 */         if (Thread.interrupted()) {
/* 240 */           throw new InterruptedException();
/*     */         }
/*     */         
/* 243 */         synchronized (this) {
/* 244 */           wait(100L);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void doPostStop() throws IOException {
/*     */     try {
/* 252 */       byte[] buf = new byte[this.bufferSize];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 258 */       if (this.askedToStop) {
/*     */         int bytesReadableWithoutBlocking;
/* 260 */         while ((bytesReadableWithoutBlocking = this.is.available()) > 0) {
/* 261 */           int length = this.is.read(buf, 0, Math.min(bytesReadableWithoutBlocking, buf.length));
/* 262 */           if (length <= 0) {
/*     */             break;
/*     */           }
/* 265 */           this.os.write(buf, 0, length);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 271 */       this.os.flush();
/*     */     } finally {
/* 273 */       if (this.postStopHandle != null) {
/* 274 */         this.postStopHandle.latch.countDown();
/* 275 */         this.postStopHandle.inPostStopTasks = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final class PostStopHandle
/*     */   {
/*     */     private boolean inPostStopTasks = true;
/*     */ 
/*     */     
/* 287 */     private final CountDownLatch latch = new CountDownLatch(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isInPostStopTasks() {
/* 295 */       return this.inPostStopTasks;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean awaitPostStopCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
/* 308 */       return this.latch.await(timeout, timeUnit);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/taskdefs/StreamPumper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */