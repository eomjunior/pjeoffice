/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.Message;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
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
/*     */ public class BasicResponseConsumer<T>
/*     */   implements AsyncResponseConsumer<Message<HttpResponse, T>>
/*     */ {
/*     */   private final Supplier<AsyncEntityConsumer<T>> dataConsumerSupplier;
/*     */   private final AtomicReference<AsyncEntityConsumer<T>> dataConsumerRef;
/*     */   
/*     */   public BasicResponseConsumer(Supplier<AsyncEntityConsumer<T>> dataConsumerSupplier) {
/*  61 */     this.dataConsumerSupplier = (Supplier<AsyncEntityConsumer<T>>)Args.notNull(dataConsumerSupplier, "Data consumer supplier");
/*  62 */     this.dataConsumerRef = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public BasicResponseConsumer(AsyncEntityConsumer<T> dataConsumer) {
/*  66 */     this(() -> dataConsumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeResponse(final HttpResponse response, EntityDetails entityDetails, HttpContext httpContext, final FutureCallback<Message<HttpResponse, T>> resultCallback) throws HttpException, IOException {
/*  74 */     Args.notNull(response, "Response");
/*     */     
/*  76 */     if (entityDetails != null) {
/*  77 */       AsyncEntityConsumer<T> dataConsumer = (AsyncEntityConsumer<T>)this.dataConsumerSupplier.get();
/*  78 */       if (dataConsumer == null) {
/*  79 */         throw new HttpException("Supplied data consumer is null");
/*     */       }
/*  81 */       this.dataConsumerRef.set(dataConsumer);
/*  82 */       dataConsumer.streamStart(entityDetails, (FutureCallback)new CallbackContribution<T>(resultCallback)
/*     */           {
/*     */             public void completed(T body)
/*     */             {
/*  86 */               Message<HttpResponse, T> result = new Message((MessageHeaders)response, body);
/*  87 */               if (resultCallback != null) {
/*  88 */                 resultCallback.completed(result);
/*     */               }
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/*  94 */       Message<HttpResponse, T> result = new Message((MessageHeaders)response, null);
/*  95 */       if (resultCallback != null) {
/*  96 */         resultCallback.completed(result);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void informationResponse(HttpResponse response, HttpContext httpContext) throws HttpException, IOException {}
/*     */ 
/*     */   
/*     */   public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 107 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.get();
/* 108 */     dataConsumer.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consume(ByteBuffer src) throws IOException {
/* 113 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.get();
/* 114 */     dataConsumer.consume(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 119 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.get();
/* 120 */     dataConsumer.streamEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/* 125 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.get();
/* 126 */     if (dataConsumer != null) {
/* 127 */       dataConsumer.failed(cause);
/*     */     }
/* 129 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 134 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.getAndSet(null);
/* 135 */     if (dataConsumer != null)
/* 136 */       dataConsumer.releaseResources(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/BasicResponseConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */