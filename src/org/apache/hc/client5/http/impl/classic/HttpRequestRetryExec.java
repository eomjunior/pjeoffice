/*     */ package org.apache.hc.client5.http.impl.classic;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import org.apache.hc.client5.http.HttpRequestRetryStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.classic.ExecChain;
/*     */ import org.apache.hc.client5.http.classic.ExecChainHandler;
/*     */ import org.apache.hc.client5.http.config.RequestConfig;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ClassicHttpRequest;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.NoHttpResponseException;
/*     */ import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ @Internal
/*     */ public class HttpRequestRetryExec
/*     */   implements ExecChainHandler
/*     */ {
/*  71 */   private static final Logger LOG = LoggerFactory.getLogger(HttpRequestRetryExec.class);
/*     */   
/*     */   private final HttpRequestRetryStrategy retryStrategy;
/*     */ 
/*     */   
/*     */   public HttpRequestRetryExec(HttpRequestRetryStrategy retryStrategy) {
/*  77 */     Args.notNull(retryStrategy, "retryStrategy");
/*  78 */     this.retryStrategy = retryStrategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse execute(ClassicHttpRequest request, ExecChain.Scope scope, ExecChain chain) throws IOException, HttpException {
/*  86 */     Args.notNull(request, "request");
/*  87 */     Args.notNull(scope, "scope");
/*  88 */     String exchangeId = scope.exchangeId;
/*  89 */     HttpRoute route = scope.route;
/*  90 */     HttpClientContext context = scope.clientContext;
/*  91 */     ClassicHttpRequest currentRequest = request;
/*     */     
/*  93 */     for (int execCount = 1;; execCount++) {
/*     */       ClassicHttpResponse response;
/*     */       try {
/*  96 */         response = chain.proceed(currentRequest, scope);
/*  97 */       } catch (IOException ex) {
/*  98 */         if (scope.execRuntime.isExecutionAborted()) {
/*  99 */           throw new RequestFailedException("Request aborted");
/*     */         }
/* 101 */         HttpEntity requestEntity = request.getEntity();
/* 102 */         if (requestEntity != null && !requestEntity.isRepeatable()) {
/* 103 */           if (LOG.isDebugEnabled()) {
/* 104 */             LOG.debug("{} cannot retry non-repeatable request", exchangeId);
/*     */           }
/* 106 */           throw ex;
/*     */         } 
/* 108 */         if (this.retryStrategy.retryRequest((HttpRequest)request, ex, execCount, (HttpContext)context)) {
/* 109 */           if (LOG.isDebugEnabled()) {
/* 110 */             LOG.debug("{} {}", new Object[] { exchangeId, ex.getMessage(), ex });
/*     */           }
/* 112 */           if (LOG.isInfoEnabled()) {
/* 113 */             LOG.info("Recoverable I/O exception ({}) caught when processing request to {}", ex
/* 114 */                 .getClass().getName(), route);
/*     */           }
/* 116 */           TimeValue nextInterval = this.retryStrategy.getRetryInterval((HttpRequest)request, ex, execCount, (HttpContext)context);
/* 117 */           if (TimeValue.isPositive(nextInterval)) {
/*     */             try {
/* 119 */               if (LOG.isDebugEnabled()) {
/* 120 */                 LOG.debug("{} wait for {}", exchangeId, nextInterval);
/*     */               }
/* 122 */               nextInterval.sleep();
/* 123 */             } catch (InterruptedException e) {
/* 124 */               Thread.currentThread().interrupt();
/* 125 */               throw new InterruptedIOException();
/*     */             } 
/*     */           }
/* 128 */           currentRequest = ClassicRequestBuilder.copy(scope.originalRequest).build();
/*     */         } else {
/*     */           
/* 131 */           if (ex instanceof NoHttpResponseException) {
/*     */             
/* 133 */             NoHttpResponseException updatedex = new NoHttpResponseException(route.getTargetHost().toHostString() + " failed to respond");
/* 134 */             updatedex.setStackTrace(ex.getStackTrace());
/* 135 */             throw updatedex;
/*     */           } 
/* 137 */           throw ex;
/*     */         } 
/*     */       } 
/*     */       
/*     */       try {
/* 142 */         HttpEntity entity = request.getEntity();
/* 143 */         if (entity != null && !entity.isRepeatable()) {
/* 144 */           if (LOG.isDebugEnabled()) {
/* 145 */             LOG.debug("{} cannot retry non-repeatable request", exchangeId);
/*     */           }
/* 147 */           return response;
/*     */         } 
/* 149 */         if (this.retryStrategy.retryRequest((HttpResponse)response, execCount, (HttpContext)context)) {
/* 150 */           TimeValue nextInterval = this.retryStrategy.getRetryInterval((HttpResponse)response, execCount, (HttpContext)context);
/*     */           
/* 152 */           if (TimeValue.isPositive(nextInterval)) {
/* 153 */             RequestConfig requestConfig = context.getRequestConfig();
/* 154 */             Timeout responseTimeout = requestConfig.getResponseTimeout();
/* 155 */             if (responseTimeout != null && nextInterval.compareTo((TimeValue)responseTimeout) > 0) {
/* 156 */               return response;
/*     */             }
/*     */           } 
/* 159 */           response.close();
/* 160 */           if (TimeValue.isPositive(nextInterval)) {
/*     */             try {
/* 162 */               if (LOG.isDebugEnabled()) {
/* 163 */                 LOG.debug("{} wait for {}", exchangeId, nextInterval);
/*     */               }
/* 165 */               nextInterval.sleep();
/* 166 */             } catch (InterruptedException e) {
/* 167 */               Thread.currentThread().interrupt();
/* 168 */               throw new InterruptedIOException();
/*     */             } 
/*     */           }
/* 171 */           currentRequest = ClassicRequestBuilder.copy(scope.originalRequest).build();
/*     */         } else {
/* 173 */           return response;
/*     */         } 
/* 175 */       } catch (RuntimeException ex) {
/* 176 */         response.close();
/* 177 */         throw ex;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/classic/HttpRequestRetryExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */