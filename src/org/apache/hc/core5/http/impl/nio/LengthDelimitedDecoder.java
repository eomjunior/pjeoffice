/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.nio.FileContentDecoder;
/*     */ import org.apache.hc.core5.http.nio.SessionInputBuffer;
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
/*     */ public class LengthDelimitedDecoder
/*     */   extends AbstractContentDecoder
/*     */   implements FileContentDecoder
/*     */ {
/*     */   private final long contentLength;
/*     */   private long len;
/*     */   
/*     */   public LengthDelimitedDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, BasicHttpTransportMetrics metrics, long contentLength) {
/*  65 */     super(channel, buffer, metrics);
/*  66 */     Args.notNegative(contentLength, "Content length");
/*  67 */     this.contentLength = contentLength;
/*     */   }
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/*     */     int bytesRead;
/*  72 */     Args.notNull(dst, "Byte buffer");
/*  73 */     if (isCompleted()) {
/*  74 */       return -1;
/*     */     }
/*  76 */     int chunk = (int)Math.min(this.contentLength - this.len, 2147483647L);
/*     */ 
/*     */     
/*  79 */     if (this.buffer.hasData()) {
/*  80 */       int maxLen = Math.min(chunk, this.buffer.length());
/*  81 */       bytesRead = this.buffer.read(dst, maxLen);
/*     */     } else {
/*  83 */       bytesRead = readFromChannel(dst, chunk);
/*     */     } 
/*  85 */     if (bytesRead == -1) {
/*  86 */       setCompleted();
/*  87 */       if (this.len < this.contentLength)
/*  88 */         throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: %d; received: %d)", new Object[] {
/*     */               
/*  90 */               Long.valueOf(this.contentLength), Long.valueOf(this.len)
/*     */             }); 
/*     */     } 
/*  93 */     this.len += bytesRead;
/*  94 */     if (this.len >= this.contentLength) {
/*  95 */       this.completed = true;
/*     */     }
/*  97 */     if (this.completed && bytesRead == 0) {
/*  98 */       return -1;
/*     */     }
/* 100 */     return bytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transfer(FileChannel dst, long position, long count) throws IOException {
/*     */     long bytesRead;
/* 109 */     if (dst == null) {
/* 110 */       return 0L;
/*     */     }
/* 112 */     if (isCompleted()) {
/* 113 */       return -1L;
/*     */     }
/*     */     
/* 116 */     int chunk = (int)Math.min(this.contentLength - this.len, 2147483647L);
/*     */ 
/*     */     
/* 119 */     if (this.buffer.hasData()) {
/* 120 */       int maxLen = Math.min(chunk, this.buffer.length());
/* 121 */       dst.position(position);
/* 122 */       bytesRead = this.buffer.read(dst, (count < maxLen) ? (int)count : maxLen);
/*     */     } else {
/* 124 */       if (this.channel.isOpen()) {
/* 125 */         if (position > dst.size())
/* 126 */           throw new IOException(String.format("Position past end of file [%d > %d]", new Object[] {
/* 127 */                   Long.valueOf(position), Long.valueOf(dst.size())
/*     */                 })); 
/* 129 */         bytesRead = dst.transferFrom(this.channel, position, (count < chunk) ? count : chunk);
/*     */       } else {
/* 131 */         bytesRead = -1L;
/*     */       } 
/* 133 */       if (bytesRead > 0L) {
/* 134 */         this.metrics.incrementBytesTransferred(bytesRead);
/*     */       }
/*     */     } 
/* 137 */     if (bytesRead == -1L) {
/* 138 */       setCompleted();
/* 139 */       if (this.len < this.contentLength)
/* 140 */         throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: %d; received: %d)", new Object[] {
/*     */               
/* 142 */               Long.valueOf(this.contentLength), Long.valueOf(this.len)
/*     */             }); 
/*     */     } 
/* 145 */     this.len += bytesRead;
/* 146 */     if (this.len >= this.contentLength) {
/* 147 */       this.completed = true;
/*     */     }
/* 149 */     return bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 154 */     StringBuilder sb = new StringBuilder();
/* 155 */     sb.append("[content length: ");
/* 156 */     sb.append(this.contentLength);
/* 157 */     sb.append("; pos: ");
/* 158 */     sb.append(this.len);
/* 159 */     sb.append("; completed: ");
/* 160 */     sb.append(this.completed);
/* 161 */     sb.append("]");
/* 162 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/LengthDelimitedDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */