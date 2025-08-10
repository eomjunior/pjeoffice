/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.concurrent.FutureCallback;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerRequestHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.ResponseChannel;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractServerExchangeHandler<T>
/*     */   implements AsyncServerExchangeHandler
/*     */ {
/*  63 */   private final AtomicReference<AsyncRequestConsumer<T>> requestConsumerRef = new AtomicReference<>();
/*  64 */   private final AtomicReference<AsyncResponseProducer> responseProducerRef = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void handleRequest(HttpRequest request, EntityDetails entityDetails, final ResponseChannel responseChannel, final HttpContext context) throws HttpException, IOException {
/* 101 */     AsyncRequestConsumer<T> requestConsumer = supplyConsumer(request, entityDetails, context);
/* 102 */     if (requestConsumer == null) {
/* 103 */       throw new HttpException("Unable to handle request");
/*     */     }
/* 105 */     this.requestConsumerRef.set(requestConsumer);
/* 106 */     final AsyncServerRequestHandler.ResponseTrigger responseTrigger = new AsyncServerRequestHandler.ResponseTrigger()
/*     */       {
/*     */         public void sendInformation(HttpResponse response, HttpContext httpContext) throws HttpException, IOException
/*     */         {
/* 110 */           responseChannel.sendInformation(response, httpContext);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void submitResponse(AsyncResponseProducer producer, HttpContext httpContext) throws HttpException, IOException {
/* 116 */           if (AbstractServerExchangeHandler.this.responseProducerRef.compareAndSet(null, producer)) {
/* 117 */             producer.sendResponse(responseChannel, httpContext);
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void pushPromise(HttpRequest promise, HttpContext httpContext, AsyncPushProducer pushProducer) throws HttpException, IOException {
/* 124 */           responseChannel.pushPromise(promise, pushProducer, httpContext);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 129 */           return "Response trigger: " + responseChannel;
/*     */         }
/*     */       };
/*     */     
/* 133 */     requestConsumer.consumeRequest(request, entityDetails, context, new FutureCallback<T>()
/*     */         {
/*     */           public void completed(T result)
/*     */           {
/*     */             try {
/* 138 */               AbstractServerExchangeHandler.this.handle(result, responseTrigger, context);
/* 139 */             } catch (HttpException ex) {
/*     */               try {
/* 141 */                 responseTrigger.submitResponse(
/* 142 */                     AsyncResponseBuilder.create(500)
/* 143 */                     .setEntity(ex.getMessage())
/* 144 */                     .build(), context);
/*     */               }
/* 146 */               catch (HttpException|IOException ex2) {
/* 147 */                 failed(ex2);
/*     */               } 
/* 149 */             } catch (IOException ex) {
/* 150 */               failed(ex);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception ex) {
/* 156 */             AbstractServerExchangeHandler.this.failed(ex);
/*     */           }
/*     */ 
/*     */           
/*     */           public void cancelled() {
/* 161 */             AbstractServerExchangeHandler.this.releaseResources();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 170 */     AsyncRequestConsumer<T> requestConsumer = this.requestConsumerRef.get();
/* 171 */     Asserts.notNull(requestConsumer, "Data consumer");
/* 172 */     requestConsumer.updateCapacity(capacityChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void consume(ByteBuffer src) throws IOException {
/* 177 */     AsyncRequestConsumer<T> requestConsumer = this.requestConsumerRef.get();
/* 178 */     Asserts.notNull(requestConsumer, "Data consumer");
/* 179 */     requestConsumer.consume(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 184 */     AsyncRequestConsumer<T> requestConsumer = this.requestConsumerRef.get();
/* 185 */     Asserts.notNull(requestConsumer, "Data consumer");
/* 186 */     requestConsumer.streamEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int available() {
/* 191 */     AsyncResponseProducer dataProducer = this.responseProducerRef.get();
/* 192 */     return (dataProducer != null) ? dataProducer.available() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void produce(DataStreamChannel channel) throws IOException {
/* 197 */     AsyncResponseProducer dataProducer = this.responseProducerRef.get();
/* 198 */     Asserts.notNull(dataProducer, "Data producer");
/* 199 */     dataProducer.produce(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/*     */     try {
/* 205 */       AsyncRequestConsumer<T> requestConsumer = this.requestConsumerRef.get();
/* 206 */       if (requestConsumer != null) {
/* 207 */         requestConsumer.failed(cause);
/*     */       }
/* 209 */       AsyncResponseProducer dataProducer = this.responseProducerRef.get();
/* 210 */       if (dataProducer != null) {
/* 211 */         dataProducer.failed(cause);
/*     */       }
/*     */     } finally {
/* 214 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void releaseResources() {
/* 220 */     AsyncRequestConsumer<T> requestConsumer = this.requestConsumerRef.getAndSet(null);
/* 221 */     if (requestConsumer != null) {
/* 222 */       requestConsumer.releaseResources();
/*     */     }
/* 224 */     AsyncResponseProducer dataProducer = this.responseProducerRef.getAndSet(null);
/* 225 */     if (dataProducer != null)
/* 226 */       dataProducer.releaseResources(); 
/*     */   }
/*     */   
/*     */   protected abstract AsyncRequestConsumer<T> supplyConsumer(HttpRequest paramHttpRequest, EntityDetails paramEntityDetails, HttpContext paramHttpContext) throws HttpException;
/*     */   
/*     */   protected abstract void handle(T paramT, AsyncServerRequestHandler.ResponseTrigger paramResponseTrigger, HttpContext paramHttpContext) throws HttpException, IOException;
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AbstractServerExchangeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */