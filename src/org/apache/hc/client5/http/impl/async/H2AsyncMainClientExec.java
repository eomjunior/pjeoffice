/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.async.AsyncExecCallback;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChain;
/*     */ import org.apache.hc.client5.http.async.AsyncExecChainHandler;
/*     */ import org.apache.hc.client5.http.async.AsyncExecRuntime;
/*     */ import org.apache.hc.client5.http.protocol.HttpClientContext;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.Internal;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.concurrent.CancellableDependency;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.message.RequestLine;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.RequestChannel;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Internal
/*     */ public class H2AsyncMainClientExec
/*     */   implements AsyncExecChainHandler
/*     */ {
/*  75 */   private static final Logger LOG = LoggerFactory.getLogger(H2AsyncMainClientExec.class);
/*     */   
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   H2AsyncMainClientExec(HttpProcessor httpProcessor) {
/*  80 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP protocol processor");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(final HttpRequest request, final AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecChain chain, final AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/*  90 */     String exchangeId = scope.exchangeId;
/*  91 */     final HttpRoute route = scope.route;
/*  92 */     CancellableDependency operation = scope.cancellableDependency;
/*  93 */     final HttpClientContext clientContext = scope.clientContext;
/*  94 */     final AsyncExecRuntime execRuntime = scope.execRuntime;
/*     */     
/*  96 */     if (LOG.isDebugEnabled()) {
/*  97 */       LOG.debug("{} executing {}", exchangeId, new RequestLine(request));
/*     */     }
/*     */     
/* 100 */     AsyncClientExchangeHandler internalExchangeHandler = new AsyncClientExchangeHandler()
/*     */       {
/* 102 */         private final AtomicReference<AsyncDataConsumer> entityConsumerRef = new AtomicReference<>();
/*     */ 
/*     */         
/*     */         public void releaseResources() {
/* 106 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.getAndSet(null);
/* 107 */           if (entityConsumer != null) {
/* 108 */             entityConsumer.releaseResources();
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public void failed(Exception cause) {
/* 114 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.getAndSet(null);
/* 115 */           if (entityConsumer != null) {
/* 116 */             entityConsumer.releaseResources();
/*     */           }
/* 118 */           execRuntime.markConnectionNonReusable();
/* 119 */           asyncExecCallback.failed(cause);
/*     */         }
/*     */ 
/*     */         
/*     */         public void cancel() {
/* 124 */           failed(new InterruptedIOException());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void produceRequest(RequestChannel channel, HttpContext context) throws HttpException, IOException {
/* 130 */           clientContext.setAttribute("http.route", route);
/* 131 */           clientContext.setAttribute("http.request", request);
/* 132 */           H2AsyncMainClientExec.this.httpProcessor.process(request, (EntityDetails)entityProducer, (HttpContext)clientContext);
/*     */           
/* 134 */           channel.sendRequest(request, (EntityDetails)entityProducer, context);
/*     */         }
/*     */ 
/*     */         
/*     */         public int available() {
/* 139 */           return entityProducer.available();
/*     */         }
/*     */ 
/*     */         
/*     */         public void produce(DataStreamChannel channel) throws IOException {
/* 144 */           entityProducer.produce(channel);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void consumeInformation(HttpResponse response, HttpContext context) throws HttpException, IOException {}
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext context) throws HttpException, IOException {
/* 157 */           clientContext.setAttribute("http.response", response);
/* 158 */           H2AsyncMainClientExec.this.httpProcessor.process(response, entityDetails, (HttpContext)clientContext);
/*     */           
/* 160 */           this.entityConsumerRef.set(asyncExecCallback.handleResponse(response, entityDetails));
/* 161 */           if (entityDetails == null) {
/* 162 */             execRuntime.validateConnection();
/* 163 */             asyncExecCallback.completed();
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 169 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.get();
/* 170 */           if (entityConsumer != null) {
/* 171 */             entityConsumer.updateCapacity(capacityChannel);
/*     */           } else {
/* 173 */             capacityChannel.update(2147483647);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void consume(ByteBuffer src) throws IOException {
/* 179 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.get();
/* 180 */           if (entityConsumer != null) {
/* 181 */             entityConsumer.consume(src);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 187 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.getAndSet(null);
/* 188 */           if (entityConsumer != null) {
/* 189 */             entityConsumer.streamEnd(trailers);
/*     */           } else {
/* 191 */             execRuntime.validateConnection();
/*     */           } 
/* 193 */           asyncExecCallback.completed();
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 198 */     if (LOG.isDebugEnabled()) {
/* 199 */       operation.setDependency(execRuntime.execute(exchangeId, new LoggingAsyncClientExchangeHandler(LOG, exchangeId, internalExchangeHandler), clientContext));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 204 */       operation.setDependency(execRuntime.execute(exchangeId, internalExchangeHandler, clientContext));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/H2AsyncMainClientExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */