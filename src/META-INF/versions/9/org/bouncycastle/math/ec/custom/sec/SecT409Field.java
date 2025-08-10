/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat448;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT409Field
/*     */ {
/*     */   private static final long M25 = 33554431L;
/*     */   private static final long M59 = 576460752303423487L;
/*     */   
/*     */   public static void add(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  16 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  17 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*  18 */     paramArrayOflong3[2] = paramArrayOflong1[2] ^ paramArrayOflong2[2];
/*  19 */     paramArrayOflong3[3] = paramArrayOflong1[3] ^ paramArrayOflong2[3];
/*  20 */     paramArrayOflong3[4] = paramArrayOflong1[4] ^ paramArrayOflong2[4];
/*  21 */     paramArrayOflong3[5] = paramArrayOflong1[5] ^ paramArrayOflong2[5];
/*  22 */     paramArrayOflong3[6] = paramArrayOflong1[6] ^ paramArrayOflong2[6];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  27 */     for (byte b = 0; b < 13; b++)
/*     */     {
/*  29 */       paramArrayOflong3[b] = paramArrayOflong1[b] ^ paramArrayOflong2[b];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  35 */     paramArrayOflong2[0] = paramArrayOflong1[0] ^ 0x1L;
/*  36 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  37 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*  38 */     paramArrayOflong2[3] = paramArrayOflong1[3];
/*  39 */     paramArrayOflong2[4] = paramArrayOflong1[4];
/*  40 */     paramArrayOflong2[5] = paramArrayOflong1[5];
/*  41 */     paramArrayOflong2[6] = paramArrayOflong1[6];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  46 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ paramArrayOflong1[0];
/*  47 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ paramArrayOflong1[1];
/*  48 */     paramArrayOflong2[2] = paramArrayOflong2[2] ^ paramArrayOflong1[2];
/*  49 */     paramArrayOflong2[3] = paramArrayOflong2[3] ^ paramArrayOflong1[3];
/*  50 */     paramArrayOflong2[4] = paramArrayOflong2[4] ^ paramArrayOflong1[4];
/*  51 */     paramArrayOflong2[5] = paramArrayOflong2[5] ^ paramArrayOflong1[5];
/*  52 */     paramArrayOflong2[6] = paramArrayOflong2[6] ^ paramArrayOflong1[6];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger(BigInteger paramBigInteger) {
/*  57 */     return Nat.fromBigInteger64(409, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  62 */     long[] arrayOfLong = Nat.create64(13);
/*     */     
/*  64 */     Nat448.copy64(paramArrayOflong1, paramArrayOflong2);
/*  65 */     for (byte b = 1; b < 'ƙ'; b += 2) {
/*     */       
/*  67 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  68 */       reduce(arrayOfLong, paramArrayOflong2);
/*  69 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  70 */       reduce(arrayOfLong, paramArrayOflong2);
/*  71 */       addTo(paramArrayOflong1, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invert(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  77 */     if (Nat448.isZero64(paramArrayOflong1))
/*     */     {
/*  79 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  84 */     long[] arrayOfLong1 = Nat448.create64();
/*  85 */     long[] arrayOfLong2 = Nat448.create64();
/*  86 */     long[] arrayOfLong3 = Nat448.create64();
/*     */     
/*  88 */     square(paramArrayOflong1, arrayOfLong1);
/*     */ 
/*     */     
/*  91 */     squareN(arrayOfLong1, 1, arrayOfLong2);
/*  92 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  93 */     squareN(arrayOfLong2, 1, arrayOfLong2);
/*  94 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/*  97 */     squareN(arrayOfLong1, 3, arrayOfLong2);
/*  98 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 101 */     squareN(arrayOfLong1, 6, arrayOfLong2);
/* 102 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 105 */     squareN(arrayOfLong1, 12, arrayOfLong2);
/* 106 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong3);
/*     */ 
/*     */     
/* 109 */     squareN(arrayOfLong3, 24, arrayOfLong1);
/* 110 */     squareN(arrayOfLong1, 24, arrayOfLong2);
/* 111 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 114 */     squareN(arrayOfLong1, 48, arrayOfLong2);
/* 115 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 118 */     squareN(arrayOfLong1, 96, arrayOfLong2);
/* 119 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 122 */     squareN(arrayOfLong1, 192, arrayOfLong2);
/* 123 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */     
/* 125 */     multiply(arrayOfLong1, arrayOfLong3, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 130 */     long[] arrayOfLong = Nat448.createExt64();
/* 131 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 132 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 137 */     long[] arrayOfLong = Nat448.createExt64();
/* 138 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 139 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 144 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3];
/* 145 */     long l5 = paramArrayOflong1[4], l6 = paramArrayOflong1[5], l7 = paramArrayOflong1[6], l8 = paramArrayOflong1[7];
/*     */     
/* 147 */     long l9 = paramArrayOflong1[12];
/* 148 */     l6 ^= l9 << 39L;
/* 149 */     l7 ^= l9 >>> 25L ^ l9 << 62L;
/* 150 */     l8 ^= l9 >>> 2L;
/*     */     
/* 152 */     l9 = paramArrayOflong1[11];
/* 153 */     l5 ^= l9 << 39L;
/* 154 */     l6 ^= l9 >>> 25L ^ l9 << 62L;
/* 155 */     l7 ^= l9 >>> 2L;
/*     */     
/* 157 */     l9 = paramArrayOflong1[10];
/* 158 */     l4 ^= l9 << 39L;
/* 159 */     l5 ^= l9 >>> 25L ^ l9 << 62L;
/* 160 */     l6 ^= l9 >>> 2L;
/*     */     
/* 162 */     l9 = paramArrayOflong1[9];
/* 163 */     l3 ^= l9 << 39L;
/* 164 */     l4 ^= l9 >>> 25L ^ l9 << 62L;
/* 165 */     l5 ^= l9 >>> 2L;
/*     */     
/* 167 */     l9 = paramArrayOflong1[8];
/* 168 */     l2 ^= l9 << 39L;
/* 169 */     l3 ^= l9 >>> 25L ^ l9 << 62L;
/* 170 */     l4 ^= l9 >>> 2L;
/*     */     
/* 172 */     l9 = l8;
/* 173 */     l1 ^= l9 << 39L;
/* 174 */     l2 ^= l9 >>> 25L ^ l9 << 62L;
/* 175 */     l3 ^= l9 >>> 2L;
/*     */     
/* 177 */     long l10 = l7 >>> 25L;
/* 178 */     paramArrayOflong2[0] = l1 ^ l10;
/* 179 */     paramArrayOflong2[1] = l2 ^ l10 << 23L;
/* 180 */     paramArrayOflong2[2] = l3;
/* 181 */     paramArrayOflong2[3] = l4;
/* 182 */     paramArrayOflong2[4] = l5;
/* 183 */     paramArrayOflong2[5] = l6;
/* 184 */     paramArrayOflong2[6] = l7 & 0x1FFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce39(long[] paramArrayOflong, int paramInt) {
/* 189 */     long l1 = paramArrayOflong[paramInt + 6], l2 = l1 >>> 25L;
/* 190 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2;
/* 191 */     paramArrayOflong[paramInt + 1] = paramArrayOflong[paramInt + 1] ^ l2 << 23L;
/* 192 */     paramArrayOflong[paramInt + 6] = l1 & 0x1FFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 198 */     long l1 = Interleave.unshuffle(paramArrayOflong1[0]), l2 = Interleave.unshuffle(paramArrayOflong1[1]);
/* 199 */     long l3 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 200 */     long l4 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 202 */     l1 = Interleave.unshuffle(paramArrayOflong1[2]); l2 = Interleave.unshuffle(paramArrayOflong1[3]);
/* 203 */     long l5 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 204 */     long l6 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 206 */     l1 = Interleave.unshuffle(paramArrayOflong1[4]); l2 = Interleave.unshuffle(paramArrayOflong1[5]);
/* 207 */     long l7 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 208 */     long l8 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 210 */     l1 = Interleave.unshuffle(paramArrayOflong1[6]);
/* 211 */     long l9 = l1 & 0xFFFFFFFFL;
/* 212 */     long l10 = l1 >>> 32L;
/*     */     
/* 214 */     paramArrayOflong2[0] = l3 ^ l4 << 44L;
/* 215 */     paramArrayOflong2[1] = l5 ^ l6 << 44L ^ l4 >>> 20L;
/* 216 */     paramArrayOflong2[2] = l7 ^ l8 << 44L ^ l6 >>> 20L;
/* 217 */     paramArrayOflong2[3] = l9 ^ l10 << 44L ^ l8 >>> 20L ^ l4 << 13L;
/* 218 */     paramArrayOflong2[4] = l10 >>> 20L ^ l6 << 13L ^ l4 >>> 51L;
/* 219 */     paramArrayOflong2[5] = l8 << 13L ^ l6 >>> 51L;
/* 220 */     paramArrayOflong2[6] = l10 << 13L ^ l8 >>> 51L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 227 */     long[] arrayOfLong = Nat.create64(13);
/* 228 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 229 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 234 */     long[] arrayOfLong = Nat.create64(13);
/* 235 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 236 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 243 */     long[] arrayOfLong = Nat.create64(13);
/* 244 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 245 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 247 */     while (--paramInt > 0) {
/*     */       
/* 249 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 250 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 257 */     return (int)paramArrayOflong[0] & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implCompactExt(long[] paramArrayOflong) {
/* 262 */     long l1 = paramArrayOflong[0], l2 = paramArrayOflong[1], l3 = paramArrayOflong[2], l4 = paramArrayOflong[3], l5 = paramArrayOflong[4], l6 = paramArrayOflong[5], l7 = paramArrayOflong[6];
/* 263 */     long l8 = paramArrayOflong[7], l9 = paramArrayOflong[8], l10 = paramArrayOflong[9], l11 = paramArrayOflong[10], l12 = paramArrayOflong[11], l13 = paramArrayOflong[12], l14 = paramArrayOflong[13];
/* 264 */     paramArrayOflong[0] = l1 ^ l2 << 59L;
/* 265 */     paramArrayOflong[1] = l2 >>> 5L ^ l3 << 54L;
/* 266 */     paramArrayOflong[2] = l3 >>> 10L ^ l4 << 49L;
/* 267 */     paramArrayOflong[3] = l4 >>> 15L ^ l5 << 44L;
/* 268 */     paramArrayOflong[4] = l5 >>> 20L ^ l6 << 39L;
/* 269 */     paramArrayOflong[5] = l6 >>> 25L ^ l7 << 34L;
/* 270 */     paramArrayOflong[6] = l7 >>> 30L ^ l8 << 29L;
/* 271 */     paramArrayOflong[7] = l8 >>> 35L ^ l9 << 24L;
/* 272 */     paramArrayOflong[8] = l9 >>> 40L ^ l10 << 19L;
/* 273 */     paramArrayOflong[9] = l10 >>> 45L ^ l11 << 14L;
/* 274 */     paramArrayOflong[10] = l11 >>> 50L ^ l12 << 9L;
/* 275 */     paramArrayOflong[11] = l12 >>> 55L ^ l13 << 4L ^ l14 << 63L;
/*     */     
/* 277 */     paramArrayOflong[12] = l14 >>> 1L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implExpand(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 283 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3], l5 = paramArrayOflong1[4], l6 = paramArrayOflong1[5], l7 = paramArrayOflong1[6];
/* 284 */     paramArrayOflong2[0] = l1 & 0x7FFFFFFFFFFFFFFL;
/* 285 */     paramArrayOflong2[1] = (l1 >>> 59L ^ l2 << 5L) & 0x7FFFFFFFFFFFFFFL;
/* 286 */     paramArrayOflong2[2] = (l2 >>> 54L ^ l3 << 10L) & 0x7FFFFFFFFFFFFFFL;
/* 287 */     paramArrayOflong2[3] = (l3 >>> 49L ^ l4 << 15L) & 0x7FFFFFFFFFFFFFFL;
/* 288 */     paramArrayOflong2[4] = (l4 >>> 44L ^ l5 << 20L) & 0x7FFFFFFFFFFFFFFL;
/* 289 */     paramArrayOflong2[5] = (l5 >>> 39L ^ l6 << 25L) & 0x7FFFFFFFFFFFFFFL;
/* 290 */     paramArrayOflong2[6] = l6 >>> 34L ^ l7 << 30L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 295 */     long[] arrayOfLong1 = new long[7], arrayOfLong2 = new long[7];
/* 296 */     implExpand(paramArrayOflong1, arrayOfLong1);
/* 297 */     implExpand(paramArrayOflong2, arrayOfLong2);
/*     */     
/* 299 */     long[] arrayOfLong3 = new long[8];
/* 300 */     for (byte b = 0; b < 7; b++)
/*     */     {
/* 302 */       implMulwAcc(arrayOfLong3, arrayOfLong1[b], arrayOfLong2[b], paramArrayOflong3, b << 1);
/*     */     }
/*     */     
/* 305 */     long l1 = paramArrayOflong3[0], l2 = paramArrayOflong3[1];
/* 306 */     l1 ^= paramArrayOflong3[2]; paramArrayOflong3[1] = l1 ^ l2; l2 ^= paramArrayOflong3[3];
/* 307 */     l1 ^= paramArrayOflong3[4]; paramArrayOflong3[2] = l1 ^ l2; l2 ^= paramArrayOflong3[5];
/* 308 */     l1 ^= paramArrayOflong3[6]; paramArrayOflong3[3] = l1 ^ l2; l2 ^= paramArrayOflong3[7];
/* 309 */     l1 ^= paramArrayOflong3[8]; paramArrayOflong3[4] = l1 ^ l2; l2 ^= paramArrayOflong3[9];
/* 310 */     l1 ^= paramArrayOflong3[10]; paramArrayOflong3[5] = l1 ^ l2; l2 ^= paramArrayOflong3[11];
/* 311 */     l1 ^= paramArrayOflong3[12]; paramArrayOflong3[6] = l1 ^ l2; l2 ^= paramArrayOflong3[13];
/*     */     
/* 313 */     long l3 = l1 ^ l2;
/* 314 */     paramArrayOflong3[7] = paramArrayOflong3[0] ^ l3;
/* 315 */     paramArrayOflong3[8] = paramArrayOflong3[1] ^ l3;
/* 316 */     paramArrayOflong3[9] = paramArrayOflong3[2] ^ l3;
/* 317 */     paramArrayOflong3[10] = paramArrayOflong3[3] ^ l3;
/* 318 */     paramArrayOflong3[11] = paramArrayOflong3[4] ^ l3;
/* 319 */     paramArrayOflong3[12] = paramArrayOflong3[5] ^ l3;
/* 320 */     paramArrayOflong3[13] = paramArrayOflong3[6] ^ l3;
/*     */     
/* 322 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[1], arrayOfLong2[0] ^ arrayOfLong2[1], paramArrayOflong3, 1);
/*     */     
/* 324 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[2], arrayOfLong2[0] ^ arrayOfLong2[2], paramArrayOflong3, 2);
/*     */     
/* 326 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[3], arrayOfLong2[0] ^ arrayOfLong2[3], paramArrayOflong3, 3);
/* 327 */     implMulwAcc(arrayOfLong3, arrayOfLong1[1] ^ arrayOfLong1[2], arrayOfLong2[1] ^ arrayOfLong2[2], paramArrayOflong3, 3);
/*     */     
/* 329 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[4], arrayOfLong2[0] ^ arrayOfLong2[4], paramArrayOflong3, 4);
/* 330 */     implMulwAcc(arrayOfLong3, arrayOfLong1[1] ^ arrayOfLong1[3], arrayOfLong2[1] ^ arrayOfLong2[3], paramArrayOflong3, 4);
/*     */     
/* 332 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[5], arrayOfLong2[0] ^ arrayOfLong2[5], paramArrayOflong3, 5);
/* 333 */     implMulwAcc(arrayOfLong3, arrayOfLong1[1] ^ arrayOfLong1[4], arrayOfLong2[1] ^ arrayOfLong2[4], paramArrayOflong3, 5);
/* 334 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2] ^ arrayOfLong1[3], arrayOfLong2[2] ^ arrayOfLong2[3], paramArrayOflong3, 5);
/*     */     
/* 336 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[6], arrayOfLong2[0] ^ arrayOfLong2[6], paramArrayOflong3, 6);
/* 337 */     implMulwAcc(arrayOfLong3, arrayOfLong1[1] ^ arrayOfLong1[5], arrayOfLong2[1] ^ arrayOfLong2[5], paramArrayOflong3, 6);
/* 338 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2] ^ arrayOfLong1[4], arrayOfLong2[2] ^ arrayOfLong2[4], paramArrayOflong3, 6);
/*     */     
/* 340 */     implMulwAcc(arrayOfLong3, arrayOfLong1[1] ^ arrayOfLong1[6], arrayOfLong2[1] ^ arrayOfLong2[6], paramArrayOflong3, 7);
/* 341 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2] ^ arrayOfLong1[5], arrayOfLong2[2] ^ arrayOfLong2[5], paramArrayOflong3, 7);
/* 342 */     implMulwAcc(arrayOfLong3, arrayOfLong1[3] ^ arrayOfLong1[4], arrayOfLong2[3] ^ arrayOfLong2[4], paramArrayOflong3, 7);
/*     */     
/* 344 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2] ^ arrayOfLong1[6], arrayOfLong2[2] ^ arrayOfLong2[6], paramArrayOflong3, 8);
/* 345 */     implMulwAcc(arrayOfLong3, arrayOfLong1[3] ^ arrayOfLong1[5], arrayOfLong2[3] ^ arrayOfLong2[5], paramArrayOflong3, 8);
/*     */     
/* 347 */     implMulwAcc(arrayOfLong3, arrayOfLong1[3] ^ arrayOfLong1[6], arrayOfLong2[3] ^ arrayOfLong2[6], paramArrayOflong3, 9);
/* 348 */     implMulwAcc(arrayOfLong3, arrayOfLong1[4] ^ arrayOfLong1[5], arrayOfLong2[4] ^ arrayOfLong2[5], paramArrayOflong3, 9);
/*     */     
/* 350 */     implMulwAcc(arrayOfLong3, arrayOfLong1[4] ^ arrayOfLong1[6], arrayOfLong2[4] ^ arrayOfLong2[6], paramArrayOflong3, 10);
/*     */     
/* 352 */     implMulwAcc(arrayOfLong3, arrayOfLong1[5] ^ arrayOfLong1[6], arrayOfLong2[5] ^ arrayOfLong2[6], paramArrayOflong3, 11);
/*     */     
/* 354 */     implCompactExt(paramArrayOflong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulwAcc(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 363 */     paramArrayOflong1[1] = paramLong2;
/* 364 */     paramArrayOflong1[2] = paramArrayOflong1[1] << 1L;
/* 365 */     paramArrayOflong1[3] = paramArrayOflong1[2] ^ paramLong2;
/* 366 */     paramArrayOflong1[4] = paramArrayOflong1[2] << 1L;
/* 367 */     paramArrayOflong1[5] = paramArrayOflong1[4] ^ paramLong2;
/* 368 */     paramArrayOflong1[6] = paramArrayOflong1[3] << 1L;
/* 369 */     paramArrayOflong1[7] = paramArrayOflong1[6] ^ paramLong2;
/*     */     
/* 371 */     int i = (int)paramLong1;
/* 372 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L;
/*     */     
/* 374 */     byte b = 54;
/*     */ 
/*     */     
/* 377 */     do { i = (int)(paramLong1 >>> b);
/* 378 */       long l = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L;
/*     */       
/* 380 */       l2 ^= l << b;
/* 381 */       l1 ^= l >>> -b;
/*     */       
/* 383 */       b -= 6; } while (b > 0);
/*     */ 
/*     */ 
/*     */     
/* 387 */     paramArrayOflong2[paramInt] = paramArrayOflong2[paramInt] ^ l2 & 0x7FFFFFFFFFFFFFFL;
/* 388 */     paramArrayOflong2[paramInt + 1] = paramArrayOflong2[paramInt + 1] ^ l2 >>> 59L ^ l1 << 5L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 393 */     Interleave.expand64To128(paramArrayOflong1, 0, 6, paramArrayOflong2, 0);
/* 394 */     paramArrayOflong2[12] = Interleave.expand32to64((int)paramArrayOflong1[6]);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT409Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */