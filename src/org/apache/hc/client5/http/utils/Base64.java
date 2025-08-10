/*     */ package org.apache.hc.client5.http.utils;
/*     */ 
/*     */ import org.apache.hc.core5.annotation.Internal;
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
/*     */ @Internal
/*     */ public class Base64
/*     */ {
/*  54 */   private static final Base64 CODEC = new Base64();
/*  55 */   private static final byte[] EMPTY_BYTES = new byte[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base64() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Base64(int lineLength) {
/*  72 */     if (lineLength != 0) {
/*  73 */       throw new UnsupportedOperationException("Line breaks not supported");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeBase64(byte[] base64) {
/*  83 */     return CODEC.decode(base64);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeBase64(String base64) {
/*  93 */     return CODEC.decode(base64);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] encodeBase64(byte[] base64) {
/* 101 */     return CODEC.encode(base64);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeBase64String(byte[] bytes) {
/* 109 */     if (null == bytes) {
/* 110 */       return null;
/*     */     }
/*     */     
/* 113 */     return java.util.Base64.getEncoder().encodeToString(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] base64) {
/* 121 */     if (null == base64) {
/* 122 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 126 */       return java.util.Base64.getMimeDecoder().decode(base64);
/* 127 */     } catch (IllegalArgumentException e) {
/* 128 */       return EMPTY_BYTES;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(String base64) {
/* 137 */     if (null == base64) {
/* 138 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 149 */       return java.util.Base64.getMimeDecoder().decode(base64);
/* 150 */     } catch (IllegalArgumentException e) {
/* 151 */       return EMPTY_BYTES;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] value) {
/* 159 */     if (null == value) {
/* 160 */       return null;
/*     */     }
/* 162 */     return java.util.Base64.getEncoder().encode(value);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/hc/client5/http/utils/Base64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */