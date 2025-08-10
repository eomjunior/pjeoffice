/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.hc.core5.http.ContentType;
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
/*     */ public class InputStreamEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private final InputStream content;
/*     */   private final long length;
/*     */   
/*     */   public InputStreamEntity(InputStream inStream, long length, ContentType contentType, String contentEncoding) {
/*  49 */     super(contentType, contentEncoding);
/*  50 */     this.content = (InputStream)Args.notNull(inStream, "Source input stream");
/*  51 */     this.length = length;
/*     */   }
/*     */   
/*     */   public InputStreamEntity(InputStream inStream, long length, ContentType contentType) {
/*  55 */     this(inStream, length, contentType, null);
/*     */   }
/*     */   
/*     */   public InputStreamEntity(InputStream inStream, ContentType contentType) {
/*  59 */     this(inStream, -1L, contentType, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRepeatable() {
/*  64 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getContentLength() {
/*  72 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public final InputStream getContent() throws IOException {
/*  77 */     return this.content;
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
/*     */   public final void writeTo(OutputStream outStream) throws IOException {
/*  89 */     Args.notNull(outStream, "Output stream");
/*  90 */     try (InputStream inStream = this.content) {
/*  91 */       byte[] buffer = new byte[4096];
/*     */       
/*  93 */       if (this.length < 0L) {
/*     */         int readLen;
/*  95 */         while ((readLen = inStream.read(buffer)) != -1) {
/*  96 */           outStream.write(buffer, 0, readLen);
/*     */         }
/*     */       } else {
/*     */         
/* 100 */         long remaining = this.length;
/* 101 */         while (remaining > 0L) {
/* 102 */           int readLen = inStream.read(buffer, 0, (int)Math.min(4096L, remaining));
/* 103 */           if (readLen == -1) {
/*     */             break;
/*     */           }
/* 106 */           outStream.write(buffer, 0, readLen);
/* 107 */           remaining -= readLen;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStreaming() {
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() throws IOException {
/* 120 */     this.content.close();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/InputStreamEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */