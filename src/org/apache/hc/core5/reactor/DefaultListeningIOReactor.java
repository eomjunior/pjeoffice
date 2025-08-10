/*     */ package org.apache.hc.core5.reactor;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
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
/*     */ public class DefaultListeningIOReactor
/*     */   extends AbstractIOReactorBase
/*     */   implements ConnectionAcceptor
/*     */ {
/*  55 */   private static final ThreadFactory DISPATCH_THREAD_FACTORY = (ThreadFactory)new DefaultThreadFactory("I/O server dispatch", true);
/*  56 */   private static final ThreadFactory LISTENER_THREAD_FACTORY = (ThreadFactory)new DefaultThreadFactory("I/O listener", true);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int workerCount;
/*     */ 
/*     */ 
/*     */   
/*     */   private final SingleCoreIOReactor[] workers;
/*     */ 
/*     */ 
/*     */   
/*     */   private final SingleCoreListeningIOReactor listener;
/*     */ 
/*     */ 
/*     */   
/*     */   private final MultiCoreIOReactor ioReactor;
/*     */ 
/*     */ 
/*     */   
/*     */   private final IOWorkers.Selector workerSelector;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultListeningIOReactor(IOEventHandlerFactory eventHandlerFactory, IOReactorConfig ioReactorConfig, ThreadFactory dispatchThreadFactory, ThreadFactory listenerThreadFactory, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, Callback<IOSession> sessionShutdownCallback) {
/*  83 */     Args.notNull(eventHandlerFactory, "Event handler factory");
/*  84 */     this.workerCount = (ioReactorConfig != null) ? ioReactorConfig.getIoThreadCount() : IOReactorConfig.DEFAULT.getIoThreadCount();
/*  85 */     this.workers = new SingleCoreIOReactor[this.workerCount];
/*  86 */     Thread[] threads = new Thread[this.workerCount + 1];
/*  87 */     for (int i = 0; i < this.workers.length; i++) {
/*  88 */       SingleCoreIOReactor dispatcher = new SingleCoreIOReactor(exceptionCallback, eventHandlerFactory, (ioReactorConfig != null) ? ioReactorConfig : IOReactorConfig.DEFAULT, ioSessionDecorator, sessionListener, sessionShutdownCallback);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  95 */       this.workers[i] = dispatcher;
/*  96 */       threads[i + 1] = ((dispatchThreadFactory != null) ? dispatchThreadFactory : DISPATCH_THREAD_FACTORY).newThread(new IOReactorWorker(dispatcher));
/*     */     } 
/*  98 */     IOReactor[] ioReactors = new IOReactor[this.workerCount + 1];
/*  99 */     System.arraycopy(this.workers, 0, ioReactors, 1, this.workerCount);
/* 100 */     this.listener = new SingleCoreListeningIOReactor(exceptionCallback, ioReactorConfig, this::enqueueChannel);
/* 101 */     ioReactors[0] = this.listener;
/* 102 */     threads[0] = ((listenerThreadFactory != null) ? listenerThreadFactory : LISTENER_THREAD_FACTORY).newThread(new IOReactorWorker(this.listener));
/*     */     
/* 104 */     this.ioReactor = new MultiCoreIOReactor(ioReactors, threads);
/*     */     
/* 106 */     this.workerSelector = IOWorkers.newSelector(this.workers);
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
/*     */   public DefaultListeningIOReactor(IOEventHandlerFactory eventHandlerFactory, IOReactorConfig config, Callback<IOSession> sessionShutdownCallback) {
/* 122 */     this(eventHandlerFactory, config, null, null, null, null, null, sessionShutdownCallback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultListeningIOReactor(IOEventHandlerFactory eventHandlerFactory) {
/* 133 */     this(eventHandlerFactory, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 138 */     this.ioReactor.start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<ListenerEndpoint> listen(SocketAddress address, Object attachment, FutureCallback<ListenerEndpoint> callback) {
/* 144 */     return this.listener.listen(address, attachment, callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<ListenerEndpoint> listen(SocketAddress address, FutureCallback<ListenerEndpoint> callback) {
/* 149 */     return listen(address, null, callback);
/*     */   }
/*     */   
/*     */   public Future<ListenerEndpoint> listen(SocketAddress address) {
/* 153 */     return listen(address, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ListenerEndpoint> getEndpoints() {
/* 158 */     return this.listener.getEndpoints();
/*     */   }
/*     */ 
/*     */   
/*     */   public void pause() throws IOException {
/* 163 */     this.listener.pause();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resume() throws IOException {
/* 168 */     this.listener.resume();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOReactorStatus getStatus() {
/* 173 */     return this.ioReactor.getStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   IOWorkers.Selector getWorkerSelector() {
/* 178 */     return this.workerSelector;
/*     */   }
/*     */   
/*     */   private void enqueueChannel(ChannelEntry entry) {
/*     */     try {
/* 183 */       this.workerSelector.next().enqueueChannel(entry);
/* 184 */     } catch (IOReactorShutdownException ex) {
/* 185 */       initiateShutdown();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initiateShutdown() {
/* 192 */     this.ioReactor.initiateShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void awaitShutdown(TimeValue waitTime) throws InterruptedException {
/* 197 */     this.ioReactor.awaitShutdown(waitTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 202 */     this.ioReactor.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 207 */     this.ioReactor.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/reactor/DefaultListeningIOReactor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */