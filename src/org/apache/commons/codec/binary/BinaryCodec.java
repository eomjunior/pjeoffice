/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
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
/*     */ public class BinaryCodec
/*     */   implements BinaryDecoder, BinaryEncoder
/*     */ {
/*  41 */   private static final char[] EMPTY_CHAR_ARRAY = new char[0];
/*     */ 
/*     */   
/*  44 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */ 
/*     */   
/*     */   private static final int BIT_0 = 1;
/*     */ 
/*     */   
/*     */   private static final int BIT_1 = 2;
/*     */ 
/*     */   
/*     */   private static final int BIT_2 = 4;
/*     */ 
/*     */   
/*     */   private static final int BIT_3 = 8;
/*     */ 
/*     */   
/*     */   private static final int BIT_4 = 16;
/*     */ 
/*     */   
/*     */   private static final int BIT_5 = 32;
/*     */ 
/*     */   
/*     */   private static final int BIT_6 = 64;
/*     */ 
/*     */   
/*     */   private static final int BIT_7 = 128;
/*     */   
/*  70 */   private static final int[] BITS = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] fromAscii(byte[] ascii) {
/*  80 */     if (isEmpty(ascii)) {
/*  81 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/*  84 */     byte[] l_raw = new byte[ascii.length >> 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
/*  90 */       for (int bits = 0; bits < BITS.length; bits++) {
/*  91 */         if (ascii[jj - bits] == 49) {
/*  92 */           l_raw[ii] = (byte)(l_raw[ii] | BITS[bits]);
/*     */         }
/*     */       } 
/*     */     } 
/*  96 */     return l_raw;
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
/*     */   public static byte[] fromAscii(char[] ascii) {
/* 112 */     if (ascii == null || ascii.length == 0) {
/* 113 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 116 */     byte[] l_raw = new byte[ascii.length >> 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     for (int ii = 0, jj = ascii.length - 1; ii < l_raw.length; ii++, jj -= 8) {
/* 122 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 123 */         if (ascii[jj - bits] == '1') {
/* 124 */           l_raw[ii] = (byte)(l_raw[ii] | BITS[bits]);
/*     */         }
/*     */       } 
/*     */     } 
/* 128 */     return l_raw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isEmpty(byte[] array) {
/* 139 */     return (array == null || array.length == 0);
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
/*     */   public static byte[] toAsciiBytes(byte[] raw) {
/* 152 */     if (isEmpty(raw)) {
/* 153 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/*     */     
/* 156 */     byte[] l_ascii = new byte[raw.length << 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ii++, jj -= 8) {
/* 162 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 163 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 164 */           l_ascii[jj - bits] = 48;
/*     */         } else {
/* 166 */           l_ascii[jj - bits] = 49;
/*     */         } 
/*     */       } 
/*     */     } 
/* 170 */     return l_ascii;
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
/*     */   public static char[] toAsciiChars(byte[] raw) {
/* 182 */     if (isEmpty(raw)) {
/* 183 */       return EMPTY_CHAR_ARRAY;
/*     */     }
/*     */     
/* 186 */     char[] l_ascii = new char[raw.length << 3];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     for (int ii = 0, jj = l_ascii.length - 1; ii < raw.length; ii++, jj -= 8) {
/* 192 */       for (int bits = 0; bits < BITS.length; bits++) {
/* 193 */         if ((raw[ii] & BITS[bits]) == 0) {
/* 194 */           l_ascii[jj - bits] = '0';
/*     */         } else {
/* 196 */           l_ascii[jj - bits] = '1';
/*     */         } 
/*     */       } 
/*     */     } 
/* 200 */     return l_ascii;
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
/*     */   public static String toAsciiString(byte[] raw) {
/* 212 */     return new String(toAsciiChars(raw));
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
/*     */   public byte[] decode(byte[] ascii) {
/* 225 */     return fromAscii(ascii);
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
/*     */   public Object decode(Object ascii) throws DecoderException {
/* 240 */     if (ascii == null) {
/* 241 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 243 */     if (ascii instanceof byte[]) {
/* 244 */       return fromAscii((byte[])ascii);
/*     */     }
/* 246 */     if (ascii instanceof char[]) {
/* 247 */       return fromAscii((char[])ascii);
/*     */     }
/* 249 */     if (ascii instanceof String) {
/* 250 */       return fromAscii(((String)ascii).toCharArray());
/*     */     }
/* 252 */     throw new DecoderException("argument not a byte array");
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
/*     */   public byte[] encode(byte[] raw) {
/* 265 */     return toAsciiBytes(raw);
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
/*     */   public Object encode(Object raw) throws EncoderException {
/* 280 */     if (!(raw instanceof byte[])) {
/* 281 */       throw new EncoderException("argument not a byte array");
/*     */     }
/* 283 */     return toAsciiChars((byte[])raw);
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
/*     */   public byte[] toByteArray(String ascii) {
/* 295 */     if (ascii == null) {
/* 296 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 298 */     return fromAscii(ascii.toCharArray());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/BinaryCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */