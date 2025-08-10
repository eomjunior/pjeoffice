/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.nio.FileContentEncoder;
/*     */ import org.apache.hc.core5.http.nio.SessionOutputBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdentityEncoder
/*     */   extends AbstractContentEncoder
/*     */   implements FileContentEncoder
/*     */ {
/*     */   private final int fragHint;
/*     */   
/*     */   public IdentityEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics, int chunkSizeHint) {
/*  70 */     super(channel, buffer, metrics);
/*  71 */     this.fragHint = Math.max(chunkSizeHint, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IdentityEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics) {
/*  78 */     this(channel, buffer, metrics, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/*  83 */     if (src == null) {
/*  84 */       return 0;
/*     */     }
/*  86 */     assertNotCompleted();
/*     */     
/*  88 */     int total = 0;
/*  89 */     while (src.hasRemaining()) {
/*  90 */       if ((this.buffer.hasData() || this.fragHint > 0) && 
/*  91 */         src.remaining() <= this.fragHint) {
/*  92 */         int capacity = this.fragHint - this.buffer.length();
/*  93 */         if (capacity > 0) {
/*  94 */           int limit = Math.min(capacity, src.remaining());
/*  95 */           int bytesWritten = writeToBuffer(src, limit);
/*  96 */           total += bytesWritten;
/*     */         } 
/*     */       } 
/*     */       
/* 100 */       if (this.buffer.hasData() && (
/* 101 */         this.buffer.length() >= this.fragHint || src.hasRemaining())) {
/* 102 */         int bytesWritten = flushToChannel();
/* 103 */         if (bytesWritten == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 108 */       if (!this.buffer.hasData() && src.remaining() > this.fragHint) {
/* 109 */         int bytesWritten = writeToChannel(src);
/* 110 */         total += bytesWritten;
/* 111 */         if (bytesWritten == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 116 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transfer(FileChannel src, long position, long count) throws IOException {
/* 125 */     if (src == null) {
/* 126 */       return 0L;
/*     */     }
/* 128 */     assertNotCompleted();
/*     */     
/* 130 */     flushToChannel();
/* 131 */     if (this.buffer.hasData()) {
/* 132 */       return 0L;
/*     */     }
/*     */     
/* 135 */     long bytesWritten = src.transferTo(position, count, this.channel);
/* 136 */     if (bytesWritten > 0L) {
/* 137 */       this.metrics.incrementBytesTransferred(bytesWritten);
/*     */     }
/* 139 */     return bytesWritten;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 144 */     StringBuilder sb = new StringBuilder();
/* 145 */     sb.append("[identity; completed: ");
/* 146 */     sb.append(isCompleted());
/* 147 */     sb.append("]");
/* 148 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/IdentityEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */