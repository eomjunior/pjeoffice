/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT131Field
/*     */ {
/*     */   private static final long M03 = 7L;
/*     */   private static final long M44 = 17592186044415L;
/*  14 */   private static final long[] ROOT_Z = new long[] { 2791191049453778211L, 2791191049453778402L, 6L };
/*     */ 
/*     */   
/*     */   public static void add(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  18 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  19 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*  20 */     paramArrayOflong3[2] = paramArrayOflong1[2] ^ paramArrayOflong2[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  25 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  26 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*  27 */     paramArrayOflong3[2] = paramArrayOflong1[2] ^ paramArrayOflong2[2];
/*  28 */     paramArrayOflong3[3] = paramArrayOflong1[3] ^ paramArrayOflong2[3];
/*  29 */     paramArrayOflong3[4] = paramArrayOflong1[4] ^ paramArrayOflong2[4];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  34 */     paramArrayOflong2[0] = paramArrayOflong1[0] ^ 0x1L;
/*  35 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  36 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  41 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ paramArrayOflong1[0];
/*  42 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ paramArrayOflong1[1];
/*  43 */     paramArrayOflong2[2] = paramArrayOflong2[2] ^ paramArrayOflong1[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger(BigInteger paramBigInteger) {
/*  48 */     return Nat.fromBigInteger64(131, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  53 */     long[] arrayOfLong = Nat.create64(5);
/*     */     
/*  55 */     Nat192.copy64(paramArrayOflong1, paramArrayOflong2);
/*  56 */     for (byte b = 1; b < ''; b += 2) {
/*     */       
/*  58 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  59 */       reduce(arrayOfLong, paramArrayOflong2);
/*  60 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  61 */       reduce(arrayOfLong, paramArrayOflong2);
/*  62 */       addTo(paramArrayOflong1, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invert(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  68 */     if (Nat192.isZero64(paramArrayOflong1))
/*     */     {
/*  70 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  75 */     long[] arrayOfLong1 = Nat192.create64();
/*  76 */     long[] arrayOfLong2 = Nat192.create64();
/*     */     
/*  78 */     square(paramArrayOflong1, arrayOfLong1);
/*  79 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/*  80 */     squareN(arrayOfLong1, 2, arrayOfLong2);
/*  81 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  82 */     squareN(arrayOfLong2, 4, arrayOfLong1);
/*  83 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  84 */     squareN(arrayOfLong1, 8, arrayOfLong2);
/*  85 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  86 */     squareN(arrayOfLong2, 16, arrayOfLong1);
/*  87 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  88 */     squareN(arrayOfLong1, 32, arrayOfLong2);
/*  89 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  90 */     square(arrayOfLong2, arrayOfLong2);
/*  91 */     multiply(arrayOfLong2, paramArrayOflong1, arrayOfLong2);
/*  92 */     squareN(arrayOfLong2, 65, arrayOfLong1);
/*  93 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  94 */     square(arrayOfLong1, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  99 */     long[] arrayOfLong = new long[8];
/* 100 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 101 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 106 */     long[] arrayOfLong = new long[8];
/* 107 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 108 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 113 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3], l5 = paramArrayOflong1[4];
/*     */     
/* 115 */     l2 ^= l5 << 61L ^ l5 << 63L;
/* 116 */     l3 ^= l5 >>> 3L ^ l5 >>> 1L ^ l5 ^ l5 << 5L;
/* 117 */     l4 ^= l5 >>> 59L;
/*     */     
/* 119 */     l1 ^= l4 << 61L ^ l4 << 63L;
/* 120 */     l2 ^= l4 >>> 3L ^ l4 >>> 1L ^ l4 ^ l4 << 5L;
/* 121 */     l3 ^= l4 >>> 59L;
/*     */     
/* 123 */     long l6 = l3 >>> 3L;
/* 124 */     paramArrayOflong2[0] = l1 ^ l6 ^ l6 << 2L ^ l6 << 3L ^ l6 << 8L;
/* 125 */     paramArrayOflong2[1] = l2 ^ l6 >>> 56L;
/* 126 */     paramArrayOflong2[2] = l3 & 0x7L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce61(long[] paramArrayOflong, int paramInt) {
/* 131 */     long l1 = paramArrayOflong[paramInt + 2], l2 = l1 >>> 3L;
/* 132 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2 ^ l2 << 2L ^ l2 << 3L ^ l2 << 8L;
/* 133 */     paramArrayOflong[paramInt + 1] = paramArrayOflong[paramInt + 1] ^ l2 >>> 56L;
/* 134 */     paramArrayOflong[paramInt + 2] = l1 & 0x7L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 139 */     long[] arrayOfLong = Nat192.create64();
/*     */ 
/*     */     
/* 142 */     long l1 = Interleave.unshuffle(paramArrayOflong1[0]), l2 = Interleave.unshuffle(paramArrayOflong1[1]);
/* 143 */     long l3 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 144 */     arrayOfLong[0] = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 146 */     l1 = Interleave.unshuffle(paramArrayOflong1[2]);
/* 147 */     long l4 = l1 & 0xFFFFFFFFL;
/* 148 */     arrayOfLong[1] = l1 >>> 32L;
/*     */     
/* 150 */     multiply(arrayOfLong, ROOT_Z, paramArrayOflong2);
/*     */     
/* 152 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ l3;
/* 153 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ l4;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 158 */     long[] arrayOfLong = Nat.create64(5);
/* 159 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 160 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 165 */     long[] arrayOfLong = Nat.create64(5);
/* 166 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 167 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 174 */     long[] arrayOfLong = Nat.create64(5);
/* 175 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 176 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 178 */     while (--paramInt > 0) {
/*     */       
/* 180 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 181 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 188 */     return (int)(paramArrayOflong[0] ^ paramArrayOflong[1] >>> 59L ^ paramArrayOflong[2] >>> 1L) & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implCompactExt(long[] paramArrayOflong) {
/* 193 */     long l1 = paramArrayOflong[0], l2 = paramArrayOflong[1], l3 = paramArrayOflong[2], l4 = paramArrayOflong[3], l5 = paramArrayOflong[4], l6 = paramArrayOflong[5];
/* 194 */     paramArrayOflong[0] = l1 ^ l2 << 44L;
/* 195 */     paramArrayOflong[1] = l2 >>> 20L ^ l3 << 24L;
/* 196 */     paramArrayOflong[2] = l3 >>> 40L ^ l4 << 4L ^ l5 << 48L;
/*     */     
/* 198 */     paramArrayOflong[3] = l4 >>> 60L ^ l6 << 28L ^ l5 >>> 16L;
/*     */     
/* 200 */     paramArrayOflong[4] = l6 >>> 36L;
/* 201 */     paramArrayOflong[5] = 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 210 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2];
/* 211 */     l3 = (l2 >>> 24L ^ l3 << 40L) & 0xFFFFFFFFFFFL;
/* 212 */     l2 = (l1 >>> 44L ^ l2 << 20L) & 0xFFFFFFFFFFFL;
/* 213 */     l1 &= 0xFFFFFFFFFFFL;
/*     */     
/* 215 */     long l4 = paramArrayOflong2[0], l5 = paramArrayOflong2[1], l6 = paramArrayOflong2[2];
/* 216 */     l6 = (l5 >>> 24L ^ l6 << 40L) & 0xFFFFFFFFFFFL;
/* 217 */     l5 = (l4 >>> 44L ^ l5 << 20L) & 0xFFFFFFFFFFFL;
/* 218 */     l4 &= 0xFFFFFFFFFFFL;
/*     */     
/* 220 */     long[] arrayOfLong1 = paramArrayOflong3;
/* 221 */     long[] arrayOfLong2 = new long[10];
/*     */     
/* 223 */     implMulw(arrayOfLong1, l1, l4, arrayOfLong2, 0);
/* 224 */     implMulw(arrayOfLong1, l3, l6, arrayOfLong2, 2);
/*     */     
/* 226 */     long l7 = l1 ^ l2 ^ l3;
/* 227 */     long l8 = l4 ^ l5 ^ l6;
/*     */     
/* 229 */     implMulw(arrayOfLong1, l7, l8, arrayOfLong2, 4);
/*     */     
/* 231 */     long l9 = l2 << 1L ^ l3 << 2L;
/* 232 */     long l10 = l5 << 1L ^ l6 << 2L;
/*     */     
/* 234 */     implMulw(arrayOfLong1, l1 ^ l9, l4 ^ l10, arrayOfLong2, 6);
/* 235 */     implMulw(arrayOfLong1, l7 ^ l9, l8 ^ l10, arrayOfLong2, 8);
/*     */     
/* 237 */     long l11 = arrayOfLong2[6] ^ arrayOfLong2[8];
/* 238 */     long l12 = arrayOfLong2[7] ^ arrayOfLong2[9];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 243 */     long l13 = l11 << 1L ^ arrayOfLong2[6];
/* 244 */     long l14 = l11 ^ l12 << 1L ^ arrayOfLong2[7];
/* 245 */     long l15 = l12;
/*     */ 
/*     */     
/* 248 */     long l16 = arrayOfLong2[0];
/* 249 */     long l17 = arrayOfLong2[1] ^ arrayOfLong2[0] ^ arrayOfLong2[4];
/* 250 */     long l18 = arrayOfLong2[1] ^ arrayOfLong2[5];
/*     */ 
/*     */     
/* 253 */     long l19 = l16 ^ l13 ^ arrayOfLong2[2] << 4L ^ arrayOfLong2[2] << 1L;
/* 254 */     long l20 = l17 ^ l14 ^ arrayOfLong2[3] << 4L ^ arrayOfLong2[3] << 1L;
/* 255 */     long l21 = l18 ^ l15;
/*     */ 
/*     */     
/* 258 */     l20 ^= l19 >>> 44L; l19 &= 0xFFFFFFFFFFFL;
/* 259 */     l21 ^= l20 >>> 44L; l20 &= 0xFFFFFFFFFFFL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 265 */     l19 = l19 >>> 1L ^ (l20 & 0x1L) << 43L;
/* 266 */     l20 = l20 >>> 1L ^ (l21 & 0x1L) << 43L;
/* 267 */     l21 >>>= 1L;
/*     */ 
/*     */ 
/*     */     
/* 271 */     l19 ^= l19 << 1L;
/* 272 */     l19 ^= l19 << 2L;
/* 273 */     l19 ^= l19 << 4L;
/* 274 */     l19 ^= l19 << 8L;
/* 275 */     l19 ^= l19 << 16L;
/* 276 */     l19 ^= l19 << 32L;
/*     */     
/* 278 */     l19 &= 0xFFFFFFFFFFFL; l20 ^= l19 >>> 43L;
/*     */     
/* 280 */     l20 ^= l20 << 1L;
/* 281 */     l20 ^= l20 << 2L;
/* 282 */     l20 ^= l20 << 4L;
/* 283 */     l20 ^= l20 << 8L;
/* 284 */     l20 ^= l20 << 16L;
/* 285 */     l20 ^= l20 << 32L;
/*     */     
/* 287 */     l20 &= 0xFFFFFFFFFFFL; l21 ^= l20 >>> 43L;
/*     */     
/* 289 */     l21 ^= l21 << 1L;
/* 290 */     l21 ^= l21 << 2L;
/* 291 */     l21 ^= l21 << 4L;
/* 292 */     l21 ^= l21 << 8L;
/* 293 */     l21 ^= l21 << 16L;
/* 294 */     l21 ^= l21 << 32L;
/*     */ 
/*     */ 
/*     */     
/* 298 */     paramArrayOflong3[0] = l16;
/* 299 */     paramArrayOflong3[1] = l17 ^ l19 ^ arrayOfLong2[2];
/* 300 */     paramArrayOflong3[2] = l18 ^ l20 ^ l19 ^ arrayOfLong2[3];
/* 301 */     paramArrayOflong3[3] = l21 ^ l20;
/* 302 */     paramArrayOflong3[4] = l21 ^ arrayOfLong2[2];
/* 303 */     paramArrayOflong3[5] = arrayOfLong2[3];
/*     */     
/* 305 */     implCompactExt(paramArrayOflong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulw(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 314 */     paramArrayOflong1[1] = paramLong2;
/* 315 */     paramArrayOflong1[2] = paramArrayOflong1[1] << 1L;
/* 316 */     paramArrayOflong1[3] = paramArrayOflong1[2] ^ paramLong2;
/* 317 */     paramArrayOflong1[4] = paramArrayOflong1[2] << 1L;
/* 318 */     paramArrayOflong1[5] = paramArrayOflong1[4] ^ paramLong2;
/* 319 */     paramArrayOflong1[6] = paramArrayOflong1[3] << 1L;
/* 320 */     paramArrayOflong1[7] = paramArrayOflong1[6] ^ paramLong2;
/*     */     
/* 322 */     int i = (int)paramLong1;
/* 323 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L ^ paramArrayOflong1[i >>> 6 & 0x7] << 6L ^ paramArrayOflong1[i >>> 9 & 0x7] << 9L ^ paramArrayOflong1[i >>> 12 & 0x7] << 12L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 328 */     byte b = 30;
/*     */ 
/*     */     
/* 331 */     do { i = (int)(paramLong1 >>> b);
/* 332 */       long l = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L ^ paramArrayOflong1[i >>> 6 & 0x7] << 6L ^ paramArrayOflong1[i >>> 9 & 0x7] << 9L ^ paramArrayOflong1[i >>> 12 & 0x7] << 12L;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 337 */       l2 ^= l << b;
/* 338 */       l1 ^= l >>> -b;
/*     */       
/* 340 */       b -= 15; } while (b > 0);
/*     */ 
/*     */ 
/*     */     
/* 344 */     paramArrayOflong2[paramInt] = l2 & 0xFFFFFFFFFFFL;
/* 345 */     paramArrayOflong2[paramInt + 1] = l2 >>> 44L ^ l1 << 20L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 350 */     Interleave.expand64To128(paramArrayOflong1, 0, 2, paramArrayOflong2, 0);
/* 351 */     paramArrayOflong2[4] = Interleave.expand8to16((int)paramArrayOflong1[2]) & 0xFFFFFFFFL;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT131Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */