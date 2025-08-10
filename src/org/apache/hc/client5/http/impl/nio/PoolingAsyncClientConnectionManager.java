/*     */ package org.apache.hc.client5.http.impl.nio;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.impl.ConnPoolSupport;
/*     */ import org.apache.hc.client5.http.impl.ConnectionShutdownException;
/*     */ import org.apache.hc.client5.http.impl.PrefixedIncrementingId;
/*     */ import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
/*     */ import org.apache.hc.client5.http.nio.AsyncClientConnectionOperator;
/*     */ import org.apache.hc.client5.http.nio.AsyncConnectionEndpoint;
/*     */ import org.apache.hc.client5.http.nio.ManagedAsyncClientConnection;
/*     */ import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.BasicFuture;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.ComplexFuture;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.command.RequestExecutionCommand;
/*     */ import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http2.HttpVersionPolicy;
/*     */ import org.apache.hc.core5.http2.nio.AsyncPingHandler;
/*     */ import org.apache.hc.core5.http2.nio.command.PingCommand;
/*     */ import org.apache.hc.core5.http2.nio.support.BasicPingHandler;
/*     */ import org.apache.hc.core5.http2.ssl.ApplicationProtocol;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.io.ModalCloseable;
/*     */ import org.apache.hc.core5.pool.ConnPoolControl;
/*     */ import org.apache.hc.core5.pool.ConnPoolListener;
/*     */ import org.apache.hc.core5.pool.LaxConnPool;
/*     */ import org.apache.hc.core5.pool.ManagedConnPool;
/*     */ import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
/*     */ import org.apache.hc.core5.pool.PoolEntry;
/*     */ import org.apache.hc.core5.pool.PoolReusePolicy;
/*     */ import org.apache.hc.core5.pool.PoolStats;
/*     */ import org.apache.hc.core5.pool.StrictConnPool;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.reactor.ProtocolIOSession;
/*     */ import org.apache.hc.core5.reactor.ssl.TlsDetails;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Deadline;
/*     */ import org.apache.hc.core5.util.Identifiable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public class PoolingAsyncClientConnectionManager
/*     */   implements AsyncClientConnectionManager, ConnPoolControl<HttpRoute>
/*     */ {
/* 118 */   private static final Logger LOG = LoggerFactory.getLogger(PoolingAsyncClientConnectionManager.class);
/*     */   
/*     */   public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 25;
/*     */   
/*     */   public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
/*     */   
/*     */   private final ManagedConnPool<HttpRoute, ManagedAsyncClientConnection> pool;
/*     */   private final AsyncClientConnectionOperator connectionOperator;
/*     */   private final AtomicBoolean closed;
/*     */   private volatile Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver;
/*     */   private volatile Resolver<HttpHost, TlsConfig> tlsConfigResolver;
/*     */   
/*     */   public PoolingAsyncClientConnectionManager() {
/* 131 */     this((Lookup<TlsStrategy>)RegistryBuilder.create()
/* 132 */         .register(URIScheme.HTTPS.getId(), DefaultClientTlsStrategy.getDefault())
/* 133 */         .build());
/*     */   }
/*     */   
/*     */   public PoolingAsyncClientConnectionManager(Lookup<TlsStrategy> tlsStrategyLookup) {
/* 137 */     this(tlsStrategyLookup, PoolConcurrencyPolicy.STRICT, TimeValue.NEG_ONE_MILLISECOND);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingAsyncClientConnectionManager(Lookup<TlsStrategy> tlsStrategyLookup, PoolConcurrencyPolicy poolConcurrencyPolicy, TimeValue timeToLive) {
/* 144 */     this(tlsStrategyLookup, poolConcurrencyPolicy, PoolReusePolicy.LIFO, timeToLive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingAsyncClientConnectionManager(Lookup<TlsStrategy> tlsStrategyLookup, PoolConcurrencyPolicy poolConcurrencyPolicy, PoolReusePolicy poolReusePolicy, TimeValue timeToLive) {
/* 152 */     this(tlsStrategyLookup, poolConcurrencyPolicy, poolReusePolicy, timeToLive, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingAsyncClientConnectionManager(Lookup<TlsStrategy> tlsStrategyLookup, PoolConcurrencyPolicy poolConcurrencyPolicy, PoolReusePolicy poolReusePolicy, TimeValue timeToLive, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
/* 162 */     this(new DefaultAsyncClientConnectionOperator(tlsStrategyLookup, schemePortResolver, dnsResolver), poolConcurrencyPolicy, poolReusePolicy, timeToLive);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Internal
/*     */   protected PoolingAsyncClientConnectionManager(AsyncClientConnectionOperator connectionOperator, PoolConcurrencyPolicy poolConcurrencyPolicy, PoolReusePolicy poolReusePolicy, TimeValue timeToLive) {
/* 172 */     this.connectionOperator = (AsyncClientConnectionOperator)Args.notNull(connectionOperator, "Connection operator");
/* 173 */     switch ((poolConcurrencyPolicy != null) ? poolConcurrencyPolicy : PoolConcurrencyPolicy.STRICT) {
/*     */       case STRICT:
/* 175 */         this.pool = (ManagedConnPool<HttpRoute, ManagedAsyncClientConnection>)new StrictConnPool<HttpRoute, ManagedAsyncClientConnection>(5, 25, timeToLive, poolReusePolicy, null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void closeExpired()
/*     */             {
/* 184 */               enumAvailable(e -> PoolingAsyncClientConnectionManager.this.closeIfExpired(e));
/*     */             }
/*     */           };
/*     */         break;
/*     */       
/*     */       case LAX:
/* 190 */         this.pool = (ManagedConnPool<HttpRoute, ManagedAsyncClientConnection>)new LaxConnPool<HttpRoute, ManagedAsyncClientConnection>(5, timeToLive, poolReusePolicy, null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void closeExpired()
/*     */             {
/* 198 */               enumAvailable(e -> PoolingAsyncClientConnectionManager.this.closeIfExpired(e));
/*     */             }
/*     */           };
/*     */         break;
/*     */       
/*     */       default:
/* 204 */         throw new IllegalArgumentException("Unexpected PoolConcurrencyPolicy value: " + poolConcurrencyPolicy);
/*     */     } 
/* 206 */     this.closed = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Internal
/*     */   protected PoolingAsyncClientConnectionManager(ManagedConnPool<HttpRoute, ManagedAsyncClientConnection> pool, AsyncClientConnectionOperator connectionOperator) {
/* 213 */     this.connectionOperator = (AsyncClientConnectionOperator)Args.notNull(connectionOperator, "Connection operator");
/* 214 */     this.pool = (ManagedConnPool<HttpRoute, ManagedAsyncClientConnection>)Args.notNull(pool, "Connection pool");
/* 215 */     this.closed = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 220 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 225 */     if (this.closed.compareAndSet(false, true)) {
/* 226 */       if (LOG.isDebugEnabled()) {
/* 227 */         LOG.debug("Shutdown connection pool {}", closeMode);
/*     */       }
/* 229 */       this.pool.close(closeMode);
/* 230 */       LOG.debug("Connection pool shut down");
/*     */     } 
/*     */   }
/*     */   
/*     */   private InternalConnectionEndpoint cast(AsyncConnectionEndpoint endpoint) {
/* 235 */     if (endpoint instanceof InternalConnectionEndpoint) {
/* 236 */       return (InternalConnectionEndpoint)endpoint;
/*     */     }
/* 238 */     throw new IllegalStateException("Unexpected endpoint class: " + endpoint.getClass());
/*     */   }
/*     */   
/*     */   private ConnectionConfig resolveConnectionConfig(HttpRoute route) {
/* 242 */     Resolver<HttpRoute, ConnectionConfig> resolver = this.connectionConfigResolver;
/* 243 */     ConnectionConfig connectionConfig = (resolver != null) ? (ConnectionConfig)resolver.resolve(route) : null;
/* 244 */     return (connectionConfig != null) ? connectionConfig : ConnectionConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   private TlsConfig resolveTlsConfig(HttpHost host, Object attachment) {
/* 248 */     if (attachment instanceof TlsConfig) {
/* 249 */       return (TlsConfig)attachment;
/*     */     }
/* 251 */     Resolver<HttpHost, TlsConfig> resolver = this.tlsConfigResolver;
/* 252 */     TlsConfig tlsConfig = (resolver != null) ? (TlsConfig)resolver.resolve(host) : null;
/* 253 */     return (tlsConfig != null) ? tlsConfig : TlsConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AsyncConnectionEndpoint> lease(final String id, final HttpRoute route, final Object state, final Timeout requestTimeout, final FutureCallback<AsyncConnectionEndpoint> callback) {
/* 263 */     if (LOG.isDebugEnabled()) {
/* 264 */       LOG.debug("{} endpoint lease request ({}) {}", new Object[] { id, requestTimeout, ConnPoolSupport.formatStats(route, state, (ConnPoolControl)this.pool) });
/*     */     }
/* 266 */     return new Future<AsyncConnectionEndpoint>()
/*     */       {
/* 268 */         final ConnectionConfig connectionConfig = PoolingAsyncClientConnectionManager.this.resolveConnectionConfig(route);
/* 269 */         final BasicFuture<AsyncConnectionEndpoint> resultFuture = new BasicFuture(callback);
/*     */         
/* 271 */         final Future<PoolEntry<HttpRoute, ManagedAsyncClientConnection>> leaseFuture = PoolingAsyncClientConnectionManager.this.pool.lease(route, state, requestTimeout, new FutureCallback<PoolEntry<HttpRoute, ManagedAsyncClientConnection>>()
/*     */             {
/*     */ 
/*     */ 
/*     */               
/*     */               public void completed(PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry)
/*     */               {
/* 278 */                 if (poolEntry.hasConnection()) {
/* 279 */                   TimeValue timeToLive = PoolingAsyncClientConnectionManager.null.this.connectionConfig.getTimeToLive();
/* 280 */                   if (TimeValue.isNonNegative(timeToLive)) {
/* 281 */                     Deadline deadline = Deadline.calculate(poolEntry.getCreated(), timeToLive);
/* 282 */                     if (deadline.isExpired()) {
/* 283 */                       poolEntry.discardConnection(CloseMode.GRACEFUL);
/*     */                     }
/*     */                   } 
/*     */                 } 
/* 287 */                 if (poolEntry.hasConnection()) {
/* 288 */                   ManagedAsyncClientConnection connection = (ManagedAsyncClientConnection)poolEntry.getConnection();
/* 289 */                   TimeValue timeValue = PoolingAsyncClientConnectionManager.null.this.connectionConfig.getValidateAfterInactivity();
/* 290 */                   if (connection.isOpen() && TimeValue.isNonNegative(timeValue)) {
/* 291 */                     Deadline deadline = Deadline.calculate(poolEntry.getUpdated(), timeValue);
/* 292 */                     if (deadline.isExpired()) {
/* 293 */                       ProtocolVersion protocolVersion = connection.getProtocolVersion();
/* 294 */                       if (protocolVersion != null && protocolVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2_0)) {
/* 295 */                         connection.submitCommand((Command)new PingCommand((AsyncPingHandler)new BasicPingHandler(result -> { if (result == null || !result.booleanValue()) { if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) PoolingAsyncClientConnectionManager.LOG.debug("{} connection {} is stale", id, ConnPoolSupport.getId(connection));  poolEntry.discardConnection(CloseMode.GRACEFUL); }  leaseCompleted(poolEntry); })), Command.Priority.IMMEDIATE);
/*     */ 
/*     */ 
/*     */ 
/*     */                         
/*     */                         return;
/*     */                       } 
/*     */ 
/*     */ 
/*     */ 
/*     */                       
/* 306 */                       if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 307 */                         PoolingAsyncClientConnectionManager.LOG.debug("{} connection {} is closed", id, ConnPoolSupport.getId(connection));
/*     */                       }
/* 309 */                       poolEntry.discardConnection(CloseMode.IMMEDIATE);
/*     */                     } 
/*     */                   } 
/*     */                 } 
/*     */                 
/* 314 */                 leaseCompleted(poolEntry);
/*     */               }
/*     */               
/*     */               void leaseCompleted(PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry) {
/* 318 */                 ManagedAsyncClientConnection connection = (ManagedAsyncClientConnection)poolEntry.getConnection();
/* 319 */                 if (connection != null) {
/* 320 */                   connection.activate();
/*     */                 }
/* 322 */                 if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 323 */                   PoolingAsyncClientConnectionManager.LOG.debug("{} endpoint leased {}", id, ConnPoolSupport.formatStats(route, state, (ConnPoolControl)PoolingAsyncClientConnectionManager.this.pool));
/*     */                 }
/* 325 */                 AsyncConnectionEndpoint endpoint = new PoolingAsyncClientConnectionManager.InternalConnectionEndpoint(poolEntry);
/* 326 */                 if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 327 */                   PoolingAsyncClientConnectionManager.LOG.debug("{} acquired {}", id, ConnPoolSupport.getId(endpoint));
/*     */                 }
/* 329 */                 PoolingAsyncClientConnectionManager.null.this.resultFuture.completed(endpoint);
/*     */               }
/*     */ 
/*     */               
/*     */               public void failed(Exception ex) {
/* 334 */                 if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 335 */                   PoolingAsyncClientConnectionManager.LOG.debug("{} endpoint lease failed", id);
/*     */                 }
/* 337 */                 PoolingAsyncClientConnectionManager.null.this.resultFuture.failed(ex);
/*     */               }
/*     */ 
/*     */               
/*     */               public void cancelled() {
/* 342 */                 if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 343 */                   PoolingAsyncClientConnectionManager.LOG.debug("{} endpoint lease cancelled", id);
/*     */                 }
/* 345 */                 PoolingAsyncClientConnectionManager.null.this.resultFuture.cancel();
/*     */               }
/*     */             });
/*     */ 
/*     */ 
/*     */         
/*     */         public AsyncConnectionEndpoint get() throws InterruptedException, ExecutionException {
/* 352 */           return (AsyncConnectionEndpoint)this.resultFuture.get();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public AsyncConnectionEndpoint get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 358 */           return (AsyncConnectionEndpoint)this.resultFuture.get(timeout, unit);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean cancel(boolean mayInterruptIfRunning) {
/* 363 */           return this.leaseFuture.cancel(mayInterruptIfRunning);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDone() {
/* 368 */           return this.resultFuture.isDone();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isCancelled() {
/* 373 */           return this.resultFuture.isCancelled();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void release(AsyncConnectionEndpoint endpoint, Object state, TimeValue keepAlive) {
/* 381 */     Args.notNull(endpoint, "Managed endpoint");
/* 382 */     Args.notNull(keepAlive, "Keep-alive time");
/* 383 */     PoolEntry<HttpRoute, ManagedAsyncClientConnection> entry = cast(endpoint).detach();
/* 384 */     if (entry == null) {
/*     */       return;
/*     */     }
/* 387 */     if (LOG.isDebugEnabled()) {
/* 388 */       LOG.debug("{} releasing endpoint", ConnPoolSupport.getId(endpoint));
/*     */     }
/* 390 */     ManagedAsyncClientConnection connection = (ManagedAsyncClientConnection)entry.getConnection();
/* 391 */     boolean reusable = (connection != null && connection.isOpen());
/*     */     try {
/* 393 */       if (reusable) {
/* 394 */         entry.updateState(state);
/* 395 */         entry.updateExpiry(keepAlive);
/* 396 */         connection.passivate();
/* 397 */         if (LOG.isDebugEnabled()) {
/*     */           String s;
/* 399 */           if (TimeValue.isPositive(keepAlive)) {
/* 400 */             s = "for " + keepAlive;
/*     */           } else {
/* 402 */             s = "indefinitely";
/*     */           } 
/* 404 */           LOG.debug("{} connection {} can be kept alive {}", new Object[] { ConnPoolSupport.getId(endpoint), ConnPoolSupport.getId(connection), s });
/*     */         } 
/*     */       } 
/* 407 */     } catch (RuntimeException ex) {
/* 408 */       reusable = false;
/* 409 */       throw ex;
/*     */     } finally {
/* 411 */       this.pool.release(entry, reusable);
/* 412 */       if (LOG.isDebugEnabled()) {
/* 413 */         LOG.debug("{} connection released {}", ConnPoolSupport.getId(endpoint), ConnPoolSupport.formatStats((HttpRoute)entry.getRoute(), entry.getState(), (ConnPoolControl)this.pool));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<AsyncConnectionEndpoint> connect(final AsyncConnectionEndpoint endpoint, ConnectionInitiator connectionInitiator, Timeout timeout, Object attachment, HttpContext context, FutureCallback<AsyncConnectionEndpoint> callback) {
/*     */     HttpHost host;
/* 426 */     Args.notNull(endpoint, "Endpoint");
/* 427 */     Args.notNull(connectionInitiator, "Connection initiator");
/* 428 */     final InternalConnectionEndpoint internalEndpoint = cast(endpoint);
/* 429 */     final ComplexFuture<AsyncConnectionEndpoint> resultFuture = new ComplexFuture(callback);
/* 430 */     if (internalEndpoint.isConnected()) {
/* 431 */       resultFuture.completed(endpoint);
/* 432 */       return (Future<AsyncConnectionEndpoint>)resultFuture;
/*     */     } 
/* 434 */     final PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry = internalEndpoint.getPoolEntry();
/* 435 */     HttpRoute route = (HttpRoute)poolEntry.getRoute();
/*     */     
/* 437 */     if (route.getProxyHost() != null) {
/* 438 */       host = route.getProxyHost();
/*     */     } else {
/* 440 */       host = route.getTargetHost();
/*     */     } 
/* 442 */     InetSocketAddress localAddress = route.getLocalSocketAddress();
/* 443 */     final ConnectionConfig connectionConfig = resolveConnectionConfig(route);
/* 444 */     TlsConfig tlsConfig = resolveTlsConfig(host, attachment);
/* 445 */     Timeout connectTimeout = (timeout != null) ? timeout : connectionConfig.getConnectTimeout();
/*     */     
/* 447 */     if (LOG.isDebugEnabled()) {
/* 448 */       LOG.debug("{} connecting endpoint to {} ({})", new Object[] { ConnPoolSupport.getId(endpoint), host, connectTimeout });
/*     */     }
/* 450 */     Future<ManagedAsyncClientConnection> connectFuture = this.connectionOperator.connect(connectionInitiator, host, localAddress, connectTimeout, 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 455 */         route.isTunnelled() ? TlsConfig.copy(tlsConfig)
/* 456 */         .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1)
/* 457 */         .build() : tlsConfig, context, new FutureCallback<ManagedAsyncClientConnection>()
/*     */         {
/*     */ 
/*     */           
/*     */           public void completed(ManagedAsyncClientConnection connection)
/*     */           {
/*     */             try {
/* 464 */               if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 465 */                 PoolingAsyncClientConnectionManager.LOG.debug("{} connected {}", ConnPoolSupport.getId(endpoint), ConnPoolSupport.getId(connection));
/*     */               }
/* 467 */               ProtocolVersion protocolVersion = connection.getProtocolVersion();
/* 468 */               Timeout socketTimeout = connectionConfig.getSocketTimeout();
/* 469 */               if (socketTimeout != null) {
/* 470 */                 connection.setSocketTimeout(socketTimeout);
/*     */               }
/* 472 */               poolEntry.assignConnection((ModalCloseable)connection);
/* 473 */               resultFuture.completed(internalEndpoint);
/* 474 */             } catch (RuntimeException ex) {
/* 475 */               resultFuture.failed(ex);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception ex) {
/* 481 */             resultFuture.failed(ex);
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 486 */             resultFuture.cancel();
/*     */           }
/*     */         });
/* 489 */     resultFuture.setDependency(connectFuture);
/* 490 */     return (Future<AsyncConnectionEndpoint>)resultFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(final AsyncConnectionEndpoint endpoint, Object attachment, HttpContext context, final FutureCallback<AsyncConnectionEndpoint> callback) {
/* 499 */     Args.notNull(endpoint, "Managed endpoint");
/* 500 */     final InternalConnectionEndpoint internalEndpoint = cast(endpoint);
/* 501 */     PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry = internalEndpoint.getValidatedPoolEntry();
/* 502 */     HttpRoute route = (HttpRoute)poolEntry.getRoute();
/* 503 */     HttpHost host = (route.getProxyHost() != null) ? route.getProxyHost() : route.getTargetHost();
/* 504 */     TlsConfig tlsConfig = resolveTlsConfig(host, attachment);
/* 505 */     this.connectionOperator.upgrade((ManagedAsyncClientConnection)poolEntry
/* 506 */         .getConnection(), route
/* 507 */         .getTargetHost(), (attachment != null) ? attachment : tlsConfig, context, (FutureCallback)new CallbackContribution<ManagedAsyncClientConnection>(callback)
/*     */         {
/*     */ 
/*     */ 
/*     */           
/*     */           public void completed(ManagedAsyncClientConnection connection)
/*     */           {
/* 514 */             if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 515 */               PoolingAsyncClientConnectionManager.LOG.debug("{} upgraded {}", ConnPoolSupport.getId(internalEndpoint), ConnPoolSupport.getId(connection));
/*     */             }
/* 517 */             TlsDetails tlsDetails = connection.getTlsDetails();
/* 518 */             if (tlsDetails != null && ApplicationProtocol.HTTP_2.id.equals(tlsDetails.getApplicationProtocol())) {
/* 519 */               connection.switchProtocol(ApplicationProtocol.HTTP_2.id, (FutureCallback)new CallbackContribution<ProtocolIOSession>(callback)
/*     */                   {
/*     */                     public void completed(ProtocolIOSession protocolIOSession)
/*     */                     {
/* 523 */                       if (callback != null) {
/* 524 */                         callback.completed(endpoint);
/*     */                       
/*     */                       }
/*     */                     }
/*     */                   });
/*     */             }
/* 530 */             else if (callback != null) {
/* 531 */               callback.completed(endpoint);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgrade(AsyncConnectionEndpoint endpoint, Object attachment, HttpContext context) {
/* 540 */     upgrade(endpoint, attachment, context, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<HttpRoute> getRoutes() {
/* 545 */     return this.pool.getRoutes();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 550 */     this.pool.setMaxTotal(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 555 */     return this.pool.getMaxTotal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 560 */     this.pool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 565 */     return this.pool.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(HttpRoute route, int max) {
/* 570 */     this.pool.setMaxPerRoute(route, max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(HttpRoute route) {
/* 575 */     return this.pool.getMaxPerRoute(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdle(TimeValue idletime) {
/* 580 */     this.pool.closeIdle(idletime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 585 */     this.pool.closeExpired();
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 590 */     return this.pool.getTotalStats();
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(HttpRoute route) {
/* 595 */     return this.pool.getStats(route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultConnectionConfig(ConnectionConfig config) {
/* 604 */     this.connectionConfigResolver = (route -> config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectionConfigResolver(Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver) {
/* 613 */     this.connectionConfigResolver = connectionConfigResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultTlsConfig(TlsConfig config) {
/* 622 */     this.tlsConfigResolver = (host -> config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTlsConfigResolver(Resolver<HttpHost, TlsConfig> tlsConfigResolver) {
/* 631 */     this.tlsConfigResolver = tlsConfigResolver;
/*     */   }
/*     */   
/*     */   void closeIfExpired(PoolEntry<HttpRoute, ManagedAsyncClientConnection> entry) {
/* 635 */     long now = System.currentTimeMillis();
/* 636 */     if (entry.getExpiryDeadline().isBefore(now)) {
/* 637 */       entry.discardConnection(CloseMode.GRACEFUL);
/*     */     } else {
/* 639 */       ConnectionConfig connectionConfig = resolveConnectionConfig((HttpRoute)entry.getRoute());
/* 640 */       TimeValue timeToLive = connectionConfig.getTimeToLive();
/* 641 */       if (timeToLive != null && Deadline.calculate(entry.getCreated(), timeToLive).isBefore(now)) {
/* 642 */         entry.discardConnection(CloseMode.GRACEFUL);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TimeValue getValidateAfterInactivity() {
/* 652 */     return ConnectionConfig.DEFAULT.getValidateAfterInactivity();
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
/*     */   @Deprecated
/*     */   public void setValidateAfterInactivity(TimeValue validateAfterInactivity) {
/* 666 */     setDefaultConnectionConfig(ConnectionConfig.custom()
/* 667 */         .setValidateAfterInactivity(validateAfterInactivity)
/* 668 */         .build());
/*     */   }
/*     */   
/* 671 */   private static final PrefixedIncrementingId INCREMENTING_ID = new PrefixedIncrementingId("ep-");
/*     */   
/*     */   class InternalConnectionEndpoint
/*     */     extends AsyncConnectionEndpoint implements Identifiable {
/*     */     private final AtomicReference<PoolEntry<HttpRoute, ManagedAsyncClientConnection>> poolEntryRef;
/*     */     private final String id;
/*     */     
/*     */     InternalConnectionEndpoint(PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry) {
/* 679 */       this.poolEntryRef = new AtomicReference<>(poolEntry);
/* 680 */       this.id = PoolingAsyncClientConnectionManager.INCREMENTING_ID.getNextId();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getId() {
/* 685 */       return this.id;
/*     */     }
/*     */     
/*     */     PoolEntry<HttpRoute, ManagedAsyncClientConnection> getPoolEntry() {
/* 689 */       PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry = this.poolEntryRef.get();
/* 690 */       if (poolEntry == null) {
/* 691 */         throw new ConnectionShutdownException();
/*     */       }
/* 693 */       return poolEntry;
/*     */     }
/*     */     
/*     */     PoolEntry<HttpRoute, ManagedAsyncClientConnection> getValidatedPoolEntry() {
/* 697 */       PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry = getPoolEntry();
/* 698 */       if (poolEntry.getConnection() == null) {
/* 699 */         throw new ConnectionShutdownException();
/*     */       }
/* 701 */       return poolEntry;
/*     */     }
/*     */     
/*     */     PoolEntry<HttpRoute, ManagedAsyncClientConnection> detach() {
/* 705 */       return this.poolEntryRef.getAndSet(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close(CloseMode closeMode) {
/* 710 */       PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry = this.poolEntryRef.get();
/* 711 */       if (poolEntry != null) {
/* 712 */         if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 713 */           PoolingAsyncClientConnectionManager.LOG.debug("{} close {}", this.id, closeMode);
/*     */         }
/* 715 */         poolEntry.discardConnection(closeMode);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isConnected() {
/* 721 */       PoolEntry<HttpRoute, ManagedAsyncClientConnection> poolEntry = this.poolEntryRef.get();
/* 722 */       if (poolEntry == null) {
/* 723 */         return false;
/*     */       }
/* 725 */       ManagedAsyncClientConnection connection = (ManagedAsyncClientConnection)poolEntry.getConnection();
/* 726 */       if (connection == null) {
/* 727 */         return false;
/*     */       }
/* 729 */       if (!connection.isOpen()) {
/* 730 */         poolEntry.discardConnection(CloseMode.IMMEDIATE);
/* 731 */         return false;
/*     */       } 
/* 733 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSocketTimeout(Timeout timeout) {
/* 738 */       ((ManagedAsyncClientConnection)getValidatedPoolEntry().getConnection()).setSocketTimeout(timeout);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void execute(String exchangeId, AsyncClientExchangeHandler exchangeHandler, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context) {
/* 747 */       ManagedAsyncClientConnection connection = (ManagedAsyncClientConnection)getValidatedPoolEntry().getConnection();
/* 748 */       if (PoolingAsyncClientConnectionManager.LOG.isDebugEnabled()) {
/* 749 */         PoolingAsyncClientConnectionManager.LOG.debug("{} executing exchange {} over {}", new Object[] { this.id, exchangeId, ConnPoolSupport.getId(connection) });
/*     */       }
/* 751 */       context.setProtocolVersion(connection.getProtocolVersion());
/* 752 */       connection.submitCommand((Command)new RequestExecutionCommand(exchangeHandler, pushHandlerFactory, context), Command.Priority.NORMAL);
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
/*     */   boolean isClosed() {
/* 767 */     return this.closed.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/nio/PoolingAsyncClientConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */