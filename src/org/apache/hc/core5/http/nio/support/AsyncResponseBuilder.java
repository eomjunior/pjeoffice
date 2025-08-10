/*     */ package org.apache.hc.core5.http.nio.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.AsyncResponseProducer;
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
/*     */ public class AsyncResponseBuilder
/*     */   extends AbstractResponseBuilder<AsyncResponseProducer>
/*     */ {
/*     */   private AsyncEntityProducer entityProducer;
/*     */   
/*     */   AsyncResponseBuilder(int status) {
/*  52 */     super(status);
/*     */   }
/*     */   
/*     */   public static AsyncResponseBuilder create(int status) {
/*  56 */     Args.checkRange(status, 100, 599, "HTTP status code");
/*  57 */     return new AsyncResponseBuilder(status);
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseBuilder setVersion(ProtocolVersion version) {
/*  62 */     super.setVersion(version);
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseBuilder setHeaders(Header... headers) {
/*  68 */     super.setHeaders(headers);
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseBuilder addHeader(Header header) {
/*  74 */     super.addHeader(header);
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseBuilder addHeader(String name, String value) {
/*  80 */     super.addHeader(name, value);
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseBuilder removeHeader(Header header) {
/*  86 */     super.removeHeader(header);
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseBuilder removeHeaders(String name) {
/*  92 */     super.removeHeaders(name);
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseBuilder setHeader(Header header) {
/*  98 */     super.setHeader(header);
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseBuilder setHeader(String name, String value) {
/* 104 */     super.setHeader(name, value);
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncEntityProducer getEntity() {
/* 109 */     return this.entityProducer;
/*     */   }
/*     */   
/*     */   public AsyncResponseBuilder setEntity(AsyncEntityProducer entityProducer) {
/* 113 */     this.entityProducer = entityProducer;
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncResponseBuilder setEntity(String content, ContentType contentType) {
/* 118 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content, contentType);
/* 119 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncResponseBuilder setEntity(String content) {
/* 123 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content);
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   public AsyncResponseBuilder setEntity(byte[] content, ContentType contentType) {
/* 128 */     this.entityProducer = (AsyncEntityProducer)new BasicAsyncEntityProducer(content, contentType);
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AsyncResponseProducer build() {
/* 134 */     BasicHttpResponse response = new BasicHttpResponse(getStatus());
/* 135 */     response.setVersion(getVersion());
/* 136 */     response.setHeaders(getHeaders());
/* 137 */     return new BasicResponseProducer((HttpResponse)response, this.entityProducer);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 142 */     StringBuilder builder = new StringBuilder();
/* 143 */     builder.append("AsyncResponseBuilder [status=");
/* 144 */     builder.append(getStatus());
/* 145 */     builder.append(", headerGroup=");
/* 146 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 147 */     builder.append(", entity=");
/* 148 */     builder.append((this.entityProducer != null) ? this.entityProducer.getClass() : null);
/* 149 */     builder.append("]");
/* 150 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/support/AsyncResponseBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */