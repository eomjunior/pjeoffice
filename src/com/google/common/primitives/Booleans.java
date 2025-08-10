/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class Booleans
/*     */ {
/*     */   private enum BooleanComparator
/*     */     implements Comparator<Boolean>
/*     */   {
/*  50 */     TRUE_FIRST(1, "Booleans.trueFirst()"),
/*  51 */     FALSE_FIRST(-1, "Booleans.falseFirst()");
/*     */     
/*     */     private final int trueValue;
/*     */     private final String toString;
/*     */     
/*     */     BooleanComparator(int trueValue, String toString) {
/*  57 */       this.trueValue = trueValue;
/*  58 */       this.toString = toString;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(Boolean a, Boolean b) {
/*  63 */       int aVal = a.booleanValue() ? this.trueValue : 0;
/*  64 */       int bVal = b.booleanValue() ? this.trueValue : 0;
/*  65 */       return bVal - aVal;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  70 */       return this.toString;
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
/*     */   public static Comparator<Boolean> trueFirst() {
/*  83 */     return BooleanComparator.TRUE_FIRST;
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
/*     */   public static Comparator<Boolean> falseFirst() {
/*  95 */     return BooleanComparator.FALSE_FIRST;
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
/*     */   public static int hashCode(boolean value) {
/* 108 */     return value ? 1231 : 1237;
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
/*     */   public static int compare(boolean a, boolean b) {
/* 125 */     return (a == b) ? 0 : (a ? 1 : -1);
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
/*     */   public static boolean contains(boolean[] array, boolean target) {
/* 140 */     for (boolean value : array) {
/* 141 */       if (value == target) {
/* 142 */         return true;
/*     */       }
/*     */     } 
/* 145 */     return false;
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
/*     */   public static int indexOf(boolean[] array, boolean target) {
/* 160 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(boolean[] array, boolean target, int start, int end) {
/* 165 */     for (int i = start; i < end; i++) {
/* 166 */       if (array[i] == target) {
/* 167 */         return i;
/*     */       }
/*     */     } 
/* 170 */     return -1;
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
/*     */   public static int indexOf(boolean[] array, boolean[] target) {
/* 184 */     Preconditions.checkNotNull(array, "array");
/* 185 */     Preconditions.checkNotNull(target, "target");
/* 186 */     if (target.length == 0) {
/* 187 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 191 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 192 */       int j = 0; while (true) { if (j < target.length) {
/* 193 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 197 */         return i; }
/*     */     
/* 199 */     }  return -1;
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
/*     */   public static int lastIndexOf(boolean[] array, boolean target) {
/* 211 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(boolean[] array, boolean target, int start, int end) {
/* 216 */     for (int i = end - 1; i >= start; i--) {
/* 217 */       if (array[i] == target) {
/* 218 */         return i;
/*     */       }
/*     */     } 
/* 221 */     return -1;
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
/*     */   public static boolean[] concat(boolean[]... arrays) {
/* 233 */     int length = 0;
/* 234 */     for (boolean[] array : arrays) {
/* 235 */       length += array.length;
/*     */     }
/* 237 */     boolean[] result = new boolean[length];
/* 238 */     int pos = 0;
/* 239 */     for (boolean[] array : arrays) {
/* 240 */       System.arraycopy(array, 0, result, pos, array.length);
/* 241 */       pos += array.length;
/*     */     } 
/* 243 */     return result;
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
/*     */   public static boolean[] ensureCapacity(boolean[] array, int minLength, int padding) {
/* 260 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 261 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 262 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, boolean... array) {
/* 275 */     Preconditions.checkNotNull(separator);
/* 276 */     if (array.length == 0) {
/* 277 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 281 */     StringBuilder builder = new StringBuilder(array.length * 7);
/* 282 */     builder.append(array[0]);
/* 283 */     for (int i = 1; i < array.length; i++) {
/* 284 */       builder.append(separator).append(array[i]);
/*     */     }
/* 286 */     return builder.toString();
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
/*     */   public static Comparator<boolean[]> lexicographicalComparator() {
/* 303 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<boolean[]> {
/* 307 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(boolean[] left, boolean[] right) {
/* 311 */       int minLength = Math.min(left.length, right.length);
/* 312 */       for (int i = 0; i < minLength; i++) {
/* 313 */         int result = Booleans.compare(left[i], right[i]);
/* 314 */         if (result != 0) {
/* 315 */           return result;
/*     */         }
/*     */       } 
/* 318 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 323 */       return "Booleans.lexicographicalComparator()";
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
/*     */   public static boolean[] toArray(Collection<Boolean> collection) {
/* 342 */     if (collection instanceof BooleanArrayAsList) {
/* 343 */       return ((BooleanArrayAsList)collection).toBooleanArray();
/*     */     }
/*     */     
/* 346 */     Object[] boxedArray = collection.toArray();
/* 347 */     int len = boxedArray.length;
/* 348 */     boolean[] array = new boolean[len];
/* 349 */     for (int i = 0; i < len; i++)
/*     */     {
/* 351 */       array[i] = ((Boolean)Preconditions.checkNotNull(boxedArray[i])).booleanValue();
/*     */     }
/* 353 */     return array;
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
/*     */   public static List<Boolean> asList(boolean... backingArray) {
/* 370 */     if (backingArray.length == 0) {
/* 371 */       return Collections.emptyList();
/*     */     }
/* 373 */     return new BooleanArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable {
/*     */     final boolean[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BooleanArrayAsList(boolean[] array) {
/* 384 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     BooleanArrayAsList(boolean[] array, int start, int end) {
/* 388 */       this.array = array;
/* 389 */       this.start = start;
/* 390 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 395 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 400 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean get(int index) {
/* 405 */       Preconditions.checkElementIndex(index, size());
/* 406 */       return Boolean.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object target) {
/* 412 */       return (target instanceof Boolean && Booleans
/* 413 */         .indexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object target) {
/* 419 */       if (target instanceof Boolean) {
/* 420 */         int i = Booleans.indexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end);
/* 421 */         if (i >= 0) {
/* 422 */           return i - this.start;
/*     */         }
/*     */       } 
/* 425 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object target) {
/* 431 */       if (target instanceof Boolean) {
/* 432 */         int i = Booleans.lastIndexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end);
/* 433 */         if (i >= 0) {
/* 434 */           return i - this.start;
/*     */         }
/*     */       } 
/* 437 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean set(int index, Boolean element) {
/* 442 */       Preconditions.checkElementIndex(index, size());
/* 443 */       boolean oldValue = this.array[this.start + index];
/*     */       
/* 445 */       this.array[this.start + index] = ((Boolean)Preconditions.checkNotNull(element)).booleanValue();
/* 446 */       return Boolean.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Boolean> subList(int fromIndex, int toIndex) {
/* 451 */       int size = size();
/* 452 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 453 */       if (fromIndex == toIndex) {
/* 454 */         return Collections.emptyList();
/*     */       }
/* 456 */       return new BooleanArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 461 */       if (object == this) {
/* 462 */         return true;
/*     */       }
/* 464 */       if (object instanceof BooleanArrayAsList) {
/* 465 */         BooleanArrayAsList that = (BooleanArrayAsList)object;
/* 466 */         int size = size();
/* 467 */         if (that.size() != size) {
/* 468 */           return false;
/*     */         }
/* 470 */         for (int i = 0; i < size; i++) {
/* 471 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 472 */             return false;
/*     */           }
/*     */         } 
/* 475 */         return true;
/*     */       } 
/* 477 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 482 */       int result = 1;
/* 483 */       for (int i = this.start; i < this.end; i++) {
/* 484 */         result = 31 * result + Booleans.hashCode(this.array[i]);
/*     */       }
/* 486 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 491 */       StringBuilder builder = new StringBuilder(size() * 7);
/* 492 */       builder.append(this.array[this.start] ? "[true" : "[false");
/* 493 */       for (int i = this.start + 1; i < this.end; i++) {
/* 494 */         builder.append(this.array[i] ? ", true" : ", false");
/*     */       }
/* 496 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     boolean[] toBooleanArray() {
/* 500 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   public static int countTrue(boolean... values) {
/* 512 */     int count = 0;
/* 513 */     for (boolean value : values) {
/* 514 */       if (value) {
/* 515 */         count++;
/*     */       }
/*     */     } 
/* 518 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(boolean[] array) {
/* 528 */     Preconditions.checkNotNull(array);
/* 529 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(boolean[] array, int fromIndex, int toIndex) {
/* 543 */     Preconditions.checkNotNull(array);
/* 544 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 545 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 546 */       boolean tmp = array[i];
/* 547 */       array[i] = array[j];
/* 548 */       array[j] = tmp;
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
/*     */   public static void rotate(boolean[] array, int distance) {
/* 563 */     rotate(array, distance, 0, array.length);
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
/*     */   public static void rotate(boolean[] array, int distance, int fromIndex, int toIndex) {
/* 580 */     Preconditions.checkNotNull(array);
/* 581 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 582 */     if (array.length <= 1) {
/*     */       return;
/*     */     }
/*     */     
/* 586 */     int length = toIndex - fromIndex;
/*     */ 
/*     */     
/* 589 */     int m = -distance % length;
/* 590 */     m = (m < 0) ? (m + length) : m;
/*     */     
/* 592 */     int newFirstIndex = m + fromIndex;
/* 593 */     if (newFirstIndex == fromIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 597 */     reverse(array, fromIndex, newFirstIndex);
/* 598 */     reverse(array, newFirstIndex, toIndex);
/* 599 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Booleans.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */