/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BufferedHttpEntity
/*     */   extends HttpEntityWrapper
/*     */ {
/*     */   private final byte[] buffer;
/*     */   
/*     */   public BufferedHttpEntity(HttpEntity entity) throws IOException {
/*  59 */     super(entity);
/*  60 */     if (!entity.isRepeatable() || entity.getContentLength() < 0L) {
/*  61 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/*  62 */       entity.writeTo(out);
/*  63 */       out.flush();
/*  64 */       this.buffer = out.toByteArray();
/*     */     } else {
/*  66 */       this.buffer = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  72 */     if (this.buffer != null) {
/*  73 */       return this.buffer.length;
/*     */     }
/*  75 */     return super.getContentLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  80 */     if (this.buffer != null) {
/*  81 */       return new ByteArrayInputStream(this.buffer);
/*     */     }
/*  83 */     return super.getContent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  93 */     return (this.buffer == null && super.isChunked());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {
/* 109 */     Args.notNull(outStream, "Output stream");
/* 110 */     if (this.buffer != null) {
/* 111 */       outStream.write(this.buffer);
/*     */     } else {
/* 113 */       super.writeTo(outStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 121 */     return (this.buffer == null && super.isStreaming());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/BufferedHttpEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */