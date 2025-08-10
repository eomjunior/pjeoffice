/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.function.Callback;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncFilterChain;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.ResourceHolder;
/*     */ import org.apache.hc.core5.http.nio.ResponseChannel;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Args;
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
/*     */ public final class AsyncServerFilterChainExchangeHandlerFactory
/*     */   implements HandlerFactory<AsyncServerExchangeHandler>
/*     */ {
/*     */   private final AsyncServerFilterChainElement filterChain;
/*     */   private final Callback<Exception> exceptionCallback;
/*     */   
/*     */   public AsyncServerFilterChainExchangeHandlerFactory(AsyncServerFilterChainElement filterChain, Callback<Exception> exceptionCallback) {
/*  68 */     this.filterChain = (AsyncServerFilterChainElement)Args.notNull(filterChain, "Filter chain");
/*  69 */     this.exceptionCallback = exceptionCallback;
/*     */   }
/*     */   
/*     */   public AsyncServerFilterChainExchangeHandlerFactory(AsyncServerFilterChainElement filterChain) {
/*  73 */     this(filterChain, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncServerExchangeHandler create(HttpRequest request, HttpContext context) throws HttpException {
/*  78 */     return new AsyncServerExchangeHandler()
/*     */       {
/*  80 */         private final AtomicReference<AsyncDataConsumer> dataConsumerRef = new AtomicReference<>();
/*  81 */         private final AtomicReference<AsyncResponseProducer> responseProducerRef = new AtomicReference<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void handleRequest(HttpRequest request, EntityDetails entityDetails, final ResponseChannel responseChannel, final HttpContext context) throws HttpException, IOException {
/*  89 */           this.dataConsumerRef.set(AsyncServerFilterChainExchangeHandlerFactory.this.filterChain.handle(request, entityDetails, context, new AsyncFilterChain.ResponseTrigger()
/*     */                 {
/*     */                   
/*     */                   public void sendInformation(HttpResponse response) throws HttpException, IOException
/*     */                   {
/*  94 */                     responseChannel.sendInformation(response, context);
/*     */                   }
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   public void submitResponse(HttpResponse response, AsyncEntityProducer entityProducer) throws HttpException, IOException {
/* 101 */                     AsyncResponseProducer responseProducer = new BasicResponseProducer(response, entityProducer);
/* 102 */                     AsyncServerFilterChainExchangeHandlerFactory.null.this.responseProducerRef.set(responseProducer);
/* 103 */                     responseProducer.sendResponse(responseChannel, context);
/*     */                   }
/*     */ 
/*     */                   
/*     */                   public void pushPromise(HttpRequest promise, AsyncPushProducer responseProducer) throws HttpException, IOException {
/* 108 */                     responseChannel.pushPromise(promise, responseProducer, context);
/*     */                   }
/*     */                 }));
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void failed(Exception cause) {
/* 116 */           if (AsyncServerFilterChainExchangeHandlerFactory.this.exceptionCallback != null) {
/* 117 */             AsyncServerFilterChainExchangeHandlerFactory.this.exceptionCallback.execute(cause);
/*     */           }
/* 119 */           AsyncResponseProducer handler = this.responseProducerRef.get();
/* 120 */           if (handler != null) {
/* 121 */             handler.failed(cause);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 127 */           AsyncDataConsumer dataConsumer = this.dataConsumerRef.get();
/* 128 */           if (dataConsumer != null) {
/* 129 */             dataConsumer.updateCapacity(capacityChannel);
/*     */           } else {
/* 131 */             capacityChannel.update(2147483647);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void consume(ByteBuffer src) throws IOException {
/* 137 */           AsyncDataConsumer dataConsumer = this.dataConsumerRef.get();
/* 138 */           if (dataConsumer != null) {
/* 139 */             dataConsumer.consume(src);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 145 */           AsyncDataConsumer dataConsumer = this.dataConsumerRef.get();
/* 146 */           if (dataConsumer != null) {
/* 147 */             dataConsumer.streamEnd(trailers);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public int available() {
/* 153 */           AsyncResponseProducer responseProducer = this.responseProducerRef.get();
/* 154 */           Asserts.notNull(responseProducer, "Response producer");
/* 155 */           return responseProducer.available();
/*     */         }
/*     */ 
/*     */         
/*     */         public void produce(DataStreamChannel channel) throws IOException {
/* 160 */           AsyncResponseProducer responseProducer = this.responseProducerRef.get();
/* 161 */           Asserts.notNull(responseProducer, "Response producer");
/* 162 */           responseProducer.produce(channel);
/*     */         }
/*     */ 
/*     */         
/*     */         public void releaseResources() {
/* 167 */           AsyncDataConsumer dataConsumer = this.dataConsumerRef.getAndSet(null);
/* 168 */           if (dataConsumer != null) {
/* 169 */             dataConsumer.releaseResources();
/*     */           }
/* 171 */           AsyncResponseProducer responseProducer = this.responseProducerRef.getAndSet(null);
/* 172 */           if (responseProducer != null)
/* 173 */             responseProducer.releaseResources(); 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AsyncServerFilterChainExchangeHandlerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */