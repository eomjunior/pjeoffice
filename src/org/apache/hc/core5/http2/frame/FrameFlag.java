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
/*    */ public enum FrameFlag
/*    */ {
/* 36 */   END_STREAM(1),
/* 37 */   ACK(1),
/* 38 */   END_HEADERS(4),
/* 39 */   PADDED(8),
/* 40 */   PRIORITY(32);
/*    */   
/*    */   final int value;
/*    */   
/*    */   FrameFlag(int value) {
/* 45 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 49 */     return this.value;
/*    */   }
/*    */   
/*    */   public static int of(FrameFlag... flags) {
/* 53 */     int value = 0;
/* 54 */     for (FrameFlag flag : flags) {
/* 55 */       value |= flag.value;
/*    */     }
/* 57 */     return value;
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/frame/FrameFlag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */