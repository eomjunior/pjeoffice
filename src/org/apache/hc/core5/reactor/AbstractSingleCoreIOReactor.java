/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.nio.channels.ClosedSelectorException;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.Selector;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractSingleCoreIOReactor
/*     */   implements IOReactor
/*     */ {
/*     */   private final Callback<Exception> exceptionCallback;
/*     */   private final AtomicReference<IOReactorStatus> status;
/*     */   private final AtomicBoolean terminated;
/*     */   private final Object shutdownMutex;
/*     */   final Selector selector;
/*     */   
/*     */   AbstractSingleCoreIOReactor(Callback<Exception> exceptionCallback) {
/*  57 */     this.exceptionCallback = exceptionCallback;
/*  58 */     this.shutdownMutex = new Object();
/*  59 */     this.status = new AtomicReference<>(IOReactorStatus.INACTIVE);
/*  60 */     this.terminated = new AtomicBoolean();
/*     */     try {
/*  62 */       this.selector = Selector.open();
/*  63 */     } catch (IOException ex) {
/*  64 */       throw new IllegalStateException("Unexpected failure opening I/O selector", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final IOReactorStatus getStatus() {
/*  70 */     return this.status.get();
/*     */   }
/*     */   
/*     */   void logException(Exception ex) {
/*  74 */     if (this.exceptionCallback != null) {
/*  75 */       this.exceptionCallback.execute(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   abstract void doExecute() throws IOException;
/*     */   
/*     */   abstract void doTerminate() throws IOException;
/*     */   
/*     */   public void execute() {
/*  84 */     if (this.status.compareAndSet(IOReactorStatus.INACTIVE, IOReactorStatus.ACTIVE)) {
/*     */       
/*  86 */       try { doExecute(); }
/*  87 */       catch (ClosedSelectorException closedSelectorException)
/*     */       
/*     */       { 
/*     */         
/*     */         try { 
/*     */           
/*  93 */           doTerminate(); }
/*  94 */         catch (Exception ex)
/*  95 */         { logException(ex); }
/*     */         finally
/*  97 */         { close(CloseMode.IMMEDIATE); }  } catch (Exception ex) { logException(ex); } finally { try { doTerminate(); } catch (Exception ex) { logException(ex); } finally { close(CloseMode.IMMEDIATE); }
/*     */          }
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitShutdown(TimeValue waitTime) throws InterruptedException {
/* 105 */     Args.notNull(waitTime, "Wait time");
/* 106 */     long deadline = System.currentTimeMillis() + waitTime.toMilliseconds();
/* 107 */     long remaining = waitTime.toMilliseconds();
/* 108 */     synchronized (this.shutdownMutex) {
/* 109 */       while (((IOReactorStatus)this.status.get()).compareTo(IOReactorStatus.SHUT_DOWN) < 0) {
/* 110 */         this.shutdownMutex.wait(remaining);
/* 111 */         remaining = deadline - System.currentTimeMillis();
/* 112 */         if (remaining <= 0L) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void initiateShutdown() {
/* 121 */     if (this.status.compareAndSet(IOReactorStatus.INACTIVE, IOReactorStatus.SHUT_DOWN)) {
/* 122 */       synchronized (this.shutdownMutex) {
/* 123 */         this.shutdownMutex.notifyAll();
/*     */       } 
/* 125 */     } else if (this.status.compareAndSet(IOReactorStatus.ACTIVE, IOReactorStatus.SHUTTING_DOWN)) {
/* 126 */       this.selector.wakeup();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close(CloseMode closeMode) {
/* 132 */     close(closeMode, Timeout.ofSeconds(5L));
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
/*     */   public void close(CloseMode closeMode, Timeout timeout) {
/* 147 */     if (closeMode == CloseMode.GRACEFUL) {
/* 148 */       initiateShutdown();
/*     */       try {
/* 150 */         awaitShutdown((TimeValue)timeout);
/* 151 */       } catch (InterruptedException e) {
/* 152 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/* 155 */     this.status.set(IOReactorStatus.SHUT_DOWN);
/* 156 */     if (this.terminated.compareAndSet(false, true)) {
/*     */       try {
/* 158 */         Set<SelectionKey> keys = this.selector.keys();
/* 159 */         for (SelectionKey key : keys) {
/*     */           try {
/* 161 */             Closer.close((Closeable)key.attachment());
/* 162 */           } catch (IOException ex) {
/* 163 */             logException(ex);
/*     */           } 
/* 165 */           key.channel().close();
/*     */         } 
/* 167 */         this.selector.close();
/* 168 */       } catch (Exception ex) {
/* 169 */         logException(ex);
/*     */       } 
/*     */     }
/* 172 */     synchronized (this.shutdownMutex) {
/* 173 */       this.shutdownMutex.notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/* 179 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 184 */     return super.toString() + " [status=" + this.status + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/AbstractSingleCoreIOReactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */