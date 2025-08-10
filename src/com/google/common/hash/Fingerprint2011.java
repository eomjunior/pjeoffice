/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class Fingerprint2011
/*     */   extends AbstractNonStreamingHashFunction
/*     */ {
/*  33 */   static final HashFunction FINGERPRINT_2011 = new Fingerprint2011();
/*     */   
/*     */   private static final long K0 = -6505348102511208375L;
/*     */   
/*     */   private static final long K1 = -8261664234251669945L;
/*     */   
/*     */   private static final long K2 = -4288712594273399085L;
/*     */   private static final long K3 = -4132994306676758123L;
/*     */   
/*     */   public HashCode hashBytes(byte[] input, int off, int len) {
/*  43 */     Preconditions.checkPositionIndexes(off, off + len, input.length);
/*  44 */     return HashCode.fromLong(fingerprint(input, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  49 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  54 */     return "Hashing.fingerprint2011()";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static long fingerprint(byte[] bytes, int offset, int length) {
/*  63 */     if (length <= 32) {
/*  64 */       result = murmurHash64WithSeed(bytes, offset, length, -1397348546323613475L);
/*  65 */     } else if (length <= 64) {
/*  66 */       result = hashLength33To64(bytes, offset, length);
/*     */     } else {
/*  68 */       result = fullFingerprint(bytes, offset, length);
/*     */     } 
/*     */     
/*  71 */     long u = (length >= 8) ? LittleEndianByteArray.load64(bytes, offset) : -6505348102511208375L;
/*  72 */     long v = (length >= 9) ? LittleEndianByteArray.load64(bytes, offset + length - 8) : -6505348102511208375L;
/*  73 */     long result = hash128to64(result + v, u);
/*  74 */     return (result == 0L || result == 1L) ? (result + -2L) : result;
/*     */   }
/*     */   
/*     */   private static long shiftMix(long val) {
/*  78 */     return val ^ val >>> 47L;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static long hash128to64(long high, long low) {
/*  84 */     long a = (low ^ high) * -4132994306676758123L;
/*  85 */     a ^= a >>> 47L;
/*  86 */     long b = (high ^ a) * -4132994306676758123L;
/*  87 */     b ^= b >>> 47L;
/*  88 */     b *= -4132994306676758123L;
/*  89 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void weakHashLength32WithSeeds(byte[] bytes, int offset, long seedA, long seedB, long[] output) {
/*  98 */     long part1 = LittleEndianByteArray.load64(bytes, offset);
/*  99 */     long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
/* 100 */     long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
/* 101 */     long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
/*     */     
/* 103 */     seedA += part1;
/* 104 */     seedB = Long.rotateRight(seedB + seedA + part4, 51);
/* 105 */     long c = seedA;
/* 106 */     seedA += part2;
/* 107 */     seedA += part3;
/* 108 */     seedB += Long.rotateRight(seedA, 23);
/* 109 */     output[0] = seedA + part4;
/* 110 */     output[1] = seedB + c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long fullFingerprint(byte[] bytes, int offset, int length) {
/* 119 */     long x = LittleEndianByteArray.load64(bytes, offset);
/* 120 */     long y = LittleEndianByteArray.load64(bytes, offset + length - 16) ^ 0x8D58AC26AFE12E47L;
/* 121 */     long z = LittleEndianByteArray.load64(bytes, offset + length - 56) ^ 0xA5B85C5E198ED849L;
/* 122 */     long[] v = new long[2];
/* 123 */     long[] w = new long[2];
/* 124 */     weakHashLength32WithSeeds(bytes, offset + length - 64, length, y, v);
/* 125 */     weakHashLength32WithSeeds(bytes, offset + length - 32, length * -8261664234251669945L, -6505348102511208375L, w);
/* 126 */     z += shiftMix(v[1]) * -8261664234251669945L;
/* 127 */     x = Long.rotateRight(z + x, 39) * -8261664234251669945L;
/* 128 */     y = Long.rotateRight(y, 33) * -8261664234251669945L;
/*     */ 
/*     */     
/* 131 */     length = length - 1 & 0xFFFFFFC0;
/*     */     while (true) {
/* 133 */       x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 16), 37) * -8261664234251669945L;
/* 134 */       y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * -8261664234251669945L;
/* 135 */       x ^= w[1];
/* 136 */       y ^= v[0];
/* 137 */       z = Long.rotateRight(z ^ w[0], 33);
/* 138 */       weakHashLength32WithSeeds(bytes, offset, v[1] * -8261664234251669945L, x + w[0], v);
/* 139 */       weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y, w);
/* 140 */       long tmp = z;
/* 141 */       z = x;
/* 142 */       x = tmp;
/* 143 */       offset += 64;
/* 144 */       length -= 64;
/* 145 */       if (length == 0)
/* 146 */         return hash128to64(hash128to64(v[0], w[0]) + shiftMix(y) * -8261664234251669945L + z, hash128to64(v[1], w[1]) + x); 
/*     */     } 
/*     */   }
/*     */   private static long hashLength33To64(byte[] bytes, int offset, int length) {
/* 150 */     long z = LittleEndianByteArray.load64(bytes, offset + 24);
/* 151 */     long a = LittleEndianByteArray.load64(bytes, offset) + (length + LittleEndianByteArray.load64(bytes, offset + length - 16)) * -6505348102511208375L;
/* 152 */     long b = Long.rotateRight(a + z, 52);
/* 153 */     long c = Long.rotateRight(a, 37);
/* 154 */     a += LittleEndianByteArray.load64(bytes, offset + 8);
/* 155 */     c += Long.rotateRight(a, 7);
/* 156 */     a += LittleEndianByteArray.load64(bytes, offset + 16);
/* 157 */     long vf = a + z;
/* 158 */     long vs = b + Long.rotateRight(a, 31) + c;
/* 159 */     a = LittleEndianByteArray.load64(bytes, offset + 16) + LittleEndianByteArray.load64(bytes, offset + length - 32);
/* 160 */     z = LittleEndianByteArray.load64(bytes, offset + length - 8);
/* 161 */     b = Long.rotateRight(a + z, 52);
/* 162 */     c = Long.rotateRight(a, 37);
/* 163 */     a += LittleEndianByteArray.load64(bytes, offset + length - 24);
/* 164 */     c += Long.rotateRight(a, 7);
/* 165 */     a += LittleEndianByteArray.load64(bytes, offset + length - 16);
/* 166 */     long wf = a + z;
/* 167 */     long ws = b + Long.rotateRight(a, 31) + c;
/* 168 */     long r = shiftMix((vf + ws) * -4288712594273399085L + (wf + vs) * -6505348102511208375L);
/* 169 */     return shiftMix(r * -6505348102511208375L + vs) * -4288712594273399085L;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static long murmurHash64WithSeed(byte[] bytes, int offset, int length, long seed) {
/* 174 */     long mul = -4132994306676758123L;
/* 175 */     int topBit = 7;
/*     */     
/* 177 */     int lengthAligned = length & (topBit ^ 0xFFFFFFFF);
/* 178 */     int lengthRemainder = length & topBit;
/* 179 */     long hash = seed ^ length * mul;
/*     */     
/* 181 */     for (int i = 0; i < lengthAligned; i += 8) {
/* 182 */       long loaded = LittleEndianByteArray.load64(bytes, offset + i);
/* 183 */       long data = shiftMix(loaded * mul) * mul;
/* 184 */       hash ^= data;
/* 185 */       hash *= mul;
/*     */     } 
/*     */     
/* 188 */     if (lengthRemainder != 0) {
/* 189 */       long data = LittleEndianByteArray.load64Safely(bytes, offset + lengthAligned, lengthRemainder);
/* 190 */       hash ^= data;
/* 191 */       hash *= mul;
/*     */     } 
/*     */     
/* 194 */     hash = shiftMix(hash) * mul;
/* 195 */     hash = shiftMix(hash);
/* 196 */     return hash;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/Fingerprint2011.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */