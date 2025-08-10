/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.classic.ExecRuntime;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.ConnPoolSupport;
/*     */ import org.apache.hc.client5.http.io.ConnectionEndpoint;
/*     */ import org.apache.hc.client5.http.io.HttpClientConnectionManager;
/*     */ import org.apache.hc.client5.http.io.LeaseRequest;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ConnectionRequestTimeoutException;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.impl.io.HttpRequestExecutor;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ class InternalExecRuntime
/*     */   implements ExecRuntime, Cancellable
/*     */ {
/*     */   private final Logger log;
/*     */   private final HttpClientConnectionManager manager;
/*     */   private final HttpRequestExecutor requestExecutor;
/*     */   private final CancellableDependency cancellableDependency;
/*     */   private final AtomicReference<ConnectionEndpoint> endpointRef;
/*     */   private volatile boolean reusable;
/*     */   private volatile Object state;
/*     */   private volatile TimeValue validDuration;
/*     */   
/*     */   InternalExecRuntime(Logger log, HttpClientConnectionManager manager, HttpRequestExecutor requestExecutor, CancellableDependency cancellableDependency) {
/*  75 */     this.log = log;
/*  76 */     this.manager = manager;
/*  77 */     this.requestExecutor = requestExecutor;
/*  78 */     this.cancellableDependency = cancellableDependency;
/*  79 */     this.endpointRef = new AtomicReference<>();
/*  80 */     this.validDuration = TimeValue.NEG_ONE_MILLISECOND;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExecutionAborted() {
/*  85 */     return (this.cancellableDependency != null && this.cancellableDependency.isCancelled());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndpointAcquired() {
/*  90 */     return (this.endpointRef.get() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void acquireEndpoint(String id, HttpRoute route, Object object, HttpClientContext context) throws IOException {
/*  96 */     Args.notNull(route, "Route");
/*  97 */     if (this.endpointRef.get() == null) {
/*  98 */       RequestConfig requestConfig = context.getRequestConfig();
/*  99 */       Timeout connectionRequestTimeout = requestConfig.getConnectionRequestTimeout();
/* 100 */       if (this.log.isDebugEnabled()) {
/* 101 */         this.log.debug("{} acquiring endpoint ({})", id, connectionRequestTimeout);
/*     */       }
/* 103 */       LeaseRequest connRequest = this.manager.lease(id, route, connectionRequestTimeout, object);
/* 104 */       this.state = object;
/* 105 */       if (this.cancellableDependency != null) {
/* 106 */         this.cancellableDependency.setDependency((Cancellable)connRequest);
/*     */       }
/*     */       try {
/* 109 */         ConnectionEndpoint connectionEndpoint = connRequest.get(connectionRequestTimeout);
/* 110 */         this.endpointRef.set(connectionEndpoint);
/* 111 */         this.reusable = connectionEndpoint.isConnected();
/* 112 */         if (this.cancellableDependency != null) {
/* 113 */           this.cancellableDependency.setDependency(this);
/*     */         }
/* 115 */         if (this.log.isDebugEnabled()) {
/* 116 */           this.log.debug("{} acquired endpoint {}", id, ConnPoolSupport.getId(connectionEndpoint));
/*     */         }
/* 118 */       } catch (TimeoutException ex) {
/* 119 */         connRequest.cancel();
/* 120 */         throw new ConnectionRequestTimeoutException(ex.getMessage());
/* 121 */       } catch (InterruptedException interrupted) {
/* 122 */         connRequest.cancel();
/* 123 */         Thread.currentThread().interrupt();
/* 124 */         throw new RequestFailedException("Request aborted", interrupted);
/* 125 */       } catch (ExecutionException ex) {
/* 126 */         connRequest.cancel();
/* 127 */         Throwable cause = ex.getCause();
/* 128 */         if (cause == null) {
/* 129 */           cause = ex;
/*     */         }
/* 131 */         throw new RequestFailedException("Request execution failed", cause);
/*     */       } 
/*     */     } else {
/* 134 */       throw new IllegalStateException("Endpoint already acquired");
/*     */     } 
/*     */   }
/*     */   
/*     */   ConnectionEndpoint ensureValid() {
/* 139 */     ConnectionEndpoint endpoint = this.endpointRef.get();
/* 140 */     if (endpoint == null) {
/* 141 */       throw new IllegalStateException("Endpoint not acquired / already released");
/*     */     }
/* 143 */     return endpoint;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndpointConnected() {
/* 148 */     ConnectionEndpoint endpoint = this.endpointRef.get();
/* 149 */     return (endpoint != null && endpoint.isConnected());
/*     */   }
/*     */   
/*     */   private void connectEndpoint(ConnectionEndpoint endpoint, HttpClientContext context) throws IOException {
/* 153 */     if (isExecutionAborted()) {
/* 154 */       throw new RequestFailedException("Request aborted");
/*     */     }
/* 156 */     RequestConfig requestConfig = context.getRequestConfig();
/*     */     
/* 158 */     Timeout connectTimeout = requestConfig.getConnectTimeout();
/* 159 */     if (this.log.isDebugEnabled()) {
/* 160 */       this.log.debug("{} connecting endpoint ({})", ConnPoolSupport.getId(endpoint), connectTimeout);
/*     */     }
/* 162 */     this.manager.connect(endpoint, (TimeValue)connectTimeout, (HttpContext)context);
/* 163 */     if (this.log.isDebugEnabled()) {
/* 164 */       this.log.debug("{} endpoint connected", ConnPoolSupport.getId(endpoint));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void connectEndpoint(HttpClientContext context) throws IOException {
/* 170 */     ConnectionEndpoint endpoint = ensureValid();
/* 171 */     if (!endpoint.isConnected()) {
/* 172 */       connectEndpoint(endpoint, context);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnectEndpoint() throws IOException {
/* 178 */     ConnectionEndpoint endpoint = this.endpointRef.get();
/* 179 */     if (endpoint != null) {
/* 180 */       endpoint.close();
/* 181 */       if (this.log.isDebugEnabled()) {
/* 182 */         this.log.debug("{} endpoint closed", ConnPoolSupport.getId(endpoint));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void upgradeTls(HttpClientContext context) throws IOException {
/* 189 */     ConnectionEndpoint endpoint = ensureValid();
/* 190 */     if (this.log.isDebugEnabled()) {
/* 191 */       this.log.debug("{} upgrading endpoint", ConnPoolSupport.getId(endpoint));
/*     */     }
/* 193 */     this.manager.upgrade(endpoint, (HttpContext)context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(String id, ClassicHttpRequest request, HttpClientContext context) throws IOException, HttpException {
/* 201 */     ConnectionEndpoint endpoint = ensureValid();
/* 202 */     if (!endpoint.isConnected()) {
/* 203 */       connectEndpoint(endpoint, context);
/*     */     }
/* 205 */     if (isExecutionAborted()) {
/* 206 */       throw new RequestFailedException("Request aborted");
/*     */     }
/* 208 */     RequestConfig requestConfig = context.getRequestConfig();
/* 209 */     Timeout responseTimeout = requestConfig.getResponseTimeout();
/* 210 */     if (responseTimeout != null) {
/* 211 */       endpoint.setSocketTimeout(responseTimeout);
/*     */     }
/* 213 */     if (this.log.isDebugEnabled()) {
/* 214 */       this.log.debug("{} start execution {}", ConnPoolSupport.getId(endpoint), id);
/*     */     }
/* 216 */     return endpoint.execute(id, request, this.requestExecutor, (HttpContext)context);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConnectionReusable() {
/* 221 */     return this.reusable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markConnectionReusable(Object state, TimeValue validDuration) {
/* 226 */     this.reusable = true;
/* 227 */     this.state = state;
/* 228 */     this.validDuration = validDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markConnectionNonReusable() {
/* 233 */     this.reusable = false;
/*     */   }
/*     */   
/*     */   private void discardEndpoint(ConnectionEndpoint endpoint) {
/*     */     try {
/* 238 */       endpoint.close(CloseMode.IMMEDIATE);
/* 239 */       if (this.log.isDebugEnabled()) {
/* 240 */         this.log.debug("{} endpoint closed", ConnPoolSupport.getId(endpoint));
/*     */       }
/*     */     } finally {
/* 243 */       if (this.log.isDebugEnabled()) {
/* 244 */         this.log.debug("{} discarding endpoint", ConnPoolSupport.getId(endpoint));
/*     */       }
/* 246 */       this.manager.release(endpoint, null, TimeValue.ZERO_MILLISECONDS);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseEndpoint() {
/* 252 */     ConnectionEndpoint endpoint = this.endpointRef.getAndSet(null);
/* 253 */     if (endpoint != null) {
/* 254 */       if (this.reusable) {
/* 255 */         if (this.log.isDebugEnabled()) {
/* 256 */           this.log.debug("{} releasing valid endpoint", ConnPoolSupport.getId(endpoint));
/*     */         }
/* 258 */         this.manager.release(endpoint, this.state, this.validDuration);
/*     */       } else {
/* 260 */         discardEndpoint(endpoint);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void discardEndpoint() {
/* 267 */     ConnectionEndpoint endpoint = this.endpointRef.getAndSet(null);
/* 268 */     if (endpoint != null) {
/* 269 */       discardEndpoint(endpoint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel() {
/* 275 */     boolean alreadyReleased = (this.endpointRef.get() == null);
/* 276 */     ConnectionEndpoint endpoint = this.endpointRef.getAndSet(null);
/* 277 */     if (endpoint != null) {
/* 278 */       if (this.log.isDebugEnabled()) {
/* 279 */         this.log.debug("{} cancel", ConnPoolSupport.getId(endpoint));
/*     */       }
/* 281 */       discardEndpoint(endpoint);
/*     */     } 
/* 283 */     return !alreadyReleased;
/*     */   }
/*     */ 
/*     */   
/*     */   public ExecRuntime fork(CancellableDependency cancellableDependency) {
/* 288 */     return new InternalExecRuntime(this.log, this.manager, this.requestExecutor, cancellableDependency);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/InternalExecRuntime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */