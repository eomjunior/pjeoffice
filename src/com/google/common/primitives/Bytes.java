/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ @GwtCompatible
/*     */ public final class Bytes
/*     */ {
/*     */   public static int hashCode(byte value) {
/*  61 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean contains(byte[] array, byte target) {
/*  72 */     for (byte value : array) {
/*  73 */       if (value == target) {
/*  74 */         return true;
/*     */       }
/*     */     } 
/*  77 */     return false;
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
/*     */   public static int indexOf(byte[] array, byte target) {
/*  89 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(byte[] array, byte target, int start, int end) {
/*  94 */     for (int i = start; i < end; i++) {
/*  95 */       if (array[i] == target) {
/*  96 */         return i;
/*     */       }
/*     */     } 
/*  99 */     return -1;
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
/*     */   public static int indexOf(byte[] array, byte[] target) {
/* 113 */     Preconditions.checkNotNull(array, "array");
/* 114 */     Preconditions.checkNotNull(target, "target");
/* 115 */     if (target.length == 0) {
/* 116 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 120 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 121 */       int j = 0; while (true) { if (j < target.length) {
/* 122 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 126 */         return i; }
/*     */     
/* 128 */     }  return -1;
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
/*     */   public static int lastIndexOf(byte[] array, byte target) {
/* 140 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(byte[] array, byte target, int start, int end) {
/* 145 */     for (int i = end - 1; i >= start; i--) {
/* 146 */       if (array[i] == target) {
/* 147 */         return i;
/*     */       }
/*     */     } 
/* 150 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] concat(byte[]... arrays) {
/* 161 */     int length = 0;
/* 162 */     for (byte[] array : arrays) {
/* 163 */       length += array.length;
/*     */     }
/* 165 */     byte[] result = new byte[length];
/* 166 */     int pos = 0;
/* 167 */     for (byte[] array : arrays) {
/* 168 */       System.arraycopy(array, 0, result, pos, array.length);
/* 169 */       pos += array.length;
/*     */     } 
/* 171 */     return result;
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
/*     */   public static byte[] ensureCapacity(byte[] array, int minLength, int padding) {
/* 188 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 189 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 190 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static byte[] toArray(Collection<? extends Number> collection) {
/* 207 */     if (collection instanceof ByteArrayAsList) {
/* 208 */       return ((ByteArrayAsList)collection).toByteArray();
/*     */     }
/*     */     
/* 211 */     Object[] boxedArray = collection.toArray();
/* 212 */     int len = boxedArray.length;
/* 213 */     byte[] array = new byte[len];
/* 214 */     for (int i = 0; i < len; i++)
/*     */     {
/* 216 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).byteValue();
/*     */     }
/* 218 */     return array;
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
/*     */   public static List<Byte> asList(byte... backingArray) {
/* 236 */     if (backingArray.length == 0) {
/* 237 */       return Collections.emptyList();
/*     */     }
/* 239 */     return new ByteArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable {
/*     */     final byte[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ByteArrayAsList(byte[] array) {
/* 250 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ByteArrayAsList(byte[] array, int start, int end) {
/* 254 */       this.array = array;
/* 255 */       this.start = start;
/* 256 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 261 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Byte get(int index) {
/* 271 */       Preconditions.checkElementIndex(index, size());
/* 272 */       return Byte.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(@CheckForNull Object target) {
/* 278 */       return (target instanceof Byte && Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(@CheckForNull Object target) {
/* 284 */       if (target instanceof Byte) {
/* 285 */         int i = Bytes.indexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
/* 286 */         if (i >= 0) {
/* 287 */           return i - this.start;
/*     */         }
/*     */       } 
/* 290 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@CheckForNull Object target) {
/* 296 */       if (target instanceof Byte) {
/* 297 */         int i = Bytes.lastIndexOf(this.array, ((Byte)target).byteValue(), this.start, this.end);
/* 298 */         if (i >= 0) {
/* 299 */           return i - this.start;
/*     */         }
/*     */       } 
/* 302 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Byte set(int index, Byte element) {
/* 307 */       Preconditions.checkElementIndex(index, size());
/* 308 */       byte oldValue = this.array[this.start + index];
/*     */       
/* 310 */       this.array[this.start + index] = ((Byte)Preconditions.checkNotNull(element)).byteValue();
/* 311 */       return Byte.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Byte> subList(int fromIndex, int toIndex) {
/* 316 */       int size = size();
/* 317 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 318 */       if (fromIndex == toIndex) {
/* 319 */         return Collections.emptyList();
/*     */       }
/* 321 */       return new ByteArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object object) {
/* 326 */       if (object == this) {
/* 327 */         return true;
/*     */       }
/* 329 */       if (object instanceof ByteArrayAsList) {
/* 330 */         ByteArrayAsList that = (ByteArrayAsList)object;
/* 331 */         int size = size();
/* 332 */         if (that.size() != size) {
/* 333 */           return false;
/*     */         }
/* 335 */         for (int i = 0; i < size; i++) {
/* 336 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 337 */             return false;
/*     */           }
/*     */         } 
/* 340 */         return true;
/*     */       } 
/* 342 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 347 */       int result = 1;
/* 348 */       for (int i = this.start; i < this.end; i++) {
/* 349 */         result = 31 * result + Bytes.hashCode(this.array[i]);
/*     */       }
/* 351 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 356 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 357 */       builder.append('[').append(this.array[this.start]);
/* 358 */       for (int i = this.start + 1; i < this.end; i++) {
/* 359 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 361 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     byte[] toByteArray() {
/* 365 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   public static void reverse(byte[] array) {
/* 378 */     Preconditions.checkNotNull(array);
/* 379 */     reverse(array, 0, array.length);
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
/*     */   public static void reverse(byte[] array, int fromIndex, int toIndex) {
/* 393 */     Preconditions.checkNotNull(array);
/* 394 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 395 */     for (int i = fromIndex, j = toIndex - 1; i < j; i++, j--) {
/* 396 */       byte tmp = array[i];
/* 397 */       array[i] = array[j];
/* 398 */       array[j] = tmp;
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
/*     */   public static void rotate(byte[] array, int distance) {
/* 413 */     rotate(array, distance, 0, array.length);
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
/*     */   public static void rotate(byte[] array, int distance, int fromIndex, int toIndex) {
/* 430 */     Preconditions.checkNotNull(array);
/* 431 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 432 */     if (array.length <= 1) {
/*     */       return;
/*     */     }
/*     */     
/* 436 */     int length = toIndex - fromIndex;
/*     */ 
/*     */     
/* 439 */     int m = -distance % length;
/* 440 */     m = (m < 0) ? (m + length) : m;
/*     */     
/* 442 */     int newFirstIndex = m + fromIndex;
/* 443 */     if (newFirstIndex == fromIndex) {
/*     */       return;
/*     */     }
/*     */     
/* 447 */     reverse(array, fromIndex, newFirstIndex);
/* 448 */     reverse(array, newFirstIndex, toIndex);
/* 449 */     reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/Bytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */