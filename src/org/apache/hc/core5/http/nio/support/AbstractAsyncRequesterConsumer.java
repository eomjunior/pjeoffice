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
/*     */ import org.apache.hc.core5.http.HttpRequest;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAsyncRequesterConsumer<T, E>
/*     */   implements AsyncRequestConsumer<T>
/*     */ {
/*     */   private final Supplier<AsyncEntityConsumer<E>> dataConsumerSupplier;
/*     */   private final AtomicReference<AsyncEntityConsumer<E>> dataConsumerRef;
/*     */   
/*     */   public AbstractAsyncRequesterConsumer(Supplier<AsyncEntityConsumer<E>> dataConsumerSupplier) {
/*  64 */     this.dataConsumerSupplier = (Supplier<AsyncEntityConsumer<E>>)Args.notNull(dataConsumerSupplier, "Data consumer supplier");
/*  65 */     this.dataConsumerRef = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public AbstractAsyncRequesterConsumer(AsyncEntityConsumer<E> dataConsumer) {
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
/*     */   public final void consumeRequest(final HttpRequest request, final EntityDetails entityDetails, HttpContext httpContext, final FutureCallback<T> resultCallback) throws HttpException, IOException {
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
/*  99 */                 T result = AbstractAsyncRequesterConsumer.this.buildResult(request, entity, contentType);
/* 100 */                 resultCallback.completed(result);
/* 101 */               } catch (UnsupportedCharsetException ex) {
/* 102 */                 resultCallback.failed(ex);
/*     */               } 
/*     */             }
/*     */           });
/*     */     } else {
/*     */       
/* 108 */       resultCallback.completed(buildResult(request, null, null));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 115 */     AsyncEntityConsumer<E> dataConsumer = this.dataConsumerRef.get();
/* 116 */     dataConsumer.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void consume(ByteBuffer src) throws IOException {
/* 121 */     AsyncEntityConsumer<E> dataConsumer = this.dataConsumerRef.get();
/* 122 */     dataConsumer.consume(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 127 */     AsyncEntityConsumer<E> dataConsumer = this.dataConsumerRef.get();
/* 128 */     dataConsumer.streamEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 133 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void releaseResources() {
/* 138 */     AsyncEntityConsumer<E> dataConsumer = this.dataConsumerRef.getAndSet(null);
/* 139 */     if (dataConsumer != null)
/* 140 */       dataConsumer.releaseResources(); 
/*     */   }
/*     */   
/*     */   protected abstract T buildResult(HttpRequest paramHttpRequest, E paramE, ContentType paramContentType);
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AbstractAsyncRequesterConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */