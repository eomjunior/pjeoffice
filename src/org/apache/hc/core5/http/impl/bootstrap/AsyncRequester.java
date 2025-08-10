/*     */ package org.apache.hc.core5.http.impl.bootstrap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.impl.DefaultAddressResolver;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOReactorService;
/*     */ import org.apache.hc.core5.reactor.IOReactorStatus;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.IOSessionListener;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncRequester
/*     */   extends AbstractConnectionInitiatorBase
/*     */   implements IOReactorService
/*     */ {
/*     */   private final DefaultConnectingIOReactor ioReactor;
/*     */   private final Resolver<HttpHost, InetSocketAddress> addressResolver;
/*     */   
/*     */   @Internal
/*     */   public AsyncRequester(IOEventHandlerFactory eventHandlerFactory, IOReactorConfig ioReactorConfig, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, Callback<IOSession> sessionShutdownCallback, Resolver<HttpHost, InetSocketAddress> addressResolver) {
/*  74 */     this.ioReactor = new DefaultConnectingIOReactor(eventHandlerFactory, ioReactorConfig, (ThreadFactory)new DefaultThreadFactory("requester-dispatch", true), ioSessionDecorator, exceptionCallback, sessionListener, sessionShutdownCallback);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  82 */     this.addressResolver = (addressResolver != null) ? addressResolver : (Resolver<HttpHost, InetSocketAddress>)DefaultAddressResolver.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   ConnectionInitiator getIOReactor() {
/*  87 */     return (ConnectionInitiator)this.ioReactor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<IOSession> requestSession(HttpHost host, Timeout timeout, Object attachment, FutureCallback<IOSession> callback) {
/*  95 */     Args.notNull(host, "Host");
/*  96 */     Args.notNull(timeout, "Timeout");
/*  97 */     return connect((NamedEndpoint)host, (SocketAddress)this.addressResolver.resolve(host), null, timeout, attachment, callback);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 102 */     this.ioReactor.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public IOReactorStatus getStatus() {
/* 107 */     return this.ioReactor.getStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initiateShutdown() {
/* 112 */     this.ioReactor.initiateShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void awaitShutdown(TimeValue waitTime) throws InterruptedException {
/* 117 */     this.ioReactor.awaitShutdown(waitTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 122 */     this.ioReactor.close(closeMode);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 127 */     this.ioReactor.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/AsyncRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */