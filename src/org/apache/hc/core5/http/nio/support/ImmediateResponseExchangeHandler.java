/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncServerExchangeHandler;
/*     */ import org.apache.hc.core5.http.nio.CapacityChannel;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
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
/*     */ public final class ImmediateResponseExchangeHandler
/*     */   implements AsyncServerExchangeHandler
/*     */ {
/*     */   private final AsyncResponseProducer responseProducer;
/*     */   
/*     */   public ImmediateResponseExchangeHandler(AsyncResponseProducer responseProducer) {
/*  60 */     this.responseProducer = (AsyncResponseProducer)Args.notNull(responseProducer, "Response producer");
/*     */   }
/*     */   
/*     */   public ImmediateResponseExchangeHandler(HttpResponse response, String message) {
/*  64 */     this(new BasicResponseProducer(response, AsyncEntityProducers.create(message)));
/*     */   }
/*     */   
/*     */   public ImmediateResponseExchangeHandler(int status, String message) {
/*  68 */     this((HttpResponse)new BasicHttpResponse(status), message);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleRequest(HttpRequest request, EntityDetails entityDetails, ResponseChannel responseChannel, HttpContext context) throws HttpException, IOException {
/*  77 */     this.responseProducer.sendResponse(responseChannel, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCapacity(CapacityChannel capacityChannel) throws IOException {
/*  82 */     capacityChannel.update(2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void consume(ByteBuffer src) throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void streamEnd(List<? extends Header> trailers) throws HttpException, IOException {}
/*     */ 
/*     */   
/*     */   public int available() {
/*  95 */     return this.responseProducer.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public void produce(DataStreamChannel channel) throws IOException {
/* 100 */     this.responseProducer.produce(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/* 105 */     this.responseProducer.failed(cause);
/* 106 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 111 */     this.responseProducer.releaseResources();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/ImmediateResponseExchangeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */