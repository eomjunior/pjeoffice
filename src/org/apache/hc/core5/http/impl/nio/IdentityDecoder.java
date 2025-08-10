/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
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
/*     */ public class IdentityDecoder
/*     */   extends AbstractContentDecoder
/*     */   implements FileContentDecoder
/*     */ {
/*     */   public IdentityDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, BasicHttpTransportMetrics metrics) {
/*  58 */     super(channel, buffer, metrics);
/*     */   }
/*     */   
/*     */   public int read(ByteBuffer dst) throws IOException {
/*     */     int bytesRead;
/*  63 */     Args.notNull(dst, "Byte buffer");
/*  64 */     if (isCompleted()) {
/*  65 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*  69 */     if (this.buffer.hasData()) {
/*  70 */       bytesRead = this.buffer.read(dst);
/*     */     } else {
/*  72 */       bytesRead = readFromChannel(dst);
/*     */     } 
/*  74 */     if (bytesRead == -1) {
/*  75 */       setCompleted();
/*     */     }
/*  77 */     return bytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long transfer(FileChannel dst, long position, long count) throws IOException {
/*     */     long bytesRead;
/*  86 */     if (dst == null) {
/*  87 */       return 0L;
/*     */     }
/*  89 */     if (isCompleted()) {
/*  90 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/*  94 */     if (this.buffer.hasData()) {
/*  95 */       int maxLen = this.buffer.length();
/*  96 */       dst.position(position);
/*  97 */       bytesRead = this.buffer.read(dst, (count < maxLen) ? (int)count : maxLen);
/*     */     } else {
/*  99 */       if (this.channel.isOpen()) {
/* 100 */         if (position > dst.size()) {
/* 101 */           throw new IOException("Position past end of file [" + position + " > " + dst
/* 102 */               .size() + "]");
/*     */         }
/* 104 */         bytesRead = dst.transferFrom(this.channel, position, count);
/* 105 */         if (count > 0L && bytesRead == 0L) {
/* 106 */           bytesRead = this.buffer.fill(this.channel);
/*     */         }
/*     */       } else {
/* 109 */         bytesRead = -1L;
/*     */       } 
/* 111 */       if (bytesRead > 0L) {
/* 112 */         this.metrics.incrementBytesTransferred(bytesRead);
/*     */       }
/*     */     } 
/* 115 */     if (bytesRead == -1L) {
/* 116 */       setCompleted();
/*     */     }
/* 118 */     return bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     StringBuilder sb = new StringBuilder();
/* 124 */     sb.append("[identity; completed: ");
/* 125 */     sb.append(this.completed);
/* 126 */     sb.append("]");
/* 127 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/IdentityDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */