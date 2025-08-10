/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
/*     */ import org.apache.hc.core5.http.io.entity.AbstractHttpEntity;
/*     */ import org.apache.hc.core5.http.io.entity.EmptyInputStream;
/*     */ import org.apache.hc.core5.io.Closer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class IncomingHttpEntity
/*     */   implements HttpEntity
/*     */ {
/*     */   private final InputStream content;
/*     */   private final long len;
/*     */   private final boolean chunked;
/*     */   private final Header contentType;
/*     */   private final Header contentEncoding;
/*     */   
/*     */   IncomingHttpEntity(InputStream content, long len, boolean chunked, Header contentType, Header contentEncoding) {
/*  53 */     this.content = content;
/*  54 */     this.len = len;
/*  55 */     this.chunked = chunked;
/*  56 */     this.contentType = contentType;
/*  57 */     this.contentEncoding = contentEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  67 */     return this.chunked;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  72 */     return this.len;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  77 */     return (this.contentType != null) ? this.contentType.getValue() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/*  82 */     return (this.contentEncoding != null) ? this.contentEncoding.getValue() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException, IllegalStateException {
/*  87 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/*  92 */     return (this.content != null && this.content != EmptyInputStream.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {
/*  97 */     AbstractHttpEntity.writeTo(this, outStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<List<? extends Header>> getTrailers() {
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 107 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 112 */     Closer.close(this.content);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 117 */     StringBuilder sb = new StringBuilder();
/* 118 */     sb.append('[');
/* 119 */     sb.append("Content-Type: ");
/* 120 */     sb.append(getContentType());
/* 121 */     sb.append(',');
/* 122 */     sb.append("Content-Encoding: ");
/* 123 */     sb.append(getContentEncoding());
/* 124 */     sb.append(',');
/* 125 */     long len = getContentLength();
/* 126 */     if (len >= 0L) {
/* 127 */       sb.append("Content-Length: ");
/* 128 */       sb.append(len);
/* 129 */       sb.append(',');
/*     */     } 
/* 131 */     sb.append("Chunked: ");
/* 132 */     sb.append(isChunked());
/* 133 */     sb.append(']');
/* 134 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/IncomingHttpEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */