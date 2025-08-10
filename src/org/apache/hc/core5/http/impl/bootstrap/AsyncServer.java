/*     */ package org.apache.hc.core5.http.impl.bootstrap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.ConnectionAcceptor;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.DefaultListeningIOReactor;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOReactorService;
/*     */ import org.apache.hc.core5.reactor.IOReactorStatus;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.IOSessionListener;
/*     */ import org.apache.hc.core5.reactor.ListenerEndpoint;
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
/*     */ public class AsyncServer
/*     */   extends AbstractConnectionInitiatorBase
/*     */   implements IOReactorService, ConnectionAcceptor
/*     */ {
/*     */   private final DefaultListeningIOReactor ioReactor;
/*     */   
/*     */   @Internal
/*     */   public AsyncServer(IOEventHandlerFactory eventHandlerFactory, IOReactorConfig ioReactorConfig, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, Callback<IOSession> sessionShutdownCallback) {
/*  68 */     this.ioReactor = new DefaultListeningIOReactor(eventHandlerFactory, ioReactorConfig, (ThreadFactory)new DefaultThreadFactory("server-dispatch", true), (ThreadFactory)new DefaultThreadFactory("server-listener", true), ioSessionDecorator, exceptionCallback, sessionListener, sessionShutdownCallback);
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
/*     */   public void start() {
/*  81 */     this.ioReactor.start();
/*     */   }
/*     */ 
/*     */   
/*     */   ConnectionInitiator getIOReactor() {
/*  86 */     return (ConnectionInitiator)this.ioReactor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<ListenerEndpoint> listen(SocketAddress address, Object attachment, FutureCallback<ListenerEndpoint> callback) {
/*  95 */     return this.ioReactor.listen(address, attachment, callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<ListenerEndpoint> listen(SocketAddress address, FutureCallback<ListenerEndpoint> callback) {
/* 100 */     return listen(address, null, callback);
/*     */   }
/*     */   
/*     */   public Future<ListenerEndpoint> listen(SocketAddress address) {
/* 104 */     return this.ioReactor.listen(address, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pause() throws IOException {
/* 109 */     this.ioReactor.pause();
/*     */   }
/*     */ 
/*     */   
/*     */   public void resume() throws IOException {
/* 114 */     this.ioReactor.resume();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ListenerEndpoint> getEndpoints() {
/* 119 */     return this.ioReactor.getEndpoints();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOReactorStatus getStatus() {
/* 124 */     return this.ioReactor.getStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initiateShutdown() {
/* 129 */     this.ioReactor.initiateShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void awaitShutdown(TimeValue waitTime) throws InterruptedException {
/* 134 */     this.ioReactor.awaitShutdown(waitTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 139 */     this.ioReactor.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 144 */     this.ioReactor.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/AsyncServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */