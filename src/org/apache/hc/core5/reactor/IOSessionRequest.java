/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class IOSessionRequest
/*     */   implements Future<IOSession>
/*     */ {
/*     */   final NamedEndpoint remoteEndpoint;
/*     */   final SocketAddress remoteAddress;
/*     */   final SocketAddress localAddress;
/*     */   final Timeout timeout;
/*     */   final Object attachment;
/*     */   final BasicFuture<IOSession> future;
/*     */   private final AtomicReference<ModalCloseable> closeableRef;
/*     */   
/*     */   public IOSessionRequest(NamedEndpoint remoteEndpoint, SocketAddress remoteAddress, SocketAddress localAddress, Timeout timeout, Object attachment, FutureCallback<IOSession> callback) {
/*  63 */     this.remoteEndpoint = remoteEndpoint;
/*  64 */     this.remoteAddress = remoteAddress;
/*  65 */     this.localAddress = localAddress;
/*  66 */     this.timeout = timeout;
/*  67 */     this.attachment = attachment;
/*  68 */     this.future = new BasicFuture(callback);
/*  69 */     this.closeableRef = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public void completed(ProtocolIOSession ioSession) {
/*  73 */     this.future.completed(ioSession);
/*  74 */     this.closeableRef.set(null);
/*     */   }
/*     */   
/*     */   public void failed(Exception cause) {
/*  78 */     this.future.failed(cause);
/*  79 */     this.closeableRef.set(null);
/*     */   }
/*     */   
/*     */   public boolean cancel() {
/*  83 */     boolean cancelled = this.future.cancel();
/*  84 */     ModalCloseable closeable = this.closeableRef.getAndSet(null);
/*  85 */     if (cancelled && closeable != null) {
/*  86 */       closeable.close(CloseMode.IMMEDIATE);
/*     */     }
/*  88 */     return cancelled;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  93 */     return cancel();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  98 */     return this.future.isCancelled();
/*     */   }
/*     */   
/*     */   public void assign(ModalCloseable closeable) {
/* 102 */     this.closeableRef.set(closeable);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 107 */     return this.future.isDone();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOSession get() throws InterruptedException, ExecutionException {
/* 112 */     return (IOSession)this.future.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOSession get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 117 */     return (IOSession)this.future.get(timeout, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return "[remoteEndpoint=" + this.remoteEndpoint + ", remoteAddress=" + this.remoteAddress + ", localAddress=" + this.localAddress + ", attachment=" + this.attachment + ']';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/IOSessionRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */