/*     */ package org.apache.hc.core5.http.io.entity;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ @Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/*     */ public class ByteArrayEntity
/*     */   extends AbstractHttpEntity
/*     */ {
/*     */   private final byte[] b;
/*     */   private final int off;
/*     */   private final int len;
/*     */   
/*     */   public ByteArrayEntity(byte[] b, int off, int len, ContentType contentType, String contentEncoding, boolean chunked) {
/*  57 */     super(contentType, contentEncoding, chunked);
/*  58 */     Args.notNull(b, "Source byte array");
/*  59 */     Args.notNegative(off, "offset");
/*  60 */     Args.notNegative(len, "length");
/*  61 */     Args.notNegative(off + len, "off + len");
/*  62 */     Args.check((off <= b.length), "off %s cannot be greater then b.length %s ", new Object[] { Integer.valueOf(off), Integer.valueOf(b.length) });
/*  63 */     Args.check((off + len <= b.length), "off + len  %s cannot be less then b.length %s ", new Object[] { Integer.valueOf(off + len), Integer.valueOf(b.length) });
/*  64 */     this.b = b;
/*  65 */     this.off = off;
/*  66 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayEntity(byte[] b, int off, int len, ContentType contentType, String contentEncoding) {
/*  74 */     this(b, off, len, contentType, contentEncoding, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayEntity(byte[] b, ContentType contentType, String contentEncoding, boolean chunked) {
/*  82 */     super(contentType, contentEncoding, chunked);
/*  83 */     Args.notNull(b, "Source byte array");
/*  84 */     this.b = b;
/*  85 */     this.off = 0;
/*  86 */     this.len = this.b.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArrayEntity(byte[] b, ContentType contentType, String contentEncoding) {
/*  93 */     this(b, contentType, contentEncoding, false);
/*     */   }
/*     */   
/*     */   public ByteArrayEntity(byte[] b, ContentType contentType, boolean chunked) {
/*  97 */     this(b, contentType, (String)null, chunked);
/*     */   }
/*     */   
/*     */   public ByteArrayEntity(byte[] b, ContentType contentType) {
/* 101 */     this(b, contentType, (String)null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteArrayEntity(byte[] b, int off, int len, ContentType contentType, boolean chunked) {
/* 106 */     this(b, off, len, contentType, null, chunked);
/*     */   }
/*     */   
/*     */   public ByteArrayEntity(byte[] b, int off, int len, ContentType contentType) {
/* 110 */     this(b, off, len, contentType, null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRepeatable() {
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getContentLength() {
/* 120 */     return this.len;
/*     */   }
/*     */ 
/*     */   
/*     */   public final InputStream getContent() {
/* 125 */     return new ByteArrayInputStream(this.b, this.off, this.len);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void writeTo(OutputStream outStream) throws IOException {
/* 130 */     Args.notNull(outStream, "Output stream");
/* 131 */     outStream.write(this.b, this.off, this.len);
/* 132 */     outStream.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isStreaming() {
/* 137 */     return false;
/*     */   }
/*     */   
/*     */   public final void close() throws IOException {}
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/io/entity/ByteArrayEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */