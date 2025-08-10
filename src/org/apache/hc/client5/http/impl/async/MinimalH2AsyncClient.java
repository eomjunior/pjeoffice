/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.config.Configurable;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.ConnPoolSupport;
/*     */ import org.apache.hc.client5.http.impl.ExecSupport;
/*     */ import org.apache.hc.client5.http.impl.classic.RequestFailedException;
/*     */ import org.apache.hc.client5.http.impl.nio.MultihomeConnectionInitiator;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.concurrent.ComplexCancellable;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.RequestChannel;
/*     */ import org.apache.hc.core5.http.nio.command.RequestExecutionCommand;
/*     */ import org.apache.hc.core5.http.nio.command.ShutdownCommand;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.net.NamedEndpoint;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
/*     */ import org.apache.hc.core5.reactor.IOEventHandlerFactory;
/*     */ import org.apache.hc.core5.reactor.IOReactorConfig;
/*     */ import org.apache.hc.core5.reactor.IOSession;
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
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public final class MinimalH2AsyncClient
/*     */   extends AbstractMinimalHttpAsyncClientBase
/*     */ {
/*  93 */   private static final Logger LOG = LoggerFactory.getLogger(MinimalH2AsyncClient.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private final InternalH2ConnPool connPool;
/*     */ 
/*     */   
/*     */   private final ConnectionInitiator connectionInitiator;
/*     */ 
/*     */ 
/*     */   
/*     */   MinimalH2AsyncClient(IOEventHandlerFactory eventHandlerFactory, AsyncPushConsumerRegistry pushConsumerRegistry, IOReactorConfig reactorConfig, ThreadFactory threadFactory, ThreadFactory workerThreadFactory, DnsResolver dnsResolver, TlsStrategy tlsStrategy) {
/* 105 */     super(new DefaultConnectingIOReactor(eventHandlerFactory, reactorConfig, workerThreadFactory, LoggingIOSessionDecorator.INSTANCE, LoggingExceptionCallback.INSTANCE, null, ioSession -> ioSession.enqueue((Command)new ShutdownCommand(CloseMode.GRACEFUL), Command.Priority.IMMEDIATE)), pushConsumerRegistry, threadFactory);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     this.connectionInitiator = (ConnectionInitiator)new MultihomeConnectionInitiator(getConnectionInitiator(), dnsResolver);
/* 116 */     this.connPool = new InternalH2ConnPool(this.connectionInitiator, object -> null, tlsStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable execute(final AsyncClientExchangeHandler exchangeHandler, final HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context) {
/* 124 */     final ComplexCancellable cancellable = new ComplexCancellable();
/*     */     try {
/* 126 */       if (!isRunning()) {
/* 127 */         throw new CancellationException("Request execution cancelled");
/*     */       }
/* 129 */       final HttpClientContext clientContext = (context != null) ? HttpClientContext.adapt(context) : HttpClientContext.create();
/* 130 */       exchangeHandler.produceRequest((request, entityDetails, context1) -> {
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
/*     */             Timeout connectTimeout = requestConfig.getConnectTimeout();
/*     */             HttpHost target = new HttpHost(request.getScheme(), (NamedEndpoint)request.getAuthority());
/*     */             Future<IOSession> sessionFuture = this.connPool.getSession(target, connectTimeout, new FutureCallback<IOSession>()
/*     */                 {
/*     */                   public void completed(IOSession session)
/*     */                   {
/* 149 */                     AsyncClientExchangeHandler internalExchangeHandler = new AsyncClientExchangeHandler()
/*     */                       {
/*     */                         public void releaseResources()
/*     */                         {
/* 153 */                           exchangeHandler.releaseResources();
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void failed(Exception cause) {
/* 158 */                           exchangeHandler.failed(cause);
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void cancel() {
/* 163 */                           failed((Exception)new RequestFailedException("Request aborted"));
/*     */                         }
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         public void produceRequest(RequestChannel channel, HttpContext context1) throws HttpException, IOException {
/* 170 */                           channel.sendRequest(request, entityDetails, context1);
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public int available() {
/* 175 */                           return exchangeHandler.available();
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void produce(DataStreamChannel channel) throws IOException {
/* 180 */                           exchangeHandler.produce(channel);
/*     */                         }
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         public void consumeInformation(HttpResponse response, HttpContext context1) throws HttpException, IOException {
/* 187 */                           exchangeHandler.consumeInformation(response, context1);
/*     */                         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext context1) throws HttpException, IOException {
/* 195 */                           exchangeHandler.consumeResponse(response, entityDetails, context1);
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 200 */                           exchangeHandler.updateCapacity(capacityChannel);
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void consume(ByteBuffer src) throws IOException {
/* 205 */                           exchangeHandler.consume(src);
/*     */                         }
/*     */ 
/*     */                         
/*     */                         public void streamEnd(List trailers) throws HttpException, IOException {
/* 210 */                           exchangeHandler.streamEnd(trailers);
/*     */                         }
/*     */                       };
/*     */                     
/* 214 */                     if (MinimalH2AsyncClient.LOG.isDebugEnabled()) {
/* 215 */                       String exchangeId = ExecSupport.getNextExchangeId();
/* 216 */                       clientContext.setExchangeId(exchangeId);
/* 217 */                       if (MinimalH2AsyncClient.LOG.isDebugEnabled()) {
/* 218 */                         MinimalH2AsyncClient.LOG.debug("{} executing message exchange {}", exchangeId, ConnPoolSupport.getId(session));
/*     */                       }
/* 220 */                       session.enqueue((Command)new RequestExecutionCommand(new LoggingAsyncClientExchangeHandler(MinimalH2AsyncClient
/*     */                               
/* 222 */                               .LOG, exchangeId, internalExchangeHandler), pushHandlerFactory, (CancellableDependency)cancellable, (HttpContext)clientContext), Command.Priority.NORMAL);
/*     */                     
/*     */                     }
/*     */                     else {
/*     */ 
/*     */                       
/* 228 */                       session.enqueue((Command)new RequestExecutionCommand(internalExchangeHandler, pushHandlerFactory, (CancellableDependency)cancellable, (HttpContext)clientContext), Command.Priority.NORMAL);
/*     */                     } 
/*     */                   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   public void failed(Exception ex) {
/* 240 */                     exchangeHandler.failed(ex);
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void cancelled() {
/* 245 */                     exchangeHandler.cancel();
/*     */                   }
/*     */                 });
/*     */             
/*     */             cancellable.setDependency(());
/*     */           }context);
/* 251 */     } catch (HttpException|IOException|IllegalStateException ex) {
/* 252 */       exchangeHandler.failed(ex);
/*     */     } 
/* 254 */     return (Cancellable)cancellable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectionConfigResolver(Resolver<HttpHost, ConnectionConfig> connectionConfigResolver) {
/* 263 */     this.connPool.setConnectionConfigResolver(connectionConfigResolver);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/MinimalH2AsyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */