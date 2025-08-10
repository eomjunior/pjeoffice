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
/*    */ public enum FrameType
/*    */ {
/* 36 */   DATA(0),
/* 37 */   HEADERS(1),
/* 38 */   PRIORITY(2),
/* 39 */   RST_STREAM(3),
/* 40 */   SETTINGS(4),
/* 41 */   PUSH_PROMISE(5),
/* 42 */   PING(6),
/* 43 */   GOAWAY(7),
/* 44 */   WINDOW_UPDATE(8),
/* 45 */   CONTINUATION(9);
/*    */   int value;
/*    */   private static final FrameType[] LOOKUP_TABLE;
/*    */   
/*    */   FrameType(int value) {
/* 50 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 54 */     return this.value;
/*    */   }
/*    */   static {
/* 57 */     LOOKUP_TABLE = new FrameType[10];
/*    */     
/* 59 */     for (FrameType frameType : values()) {
/* 60 */       LOOKUP_TABLE[frameType.value] = frameType;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String toString(int value) {
/* 72 */     if (value < 0 || value >= LOOKUP_TABLE.length) {
/* 73 */       return Integer.toString(value);
/*    */     }
/* 75 */     return LOOKUP_TABLE[value].name();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/frame/FrameType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */