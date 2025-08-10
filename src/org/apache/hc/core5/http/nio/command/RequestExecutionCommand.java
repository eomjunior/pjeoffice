/*     */ package org.apache.hc.core5.http.nio.command;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.http.RequestNotExecutedException;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Internal
/*     */ public final class RequestExecutionCommand
/*     */   extends ExecutableCommand
/*     */ {
/*     */   private final AsyncClientExchangeHandler exchangeHandler;
/*     */   private final HandlerFactory<AsyncPushConsumer> pushHandlerFactory;
/*     */   private final CancellableDependency cancellableDependency;
/*     */   private final HttpContext context;
/*     */   private final AtomicBoolean failed;
/*     */   
/*     */   public RequestExecutionCommand(AsyncClientExchangeHandler exchangeHandler, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, CancellableDependency cancellableDependency, HttpContext context) {
/*  60 */     this.exchangeHandler = (AsyncClientExchangeHandler)Args.notNull(exchangeHandler, "Handler");
/*  61 */     this.pushHandlerFactory = pushHandlerFactory;
/*  62 */     this.cancellableDependency = cancellableDependency;
/*  63 */     this.context = context;
/*  64 */     this.failed = new AtomicBoolean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestExecutionCommand(AsyncClientExchangeHandler exchangeHandler, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpContext context) {
/*  71 */     this(exchangeHandler, pushHandlerFactory, null, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestExecutionCommand(AsyncClientExchangeHandler exchangeHandler, HttpContext context) {
/*  77 */     this(exchangeHandler, null, null, context);
/*     */   }
/*     */   
/*     */   public AsyncClientExchangeHandler getExchangeHandler() {
/*  81 */     return this.exchangeHandler;
/*     */   }
/*     */   
/*     */   public HandlerFactory<AsyncPushConsumer> getPushHandlerFactory() {
/*  85 */     return this.pushHandlerFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public CancellableDependency getCancellableDependency() {
/*  90 */     return this.cancellableDependency;
/*     */   }
/*     */   
/*     */   public HttpContext getContext() {
/*  94 */     return this.context;
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception ex) {
/*  99 */     if (this.failed.compareAndSet(false, true)) {
/*     */       try {
/* 101 */         this.exchangeHandler.failed(ex);
/*     */       } finally {
/* 103 */         this.exchangeHandler.releaseResources();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cancel() {
/* 110 */     if (this.failed.compareAndSet(false, true)) {
/*     */       try {
/* 112 */         this.exchangeHandler.failed((Exception)new RequestNotExecutedException());
/* 113 */         return true;
/*     */       } finally {
/* 115 */         this.exchangeHandler.releaseResources();
/*     */       } 
/*     */     }
/* 118 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/command/RequestExecutionCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */