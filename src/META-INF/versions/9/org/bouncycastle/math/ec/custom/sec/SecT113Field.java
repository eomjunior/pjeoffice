/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import org.bouncycastle.math.raw.Interleave;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat128;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecT113Field
/*     */ {
/*     */   private static final long M49 = 562949953421311L;
/*     */   private static final long M57 = 144115188075855871L;
/*     */   
/*     */   public static void add(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  16 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  17 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  22 */     paramArrayOflong3[0] = paramArrayOflong1[0] ^ paramArrayOflong2[0];
/*  23 */     paramArrayOflong3[1] = paramArrayOflong1[1] ^ paramArrayOflong2[1];
/*  24 */     paramArrayOflong3[2] = paramArrayOflong1[2] ^ paramArrayOflong2[2];
/*  25 */     paramArrayOflong3[3] = paramArrayOflong1[3] ^ paramArrayOflong2[3];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  30 */     paramArrayOflong2[0] = paramArrayOflong1[0] ^ 0x1L;
/*  31 */     paramArrayOflong2[1] = paramArrayOflong1[1];
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addTo(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  36 */     paramArrayOflong2[0] = paramArrayOflong2[0] ^ paramArrayOflong1[0];
/*  37 */     paramArrayOflong2[1] = paramArrayOflong2[1] ^ paramArrayOflong1[1];
/*     */   }
/*     */ 
/*     */   
/*     */   public static long[] fromBigInteger(BigInteger paramBigInteger) {
/*  42 */     return Nat.fromBigInteger64(113, paramBigInteger);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void halfTrace(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  47 */     long[] arrayOfLong = Nat128.createExt64();
/*     */     
/*  49 */     Nat128.copy64(paramArrayOflong1, paramArrayOflong2);
/*  50 */     for (byte b = 1; b < 113; b += 2) {
/*     */       
/*  52 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  53 */       reduce(arrayOfLong, paramArrayOflong2);
/*  54 */       implSquare(paramArrayOflong2, arrayOfLong);
/*  55 */       reduce(arrayOfLong, paramArrayOflong2);
/*  56 */       addTo(paramArrayOflong1, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void invert(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/*  62 */     if (Nat128.isZero64(paramArrayOflong1))
/*     */     {
/*  64 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  69 */     long[] arrayOfLong1 = Nat128.create64();
/*  70 */     long[] arrayOfLong2 = Nat128.create64();
/*     */     
/*  72 */     square(paramArrayOflong1, arrayOfLong1);
/*  73 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/*  74 */     square(arrayOfLong1, arrayOfLong1);
/*  75 */     multiply(arrayOfLong1, paramArrayOflong1, arrayOfLong1);
/*  76 */     squareN(arrayOfLong1, 3, arrayOfLong2);
/*  77 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  78 */     square(arrayOfLong2, arrayOfLong2);
/*  79 */     multiply(arrayOfLong2, paramArrayOflong1, arrayOfLong2);
/*  80 */     squareN(arrayOfLong2, 7, arrayOfLong1);
/*  81 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  82 */     squareN(arrayOfLong1, 14, arrayOfLong2);
/*  83 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  84 */     squareN(arrayOfLong2, 28, arrayOfLong1);
/*  85 */     multiply(arrayOfLong1, arrayOfLong2, arrayOfLong1);
/*  86 */     squareN(arrayOfLong1, 56, arrayOfLong2);
/*  87 */     multiply(arrayOfLong2, arrayOfLong1, arrayOfLong2);
/*  88 */     square(arrayOfLong2, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/*  93 */     long[] arrayOfLong = new long[8];
/*  94 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/*  95 */     reduce(arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 100 */     long[] arrayOfLong = new long[8];
/* 101 */     implMultiply(paramArrayOflong1, paramArrayOflong2, arrayOfLong);
/* 102 */     addExt(paramArrayOflong3, arrayOfLong, paramArrayOflong3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 107 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1], l3 = paramArrayOflong1[2], l4 = paramArrayOflong1[3];
/*     */     
/* 109 */     l2 ^= l4 << 15L ^ l4 << 24L;
/* 110 */     l3 ^= l4 >>> 49L ^ l4 >>> 40L;
/*     */     
/* 112 */     l1 ^= l3 << 15L ^ l3 << 24L;
/* 113 */     l2 ^= l3 >>> 49L ^ l3 >>> 40L;
/*     */     
/* 115 */     long l5 = l2 >>> 49L;
/* 116 */     paramArrayOflong2[0] = l1 ^ l5 ^ l5 << 9L;
/* 117 */     paramArrayOflong2[1] = l2 & 0x1FFFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce15(long[] paramArrayOflong, int paramInt) {
/* 122 */     long l1 = paramArrayOflong[paramInt + 1], l2 = l1 >>> 49L;
/* 123 */     paramArrayOflong[paramInt] = paramArrayOflong[paramInt] ^ l2 ^ l2 << 9L;
/* 124 */     paramArrayOflong[paramInt + 1] = l1 & 0x1FFFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void sqrt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 129 */     long l1 = Interleave.unshuffle(paramArrayOflong1[0]), l2 = Interleave.unshuffle(paramArrayOflong1[1]);
/* 130 */     long l3 = l1 & 0xFFFFFFFFL | l2 << 32L;
/* 131 */     long l4 = l1 >>> 32L | l2 & 0xFFFFFFFF00000000L;
/*     */     
/* 133 */     paramArrayOflong2[0] = l3 ^ l4 << 57L ^ l4 << 5L;
/* 134 */     paramArrayOflong2[1] = l4 >>> 7L ^ l4 >>> 59L;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 139 */     long[] arrayOfLong = Nat128.createExt64();
/* 140 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 141 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void squareAddToExt(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 146 */     long[] arrayOfLong = Nat128.createExt64();
/* 147 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 148 */     addExt(paramArrayOflong2, arrayOfLong, paramArrayOflong2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(long[] paramArrayOflong1, int paramInt, long[] paramArrayOflong2) {
/* 155 */     long[] arrayOfLong = Nat128.createExt64();
/* 156 */     implSquare(paramArrayOflong1, arrayOfLong);
/* 157 */     reduce(arrayOfLong, paramArrayOflong2);
/*     */     
/* 159 */     while (--paramInt > 0) {
/*     */       
/* 161 */       implSquare(paramArrayOflong2, arrayOfLong);
/* 162 */       reduce(arrayOfLong, paramArrayOflong2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int trace(long[] paramArrayOflong) {
/* 169 */     return (int)paramArrayOflong[0] & 0x1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMultiply(long[] paramArrayOflong1, long[] paramArrayOflong2, long[] paramArrayOflong3) {
/* 178 */     long l1 = paramArrayOflong1[0], l2 = paramArrayOflong1[1];
/* 179 */     l2 = (l1 >>> 57L ^ l2 << 7L) & 0x1FFFFFFFFFFFFFFL;
/* 180 */     l1 &= 0x1FFFFFFFFFFFFFFL;
/*     */     
/* 182 */     long l3 = paramArrayOflong2[0], l4 = paramArrayOflong2[1];
/* 183 */     l4 = (l3 >>> 57L ^ l4 << 7L) & 0x1FFFFFFFFFFFFFFL;
/* 184 */     l3 &= 0x1FFFFFFFFFFFFFFL;
/*     */     
/* 186 */     long[] arrayOfLong1 = paramArrayOflong3;
/* 187 */     long[] arrayOfLong2 = new long[6];
/*     */     
/* 189 */     implMulw(arrayOfLong1, l1, l3, arrayOfLong2, 0);
/* 190 */     implMulw(arrayOfLong1, l2, l4, arrayOfLong2, 2);
/* 191 */     implMulw(arrayOfLong1, l1 ^ l2, l3 ^ l4, arrayOfLong2, 4);
/*     */     
/* 193 */     long l5 = arrayOfLong2[1] ^ arrayOfLong2[2];
/* 194 */     long l6 = arrayOfLong2[0];
/* 195 */     long l7 = arrayOfLong2[3];
/* 196 */     long l8 = arrayOfLong2[4] ^ l6 ^ l5;
/* 197 */     long l9 = arrayOfLong2[5] ^ l7 ^ l5;
/*     */     
/* 199 */     paramArrayOflong3[0] = l6 ^ l8 << 57L;
/* 200 */     paramArrayOflong3[1] = l8 >>> 7L ^ l9 << 50L;
/* 201 */     paramArrayOflong3[2] = l9 >>> 14L ^ l7 << 43L;
/* 202 */     paramArrayOflong3[3] = l7 >>> 21L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void implMulw(long[] paramArrayOflong1, long paramLong1, long paramLong2, long[] paramArrayOflong2, int paramInt) {
/* 211 */     paramArrayOflong1[1] = paramLong2;
/* 212 */     paramArrayOflong1[2] = paramArrayOflong1[1] << 1L;
/* 213 */     paramArrayOflong1[3] = paramArrayOflong1[2] ^ paramLong2;
/* 214 */     paramArrayOflong1[4] = paramArrayOflong1[2] << 1L;
/* 215 */     paramArrayOflong1[5] = paramArrayOflong1[4] ^ paramLong2;
/* 216 */     paramArrayOflong1[6] = paramArrayOflong1[3] << 1L;
/* 217 */     paramArrayOflong1[7] = paramArrayOflong1[6] ^ paramLong2;
/*     */     
/* 219 */     int i = (int)paramLong1;
/* 220 */     long l1 = 0L, l2 = paramArrayOflong1[i & 0x7];
/* 221 */     byte b = 48;
/*     */ 
/*     */     
/* 224 */     do { i = (int)(paramLong1 >>> b);
/* 225 */       long l = paramArrayOflong1[i & 0x7] ^ paramArrayOflong1[i >>> 3 & 0x7] << 3L ^ paramArrayOflong1[i >>> 6 & 0x7] << 6L;
/*     */ 
/*     */       
/* 228 */       l2 ^= l << b;
/* 229 */       l1 ^= l >>> -b;
/*     */       
/* 231 */       b -= 9; } while (b > 0);
/*     */     
/* 233 */     l1 ^= (paramLong1 & 0x100804020100800L & paramLong2 << 7L >> 63L) >>> 8L;
/*     */ 
/*     */ 
/*     */     
/* 237 */     paramArrayOflong2[paramInt] = l2 & 0x1FFFFFFFFFFFFFFL;
/* 238 */     paramArrayOflong2[paramInt + 1] = l2 >>> 57L ^ l1 << 7L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(long[] paramArrayOflong1, long[] paramArrayOflong2) {
/* 243 */     Interleave.expand64To128(paramArrayOflong1, 0, 2, paramArrayOflong2, 0);
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecT113Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */