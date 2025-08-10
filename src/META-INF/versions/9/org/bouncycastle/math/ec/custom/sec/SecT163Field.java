/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat192;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT163Field
/*     */ {
/*     */   private static final long M35 = 34359738367L;
/*     */   private static final long M55 = 36028797018963967L;
/*  14 */   private static final long[] ROOT_Z = new long[] { -5270498306774157648L, 5270498306774195053L, 19634136210L };
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
/*  30 */     paramArrayOflong3[5] = paramArrayOflong1[5] ^ paramArrayOflong2[5];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  35 */     paramArrayOflong2[0] = paramArrayOflong1[0] ^ 0x1L;
/*  36 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  37 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  42 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ paramArrayOflong1[0];
/*  43 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ paramArrayOflong1[1];
/*  44 */     paramArrayOflong2[2] = paramArrayOflong2[2] ^ paramArrayOflong1[2];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger(BigInteger paramBigInteger) {
/*  49 */     return Nat.fromBigInteger64(163, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  54 */     long[] arrayOfLong = Nat192.createExt64();
/*     */     
/*  56 */     Nat192.copy64(paramArrayOflong1, paramArrayOflong2);
/*  57 */     for (byte b = 1; b < '£'; b += 2) {
/*     */       
/*  59 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  60 */       reduce(arrayOfLong, paramArrayOflong2);
/*  61 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  62 */       reduce(arrayOfLong, paramArrayOflong2);
/*  63 */       addTo(paramArrayOflong1, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invert(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  69 */     if (Nat192.isZero64(paramArrayOflong1))
/*     */     {
/*  71 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  76 */     long[] arrayOfLong1 = Nat192.create64();
/*  77 */     long[] arrayOfLong2 = Nat192.create64();
/*     */     
/*  79 */     square(paramArrayOflong1, arrayOfLong1);
/*     */ 
/*     */     
/*  82 */     squareN(arrayOfLong1, 1, arrayOfLong2);
/*  83 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  84 */     squareN(arrayOfLong2, 1, arrayOfLong2);
/*  85 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/*  88 */     squareN(arrayOfLong1, 3, arrayOfLong2);
/*  89 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  90 */     squareN(arrayOfLong2, 3, arrayOfLong2);
/*  91 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/*  94 */     squareN(arrayOfLong1, 9, arrayOfLong2);
/*  95 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  96 */     squareN(arrayOfLong2, 9, arrayOfLong2);
/*  97 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 100 */     squareN(arrayOfLong1, 27, arrayOfLong2);
/* 101 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/* 102 */     squareN(arrayOfLong2, 27, arrayOfLong2);
/* 103 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 106 */     squareN(arrayOfLong1, 81, arrayOfLong2);
/* 107 */     multiply(arrayOfLong1, arrayOfLong2, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 112 */     long[] arrayOfLong = new long[8];
/* 113 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 114 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 119 */     long[] arrayOfLong = new long[8];
/* 120 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 121 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 126 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3], l5 = paramArrayOflong1[4], l6 = paramArrayOflong1[5];
/*     */     
/* 128 */     l3 ^= l6 << 29L ^ l6 << 32L ^ l6 << 35L ^ l6 << 36L;
/* 129 */     l4 ^= l6 >>> 35L ^ l6 >>> 32L ^ l6 >>> 29L ^ l6 >>> 28L;
/*     */     
/* 131 */     l2 ^= l5 << 29L ^ l5 << 32L ^ l5 << 35L ^ l5 << 36L;
/* 132 */     l3 ^= l5 >>> 35L ^ l5 >>> 32L ^ l5 >>> 29L ^ l5 >>> 28L;
/*     */     
/* 134 */     l1 ^= l4 << 29L ^ l4 << 32L ^ l4 << 35L ^ l4 << 36L;
/* 135 */     l2 ^= l4 >>> 35L ^ l4 >>> 32L ^ l4 >>> 29L ^ l4 >>> 28L;
/*     */     
/* 137 */     long l7 = l3 >>> 35L;
/* 138 */     paramArrayOflong2[0] = l1 ^ l7 ^ l7 << 3L ^ l7 << 6L ^ l7 << 7L;
/* 139 */     paramArrayOflong2[1] = l2;
/* 140 */     paramArrayOflong2[2] = l3 & 0x7FFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce29(long[] paramArrayOflong, int paramInt) {
/* 145 */     long l1 = paramArrayOflong[paramInt + 2], l2 = l1 >>> 35L;
/* 146 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2 ^ l2 << 3L ^ l2 << 6L ^ l2 << 7L;
/* 147 */     paramArrayOflong[paramInt + 2] = l1 & 0x7FFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 152 */     long[] arrayOfLong = Nat192.create64();
/*     */ 
/*     */     
/* 155 */     long l1 = Interleave.unshuffle(paramArrayOflong1[0]), l2 = Interleave.unshuffle(paramArrayOflong1[1]);
/* 156 */     long l3 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 157 */     arrayOfLong[0] = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 159 */     l1 = Interleave.unshuffle(paramArrayOflong1[2]);
/* 160 */     long l4 = l1 & 0xFFFFFFFFL;
/* 161 */     arrayOfLong[1] = l1 >>> 32L;
/*     */     
/* 163 */     multiply(arrayOfLong, ROOT_Z, paramArrayOflong2);
/*     */     
/* 165 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ l3;
/* 166 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ l4;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 171 */     long[] arrayOfLong = Nat192.createExt64();
/* 172 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 173 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 178 */     long[] arrayOfLong = Nat192.createExt64();
/* 179 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 180 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 187 */     long[] arrayOfLong = Nat192.createExt64();
/* 188 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 189 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 191 */     while (--paramInt > 0) {
/*     */       
/* 193 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 194 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 201 */     return (int)(paramArrayOflong[0] ^ paramArrayOflong[2] >>> 29L) & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implCompactExt(long[] paramArrayOflong) {
/* 206 */     long l1 = paramArrayOflong[0], l2 = paramArrayOflong[1], l3 = paramArrayOflong[2], l4 = paramArrayOflong[3], l5 = paramArrayOflong[4], l6 = paramArrayOflong[5];
/* 207 */     paramArrayOflong[0] = l1 ^ l2 << 55L;
/* 208 */     paramArrayOflong[1] = l2 >>> 9L ^ l3 << 46L;
/* 209 */     paramArrayOflong[2] = l3 >>> 18L ^ l4 << 37L;
/* 210 */     paramArrayOflong[3] = l4 >>> 27L ^ l5 << 28L;
/* 211 */     paramArrayOflong[4] = l5 >>> 36L ^ l6 << 19L;
/* 212 */     paramArrayOflong[5] = l6 >>> 45L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 221 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2];
/* 222 */     l3 = l2 >>> 46L ^ l3 << 18L;
/* 223 */     l2 = (l1 >>> 55L ^ l2 << 9L) & 0x7FFFFFFFFFFFFFL;
/* 224 */     l1 &= 0x7FFFFFFFFFFFFFL;
/*     */     
/* 226 */     long l4 = paramArrayOflong2[0], l5 = paramArrayOflong2[1], l6 = paramArrayOflong2[2];
/* 227 */     l6 = l5 >>> 46L ^ l6 << 18L;
/* 228 */     l5 = (l4 >>> 55L ^ l5 << 9L) & 0x7FFFFFFFFFFFFFL;
/* 229 */     l4 &= 0x7FFFFFFFFFFFFFL;
/*     */     
/* 231 */     long[] arrayOfLong1 = paramArrayOflong3;
/* 232 */     long[] arrayOfLong2 = new long[10];
/*     */     
/* 234 */     implMulw(arrayOfLong1, l1, l4, arrayOfLong2, 0);
/* 235 */     implMulw(arrayOfLong1, l3, l6, arrayOfLong2, 2);
/*     */     
/* 237 */     long l7 = l1 ^ l2 ^ l3;
/* 238 */     long l8 = l4 ^ l5 ^ l6;
/*     */     
/* 240 */     implMulw(arrayOfLong1, l7, l8, arrayOfLong2, 4);
/*     */     
/* 242 */     long l9 = l2 << 1L ^ l3 << 2L;
/* 243 */     long l10 = l5 << 1L ^ l6 << 2L;
/*     */     
/* 245 */     implMulw(arrayOfLong1, l1 ^ l9, l4 ^ l10, arrayOfLong2, 6);
/* 246 */     implMulw(arrayOfLong1, l7 ^ l9, l8 ^ l10, arrayOfLong2, 8);
/*     */     
/* 248 */     long l11 = arrayOfLong2[6] ^ arrayOfLong2[8];
/* 249 */     long l12 = arrayOfLong2[7] ^ arrayOfLong2[9];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 254 */     long l13 = l11 << 1L ^ arrayOfLong2[6];
/* 255 */     long l14 = l11 ^ l12 << 1L ^ arrayOfLong2[7];
/* 256 */     long l15 = l12;
/*     */ 
/*     */     
/* 259 */     long l16 = arrayOfLong2[0];
/* 260 */     long l17 = arrayOfLong2[1] ^ arrayOfLong2[0] ^ arrayOfLong2[4];
/* 261 */     long l18 = arrayOfLong2[1] ^ arrayOfLong2[5];
/*     */ 
/*     */     
/* 264 */     long l19 = l16 ^ l13 ^ arrayOfLong2[2] << 4L ^ arrayOfLong2[2] << 1L;
/* 265 */     long l20 = l17 ^ l14 ^ arrayOfLong2[3] << 4L ^ arrayOfLong2[3] << 1L;
/* 266 */     long l21 = l18 ^ l15;
/*     */ 
/*     */     
/* 269 */     l20 ^= l19 >>> 55L; l19 &= 0x7FFFFFFFFFFFFFL;
/* 270 */     l21 ^= l20 >>> 55L; l20 &= 0x7FFFFFFFFFFFFFL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 276 */     l19 = l19 >>> 1L ^ (l20 & 0x1L) << 54L;
/* 277 */     l20 = l20 >>> 1L ^ (l21 & 0x1L) << 54L;
/* 278 */     l21 >>>= 1L;
/*     */ 
/*     */ 
/*     */     
/* 282 */     l19 ^= l19 << 1L;
/* 283 */     l19 ^= l19 << 2L;
/* 284 */     l19 ^= l19 << 4L;
/* 285 */     l19 ^= l19 << 8L;
/* 286 */     l19 ^= l19 << 16L;
/* 287 */     l19 ^= l19 << 32L;
/*     */     
/* 289 */     l19 &= 0x7FFFFFFFFFFFFFL; l20 ^= l19 >>> 54L;
/*     */     
/* 291 */     l20 ^= l20 << 1L;
/* 292 */     l20 ^= l20 << 2L;
/* 293 */     l20 ^= l20 << 4L;
/* 294 */     l20 ^= l20 << 8L;
/* 295 */     l20 ^= l20 << 16L;
/* 296 */     l20 ^= l20 << 32L;
/*     */     
/* 298 */     l20 &= 0x7FFFFFFFFFFFFFL; l21 ^= l20 >>> 54L;
/*     */     
/* 300 */     l21 ^= l21 << 1L;
/* 301 */     l21 ^= l21 << 2L;
/* 302 */     l21 ^= l21 << 4L;
/* 303 */     l21 ^= l21 << 8L;
/* 304 */     l21 ^= l21 << 16L;
/* 305 */     l21 ^= l21 << 32L;
/*     */ 
/*     */ 
/*     */     
/* 309 */     paramArrayOflong3[0] = l16;
/* 310 */     paramArrayOflong3[1] = l17 ^ l19 ^ arrayOfLong2[2];
/* 311 */     paramArrayOflong3[2] = l18 ^ l20 ^ l19 ^ arrayOfLong2[3];
/* 312 */     paramArrayOflong3[3] = l21 ^ l20;
/* 313 */     paramArrayOflong3[4] = l21 ^ arrayOfLong2[2];
/* 314 */     paramArrayOflong3[5] = arrayOfLong2[3];
/*     */     
/* 316 */     implCompactExt(paramArrayOflong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulw(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 325 */     paramArrayOflong1[1] = paramLong2;
/* 326 */     paramArrayOflong1[2] = paramArrayOflong1[1] << 1L;
/* 327 */     paramArrayOflong1[3] = paramArrayOflong1[2] ^ paramLong2;
/* 328 */     paramArrayOflong1[4] = paramArrayOflong1[2] << 1L;
/* 329 */     paramArrayOflong1[5] = paramArrayOflong1[4] ^ paramLong2;
/* 330 */     paramArrayOflong1[6] = paramArrayOflong1[3] << 1L;
/* 331 */     paramArrayOflong1[7] = paramArrayOflong1[6] ^ paramLong2;
/*     */     
/* 333 */     int i = (int)paramLong1;
/* 334 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0x3];
/* 335 */     byte b = 47;
/*     */ 
/*     */     
/* 338 */     do { i = (int)(paramLong1 >>> b);
/* 339 */       long l = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L ^ paramArrayOflong1[i >>> 6 & 0x7] << 6L;
/*     */ 
/*     */       
/* 342 */       l2 ^= l << b;
/* 343 */       l1 ^= l >>> -b;
/*     */       
/* 345 */       b -= 9; } while (b > 0);
/*     */ 
/*     */ 
/*     */     
/* 349 */     paramArrayOflong2[paramInt] = l2 & 0x7FFFFFFFFFFFFFL;
/* 350 */     paramArrayOflong2[paramInt + 1] = l2 >>> 55L ^ l1 << 9L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 355 */     Interleave.expand64To128(paramArrayOflong1, 0, 3, paramArrayOflong2, 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT163Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */