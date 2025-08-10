/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT233Field
/*     */ {
/*     */   private static final long M41 = 2199023255551L;
/*     */   private static final long M59 = 576460752303423487L;
/*     */   
/*     */   public static void add(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  16 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  17 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*  18 */     paramArrayOflong3[2] = paramArrayOflong1[2] ^ paramArrayOflong2[2];
/*  19 */     paramArrayOflong3[3] = paramArrayOflong1[3] ^ paramArrayOflong2[3];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  24 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  25 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*  26 */     paramArrayOflong3[2] = paramArrayOflong1[2] ^ paramArrayOflong2[2];
/*  27 */     paramArrayOflong3[3] = paramArrayOflong1[3] ^ paramArrayOflong2[3];
/*  28 */     paramArrayOflong3[4] = paramArrayOflong1[4] ^ paramArrayOflong2[4];
/*  29 */     paramArrayOflong3[5] = paramArrayOflong1[5] ^ paramArrayOflong2[5];
/*  30 */     paramArrayOflong3[6] = paramArrayOflong1[6] ^ paramArrayOflong2[6];
/*  31 */     paramArrayOflong3[7] = paramArrayOflong1[7] ^ paramArrayOflong2[7];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  36 */     paramArrayOflong2[0] = paramArrayOflong1[0] ^ 0x1L;
/*  37 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  38 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*  39 */     paramArrayOflong2[3] = paramArrayOflong1[3];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  44 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ paramArrayOflong1[0];
/*  45 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ paramArrayOflong1[1];
/*  46 */     paramArrayOflong2[2] = paramArrayOflong2[2] ^ paramArrayOflong1[2];
/*  47 */     paramArrayOflong2[3] = paramArrayOflong2[3] ^ paramArrayOflong1[3];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger(BigInteger paramBigInteger) {
/*  52 */     return Nat.fromBigInteger64(233, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  57 */     long[] arrayOfLong = Nat256.createExt64();
/*     */     
/*  59 */     Nat256.copy64(paramArrayOflong1, paramArrayOflong2);
/*  60 */     for (byte b = 1; b < 'é'; b += 2) {
/*     */       
/*  62 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  63 */       reduce(arrayOfLong, paramArrayOflong2);
/*  64 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  65 */       reduce(arrayOfLong, paramArrayOflong2);
/*  66 */       addTo(paramArrayOflong1, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invert(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  72 */     if (Nat256.isZero64(paramArrayOflong1))
/*     */     {
/*  74 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  79 */     long[] arrayOfLong1 = Nat256.create64();
/*  80 */     long[] arrayOfLong2 = Nat256.create64();
/*     */     
/*  82 */     square(paramArrayOflong1, arrayOfLong1);
/*  83 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/*  84 */     square(arrayOfLong1, arrayOfLong1);
/*  85 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/*  86 */     squareN(arrayOfLong1, 3, arrayOfLong2);
/*  87 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  88 */     square(arrayOfLong2, arrayOfLong2);
/*  89 */     multiply(arrayOfLong2, paramArrayOflong1, arrayOfLong2);
/*  90 */     squareN(arrayOfLong2, 7, arrayOfLong1);
/*  91 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  92 */     squareN(arrayOfLong1, 14, arrayOfLong2);
/*  93 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  94 */     square(arrayOfLong2, arrayOfLong2);
/*  95 */     multiply(arrayOfLong2, paramArrayOflong1, arrayOfLong2);
/*  96 */     squareN(arrayOfLong2, 29, arrayOfLong1);
/*  97 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  98 */     squareN(arrayOfLong1, 58, arrayOfLong2);
/*  99 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/* 100 */     squareN(arrayOfLong2, 116, arrayOfLong1);
/* 101 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 102 */     square(arrayOfLong1, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 107 */     long[] arrayOfLong = Nat256.createExt64();
/* 108 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 109 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 114 */     long[] arrayOfLong = Nat256.createExt64();
/* 115 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 116 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 121 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3];
/* 122 */     long l5 = paramArrayOflong1[4], l6 = paramArrayOflong1[5], l7 = paramArrayOflong1[6], l8 = paramArrayOflong1[7];
/*     */     
/* 124 */     l4 ^= l8 << 23L;
/* 125 */     l5 ^= l8 >>> 41L ^ l8 << 33L;
/* 126 */     l6 ^= l8 >>> 31L;
/*     */     
/* 128 */     l3 ^= l7 << 23L;
/* 129 */     l4 ^= l7 >>> 41L ^ l7 << 33L;
/* 130 */     l5 ^= l7 >>> 31L;
/*     */     
/* 132 */     l2 ^= l6 << 23L;
/* 133 */     l3 ^= l6 >>> 41L ^ l6 << 33L;
/* 134 */     l4 ^= l6 >>> 31L;
/*     */     
/* 136 */     l1 ^= l5 << 23L;
/* 137 */     l2 ^= l5 >>> 41L ^ l5 << 33L;
/* 138 */     l3 ^= l5 >>> 31L;
/*     */     
/* 140 */     long l9 = l4 >>> 41L;
/* 141 */     paramArrayOflong2[0] = l1 ^ l9;
/* 142 */     paramArrayOflong2[1] = l2 ^ l9 << 10L;
/* 143 */     paramArrayOflong2[2] = l3;
/* 144 */     paramArrayOflong2[3] = l4 & 0x1FFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce23(long[] paramArrayOflong, int paramInt) {
/* 149 */     long l1 = paramArrayOflong[paramInt + 3], l2 = l1 >>> 41L;
/* 150 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2;
/* 151 */     paramArrayOflong[paramInt + 1] = paramArrayOflong[paramInt + 1] ^ l2 << 10L;
/* 152 */     paramArrayOflong[paramInt + 3] = l1 & 0x1FFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 158 */     long l1 = Interleave.unshuffle(paramArrayOflong1[0]), l2 = Interleave.unshuffle(paramArrayOflong1[1]);
/* 159 */     long l3 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 160 */     long l4 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 162 */     l1 = Interleave.unshuffle(paramArrayOflong1[2]); l2 = Interleave.unshuffle(paramArrayOflong1[3]);
/* 163 */     long l5 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 164 */     long l6 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */ 
/*     */     
/* 167 */     long l7 = l6 >>> 27L;
/* 168 */     l6 ^= l4 >>> 27L | l6 << 37L;
/* 169 */     l4 ^= l4 << 37L;
/*     */     
/* 171 */     long[] arrayOfLong = Nat256.createExt64();
/*     */     
/* 173 */     int[] arrayOfInt = { 32, 117, 191 };
/* 174 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/*     */       
/* 176 */       int i = arrayOfInt[b] >>> 6, j = arrayOfInt[b] & 0x3F;
/*     */       
/* 178 */       arrayOfLong[i] = arrayOfLong[i] ^ l4 << j;
/* 179 */       arrayOfLong[i + 1] = arrayOfLong[i + 1] ^ (l6 << j | l4 >>> -j);
/* 180 */       arrayOfLong[i + 2] = arrayOfLong[i + 2] ^ (l7 << j | l6 >>> -j);
/* 181 */       arrayOfLong[i + 3] = arrayOfLong[i + 3] ^ l7 >>> -j;
/*     */     } 
/*     */     
/* 184 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 186 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ l3;
/* 187 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ l5;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 192 */     long[] arrayOfLong = Nat256.createExt64();
/* 193 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 194 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 199 */     long[] arrayOfLong = Nat256.createExt64();
/* 200 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 201 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 208 */     long[] arrayOfLong = Nat256.createExt64();
/* 209 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 210 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 212 */     while (--paramInt > 0) {
/*     */       
/* 214 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 215 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 222 */     return (int)(paramArrayOflong[0] ^ paramArrayOflong[2] >>> 31L) & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implCompactExt(long[] paramArrayOflong) {
/* 227 */     long l1 = paramArrayOflong[0], l2 = paramArrayOflong[1], l3 = paramArrayOflong[2], l4 = paramArrayOflong[3], l5 = paramArrayOflong[4], l6 = paramArrayOflong[5], l7 = paramArrayOflong[6], l8 = paramArrayOflong[7];
/* 228 */     paramArrayOflong[0] = l1 ^ l2 << 59L;
/* 229 */     paramArrayOflong[1] = l2 >>> 5L ^ l3 << 54L;
/* 230 */     paramArrayOflong[2] = l3 >>> 10L ^ l4 << 49L;
/* 231 */     paramArrayOflong[3] = l4 >>> 15L ^ l5 << 44L;
/* 232 */     paramArrayOflong[4] = l5 >>> 20L ^ l6 << 39L;
/* 233 */     paramArrayOflong[5] = l6 >>> 25L ^ l7 << 34L;
/* 234 */     paramArrayOflong[6] = l7 >>> 30L ^ l8 << 29L;
/* 235 */     paramArrayOflong[7] = l8 >>> 35L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implExpand(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 240 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3];
/* 241 */     paramArrayOflong2[0] = l1 & 0x7FFFFFFFFFFFFFFL;
/* 242 */     paramArrayOflong2[1] = (l1 >>> 59L ^ l2 << 5L) & 0x7FFFFFFFFFFFFFFL;
/* 243 */     paramArrayOflong2[2] = (l2 >>> 54L ^ l3 << 10L) & 0x7FFFFFFFFFFFFFFL;
/* 244 */     paramArrayOflong2[3] = l3 >>> 49L ^ l4 << 15L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 253 */     long[] arrayOfLong1 = new long[4], arrayOfLong2 = new long[4];
/* 254 */     implExpand(paramArrayOflong1, arrayOfLong1);
/* 255 */     implExpand(paramArrayOflong2, arrayOfLong2);
/*     */     
/* 257 */     long[] arrayOfLong3 = new long[8];
/*     */     
/* 259 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0], arrayOfLong2[0], paramArrayOflong3, 0);
/* 260 */     implMulwAcc(arrayOfLong3, arrayOfLong1[1], arrayOfLong2[1], paramArrayOflong3, 1);
/* 261 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2], arrayOfLong2[2], paramArrayOflong3, 2);
/* 262 */     implMulwAcc(arrayOfLong3, arrayOfLong1[3], arrayOfLong2[3], paramArrayOflong3, 3);
/*     */     
/*     */     byte b;
/* 265 */     for (b = 5; b > 0; b--)
/*     */     {
/* 267 */       paramArrayOflong3[b] = paramArrayOflong3[b] ^ paramArrayOflong3[b - 1];
/*     */     }
/*     */     
/* 270 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[1], arrayOfLong2[0] ^ arrayOfLong2[1], paramArrayOflong3, 1);
/* 271 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2] ^ arrayOfLong1[3], arrayOfLong2[2] ^ arrayOfLong2[3], paramArrayOflong3, 3);
/*     */ 
/*     */     
/* 274 */     for (b = 7; b > 1; b--)
/*     */     {
/* 276 */       paramArrayOflong3[b] = paramArrayOflong3[b] ^ paramArrayOflong3[b - 2];
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 281 */     long l1 = arrayOfLong1[0] ^ arrayOfLong1[2], l2 = arrayOfLong1[1] ^ arrayOfLong1[3];
/* 282 */     long l3 = arrayOfLong2[0] ^ arrayOfLong2[2], l4 = arrayOfLong2[1] ^ arrayOfLong2[3];
/* 283 */     implMulwAcc(arrayOfLong3, l1 ^ l2, l3 ^ l4, paramArrayOflong3, 3);
/* 284 */     long[] arrayOfLong4 = new long[3];
/* 285 */     implMulwAcc(arrayOfLong3, l1, l3, arrayOfLong4, 0);
/* 286 */     implMulwAcc(arrayOfLong3, l2, l4, arrayOfLong4, 1);
/* 287 */     long l5 = arrayOfLong4[0], l6 = arrayOfLong4[1], l7 = arrayOfLong4[2];
/* 288 */     paramArrayOflong3[2] = paramArrayOflong3[2] ^ l5;
/* 289 */     paramArrayOflong3[3] = paramArrayOflong3[3] ^ l5 ^ l6;
/* 290 */     paramArrayOflong3[4] = paramArrayOflong3[4] ^ l7 ^ l6;
/* 291 */     paramArrayOflong3[5] = paramArrayOflong3[5] ^ l7;
/*     */ 
/*     */     
/* 294 */     implCompactExt(paramArrayOflong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulwAcc(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 303 */     paramArrayOflong1[1] = paramLong2;
/* 304 */     paramArrayOflong1[2] = paramArrayOflong1[1] << 1L;
/* 305 */     paramArrayOflong1[3] = paramArrayOflong1[2] ^ paramLong2;
/* 306 */     paramArrayOflong1[4] = paramArrayOflong1[2] << 1L;
/* 307 */     paramArrayOflong1[5] = paramArrayOflong1[4] ^ paramLong2;
/* 308 */     paramArrayOflong1[6] = paramArrayOflong1[3] << 1L;
/* 309 */     paramArrayOflong1[7] = paramArrayOflong1[6] ^ paramLong2;
/*     */     
/* 311 */     int i = (int)paramLong1;
/* 312 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L;
/*     */     
/* 314 */     byte b = 54;
/*     */ 
/*     */     
/* 317 */     do { i = (int)(paramLong1 >>> b);
/* 318 */       long l = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L;
/*     */       
/* 320 */       l2 ^= l << b;
/* 321 */       l1 ^= l >>> -b;
/*     */       
/* 323 */       b -= 6; } while (b > 0);
/*     */ 
/*     */ 
/*     */     
/* 327 */     paramArrayOflong2[paramInt] = paramArrayOflong2[paramInt] ^ l2 & 0x7FFFFFFFFFFFFFFL;
/* 328 */     paramArrayOflong2[paramInt + 1] = paramArrayOflong2[paramInt + 1] ^ l2 >>> 59L ^ l1 << 5L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 333 */     Interleave.expand64To128(paramArrayOflong1, 0, 4, paramArrayOflong2, 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT233Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */