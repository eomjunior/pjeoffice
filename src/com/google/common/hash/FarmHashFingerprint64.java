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
/*     */ 
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
/*     */ final class FarmHashFingerprint64
/*     */   extends AbstractNonStreamingHashFunction
/*     */ {
/*  43 */   static final HashFunction FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();
/*     */   
/*     */   private static final long K0 = -4348849565147123417L;
/*     */   
/*     */   private static final long K1 = -5435081209227447693L;
/*     */   
/*     */   private static final long K2 = -7286425919675154353L;
/*     */   
/*     */   public HashCode hashBytes(byte[] input, int off, int len) {
/*  52 */     Preconditions.checkPositionIndexes(off, off + len, input.length);
/*  53 */     return HashCode.fromLong(fingerprint(input, off, len));
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  58 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  63 */     return "Hashing.farmHashFingerprint64()";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static long fingerprint(byte[] bytes, int offset, int length) {
/*  70 */     if (length <= 32) {
/*  71 */       if (length <= 16) {
/*  72 */         return hashLength0to16(bytes, offset, length);
/*     */       }
/*  74 */       return hashLength17to32(bytes, offset, length);
/*     */     } 
/*  76 */     if (length <= 64) {
/*  77 */       return hashLength33To64(bytes, offset, length);
/*     */     }
/*  79 */     return hashLength65Plus(bytes, offset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static long shiftMix(long val) {
/*  84 */     return val ^ val >>> 47L;
/*     */   }
/*     */   
/*     */   private static long hashLength16(long u, long v, long mul) {
/*  88 */     long a = (u ^ v) * mul;
/*  89 */     a ^= a >>> 47L;
/*  90 */     long b = (v ^ a) * mul;
/*  91 */     b ^= b >>> 47L;
/*  92 */     b *= mul;
/*  93 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void weakHashLength32WithSeeds(byte[] bytes, int offset, long seedA, long seedB, long[] output) {
/* 103 */     long part1 = LittleEndianByteArray.load64(bytes, offset);
/* 104 */     long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
/* 105 */     long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
/* 106 */     long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
/*     */     
/* 108 */     seedA += part1;
/* 109 */     seedB = Long.rotateRight(seedB + seedA + part4, 21);
/* 110 */     long c = seedA;
/* 111 */     seedA += part2;
/* 112 */     seedA += part3;
/* 113 */     seedB += Long.rotateRight(seedA, 44);
/* 114 */     output[0] = seedA + part4;
/* 115 */     output[1] = seedB + c;
/*     */   }
/*     */   
/*     */   private static long hashLength0to16(byte[] bytes, int offset, int length) {
/* 119 */     if (length >= 8) {
/* 120 */       long mul = -7286425919675154353L + length * 2L;
/* 121 */       long a = LittleEndianByteArray.load64(bytes, offset) + -7286425919675154353L;
/* 122 */       long b = LittleEndianByteArray.load64(bytes, offset + length - 8);
/* 123 */       long c = Long.rotateRight(b, 37) * mul + a;
/* 124 */       long d = (Long.rotateRight(a, 25) + b) * mul;
/* 125 */       return hashLength16(c, d, mul);
/*     */     } 
/* 127 */     if (length >= 4) {
/* 128 */       long mul = -7286425919675154353L + (length * 2);
/* 129 */       long a = LittleEndianByteArray.load32(bytes, offset) & 0xFFFFFFFFL;
/* 130 */       return hashLength16(length + (a << 3L), LittleEndianByteArray.load32(bytes, offset + length - 4) & 0xFFFFFFFFL, mul);
/*     */     } 
/* 132 */     if (length > 0) {
/* 133 */       byte a = bytes[offset];
/* 134 */       byte b = bytes[offset + (length >> 1)];
/* 135 */       byte c = bytes[offset + length - 1];
/* 136 */       int y = (a & 0xFF) + ((b & 0xFF) << 8);
/* 137 */       int z = length + ((c & 0xFF) << 2);
/* 138 */       return shiftMix(y * -7286425919675154353L ^ z * -4348849565147123417L) * -7286425919675154353L;
/*     */     } 
/* 140 */     return -7286425919675154353L;
/*     */   }
/*     */   
/*     */   private static long hashLength17to32(byte[] bytes, int offset, int length) {
/* 144 */     long mul = -7286425919675154353L + length * 2L;
/* 145 */     long a = LittleEndianByteArray.load64(bytes, offset) * -5435081209227447693L;
/* 146 */     long b = LittleEndianByteArray.load64(bytes, offset + 8);
/* 147 */     long c = LittleEndianByteArray.load64(bytes, offset + length - 8) * mul;
/* 148 */     long d = LittleEndianByteArray.load64(bytes, offset + length - 16) * -7286425919675154353L;
/* 149 */     return hashLength16(
/* 150 */         Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d, a + Long.rotateRight(b + -7286425919675154353L, 18) + c, mul);
/*     */   }
/*     */   
/*     */   private static long hashLength33To64(byte[] bytes, int offset, int length) {
/* 154 */     long mul = -7286425919675154353L + length * 2L;
/* 155 */     long a = LittleEndianByteArray.load64(bytes, offset) * -7286425919675154353L;
/* 156 */     long b = LittleEndianByteArray.load64(bytes, offset + 8);
/* 157 */     long c = LittleEndianByteArray.load64(bytes, offset + length - 8) * mul;
/* 158 */     long d = LittleEndianByteArray.load64(bytes, offset + length - 16) * -7286425919675154353L;
/* 159 */     long y = Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d;
/* 160 */     long z = hashLength16(y, a + Long.rotateRight(b + -7286425919675154353L, 18) + c, mul);
/* 161 */     long e = LittleEndianByteArray.load64(bytes, offset + 16) * mul;
/* 162 */     long f = LittleEndianByteArray.load64(bytes, offset + 24);
/* 163 */     long g = (y + LittleEndianByteArray.load64(bytes, offset + length - 32)) * mul;
/* 164 */     long h = (z + LittleEndianByteArray.load64(bytes, offset + length - 24)) * mul;
/* 165 */     return hashLength16(
/* 166 */         Long.rotateRight(e + f, 43) + Long.rotateRight(g, 30) + h, e + Long.rotateRight(f + a, 18) + g, mul);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long hashLength65Plus(byte[] bytes, int offset, int length) {
/* 173 */     int seed = 81;
/*     */     
/* 175 */     long x = seed;
/*     */     
/* 177 */     long y = seed * -5435081209227447693L + 113L;
/* 178 */     long z = shiftMix(y * -7286425919675154353L + 113L) * -7286425919675154353L;
/* 179 */     long[] v = new long[2];
/* 180 */     long[] w = new long[2];
/* 181 */     x = x * -7286425919675154353L + LittleEndianByteArray.load64(bytes, offset);
/*     */ 
/*     */     
/* 184 */     int end = offset + (length - 1) / 64 * 64;
/* 185 */     int last64offset = end + (length - 1 & 0x3F) - 63;
/*     */     while (true) {
/* 187 */       x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * -5435081209227447693L;
/* 188 */       y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * -5435081209227447693L;
/* 189 */       x ^= w[1];
/* 190 */       y += v[0] + LittleEndianByteArray.load64(bytes, offset + 40);
/* 191 */       z = Long.rotateRight(z + w[0], 33) * -5435081209227447693L;
/* 192 */       weakHashLength32WithSeeds(bytes, offset, v[1] * -5435081209227447693L, x + w[0], v);
/* 193 */       weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
/* 194 */       long tmp = x;
/* 195 */       x = z;
/* 196 */       z = tmp;
/* 197 */       offset += 64;
/* 198 */       if (offset == end) {
/* 199 */         long mul = -5435081209227447693L + ((z & 0xFFL) << 1L);
/*     */         
/* 201 */         offset = last64offset;
/* 202 */         w[0] = w[0] + (length - 1 & 0x3F);
/* 203 */         v[0] = v[0] + w[0];
/* 204 */         w[0] = w[0] + v[0];
/* 205 */         x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * mul;
/* 206 */         y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * mul;
/* 207 */         x ^= w[1] * 9L;
/* 208 */         y += v[0] * 9L + LittleEndianByteArray.load64(bytes, offset + 40);
/* 209 */         z = Long.rotateRight(z + w[0], 33) * mul;
/* 210 */         weakHashLength32WithSeeds(bytes, offset, v[1] * mul, x + w[0], v);
/* 211 */         weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
/* 212 */         return hashLength16(
/* 213 */             hashLength16(v[0], w[0], mul) + shiftMix(y) * -4348849565147123417L + x, 
/* 214 */             hashLength16(v[1], w[1], mul) + z, mul);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/com/google/common/hash/FarmHashFingerprint64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */