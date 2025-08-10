/*    */ package org.apache.hc.core5.http2.frame;
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
/*    */ 
/*    */ public abstract class Frame<T>
/*    */ {
/*    */   private final int type;
/*    */   private final int flags;
/*    */   private final int streamId;
/*    */   
/*    */   public Frame(int type, int flags, int streamId) {
/* 43 */     this.type = type;
/* 44 */     this.flags = flags;
/* 45 */     this.streamId = streamId;
/*    */   }
/*    */   
/*    */   public boolean isType(FrameType type) {
/* 49 */     return (getType() == type.value);
/*    */   }
/*    */   
/*    */   public boolean isFlagSet(FrameFlag flag) {
/* 53 */     return ((getFlags() & flag.value) != 0);
/*    */   }
/*    */   
/*    */   public int getType() {
/* 57 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getFlags() {
/* 61 */     return this.flags;
/*    */   }
/*    */   
/*    */   public int getStreamId() {
/* 65 */     return this.streamId;
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract T getPayload();
/*    */   
/*    */   public String toString() {
/* 72 */     StringBuilder sb = new StringBuilder("[");
/* 73 */     sb.append("type=").append(this.type);
/* 74 */     sb.append(", flags=").append(this.flags);
/* 75 */     sb.append(", streamId=").append(this.streamId);
/* 76 */     sb.append(", payoad=").append(getPayload());
/* 77 */     sb.append(']');
/* 78 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/frame/Frame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */