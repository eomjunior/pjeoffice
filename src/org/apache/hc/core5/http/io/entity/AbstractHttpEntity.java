/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.function.Supplier;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.HttpEntity;
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
/*     */ public abstract class AbstractHttpEntity
/*     */   implements HttpEntity
/*     */ {
/*     */   static final int OUTPUT_BUFFER_SIZE = 4096;
/*     */   private final String contentType;
/*     */   private final String contentEncoding;
/*     */   private final boolean chunked;
/*     */   
/*     */   protected AbstractHttpEntity(String contentType, String contentEncoding, boolean chunked) {
/*  58 */     this.contentType = contentType;
/*  59 */     this.contentEncoding = contentEncoding;
/*  60 */     this.chunked = chunked;
/*     */   }
/*     */   
/*     */   protected AbstractHttpEntity(ContentType contentType, String contentEncoding, boolean chunked) {
/*  64 */     this.contentType = (contentType != null) ? contentType.toString() : null;
/*  65 */     this.contentEncoding = contentEncoding;
/*  66 */     this.chunked = chunked;
/*     */   }
/*     */   
/*     */   protected AbstractHttpEntity(String contentType, String contentEncoding) {
/*  70 */     this(contentType, contentEncoding, false);
/*     */   }
/*     */   
/*     */   protected AbstractHttpEntity(ContentType contentType, String contentEncoding) {
/*  74 */     this(contentType, contentEncoding, false);
/*     */   }
/*     */   
/*     */   public static void writeTo(HttpEntity entity, OutputStream outStream) throws IOException {
/*  78 */     Args.notNull(entity, "Entity");
/*  79 */     Args.notNull(outStream, "Output stream");
/*  80 */     try (InputStream inStream = entity.getContent()) {
/*  81 */       if (inStream != null) {
/*     */         
/*  83 */         byte[] tmp = new byte[4096]; int count;
/*  84 */         while ((count = inStream.read(tmp)) != -1) {
/*  85 */           outStream.write(tmp, 0, count);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {
/*  93 */     writeTo(this, outStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getContentType() {
/*  98 */     return this.contentType;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getContentEncoding() {
/* 103 */     return this.contentEncoding;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isChunked() {
/* 108 */     return this.chunked;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<List<? extends Header>> getTrailers() {
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 123 */     return Collections.emptySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     StringBuilder sb = new StringBuilder();
/* 129 */     sb.append("[Entity-Class: ");
/* 130 */     sb.append(getClass().getSimpleName());
/* 131 */     sb.append(", Content-Type: ");
/* 132 */     sb.append(this.contentType);
/* 133 */     sb.append(", Content-Encoding: ");
/* 134 */     sb.append(this.contentEncoding);
/* 135 */     sb.append(", chunked: ");
/* 136 */     sb.append(this.chunked);
/* 137 */     sb.append(']');
/* 138 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/AbstractHttpEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */