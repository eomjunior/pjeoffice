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
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.UnsupportedHttpVersionException;
/*     */ import org.apache.hc.core5.http.config.Http1Config;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.ResourceHolder;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.util.Timeout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ClientHttp1StreamHandler
/*     */   implements ResourceHolder
/*     */ {
/*     */   private final Http1StreamChannel<HttpRequest> outputChannel;
/*     */   private final DataStreamChannel internalDataChannel;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final Http1Config http1Config;
/*     */   private final ConnectionReuseStrategy connectionReuseStrategy;
/*     */   private final AsyncClientExchangeHandler exchangeHandler;
/*     */   private final HttpCoreContext context;
/*     */   private final AtomicBoolean requestCommitted;
/*     */   private final AtomicBoolean done;
/*     */   private volatile boolean keepAlive;
/*     */   private volatile Timeout timeout;
/*     */   private volatile HttpRequest committedRequest;
/*     */   private volatile MessageState requestState;
/*     */   private volatile MessageState responseState;
/*     */   
/*     */   ClientHttp1StreamHandler(final Http1StreamChannel<HttpRequest> outputChannel, HttpProcessor httpProcessor, Http1Config http1Config, ConnectionReuseStrategy connectionReuseStrategy, AsyncClientExchangeHandler exchangeHandler, HttpCoreContext context) {
/*  82 */     this.outputChannel = outputChannel;
/*  83 */     this.internalDataChannel = new DataStreamChannel()
/*     */       {
/*     */         public void requestOutput()
/*     */         {
/*  87 */           outputChannel.requestOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream(List<? extends Header> trailers) throws IOException {
/*  92 */           outputChannel.complete(trailers);
/*  93 */           ClientHttp1StreamHandler.this.requestState = MessageState.COMPLETE;
/*     */         }
/*     */ 
/*     */         
/*     */         public int write(ByteBuffer src) throws IOException {
/*  98 */           return outputChannel.write(src);
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream() throws IOException {
/* 103 */           endStream(null);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 108 */     this.httpProcessor = httpProcessor;
/* 109 */     this.http1Config = http1Config;
/* 110 */     this.connectionReuseStrategy = connectionReuseStrategy;
/* 111 */     this.exchangeHandler = exchangeHandler;
/* 112 */     this.context = context;
/* 113 */     this.requestCommitted = new AtomicBoolean(false);
/* 114 */     this.done = new AtomicBoolean(false);
/* 115 */     this.keepAlive = true;
/* 116 */     this.requestState = MessageState.IDLE;
/* 117 */     this.responseState = MessageState.HEADERS;
/*     */   }
/*     */   
/*     */   boolean isResponseFinal() {
/* 121 */     return (this.responseState == MessageState.COMPLETE);
/*     */   }
/*     */   
/*     */   boolean isCompleted() {
/* 125 */     return (this.requestState == MessageState.COMPLETE && this.responseState == MessageState.COMPLETE);
/*     */   }
/*     */   
/*     */   String getRequestMethod() {
/* 129 */     return (this.committedRequest != null) ? this.committedRequest.getMethod() : null;
/*     */   }
/*     */   
/*     */   boolean isOutputReady() {
/* 133 */     switch (this.requestState) {
/*     */       case IDLE:
/*     */       case ACK:
/* 136 */         return true;
/*     */       case BODY:
/* 138 */         return (this.exchangeHandler.available() > 0);
/*     */     } 
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void commitRequest(HttpRequest request, EntityDetails entityDetails) throws IOException, HttpException {
/* 145 */     if (this.requestCommitted.compareAndSet(false, true)) {
/* 146 */       ProtocolVersion transportVersion = request.getVersion();
/* 147 */       if (transportVersion != null && transportVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2)) {
/* 148 */         throw new UnsupportedHttpVersionException(transportVersion);
/*     */       }
/* 150 */       this.context.setProtocolVersion((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 151 */       this.context.setAttribute("http.request", request);
/*     */       
/* 153 */       this.httpProcessor.process(request, entityDetails, (HttpContext)this.context);
/*     */       
/* 155 */       boolean endStream = (entityDetails == null);
/* 156 */       if (endStream) {
/* 157 */         this.outputChannel.submit(request, true, FlushMode.IMMEDIATE);
/* 158 */         this.committedRequest = request;
/* 159 */         this.requestState = MessageState.COMPLETE;
/*     */       } else {
/* 161 */         Header h = request.getFirstHeader("Expect");
/* 162 */         boolean expectContinue = (h != null && "100-continue".equalsIgnoreCase(h.getValue()));
/* 163 */         this.outputChannel.submit(request, false, expectContinue ? FlushMode.IMMEDIATE : FlushMode.BUFFER);
/* 164 */         this.committedRequest = request;
/* 165 */         if (expectContinue) {
/* 166 */           this.requestState = MessageState.ACK;
/* 167 */           this.timeout = this.outputChannel.getSocketTimeout();
/* 168 */           this.outputChannel.setSocketTimeout(this.http1Config.getWaitForContinueTimeout());
/*     */         } else {
/* 170 */           this.requestState = MessageState.BODY;
/* 171 */           this.exchangeHandler.produce(this.internalDataChannel);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 175 */       throw new HttpException("Request already committed");
/*     */     } 
/*     */   }
/*     */   
/*     */   void produceOutput() throws HttpException, IOException {
/* 180 */     switch (this.requestState) {
/*     */       case IDLE:
/* 182 */         this.requestState = MessageState.HEADERS;
/* 183 */         this.exchangeHandler.produceRequest((request, entityDetails, httpContext) -> commitRequest(request, entityDetails), (HttpContext)this.context);
/*     */         break;
/*     */       case ACK:
/* 186 */         this.outputChannel.suspendOutput();
/*     */         break;
/*     */       case BODY:
/* 189 */         this.exchangeHandler.produce(this.internalDataChannel);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   void consumeHeader(HttpResponse response, EntityDetails entityDetails) throws HttpException, IOException {
/* 195 */     if (this.done.get() || this.responseState != MessageState.HEADERS) {
/* 196 */       throw new ProtocolException("Unexpected message head");
/*     */     }
/* 198 */     ProtocolVersion transportVersion = response.getVersion();
/* 199 */     if (transportVersion != null && transportVersion.greaterEquals((ProtocolVersion)HttpVersion.HTTP_2)) {
/* 200 */       throw new UnsupportedHttpVersionException(transportVersion);
/*     */     }
/*     */     
/* 203 */     int status = response.getCode();
/* 204 */     if (status < 100) {
/* 205 */       throw new ProtocolException("Invalid response: " + new StatusLine(response));
/*     */     }
/* 207 */     if (status > 100 && status < 200) {
/* 208 */       this.exchangeHandler.consumeInformation(response, (HttpContext)this.context);
/*     */     }
/* 210 */     else if (!this.connectionReuseStrategy.keepAlive(this.committedRequest, response, (HttpContext)this.context)) {
/* 211 */       this.keepAlive = false;
/*     */     } 
/*     */     
/* 214 */     if (this.requestState == MessageState.ACK && (
/* 215 */       status == 100 || status >= 200)) {
/* 216 */       this.outputChannel.setSocketTimeout(this.timeout);
/* 217 */       this.requestState = MessageState.BODY;
/* 218 */       if (status < 400) {
/* 219 */         this.exchangeHandler.produce(this.internalDataChannel);
/*     */       }
/*     */     } 
/*     */     
/* 223 */     if (status < 200) {
/*     */       return;
/*     */     }
/* 226 */     if (this.requestState == MessageState.BODY && 
/* 227 */       status >= 400) {
/* 228 */       this.requestState = MessageState.COMPLETE;
/* 229 */       if (!this.outputChannel.abortGracefully()) {
/* 230 */         this.keepAlive = false;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 235 */     this.context.setProtocolVersion((transportVersion != null) ? transportVersion : (ProtocolVersion)HttpVersion.HTTP_1_1);
/* 236 */     this.context.setAttribute("http.response", response);
/* 237 */     this.httpProcessor.process(response, entityDetails, (HttpContext)this.context);
/*     */     
/* 239 */     if (entityDetails == null && !this.keepAlive) {
/* 240 */       this.outputChannel.close();
/*     */     }
/*     */     
/* 243 */     this.exchangeHandler.consumeResponse(response, entityDetails, (HttpContext)this.context);
/* 244 */     if (entityDetails == null) {
/* 245 */       this.responseState = MessageState.COMPLETE;
/*     */     } else {
/* 247 */       this.responseState = MessageState.BODY;
/*     */     } 
/*     */   }
/*     */   
/*     */   void consumeData(ByteBuffer src) throws HttpException, IOException {
/* 252 */     if (this.done.get() || this.responseState != MessageState.BODY) {
/* 253 */       throw new ProtocolException("Unexpected message data");
/*     */     }
/* 255 */     this.exchangeHandler.consume(src);
/*     */   }
/*     */   
/*     */   void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 259 */     this.exchangeHandler.updateCapacity(capacityChannel);
/*     */   }
/*     */   
/*     */   void dataEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 263 */     if (this.done.get() || this.responseState != MessageState.BODY) {
/* 264 */       throw new ProtocolException("Unexpected message data");
/*     */     }
/* 266 */     if (!this.keepAlive) {
/* 267 */       this.outputChannel.close();
/*     */     }
/* 269 */     this.responseState = MessageState.COMPLETE;
/* 270 */     this.exchangeHandler.streamEnd(trailers);
/*     */   }
/*     */   
/*     */   boolean handleTimeout() {
/* 274 */     if (this.requestState == MessageState.ACK) {
/* 275 */       this.requestState = MessageState.BODY;
/* 276 */       this.outputChannel.setSocketTimeout(this.timeout);
/* 277 */       this.outputChannel.requestOutput();
/* 278 */       return true;
/*     */     } 
/* 280 */     return false;
/*     */   }
/*     */   
/*     */   void failed(Exception cause) {
/* 284 */     if (!this.done.get()) {
/* 285 */       this.exchangeHandler.failed(cause);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 291 */     if (this.done.compareAndSet(false, true)) {
/* 292 */       this.responseState = MessageState.COMPLETE;
/* 293 */       this.requestState = MessageState.COMPLETE;
/* 294 */       this.exchangeHandler.releaseResources();
/*     */     } 
/*     */   }
/*     */   
/*     */   void appendState(StringBuilder buf) {
/* 299 */     buf.append("requestState=").append(this.requestState)
/* 300 */       .append(", responseState=").append(this.responseState)
/* 301 */       .append(", responseCommitted=").append(this.requestCommitted)
/* 302 */       .append(", keepAlive=").append(this.keepAlive)
/* 303 */       .append(", done=").append(this.done);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 308 */     StringBuilder buf = new StringBuilder();
/* 309 */     buf.append("[");
/* 310 */     appendState(buf);
/* 311 */     buf.append("]");
/* 312 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ClientHttp1StreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */