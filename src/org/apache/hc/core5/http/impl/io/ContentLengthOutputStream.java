/*     */ package org.apache.hc.core5.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.hc.core5.http.StreamClosedException;
/*     */ import org.apache.hc.core5.http.io.SessionOutputBuffer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentLengthOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final SessionOutputBuffer buffer;
/*     */   private final OutputStream outputStream;
/*     */   private final long contentLength;
/*     */   private long total;
/*     */   private boolean closed;
/*     */   
/*     */   public ContentLengthOutputStream(SessionOutputBuffer buffer, OutputStream outputStream, long contentLength) {
/*  79 */     this.buffer = (SessionOutputBuffer)Args.notNull(buffer, "Session output buffer");
/*  80 */     this.outputStream = (OutputStream)Args.notNull(outputStream, "Output stream");
/*  81 */     this.contentLength = Args.notNegative(contentLength, "Content length");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  90 */     if (!this.closed) {
/*  91 */       this.closed = true;
/*  92 */       this.buffer.flush(this.outputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  98 */     this.buffer.flush(this.outputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/* 103 */     if (this.closed) {
/* 104 */       throw new StreamClosedException();
/*     */     }
/* 106 */     if (this.total < this.contentLength) {
/* 107 */       long max = this.contentLength - this.total;
/* 108 */       int chunk = len;
/* 109 */       if (chunk > max) {
/* 110 */         chunk = (int)max;
/*     */       }
/* 112 */       this.buffer.write(b, off, chunk, this.outputStream);
/* 113 */       this.total += chunk;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/* 119 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/* 124 */     if (this.closed) {
/* 125 */       throw new StreamClosedException();
/*     */     }
/* 127 */     if (this.total < this.contentLength) {
/* 128 */       this.buffer.write(b, this.outputStream);
/* 129 */       this.total++;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/io/ContentLengthOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */