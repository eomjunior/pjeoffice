/*     */ package org.apache.hc.core5.http2.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.hc.core5.http2.H2ConnectionException;
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
/*     */ public final class FrameOutputBuffer
/*     */ {
/*     */   private final BasicH2TransportMetrics metrics;
/*     */   private final int maxFramePayloadSize;
/*     */   private final byte[] buffer;
/*     */   
/*     */   public FrameOutputBuffer(BasicH2TransportMetrics metrics, int maxFramePayloadSize) {
/*  56 */     Args.notNull(metrics, "HTTP2 transport metrics");
/*  57 */     Args.positive(maxFramePayloadSize, "Maximum payload size");
/*  58 */     this.metrics = metrics;
/*  59 */     this.maxFramePayloadSize = maxFramePayloadSize;
/*  60 */     this.buffer = new byte[9 + maxFramePayloadSize + 255 + 1];
/*     */   }
/*     */   
/*     */   public FrameOutputBuffer(int maxFramePayloadSize) {
/*  64 */     this(new BasicH2TransportMetrics(), maxFramePayloadSize);
/*     */   }
/*     */   
/*     */   public void write(RawFrame frame, OutputStream outStream) throws IOException {
/*  68 */     if (frame == null) {
/*     */       return;
/*     */     }
/*  71 */     int type = frame.getType();
/*  72 */     long streamId = frame.getStreamId();
/*  73 */     int flags = frame.getFlags();
/*  74 */     ByteBuffer payload = frame.getPayload();
/*  75 */     int payloadLen = (payload != null) ? payload.remaining() : 0;
/*  76 */     if (payload != null && payload.remaining() > this.maxFramePayloadSize) {
/*  77 */       throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Frame size exceeds maximum");
/*     */     }
/*  79 */     this.buffer[0] = (byte)(payloadLen >> 16 & 0xFF);
/*  80 */     this.buffer[1] = (byte)(payloadLen >> 8 & 0xFF);
/*  81 */     this.buffer[2] = (byte)(payloadLen & 0xFF);
/*     */     
/*  83 */     this.buffer[3] = (byte)(type & 0xFF);
/*  84 */     this.buffer[4] = (byte)(flags & 0xFF);
/*     */     
/*  86 */     this.buffer[5] = (byte)(int)(streamId >> 24L & 0xFFL);
/*  87 */     this.buffer[6] = (byte)(int)(streamId >> 16L & 0xFFL);
/*  88 */     this.buffer[7] = (byte)(int)(streamId >> 8L & 0xFFL);
/*  89 */     this.buffer[8] = (byte)(int)(streamId & 0xFFL);
/*     */     
/*  91 */     int frameLen = 9;
/*  92 */     int padding = 0;
/*  93 */     if ((flags & FrameFlag.PADDED.getValue()) > 0) {
/*  94 */       padding = 16;
/*  95 */       this.buffer[9] = (byte)(padding & 0xFF);
/*  96 */       frameLen++;
/*     */     } 
/*  98 */     if (payload != null) {
/*  99 */       payload.get(this.buffer, frameLen, payload.remaining());
/* 100 */       frameLen += payloadLen;
/*     */     } 
/* 102 */     for (int i = 0; i < padding; i++) {
/* 103 */       this.buffer[frameLen++] = 0;
/*     */     }
/* 105 */     outStream.write(this.buffer, 0, frameLen);
/*     */     
/* 107 */     this.metrics.incrementFramesTransferred();
/* 108 */     this.metrics.incrementBytesTransferred(frameLen);
/*     */   }
/*     */   
/*     */   public H2TransportMetrics getMetrics() {
/* 112 */     return (H2TransportMetrics)this.metrics;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/io/FrameOutputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */