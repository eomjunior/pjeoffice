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
/*     */ public final class Shorts
/*     */   extends ShortsMethodsForWeb
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   public static final short MAX_POWER_OF_TWO = 16384;
/*     */   
/*     */   public static int hashCode(short value) {
/*  74 */     return value;
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
/*     */   public static short checkedCast(long value) {
/*  86 */     short result = (short)(int)value;
/*  87 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  88 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short saturatedCast(long value) {
/*  99 */     if (value > 32767L) {
/* 100 */       return Short.MAX_VALUE;
/*     */     }
/* 102 */     if (value < -32768L) {
/* 103 */       return Short.MIN_VALUE;
/*     */     }
/* 105 */     return (short)(int)value;
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
/*     */   public static int compare(short a, short b) {
/* 121 */     return a - b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(short[] array, short target) {
/* 132 */     for (short value : array) {
/* 133 */       if (value == target) {
/* 134 */         return true;
/*     */       }
/*     */     } 
/* 137 */     return false;
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
/*     */   public static int indexOf(short[] array, short target) {
/* 149 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(short[] array, short target, int start, int end) {
/* 154 */     for (int i = start; i < end; i++) {
/* 155 */       if (array[i] == target) {
/* 156 */         return i;
/*     */       }
/*     */     } 
/* 159 */     return -1;
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
/*     */   public static int indexOf(short[] array, short[] target) {
/* 173 */     Preconditions.checkNotNull(array, "array");
/* 174 */     Preconditions.checkNotNull(target, "target");
/* 175 */     if (target.length == 0) {
/* 176 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 180 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 181 */       int j = 0; while (true) { if (j < target.length) {
/* 182 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 186 */         return i; }
/*     */     
/* 188 */     }  return -1;
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
/*     */   public static int lastIndexOf(short[] array, short target) {
/* 200 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(short[] array, short target, int start, int end) {
/* 205 */     for (int i = end - 1; i >= start; i--) {
/* 206 */       if (array[i] == target) {
/* 207 */         return i;
/*     */       }
/*     */     } 
/* 210 */     return -1;
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
/*     */   public static short min(short... array) {
/* 224 */     Preconditions.checkArgument((array.length > 0));
/* 225 */     short min = array[0];
/* 226 */     for (int i = 1; i < array.length; i++) {
/* 227 */       if (array[i] < min) {
/* 228 */         min = array[i];
/*     */       }
/*     */     } 
/* 231 */     return min;
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
/*     */   public static short max(short... array) {
/* 245 */     Preconditions.checkArgument((array.length > 0));
/* 246 */     short max = array[0];
/* 247 */     for (int i = 1; i < array.length; i++) {
/* 248 */       if (array[i] > max) {
/* 249 */         max = array[i];
/*     */       }
/*     */     } 
/* 252 */     return max;
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
/*     */   public static short constrainToRange(short value, short min, short max) {
/* 269 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 270 */     return (value < min) ? min : ((value < max) ? value : max);
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
/*     */   public static short[] concat(short[]... arrays) {
/* 282 */     int length = 0;
/* 283 */     for (short[] array : arrays) {
/* 284 */       length += array.length;
/*     */     }
/* 286 */     short[] result = new short[length];
/* 287 */     int pos = 0;
/* 288 */     for (short[] array : arrays) {
/* 289 */       System.arraycopy(array, 0, result, pos, array.length);
/* 290 */       pos += array.length;
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
/*     */   @GwtIncompatible
/*     */   public static byte[] toByteArray(short value) {
/* 306 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   @GwtIncompatible
/*     */   public static short fromByteArray(byte[] bytes) {
/* 321 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", bytes.length, 2);
/* 322 */     return fromBytes(bytes[0], bytes[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static short fromBytes(byte b1, byte b2) {
/* 333 */     return (short)(b1 << 8 | b2 & 0xFF);
/*     */   }
/*     */   
/*     */   private static final class ShortConverter
/*     */     extends Converter<String, Short> implements Serializable {
/* 338 */     static final Converter<String, Short> INSTANCE = new ShortConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Short doForward(String value) {
/* 342 */       return Short.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Short value) {
/* 347 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 352 */       return "Shorts.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 356 */       return INSTANCE;
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
/*     */   public static Converter<String, Short> stringConverter() {
/* 374 */     return ShortConverter.INSTANCE;
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
/*     */   public static short[] ensureCapacity(short[] array, int minLength, int padding) {
/* 391 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 392 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 393 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, short... array) {
/* 406 */     Preconditions.checkNotNull(separator);
/* 407 */     if (array.length == 0) {
/* 408 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 412 */     StringBuilder builder = new StringBuilder(array.length * 6);
/* 413 */     builder.append(array[0]);
/* 414 */     for (int i = 1; i < array.length; i++) {
/* 415 */       builder.append(separator).append(array[i]);
/*     */     }
/* 417 */     return builder.toString();
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
/*     */   public static Comparator<short[]> lexicographicalComparator() {
/* 434 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<short[]> {
/* 438 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(short[] left, short[] right) {
/* 442 */       int minLength = Math.min(left.length, right.length);
/* 443 */       for (int i = 0; i < minLength; i++) {
/* 444 */         int result = Shorts.compare(left[i], right[i]);
/* 445 */         if (result != 0) {
/* 446 */           return result;
/*     */         }
/*     */       } 
/* 449 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 454 */       return "Shorts.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(short[] array) {
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
/*     */   public static void sortDescending(short[] array, int fromIndex, int toIndex) {
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
/*     */   public static void reverse(short[] array) {
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
/*     */   public static void reverse(short[] array, int fromIndex, int toIndex) {
/* 503 */     Preconditions.checkNotNull(array);
/* 504 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 505 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 506 */       short tmp = array[i];
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
/*     */   public static void rotate(short[] array, int distance) {
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
/*     */   public static void rotate(short[] array, int distance, int fromIndex, int toIndex) {
/* 540 */     Preconditions.checkNotNull(array);
/* 541 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 542 */     if (array.length <= 1) {
/*     */       return;
/*     */     }
/*     */     
/* 546 */     int length = toIndex - fromIndex;
/*     */ 
/*     */     
/* 549 */     int m = -distance % length;
/* 550 */     m = (m < 0) ? (m + length) : m;
/*     */     
/* 552 */     int newFirstIndex = m + fromIndex;
/* 553 */     if (newFirstIndex == fromIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 557 */     reverse(array, fromIndex, newFirstIndex);
/* 558 */     reverse(array, newFirstIndex, toIndex);
/* 559 */     reverse(array, fromIndex, toIndex);
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
/*     */   public static short[] toArray(Collection<? extends Number> collection) {
/* 576 */     if (collection instanceof ShortArrayAsList) {
/* 577 */       return ((ShortArrayAsList)collection).toShortArray();
/*     */     }
/*     */     
/* 580 */     Object[] boxedArray = collection.toArray();
/* 581 */     int len = boxedArray.length;
/* 582 */     short[] array = new short[len];
/* 583 */     for (int i = 0; i < len; i++)
/*     */     {
/* 585 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).shortValue();
/*     */     }
/* 587 */     return array;
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
/*     */   public static List<Short> asList(short... backingArray) {
/* 605 */     if (backingArray.length == 0) {
/* 606 */       return Collections.emptyList();
/*     */     }
/* 608 */     return new ShortArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ShortArrayAsList extends AbstractList<Short> implements RandomAccess, Serializable {
/*     */     final short[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ShortArrayAsList(short[] array) {
/* 619 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ShortArrayAsList(short[] array, int start, int end) {
/* 623 */       this.array = array;
/* 624 */       this.start = start;
/* 625 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 630 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 635 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Short get(int index) {
/* 640 */       Preconditions.checkElementIndex(index, size());
/* 641 */       return Short.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object target) {
/* 647 */       return (target instanceof Short && Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object target) {
/* 653 */       if (target instanceof Short) {
/* 654 */         int i = Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 655 */         if (i >= 0) {
/* 656 */           return i - this.start;
/*     */         }
/*     */       } 
/* 659 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object target) {
/* 665 */       if (target instanceof Short) {
/* 666 */         int i = Shorts.lastIndexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 667 */         if (i >= 0) {
/* 668 */           return i - this.start;
/*     */         }
/*     */       } 
/* 671 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Short set(int index, Short element) {
/* 676 */       Preconditions.checkElementIndex(index, size());
/* 677 */       short oldValue = this.array[this.start + index];
/*     */       
/* 679 */       this.array[this.start + index] = ((Short)Preconditions.checkNotNull(element)).shortValue();
/* 680 */       return Short.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Short> subList(int fromIndex, int toIndex) {
/* 685 */       int size = size();
/* 686 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 687 */       if (fromIndex == toIndex) {
/* 688 */         return Collections.emptyList();
/*     */       }
/* 690 */       return new ShortArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 695 */       if (object == this) {
/* 696 */         return true;
/*     */       }
/* 698 */       if (object instanceof ShortArrayAsList) {
/* 699 */         ShortArrayAsList that = (ShortArrayAsList)object;
/* 700 */         int size = size();
/* 701 */         if (that.size() != size) {
/* 702 */           return false;
/*     */         }
/* 704 */         for (int i = 0; i < size; i++) {
/* 705 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 706 */             return false;
/*     */           }
/*     */         } 
/* 709 */         return true;
/*     */       } 
/* 711 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 716 */       int result = 1;
/* 717 */       for (int i = this.start; i < this.end; i++) {
/* 718 */         result = 31 * result + Shorts.hashCode(this.array[i]);
/*     */       }
/* 720 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 725 */       StringBuilder builder = new StringBuilder(size() * 6);
/* 726 */       builder.append('[').append(this.array[this.start]);
/* 727 */       for (int i = this.start + 1; i < this.end; i++) {
/* 728 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 730 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     short[] toShortArray() {
/* 734 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Shorts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */