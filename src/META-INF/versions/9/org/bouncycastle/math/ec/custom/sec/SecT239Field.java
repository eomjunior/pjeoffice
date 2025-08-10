/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT239Field
/*     */ {
/*     */   private static final long M47 = 140737488355327L;
/*     */   private static final long M60 = 1152921504606846975L;
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
/*  52 */     return Nat.fromBigInteger64(239, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  57 */     long[] arrayOfLong = Nat256.createExt64();
/*     */     
/*  59 */     Nat256.copy64(paramArrayOflong1, paramArrayOflong2);
/*  60 */     for (byte b = 1; b < 'ï'; b += 2) {
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
/*  98 */     square(arrayOfLong1, arrayOfLong1);
/*  99 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/* 100 */     squareN(arrayOfLong1, 59, arrayOfLong2);
/* 101 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/* 102 */     square(arrayOfLong2, arrayOfLong2);
/* 103 */     multiply(arrayOfLong2, paramArrayOflong1, arrayOfLong2);
/* 104 */     squareN(arrayOfLong2, 119, arrayOfLong1);
/* 105 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 106 */     square(arrayOfLong1, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 111 */     long[] arrayOfLong = Nat256.createExt64();
/* 112 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 113 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 118 */     long[] arrayOfLong = Nat256.createExt64();
/* 119 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 120 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 125 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3];
/* 126 */     long l5 = paramArrayOflong1[4], l6 = paramArrayOflong1[5], l7 = paramArrayOflong1[6], l8 = paramArrayOflong1[7];
/*     */     
/* 128 */     l4 ^= l8 << 17L;
/* 129 */     l5 ^= l8 >>> 47L;
/* 130 */     l6 ^= l8 << 47L;
/* 131 */     l7 ^= l8 >>> 17L;
/*     */     
/* 133 */     l3 ^= l7 << 17L;
/* 134 */     l4 ^= l7 >>> 47L;
/* 135 */     l5 ^= l7 << 47L;
/* 136 */     l6 ^= l7 >>> 17L;
/*     */     
/* 138 */     l2 ^= l6 << 17L;
/* 139 */     l3 ^= l6 >>> 47L;
/* 140 */     l4 ^= l6 << 47L;
/* 141 */     l5 ^= l6 >>> 17L;
/*     */     
/* 143 */     l1 ^= l5 << 17L;
/* 144 */     l2 ^= l5 >>> 47L;
/* 145 */     l3 ^= l5 << 47L;
/* 146 */     l4 ^= l5 >>> 17L;
/*     */     
/* 148 */     long l9 = l4 >>> 47L;
/* 149 */     paramArrayOflong2[0] = l1 ^ l9;
/* 150 */     paramArrayOflong2[1] = l2;
/* 151 */     paramArrayOflong2[2] = l3 ^ l9 << 30L;
/* 152 */     paramArrayOflong2[3] = l4 & 0x7FFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce17(long[] paramArrayOflong, int paramInt) {
/* 157 */     long l1 = paramArrayOflong[paramInt + 3], l2 = l1 >>> 47L;
/* 158 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2;
/* 159 */     paramArrayOflong[paramInt + 2] = paramArrayOflong[paramInt + 2] ^ l2 << 30L;
/* 160 */     paramArrayOflong[paramInt + 3] = l1 & 0x7FFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 166 */     long l1 = Interleave.unshuffle(paramArrayOflong1[0]), l2 = Interleave.unshuffle(paramArrayOflong1[1]);
/* 167 */     long l3 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 168 */     long l4 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 170 */     l1 = Interleave.unshuffle(paramArrayOflong1[2]); l2 = Interleave.unshuffle(paramArrayOflong1[3]);
/* 171 */     long l5 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 172 */     long l6 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */ 
/*     */     
/* 175 */     long l8 = l6 >>> 49L;
/* 176 */     long l7 = l4 >>> 49L | l6 << 15L;
/* 177 */     l6 ^= l4 << 15L;
/*     */     
/* 179 */     long[] arrayOfLong = Nat256.createExt64();
/*     */     
/* 181 */     int[] arrayOfInt = { 39, 120 };
/* 182 */     for (byte b = 0; b < arrayOfInt.length; b++) {
/*     */       
/* 184 */       int i = arrayOfInt[b] >>> 6, j = arrayOfInt[b] & 0x3F;
/*     */       
/* 186 */       arrayOfLong[i] = arrayOfLong[i] ^ l4 << j;
/* 187 */       arrayOfLong[i + 1] = arrayOfLong[i + 1] ^ (l6 << j | l4 >>> -j);
/* 188 */       arrayOfLong[i + 2] = arrayOfLong[i + 2] ^ (l7 << j | l6 >>> -j);
/* 189 */       arrayOfLong[i + 3] = arrayOfLong[i + 3] ^ (l8 << j | l7 >>> -j);
/* 190 */       arrayOfLong[i + 4] = arrayOfLong[i + 4] ^ l8 >>> -j;
/*     */     } 
/*     */     
/* 193 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 195 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ l3;
/* 196 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ l5;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 201 */     long[] arrayOfLong = Nat256.createExt64();
/* 202 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 203 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 208 */     long[] arrayOfLong = Nat256.createExt64();
/* 209 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 210 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 217 */     long[] arrayOfLong = Nat256.createExt64();
/* 218 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 219 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 221 */     while (--paramInt > 0) {
/*     */       
/* 223 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 224 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 231 */     return (int)(paramArrayOflong[0] ^ paramArrayOflong[1] >>> 17L ^ paramArrayOflong[2] >>> 34L) & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implCompactExt(long[] paramArrayOflong) {
/* 236 */     long l1 = paramArrayOflong[0], l2 = paramArrayOflong[1], l3 = paramArrayOflong[2], l4 = paramArrayOflong[3], l5 = paramArrayOflong[4], l6 = paramArrayOflong[5], l7 = paramArrayOflong[6], l8 = paramArrayOflong[7];
/* 237 */     paramArrayOflong[0] = l1 ^ l2 << 60L;
/* 238 */     paramArrayOflong[1] = l2 >>> 4L ^ l3 << 56L;
/* 239 */     paramArrayOflong[2] = l3 >>> 8L ^ l4 << 52L;
/* 240 */     paramArrayOflong[3] = l4 >>> 12L ^ l5 << 48L;
/* 241 */     paramArrayOflong[4] = l5 >>> 16L ^ l6 << 44L;
/* 242 */     paramArrayOflong[5] = l6 >>> 20L ^ l7 << 40L;
/* 243 */     paramArrayOflong[6] = l7 >>> 24L ^ l8 << 36L;
/* 244 */     paramArrayOflong[7] = l8 >>> 28L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implExpand(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 249 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3];
/* 250 */     paramArrayOflong2[0] = l1 & 0xFFFFFFFFFFFFFFFL;
/* 251 */     paramArrayOflong2[1] = (l1 >>> 60L ^ l2 << 4L) & 0xFFFFFFFFFFFFFFFL;
/* 252 */     paramArrayOflong2[2] = (l2 >>> 56L ^ l3 << 8L) & 0xFFFFFFFFFFFFFFFL;
/* 253 */     paramArrayOflong2[3] = l3 >>> 52L ^ l4 << 12L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 262 */     long[] arrayOfLong1 = new long[4], arrayOfLong2 = new long[4];
/* 263 */     implExpand(paramArrayOflong1, arrayOfLong1);
/* 264 */     implExpand(paramArrayOflong2, arrayOfLong2);
/*     */     
/* 266 */     long[] arrayOfLong3 = new long[8];
/*     */     
/* 268 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0], arrayOfLong2[0], paramArrayOflong3, 0);
/* 269 */     implMulwAcc(arrayOfLong3, arrayOfLong1[1], arrayOfLong2[1], paramArrayOflong3, 1);
/* 270 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2], arrayOfLong2[2], paramArrayOflong3, 2);
/* 271 */     implMulwAcc(arrayOfLong3, arrayOfLong1[3], arrayOfLong2[3], paramArrayOflong3, 3);
/*     */     
/*     */     byte b;
/* 274 */     for (b = 5; b > 0; b--)
/*     */     {
/* 276 */       paramArrayOflong3[b] = paramArrayOflong3[b] ^ paramArrayOflong3[b - 1];
/*     */     }
/*     */     
/* 279 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[1], arrayOfLong2[0] ^ arrayOfLong2[1], paramArrayOflong3, 1);
/* 280 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2] ^ arrayOfLong1[3], arrayOfLong2[2] ^ arrayOfLong2[3], paramArrayOflong3, 3);
/*     */ 
/*     */     
/* 283 */     for (b = 7; b > 1; b--)
/*     */     {
/* 285 */       paramArrayOflong3[b] = paramArrayOflong3[b] ^ paramArrayOflong3[b - 2];
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 290 */     long l1 = arrayOfLong1[0] ^ arrayOfLong1[2], l2 = arrayOfLong1[1] ^ arrayOfLong1[3];
/* 291 */     long l3 = arrayOfLong2[0] ^ arrayOfLong2[2], l4 = arrayOfLong2[1] ^ arrayOfLong2[3];
/* 292 */     implMulwAcc(arrayOfLong3, l1 ^ l2, l3 ^ l4, paramArrayOflong3, 3);
/* 293 */     long[] arrayOfLong4 = new long[3];
/* 294 */     implMulwAcc(arrayOfLong3, l1, l3, arrayOfLong4, 0);
/* 295 */     implMulwAcc(arrayOfLong3, l2, l4, arrayOfLong4, 1);
/* 296 */     long l5 = arrayOfLong4[0], l6 = arrayOfLong4[1], l7 = arrayOfLong4[2];
/* 297 */     paramArrayOflong3[2] = paramArrayOflong3[2] ^ l5;
/* 298 */     paramArrayOflong3[3] = paramArrayOflong3[3] ^ l5 ^ l6;
/* 299 */     paramArrayOflong3[4] = paramArrayOflong3[4] ^ l7 ^ l6;
/* 300 */     paramArrayOflong3[5] = paramArrayOflong3[5] ^ l7;
/*     */ 
/*     */     
/* 303 */     implCompactExt(paramArrayOflong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulwAcc(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 312 */     paramArrayOflong1[1] = paramLong2;
/* 313 */     paramArrayOflong1[2] = paramArrayOflong1[1] << 1L;
/* 314 */     paramArrayOflong1[3] = paramArrayOflong1[2] ^ paramLong2;
/* 315 */     paramArrayOflong1[4] = paramArrayOflong1[2] << 1L;
/* 316 */     paramArrayOflong1[5] = paramArrayOflong1[4] ^ paramLong2;
/* 317 */     paramArrayOflong1[6] = paramArrayOflong1[3] << 1L;
/* 318 */     paramArrayOflong1[7] = paramArrayOflong1[6] ^ paramLong2;
/*     */     
/* 320 */     int i = (int)paramLong1;
/* 321 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L;
/*     */     
/* 323 */     byte b = 54;
/*     */ 
/*     */     
/* 326 */     do { i = (int)(paramLong1 >>> b);
/* 327 */       long l = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L;
/*     */       
/* 329 */       l2 ^= l << b;
/* 330 */       l1 ^= l >>> -b;
/*     */       
/* 332 */       b -= 6; } while (b > 0);
/*     */     
/* 334 */     l1 ^= (paramLong1 & 0x820820820820820L & paramLong2 << 4L >> 63L) >>> 5L;
/*     */ 
/*     */ 
/*     */     
/* 338 */     paramArrayOflong2[paramInt] = paramArrayOflong2[paramInt] ^ l2 & 0xFFFFFFFFFFFFFFFL;
/* 339 */     paramArrayOflong2[paramInt + 1] = paramArrayOflong2[paramInt + 1] ^ l2 >>> 60L ^ l1 << 4L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 344 */     Interleave.expand64To128(paramArrayOflong1, 0, 4, paramArrayOflong2, 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT239Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */