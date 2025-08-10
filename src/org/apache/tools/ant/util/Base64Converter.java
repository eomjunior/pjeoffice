/*     */ package org.apache.tools.ant.util;
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
/*     */ public class Base64Converter
/*     */ {
/*     */   private static final int BYTE = 8;
/*     */   private static final int WORD = 16;
/*     */   private static final int BYTE_MASK = 255;
/*     */   private static final int POS_0_MASK = 63;
/*     */   private static final int POS_1_MASK = 4032;
/*     */   private static final int POS_1_SHIFT = 6;
/*     */   private static final int POS_2_MASK = 258048;
/*     */   private static final int POS_2_SHIFT = 12;
/*     */   private static final int POS_3_MASK = 16515072;
/*     */   private static final int POS_3_SHIFT = 18;
/*  40 */   private static final char[] ALPHABET = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
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
/*  52 */   public static final char[] alphabet = ALPHABET;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String encode(String s) {
/*  62 */     return encode(s.getBytes());
/*     */   }
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
/*     */   public String encode(byte[] octetString) {
/*  75 */     char[] out = new char[((octetString.length - 1) / 3 + 1) * 4];
/*     */     
/*  77 */     int outIndex = 0;
/*  78 */     int i = 0;
/*     */ 
/*     */     
/*  81 */     while (i + 3 <= octetString.length) {
/*     */ 
/*     */       
/*  84 */       int bits24 = (octetString[i++] & 0xFF) << 16;
/*  85 */       bits24 |= (octetString[i++] & 0xFF) << 8;
/*  86 */       bits24 |= octetString[i++] & 0xFF;
/*     */       
/*  88 */       int bits6 = (bits24 & 0xFC0000) >> 18;
/*  89 */       out[outIndex++] = ALPHABET[bits6];
/*  90 */       bits6 = (bits24 & 0x3F000) >> 12;
/*  91 */       out[outIndex++] = ALPHABET[bits6];
/*  92 */       bits6 = (bits24 & 0xFC0) >> 6;
/*  93 */       out[outIndex++] = ALPHABET[bits6];
/*  94 */       bits6 = bits24 & 0x3F;
/*  95 */       out[outIndex++] = ALPHABET[bits6];
/*     */     } 
/*  97 */     if (octetString.length - i == 2) {
/*     */       
/*  99 */       int bits24 = (octetString[i] & 0xFF) << 16;
/* 100 */       bits24 |= (octetString[i + 1] & 0xFF) << 8;
/* 101 */       int bits6 = (bits24 & 0xFC0000) >> 18;
/* 102 */       out[outIndex++] = ALPHABET[bits6];
/* 103 */       bits6 = (bits24 & 0x3F000) >> 12;
/* 104 */       out[outIndex++] = ALPHABET[bits6];
/* 105 */       bits6 = (bits24 & 0xFC0) >> 6;
/* 106 */       out[outIndex++] = ALPHABET[bits6];
/*     */ 
/*     */       
/* 109 */       out[outIndex++] = '=';
/* 110 */     } else if (octetString.length - i == 1) {
/*     */       
/* 112 */       int bits24 = (octetString[i] & 0xFF) << 16;
/* 113 */       int bits6 = (bits24 & 0xFC0000) >> 18;
/* 114 */       out[outIndex++] = ALPHABET[bits6];
/* 115 */       bits6 = (bits24 & 0x3F000) >> 12;
/* 116 */       out[outIndex++] = ALPHABET[bits6];
/*     */ 
/*     */       
/* 119 */       out[outIndex++] = '=';
/* 120 */       out[outIndex++] = '=';
/*     */     } 
/* 122 */     return new String(out);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/ant/util/Base64Converter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */