/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncDataConsumer;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncFilterChain;
/*     */ import org.apache.hc.core5.http.nio.AsyncFilterHandler;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.HandlerFactory;
/*     */ import org.apache.hc.core5.http.nio.ResponseChannel;
/*     */ import org.apache.hc.core5.http.nio.entity.AsyncEntityProducers;
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
/*     */ @Contract(threading = ThreadingBehavior.STATELESS)
/*     */ public final class TerminalAsyncServerFilter
/*     */   implements AsyncFilterHandler
/*     */ {
/*     */   private final HandlerFactory<AsyncServerExchangeHandler> handlerFactory;
/*     */   
/*     */   public TerminalAsyncServerFilter(HandlerFactory<AsyncServerExchangeHandler> handlerFactory) {
/*  66 */     this.handlerFactory = (HandlerFactory<AsyncServerExchangeHandler>)Args.notNull(handlerFactory, "Handler factory");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AsyncDataConsumer handle(HttpRequest request, EntityDetails entityDetails, HttpContext context, final AsyncFilterChain.ResponseTrigger responseTrigger, AsyncFilterChain chain) throws HttpException, IOException {
/*  76 */     final AsyncServerExchangeHandler exchangeHandler = (AsyncServerExchangeHandler)this.handlerFactory.create(request, context);
/*  77 */     if (exchangeHandler != null) {
/*  78 */       exchangeHandler.handleRequest(request, entityDetails, new ResponseChannel()
/*     */           {
/*     */             public void sendInformation(HttpResponse response, HttpContext httpContext) throws HttpException, IOException
/*     */             {
/*  82 */               responseTrigger.sendInformation(response);
/*     */             }
/*     */ 
/*     */             
/*     */             public void sendResponse(HttpResponse response, final EntityDetails entityDetails, HttpContext httpContext) throws HttpException, IOException {
/*  87 */               responseTrigger.submitResponse(response, (entityDetails != null) ? new AsyncEntityProducer()
/*     */                   {
/*     */                     public void failed(Exception cause)
/*     */                     {
/*  91 */                       exchangeHandler.failed(cause);
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public boolean isRepeatable() {
/*  96 */                       return false;
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public long getContentLength() {
/* 101 */                       return entityDetails.getContentLength();
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public String getContentType() {
/* 106 */                       return entityDetails.getContentType();
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public String getContentEncoding() {
/* 111 */                       return entityDetails.getContentEncoding();
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public boolean isChunked() {
/* 116 */                       return entityDetails.isChunked();
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public Set<String> getTrailerNames() {
/* 121 */                       return entityDetails.getTrailerNames();
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public int available() {
/* 126 */                       return exchangeHandler.available();
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public void produce(DataStreamChannel channel) throws IOException {
/* 131 */                       exchangeHandler.produce(channel);
/*     */                     }
/*     */ 
/*     */                     
/*     */                     public void releaseResources() {
/* 136 */                       exchangeHandler.releaseResources();
/*     */                     }
/*     */                   } : null);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public void pushPromise(HttpRequest promise, AsyncPushProducer pushProducer, HttpContext httpContext) throws HttpException, IOException {
/* 144 */               responseTrigger.pushPromise(promise, pushProducer);
/*     */             }
/*     */           }context);
/*     */       
/* 148 */       return (AsyncDataConsumer)exchangeHandler;
/*     */     } 
/* 150 */     responseTrigger.submitResponse((HttpResponse)new BasicHttpResponse(404), AsyncEntityProducers.create("Not found"));
/* 151 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/TerminalAsyncServerFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */