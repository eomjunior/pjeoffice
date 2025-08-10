/*     */ package org.apache.hc.core5.http.nio.support.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class SharedInputBuffer
/*     */   extends AbstractSharedBuffer
/*     */   implements ContentInputBuffer
/*     */ {
/*     */   private final int initialBufferSize;
/*     */   private final AtomicInteger capacityIncrement;
/*     */   private volatile CapacityChannel capacityChannel;
/*     */   
/*     */   public SharedInputBuffer(ReentrantLock lock, int initialBufferSize) {
/*  51 */     super(lock, initialBufferSize);
/*  52 */     this.initialBufferSize = initialBufferSize;
/*  53 */     this.capacityIncrement = new AtomicInteger(0);
/*     */   }
/*     */   
/*     */   public SharedInputBuffer(int bufferSize) {
/*  57 */     this(new ReentrantLock(), bufferSize);
/*     */   }
/*     */   
/*     */   public int fill(ByteBuffer src) {
/*  61 */     this.lock.lock();
/*     */     try {
/*  63 */       setInputMode();
/*  64 */       ensureAdjustedCapacity(buffer().position() + src.remaining());
/*  65 */       buffer().put(src);
/*  66 */       int remaining = buffer().remaining();
/*  67 */       this.condition.signalAll();
/*  68 */       return remaining;
/*     */     } finally {
/*  70 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void incrementCapacity() throws IOException {
/*  75 */     if (this.capacityChannel != null) {
/*  76 */       int increment = this.capacityIncrement.getAndSet(0);
/*  77 */       if (increment > 0) {
/*  78 */         this.capacityChannel.update(increment);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/*  84 */     this.lock.lock();
/*     */     try {
/*  86 */       this.capacityChannel = capacityChannel;
/*  87 */       setInputMode();
/*  88 */       if (buffer().position() == 0) {
/*  89 */         capacityChannel.update(this.initialBufferSize);
/*     */       }
/*     */     } finally {
/*  92 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void awaitInput() throws InterruptedIOException {
/*  97 */     if (!buffer().hasRemaining()) {
/*  98 */       setInputMode();
/*  99 */       while (buffer().position() == 0 && !this.endStream && !this.aborted) {
/*     */         try {
/* 101 */           this.condition.await();
/* 102 */         } catch (InterruptedException ex) {
/* 103 */           Thread.currentThread().interrupt();
/* 104 */           throw new InterruptedIOException(ex.getMessage());
/*     */         } 
/*     */       } 
/* 107 */       setOutputMode();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 113 */     this.lock.lock();
/*     */     try {
/* 115 */       setOutputMode();
/* 116 */       awaitInput();
/* 117 */       if (this.aborted) {
/* 118 */         return -1;
/*     */       }
/* 120 */       if (!buffer().hasRemaining() && this.endStream) {
/* 121 */         return -1;
/*     */       }
/* 123 */       int b = buffer().get() & 0xFF;
/* 124 */       this.capacityIncrement.incrementAndGet();
/* 125 */       if (!buffer().hasRemaining()) {
/* 126 */         incrementCapacity();
/*     */       }
/* 128 */       return b;
/*     */     } finally {
/* 130 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 136 */     this.lock.lock();
/*     */     try {
/* 138 */       setOutputMode();
/* 139 */       awaitInput();
/* 140 */       if (this.aborted) {
/* 141 */         return -1;
/*     */       }
/* 143 */       if (!buffer().hasRemaining() && this.endStream) {
/* 144 */         return -1;
/*     */       }
/* 146 */       int chunk = Math.min(buffer().remaining(), len);
/* 147 */       buffer().get(b, off, chunk);
/* 148 */       this.capacityIncrement.addAndGet(chunk);
/* 149 */       if (!buffer().hasRemaining()) {
/* 150 */         incrementCapacity();
/*     */       }
/* 152 */       return chunk;
/*     */     } finally {
/* 154 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void markEndStream() {
/* 159 */     if (this.endStream) {
/*     */       return;
/*     */     }
/* 162 */     this.lock.lock();
/*     */     try {
/* 164 */       if (!this.endStream) {
/* 165 */         this.endStream = true;
/* 166 */         this.capacityChannel = null;
/* 167 */         this.condition.signalAll();
/*     */       } 
/*     */     } finally {
/* 170 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/SharedInputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */