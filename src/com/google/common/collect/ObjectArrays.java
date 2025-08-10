/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class ObjectArrays
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static <T> T[] newArray(Class<T> type, int length) {
/*  52 */     return (T[])Array.newInstance(type, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T[] newArray(T[] reference, int length) {
/*  62 */     return Platform.newArray(reference, length);
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
/*     */   @GwtIncompatible
/*     */   public static <T> T[] concat(T[] first, T[] second, Class<T> type) {
/*  75 */     T[] result = newArray(type, first.length + second.length);
/*  76 */     System.arraycopy(first, 0, result, 0, first.length);
/*  77 */     System.arraycopy(second, 0, result, first.length, second.length);
/*  78 */     return result;
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
/*     */   public static <T> T[] concat(@ParametricNullness T element, T[] array) {
/*  90 */     T[] result = newArray(array, array.length + 1);
/*  91 */     result[0] = element;
/*  92 */     System.arraycopy(array, 0, result, 1, array.length);
/*  93 */     return result;
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
/*     */   public static <T> T[] concat(T[] array, @ParametricNullness T element) {
/* 105 */     T[] result = Arrays.copyOf(array, array.length + 1);
/* 106 */     result[array.length] = element;
/* 107 */     return result;
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
/*     */   static <T> T[] toArrayImpl(Collection<?> c, T[] array) {
/* 132 */     int size = c.size();
/* 133 */     if (array.length < size) {
/* 134 */       array = newArray(array, size);
/*     */     }
/* 136 */     fillArray(c, (Object[])array);
/* 137 */     if (array.length > size) {
/* 138 */       T[] arrayOfT = array;
/* 139 */       arrayOfT[size] = null;
/*     */     } 
/* 141 */     return array;
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
/*     */   static <T> T[] toArrayImpl(Object[] src, int offset, int len, T[] dst) {
/* 157 */     Preconditions.checkPositionIndexes(offset, offset + len, src.length);
/* 158 */     if (dst.length < len) {
/* 159 */       dst = newArray(dst, len);
/* 160 */     } else if (dst.length > len) {
/* 161 */       T[] arrayOfT = dst;
/* 162 */       arrayOfT[len] = null;
/*     */     } 
/* 164 */     System.arraycopy(src, offset, dst, 0, len);
/* 165 */     return dst;
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
/*     */   static Object[] toArrayImpl(Collection<?> c) {
/* 181 */     return fillArray(c, new Object[c.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Object[] copyAsObjectArray(Object[] elements, int offset, int length) {
/* 189 */     Preconditions.checkPositionIndexes(offset, offset + length, elements.length);
/* 190 */     if (length == 0) {
/* 191 */       return new Object[0];
/*     */     }
/* 193 */     Object[] result = new Object[length];
/* 194 */     System.arraycopy(elements, offset, result, 0, length);
/* 195 */     return result;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private static Object[] fillArray(Iterable<?> elements, Object[] array) {
/* 200 */     int i = 0;
/* 201 */     for (Object element : elements) {
/* 202 */       array[i++] = element;
/*     */     }
/* 204 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   static void swap(Object[] array, int i, int j) {
/* 209 */     Object temp = array[i];
/* 210 */     array[i] = array[j];
/* 211 */     array[j] = temp;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object[] checkElementsNotNull(Object... array) {
/* 216 */     return checkElementsNotNull(array, array.length);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object[] checkElementsNotNull(Object[] array, int length) {
/* 221 */     for (int i = 0; i < length; i++) {
/* 222 */       checkElementNotNull(array[i], i);
/*     */     }
/* 224 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static Object checkElementNotNull(Object element, int index) {
/* 231 */     if (element == null) {
/* 232 */       throw new NullPointerException("at index " + index);
/*     */     }
/* 234 */     return element;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/collect/ObjectArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */