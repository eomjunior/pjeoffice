/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat512;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP521R1Field
/*     */ {
/*  14 */   static final int[] P = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 511 };
/*     */   
/*     */   private static final int P16 = 511;
/*     */ 
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  20 */     int i = Nat.add(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3) + paramArrayOfint1[16] + paramArrayOfint2[16];
/*  21 */     if (i > 511 || (i == 511 && Nat.eq(16, paramArrayOfint3, P))) {
/*     */       
/*  23 */       i += Nat.inc(16, paramArrayOfint3);
/*  24 */       i &= 0x1FF;
/*     */     } 
/*  26 */     paramArrayOfint3[16] = i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  31 */     int i = Nat.inc(16, paramArrayOfint1, paramArrayOfint2) + paramArrayOfint1[16];
/*  32 */     if (i > 511 || (i == 511 && Nat.eq(16, paramArrayOfint2, P))) {
/*     */       
/*  34 */       i += Nat.inc(16, paramArrayOfint2);
/*  35 */       i &= 0x1FF;
/*     */     } 
/*  37 */     paramArrayOfint2[16] = i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  42 */     int[] arrayOfInt = Nat.fromBigInteger(521, paramBigInteger);
/*  43 */     if (Nat.eq(17, arrayOfInt, P))
/*     */     {
/*  45 */       Nat.zero(17, arrayOfInt);
/*     */     }
/*  47 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  52 */     int i = paramArrayOfint1[16];
/*  53 */     int j = Nat.shiftDownBit(16, paramArrayOfint1, i, paramArrayOfint2);
/*  54 */     paramArrayOfint2[16] = i >>> 1 | j >>> 23;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  59 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  64 */     int i = 0;
/*  65 */     for (byte b = 0; b < 17; b++)
/*     */     {
/*  67 */       i |= paramArrayOfint[b];
/*     */     }
/*  69 */     i = i >>> 1 | i & 0x1;
/*  70 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  75 */     int[] arrayOfInt = Nat.create(33);
/*  76 */     implMultiply(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  77 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  82 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/*  84 */       Nat.sub(17, P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  88 */       Nat.sub(17, P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*  94 */     byte[] arrayOfByte = new byte[68];
/*     */     
/*     */     do {
/*  97 */       paramSecureRandom.nextBytes(arrayOfByte);
/*  98 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 17);
/*  99 */       paramArrayOfint[16] = paramArrayOfint[16] & 0x1FF;
/*     */     }
/* 101 */     while (0 == Nat.lessThan(17, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 108 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 110 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 117 */     int i = paramArrayOfint1[32];
/* 118 */     int j = Nat.shiftDownBits(16, paramArrayOfint1, 16, 9, i, paramArrayOfint2, 0) >>> 23;
/* 119 */     j += i >>> 9;
/* 120 */     j += Nat.addTo(16, paramArrayOfint1, paramArrayOfint2);
/* 121 */     if (j > 511 || (j == 511 && Nat.eq(16, paramArrayOfint2, P))) {
/*     */       
/* 123 */       j += Nat.inc(16, paramArrayOfint2);
/* 124 */       j &= 0x1FF;
/*     */     } 
/* 126 */     paramArrayOfint2[16] = j;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce23(int[] paramArrayOfint) {
/* 131 */     int i = paramArrayOfint[16];
/* 132 */     int j = Nat.addWordTo(16, i >>> 9, paramArrayOfint) + (i & 0x1FF);
/* 133 */     if (j > 511 || (j == 511 && Nat.eq(16, paramArrayOfint, P))) {
/*     */       
/* 135 */       j += Nat.inc(16, paramArrayOfint);
/* 136 */       j &= 0x1FF;
/*     */     } 
/* 138 */     paramArrayOfint[16] = j;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 143 */     int[] arrayOfInt = Nat.create(33);
/* 144 */     implSquare(paramArrayOfint1, arrayOfInt);
/* 145 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 152 */     int[] arrayOfInt = Nat.create(33);
/* 153 */     implSquare(paramArrayOfint1, arrayOfInt);
/* 154 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 156 */     while (--paramInt > 0) {
/*     */       
/* 158 */       implSquare(paramArrayOfint2, arrayOfInt);
/* 159 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 165 */     int i = Nat.sub(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3) + paramArrayOfint1[16] - paramArrayOfint2[16];
/* 166 */     if (i < 0) {
/*     */       
/* 168 */       i += Nat.dec(16, paramArrayOfint3);
/* 169 */       i &= 0x1FF;
/*     */     } 
/* 171 */     paramArrayOfint3[16] = i;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 176 */     int i = paramArrayOfint1[16];
/* 177 */     int j = Nat.shiftUpBit(16, paramArrayOfint1, i << 23, paramArrayOfint2) | i << 1;
/* 178 */     paramArrayOfint2[16] = j & 0x1FF;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implMultiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 183 */     Nat512.mul(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*     */     
/* 185 */     int i = paramArrayOfint1[16], j = paramArrayOfint2[16];
/* 186 */     paramArrayOfint3[32] = Nat.mul31BothAdd(16, i, paramArrayOfint2, j, paramArrayOfint1, paramArrayOfint3, 16) + i * j;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void implSquare(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 191 */     Nat512.square(paramArrayOfint1, paramArrayOfint2);
/*     */     
/* 193 */     int i = paramArrayOfint1[16];
/* 194 */     paramArrayOfint2[32] = Nat.mulWordAddTo(16, i << 1, paramArrayOfint1, 0, paramArrayOfint2, 16) + i * i;
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP521R1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */