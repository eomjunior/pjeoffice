/*     */ package org.apache.commons.codec.binary;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ 
/*     */ public class Hex
/*     */   implements BinaryEncoder, BinaryDecoder
/*     */ {
/*  45 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_CHARSET_NAME = "UTF-8";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   private static final char[] DIGITS_LOWER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  63 */   private static final char[] DIGITS_UPPER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Charset charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeHex(char[] data) throws DecoderException {
/*  76 */     byte[] out = new byte[data.length >> 1];
/*  77 */     decodeHex(data, out, 0);
/*  78 */     return out;
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
/*     */   public static int decodeHex(char[] data, byte[] out, int outOffset) throws DecoderException {
/*  94 */     int len = data.length;
/*     */     
/*  96 */     if ((len & 0x1) != 0) {
/*  97 */       throw new DecoderException("Odd number of characters.");
/*     */     }
/*     */     
/* 100 */     int outLen = len >> 1;
/* 101 */     if (out.length - outOffset < outLen) {
/* 102 */       throw new DecoderException("Output array is not large enough to accommodate decoded data.");
/*     */     }
/*     */ 
/*     */     
/* 106 */     for (int i = outOffset, j = 0; j < len; i++) {
/* 107 */       int f = toDigit(data[j], j) << 4;
/* 108 */       j++;
/* 109 */       f |= toDigit(data[j], j);
/* 110 */       j++;
/* 111 */       out[i] = (byte)(f & 0xFF);
/*     */     } 
/*     */     
/* 114 */     return outLen;
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
/*     */   public static byte[] decodeHex(String data) throws DecoderException {
/* 128 */     return decodeHex(data.toCharArray());
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
/*     */   public static char[] encodeHex(byte[] data) {
/* 140 */     return encodeHex(data, true);
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
/*     */   public static char[] encodeHex(byte[] data, boolean toLowerCase) {
/* 154 */     return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
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
/*     */   protected static char[] encodeHex(byte[] data, char[] toDigits) {
/* 169 */     int l = data.length;
/* 170 */     char[] out = new char[l << 1];
/* 171 */     encodeHex(data, 0, data.length, toDigits, out, 0);
/* 172 */     return out;
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
/*     */   public static char[] encodeHex(byte[] data, int dataOffset, int dataLen, boolean toLowerCase) {
/* 188 */     char[] out = new char[dataLen << 1];
/* 189 */     encodeHex(data, dataOffset, dataLen, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER, out, 0);
/* 190 */     return out;
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
/*     */   public static void encodeHex(byte[] data, int dataOffset, int dataLen, boolean toLowerCase, char[] out, int outOffset) {
/* 206 */     encodeHex(data, dataOffset, dataLen, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER, out, outOffset);
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
/*     */   private static void encodeHex(byte[] data, int dataOffset, int dataLen, char[] toDigits, char[] out, int outOffset) {
/* 222 */     for (int i = dataOffset, j = outOffset; i < dataOffset + dataLen; i++) {
/* 223 */       out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
/* 224 */       out[j++] = toDigits[0xF & data[i]];
/*     */     } 
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
/*     */   public static char[] encodeHex(ByteBuffer data) {
/* 241 */     return encodeHex(data, true);
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
/*     */   public static char[] encodeHex(ByteBuffer data, boolean toLowerCase) {
/* 258 */     return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
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
/*     */   protected static char[] encodeHex(ByteBuffer byteBuffer, char[] toDigits) {
/* 276 */     return encodeHex(toByteArray(byteBuffer), toDigits);
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
/*     */   public static String encodeHexString(byte[] data) {
/* 288 */     return new String(encodeHex(data));
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
/*     */   public static String encodeHexString(byte[] data, boolean toLowerCase) {
/* 301 */     return new String(encodeHex(data, toLowerCase));
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
/*     */   public static String encodeHexString(ByteBuffer data) {
/* 316 */     return new String(encodeHex(data));
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
/*     */   public static String encodeHexString(ByteBuffer data, boolean toLowerCase) {
/* 332 */     return new String(encodeHex(data, toLowerCase));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] toByteArray(ByteBuffer byteBuffer) {
/* 343 */     int remaining = byteBuffer.remaining();
/*     */     
/* 345 */     if (byteBuffer.hasArray()) {
/* 346 */       byte[] arrayOfByte = byteBuffer.array();
/* 347 */       if (remaining == arrayOfByte.length) {
/* 348 */         byteBuffer.position(remaining);
/* 349 */         return arrayOfByte;
/*     */       } 
/*     */     } 
/*     */     
/* 353 */     byte[] byteArray = new byte[remaining];
/* 354 */     byteBuffer.get(byteArray);
/* 355 */     return byteArray;
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
/*     */   protected static int toDigit(char ch, int index) throws DecoderException {
/* 367 */     int digit = Character.digit(ch, 16);
/* 368 */     if (digit == -1) {
/* 369 */       throw new DecoderException("Illegal hexadecimal character " + ch + " at index " + index);
/*     */     }
/* 371 */     return digit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hex() {
/* 381 */     this.charset = DEFAULT_CHARSET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hex(Charset charset) {
/* 391 */     this.charset = charset;
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
/*     */   public Hex(String charsetName) {
/* 403 */     this(Charset.forName(charsetName));
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
/*     */   public byte[] decode(byte[] array) throws DecoderException {
/* 418 */     return decodeHex((new String(array, getCharset())).toCharArray());
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
/*     */   public byte[] decode(ByteBuffer buffer) throws DecoderException {
/* 436 */     return decodeHex((new String(toByteArray(buffer), getCharset())).toCharArray());
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
/*     */   public Object decode(Object object) throws DecoderException {
/* 452 */     if (object instanceof String)
/* 453 */       return decode(((String)object).toCharArray()); 
/* 454 */     if (object instanceof byte[])
/* 455 */       return decode((byte[])object); 
/* 456 */     if (object instanceof ByteBuffer) {
/* 457 */       return decode((ByteBuffer)object);
/*     */     }
/*     */     try {
/* 460 */       return decodeHex((char[])object);
/* 461 */     } catch (ClassCastException e) {
/* 462 */       throw new DecoderException(e.getMessage(), e);
/*     */     } 
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
/*     */   public byte[] encode(byte[] array) {
/* 483 */     return encodeHexString(array).getBytes(getCharset());
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
/*     */   public byte[] encode(ByteBuffer array) {
/* 503 */     return encodeHexString(array).getBytes(getCharset());
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
/*     */   public Object encode(Object object) throws EncoderException {
/*     */     byte[] byteArray;
/* 523 */     if (object instanceof String) {
/* 524 */       byteArray = ((String)object).getBytes(getCharset());
/* 525 */     } else if (object instanceof ByteBuffer) {
/* 526 */       byteArray = toByteArray((ByteBuffer)object);
/*     */     } else {
/*     */       try {
/* 529 */         byteArray = (byte[])object;
/* 530 */       } catch (ClassCastException e) {
/* 531 */         throw new EncoderException(e.getMessage(), e);
/*     */       } 
/*     */     } 
/* 534 */     return encodeHex(byteArray);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getCharset() {
/* 544 */     return this.charset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCharsetName() {
/* 554 */     return this.charset.name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 564 */     return super.toString() + "[charsetName=" + this.charset + "]";
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/commons/codec/binary/Hex.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */