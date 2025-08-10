/*     */ package org.apache.hc.client5.http.impl.async;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.message.RequestLine;
/*     */ import org.apache.hc.core5.http.message.StatusLine;
/*     */ import org.apache.hc.core5.http.nio.AsyncClientExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.RequestChannel;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
/*     */ import org.apache.hc.core5.util.Identifiable;
/*     */ import org.slf4j.Logger;
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
/*     */ final class LoggingAsyncClientExchangeHandler
/*     */   implements AsyncClientExchangeHandler, Identifiable
/*     */ {
/*     */   private final Logger log;
/*     */   private final String exchangeId;
/*     */   private final AsyncClientExchangeHandler handler;
/*     */   
/*     */   LoggingAsyncClientExchangeHandler(Logger log, String exchangeId, AsyncClientExchangeHandler handler) {
/*  54 */     this.log = log;
/*  55 */     this.exchangeId = exchangeId;
/*  56 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  61 */     return this.exchangeId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/*  66 */     this.handler.releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public void produceRequest(RequestChannel channel, HttpContext context) throws HttpException, IOException {
/*  71 */     this.handler.produceRequest((request, entityDetails, context1) -> { if (this.log.isDebugEnabled()) this.log.debug("{} send request {}, {}", new Object[] { this.exchangeId, new RequestLine(request), (entityDetails != null) ? ("entity len " + entityDetails.getContentLength()) : "null entity" });  channel.sendRequest(request, entityDetails, context1); }context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/*  82 */     return this.handler.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void produce(final DataStreamChannel channel) throws IOException {
/*  87 */     if (this.log.isDebugEnabled()) {
/*  88 */       this.log.debug("{}: produce request data", this.exchangeId);
/*     */     }
/*  90 */     this.handler.produce(new DataStreamChannel()
/*     */         {
/*     */           public void requestOutput()
/*     */           {
/*  94 */             channel.requestOutput();
/*     */           }
/*     */ 
/*     */           
/*     */           public int write(ByteBuffer src) throws IOException {
/*  99 */             if (LoggingAsyncClientExchangeHandler.this.log.isDebugEnabled()) {
/* 100 */               LoggingAsyncClientExchangeHandler.this.log.debug("{}: produce request data, len {} bytes", LoggingAsyncClientExchangeHandler.this.exchangeId, Integer.valueOf(src.remaining()));
/*     */             }
/* 102 */             return channel.write(src);
/*     */           }
/*     */ 
/*     */           
/*     */           public void endStream() throws IOException {
/* 107 */             if (LoggingAsyncClientExchangeHandler.this.log.isDebugEnabled()) {
/* 108 */               LoggingAsyncClientExchangeHandler.this.log.debug("{}: end of request data", LoggingAsyncClientExchangeHandler.this.exchangeId);
/*     */             }
/* 110 */             channel.endStream();
/*     */           }
/*     */ 
/*     */           
/*     */           public void endStream(List<? extends Header> trailers) throws IOException {
/* 115 */             if (LoggingAsyncClientExchangeHandler.this.log.isDebugEnabled()) {
/* 116 */               LoggingAsyncClientExchangeHandler.this.log.debug("{}: end of request data", LoggingAsyncClientExchangeHandler.this.exchangeId);
/*     */             }
/* 118 */             channel.endStream(trailers);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeInformation(HttpResponse response, HttpContext context) throws HttpException, IOException {
/* 128 */     if (this.log.isDebugEnabled()) {
/* 129 */       this.log.debug("{}: information response {}", this.exchangeId, new StatusLine(response));
/*     */     }
/* 131 */     this.handler.consumeInformation(response, context);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consumeResponse(HttpResponse response, EntityDetails entityDetails, HttpContext context) throws HttpException, IOException {
/* 139 */     if (this.log.isDebugEnabled()) {
/* 140 */       this.log.debug("{}: consume response {}, {}", new Object[] { this.exchangeId, new StatusLine(response), (entityDetails != null) ? ("entity len " + entityDetails.getContentLength()) : " null entity" });
/*     */     }
/* 142 */     this.handler.consumeResponse(response, entityDetails, context);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/* 148 */     this.handler.updateCapacity(increment -> {
/*     */           if (this.log.isDebugEnabled()) {
/*     */             this.log.debug("{} capacity update {}", this.exchangeId, Integer.valueOf(increment));
/*     */           }
/*     */           capacityChannel.update(increment);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void consume(ByteBuffer src) throws IOException {
/* 158 */     if (this.log.isDebugEnabled()) {
/* 159 */       this.log.debug("{}: consume response data, len {} bytes", this.exchangeId, Integer.valueOf(src.remaining()));
/*     */     }
/* 161 */     this.handler.consume(src);
/*     */   }
/*     */ 
/*     */   
/*     */   public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {
/* 166 */     if (this.log.isDebugEnabled()) {
/* 167 */       this.log.debug("{}: end of response data", this.exchangeId);
/*     */     }
/* 169 */     this.handler.streamEnd(trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/* 174 */     if (this.log.isDebugEnabled()) {
/* 175 */       this.log.debug("{}: execution failed: {}", this.exchangeId, cause.getMessage());
/*     */     }
/* 177 */     this.handler.failed(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public void cancel() {
/* 182 */     if (this.log.isDebugEnabled()) {
/* 183 */       this.log.debug("{}: execution cancelled", this.exchangeId);
/*     */     }
/* 185 */     this.handler.cancel();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/impl/async/LoggingAsyncClientExchangeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */