/*     */ package org.apache.hc.client5.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.NoRouteToHostException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.time.Instant;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.apache.hc.client5.http.HttpRequestRetryStrategy;
/*     */ import org.apache.hc.client5.http.utils.DateUtils;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.TimeValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class DefaultHttpRequestRetryStrategy
/*     */   implements HttpRequestRetryStrategy
/*     */ {
/*  67 */   public static final DefaultHttpRequestRetryStrategy INSTANCE = new DefaultHttpRequestRetryStrategy();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxRetries;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final TimeValue defaultRetryInterval;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Set<Class<? extends IOException>> nonRetriableIOExceptionClasses;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Set<Integer> retriableCodes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultHttpRequestRetryStrategy(int maxRetries, TimeValue defaultRetryInterval, Collection<Class<? extends IOException>> clazzes, Collection<Integer> codes) {
/*  94 */     Args.notNegative(maxRetries, "maxRetries");
/*  95 */     Args.notNegative(defaultRetryInterval.getDuration(), "defaultRetryInterval");
/*  96 */     this.maxRetries = maxRetries;
/*  97 */     this.defaultRetryInterval = defaultRetryInterval;
/*  98 */     this.nonRetriableIOExceptionClasses = new HashSet<>(clazzes);
/*  99 */     this.retriableCodes = new HashSet<>(codes);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpRequestRetryStrategy(int maxRetries, TimeValue defaultRetryInterval) {
/* 128 */     this(maxRetries, defaultRetryInterval, 
/* 129 */         Arrays.asList((Class<? extends IOException>[])new Class[] {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             InterruptedIOException.class, UnknownHostException.class, ConnectException.class, ConnectionClosedException.class, NoRouteToHostException.class, SSLException.class
/* 136 */           }, ), Arrays.asList(new Integer[] {
/* 137 */             Integer.valueOf(429), 
/* 138 */             Integer.valueOf(503)
/*     */           }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultHttpRequestRetryStrategy() {
/* 160 */     this(1, TimeValue.ofSeconds(1L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retryRequest(HttpRequest request, IOException exception, int execCount, HttpContext context) {
/* 169 */     Args.notNull(request, "request");
/* 170 */     Args.notNull(exception, "exception");
/*     */     
/* 172 */     if (execCount > this.maxRetries)
/*     */     {
/* 174 */       return false;
/*     */     }
/* 176 */     if (this.nonRetriableIOExceptionClasses.contains(exception.getClass())) {
/* 177 */       return false;
/*     */     }
/* 179 */     for (Class<? extends IOException> rejectException : this.nonRetriableIOExceptionClasses) {
/* 180 */       if (rejectException.isInstance(exception)) {
/* 181 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 185 */     if (request instanceof CancellableDependency && ((CancellableDependency)request).isCancelled()) {
/* 186 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 190 */     return handleAsIdempotent(request);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retryRequest(HttpResponse response, int execCount, HttpContext context) {
/* 198 */     Args.notNull(response, "response");
/*     */     
/* 200 */     return (execCount <= this.maxRetries && this.retriableCodes.contains(Integer.valueOf(response.getCode())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
/* 208 */     Args.notNull(response, "response");
/*     */     
/* 210 */     Header header = response.getFirstHeader("Retry-After");
/* 211 */     TimeValue retryAfter = null;
/* 212 */     if (header != null) {
/* 213 */       String value = header.getValue();
/*     */       try {
/* 215 */         retryAfter = TimeValue.ofSeconds(Long.parseLong(value));
/* 216 */       } catch (NumberFormatException ignore) {
/* 217 */         Instant retryAfterDate = DateUtils.parseStandardDate(value);
/* 218 */         if (retryAfterDate != null)
/*     */         {
/* 220 */           retryAfter = TimeValue.ofMilliseconds(retryAfterDate.toEpochMilli() - System.currentTimeMillis());
/*     */         }
/*     */       } 
/*     */       
/* 224 */       if (TimeValue.isPositive(retryAfter)) {
/* 225 */         return retryAfter;
/*     */       }
/*     */     } 
/* 228 */     return this.defaultRetryInterval;
/*     */   }
/*     */   
/*     */   protected boolean handleAsIdempotent(HttpRequest request) {
/* 232 */     return Method.isIdempotent(request.getMethod());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/DefaultHttpRequestRetryStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */