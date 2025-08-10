/*     */ package org.apache.hc.core5.http2.impl.nio.bootstrap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Future;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.concurrent.FutureContribution;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.function.Decorator;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.impl.DefaultAddressResolver;
/*     */ import org.apache.hc.core5.http.impl.bootstrap.AsyncRequester;
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
/*     */ import org.apache.hc.core5.http.nio.support.BasicClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.http2.nio.pool.H2ConnPool;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.net.URIAuthority;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class H2MultiplexingRequester
/*     */   extends AsyncRequester
/*     */ {
/*     */   private final H2ConnPool connPool;
/*     */   
/*     */   @Internal
/*     */   public H2MultiplexingRequester(IOReactorConfig ioReactorConfig, IOEventHandlerFactory eventHandlerFactory, Decorator<IOSession> ioSessionDecorator, Callback<Exception> exceptionCallback, IOSessionListener sessionListener, Resolver<HttpHost, InetSocketAddress> addressResolver, TlsStrategy tlsStrategy) {
/* 101 */     super(eventHandlerFactory, ioReactorConfig, ioSessionDecorator, exceptionCallback, sessionListener, ShutdownCommand.GRACEFUL_IMMEDIATE_CALLBACK, (Resolver)DefaultAddressResolver.INSTANCE);
/*     */     
/* 103 */     this.connPool = new H2ConnPool((ConnectionInitiator)this, addressResolver, tlsStrategy);
/*     */   }
/*     */   
/*     */   public void closeIdle(TimeValue idleTime) {
/* 107 */     this.connPool.closeIdle(idleTime);
/*     */   }
/*     */   
/*     */   public Set<HttpHost> getRoutes() {
/* 111 */     return this.connPool.getRoutes();
/*     */   }
/*     */   
/*     */   public TimeValue getValidateAfterInactivity() {
/* 115 */     return this.connPool.getValidateAfterInactivity();
/*     */   }
/*     */   
/*     */   public void setValidateAfterInactivity(TimeValue timeValue) {
/* 119 */     this.connPool.setValidateAfterInactivity(timeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable execute(AsyncClientExchangeHandler exchangeHandler, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, Timeout timeout, HttpContext context) {
/* 127 */     Args.notNull(exchangeHandler, "Exchange handler");
/* 128 */     Args.notNull(timeout, "Timeout");
/* 129 */     Args.notNull(context, "Context");
/* 130 */     CancellableExecution cancellableExecution = new CancellableExecution();
/* 131 */     execute(exchangeHandler, pushHandlerFactory, cancellableExecution, timeout, context);
/* 132 */     return (Cancellable)cancellableExecution;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable execute(AsyncClientExchangeHandler exchangeHandler, Timeout timeout, HttpContext context) {
/* 139 */     return execute(exchangeHandler, (HandlerFactory<AsyncPushConsumer>)null, timeout, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void execute(final AsyncClientExchangeHandler exchangeHandler, final HandlerFactory<AsyncPushConsumer> pushHandlerFactory, final CancellableDependency cancellableDependency, Timeout timeout, final HttpContext context) {
/* 148 */     Args.notNull(exchangeHandler, "Exchange handler");
/* 149 */     Args.notNull(timeout, "Timeout");
/* 150 */     Args.notNull(context, "Context");
/*     */     try {
/* 152 */       exchangeHandler.produceRequest((request, entityDetails, httpContext) -> {
/*     */             String scheme = request.getScheme();
/*     */             URIAuthority authority = request.getAuthority();
/*     */             if (authority == null) {
/*     */               throw new ProtocolException("Request authority not specified");
/*     */             }
/*     */             HttpHost target = new HttpHost(scheme, (NamedEndpoint)authority);
/*     */             this.connPool.getSession(target, timeout, new FutureCallback<IOSession>()
/*     */                 {
/*     */                   public void completed(IOSession ioSession)
/*     */                   {
/* 163 */                     ioSession.enqueue((Command)new RequestExecutionCommand(new AsyncClientExchangeHandler()
/*     */                           {
/*     */                             public void releaseResources()
/*     */                             {
/* 167 */                               exchangeHandler.releaseResources();
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public void produceRequest(RequestChannel channel, HttpContext httpContext) throws HttpException, IOException {
/* 172 */                               channel.sendRequest(request, entityDetails, httpContext);
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public int available() {
/* 177 */                               return exchangeHandler.available();
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public void produce(DataStreamChannel channel) throws IOException {
/* 182 */                               exchangeHandler.produce(channel);
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public void consumeInformation(HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
/* 187 */                               exchangeHandler.consumeInformation(response, httpContext);
/*     */                             }
/*     */ 
/*     */ 
/*     */                             
/*     */                             public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
/* 193 */                               exchangeHandler.consumeResponse(response, entityDetails, httpContext);
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 198 */                               exchangeHandler.updateCapacity(capacityChannel);
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public void consume(ByteBuffer src) throws IOException {
/* 203 */                               exchangeHandler.consume(src);
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public void streamEnd(List trailers) throws HttpException, IOException {
/* 208 */                               exchangeHandler.streamEnd(trailers);
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public void cancel() {
/* 213 */                               exchangeHandler.cancel();
/*     */                             }
/*     */ 
/*     */                             
/*     */                             public void failed(Exception cause) {
/* 218 */                               exchangeHandler.failed(cause);
/*     */                             }
/*     */                           }pushHandlerFactory, cancellableDependency, context), Command.Priority.NORMAL);
/*     */                     
/* 222 */                     if (!ioSession.isOpen()) {
/* 223 */                       exchangeHandler.failed((Exception)new ConnectionClosedException());
/*     */                     }
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void failed(Exception ex) {
/* 229 */                     exchangeHandler.failed(ex);
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void cancelled() {
/* 234 */                     exchangeHandler.cancel();
/*     */                   }
/*     */                 });
/*     */ 
/*     */           
/*     */           }context);
/* 240 */     } catch (IOException|HttpException ex) {
/* 241 */       exchangeHandler.failed(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, Timeout timeout, HttpContext context, FutureCallback<T> callback) {
/* 252 */     Args.notNull(requestProducer, "Request producer");
/* 253 */     Args.notNull(responseConsumer, "Response consumer");
/* 254 */     Args.notNull(timeout, "Timeout");
/* 255 */     final ComplexFuture<T> future = new ComplexFuture(callback);
/* 256 */     BasicClientExchangeHandler basicClientExchangeHandler = new BasicClientExchangeHandler(requestProducer, responseConsumer, (FutureCallback)new FutureContribution<T>((BasicFuture)future)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public void completed(T result)
/*     */           {
/* 263 */             future.completed(result);
/*     */           }
/*     */         });
/*     */     
/* 267 */     execute((AsyncClientExchangeHandler)basicClientExchangeHandler, pushHandlerFactory, (CancellableDependency)future, timeout, (context != null) ? context : (HttpContext)HttpCoreContext.create());
/* 268 */     return (Future<T>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, Timeout timeout, HttpContext context, FutureCallback<T> callback) {
/* 277 */     return execute(requestProducer, responseConsumer, null, timeout, context, callback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T> Future<T> execute(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, Timeout timeout, FutureCallback<T> callback) {
/* 285 */     return execute(requestProducer, responseConsumer, null, timeout, null, callback);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/bootstrap/H2MultiplexingRequester.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */