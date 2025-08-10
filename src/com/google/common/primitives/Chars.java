/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Chars
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   
/*     */   public static int hashCode(char value) {
/*  69 */     return value;
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
/*     */   public static char checkedCast(long value) {
/*  81 */     char result = (char)(int)value;
/*  82 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  83 */     return result;
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
/*     */   public static char saturatedCast(long value) {
/*  95 */     if (value > 65535L) {
/*  96 */       return Character.MAX_VALUE;
/*     */     }
/*  98 */     if (value < 0L) {
/*  99 */       return Character.MIN_VALUE;
/*     */     }
/* 101 */     return (char)(int)value;
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
/*     */   public static int compare(char a, char b) {
/* 117 */     return a - b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(char[] array, char target) {
/* 128 */     for (char value : array) {
/* 129 */       if (value == target) {
/* 130 */         return true;
/*     */       }
/*     */     } 
/* 133 */     return false;
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
/*     */   public static int indexOf(char[] array, char target) {
/* 145 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(char[] array, char target, int start, int end) {
/* 150 */     for (int i = start; i < end; i++) {
/* 151 */       if (array[i] == target) {
/* 152 */         return i;
/*     */       }
/*     */     } 
/* 155 */     return -1;
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
/*     */   public static int indexOf(char[] array, char[] target) {
/* 169 */     Preconditions.checkNotNull(array, "array");
/* 170 */     Preconditions.checkNotNull(target, "target");
/* 171 */     if (target.length == 0) {
/* 172 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 176 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 177 */       int j = 0; while (true) { if (j < target.length) {
/* 178 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 182 */         return i; }
/*     */     
/* 184 */     }  return -1;
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
/*     */   public static int lastIndexOf(char[] array, char target) {
/* 196 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(char[] array, char target, int start, int end) {
/* 201 */     for (int i = end - 1; i >= start; i--) {
/* 202 */       if (array[i] == target) {
/* 203 */         return i;
/*     */       }
/*     */     } 
/* 206 */     return -1;
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
/*     */   public static char min(char... array) {
/* 218 */     Preconditions.checkArgument((array.length > 0));
/* 219 */     char min = array[0];
/* 220 */     for (int i = 1; i < array.length; i++) {
/* 221 */       if (array[i] < min) {
/* 222 */         min = array[i];
/*     */       }
/*     */     } 
/* 225 */     return min;
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
/*     */   public static char max(char... array) {
/* 237 */     Preconditions.checkArgument((array.length > 0));
/* 238 */     char max = array[0];
/* 239 */     for (int i = 1; i < array.length; i++) {
/* 240 */       if (array[i] > max) {
/* 241 */         max = array[i];
/*     */       }
/*     */     } 
/* 244 */     return max;
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
/*     */   public static char constrainToRange(char value, char min, char max) {
/* 261 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 262 */     return (value < min) ? min : ((value < max) ? value : max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] concat(char[]... arrays) {
/* 273 */     int length = 0;
/* 274 */     for (char[] array : arrays) {
/* 275 */       length += array.length;
/*     */     }
/* 277 */     char[] result = new char[length];
/* 278 */     int pos = 0;
/* 279 */     for (char[] array : arrays) {
/* 280 */       System.arraycopy(array, 0, result, pos, array.length);
/* 281 */       pos += array.length;
/*     */     } 
/* 283 */     return result;
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
/*     */   public static byte[] toByteArray(char value) {
/* 297 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   public static char fromByteArray(byte[] bytes) {
/* 312 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", bytes.length, 2);
/* 313 */     return fromBytes(bytes[0], bytes[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static char fromBytes(byte b1, byte b2) {
/* 324 */     return (char)(b1 << 8 | b2 & 0xFF);
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
/*     */   public static char[] ensureCapacity(char[] array, int minLength, int padding) {
/* 341 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 342 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 343 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, char... array) {
/* 355 */     Preconditions.checkNotNull(separator);
/* 356 */     int len = array.length;
/* 357 */     if (len == 0) {
/* 358 */       return "";
/*     */     }
/*     */     
/* 361 */     StringBuilder builder = new StringBuilder(len + separator.length() * (len - 1));
/* 362 */     builder.append(array[0]);
/* 363 */     for (int i = 1; i < len; i++) {
/* 364 */       builder.append(separator).append(array[i]);
/*     */     }
/* 366 */     return builder.toString();
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
/*     */   public static Comparator<char[]> lexicographicalComparator() {
/* 384 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<char[]> {
/* 388 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(char[] left, char[] right) {
/* 392 */       int minLength = Math.min(left.length, right.length);
/* 393 */       for (int i = 0; i < minLength; i++) {
/* 394 */         int result = Chars.compare(left[i], right[i]);
/* 395 */         if (result != 0) {
/* 396 */           return result;
/*     */         }
/*     */       } 
/* 399 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 404 */       return "Chars.lexicographicalComparator()";
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
/*     */   public static char[] toArray(Collection<Character> collection) {
/* 421 */     if (collection instanceof CharArrayAsList) {
/* 422 */       return ((CharArrayAsList)collection).toCharArray();
/*     */     }
/*     */     
/* 425 */     Object[] boxedArray = collection.toArray();
/* 426 */     int len = boxedArray.length;
/* 427 */     char[] array = new char[len];
/* 428 */     for (int i = 0; i < len; i++)
/*     */     {
/* 430 */       array[i] = ((Character)Preconditions.checkNotNull(boxedArray[i])).charValue();
/*     */     }
/* 432 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(char[] array) {
/* 441 */     Preconditions.checkNotNull(array);
/* 442 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(char[] array, int fromIndex, int toIndex) {
/* 452 */     Preconditions.checkNotNull(array);
/* 453 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 454 */     Arrays.sort(array, fromIndex, toIndex);
/* 455 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reverse(char[] array) {
/* 465 */     Preconditions.checkNotNull(array);
/* 466 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(char[] array, int fromIndex, int toIndex) {
/* 480 */     Preconditions.checkNotNull(array);
/* 481 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 482 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 483 */       char tmp = array[i];
/* 484 */       array[i] = array[j];
/* 485 */       array[j] = tmp;
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
/*     */   public static void rotate(char[] array, int distance) {
/* 500 */     rotate(array, distance, 0, array.length);
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
/*     */   public static void rotate(char[] array, int distance, int fromIndex, int toIndex) {
/* 517 */     Preconditions.checkNotNull(array);
/* 518 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 519 */     if (array.length <= 1) {
/*     */       return;
/*     */     }
/*     */     
/* 523 */     int length = toIndex - fromIndex;
/*     */ 
/*     */     
/* 526 */     int m = -distance % length;
/* 527 */     m = (m < 0) ? (m + length) : m;
/*     */     
/* 529 */     int newFirstIndex = m + fromIndex;
/* 530 */     if (newFirstIndex == fromIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 534 */     reverse(array, fromIndex, newFirstIndex);
/* 535 */     reverse(array, newFirstIndex, toIndex);
/* 536 */     reverse(array, fromIndex, toIndex);
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
/*     */   public static List<Character> asList(char... backingArray) {
/* 554 */     if (backingArray.length == 0) {
/* 555 */       return Collections.emptyList();
/*     */     }
/* 557 */     return new CharArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable {
/*     */     final char[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     CharArrayAsList(char[] array) {
/* 568 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     CharArrayAsList(char[] array, int start, int end) {
/* 572 */       this.array = array;
/* 573 */       this.start = start;
/* 574 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 579 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 584 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Character get(int index) {
/* 589 */       Preconditions.checkElementIndex(index, size());
/* 590 */       return Character.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object target) {
/* 596 */       return (target instanceof Character && Chars
/* 597 */         .indexOf(this.array, ((Character)target).charValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object target) {
/* 603 */       if (target instanceof Character) {
/* 604 */         int i = Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 605 */         if (i >= 0) {
/* 606 */           return i - this.start;
/*     */         }
/*     */       } 
/* 609 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object target) {
/* 615 */       if (target instanceof Character) {
/* 616 */         int i = Chars.lastIndexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 617 */         if (i >= 0) {
/* 618 */           return i - this.start;
/*     */         }
/*     */       } 
/* 621 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Character set(int index, Character element) {
/* 626 */       Preconditions.checkElementIndex(index, size());
/* 627 */       char oldValue = this.array[this.start + index];
/*     */       
/* 629 */       this.array[this.start + index] = ((Character)Preconditions.checkNotNull(element)).charValue();
/* 630 */       return Character.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Character> subList(int fromIndex, int toIndex) {
/* 635 */       int size = size();
/* 636 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 637 */       if (fromIndex == toIndex) {
/* 638 */         return Collections.emptyList();
/*     */       }
/* 640 */       return new CharArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 645 */       if (object == this) {
/* 646 */         return true;
/*     */       }
/* 648 */       if (object instanceof CharArrayAsList) {
/* 649 */         CharArrayAsList that = (CharArrayAsList)object;
/* 650 */         int size = size();
/* 651 */         if (that.size() != size) {
/* 652 */           return false;
/*     */         }
/* 654 */         for (int i = 0; i < size; i++) {
/* 655 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 656 */             return false;
/*     */           }
/*     */         } 
/* 659 */         return true;
/*     */       } 
/* 661 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 666 */       int result = 1;
/* 667 */       for (int i = this.start; i < this.end; i++) {
/* 668 */         result = 31 * result + Chars.hashCode(this.array[i]);
/*     */       }
/* 670 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 675 */       StringBuilder builder = new StringBuilder(size() * 3);
/* 676 */       builder.append('[').append(this.array[this.start]);
/* 677 */       for (int i = this.start + 1; i < this.end; i++) {
/* 678 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 680 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     char[] toCharArray() {
/* 684 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Chars.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */