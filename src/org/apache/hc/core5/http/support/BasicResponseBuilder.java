/*     */ package org.apache.hc.core5.http.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.HttpResponse;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.message.BasicHttpResponse;
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
/*     */ public class BasicResponseBuilder
/*     */   extends AbstractResponseBuilder<BasicHttpResponse>
/*     */ {
/*     */   protected BasicResponseBuilder(int status) {
/*  46 */     super(status);
/*     */   }
/*     */   
/*     */   public static BasicResponseBuilder create(int status) {
/*  50 */     Args.checkRange(status, 100, 599, "HTTP status code");
/*  51 */     return new BasicResponseBuilder(status);
/*     */   }
/*     */   
/*     */   public static BasicResponseBuilder copy(HttpResponse response) {
/*  55 */     Args.notNull(response, "HTTP response");
/*  56 */     BasicResponseBuilder builder = new BasicResponseBuilder(response.getCode());
/*  57 */     builder.digest((HttpMessage)response);
/*  58 */     return builder;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicResponseBuilder setVersion(ProtocolVersion version) {
/*  63 */     super.setVersion(version);
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicResponseBuilder setHeaders(Header... headers) {
/*  69 */     super.setHeaders(headers);
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicResponseBuilder addHeader(Header header) {
/*  75 */     super.addHeader(header);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicResponseBuilder addHeader(String name, String value) {
/*  81 */     super.addHeader(name, value);
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicResponseBuilder removeHeader(Header header) {
/*  87 */     super.removeHeader(header);
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicResponseBuilder removeHeaders(String name) {
/*  93 */     super.removeHeaders(name);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicResponseBuilder setHeader(Header header) {
/*  99 */     super.setHeader(header);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicResponseBuilder setHeader(String name, String value) {
/* 105 */     super.setHeader(name, value);
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHttpResponse build() {
/* 111 */     BasicHttpResponse result = new BasicHttpResponse(getStatus());
/* 112 */     result.setVersion(getVersion());
/* 113 */     result.setHeaders(getHeaders());
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 119 */     StringBuilder builder = new StringBuilder();
/* 120 */     builder.append("BasicResponseBuilder [status=");
/* 121 */     builder.append(getStatus());
/* 122 */     builder.append(", headerGroup=");
/* 123 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 124 */     builder.append("]");
/* 125 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/support/BasicResponseBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */