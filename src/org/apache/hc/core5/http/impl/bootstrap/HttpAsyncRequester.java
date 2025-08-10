/*     */ package org.apache.hc.core5.http.impl.bootstrap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.concurrent.FutureContribution;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpConnection;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.impl.DefaultAddressResolver;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.RequestChannel;
/*     */ import org.apache.hc.core5.http.nio.command.RequestExecutionCommand;
/*     */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsUpgradeCapable;
/*     */ import org.apache.hc.core5.http.nio.support.BasicClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.pool.ConnPoolControl;
/*     */ import org.apache.hc.core5.pool.ManagedConnPool;
/*     */ import org.apache.hc.core5.pool.PoolEntry;
/*     */ import org.apache.hc.core5.pool.PoolStats;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.EndpointParameters;
/*     */ import org.apache.hc.core5.reactor.IOEventHandler;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.reactor.IOSessionListener;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TransportSecurityLayer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpAsyncRequester
/*     */   extends AsyncRequester
/*     */   implements ConnPoolControl<HttpHost>
/*     */ {
/*     */   private final ManagedConnPool<HttpHost, IOSession> connPool;
/*     */   private final TlsStrategy tlsStrategy;
/*     */   private final Timeout handshakeTimeout;
/*     */   
/*     */   @Internal
/*     */   public HttpAsyncRequester(IOReactorConfig ioReactorConfig, IOEventHandlerFactory eventHandlerFactory, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, ManagedConnPool<HttpHost, IOSession> connPool, TlsStrategy tlsStrategy, Timeout handshakeTimeout) {
/* 116 */     super(eventHandlerFactory, ioReactorConfig, ioSessionDecorator, exceptionCallback, sessionListener, ShutdownCommand.GRACEFUL_IMMEDIATE_CALLBACK, (Resolver<HttpHost, InetSocketAddress>)DefaultAddressResolver.INSTANCE);
/*     */     
/* 118 */     this.connPool = (ManagedConnPool<HttpHost, IOSession>)Args.notNull(connPool, "Connection pool");
/* 119 */     this.tlsStrategy = tlsStrategy;
/* 120 */     this.handshakeTimeout = handshakeTimeout;
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
/*     */   @Internal
/*     */   public HttpAsyncRequester(IOReactorConfig ioReactorConfig, IOEventHandlerFactory eventHandlerFactory, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, ManagedConnPool<HttpHost, IOSession> connPool) {
/* 134 */     this(ioReactorConfig, eventHandlerFactory, ioSessionDecorator, exceptionCallback, sessionListener, connPool, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 140 */     return this.connPool.getTotalStats();
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(HttpHost route) {
/* 145 */     return this.connPool.getStats(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 150 */     this.connPool.setMaxTotal(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 155 */     return this.connPool.getMaxTotal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 160 */     this.connPool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 165 */     return this.connPool.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(HttpHost route, int max) {
/* 170 */     this.connPool.setMaxPerRoute(route, max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(HttpHost route) {
/* 175 */     return this.connPool.getMaxPerRoute(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdle(TimeValue idleTime) {
/* 180 */     this.connPool.closeIdle(idleTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 185 */     this.connPool.closeExpired();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<HttpHost> getRoutes() {
/* 190 */     return this.connPool.getRoutes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AsyncClientEndpoint> connect(HttpHost host, Timeout timeout, Object attachment, FutureCallback<AsyncClientEndpoint> callback) {
/* 198 */     return doConnect(host, timeout, attachment, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Future<AsyncClientEndpoint> doConnect(final HttpHost host, final Timeout timeout, final Object attachment, FutureCallback<AsyncClientEndpoint> callback) {
/* 206 */     Args.notNull(host, "Host");
/* 207 */     Args.notNull(timeout, "Timeout");
/* 208 */     final ComplexFuture<AsyncClientEndpoint> resultFuture = new ComplexFuture(callback);
/* 209 */     Future<PoolEntry<HttpHost, IOSession>> leaseFuture = this.connPool.lease(host, null, timeout, new FutureCallback<PoolEntry<HttpHost, IOSession>>()
/*     */         {
/*     */           
/*     */           public void completed(final PoolEntry<HttpHost, IOSession> poolEntry)
/*     */           {
/* 214 */             final AsyncClientEndpoint endpoint = new HttpAsyncRequester.InternalAsyncClientEndpoint(poolEntry);
/* 215 */             IOSession ioSession = (IOSession)poolEntry.getConnection();
/* 216 */             if (ioSession != null && !ioSession.isOpen()) {
/* 217 */               poolEntry.discardConnection(CloseMode.IMMEDIATE);
/*     */             }
/* 219 */             if (poolEntry.hasConnection()) {
/* 220 */               resultFuture.completed(endpoint);
/*     */             } else {
/* 222 */               Future<IOSession> future = HttpAsyncRequester.this.requestSession(host, timeout, new EndpointParameters(host, attachment), new FutureCallback<IOSession>()
/*     */                   {
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/*     */                     public void completed(IOSession session)
/*     */                     {
/* 230 */                       session.setSocketTimeout(timeout);
/* 231 */                       poolEntry.assignConnection((ModalCloseable)session);
/* 232 */                       resultFuture.completed(endpoint);
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public void failed(Exception cause) {
/*     */                       try {
/* 238 */                         resultFuture.failed(cause);
/*     */                       } finally {
/* 240 */                         endpoint.releaseAndDiscard();
/*     */                       } 
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public void cancelled() {
/*     */                       try {
/* 247 */                         resultFuture.cancel();
/*     */                       } finally {
/* 249 */                         endpoint.releaseAndDiscard();
/*     */                       } 
/*     */                     }
/*     */                   });
/*     */               
/* 254 */               resultFuture.setDependency(future);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception ex) {
/* 260 */             resultFuture.failed(ex);
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 265 */             resultFuture.cancel();
/*     */           }
/*     */         });
/*     */     
/* 269 */     resultFuture.setDependency(leaseFuture);
/* 270 */     return (Future<AsyncClientEndpoint>)resultFuture;
/*     */   }
/*     */   
/*     */   public Future<AsyncClientEndpoint> connect(HttpHost host, Timeout timeout) {
/* 274 */     return connect(host, timeout, (Object)null, (FutureCallback<AsyncClientEndpoint>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(final AsyncClientExchangeHandler exchangeHandler, final HandlerFactory<AsyncPushConsumer> pushHandlerFactory, Timeout timeout, final HttpContext executeContext) {
/* 282 */     Args.notNull(exchangeHandler, "Exchange handler");
/* 283 */     Args.notNull(timeout, "Timeout");
/* 284 */     Args.notNull(executeContext, "Context");
/*     */     try {
/* 286 */       exchangeHandler.produceRequest((request, entityDetails, requestContext) -> {
/*     */             String scheme = request.getScheme();
/*     */             URIAuthority authority = request.getAuthority();
/*     */             if (authority == null) {
/*     */               throw new ProtocolException("Request authority not specified");
/*     */             }
/*     */             HttpHost target = new HttpHost(scheme, (NamedEndpoint)authority);
/*     */             connect(target, timeout, null, new FutureCallback<AsyncClientEndpoint>()
/*     */                 {
/*     */                   public void completed(final AsyncClientEndpoint endpoint)
/*     */                   {
/* 297 */                     endpoint.execute(new AsyncClientExchangeHandler()
/*     */                         {
/*     */                           public void releaseResources()
/*     */                           {
/* 301 */                             endpoint.releaseAndDiscard();
/* 302 */                             exchangeHandler.releaseResources();
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void failed(Exception cause) {
/* 307 */                             endpoint.releaseAndDiscard();
/* 308 */                             exchangeHandler.failed(cause);
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void cancel() {
/* 313 */                             endpoint.releaseAndDiscard();
/* 314 */                             exchangeHandler.cancel();
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void produceRequest(RequestChannel channel, HttpContext httpContext) throws HttpException, IOException {
/* 319 */                             channel.sendRequest(request, entityDetails, httpContext);
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public int available() {
/* 324 */                             return exchangeHandler.available();
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void produce(DataStreamChannel channel) throws IOException {
/* 329 */                             exchangeHandler.produce(channel);
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void consumeInformation(HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
/* 334 */                             exchangeHandler.consumeInformation(response, httpContext);
/*     */                           }
/*     */ 
/*     */ 
/*     */                           
/*     */                           public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
/* 340 */                             if (entityDetails == null) {
/* 341 */                               endpoint.releaseAndReuse();
/*     */                             }
/* 343 */                             exchangeHandler.consumeResponse(response, entityDetails, httpContext);
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 348 */                             exchangeHandler.updateCapacity(capacityChannel);
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void consume(ByteBuffer src) throws IOException {
/* 353 */                             exchangeHandler.consume(src);
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void streamEnd(List trailers) throws HttpException, IOException {
/* 358 */                             endpoint.releaseAndReuse();
/* 359 */                             exchangeHandler.streamEnd(trailers);
/*     */                           }
/*     */                         }pushHandlerFactory, executeContext);
/*     */                   }
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   public void failed(Exception ex) {
/* 368 */                     exchangeHandler.failed(ex);
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void cancelled() {
/* 373 */                     exchangeHandler.cancel();
/*     */                   }
/*     */                 });
/*     */ 
/*     */           
/*     */           }executeContext);
/*     */     }
/* 380 */     catch (IOException|HttpException ex) {
/* 381 */       exchangeHandler.failed(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(AsyncClientExchangeHandler exchangeHandler, Timeout timeout, HttpContext executeContext) {
/* 389 */     execute(exchangeHandler, (HandlerFactory<AsyncPushConsumer>)null, timeout, executeContext);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, Timeout timeout, HttpContext context, FutureCallback<T> callback) {
/* 399 */     Args.notNull(requestProducer, "Request producer");
/* 400 */     Args.notNull(responseConsumer, "Response consumer");
/* 401 */     Args.notNull(timeout, "Timeout");
/* 402 */     final BasicFuture<T> future = new BasicFuture(callback);
/* 403 */     BasicClientExchangeHandler basicClientExchangeHandler = new BasicClientExchangeHandler(requestProducer, responseConsumer, (FutureCallback)new FutureContribution<T>(future)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public void completed(T result)
/*     */           {
/* 410 */             future.completed(result);
/*     */           }
/*     */         });
/*     */     
/* 414 */     execute((AsyncClientExchangeHandler)basicClientExchangeHandler, pushHandlerFactory, timeout, (context != null) ? context : (HttpContext)HttpCoreContext.create());
/* 415 */     return (Future<T>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, Timeout timeout, HttpContext context, FutureCallback<T> callback) {
/* 424 */     return execute(requestProducer, responseConsumer, (HandlerFactory<AsyncPushConsumer>)null, timeout, context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, Timeout timeout, FutureCallback<T> callback) {
/* 432 */     return execute(requestProducer, responseConsumer, (HandlerFactory<AsyncPushConsumer>)null, timeout, (HttpContext)null, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doTlsUpgrade(final ProtocolIOSession ioSession, NamedEndpoint endpoint, final FutureCallback<ProtocolIOSession> callback) {
/* 439 */     if (this.tlsStrategy != null) {
/* 440 */       this.tlsStrategy.upgrade((TransportSecurityLayer)ioSession, endpoint, null, this.handshakeTimeout, (FutureCallback)new CallbackContribution<TransportSecurityLayer>(callback)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void completed(TransportSecurityLayer transportSecurityLayer)
/*     */             {
/* 448 */               if (callback != null) {
/* 449 */                 callback.completed(ioSession);
/*     */               }
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 455 */       throw new IllegalStateException("TLS upgrade not supported");
/*     */     } 
/*     */   }
/*     */   
/*     */   private class InternalAsyncClientEndpoint
/*     */     extends AsyncClientEndpoint implements TlsUpgradeCapable {
/*     */     final AtomicReference<PoolEntry<HttpHost, IOSession>> poolEntryRef;
/*     */     
/*     */     InternalAsyncClientEndpoint(PoolEntry<HttpHost, IOSession> poolEntry) {
/* 464 */       this.poolEntryRef = new AtomicReference<>(poolEntry);
/*     */     }
/*     */     
/*     */     private IOSession getIOSession() {
/* 468 */       PoolEntry<HttpHost, IOSession> poolEntry = this.poolEntryRef.get();
/* 469 */       if (poolEntry == null) {
/* 470 */         throw new IllegalStateException("Endpoint has already been released");
/*     */       }
/* 472 */       IOSession ioSession = (IOSession)poolEntry.getConnection();
/* 473 */       if (ioSession == null) {
/* 474 */         throw new IllegalStateException("I/O session is invalid");
/*     */       }
/* 476 */       return ioSession;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void execute(AsyncClientExchangeHandler exchangeHandler, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context) {
/* 484 */       IOSession ioSession = getIOSession();
/* 485 */       ioSession.enqueue((Command)new RequestExecutionCommand(exchangeHandler, pushHandlerFactory, null, context), Command.Priority.NORMAL);
/* 486 */       if (!ioSession.isOpen()) {
/*     */         try {
/* 488 */           exchangeHandler.failed((Exception)new ConnectionClosedException());
/*     */         } finally {
/* 490 */           exchangeHandler.releaseResources();
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isConnected() {
/* 497 */       PoolEntry<HttpHost, IOSession> poolEntry = this.poolEntryRef.get();
/* 498 */       if (poolEntry != null) {
/* 499 */         IOSession ioSession = (IOSession)poolEntry.getConnection();
/* 500 */         if (ioSession == null || !ioSession.isOpen()) {
/* 501 */           return false;
/*     */         }
/* 503 */         IOEventHandler handler = ioSession.getHandler();
/* 504 */         return (handler instanceof HttpConnection && ((HttpConnection)handler).isOpen());
/*     */       } 
/* 506 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void releaseAndReuse() {
/* 511 */       PoolEntry<HttpHost, IOSession> poolEntry = this.poolEntryRef.getAndSet(null);
/* 512 */       if (poolEntry != null) {
/* 513 */         IOSession ioSession = (IOSession)poolEntry.getConnection();
/* 514 */         HttpAsyncRequester.this.connPool.release(poolEntry, (ioSession != null && ioSession.isOpen()));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void releaseAndDiscard() {
/* 520 */       PoolEntry<HttpHost, IOSession> poolEntry = this.poolEntryRef.getAndSet(null);
/* 521 */       if (poolEntry != null) {
/* 522 */         poolEntry.discardConnection(CloseMode.GRACEFUL);
/* 523 */         HttpAsyncRequester.this.connPool.release(poolEntry, false);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void tlsUpgrade(NamedEndpoint endpoint, FutureCallback<ProtocolIOSession> callback) {
/* 529 */       IOSession ioSession = getIOSession();
/* 530 */       if (ioSession instanceof ProtocolIOSession) {
/* 531 */         HttpAsyncRequester.this.doTlsUpgrade((ProtocolIOSession)ioSession, endpoint, callback);
/*     */       } else {
/* 533 */         throw new IllegalStateException("TLS upgrade not supported");
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/bootstrap/HttpAsyncRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */