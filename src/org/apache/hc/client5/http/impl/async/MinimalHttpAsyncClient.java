/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.config.Configurable;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.impl.ConnPoolSupport;
/*     */ import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
/*     */ import org.apache.hc.client5.http.impl.ExecSupport;
/*     */ import org.apache.hc.client5.http.impl.classic.RequestFailedException;
/*     */ import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
/*     */ import org.apache.hc.client5.http.nio.AsyncConnectionEndpoint;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.routing.RoutingSupport;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.ComplexCancellable;
/*     */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientEndpoint;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.RequestChannel;
/*     */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.Timeout;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public final class MinimalHttpAsyncClient
/*     */   extends AbstractMinimalHttpAsyncClientBase
/*     */ {
/* 101 */   private static final Logger LOG = LoggerFactory.getLogger(MinimalHttpAsyncClient.class);
/*     */ 
/*     */   
/*     */   private final AsyncClientConnectionManager manager;
/*     */ 
/*     */   
/*     */   private final SchemePortResolver schemePortResolver;
/*     */ 
/*     */   
/*     */   private final TlsConfig tlsConfig;
/*     */ 
/*     */ 
/*     */   
/*     */   MinimalHttpAsyncClient(IOEventHandlerFactory eventHandlerFactory, AsyncPushConsumerRegistry pushConsumerRegistry, IOReactorConfig reactorConfig, ThreadFactory threadFactory, ThreadFactory workerThreadFactory, AsyncClientConnectionManager manager, SchemePortResolver schemePortResolver, TlsConfig tlsConfig) {
/* 115 */     super(new DefaultConnectingIOReactor(eventHandlerFactory, reactorConfig, workerThreadFactory, LoggingIOSessionDecorator.INSTANCE, LoggingExceptionCallback.INSTANCE, null, ioSession -> ioSession.enqueue((Command)new ShutdownCommand(CloseMode.GRACEFUL), Command.Priority.NORMAL)), pushConsumerRegistry, threadFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     this.manager = manager;
/* 126 */     this.schemePortResolver = (schemePortResolver != null) ? schemePortResolver : (SchemePortResolver)DefaultSchemePortResolver.INSTANCE;
/* 127 */     this.tlsConfig = tlsConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Future<AsyncConnectionEndpoint> leaseEndpoint(HttpHost host, Timeout connectionRequestTimeout, final Timeout connectTimeout, final HttpClientContext clientContext, final FutureCallback<AsyncConnectionEndpoint> callback) {
/* 136 */     HttpRoute route = new HttpRoute(RoutingSupport.normalize(host, this.schemePortResolver));
/* 137 */     final ComplexFuture<AsyncConnectionEndpoint> resultFuture = new ComplexFuture(callback);
/* 138 */     String exchangeId = ExecSupport.getNextExchangeId();
/* 139 */     clientContext.setExchangeId(exchangeId);
/* 140 */     Future<AsyncConnectionEndpoint> leaseFuture = this.manager.lease(exchangeId, route, null, connectionRequestTimeout, new FutureCallback<AsyncConnectionEndpoint>()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void completed(final AsyncConnectionEndpoint connectionEndpoint)
/*     */           {
/* 149 */             if (connectionEndpoint.isConnected()) {
/* 150 */               resultFuture.completed(connectionEndpoint);
/*     */             } else {
/* 152 */               Future<AsyncConnectionEndpoint> connectFuture = MinimalHttpAsyncClient.this.manager.connect(connectionEndpoint, MinimalHttpAsyncClient.this
/*     */                   
/* 154 */                   .getConnectionInitiator(), connectTimeout, MinimalHttpAsyncClient.this
/*     */                   
/* 156 */                   .tlsConfig, (HttpContext)clientContext, new FutureCallback<AsyncConnectionEndpoint>()
/*     */                   {
/*     */ 
/*     */                     
/*     */                     public void completed(AsyncConnectionEndpoint result)
/*     */                     {
/* 162 */                       resultFuture.completed(result);
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public void failed(Exception ex) {
/*     */                       try {
/* 168 */                         Closer.closeQuietly((Closeable)connectionEndpoint);
/* 169 */                         MinimalHttpAsyncClient.this.manager.release(connectionEndpoint, null, TimeValue.ZERO_MILLISECONDS);
/*     */                       } finally {
/* 171 */                         resultFuture.failed(ex);
/*     */                       } 
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public void cancelled() {
/*     */                       try {
/* 178 */                         Closer.closeQuietly((Closeable)connectionEndpoint);
/* 179 */                         MinimalHttpAsyncClient.this.manager.release(connectionEndpoint, null, TimeValue.ZERO_MILLISECONDS);
/*     */                       } finally {
/* 181 */                         resultFuture.cancel(true);
/*     */                       } 
/*     */                     }
/*     */                   });
/*     */               
/* 186 */               resultFuture.setDependency(connectFuture);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception ex) {
/* 192 */             callback.failed(ex);
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 197 */             callback.cancelled();
/*     */           }
/*     */         });
/*     */     
/* 201 */     resultFuture.setDependency(leaseFuture);
/* 202 */     return (Future<AsyncConnectionEndpoint>)resultFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AsyncClientEndpoint> lease(HttpHost host, FutureCallback<AsyncClientEndpoint> callback) {
/* 208 */     return lease(host, (HttpContext)HttpClientContext.create(), callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AsyncClientEndpoint> lease(HttpHost host, HttpContext context, FutureCallback<AsyncClientEndpoint> callback) {
/* 215 */     Args.notNull(host, "Host");
/* 216 */     Args.notNull(context, "HTTP context");
/* 217 */     final BasicFuture<AsyncClientEndpoint> future = new BasicFuture(callback);
/* 218 */     if (!isRunning()) {
/* 219 */       future.failed(new CancellationException("Connection lease cancelled"));
/* 220 */       return (Future<AsyncClientEndpoint>)future;
/*     */     } 
/* 222 */     HttpClientContext clientContext = HttpClientContext.adapt(context);
/* 223 */     RequestConfig requestConfig = clientContext.getRequestConfig();
/* 224 */     Timeout connectionRequestTimeout = requestConfig.getConnectionRequestTimeout();
/*     */     
/* 226 */     Timeout connectTimeout = requestConfig.getConnectTimeout();
/* 227 */     leaseEndpoint(host, connectionRequestTimeout, connectTimeout, clientContext, new FutureCallback<AsyncConnectionEndpoint>()
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void completed(AsyncConnectionEndpoint result)
/*     */           {
/* 236 */             future.completed(new MinimalHttpAsyncClient.InternalAsyncClientEndpoint(result));
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception ex) {
/* 241 */             future.failed(ex);
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 246 */             future.cancel(true);
/*     */           }
/*     */         });
/*     */     
/* 250 */     return (Future<AsyncClientEndpoint>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable execute(final AsyncClientExchangeHandler exchangeHandler, final HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context) {
/* 258 */     ComplexCancellable cancellable = new ComplexCancellable();
/*     */     try {
/* 260 */       if (!isRunning()) {
/* 261 */         throw new CancellationException("Request execution cancelled");
/*     */       }
/* 263 */       final HttpClientContext clientContext = (context != null) ? HttpClientContext.adapt(context) : HttpClientContext.create();
/* 264 */       exchangeHandler.produceRequest((request, entityDetails, context1) -> {
/*     */             RequestConfig requestConfig = null;
/*     */             
/*     */             if (request instanceof Configurable) {
/*     */               requestConfig = ((Configurable)request).getConfig();
/*     */             }
/*     */             
/*     */             if (requestConfig != null) {
/*     */               clientContext.setRequestConfig(requestConfig);
/*     */             } else {
/*     */               requestConfig = clientContext.getRequestConfig();
/*     */             } 
/*     */             
/*     */             Timeout connectionRequestTimeout = requestConfig.getConnectionRequestTimeout();
/*     */             
/*     */             Timeout connectTimeout = requestConfig.getConnectTimeout();
/*     */             
/*     */             final Timeout responseTimeout = requestConfig.getResponseTimeout();
/*     */             
/*     */             HttpHost target = new HttpHost(request.getScheme(), (NamedEndpoint)request.getAuthority());
/*     */             
/*     */             Future<AsyncConnectionEndpoint> leaseFuture = leaseEndpoint(target, connectionRequestTimeout, connectTimeout, clientContext, new FutureCallback<AsyncConnectionEndpoint>()
/*     */                 {
/*     */                   public void completed(AsyncConnectionEndpoint connectionEndpoint)
/*     */                   {
/* 289 */                     final MinimalHttpAsyncClient.InternalAsyncClientEndpoint endpoint = new MinimalHttpAsyncClient.InternalAsyncClientEndpoint(connectionEndpoint);
/* 290 */                     final AtomicInteger messageCountDown = new AtomicInteger(2);
/* 291 */                     AsyncClientExchangeHandler internalExchangeHandler = new AsyncClientExchangeHandler()
/*     */                       {
/*     */                         public void releaseResources()
/*     */                         {
/*     */                           try {
/* 296 */                             exchangeHandler.releaseResources();
/*     */                           } finally {
/* 298 */                             endpoint.releaseAndDiscard();
/*     */                           } 
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void failed(Exception cause) {
/*     */                           try {
/* 305 */                             exchangeHandler.failed(cause);
/*     */                           } finally {
/* 307 */                             endpoint.releaseAndDiscard();
/*     */                           } 
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void cancel() {
/* 313 */                           failed((Exception)new RequestFailedException("Request aborted"));
/*     */                         }
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         public void produceRequest(RequestChannel channel, HttpContext context1) throws HttpException, IOException {
/* 320 */                           channel.sendRequest(request, entityDetails, context1);
/* 321 */                           if (entityDetails == null) {
/* 322 */                             messageCountDown.decrementAndGet();
/*     */                           }
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public int available() {
/* 328 */                           return exchangeHandler.available();
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void produce(final DataStreamChannel channel) throws IOException {
/* 333 */                           exchangeHandler.produce(new DataStreamChannel()
/*     */                               {
/*     */                                 public void requestOutput()
/*     */                                 {
/* 337 */                                   channel.requestOutput();
/*     */                                 }
/*     */ 
/*     */                                 
/*     */                                 public int write(ByteBuffer src) throws IOException {
/* 342 */                                   return channel.write(src);
/*     */                                 }
/*     */ 
/*     */                                 
/*     */                                 public void endStream(List trailers) throws IOException {
/* 347 */                                   channel.endStream(trailers);
/* 348 */                                   if (messageCountDown.decrementAndGet() <= 0) {
/* 349 */                                     endpoint.releaseAndReuse();
/*     */                                   }
/*     */                                 }
/*     */ 
/*     */                                 
/*     */                                 public void endStream() throws IOException {
/* 355 */                                   channel.endStream();
/* 356 */                                   if (messageCountDown.decrementAndGet() <= 0) {
/* 357 */                                     endpoint.releaseAndReuse();
/*     */                                   }
/*     */                                 }
/*     */                               });
/*     */                         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         public void consumeInformation(HttpResponse response, HttpContext context1) throws HttpException, IOException {
/* 368 */                           exchangeHandler.consumeInformation(response, context1);
/*     */                         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext context1) throws HttpException, IOException {
/* 376 */                           exchangeHandler.consumeResponse(response, entityDetails, context1);
/* 377 */                           if (response.getCode() >= 400) {
/* 378 */                             messageCountDown.decrementAndGet();
/*     */                           }
/* 380 */                           if (entityDetails == null && 
/* 381 */                             messageCountDown.decrementAndGet() <= 0) {
/* 382 */                             endpoint.releaseAndReuse();
/*     */                           }
/*     */                         }
/*     */ 
/*     */ 
/*     */                         
/*     */                         public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 389 */                           exchangeHandler.updateCapacity(capacityChannel);
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void consume(ByteBuffer src) throws IOException {
/* 394 */                           exchangeHandler.consume(src);
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void streamEnd(List trailers) throws HttpException, IOException {
/* 399 */                           if (messageCountDown.decrementAndGet() <= 0) {
/* 400 */                             endpoint.releaseAndReuse();
/*     */                           }
/* 402 */                           exchangeHandler.streamEnd(trailers);
/*     */                         }
/*     */                       };
/*     */                     
/* 406 */                     if (responseTimeout != null) {
/* 407 */                       endpoint.setSocketTimeout(responseTimeout);
/*     */                     }
/* 409 */                     endpoint.execute(internalExchangeHandler, pushHandlerFactory, (HttpContext)clientContext);
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void failed(Exception ex) {
/* 414 */                     exchangeHandler.failed(ex);
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void cancelled() {
/* 419 */                     exchangeHandler.cancel();
/*     */                   }
/*     */                 });
/*     */ 
/*     */ 
/*     */             
/*     */             cancellable.setDependency(());
/*     */           }context);
/* 427 */     } catch (HttpException|IOException|IllegalStateException ex) {
/* 428 */       exchangeHandler.failed(ex);
/*     */     } 
/* 430 */     return (Cancellable)cancellable;
/*     */   }
/*     */   
/*     */   private class InternalAsyncClientEndpoint
/*     */     extends AsyncClientEndpoint {
/*     */     private final AsyncConnectionEndpoint connectionEndpoint;
/*     */     private final AtomicBoolean released;
/*     */     
/*     */     InternalAsyncClientEndpoint(AsyncConnectionEndpoint connectionEndpoint) {
/* 439 */       this.connectionEndpoint = connectionEndpoint;
/* 440 */       this.released = new AtomicBoolean(false);
/*     */     }
/*     */     
/*     */     boolean isReleased() {
/* 444 */       return this.released.get();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isConnected() {
/* 449 */       return (!isReleased() && this.connectionEndpoint.isConnected());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void execute(AsyncClientExchangeHandler exchangeHandler, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context) {
/* 457 */       Asserts.check(!this.released.get(), "Endpoint has already been released");
/*     */       
/* 459 */       HttpClientContext clientContext = (context != null) ? HttpClientContext.adapt(context) : HttpClientContext.create();
/* 460 */       String exchangeId = ExecSupport.getNextExchangeId();
/* 461 */       clientContext.setExchangeId(exchangeId);
/* 462 */       if (MinimalHttpAsyncClient.LOG.isDebugEnabled()) {
/* 463 */         MinimalHttpAsyncClient.LOG.debug("{} executing message exchange {}", exchangeId, ConnPoolSupport.getId(this.connectionEndpoint));
/* 464 */         this.connectionEndpoint.execute(exchangeId, new LoggingAsyncClientExchangeHandler(MinimalHttpAsyncClient
/*     */               
/* 466 */               .LOG, exchangeId, exchangeHandler), pushHandlerFactory, (HttpContext)clientContext);
/*     */       }
/*     */       else {
/*     */         
/* 470 */         this.connectionEndpoint.execute(exchangeId, exchangeHandler, (HttpContext)clientContext);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setSocketTimeout(Timeout timeout) {
/* 475 */       this.connectionEndpoint.setSocketTimeout(timeout);
/*     */     }
/*     */ 
/*     */     
/*     */     public void releaseAndReuse() {
/* 480 */       if (this.released.compareAndSet(false, true)) {
/* 481 */         MinimalHttpAsyncClient.this.manager.release(this.connectionEndpoint, null, TimeValue.NEG_ONE_MILLISECOND);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void releaseAndDiscard() {
/* 487 */       if (this.released.compareAndSet(false, true)) {
/* 488 */         Closer.closeQuietly((Closeable)this.connectionEndpoint);
/* 489 */         MinimalHttpAsyncClient.this.manager.release(this.connectionEndpoint, null, TimeValue.ZERO_MILLISECONDS);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/MinimalHttpAsyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */