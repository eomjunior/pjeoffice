/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
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
/*     */ public class MultipartPartBuilder
/*     */ {
/*     */   private ContentBody body;
/*     */   private final Header header;
/*     */   
/*     */   public static MultipartPartBuilder create(ContentBody body) {
/*  48 */     return new MultipartPartBuilder(body);
/*     */   }
/*     */   
/*     */   public static MultipartPartBuilder create() {
/*  52 */     return new MultipartPartBuilder();
/*     */   }
/*     */   
/*     */   MultipartPartBuilder(ContentBody body) {
/*  56 */     this();
/*  57 */     this.body = body;
/*     */   }
/*     */   
/*     */   MultipartPartBuilder() {
/*  61 */     this.header = new Header();
/*     */   }
/*     */   
/*     */   public MultipartPartBuilder setBody(ContentBody body) {
/*  65 */     this.body = body;
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartPartBuilder addHeader(String name, String value, List<NameValuePair> parameters) {
/*  70 */     Args.notNull(name, "Header name");
/*  71 */     this.header.addField(new MimeField(name, value, parameters));
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartPartBuilder addHeader(String name, String value) {
/*  76 */     Args.notNull(name, "Header name");
/*  77 */     this.header.addField(new MimeField(name, value));
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartPartBuilder setHeader(String name, String value) {
/*  82 */     Args.notNull(name, "Header name");
/*  83 */     this.header.setField(new MimeField(name, value));
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartPartBuilder removeHeaders(String name) {
/*  88 */     Args.notNull(name, "Header name");
/*  89 */     this.header.removeFields(name);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartPart build() {
/*  94 */     Asserts.notNull(this.body, "Content body");
/*  95 */     Header headerCopy = new Header();
/*  96 */     List<MimeField> fields = this.header.getFields();
/*  97 */     for (MimeField field : fields) {
/*  98 */       headerCopy.addField(field);
/*     */     }
/* 100 */     if (headerCopy.getField("Content-Type") == null) {
/*     */       ContentType contentType;
/* 102 */       if (this.body instanceof AbstractContentBody) {
/* 103 */         contentType = ((AbstractContentBody)this.body).getContentType();
/*     */       } else {
/* 105 */         contentType = null;
/*     */       } 
/* 107 */       if (contentType != null) {
/* 108 */         headerCopy.addField(new MimeField("Content-Type", contentType.toString()));
/*     */       } else {
/* 110 */         StringBuilder buffer = new StringBuilder();
/* 111 */         buffer.append(this.body.getMimeType());
/* 112 */         if (this.body.getCharset() != null) {
/* 113 */           buffer.append("; charset=");
/* 114 */           buffer.append(this.body.getCharset());
/*     */         } 
/* 116 */         headerCopy.addField(new MimeField("Content-Type", buffer.toString()));
/*     */       } 
/*     */     } 
/* 119 */     return new MultipartPart(this.body, headerCopy);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/MultipartPartBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */