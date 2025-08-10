/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.function.Supplier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ public class HttpEntityWrapper
/*     */   implements HttpEntity
/*     */ {
/*     */   private final HttpEntity wrappedEntity;
/*     */   
/*     */   public HttpEntityWrapper(HttpEntity wrappedEntity) {
/*  63 */     this.wrappedEntity = (HttpEntity)Args.notNull(wrappedEntity, "Wrapped entity");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  68 */     return this.wrappedEntity.isRepeatable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/*  73 */     return this.wrappedEntity.isChunked();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/*  78 */     return this.wrappedEntity.getContentLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentType() {
/*  83 */     return this.wrappedEntity.getContentType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/*  88 */     return this.wrappedEntity.getContentEncoding();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getContent() throws IOException {
/*  94 */     return this.wrappedEntity.getContent();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream outStream) throws IOException {
/* 100 */     this.wrappedEntity.writeTo(outStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStreaming() {
/* 105 */     return this.wrappedEntity.isStreaming();
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<List<? extends Header>> getTrailers() {
/* 110 */     return this.wrappedEntity.getTrailers();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 115 */     return this.wrappedEntity.getTrailerNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 120 */     this.wrappedEntity.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 125 */     return "Wrapper [" + this.wrappedEntity + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/HttpEntityWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */