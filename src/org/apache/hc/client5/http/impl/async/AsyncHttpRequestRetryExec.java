/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.client5.http.HttpRequestRetryStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.async.AsyncExecCallback;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChain;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChainHandler;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.entity.DiscardingEntityConsumer;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.support.BasicRequestBuilder;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public final class AsyncHttpRequestRetryExec
/*     */   implements AsyncExecChainHandler
/*     */ {
/*  71 */   private static final Logger LOG = LoggerFactory.getLogger(AsyncHttpRequestRetryExec.class);
/*     */   
/*     */   private final HttpRequestRetryStrategy retryStrategy;
/*     */   
/*     */   public AsyncHttpRequestRetryExec(HttpRequestRetryStrategy retryStrategy) {
/*  76 */     Args.notNull(retryStrategy, "retryStrategy");
/*  77 */     this.retryStrategy = retryStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class State
/*     */   {
/*     */     volatile boolean retrying;
/*     */ 
/*     */     
/*     */     volatile TimeValue delay;
/*     */ 
/*     */     
/*     */     private State() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private void internalExecute(final State state, final HttpRequest request, final AsyncEntityProducer entityProducer, final AsyncExecChain.Scope scope, AsyncExecChain chain, final AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/*  95 */     final String exchangeId = scope.exchangeId;
/*     */     
/*  97 */     chain.proceed((HttpRequest)BasicRequestBuilder.copy(request).build(), entityProducer, scope, new AsyncExecCallback()
/*     */         {
/*     */ 
/*     */           
/*     */           public AsyncDataConsumer handleResponse(HttpResponse response, EntityDetails entityDetails) throws HttpException, IOException
/*     */           {
/* 103 */             HttpClientContext clientContext = scope.clientContext;
/* 104 */             if (entityProducer != null && !entityProducer.isRepeatable()) {
/* 105 */               if (AsyncHttpRequestRetryExec.LOG.isDebugEnabled()) {
/* 106 */                 AsyncHttpRequestRetryExec.LOG.debug("{} cannot retry non-repeatable request", exchangeId);
/*     */               }
/* 108 */               return asyncExecCallback.handleResponse(response, entityDetails);
/*     */             } 
/* 110 */             state.retrying = AsyncHttpRequestRetryExec.this.retryStrategy.retryRequest(response, scope.execCount.get(), (HttpContext)clientContext);
/* 111 */             if (state.retrying) {
/* 112 */               state.delay = AsyncHttpRequestRetryExec.this.retryStrategy.getRetryInterval(response, scope.execCount.get(), (HttpContext)clientContext);
/* 113 */               if (AsyncHttpRequestRetryExec.LOG.isDebugEnabled()) {
/* 114 */                 AsyncHttpRequestRetryExec.LOG.debug("{} retrying request in {}", exchangeId, state.delay);
/*     */               }
/* 116 */               return (AsyncDataConsumer)new DiscardingEntityConsumer();
/*     */             } 
/* 118 */             return asyncExecCallback.handleResponse(response, entityDetails);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void handleInformationResponse(HttpResponse response) throws HttpException, IOException {
/* 124 */             asyncExecCallback.handleInformationResponse(response);
/*     */           }
/*     */ 
/*     */           
/*     */           public void completed() {
/* 129 */             if (state.retrying) {
/* 130 */               scope.execCount.incrementAndGet();
/* 131 */               if (entityProducer != null) {
/* 132 */                 entityProducer.releaseResources();
/*     */               }
/* 134 */               scope.scheduler.scheduleExecution(request, entityProducer, scope, asyncExecCallback, state.delay);
/*     */             } else {
/* 136 */               asyncExecCallback.completed();
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception cause) {
/* 142 */             if (cause instanceof IOException) {
/* 143 */               HttpRoute route = scope.route;
/* 144 */               HttpClientContext clientContext = scope.clientContext;
/* 145 */               if (entityProducer != null && !entityProducer.isRepeatable()) {
/* 146 */                 if (AsyncHttpRequestRetryExec.LOG.isDebugEnabled()) {
/* 147 */                   AsyncHttpRequestRetryExec.LOG.debug("{} cannot retry non-repeatable request", exchangeId);
/*     */                 }
/* 149 */               } else if (AsyncHttpRequestRetryExec.this.retryStrategy.retryRequest(request, (IOException)cause, scope.execCount.get(), (HttpContext)clientContext)) {
/* 150 */                 if (AsyncHttpRequestRetryExec.LOG.isDebugEnabled()) {
/* 151 */                   AsyncHttpRequestRetryExec.LOG.debug("{} {}", new Object[] { this.val$exchangeId, cause.getMessage(), cause });
/*     */                 }
/* 153 */                 if (AsyncHttpRequestRetryExec.LOG.isInfoEnabled()) {
/* 154 */                   AsyncHttpRequestRetryExec.LOG.info("Recoverable I/O exception ({}) caught when processing request to {}", cause
/* 155 */                       .getClass().getName(), route);
/*     */                 }
/* 157 */                 scope.execRuntime.discardEndpoint();
/* 158 */                 if (entityProducer != null) {
/* 159 */                   entityProducer.releaseResources();
/*     */                 }
/* 161 */                 state.retrying = true;
/* 162 */                 int execCount = scope.execCount.incrementAndGet();
/* 163 */                 state.delay = AsyncHttpRequestRetryExec.this.retryStrategy.getRetryInterval(request, (IOException)cause, execCount - 1, (HttpContext)clientContext);
/* 164 */                 scope.scheduler.scheduleExecution(request, entityProducer, scope, asyncExecCallback, state.delay);
/*     */                 return;
/*     */               } 
/*     */             } 
/* 168 */             asyncExecCallback.failed(cause);
/*     */           }
/*     */         });
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
/*     */   public void execute(HttpRequest request, AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecChain chain, AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 182 */     State state = new State();
/* 183 */     state.retrying = false;
/* 184 */     internalExecute(state, request, entityProducer, scope, chain, asyncExecCallback);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/AsyncHttpRequestRetryExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */