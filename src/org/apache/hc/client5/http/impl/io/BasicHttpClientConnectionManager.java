/*     */ package org.apache.hc.client5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.time.Instant;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.client5.http.DnsResolver;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.SchemePortResolver;
/*     */ import org.apache.hc.client5.http.config.ConnectionConfig;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.impl.ConnPoolSupport;
/*     */ import org.apache.hc.client5.http.impl.ConnectionShutdownException;
/*     */ import org.apache.hc.client5.http.io.ConnectionEndpoint;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionOperator;
/*     */ import org.apache.hc.client5.http.io.LeaseRequest;
/*     */ import org.apache.hc.client5.http.io.ManagedHttpClientConnection;
/*     */ import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
/*     */ import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ import org.apache.hc.core5.util.Deadline;
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
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.SAFE)
/*     */ public class BasicHttpClientConnectionManager
/*     */   implements HttpClientConnectionManager
/*     */ {
/*  98 */   private static final Logger LOG = LoggerFactory.getLogger(BasicHttpClientConnectionManager.class);
/*     */   
/* 100 */   private static final AtomicLong COUNT = new AtomicLong(0L);
/*     */   
/*     */   private final HttpClientConnectionOperator connectionOperator;
/*     */   
/*     */   private final HttpConnectionFactory<ManagedHttpClientConnection> connFactory;
/*     */   
/*     */   private final String id;
/*     */   private ManagedHttpClientConnection conn;
/*     */   private HttpRoute route;
/*     */   private Object state;
/*     */   private long created;
/*     */   private long updated;
/*     */   private long expiry;
/*     */   private boolean leased;
/*     */   private SocketConfig socketConfig;
/*     */   private ConnectionConfig connectionConfig;
/*     */   private TlsConfig tlsConfig;
/*     */   private final AtomicBoolean closed;
/*     */   
/*     */   private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
/* 120 */     return RegistryBuilder.create()
/* 121 */       .register(URIScheme.HTTP.id, PlainConnectionSocketFactory.getSocketFactory())
/* 122 */       .register(URIScheme.HTTPS.id, SSLConnectionSocketFactory.getSocketFactory())
/* 123 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<ManagedHttpClientConnection> connFactory, SchemePortResolver schemePortResolver, DnsResolver dnsResolver) {
/* 131 */     this(new DefaultHttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver), connFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
/* 142 */     this.connectionOperator = (HttpClientConnectionOperator)Args.notNull(httpClientConnectionOperator, "Connection operator");
/* 143 */     this.connFactory = (connFactory != null) ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE;
/* 144 */     this.id = String.format("ep-%010d", new Object[] { Long.valueOf(COUNT.getAndIncrement()) });
/* 145 */     this.expiry = Long.MAX_VALUE;
/* 146 */     this.socketConfig = SocketConfig.DEFAULT;
/* 147 */     this.connectionConfig = ConnectionConfig.DEFAULT;
/* 148 */     this.tlsConfig = TlsConfig.DEFAULT;
/* 149 */     this.closed = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<ManagedHttpClientConnection> connFactory) {
/* 155 */     this(socketFactoryRegistry, connFactory, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry) {
/* 160 */     this(socketFactoryRegistry, null, null, null);
/*     */   }
/*     */   
/*     */   public BasicHttpClientConnectionManager() {
/* 164 */     this((Lookup<ConnectionSocketFactory>)getDefaultRegistry(), null, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 169 */     close(CloseMode.GRACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(CloseMode closeMode) {
/* 174 */     if (this.closed.compareAndSet(false, true)) {
/* 175 */       closeConnection(closeMode);
/*     */     }
/*     */   }
/*     */   
/*     */   HttpRoute getRoute() {
/* 180 */     return this.route;
/*     */   }
/*     */   
/*     */   Object getState() {
/* 184 */     return this.state;
/*     */   }
/*     */   
/*     */   public synchronized SocketConfig getSocketConfig() {
/* 188 */     return this.socketConfig;
/*     */   }
/*     */   
/*     */   public synchronized void setSocketConfig(SocketConfig socketConfig) {
/* 192 */     this.socketConfig = (socketConfig != null) ? socketConfig : SocketConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized ConnectionConfig getConnectionConfig() {
/* 199 */     return this.connectionConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setConnectionConfig(ConnectionConfig connectionConfig) {
/* 206 */     this.connectionConfig = (connectionConfig != null) ? connectionConfig : ConnectionConfig.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized TlsConfig getTlsConfig() {
/* 213 */     return this.tlsConfig;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setTlsConfig(TlsConfig tlsConfig) {
/* 220 */     this.tlsConfig = (tlsConfig != null) ? tlsConfig : TlsConfig.DEFAULT;
/*     */   }
/*     */   
/*     */   public LeaseRequest lease(String id, HttpRoute route, Object state) {
/* 224 */     return lease(id, route, Timeout.DISABLED, state);
/*     */   }
/*     */ 
/*     */   
/*     */   public LeaseRequest lease(String id, final HttpRoute route, Timeout requestTimeout, final Object state) {
/* 229 */     return new LeaseRequest()
/*     */       {
/*     */         
/*     */         public ConnectionEndpoint get(Timeout timeout) throws InterruptedException, ExecutionException, TimeoutException
/*     */         {
/*     */           try {
/* 235 */             return new BasicHttpClientConnectionManager.InternalConnectionEndpoint(route, BasicHttpClientConnectionManager.this.getConnection(route, state));
/* 236 */           } catch (IOException ex) {
/* 237 */             throw new ExecutionException(ex.getMessage(), ex);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean cancel() {
/* 243 */           return false;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void closeConnection(CloseMode closeMode) {
/* 250 */     if (this.conn != null) {
/* 251 */       if (LOG.isDebugEnabled()) {
/* 252 */         LOG.debug("{} Closing connection {}", this.id, closeMode);
/*     */       }
/* 254 */       this.conn.close(closeMode);
/* 255 */       this.conn = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkExpiry() {
/* 260 */     if (this.conn != null && System.currentTimeMillis() >= this.expiry) {
/* 261 */       if (LOG.isDebugEnabled()) {
/* 262 */         LOG.debug("{} Connection expired @ {}", this.id, Instant.ofEpochMilli(this.expiry));
/*     */       }
/* 264 */       closeConnection(CloseMode.GRACEFUL);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validate() {
/* 269 */     if (this.conn != null) {
/* 270 */       TimeValue timeToLive = this.connectionConfig.getTimeToLive();
/* 271 */       if (TimeValue.isNonNegative(timeToLive)) {
/* 272 */         Deadline deadline = Deadline.calculate(this.created, timeToLive);
/* 273 */         if (deadline.isExpired()) {
/* 274 */           closeConnection(CloseMode.GRACEFUL);
/*     */         }
/*     */       } 
/*     */     } 
/* 278 */     if (this.conn != null) {
/*     */       
/* 280 */       TimeValue timeValue = (this.connectionConfig.getValidateAfterInactivity() != null) ? this.connectionConfig.getValidateAfterInactivity() : TimeValue.ofSeconds(2L);
/* 281 */       if (TimeValue.isNonNegative(timeValue)) {
/* 282 */         Deadline deadline = Deadline.calculate(this.updated, timeValue);
/* 283 */         if (deadline.isExpired()) {
/*     */           boolean stale;
/*     */           try {
/* 286 */             stale = this.conn.isStale();
/* 287 */           } catch (IOException ignore) {
/* 288 */             stale = true;
/*     */           } 
/* 290 */           if (stale) {
/* 291 */             if (LOG.isDebugEnabled()) {
/* 292 */               LOG.debug("{} connection {} is stale", this.id, ConnPoolSupport.getId(this.conn));
/*     */             }
/* 294 */             closeConnection(CloseMode.GRACEFUL);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   synchronized ManagedHttpClientConnection getConnection(HttpRoute route, Object state) throws IOException {
/* 302 */     Asserts.check(!isClosed(), "Connection manager has been shut down");
/* 303 */     if (LOG.isDebugEnabled()) {
/* 304 */       LOG.debug("{} Get connection for route {}", this.id, route);
/*     */     }
/* 306 */     Asserts.check(!this.leased, "Connection %s is still allocated", this.conn);
/* 307 */     if (!Objects.equals(this.route, route) || !Objects.equals(this.state, state)) {
/* 308 */       closeConnection(CloseMode.GRACEFUL);
/*     */     }
/* 310 */     this.route = route;
/* 311 */     this.state = state;
/* 312 */     checkExpiry();
/* 313 */     validate();
/* 314 */     if (this.conn == null) {
/* 315 */       this.conn = (ManagedHttpClientConnection)this.connFactory.createConnection(null);
/* 316 */       this.created = System.currentTimeMillis();
/*     */     } else {
/* 318 */       this.conn.activate();
/*     */     } 
/* 320 */     this.leased = true;
/* 321 */     if (LOG.isDebugEnabled()) {
/* 322 */       LOG.debug("{} Using connection {}", this.id, this.conn);
/*     */     }
/* 324 */     return this.conn;
/*     */   }
/*     */   
/*     */   private InternalConnectionEndpoint cast(ConnectionEndpoint endpoint) {
/* 328 */     if (endpoint instanceof InternalConnectionEndpoint) {
/* 329 */       return (InternalConnectionEndpoint)endpoint;
/*     */     }
/* 331 */     throw new IllegalStateException("Unexpected endpoint class: " + endpoint.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void release(ConnectionEndpoint endpoint, Object state, TimeValue keepAlive) {
/* 336 */     Args.notNull(endpoint, "Managed endpoint");
/* 337 */     InternalConnectionEndpoint internalEndpoint = cast(endpoint);
/* 338 */     ManagedHttpClientConnection conn = internalEndpoint.detach();
/* 339 */     if (LOG.isDebugEnabled()) {
/* 340 */       LOG.debug("{} Releasing connection {}", this.id, conn);
/*     */     }
/* 342 */     if (isClosed()) {
/*     */       return;
/*     */     }
/*     */     try {
/* 346 */       if (keepAlive == null) {
/* 347 */         this.conn.close(CloseMode.GRACEFUL);
/*     */       }
/* 349 */       this.updated = System.currentTimeMillis();
/* 350 */       if (!this.conn.isOpen() && !this.conn.isConsistent()) {
/* 351 */         this.route = null;
/* 352 */         this.conn = null;
/* 353 */         this.expiry = Long.MAX_VALUE;
/* 354 */         if (LOG.isDebugEnabled()) {
/* 355 */           LOG.debug("{} Connection is not kept alive", this.id);
/*     */         }
/*     */       } else {
/* 358 */         this.state = state;
/* 359 */         if (conn != null) {
/* 360 */           conn.passivate();
/*     */         }
/* 362 */         if (TimeValue.isPositive(keepAlive)) {
/* 363 */           if (LOG.isDebugEnabled()) {
/* 364 */             LOG.debug("{} Connection can be kept alive for {}", this.id, keepAlive);
/*     */           }
/* 366 */           this.expiry = this.updated + keepAlive.toMilliseconds();
/*     */         } else {
/* 368 */           if (LOG.isDebugEnabled()) {
/* 369 */             LOG.debug("{} Connection can be kept alive indefinitely", this.id);
/*     */           }
/* 371 */           this.expiry = Long.MAX_VALUE;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 375 */       this.leased = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void connect(ConnectionEndpoint endpoint, TimeValue timeout, HttpContext context) throws IOException {
/*     */     HttpHost host;
/* 381 */     Args.notNull(endpoint, "Endpoint");
/*     */     
/* 383 */     InternalConnectionEndpoint internalEndpoint = cast(endpoint);
/* 384 */     if (internalEndpoint.isConnected()) {
/*     */       return;
/*     */     }
/* 387 */     HttpRoute route = internalEndpoint.getRoute();
/*     */     
/* 389 */     if (route.getProxyHost() != null) {
/* 390 */       host = route.getProxyHost();
/*     */     } else {
/* 392 */       host = route.getTargetHost();
/*     */     } 
/* 394 */     Timeout connectTimeout = (timeout != null) ? Timeout.of(timeout.getDuration(), timeout.getTimeUnit()) : this.connectionConfig.getConnectTimeout();
/* 395 */     ManagedHttpClientConnection connection = internalEndpoint.getConnection();
/* 396 */     if (LOG.isDebugEnabled()) {
/* 397 */       LOG.debug("{} connecting endpoint to {} ({})", new Object[] { ConnPoolSupport.getId(endpoint), host, connectTimeout });
/*     */     }
/* 399 */     this.connectionOperator.connect(connection, host, route
/*     */ 
/*     */         
/* 402 */         .getLocalSocketAddress(), connectTimeout, this.socketConfig, this.tlsConfig, context);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 407 */     if (LOG.isDebugEnabled()) {
/* 408 */       LOG.debug("{} connected {}", ConnPoolSupport.getId(endpoint), ConnPoolSupport.getId(this.conn));
/*     */     }
/* 410 */     Timeout socketTimeout = this.connectionConfig.getSocketTimeout();
/* 411 */     if (socketTimeout != null) {
/* 412 */       connection.setSocketTimeout(socketTimeout);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void upgrade(ConnectionEndpoint endpoint, HttpContext context) throws IOException {
/* 420 */     Args.notNull(endpoint, "Endpoint");
/* 421 */     Args.notNull(this.route, "HTTP route");
/* 422 */     InternalConnectionEndpoint internalEndpoint = cast(endpoint);
/* 423 */     this.connectionOperator.upgrade(internalEndpoint
/* 424 */         .getConnection(), internalEndpoint
/* 425 */         .getRoute().getTargetHost(), this.tlsConfig, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void closeExpired() {
/* 431 */     if (isClosed()) {
/*     */       return;
/*     */     }
/* 434 */     if (!this.leased) {
/* 435 */       checkExpiry();
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void closeIdle(TimeValue idleTime) {
/* 440 */     Args.notNull(idleTime, "Idle time");
/* 441 */     if (isClosed()) {
/*     */       return;
/*     */     }
/* 444 */     if (!this.leased) {
/* 445 */       long time = idleTime.toMilliseconds();
/* 446 */       if (time < 0L) {
/* 447 */         time = 0L;
/*     */       }
/* 449 */       long deadline = System.currentTimeMillis() - time;
/* 450 */       if (this.updated <= deadline) {
/* 451 */         closeConnection(CloseMode.GRACEFUL);
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
/*     */   
/*     */   @Deprecated
/*     */   public TimeValue getValidateAfterInactivity() {
/* 465 */     return this.connectionConfig.getValidateAfterInactivity();
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
/* 480 */     this
/*     */       
/* 482 */       .connectionConfig = ConnectionConfig.custom().setValidateAfterInactivity(validateAfterInactivity).build();
/*     */   }
/*     */   
/*     */   class InternalConnectionEndpoint
/*     */     extends ConnectionEndpoint {
/*     */     private final HttpRoute route;
/*     */     private final AtomicReference<ManagedHttpClientConnection> connRef;
/*     */     
/*     */     public InternalConnectionEndpoint(HttpRoute route, ManagedHttpClientConnection conn) {
/* 491 */       this.route = route;
/* 492 */       this.connRef = new AtomicReference<>(conn);
/*     */     }
/*     */     
/*     */     HttpRoute getRoute() {
/* 496 */       return this.route;
/*     */     }
/*     */     
/*     */     ManagedHttpClientConnection getConnection() {
/* 500 */       ManagedHttpClientConnection conn = this.connRef.get();
/* 501 */       if (conn == null) {
/* 502 */         throw new ConnectionShutdownException();
/*     */       }
/* 504 */       return conn;
/*     */     }
/*     */     
/*     */     ManagedHttpClientConnection getValidatedConnection() {
/* 508 */       ManagedHttpClientConnection conn = getConnection();
/* 509 */       Asserts.check(conn.isOpen(), "Endpoint is not connected");
/* 510 */       return conn;
/*     */     }
/*     */     
/*     */     ManagedHttpClientConnection detach() {
/* 514 */       return this.connRef.getAndSet(null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isConnected() {
/* 519 */       ManagedHttpClientConnection conn = getConnection();
/* 520 */       return (conn != null && conn.isOpen());
/*     */     }
/*     */ 
/*     */     
/*     */     public void close(CloseMode closeMode) {
/* 525 */       ManagedHttpClientConnection conn = detach();
/* 526 */       if (conn != null) {
/* 527 */         conn.close(closeMode);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 533 */       ManagedHttpClientConnection conn = detach();
/* 534 */       if (conn != null) {
/* 535 */         conn.close();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSocketTimeout(Timeout timeout) {
/* 541 */       getValidatedConnection().setSocketTimeout(timeout);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ClassicHttpResponse execute(String exchangeId, ClassicHttpRequest request, HttpRequestExecutor requestExecutor, HttpContext context) throws IOException, HttpException {
/* 550 */       Args.notNull(request, "HTTP request");
/* 551 */       Args.notNull(requestExecutor, "Request executor");
/* 552 */       if (BasicHttpClientConnectionManager.LOG.isDebugEnabled()) {
/* 553 */         BasicHttpClientConnectionManager.LOG.debug("{} Executing exchange {}", BasicHttpClientConnectionManager.this.id, exchangeId);
/*     */       }
/* 555 */       return requestExecutor.execute(request, (HttpClientConnection)getValidatedConnection(), context);
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
/*     */   boolean isClosed() {
/* 568 */     return this.closed.get();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/io/BasicHttpClientConnectionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */