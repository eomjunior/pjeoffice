/*     */ package org.apache.hc.client5.http.entity.mime;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ContentTooLongException;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class MultipartFormEntity
/*     */   implements HttpEntity
/*     */ {
/*     */   private final AbstractMultipartFormat multipart;
/*     */   private final ContentType contentType;
/*     */   private final long contentLength;
/*     */   
/*     */   MultipartFormEntity(AbstractMultipartFormat multipart, ContentType contentType, long contentLength) {
/*  55 */     this.multipart = multipart;
/*  56 */     this.contentType = contentType;
/*  57 */     this.contentLength = contentLength;
/*     */   }
/*     */   
/*     */   AbstractMultipartFormat getMultipart() {
/*  61 */     return this.multipart;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  66 */     return (this.contentLength != -1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  71 */     return !isRepeatable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/*  76 */     return !isRepeatable();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  81 */     return this.contentLength;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  86 */     return (this.contentType != null) ? this.contentType.toString() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  96 */     if (this.contentLength < 0L)
/*  97 */       throw new ContentTooLongException("Content length is unknown"); 
/*  98 */     if (this.contentLength > 25600L) {
/*  99 */       throw new ContentTooLongException("Content length is too long: " + this.contentLength);
/*     */     }
/* 101 */     ByteArrayOutputStream outStream = new ByteArrayOutputStream();
/* 102 */     writeTo(outStream);
/* 103 */     outStream.flush();
/* 104 */     return new ByteArrayInputStream(outStream.toByteArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {
/* 109 */     this.multipart.writeTo(outStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<List<? extends Header>> getTrailers() {
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/entity/mime/MultipartFormEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */