/*    */ package org.apache.hc.core5.http2.frame;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ public final class RawFrame
/*    */   extends Frame<ByteBuffer>
/*    */ {
/*    */   private final ByteBuffer payload;
/*    */   private final int len;
/*    */   
/*    */   public RawFrame(int type, int flags, int streamId, ByteBuffer payload) {
/* 42 */     super(type, flags, streamId);
/* 43 */     this.payload = payload;
/* 44 */     this.len = (payload != null) ? payload.remaining() : 0;
/*    */   }
/*    */   
/*    */   public boolean isPadded() {
/* 48 */     return isFlagSet(FrameFlag.PADDED);
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 52 */     return this.len;
/*    */   }
/*    */   
/*    */   public ByteBuffer getPayloadContent() {
/* 56 */     if (this.payload != null) {
/* 57 */       if (isPadded()) {
/* 58 */         ByteBuffer dup = this.payload.duplicate();
/* 59 */         if (dup.remaining() == 0) {
/* 60 */           return null;
/*    */         }
/* 62 */         int padding = dup.get() & 0xFF;
/* 63 */         if (padding > dup.remaining()) {
/* 64 */           return null;
/*    */         }
/* 66 */         dup.limit(dup.limit() - padding);
/* 67 */         return dup;
/*    */       } 
/* 69 */       return this.payload.duplicate();
/*    */     } 
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public ByteBuffer getPayload() {
/* 76 */     return (this.payload != null) ? this.payload.duplicate() : null;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/frame/RawFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */