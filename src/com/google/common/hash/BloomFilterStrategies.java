/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.LongMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.atomic.AtomicLongArray;
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
/*     */ @ElementTypesAreNonnullByDefault
/*     */ enum BloomFilterStrategies
/*     */   implements BloomFilter.Strategy
/*     */ {
/*  47 */   MURMUR128_MITZ_32
/*     */   {
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean put(@ParametricNullness T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits)
/*     */     {
/*  54 */       long bitSize = bits.bitSize();
/*  55 */       long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
/*  56 */       int hash1 = (int)hash64;
/*  57 */       int hash2 = (int)(hash64 >>> 32L);
/*     */       
/*  59 */       boolean bitsChanged = false;
/*  60 */       for (int i = 1; i <= numHashFunctions; i++) {
/*  61 */         int combinedHash = hash1 + i * hash2;
/*     */         
/*  63 */         if (combinedHash < 0) {
/*  64 */           combinedHash ^= 0xFFFFFFFF;
/*     */         }
/*  66 */         bitsChanged |= bits.set(combinedHash % bitSize);
/*     */       } 
/*  68 */       return bitsChanged;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean mightContain(@ParametricNullness T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits) {
/*  77 */       long bitSize = bits.bitSize();
/*  78 */       long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
/*  79 */       int hash1 = (int)hash64;
/*  80 */       int hash2 = (int)(hash64 >>> 32L);
/*     */       
/*  82 */       for (int i = 1; i <= numHashFunctions; i++) {
/*  83 */         int combinedHash = hash1 + i * hash2;
/*     */         
/*  85 */         if (combinedHash < 0) {
/*  86 */           combinedHash ^= 0xFFFFFFFF;
/*     */         }
/*  88 */         if (!bits.get(combinedHash % bitSize)) {
/*  89 */           return false;
/*     */         }
/*     */       } 
/*  92 */       return true;
/*     */     }
/*     */   },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   MURMUR128_MITZ_64
/*     */   {
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean put(@ParametricNullness T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits)
/*     */     {
/* 108 */       long bitSize = bits.bitSize();
/* 109 */       byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
/* 110 */       long hash1 = lowerEight(bytes);
/* 111 */       long hash2 = upperEight(bytes);
/*     */       
/* 113 */       boolean bitsChanged = false;
/* 114 */       long combinedHash = hash1;
/* 115 */       for (int i = 0; i < numHashFunctions; i++) {
/*     */         
/* 117 */         bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
/* 118 */         combinedHash += hash2;
/*     */       } 
/* 120 */       return bitsChanged;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> boolean mightContain(@ParametricNullness T object, Funnel<? super T> funnel, int numHashFunctions, LockFreeBitArray bits) {
/* 129 */       long bitSize = bits.bitSize();
/* 130 */       byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
/* 131 */       long hash1 = lowerEight(bytes);
/* 132 */       long hash2 = upperEight(bytes);
/*     */       
/* 134 */       long combinedHash = hash1;
/* 135 */       for (int i = 0; i < numHashFunctions; i++) {
/*     */         
/* 137 */         if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
/* 138 */           return false;
/*     */         }
/* 140 */         combinedHash += hash2;
/*     */       } 
/* 142 */       return true;
/*     */     }
/*     */     
/*     */     private long lowerEight(byte[] bytes) {
/* 146 */       return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     private long upperEight(byte[] bytes) {
/* 151 */       return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
/*     */     }
/*     */   };
/*     */ 
/*     */ 
/*     */   
/*     */   static final class LockFreeBitArray
/*     */   {
/*     */     private static final int LONG_ADDRESSABLE_BITS = 6;
/*     */ 
/*     */     
/*     */     final AtomicLongArray data;
/*     */     
/*     */     private final LongAddable bitCount;
/*     */ 
/*     */     
/*     */     LockFreeBitArray(long bits) {
/* 168 */       Preconditions.checkArgument((bits > 0L), "data length is zero!");
/*     */ 
/*     */       
/* 171 */       this
/* 172 */         .data = new AtomicLongArray(Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING)));
/* 173 */       this.bitCount = LongAddables.create();
/*     */     }
/*     */ 
/*     */     
/*     */     LockFreeBitArray(long[] data) {
/* 178 */       Preconditions.checkArgument((data.length > 0), "data length is zero!");
/* 179 */       this.data = new AtomicLongArray(data);
/* 180 */       this.bitCount = LongAddables.create();
/* 181 */       long bitCount = 0L;
/* 182 */       for (long value : data) {
/* 183 */         bitCount += Long.bitCount(value);
/*     */       }
/* 185 */       this.bitCount.add(bitCount);
/*     */     }
/*     */     
/*     */     boolean set(long bitIndex) {
/*     */       long oldValue, newValue;
/* 190 */       if (get(bitIndex)) {
/* 191 */         return false;
/*     */       }
/*     */       
/* 194 */       int longIndex = (int)(bitIndex >>> 6L);
/* 195 */       long mask = 1L << (int)bitIndex;
/*     */ 
/*     */ 
/*     */       
/*     */       do {
/* 200 */         oldValue = this.data.get(longIndex);
/* 201 */         newValue = oldValue | mask;
/* 202 */         if (oldValue == newValue) {
/* 203 */           return false;
/*     */         }
/* 205 */       } while (!this.data.compareAndSet(longIndex, oldValue, newValue));
/*     */ 
/*     */       
/* 208 */       this.bitCount.increment();
/* 209 */       return true;
/*     */     }
/*     */     
/*     */     boolean get(long bitIndex) {
/* 213 */       return ((this.data.get((int)(bitIndex >>> 6L)) & 1L << (int)bitIndex) != 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static long[] toPlainArray(AtomicLongArray atomicLongArray) {
/* 222 */       long[] array = new long[atomicLongArray.length()];
/* 223 */       for (int i = 0; i < array.length; i++) {
/* 224 */         array[i] = atomicLongArray.get(i);
/*     */       }
/* 226 */       return array;
/*     */     }
/*     */ 
/*     */     
/*     */     long bitSize() {
/* 231 */       return this.data.length() * 64L;
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
/*     */     long bitCount() {
/* 243 */       return this.bitCount.sum();
/*     */     }
/*     */     
/*     */     LockFreeBitArray copy() {
/* 247 */       return new LockFreeBitArray(toPlainArray(this.data));
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
/*     */     void putAll(LockFreeBitArray other) {
/* 260 */       Preconditions.checkArgument(
/* 261 */           (this.data.length() == other.data.length()), "BitArrays must be of equal length (%s != %s)", this.data
/*     */           
/* 263 */           .length(), other.data
/* 264 */           .length());
/* 265 */       for (int i = 0; i < this.data.length(); i++) {
/* 266 */         putData(i, other.data.get(i));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void putData(int i, long longValue) {
/*     */       long ourLongOld, ourLongNew;
/* 277 */       boolean changedAnyBits = true;
/*     */       do {
/* 279 */         ourLongOld = this.data.get(i);
/* 280 */         ourLongNew = ourLongOld | longValue;
/* 281 */         if (ourLongOld == ourLongNew) {
/* 282 */           changedAnyBits = false;
/*     */           break;
/*     */         } 
/* 285 */       } while (!this.data.compareAndSet(i, ourLongOld, ourLongNew));
/*     */       
/* 287 */       if (changedAnyBits) {
/* 288 */         int bitsAdded = Long.bitCount(ourLongNew) - Long.bitCount(ourLongOld);
/* 289 */         this.bitCount.add(bitsAdded);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int dataLength() {
/* 295 */       return this.data.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@CheckForNull Object o) {
/* 300 */       if (o instanceof LockFreeBitArray) {
/* 301 */         LockFreeBitArray lockFreeBitArray = (LockFreeBitArray)o;
/*     */         
/* 303 */         return Arrays.equals(toPlainArray(this.data), toPlainArray(lockFreeBitArray.data));
/*     */       } 
/* 305 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 311 */       return Arrays.hashCode(toPlainArray(this.data));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/BloomFilterStrategies.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */