/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class UnsignedInts
/*     */ {
/*     */   static final long INT_MASK = 4294967295L;
/*     */   
/*     */   static int flip(int value) {
/*  55 */     return value ^ Integer.MIN_VALUE;
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
/*     */   public static int compare(int a, int b) {
/*  70 */     return Ints.compare(flip(a), flip(b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long toLong(int value) {
/*  79 */     return value & 0xFFFFFFFFL;
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
/*     */   public static int checkedCast(long value) {
/*  93 */     Preconditions.checkArgument((value >> 32L == 0L), "out of range: %s", value);
/*  94 */     return (int)value;
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
/*     */   public static int saturatedCast(long value) {
/* 107 */     if (value <= 0L)
/* 108 */       return 0; 
/* 109 */     if (value >= 4294967296L) {
/* 110 */       return -1;
/*     */     }
/* 112 */     return (int)value;
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
/*     */   public static int min(int... array) {
/* 125 */     Preconditions.checkArgument((array.length > 0));
/* 126 */     int min = flip(array[0]);
/* 127 */     for (int i = 1; i < array.length; i++) {
/* 128 */       int next = flip(array[i]);
/* 129 */       if (next < min) {
/* 130 */         min = next;
/*     */       }
/*     */     } 
/* 133 */     return flip(min);
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
/*     */   public static int max(int... array) {
/* 145 */     Preconditions.checkArgument((array.length > 0));
/* 146 */     int max = flip(array[0]);
/* 147 */     for (int i = 1; i < array.length; i++) {
/* 148 */       int next = flip(array[i]);
/* 149 */       if (next > max) {
/* 150 */         max = next;
/*     */       }
/*     */     } 
/* 153 */     return flip(max);
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
/*     */   public static String join(String separator, int... array) {
/* 165 */     Preconditions.checkNotNull(separator);
/* 166 */     if (array.length == 0) {
/* 167 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 171 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 172 */     builder.append(toString(array[0]));
/* 173 */     for (int i = 1; i < array.length; i++) {
/* 174 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 176 */     return builder.toString();
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
/*     */   public static Comparator<int[]> lexicographicalComparator() {
/* 190 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   enum LexicographicalComparator implements Comparator<int[]> {
/* 194 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 198 */       int minLength = Math.min(left.length, right.length);
/* 199 */       for (int i = 0; i < minLength; i++) {
/* 200 */         if (left[i] != right[i]) {
/* 201 */           return UnsignedInts.compare(left[i], right[i]);
/*     */         }
/*     */       } 
/* 204 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 209 */       return "UnsignedInts.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(int[] array) {
/* 219 */     Preconditions.checkNotNull(array);
/* 220 */     sort(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(int[] array, int fromIndex, int toIndex) {
/* 230 */     Preconditions.checkNotNull(array);
/* 231 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 232 */     for (i = fromIndex; i < toIndex; i++) {
/* 233 */       array[i] = flip(array[i]);
/*     */     }
/* 235 */     Arrays.sort(array, fromIndex, toIndex);
/* 236 */     for (i = fromIndex; i < toIndex; i++) {
/* 237 */       array[i] = flip(array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array) {
/* 248 */     Preconditions.checkNotNull(array);
/* 249 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array, int fromIndex, int toIndex) {
/* 259 */     Preconditions.checkNotNull(array);
/* 260 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 261 */     for (i = fromIndex; i < toIndex; i++) {
/* 262 */       array[i] = array[i] ^ Integer.MAX_VALUE;
/*     */     }
/* 264 */     Arrays.sort(array, fromIndex, toIndex);
/* 265 */     for (i = fromIndex; i < toIndex; i++) {
/* 266 */       array[i] = array[i] ^ Integer.MAX_VALUE;
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
/*     */   public static int divide(int dividend, int divisor) {
/* 281 */     return (int)(toLong(dividend) / toLong(divisor));
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
/*     */   public static int remainder(int dividend, int divisor) {
/* 295 */     return (int)(toLong(dividend) % toLong(divisor));
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int decode(String stringValue) {
/* 315 */     ParseRequest request = ParseRequest.fromString(stringValue);
/*     */     
/*     */     try {
/* 318 */       return parseUnsignedInt(request.rawValue, request.radix);
/* 319 */     } catch (NumberFormatException e) {
/* 320 */       NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
/*     */       
/* 322 */       decodeException.initCause(e);
/* 323 */       throw decodeException;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int parseUnsignedInt(String s) {
/* 338 */     return parseUnsignedInt(s, 10);
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
/*     */   public static int parseUnsignedInt(String string, int radix) {
/* 356 */     Preconditions.checkNotNull(string);
/* 357 */     long result = Long.parseLong(string, radix);
/* 358 */     if ((result & 0xFFFFFFFFL) != result) {
/* 359 */       throw new NumberFormatException("Input " + string + " in base " + radix + " is not in the range of an unsigned integer");
/*     */     }
/*     */     
/* 362 */     return (int)result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(int x) {
/* 371 */     return toString(x, 10);
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
/*     */   public static String toString(int x, int radix) {
/* 386 */     long asLong = x & 0xFFFFFFFFL;
/* 387 */     return Long.toString(asLong, radix);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/UnsignedInts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */