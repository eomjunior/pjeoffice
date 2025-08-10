/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.J2ktIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class UnsignedBytes
/*     */ {
/*     */   public static final byte MAX_POWER_OF_TWO = -128;
/*     */   public static final byte MAX_VALUE = -1;
/*     */   private static final int UNSIGNED_MASK = 255;
/*     */   
/*     */   public static int toInt(byte value) {
/*  77 */     return value & 0xFF;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static byte checkedCast(long value) {
/*  90 */     Preconditions.checkArgument((value >> 8L == 0L), "out of range: %s", value);
/*  91 */     return (byte)(int)value;
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
/*     */   public static byte saturatedCast(long value) {
/* 103 */     if (value > toInt((byte)-1)) {
/* 104 */       return -1;
/*     */     }
/* 106 */     if (value < 0L) {
/* 107 */       return 0;
/*     */     }
/* 109 */     return (byte)(int)value;
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
/*     */   public static int compare(byte a, byte b) {
/* 123 */     return toInt(a) - toInt(b);
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
/* 135 */     Preconditions.checkArgument((array.length > 0));
/* 136 */     int min = toInt(array[0]);
/* 137 */     for (int i = 1; i < array.length; i++) {
/* 138 */       int next = toInt(array[i]);
/* 139 */       if (next < min) {
/* 140 */         min = next;
/*     */       }
/*     */     } 
/* 143 */     return (byte)min;
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
/* 155 */     Preconditions.checkArgument((array.length > 0));
/* 156 */     int max = toInt(array[0]);
/* 157 */     for (int i = 1; i < array.length; i++) {
/* 158 */       int next = toInt(array[i]);
/* 159 */       if (next > max) {
/* 160 */         max = next;
/*     */       }
/*     */     } 
/* 163 */     return (byte)max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(byte x) {
/* 172 */     return toString(x, 10);
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
/*     */   public static String toString(byte x, int radix) {
/* 186 */     Preconditions.checkArgument((radix >= 2 && radix <= 36), "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     return Integer.toString(toInt(x), radix);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static byte parseUnsignedByte(String string) {
/* 205 */     return parseUnsignedByte(string, 10);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static byte parseUnsignedByte(String string, int radix) {
/* 222 */     int parse = Integer.parseInt((String)Preconditions.checkNotNull(string), radix);
/*     */     
/* 224 */     if (parse >> 8 == 0) {
/* 225 */       return (byte)parse;
/*     */     }
/* 227 */     throw new NumberFormatException("out of range: " + parse);
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
/*     */   public static String join(String separator, byte... array) {
/* 241 */     Preconditions.checkNotNull(separator);
/* 242 */     if (array.length == 0) {
/* 243 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 247 */     StringBuilder builder = new StringBuilder(array.length * (3 + separator.length()));
/* 248 */     builder.append(toInt(array[0]));
/* 249 */     for (int i = 1; i < array.length; i++) {
/* 250 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 252 */     return builder.toString();
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
/* 270 */     return LexicographicalComparatorHolder.BEST_COMPARATOR;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
/* 275 */     return LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class LexicographicalComparatorHolder
/*     */   {
/* 287 */     static final String UNSAFE_COMPARATOR_NAME = LexicographicalComparatorHolder.class
/* 288 */       .getName() + "$UnsafeComparator";
/*     */     
/* 290 */     static final Comparator<byte[]> BEST_COMPARATOR = getBestComparator();
/*     */     
/*     */     @VisibleForTesting
/*     */     enum UnsafeComparator implements Comparator<byte[]> {
/* 294 */       INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       static final int BYTE_ARRAY_BASE_OFFSET;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 314 */       static final Unsafe theUnsafe = getUnsafe(); static final boolean BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
/*     */       
/*     */       static {
/* 317 */         BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 322 */         if (!"64".equals(System.getProperty("sun.arch.data.model")) || BYTE_ARRAY_BASE_OFFSET % 8 != 0 || theUnsafe
/*     */ 
/*     */           
/* 325 */           .arrayIndexScale(byte[].class) != 1) {
/* 326 */           throw new Error();
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private static Unsafe getUnsafe() {
/*     */         try {
/* 338 */           return Unsafe.getUnsafe();
/* 339 */         } catch (SecurityException securityException) {
/*     */ 
/*     */           
/*     */           try {
/* 343 */             return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*     */                 {
/*     */                   public Unsafe run() throws Exception
/*     */                   {
/* 347 */                     Class<Unsafe> k = Unsafe.class;
/* 348 */                     for (Field f : k.getDeclaredFields()) {
/* 349 */                       f.setAccessible(true);
/* 350 */                       Object x = f.get(null);
/* 351 */                       if (k.isInstance(x)) {
/* 352 */                         return k.cast(x);
/*     */                       }
/*     */                     } 
/* 355 */                     throw new NoSuchFieldError("the Unsafe");
/*     */                   }
/*     */                 });
/* 358 */           } catch (PrivilegedActionException e) {
/* 359 */             throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/*     */       public int compare(byte[] left, byte[] right) {
/* 365 */         int stride = 8;
/* 366 */         int minLength = Math.min(left.length, right.length);
/* 367 */         int strideLimit = minLength & (stride - 1 ^ 0xFFFFFFFF);
/*     */ 
/*     */ 
/*     */         
/*     */         int i;
/*     */ 
/*     */         
/* 374 */         for (i = 0; i < strideLimit; i += stride) {
/* 375 */           long lw = theUnsafe.getLong(left, BYTE_ARRAY_BASE_OFFSET + i);
/* 376 */           long rw = theUnsafe.getLong(right, BYTE_ARRAY_BASE_OFFSET + i);
/* 377 */           if (lw != rw) {
/* 378 */             if (BIG_ENDIAN) {
/* 379 */               return UnsignedLongs.compare(lw, rw);
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 389 */             int n = Long.numberOfTrailingZeros(lw ^ rw) & 0xFFFFFFF8;
/* 390 */             return (int)(lw >>> n & 0xFFL) - (int)(rw >>> n & 0xFFL);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 395 */         for (; i < minLength; i++) {
/* 396 */           int result = UnsignedBytes.compare(left[i], right[i]);
/* 397 */           if (result != 0) {
/* 398 */             return result;
/*     */           }
/*     */         } 
/* 401 */         return left.length - right.length;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 406 */         return "UnsignedBytes.lexicographicalComparator() (sun.misc.Unsafe version)";
/*     */       }
/*     */     }
/*     */     
/*     */     enum PureJavaComparator implements Comparator<byte[]> {
/* 411 */       INSTANCE;
/*     */ 
/*     */       
/*     */       public int compare(byte[] left, byte[] right) {
/* 415 */         int minLength = Math.min(left.length, right.length);
/* 416 */         for (int i = 0; i < minLength; i++) {
/* 417 */           int result = UnsignedBytes.compare(left[i], right[i]);
/* 418 */           if (result != 0) {
/* 419 */             return result;
/*     */           }
/*     */         } 
/* 422 */         return left.length - right.length;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 427 */         return "UnsignedBytes.lexicographicalComparator() (pure Java version)";
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static Comparator<byte[]> getBestComparator() {
/*     */       try {
/* 437 */         Class<?> theClass = Class.forName(UNSAFE_COMPARATOR_NAME);
/*     */ 
/*     */         
/* 440 */         Object[] constants = Objects.<Object[]>requireNonNull(theClass.getEnumConstants());
/*     */ 
/*     */ 
/*     */         
/* 444 */         Comparator<byte[]> comparator = (Comparator<byte[]>)constants[0];
/* 445 */         return comparator;
/* 446 */       } catch (Throwable t) {
/* 447 */         return UnsignedBytes.lexicographicalComparatorJavaImpl();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static byte flip(byte b) {
/* 453 */     return (byte)(b ^ 0x80);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(byte[] array) {
/* 462 */     Preconditions.checkNotNull(array);
/* 463 */     sort(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(byte[] array, int fromIndex, int toIndex) {
/* 473 */     Preconditions.checkNotNull(array);
/* 474 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 475 */     for (i = fromIndex; i < toIndex; i++) {
/* 476 */       array[i] = flip(array[i]);
/*     */     }
/* 478 */     Arrays.sort(array, fromIndex, toIndex);
/* 479 */     for (i = fromIndex; i < toIndex; i++) {
/* 480 */       array[i] = flip(array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(byte[] array) {
/* 491 */     Preconditions.checkNotNull(array);
/* 492 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */   enum PureJavaComparator implements Comparator<byte[]> {
/*     */     INSTANCE; public int compare(byte[] left, byte[] right) { int minLength = Math.min(left.length, right.length);
/*     */       for (int i = 0; i < minLength; i++) {
/*     */         int result = UnsignedBytes.compare(left[i], right[i]);
/*     */         if (result != 0)
/*     */           return result; 
/*     */       } 
/*     */       return left.length - right.length; } public String toString() { return "UnsignedBytes.lexicographicalComparator() (pure Java version)"; } } public static void sortDescending(byte[] array, int fromIndex, int toIndex) {
/* 502 */     Preconditions.checkNotNull(array);
/* 503 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 504 */     for (i = fromIndex; i < toIndex; i++) {
/* 505 */       array[i] = (byte)(array[i] ^ Byte.MAX_VALUE);
/*     */     }
/* 507 */     Arrays.sort(array, fromIndex, toIndex);
/* 508 */     for (i = fromIndex; i < toIndex; i++)
/* 509 */       array[i] = (byte)(array[i] ^ Byte.MAX_VALUE); 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/primitives/UnsignedBytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */