/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
/*     */ import org.apache.hc.core5.reactor.IOReactorStatus;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractHttpAsyncClientBase
/*     */   extends CloseableHttpAsyncClient
/*     */ {
/*     */   enum Status
/*     */   {
/*  46 */     READY, RUNNING, TERMINATED;
/*     */   }
/*  48 */   private static final Logger LOG = LoggerFactory.getLogger(AbstractHttpAsyncClientBase.class);
/*     */   
/*     */   private final AsyncPushConsumerRegistry pushConsumerRegistry;
/*     */   
/*     */   private final DefaultConnectingIOReactor ioReactor;
/*     */   
/*     */   private final ExecutorService executorService;
/*     */   
/*     */   private final AtomicReference<Status> status;
/*     */ 
/*     */   
/*     */   AbstractHttpAsyncClientBase(DefaultConnectingIOReactor ioReactor, AsyncPushConsumerRegistry pushConsumerRegistry, ThreadFactory threadFactory) {
/*  60 */     this.ioReactor = ioReactor;
/*  61 */     this.pushConsumerRegistry = pushConsumerRegistry;
/*  62 */     this.executorService = Executors.newSingleThreadExecutor(threadFactory);
/*  63 */     this.status = new AtomicReference<>(Status.READY);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void start() {
/*  68 */     if (this.status.compareAndSet(Status.READY, Status.RUNNING)) {
/*  69 */       this.executorService.execute(this.ioReactor::start);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void register(String hostname, String uriPattern, Supplier<AsyncPushConsumer> supplier) {
/*  75 */     this.pushConsumerRegistry.register(hostname, uriPattern, supplier);
/*     */   }
/*     */   
/*     */   boolean isRunning() {
/*  79 */     return (this.status.get() == Status.RUNNING);
/*     */   }
/*     */   
/*     */   ConnectionInitiator getConnectionInitiator() {
/*  83 */     return (ConnectionInitiator)this.ioReactor;
/*     */   }
/*     */ 
/*     */   
/*     */   public final IOReactorStatus getStatus() {
/*  88 */     return this.ioReactor.getStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitShutdown(TimeValue waitTime) throws InterruptedException {
/*  93 */     this.ioReactor.awaitShutdown(waitTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void initiateShutdown() {
/*  98 */     if (LOG.isDebugEnabled()) {
/*  99 */       LOG.debug("Initiating shutdown");
/*     */     }
/* 101 */     this.ioReactor.initiateShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   void internalClose(CloseMode closeMode) {}
/*     */ 
/*     */   
/*     */   public final void close(CloseMode closeMode) {
/* 109 */     if (LOG.isDebugEnabled()) {
/* 110 */       LOG.debug("Shutdown {}", closeMode);
/*     */     }
/* 112 */     this.ioReactor.initiateShutdown();
/* 113 */     this.ioReactor.close(closeMode);
/* 114 */     this.executorService.shutdownNow();
/* 115 */     internalClose(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 120 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/AbstractHttpAsyncClientBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */