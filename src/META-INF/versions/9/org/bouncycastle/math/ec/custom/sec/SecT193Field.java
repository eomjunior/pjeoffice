/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT193Field
/*     */ {
/*     */   private static final long M01 = 1L;
/*     */   private static final long M49 = 562949953421311L;
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
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  35 */     paramArrayOflong2[0] = paramArrayOflong1[0] ^ 0x1L;
/*  36 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*  37 */     paramArrayOflong2[2] = paramArrayOflong1[2];
/*  38 */     paramArrayOflong2[3] = paramArrayOflong1[3];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  43 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ paramArrayOflong1[0];
/*  44 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ paramArrayOflong1[1];
/*  45 */     paramArrayOflong2[2] = paramArrayOflong2[2] ^ paramArrayOflong1[2];
/*  46 */     paramArrayOflong2[3] = paramArrayOflong2[3] ^ paramArrayOflong1[3];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger(BigInteger paramBigInteger) {
/*  51 */     return Nat.fromBigInteger64(193, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  56 */     long[] arrayOfLong = Nat256.createExt64();
/*     */     
/*  58 */     Nat256.copy64(paramArrayOflong1, paramArrayOflong2);
/*  59 */     for (byte b = 1; b < 'Á'; b += 2) {
/*     */       
/*  61 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  62 */       reduce(arrayOfLong, paramArrayOflong2);
/*  63 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  64 */       reduce(arrayOfLong, paramArrayOflong2);
/*  65 */       addTo(paramArrayOflong1, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invert(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  71 */     if (Nat256.isZero64(paramArrayOflong1))
/*     */     {
/*  73 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  78 */     long[] arrayOfLong1 = Nat256.create64();
/*  79 */     long[] arrayOfLong2 = Nat256.create64();
/*     */     
/*  81 */     square(paramArrayOflong1, arrayOfLong1);
/*     */ 
/*     */     
/*  84 */     squareN(arrayOfLong1, 1, arrayOfLong2);
/*  85 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  86 */     squareN(arrayOfLong2, 1, arrayOfLong2);
/*  87 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/*  90 */     squareN(arrayOfLong1, 3, arrayOfLong2);
/*  91 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/*  94 */     squareN(arrayOfLong1, 6, arrayOfLong2);
/*  95 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/*  98 */     squareN(arrayOfLong1, 12, arrayOfLong2);
/*  99 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 102 */     squareN(arrayOfLong1, 24, arrayOfLong2);
/* 103 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 106 */     squareN(arrayOfLong1, 48, arrayOfLong2);
/* 107 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*     */ 
/*     */     
/* 110 */     squareN(arrayOfLong1, 96, arrayOfLong2);
/* 111 */     multiply(arrayOfLong1, arrayOfLong2, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 116 */     long[] arrayOfLong = Nat256.createExt64();
/* 117 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 118 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 123 */     long[] arrayOfLong = Nat256.createExt64();
/* 124 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 125 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 130 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3], l5 = paramArrayOflong1[4], l6 = paramArrayOflong1[5], l7 = paramArrayOflong1[6];
/*     */     
/* 132 */     l3 ^= l7 << 63L;
/* 133 */     l4 ^= l7 >>> 1L ^ l7 << 14L;
/* 134 */     l5 ^= l7 >>> 50L;
/*     */     
/* 136 */     l2 ^= l6 << 63L;
/* 137 */     l3 ^= l6 >>> 1L ^ l6 << 14L;
/* 138 */     l4 ^= l6 >>> 50L;
/*     */     
/* 140 */     l1 ^= l5 << 63L;
/* 141 */     l2 ^= l5 >>> 1L ^ l5 << 14L;
/* 142 */     l3 ^= l5 >>> 50L;
/*     */     
/* 144 */     long l8 = l4 >>> 1L;
/* 145 */     paramArrayOflong2[0] = l1 ^ l8 ^ l8 << 15L;
/* 146 */     paramArrayOflong2[1] = l2 ^ l8 >>> 49L;
/* 147 */     paramArrayOflong2[2] = l3;
/* 148 */     paramArrayOflong2[3] = l4 & 0x1L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce63(long[] paramArrayOflong, int paramInt) {
/* 153 */     long l1 = paramArrayOflong[paramInt + 3], l2 = l1 >>> 1L;
/* 154 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2 ^ l2 << 15L;
/* 155 */     paramArrayOflong[paramInt + 1] = paramArrayOflong[paramInt + 1] ^ l2 >>> 49L;
/* 156 */     paramArrayOflong[paramInt + 3] = l1 & 0x1L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 162 */     long l1 = Interleave.unshuffle(paramArrayOflong1[0]), l2 = Interleave.unshuffle(paramArrayOflong1[1]);
/* 163 */     long l3 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 164 */     long l4 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 166 */     l1 = Interleave.unshuffle(paramArrayOflong1[2]);
/* 167 */     long l5 = l1 & 0xFFFFFFFFL ^ paramArrayOflong1[3] << 32L;
/* 168 */     long l6 = l1 >>> 32L;
/*     */     
/* 170 */     paramArrayOflong2[0] = l3 ^ l4 << 8L;
/* 171 */     paramArrayOflong2[1] = l5 ^ l6 << 8L ^ l4 >>> 56L ^ l4 << 33L;
/* 172 */     paramArrayOflong2[2] = l6 >>> 56L ^ l6 << 33L ^ l4 >>> 31L;
/* 173 */     paramArrayOflong2[3] = l6 >>> 31L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 178 */     long[] arrayOfLong = Nat256.createExt64();
/* 179 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 180 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 185 */     long[] arrayOfLong = Nat256.createExt64();
/* 186 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 187 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 194 */     long[] arrayOfLong = Nat256.createExt64();
/* 195 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 196 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 198 */     while (--paramInt > 0) {
/*     */       
/* 200 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 201 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 208 */     return (int)paramArrayOflong[0] & 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implCompactExt(long[] paramArrayOflong) {
/* 213 */     long l1 = paramArrayOflong[0], l2 = paramArrayOflong[1], l3 = paramArrayOflong[2], l4 = paramArrayOflong[3], l5 = paramArrayOflong[4], l6 = paramArrayOflong[5], l7 = paramArrayOflong[6], l8 = paramArrayOflong[7];
/* 214 */     paramArrayOflong[0] = l1 ^ l2 << 49L;
/* 215 */     paramArrayOflong[1] = l2 >>> 15L ^ l3 << 34L;
/* 216 */     paramArrayOflong[2] = l3 >>> 30L ^ l4 << 19L;
/* 217 */     paramArrayOflong[3] = l4 >>> 45L ^ l5 << 4L ^ l6 << 53L;
/*     */     
/* 219 */     paramArrayOflong[4] = l5 >>> 60L ^ l7 << 38L ^ l6 >>> 11L;
/*     */     
/* 221 */     paramArrayOflong[5] = l7 >>> 26L ^ l8 << 23L;
/* 222 */     paramArrayOflong[6] = l8 >>> 41L;
/* 223 */     paramArrayOflong[7] = 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implExpand(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 228 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3];
/* 229 */     paramArrayOflong2[0] = l1 & 0x1FFFFFFFFFFFFL;
/* 230 */     paramArrayOflong2[1] = (l1 >>> 49L ^ l2 << 15L) & 0x1FFFFFFFFFFFFL;
/* 231 */     paramArrayOflong2[2] = (l2 >>> 34L ^ l3 << 30L) & 0x1FFFFFFFFFFFFL;
/* 232 */     paramArrayOflong2[3] = l3 >>> 19L ^ l4 << 45L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 241 */     long[] arrayOfLong1 = new long[4], arrayOfLong2 = new long[4];
/* 242 */     implExpand(paramArrayOflong1, arrayOfLong1);
/* 243 */     implExpand(paramArrayOflong2, arrayOfLong2);
/*     */     
/* 245 */     long[] arrayOfLong3 = new long[8];
/*     */     
/* 247 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0], arrayOfLong2[0], paramArrayOflong3, 0);
/* 248 */     implMulwAcc(arrayOfLong3, arrayOfLong1[1], arrayOfLong2[1], paramArrayOflong3, 1);
/* 249 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2], arrayOfLong2[2], paramArrayOflong3, 2);
/* 250 */     implMulwAcc(arrayOfLong3, arrayOfLong1[3], arrayOfLong2[3], paramArrayOflong3, 3);
/*     */     
/*     */     byte b;
/* 253 */     for (b = 5; b > 0; b--)
/*     */     {
/* 255 */       paramArrayOflong3[b] = paramArrayOflong3[b] ^ paramArrayOflong3[b - 1];
/*     */     }
/*     */     
/* 258 */     implMulwAcc(arrayOfLong3, arrayOfLong1[0] ^ arrayOfLong1[1], arrayOfLong2[0] ^ arrayOfLong2[1], paramArrayOflong3, 1);
/* 259 */     implMulwAcc(arrayOfLong3, arrayOfLong1[2] ^ arrayOfLong1[3], arrayOfLong2[2] ^ arrayOfLong2[3], paramArrayOflong3, 3);
/*     */ 
/*     */     
/* 262 */     for (b = 7; b > 1; b--)
/*     */     {
/* 264 */       paramArrayOflong3[b] = paramArrayOflong3[b] ^ paramArrayOflong3[b - 2];
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 269 */     long l1 = arrayOfLong1[0] ^ arrayOfLong1[2], l2 = arrayOfLong1[1] ^ arrayOfLong1[3];
/* 270 */     long l3 = arrayOfLong2[0] ^ arrayOfLong2[2], l4 = arrayOfLong2[1] ^ arrayOfLong2[3];
/* 271 */     implMulwAcc(arrayOfLong3, l1 ^ l2, l3 ^ l4, paramArrayOflong3, 3);
/* 272 */     long[] arrayOfLong4 = new long[3];
/* 273 */     implMulwAcc(arrayOfLong3, l1, l3, arrayOfLong4, 0);
/* 274 */     implMulwAcc(arrayOfLong3, l2, l4, arrayOfLong4, 1);
/* 275 */     long l5 = arrayOfLong4[0], l6 = arrayOfLong4[1], l7 = arrayOfLong4[2];
/* 276 */     paramArrayOflong3[2] = paramArrayOflong3[2] ^ l5;
/* 277 */     paramArrayOflong3[3] = paramArrayOflong3[3] ^ l5 ^ l6;
/* 278 */     paramArrayOflong3[4] = paramArrayOflong3[4] ^ l7 ^ l6;
/* 279 */     paramArrayOflong3[5] = paramArrayOflong3[5] ^ l7;
/*     */ 
/*     */     
/* 282 */     implCompactExt(paramArrayOflong3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulwAcc(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 291 */     paramArrayOflong1[1] = paramLong2;
/* 292 */     paramArrayOflong1[2] = paramArrayOflong1[1] << 1L;
/* 293 */     paramArrayOflong1[3] = paramArrayOflong1[2] ^ paramLong2;
/* 294 */     paramArrayOflong1[4] = paramArrayOflong1[2] << 1L;
/* 295 */     paramArrayOflong1[5] = paramArrayOflong1[4] ^ paramLong2;
/* 296 */     paramArrayOflong1[6] = paramArrayOflong1[3] << 1L;
/* 297 */     paramArrayOflong1[7] = paramArrayOflong1[6] ^ paramLong2;
/*     */     
/* 299 */     int i = (int)paramLong1;
/* 300 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L;
/*     */     
/* 302 */     byte b = 36;
/*     */ 
/*     */     
/* 305 */     do { i = (int)(paramLong1 >>> b);
/* 306 */       long l = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L ^ paramArrayOflong1[i >>> 6 & 0x7] << 6L ^ paramArrayOflong1[i >>> 9 & 0x7] << 9L ^ paramArrayOflong1[i >>> 12 & 0x7] << 12L;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 311 */       l2 ^= l << b;
/* 312 */       l1 ^= l >>> -b;
/*     */       
/* 314 */       b -= 15; } while (b > 0);
/*     */ 
/*     */ 
/*     */     
/* 318 */     paramArrayOflong2[paramInt] = paramArrayOflong2[paramInt] ^ l2 & 0x1FFFFFFFFFFFFL;
/* 319 */     paramArrayOflong2[paramInt + 1] = paramArrayOflong2[paramInt + 1] ^ l2 >>> 49L ^ l1 << 15L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 324 */     Interleave.expand64To128(paramArrayOflong1, 0, 3, paramArrayOflong2, 0);
/* 325 */     paramArrayOflong2[6] = paramArrayOflong1[3] & 0x1L;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT193Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */