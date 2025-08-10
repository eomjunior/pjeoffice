/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.nio.ContentEncoder;
/*     */ import org.apache.hc.core5.http.nio.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.util.Args;
/*     */ import org.apache.hc.core5.util.Asserts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractContentEncoder
/*     */   implements ContentEncoder
/*     */ {
/*     */   final WritableByteChannel channel;
/*     */   final SessionOutputBuffer buffer;
/*     */   final BasicHttpTransportMetrics metrics;
/*     */   boolean completed;
/*     */   
/*     */   public AbstractContentEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics) {
/*  69 */     Args.notNull(channel, "Channel");
/*  70 */     Args.notNull(buffer, "Session input buffer");
/*  71 */     Args.notNull(metrics, "Transport metrics");
/*  72 */     this.buffer = buffer;
/*  73 */     this.channel = channel;
/*  74 */     this.metrics = metrics;
/*     */   }
/*     */   
/*     */   protected WritableByteChannel channel() {
/*  78 */     return this.channel;
/*     */   }
/*     */   
/*     */   protected SessionOutputBuffer buffer() {
/*  82 */     return this.buffer;
/*     */   }
/*     */   
/*     */   protected BasicHttpTransportMetrics metrics() {
/*  86 */     return this.metrics;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCompleted() {
/*  91 */     return this.completed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void complete(List<? extends Header> trailers) throws IOException {
/*  96 */     this.completed = true;
/*     */   }
/*     */   
/*     */   public final void complete() throws IOException {
/* 100 */     complete(null);
/*     */   }
/*     */   
/*     */   protected void assertNotCompleted() {
/* 104 */     Asserts.check(!this.completed, "Encoding process already completed");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int flushToChannel() throws IOException {
/* 115 */     if (!this.buffer.hasData()) {
/* 116 */       return 0;
/*     */     }
/* 118 */     int bytesWritten = this.buffer.flush(this.channel);
/* 119 */     if (bytesWritten > 0) {
/* 120 */       this.metrics.incrementBytesTransferred(bytesWritten);
/*     */     }
/* 122 */     return bytesWritten;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int writeToChannel(ByteBuffer src) throws IOException {
/* 133 */     if (!src.hasRemaining()) {
/* 134 */       return 0;
/*     */     }
/* 136 */     int bytesWritten = this.channel.write(src);
/* 137 */     if (bytesWritten > 0) {
/* 138 */       this.metrics.incrementBytesTransferred(bytesWritten);
/*     */     }
/* 140 */     return bytesWritten;
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
/*     */   protected int writeToChannel(ByteBuffer src, int limit) throws IOException {
/* 153 */     return doWriteChunk(src, limit, true);
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
/*     */   protected int writeToBuffer(ByteBuffer src, int limit) throws IOException {
/* 166 */     return doWriteChunk(src, limit, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private int doWriteChunk(ByteBuffer src, int chunk, boolean direct) throws IOException {
/*     */     int bytesWritten;
/* 172 */     if (src.remaining() > chunk) {
/* 173 */       int oldLimit = src.limit();
/* 174 */       int newLimit = oldLimit - src.remaining() - chunk;
/* 175 */       src.limit(newLimit);
/* 176 */       bytesWritten = doWriteChunk(src, direct);
/* 177 */       src.limit(oldLimit);
/*     */     } else {
/* 179 */       bytesWritten = doWriteChunk(src, direct);
/*     */     } 
/* 181 */     return bytesWritten;
/*     */   }
/*     */   
/*     */   private int doWriteChunk(ByteBuffer src, boolean direct) throws IOException {
/* 185 */     if (direct) {
/* 186 */       int bytesWritten = this.channel.write(src);
/* 187 */       if (bytesWritten > 0) {
/* 188 */         this.metrics.incrementBytesTransferred(bytesWritten);
/*     */       }
/* 190 */       return bytesWritten;
/*     */     } 
/* 192 */     int chunk = src.remaining();
/* 193 */     this.buffer.write(src);
/* 194 */     return chunk;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/AbstractContentEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */