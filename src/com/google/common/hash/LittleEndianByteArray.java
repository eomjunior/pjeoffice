/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ final class LittleEndianByteArray
/*     */ {
/*     */   private static final LittleEndianBytes byteArray;
/*     */   
/*     */   static long load64(byte[] input, int offset) {
/*  47 */     assert input.length >= offset + 8;
/*     */     
/*  49 */     return byteArray.getLongLittleEndian(input, offset);
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
/*     */   static long load64Safely(byte[] input, int offset, int length) {
/*  63 */     long result = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     int limit = Math.min(length, 8);
/*  69 */     for (int i = 0; i < limit; i++)
/*     */     {
/*  71 */       result |= (input[offset + i] & 0xFFL) << i * 8;
/*     */     }
/*  73 */     return result;
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
/*     */   static void store64(byte[] sink, int offset, long value) {
/*  85 */     assert offset >= 0 && offset + 8 <= sink.length;
/*     */     
/*  87 */     byteArray.putLongLittleEndian(sink, offset, value);
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
/*     */   static int load32(byte[] source, int offset) {
/*  99 */     return source[offset] & 0xFF | (source[offset + 1] & 0xFF) << 8 | (source[offset + 2] & 0xFF) << 16 | (source[offset + 3] & 0xFF) << 24;
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
/*     */   static boolean usingUnsafe() {
/* 111 */     return byteArray instanceof UnsafeByteArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface LittleEndianBytes
/*     */   {
/*     */     long getLongLittleEndian(byte[] param1ArrayOfbyte, int param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void putLongLittleEndian(byte[] param1ArrayOfbyte, int param1Int, long param1Long);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum UnsafeByteArray
/*     */     implements LittleEndianBytes
/*     */   {
/* 133 */     UNSAFE_LITTLE_ENDIAN
/*     */     {
/*     */       public long getLongLittleEndian(byte[] array, int offset) {
/* 136 */         return UnsafeByteArray.theUnsafe.getLong(array, offset + UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET);
/*     */       }
/*     */ 
/*     */       
/*     */       public void putLongLittleEndian(byte[] array, int offset, long value) {
/* 141 */         UnsafeByteArray.theUnsafe.putLong(array, offset + UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET, value);
/*     */       }
/*     */     },
/* 144 */     UNSAFE_BIG_ENDIAN
/*     */     {
/*     */       public long getLongLittleEndian(byte[] array, int offset) {
/* 147 */         long bigEndian = UnsafeByteArray.theUnsafe.getLong(array, offset + UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET);
/*     */         
/* 149 */         return Long.reverseBytes(bigEndian);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putLongLittleEndian(byte[] array, int offset, long value) {
/* 155 */         long littleEndianValue = Long.reverseBytes(value);
/* 156 */         UnsafeByteArray.theUnsafe.putLong(array, offset + UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET, littleEndianValue);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 198 */     private static final Unsafe theUnsafe = getUnsafe();
/* 199 */     private static final int BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
/*     */     private static Unsafe getUnsafe() { try { return Unsafe.getUnsafe(); } catch (SecurityException securityException) { try { return AccessController.<Unsafe>doPrivileged(() -> { Class<Unsafe> k = Unsafe.class; for (Field f : k.getDeclaredFields()) { f.setAccessible(true); Object x = f.get(null); if (k.isInstance(x))
/*     */                     return k.cast(x);  }  throw new NoSuchFieldError("the Unsafe"); }); } catch (PrivilegedActionException e) { throw new RuntimeException("Could not initialize intrinsics", e.getCause()); }  }
/* 202 */        } static { if (theUnsafe.arrayIndexScale(byte[].class) != 1)
/* 203 */         throw new AssertionError();  }
/*     */   
/*     */   }
/*     */   
/*     */   private enum JavaLittleEndianBytes
/*     */     implements LittleEndianBytes
/*     */   {
/* 210 */     INSTANCE
/*     */     {
/*     */       public long getLongLittleEndian(byte[] source, int offset) {
/* 213 */         return Longs.fromBytes(source[offset + 7], source[offset + 6], source[offset + 5], source[offset + 4], source[offset + 3], source[offset + 2], source[offset + 1], source[offset]);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void putLongLittleEndian(byte[] sink, int offset, long value) {
/* 226 */         long mask = 255L;
/* 227 */         for (int i = 0; i < 8; mask <<= 8L, i++) {
/* 228 */           sink[offset + i] = (byte)(int)((value & mask) >> i * 8);
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   static {
/* 235 */     LittleEndianBytes theGetter = JavaLittleEndianBytes.INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 248 */       String arch = System.getProperty("os.arch");
/* 249 */       if ("amd64".equals(arch) || "aarch64".equals(arch))
/*     */       {
/*     */ 
/*     */         
/* 253 */         theGetter = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) ? UnsafeByteArray.UNSAFE_LITTLE_ENDIAN : UnsafeByteArray.UNSAFE_BIG_ENDIAN;
/*     */       }
/* 255 */     } catch (Throwable throwable) {}
/*     */ 
/*     */     
/* 258 */     byteArray = theGetter;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/LittleEndianByteArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */