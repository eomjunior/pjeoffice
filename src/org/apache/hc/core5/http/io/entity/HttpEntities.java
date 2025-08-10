/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.NameValuePair;
/*     */ import org.apache.hc.core5.io.IOCallback;
/*     */ import org.apache.hc.core5.net.WWWFormCodec;
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
/*     */ public final class HttpEntities
/*     */ {
/*     */   public static HttpEntity create(String content, ContentType contentType) {
/*  63 */     return new StringEntity(content, contentType);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(String content, Charset charset) {
/*  67 */     return new StringEntity(content, ContentType.TEXT_PLAIN.withCharset(charset));
/*     */   }
/*     */   
/*     */   public static HttpEntity create(String content) {
/*  71 */     return new StringEntity(content, ContentType.TEXT_PLAIN);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(byte[] content, ContentType contentType) {
/*  75 */     return new ByteArrayEntity(content, contentType);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(File content, ContentType contentType) {
/*  79 */     return new FileEntity(content, contentType);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(Serializable serializable, ContentType contentType) {
/*  83 */     return new SerializableEntity(serializable, contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static HttpEntity createUrlEncoded(Iterable<? extends NameValuePair> parameters, Charset charset) {
/*  89 */     ContentType contentType = (charset != null) ? ContentType.APPLICATION_FORM_URLENCODED.withCharset(charset) : ContentType.APPLICATION_FORM_URLENCODED;
/*     */     
/*  91 */     return create(WWWFormCodec.format(parameters, contentType.getCharset()), contentType);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(IOCallback<OutputStream> callback, ContentType contentType) {
/*  95 */     return new EntityTemplate(-1L, contentType, null, callback);
/*     */   }
/*     */   
/*     */   public static HttpEntity gzip(HttpEntity entity) {
/*  99 */     return new HttpEntityWrapper(entity)
/*     */       {
/*     */         public String getContentEncoding()
/*     */         {
/* 103 */           return "gzip";
/*     */         }
/*     */ 
/*     */         
/*     */         public long getContentLength() {
/* 108 */           return -1L;
/*     */         }
/*     */ 
/*     */         
/*     */         public InputStream getContent() throws IOException {
/* 113 */           throw new UnsupportedOperationException();
/*     */         }
/*     */ 
/*     */         
/*     */         public void writeTo(OutputStream outStream) throws IOException {
/* 118 */           Args.notNull(outStream, "Output stream");
/* 119 */           GZIPOutputStream gzip = new GZIPOutputStream(outStream);
/* 120 */           super.writeTo(gzip);
/*     */ 
/*     */           
/* 123 */           gzip.close();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static HttpEntity createGzipped(String content, ContentType contentType) {
/* 130 */     return gzip(create(content, contentType));
/*     */   }
/*     */   
/*     */   public static HttpEntity createGzipped(String content, Charset charset) {
/* 134 */     return gzip(create(content, charset));
/*     */   }
/*     */   
/*     */   public static HttpEntity createGzipped(String content) {
/* 138 */     return gzip(create(content));
/*     */   }
/*     */   
/*     */   public static HttpEntity createGzipped(byte[] content, ContentType contentType) {
/* 142 */     return gzip(create(content, contentType));
/*     */   }
/*     */   
/*     */   public static HttpEntity createGzipped(File content, ContentType contentType) {
/* 146 */     return gzip(create(content, contentType));
/*     */   }
/*     */   
/*     */   public static HttpEntity createGzipped(Serializable serializable, ContentType contentType) {
/* 150 */     return gzip(create(serializable, contentType));
/*     */   }
/*     */   
/*     */   public static HttpEntity createGzipped(IOCallback<OutputStream> callback, ContentType contentType) {
/* 154 */     return gzip(create(callback, contentType));
/*     */   }
/*     */   
/*     */   public static HttpEntity createGzipped(Path content, ContentType contentType) {
/* 158 */     return gzip(create(content, contentType));
/*     */   }
/*     */   
/*     */   public static HttpEntity withTrailers(HttpEntity entity, Header... trailers) {
/* 162 */     return new HttpEntityWrapper(entity)
/*     */       {
/*     */         
/*     */         public boolean isChunked()
/*     */         {
/* 167 */           return true;
/*     */         }
/*     */ 
/*     */         
/*     */         public long getContentLength() {
/* 172 */           return -1L;
/*     */         }
/*     */ 
/*     */         
/*     */         public Supplier<List<? extends Header>> getTrailers() {
/* 177 */           return () -> Arrays.asList(trailers);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<String> getTrailerNames() {
/* 182 */           Set<String> names = new LinkedHashSet<>();
/* 183 */           for (Header trailer : trailers) {
/* 184 */             names.add(trailer.getName());
/*     */           }
/* 186 */           return names;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static HttpEntity create(String content, ContentType contentType, Header... trailers) {
/* 193 */     return withTrailers(create(content, contentType), trailers);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(String content, Charset charset, Header... trailers) {
/* 197 */     return withTrailers(create(content, charset), trailers);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(String content, Header... trailers) {
/* 201 */     return withTrailers(create(content), trailers);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(byte[] content, ContentType contentType, Header... trailers) {
/* 205 */     return withTrailers(create(content, contentType), trailers);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(File content, ContentType contentType, Header... trailers) {
/* 209 */     return withTrailers(create(content, contentType), trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public static HttpEntity create(Serializable serializable, ContentType contentType, Header... trailers) {
/* 214 */     return withTrailers(create(serializable, contentType), trailers);
/*     */   }
/*     */ 
/*     */   
/*     */   public static HttpEntity create(IOCallback<OutputStream> callback, ContentType contentType, Header... trailers) {
/* 219 */     return withTrailers(create(callback, contentType), trailers);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(Path content, ContentType contentType) {
/* 223 */     return new PathEntity(content, contentType);
/*     */   }
/*     */   
/*     */   public static HttpEntity create(Path content, ContentType contentType, Header... trailers) {
/* 227 */     return withTrailers(create(content, contentType), trailers);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/HttpEntities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */