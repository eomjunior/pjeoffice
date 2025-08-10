/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class SignedBytes
/*     */ {
/*     */   public static final byte MAX_POWER_OF_TWO = 64;
/*     */   
/*     */   public static byte checkedCast(long value) {
/*  59 */     byte result = (byte)(int)value;
/*  60 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  61 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte saturatedCast(long value) {
/*  72 */     if (value > 127L) {
/*  73 */       return Byte.MAX_VALUE;
/*     */     }
/*  75 */     if (value < -128L) {
/*  76 */       return Byte.MIN_VALUE;
/*     */     }
/*  78 */     return (byte)(int)value;
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
/*     */   public static int compare(byte a, byte b) {
/*  95 */     return a - b;
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
/*     */   public static byte min(byte... array) {
/* 107 */     Preconditions.checkArgument((array.length > 0));
/* 108 */     byte min = array[0];
/* 109 */     for (int i = 1; i < array.length; i++) {
/* 110 */       if (array[i] < min) {
/* 111 */         min = array[i];
/*     */       }
/*     */     } 
/* 114 */     return min;
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
/*     */   public static byte max(byte... array) {
/* 126 */     Preconditions.checkArgument((array.length > 0));
/* 127 */     byte max = array[0];
/* 128 */     for (int i = 1; i < array.length; i++) {
/* 129 */       if (array[i] > max) {
/* 130 */         max = array[i];
/*     */       }
/*     */     } 
/* 133 */     return max;
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
/*     */   public static String join(String separator, byte... array) {
/* 145 */     Preconditions.checkNotNull(separator);
/* 146 */     if (array.length == 0) {
/* 147 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 151 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 152 */     builder.append(array[0]);
/* 153 */     for (int i = 1; i < array.length; i++) {
/* 154 */       builder.append(separator).append(array[i]);
/*     */     }
/* 156 */     return builder.toString();
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
/*     */   public static Comparator<byte[]> lexicographicalComparator() {
/* 174 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<byte[]> {
/* 178 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(byte[] left, byte[] right) {
/* 182 */       int minLength = Math.min(left.length, right.length);
/* 183 */       for (int i = 0; i < minLength; i++) {
/* 184 */         int result = SignedBytes.compare(left[i], right[i]);
/* 185 */         if (result != 0) {
/* 186 */           return result;
/*     */         }
/*     */       } 
/* 189 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 194 */       return "SignedBytes.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(byte[] array) {
/* 204 */     Preconditions.checkNotNull(array);
/* 205 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(byte[] array, int fromIndex, int toIndex) {
/* 215 */     Preconditions.checkNotNull(array);
/* 216 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 217 */     Arrays.sort(array, fromIndex, toIndex);
/* 218 */     Bytes.reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/SignedBytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */