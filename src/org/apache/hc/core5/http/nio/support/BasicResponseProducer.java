/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseProducer;
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
/*     */ public class BasicResponseProducer
/*     */   implements AsyncResponseProducer
/*     */ {
/*     */   private final HttpResponse response;
/*     */   private final AsyncEntityProducer dataProducer;
/*     */   
/*     */   public BasicResponseProducer(HttpResponse response, AsyncEntityProducer dataProducer) {
/*  56 */     this.response = (HttpResponse)Args.notNull(response, "Response");
/*  57 */     this.dataProducer = dataProducer;
/*     */   }
/*     */   
/*     */   public BasicResponseProducer(HttpResponse response) {
/*  61 */     this.response = (HttpResponse)Args.notNull(response, "Response");
/*  62 */     this.dataProducer = null;
/*     */   }
/*     */   
/*     */   public BasicResponseProducer(int code, AsyncEntityProducer dataProducer) {
/*  66 */     this((HttpResponse)new BasicHttpResponse(code), dataProducer);
/*     */   }
/*     */   
/*     */   public BasicResponseProducer(HttpResponse response, String message, ContentType contentType) {
/*  70 */     this(response, AsyncEntityProducers.create(message, contentType));
/*     */   }
/*     */   
/*     */   public BasicResponseProducer(HttpResponse response, String message) {
/*  74 */     this(response, message, ContentType.TEXT_PLAIN);
/*     */   }
/*     */   
/*     */   public BasicResponseProducer(int code, String message, ContentType contentType) {
/*  78 */     this((HttpResponse)new BasicHttpResponse(code), message, contentType);
/*     */   }
/*     */   
/*     */   public BasicResponseProducer(int code, String message) {
/*  82 */     this((HttpResponse)new BasicHttpResponse(code), message);
/*     */   }
/*     */   
/*     */   public BasicResponseProducer(AsyncEntityProducer dataProducer) {
/*  86 */     this(200, dataProducer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendResponse(ResponseChannel responseChannel, HttpContext httpContext) throws HttpException, IOException {
/*  91 */     responseChannel.sendResponse(this.response, (EntityDetails)this.dataProducer, httpContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/*  96 */     return (this.dataProducer != null) ? this.dataProducer.available() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void produce(DataStreamChannel channel) throws IOException {
/* 101 */     if (this.dataProducer != null) {
/* 102 */       this.dataProducer.produce(channel);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/* 108 */     if (this.dataProducer != null) {
/* 109 */       this.dataProducer.failed(cause);
/*     */     }
/* 111 */     releaseResources();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 116 */     if (this.dataProducer != null)
/* 117 */       this.dataProducer.releaseResources(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/BasicResponseProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */