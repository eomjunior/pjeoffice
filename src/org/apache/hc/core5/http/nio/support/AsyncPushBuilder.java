/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncPushProducer;
/*     */ import org.apache.hc.core5.http.nio.entity.BasicAsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.support.AbstractMessageBuilder;
/*     */ import org.apache.hc.core5.http.support.AbstractResponseBuilder;
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
/*     */ public class AsyncPushBuilder
/*     */   extends AbstractResponseBuilder<AsyncPushProducer>
/*     */ {
/*     */   private AsyncEntityProducer entityProducer;
/*     */   
/*     */   AsyncPushBuilder(int status) {
/*  53 */     super(status);
/*     */   }
/*     */   
/*     */   public static AsyncPushBuilder create(int status) {
/*  57 */     Args.checkRange(status, 100, 599, "HTTP status code");
/*  58 */     return new AsyncPushBuilder(status);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushBuilder setVersion(ProtocolVersion version) {
/*  63 */     super.setVersion(version);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushBuilder setHeaders(Header... headers) {
/*  69 */     super.setHeaders(headers);
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushBuilder addHeader(Header header) {
/*  75 */     super.addHeader(header);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushBuilder addHeader(String name, String value) {
/*  81 */     super.addHeader(name, value);
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushBuilder removeHeader(Header header) {
/*  87 */     super.removeHeader(header);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushBuilder removeHeaders(String name) {
/*  93 */     super.removeHeaders(name);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushBuilder setHeader(Header header) {
/*  99 */     super.setHeader(header);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushBuilder setHeader(String name, String value) {
/* 105 */     super.setHeader(name, value);
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncEntityProducer getEntity() {
/* 110 */     return this.entityProducer;
/*     */   }
/*     */   
/*     */   public AsyncPushBuilder setEntity(AsyncEntityProducer entityProducer) {
/* 114 */     this.entityProducer = entityProducer;
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncPushBuilder setEntity(String content, ContentType contentType) {
/* 119 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content, contentType);
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncPushBuilder setEntity(String content) {
/* 124 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content);
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncPushBuilder setEntity(byte[] content, ContentType contentType) {
/* 129 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content, contentType);
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncPushProducer build() {
/* 135 */     BasicHttpResponse basicHttpResponse = new BasicHttpResponse(getStatus());
/* 136 */     basicHttpResponse.setVersion(getVersion());
/* 137 */     basicHttpResponse.setHeaders(getHeaders());
/* 138 */     return new BasicPushProducer((HttpResponse)basicHttpResponse, this.entityProducer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     StringBuilder builder = new StringBuilder();
/* 144 */     builder.append("AsyncPushProducer [status=");
/* 145 */     builder.append(getStatus());
/* 146 */     builder.append(", headerGroup=");
/* 147 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 148 */     builder.append(", entity=");
/* 149 */     builder.append((this.entityProducer != null) ? this.entityProducer.getClass() : null);
/* 150 */     builder.append("]");
/* 151 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AsyncPushBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */