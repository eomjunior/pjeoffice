/*     */ package org.apache.hc.core5.http.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.List;
/*     */ import org.apache.hc.core5.http.FormattedHeader;
/*     */ import org.apache.hc.core5.http.Header;
/*     */ import org.apache.hc.core5.http.impl.BasicHttpTransportMetrics;
/*     */ import org.apache.hc.core5.http.message.BasicLineFormatter;
/*     */ import org.apache.hc.core5.http.nio.SessionOutputBuffer;
/*     */ import org.apache.hc.core5.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkEncoder
/*     */   extends AbstractContentEncoder
/*     */ {
/*     */   private final int chunkSizeHint;
/*     */   private final CharArrayBuffer lineBuffer;
/*     */   
/*     */   public ChunkEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics, int chunkSizeHint) {
/*  65 */     super(channel, buffer, metrics);
/*  66 */     this.chunkSizeHint = Math.max(chunkSizeHint, 0);
/*  67 */     this.lineBuffer = new CharArrayBuffer(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkEncoder(WritableByteChannel channel, SessionOutputBuffer buffer, BasicHttpTransportMetrics metrics) {
/*  74 */     this(channel, buffer, metrics, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int write(ByteBuffer src) throws IOException {
/*  79 */     if (src == null) {
/*  80 */       return 0;
/*     */     }
/*  82 */     assertNotCompleted();
/*     */     
/*  84 */     int total = 0;
/*  85 */     while (src.hasRemaining()) {
/*  86 */       int chunk = src.remaining();
/*     */       
/*  88 */       int avail = this.buffer.capacity();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  93 */       avail -= 12;
/*  94 */       if (avail > 0) {
/*  95 */         if (avail < chunk) {
/*     */           
/*  97 */           chunk = avail;
/*  98 */           this.lineBuffer.clear();
/*  99 */           this.lineBuffer.append(Integer.toHexString(chunk));
/* 100 */           this.buffer.writeLine(this.lineBuffer);
/* 101 */           int oldlimit = src.limit();
/* 102 */           src.limit(src.position() + chunk);
/* 103 */           this.buffer.write(src);
/* 104 */           src.limit(oldlimit);
/*     */         } else {
/*     */           
/* 107 */           this.lineBuffer.clear();
/* 108 */           this.lineBuffer.append(Integer.toHexString(chunk));
/* 109 */           this.buffer.writeLine(this.lineBuffer);
/* 110 */           this.buffer.write(src);
/*     */         } 
/* 112 */         this.lineBuffer.clear();
/* 113 */         this.buffer.writeLine(this.lineBuffer);
/* 114 */         total += chunk;
/*     */       } 
/* 116 */       if (this.buffer.length() >= this.chunkSizeHint || src.hasRemaining()) {
/* 117 */         int bytesWritten = flushToChannel();
/* 118 */         if (bytesWritten == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/* 123 */     return total;
/*     */   }
/*     */ 
/*     */   
/*     */   public void complete(List<? extends Header> trailers) throws IOException {
/* 128 */     assertNotCompleted();
/* 129 */     this.lineBuffer.clear();
/* 130 */     this.lineBuffer.append("0");
/* 131 */     this.buffer.writeLine(this.lineBuffer);
/* 132 */     writeTrailers(trailers);
/* 133 */     this.lineBuffer.clear();
/* 134 */     this.buffer.writeLine(this.lineBuffer);
/* 135 */     super.complete(trailers);
/*     */   }
/*     */   
/*     */   private void writeTrailers(List<? extends Header> trailers) throws IOException {
/* 139 */     if (trailers != null) {
/* 140 */       for (int i = 0; i < trailers.size(); i++) {
/* 141 */         Header header = trailers.get(i);
/* 142 */         if (header instanceof FormattedHeader) {
/* 143 */           CharArrayBuffer chbuffer = ((FormattedHeader)header).getBuffer();
/* 144 */           this.buffer.writeLine(chbuffer);
/*     */         } else {
/* 146 */           this.lineBuffer.clear();
/* 147 */           BasicLineFormatter.INSTANCE.formatHeader(this.lineBuffer, header);
/* 148 */           this.buffer.writeLine(this.lineBuffer);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 156 */     StringBuilder sb = new StringBuilder();
/* 157 */     sb.append("[chunk-coded; completed: ");
/* 158 */     sb.append(isCompleted());
/* 159 */     sb.append("]");
/* 160 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http/impl/nio/ChunkEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */