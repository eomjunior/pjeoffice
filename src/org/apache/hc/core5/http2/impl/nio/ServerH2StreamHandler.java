/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.MessageHeaders;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.impl.IncomingEntityDetails;
/*     */ import org.apache.hc.core5.http.impl.ServerSupport;
/*     */ import org.apache.hc.core5.http.impl.nio.MessageState;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.ResponseChannel;
/*     */ import org.apache.hc.core5.http.nio.support.BasicResponseProducer;
/*     */ import org.apache.hc.core5.http.nio.support.ImmediateResponseExchangeHandler;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http2.H2ConnectionException;
/*     */ import org.apache.hc.core5.http2.H2Error;
/*     */ import org.apache.hc.core5.http2.H2StreamResetException;
/*     */ import org.apache.hc.core5.http2.impl.DefaultH2RequestConverter;
/*     */ import org.apache.hc.core5.http2.impl.DefaultH2ResponseConverter;
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
/*     */ class ServerH2StreamHandler
/*     */   implements H2StreamHandler
/*     */ {
/*     */   private final H2StreamChannel outputChannel;
/*     */   private final DataStreamChannel dataChannel;
/*     */   private final ResponseChannel responseChannel;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final BasicHttpConnectionMetrics connMetrics;
/*     */   private final HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory;
/*     */   private final HttpCoreContext context;
/*     */   private final AtomicBoolean responseCommitted;
/*     */   private final AtomicBoolean failed;
/*     */   private final AtomicBoolean done;
/*     */   private volatile AsyncServerExchangeHandler exchangeHandler;
/*     */   private volatile HttpRequest receivedRequest;
/*     */   private volatile MessageState requestState;
/*     */   private volatile MessageState responseState;
/*     */   
/*     */   ServerH2StreamHandler(final H2StreamChannel outputChannel, HttpProcessor httpProcessor, BasicHttpConnectionMetrics connMetrics, HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory, HttpCoreContext context) {
/*  90 */     this.outputChannel = outputChannel;
/*  91 */     this.dataChannel = new DataStreamChannel()
/*     */       {
/*     */         public void requestOutput()
/*     */         {
/*  95 */           outputChannel.requestOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public int write(ByteBuffer src) throws IOException {
/* 100 */           return outputChannel.write(src);
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream(List<? extends Header> trailers) throws IOException {
/* 105 */           outputChannel.endStream(trailers);
/* 106 */           ServerH2StreamHandler.this.responseState = MessageState.COMPLETE;
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream() throws IOException {
/* 111 */           outputChannel.endStream();
/* 112 */           ServerH2StreamHandler.this.responseState = MessageState.COMPLETE;
/*     */         }
/*     */       };
/*     */     
/* 116 */     this.responseChannel = new ResponseChannel()
/*     */       {
/*     */         public void sendInformation(HttpResponse response, HttpContext httpContext) throws HttpException, IOException
/*     */         {
/* 120 */           ServerH2StreamHandler.this.commitInformation(response);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void sendResponse(HttpResponse response, EntityDetails responseEntityDetails, HttpContext httpContext) throws HttpException, IOException {
/* 126 */           ServerSupport.validateResponse(response, responseEntityDetails);
/* 127 */           ServerH2StreamHandler.this.commitResponse(response, responseEntityDetails);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void pushPromise(HttpRequest promise, AsyncPushProducer pushProducer, HttpContext httpContext) throws HttpException, IOException {
/* 133 */           ServerH2StreamHandler.this.commitPromise(promise, pushProducer);
/*     */         }
/*     */       };
/*     */     
/* 137 */     this.httpProcessor = httpProcessor;
/* 138 */     this.connMetrics = connMetrics;
/* 139 */     this.exchangeHandlerFactory = exchangeHandlerFactory;
/* 140 */     this.context = context;
/* 141 */     this.responseCommitted = new AtomicBoolean(false);
/* 142 */     this.failed = new AtomicBoolean(false);
/* 143 */     this.done = new AtomicBoolean(false);
/* 144 */     this.requestState = MessageState.HEADERS;
/* 145 */     this.responseState = MessageState.IDLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public HandlerFactory<AsyncPushConsumer> getPushHandlerFactory() {
/* 150 */     return null;
/*     */   }
/*     */   
/*     */   private void commitInformation(HttpResponse response) throws IOException, HttpException {
/* 154 */     if (this.responseCommitted.get()) {
/* 155 */       throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Response already committed");
/*     */     }
/* 157 */     int status = response.getCode();
/* 158 */     if (status < 100 || status >= 200) {
/* 159 */       throw new HttpException("Invalid intermediate response: " + status);
/*     */     }
/* 161 */     List<Header> responseHeaders = DefaultH2ResponseConverter.INSTANCE.convert(response);
/* 162 */     this.outputChannel.submit(responseHeaders, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void commitResponse(HttpResponse response, EntityDetails responseEntityDetails) throws HttpException, IOException {
/* 168 */     if (this.responseCommitted.compareAndSet(false, true)) {
/*     */       
/* 170 */       int status = response.getCode();
/* 171 */       if (status < 200) {
/* 172 */         throw new HttpException("Invalid response: " + status);
/*     */       }
/* 174 */       this.context.setAttribute("http.response", response);
/* 175 */       this.httpProcessor.process(response, responseEntityDetails, (HttpContext)this.context);
/*     */       
/* 177 */       List<Header> responseHeaders = DefaultH2ResponseConverter.INSTANCE.convert(response);
/*     */ 
/*     */       
/* 180 */       boolean endStream = (responseEntityDetails == null || (this.receivedRequest != null && Method.HEAD.isSame(this.receivedRequest.getMethod())));
/* 181 */       this.outputChannel.submit(responseHeaders, endStream);
/* 182 */       this.connMetrics.incrementResponseCount();
/* 183 */       if (responseEntityDetails == null) {
/* 184 */         this.responseState = MessageState.COMPLETE;
/*     */       } else {
/* 186 */         this.responseState = MessageState.BODY;
/* 187 */         this.exchangeHandler.produce(this.outputChannel);
/*     */       } 
/*     */     } else {
/* 190 */       throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Response already committed");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void commitPromise(HttpRequest promise, AsyncPushProducer pushProducer) throws HttpException, IOException {
/* 198 */     this.httpProcessor.process(promise, null, (HttpContext)this.context);
/*     */     
/* 200 */     List<Header> promiseHeaders = DefaultH2RequestConverter.INSTANCE.convert(promise);
/* 201 */     this.outputChannel.push(promiseHeaders, pushProducer);
/* 202 */     this.connMetrics.incrementRequestCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumePromise(List<Header> headers) throws HttpException, IOException {
/* 207 */     throw new ProtocolException("Unexpected message promise");
/*     */   } public void consumeHeader(List<Header> headers, boolean endStream) throws HttpException, IOException {
/*     */     HttpRequest request;
/*     */     IncomingEntityDetails incomingEntityDetails;
/*     */     AsyncServerExchangeHandler handler;
/* 212 */     if (this.done.get()) {
/* 213 */       throw new ProtocolException("Unexpected message headers");
/*     */     }
/* 215 */     switch (this.requestState) {
/*     */       case HEADERS:
/* 217 */         this.requestState = endStream ? MessageState.COMPLETE : MessageState.BODY;
/*     */         
/* 219 */         request = DefaultH2RequestConverter.INSTANCE.convert(headers);
/* 220 */         incomingEntityDetails = endStream ? null : new IncomingEntityDetails((MessageHeaders)request, -1L);
/*     */ 
/*     */         
/*     */         try {
/* 224 */           handler = (this.exchangeHandlerFactory != null) ? (AsyncServerExchangeHandler)this.exchangeHandlerFactory.create(request, (HttpContext)this.context) : null;
/* 225 */         } catch (ProtocolException ex) {
/* 226 */           throw new H2StreamResetException(H2Error.PROTOCOL_ERROR, ex.getMessage());
/*     */         } 
/* 228 */         if (handler == null) {
/* 229 */           throw new H2StreamResetException(H2Error.REFUSED_STREAM, "Stream refused");
/*     */         }
/* 231 */         this.exchangeHandler = handler;
/*     */         
/* 233 */         this.context.setProtocolVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 234 */         this.context.setAttribute("http.request", request);
/*     */         
/*     */         try {
/* 237 */           this.httpProcessor.process(request, (EntityDetails)incomingEntityDetails, (HttpContext)this.context);
/* 238 */           this.connMetrics.incrementRequestCount();
/* 239 */           this.receivedRequest = request;
/*     */           
/* 241 */           this.exchangeHandler.handleRequest(request, (EntityDetails)incomingEntityDetails, this.responseChannel, (HttpContext)this.context);
/* 242 */         } catch (HttpException ex) {
/* 243 */           if (!this.responseCommitted.get()) {
/*     */ 
/*     */             
/* 246 */             BasicResponseProducer basicResponseProducer = new BasicResponseProducer(ServerSupport.toStatusCode((Exception)ex), ServerSupport.toErrorMessage((Exception)ex));
/* 247 */             this.exchangeHandler = (AsyncServerExchangeHandler)new ImmediateResponseExchangeHandler((AsyncResponseProducer)basicResponseProducer);
/* 248 */             this.exchangeHandler.handleRequest(request, (EntityDetails)incomingEntityDetails, this.responseChannel, (HttpContext)this.context);
/*     */           } else {
/* 250 */             throw ex;
/*     */           } 
/*     */         } 
/*     */         return;
/*     */       case BODY:
/* 255 */         this.responseState = MessageState.COMPLETE;
/* 256 */         this.exchangeHandler.streamEnd(headers);
/*     */         return;
/*     */     } 
/* 259 */     throw new ProtocolException("Unexpected message headers");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateInputCapacity() throws IOException {
/* 265 */     Asserts.notNull(this.exchangeHandler, "Exchange handler");
/* 266 */     this.exchangeHandler.updateCapacity(this.outputChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeData(ByteBuffer src, boolean endStream) throws HttpException, IOException {
/* 271 */     if (this.done.get() || this.requestState != MessageState.BODY) {
/* 272 */       throw new ProtocolException("Unexpected message data");
/*     */     }
/* 274 */     Asserts.notNull(this.exchangeHandler, "Exchange handler");
/* 275 */     if (src != null) {
/* 276 */       this.exchangeHandler.consume(src);
/*     */     }
/* 278 */     if (endStream) {
/* 279 */       this.requestState = MessageState.COMPLETE;
/* 280 */       this.exchangeHandler.streamEnd(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutputReady() {
/* 286 */     return (this.responseState == MessageState.BODY && this.exchangeHandler != null && this.exchangeHandler.available() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceOutput() throws HttpException, IOException {
/* 291 */     if (this.responseState == MessageState.BODY) {
/* 292 */       Asserts.notNull(this.exchangeHandler, "Exchange handler");
/* 293 */       this.exchangeHandler.produce(this.dataChannel);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(HttpException ex, boolean endStream) throws HttpException, IOException {
/* 299 */     if (this.done.get()) {
/* 300 */       throw ex;
/*     */     }
/* 302 */     switch (this.requestState) {
/*     */       case HEADERS:
/* 304 */         this.requestState = endStream ? MessageState.COMPLETE : MessageState.BODY;
/* 305 */         if (!this.responseCommitted.get()) {
/*     */ 
/*     */           
/* 308 */           BasicResponseProducer basicResponseProducer = new BasicResponseProducer(ServerSupport.toStatusCode((Exception)ex), ServerSupport.toErrorMessage((Exception)ex));
/* 309 */           this.exchangeHandler = (AsyncServerExchangeHandler)new ImmediateResponseExchangeHandler((AsyncResponseProducer)basicResponseProducer);
/* 310 */           this.exchangeHandler.handleRequest(null, null, this.responseChannel, (HttpContext)this.context);
/*     */         } else {
/* 312 */           throw ex;
/*     */         } 
/*     */         return;
/*     */       case BODY:
/* 316 */         this.responseState = MessageState.COMPLETE; break;
/*     */     } 
/* 318 */     throw ex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*     */     try {
/* 325 */       if (this.failed.compareAndSet(false, true) && 
/* 326 */         this.exchangeHandler != null) {
/* 327 */         this.exchangeHandler.failed(cause);
/*     */       }
/*     */     } finally {
/*     */       
/* 331 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 337 */     if (this.done.compareAndSet(false, true)) {
/* 338 */       this.requestState = MessageState.COMPLETE;
/* 339 */       this.responseState = MessageState.COMPLETE;
/* 340 */       if (this.exchangeHandler != null) {
/* 341 */         this.exchangeHandler.releaseResources();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 348 */     return "[requestState=" + this.requestState + ", responseState=" + this.responseState + ']';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ServerH2StreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */