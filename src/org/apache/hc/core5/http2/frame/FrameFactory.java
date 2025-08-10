/*     */ package org.apache.hc.core5.http2.frame;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.hc.core5.http2.H2Error;
/*     */ import org.apache.hc.core5.http2.config.H2Setting;
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
/*     */ public abstract class FrameFactory
/*     */ {
/*     */   public RawFrame createSettings(H2Setting... settings) {
/*  46 */     ByteBuffer payload = ByteBuffer.allocate(settings.length * 12);
/*  47 */     for (H2Setting setting : settings) {
/*  48 */       payload.putShort((short)setting.getCode());
/*  49 */       payload.putInt(setting.getValue());
/*     */     } 
/*  51 */     payload.flip();
/*  52 */     return new RawFrame(FrameType.SETTINGS.getValue(), 0, 0, payload);
/*     */   }
/*     */   
/*     */   public RawFrame createSettingsAck() {
/*  56 */     return new RawFrame(FrameType.SETTINGS.getValue(), FrameFlag.ACK.getValue(), 0, null);
/*     */   }
/*     */   
/*     */   public RawFrame createResetStream(int streamId, H2Error error) {
/*  60 */     Args.notNull(error, "Error");
/*  61 */     return createResetStream(streamId, error.getCode());
/*     */   }
/*     */   
/*     */   public RawFrame createResetStream(int streamId, int code) {
/*  65 */     Args.positive(streamId, "Stream id");
/*  66 */     ByteBuffer payload = ByteBuffer.allocate(4);
/*  67 */     payload.putInt(code);
/*  68 */     payload.flip();
/*  69 */     return new RawFrame(FrameType.RST_STREAM.getValue(), 0, streamId, payload);
/*     */   }
/*     */   
/*     */   public RawFrame createPing(ByteBuffer opaqueData) {
/*  73 */     Args.notNull(opaqueData, "Opaque data");
/*  74 */     Args.check((opaqueData.remaining() == 8), "Opaque data length must be equal 8");
/*  75 */     return new RawFrame(FrameType.PING.getValue(), 0, 0, opaqueData);
/*     */   }
/*     */   
/*     */   public RawFrame createPingAck(ByteBuffer opaqueData) {
/*  79 */     Args.notNull(opaqueData, "Opaque data");
/*  80 */     Args.check((opaqueData.remaining() == 8), "Opaque data length must be equal 8");
/*  81 */     return new RawFrame(FrameType.PING.getValue(), FrameFlag.ACK.value, 0, opaqueData);
/*     */   }
/*     */   
/*     */   public RawFrame createGoAway(int lastStream, H2Error error, String message) {
/*  85 */     Args.notNegative(lastStream, "Last stream id");
/*  86 */     byte[] debugData = (message != null) ? message.getBytes(StandardCharsets.US_ASCII) : null;
/*  87 */     ByteBuffer payload = ByteBuffer.allocate(8 + ((debugData != null) ? debugData.length : 0));
/*  88 */     payload.putInt(lastStream);
/*  89 */     payload.putInt(error.getCode());
/*  90 */     if (debugData != null) {
/*  91 */       payload.put(debugData);
/*     */     }
/*  93 */     payload.flip();
/*  94 */     return new RawFrame(FrameType.GOAWAY.getValue(), 0, 0, payload);
/*     */   }
/*     */   
/*     */   public abstract RawFrame createHeaders(int paramInt, ByteBuffer paramByteBuffer, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */   public abstract RawFrame createContinuation(int paramInt, ByteBuffer paramByteBuffer, boolean paramBoolean);
/*     */   
/*     */   public abstract RawFrame createPushPromise(int paramInt, ByteBuffer paramByteBuffer, boolean paramBoolean);
/*     */   
/*     */   public abstract RawFrame createData(int paramInt, ByteBuffer paramByteBuffer, boolean paramBoolean);
/*     */   
/*     */   public RawFrame createWindowUpdate(int streamId, int increment) {
/* 106 */     Args.notNegative(streamId, "Stream id");
/* 107 */     Args.positive(increment, "Increment");
/* 108 */     ByteBuffer payload = ByteBuffer.allocate(4);
/* 109 */     payload.putInt(increment);
/* 110 */     payload.flip();
/* 111 */     return new RawFrame(FrameType.WINDOW_UPDATE.getValue(), 0, streamId, payload);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/frame/FrameFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */