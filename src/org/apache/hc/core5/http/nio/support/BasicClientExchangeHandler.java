/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.RequestChannel;
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
/*     */ 
/*     */ public final class BasicClientExchangeHandler<T>
/*     */   implements AsyncClientExchangeHandler
/*     */ {
/*     */   private final AsyncRequestProducer requestProducer;
/*     */   private final AsyncResponseConsumer<T> responseConsumer;
/*     */   private final AtomicBoolean completed;
/*     */   private final FutureCallback<T> resultCallback;
/*     */   private final AtomicBoolean outputTerminated;
/*     */   
/*     */   public BasicClientExchangeHandler(AsyncRequestProducer requestProducer, AsyncResponseConsumer<T> responseConsumer, FutureCallback<T> resultCallback) {
/*  68 */     this.requestProducer = (AsyncRequestProducer)Args.notNull(requestProducer, "Request producer");
/*  69 */     this.responseConsumer = (AsyncResponseConsumer<T>)Args.notNull(responseConsumer, "Response consumer");
/*  70 */     this.completed = new AtomicBoolean(false);
/*  71 */     this.resultCallback = resultCallback;
/*  72 */     this.outputTerminated = new AtomicBoolean(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceRequest(RequestChannel requestChannel, HttpContext httpContext) throws HttpException, IOException {
/*  77 */     this.requestProducer.sendRequest(requestChannel, httpContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/*  82 */     return this.requestProducer.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void produce(DataStreamChannel channel) throws IOException {
/*  87 */     if (this.outputTerminated.get()) {
/*  88 */       channel.endStream();
/*     */       return;
/*     */     } 
/*  91 */     this.requestProducer.produce(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeInformation(HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
/*  96 */     this.responseConsumer.informationResponse(response, httpContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
/* 101 */     if (response.getCode() >= 400) {
/* 102 */       this.outputTerminated.set(true);
/* 103 */       this.requestProducer.releaseResources();
/*     */     } 
/* 105 */     this.responseConsumer.consumeResponse(response, entityDetails, httpContext, new FutureCallback<T>()
/*     */         {
/*     */           public void completed(T result)
/*     */           {
/* 109 */             if (BasicClientExchangeHandler.this.completed.compareAndSet(false, true)) {
/*     */               try {
/* 111 */                 if (BasicClientExchangeHandler.this.resultCallback != null) {
/* 112 */                   BasicClientExchangeHandler.this.resultCallback.completed(result);
/*     */                 }
/*     */               } finally {
/* 115 */                 BasicClientExchangeHandler.this.internalReleaseResources();
/*     */               } 
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception ex) {
/* 122 */             if (BasicClientExchangeHandler.this.completed.compareAndSet(false, true)) {
/*     */               try {
/* 124 */                 if (BasicClientExchangeHandler.this.resultCallback != null) {
/* 125 */                   BasicClientExchangeHandler.this.resultCallback.failed(ex);
/*     */                 }
/*     */               } finally {
/* 128 */                 BasicClientExchangeHandler.this.internalReleaseResources();
/*     */               } 
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 135 */             if (BasicClientExchangeHandler.this.completed.compareAndSet(false, true)) {
/*     */               try {
/* 137 */                 if (BasicClientExchangeHandler.this.resultCallback != null) {
/* 138 */                   BasicClientExchangeHandler.this.resultCallback.cancelled();
/*     */                 }
/*     */               } finally {
/* 141 */                 BasicClientExchangeHandler.this.internalReleaseResources();
/*     */               } 
/*     */             }
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 151 */     if (this.completed.compareAndSet(false, true)) {
/*     */       try {
/* 153 */         if (this.resultCallback != null) {
/* 154 */           this.resultCallback.cancelled();
/*     */         }
/*     */       } finally {
/* 157 */         internalReleaseResources();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 164 */     this.responseConsumer.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consume(ByteBuffer src) throws IOException {
/* 169 */     this.responseConsumer.consume(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 174 */     this.responseConsumer.streamEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*     */     try {
/* 180 */       this.requestProducer.failed(cause);
/* 181 */       this.responseConsumer.failed(cause);
/*     */     } finally {
/* 183 */       if (this.completed.compareAndSet(false, true)) {
/*     */         try {
/* 185 */           if (this.resultCallback != null) {
/* 186 */             this.resultCallback.failed(cause);
/*     */           }
/*     */         } finally {
/* 189 */           internalReleaseResources();
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void internalReleaseResources() {
/* 196 */     this.requestProducer.releaseResources();
/* 197 */     this.responseConsumer.releaseResources();
/*     */   }
/*     */   
/*     */   public void releaseResources() {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/BasicClientExchangeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */