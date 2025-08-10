/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Converter;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Strings;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Floats
/*     */   extends FloatsMethodsForWeb
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   
/*     */   public static int hashCode(float value) {
/*  74 */     return Float.valueOf(value).hashCode();
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
/*     */   public static int compare(float a, float b) {
/*  91 */     return Float.compare(a, b);
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
/*     */   public static boolean isFinite(float value) {
/* 103 */     return (Float.NEGATIVE_INFINITY < value && value < Float.POSITIVE_INFINITY);
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
/*     */   public static boolean contains(float[] array, float target) {
/* 115 */     for (float value : array) {
/* 116 */       if (value == target) {
/* 117 */         return true;
/*     */       }
/*     */     } 
/* 120 */     return false;
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
/*     */   public static int indexOf(float[] array, float target) {
/* 133 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(float[] array, float target, int start, int end) {
/* 138 */     for (int i = start; i < end; i++) {
/* 139 */       if (array[i] == target) {
/* 140 */         return i;
/*     */       }
/*     */     } 
/* 143 */     return -1;
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
/*     */   public static int indexOf(float[] array, float[] target) {
/* 159 */     Preconditions.checkNotNull(array, "array");
/* 160 */     Preconditions.checkNotNull(target, "target");
/* 161 */     if (target.length == 0) {
/* 162 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 166 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 167 */       int j = 0; while (true) { if (j < target.length) {
/* 168 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 172 */         return i; }
/*     */     
/* 174 */     }  return -1;
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
/*     */   public static int lastIndexOf(float[] array, float target) {
/* 187 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(float[] array, float target, int start, int end) {
/* 192 */     for (int i = end - 1; i >= start; i--) {
/* 193 */       if (array[i] == target) {
/* 194 */         return i;
/*     */       }
/*     */     } 
/* 197 */     return -1;
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
/*     */   @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
/*     */   public static float min(float... array) {
/* 212 */     Preconditions.checkArgument((array.length > 0));
/* 213 */     float min = array[0];
/* 214 */     for (int i = 1; i < array.length; i++) {
/* 215 */       min = Math.min(min, array[i]);
/*     */     }
/* 217 */     return min;
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
/*     */   @GwtIncompatible("Available in GWT! Annotation is to avoid conflict with GWT specialization of base class.")
/*     */   public static float max(float... array) {
/* 232 */     Preconditions.checkArgument((array.length > 0));
/* 233 */     float max = array[0];
/* 234 */     for (int i = 1; i < array.length; i++) {
/* 235 */       max = Math.max(max, array[i]);
/*     */     }
/* 237 */     return max;
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
/*     */   public static float constrainToRange(float value, float min, float max) {
/* 256 */     if (min <= max) {
/* 257 */       return Math.min(Math.max(value, min), max);
/*     */     }
/* 259 */     throw new IllegalArgumentException(
/* 260 */         Strings.lenientFormat("min (%s) must be less than or equal to max (%s)", new Object[] { Float.valueOf(min), Float.valueOf(max) }));
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
/*     */   public static float[] concat(float[]... arrays) {
/* 272 */     int length = 0;
/* 273 */     for (float[] array : arrays) {
/* 274 */       length += array.length;
/*     */     }
/* 276 */     float[] result = new float[length];
/* 277 */     int pos = 0;
/* 278 */     for (float[] array : arrays) {
/* 279 */       System.arraycopy(array, 0, result, pos, array.length);
/* 280 */       pos += array.length;
/*     */     } 
/* 282 */     return result;
/*     */   }
/*     */   
/*     */   private static final class FloatConverter
/*     */     extends Converter<String, Float> implements Serializable {
/* 287 */     static final Converter<String, Float> INSTANCE = new FloatConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Float doForward(String value) {
/* 291 */       return Float.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Float value) {
/* 296 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 301 */       return "Floats.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 305 */       return INSTANCE;
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
/*     */   public static Converter<String, Float> stringConverter() {
/* 318 */     return FloatConverter.INSTANCE;
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
/*     */   public static float[] ensureCapacity(float[] array, int minLength, int padding) {
/* 335 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 336 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 337 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, float... array) {
/* 353 */     Preconditions.checkNotNull(separator);
/* 354 */     if (array.length == 0) {
/* 355 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 359 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 360 */     builder.append(array[0]);
/* 361 */     for (int i = 1; i < array.length; i++) {
/* 362 */       builder.append(separator).append(array[i]);
/*     */     }
/* 364 */     return builder.toString();
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
/*     */   public static Comparator<float[]> lexicographicalComparator() {
/* 381 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<float[]> {
/* 385 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(float[] left, float[] right) {
/* 389 */       int minLength = Math.min(left.length, right.length);
/* 390 */       for (int i = 0; i < minLength; i++) {
/* 391 */         int result = Float.compare(left[i], right[i]);
/* 392 */         if (result != 0) {
/* 393 */           return result;
/*     */         }
/*     */       } 
/* 396 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 401 */       return "Floats.lexicographicalComparator()";
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
/*     */   public static void sortDescending(float[] array) {
/* 414 */     Preconditions.checkNotNull(array);
/* 415 */     sortDescending(array, 0, array.length);
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
/*     */   public static void sortDescending(float[] array, int fromIndex, int toIndex) {
/* 428 */     Preconditions.checkNotNull(array);
/* 429 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 430 */     Arrays.sort(array, fromIndex, toIndex);
/* 431 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(float[] array) {
/* 441 */     Preconditions.checkNotNull(array);
/* 442 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(float[] array, int fromIndex, int toIndex) {
/* 456 */     Preconditions.checkNotNull(array);
/* 457 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 458 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 459 */       float tmp = array[i];
/* 460 */       array[i] = array[j];
/* 461 */       array[j] = tmp;
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
/*     */   public static void rotate(float[] array, int distance) {
/* 476 */     rotate(array, distance, 0, array.length);
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
/*     */   public static void rotate(float[] array, int distance, int fromIndex, int toIndex) {
/* 493 */     Preconditions.checkNotNull(array);
/* 494 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 495 */     if (array.length <= 1) {
/*     */       return;
/*     */     }
/*     */     
/* 499 */     int length = toIndex - fromIndex;
/*     */ 
/*     */     
/* 502 */     int m = -distance % length;
/* 503 */     m = (m < 0) ? (m + length) : m;
/*     */     
/* 505 */     int newFirstIndex = m + fromIndex;
/* 506 */     if (newFirstIndex == fromIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 510 */     reverse(array, fromIndex, newFirstIndex);
/* 511 */     reverse(array, newFirstIndex, toIndex);
/* 512 */     reverse(array, fromIndex, toIndex);
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
/*     */   public static float[] toArray(Collection<? extends Number> collection) {
/* 529 */     if (collection instanceof FloatArrayAsList) {
/* 530 */       return ((FloatArrayAsList)collection).toFloatArray();
/*     */     }
/*     */     
/* 533 */     Object[] boxedArray = collection.toArray();
/* 534 */     int len = boxedArray.length;
/* 535 */     float[] array = new float[len];
/* 536 */     for (int i = 0; i < len; i++)
/*     */     {
/* 538 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).floatValue();
/*     */     }
/* 540 */     return array;
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
/*     */   public static List<Float> asList(float... backingArray) {
/* 561 */     if (backingArray.length == 0) {
/* 562 */       return Collections.emptyList();
/*     */     }
/* 564 */     return new FloatArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
/*     */     final float[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     FloatArrayAsList(float[] array) {
/* 575 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     FloatArrayAsList(float[] array, int start, int end) {
/* 579 */       this.array = array;
/* 580 */       this.start = start;
/* 581 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 586 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 591 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Float get(int index) {
/* 596 */       Preconditions.checkElementIndex(index, size());
/* 597 */       return Float.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object target) {
/* 603 */       return (target instanceof Float && Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object target) {
/* 609 */       if (target instanceof Float) {
/* 610 */         int i = Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 611 */         if (i >= 0) {
/* 612 */           return i - this.start;
/*     */         }
/*     */       } 
/* 615 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object target) {
/* 621 */       if (target instanceof Float) {
/* 622 */         int i = Floats.lastIndexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 623 */         if (i >= 0) {
/* 624 */           return i - this.start;
/*     */         }
/*     */       } 
/* 627 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Float set(int index, Float element) {
/* 632 */       Preconditions.checkElementIndex(index, size());
/* 633 */       float oldValue = this.array[this.start + index];
/*     */       
/* 635 */       this.array[this.start + index] = ((Float)Preconditions.checkNotNull(element)).floatValue();
/* 636 */       return Float.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Float> subList(int fromIndex, int toIndex) {
/* 641 */       int size = size();
/* 642 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 643 */       if (fromIndex == toIndex) {
/* 644 */         return Collections.emptyList();
/*     */       }
/* 646 */       return new FloatArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 651 */       if (object == this) {
/* 652 */         return true;
/*     */       }
/* 654 */       if (object instanceof FloatArrayAsList) {
/* 655 */         FloatArrayAsList that = (FloatArrayAsList)object;
/* 656 */         int size = size();
/* 657 */         if (that.size() != size) {
/* 658 */           return false;
/*     */         }
/* 660 */         for (int i = 0; i < size; i++) {
/* 661 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 662 */             return false;
/*     */           }
/*     */         } 
/* 665 */         return true;
/*     */       } 
/* 667 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 672 */       int result = 1;
/* 673 */       for (int i = this.start; i < this.end; i++) {
/* 674 */         result = 31 * result + Floats.hashCode(this.array[i]);
/*     */       }
/* 676 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 681 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 682 */       builder.append('[').append(this.array[this.start]);
/* 683 */       for (int i = this.start + 1; i < this.end; i++) {
/* 684 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 686 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     float[] toFloatArray() {
/* 690 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static Float tryParse(String string) {
/* 717 */     if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 721 */         return Float.valueOf(Float.parseFloat(string));
/* 722 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 727 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Floats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */