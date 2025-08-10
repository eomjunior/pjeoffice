/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat576;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT571Field
/*     */ {
/*     */   private static final long M59 = 576460752303423487L;
/*  13 */   private static final long[] ROOT_Z = new long[] { 3161836309350906777L, -7642453882179322845L, -3821226941089661423L, 7312758566309945096L, -556661012383879292L, 8945041530681231562L, -4750851271514160027L, 6847946401097695794L, 541669439031730457L };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void add(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  19 */     for (byte b = 0; b < 9; b++)
/*     */     {
/*  21 */       paramArrayOflong3[b] = paramArrayOflong1[b] ^ paramArrayOflong2[b];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void add(long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2, long[] paramArrayOflong3, int paramInt3) {
/*  27 */     for (byte b = 0; b < 9; b++)
/*     */     {
/*  29 */       paramArrayOflong3[paramInt3 + b] = paramArrayOflong1[paramInt1 + b] ^ paramArrayOflong2[paramInt2 + b];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addBothTo(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  35 */     for (byte b = 0; b < 9; b++)
/*     */     {
/*  37 */       paramArrayOflong3[b] = paramArrayOflong3[b] ^ paramArrayOflong1[b] ^ paramArrayOflong2[b];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addBothTo(long[] paramArrayOflong1, int paramInt1, long[] paramArrayOflong2, int paramInt2, long[] paramArrayOflong3, int paramInt3) {
/*  43 */     for (byte b = 0; b < 9; b++)
/*     */     {
/*  45 */       paramArrayOflong3[paramInt3 + b] = paramArrayOflong3[paramInt3 + b] ^ paramArrayOflong1[paramInt1 + b] ^ paramArrayOflong2[paramInt2 + b];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  51 */     for (byte b = 0; b < 18; b++)
/*     */     {
/*  53 */       paramArrayOflong3[b] = paramArrayOflong1[b] ^ paramArrayOflong2[b];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  59 */     paramArrayOflong2[0] = paramArrayOflong1[0] ^ 0x1L;
/*  60 */     for (byte b = 1; b < 9; b++)
/*     */     {
/*  62 */       paramArrayOflong2[b] = paramArrayOflong1[b];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  68 */     for (byte b = 0; b < 9; b++)
/*     */     {
/*  70 */       paramArrayOflong2[b] = paramArrayOflong2[b] ^ paramArrayOflong1[b];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger(BigInteger paramBigInteger) {
/*  76 */     return Nat.fromBigInteger64(571, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  81 */     long[] arrayOfLong = Nat576.createExt64();
/*     */     
/*  83 */     Nat576.copy64(paramArrayOflong1, paramArrayOflong2);
/*  84 */     for (byte b = 1; b < 'Ȼ'; b += 2) {
/*     */       
/*  86 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  87 */       reduce(arrayOfLong, paramArrayOflong2);
/*  88 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  89 */       reduce(arrayOfLong, paramArrayOflong2);
/*  90 */       addTo(paramArrayOflong1, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invert(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  96 */     if (Nat576.isZero64(paramArrayOflong1))
/*     */     {
/*  98 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 103 */     long[] arrayOfLong1 = Nat576.create64();
/* 104 */     long[] arrayOfLong2 = Nat576.create64();
/* 105 */     long[] arrayOfLong3 = Nat576.create64();
/*     */     
/* 107 */     square(paramArrayOflong1, arrayOfLong3);
/*     */ 
/*     */     
/* 110 */     square(arrayOfLong3, arrayOfLong1);
/* 111 */     square(arrayOfLong1, arrayOfLong2);
/* 112 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 113 */     squareN(arrayOfLong1, 2, arrayOfLong2);
/* 114 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 115 */     multiply(arrayOfLong1, arrayOfLong3, arrayOfLong1);
/*     */ 
/*     */     
/* 118 */     squareN(arrayOfLong1, 5, arrayOfLong2);
/* 119 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 120 */     squareN(arrayOfLong2, 5, arrayOfLong2);
/* 121 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 124 */     squareN(arrayOfLong1, 15, arrayOfLong2);
/* 125 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong3);
/*     */ 
/*     */     
/* 128 */     squareN(arrayOfLong3, 30, arrayOfLong1);
/* 129 */     squareN(arrayOfLong1, 30, arrayOfLong2);
/* 130 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 133 */     squareN(arrayOfLong1, 60, arrayOfLong2);
/* 134 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 135 */     squareN(arrayOfLong2, 60, arrayOfLong2);
/* 136 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 139 */     squareN(arrayOfLong1, 180, arrayOfLong2);
/* 140 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 141 */     squareN(arrayOfLong2, 180, arrayOfLong2);
/* 142 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */     
/* 144 */     multiply(arrayOfLong1, arrayOfLong3, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 149 */     long[] arrayOfLong = Nat576.createExt64();
/* 150 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 151 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 156 */     long[] arrayOfLong = Nat576.createExt64();
/* 157 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 158 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyPrecomp(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 163 */     long[] arrayOfLong = Nat576.createExt64();
/* 164 */     implMultiplyPrecomp(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 165 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyPrecompAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 170 */     long[] arrayOfLong = Nat576.createExt64();
/* 171 */     implMultiplyPrecomp(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 172 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long[] precompMultiplicand(long[] paramArrayOflong) {
/* 180 */     char c = '';
/* 181 */     long[] arrayOfLong = new long[c << 1];
/* 182 */     System.arraycopy(paramArrayOflong, 0, arrayOfLong, 9, 9);
/*     */     
/* 184 */     byte b1 = 0;
/* 185 */     for (byte b2 = 7; b2 > 0; b2--) {
/*     */       
/* 187 */       b1 += true;
/* 188 */       Nat.shiftUpBit64(9, arrayOfLong, b1 >>> 1, 0L, arrayOfLong, b1);
/* 189 */       reduce5(arrayOfLong, b1);
/* 190 */       add(arrayOfLong, 9, arrayOfLong, b1, arrayOfLong, b1 + 9);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 196 */     Nat.shiftUpBits64(c, arrayOfLong, 0, 4, 0L, arrayOfLong, c);
/*     */     
/* 198 */     return arrayOfLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 203 */     long l1 = paramArrayOflong1[9];
/* 204 */     long l2 = paramArrayOflong1[17], l3 = l1;
/*     */     
/* 206 */     l1 = l3 ^ l2 >>> 59L ^ l2 >>> 57L ^ l2 >>> 54L ^ l2 >>> 49L;
/* 207 */     l3 = paramArrayOflong1[8] ^ l2 << 5L ^ l2 << 7L ^ l2 << 10L ^ l2 << 15L;
/*     */     
/* 209 */     for (byte b = 16; b >= 10; b--) {
/*     */       
/* 211 */       l2 = paramArrayOflong1[b];
/* 212 */       paramArrayOflong2[b - 8] = l3 ^ l2 >>> 59L ^ l2 >>> 57L ^ l2 >>> 54L ^ l2 >>> 49L;
/* 213 */       l3 = paramArrayOflong1[b - 9] ^ l2 << 5L ^ l2 << 7L ^ l2 << 10L ^ l2 << 15L;
/*     */     } 
/*     */     
/* 216 */     l2 = l1;
/* 217 */     paramArrayOflong2[1] = l3 ^ l2 >>> 59L ^ l2 >>> 57L ^ l2 >>> 54L ^ l2 >>> 49L;
/* 218 */     l3 = paramArrayOflong1[0] ^ l2 << 5L ^ l2 << 7L ^ l2 << 10L ^ l2 << 15L;
/*     */     
/* 220 */     long l4 = paramArrayOflong2[8];
/* 221 */     long l5 = l4 >>> 59L;
/* 222 */     paramArrayOflong2[0] = l3 ^ l5 ^ l5 << 2L ^ l5 << 5L ^ l5 << 10L;
/* 223 */     paramArrayOflong2[8] = l4 & 0x7FFFFFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce5(long[] paramArrayOflong, int paramInt) {
/* 228 */     long l1 = paramArrayOflong[paramInt + 8], l2 = l1 >>> 59L;
/* 229 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2 ^ l2 << 2L ^ l2 << 5L ^ l2 << 10L;
/* 230 */     paramArrayOflong[paramInt + 8] = l1 & 0x7FFFFFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 235 */     long[] arrayOfLong1 = Nat576.create64(), arrayOfLong2 = Nat576.create64();
/*     */     
/* 237 */     byte b1 = 0;
/* 238 */     for (byte b2 = 0; b2 < 4; b2++) {
/*     */       
/* 240 */       long l1 = Interleave.unshuffle(paramArrayOflong1[b1++]);
/* 241 */       long l2 = Interleave.unshuffle(paramArrayOflong1[b1++]);
/* 242 */       arrayOfLong1[b2] = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 243 */       arrayOfLong2[b2] = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     } 
/*     */     
/* 246 */     long l = Interleave.unshuffle(paramArrayOflong1[b1]);
/* 247 */     arrayOfLong1[4] = l & 0xFFFFFFFFL;
/* 248 */     arrayOfLong2[4] = l >>> 32L;
/*     */ 
/*     */     
/* 251 */     multiply(arrayOfLong2, ROOT_Z, paramArrayOflong2);
/* 252 */     add(paramArrayOflong2, arrayOfLong1, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 257 */     long[] arrayOfLong = Nat576.createExt64();
/* 258 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 259 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 264 */     long[] arrayOfLong = Nat576.createExt64();
/* 265 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 266 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 273 */     long[] arrayOfLong = Nat576.createExt64();
/* 274 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 275 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 277 */     while (--paramInt > 0) {
/*     */       
/* 279 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 280 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 287 */     return (int)(paramArrayOflong[0] ^ paramArrayOflong[8] >>> 49L ^ paramArrayOflong[8] >>> 57L) & 0x1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 296 */     long[] arrayOfLong = new long[16];
/* 297 */     for (byte b = 0; b < 9; b++)
/*     */     {
/* 299 */       implMulwAcc(arrayOfLong, paramArrayOflong1[b], paramArrayOflong2[b], paramArrayOflong3, b << 1);
/*     */     }
/*     */     
/* 302 */     long l1 = paramArrayOflong3[0], l2 = paramArrayOflong3[1];
/* 303 */     l1 ^= paramArrayOflong3[2]; paramArrayOflong3[1] = l1 ^ l2; l2 ^= paramArrayOflong3[3];
/* 304 */     l1 ^= paramArrayOflong3[4]; paramArrayOflong3[2] = l1 ^ l2; l2 ^= paramArrayOflong3[5];
/* 305 */     l1 ^= paramArrayOflong3[6]; paramArrayOflong3[3] = l1 ^ l2; l2 ^= paramArrayOflong3[7];
/* 306 */     l1 ^= paramArrayOflong3[8]; paramArrayOflong3[4] = l1 ^ l2; l2 ^= paramArrayOflong3[9];
/* 307 */     l1 ^= paramArrayOflong3[10]; paramArrayOflong3[5] = l1 ^ l2; l2 ^= paramArrayOflong3[11];
/* 308 */     l1 ^= paramArrayOflong3[12]; paramArrayOflong3[6] = l1 ^ l2; l2 ^= paramArrayOflong3[13];
/* 309 */     l1 ^= paramArrayOflong3[14]; paramArrayOflong3[7] = l1 ^ l2; l2 ^= paramArrayOflong3[15];
/* 310 */     l1 ^= paramArrayOflong3[16]; paramArrayOflong3[8] = l1 ^ l2; l2 ^= paramArrayOflong3[17];
/*     */     
/* 312 */     long l3 = l1 ^ l2;
/* 313 */     paramArrayOflong3[9] = paramArrayOflong3[0] ^ l3;
/* 314 */     paramArrayOflong3[10] = paramArrayOflong3[1] ^ l3;
/* 315 */     paramArrayOflong3[11] = paramArrayOflong3[2] ^ l3;
/* 316 */     paramArrayOflong3[12] = paramArrayOflong3[3] ^ l3;
/* 317 */     paramArrayOflong3[13] = paramArrayOflong3[4] ^ l3;
/* 318 */     paramArrayOflong3[14] = paramArrayOflong3[5] ^ l3;
/* 319 */     paramArrayOflong3[15] = paramArrayOflong3[6] ^ l3;
/* 320 */     paramArrayOflong3[16] = paramArrayOflong3[7] ^ l3;
/* 321 */     paramArrayOflong3[17] = paramArrayOflong3[8] ^ l3;
/*     */     
/* 323 */     implMulwAcc(arrayOfLong, paramArrayOflong1[0] ^ paramArrayOflong1[1], paramArrayOflong2[0] ^ paramArrayOflong2[1], paramArrayOflong3, 1);
/*     */     
/* 325 */     implMulwAcc(arrayOfLong, paramArrayOflong1[0] ^ paramArrayOflong1[2], paramArrayOflong2[0] ^ paramArrayOflong2[2], paramArrayOflong3, 2);
/*     */     
/* 327 */     implMulwAcc(arrayOfLong, paramArrayOflong1[0] ^ paramArrayOflong1[3], paramArrayOflong2[0] ^ paramArrayOflong2[3], paramArrayOflong3, 3);
/* 328 */     implMulwAcc(arrayOfLong, paramArrayOflong1[1] ^ paramArrayOflong1[2], paramArrayOflong2[1] ^ paramArrayOflong2[2], paramArrayOflong3, 3);
/*     */     
/* 330 */     implMulwAcc(arrayOfLong, paramArrayOflong1[0] ^ paramArrayOflong1[4], paramArrayOflong2[0] ^ paramArrayOflong2[4], paramArrayOflong3, 4);
/* 331 */     implMulwAcc(arrayOfLong, paramArrayOflong1[1] ^ paramArrayOflong1[3], paramArrayOflong2[1] ^ paramArrayOflong2[3], paramArrayOflong3, 4);
/*     */     
/* 333 */     implMulwAcc(arrayOfLong, paramArrayOflong1[0] ^ paramArrayOflong1[5], paramArrayOflong2[0] ^ paramArrayOflong2[5], paramArrayOflong3, 5);
/* 334 */     implMulwAcc(arrayOfLong, paramArrayOflong1[1] ^ paramArrayOflong1[4], paramArrayOflong2[1] ^ paramArrayOflong2[4], paramArrayOflong3, 5);
/* 335 */     implMulwAcc(arrayOfLong, paramArrayOflong1[2] ^ paramArrayOflong1[3], paramArrayOflong2[2] ^ paramArrayOflong2[3], paramArrayOflong3, 5);
/*     */     
/* 337 */     implMulwAcc(arrayOfLong, paramArrayOflong1[0] ^ paramArrayOflong1[6], paramArrayOflong2[0] ^ paramArrayOflong2[6], paramArrayOflong3, 6);
/* 338 */     implMulwAcc(arrayOfLong, paramArrayOflong1[1] ^ paramArrayOflong1[5], paramArrayOflong2[1] ^ paramArrayOflong2[5], paramArrayOflong3, 6);
/* 339 */     implMulwAcc(arrayOfLong, paramArrayOflong1[2] ^ paramArrayOflong1[4], paramArrayOflong2[2] ^ paramArrayOflong2[4], paramArrayOflong3, 6);
/*     */     
/* 341 */     implMulwAcc(arrayOfLong, paramArrayOflong1[0] ^ paramArrayOflong1[7], paramArrayOflong2[0] ^ paramArrayOflong2[7], paramArrayOflong3, 7);
/* 342 */     implMulwAcc(arrayOfLong, paramArrayOflong1[1] ^ paramArrayOflong1[6], paramArrayOflong2[1] ^ paramArrayOflong2[6], paramArrayOflong3, 7);
/* 343 */     implMulwAcc(arrayOfLong, paramArrayOflong1[2] ^ paramArrayOflong1[5], paramArrayOflong2[2] ^ paramArrayOflong2[5], paramArrayOflong3, 7);
/* 344 */     implMulwAcc(arrayOfLong, paramArrayOflong1[3] ^ paramArrayOflong1[4], paramArrayOflong2[3] ^ paramArrayOflong2[4], paramArrayOflong3, 7);
/*     */     
/* 346 */     implMulwAcc(arrayOfLong, paramArrayOflong1[0] ^ paramArrayOflong1[8], paramArrayOflong2[0] ^ paramArrayOflong2[8], paramArrayOflong3, 8);
/* 347 */     implMulwAcc(arrayOfLong, paramArrayOflong1[1] ^ paramArrayOflong1[7], paramArrayOflong2[1] ^ paramArrayOflong2[7], paramArrayOflong3, 8);
/* 348 */     implMulwAcc(arrayOfLong, paramArrayOflong1[2] ^ paramArrayOflong1[6], paramArrayOflong2[2] ^ paramArrayOflong2[6], paramArrayOflong3, 8);
/* 349 */     implMulwAcc(arrayOfLong, paramArrayOflong1[3] ^ paramArrayOflong1[5], paramArrayOflong2[3] ^ paramArrayOflong2[5], paramArrayOflong3, 8);
/*     */     
/* 351 */     implMulwAcc(arrayOfLong, paramArrayOflong1[1] ^ paramArrayOflong1[8], paramArrayOflong2[1] ^ paramArrayOflong2[8], paramArrayOflong3, 9);
/* 352 */     implMulwAcc(arrayOfLong, paramArrayOflong1[2] ^ paramArrayOflong1[7], paramArrayOflong2[2] ^ paramArrayOflong2[7], paramArrayOflong3, 9);
/* 353 */     implMulwAcc(arrayOfLong, paramArrayOflong1[3] ^ paramArrayOflong1[6], paramArrayOflong2[3] ^ paramArrayOflong2[6], paramArrayOflong3, 9);
/* 354 */     implMulwAcc(arrayOfLong, paramArrayOflong1[4] ^ paramArrayOflong1[5], paramArrayOflong2[4] ^ paramArrayOflong2[5], paramArrayOflong3, 9);
/*     */     
/* 356 */     implMulwAcc(arrayOfLong, paramArrayOflong1[2] ^ paramArrayOflong1[8], paramArrayOflong2[2] ^ paramArrayOflong2[8], paramArrayOflong3, 10);
/* 357 */     implMulwAcc(arrayOfLong, paramArrayOflong1[3] ^ paramArrayOflong1[7], paramArrayOflong2[3] ^ paramArrayOflong2[7], paramArrayOflong3, 10);
/* 358 */     implMulwAcc(arrayOfLong, paramArrayOflong1[4] ^ paramArrayOflong1[6], paramArrayOflong2[4] ^ paramArrayOflong2[6], paramArrayOflong3, 10);
/*     */     
/* 360 */     implMulwAcc(arrayOfLong, paramArrayOflong1[3] ^ paramArrayOflong1[8], paramArrayOflong2[3] ^ paramArrayOflong2[8], paramArrayOflong3, 11);
/* 361 */     implMulwAcc(arrayOfLong, paramArrayOflong1[4] ^ paramArrayOflong1[7], paramArrayOflong2[4] ^ paramArrayOflong2[7], paramArrayOflong3, 11);
/* 362 */     implMulwAcc(arrayOfLong, paramArrayOflong1[5] ^ paramArrayOflong1[6], paramArrayOflong2[5] ^ paramArrayOflong2[6], paramArrayOflong3, 11);
/*     */     
/* 364 */     implMulwAcc(arrayOfLong, paramArrayOflong1[4] ^ paramArrayOflong1[8], paramArrayOflong2[4] ^ paramArrayOflong2[8], paramArrayOflong3, 12);
/* 365 */     implMulwAcc(arrayOfLong, paramArrayOflong1[5] ^ paramArrayOflong1[7], paramArrayOflong2[5] ^ paramArrayOflong2[7], paramArrayOflong3, 12);
/*     */     
/* 367 */     implMulwAcc(arrayOfLong, paramArrayOflong1[5] ^ paramArrayOflong1[8], paramArrayOflong2[5] ^ paramArrayOflong2[8], paramArrayOflong3, 13);
/* 368 */     implMulwAcc(arrayOfLong, paramArrayOflong1[6] ^ paramArrayOflong1[7], paramArrayOflong2[6] ^ paramArrayOflong2[7], paramArrayOflong3, 13);
/*     */     
/* 370 */     implMulwAcc(arrayOfLong, paramArrayOflong1[6] ^ paramArrayOflong1[8], paramArrayOflong2[6] ^ paramArrayOflong2[8], paramArrayOflong3, 14);
/*     */     
/* 372 */     implMulwAcc(arrayOfLong, paramArrayOflong1[7] ^ paramArrayOflong1[8], paramArrayOflong2[7] ^ paramArrayOflong2[8], paramArrayOflong3, 15);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implMultiplyPrecomp(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 377 */     byte b1 = 15;
/*     */ 
/*     */     
/*     */     byte b2;
/*     */ 
/*     */     
/* 383 */     for (b2 = 56; b2 >= 0; b2 -= 8) {
/*     */       
/* 385 */       for (byte b = 1; b < 9; b += 2) {
/*     */         
/* 387 */         int i = (int)(paramArrayOflong1[b] >>> b2);
/* 388 */         int j = i & b1;
/* 389 */         int k = i >>> 4 & b1;
/* 390 */         addBothTo(paramArrayOflong2, 9 * j, paramArrayOflong2, 9 * (k + 16), paramArrayOflong3, b - 1);
/*     */       } 
/* 392 */       Nat.shiftUpBits64(16, paramArrayOflong3, 0, 8, 0L);
/*     */     } 
/*     */     
/* 395 */     for (b2 = 56; b2 >= 0; b2 -= 8) {
/*     */       
/* 397 */       for (byte b = 0; b < 9; b += 2) {
/*     */         
/* 399 */         int i = (int)(paramArrayOflong1[b] >>> b2);
/* 400 */         int j = i & b1;
/* 401 */         int k = i >>> 4 & b1;
/* 402 */         addBothTo(paramArrayOflong2, 9 * j, paramArrayOflong2, 9 * (k + 16), paramArrayOflong3, b);
/*     */       } 
/* 404 */       if (b2 > 0)
/*     */       {
/* 406 */         Nat.shiftUpBits64(18, paramArrayOflong3, 0, 8, 0L);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulwAcc(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 414 */     paramArrayOflong1[1] = paramLong2; int i;
/* 415 */     for (i = 2; i < 16; i += 2) {
/*     */       
/* 417 */       paramArrayOflong1[i] = paramArrayOflong1[i >>> 1] << 1L;
/* 418 */       paramArrayOflong1[i + 1] = paramArrayOflong1[i] ^ paramLong2;
/*     */     } 
/*     */     
/* 421 */     i = (int)paramLong1;
/* 422 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0xF] ^ paramArrayOflong1[i >>> 4 & 0xF] << 4L;
/*     */     
/* 424 */     byte b1 = 56;
/*     */ 
/*     */     
/* 427 */     do { i = (int)(paramLong1 >>> b1);
/* 428 */       long l = paramArrayOflong1[i & 0xF] ^ paramArrayOflong1[i >>> 4 & 0xF] << 4L;
/*     */       
/* 430 */       l2 ^= l << b1;
/* 431 */       l1 ^= l >>> -b1;
/*     */       
/* 433 */       b1 -= 8; } while (b1 > 0);
/*     */     
/* 435 */     for (byte b2 = 0; b2 < 7; b2++) {
/*     */       
/* 437 */       paramLong1 = (paramLong1 & 0xFEFEFEFEFEFEFEFEL) >>> 1L;
/* 438 */       l1 ^= paramLong1 & paramLong2 << b2 >> 63L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 443 */     paramArrayOflong2[paramInt] = paramArrayOflong2[paramInt] ^ l2;
/* 444 */     paramArrayOflong2[paramInt + 1] = paramArrayOflong2[paramInt + 1] ^ l1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 449 */     Interleave.expand64To128(paramArrayOflong1, 0, 9, paramArrayOflong2, 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT571Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */