/*     */ package com.itextpdf.xmp.impl;
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
/*     */ public class Base64
/*     */ {
/*     */   private static final byte INVALID = -1;
/*     */   private static final byte WHITESPACE = -2;
/*     */   private static final byte EQUAL = -3;
/*  52 */   private static byte[] base64 = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
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
/*  71 */   private static byte[] ascii = new byte[255];
/*     */   
/*     */   static {
/*     */     int idx;
/*  75 */     for (idx = 0; idx < 255; idx++)
/*     */     {
/*  77 */       ascii[idx] = -1;
/*     */     }
/*     */     
/*  80 */     for (idx = 0; idx < base64.length; idx++)
/*     */     {
/*  82 */       ascii[base64[idx]] = (byte)idx;
/*     */     }
/*     */     
/*  85 */     ascii[9] = -2;
/*  86 */     ascii[10] = -2;
/*  87 */     ascii[13] = -2;
/*  88 */     ascii[32] = -2;
/*     */ 
/*     */     
/*  91 */     ascii[61] = -3;
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
/*     */   public static final byte[] encode(byte[] src) {
/* 103 */     return encode(src, 0);
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
/*     */ 
/*     */   
/*     */   public static final byte[] encode(byte[] src, int lineFeed) {
/* 118 */     lineFeed = lineFeed / 4 * 4;
/* 119 */     if (lineFeed < 0)
/*     */     {
/* 121 */       lineFeed = 0;
/*     */     }
/*     */ 
/*     */     
/* 125 */     int codeLength = (src.length + 2) / 3 * 4;
/* 126 */     if (lineFeed > 0)
/*     */     {
/* 128 */       codeLength += (codeLength - 1) / lineFeed;
/*     */     }
/*     */     
/* 131 */     byte[] dst = new byte[codeLength];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 137 */     int didx = 0;
/* 138 */     int sidx = 0;
/* 139 */     int lf = 0;
/* 140 */     while (sidx + 3 <= src.length) {
/*     */       
/* 142 */       int bits24 = (src[sidx++] & 0xFF) << 16;
/* 143 */       bits24 |= (src[sidx++] & 0xFF) << 8;
/* 144 */       bits24 |= (src[sidx++] & 0xFF) << 0;
/* 145 */       int bits6 = (bits24 & 0xFC0000) >> 18;
/* 146 */       dst[didx++] = base64[bits6];
/* 147 */       bits6 = (bits24 & 0x3F000) >> 12;
/* 148 */       dst[didx++] = base64[bits6];
/* 149 */       bits6 = (bits24 & 0xFC0) >> 6;
/* 150 */       dst[didx++] = base64[bits6];
/* 151 */       bits6 = bits24 & 0x3F;
/* 152 */       dst[didx++] = base64[bits6];
/*     */       
/* 154 */       lf += 4;
/* 155 */       if (didx < codeLength && lineFeed > 0 && lf % lineFeed == 0)
/*     */       {
/* 157 */         dst[didx++] = 10;
/*     */       }
/*     */     } 
/* 160 */     if (src.length - sidx == 2) {
/*     */       
/* 162 */       int bits24 = (src[sidx] & 0xFF) << 16;
/* 163 */       bits24 |= (src[sidx + 1] & 0xFF) << 8;
/* 164 */       int bits6 = (bits24 & 0xFC0000) >> 18;
/* 165 */       dst[didx++] = base64[bits6];
/* 166 */       bits6 = (bits24 & 0x3F000) >> 12;
/* 167 */       dst[didx++] = base64[bits6];
/* 168 */       bits6 = (bits24 & 0xFC0) >> 6;
/* 169 */       dst[didx++] = base64[bits6];
/* 170 */       dst[didx++] = 61;
/*     */     }
/* 172 */     else if (src.length - sidx == 1) {
/*     */       
/* 174 */       int bits24 = (src[sidx] & 0xFF) << 16;
/* 175 */       int bits6 = (bits24 & 0xFC0000) >> 18;
/* 176 */       dst[didx++] = base64[bits6];
/* 177 */       bits6 = (bits24 & 0x3F000) >> 12;
/* 178 */       dst[didx++] = base64[bits6];
/* 179 */       dst[didx++] = 61;
/* 180 */       dst[didx++] = 61;
/*     */     } 
/* 182 */     return dst;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String encode(String src) {
/* 193 */     return new String(encode(src.getBytes()));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final byte[] decode(byte[] src) throws IllegalArgumentException {
/* 212 */     int srcLen = 0; int sidx;
/* 213 */     for (sidx = 0; sidx < src.length; sidx++) {
/*     */       
/* 215 */       byte val = ascii[src[sidx]];
/* 216 */       if (val >= 0) {
/*     */         
/* 218 */         src[srcLen++] = val;
/*     */       }
/* 220 */       else if (val == -1) {
/*     */         
/* 222 */         throw new IllegalArgumentException("Invalid base 64 string");
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     while (srcLen > 0 && src[srcLen - 1] == -3)
/*     */     {
/* 231 */       srcLen--;
/*     */     }
/* 233 */     byte[] dst = new byte[srcLen * 3 / 4];
/*     */ 
/*     */     
/*     */     int didx;
/*     */ 
/*     */     
/* 239 */     for (sidx = 0, didx = 0; didx < dst.length - 2; sidx += 4, didx += 3) {
/*     */       
/* 241 */       dst[didx] = (byte)(src[sidx] << 2 & 0xFF | src[sidx + 1] >>> 4 & 0x3);
/*     */       
/* 243 */       dst[didx + 1] = (byte)(src[sidx + 1] << 4 & 0xFF | src[sidx + 2] >>> 2 & 0xF);
/*     */       
/* 245 */       dst[didx + 2] = (byte)(src[sidx + 2] << 6 & 0xFF | src[sidx + 3] & 0x3F);
/*     */     } 
/*     */     
/* 248 */     if (didx < dst.length)
/*     */     {
/* 250 */       dst[didx] = (byte)(src[sidx] << 2 & 0xFF | src[sidx + 1] >>> 4 & 0x3);
/*     */     }
/*     */     
/* 253 */     if (++didx < dst.length)
/*     */     {
/* 255 */       dst[didx] = (byte)(src[sidx + 1] << 4 & 0xFF | src[sidx + 2] >>> 2 & 0xF);
/*     */     }
/*     */     
/* 258 */     return dst;
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
/*     */   public static final String decode(String src) {
/* 270 */     return new String(decode(src.getBytes()));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/itextpdf/xmp/impl/Base64.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */