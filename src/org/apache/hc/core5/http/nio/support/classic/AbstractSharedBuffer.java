/*     */ package org.apache.hc.core5.http.nio.support.classic;
/*     */ 
/*     */ import java.util.concurrent.locks.Condition;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.impl.nio.ExpandableBuffer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class AbstractSharedBuffer
/*     */   extends ExpandableBuffer
/*     */ {
/*     */   final ReentrantLock lock;
/*     */   final Condition condition;
/*     */   volatile boolean endStream;
/*     */   volatile boolean aborted;
/*     */   
/*     */   public AbstractSharedBuffer(ReentrantLock lock, int initialBufferSize) {
/*  50 */     super(initialBufferSize);
/*  51 */     this.lock = (ReentrantLock)Args.notNull(lock, "Lock");
/*  52 */     this.condition = lock.newCondition();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasData() {
/*  57 */     this.lock.lock();
/*     */     try {
/*  59 */       return super.hasData();
/*     */     } finally {
/*  61 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/*  67 */     this.lock.lock();
/*     */     try {
/*  69 */       return super.capacity();
/*     */     } finally {
/*  71 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int length() {
/*  77 */     this.lock.lock();
/*     */     try {
/*  79 */       return super.length();
/*     */     } finally {
/*  81 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void abort() {
/*  86 */     this.lock.lock();
/*     */     try {
/*  88 */       this.endStream = true;
/*  89 */       this.aborted = true;
/*  90 */       this.condition.signalAll();
/*     */     } finally {
/*  92 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void reset() {
/*  97 */     if (this.aborted) {
/*     */       return;
/*     */     }
/* 100 */     this.lock.lock();
/*     */     try {
/* 102 */       setInputMode();
/* 103 */       buffer().clear();
/* 104 */       this.endStream = false;
/*     */     } finally {
/* 106 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEndStream() {
/* 111 */     this.lock.lock();
/*     */     try {
/* 113 */       return (this.endStream && !super.hasData());
/*     */     } finally {
/* 115 */       this.lock.unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/classic/AbstractSharedBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */