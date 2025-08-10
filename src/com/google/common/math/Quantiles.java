/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @J2ktIncompatible
/*     */ @GwtIncompatible
/*     */ public final class Quantiles
/*     */ {
/*     */   public static ScaleAndIndex median() {
/* 136 */     return scale(2).index(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Scale quartiles() {
/* 141 */     return scale(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Scale percentiles() {
/* 146 */     return scale(100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scale scale(int scale) {
/* 156 */     return new Scale(scale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Scale
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Scale(int scale) {
/* 170 */       Preconditions.checkArgument((scale > 0), "Quantile scale must be positive");
/* 171 */       this.scale = scale;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndex index(int index) {
/* 180 */       return new Quantiles.ScaleAndIndex(this.scale, index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndexes indexes(int... indexes) {
/* 193 */       return new Quantiles.ScaleAndIndexes(this.scale, (int[])indexes.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndexes indexes(Collection<Integer> indexes) {
/* 206 */       return new Quantiles.ScaleAndIndexes(this.scale, Ints.toArray(indexes));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ScaleAndIndex
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */     
/*     */     private final int index;
/*     */ 
/*     */ 
/*     */     
/*     */     private ScaleAndIndex(int scale, int index) {
/* 222 */       Quantiles.checkIndex(index, scale);
/* 223 */       this.scale = scale;
/* 224 */       this.index = index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(Collection<? extends Number> dataset) {
/* 236 */       return computeInPlace(Doubles.toArray(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(double... dataset) {
/* 247 */       return computeInPlace((double[])dataset.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(long... dataset) {
/* 259 */       return computeInPlace(Quantiles.longsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(int... dataset) {
/* 270 */       return computeInPlace(Quantiles.intsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double computeInPlace(double... dataset) {
/* 281 */       Preconditions.checkArgument((dataset.length > 0), "Cannot calculate quantiles of an empty dataset");
/* 282 */       if (Quantiles.containsNaN(dataset)) {
/* 283 */         return Double.NaN;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 293 */       long numerator = this.index * (dataset.length - 1);
/*     */ 
/*     */ 
/*     */       
/* 297 */       int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
/* 298 */       int remainder = (int)(numerator - quotient * this.scale);
/* 299 */       Quantiles.selectInPlace(quotient, dataset, 0, dataset.length - 1);
/* 300 */       if (remainder == 0) {
/* 301 */         return dataset[quotient];
/*     */       }
/* 303 */       Quantiles.selectInPlace(quotient + 1, dataset, quotient + 1, dataset.length - 1);
/* 304 */       return Quantiles.interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ScaleAndIndexes
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */     
/*     */     private final int[] indexes;
/*     */ 
/*     */ 
/*     */     
/*     */     private ScaleAndIndexes(int scale, int[] indexes) {
/* 321 */       for (int index : indexes) {
/* 322 */         Quantiles.checkIndex(index, scale);
/*     */       }
/* 324 */       Preconditions.checkArgument((indexes.length > 0), "Indexes must be a non empty array");
/* 325 */       this.scale = scale;
/* 326 */       this.indexes = indexes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(Collection<? extends Number> dataset) {
/* 341 */       return computeInPlace(Doubles.toArray(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(double... dataset) {
/* 355 */       return computeInPlace((double[])dataset.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(long... dataset) {
/* 370 */       return computeInPlace(Quantiles.longsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(int... dataset) {
/* 384 */       return computeInPlace(Quantiles.intsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> computeInPlace(double... dataset) {
/* 398 */       Preconditions.checkArgument((dataset.length > 0), "Cannot calculate quantiles of an empty dataset");
/* 399 */       if (Quantiles.containsNaN(dataset)) {
/* 400 */         Map<Integer, Double> nanMap = new LinkedHashMap<>();
/* 401 */         for (int index : this.indexes) {
/* 402 */           nanMap.put(Integer.valueOf(index), Double.valueOf(Double.NaN));
/*     */         }
/* 404 */         return Collections.unmodifiableMap(nanMap);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 412 */       int[] quotients = new int[this.indexes.length];
/* 413 */       int[] remainders = new int[this.indexes.length];
/*     */       
/* 415 */       int[] requiredSelections = new int[this.indexes.length * 2];
/* 416 */       int requiredSelectionsCount = 0;
/* 417 */       for (int i = 0; i < this.indexes.length; i++) {
/*     */ 
/*     */         
/* 420 */         long numerator = this.indexes[i] * (dataset.length - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 425 */         int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
/* 426 */         int remainder = (int)(numerator - quotient * this.scale);
/* 427 */         quotients[i] = quotient;
/* 428 */         remainders[i] = remainder;
/* 429 */         requiredSelections[requiredSelectionsCount] = quotient;
/* 430 */         requiredSelectionsCount++;
/* 431 */         if (remainder != 0) {
/* 432 */           requiredSelections[requiredSelectionsCount] = quotient + 1;
/* 433 */           requiredSelectionsCount++;
/*     */         } 
/*     */       } 
/* 436 */       Arrays.sort(requiredSelections, 0, requiredSelectionsCount);
/* 437 */       Quantiles.selectAllInPlace(requiredSelections, 0, requiredSelectionsCount - 1, dataset, 0, dataset.length - 1);
/*     */       
/* 439 */       Map<Integer, Double> ret = new LinkedHashMap<>();
/* 440 */       for (int j = 0; j < this.indexes.length; j++) {
/* 441 */         int quotient = quotients[j];
/* 442 */         int remainder = remainders[j];
/* 443 */         if (remainder == 0) {
/* 444 */           ret.put(Integer.valueOf(this.indexes[j]), Double.valueOf(dataset[quotient]));
/*     */         } else {
/* 446 */           ret.put(
/* 447 */               Integer.valueOf(this.indexes[j]), Double.valueOf(Quantiles.interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale)));
/*     */         } 
/*     */       } 
/* 450 */       return Collections.unmodifiableMap(ret);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean containsNaN(double... dataset) {
/* 456 */     for (double value : dataset) {
/* 457 */       if (Double.isNaN(value)) {
/* 458 */         return true;
/*     */       }
/*     */     } 
/* 461 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double interpolate(double lower, double upper, double remainder, double scale) {
/* 470 */     if (lower == Double.NEGATIVE_INFINITY) {
/* 471 */       if (upper == Double.POSITIVE_INFINITY)
/*     */       {
/* 473 */         return Double.NaN;
/*     */       }
/*     */       
/* 476 */       return Double.NEGATIVE_INFINITY;
/*     */     } 
/* 478 */     if (upper == Double.POSITIVE_INFINITY)
/*     */     {
/* 480 */       return Double.POSITIVE_INFINITY;
/*     */     }
/* 482 */     return lower + (upper - lower) * remainder / scale;
/*     */   }
/*     */   
/*     */   private static void checkIndex(int index, int scale) {
/* 486 */     if (index < 0 || index > scale) {
/* 487 */       throw new IllegalArgumentException("Quantile indexes must be between 0 and the scale, which is " + scale);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static double[] longsToDoubles(long[] longs) {
/* 493 */     int len = longs.length;
/* 494 */     double[] doubles = new double[len];
/* 495 */     for (int i = 0; i < len; i++) {
/* 496 */       doubles[i] = longs[i];
/*     */     }
/* 498 */     return doubles;
/*     */   }
/*     */   
/*     */   private static double[] intsToDoubles(int[] ints) {
/* 502 */     int len = ints.length;
/* 503 */     double[] doubles = new double[len];
/* 504 */     for (int i = 0; i < len; i++) {
/* 505 */       doubles[i] = ints[i];
/*     */     }
/* 507 */     return doubles;
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
/*     */   private static void selectInPlace(int required, double[] array, int from, int to) {
/* 534 */     if (required == from) {
/* 535 */       int min = from;
/* 536 */       for (int index = from + 1; index <= to; index++) {
/* 537 */         if (array[min] > array[index]) {
/* 538 */           min = index;
/*     */         }
/*     */       } 
/* 541 */       if (min != from) {
/* 542 */         swap(array, min, from);
/*     */       }
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 549 */     while (to > from) {
/* 550 */       int partitionPoint = partition(array, from, to);
/* 551 */       if (partitionPoint >= required) {
/* 552 */         to = partitionPoint - 1;
/*     */       }
/* 554 */       if (partitionPoint <= required) {
/* 555 */         from = partitionPoint + 1;
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
/*     */   private static int partition(double[] array, int from, int to) {
/* 570 */     movePivotToStartOfSlice(array, from, to);
/* 571 */     double pivot = array[from];
/*     */ 
/*     */ 
/*     */     
/* 575 */     int partitionPoint = to;
/* 576 */     for (int i = to; i > from; i--) {
/* 577 */       if (array[i] > pivot) {
/* 578 */         swap(array, partitionPoint, i);
/* 579 */         partitionPoint--;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 586 */     swap(array, from, partitionPoint);
/* 587 */     return partitionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void movePivotToStartOfSlice(double[] array, int from, int to) {
/* 597 */     int mid = from + to >>> 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 602 */     boolean toLessThanMid = (array[to] < array[mid]);
/* 603 */     boolean midLessThanFrom = (array[mid] < array[from]);
/* 604 */     boolean toLessThanFrom = (array[to] < array[from]);
/* 605 */     if (toLessThanMid == midLessThanFrom) {
/*     */       
/* 607 */       swap(array, mid, from);
/* 608 */     } else if (toLessThanMid != toLessThanFrom) {
/*     */       
/* 610 */       swap(array, from, to);
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
/*     */   private static void selectAllInPlace(int[] allRequired, int requiredFrom, int requiredTo, double[] array, int from, int to) {
/* 623 */     int requiredChosen = chooseNextSelection(allRequired, requiredFrom, requiredTo, from, to);
/* 624 */     int required = allRequired[requiredChosen];
/*     */ 
/*     */     
/* 627 */     selectInPlace(required, array, from, to);
/*     */ 
/*     */     
/* 630 */     int requiredBelow = requiredChosen - 1;
/* 631 */     while (requiredBelow >= requiredFrom && allRequired[requiredBelow] == required) {
/* 632 */       requiredBelow--;
/*     */     }
/* 634 */     if (requiredBelow >= requiredFrom) {
/* 635 */       selectAllInPlace(allRequired, requiredFrom, requiredBelow, array, from, required - 1);
/*     */     }
/*     */ 
/*     */     
/* 639 */     int requiredAbove = requiredChosen + 1;
/* 640 */     while (requiredAbove <= requiredTo && allRequired[requiredAbove] == required) {
/* 641 */       requiredAbove++;
/*     */     }
/* 643 */     if (requiredAbove <= requiredTo) {
/* 644 */       selectAllInPlace(allRequired, requiredAbove, requiredTo, array, required + 1, to);
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
/*     */   private static int chooseNextSelection(int[] allRequired, int requiredFrom, int requiredTo, int from, int to) {
/* 659 */     if (requiredFrom == requiredTo) {
/* 660 */       return requiredFrom;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 665 */     int centerFloor = from + to >>> 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 672 */     int low = requiredFrom;
/* 673 */     int high = requiredTo;
/* 674 */     while (high > low + 1) {
/* 675 */       int mid = low + high >>> 1;
/* 676 */       if (allRequired[mid] > centerFloor) {
/* 677 */         high = mid; continue;
/* 678 */       }  if (allRequired[mid] < centerFloor) {
/* 679 */         low = mid; continue;
/*     */       } 
/* 681 */       return mid;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 686 */     if (from + to - allRequired[low] - allRequired[high] > 0) {
/* 687 */       return high;
/*     */     }
/* 689 */     return low;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void swap(double[] array, int i, int j) {
/* 695 */     double temp = array[i];
/* 696 */     array[i] = array[j];
/* 697 */     array[j] = temp;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/math/Quantiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */