/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class UnsignedLongs
/*     */ {
/*     */   public static final long MAX_VALUE = -1L;
/*     */   
/*     */   private static long flip(long a) {
/*  63 */     return a ^ Long.MIN_VALUE;
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
/*     */   public static int compare(long a, long b) {
/*  78 */     return Longs.compare(flip(a), flip(b));
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
/*     */   public static long min(long... array) {
/*  90 */     Preconditions.checkArgument((array.length > 0));
/*  91 */     long min = flip(array[0]);
/*  92 */     for (int i = 1; i < array.length; i++) {
/*  93 */       long next = flip(array[i]);
/*  94 */       if (next < min) {
/*  95 */         min = next;
/*     */       }
/*     */     } 
/*  98 */     return flip(min);
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
/*     */   public static long max(long... array) {
/* 110 */     Preconditions.checkArgument((array.length > 0));
/* 111 */     long max = flip(array[0]);
/* 112 */     for (int i = 1; i < array.length; i++) {
/* 113 */       long next = flip(array[i]);
/* 114 */       if (next > max) {
/* 115 */         max = next;
/*     */       }
/*     */     } 
/* 118 */     return flip(max);
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
/*     */   public static String join(String separator, long... array) {
/* 130 */     Preconditions.checkNotNull(separator);
/* 131 */     if (array.length == 0) {
/* 132 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 136 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 137 */     builder.append(toString(array[0]));
/* 138 */     for (int i = 1; i < array.length; i++) {
/* 139 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 141 */     return builder.toString();
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
/*     */   public static Comparator<long[]> lexicographicalComparator() {
/* 156 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   enum LexicographicalComparator implements Comparator<long[]> {
/* 160 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 164 */       int minLength = Math.min(left.length, right.length);
/* 165 */       for (int i = 0; i < minLength; i++) {
/* 166 */         if (left[i] != right[i]) {
/* 167 */           return UnsignedLongs.compare(left[i], right[i]);
/*     */         }
/*     */       } 
/* 170 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 175 */       return "UnsignedLongs.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(long[] array) {
/* 185 */     Preconditions.checkNotNull(array);
/* 186 */     sort(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(long[] array, int fromIndex, int toIndex) {
/* 196 */     Preconditions.checkNotNull(array);
/* 197 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 198 */     for (i = fromIndex; i < toIndex; i++) {
/* 199 */       array[i] = flip(array[i]);
/*     */     }
/* 201 */     Arrays.sort(array, fromIndex, toIndex);
/* 202 */     for (i = fromIndex; i < toIndex; i++) {
/* 203 */       array[i] = flip(array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array) {
/* 214 */     Preconditions.checkNotNull(array);
/* 215 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array, int fromIndex, int toIndex) {
/* 225 */     Preconditions.checkNotNull(array);
/* 226 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 227 */     for (i = fromIndex; i < toIndex; i++) {
/* 228 */       array[i] = array[i] ^ Long.MAX_VALUE;
/*     */     }
/* 230 */     Arrays.sort(array, fromIndex, toIndex);
/* 231 */     for (i = fromIndex; i < toIndex; i++) {
/* 232 */       array[i] = array[i] ^ Long.MAX_VALUE;
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
/*     */   public static long divide(long dividend, long divisor) {
/* 247 */     if (divisor < 0L) {
/* 248 */       if (compare(dividend, divisor) < 0) {
/* 249 */         return 0L;
/*     */       }
/* 251 */       return 1L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 256 */     if (dividend >= 0L) {
/* 257 */       return dividend / divisor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 266 */     long quotient = (dividend >>> 1L) / divisor << 1L;
/* 267 */     long rem = dividend - quotient * divisor;
/* 268 */     return quotient + ((compare(rem, divisor) >= 0) ? 1L : 0L);
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
/*     */   public static long remainder(long dividend, long divisor) {
/* 283 */     if (divisor < 0L) {
/* 284 */       if (compare(dividend, divisor) < 0) {
/* 285 */         return dividend;
/*     */       }
/* 287 */       return dividend - divisor;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 292 */     if (dividend >= 0L) {
/* 293 */       return dividend % divisor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 302 */     long quotient = (dividend >>> 1L) / divisor << 1L;
/* 303 */     long rem = dividend - quotient * divisor;
/* 304 */     return rem - ((compare(rem, divisor) >= 0) ? divisor : 0L);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long parseUnsignedLong(String string) {
/* 319 */     return parseUnsignedLong(string, 10);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long parseUnsignedLong(String string, int radix) {
/* 337 */     Preconditions.checkNotNull(string);
/* 338 */     if (string.length() == 0) {
/* 339 */       throw new NumberFormatException("empty string");
/*     */     }
/* 341 */     if (radix < 2 || radix > 36) {
/* 342 */       throw new NumberFormatException("illegal radix: " + radix);
/*     */     }
/*     */     
/* 345 */     int maxSafePos = ParseOverflowDetection.maxSafeDigits[radix] - 1;
/* 346 */     long value = 0L;
/* 347 */     for (int pos = 0; pos < string.length(); pos++) {
/* 348 */       int digit = Character.digit(string.charAt(pos), radix);
/* 349 */       if (digit == -1) {
/* 350 */         throw new NumberFormatException(string);
/*     */       }
/* 352 */       if (pos > maxSafePos && ParseOverflowDetection.overflowInParse(value, digit, radix)) {
/* 353 */         throw new NumberFormatException("Too large for unsigned long: " + string);
/*     */       }
/* 355 */       value = value * radix + digit;
/*     */     } 
/*     */     
/* 358 */     return value;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long decode(String stringValue) {
/* 379 */     ParseRequest request = ParseRequest.fromString(stringValue);
/*     */     
/*     */     try {
/* 382 */       return parseUnsignedLong(request.rawValue, request.radix);
/* 383 */     } catch (NumberFormatException e) {
/* 384 */       NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
/*     */       
/* 386 */       decodeException.initCause(e);
/* 387 */       throw decodeException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ParseOverflowDetection
/*     */   {
/* 399 */     static final long[] maxValueDivs = new long[37];
/* 400 */     static final int[] maxValueMods = new int[37];
/* 401 */     static final int[] maxSafeDigits = new int[37];
/*     */     
/*     */     static {
/* 404 */       BigInteger overflow = new BigInteger("10000000000000000", 16);
/* 405 */       for (int i = 2; i <= 36; i++) {
/* 406 */         maxValueDivs[i] = UnsignedLongs.divide(-1L, i);
/* 407 */         maxValueMods[i] = (int)UnsignedLongs.remainder(-1L, i);
/* 408 */         maxSafeDigits[i] = overflow.toString(i).length() - 1;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static boolean overflowInParse(long current, int digit, int radix) {
/* 419 */       if (current >= 0L) {
/* 420 */         if (current < maxValueDivs[radix]) {
/* 421 */           return false;
/*     */         }
/* 423 */         if (current > maxValueDivs[radix]) {
/* 424 */           return true;
/*     */         }
/*     */         
/* 427 */         return (digit > maxValueMods[radix]);
/*     */       } 
/*     */ 
/*     */       
/* 431 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(long x) {
/* 441 */     return toString(x, 10);
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
/*     */   public static String toString(long x, int radix) {
/* 456 */     Preconditions.checkArgument((radix >= 2 && radix <= 36), "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
/*     */ 
/*     */ 
/*     */     
/* 460 */     if (x == 0L)
/*     */     {
/* 462 */       return "0"; } 
/* 463 */     if (x > 0L) {
/* 464 */       return Long.toString(x, radix);
/*     */     }
/* 466 */     char[] buf = new char[64];
/* 467 */     int i = buf.length;
/* 468 */     if ((radix & radix - 1) == 0) {
/*     */       
/* 470 */       int shift = Integer.numberOfTrailingZeros(radix);
/* 471 */       int mask = radix - 1;
/*     */       do {
/* 473 */         buf[--i] = Character.forDigit((int)x & mask, radix);
/* 474 */         x >>>= shift;
/* 475 */       } while (x != 0L);
/*     */     } else {
/*     */       long quotient;
/*     */ 
/*     */       
/* 480 */       if ((radix & 0x1) == 0) {
/*     */         
/* 482 */         quotient = (x >>> 1L) / (radix >>> 1);
/*     */       } else {
/* 484 */         quotient = divide(x, radix);
/*     */       } 
/* 486 */       long rem = x - quotient * radix;
/* 487 */       buf[--i] = Character.forDigit((int)rem, radix);
/* 488 */       x = quotient;
/*     */       
/* 490 */       while (x > 0L) {
/* 491 */         buf[--i] = Character.forDigit((int)(x % radix), radix);
/* 492 */         x /= radix;
/*     */       } 
/*     */     } 
/*     */     
/* 496 */     return new String(buf, i, buf.length - i);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/UnsignedLongs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */