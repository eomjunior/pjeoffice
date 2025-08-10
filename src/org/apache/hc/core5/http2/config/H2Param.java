/*    */ package org.apache.hc.core5.http2.config;
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
/*    */ public enum H2Param
/*    */ {
/* 36 */   HEADER_TABLE_SIZE(1),
/* 37 */   ENABLE_PUSH(2),
/* 38 */   MAX_CONCURRENT_STREAMS(3),
/* 39 */   INITIAL_WINDOW_SIZE(4),
/* 40 */   MAX_FRAME_SIZE(5),
/* 41 */   MAX_HEADER_LIST_SIZE(6);
/*    */   int code;
/*    */   private static final H2Param[] LOOKUP_TABLE;
/*    */   
/*    */   H2Param(int code) {
/* 46 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 50 */     return this.code;
/*    */   }
/*    */   static {
/* 53 */     LOOKUP_TABLE = new H2Param[6];
/*    */     
/* 55 */     for (H2Param param : values()) {
/* 56 */       LOOKUP_TABLE[param.code - 1] = param;
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
/*    */   public static String toString(int code) {
/* 68 */     if (code < 1 || code > LOOKUP_TABLE.length) {
/* 69 */       return Integer.toString(code);
/*    */     }
/* 71 */     return LOOKUP_TABLE[code - 1].name();
/*    */   }
/*    */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/config/H2Param.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */