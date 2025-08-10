/*     */ package org.apache.hc.core5.http2;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
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
/*     */ 
/*     */ public enum H2Error
/*     */ {
/*  44 */   NO_ERROR(0),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   PROTOCOL_ERROR(1),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   INTERNAL_ERROR(2),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   FLOW_CONTROL_ERROR(3),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  73 */   SETTINGS_TIMEOUT(4),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   STREAM_CLOSED(5),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   FRAME_SIZE_ERROR(6),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  94 */   REFUSED_STREAM(7),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   CANCEL(8),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 108 */   COMPRESSION_ERROR(9),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   CONNECT_ERROR(10),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   ENHANCE_YOUR_CALM(11),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   INADEQUATE_SECURITY(12),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   HTTP_1_1_REQUIRED(13);
/*     */   int code;
/*     */   private static final ConcurrentMap<Integer, H2Error> MAP_BY_CODE;
/*     */   
/*     */   H2Error(int code) {
/* 142 */     this.code = code;
/*     */   }
/*     */   
/*     */   public int getCode() {
/* 146 */     return this.code;
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 151 */     MAP_BY_CODE = new ConcurrentHashMap<>();
/* 152 */     for (H2Error error : values()) {
/* 153 */       MAP_BY_CODE.putIfAbsent(Integer.valueOf(error.code), error);
/*     */     }
/*     */   }
/*     */   
/*     */   public static H2Error getByCode(int code) {
/* 158 */     return MAP_BY_CODE.get(Integer.valueOf(code));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/core5/http2/H2Error.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */