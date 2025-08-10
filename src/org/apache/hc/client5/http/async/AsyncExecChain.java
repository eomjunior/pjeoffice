/*     */ package org.apache.hc.client5.http.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public interface AsyncExecChain
/*     */ {
/*     */   void proceed(HttpRequest paramHttpRequest, AsyncEntityProducer paramAsyncEntityProducer, Scope paramScope, AsyncExecCallback paramAsyncExecCallback) throws HttpException, IOException;
/*     */   
/*     */   public static interface Scheduler
/*     */   {
/*     */     void scheduleExecution(HttpRequest param1HttpRequest, AsyncEntityProducer param1AsyncEntityProducer, AsyncExecChain.Scope param1Scope, AsyncExecCallback param1AsyncExecCallback, TimeValue param1TimeValue);
/*     */   }
/*     */   
/*     */   public static final class Scope
/*     */   {
/*     */     public final String exchangeId;
/*     */     public final HttpRoute route;
/*     */     public final HttpRequest originalRequest;
/*     */     public final CancellableDependency cancellableDependency;
/*     */     public final HttpClientContext clientContext;
/*     */     public final AsyncExecRuntime execRuntime;
/*     */     public final AsyncExecChain.Scheduler scheduler;
/*     */     public final AtomicInteger execCount;
/*     */     
/*     */     public Scope(String exchangeId, HttpRoute route, HttpRequest originalRequest, CancellableDependency cancellableDependency, HttpClientContext clientContext, AsyncExecRuntime execRuntime, AsyncExecChain.Scheduler scheduler, AtomicInteger execCount) {
/*  79 */       this.exchangeId = (String)Args.notBlank(exchangeId, "Exchange id");
/*  80 */       this.route = (HttpRoute)Args.notNull(route, "Route");
/*  81 */       this.originalRequest = (HttpRequest)Args.notNull(originalRequest, "Original request");
/*  82 */       this.cancellableDependency = (CancellableDependency)Args.notNull(cancellableDependency, "Dependency");
/*  83 */       this.clientContext = (clientContext != null) ? clientContext : HttpClientContext.create();
/*  84 */       this.execRuntime = (AsyncExecRuntime)Args.notNull(execRuntime, "Exec runtime");
/*  85 */       this.scheduler = scheduler;
/*  86 */       this.execCount = (execCount != null) ? execCount : new AtomicInteger(1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Scope(String exchangeId, HttpRoute route, HttpRequest originalRequest, CancellableDependency cancellableDependency, HttpClientContext clientContext, AsyncExecRuntime execRuntime) {
/* 101 */       this(exchangeId, route, originalRequest, cancellableDependency, clientContext, execRuntime, null, new AtomicInteger(1));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/AsyncExecChain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */