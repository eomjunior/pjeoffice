/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
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
/*     */ import org.apache.hc.core5.http.nio.AsyncPushConsumer;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
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
/*     */ class ClientPushH2StreamHandler
/*     */   implements H2StreamHandler
/*     */ {
/*     */   private final H2StreamChannel internalOutputChannel;
/*     */   private final HttpProcessor httpProcessor;
/*     */   private final BasicHttpConnectionMetrics connMetrics;
/*     */   private final HandlerFactory<AsyncPushConsumer> pushHandlerFactory;
/*     */   private final HttpCoreContext context;
/*     */   private final AtomicBoolean failed;
/*     */   private final AtomicBoolean done;
/*     */   private volatile HttpRequest request;
/*     */   private volatile AsyncPushConsumer exchangeHandler;
/*     */   private volatile MessageState requestState;
/*     */   private volatile MessageState responseState;
/*     */   
/*     */   ClientPushH2StreamHandler(H2StreamChannel outputChannel, HttpProcessor httpProcessor, BasicHttpConnectionMetrics connMetrics, HandlerFactory<AsyncPushConsumer> pushHandlerFactory, HttpCoreContext context) {
/*  76 */     this.internalOutputChannel = outputChannel;
/*  77 */     this.httpProcessor = httpProcessor;
/*  78 */     this.connMetrics = connMetrics;
/*  79 */     this.pushHandlerFactory = pushHandlerFactory;
/*  80 */     this.context = context;
/*  81 */     this.failed = new AtomicBoolean(false);
/*  82 */     this.done = new AtomicBoolean(false);
/*  83 */     this.requestState = MessageState.HEADERS;
/*  84 */     this.responseState = MessageState.HEADERS;
/*     */   }
/*     */ 
/*     */   
/*     */   public HandlerFactory<AsyncPushConsumer> getPushHandlerFactory() {
/*  89 */     return this.pushHandlerFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOutputReady() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void produceOutput() throws HttpException, IOException {}
/*     */ 
/*     */   
/*     */   public void consumePromise(List<Header> headers) throws HttpException, IOException {
/* 103 */     if (this.requestState == MessageState.HEADERS) {
/*     */       
/* 105 */       this.request = DefaultH2RequestConverter.INSTANCE.convert(headers);
/*     */       try {
/* 107 */         this.exchangeHandler = (this.pushHandlerFactory != null) ? (AsyncPushConsumer)this.pushHandlerFactory.create(this.request, (HttpContext)this.context) : null;
/* 108 */       } catch (ProtocolException ex) {
/* 109 */         this.exchangeHandler = new NoopAsyncPushHandler();
/* 110 */         throw new H2StreamResetException(H2Error.PROTOCOL_ERROR, ex.getMessage());
/*     */       } 
/* 112 */       if (this.exchangeHandler == null) {
/* 113 */         this.exchangeHandler = new NoopAsyncPushHandler();
/* 114 */         throw new H2StreamResetException(H2Error.REFUSED_STREAM, "Stream refused");
/*     */       } 
/*     */       
/* 117 */       this.context.setProtocolVersion((ProtocolVersion)HttpVersion.HTTP_2);
/* 118 */       this.context.setAttribute("http.request", this.request);
/*     */       
/* 120 */       this.httpProcessor.process(this.request, null, (HttpContext)this.context);
/* 121 */       this.connMetrics.incrementRequestCount();
/* 122 */       this.requestState = MessageState.COMPLETE;
/*     */     } else {
/* 124 */       throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Unexpected promise");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeHeader(List<Header> headers, boolean endStream) throws HttpException, IOException {
/* 130 */     if (this.responseState == MessageState.HEADERS) {
/* 131 */       Asserts.notNull(this.request, "Request");
/* 132 */       Asserts.notNull(this.exchangeHandler, "Exchange handler");
/*     */       
/* 134 */       HttpResponse response = DefaultH2ResponseConverter.INSTANCE.convert(headers);
/* 135 */       IncomingEntityDetails incomingEntityDetails = endStream ? null : new IncomingEntityDetails((MessageHeaders)this.request, -1L);
/*     */       
/* 137 */       this.context.setAttribute("http.response", response);
/* 138 */       this.httpProcessor.process(response, (EntityDetails)incomingEntityDetails, (HttpContext)this.context);
/* 139 */       this.connMetrics.incrementResponseCount();
/*     */       
/* 141 */       this.exchangeHandler.consumePromise(this.request, response, (EntityDetails)incomingEntityDetails, (HttpContext)this.context);
/* 142 */       if (endStream) {
/* 143 */         this.responseState = MessageState.COMPLETE;
/* 144 */         this.exchangeHandler.streamEnd(null);
/*     */       } else {
/* 146 */         this.responseState = MessageState.BODY;
/*     */       } 
/*     */     } else {
/* 149 */       throw new ProtocolException("Unexpected message headers");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateInputCapacity() throws IOException {
/* 155 */     Asserts.notNull(this.exchangeHandler, "Exchange handler");
/* 156 */     this.exchangeHandler.updateCapacity(this.internalOutputChannel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void consumeData(ByteBuffer src, boolean endStream) throws HttpException, IOException {
/* 161 */     if (this.responseState != MessageState.BODY) {
/* 162 */       throw new ProtocolException("Unexpected message data");
/*     */     }
/* 164 */     Asserts.notNull(this.exchangeHandler, "Exchange handler");
/* 165 */     if (src != null) {
/* 166 */       this.exchangeHandler.consume(src);
/*     */     }
/* 168 */     if (endStream) {
/* 169 */       this.responseState = MessageState.COMPLETE;
/* 170 */       this.exchangeHandler.streamEnd(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/* 175 */     return (this.responseState == MessageState.COMPLETE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*     */     try {
/* 181 */       if (this.failed.compareAndSet(false, true) && 
/* 182 */         this.exchangeHandler != null) {
/* 183 */         this.exchangeHandler.failed(cause);
/*     */       }
/*     */     } finally {
/*     */       
/* 187 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(HttpException ex, boolean endStream) throws HttpException {
/* 193 */     throw ex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 198 */     if (this.done.compareAndSet(false, true)) {
/* 199 */       this.responseState = MessageState.COMPLETE;
/* 200 */       this.requestState = MessageState.COMPLETE;
/* 201 */       if (this.exchangeHandler != null) {
/* 202 */         this.exchangeHandler.releaseResources();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 209 */     return "[requestState=" + this.requestState + ", responseState=" + this.responseState + ']';
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/ClientPushH2StreamHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */