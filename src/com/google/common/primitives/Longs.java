/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Converter;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import javax.annotation.CheckForNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Longs
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   public static final long MAX_POWER_OF_TWO = 4611686018427387904L;
/*     */   
/*     */   public static int hashCode(long value) {
/*  79 */     return (int)(value ^ value >>> 32L);
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
/*     */   public static int compare(long a, long b) {
/*  95 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(long[] array, long target) {
/* 106 */     for (long value : array) {
/* 107 */       if (value == target) {
/* 108 */         return true;
/*     */       }
/*     */     } 
/* 111 */     return false;
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
/*     */   public static int indexOf(long[] array, long target) {
/* 123 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(long[] array, long target, int start, int end) {
/* 128 */     for (int i = start; i < end; i++) {
/* 129 */       if (array[i] == target) {
/* 130 */         return i;
/*     */       }
/*     */     } 
/* 133 */     return -1;
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
/*     */   public static int indexOf(long[] array, long[] target) {
/* 147 */     Preconditions.checkNotNull(array, "array");
/* 148 */     Preconditions.checkNotNull(target, "target");
/* 149 */     if (target.length == 0) {
/* 150 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 154 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 155 */       int j = 0; while (true) { if (j < target.length) {
/* 156 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 160 */         return i; }
/*     */     
/* 162 */     }  return -1;
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
/*     */   public static int lastIndexOf(long[] array, long target) {
/* 174 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(long[] array, long target, int start, int end) {
/* 179 */     for (int i = end - 1; i >= start; i--) {
/* 180 */       if (array[i] == target) {
/* 181 */         return i;
/*     */       }
/*     */     } 
/* 184 */     return -1;
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
/* 196 */     Preconditions.checkArgument((array.length > 0));
/* 197 */     long min = array[0];
/* 198 */     for (int i = 1; i < array.length; i++) {
/* 199 */       if (array[i] < min) {
/* 200 */         min = array[i];
/*     */       }
/*     */     } 
/* 203 */     return min;
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
/* 215 */     Preconditions.checkArgument((array.length > 0));
/* 216 */     long max = array[0];
/* 217 */     for (int i = 1; i < array.length; i++) {
/* 218 */       if (array[i] > max) {
/* 219 */         max = array[i];
/*     */       }
/*     */     } 
/* 222 */     return max;
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
/*     */   public static long constrainToRange(long value, long min, long max) {
/* 239 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 240 */     return Math.min(Math.max(value, min), max);
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
/*     */   public static long[] concat(long[]... arrays) {
/* 253 */     long length = 0L;
/* 254 */     for (long[] array : arrays) {
/* 255 */       length += array.length;
/*     */     }
/* 257 */     long[] result = new long[checkNoOverflow(length)];
/* 258 */     int pos = 0;
/* 259 */     for (long[] array : arrays) {
/* 260 */       System.arraycopy(array, 0, result, pos, array.length);
/* 261 */       pos += array.length;
/*     */     } 
/* 263 */     return result;
/*     */   }
/*     */   
/*     */   private static int checkNoOverflow(long result) {
/* 267 */     Preconditions.checkArgument((result == (int)result), "the total number of elements (%s) in the arrays must fit in an int", result);
/*     */ 
/*     */ 
/*     */     
/* 271 */     return (int)result;
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
/*     */   public static byte[] toByteArray(long value) {
/* 287 */     byte[] result = new byte[8];
/* 288 */     for (int i = 7; i >= 0; i--) {
/* 289 */       result[i] = (byte)(int)(value & 0xFFL);
/* 290 */       value >>= 8L;
/*     */     } 
/* 292 */     return result;
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
/*     */   public static long fromByteArray(byte[] bytes) {
/* 307 */     Preconditions.checkArgument((bytes.length >= 8), "array too small: %s < %s", bytes.length, 8);
/* 308 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
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
/*     */   public static long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
/* 320 */     return (b1 & 0xFFL) << 56L | (b2 & 0xFFL) << 48L | (b3 & 0xFFL) << 40L | (b4 & 0xFFL) << 32L | (b5 & 0xFFL) << 24L | (b6 & 0xFFL) << 16L | (b7 & 0xFFL) << 8L | b8 & 0xFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AsciiDigits
/*     */   {
/*     */     private static final byte[] asciiDigits;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 340 */       byte[] result = new byte[128];
/* 341 */       Arrays.fill(result, (byte)-1); int i;
/* 342 */       for (i = 0; i < 10; i++) {
/* 343 */         result[48 + i] = (byte)i;
/*     */       }
/* 345 */       for (i = 0; i < 26; i++) {
/* 346 */         result[65 + i] = (byte)(10 + i);
/* 347 */         result[97 + i] = (byte)(10 + i);
/*     */       } 
/* 349 */       asciiDigits = result;
/*     */     }
/*     */     
/*     */     static int digit(char c) {
/* 353 */       return (c < '') ? asciiDigits[c] : -1;
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
/*     */   @CheckForNull
/*     */   public static Long tryParse(String string) {
/* 376 */     return tryParse(string, 10);
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
/*     */   @CheckForNull
/*     */   public static Long tryParse(String string, int radix) {
/* 401 */     if (((String)Preconditions.checkNotNull(string)).isEmpty()) {
/* 402 */       return null;
/*     */     }
/* 404 */     if (radix < 2 || radix > 36) {
/* 405 */       throw new IllegalArgumentException("radix must be between MIN_RADIX and MAX_RADIX but was " + radix);
/*     */     }
/*     */     
/* 408 */     boolean negative = (string.charAt(0) == '-');
/* 409 */     int index = negative ? 1 : 0;
/* 410 */     if (index == string.length()) {
/* 411 */       return null;
/*     */     }
/* 413 */     int digit = AsciiDigits.digit(string.charAt(index++));
/* 414 */     if (digit < 0 || digit >= radix) {
/* 415 */       return null;
/*     */     }
/* 417 */     long accum = -digit;
/*     */     
/* 419 */     long cap = Long.MIN_VALUE / radix;
/*     */     
/* 421 */     while (index < string.length()) {
/* 422 */       digit = AsciiDigits.digit(string.charAt(index++));
/* 423 */       if (digit < 0 || digit >= radix || accum < cap) {
/* 424 */         return null;
/*     */       }
/* 426 */       accum *= radix;
/* 427 */       if (accum < Long.MIN_VALUE + digit) {
/* 428 */         return null;
/*     */       }
/* 430 */       accum -= digit;
/*     */     } 
/*     */     
/* 433 */     if (negative)
/* 434 */       return Long.valueOf(accum); 
/* 435 */     if (accum == Long.MIN_VALUE) {
/* 436 */       return null;
/*     */     }
/* 438 */     return Long.valueOf(-accum);
/*     */   }
/*     */   
/*     */   private static final class LongConverter
/*     */     extends Converter<String, Long> implements Serializable {
/* 443 */     static final Converter<String, Long> INSTANCE = new LongConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Long doForward(String value) {
/* 447 */       return Long.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Long value) {
/* 452 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 457 */       return "Longs.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 461 */       return INSTANCE;
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
/*     */   public static Converter<String, Long> stringConverter() {
/* 479 */     return LongConverter.INSTANCE;
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
/*     */   public static long[] ensureCapacity(long[] array, int minLength, int padding) {
/* 496 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 497 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 498 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/* 510 */     Preconditions.checkNotNull(separator);
/* 511 */     if (array.length == 0) {
/* 512 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 516 */     StringBuilder builder = new StringBuilder(array.length * 10);
/* 517 */     builder.append(array[0]);
/* 518 */     for (int i = 1; i < array.length; i++) {
/* 519 */       builder.append(separator).append(array[i]);
/*     */     }
/* 521 */     return builder.toString();
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
/*     */   public static Comparator<long[]> lexicographicalComparator() {
/* 538 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<long[]> {
/* 542 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 546 */       int minLength = Math.min(left.length, right.length);
/* 547 */       for (int i = 0; i < minLength; i++) {
/* 548 */         int result = Longs.compare(left[i], right[i]);
/* 549 */         if (result != 0) {
/* 550 */           return result;
/*     */         }
/*     */       } 
/* 553 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 558 */       return "Longs.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array) {
/* 568 */     Preconditions.checkNotNull(array);
/* 569 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array, int fromIndex, int toIndex) {
/* 579 */     Preconditions.checkNotNull(array);
/* 580 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 581 */     Arrays.sort(array, fromIndex, toIndex);
/* 582 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(long[] array) {
/* 592 */     Preconditions.checkNotNull(array);
/* 593 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(long[] array, int fromIndex, int toIndex) {
/* 607 */     Preconditions.checkNotNull(array);
/* 608 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 609 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 610 */       long tmp = array[i];
/* 611 */       array[i] = array[j];
/* 612 */       array[j] = tmp;
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
/*     */   public static void rotate(long[] array, int distance) {
/* 627 */     rotate(array, distance, 0, array.length);
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
/*     */   public static void rotate(long[] array, int distance, int fromIndex, int toIndex) {
/* 644 */     Preconditions.checkNotNull(array);
/* 645 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 646 */     if (array.length <= 1) {
/*     */       return;
/*     */     }
/*     */     
/* 650 */     int length = toIndex - fromIndex;
/*     */ 
/*     */     
/* 653 */     int m = -distance % length;
/* 654 */     m = (m < 0) ? (m + length) : m;
/*     */     
/* 656 */     int newFirstIndex = m + fromIndex;
/* 657 */     if (newFirstIndex == fromIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 661 */     reverse(array, fromIndex, newFirstIndex);
/* 662 */     reverse(array, newFirstIndex, toIndex);
/* 663 */     reverse(array, fromIndex, toIndex);
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
/*     */   public static long[] toArray(Collection<? extends Number> collection) {
/* 680 */     if (collection instanceof LongArrayAsList) {
/* 681 */       return ((LongArrayAsList)collection).toLongArray();
/*     */     }
/*     */     
/* 684 */     Object[] boxedArray = collection.toArray();
/* 685 */     int len = boxedArray.length;
/* 686 */     long[] array = new long[len];
/* 687 */     for (int i = 0; i < len; i++)
/*     */     {
/* 689 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).longValue();
/*     */     }
/* 691 */     return array;
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
/*     */   public static List<Long> asList(long... backingArray) {
/* 712 */     if (backingArray.length == 0) {
/* 713 */       return Collections.emptyList();
/*     */     }
/* 715 */     return new LongArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class LongArrayAsList extends AbstractList<Long> implements RandomAccess, Serializable {
/*     */     final long[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongArrayAsList(long[] array) {
/* 726 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     LongArrayAsList(long[] array, int start, int end) {
/* 730 */       this.array = array;
/* 731 */       this.start = start;
/* 732 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 737 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 742 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long get(int index) {
/* 747 */       Preconditions.checkElementIndex(index, size());
/* 748 */       return Long.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfLong spliterator() {
/* 753 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object target) {
/* 759 */       return (target instanceof Long && Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object target) {
/* 765 */       if (target instanceof Long) {
/* 766 */         int i = Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 767 */         if (i >= 0) {
/* 768 */           return i - this.start;
/*     */         }
/*     */       } 
/* 771 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object target) {
/* 777 */       if (target instanceof Long) {
/* 778 */         int i = Longs.lastIndexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 779 */         if (i >= 0) {
/* 780 */           return i - this.start;
/*     */         }
/*     */       } 
/* 783 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long set(int index, Long element) {
/* 788 */       Preconditions.checkElementIndex(index, size());
/* 789 */       long oldValue = this.array[this.start + index];
/*     */       
/* 791 */       this.array[this.start + index] = ((Long)Preconditions.checkNotNull(element)).longValue();
/* 792 */       return Long.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Long> subList(int fromIndex, int toIndex) {
/* 797 */       int size = size();
/* 798 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 799 */       if (fromIndex == toIndex) {
/* 800 */         return Collections.emptyList();
/*     */       }
/* 802 */       return new LongArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 807 */       if (object == this) {
/* 808 */         return true;
/*     */       }
/* 810 */       if (object instanceof LongArrayAsList) {
/* 811 */         LongArrayAsList that = (LongArrayAsList)object;
/* 812 */         int size = size();
/* 813 */         if (that.size() != size) {
/* 814 */           return false;
/*     */         }
/* 816 */         for (int i = 0; i < size; i++) {
/* 817 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 818 */             return false;
/*     */           }
/*     */         } 
/* 821 */         return true;
/*     */       } 
/* 823 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 828 */       int result = 1;
/* 829 */       for (int i = this.start; i < this.end; i++) {
/* 830 */         result = 31 * result + Longs.hashCode(this.array[i]);
/*     */       }
/* 832 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 837 */       StringBuilder builder = new StringBuilder(size() * 10);
/* 838 */       builder.append('[').append(this.array[this.start]);
/* 839 */       for (int i = this.start + 1; i < this.end; i++) {
/* 840 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 842 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     long[] toLongArray() {
/* 846 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Longs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */