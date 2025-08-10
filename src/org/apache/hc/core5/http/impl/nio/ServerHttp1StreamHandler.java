/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.hc.core5.http.ConnectionReuseStrategy;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.HttpVersion;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.MisdirectedRequestException;
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.UnsupportedHttpVersionException;
/*     */ import org.apache.hc.core5.http.impl.ServerSupport;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.ResourceHolder;
/*     */ import org.apache.hc.core5.http.nio.ResponseChannel;
/*     */ import org.apache.hc.core5.http.nio.support.BasicResponseProducer;
/*     */ import org.apache.hc.core5.http.nio.support.ImmediateResponseExchangeHandler;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ServerHttp1StreamHandler
/*     */   implements ResourceHolder
/*     */ {
/*     */   private final Http1StreamChannel<HttpResponse> outputChannel;
/*     */   private final DataStreamChannel internalDataChannel;
/*     */   private final ResponseChannel responseChannel;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory;
/*     */   private final ConnectionReuseStrategy connectionReuseStrategy;
/*     */   private final HttpCoreContext context;
/*     */   private final AtomicBoolean responseCommitted;
/*     */   private final AtomicBoolean done;
/*     */   private volatile boolean keepAlive;
/*     */   private volatile AsyncServerExchangeHandler exchangeHandler;
/*     */   private volatile HttpRequest receivedRequest;
/*     */   private volatile MessageState requestState;
/*     */   private volatile MessageState responseState;
/*     */   
/*     */   ServerHttp1StreamHandler(final Http1StreamChannel<HttpResponse> outputChannel, HttpProcessor httpProcessor, ConnectionReuseStrategy connectionReuseStrategy, HandlerFactory<AsyncServerExchangeHandler> exchangeHandlerFactory, HttpCoreContext context) {
/*  89 */     this.outputChannel = outputChannel;
/*  90 */     this.internalDataChannel = new DataStreamChannel()
/*     */       {
/*     */         public void requestOutput()
/*     */         {
/*  94 */           outputChannel.requestOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream(List<? extends Header> trailers) throws IOException {
/*  99 */           outputChannel.complete(trailers);
/* 100 */           if (!ServerHttp1StreamHandler.this.keepAlive) {
/* 101 */             outputChannel.close();
/*     */           }
/* 103 */           ServerHttp1StreamHandler.this.responseState = MessageState.COMPLETE;
/*     */         }
/*     */ 
/*     */         
/*     */         public int write(ByteBuffer src) throws IOException {
/* 108 */           return outputChannel.write(src);
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream() throws IOException {
/* 113 */           endStream(null);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 118 */     this.responseChannel = new ResponseChannel()
/*     */       {
/*     */         public void sendInformation(HttpResponse response, HttpContext httpContext) throws HttpException, IOException
/*     */         {
/* 122 */           ServerHttp1StreamHandler.this.commitInformation(response);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void sendResponse(HttpResponse response, EntityDetails responseEntityDetails, HttpContext httpContext) throws HttpException, IOException {
/* 128 */           ServerSupport.validateResponse(response, responseEntityDetails);
/* 129 */           ServerHttp1StreamHandler.this.commitResponse(response, responseEntityDetails);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void pushPromise(HttpRequest promise, AsyncPushProducer pushProducer, HttpContext httpContext) throws HttpException, IOException {
/* 135 */           ServerHttp1StreamHandler.this.commitPromise();
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 140 */           return super.toString() + " " + ServerHttp1StreamHandler.this;
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 145 */     this.httpProcessor = httpProcessor;
/* 146 */     this.connectionReuseStrategy = connectionReuseStrategy;
/* 147 */     this.exchangeHandlerFactory = exchangeHandlerFactory;
/* 148 */     this.context = context;
/* 149 */     this.responseCommitted = new AtomicBoolean(false);
/* 150 */     this.done = new AtomicBoolean(false);
/* 151 */     this.keepAlive = true;
/* 152 */     this.requestState = MessageState.HEADERS;
/* 153 */     this.responseState = MessageState.IDLE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void commitResponse(HttpResponse response, EntityDetails responseEntityDetails) throws HttpException, IOException {
/* 159 */     if (this.responseCommitted.compareAndSet(false, true)) {
/*     */       
/* 161 */       ProtocolVersion transportVersion = response.getVersion();
/* 162 */       if (transportVersion != null && transportVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2)) {
/* 163 */         throw new UnsupportedHttpVersionException(transportVersion);
/*     */       }
/*     */       
/* 166 */       int status = response.getCode();
/* 167 */       if (status < 200) {
/* 168 */         throw new HttpException("Invalid response: " + status);
/*     */       }
/*     */       
/* 171 */       this.context.setProtocolVersion((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 172 */       this.context.setAttribute("http.response", response);
/* 173 */       this.httpProcessor.process(response, responseEntityDetails, (HttpContext)this.context);
/*     */ 
/*     */       
/* 176 */       boolean endStream = (responseEntityDetails == null || (this.receivedRequest != null && Method.HEAD.isSame(this.receivedRequest.getMethod())));
/*     */       
/* 178 */       if (!this.connectionReuseStrategy.keepAlive(this.receivedRequest, response, (HttpContext)this.context)) {
/* 179 */         this.keepAlive = false;
/*     */       }
/*     */       
/* 182 */       this.outputChannel.submit(response, endStream, endStream ? FlushMode.IMMEDIATE : FlushMode.BUFFER);
/* 183 */       if (endStream) {
/* 184 */         if (!this.keepAlive) {
/* 185 */           this.outputChannel.close();
/*     */         }
/* 187 */         this.responseState = MessageState.COMPLETE;
/*     */       } else {
/* 189 */         this.responseState = MessageState.BODY;
/* 190 */         this.exchangeHandler.produce(this.internalDataChannel);
/*     */       } 
/*     */     } else {
/* 193 */       throw new HttpException("Response already committed");
/*     */     } 
/*     */   }
/*     */   
/*     */   private void commitInformation(HttpResponse response) throws IOException, HttpException {
/* 198 */     if (this.responseCommitted.get()) {
/* 199 */       throw new HttpException("Response already committed");
/*     */     }
/* 201 */     int status = response.getCode();
/* 202 */     if (status < 100 || status >= 200) {
/* 203 */       throw new HttpException("Invalid intermediate response: " + status);
/*     */     }
/* 205 */     this.outputChannel.submit(response, true, FlushMode.IMMEDIATE);
/*     */   }
/*     */   
/*     */   private void commitPromise() throws HttpException {
/* 209 */     throw new HttpException("HTTP/1.1 does not support server push");
/*     */   }
/*     */   
/*     */   void activateChannel() throws IOException, HttpException {
/* 213 */     this.outputChannel.activate();
/*     */   }
/*     */   
/*     */   boolean isResponseFinal() {
/* 217 */     return (this.responseState == MessageState.COMPLETE);
/*     */   }
/*     */   
/*     */   boolean keepAlive() {
/* 221 */     return this.keepAlive;
/*     */   }
/*     */   
/*     */   boolean isCompleted() {
/* 225 */     return (this.requestState == MessageState.COMPLETE && this.responseState == MessageState.COMPLETE);
/*     */   }
/*     */   
/*     */   void terminateExchange(HttpException ex) throws HttpException, IOException {
/* 229 */     if (this.done.get() || this.requestState != MessageState.HEADERS) {
/* 230 */       throw new ProtocolException("Unexpected message head");
/*     */     }
/* 232 */     this.receivedRequest = null;
/* 233 */     this.requestState = MessageState.COMPLETE;
/* 234 */     BasicHttpResponse basicHttpResponse = new BasicHttpResponse(ServerSupport.toStatusCode((Exception)ex));
/* 235 */     basicHttpResponse.addHeader("Connection", "close");
/* 236 */     BasicResponseProducer basicResponseProducer = new BasicResponseProducer((HttpResponse)basicHttpResponse, ServerSupport.toErrorMessage((Exception)ex));
/* 237 */     this.exchangeHandler = (AsyncServerExchangeHandler)new ImmediateResponseExchangeHandler((AsyncResponseProducer)basicResponseProducer);
/* 238 */     this.exchangeHandler.handleRequest(null, null, this.responseChannel, (HttpContext)this.context);
/*     */   }
/*     */   void consumeHeader(HttpRequest request, EntityDetails requestEntityDetails) throws HttpException, IOException {
/*     */     ImmediateResponseExchangeHandler immediateResponseExchangeHandler;
/* 242 */     if (this.done.get() || this.requestState != MessageState.HEADERS) {
/* 243 */       throw new ProtocolException("Unexpected message head");
/*     */     }
/* 245 */     this.receivedRequest = request;
/* 246 */     this.requestState = (requestEntityDetails == null) ? MessageState.COMPLETE : MessageState.BODY;
/*     */ 
/*     */     
/*     */     try {
/* 250 */       AsyncServerExchangeHandler handler = (AsyncServerExchangeHandler)this.exchangeHandlerFactory.create(request, (HttpContext)this.context);
/* 251 */     } catch (MisdirectedRequestException ex) {
/* 252 */       immediateResponseExchangeHandler = new ImmediateResponseExchangeHandler(421, ex.getMessage());
/* 253 */     } catch (HttpException ex) {
/* 254 */       immediateResponseExchangeHandler = new ImmediateResponseExchangeHandler(500, ex.getMessage());
/*     */     } 
/* 256 */     if (immediateResponseExchangeHandler == null) {
/* 257 */       immediateResponseExchangeHandler = new ImmediateResponseExchangeHandler(404, "Cannot handle request");
/*     */     }
/*     */     
/* 260 */     this.exchangeHandler = (AsyncServerExchangeHandler)immediateResponseExchangeHandler;
/*     */     
/* 262 */     ProtocolVersion transportVersion = request.getVersion();
/* 263 */     if (transportVersion != null && transportVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2)) {
/* 264 */       throw new UnsupportedHttpVersionException(transportVersion);
/*     */     }
/* 266 */     this.context.setProtocolVersion((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 267 */     this.context.setAttribute("http.request", request);
/*     */     
/*     */     try {
/* 270 */       this.httpProcessor.process(request, requestEntityDetails, (HttpContext)this.context);
/* 271 */       this.exchangeHandler.handleRequest(request, requestEntityDetails, this.responseChannel, (HttpContext)this.context);
/* 272 */     } catch (HttpException ex) {
/* 273 */       if (!this.responseCommitted.get()) {
/* 274 */         BasicHttpResponse basicHttpResponse = new BasicHttpResponse(ServerSupport.toStatusCode((Exception)ex));
/* 275 */         basicHttpResponse.addHeader("Connection", "close");
/* 276 */         BasicResponseProducer basicResponseProducer = new BasicResponseProducer((HttpResponse)basicHttpResponse, ServerSupport.toErrorMessage((Exception)ex));
/* 277 */         this.exchangeHandler = (AsyncServerExchangeHandler)new ImmediateResponseExchangeHandler((AsyncResponseProducer)basicResponseProducer);
/* 278 */         this.exchangeHandler.handleRequest(request, requestEntityDetails, this.responseChannel, (HttpContext)this.context);
/*     */       } else {
/* 280 */         throw ex;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isOutputReady() {
/* 287 */     switch (this.responseState) {
/*     */       case BODY:
/* 289 */         return (this.exchangeHandler.available() > 0);
/*     */     } 
/* 291 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   void produceOutput() throws HttpException, IOException {
/* 296 */     switch (this.responseState) {
/*     */       case BODY:
/* 298 */         this.exchangeHandler.produce(this.internalDataChannel);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   void consumeData(ByteBuffer src) throws HttpException, IOException {
/* 304 */     if (this.done.get() || this.requestState != MessageState.BODY) {
/* 305 */       throw new ProtocolException("Unexpected message data");
/*     */     }
/* 307 */     if (this.responseState == MessageState.ACK) {
/* 308 */       this.outputChannel.requestOutput();
/*     */     }
/* 310 */     this.exchangeHandler.consume(src);
/*     */   }
/*     */   
/*     */   void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 314 */     this.exchangeHandler.updateCapacity(capacityChannel);
/*     */   }
/*     */   
/*     */   void dataEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 318 */     if (this.done.get() || this.requestState != MessageState.BODY) {
/* 319 */       throw new ProtocolException("Unexpected message data");
/*     */     }
/* 321 */     this.requestState = MessageState.COMPLETE;
/* 322 */     this.exchangeHandler.streamEnd(trailers);
/*     */   }
/*     */   
/*     */   void failed(Exception cause) {
/* 326 */     if (!this.done.get()) {
/* 327 */       this.exchangeHandler.failed(cause);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 333 */     if (this.done.compareAndSet(false, true)) {
/* 334 */       this.requestState = MessageState.COMPLETE;
/* 335 */       this.responseState = MessageState.COMPLETE;
/* 336 */       this.exchangeHandler.releaseResources();
/*     */     } 
/*     */   }
/*     */   
/*     */   void appendState(StringBuilder buf) {
/* 341 */     buf.append("requestState=").append(this.requestState)
/* 342 */       .append(", responseState=").append(this.responseState)
/* 343 */       .append(", responseCommitted=").append(this.responseCommitted)
/* 344 */       .append(", keepAlive=").append(this.keepAlive)
/* 345 */       .append(", done=").append(this.done);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 350 */     StringBuilder buf = new StringBuilder();
/* 351 */     buf.append("[");
/* 352 */     appendState(buf);
/* 353 */     buf.append("]");
/* 354 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ServerHttp1StreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */