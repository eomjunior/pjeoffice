/*     */ package org.apache.hc.client5.http.entity;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
/*     */ import org.apache.hc.core5.http.io.entity.FileEntity;
/*     */ import org.apache.hc.core5.http.io.entity.InputStreamEntity;
/*     */ import org.apache.hc.core5.http.io.entity.SerializableEntity;
/*     */ import org.apache.hc.core5.http.io.entity.StringEntity;
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
/*     */ public class EntityBuilder
/*     */ {
/*     */   private String text;
/*     */   private byte[] binary;
/*     */   private InputStream stream;
/*     */   private List<NameValuePair> parameters;
/*     */   private Serializable serializable;
/*     */   private File file;
/*     */   private ContentType contentType;
/*     */   private String contentEncoding;
/*     */   private boolean chunked;
/*     */   private boolean gzipCompressed;
/*     */   
/*     */   public static EntityBuilder create() {
/*  82 */     return new EntityBuilder();
/*     */   }
/*     */   
/*     */   private void clearContent() {
/*  86 */     this.text = null;
/*  87 */     this.binary = null;
/*  88 */     this.stream = null;
/*  89 */     this.parameters = null;
/*  90 */     this.serializable = null;
/*  91 */     this.file = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getText() {
/*  98 */     return this.text;
/*     */   }
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
/*     */   public EntityBuilder setText(String text) {
/* 111 */     clearContent();
/* 112 */     this.text = text;
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getBinary() {
/* 121 */     return this.binary;
/*     */   }
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
/*     */   public EntityBuilder setBinary(byte[] binary) {
/* 134 */     clearContent();
/* 135 */     this.binary = binary;
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getStream() {
/* 144 */     return this.stream;
/*     */   }
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
/*     */   public EntityBuilder setStream(InputStream stream) {
/* 157 */     clearContent();
/* 158 */     this.stream = stream;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<NameValuePair> getParameters() {
/* 168 */     return this.parameters;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder setParameters(List<NameValuePair> parameters) {
/* 180 */     clearContent();
/* 181 */     this.parameters = parameters;
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder setParameters(NameValuePair... parameters) {
/* 194 */     return setParameters(Arrays.asList(parameters));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Serializable getSerializable() {
/* 202 */     return this.serializable;
/*     */   }
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
/*     */   public EntityBuilder setSerializable(Serializable serializable) {
/* 215 */     clearContent();
/* 216 */     this.serializable = serializable;
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/* 225 */     return this.file;
/*     */   }
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
/*     */   public EntityBuilder setFile(File file) {
/* 238 */     clearContent();
/* 239 */     this.file = file;
/* 240 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentType getContentType() {
/* 247 */     return this.contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder setContentType(ContentType contentType) {
/* 254 */     this.contentType = contentType;
/* 255 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 262 */     return this.contentEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder setContentEncoding(String contentEncoding) {
/* 269 */     this.contentEncoding = contentEncoding;
/* 270 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/* 277 */     return this.chunked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder chunked() {
/* 284 */     this.chunked = true;
/* 285 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGzipCompressed() {
/* 292 */     return this.gzipCompressed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityBuilder gzipCompressed() {
/* 299 */     this.gzipCompressed = true;
/* 300 */     return this;
/*     */   }
/*     */   
/*     */   private ContentType getContentOrDefault(ContentType def) {
/* 304 */     return (this.contentType != null) ? this.contentType : def;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpEntity build() {
/*     */     FileEntity fileEntity;
/* 312 */     if (this.text != null) {
/* 313 */       StringEntity stringEntity = new StringEntity(this.text, getContentOrDefault(ContentType.DEFAULT_TEXT), this.contentEncoding, this.chunked);
/*     */     }
/* 315 */     else if (this.binary != null) {
/* 316 */       ByteArrayEntity byteArrayEntity = new ByteArrayEntity(this.binary, getContentOrDefault(ContentType.DEFAULT_BINARY), this.contentEncoding, this.chunked);
/*     */     }
/* 318 */     else if (this.stream != null) {
/* 319 */       InputStreamEntity inputStreamEntity = new InputStreamEntity(this.stream, -1L, getContentOrDefault(ContentType.DEFAULT_BINARY), this.contentEncoding);
/*     */     }
/* 321 */     else if (this.parameters != null) {
/*     */       
/* 323 */       UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(this.parameters, (this.contentType != null) ? this.contentType.getCharset() : null);
/* 324 */     } else if (this.serializable != null) {
/* 325 */       SerializableEntity serializableEntity = new SerializableEntity(this.serializable, ContentType.DEFAULT_BINARY, this.contentEncoding);
/* 326 */     } else if (this.file != null) {
/* 327 */       fileEntity = new FileEntity(this.file, getContentOrDefault(ContentType.DEFAULT_BINARY), this.contentEncoding);
/*     */     } else {
/* 329 */       throw new IllegalStateException("No entity set");
/*     */     } 
/* 331 */     if (this.gzipCompressed) {
/* 332 */       return (HttpEntity)new GzipCompressingEntity((HttpEntity)fileEntity);
/*     */     }
/* 334 */     return (HttpEntity)fileEntity;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/EntityBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */