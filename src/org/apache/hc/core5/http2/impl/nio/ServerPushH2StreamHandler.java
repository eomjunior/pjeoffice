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
/*     */ import org.apache.hc.core5.http.ProtocolException;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpConnectionMetrics;
/*     */ import org.apache.hc.core5.http.impl.nio.MessageState;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.ResponseChannel;
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
/*     */ class ServerPushH2StreamHandler
/*     */   implements H2StreamHandler
/*     */ {
/*     */   private final H2StreamChannel outputChannel;
/*     */   private final DataStreamChannel dataChannel;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final BasicHttpConnectionMetrics connMetrics;
/*     */   private final AsyncPushProducer pushProducer;
/*     */   private final HttpCoreContext context;
/*     */   private final AtomicBoolean responseCommitted;
/*     */   private final AtomicBoolean failed;
/*     */   private final AtomicBoolean done;
/*     */   private volatile MessageState requestState;
/*     */   private volatile MessageState responseState;
/*     */   
/*     */   ServerPushH2StreamHandler(final H2StreamChannel outputChannel, HttpProcessor httpProcessor, BasicHttpConnectionMetrics connMetrics, AsyncPushProducer pushProducer, HttpCoreContext context) {
/*  78 */     this.outputChannel = outputChannel;
/*  79 */     this.dataChannel = new DataStreamChannel()
/*     */       {
/*     */         public void requestOutput()
/*     */         {
/*  83 */           outputChannel.requestOutput();
/*     */         }
/*     */ 
/*     */         
/*     */         public int write(ByteBuffer src) throws IOException {
/*  88 */           return outputChannel.write(src);
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream(List<? extends Header> trailers) throws IOException {
/*  93 */           outputChannel.endStream(trailers);
/*  94 */           ServerPushH2StreamHandler.this.responseState = MessageState.COMPLETE;
/*     */         }
/*     */ 
/*     */         
/*     */         public void endStream() throws IOException {
/*  99 */           outputChannel.endStream();
/* 100 */           ServerPushH2StreamHandler.this.responseState = MessageState.COMPLETE;
/*     */         }
/*     */       };
/*     */     
/* 104 */     this.httpProcessor = httpProcessor;
/* 105 */     this.connMetrics = connMetrics;
/* 106 */     this.pushProducer = pushProducer;
/* 107 */     this.context = context;
/* 108 */     this.responseCommitted = new AtomicBoolean(false);
/* 109 */     this.failed = new AtomicBoolean(false);
/* 110 */     this.done = new AtomicBoolean(false);
/* 111 */     this.requestState = MessageState.COMPLETE;
/* 112 */     this.responseState = MessageState.IDLE;
/*     */   }
/*     */ 
/*     */   
/*     */   public HandlerFactory<AsyncPushConsumer> getPushHandlerFactory() {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumePromise(List<Header> headers) throws HttpException, IOException {
/* 122 */     throw new ProtocolException("Unexpected message promise");
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeHeader(List<Header> requestHeaders, boolean requestEndStream) throws HttpException, IOException {
/* 127 */     throw new ProtocolException("Unexpected message headers");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateInputCapacity() throws IOException {}
/*     */ 
/*     */   
/*     */   public void consumeData(ByteBuffer src, boolean endStream) throws HttpException, IOException {
/* 136 */     throw new ProtocolException("Unexpected message data");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutputReady() {
/* 141 */     switch (this.responseState) {
/*     */       case IDLE:
/* 143 */         return true;
/*     */       case BODY:
/* 145 */         return (this.pushProducer.available() > 0);
/*     */     } 
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void commitInformation(HttpResponse response) throws IOException, HttpException {
/* 152 */     if (this.responseCommitted.get()) {
/* 153 */       throw new H2ConnectionException(H2Error.INTERNAL_ERROR, "Response already committed");
/*     */     }
/* 155 */     int status = response.getCode();
/* 156 */     if (status < 100 || status >= 200) {
/* 157 */       throw new HttpException("Invalid intermediate response: " + status);
/*     */     }
/* 159 */     List<Header> responseHeaders = DefaultH2ResponseConverter.INSTANCE.convert(response);
/* 160 */     this.outputChannel.submit(responseHeaders, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void commitResponse(HttpResponse response, EntityDetails responseEntityDetails) throws HttpException, IOException {
/* 166 */     if (this.responseCommitted.compareAndSet(false, true)) {
/*     */       
/* 168 */       this.context.setProtocolVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 169 */       this.context.setAttribute("http.response", response);
/* 170 */       this.httpProcessor.process(response, responseEntityDetails, (HttpContext)this.context);
/*     */       
/* 172 */       List<Header> headers = DefaultH2ResponseConverter.INSTANCE.convert(response);
/* 173 */       this.outputChannel.submit(headers, (responseEntityDetails == null));
/* 174 */       this.connMetrics.incrementResponseCount();
/* 175 */       if (responseEntityDetails == null) {
/* 176 */         this.responseState = MessageState.COMPLETE;
/*     */       } else {
/* 178 */         this.responseState = MessageState.BODY;
/* 179 */         this.pushProducer.produce(this.outputChannel);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void commitPromise(HttpRequest promise, AsyncPushProducer pushProducer) throws HttpException, IOException {
/* 188 */     this.context.setProtocolVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 189 */     this.context.setAttribute("http.request", promise);
/* 190 */     this.httpProcessor.process(promise, null, (HttpContext)this.context);
/*     */     
/* 192 */     List<Header> headers = DefaultH2RequestConverter.INSTANCE.convert(promise);
/*     */     
/* 194 */     this.outputChannel.push(headers, pushProducer);
/* 195 */     this.connMetrics.incrementRequestCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceOutput() throws HttpException, IOException {
/* 200 */     switch (this.responseState) {
/*     */       case IDLE:
/* 202 */         this.responseState = MessageState.HEADERS;
/* 203 */         this.pushProducer.produceResponse(new ResponseChannel()
/*     */             {
/*     */               public void sendInformation(HttpResponse response, HttpContext httpContext) throws HttpException, IOException
/*     */               {
/* 207 */                 ServerPushH2StreamHandler.this.commitInformation(response);
/*     */               }
/*     */ 
/*     */ 
/*     */               
/*     */               public void sendResponse(HttpResponse response, EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
/* 213 */                 ServerPushH2StreamHandler.this.commitResponse(response, entityDetails);
/*     */               }
/*     */ 
/*     */ 
/*     */               
/*     */               public void pushPromise(HttpRequest promise, AsyncPushProducer pushProducer, HttpContext httpContext) throws HttpException, IOException {
/* 219 */                 ServerPushH2StreamHandler.this.commitPromise(promise, pushProducer);
/*     */               }
/*     */             }(HttpContext)this.context);
/*     */         break;
/*     */       
/*     */       case BODY:
/* 225 */         this.pushProducer.produce(this.dataChannel);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(HttpException ex, boolean endStream) throws HttpException, IOException {
/* 232 */     throw ex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*     */     try {
/* 238 */       if (this.failed.compareAndSet(false, true)) {
/* 239 */         this.pushProducer.failed(cause);
/*     */       }
/*     */     } finally {
/* 242 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 248 */     if (this.done.compareAndSet(false, true)) {
/* 249 */       this.requestState = MessageState.COMPLETE;
/* 250 */       this.responseState = MessageState.COMPLETE;
/* 251 */       this.pushProducer.releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 257 */     return "[requestState=" + this.requestState + ", responseState=" + this.responseState + ']';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ServerPushH2StreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */