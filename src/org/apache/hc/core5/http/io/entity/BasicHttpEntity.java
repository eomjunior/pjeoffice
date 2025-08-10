/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.io.Closer;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ public class BasicHttpEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private final InputStream content;
/*     */   private final long length;
/*     */   
/*     */   public BasicHttpEntity(InputStream content, long length, ContentType contentType, String contentEncoding, boolean chunked) {
/*  53 */     super(contentType, contentEncoding, chunked);
/*  54 */     this.content = (InputStream)Args.notNull(content, "Content stream");
/*  55 */     this.length = length;
/*     */   }
/*     */ 
/*     */   
/*     */   public BasicHttpEntity(InputStream content, long length, ContentType contentType, String contentEncoding) {
/*  60 */     this(content, length, contentType, contentEncoding, false);
/*     */   }
/*     */   
/*     */   public BasicHttpEntity(InputStream content, long length, ContentType contentType) {
/*  64 */     this(content, length, contentType, null);
/*     */   }
/*     */   
/*     */   public BasicHttpEntity(InputStream content, ContentType contentType, String contentEncoding) {
/*  68 */     this(content, -1L, contentType, contentEncoding);
/*     */   }
/*     */   
/*     */   public BasicHttpEntity(InputStream content, ContentType contentType) {
/*  72 */     this(content, -1L, contentType, null);
/*     */   }
/*     */   
/*     */   public BasicHttpEntity(InputStream content, ContentType contentType, boolean chunked) {
/*  76 */     this(content, -1L, contentType, null, chunked);
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getContentLength() {
/*  81 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public final InputStream getContent() throws IllegalStateException {
/*  86 */     return this.content;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRepeatable() {
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStreaming() {
/*  96 */     return (this.content != null && this.content != EmptyInputStream.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() throws IOException {
/* 101 */     Closer.close(this.content);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/BasicHttpEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */