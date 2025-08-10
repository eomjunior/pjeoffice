/*    */ package org.apache.hc.core5.http2.frame;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.apache.hc.core5.util.Args;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultFrameFactory
/*    */   extends FrameFactory
/*    */ {
/* 41 */   public static final FrameFactory INSTANCE = new DefaultFrameFactory();
/*    */ 
/*    */   
/*    */   public RawFrame createHeaders(int streamId, ByteBuffer payload, boolean endHeaders, boolean endStream) {
/* 45 */     Args.positive(streamId, "Stream id");
/* 46 */     int flags = (endHeaders ? FrameFlag.END_HEADERS.value : 0) | (endStream ? FrameFlag.END_STREAM.value : 0);
/* 47 */     return new RawFrame(FrameType.HEADERS.getValue(), flags, streamId, payload);
/*    */   }
/*    */ 
/*    */   
/*    */   public RawFrame createContinuation(int streamId, ByteBuffer payload, boolean endHeaders) {
/* 52 */     Args.positive(streamId, "Stream id");
/* 53 */     int flags = endHeaders ? FrameFlag.END_HEADERS.value : 0;
/* 54 */     return new RawFrame(FrameType.CONTINUATION.getValue(), flags, streamId, payload);
/*    */   }
/*    */ 
/*    */   
/*    */   public RawFrame createPushPromise(int streamId, ByteBuffer payload, boolean endHeaders) {
/* 59 */     Args.positive(streamId, "Stream id");
/* 60 */     int flags = endHeaders ? FrameFlag.END_HEADERS.value : 0;
/* 61 */     return new RawFrame(FrameType.PUSH_PROMISE.getValue(), flags, streamId, payload);
/*    */   }
/*    */ 
/*    */   
/*    */   public RawFrame createData(int streamId, ByteBuffer payload, boolean endStream) {
/* 66 */     Args.positive(streamId, "Stream id");
/* 67 */     int flags = endStream ? FrameFlag.END_STREAM.value : 0;
/* 68 */     return new RawFrame(FrameType.DATA.getValue(), flags, streamId, payload);
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/frame/DefaultFrameFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */