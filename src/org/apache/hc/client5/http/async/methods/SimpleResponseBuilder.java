/*     */ package org.apache.hc.client5.http.async.methods;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
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
/*     */ public class SimpleResponseBuilder
/*     */   extends AbstractResponseBuilder<SimpleHttpResponse>
/*     */ {
/*     */   private SimpleBody body;
/*     */   
/*     */   SimpleResponseBuilder(int status) {
/*  48 */     super(status);
/*     */   }
/*     */   
/*     */   public static SimpleResponseBuilder create(int status) {
/*  52 */     Args.checkRange(status, 100, 599, "HTTP status code");
/*  53 */     return new SimpleResponseBuilder(status);
/*     */   }
/*     */   
/*     */   public static SimpleResponseBuilder copy(SimpleHttpResponse response) {
/*  57 */     Args.notNull(response, "HTTP response");
/*  58 */     SimpleResponseBuilder builder = new SimpleResponseBuilder(response.getCode());
/*  59 */     builder.digest(response);
/*  60 */     return builder;
/*     */   }
/*     */   
/*     */   protected void digest(SimpleHttpResponse response) {
/*  64 */     digest((HttpMessage)response);
/*  65 */     setBody(response.getBody());
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleResponseBuilder setVersion(ProtocolVersion version) {
/*  70 */     super.setVersion(version);
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleResponseBuilder setHeaders(Header... headers) {
/*  76 */     super.setHeaders(headers);
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleResponseBuilder addHeader(Header header) {
/*  82 */     super.addHeader(header);
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleResponseBuilder addHeader(String name, String value) {
/*  88 */     super.addHeader(name, value);
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleResponseBuilder removeHeader(Header header) {
/*  94 */     super.removeHeader(header);
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleResponseBuilder removeHeaders(String name) {
/* 100 */     super.removeHeaders(name);
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleResponseBuilder setHeader(Header header) {
/* 106 */     super.setHeader(header);
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleResponseBuilder setHeader(String name, String value) {
/* 112 */     super.setHeader(name, value);
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public SimpleBody getBody() {
/* 117 */     return this.body;
/*     */   }
/*     */   
/*     */   public SimpleResponseBuilder setBody(SimpleBody body) {
/* 121 */     this.body = body;
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public SimpleResponseBuilder setBody(String content, ContentType contentType) {
/* 126 */     this.body = SimpleBody.create(content, contentType);
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public SimpleResponseBuilder setBody(byte[] content, ContentType contentType) {
/* 131 */     this.body = SimpleBody.create(content, contentType);
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleHttpResponse build() {
/* 137 */     SimpleHttpResponse result = new SimpleHttpResponse(getStatus());
/* 138 */     result.setVersion(getVersion());
/* 139 */     result.setHeaders(getHeaders());
/* 140 */     result.setBody(this.body);
/* 141 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 146 */     StringBuilder builder = new StringBuilder();
/* 147 */     builder.append("SimpleResponseBuilder [status=");
/* 148 */     builder.append(getStatus());
/* 149 */     builder.append(", headerGroup=");
/* 150 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 151 */     builder.append(", body=");
/* 152 */     builder.append(this.body);
/* 153 */     builder.append("]");
/* 154 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/async/methods/SimpleResponseBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */