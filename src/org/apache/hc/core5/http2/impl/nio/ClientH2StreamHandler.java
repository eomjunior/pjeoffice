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
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.impl.IncomingEntityDetails;
/*     */ import org.apache.hc.core5.http.impl.nio.MessageState;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpCoreContext;
/*     */ import org.apache.hc.core5.http.protocol.HttpProcessor;
/*     */ import org.apache.hc.core5.http2.H2ConnectionException;
/*     */ import org.apache.hc.core5.http2.H2Error;
/*     */ import org.apache.hc.core5.http2.impl.DefaultH2RequestConverter;
/*     */ import org.apache.hc.core5.http2.impl.DefaultH2ResponseConverter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ClientH2StreamHandler
/*     */   implements H2StreamHandler
/*     */ {
/*     */   private final H2StreamChannel outputChannel;
/*     */   private final DataStreamChannel dataChannel;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final BasicHttpConnectionMetrics connMetrics;
/*     */   private final AsyncClientExchangeHandler exchangeHandler;
/*     */   private final HandlerFactory<AsyncPushConsumer> pushHandlerFactory;
/*     */   private final HttpCoreContext context;
/*     */   private final AtomicBoolean requestCommitted;
/*     */   private final AtomicBoolean failed;
/*     */   private final AtomicBoolean done;
/*     */   private volatile MessageState requestState;
/*     */   private volatile MessageState responseState;
/*     */   
/*     */   ClientH2StreamHandler(final H2StreamChannel outputChannel, HttpProcessor httpProcessor, BasicHttpConnectionMetrics connMetrics, AsyncClientExchangeHandler exchangeHandler, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpCoreContext context) {
/*  82 */     this.outputChannel = outputChannel;
/*  83 */     this.dataChannel = new DataStreamChannel()
/*     */       {
/*     */         public void requestOutput()
/*     */         {
/*  87 */           outputChannel.requestOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public int write(ByteBuffer src) throws IOException {
/*  92 */           return outputChannel.write(src);
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream(List<? extends Header> trailers) throws IOException {
/*  97 */           outputChannel.endStream(trailers);
/*  98 */           ClientH2StreamHandler.this.requestState = MessageState.COMPLETE;
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream() throws IOException {
/* 103 */           outputChannel.endStream();
/* 104 */           ClientH2StreamHandler.this.requestState = MessageState.COMPLETE;
/*     */         }
/*     */       };
/*     */     
/* 108 */     this.httpProcessor = httpProcessor;
/* 109 */     this.connMetrics = connMetrics;
/* 110 */     this.exchangeHandler = exchangeHandler;
/* 111 */     this.pushHandlerFactory = pushHandlerFactory;
/* 112 */     this.context = context;
/* 113 */     this.requestCommitted = new AtomicBoolean(false);
/* 114 */     this.failed = new AtomicBoolean(false);
/* 115 */     this.done = new AtomicBoolean(false);
/* 116 */     this.requestState = MessageState.HEADERS;
/* 117 */     this.responseState = MessageState.HEADERS;
/*     */   }
/*     */ 
/*     */   
/*     */   public HandlerFactory<AsyncPushConsumer> getPushHandlerFactory() {
/* 122 */     return this.pushHandlerFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutputReady() {
/* 127 */     switch (this.requestState) {
/*     */       case HEADERS:
/* 129 */         return true;
/*     */       case BODY:
/* 131 */         return (this.exchangeHandler.available() > 0);
/*     */     } 
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void commitRequest(HttpRequest request, EntityDetails entityDetails) throws HttpException, IOException {
/* 138 */     if (this.requestCommitted.compareAndSet(false, true)) {
/* 139 */       this.context.setProtocolVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 140 */       this.context.setAttribute("http.request", request);
/*     */       
/* 142 */       this.httpProcessor.process(request, entityDetails, (HttpContext)this.context);
/*     */       
/* 144 */       List<Header> headers = DefaultH2RequestConverter.INSTANCE.convert(request);
/* 145 */       this.outputChannel.submit(headers, (entityDetails == null));
/* 146 */       this.connMetrics.incrementRequestCount();
/*     */       
/* 148 */       if (entityDetails == null) {
/* 149 */         this.requestState = MessageState.COMPLETE;
/*     */       } else {
/* 151 */         Header h = request.getFirstHeader("Expect");
/* 152 */         boolean expectContinue = (h != null && "100-continue".equalsIgnoreCase(h.getValue()));
/* 153 */         if (expectContinue) {
/* 154 */           this.requestState = MessageState.ACK;
/*     */         } else {
/* 156 */           this.requestState = MessageState.BODY;
/* 157 */           this.exchangeHandler.produce(this.dataChannel);
/*     */         } 
/*     */       } 
/*     */     } else {
/* 161 */       throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Request already committed");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceOutput() throws HttpException, IOException {
/* 167 */     switch (this.requestState) {
/*     */       case HEADERS:
/* 169 */         this.exchangeHandler.produceRequest((request, entityDetails, httpContext) -> commitRequest(request, entityDetails), (HttpContext)this.context);
/*     */         break;
/*     */       case BODY:
/* 172 */         this.exchangeHandler.produce(this.dataChannel);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumePromise(List<Header> headers) throws HttpException, IOException {
/* 179 */     throw new ProtocolException("Unexpected message promise");
/*     */   } public void consumeHeader(List<Header> headers, boolean endStream) throws HttpException, IOException {
/*     */     HttpResponse response;
/*     */     int status;
/*     */     IncomingEntityDetails incomingEntityDetails;
/* 184 */     if (this.done.get()) {
/* 185 */       throw new ProtocolException("Unexpected message headers");
/*     */     }
/* 187 */     switch (this.responseState) {
/*     */       case HEADERS:
/* 189 */         response = DefaultH2ResponseConverter.INSTANCE.convert(headers);
/* 190 */         status = response.getCode();
/* 191 */         if (status < 100) {
/* 192 */           throw new ProtocolException("Invalid response: " + new StatusLine(response));
/*     */         }
/* 194 */         if (status > 100 && status < 200) {
/* 195 */           this.exchangeHandler.consumeInformation(response, (HttpContext)this.context);
/*     */         }
/* 197 */         if (this.requestState == MessageState.ACK && (
/* 198 */           status == 100 || status >= 200)) {
/* 199 */           this.requestState = MessageState.BODY;
/* 200 */           this.exchangeHandler.produce(this.dataChannel);
/*     */         } 
/*     */         
/* 203 */         if (status < 200) {
/*     */           return;
/*     */         }
/*     */         
/* 207 */         incomingEntityDetails = endStream ? null : new IncomingEntityDetails((MessageHeaders)response, -1L);
/* 208 */         this.context.setAttribute("http.response", response);
/* 209 */         this.httpProcessor.process(response, (EntityDetails)incomingEntityDetails, (HttpContext)this.context);
/* 210 */         this.connMetrics.incrementResponseCount();
/*     */         
/* 212 */         this.exchangeHandler.consumeResponse(response, (EntityDetails)incomingEntityDetails, (HttpContext)this.context);
/* 213 */         this.responseState = endStream ? MessageState.COMPLETE : MessageState.BODY;
/*     */         return;
/*     */       case BODY:
/* 216 */         this.responseState = MessageState.COMPLETE;
/* 217 */         this.exchangeHandler.streamEnd(headers);
/*     */         return;
/*     */     } 
/* 220 */     throw new ProtocolException("Unexpected message headers");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateInputCapacity() throws IOException {
/* 226 */     this.exchangeHandler.updateCapacity(this.outputChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeData(ByteBuffer src, boolean endStream) throws HttpException, IOException {
/* 231 */     if (this.done.get() || this.responseState != MessageState.BODY) {
/* 232 */       throw new ProtocolException("Unexpected message data");
/*     */     }
/* 234 */     if (src != null) {
/* 235 */       this.exchangeHandler.consume(src);
/*     */     }
/* 237 */     if (endStream) {
/* 238 */       this.responseState = MessageState.COMPLETE;
/* 239 */       this.exchangeHandler.streamEnd(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(HttpException ex, boolean endStream) throws HttpException, IOException {
/* 245 */     throw ex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*     */     try {
/* 251 */       if (this.failed.compareAndSet(false, true) && 
/* 252 */         this.exchangeHandler != null) {
/* 253 */         this.exchangeHandler.failed(cause);
/*     */       }
/*     */     } finally {
/*     */       
/* 257 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 263 */     if (this.done.compareAndSet(false, true)) {
/* 264 */       this.responseState = MessageState.COMPLETE;
/* 265 */       this.requestState = MessageState.COMPLETE;
/* 266 */       this.exchangeHandler.releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 272 */     return "[requestState=" + this.requestState + ", responseState=" + this.responseState + ']';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ClientH2StreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */