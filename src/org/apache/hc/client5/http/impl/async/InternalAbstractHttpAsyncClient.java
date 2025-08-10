/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.async.AsyncExecCallback;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChain;
/*     */ import org.apache.hc.client5.http.async.AsyncExecRuntime;
/*     */ import org.apache.hc.client5.http.auth.AuthSchemeFactory;
/*     */ import org.apache.hc.client5.http.auth.CredentialsProvider;
/*     */ import org.apache.hc.client5.http.config.Configurable;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.cookie.CookieSpecFactory;
/*     */ import org.apache.hc.client5.http.cookie.CookieStore;
/*     */ import org.apache.hc.client5.http.impl.ExecSupport;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.client5.http.routing.RoutingSupport;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*     */ import org.apache.hc.core5.concurrent.DefaultThreadFactory;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.support.BasicRequestBuilder;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.reactor.DefaultConnectingIOReactor;
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
/*     */ 
/*     */ 
/*     */ abstract class InternalAbstractHttpAsyncClient
/*     */   extends AbstractHttpAsyncClientBase
/*     */ {
/*  84 */   private static final ThreadFactory SCHEDULER_THREAD_FACTORY = (ThreadFactory)new DefaultThreadFactory("Scheduled-executor", true);
/*     */   
/*  86 */   private static final Logger LOG = LoggerFactory.getLogger(InternalAbstractHttpAsyncClient.class);
/*     */ 
/*     */   
/*     */   private final AsyncExecChainElement execChain;
/*     */   
/*     */   private final Lookup<CookieSpecFactory> cookieSpecRegistry;
/*     */   
/*     */   private final Lookup<AuthSchemeFactory> authSchemeRegistry;
/*     */   
/*     */   private final CookieStore cookieStore;
/*     */   
/*     */   private final CredentialsProvider credentialsProvider;
/*     */   
/*     */   private final RequestConfig defaultConfig;
/*     */   
/*     */   private final ConcurrentLinkedQueue<Closeable> closeables;
/*     */   
/*     */   private final ScheduledExecutorService scheduledExecutorService;
/*     */ 
/*     */   
/*     */   InternalAbstractHttpAsyncClient(DefaultConnectingIOReactor ioReactor, AsyncPushConsumerRegistry pushConsumerRegistry, ThreadFactory threadFactory, AsyncExecChainElement execChain, Lookup<CookieSpecFactory> cookieSpecRegistry, Lookup<AuthSchemeFactory> authSchemeRegistry, CookieStore cookieStore, CredentialsProvider credentialsProvider, RequestConfig defaultConfig, List<Closeable> closeables) {
/* 107 */     super(ioReactor, pushConsumerRegistry, threadFactory);
/* 108 */     this.execChain = execChain;
/* 109 */     this.cookieSpecRegistry = cookieSpecRegistry;
/* 110 */     this.authSchemeRegistry = authSchemeRegistry;
/* 111 */     this.cookieStore = cookieStore;
/* 112 */     this.credentialsProvider = credentialsProvider;
/* 113 */     this.defaultConfig = defaultConfig;
/* 114 */     this.closeables = (closeables != null) ? new ConcurrentLinkedQueue<>(closeables) : null;
/* 115 */     this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(SCHEDULER_THREAD_FACTORY);
/*     */   }
/*     */ 
/*     */   
/*     */   void internalClose(CloseMode closeMode) {
/* 120 */     if (this.closeables != null) {
/*     */       Closeable closeable;
/* 122 */       while ((closeable = this.closeables.poll()) != null) {
/*     */         try {
/* 124 */           if (closeable instanceof ModalCloseable) {
/* 125 */             ((ModalCloseable)closeable).close(closeMode); continue;
/*     */           } 
/* 127 */           closeable.close();
/*     */         }
/* 129 */         catch (IOException ex) {
/* 130 */           LOG.error(ex.getMessage(), ex);
/*     */         } 
/*     */       } 
/*     */     } 
/* 134 */     List<Runnable> runnables = this.scheduledExecutorService.shutdownNow();
/* 135 */     for (Runnable runnable : runnables) {
/* 136 */       if (runnable instanceof Cancellable) {
/* 137 */         ((Cancellable)runnable).cancel();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupContext(HttpClientContext context) {
/* 143 */     if (context.getAttribute("http.authscheme-registry") == null) {
/* 144 */       context.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
/*     */     }
/* 146 */     if (context.getAttribute("http.cookiespec-registry") == null) {
/* 147 */       context.setAttribute("http.cookiespec-registry", this.cookieSpecRegistry);
/*     */     }
/* 149 */     if (context.getAttribute("http.cookie-store") == null) {
/* 150 */       context.setAttribute("http.cookie-store", this.cookieStore);
/*     */     }
/* 152 */     if (context.getAttribute("http.auth.credentials-provider") == null) {
/* 153 */       context.setAttribute("http.auth.credentials-provider", this.credentialsProvider);
/*     */     }
/* 155 */     if (context.getAttribute("http.request-config") == null) {
/* 156 */       context.setAttribute("http.request-config", this.defaultConfig);
/*     */     }
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
/*     */   protected <T> Future<T> doExecute(HttpHost httpHost, final AsyncRequestProducer requestProducer, final AsyncResponseConsumer<T> responseConsumer, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context, FutureCallback<T> callback) {
/* 172 */     final ComplexFuture<T> future = new ComplexFuture(callback);
/*     */     try {
/* 174 */       if (!isRunning()) {
/* 175 */         throw new CancellationException("Request execution cancelled");
/*     */       }
/* 177 */       HttpClientContext clientContext = (context != null) ? HttpClientContext.adapt(context) : HttpClientContext.create();
/* 178 */       requestProducer.sendRequest((request, entityDetails, c) -> {
/*     */             RequestConfig requestConfig = null;
/*     */             
/*     */             if (request instanceof Configurable) {
/*     */               requestConfig = ((Configurable)request).getConfig();
/*     */             }
/*     */             
/*     */             if (requestConfig != null) {
/*     */               clientContext.setRequestConfig(requestConfig);
/*     */             }
/*     */             
/*     */             setupContext(clientContext);
/*     */             
/*     */             HttpRoute route = determineRoute((httpHost != null) ? httpHost : RoutingSupport.determineHost(request), clientContext);
/*     */             
/*     */             final String exchangeId = ExecSupport.getNextExchangeId();
/*     */             
/*     */             clientContext.setExchangeId(exchangeId);
/*     */             
/*     */             if (LOG.isDebugEnabled()) {
/*     */               LOG.debug("{} preparing request execution", exchangeId);
/*     */             }
/*     */             
/*     */             final AsyncExecRuntime execRuntime = createAsyncExecRuntime(pushHandlerFactory);
/*     */             
/*     */             AsyncExecChain.Scheduler scheduler = this::executeScheduled;
/*     */             
/*     */             AsyncExecChain.Scope scope = new AsyncExecChain.Scope(exchangeId, route, request, (CancellableDependency)future, clientContext, execRuntime, scheduler, new AtomicInteger(1));
/*     */             final AtomicBoolean outputTerminated = new AtomicBoolean(false);
/*     */             executeImmediate((HttpRequest)BasicRequestBuilder.copy(request).build(), (entityDetails != null) ? new AsyncEntityProducer()
/*     */                 {
/*     */                   public void releaseResources()
/*     */                   {
/* 211 */                     requestProducer.releaseResources();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void failed(Exception cause) {
/* 216 */                     requestProducer.failed(cause);
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public boolean isRepeatable() {
/* 221 */                     return requestProducer.isRepeatable();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public long getContentLength() {
/* 226 */                     return entityDetails.getContentLength();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public String getContentType() {
/* 231 */                     return entityDetails.getContentType();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public String getContentEncoding() {
/* 236 */                     return entityDetails.getContentEncoding();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public boolean isChunked() {
/* 241 */                     return entityDetails.isChunked();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public Set<String> getTrailerNames() {
/* 246 */                     return entityDetails.getTrailerNames();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public int available() {
/* 251 */                     return requestProducer.available();
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void produce(DataStreamChannel channel) throws IOException {
/* 256 */                     if (outputTerminated.get()) {
/* 257 */                       channel.endStream();
/*     */                       return;
/*     */                     } 
/* 260 */                     requestProducer.produce(channel);
/*     */                   }
/*     */                 }, 
/*     */ 
/*     */                 
/*     */                  : null, scope, new AsyncExecCallback()
/*     */                 {
/*     */ 
/*     */                   
/*     */                   public AsyncDataConsumer handleResponse(HttpResponse response, EntityDetails entityDetails) throws HttpException, IOException
/*     */                   {
/* 271 */                     if (response.getCode() >= 400) {
/* 272 */                       outputTerminated.set(true);
/* 273 */                       requestProducer.releaseResources();
/*     */                     } 
/* 275 */                     responseConsumer.consumeResponse(response, entityDetails, c, new FutureCallback<T>()
/*     */                         {
/*     */                           
/*     */                           public void completed(Object result)
/*     */                           {
/* 280 */                             future.completed(result);
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void failed(Exception ex) {
/* 285 */                             future.failed(ex);
/*     */                           }
/*     */ 
/*     */                           
/*     */                           public void cancelled() {
/* 290 */                             future.cancel();
/*     */                           }
/*     */                         });
/*     */                     
/* 294 */                     return (entityDetails != null) ? (AsyncDataConsumer)responseConsumer : null;
/*     */                   }
/*     */ 
/*     */ 
/*     */                   
/*     */                   public void handleInformationResponse(HttpResponse response) throws HttpException, IOException {
/* 300 */                     responseConsumer.informationResponse(response, c);
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void completed() {
/* 305 */                     if (InternalAbstractHttpAsyncClient.LOG.isDebugEnabled()) {
/* 306 */                       InternalAbstractHttpAsyncClient.LOG.debug("{} message exchange successfully completed", exchangeId);
/*     */                     }
/*     */                     try {
/* 309 */                       execRuntime.releaseEndpoint();
/*     */                     } finally {
/* 311 */                       responseConsumer.releaseResources();
/* 312 */                       requestProducer.releaseResources();
/*     */                     } 
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void failed(Exception cause) {
/* 318 */                     if (InternalAbstractHttpAsyncClient.LOG.isDebugEnabled()) {
/* 319 */                       InternalAbstractHttpAsyncClient.LOG.debug("{} request failed: {}", exchangeId, cause.getMessage());
/*     */                     }
/*     */                     try {
/* 322 */                       execRuntime.discardEndpoint();
/* 323 */                       responseConsumer.failed(cause);
/*     */                     } finally {
/*     */                       try {
/* 326 */                         future.failed(cause);
/*     */                       } finally {
/* 328 */                         responseConsumer.releaseResources();
/* 329 */                         requestProducer.releaseResources();
/*     */                       }
/*     */                     
/*     */                     } 
/*     */                   }
/*     */                 });
/*     */           }context);
/* 336 */     } catch (HttpException|IOException|IllegalStateException ex) {
/* 337 */       future.failed(ex);
/*     */     } 
/* 339 */     return (Future<T>)future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void executeImmediate(HttpRequest request, AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 347 */     this.execChain.execute(request, entityProducer, scope, asyncExecCallback);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void executeScheduled(HttpRequest request, AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecCallback asyncExecCallback, TimeValue delay) {
/* 356 */     ScheduledRequestExecution scheduledTask = new ScheduledRequestExecution(request, entityProducer, scope, asyncExecCallback, delay);
/*     */     
/* 358 */     if (TimeValue.isPositive(delay)) {
/* 359 */       this.scheduledExecutorService.schedule(scheduledTask, delay.getDuration(), delay.getTimeUnit());
/*     */     } else {
/* 361 */       this.scheduledExecutorService.execute(scheduledTask);
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract AsyncExecRuntime createAsyncExecRuntime(HandlerFactory<AsyncPushConsumer> paramHandlerFactory);
/*     */   
/*     */   abstract HttpRoute determineRoute(HttpHost paramHttpHost, HttpClientContext paramHttpClientContext) throws HttpException;
/*     */   
/*     */   class ScheduledRequestExecution
/*     */     implements Runnable, Cancellable {
/*     */     final HttpRequest request;
/*     */     final AsyncEntityProducer entityProducer;
/*     */     final AsyncExecChain.Scope scope;
/*     */     final AsyncExecCallback asyncExecCallback;
/*     */     final TimeValue delay;
/*     */     
/*     */     ScheduledRequestExecution(HttpRequest request, AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecCallback asyncExecCallback, TimeValue delay) {
/* 378 */       this.request = request;
/* 379 */       this.entityProducer = entityProducer;
/* 380 */       this.scope = scope;
/* 381 */       this.asyncExecCallback = asyncExecCallback;
/* 382 */       this.delay = delay;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 388 */         InternalAbstractHttpAsyncClient.this.execChain.execute(this.request, this.entityProducer, this.scope, this.asyncExecCallback);
/* 389 */       } catch (Exception ex) {
/* 390 */         this.asyncExecCallback.failed(ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean cancel() {
/* 396 */       this.asyncExecCallback.failed(new CancellationException("Request execution cancelled"));
/* 397 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/InternalAbstractHttpAsyncClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */