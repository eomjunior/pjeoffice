/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.async.AsyncExecRuntime;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.impl.ConnPoolSupport;
/*     */ import org.apache.hc.client5.http.impl.Operations;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.concurrent.Cancellable;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.concurrent.ComplexCancellable;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.command.RequestExecutionCommand;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.io.CloseMode;
/*     */ import org.apache.hc.core5.reactor.Command;
/*     */ import org.apache.hc.core5.reactor.IOSession;
/*     */ import org.apache.hc.core5.util.Identifiable;
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
/*     */ class InternalH2AsyncExecRuntime
/*     */   implements AsyncExecRuntime
/*     */ {
/*     */   private final Logger log;
/*     */   private final InternalH2ConnPool connPool;
/*     */   private final HandlerFactory<AsyncPushConsumer> pushHandlerFactory;
/*     */   private final AtomicReference<Endpoint> sessionRef;
/*     */   private volatile boolean reusable;
/*     */   
/*     */   InternalH2AsyncExecRuntime(Logger log, InternalH2ConnPool connPool, HandlerFactory<AsyncPushConsumer> pushHandlerFactory) {
/*  69 */     this.log = log;
/*  70 */     this.connPool = connPool;
/*  71 */     this.pushHandlerFactory = pushHandlerFactory;
/*  72 */     this.sessionRef = new AtomicReference<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndpointAcquired() {
/*  77 */     return (this.sessionRef.get() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable acquireEndpoint(final String id, HttpRoute route, Object object, HttpClientContext context, final FutureCallback<AsyncExecRuntime> callback) {
/*  87 */     if (this.sessionRef.get() == null) {
/*  88 */       final HttpHost target = route.getTargetHost();
/*  89 */       RequestConfig requestConfig = context.getRequestConfig();
/*     */       
/*  91 */       Timeout connectTimeout = requestConfig.getConnectTimeout();
/*  92 */       if (this.log.isDebugEnabled()) {
/*  93 */         this.log.debug("{} acquiring endpoint ({})", id, connectTimeout);
/*     */       }
/*  95 */       return Operations.cancellable(this.connPool.getSession(target, connectTimeout, new FutureCallback<IOSession>()
/*     */             {
/*     */               
/*     */               public void completed(IOSession ioSession)
/*     */               {
/* 100 */                 InternalH2AsyncExecRuntime.this.sessionRef.set(new InternalH2AsyncExecRuntime.Endpoint(target, ioSession));
/* 101 */                 InternalH2AsyncExecRuntime.this.reusable = true;
/* 102 */                 if (InternalH2AsyncExecRuntime.this.log.isDebugEnabled()) {
/* 103 */                   InternalH2AsyncExecRuntime.this.log.debug("{} acquired endpoint", id);
/*     */                 }
/* 105 */                 callback.completed(InternalH2AsyncExecRuntime.this);
/*     */               }
/*     */ 
/*     */               
/*     */               public void failed(Exception ex) {
/* 110 */                 callback.failed(ex);
/*     */               }
/*     */ 
/*     */               
/*     */               public void cancelled() {
/* 115 */                 callback.cancelled();
/*     */               }
/*     */             }));
/*     */     } 
/*     */     
/* 120 */     callback.completed(this);
/* 121 */     return Operations.nonCancellable();
/*     */   }
/*     */   
/*     */   private void closeEndpoint(Endpoint endpoint) {
/* 125 */     endpoint.session.close(CloseMode.GRACEFUL);
/* 126 */     if (this.log.isDebugEnabled()) {
/* 127 */       this.log.debug("{} endpoint closed", ConnPoolSupport.getId(endpoint));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseEndpoint() {
/* 133 */     Endpoint endpoint = this.sessionRef.getAndSet(null);
/* 134 */     if (endpoint != null && !this.reusable) {
/* 135 */       closeEndpoint(endpoint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void discardEndpoint() {
/* 141 */     Endpoint endpoint = this.sessionRef.getAndSet(null);
/* 142 */     if (endpoint != null) {
/* 143 */       closeEndpoint(endpoint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean validateConnection() {
/* 149 */     if (this.reusable) {
/* 150 */       Endpoint endpoint1 = this.sessionRef.get();
/* 151 */       return (endpoint1 != null && endpoint1.session.isOpen());
/*     */     } 
/* 153 */     Endpoint endpoint = this.sessionRef.getAndSet(null);
/* 154 */     if (endpoint != null) {
/* 155 */       closeEndpoint(endpoint);
/*     */     }
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEndpointConnected() {
/* 162 */     Endpoint endpoint = this.sessionRef.get();
/* 163 */     return (endpoint != null && endpoint.session.isOpen());
/*     */   }
/*     */ 
/*     */   
/*     */   Endpoint ensureValid() {
/* 168 */     Endpoint endpoint = this.sessionRef.get();
/* 169 */     if (endpoint == null) {
/* 170 */       throw new IllegalStateException("I/O session not acquired / already released");
/*     */     }
/* 172 */     return endpoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable connectEndpoint(HttpClientContext context, final FutureCallback<AsyncExecRuntime> callback) {
/* 179 */     final Endpoint endpoint = ensureValid();
/* 180 */     if (endpoint.session.isOpen()) {
/* 181 */       callback.completed(this);
/* 182 */       return Operations.nonCancellable();
/*     */     } 
/* 184 */     final HttpHost target = endpoint.target;
/* 185 */     RequestConfig requestConfig = context.getRequestConfig();
/*     */     
/* 187 */     Timeout connectTimeout = requestConfig.getConnectTimeout();
/* 188 */     if (this.log.isDebugEnabled()) {
/* 189 */       this.log.debug("{} connecting endpoint ({})", ConnPoolSupport.getId(endpoint), connectTimeout);
/*     */     }
/* 191 */     return Operations.cancellable(this.connPool.getSession(target, connectTimeout, new FutureCallback<IOSession>()
/*     */           {
/*     */             
/*     */             public void completed(IOSession ioSession)
/*     */             {
/* 196 */               InternalH2AsyncExecRuntime.this.sessionRef.set(new InternalH2AsyncExecRuntime.Endpoint(target, ioSession));
/* 197 */               InternalH2AsyncExecRuntime.this.reusable = true;
/* 198 */               if (InternalH2AsyncExecRuntime.this.log.isDebugEnabled()) {
/* 199 */                 InternalH2AsyncExecRuntime.this.log.debug("{} endpoint connected", ConnPoolSupport.getId(endpoint));
/*     */               }
/* 201 */               callback.completed(InternalH2AsyncExecRuntime.this);
/*     */             }
/*     */ 
/*     */             
/*     */             public void failed(Exception ex) {
/* 206 */               callback.failed(ex);
/*     */             }
/*     */ 
/*     */             
/*     */             public void cancelled() {
/* 211 */               callback.cancelled();
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void upgradeTls(HttpClientContext context) {
/* 220 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void upgradeTls(HttpClientContext context, FutureCallback<AsyncExecRuntime> callback) {
/* 225 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Cancellable execute(final String id, final AsyncClientExchangeHandler exchangeHandler, final HttpClientContext context) {
/* 232 */     final ComplexCancellable complexCancellable = new ComplexCancellable();
/* 233 */     final Endpoint endpoint = ensureValid();
/* 234 */     final IOSession session = endpoint.session;
/* 235 */     if (session.isOpen()) {
/* 236 */       if (this.log.isDebugEnabled()) {
/* 237 */         this.log.debug("{} start execution {}", ConnPoolSupport.getId(endpoint), id);
/*     */       }
/* 239 */       context.setProtocolVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 240 */       session.enqueue((Command)new RequestExecutionCommand(exchangeHandler, this.pushHandlerFactory, (CancellableDependency)complexCancellable, (HttpContext)context), Command.Priority.NORMAL);
/*     */     }
/*     */     else {
/*     */       
/* 244 */       final HttpHost target = endpoint.target;
/* 245 */       RequestConfig requestConfig = context.getRequestConfig();
/*     */       
/* 247 */       Timeout connectTimeout = requestConfig.getConnectTimeout();
/* 248 */       this.connPool.getSession(target, connectTimeout, new FutureCallback<IOSession>()
/*     */           {
/*     */             public void completed(IOSession ioSession)
/*     */             {
/* 252 */               InternalH2AsyncExecRuntime.this.sessionRef.set(new InternalH2AsyncExecRuntime.Endpoint(target, ioSession));
/* 253 */               InternalH2AsyncExecRuntime.this.reusable = true;
/* 254 */               if (InternalH2AsyncExecRuntime.this.log.isDebugEnabled()) {
/* 255 */                 InternalH2AsyncExecRuntime.this.log.debug("{} start execution {}", ConnPoolSupport.getId(endpoint), id);
/*     */               }
/* 257 */               context.setProtocolVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 258 */               session.enqueue((Command)new RequestExecutionCommand(exchangeHandler, InternalH2AsyncExecRuntime.this
/* 259 */                     .pushHandlerFactory, (CancellableDependency)complexCancellable, (HttpContext)context), Command.Priority.NORMAL);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public void failed(Exception ex) {
/* 265 */               exchangeHandler.failed(ex);
/*     */             }
/*     */ 
/*     */             
/*     */             public void cancelled() {
/* 270 */               exchangeHandler.failed(new InterruptedIOException());
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 275 */     return (Cancellable)complexCancellable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markConnectionReusable(Object newState, TimeValue newValidDuration) {
/* 280 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void markConnectionNonReusable() {
/* 285 */     this.reusable = false;
/*     */   }
/*     */   
/*     */   static class Endpoint
/*     */     implements Identifiable {
/*     */     final HttpHost target;
/*     */     final IOSession session;
/*     */     
/*     */     Endpoint(HttpHost target, IOSession session) {
/* 294 */       this.target = target;
/* 295 */       this.session = session;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getId() {
/* 300 */       return this.session.getId();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncExecRuntime fork() {
/* 307 */     return new InternalH2AsyncExecRuntime(this.log, this.connPool, this.pushHandlerFactory);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/InternalH2AsyncExecRuntime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */