/*     */ package org.apache.hc.core5.http.nio.entity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.hc.core5.http.ContentType;
/*     */ import org.apache.hc.core5.http.nio.AsyncEntityProducer;
/*     */ import org.apache.hc.core5.http.nio.DataStreamChannel;
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
/*     */ public class BasicAsyncEntityProducer
/*     */   implements AsyncEntityProducer
/*     */ {
/*     */   private final ByteBuffer bytebuf;
/*     */   private final int length;
/*     */   private final ContentType contentType;
/*     */   private final boolean chunked;
/*     */   private final AtomicReference<Exception> exception;
/*     */   
/*     */   public BasicAsyncEntityProducer(byte[] content, ContentType contentType, boolean chunked) {
/*  57 */     Args.notNull(content, "Content");
/*  58 */     this.bytebuf = ByteBuffer.wrap(content);
/*  59 */     this.length = this.bytebuf.remaining();
/*  60 */     this.contentType = contentType;
/*  61 */     this.chunked = chunked;
/*  62 */     this.exception = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public BasicAsyncEntityProducer(byte[] content, ContentType contentType) {
/*  66 */     this(content, contentType, false);
/*     */   }
/*     */   
/*     */   public BasicAsyncEntityProducer(byte[] content) {
/*  70 */     this(content, ContentType.APPLICATION_OCTET_STREAM);
/*     */   }
/*     */   
/*     */   public BasicAsyncEntityProducer(CharSequence content, ContentType contentType, boolean chunked) {
/*  74 */     Args.notNull(content, "Content");
/*  75 */     this.contentType = contentType;
/*  76 */     Charset charset = ContentType.getCharset(contentType, StandardCharsets.US_ASCII);
/*  77 */     this.bytebuf = charset.encode(CharBuffer.wrap(content));
/*  78 */     this.length = this.bytebuf.remaining();
/*  79 */     this.chunked = chunked;
/*  80 */     this.exception = new AtomicReference<>();
/*     */   }
/*     */   
/*     */   public BasicAsyncEntityProducer(CharSequence content, ContentType contentType) {
/*  84 */     this(content, contentType, false);
/*     */   }
/*     */   
/*     */   public BasicAsyncEntityProducer(CharSequence content) {
/*  88 */     this(content, ContentType.TEXT_PLAIN);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRepeatable() {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getContentType() {
/*  98 */     return (this.contentType != null) ? this.contentType.toString() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getContentLength() {
/* 103 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() {
/* 108 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentEncoding() {
/* 113 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChunked() {
/* 118 */     return this.chunked;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getTrailerNames() {
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void produce(DataStreamChannel channel) throws IOException {
/* 128 */     if (this.bytebuf.hasRemaining()) {
/* 129 */       channel.write(this.bytebuf);
/*     */     }
/* 131 */     if (!this.bytebuf.hasRemaining()) {
/* 132 */       channel.endStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void failed(Exception cause) {
/* 138 */     if (this.exception.compareAndSet(null, cause)) {
/* 139 */       releaseResources();
/*     */     }
/*     */   }
/*     */   
/*     */   public final Exception getException() {
/* 144 */     return this.exception.get();
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseResources() {
/* 149 */     this.bytebuf.clear();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/nio/entity/BasicAsyncEntityProducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */