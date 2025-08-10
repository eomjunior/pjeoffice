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
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.ResponseChannel;
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
/*     */ public class BasicAsyncServerExpectationDecorator
/*     */   implements AsyncServerExchangeHandler
/*     */ {
/*     */   private final AsyncServerExchangeHandler handler;
/*     */   private final Callback<Exception> exceptionCallback;
/*     */   private final AtomicReference<AsyncResponseProducer> responseProducerRef;
/*     */   
/*     */   public BasicAsyncServerExpectationDecorator(AsyncServerExchangeHandler handler, Callback<Exception> exceptionCallback) {
/*  66 */     this.handler = (AsyncServerExchangeHandler)Args.notNull(handler, "Handler");
/*  67 */     this.exceptionCallback = exceptionCallback;
/*  68 */     this.responseProducerRef = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public BasicAsyncServerExpectationDecorator(AsyncServerExchangeHandler handler) {
/*  72 */     this(handler, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected AsyncResponseProducer verify(HttpRequest request, HttpContext context) throws IOException, HttpException {
/*  78 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void handleRequest(HttpRequest request, EntityDetails entityDetails, ResponseChannel responseChannel, HttpContext context) throws HttpException, IOException {
/*  87 */     if (entityDetails != null) {
/*  88 */       Header h = request.getFirstHeader("Expect");
/*  89 */       if (h != null && "100-continue".equalsIgnoreCase(h.getValue())) {
/*  90 */         AsyncResponseProducer producer = verify(request, context);
/*  91 */         if (producer != null) {
/*  92 */           this.responseProducerRef.set(producer);
/*  93 */           producer.sendResponse(responseChannel, context);
/*     */           return;
/*     */         } 
/*  96 */         responseChannel.sendInformation((HttpResponse)new BasicHttpResponse(100), context);
/*     */       } 
/*     */     } 
/*  99 */     this.handler.handleRequest(request, entityDetails, responseChannel, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 104 */     AsyncResponseProducer responseProducer = this.responseProducerRef.get();
/* 105 */     if (responseProducer == null) {
/* 106 */       this.handler.updateCapacity(capacityChannel);
/*     */     } else {
/* 108 */       capacityChannel.update(2147483647);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void consume(ByteBuffer src) throws IOException {
/* 114 */     AsyncResponseProducer responseProducer = this.responseProducerRef.get();
/* 115 */     if (responseProducer == null) {
/* 116 */       this.handler.consume(src);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 122 */     AsyncResponseProducer responseProducer = this.responseProducerRef.get();
/* 123 */     if (responseProducer == null) {
/* 124 */       this.handler.streamEnd(trailers);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final int available() {
/* 130 */     AsyncResponseProducer responseProducer = this.responseProducerRef.get();
/* 131 */     return (responseProducer == null) ? this.handler.available() : responseProducer.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void produce(DataStreamChannel channel) throws IOException {
/* 136 */     AsyncResponseProducer responseProducer = this.responseProducerRef.get();
/* 137 */     if (responseProducer == null) {
/* 138 */       this.handler.produce(channel);
/*     */     } else {
/* 140 */       responseProducer.produce(channel);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 146 */     if (this.exceptionCallback != null) {
/* 147 */       this.exceptionCallback.execute(cause);
/*     */     }
/* 149 */     AsyncResponseProducer dataProducer = this.responseProducerRef.get();
/* 150 */     if (dataProducer == null) {
/* 151 */       this.handler.failed(cause);
/*     */     } else {
/* 153 */       dataProducer.failed(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void releaseResources() {
/* 159 */     this.handler.releaseResources();
/* 160 */     AsyncResponseProducer dataProducer = this.responseProducerRef.getAndSet(null);
/* 161 */     if (dataProducer != null)
/* 162 */       dataProducer.releaseResources(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/BasicAsyncServerExpectationDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */