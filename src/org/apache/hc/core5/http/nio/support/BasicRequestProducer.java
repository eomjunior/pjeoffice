/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import org.apache.hc.core5.http.EntityDetails;
/*     */ import org.apache.hc.core5.http.HttpException;
/*     */ import org.apache.hc.core5.http.HttpHost;
/*     */ import org.apache.hc.core5.http.HttpRequest;
/*     */ import org.apache.hc.core5.http.Method;
/*     */ import org.apache.hc.core5.http.message.BasicHttpRequest;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncRequestProducer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
/*     */ import org.apache.hc.core5.http.nio.RequestChannel;
/*     */ import org.apache.hc.core5.http.protocol.HttpContext;
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
/*     */ public class BasicRequestProducer
/*     */   implements AsyncRequestProducer
/*     */ {
/*     */   private final HttpRequest request;
/*     */   private final AsyncEntityProducer dataProducer;
/*     */   
/*     */   public BasicRequestProducer(HttpRequest request, AsyncEntityProducer dataProducer) {
/*  55 */     this.request = request;
/*  56 */     this.dataProducer = dataProducer;
/*     */   }
/*     */   
/*     */   public BasicRequestProducer(String method, HttpHost host, String path, AsyncEntityProducer dataProducer) {
/*  60 */     this((HttpRequest)new BasicHttpRequest(method, host, path), dataProducer);
/*     */   }
/*     */   
/*     */   public BasicRequestProducer(String method, HttpHost host, String path) {
/*  64 */     this(method, host, path, (AsyncEntityProducer)null);
/*     */   }
/*     */   
/*     */   public BasicRequestProducer(String method, URI requestUri, AsyncEntityProducer dataProducer) {
/*  68 */     this((HttpRequest)new BasicHttpRequest(method, requestUri), dataProducer);
/*     */   }
/*     */   
/*     */   public BasicRequestProducer(String method, URI requestUri) {
/*  72 */     this(method, requestUri, (AsyncEntityProducer)null);
/*     */   }
/*     */   
/*     */   public BasicRequestProducer(Method method, HttpHost host, String path, AsyncEntityProducer dataProducer) {
/*  76 */     this((HttpRequest)new BasicHttpRequest(method, host, path), dataProducer);
/*     */   }
/*     */   
/*     */   public BasicRequestProducer(Method method, HttpHost host, String path) {
/*  80 */     this(method, host, path, (AsyncEntityProducer)null);
/*     */   }
/*     */   
/*     */   public BasicRequestProducer(Method method, URI requestUri, AsyncEntityProducer dataProducer) {
/*  84 */     this((HttpRequest)new BasicHttpRequest(method, requestUri), dataProducer);
/*     */   }
/*     */   
/*     */   public BasicRequestProducer(Method method, URI requestUri) {
/*  88 */     this(method, requestUri, (AsyncEntityProducer)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendRequest(RequestChannel requestChannel, HttpContext httpContext) throws HttpException, IOException {
/*  93 */     requestChannel.sendRequest(this.request, (EntityDetails)this.dataProducer, httpContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/*  98 */     return (this.dataProducer != null) ? this.dataProducer.available() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void produce(DataStreamChannel channel) throws IOException {
/* 103 */     if (this.dataProducer != null) {
/* 104 */       this.dataProducer.produce(channel);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 110 */     return (this.dataProducer == null || this.dataProducer.isRepeatable());
/*     */   }
/*     */ 
/*     */   
/*     */   public void failed(Exception cause) {
/*     */     try {
/* 116 */       if (this.dataProducer != null) {
/* 117 */         this.dataProducer.failed(cause);
/*     */       }
/*     */     } finally {
/* 120 */       releaseResources();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 126 */     if (this.dataProducer != null)
/* 127 */       this.dataProducer.releaseResources(); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/BasicRequestProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */