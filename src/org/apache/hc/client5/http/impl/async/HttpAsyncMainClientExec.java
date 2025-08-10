/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
/*     */ import org.apache.hc.client5.http.HttpRoute;
/*     */ import org.apache.hc.client5.http.UserTokenHandler;
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
/*     */ import org.apache.hc.core5.util.TimeValue;
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
/*     */ class HttpAsyncMainClientExec
/*     */   implements AsyncExecChainHandler
/*     */ {
/*  80 */   private static final Logger LOG = LoggerFactory.getLogger(HttpAsyncMainClientExec.class);
/*     */   
/*     */   private final HttpProcessor httpProcessor;
/*     */   
/*     */   private final ConnectionKeepAliveStrategy keepAliveStrategy;
/*     */   
/*     */   private final UserTokenHandler userTokenHandler;
/*     */   
/*     */   HttpAsyncMainClientExec(HttpProcessor httpProcessor, ConnectionKeepAliveStrategy keepAliveStrategy, UserTokenHandler userTokenHandler) {
/*  89 */     this.httpProcessor = (HttpProcessor)Args.notNull(httpProcessor, "HTTP protocol processor");
/*  90 */     this.keepAliveStrategy = keepAliveStrategy;
/*  91 */     this.userTokenHandler = userTokenHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(final HttpRequest request, final AsyncEntityProducer entityProducer, AsyncExecChain.Scope scope, AsyncExecChain chain, final AsyncExecCallback asyncExecCallback) throws HttpException, IOException {
/* 101 */     String exchangeId = scope.exchangeId;
/* 102 */     final HttpRoute route = scope.route;
/* 103 */     CancellableDependency operation = scope.cancellableDependency;
/* 104 */     final HttpClientContext clientContext = scope.clientContext;
/* 105 */     final AsyncExecRuntime execRuntime = scope.execRuntime;
/*     */     
/* 107 */     if (LOG.isDebugEnabled()) {
/* 108 */       LOG.debug("{} executing {}", exchangeId, new RequestLine(request));
/*     */     }
/*     */     
/* 111 */     final AtomicInteger messageCountDown = new AtomicInteger(2);
/* 112 */     AsyncClientExchangeHandler internalExchangeHandler = new AsyncClientExchangeHandler()
/*     */       {
/* 114 */         private final AtomicReference<AsyncDataConsumer> entityConsumerRef = new AtomicReference<>();
/*     */ 
/*     */         
/*     */         public void releaseResources() {
/* 118 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.getAndSet(null);
/* 119 */           if (entityConsumer != null) {
/* 120 */             entityConsumer.releaseResources();
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public void failed(Exception cause) {
/* 126 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.getAndSet(null);
/* 127 */           if (entityConsumer != null) {
/* 128 */             entityConsumer.releaseResources();
/*     */           }
/* 130 */           execRuntime.markConnectionNonReusable();
/* 131 */           asyncExecCallback.failed(cause);
/*     */         }
/*     */ 
/*     */         
/*     */         public void cancel() {
/* 136 */           if (messageCountDown.get() > 0) {
/* 137 */             failed(new InterruptedIOException());
/*     */           }
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void produceRequest(RequestChannel channel, HttpContext context) throws HttpException, IOException {
/* 146 */           clientContext.setAttribute("http.route", route);
/* 147 */           clientContext.setAttribute("http.request", request);
/* 148 */           HttpAsyncMainClientExec.this.httpProcessor.process(request, (EntityDetails)entityProducer, (HttpContext)clientContext);
/*     */           
/* 150 */           channel.sendRequest(request, (EntityDetails)entityProducer, context);
/* 151 */           if (entityProducer == null) {
/* 152 */             messageCountDown.decrementAndGet();
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public int available() {
/* 158 */           return entityProducer.available();
/*     */         }
/*     */ 
/*     */         
/*     */         public void produce(final DataStreamChannel channel) throws IOException {
/* 163 */           entityProducer.produce(new DataStreamChannel()
/*     */               {
/*     */                 public void requestOutput()
/*     */                 {
/* 167 */                   channel.requestOutput();
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public int write(ByteBuffer src) throws IOException {
/* 172 */                   return channel.write(src);
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void endStream(List<? extends Header> trailers) throws IOException {
/* 177 */                   channel.endStream(trailers);
/* 178 */                   if (messageCountDown.decrementAndGet() <= 0) {
/* 179 */                     asyncExecCallback.completed();
/*     */                   }
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public void endStream() throws IOException {
/* 185 */                   channel.endStream();
/* 186 */                   if (messageCountDown.decrementAndGet() <= 0) {
/* 187 */                     asyncExecCallback.completed();
/*     */                   }
/*     */                 }
/*     */               });
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void consumeInformation(HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 198 */           asyncExecCallback.handleInformationResponse(response);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext context) throws HttpException, IOException {
/* 207 */           clientContext.setAttribute("http.response", response);
/* 208 */           HttpAsyncMainClientExec.this.httpProcessor.process(response, entityDetails, (HttpContext)clientContext);
/*     */           
/* 210 */           this.entityConsumerRef.set(asyncExecCallback.handleResponse(response, entityDetails));
/* 211 */           if (response.getCode() >= 400) {
/* 212 */             messageCountDown.decrementAndGet();
/*     */           }
/* 214 */           TimeValue keepAliveDuration = HttpAsyncMainClientExec.this.keepAliveStrategy.getKeepAliveDuration(response, (HttpContext)clientContext);
/* 215 */           Object userToken = clientContext.getUserToken();
/* 216 */           if (userToken == null) {
/* 217 */             userToken = HttpAsyncMainClientExec.this.userTokenHandler.getUserToken(route, request, (HttpContext)clientContext);
/* 218 */             clientContext.setAttribute("http.user-token", userToken);
/*     */           } 
/* 220 */           execRuntime.markConnectionReusable(userToken, keepAliveDuration);
/* 221 */           if (entityDetails == null) {
/* 222 */             execRuntime.validateConnection();
/* 223 */             if (messageCountDown.decrementAndGet() <= 0) {
/* 224 */               asyncExecCallback.completed();
/*     */             }
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 231 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.get();
/* 232 */           if (entityConsumer != null) {
/* 233 */             entityConsumer.updateCapacity(capacityChannel);
/*     */           } else {
/* 235 */             capacityChannel.update(2147483647);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public void consume(ByteBuffer src) throws IOException {
/* 241 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.get();
/* 242 */           if (entityConsumer != null) {
/* 243 */             entityConsumer.consume(src);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 249 */           AsyncDataConsumer entityConsumer = this.entityConsumerRef.getAndSet(null);
/* 250 */           if (entityConsumer != null) {
/* 251 */             entityConsumer.streamEnd(trailers);
/*     */           } else {
/* 253 */             execRuntime.validateConnection();
/*     */           } 
/* 255 */           if (messageCountDown.decrementAndGet() <= 0) {
/* 256 */             asyncExecCallback.completed();
/*     */           }
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 262 */     if (LOG.isDebugEnabled()) {
/* 263 */       operation.setDependency(execRuntime.execute(exchangeId, new LoggingAsyncClientExchangeHandler(LOG, exchangeId, internalExchangeHandler), clientContext));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 268 */       operation.setDependency(execRuntime.execute(exchangeId, internalExchangeHandler, clientContext));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/HttpAsyncMainClientExec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */