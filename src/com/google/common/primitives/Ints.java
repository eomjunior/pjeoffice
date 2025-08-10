/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Ints
/*     */   extends IntsMethodsForWeb
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   public static final int MAX_POWER_OF_TWO = 1073741824;
/*     */   
/*     */   public static int hashCode(int value) {
/*  76 */     return value;
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
/*     */   public static int checkedCast(long value) {
/*  88 */     int result = (int)value;
/*  89 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  90 */     return result;
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
/*     */   public static int saturatedCast(long value) {
/* 102 */     if (value > 2147483647L) {
/* 103 */       return Integer.MAX_VALUE;
/*     */     }
/* 105 */     if (value < -2147483648L) {
/* 106 */       return Integer.MIN_VALUE;
/*     */     }
/* 108 */     return (int)value;
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
/*     */   public static int compare(int a, int b) {
/* 124 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(int[] array, int target) {
/* 135 */     for (int value : array) {
/* 136 */       if (value == target) {
/* 137 */         return true;
/*     */       }
/*     */     } 
/* 140 */     return false;
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
/*     */   public static int indexOf(int[] array, int target) {
/* 152 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(int[] array, int target, int start, int end) {
/* 157 */     for (int i = start; i < end; i++) {
/* 158 */       if (array[i] == target) {
/* 159 */         return i;
/*     */       }
/*     */     } 
/* 162 */     return -1;
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
/*     */   public static int indexOf(int[] array, int[] target) {
/* 176 */     Preconditions.checkNotNull(array, "array");
/* 177 */     Preconditions.checkNotNull(target, "target");
/* 178 */     if (target.length == 0) {
/* 179 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 183 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 184 */       int j = 0; while (true) { if (j < target.length) {
/* 185 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 189 */         return i; }
/*     */     
/* 191 */     }  return -1;
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
/*     */   public static int lastIndexOf(int[] array, int target) {
/* 203 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(int[] array, int target, int start, int end) {
/* 208 */     for (int i = end - 1; i >= start; i--) {
/* 209 */       if (array[i] == target) {
/* 210 */         return i;
/*     */       }
/*     */     } 
/* 213 */     return -1;
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
/*     */   @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
/*     */   public static int min(int... array) {
/* 227 */     Preconditions.checkArgument((array.length > 0));
/* 228 */     int min = array[0];
/* 229 */     for (int i = 1; i < array.length; i++) {
/* 230 */       if (array[i] < min) {
/* 231 */         min = array[i];
/*     */       }
/*     */     } 
/* 234 */     return min;
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
/*     */   @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
/*     */   public static int max(int... array) {
/* 248 */     Preconditions.checkArgument((array.length > 0));
/* 249 */     int max = array[0];
/* 250 */     for (int i = 1; i < array.length; i++) {
/* 251 */       if (array[i] > max) {
/* 252 */         max = array[i];
/*     */       }
/*     */     } 
/* 255 */     return max;
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
/*     */   public static int constrainToRange(int value, int min, int max) {
/* 272 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 273 */     return Math.min(Math.max(value, min), max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] concat(int[]... arrays) {
/* 284 */     int length = 0;
/* 285 */     for (int[] array : arrays) {
/* 286 */       length += array.length;
/*     */     }
/* 288 */     int[] result = new int[length];
/* 289 */     int pos = 0;
/* 290 */     for (int[] array : arrays) {
/* 291 */       System.arraycopy(array, 0, result, pos, array.length);
/* 292 */       pos += array.length;
/*     */     } 
/* 294 */     return result;
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
/*     */   public static byte[] toByteArray(int value) {
/* 307 */     return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
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
/*     */   public static int fromByteArray(byte[] bytes) {
/* 324 */     Preconditions.checkArgument((bytes.length >= 4), "array too small: %s < %s", bytes.length, 4);
/* 325 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
/* 335 */     return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | b4 & 0xFF;
/*     */   }
/*     */   
/*     */   private static final class IntConverter
/*     */     extends Converter<String, Integer> implements Serializable {
/* 340 */     static final Converter<String, Integer> INSTANCE = new IntConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Integer doForward(String value) {
/* 344 */       return Integer.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Integer value) {
/* 349 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 354 */       return "Ints.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 358 */       return INSTANCE;
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
/*     */   public static Converter<String, Integer> stringConverter() {
/* 376 */     return IntConverter.INSTANCE;
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
/*     */   public static int[] ensureCapacity(int[] array, int minLength, int padding) {
/* 393 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 394 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 395 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/* 407 */     Preconditions.checkNotNull(separator);
/* 408 */     if (array.length == 0) {
/* 409 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 413 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 414 */     builder.append(array[0]);
/* 415 */     for (int i = 1; i < array.length; i++) {
/* 416 */       builder.append(separator).append(array[i]);
/*     */     }
/* 418 */     return builder.toString();
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
/*     */   public static Comparator<int[]> lexicographicalComparator() {
/* 434 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<int[]> {
/* 438 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 442 */       int minLength = Math.min(left.length, right.length);
/* 443 */       for (int i = 0; i < minLength; i++) {
/* 444 */         int result = Ints.compare(left[i], right[i]);
/* 445 */         if (result != 0) {
/* 446 */           return result;
/*     */         }
/*     */       } 
/* 449 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 454 */       return "Ints.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array) {
/* 464 */     Preconditions.checkNotNull(array);
/* 465 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array, int fromIndex, int toIndex) {
/* 475 */     Preconditions.checkNotNull(array);
/* 476 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 477 */     Arrays.sort(array, fromIndex, toIndex);
/* 478 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(int[] array) {
/* 488 */     Preconditions.checkNotNull(array);
/* 489 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(int[] array, int fromIndex, int toIndex) {
/* 503 */     Preconditions.checkNotNull(array);
/* 504 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 505 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 506 */       int tmp = array[i];
/* 507 */       array[i] = array[j];
/* 508 */       array[j] = tmp;
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
/*     */   public static void rotate(int[] array, int distance) {
/* 523 */     rotate(array, distance, 0, array.length);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void rotate(int[] array, int distance, int fromIndex, int toIndex) {
/* 566 */     Preconditions.checkNotNull(array);
/* 567 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 568 */     if (array.length <= 1) {
/*     */       return;
/*     */     }
/*     */     
/* 572 */     int length = toIndex - fromIndex;
/*     */ 
/*     */     
/* 575 */     int m = -distance % length;
/* 576 */     m = (m < 0) ? (m + length) : m;
/*     */     
/* 578 */     int newFirstIndex = m + fromIndex;
/* 579 */     if (newFirstIndex == fromIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 583 */     reverse(array, fromIndex, newFirstIndex);
/* 584 */     reverse(array, newFirstIndex, toIndex);
/* 585 */     reverse(array, fromIndex, toIndex);
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
/*     */   public static int[] toArray(Collection<? extends Number> collection) {
/* 602 */     if (collection instanceof IntArrayAsList) {
/* 603 */       return ((IntArrayAsList)collection).toIntArray();
/*     */     }
/*     */     
/* 606 */     Object[] boxedArray = collection.toArray();
/* 607 */     int len = boxedArray.length;
/* 608 */     int[] array = new int[len];
/* 609 */     for (int i = 0; i < len; i++)
/*     */     {
/* 611 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).intValue();
/*     */     }
/* 613 */     return array;
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
/*     */   public static List<Integer> asList(int... backingArray) {
/* 634 */     if (backingArray.length == 0) {
/* 635 */       return Collections.emptyList();
/*     */     }
/* 637 */     return new IntArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
/*     */     final int[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     IntArrayAsList(int[] array) {
/* 648 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     IntArrayAsList(int[] array, int start, int end) {
/* 652 */       this.array = array;
/* 653 */       this.start = start;
/* 654 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 659 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 664 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer get(int index) {
/* 669 */       Preconditions.checkElementIndex(index, size());
/* 670 */       return Integer.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfInt spliterator() {
/* 675 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object target) {
/* 681 */       return (target instanceof Integer && Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object target) {
/* 687 */       if (target instanceof Integer) {
/* 688 */         int i = Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 689 */         if (i >= 0) {
/* 690 */           return i - this.start;
/*     */         }
/*     */       } 
/* 693 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object target) {
/* 699 */       if (target instanceof Integer) {
/* 700 */         int i = Ints.lastIndexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 701 */         if (i >= 0) {
/* 702 */           return i - this.start;
/*     */         }
/*     */       } 
/* 705 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer set(int index, Integer element) {
/* 710 */       Preconditions.checkElementIndex(index, size());
/* 711 */       int oldValue = this.array[this.start + index];
/*     */       
/* 713 */       this.array[this.start + index] = ((Integer)Preconditions.checkNotNull(element)).intValue();
/* 714 */       return Integer.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Integer> subList(int fromIndex, int toIndex) {
/* 719 */       int size = size();
/* 720 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 721 */       if (fromIndex == toIndex) {
/* 722 */         return Collections.emptyList();
/*     */       }
/* 724 */       return new IntArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 729 */       if (object == this) {
/* 730 */         return true;
/*     */       }
/* 732 */       if (object instanceof IntArrayAsList) {
/* 733 */         IntArrayAsList that = (IntArrayAsList)object;
/* 734 */         int size = size();
/* 735 */         if (that.size() != size) {
/* 736 */           return false;
/*     */         }
/* 738 */         for (int i = 0; i < size; i++) {
/* 739 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 740 */             return false;
/*     */           }
/*     */         } 
/* 743 */         return true;
/*     */       } 
/* 745 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 750 */       int result = 1;
/* 751 */       for (int i = this.start; i < this.end; i++) {
/* 752 */         result = 31 * result + Ints.hashCode(this.array[i]);
/*     */       }
/* 754 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 759 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 760 */       builder.append('[').append(this.array[this.start]);
/* 761 */       for (int i = this.start + 1; i < this.end; i++) {
/* 762 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 764 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     int[] toIntArray() {
/* 768 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @CheckForNull
/*     */   public static Integer tryParse(String string) {
/* 793 */     return tryParse(string, 10);
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
/*     */   public static Integer tryParse(String string, int radix) {
/* 818 */     Long result = Longs.tryParse(string, radix);
/* 819 */     if (result == null || result.longValue() != result.intValue()) {
/* 820 */       return null;
/*     */     }
/* 822 */     return Integer.valueOf(result.intValue());
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Ints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */