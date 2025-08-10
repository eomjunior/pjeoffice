/*     */ package org.apache.hc.core5.http.io.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.apache.hc.core5.http.ClassicHttpResponse;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.HttpMessage;
/*     */ import org.apache.hc.core5.http.ProtocolVersion;
/*     */ import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
/*     */ import org.apache.hc.core5.http.io.entity.StringEntity;
/*     */ import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
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
/*     */ public class ClassicResponseBuilder
/*     */   extends AbstractResponseBuilder<ClassicHttpResponse>
/*     */ {
/*     */   private HttpEntity entity;
/*     */   
/*     */   ClassicResponseBuilder(int status) {
/*  53 */     super(status);
/*     */   }
/*     */   
/*     */   public static ClassicResponseBuilder create(int status) {
/*  57 */     Args.checkRange(status, 100, 599, "HTTP status code");
/*  58 */     return new ClassicResponseBuilder(status);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ClassicResponseBuilder copy(ClassicHttpResponse response) {
/*  65 */     Args.notNull(response, "HTTP response");
/*  66 */     ClassicResponseBuilder builder = new ClassicResponseBuilder(response.getCode());
/*  67 */     builder.digest(response);
/*  68 */     return builder;
/*     */   }
/*     */   
/*     */   protected void digest(ClassicHttpResponse response) {
/*  72 */     digest((HttpMessage)response);
/*  73 */     setEntity(response.getEntity());
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicResponseBuilder setVersion(ProtocolVersion version) {
/*  78 */     super.setVersion(version);
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicResponseBuilder setHeaders(Header... headers) {
/*  84 */     super.setHeaders(headers);
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicResponseBuilder addHeader(Header header) {
/*  90 */     super.addHeader(header);
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicResponseBuilder addHeader(String name, String value) {
/*  96 */     super.addHeader(name, value);
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicResponseBuilder removeHeader(Header header) {
/* 102 */     super.removeHeader(header);
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicResponseBuilder removeHeaders(String name) {
/* 108 */     super.removeHeaders(name);
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicResponseBuilder setHeader(Header header) {
/* 114 */     super.setHeader(header);
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicResponseBuilder setHeader(String name, String value) {
/* 120 */     super.setHeader(name, value);
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public HttpEntity getEntity() {
/* 125 */     return this.entity;
/*     */   }
/*     */   
/*     */   public ClassicResponseBuilder setEntity(HttpEntity entity) {
/* 129 */     this.entity = entity;
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public ClassicResponseBuilder setEntity(String content, ContentType contentType) {
/* 134 */     this.entity = (HttpEntity)new StringEntity(content, contentType);
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public ClassicResponseBuilder setEntity(String content) {
/* 139 */     this.entity = (HttpEntity)new StringEntity(content);
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public ClassicResponseBuilder setEntity(byte[] content, ContentType contentType) {
/* 144 */     this.entity = (HttpEntity)new ByteArrayEntity(content, contentType);
/* 145 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClassicHttpResponse build() {
/* 150 */     BasicClassicHttpResponse result = new BasicClassicHttpResponse(getStatus());
/* 151 */     result.setVersion(getVersion());
/* 152 */     result.setHeaders(getHeaders());
/* 153 */     result.setEntity(this.entity);
/* 154 */     return (ClassicHttpResponse)result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 159 */     StringBuilder builder = new StringBuilder();
/* 160 */     builder.append("ClassicResponseBuilder [status=");
/* 161 */     builder.append(getStatus());
/* 162 */     builder.append(", headerGroup=");
/* 163 */     builder.append(Arrays.toString((Object[])getHeaders()));
/* 164 */     builder.append(", entity=");
/* 165 */     builder.append((this.entity != null) ? this.entity.getClass() : null);
/* 166 */     builder.append("]");
/* 167 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/support/ClassicResponseBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */