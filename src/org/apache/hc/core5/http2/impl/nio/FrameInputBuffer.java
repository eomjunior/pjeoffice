/*     */ package org.apache.hc.core5.http2.impl.nio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.ReadableByteChannel;
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
/*     */ public final class FrameInputBuffer
/*     */ {
/*     */   private final BasicH2TransportMetrics metrics;
/*     */   private final int maxFramePayloadSize;
/*     */   private final byte[] bytes;
/*     */   private final ByteBuffer buffer;
/*     */   private State state;
/*     */   private int payloadLen;
/*     */   private int type;
/*     */   private int flags;
/*     */   private int streamId;
/*     */   
/*     */   enum State
/*     */   {
/*  51 */     HEAD_EXPECTED, PAYLOAD_EXPECTED;
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
/*     */   FrameInputBuffer(BasicH2TransportMetrics metrics, int bufferLen, int maxFramePayloadSize) {
/*  65 */     Args.notNull(metrics, "HTTP2 transport metrics");
/*  66 */     Args.positive(maxFramePayloadSize, "Maximum payload size");
/*  67 */     this.metrics = metrics;
/*  68 */     this.maxFramePayloadSize = Math.max(maxFramePayloadSize, 16384);
/*  69 */     this.bytes = new byte[bufferLen];
/*  70 */     this.buffer = ByteBuffer.wrap(this.bytes);
/*  71 */     this.buffer.flip();
/*  72 */     this.state = State.HEAD_EXPECTED;
/*     */   }
/*     */   
/*     */   public FrameInputBuffer(BasicH2TransportMetrics metrics, int maxFramePayloadSize) {
/*  76 */     this(metrics, 9 + maxFramePayloadSize, maxFramePayloadSize);
/*     */   }
/*     */   
/*     */   public FrameInputBuffer(int maxFramePayloadSize) {
/*  80 */     this(new BasicH2TransportMetrics(), maxFramePayloadSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void put(ByteBuffer src) {
/*  88 */     if (this.buffer.hasRemaining()) {
/*  89 */       this.buffer.compact();
/*     */     } else {
/*  91 */       this.buffer.clear();
/*     */     } 
/*  93 */     this.buffer.put(src);
/*  94 */     this.buffer.flip();
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
/*     */ 
/*     */   
/*     */   public RawFrame read(ByteBuffer src, ReadableByteChannel channel) throws IOException {
/*     */     while (true) {
/* 111 */       if (src != null) {
/* 112 */         if (this.buffer.hasRemaining()) {
/* 113 */           this.buffer.compact();
/*     */         } else {
/* 115 */           this.buffer.clear();
/*     */         } 
/* 117 */         int remaining = this.buffer.remaining();
/* 118 */         int n = src.remaining();
/* 119 */         if (remaining >= n) {
/* 120 */           this.buffer.put(src);
/* 121 */           this.metrics.incrementBytesTransferred(n);
/*     */         } else {
/* 123 */           int limit = src.limit();
/* 124 */           src.limit(remaining);
/* 125 */           this.buffer.put(src);
/* 126 */           src.limit(limit);
/* 127 */           this.metrics.incrementBytesTransferred(remaining);
/*     */         } 
/* 129 */         this.buffer.flip();
/*     */       } 
/* 131 */       switch (this.state) {
/*     */         case HEAD_EXPECTED:
/* 133 */           if (this.buffer.remaining() >= 9) {
/* 134 */             int lengthAndType = this.buffer.getInt();
/* 135 */             this.payloadLen = lengthAndType >> 8;
/* 136 */             if (this.payloadLen > this.maxFramePayloadSize) {
/* 137 */               throw new H2ConnectionException(H2Error.FRAME_SIZE_ERROR, "Frame size exceeds maximum");
/*     */             }
/* 139 */             this.type = lengthAndType & 0xFF;
/* 140 */             this.flags = this.buffer.get();
/* 141 */             this.streamId = Math.abs(this.buffer.getInt());
/* 142 */             this.state = State.PAYLOAD_EXPECTED;
/*     */           } else {
/*     */             break;
/*     */           } 
/*     */         case PAYLOAD_EXPECTED:
/* 147 */           if (this.buffer.remaining() >= this.payloadLen) {
/* 148 */             if ((this.flags & FrameFlag.PADDED.getValue()) > 0) {
/* 149 */               if (this.payloadLen == 0) {
/* 150 */                 throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Inconsistent padding");
/*     */               }
/* 152 */               this.buffer.mark();
/* 153 */               int padding = this.buffer.get();
/* 154 */               if (this.payloadLen < padding + 1) {
/* 155 */                 throw new H2ConnectionException(H2Error.PROTOCOL_ERROR, "Inconsistent padding");
/*     */               }
/* 157 */               this.buffer.reset();
/*     */             } 
/* 159 */             ByteBuffer payload = (this.payloadLen > 0) ? ByteBuffer.wrap(this.bytes, this.buffer.position(), this.payloadLen) : null;
/* 160 */             this.buffer.position(this.buffer.position() + this.payloadLen);
/* 161 */             this.state = State.HEAD_EXPECTED;
/* 162 */             this.metrics.incrementFramesTransferred();
/* 163 */             return new RawFrame(this.type, this.flags, this.streamId, payload);
/*     */           }  break;
/*     */       } 
/* 166 */       if (this.buffer.hasRemaining()) {
/* 167 */         this.buffer.compact();
/*     */       } else {
/* 169 */         this.buffer.clear();
/*     */       } 
/* 171 */       int bytesRead = channel.read(this.buffer);
/* 172 */       this.buffer.flip();
/* 173 */       if (bytesRead > 0) {
/* 174 */         this.metrics.incrementBytesTransferred(bytesRead);
/*     */       }
/* 176 */       if (bytesRead == 0)
/*     */         break; 
/* 178 */       if (bytesRead < 0) {
/* 179 */         if (this.state != State.HEAD_EXPECTED || this.buffer.hasRemaining()) {
/* 180 */           throw new H2CorruptFrameException("Corrupt or incomplete HTTP2 frame");
/*     */         }
/* 182 */         throw new ConnectionClosedException();
/*     */       } 
/*     */     } 
/*     */     
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RawFrame read(ReadableByteChannel channel) throws IOException {
/* 195 */     return read(null, channel);
/*     */   }
/*     */   
/*     */   public void reset() {
/* 199 */     this.buffer.compact();
/* 200 */     this.state = State.HEAD_EXPECTED;
/*     */   }
/*     */   
/*     */   public H2TransportMetrics getMetrics() {
/* 204 */     return (H2TransportMetrics)this.metrics;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/impl/nio/FrameInputBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */