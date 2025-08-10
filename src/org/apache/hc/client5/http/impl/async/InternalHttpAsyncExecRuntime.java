/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.async.AsyncExecRuntime;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.config.TlsConfig;
/*     */ import org.apache.hc.client5.http.impl.ConnPoolSupport;
/*     */ import org.apache.hc.client5.http.impl.Operations;
/*     */ import org.apache.hc.client5.http.nio.AsyncClientConnectionManager;
/*     */ import org.apache.hc.client5.http.nio.AsyncConnectionEndpoint;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.ConnectionInitiator;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class InternalHttpAsyncExecRuntime
/*     */   implements AsyncExecRuntime
/*     */ {
/*     */   private final Logger log;
/*     */   private final AsyncClientConnectionManager manager;
/*     */   private final ConnectionInitiator connectionInitiator;
/*     */   private final HandlerFactory<AsyncPushConsumer> pushHandlerFactory;
/*     */   @Deprecated
/*     */   private final TlsConfig tlsConfig;
/*     */   private final AtomicReference<AsyncConnectionEndpoint> endpointRef;
/*     */   private volatile boolean reusable;
/*     */   private volatile Object state;
/*     */   private volatile TimeValue validDuration;
/*     */   
/*     */   InternalHttpAsyncExecRuntime(Logger log, AsyncClientConnectionManager manager, ConnectionInitiator connectionInitiator, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, TlsConfig tlsConfig) {
/*  77 */     this.log = log;
/*  78 */     this.manager = manager;
/*  79 */     this.connectionInitiator = connectionInitiator;
/*  80 */     this.pushHandlerFactory = pushHandlerFactory;
/*  81 */     this.tlsConfig = tlsConfig;
/*  82 */     this.endpointRef = new AtomicReference<>();
/*  83 */     this.validDuration = TimeValue.NEG_ONE_MILLISECOND;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndpointAcquired() {
/*  88 */     return (this.endpointRef.get() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable acquireEndpoint(final String id, HttpRoute route, Object object, HttpClientContext context, final FutureCallback<AsyncExecRuntime> callback) {
/*  98 */     if (this.endpointRef.get() == null) {
/*  99 */       this.state = object;
/* 100 */       RequestConfig requestConfig = context.getRequestConfig();
/* 101 */       Timeout connectionRequestTimeout = requestConfig.getConnectionRequestTimeout();
/* 102 */       if (this.log.isDebugEnabled()) {
/* 103 */         this.log.debug("{} acquiring endpoint ({})", id, connectionRequestTimeout);
/*     */       }
/* 105 */       return Operations.cancellable(this.manager.lease(id, route, object, connectionRequestTimeout, new FutureCallback<AsyncConnectionEndpoint>()
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public void completed(AsyncConnectionEndpoint connectionEndpoint)
/*     */               {
/* 114 */                 InternalHttpAsyncExecRuntime.this.endpointRef.set(connectionEndpoint);
/* 115 */                 InternalHttpAsyncExecRuntime.this.reusable = connectionEndpoint.isConnected();
/* 116 */                 if (InternalHttpAsyncExecRuntime.this.log.isDebugEnabled()) {
/* 117 */                   InternalHttpAsyncExecRuntime.this.log.debug("{} acquired endpoint {}", id, ConnPoolSupport.getId(connectionEndpoint));
/*     */                 }
/* 119 */                 callback.completed(InternalHttpAsyncExecRuntime.this);
/*     */               }
/*     */ 
/*     */               
/*     */               public void failed(Exception ex) {
/* 124 */                 callback.failed(ex);
/*     */               }
/*     */ 
/*     */               
/*     */               public void cancelled() {
/* 129 */                 callback.cancelled();
/*     */               }
/*     */             }));
/*     */     } 
/* 133 */     callback.completed(this);
/* 134 */     return Operations.nonCancellable();
/*     */   }
/*     */   
/*     */   private void discardEndpoint(AsyncConnectionEndpoint endpoint) {
/*     */     try {
/* 139 */       endpoint.close(CloseMode.IMMEDIATE);
/* 140 */       if (this.log.isDebugEnabled()) {
/* 141 */         this.log.debug("{} endpoint closed", ConnPoolSupport.getId(endpoint));
/*     */       }
/*     */     } finally {
/* 144 */       if (this.log.isDebugEnabled()) {
/* 145 */         this.log.debug("{} discarding endpoint", ConnPoolSupport.getId(endpoint));
/*     */       }
/* 147 */       this.manager.release(endpoint, null, TimeValue.ZERO_MILLISECONDS);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseEndpoint() {
/* 153 */     AsyncConnectionEndpoint endpoint = this.endpointRef.getAndSet(null);
/* 154 */     if (endpoint != null) {
/* 155 */       if (this.reusable) {
/* 156 */         if (this.log.isDebugEnabled()) {
/* 157 */           this.log.debug("{} releasing valid endpoint", ConnPoolSupport.getId(endpoint));
/*     */         }
/* 159 */         this.manager.release(endpoint, this.state, this.validDuration);
/*     */       } else {
/* 161 */         discardEndpoint(endpoint);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void discardEndpoint() {
/* 168 */     AsyncConnectionEndpoint endpoint = this.endpointRef.getAndSet(null);
/* 169 */     if (endpoint != null) {
/* 170 */       discardEndpoint(endpoint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean validateConnection() {
/* 176 */     if (this.reusable) {
/* 177 */       AsyncConnectionEndpoint asyncConnectionEndpoint = this.endpointRef.get();
/* 178 */       return (asyncConnectionEndpoint != null && asyncConnectionEndpoint.isConnected());
/*     */     } 
/* 180 */     AsyncConnectionEndpoint endpoint = this.endpointRef.getAndSet(null);
/* 181 */     if (endpoint != null) {
/* 182 */       discardEndpoint(endpoint);
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */   
/*     */   AsyncConnectionEndpoint ensureValid() {
/* 188 */     AsyncConnectionEndpoint endpoint = this.endpointRef.get();
/* 189 */     if (endpoint == null) {
/* 190 */       throw new IllegalStateException("Endpoint not acquired / already released");
/*     */     }
/* 192 */     return endpoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndpointConnected() {
/* 197 */     AsyncConnectionEndpoint endpoint = this.endpointRef.get();
/* 198 */     return (endpoint != null && endpoint.isConnected());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable connectEndpoint(HttpClientContext context, final FutureCallback<AsyncExecRuntime> callback) {
/* 205 */     AsyncConnectionEndpoint endpoint = ensureValid();
/* 206 */     if (endpoint.isConnected()) {
/* 207 */       callback.completed(this);
/* 208 */       return Operations.nonCancellable();
/*     */     } 
/* 210 */     RequestConfig requestConfig = context.getRequestConfig();
/*     */     
/* 212 */     Timeout connectTimeout = requestConfig.getConnectTimeout();
/* 213 */     if (this.log.isDebugEnabled()) {
/* 214 */       this.log.debug("{} connecting endpoint ({})", ConnPoolSupport.getId(endpoint), connectTimeout);
/*     */     }
/* 216 */     return Operations.cancellable(this.manager.connect(endpoint, this.connectionInitiator, connectTimeout, this.tlsConfig, (HttpContext)context, (FutureCallback)new CallbackContribution<AsyncConnectionEndpoint>(callback)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             public void completed(AsyncConnectionEndpoint endpoint)
/*     */             {
/* 226 */               if (InternalHttpAsyncExecRuntime.this.log.isDebugEnabled()) {
/* 227 */                 InternalHttpAsyncExecRuntime.this.log.debug("{} endpoint connected", ConnPoolSupport.getId(endpoint));
/*     */               }
/* 229 */               if (callback != null) {
/* 230 */                 callback.completed(InternalHttpAsyncExecRuntime.this);
/*     */               }
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgradeTls(HttpClientContext context) {
/* 240 */     upgradeTls(context, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void upgradeTls(HttpClientContext context, final FutureCallback<AsyncExecRuntime> callback) {
/* 245 */     AsyncConnectionEndpoint endpoint = ensureValid();
/* 246 */     if (this.log.isDebugEnabled()) {
/* 247 */       this.log.debug("{} upgrading endpoint", ConnPoolSupport.getId(endpoint));
/*     */     }
/* 249 */     this.manager.upgrade(endpoint, this.tlsConfig, (HttpContext)context, (FutureCallback)new CallbackContribution<AsyncConnectionEndpoint>(callback)
/*     */         {
/*     */           public void completed(AsyncConnectionEndpoint endpoint)
/*     */           {
/* 253 */             if (callback != null) {
/* 254 */               callback.completed(InternalHttpAsyncExecRuntime.this);
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable execute(final String id, final AsyncClientExchangeHandler exchangeHandler, final HttpClientContext context) {
/* 264 */     final AsyncConnectionEndpoint endpoint = ensureValid();
/* 265 */     if (endpoint.isConnected()) {
/* 266 */       if (this.log.isDebugEnabled()) {
/* 267 */         this.log.debug("{} start execution {}", ConnPoolSupport.getId(endpoint), id);
/*     */       }
/* 269 */       RequestConfig requestConfig = context.getRequestConfig();
/* 270 */       Timeout responseTimeout = requestConfig.getResponseTimeout();
/* 271 */       if (responseTimeout != null) {
/* 272 */         endpoint.setSocketTimeout(responseTimeout);
/*     */       }
/* 274 */       endpoint.execute(id, exchangeHandler, (HttpContext)context);
/* 275 */       if (context.getRequestConfig().isHardCancellationEnabled()) {
/* 276 */         return () -> {
/*     */             exchangeHandler.cancel();
/*     */             return true;
/*     */           };
/*     */       }
/*     */     } else {
/* 282 */       connectEndpoint(context, new FutureCallback<AsyncExecRuntime>()
/*     */           {
/*     */             public void completed(AsyncExecRuntime runtime)
/*     */             {
/* 286 */               if (InternalHttpAsyncExecRuntime.this.log.isDebugEnabled()) {
/* 287 */                 InternalHttpAsyncExecRuntime.this.log.debug("{} start execution {}", ConnPoolSupport.getId(endpoint), id);
/*     */               }
/*     */               try {
/* 290 */                 endpoint.execute(id, exchangeHandler, InternalHttpAsyncExecRuntime.this.pushHandlerFactory, (HttpContext)context);
/* 291 */               } catch (RuntimeException ex) {
/* 292 */                 failed(ex);
/*     */               } 
/*     */             }
/*     */ 
/*     */             
/*     */             public void failed(Exception ex) {
/* 298 */               exchangeHandler.failed(ex);
/*     */             }
/*     */ 
/*     */             
/*     */             public void cancelled() {
/* 303 */               exchangeHandler.failed(new InterruptedIOException());
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 308 */     return Operations.nonCancellable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void markConnectionReusable(Object newState, TimeValue newValidDuration) {
/* 313 */     this.reusable = true;
/* 314 */     this.state = newState;
/* 315 */     this.validDuration = newValidDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markConnectionNonReusable() {
/* 320 */     this.reusable = false;
/* 321 */     this.state = null;
/* 322 */     this.validDuration = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncExecRuntime fork() {
/* 327 */     return new InternalHttpAsyncExecRuntime(this.log, this.manager, this.connectionInitiator, this.pushHandlerFactory, this.tlsConfig);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/InternalHttpAsyncExecRuntime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */