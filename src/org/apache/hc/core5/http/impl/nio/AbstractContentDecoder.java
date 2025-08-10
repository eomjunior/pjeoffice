/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.nio.ContentDecoder;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractContentDecoder
/*     */   implements ContentDecoder
/*     */ {
/*     */   final ReadableByteChannel channel;
/*     */   final SessionInputBuffer buffer;
/*     */   final BasicHttpTransportMetrics metrics;
/*     */   protected boolean completed;
/*     */   
/*     */   public AbstractContentDecoder(ReadableByteChannel channel, SessionInputBuffer buffer, BasicHttpTransportMetrics metrics) {
/*  68 */     Args.notNull(channel, "Channel");
/*  69 */     Args.notNull(buffer, "Session input buffer");
/*  70 */     Args.notNull(metrics, "Transport metrics");
/*  71 */     this.buffer = buffer;
/*  72 */     this.channel = channel;
/*  73 */     this.metrics = metrics;
/*     */   }
/*     */   
/*     */   protected ReadableByteChannel channel() {
/*  77 */     return this.channel;
/*     */   }
/*     */   
/*     */   protected SessionInputBuffer buffer() {
/*  81 */     return this.buffer;
/*     */   }
/*     */   
/*     */   protected BasicHttpTransportMetrics metrics() {
/*  85 */     return this.metrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompleted() {
/*  90 */     return this.completed;
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
/*     */   public void setCompleted(boolean completed) {
/* 104 */     this.completed = completed;
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
/*     */   protected void setCompleted() {
/* 117 */     this.completed = true;
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
/*     */   protected int readFromChannel(ByteBuffer dst) throws IOException {
/* 129 */     int bytesRead = this.channel.read(dst);
/* 130 */     if (bytesRead > 0) {
/* 131 */       this.metrics.incrementBytesTransferred(bytesRead);
/*     */     }
/* 133 */     return bytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int fillBufferFromChannel() throws IOException {
/* 143 */     int bytesRead = this.buffer.fill(this.channel);
/* 144 */     if (bytesRead > 0) {
/* 145 */       this.metrics.incrementBytesTransferred(bytesRead);
/*     */     }
/* 147 */     return bytesRead;
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
/*     */   protected int readFromChannel(ByteBuffer dst, int limit) throws IOException {
/*     */     int bytesRead;
/* 161 */     if (dst.remaining() > limit) {
/* 162 */       int oldLimit = dst.limit();
/* 163 */       int newLimit = oldLimit - dst.remaining() - limit;
/* 164 */       dst.limit(newLimit);
/* 165 */       bytesRead = this.channel.read(dst);
/* 166 */       dst.limit(oldLimit);
/*     */     } else {
/* 168 */       bytesRead = this.channel.read(dst);
/*     */     } 
/* 170 */     if (bytesRead > 0) {
/* 171 */       this.metrics.incrementBytesTransferred(bytesRead);
/*     */     }
/* 173 */     return bytesRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<? extends Header> getTrailers() {
/* 178 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/AbstractContentDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */