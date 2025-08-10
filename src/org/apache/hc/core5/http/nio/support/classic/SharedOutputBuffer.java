/*     */ package org.apache.hc.core5.http.nio.support.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public final class SharedOutputBuffer
/*     */   extends AbstractSharedBuffer
/*     */   implements ContentOutputBuffer
/*     */ {
/*     */   private volatile DataStreamChannel dataStreamChannel;
/*     */   private volatile boolean hasCapacity;
/*     */   private volatile boolean endStreamPropagated;
/*     */   
/*     */   public SharedOutputBuffer(ReentrantLock lock, int initialBufferSize) {
/*  49 */     super(lock, initialBufferSize);
/*  50 */     this.hasCapacity = false;
/*  51 */     this.endStreamPropagated = false;
/*     */   }
/*     */   
/*     */   public SharedOutputBuffer(int bufferSize) {
/*  55 */     this(new ReentrantLock(), bufferSize);
/*     */   }
/*     */   
/*     */   public void flush(DataStreamChannel channel) throws IOException {
/*  59 */     this.lock.lock();
/*     */     try {
/*  61 */       this.dataStreamChannel = channel;
/*  62 */       this.hasCapacity = true;
/*  63 */       setOutputMode();
/*  64 */       if (buffer().hasRemaining()) {
/*  65 */         this.dataStreamChannel.write(buffer());
/*     */       }
/*  67 */       if (!buffer().hasRemaining() && this.endStream) {
/*  68 */         propagateEndStream();
/*     */       }
/*  70 */       this.condition.signalAll();
/*     */     } finally {
/*  72 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void ensureNotAborted() throws InterruptedIOException {
/*  77 */     if (this.aborted) {
/*  78 */       throw new InterruptedIOException("Operation aborted");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  84 */     ByteBuffer src = ByteBuffer.wrap(b, off, len);
/*  85 */     this.lock.lock();
/*     */     try {
/*  87 */       ensureNotAborted();
/*  88 */       setInputMode();
/*  89 */       while (src.hasRemaining()) {
/*     */         
/*  91 */         if (src.remaining() < 1024 && buffer().remaining() > src.remaining()) {
/*  92 */           buffer().put(src); continue;
/*     */         } 
/*  94 */         if (buffer().position() > 0 || this.dataStreamChannel == null) {
/*  95 */           waitFlush();
/*     */         }
/*  97 */         if (buffer().position() == 0 && this.dataStreamChannel != null) {
/*  98 */           int bytesWritten = this.dataStreamChannel.write(src);
/*  99 */           if (bytesWritten == 0) {
/* 100 */             this.hasCapacity = false;
/* 101 */             waitFlush();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 107 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 113 */     this.lock.lock();
/*     */     try {
/* 115 */       ensureNotAborted();
/* 116 */       setInputMode();
/* 117 */       if (!buffer().hasRemaining()) {
/* 118 */         waitFlush();
/*     */       }
/* 120 */       buffer().put((byte)b);
/*     */     } finally {
/* 122 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeCompleted() throws IOException {
/* 128 */     if (this.endStream) {
/*     */       return;
/*     */     }
/* 131 */     this.lock.lock();
/*     */     try {
/* 133 */       if (!this.endStream) {
/* 134 */         this.endStream = true;
/* 135 */         if (this.dataStreamChannel != null) {
/* 136 */           setOutputMode();
/* 137 */           if (buffer().hasRemaining()) {
/* 138 */             this.dataStreamChannel.requestOutput();
/*     */           } else {
/* 140 */             propagateEndStream();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 145 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void waitFlush() throws InterruptedIOException {
/* 150 */     setOutputMode();
/* 151 */     if (this.dataStreamChannel != null) {
/* 152 */       this.dataStreamChannel.requestOutput();
/*     */     }
/* 154 */     ensureNotAborted();
/* 155 */     while (buffer().hasRemaining() || !this.hasCapacity) {
/*     */       try {
/* 157 */         this.condition.await();
/* 158 */       } catch (InterruptedException ex) {
/* 159 */         Thread.currentThread().interrupt();
/* 160 */         throw new InterruptedIOException(ex.getMessage());
/*     */       } 
/* 162 */       ensureNotAborted();
/*     */     } 
/* 164 */     setInputMode();
/*     */   }
/*     */   
/*     */   private void propagateEndStream() throws IOException {
/* 168 */     if (!this.endStreamPropagated) {
/* 169 */       this.dataStreamChannel.endStream();
/* 170 */       this.endStreamPropagated = true;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/SharedOutputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */