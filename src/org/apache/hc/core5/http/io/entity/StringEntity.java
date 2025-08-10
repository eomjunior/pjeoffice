/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.hc.core5.annotation.Contract;
/*     */ import org.apache.hc.core5.annotation.ThreadingBehavior;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE)
/*     */ public class StringEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private final byte[] content;
/*     */   
/*     */   public StringEntity(String string, ContentType contentType, String contentEncoding, boolean chunked) {
/*  63 */     super(contentType, contentEncoding, chunked);
/*  64 */     Args.notNull(string, "Source string");
/*  65 */     Charset charset = ContentType.getCharset(contentType, StandardCharsets.ISO_8859_1);
/*  66 */     this.content = string.getBytes(charset);
/*     */   }
/*     */   
/*     */   public StringEntity(String string, ContentType contentType, boolean chunked) {
/*  70 */     this(string, contentType, null, chunked);
/*     */   }
/*     */   
/*     */   public StringEntity(String string, ContentType contentType) {
/*  74 */     this(string, contentType, null, false);
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
/*     */ 
/*     */   
/*     */   public StringEntity(String string, Charset charset) {
/*  88 */     this(string, ContentType.TEXT_PLAIN.withCharset(charset));
/*     */   }
/*     */   
/*     */   public StringEntity(String string, Charset charset, boolean chunked) {
/*  92 */     this(string, ContentType.TEXT_PLAIN.withCharset(charset), chunked);
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
/*     */   public StringEntity(String string) {
/* 104 */     this(string, ContentType.DEFAULT_TEXT);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRepeatable() {
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getContentLength() {
/* 114 */     return this.content.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public final InputStream getContent() throws IOException {
/* 119 */     return new ByteArrayInputStream(this.content);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeTo(OutputStream outStream) throws IOException {
/* 124 */     Args.notNull(outStream, "Output stream");
/* 125 */     outStream.write(this.content);
/* 126 */     outStream.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStreaming() {
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   public final void close() throws IOException {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/StringEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */