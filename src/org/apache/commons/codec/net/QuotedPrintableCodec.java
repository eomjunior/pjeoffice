/*     */ package org.apache.commons.codec.net;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.BitSet;
/*     */ import org.apache.commons.codec.BinaryDecoder;
/*     */ import org.apache.commons.codec.BinaryEncoder;
/*     */ import org.apache.commons.codec.DecoderException;
/*     */ import org.apache.commons.codec.EncoderException;
/*     */ import org.apache.commons.codec.StringDecoder;
/*     */ import org.apache.commons.codec.StringEncoder;
/*     */ import org.apache.commons.codec.binary.StringUtils;
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
/*     */ public class QuotedPrintableCodec
/*     */   implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder
/*     */ {
/*     */   private final Charset charset;
/*     */   private final boolean strict;
/*  85 */   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
/*     */ 
/*     */   
/*     */   private static final byte ESCAPE_CHAR = 61;
/*     */ 
/*     */   
/*     */   private static final byte TAB = 9;
/*     */ 
/*     */   
/*     */   private static final byte SPACE = 32;
/*     */   
/*     */   private static final byte CR = 13;
/*     */   
/*     */   private static final byte LF = 10;
/*     */   
/*     */   private static final int SAFE_LENGTH = 73;
/*     */ 
/*     */   
/*     */   static {
/*     */     int i;
/* 105 */     for (i = 33; i <= 60; i++) {
/* 106 */       PRINTABLE_CHARS.set(i);
/*     */     }
/* 108 */     for (i = 62; i <= 126; i++) {
/* 109 */       PRINTABLE_CHARS.set(i);
/*     */     }
/* 111 */     PRINTABLE_CHARS.set(9);
/* 112 */     PRINTABLE_CHARS.set(32);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QuotedPrintableCodec() {
/* 119 */     this(StandardCharsets.UTF_8, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QuotedPrintableCodec(boolean strict) {
/* 130 */     this(StandardCharsets.UTF_8, strict);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QuotedPrintableCodec(Charset charset) {
/* 141 */     this(charset, false);
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
/*     */   public QuotedPrintableCodec(Charset charset, boolean strict) {
/* 154 */     this.charset = charset;
/* 155 */     this.strict = strict;
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
/*     */   
/*     */   public QuotedPrintableCodec(String charsetName) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
/* 175 */     this(Charset.forName(charsetName), false);
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
/*     */   private static final int encodeQuotedPrintable(int b, ByteArrayOutputStream buffer) {
/* 188 */     buffer.write(61);
/* 189 */     char hex1 = Utils.hexDigit(b >> 4);
/* 190 */     char hex2 = Utils.hexDigit(b);
/* 191 */     buffer.write(hex1);
/* 192 */     buffer.write(hex2);
/* 193 */     return 3;
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
/*     */   private static int getUnsignedOctet(int index, byte[] bytes) {
/* 207 */     int b = bytes[index];
/* 208 */     if (b < 0) {
/* 209 */       b = 256 + b;
/*     */     }
/* 211 */     return b;
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
/*     */   private static int encodeByte(int b, boolean encode, ByteArrayOutputStream buffer) {
/* 227 */     if (encode) {
/* 228 */       return encodeQuotedPrintable(b, buffer);
/*     */     }
/* 230 */     buffer.write(b);
/* 231 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isWhitespace(int b) {
/* 242 */     return (b == 32 || b == 9);
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
/*     */   public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes) {
/* 258 */     return encodeQuotedPrintable(printable, bytes, false);
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
/*     */   
/*     */   public static final byte[] encodeQuotedPrintable(BitSet printable, byte[] bytes, boolean strict) {
/* 278 */     if (bytes == null) {
/* 279 */       return null;
/*     */     }
/* 281 */     if (printable == null) {
/* 282 */       printable = PRINTABLE_CHARS;
/*     */     }
/* 284 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/*     */     
/* 286 */     if (strict) {
/* 287 */       int pos = 1;
/*     */ 
/*     */       
/* 290 */       for (int i = 0; i < bytes.length - 3; i++) {
/* 291 */         int k = getUnsignedOctet(i, bytes);
/* 292 */         if (pos < 73) {
/*     */           
/* 294 */           pos += encodeByte(k, !printable.get(k), buffer);
/*     */         } else {
/*     */           
/* 297 */           encodeByte(k, (!printable.get(k) || isWhitespace(k)), buffer);
/*     */ 
/*     */           
/* 300 */           buffer.write(61);
/* 301 */           buffer.write(13);
/* 302 */           buffer.write(10);
/* 303 */           pos = 1;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 309 */       int b = getUnsignedOctet(bytes.length - 3, bytes);
/* 310 */       boolean encode = (!printable.get(b) || (isWhitespace(b) && pos > 68));
/* 311 */       pos += encodeByte(b, encode, buffer);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 316 */       if (pos > 71) {
/* 317 */         buffer.write(61);
/* 318 */         buffer.write(13);
/* 319 */         buffer.write(10);
/*     */       } 
/* 321 */       for (int j = bytes.length - 2; j < bytes.length; j++) {
/* 322 */         b = getUnsignedOctet(j, bytes);
/*     */         
/* 324 */         encode = (!printable.get(b) || (j > bytes.length - 2 && isWhitespace(b)));
/* 325 */         encodeByte(b, encode, buffer);
/*     */       } 
/*     */     } else {
/* 328 */       for (byte c : bytes) {
/* 329 */         int b = c;
/* 330 */         if (b < 0) {
/* 331 */           b = 256 + b;
/*     */         }
/* 333 */         if (printable.get(b)) {
/* 334 */           buffer.write(b);
/*     */         } else {
/* 336 */           encodeQuotedPrintable(b, buffer);
/*     */         } 
/*     */       } 
/*     */     } 
/* 340 */     return buffer.toByteArray();
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
/*     */   public static final byte[] decodeQuotedPrintable(byte[] bytes) throws DecoderException {
/* 357 */     if (bytes == null) {
/* 358 */       return null;
/*     */     }
/* 360 */     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
/* 361 */     for (int i = 0; i < bytes.length; i++) {
/* 362 */       int b = bytes[i];
/* 363 */       if (b == 61) {
/*     */         
/*     */         try {
/* 366 */           if (bytes[++i] != 13)
/*     */           
/*     */           { 
/* 369 */             int u = Utils.digit16(bytes[i]);
/* 370 */             int l = Utils.digit16(bytes[++i]);
/* 371 */             buffer.write((char)((u << 4) + l)); } 
/* 372 */         } catch (ArrayIndexOutOfBoundsException e) {
/* 373 */           throw new DecoderException("Invalid quoted-printable encoding", e);
/*     */         } 
/* 375 */       } else if (b != 13 && b != 10) {
/*     */         
/* 377 */         buffer.write(b);
/*     */       } 
/*     */     } 
/* 380 */     return buffer.toByteArray();
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
/*     */   public byte[] encode(byte[] bytes) {
/* 396 */     return encodeQuotedPrintable(PRINTABLE_CHARS, bytes, this.strict);
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
/*     */   public byte[] decode(byte[] bytes) throws DecoderException {
/* 414 */     return decodeQuotedPrintable(bytes);
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
/*     */   
/*     */   public String encode(String sourceStr) throws EncoderException {
/* 434 */     return encode(sourceStr, getCharset());
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
/*     */   public String decode(String sourceStr, Charset sourceCharset) throws DecoderException {
/* 451 */     if (sourceStr == null) {
/* 452 */       return null;
/*     */     }
/* 454 */     return new String(decode(StringUtils.getBytesUsAscii(sourceStr)), sourceCharset);
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
/*     */   public String decode(String sourceStr, String sourceCharset) throws DecoderException, UnsupportedEncodingException {
/* 473 */     if (sourceStr == null) {
/* 474 */       return null;
/*     */     }
/* 476 */     return new String(decode(StringUtils.getBytesUsAscii(sourceStr)), sourceCharset);
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
/*     */   public String decode(String sourceStr) throws DecoderException {
/* 492 */     return decode(sourceStr, getCharset());
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
/*     */   public Object encode(Object obj) throws EncoderException {
/* 507 */     if (obj == null)
/* 508 */       return null; 
/* 509 */     if (obj instanceof byte[])
/* 510 */       return encode((byte[])obj); 
/* 511 */     if (obj instanceof String) {
/* 512 */       return encode((String)obj);
/*     */     }
/* 514 */     throw new EncoderException("Objects of type " + obj
/* 515 */         .getClass().getName() + " cannot be quoted-printable encoded");
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
/*     */   public Object decode(Object obj) throws DecoderException {
/* 533 */     if (obj == null)
/* 534 */       return null; 
/* 535 */     if (obj instanceof byte[])
/* 536 */       return decode((byte[])obj); 
/* 537 */     if (obj instanceof String) {
/* 538 */       return decode((String)obj);
/*     */     }
/* 540 */     throw new DecoderException("Objects of type " + obj
/* 541 */         .getClass().getName() + " cannot be quoted-printable decoded");
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
/*     */   public Charset getCharset() {
/* 553 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultCharset() {
/* 562 */     return this.charset.name();
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
/*     */   public String encode(String sourceStr, Charset sourceCharset) {
/* 580 */     if (sourceStr == null) {
/* 581 */       return null;
/*     */     }
/* 583 */     return StringUtils.newStringUsAscii(encode(sourceStr.getBytes(sourceCharset)));
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
/*     */   public String encode(String sourceStr, String sourceCharset) throws UnsupportedEncodingException {
/* 602 */     if (sourceStr == null) {
/* 603 */       return null;
/*     */     }
/* 605 */     return StringUtils.newStringUsAscii(encode(sourceStr.getBytes(sourceCharset)));
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/net/QuotedPrintableCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */