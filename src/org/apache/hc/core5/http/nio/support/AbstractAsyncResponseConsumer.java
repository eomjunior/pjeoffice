/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.concurrent.CallbackContribution;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpResponse;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAsyncResponseConsumer<T, E>
/*     */   implements AsyncResponseConsumer<T>
/*     */ {
/*     */   private final Supplier<AsyncEntityConsumer<E>> dataConsumerSupplier;
/*     */   private final AtomicReference<AsyncEntityConsumer<E>> dataConsumerRef;
/*     */   
/*     */   public AbstractAsyncResponseConsumer(Supplier<AsyncEntityConsumer<E>> dataConsumerSupplier) {
/*  64 */     this.dataConsumerSupplier = (Supplier<AsyncEntityConsumer<E>>)Args.notNull(dataConsumerSupplier, "Data consumer supplier");
/*  65 */     this.dataConsumerRef = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public AbstractAsyncResponseConsumer(AsyncEntityConsumer<E> dataConsumer) {
/*  69 */     this(() -> dataConsumer);
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
/*     */   public final void consumeResponse(final HttpResponse response, final EntityDetails entityDetails, HttpContext httpContext, final FutureCallback<T> resultCallback) throws HttpException, IOException {
/*  86 */     if (entityDetails != null) {
/*  87 */       AsyncEntityConsumer<E> dataConsumer = (AsyncEntityConsumer<E>)this.dataConsumerSupplier.get();
/*  88 */       if (dataConsumer == null) {
/*  89 */         throw new HttpException("Supplied data consumer is null");
/*     */       }
/*  91 */       this.dataConsumerRef.set(dataConsumer);
/*  92 */       dataConsumer.streamStart(entityDetails, (FutureCallback)new CallbackContribution<E>(resultCallback)
/*     */           {
/*     */             
/*     */             public void completed(E entity)
/*     */             {
/*     */               try {
/*  98 */                 ContentType contentType = ContentType.parse(entityDetails.getContentType());
/*  99 */                 T result = AbstractAsyncResponseConsumer.this.buildResult(response, entity, contentType);
/* 100 */                 if (resultCallback != null) {
/* 101 */                   resultCallback.completed(result);
/*     */                 }
/* 103 */               } catch (UnsupportedCharsetException ex) {
/* 104 */                 if (resultCallback != null) {
/* 105 */                   resultCallback.failed(ex);
/*     */                 }
/*     */               } 
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 112 */       T result = buildResult(response, null, null);
/* 113 */       if (resultCallback != null) {
/* 114 */         resultCallback.completed(result);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 122 */     AsyncEntityConsumer<E> dataConsumer = this.dataConsumerRef.get();
/* 123 */     if (dataConsumer != null) {
/* 124 */       dataConsumer.updateCapacity(capacityChannel);
/*     */     } else {
/* 126 */       capacityChannel.update(2147483647);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void consume(ByteBuffer src) throws IOException {
/* 132 */     AsyncEntityConsumer<E> dataConsumer = this.dataConsumerRef.get();
/* 133 */     if (dataConsumer != null) {
/* 134 */       dataConsumer.consume(src);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 140 */     AsyncEntityConsumer<E> dataConsumer = this.dataConsumerRef.get();
/* 141 */     if (dataConsumer != null) {
/* 142 */       dataConsumer.streamEnd(trailers);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 148 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void releaseResources() {
/* 153 */     AsyncEntityConsumer<E> dataConsumer = this.dataConsumerRef.getAndSet(null);
/* 154 */     if (dataConsumer != null)
/* 155 */       dataConsumer.releaseResources(); 
/*     */   }
/*     */   
/*     */   protected abstract T buildResult(HttpResponse paramHttpResponse, E paramE, ContentType paramContentType);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AbstractAsyncResponseConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */