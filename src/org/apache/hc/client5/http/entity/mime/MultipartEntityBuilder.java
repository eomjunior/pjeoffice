/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.http.message.BasicNameValuePair;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultipartEntityBuilder
/*     */ {
/*  56 */   private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
/*     */     
/*  58 */     .toCharArray();
/*     */   
/*     */   private ContentType contentType;
/*  61 */   private HttpMultipartMode mode = HttpMultipartMode.STRICT;
/*     */   
/*     */   private String boundary;
/*     */   
/*     */   private Charset charset;
/*     */   
/*     */   private List<MultipartPart> multipartParts;
/*     */   
/*  69 */   private static final NameValuePair[] EMPTY_NAME_VALUE_ARRAY = new NameValuePair[0];
/*     */   
/*     */   public static MultipartEntityBuilder create() {
/*  72 */     return new MultipartEntityBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder setMode(HttpMultipartMode mode) {
/*  79 */     this.mode = mode;
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartEntityBuilder setLaxMode() {
/*  84 */     this.mode = HttpMultipartMode.LEGACY;
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartEntityBuilder setStrictMode() {
/*  89 */     this.mode = HttpMultipartMode.STRICT;
/*  90 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartEntityBuilder setBoundary(String boundary) {
/*  94 */     this.boundary = boundary;
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder setMimeSubtype(String subType) {
/* 102 */     Args.notBlank(subType, "MIME subtype");
/* 103 */     this.contentType = ContentType.create("multipart/" + subType);
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder setContentType(ContentType contentType) {
/* 111 */     Args.notNull(contentType, "Content type");
/* 112 */     this.contentType = contentType;
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addParameter(BasicNameValuePair parameter) {
/* 123 */     this.contentType = this.contentType.withParameters(new NameValuePair[] { (NameValuePair)parameter });
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartEntityBuilder setCharset(Charset charset) {
/* 128 */     this.charset = charset;
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addPart(MultipartPart multipartPart) {
/* 136 */     if (multipartPart == null) {
/* 137 */       return this;
/*     */     }
/* 139 */     if (this.multipartParts == null) {
/* 140 */       this.multipartParts = new ArrayList<>();
/*     */     }
/* 142 */     this.multipartParts.add(multipartPart);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public MultipartEntityBuilder addPart(String name, ContentBody contentBody) {
/* 147 */     Args.notNull(name, "Name");
/* 148 */     Args.notNull(contentBody, "Content body");
/* 149 */     return addPart(FormBodyPartBuilder.create(name, contentBody).build());
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addTextBody(String name, String text, ContentType contentType) {
/* 154 */     return addPart(name, new StringBody(text, contentType));
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addTextBody(String name, String text) {
/* 159 */     return addTextBody(name, text, ContentType.DEFAULT_TEXT);
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addBinaryBody(String name, byte[] b, ContentType contentType, String filename) {
/* 164 */     return addPart(name, new ByteArrayBody(b, contentType, filename));
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addBinaryBody(String name, byte[] b) {
/* 169 */     return addPart(name, new ByteArrayBody(b, ContentType.DEFAULT_BINARY));
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addBinaryBody(String name, File file, ContentType contentType, String filename) {
/* 174 */     return addPart(name, new FileBody(file, contentType, filename));
/*     */   }
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addBinaryBody(String name, File file) {
/* 179 */     return addBinaryBody(name, file, ContentType.DEFAULT_BINARY, (file != null) ? file.getName() : null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MultipartEntityBuilder addBinaryBody(String name, InputStream stream, ContentType contentType, String filename) {
/* 185 */     return addPart(name, new InputStreamBody(stream, contentType, filename));
/*     */   }
/*     */   
/*     */   public MultipartEntityBuilder addBinaryBody(String name, InputStream stream) {
/* 189 */     return addBinaryBody(name, stream, ContentType.DEFAULT_BINARY, (String)null);
/*     */   }
/*     */   
/*     */   private String generateBoundary() {
/* 193 */     ThreadLocalRandom rand = ThreadLocalRandom.current();
/* 194 */     int count = rand.nextInt(30, 41);
/* 195 */     CharBuffer buffer = CharBuffer.allocate(count);
/* 196 */     while (buffer.hasRemaining()) {
/* 197 */       buffer.put(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
/*     */     }
/* 199 */     buffer.flip();
/* 200 */     return buffer.toString();
/*     */   }
/*     */   MultipartFormEntity buildEntity() {
/*     */     ContentType contentTypeCopy;
/* 204 */     String boundaryCopy = this.boundary;
/* 205 */     if (boundaryCopy == null && this.contentType != null) {
/* 206 */       boundaryCopy = this.contentType.getParameter("boundary");
/*     */     }
/* 208 */     if (boundaryCopy == null) {
/* 209 */       boundaryCopy = generateBoundary();
/*     */     }
/* 211 */     Charset charsetCopy = this.charset;
/* 212 */     if (charsetCopy == null && this.contentType != null) {
/* 213 */       charsetCopy = this.contentType.getCharset();
/*     */     }
/* 215 */     List<NameValuePair> paramsList = new ArrayList<>(2);
/* 216 */     paramsList.add(new BasicNameValuePair("boundary", boundaryCopy));
/* 217 */     if (charsetCopy != null) {
/* 218 */       paramsList.add(new BasicNameValuePair("charset", charsetCopy.name()));
/*     */     }
/* 220 */     NameValuePair[] params = paramsList.<NameValuePair>toArray(EMPTY_NAME_VALUE_ARRAY);
/*     */ 
/*     */     
/* 223 */     if (this.contentType != null) {
/* 224 */       contentTypeCopy = this.contentType.withParameters(params);
/*     */     } else {
/* 226 */       boolean formData = false;
/* 227 */       if (this.multipartParts != null) {
/* 228 */         for (MultipartPart multipartPart : this.multipartParts) {
/* 229 */           if (multipartPart instanceof FormBodyPart) {
/* 230 */             formData = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/* 236 */       if (formData) {
/* 237 */         contentTypeCopy = ContentType.MULTIPART_FORM_DATA.withParameters(params);
/*     */       } else {
/* 239 */         contentTypeCopy = ContentType.create("multipart/mixed", params);
/*     */       } 
/*     */     } 
/*     */     
/* 243 */     List<MultipartPart> multipartPartsCopy = (this.multipartParts != null) ? new ArrayList<>(this.multipartParts) : Collections.<MultipartPart>emptyList();
/* 244 */     HttpMultipartMode modeCopy = (this.mode != null) ? this.mode : HttpMultipartMode.STRICT;
/*     */     
/* 246 */     switch (modeCopy)
/*     */     { case LEGACY:
/* 248 */         form = new LegacyMultipart(charsetCopy, boundaryCopy, multipartPartsCopy);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 263 */         return new MultipartFormEntity(form, contentTypeCopy, form.getTotalLength());case EXTENDED: if (contentTypeCopy.isSameMimeType(ContentType.MULTIPART_FORM_DATA)) { if (charsetCopy == null) charsetCopy = StandardCharsets.UTF_8;  form = new HttpRFC7578Multipart(charsetCopy, boundaryCopy, multipartPartsCopy); } else { form = new HttpRFC6532Multipart(charsetCopy, boundaryCopy, multipartPartsCopy); }  return new MultipartFormEntity(form, contentTypeCopy, form.getTotalLength()); }  AbstractMultipartFormat form = new HttpStrictMultipart(StandardCharsets.US_ASCII, boundaryCopy, multipartPartsCopy); return new MultipartFormEntity(form, contentTypeCopy, form.getTotalLength());
/*     */   }
/*     */   
/*     */   public HttpEntity build() {
/* 267 */     return buildEntity();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/MultipartEntityBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */