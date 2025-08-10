/*     */ package com.github.utils4j.imp;
/*     */ 
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.List;
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
/*     */ public final class Base64
/*     */ {
/*     */   private static byte[] DEC_BASE64;
/*  41 */   private static byte[] ENC_BASE64 = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  49 */     DEC_BASE64 = new byte[128];
/*  50 */     for (int i = 0; i < ENC_BASE64.length; i++) {
/*  51 */       DEC_BASE64[ENC_BASE64[i]] = (byte)i;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String base64Encode(List<Certificate> chain) throws CertificateException {
/*  58 */     return base64Encode(Certificates.toByteArray(chain));
/*     */   }
/*     */   
/*     */   public static String base64Encode(byte[] data) {
/*  62 */     Args.requireNonEmpty(data, "byte array can't be empty");
/*  63 */     byte[] encodedBuf = new byte[(data.length + 2) / 3 * 4];
/*  64 */     int srcIndex = 0;
/*  65 */     int destIndex = 0;
/*  66 */     while (srcIndex < data.length - 2) {
/*  67 */       encodedBuf[destIndex++] = ENC_BASE64[data[srcIndex] >>> 2 & 0x3F];
/*  68 */       encodedBuf[destIndex++] = ENC_BASE64[data[srcIndex + 1] >>> 4 & 0xF | data[srcIndex] << 4 & 0x3F];
/*  69 */       encodedBuf[destIndex++] = ENC_BASE64[data[srcIndex + 2] >>> 6 & 0x3 | data[srcIndex + 1] << 2 & 0x3F];
/*  70 */       encodedBuf[destIndex++] = ENC_BASE64[data[srcIndex + 2] & 0x3F];
/*  71 */       srcIndex += 3;
/*     */     } 
/*  73 */     if (srcIndex < data.length) {
/*  74 */       encodedBuf[destIndex++] = ENC_BASE64[data[srcIndex] >>> 2 & 0x3F];
/*  75 */       if (srcIndex < data.length - 1) {
/*  76 */         encodedBuf[destIndex++] = ENC_BASE64[data[srcIndex + 1] >>> 4 & 0xF | data[srcIndex] << 4 & 0x3F];
/*  77 */         encodedBuf[destIndex++] = ENC_BASE64[data[srcIndex + 1] << 2 & 0x3F];
/*     */       } else {
/*     */         
/*  80 */         encodedBuf[destIndex++] = ENC_BASE64[data[srcIndex] << 4 & 0x3F];
/*     */       } 
/*     */     } 
/*  83 */     while (destIndex < encodedBuf.length) {
/*  84 */       encodedBuf[destIndex] = 61;
/*  85 */       destIndex++;
/*     */     } 
/*  87 */     String result = new String(encodedBuf);
/*  88 */     return result;
/*     */   }
/*     */   
/*     */   public static byte[] base64Decode(String aData) {
/*  92 */     Args.requireText(aData, "unabled to decode empty data");
/*     */     byte[] data;
/*     */     int tail;
/*  95 */     for (data = aData.getBytes(), tail = data.length; data[tail - 1] == 61; tail--);
/*  96 */     byte[] decodedBuf = new byte[tail - data.length / 4];
/*  97 */     for (int i = 0; i < data.length; i++) {
/*  98 */       data[i] = DEC_BASE64[data[i]];
/*     */     }
/* 100 */     int srcIndex = 0;
/*     */     int destIndex;
/* 102 */     for (destIndex = 0; destIndex < decodedBuf.length - 2; destIndex += 3) {
/* 103 */       decodedBuf[destIndex] = (byte)(data[srcIndex] << 2 & 0xFF | data[srcIndex + 1] >>> 4 & 0x3);
/* 104 */       decodedBuf[destIndex + 1] = (byte)(data[srcIndex + 1] << 4 & 0xFF | data[srcIndex + 2] >>> 2 & 0xF);
/* 105 */       decodedBuf[destIndex + 2] = (byte)(data[srcIndex + 2] << 6 & 0xFF | data[srcIndex + 3] & 0x3F);
/* 106 */       srcIndex += 4;
/*     */     } 
/* 108 */     if (destIndex < decodedBuf.length) {
/* 109 */       decodedBuf[destIndex] = (byte)(data[srcIndex] << 2 & 0xFF | data[srcIndex + 1] >>> 4 & 0x3);
/*     */     }
/* 111 */     if (++destIndex < decodedBuf.length) {
/* 112 */       decodedBuf[destIndex] = (byte)(data[srcIndex + 1] << 4 & 0xFF | data[srcIndex + 2] >>> 2 & 0xF);
/*     */     }
/* 114 */     return decodedBuf;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/github/utils4j/imp/Base64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */