/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultConnectingIOReactor
/*     */   extends AbstractIOReactorBase
/*     */ {
/*     */   private final int workerCount;
/*     */   private final SingleCoreIOReactor[] workers;
/*     */   private final MultiCoreIOReactor ioReactor;
/*     */   private final IOWorkers.Selector workerSelector;
/*  56 */   private static final ThreadFactory THREAD_FACTORY = (ThreadFactory)new DefaultThreadFactory("I/O client dispatch", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultConnectingIOReactor(IOEventHandlerFactory eventHandlerFactory, IOReactorConfig ioReactorConfig, ThreadFactory threadFactory, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, Callback<IOSession> sessionShutdownCallback) {
/*  66 */     Args.notNull(eventHandlerFactory, "Event handler factory");
/*  67 */     this.workerCount = (ioReactorConfig != null) ? ioReactorConfig.getIoThreadCount() : IOReactorConfig.DEFAULT.getIoThreadCount();
/*  68 */     this.workers = new SingleCoreIOReactor[this.workerCount];
/*  69 */     Thread[] threads = new Thread[this.workerCount];
/*  70 */     for (int i = 0; i < this.workers.length; i++) {
/*  71 */       SingleCoreIOReactor dispatcher = new SingleCoreIOReactor(exceptionCallback, eventHandlerFactory, (ioReactorConfig != null) ? ioReactorConfig : IOReactorConfig.DEFAULT, ioSessionDecorator, sessionListener, sessionShutdownCallback);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  78 */       this.workers[i] = dispatcher;
/*  79 */       threads[i] = ((threadFactory != null) ? threadFactory : THREAD_FACTORY).newThread(new IOReactorWorker(dispatcher));
/*     */     } 
/*  81 */     this.ioReactor = new MultiCoreIOReactor((IOReactor[])this.workers, threads);
/*  82 */     this.workerSelector = IOWorkers.newSelector(this.workers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultConnectingIOReactor(IOEventHandlerFactory eventHandlerFactory, IOReactorConfig config, Callback<IOSession> sessionShutdownCallback) {
/*  89 */     this(eventHandlerFactory, config, null, null, null, null, sessionShutdownCallback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultConnectingIOReactor(IOEventHandlerFactory eventHandlerFactory) {
/*  98 */     this(eventHandlerFactory, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 103 */     this.ioReactor.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOReactorStatus getStatus() {
/* 108 */     return this.ioReactor.getStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   IOWorkers.Selector getWorkerSelector() {
/* 113 */     return this.workerSelector;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initiateShutdown() {
/* 118 */     this.ioReactor.initiateShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void awaitShutdown(TimeValue waitTime) throws InterruptedException {
/* 123 */     this.ioReactor.awaitShutdown(waitTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 128 */     this.ioReactor.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 133 */     this.ioReactor.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/DefaultConnectingIOReactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */