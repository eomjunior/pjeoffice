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
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.regex.Pattern;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Doubles
/*     */   extends DoublesMethodsForWeb
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   
/*     */   public static int hashCode(double value) {
/*  75 */     return Double.valueOf(value).hashCode();
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
/*     */   public static int compare(double a, double b) {
/*  96 */     return Double.compare(a, b);
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
/*     */   public static boolean isFinite(double value) {
/* 108 */     return (Double.NEGATIVE_INFINITY < value && value < Double.POSITIVE_INFINITY);
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
/*     */   public static boolean contains(double[] array, double target) {
/* 120 */     for (double value : array) {
/* 121 */       if (value == target) {
/* 122 */         return true;
/*     */       }
/*     */     } 
/* 125 */     return false;
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
/*     */   public static int indexOf(double[] array, double target) {
/* 138 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(double[] array, double target, int start, int end) {
/* 143 */     for (int i = start; i < end; i++) {
/* 144 */       if (array[i] == target) {
/* 145 */         return i;
/*     */       }
/*     */     } 
/* 148 */     return -1;
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
/*     */   public static int indexOf(double[] array, double[] target) {
/* 164 */     Preconditions.checkNotNull(array, "array");
/* 165 */     Preconditions.checkNotNull(target, "target");
/* 166 */     if (target.length == 0) {
/* 167 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 171 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 172 */       int j = 0; while (true) { if (j < target.length) {
/* 173 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 177 */         return i; }
/*     */     
/* 179 */     }  return -1;
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
/*     */   public static int lastIndexOf(double[] array, double target) {
/* 192 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(double[] array, double target, int start, int end) {
/* 197 */     for (int i = end - 1; i >= start; i--) {
/* 198 */       if (array[i] == target) {
/* 199 */         return i;
/*     */       }
/*     */     } 
/* 202 */     return -1;
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
/*     */   public static double min(double... array) {
/* 217 */     Preconditions.checkArgument((array.length > 0));
/* 218 */     double min = array[0];
/* 219 */     for (int i = 1; i < array.length; i++) {
/* 220 */       min = Math.min(min, array[i]);
/*     */     }
/* 222 */     return min;
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
/*     */   public static double max(double... array) {
/* 237 */     Preconditions.checkArgument((array.length > 0));
/* 238 */     double max = array[0];
/* 239 */     for (int i = 1; i < array.length; i++) {
/* 240 */       max = Math.max(max, array[i]);
/*     */     }
/* 242 */     return max;
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
/*     */   public static double constrainToRange(double value, double min, double max) {
/* 261 */     if (min <= max) {
/* 262 */       return Math.min(Math.max(value, min), max);
/*     */     }
/* 264 */     throw new IllegalArgumentException(
/* 265 */         Strings.lenientFormat("min (%s) must be less than or equal to max (%s)", new Object[] { Double.valueOf(min), Double.valueOf(max) }));
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
/*     */   public static double[] concat(double[]... arrays) {
/* 277 */     int length = 0;
/* 278 */     for (double[] array : arrays) {
/* 279 */       length += array.length;
/*     */     }
/* 281 */     double[] result = new double[length];
/* 282 */     int pos = 0;
/* 283 */     for (double[] array : arrays) {
/* 284 */       System.arraycopy(array, 0, result, pos, array.length);
/* 285 */       pos += array.length;
/*     */     } 
/* 287 */     return result;
/*     */   }
/*     */   
/*     */   private static final class DoubleConverter
/*     */     extends Converter<String, Double> implements Serializable {
/* 292 */     static final Converter<String, Double> INSTANCE = new DoubleConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Double doForward(String value) {
/* 296 */       return Double.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Double value) {
/* 301 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 306 */       return "Doubles.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 310 */       return INSTANCE;
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
/*     */   public static Converter<String, Double> stringConverter() {
/* 323 */     return DoubleConverter.INSTANCE;
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
/*     */   public static double[] ensureCapacity(double[] array, int minLength, int padding) {
/* 340 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 341 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 342 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, double... array) {
/* 358 */     Preconditions.checkNotNull(separator);
/* 359 */     if (array.length == 0) {
/* 360 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 364 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 365 */     builder.append(array[0]);
/* 366 */     for (int i = 1; i < array.length; i++) {
/* 367 */       builder.append(separator).append(array[i]);
/*     */     }
/* 369 */     return builder.toString();
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
/*     */   public static Comparator<double[]> lexicographicalComparator() {
/* 386 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<double[]> {
/* 390 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(double[] left, double[] right) {
/* 394 */       int minLength = Math.min(left.length, right.length);
/* 395 */       for (int i = 0; i < minLength; i++) {
/* 396 */         int result = Double.compare(left[i], right[i]);
/* 397 */         if (result != 0) {
/* 398 */           return result;
/*     */         }
/*     */       } 
/* 401 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 406 */       return "Doubles.lexicographicalComparator()";
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
/*     */   public static void sortDescending(double[] array) {
/* 419 */     Preconditions.checkNotNull(array);
/* 420 */     sortDescending(array, 0, array.length);
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
/*     */   public static void sortDescending(double[] array, int fromIndex, int toIndex) {
/* 433 */     Preconditions.checkNotNull(array);
/* 434 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 435 */     Arrays.sort(array, fromIndex, toIndex);
/* 436 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(double[] array) {
/* 446 */     Preconditions.checkNotNull(array);
/* 447 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(double[] array, int fromIndex, int toIndex) {
/* 461 */     Preconditions.checkNotNull(array);
/* 462 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 463 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 464 */       double tmp = array[i];
/* 465 */       array[i] = array[j];
/* 466 */       array[j] = tmp;
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
/*     */   public static void rotate(double[] array, int distance) {
/* 481 */     rotate(array, distance, 0, array.length);
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
/*     */   public static void rotate(double[] array, int distance, int fromIndex, int toIndex) {
/* 498 */     Preconditions.checkNotNull(array);
/* 499 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 500 */     if (array.length <= 1) {
/*     */       return;
/*     */     }
/*     */     
/* 504 */     int length = toIndex - fromIndex;
/*     */ 
/*     */     
/* 507 */     int m = -distance % length;
/* 508 */     m = (m < 0) ? (m + length) : m;
/*     */     
/* 510 */     int newFirstIndex = m + fromIndex;
/* 511 */     if (newFirstIndex == fromIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 515 */     reverse(array, fromIndex, newFirstIndex);
/* 516 */     reverse(array, newFirstIndex, toIndex);
/* 517 */     reverse(array, fromIndex, toIndex);
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
/*     */   public static double[] toArray(Collection<? extends Number> collection) {
/* 534 */     if (collection instanceof DoubleArrayAsList) {
/* 535 */       return ((DoubleArrayAsList)collection).toDoubleArray();
/*     */     }
/*     */     
/* 538 */     Object[] boxedArray = collection.toArray();
/* 539 */     int len = boxedArray.length;
/* 540 */     double[] array = new double[len];
/* 541 */     for (int i = 0; i < len; i++)
/*     */     {
/* 543 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).doubleValue();
/*     */     }
/* 545 */     return array;
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
/*     */   public static List<Double> asList(double... backingArray) {
/* 569 */     if (backingArray.length == 0) {
/* 570 */       return Collections.emptyList();
/*     */     }
/* 572 */     return new DoubleArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
/*     */     final double[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     DoubleArrayAsList(double[] array) {
/* 583 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     DoubleArrayAsList(double[] array, int start, int end) {
/* 587 */       this.array = array;
/* 588 */       this.start = start;
/* 589 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 594 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 599 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double get(int index) {
/* 604 */       Preconditions.checkElementIndex(index, size());
/* 605 */       return Double.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator.OfDouble spliterator() {
/* 610 */       return Spliterators.spliterator(this.array, this.start, this.end, 0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object target) {
/* 616 */       return (target instanceof Double && Doubles
/* 617 */         .indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object target) {
/* 623 */       if (target instanceof Double) {
/* 624 */         int i = Doubles.indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 625 */         if (i >= 0) {
/* 626 */           return i - this.start;
/*     */         }
/*     */       } 
/* 629 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object target) {
/* 635 */       if (target instanceof Double) {
/* 636 */         int i = Doubles.lastIndexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 637 */         if (i >= 0) {
/* 638 */           return i - this.start;
/*     */         }
/*     */       } 
/* 641 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double set(int index, Double element) {
/* 646 */       Preconditions.checkElementIndex(index, size());
/* 647 */       double oldValue = this.array[this.start + index];
/*     */       
/* 649 */       this.array[this.start + index] = ((Double)Preconditions.checkNotNull(element)).doubleValue();
/* 650 */       return Double.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Double> subList(int fromIndex, int toIndex) {
/* 655 */       int size = size();
/* 656 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 657 */       if (fromIndex == toIndex) {
/* 658 */         return Collections.emptyList();
/*     */       }
/* 660 */       return new DoubleArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 665 */       if (object == this) {
/* 666 */         return true;
/*     */       }
/* 668 */       if (object instanceof DoubleArrayAsList) {
/* 669 */         DoubleArrayAsList that = (DoubleArrayAsList)object;
/* 670 */         int size = size();
/* 671 */         if (that.size() != size) {
/* 672 */           return false;
/*     */         }
/* 674 */         for (int i = 0; i < size; i++) {
/* 675 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 676 */             return false;
/*     */           }
/*     */         } 
/* 679 */         return true;
/*     */       } 
/* 681 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 686 */       int result = 1;
/* 687 */       for (int i = this.start; i < this.end; i++) {
/* 688 */         result = 31 * result + Doubles.hashCode(this.array[i]);
/*     */       }
/* 690 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 695 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 696 */       builder.append('[').append(this.array[this.start]);
/* 697 */       for (int i = this.start + 1; i < this.end; i++) {
/* 698 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 700 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     double[] toDoubleArray() {
/* 704 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/* 718 */   static final Pattern FLOATING_POINT_PATTERN = fpPattern();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Pattern fpPattern() {
/* 729 */     String decimal = "(?:\\d+#(?:\\.\\d*#)?|\\.\\d+#)";
/* 730 */     String completeDec = decimal + "(?:[eE][+-]?\\d+#)?[fFdD]?";
/* 731 */     String hex = "(?:[0-9a-fA-F]+#(?:\\.[0-9a-fA-F]*#)?|\\.[0-9a-fA-F]+#)";
/* 732 */     String completeHex = "0[xX]" + hex + "[pP][+-]?\\d+#[fFdD]?";
/* 733 */     String fpPattern = "[+-]?(?:NaN|Infinity|" + completeDec + "|" + completeHex + ")";
/*     */     
/* 735 */     fpPattern = fpPattern.replace("#", "+");
/*     */ 
/*     */ 
/*     */     
/* 739 */     return 
/*     */       
/* 741 */       Pattern.compile(fpPattern);
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
/*     */   @J2ktIncompatible
/*     */   @GwtIncompatible
/*     */   public static Double tryParse(String string) {
/* 765 */     if (FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 769 */         return Double.valueOf(Double.parseDouble(string));
/* 770 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 775 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Doubles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */