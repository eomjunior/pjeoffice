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
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Message;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestConsumer;
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
/*     */ public class BasicRequestConsumer<T>
/*     */   implements AsyncRequestConsumer<Message<HttpRequest, T>>
/*     */ {
/*     */   private final Supplier<AsyncEntityConsumer<T>> dataConsumerSupplier;
/*     */   private final AtomicReference<AsyncEntityConsumer<T>> dataConsumerRef;
/*     */   
/*     */   public BasicRequestConsumer(Supplier<AsyncEntityConsumer<T>> dataConsumerSupplier) {
/*  60 */     this.dataConsumerSupplier = (Supplier<AsyncEntityConsumer<T>>)Args.notNull(dataConsumerSupplier, "Data consumer supplier");
/*  61 */     this.dataConsumerRef = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public BasicRequestConsumer(AsyncEntityConsumer<T> dataConsumer) {
/*  65 */     this(() -> dataConsumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeRequest(final HttpRequest request, EntityDetails entityDetails, HttpContext httpContext, final FutureCallback<Message<HttpRequest, T>> resultCallback) throws HttpException, IOException {
/*  74 */     Args.notNull(request, "Request");
/*  75 */     if (entityDetails != null) {
/*  76 */       AsyncEntityConsumer<T> dataConsumer = (AsyncEntityConsumer<T>)this.dataConsumerSupplier.get();
/*  77 */       if (dataConsumer == null) {
/*  78 */         throw new HttpException("Supplied data consumer is null");
/*     */       }
/*  80 */       this.dataConsumerRef.set(dataConsumer);
/*     */       
/*  82 */       dataConsumer.streamStart(entityDetails, (FutureCallback)new CallbackContribution<T>(resultCallback)
/*     */           {
/*     */             public void completed(T body)
/*     */             {
/*  86 */               Message<HttpRequest, T> result = new Message((MessageHeaders)request, body);
/*  87 */               if (resultCallback != null) {
/*  88 */                 resultCallback.completed(result);
/*     */               }
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/*  94 */       Message<HttpRequest, T> result = new Message((MessageHeaders)request, null);
/*  95 */       if (resultCallback != null) {
/*  96 */         resultCallback.completed(result);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 103 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.get();
/* 104 */     dataConsumer.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consume(ByteBuffer src) throws IOException {
/* 109 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.get();
/* 110 */     dataConsumer.consume(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 115 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.get();
/* 116 */     dataConsumer.streamEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/* 121 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 126 */     AsyncEntityConsumer<T> dataConsumer = this.dataConsumerRef.getAndSet(null);
/* 127 */     if (dataConsumer != null)
/* 128 */       dataConsumer.releaseResources(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/BasicRequestConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */