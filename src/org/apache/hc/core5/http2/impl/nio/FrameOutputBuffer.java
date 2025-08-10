/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.GatheringByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import org.apache.hc.core5.http2.H2TransportMetrics;
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
/*     */ public final class FrameOutputBuffer
/*     */ {
/*     */   private final BasicH2TransportMetrics metrics;
/*     */   private volatile int maxFramePayloadSize;
/*     */   private volatile ByteBuffer buffer;
/*     */   
/*     */   public FrameOutputBuffer(BasicH2TransportMetrics metrics, int maxFramePayloadSize) {
/*  52 */     Args.notNull(metrics, "HTTP2 transport metrics");
/*  53 */     Args.positive(maxFramePayloadSize, "Maximum payload size");
/*  54 */     this.metrics = metrics;
/*  55 */     this.maxFramePayloadSize = maxFramePayloadSize;
/*  56 */     this.buffer = ByteBuffer.allocate(9 + maxFramePayloadSize);
/*     */   }
/*     */   
/*     */   public FrameOutputBuffer(int maxFramePayloadSize) {
/*  60 */     this(new BasicH2TransportMetrics(), maxFramePayloadSize);
/*     */   }
/*     */   
/*     */   public void expand(int maxFramePayloadSize) {
/*  64 */     this.maxFramePayloadSize = maxFramePayloadSize;
/*  65 */     ByteBuffer newBuffer = ByteBuffer.allocate(9 + maxFramePayloadSize);
/*  66 */     if (this.buffer.position() > 0) {
/*  67 */       this.buffer.flip();
/*  68 */       newBuffer.put(this.buffer);
/*     */     } 
/*  70 */     this.buffer = newBuffer;
/*     */   }
/*     */   
/*     */   public void write(RawFrame frame, WritableByteChannel channel) throws IOException {
/*  74 */     Args.notNull(frame, "Frame");
/*     */     
/*  76 */     ByteBuffer payload = frame.getPayload();
/*  77 */     Args.check((payload == null || payload.remaining() <= this.maxFramePayloadSize), "Frame size exceeds maximum");
/*  78 */     this.buffer.putInt(((payload != null) ? (payload.remaining() << 8) : 0) | frame.getType() & 0xFF);
/*  79 */     this.buffer.put((byte)(frame.getFlags() & 0xFF));
/*  80 */     this.buffer.putInt(frame.getStreamId());
/*     */     
/*  82 */     if (payload != null) {
/*  83 */       if (channel instanceof GatheringByteChannel) {
/*  84 */         this.buffer.flip();
/*  85 */         ((GatheringByteChannel)channel).write(new ByteBuffer[] { this.buffer, payload });
/*  86 */         this.buffer.compact();
/*  87 */         if (payload.hasRemaining()) {
/*  88 */           this.buffer.put(payload);
/*     */         }
/*     */       } else {
/*  91 */         this.buffer.put(payload);
/*     */       } 
/*     */     }
/*     */     
/*  95 */     flush(channel);
/*     */     
/*  97 */     this.metrics.incrementFramesTransferred();
/*     */   }
/*     */   
/*     */   public void flush(WritableByteChannel channel) throws IOException {
/* 101 */     if (this.buffer.position() > 0) {
/* 102 */       this.buffer.flip();
/*     */       try {
/* 104 */         int bytesWritten = channel.write(this.buffer);
/* 105 */         if (bytesWritten > 0) {
/* 106 */           this.metrics.incrementBytesTransferred(bytesWritten);
/*     */         }
/*     */       } finally {
/* 109 */         this.buffer.compact();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 115 */     return (this.buffer.position() == 0);
/*     */   }
/*     */   
/*     */   public H2TransportMetrics getMetrics() {
/* 119 */     return (H2TransportMetrics)this.metrics;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/FrameOutputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */