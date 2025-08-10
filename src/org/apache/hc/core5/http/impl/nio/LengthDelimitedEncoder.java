/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.nio.FileContentEncoder;
/*     */ import org.apache.hc.core5.http.nio.SessionOutputBuffer;
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
/*     */ public class LengthDelimitedEncoder
/*     */   extends AbstractContentEncoder
/*     */   implements FileContentEncoder
/*     */ {
/*     */   private final long contentLength;
/*     */   private final int fragHint;
/*     */   private long remaining;
/*     */   
/*     */   public LengthDelimitedEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics, long contentLength, int chunkSizeHint) {
/*  77 */     super(channel, buffer, metrics);
/*  78 */     Args.notNegative(contentLength, "Content length");
/*  79 */     this.contentLength = contentLength;
/*  80 */     this.fragHint = Math.max(chunkSizeHint, 0);
/*  81 */     this.remaining = contentLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LengthDelimitedEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics, long contentLength) {
/*  89 */     this(channel, buffer, metrics, contentLength, 0);
/*     */   }
/*     */   
/*     */   private int nextChunk(ByteBuffer src) {
/*  93 */     return (int)Math.min(Math.min(this.remaining, 2147483647L), src.remaining());
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/*  98 */     if (src == null) {
/*  99 */       return 0;
/*     */     }
/* 101 */     assertNotCompleted();
/*     */     
/* 103 */     int total = 0;
/* 104 */     while (src.hasRemaining() && this.remaining > 0L) {
/* 105 */       if (this.buffer.hasData() || this.fragHint > 0) {
/* 106 */         int chunk = nextChunk(src);
/* 107 */         if (chunk <= this.fragHint) {
/* 108 */           int capacity = this.fragHint - this.buffer.length();
/* 109 */           if (capacity > 0) {
/* 110 */             int limit = Math.min(capacity, chunk);
/* 111 */             int bytesWritten = writeToBuffer(src, limit);
/* 112 */             this.remaining -= bytesWritten;
/* 113 */             total += bytesWritten;
/*     */           } 
/*     */         } 
/*     */       } 
/* 117 */       if (this.buffer.hasData()) {
/* 118 */         int chunk = nextChunk(src);
/* 119 */         if (this.buffer.length() >= this.fragHint || chunk > 0) {
/* 120 */           int bytesWritten = flushToChannel();
/* 121 */           if (bytesWritten == 0) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/* 126 */       if (!this.buffer.hasData()) {
/* 127 */         int chunk = nextChunk(src);
/* 128 */         if (chunk > this.fragHint) {
/* 129 */           int bytesWritten = writeToChannel(src, chunk);
/* 130 */           this.remaining -= bytesWritten;
/* 131 */           total += bytesWritten;
/* 132 */           if (bytesWritten == 0) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 138 */     if (this.remaining <= 0L) {
/* 139 */       complete(null);
/*     */     }
/* 141 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transfer(FileChannel src, long position, long count) throws IOException {
/* 150 */     if (src == null) {
/* 151 */       return 0L;
/*     */     }
/* 153 */     assertNotCompleted();
/*     */     
/* 155 */     flushToChannel();
/* 156 */     if (this.buffer.hasData()) {
/* 157 */       return 0L;
/*     */     }
/*     */     
/* 160 */     long chunk = Math.min(this.remaining, count);
/* 161 */     long bytesWritten = src.transferTo(position, chunk, this.channel);
/* 162 */     if (bytesWritten > 0L) {
/* 163 */       this.metrics.incrementBytesTransferred(bytesWritten);
/*     */     }
/* 165 */     this.remaining -= bytesWritten;
/* 166 */     if (this.remaining <= 0L) {
/* 167 */       complete(null);
/*     */     }
/* 169 */     return bytesWritten;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 174 */     StringBuilder sb = new StringBuilder();
/* 175 */     sb.append("[content length: ");
/* 176 */     sb.append(this.contentLength);
/* 177 */     sb.append("; pos: ");
/* 178 */     sb.append(this.contentLength - this.remaining);
/* 179 */     sb.append("; completed: ");
/* 180 */     sb.append(isCompleted());
/* 181 */     sb.append("]");
/* 182 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/LengthDelimitedEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */