/*     */ package org.apache.hc.core5.http2.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.hc.core5.http.ConnectionClosedException;
/*     */ import org.apache.hc.core5.http2.H2ConnectionException;
/*     */ import org.apache.hc.core5.http2.H2CorruptFrameException;
/*     */ import org.apache.hc.core5.http2.H2Error;
/*     */ import org.apache.hc.core5.http2.H2TransportMetrics;
/*     */ import org.apache.hc.core5.http2.frame.FrameFlag;
/*     */ import org.apache.hc.core5.http2.frame.RawFrame;
/*     */ import org.apache.hc.core5.http2.impl.BasicH2TransportMetrics;
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
/*     */ public final class FrameInputBuffer
/*     */ {
/*     */   private final BasicH2TransportMetrics metrics;
/*     */   private final int maxFramePayloadSize;
/*     */   private final byte[] buffer;
/*     */   private int off;
/*     */   private int dataLen;
/*     */   
/*     */   FrameInputBuffer(BasicH2TransportMetrics metrics, int bufferLen, int maxFramePayloadSize) {
/*  60 */     Args.notNull(metrics, "HTTP2 transport metrics");
/*  61 */     Args.positive(maxFramePayloadSize, "Maximum payload size");
/*  62 */     this.metrics = metrics;
/*  63 */     this.maxFramePayloadSize = maxFramePayloadSize;
/*  64 */     this.buffer = new byte[bufferLen];
/*  65 */     this.dataLen = 0;
/*     */   }
/*     */   
/*     */   public FrameInputBuffer(BasicH2TransportMetrics metrics, int maxFramePayloadSize) {
/*  69 */     this(metrics, 9 + maxFramePayloadSize, maxFramePayloadSize);
/*     */   }
/*     */   
/*     */   public FrameInputBuffer(int maxFramePayloadSize) {
/*  73 */     this(new BasicH2TransportMetrics(), maxFramePayloadSize);
/*     */   }
/*     */   
/*     */   boolean hasData() {
/*  77 */     return (this.dataLen > 0);
/*     */   }
/*     */   
/*     */   void fillBuffer(InputStream inStream, int requiredLen) throws IOException {
/*  81 */     while (this.dataLen < requiredLen) {
/*  82 */       if (this.off > 0) {
/*  83 */         System.arraycopy(this.buffer, this.off, this.buffer, 0, this.dataLen);
/*  84 */         this.off = 0;
/*     */       } 
/*  86 */       int bytesRead = inStream.read(this.buffer, this.off + this.dataLen, this.buffer.length - this.dataLen);
/*  87 */       if (bytesRead == -1) {
/*  88 */         if (this.dataLen > 0) {
/*  89 */           throw new H2CorruptFrameException("Corrupt or incomplete HTTP2 frame");
/*     */         }
/*  91 */         throw new ConnectionClosedException();
/*     */       } 
/*  93 */       this.dataLen += bytesRead;
/*  94 */       this.metrics.incrementBytesTransferred(bytesRead);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public RawFrame read(InputStream inStream) throws IOException {
/* 100 */     fillBuffer(inStream, 9);
/* 101 */     int payloadOff = 9;
/*     */     
/* 103 */     int payloadLen = (this.buffer[this.off] & 0xFF) << 16 | (this.buffer[this.off + 1] & 0xFF) << 8 | this.buffer[this.off + 2] & 0xFF;
/* 104 */     int type = this.buffer[this.off + 3] & 0xFF;
/* 105 */     int flags = this.buffer[this.off + 4] & 0xFF;
/* 106 */     int streamId = Math.abs(this.buffer[this.off + 5] & 0xFF) << 24 | this.buffer[this.off + 6] & 0xFF0000 | (this.buffer[this.off + 7] & 0xFF) << 8 | this.buffer[this.off + 8] & 0xFF;
/* 107 */     if (payloadLen > this.maxFramePayloadSize) {
/* 108 */       throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Frame size exceeds maximum");
/*     */     }
/*     */     
/* 111 */     int frameLen = 9 + payloadLen;
/* 112 */     fillBuffer(inStream, frameLen);
/*     */     
/* 114 */     if ((flags & FrameFlag.PADDED.getValue()) > 0) {
/* 115 */       if (payloadLen == 0) {
/* 116 */         throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Inconsistent padding");
/*     */       }
/* 118 */       int padding = this.buffer[this.off + 9] & 0xFF;
/* 119 */       if (payloadLen < padding + 1) {
/* 120 */         throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Inconsistent padding");
/*     */       }
/*     */     } 
/*     */     
/* 124 */     ByteBuffer payload = (payloadLen > 0) ? ByteBuffer.wrap(this.buffer, this.off + 9, payloadLen) : null;
/* 125 */     RawFrame frame = new RawFrame(type, flags, streamId, payload);
/*     */     
/* 127 */     this.off += frameLen;
/* 128 */     this.dataLen -= frameLen;
/*     */     
/* 130 */     this.metrics.incrementFramesTransferred();
/*     */     
/* 132 */     return frame;
/*     */   }
/*     */   
/*     */   public H2TransportMetrics getMetrics() {
/* 136 */     return (H2TransportMetrics)this.metrics;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/io/FrameInputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */