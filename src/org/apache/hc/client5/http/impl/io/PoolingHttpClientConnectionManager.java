/*     */ package org.apache.hc.client5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
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
/*     */ import org.apache.hc.client5.http.io.ConnectionEndpoint;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionOperator;
/*     */ import org.apache.hc.client5.http.io.LeaseRequest;
/*     */ import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
/*     */ import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.function.Resolver;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.URIScheme;
/*     */ import org.apache.hc.core5.http.config.Lookup;
/*     */ import org.apache.hc.core5.http.config.Registry;
/*     */ import org.apache.hc.core5.http.config.RegistryBuilder;
/*     */ import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
/*     */ import org.apache.hc.core5.http.io.HttpClientConnection;
/*     */ import org.apache.hc.core5.http.io.HttpConnectionFactory;
/*     */ import org.apache.hc.core5.http.io.SocketConfig;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ @Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
/*     */ public class PoolingHttpClientConnectionManager
/*     */   implements HttpClientConnectionManager, ConnPoolControl<HttpRoute>
/*     */ {
/* 108 */   private static final Logger LOG = LoggerFactory.getLogger(PoolingHttpClientConnectionManager.class);
/*     */   
/*     */   public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 25;
/*     */   
/*     */   public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 5;
/*     */   
/*     */   private final HttpClientConnectionOperator connectionOperator;
/*     */   private final ManagedConnPool<HttpRoute, ManagedHttpClientConnection> pool;
/*     */   private final HttpConnectionFactory<ManagedHttpClientConnection> connFactory;
/*     */   private final AtomicBoolean closed;
/*     */   private volatile Resolver<HttpRoute, SocketConfig> socketConfigResolver;
/*     */   private volatile Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver;
/*     */   private volatile Resolver<HttpHost, TlsConfig> tlsConfigResolver;
/*     */   
/*     */   public PoolingHttpClientConnectionManager() {
/* 123 */     this(RegistryBuilder.create()
/* 124 */         .register(URIScheme.HTTP.id, PlainConnectionSocketFactory.getSocketFactory())
/* 125 */         .register(URIScheme.HTTPS.id, SSLConnectionSocketFactory.getSocketFactory())
/* 126 */         .build());
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry) {
/* 131 */     this(socketFactoryRegistry, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
/* 137 */     this(socketFactoryRegistry, PoolConcurrencyPolicy.STRICT, TimeValue.NEG_ONE_MILLISECOND, connFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, PoolConcurrencyPolicy poolConcurrencyPolicy, TimeValue timeToLive, HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
/* 145 */     this(socketFactoryRegistry, poolConcurrencyPolicy, PoolReusePolicy.LIFO, timeToLive, connFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, PoolConcurrencyPolicy poolConcurrencyPolicy, PoolReusePolicy poolReusePolicy, TimeValue timeToLive) {
/* 153 */     this(socketFactoryRegistry, poolConcurrencyPolicy, poolReusePolicy, timeToLive, (HttpConnectionFactory<ManagedHttpClientConnection>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, PoolConcurrencyPolicy poolConcurrencyPolicy, PoolReusePolicy poolReusePolicy, TimeValue timeToLive, HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
/* 162 */     this(socketFactoryRegistry, poolConcurrencyPolicy, poolReusePolicy, timeToLive, null, null, connFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, PoolConcurrencyPolicy poolConcurrencyPolicy, PoolReusePolicy poolReusePolicy, TimeValue timeToLive, SchemePortResolver schemePortResolver, DnsResolver dnsResolver, HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
/* 173 */     this(new DefaultHttpClientConnectionOperator((Lookup<ConnectionSocketFactory>)socketFactoryRegistry, schemePortResolver, dnsResolver), poolConcurrencyPolicy, poolReusePolicy, timeToLive, connFactory);
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
/*     */   @Internal
/*     */   protected PoolingHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, PoolConcurrencyPolicy poolConcurrencyPolicy, PoolReusePolicy poolReusePolicy, TimeValue timeToLive, HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
/* 188 */     this.connectionOperator = (HttpClientConnectionOperator)Args.notNull(httpClientConnectionOperator, "Connection operator");
/* 189 */     switch ((poolConcurrencyPolicy != null) ? poolConcurrencyPolicy : PoolConcurrencyPolicy.STRICT) {
/*     */       case STRICT:
/* 191 */         this.pool = (ManagedConnPool<HttpRoute, ManagedHttpClientConnection>)new StrictConnPool<HttpRoute, ManagedHttpClientConnection>(5, 25, timeToLive, poolReusePolicy, null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void closeExpired()
/*     */             {
/* 200 */               enumAvailable(e -> PoolingHttpClientConnectionManager.this.closeIfExpired(e));
/*     */             }
/*     */           };
/*     */         break;
/*     */       
/*     */       case LAX:
/* 206 */         this.pool = (ManagedConnPool<HttpRoute, ManagedHttpClientConnection>)new LaxConnPool<HttpRoute, ManagedHttpClientConnection>(5, timeToLive, poolReusePolicy, null)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void closeExpired()
/*     */             {
/* 214 */               enumAvailable(e -> PoolingHttpClientConnectionManager.this.closeIfExpired(e));
/*     */             }
/*     */           };
/*     */         break;
/*     */       
/*     */       default:
/* 220 */         throw new IllegalArgumentException("Unexpected PoolConcurrencyPolicy value: " + poolConcurrencyPolicy);
/*     */     } 
/* 222 */     this.connFactory = (connFactory != null) ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE;
/* 223 */     this.closed = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Internal
/*     */   protected PoolingHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, ManagedConnPool<HttpRoute, ManagedHttpClientConnection> pool, HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
/* 232 */     this.connectionOperator = (HttpClientConnectionOperator)Args.notNull(httpClientConnectionOperator, "Connection operator");
/* 233 */     this.pool = (ManagedConnPool<HttpRoute, ManagedHttpClientConnection>)Args.notNull(pool, "Connection pool");
/* 234 */     this.connFactory = (connFactory != null) ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE;
/* 235 */     this.closed = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 240 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 245 */     if (this.closed.compareAndSet(false, true)) {
/* 246 */       if (LOG.isDebugEnabled()) {
/* 247 */         LOG.debug("Shutdown connection pool {}", closeMode);
/*     */       }
/* 249 */       this.pool.close(closeMode);
/* 250 */       LOG.debug("Connection pool shut down");
/*     */     } 
/*     */   }
/*     */   
/*     */   private InternalConnectionEndpoint cast(ConnectionEndpoint endpoint) {
/* 255 */     if (endpoint instanceof InternalConnectionEndpoint) {
/* 256 */       return (InternalConnectionEndpoint)endpoint;
/*     */     }
/* 258 */     throw new IllegalStateException("Unexpected endpoint class: " + endpoint.getClass());
/*     */   }
/*     */   
/*     */   private SocketConfig resolveSocketConfig(HttpRoute route) {
/* 262 */     Resolver<HttpRoute, SocketConfig> resolver = this.socketConfigResolver;
/* 263 */     SocketConfig socketConfig = (resolver != null) ? (SocketConfig)resolver.resolve(route) : null;
/* 264 */     return (socketConfig != null) ? socketConfig : SocketConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   private ConnectionConfig resolveConnectionConfig(HttpRoute route) {
/* 268 */     Resolver<HttpRoute, ConnectionConfig> resolver = this.connectionConfigResolver;
/* 269 */     ConnectionConfig connectionConfig = (resolver != null) ? (ConnectionConfig)resolver.resolve(route) : null;
/* 270 */     return (connectionConfig != null) ? connectionConfig : ConnectionConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   private TlsConfig resolveTlsConfig(HttpHost host) {
/* 274 */     Resolver<HttpHost, TlsConfig> resolver = this.tlsConfigResolver;
/* 275 */     TlsConfig tlsConfig = (resolver != null) ? (TlsConfig)resolver.resolve(host) : null;
/* 276 */     return (tlsConfig != null) ? tlsConfig : TlsConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   private TimeValue resolveValidateAfterInactivity(ConnectionConfig connectionConfig) {
/* 280 */     TimeValue timeValue = connectionConfig.getValidateAfterInactivity();
/* 281 */     return (timeValue != null) ? timeValue : TimeValue.ofSeconds(2L);
/*     */   }
/*     */   
/*     */   public LeaseRequest lease(String id, HttpRoute route, Object state) {
/* 285 */     return lease(id, route, Timeout.DISABLED, state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LeaseRequest lease(final String id, final HttpRoute route, Timeout requestTimeout, final Object state) {
/* 294 */     Args.notNull(route, "HTTP route");
/* 295 */     if (LOG.isDebugEnabled()) {
/* 296 */       LOG.debug("{} endpoint lease request ({}) {}", new Object[] { id, requestTimeout, ConnPoolSupport.formatStats(route, state, (ConnPoolControl)this.pool) });
/*     */     }
/* 298 */     final Future<PoolEntry<HttpRoute, ManagedHttpClientConnection>> leaseFuture = this.pool.lease(route, state, requestTimeout, null);
/* 299 */     return new LeaseRequest()
/*     */       {
/*     */         private volatile ConnectionEndpoint endpoint;
/*     */ 
/*     */         
/*     */         public synchronized ConnectionEndpoint get(Timeout timeout) throws InterruptedException, ExecutionException, TimeoutException {
/*     */           PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry;
/* 306 */           Args.notNull(timeout, "Operation timeout");
/* 307 */           if (this.endpoint != null) {
/* 308 */             return this.endpoint;
/*     */           }
/*     */           
/*     */           try {
/* 312 */             poolEntry = leaseFuture.get(timeout.getDuration(), timeout.getTimeUnit());
/* 313 */           } catch (TimeoutException ex) {
/* 314 */             leaseFuture.cancel(true);
/* 315 */             throw ex;
/*     */           } 
/* 317 */           if (PoolingHttpClientConnectionManager.LOG.isDebugEnabled()) {
/* 318 */             PoolingHttpClientConnectionManager.LOG.debug("{} endpoint leased {}", id, ConnPoolSupport.formatStats(route, state, (ConnPoolControl)PoolingHttpClientConnectionManager.this.pool));
/*     */           }
/* 320 */           ConnectionConfig connectionConfig = PoolingHttpClientConnectionManager.this.resolveConnectionConfig(route);
/*     */           try {
/* 322 */             if (poolEntry.hasConnection()) {
/* 323 */               TimeValue timeToLive = connectionConfig.getTimeToLive();
/* 324 */               if (TimeValue.isNonNegative(timeToLive)) {
/* 325 */                 Deadline deadline = Deadline.calculate(poolEntry.getCreated(), timeToLive);
/* 326 */                 if (deadline.isExpired()) {
/* 327 */                   poolEntry.discardConnection(CloseMode.GRACEFUL);
/*     */                 }
/*     */               } 
/*     */             } 
/* 331 */             if (poolEntry.hasConnection()) {
/* 332 */               TimeValue timeValue = PoolingHttpClientConnectionManager.this.resolveValidateAfterInactivity(connectionConfig);
/* 333 */               if (TimeValue.isNonNegative(timeValue)) {
/* 334 */                 Deadline deadline = Deadline.calculate(poolEntry.getUpdated(), timeValue);
/* 335 */                 if (deadline.isExpired()) {
/* 336 */                   boolean stale; ManagedHttpClientConnection managedHttpClientConnection = (ManagedHttpClientConnection)poolEntry.getConnection();
/*     */                   
/*     */                   try {
/* 339 */                     stale = managedHttpClientConnection.isStale();
/* 340 */                   } catch (IOException ignore) {
/* 341 */                     stale = true;
/*     */                   } 
/* 343 */                   if (stale) {
/* 344 */                     if (PoolingHttpClientConnectionManager.LOG.isDebugEnabled()) {
/* 345 */                       PoolingHttpClientConnectionManager.LOG.debug("{} connection {} is stale", id, ConnPoolSupport.getId(managedHttpClientConnection));
/*     */                     }
/* 347 */                     poolEntry.discardConnection(CloseMode.IMMEDIATE);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/* 352 */             ManagedHttpClientConnection conn = (ManagedHttpClientConnection)poolEntry.getConnection();
/* 353 */             if (conn != null) {
/* 354 */               conn.activate();
/*     */             } else {
/* 356 */               poolEntry.assignConnection((ModalCloseable)PoolingHttpClientConnectionManager.this.connFactory.createConnection(null));
/*     */             } 
/* 358 */             this.endpoint = new PoolingHttpClientConnectionManager.InternalConnectionEndpoint(poolEntry);
/* 359 */             if (PoolingHttpClientConnectionManager.LOG.isDebugEnabled()) {
/* 360 */               PoolingHttpClientConnectionManager.LOG.debug("{} acquired {}", id, ConnPoolSupport.getId(this.endpoint));
/*     */             }
/* 362 */             return this.endpoint;
/* 363 */           } catch (Exception ex) {
/* 364 */             if (PoolingHttpClientConnectionManager.LOG.isDebugEnabled()) {
/* 365 */               PoolingHttpClientConnectionManager.LOG.debug("{} endpoint lease failed", id);
/*     */             }
/* 367 */             PoolingHttpClientConnectionManager.this.pool.release(poolEntry, false);
/* 368 */             throw new ExecutionException(ex.getMessage(), ex);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean cancel() {
/* 374 */           return leaseFuture.cancel(true);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void release(ConnectionEndpoint endpoint, Object state, TimeValue keepAlive) {
/* 383 */     Args.notNull(endpoint, "Managed endpoint");
/* 384 */     PoolEntry<HttpRoute, ManagedHttpClientConnection> entry = cast(endpoint).detach();
/* 385 */     if (entry == null) {
/*     */       return;
/*     */     }
/* 388 */     if (LOG.isDebugEnabled()) {
/* 389 */       LOG.debug("{} releasing endpoint", ConnPoolSupport.getId(endpoint));
/*     */     }
/* 391 */     ManagedHttpClientConnection conn = (ManagedHttpClientConnection)entry.getConnection();
/* 392 */     if (conn != null && keepAlive == null) {
/* 393 */       conn.close(CloseMode.GRACEFUL);
/*     */     }
/* 395 */     boolean reusable = (conn != null && conn.isOpen() && conn.isConsistent());
/*     */     try {
/* 397 */       if (reusable) {
/* 398 */         entry.updateState(state);
/* 399 */         entry.updateExpiry(keepAlive);
/* 400 */         conn.passivate();
/* 401 */         if (LOG.isDebugEnabled()) {
/*     */           String s;
/* 403 */           if (TimeValue.isPositive(keepAlive)) {
/* 404 */             s = "for " + keepAlive;
/*     */           } else {
/* 406 */             s = "indefinitely";
/*     */           } 
/* 408 */           LOG.debug("{} connection {} can be kept alive {}", new Object[] { ConnPoolSupport.getId(endpoint), ConnPoolSupport.getId(conn), s });
/*     */         }
/*     */       
/* 411 */       } else if (LOG.isDebugEnabled()) {
/* 412 */         LOG.debug("{} connection is not kept alive", ConnPoolSupport.getId(endpoint));
/*     */       }
/*     */     
/* 415 */     } catch (RuntimeException ex) {
/* 416 */       reusable = false;
/* 417 */       throw ex;
/*     */     } finally {
/* 419 */       this.pool.release(entry, reusable);
/* 420 */       if (LOG.isDebugEnabled()) {
/* 421 */         LOG.debug("{} connection released {}", ConnPoolSupport.getId(endpoint), ConnPoolSupport.formatStats((HttpRoute)entry.getRoute(), entry.getState(), (ConnPoolControl)this.pool));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect(ConnectionEndpoint endpoint, TimeValue timeout, HttpContext context) throws IOException {
/* 428 */     Args.notNull(endpoint, "Managed endpoint");
/* 429 */     InternalConnectionEndpoint internalEndpoint = cast(endpoint);
/* 430 */     if (internalEndpoint.isConnected()) {
/*     */       return;
/*     */     }
/* 433 */     PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry = internalEndpoint.getPoolEntry();
/* 434 */     if (!poolEntry.hasConnection()) {
/* 435 */       poolEntry.assignConnection((ModalCloseable)this.connFactory.createConnection(null));
/*     */     }
/* 437 */     HttpRoute route = (HttpRoute)poolEntry.getRoute();
/* 438 */     HttpHost host = (route.getProxyHost() != null) ? route.getProxyHost() : route.getTargetHost();
/* 439 */     SocketConfig socketConfig = resolveSocketConfig(route);
/* 440 */     ConnectionConfig connectionConfig = resolveConnectionConfig(route);
/* 441 */     TlsConfig tlsConfig = resolveTlsConfig(host);
/* 442 */     Timeout connectTimeout = (timeout != null) ? Timeout.of(timeout.getDuration(), timeout.getTimeUnit()) : connectionConfig.getConnectTimeout();
/* 443 */     if (LOG.isDebugEnabled()) {
/* 444 */       LOG.debug("{} connecting endpoint to {} ({})", new Object[] { ConnPoolSupport.getId(endpoint), host, connectTimeout });
/*     */     }
/* 446 */     ManagedHttpClientConnection conn = (ManagedHttpClientConnection)poolEntry.getConnection();
/* 447 */     this.connectionOperator.connect(conn, host, route
/*     */ 
/*     */         
/* 450 */         .getLocalSocketAddress(), connectTimeout, socketConfig, tlsConfig, context);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 455 */     if (LOG.isDebugEnabled()) {
/* 456 */       LOG.debug("{} connected {}", ConnPoolSupport.getId(endpoint), ConnPoolSupport.getId(conn));
/*     */     }
/* 458 */     Timeout socketTimeout = connectionConfig.getSocketTimeout();
/* 459 */     if (socketTimeout != null) {
/* 460 */       conn.setSocketTimeout(socketTimeout);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void upgrade(ConnectionEndpoint endpoint, HttpContext context) throws IOException {
/* 466 */     Args.notNull(endpoint, "Managed endpoint");
/* 467 */     InternalConnectionEndpoint internalEndpoint = cast(endpoint);
/* 468 */     PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry = internalEndpoint.getValidatedPoolEntry();
/* 469 */     HttpRoute route = (HttpRoute)poolEntry.getRoute();
/* 470 */     HttpHost host = (route.getProxyHost() != null) ? route.getProxyHost() : route.getTargetHost();
/* 471 */     TlsConfig tlsConfig = resolveTlsConfig(host);
/* 472 */     this.connectionOperator.upgrade((ManagedHttpClientConnection)poolEntry.getConnection(), route.getTargetHost(), tlsConfig, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeIdle(TimeValue idleTime) {
/* 477 */     Args.notNull(idleTime, "Idle time");
/* 478 */     if (LOG.isDebugEnabled()) {
/* 479 */       LOG.debug("Closing connections idle longer than {}", idleTime);
/*     */     }
/* 481 */     this.pool.closeIdle(idleTime);
/*     */   }
/*     */ 
/*     */   
/*     */   public void closeExpired() {
/* 486 */     LOG.debug("Closing expired connections");
/* 487 */     this.pool.closeExpired();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<HttpRoute> getRoutes() {
/* 492 */     return this.pool.getRoutes();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTotal() {
/* 497 */     return this.pool.getMaxTotal();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxTotal(int max) {
/* 502 */     this.pool.setMaxTotal(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultMaxPerRoute() {
/* 507 */     return this.pool.getDefaultMaxPerRoute();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultMaxPerRoute(int max) {
/* 512 */     this.pool.setDefaultMaxPerRoute(max);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxPerRoute(HttpRoute route) {
/* 517 */     return this.pool.getMaxPerRoute(route);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMaxPerRoute(HttpRoute route, int max) {
/* 522 */     this.pool.setMaxPerRoute(route, max);
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getTotalStats() {
/* 527 */     return this.pool.getTotalStats();
/*     */   }
/*     */ 
/*     */   
/*     */   public PoolStats getStats(HttpRoute route) {
/* 532 */     return this.pool.getStats(route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultSocketConfig(SocketConfig config) {
/* 539 */     this.socketConfigResolver = (route -> config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSocketConfigResolver(Resolver<HttpRoute, SocketConfig> socketConfigResolver) {
/* 548 */     this.socketConfigResolver = socketConfigResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultConnectionConfig(ConnectionConfig config) {
/* 557 */     this.connectionConfigResolver = (route -> config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConnectionConfigResolver(Resolver<HttpRoute, ConnectionConfig> connectionConfigResolver) {
/* 566 */     this.connectionConfigResolver = connectionConfigResolver;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultTlsConfig(TlsConfig config) {
/* 575 */     this.tlsConfigResolver = (host -> config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTlsConfigResolver(Resolver<HttpHost, TlsConfig> tlsConfigResolver) {
/* 584 */     this.tlsConfigResolver = tlsConfigResolver;
/*     */   }
/*     */   
/*     */   void closeIfExpired(PoolEntry<HttpRoute, ManagedHttpClientConnection> entry) {
/* 588 */     long now = System.currentTimeMillis();
/* 589 */     if (entry.getExpiryDeadline().isBefore(now)) {
/* 590 */       entry.discardConnection(CloseMode.GRACEFUL);
/*     */     } else {
/* 592 */       ConnectionConfig connectionConfig = resolveConnectionConfig((HttpRoute)entry.getRoute());
/* 593 */       TimeValue timeToLive = connectionConfig.getTimeToLive();
/* 594 */       if (timeToLive != null && Deadline.calculate(entry.getCreated(), timeToLive).isBefore(now)) {
/* 595 */         entry.discardConnection(CloseMode.GRACEFUL);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SocketConfig getDefaultSocketConfig() {
/* 605 */     return SocketConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public TimeValue getValidateAfterInactivity() {
/* 615 */     return ConnectionConfig.DEFAULT.getValidateAfterInactivity();
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
/*     */   @Deprecated
/*     */   public void setValidateAfterInactivity(TimeValue validateAfterInactivity) {
/* 630 */     setDefaultConnectionConfig(ConnectionConfig.custom()
/* 631 */         .setValidateAfterInactivity(validateAfterInactivity)
/* 632 */         .build());
/*     */   }
/*     */   
/* 635 */   private static final PrefixedIncrementingId INCREMENTING_ID = new PrefixedIncrementingId("ep-");
/*     */   
/*     */   class InternalConnectionEndpoint
/*     */     extends ConnectionEndpoint
/*     */     implements Identifiable {
/*     */     private final AtomicReference<PoolEntry<HttpRoute, ManagedHttpClientConnection>> poolEntryRef;
/*     */     private final String id;
/*     */     
/*     */     InternalConnectionEndpoint(PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry) {
/* 644 */       this.poolEntryRef = new AtomicReference<>(poolEntry);
/* 645 */       this.id = PoolingHttpClientConnectionManager.INCREMENTING_ID.getNextId();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getId() {
/* 650 */       return this.id;
/*     */     }
/*     */     
/*     */     PoolEntry<HttpRoute, ManagedHttpClientConnection> getPoolEntry() {
/* 654 */       PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry = this.poolEntryRef.get();
/* 655 */       if (poolEntry == null) {
/* 656 */         throw new ConnectionShutdownException();
/*     */       }
/* 658 */       return poolEntry;
/*     */     }
/*     */     
/*     */     PoolEntry<HttpRoute, ManagedHttpClientConnection> getValidatedPoolEntry() {
/* 662 */       PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry = getPoolEntry();
/* 663 */       ManagedHttpClientConnection connection = (ManagedHttpClientConnection)poolEntry.getConnection();
/* 664 */       Asserts.check((connection != null && connection.isOpen()), "Endpoint is not connected");
/* 665 */       return poolEntry;
/*     */     }
/*     */     
/*     */     PoolEntry<HttpRoute, ManagedHttpClientConnection> detach() {
/* 669 */       return this.poolEntryRef.getAndSet(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close(CloseMode closeMode) {
/* 674 */       PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry = this.poolEntryRef.get();
/* 675 */       if (poolEntry != null) {
/* 676 */         poolEntry.discardConnection(closeMode);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 682 */       PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry = this.poolEntryRef.get();
/* 683 */       if (poolEntry != null) {
/* 684 */         poolEntry.discardConnection(CloseMode.GRACEFUL);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isConnected() {
/* 690 */       PoolEntry<HttpRoute, ManagedHttpClientConnection> poolEntry = getPoolEntry();
/* 691 */       ManagedHttpClientConnection connection = (ManagedHttpClientConnection)poolEntry.getConnection();
/* 692 */       return (connection != null && connection.isOpen());
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSocketTimeout(Timeout timeout) {
/* 697 */       ((ManagedHttpClientConnection)getValidatedPoolEntry().getConnection()).setSocketTimeout(timeout);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ClassicHttpResponse execute(String exchangeId, ClassicHttpRequest request, HttpRequestExecutor requestExecutor, HttpContext context) throws IOException, HttpException {
/* 706 */       Args.notNull(request, "HTTP request");
/* 707 */       Args.notNull(requestExecutor, "Request executor");
/* 708 */       ManagedHttpClientConnection connection = (ManagedHttpClientConnection)getValidatedPoolEntry().getConnection();
/* 709 */       if (PoolingHttpClientConnectionManager.LOG.isDebugEnabled()) {
/* 710 */         PoolingHttpClientConnectionManager.LOG.debug("{} executing exchange {} over {}", new Object[] { this.id, exchangeId, ConnPoolSupport.getId(connection) });
/*     */       }
/* 712 */       return requestExecutor.execute(request, (HttpClientConnection)connection, context);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/PoolingHttpClientConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */