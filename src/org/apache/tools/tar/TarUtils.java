/*     */ package org.apache.tools.tar;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigInteger;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.tools.zip.ZipEncoding;
/*     */ import org.apache.tools.zip.ZipEncodingHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TarUtils
/*     */ {
/*     */   private static final int BYTE_MASK = 255;
/*  43 */   static final ZipEncoding DEFAULT_ENCODING = ZipEncodingHelper.getZipEncoding(null);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   static final ZipEncoding FALLBACK_ENCODING = new ZipEncoding() {
/*     */       public boolean canEncode(String name) {
/*  50 */         return true;
/*     */       }
/*     */       
/*     */       public ByteBuffer encode(String name) {
/*  54 */         int length = name.length();
/*  55 */         byte[] buf = new byte[length];
/*     */ 
/*     */         
/*  58 */         for (int i = 0; i < length; i++) {
/*  59 */           buf[i] = (byte)name.charAt(i);
/*     */         }
/*  61 */         return ByteBuffer.wrap(buf);
/*     */       }
/*     */       
/*     */       public String decode(byte[] buffer) {
/*  65 */         StringBuilder result = new StringBuilder(buffer.length);
/*     */         
/*  67 */         for (byte b : buffer) {
/*  68 */           if (b == 0) {
/*     */             break;
/*     */           }
/*  71 */           result.append((char)(b & 0xFF));
/*     */         } 
/*     */         
/*  74 */         return result.toString();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long parseOctal(byte[] buffer, int offset, int length) {
/* 104 */     long result = 0L;
/* 105 */     int end = offset + length;
/* 106 */     int start = offset;
/*     */     
/* 108 */     if (length < 2) {
/* 109 */       throw new IllegalArgumentException("Length " + length + " must be at least 2");
/*     */     }
/*     */     
/* 112 */     if (buffer[start] == 0) {
/* 113 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/* 117 */     while (start < end && 
/* 118 */       buffer[start] == 32) {
/* 119 */       start++;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     byte trailer = buffer[end - 1];
/* 130 */     while (start < end && (trailer == 0 || trailer == 32)) {
/* 131 */       end--;
/* 132 */       trailer = buffer[end - 1];
/*     */     } 
/*     */     
/* 135 */     while (start < end) {
/* 136 */       byte currentByte = buffer[start];
/*     */       
/* 138 */       if (currentByte < 48 || currentByte > 55) {
/* 139 */         throw new IllegalArgumentException(
/* 140 */             exceptionMessage(buffer, offset, length, start, currentByte));
/*     */       }
/* 142 */       result = (result << 3L) + (currentByte - 48);
/*     */       
/* 144 */       start++;
/*     */     } 
/*     */     
/* 147 */     return result;
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
/*     */ 
/*     */   
/*     */   public static long parseOctalOrBinary(byte[] buffer, int offset, int length) {
/* 169 */     if ((buffer[offset] & 0x80) == 0) {
/* 170 */       return parseOctal(buffer, offset, length);
/*     */     }
/* 172 */     boolean negative = (buffer[offset] == -1);
/* 173 */     if (length < 9) {
/* 174 */       return parseBinaryLong(buffer, offset, length, negative);
/*     */     }
/* 176 */     return parseBinaryBigInteger(buffer, offset, length, negative);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static long parseBinaryLong(byte[] buffer, int offset, int length, boolean negative) {
/* 182 */     if (length >= 9)
/* 183 */       throw new IllegalArgumentException(String.format("At offset %d, %d byte binary number exceeds maximum signed long value", new Object[] {
/*     */               
/* 185 */               Integer.valueOf(offset), Integer.valueOf(length)
/*     */             })); 
/* 187 */     long val = 0L;
/* 188 */     for (int i = 1; i < length; i++) {
/* 189 */       val = (val << 8L) + (buffer[offset + i] & 0xFF);
/*     */     }
/* 191 */     if (negative) {
/*     */       
/* 193 */       val--;
/* 194 */       val ^= (long)Math.pow(2.0D, (length - 1) * 8.0D) - 1L;
/*     */     } 
/* 196 */     return negative ? -val : val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long parseBinaryBigInteger(byte[] buffer, int offset, int length, boolean negative) {
/* 203 */     byte[] remainder = new byte[length - 1];
/* 204 */     System.arraycopy(buffer, offset + 1, remainder, 0, length - 1);
/* 205 */     BigInteger val = new BigInteger(remainder);
/* 206 */     if (negative)
/*     */     {
/* 208 */       val = val.add(BigInteger.valueOf(-1L)).not();
/*     */     }
/* 210 */     if (val.bitLength() > 63)
/* 211 */       throw new IllegalArgumentException(String.format("At offset %d, %d byte binary number exceeds maximum signed long value", new Object[] {
/*     */               
/* 213 */               Integer.valueOf(offset), Integer.valueOf(length)
/*     */             })); 
/* 215 */     return negative ? -val.longValue() : val.longValue();
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
/*     */   public static boolean parseBoolean(byte[] buffer, int offset) {
/* 229 */     return (buffer[offset] == 1);
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
/*     */   private static String exceptionMessage(byte[] buffer, int offset, int length, int current, byte currentByte) {
/* 242 */     String string = new String(buffer, offset, length);
/*     */     
/* 244 */     string = string.replaceAll("\000", "{NUL}");
/* 245 */     return String.format("Invalid byte %s at offset %d in '%s' len=%d", new Object[] {
/* 246 */           Byte.valueOf(currentByte), Integer.valueOf(current - offset), string, Integer.valueOf(length)
/*     */         });
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
/*     */   public static String parseName(byte[] buffer, int offset, int length) {
/*     */     try {
/* 261 */       return parseName(buffer, offset, length, DEFAULT_ENCODING);
/* 262 */     } catch (IOException ex) {
/*     */       try {
/* 264 */         return parseName(buffer, offset, length, FALLBACK_ENCODING);
/* 265 */       } catch (IOException ex2) {
/*     */         
/* 267 */         throw new RuntimeException(ex2);
/*     */       } 
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
/*     */   public static String parseName(byte[] buffer, int offset, int length, ZipEncoding encoding) throws IOException {
/* 289 */     int len = length;
/* 290 */     for (; len > 0 && 
/* 291 */       buffer[offset + len - 1] == 0; len--);
/*     */ 
/*     */ 
/*     */     
/* 295 */     if (len > 0) {
/* 296 */       byte[] b = new byte[len];
/* 297 */       System.arraycopy(buffer, offset, b, 0, len);
/* 298 */       return encoding.decode(b);
/*     */     } 
/* 300 */     return "";
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
/*     */   public static int formatNameBytes(String name, byte[] buf, int offset, int length) {
/*     */     try {
/* 320 */       return formatNameBytes(name, buf, offset, length, DEFAULT_ENCODING);
/* 321 */     } catch (IOException ex) {
/*     */       try {
/* 323 */         return formatNameBytes(name, buf, offset, length, FALLBACK_ENCODING);
/*     */       }
/* 325 */       catch (IOException ex2) {
/*     */         
/* 327 */         throw new RuntimeException(ex2);
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int formatNameBytes(String name, byte[] buf, int offset, int length, ZipEncoding encoding) throws IOException {
/* 353 */     int len = name.length();
/* 354 */     ByteBuffer b = encoding.encode(name);
/* 355 */     while (b.limit() > length && len > 0) {
/* 356 */       b = encoding.encode(name.substring(0, --len));
/*     */     }
/* 358 */     int limit = b.limit() - b.position();
/* 359 */     System.arraycopy(b.array(), b.arrayOffset(), buf, offset, limit);
/*     */ 
/*     */     
/* 362 */     for (int i = limit; i < length; i++) {
/* 363 */       buf[offset + i] = 0;
/*     */     }
/*     */     
/* 366 */     return offset + length;
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
/*     */   public static void formatUnsignedOctalString(long value, byte[] buffer, int offset, int length) {
/* 380 */     int remaining = length;
/* 381 */     remaining--;
/* 382 */     if (value == 0L) {
/* 383 */       buffer[offset + remaining--] = 48;
/*     */     } else {
/* 385 */       long val = value;
/* 386 */       for (; remaining >= 0 && val != 0L; remaining--) {
/*     */         
/* 388 */         buffer[offset + remaining] = (byte)(48 + (byte)(int)(val & 0x7L));
/* 389 */         val >>>= 3L;
/*     */       } 
/*     */       
/* 392 */       if (val != 0L) {
/* 393 */         throw new IllegalArgumentException(String.format("%d=%s will not fit in octal number buffer of length %d", new Object[] {
/*     */                 
/* 395 */                 Long.valueOf(value), Long.toOctalString(value), Integer.valueOf(length)
/*     */               }));
/*     */       }
/*     */     } 
/* 399 */     for (; remaining >= 0; remaining--) {
/* 400 */       buffer[offset + remaining] = 48;
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
/*     */   public static int formatOctalBytes(long value, byte[] buf, int offset, int length) {
/* 420 */     int idx = length - 2;
/* 421 */     formatUnsignedOctalString(value, buf, offset, idx);
/*     */     
/* 423 */     buf[offset + idx++] = 32;
/* 424 */     buf[offset + idx] = 0;
/*     */     
/* 426 */     return offset + length;
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
/*     */   public static int formatLongOctalBytes(long value, byte[] buf, int offset, int length) {
/* 445 */     int idx = length - 1;
/*     */     
/* 447 */     formatUnsignedOctalString(value, buf, offset, idx);
/* 448 */     buf[offset + idx] = 32;
/*     */     
/* 450 */     return offset + length;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static int formatLongOctalOrBinaryBytes(long value, byte[] buf, int offset, int length) {
/* 473 */     long maxAsOctalChar = (length == 8) ? 2097151L : 8589934591L;
/*     */     
/* 475 */     boolean negative = (value < 0L);
/* 476 */     if (!negative && value <= maxAsOctalChar) {
/* 477 */       return formatLongOctalBytes(value, buf, offset, length);
/*     */     }
/*     */     
/* 480 */     if (length < 9) {
/* 481 */       formatLongBinary(value, buf, offset, length, negative);
/*     */     }
/* 483 */     formatBigIntegerBinary(value, buf, offset, length, negative);
/*     */     
/* 485 */     buf[offset] = (byte)(negative ? 255 : 128);
/* 486 */     return offset + length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatLongBinary(long value, byte[] buf, int offset, int length, boolean negative) {
/* 492 */     int bits = (length - 1) * 8;
/* 493 */     long max = 1L << bits;
/* 494 */     long val = Math.abs(value);
/* 495 */     if (val >= max) {
/* 496 */       throw new IllegalArgumentException("Value " + value + " is too large for " + length + " byte field.");
/*     */     }
/*     */     
/* 499 */     if (negative) {
/* 500 */       val ^= max - 1L;
/* 501 */       val |= (255 << bits);
/* 502 */       val++;
/*     */     } 
/* 504 */     for (int i = offset + length - 1; i >= offset; i--) {
/* 505 */       buf[i] = (byte)(int)val;
/* 506 */       val >>= 8L;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void formatBigIntegerBinary(long value, byte[] buf, int offset, int length, boolean negative) {
/* 514 */     BigInteger val = BigInteger.valueOf(value);
/* 515 */     byte[] b = val.toByteArray();
/* 516 */     int len = b.length;
/* 517 */     int off = offset + length - len;
/* 518 */     System.arraycopy(b, 0, buf, off, len);
/* 519 */     byte fill = (byte)(negative ? 255 : 0);
/* 520 */     for (int i = offset + 1; i < off; i++) {
/* 521 */       buf[i] = fill;
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
/*     */   public static int formatCheckSumOctalBytes(long value, byte[] buf, int offset, int length) {
/* 541 */     int idx = length - 2;
/* 542 */     formatUnsignedOctalString(value, buf, offset, idx);
/*     */     
/* 544 */     buf[offset + idx++] = 0;
/* 545 */     buf[offset + idx] = 32;
/*     */     
/* 547 */     return offset + length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long computeCheckSum(byte[] buf) {
/* 557 */     long sum = 0L;
/*     */     
/* 559 */     for (byte element : buf) {
/* 560 */       sum += (0xFF & element);
/*     */     }
/*     */     
/* 563 */     return sum;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/org/apache/tools/tar/TarUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */