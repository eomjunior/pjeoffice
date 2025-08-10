/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat320;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT283Field
/*     */ {
/*     */   private static final long M27 = 134217727L;
/*     */   private static final long M57 = 144115188075855871L;
/*  14 */   private static final long[] ROOT_Z = new long[] { 878416384462358536L, 3513665537849438403L, -9076969306111048948L, 585610922974906400L, 34087042L };
/*     */ 
/*     */ 
/*     */   
/*     */   public static void add(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  19 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  20 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*  21 */     paramArrayOflong3[2] = paramArrayOflong1[2] ^ paramArrayOflong2[2];
/*  22 */     paramArrayOflong3[3] = paramArrayOflong1[3] ^ paramArrayOflong2[3];
/*  23 */     paramArrayOflong3[4] = paramArrayOflong1[4] ^ paramArrayOflong2[4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  28 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  29 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*  30 */     paramArrayOflong3[2] = paramArrayOflong1[2] ^ paramArrayOflong2[2];
/*  31 */     paramArrayOflong3[3] = paramArrayOflong1[3] ^ paramArrayOflong2[3];
/*  32 */     paramArrayOflong3[4] = paramArrayOflong1[4] ^ paramArrayOflong2[4];
/*  33 */     paramArrayOflong3[5] = paramArrayOflong1[5] ^ paramArrayOflong2[5];
/*  34 */     paramArrayOflong3[6] = paramArrayOflong1[6] ^ paramArrayOflong2[6];
/*  35 */     paramArrayOflong3[7] = paramArrayOflong1[7] ^ paramArrayOflong2[7];
/*  36 */     paramArrayOflong3[8] = paramArrayOflong1[8] ^ paramArrayOflong2[8];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  41 */     paramArrayOflong2[0] = paramArrayOflong1[0] ^ 0x1L;
/*  42 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  43 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*  44 */     paramArrayOflong2[3] = paramArrayOflong1[3];
/*  45 */     paramArrayOflong2[4] = paramArrayOflong1[4];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  50 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ paramArrayOflong1[0];
/*  51 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ paramArrayOflong1[1];
/*  52 */     paramArrayOflong2[2] = paramArrayOflong2[2] ^ paramArrayOflong1[2];
/*  53 */     paramArrayOflong2[3] = paramArrayOflong2[3] ^ paramArrayOflong1[3];
/*  54 */     paramArrayOflong2[4] = paramArrayOflong2[4] ^ paramArrayOflong1[4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger(BigInteger paramBigInteger) {
/*  59 */     return Nat.fromBigInteger64(283, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  64 */     long[] arrayOfLong = Nat.create64(9);
/*     */     
/*  66 */     Nat320.copy64(paramArrayOflong1, paramArrayOflong2);
/*  67 */     for (byte b = 1; b < 'ě'; b += 2) {
/*     */       
/*  69 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  70 */       reduce(arrayOfLong, paramArrayOflong2);
/*  71 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  72 */       reduce(arrayOfLong, paramArrayOflong2);
/*  73 */       addTo(paramArrayOflong1, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invert(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  79 */     if (Nat320.isZero64(paramArrayOflong1))
/*     */     {
/*  81 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  86 */     long[] arrayOfLong1 = Nat320.create64();
/*  87 */     long[] arrayOfLong2 = Nat320.create64();
/*     */     
/*  89 */     square(paramArrayOflong1, arrayOfLong1);
/*  90 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/*  91 */     squareN(arrayOfLong1, 2, arrayOfLong2);
/*  92 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  93 */     squareN(arrayOfLong2, 4, arrayOfLong1);
/*  94 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  95 */     squareN(arrayOfLong1, 8, arrayOfLong2);
/*  96 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  97 */     square(arrayOfLong2, arrayOfLong2);
/*  98 */     multiply(arrayOfLong2, paramArrayOflong1, arrayOfLong2);
/*  99 */     squareN(arrayOfLong2, 17, arrayOfLong1);
/* 100 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 101 */     square(arrayOfLong1, arrayOfLong1);
/* 102 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/* 103 */     squareN(arrayOfLong1, 35, arrayOfLong2);
/* 104 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/* 105 */     squareN(arrayOfLong2, 70, arrayOfLong1);
/* 106 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 107 */     square(arrayOfLong1, arrayOfLong1);
/* 108 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/* 109 */     squareN(arrayOfLong1, 141, arrayOfLong2);
/* 110 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/* 111 */     square(arrayOfLong2, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 116 */     long[] arrayOfLong = Nat320.createExt64();
/* 117 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 118 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 123 */     long[] arrayOfLong = Nat320.createExt64();
/* 124 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 125 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 130 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3], l5 = paramArrayOflong1[4];
/* 131 */     long l6 = paramArrayOflong1[5], l7 = paramArrayOflong1[6], l8 = paramArrayOflong1[7], l9 = paramArrayOflong1[8];
/*     */     
/* 133 */     l4 ^= l9 << 37L ^ l9 << 42L ^ l9 << 44L ^ l9 << 49L;
/* 134 */     l5 ^= l9 >>> 27L ^ l9 >>> 22L ^ l9 >>> 20L ^ l9 >>> 15L;
/*     */     
/* 136 */     l3 ^= l8 << 37L ^ l8 << 42L ^ l8 << 44L ^ l8 << 49L;
/* 137 */     l4 ^= l8 >>> 27L ^ l8 >>> 22L ^ l8 >>> 20L ^ l8 >>> 15L;
/*     */     
/* 139 */     l2 ^= l7 << 37L ^ l7 << 42L ^ l7 << 44L ^ l7 << 49L;
/* 140 */     l3 ^= l7 >>> 27L ^ l7 >>> 22L ^ l7 >>> 20L ^ l7 >>> 15L;
/*     */     
/* 142 */     l1 ^= l6 << 37L ^ l6 << 42L ^ l6 << 44L ^ l6 << 49L;
/* 143 */     l2 ^= l6 >>> 27L ^ l6 >>> 22L ^ l6 >>> 20L ^ l6 >>> 15L;
/*     */     
/* 145 */     long l10 = l5 >>> 27L;
/* 146 */     paramArrayOflong2[0] = l1 ^ l10 ^ l10 << 5L ^ l10 << 7L ^ l10 << 12L;
/* 147 */     paramArrayOflong2[1] = l2;
/* 148 */     paramArrayOflong2[2] = l3;
/* 149 */     paramArrayOflong2[3] = l4;
/* 150 */     paramArrayOflong2[4] = l5 & 0x7FFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce37(long[] paramArrayOflong, int paramInt) {
/* 155 */     long l1 = paramArrayOflong[paramInt + 4], l2 = l1 >>> 27L;
/* 156 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2 ^ l2 << 5L ^ l2 << 7L ^ l2 << 12L;
/* 157 */     paramArrayOflong[paramInt + 4] = l1 & 0x7FFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 162 */     long[] arrayOfLong = Nat320.create64();
/*     */ 
/*     */     
/* 165 */     long l1 = Interleave.unshuffle(paramArrayOflong1[0]), l2 = Interleave.unshuffle(paramArrayOflong1[1]);
/* 166 */     long l3 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 167 */     arrayOfLong[0] = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 169 */     l1 = Interleave.unshuffle(paramArrayOflong1[2]); l2 = Interleave.unshuffle(paramArrayOflong1[3]);
/* 170 */     long l4 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 171 */     arrayOfLong[1] = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 173 */     l1 = Interleave.unshuffle(paramArrayOflong1[4]);
/* 174 */     long l5 = l1 & 0xFFFFFFFFL;
/* 175 */     arrayOfLong[2] = l1 >>> 32L;
/*     */     
/* 177 */     multiply(arrayOfLong, ROOT_Z, paramArrayOflong2);
/*     */     
/* 179 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ l3;
/* 180 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ l4;
/* 181 */     paramArrayOflong2[2] = paramArrayOflong2[2] ^ l5;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 186 */     long[] arrayOfLong = Nat.create64(9);
/* 187 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 188 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 193 */     long[] arrayOfLong = Nat.create64(9);
/* 194 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 195 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 202 */     long[] arrayOfLong = Nat.create64(9);
/* 203 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 204 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 206 */     while (--paramInt > 0) {
/*     */       
/* 208 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 209 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 216 */     return (int)(paramArrayOflong[0] ^ paramArrayOflong[4] >>> 15L) & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implCompactExt(long[] paramArrayOflong) {
/* 221 */     long l1 = paramArrayOflong[0], l2 = paramArrayOflong[1], l3 = paramArrayOflong[2], l4 = paramArrayOflong[3], l5 = paramArrayOflong[4];
/* 222 */     long l6 = paramArrayOflong[5], l7 = paramArrayOflong[6], l8 = paramArrayOflong[7], l9 = paramArrayOflong[8], l10 = paramArrayOflong[9];
/* 223 */     paramArrayOflong[0] = l1 ^ l2 << 57L;
/* 224 */     paramArrayOflong[1] = l2 >>> 7L ^ l3 << 50L;
/* 225 */     paramArrayOflong[2] = l3 >>> 14L ^ l4 << 43L;
/* 226 */     paramArrayOflong[3] = l4 >>> 21L ^ l5 << 36L;
/* 227 */     paramArrayOflong[4] = l5 >>> 28L ^ l6 << 29L;
/* 228 */     paramArrayOflong[5] = l6 >>> 35L ^ l7 << 22L;
/* 229 */     paramArrayOflong[6] = l7 >>> 42L ^ l8 << 15L;
/* 230 */     paramArrayOflong[7] = l8 >>> 49L ^ l9 << 8L;
/* 231 */     paramArrayOflong[8] = l9 >>> 56L ^ l10 << 1L;
/* 232 */     paramArrayOflong[9] = l10 >>> 63L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implExpand(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 237 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3], l5 = paramArrayOflong1[4];
/* 238 */     paramArrayOflong2[0] = l1 & 0x1FFFFFFFFFFFFFFL;
/* 239 */     paramArrayOflong2[1] = (l1 >>> 57L ^ l2 << 7L) & 0x1FFFFFFFFFFFFFFL;
/* 240 */     paramArrayOflong2[2] = (l2 >>> 50L ^ l3 << 14L) & 0x1FFFFFFFFFFFFFFL;
/* 241 */     paramArrayOflong2[3] = (l3 >>> 43L ^ l4 << 21L) & 0x1FFFFFFFFFFFFFFL;
/* 242 */     paramArrayOflong2[4] = l4 >>> 36L ^ l5 << 28L;
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
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 266 */     long[] arrayOfLong1 = new long[5], arrayOfLong2 = new long[5];
/* 267 */     implExpand(paramArrayOflong1, arrayOfLong1);
/* 268 */     implExpand(paramArrayOflong2, arrayOfLong2);
/*     */     
/* 270 */     long[] arrayOfLong3 = paramArrayOflong3;
/* 271 */     long[] arrayOfLong4 = new long[26];
/*     */     
/* 273 */     implMulw(arrayOfLong3, arrayOfLong1[0], arrayOfLong2[0], arrayOfLong4, 0);
/* 274 */     implMulw(arrayOfLong3, arrayOfLong1[1], arrayOfLong2[1], arrayOfLong4, 2);
/* 275 */     implMulw(arrayOfLong3, arrayOfLong1[2], arrayOfLong2[2], arrayOfLong4, 4);
/* 276 */     implMulw(arrayOfLong3, arrayOfLong1[3], arrayOfLong2[3], arrayOfLong4, 6);
/* 277 */     implMulw(arrayOfLong3, arrayOfLong1[4], arrayOfLong2[4], arrayOfLong4, 8);
/*     */     
/* 279 */     long l1 = arrayOfLong1[0] ^ arrayOfLong1[1], l2 = arrayOfLong2[0] ^ arrayOfLong2[1];
/* 280 */     long l3 = arrayOfLong1[0] ^ arrayOfLong1[2], l4 = arrayOfLong2[0] ^ arrayOfLong2[2];
/* 281 */     long l5 = arrayOfLong1[2] ^ arrayOfLong1[4], l6 = arrayOfLong2[2] ^ arrayOfLong2[4];
/* 282 */     long l7 = arrayOfLong1[3] ^ arrayOfLong1[4], l8 = arrayOfLong2[3] ^ arrayOfLong2[4];
/*     */     
/* 284 */     implMulw(arrayOfLong3, l3 ^ arrayOfLong1[3], l4 ^ arrayOfLong2[3], arrayOfLong4, 18);
/* 285 */     implMulw(arrayOfLong3, l5 ^ arrayOfLong1[1], l6 ^ arrayOfLong2[1], arrayOfLong4, 20);
/*     */     
/* 287 */     long l9 = l1 ^ l7, l10 = l2 ^ l8;
/* 288 */     long l11 = l9 ^ arrayOfLong1[2], l12 = l10 ^ arrayOfLong2[2];
/*     */     
/* 290 */     implMulw(arrayOfLong3, l9, l10, arrayOfLong4, 22);
/* 291 */     implMulw(arrayOfLong3, l11, l12, arrayOfLong4, 24);
/*     */     
/* 293 */     implMulw(arrayOfLong3, l1, l2, arrayOfLong4, 10);
/* 294 */     implMulw(arrayOfLong3, l3, l4, arrayOfLong4, 12);
/* 295 */     implMulw(arrayOfLong3, l5, l6, arrayOfLong4, 14);
/* 296 */     implMulw(arrayOfLong3, l7, l8, arrayOfLong4, 16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 313 */     paramArrayOflong3[0] = arrayOfLong4[0];
/* 314 */     paramArrayOflong3[9] = arrayOfLong4[9];
/*     */     
/* 316 */     long l13 = arrayOfLong4[0] ^ arrayOfLong4[1];
/* 317 */     long l14 = l13 ^ arrayOfLong4[2];
/* 318 */     long l15 = l14 ^ arrayOfLong4[10];
/*     */     
/* 320 */     paramArrayOflong3[1] = l15;
/*     */     
/* 322 */     long l16 = arrayOfLong4[3] ^ arrayOfLong4[4];
/* 323 */     long l17 = arrayOfLong4[11] ^ arrayOfLong4[12];
/* 324 */     long l18 = l16 ^ l17;
/* 325 */     long l19 = l14 ^ l18;
/*     */     
/* 327 */     paramArrayOflong3[2] = l19;
/*     */     
/* 329 */     long l20 = l13 ^ l16;
/* 330 */     long l21 = arrayOfLong4[5] ^ arrayOfLong4[6];
/* 331 */     long l22 = l20 ^ l21;
/* 332 */     long l23 = l22 ^ arrayOfLong4[8];
/* 333 */     long l24 = arrayOfLong4[13] ^ arrayOfLong4[14];
/* 334 */     long l25 = l23 ^ l24;
/* 335 */     long l26 = arrayOfLong4[18] ^ arrayOfLong4[22];
/* 336 */     long l27 = l26 ^ arrayOfLong4[24];
/* 337 */     long l28 = l25 ^ l27;
/*     */     
/* 339 */     paramArrayOflong3[3] = l28;
/*     */     
/* 341 */     long l29 = arrayOfLong4[7] ^ arrayOfLong4[8];
/* 342 */     long l30 = l29 ^ arrayOfLong4[9];
/* 343 */     long l31 = l30 ^ arrayOfLong4[17];
/*     */     
/* 345 */     paramArrayOflong3[8] = l31;
/*     */     
/* 347 */     long l32 = l30 ^ l21;
/* 348 */     long l33 = arrayOfLong4[15] ^ arrayOfLong4[16];
/* 349 */     long l34 = l32 ^ l33;
/*     */     
/* 351 */     paramArrayOflong3[7] = l34;
/*     */     
/* 353 */     long l35 = l34 ^ l15;
/* 354 */     long l36 = arrayOfLong4[19] ^ arrayOfLong4[20];
/*     */     
/* 356 */     long l37 = arrayOfLong4[25] ^ arrayOfLong4[24];
/* 357 */     long l38 = arrayOfLong4[18] ^ arrayOfLong4[23];
/* 358 */     long l39 = l36 ^ l37;
/* 359 */     long l40 = l39 ^ l38;
/* 360 */     long l41 = l40 ^ l35;
/*     */     
/* 362 */     paramArrayOflong3[4] = l41;
/*     */     
/* 364 */     long l42 = l19 ^ l31;
/* 365 */     long l43 = l39 ^ l42;
/* 366 */     long l44 = arrayOfLong4[21] ^ arrayOfLong4[22];
/* 367 */     long l45 = l43 ^ l44;
/*     */     
/* 369 */     paramArrayOflong3[5] = l45;
/*     */     
/* 371 */     long l46 = l23 ^ arrayOfLong4[0];
/* 372 */     long l47 = l46 ^ arrayOfLong4[9];
/* 373 */     long l48 = l47 ^ l24;
/* 374 */     long l49 = l48 ^ arrayOfLong4[21];
/* 375 */     long l50 = l49 ^ arrayOfLong4[23];
/* 376 */     long l51 = l50 ^ arrayOfLong4[25];
/*     */     
/* 378 */     paramArrayOflong3[6] = l51;
/*     */     
/* 380 */     implCompactExt(paramArrayOflong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulw(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 389 */     paramArrayOflong1[1] = paramLong2;
/* 390 */     paramArrayOflong1[2] = paramArrayOflong1[1] << 1L;
/* 391 */     paramArrayOflong1[3] = paramArrayOflong1[2] ^ paramLong2;
/* 392 */     paramArrayOflong1[4] = paramArrayOflong1[2] << 1L;
/* 393 */     paramArrayOflong1[5] = paramArrayOflong1[4] ^ paramLong2;
/* 394 */     paramArrayOflong1[6] = paramArrayOflong1[3] << 1L;
/* 395 */     paramArrayOflong1[7] = paramArrayOflong1[6] ^ paramLong2;
/*     */     
/* 397 */     int i = (int)paramLong1;
/* 398 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0x7];
/* 399 */     byte b = 48;
/*     */ 
/*     */     
/* 402 */     do { i = (int)(paramLong1 >>> b);
/* 403 */       long l = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L ^ paramArrayOflong1[i >>> 6 & 0x7] << 6L;
/*     */ 
/*     */       
/* 406 */       l2 ^= l << b;
/* 407 */       l1 ^= l >>> -b;
/*     */       
/* 409 */       b -= 9; } while (b > 0);
/*     */     
/* 411 */     l1 ^= (paramLong1 & 0x100804020100800L & paramLong2 << 7L >> 63L) >>> 8L;
/*     */ 
/*     */ 
/*     */     
/* 415 */     paramArrayOflong2[paramInt] = l2 & 0x1FFFFFFFFFFFFFFL;
/* 416 */     paramArrayOflong2[paramInt + 1] = l2 >>> 57L ^ l1 << 7L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 421 */     Interleave.expand64To128(paramArrayOflong1, 0, 4, paramArrayOflong2, 0);
/* 422 */     paramArrayOflong2[8] = Interleave.expand32to64((int)paramArrayOflong1[4]);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT283Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */