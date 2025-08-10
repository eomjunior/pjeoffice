/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat160;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP160R1Field
/*     */ {
/*     */   private static final long M = 4294967295L;
/*  16 */   static final int[] P = new int[] { Integer.MAX_VALUE, -1, -1, -1, -1 };
/*  17 */   private static final int[] PExt = new int[] { 1, 1073741825, 0, 0, 0, -2, -2, -1, -1, -1 };
/*     */   
/*  19 */   private static final int[] PExtInv = new int[] { -1, -1073741826, -1, -1, -1, 1, 1 };
/*     */   
/*     */   private static final int P4 = -1;
/*     */   
/*     */   private static final int PExt9 = -1;
/*     */   private static final int PInv = -2147483647;
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  27 */     int i = Nat160.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  28 */     if (i != 0 || (paramArrayOfint3[4] == -1 && Nat160.gte(paramArrayOfint3, P)))
/*     */     {
/*  30 */       Nat.addWordTo(5, -2147483647, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  36 */     int i = Nat.add(10, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  37 */     if (i != 0 || (paramArrayOfint3[9] == -1 && Nat.gte(10, paramArrayOfint3, PExt)))
/*     */     {
/*  39 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/*  41 */         Nat.incAt(10, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  48 */     int i = Nat.inc(5, paramArrayOfint1, paramArrayOfint2);
/*  49 */     if (i != 0 || (paramArrayOfint2[4] == -1 && Nat160.gte(paramArrayOfint2, P)))
/*     */     {
/*  51 */       Nat.addWordTo(5, -2147483647, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  57 */     int[] arrayOfInt = Nat160.fromBigInteger(paramBigInteger);
/*  58 */     if (arrayOfInt[4] == -1 && Nat160.gte(arrayOfInt, P))
/*     */     {
/*  60 */       Nat160.subFrom(P, arrayOfInt);
/*     */     }
/*  62 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  67 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  69 */       Nat.shiftDownBit(5, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  73 */       int i = Nat160.add(paramArrayOfint1, P, paramArrayOfint2);
/*  74 */       Nat.shiftDownBit(5, paramArrayOfint2, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  80 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  85 */     int i = 0;
/*  86 */     for (byte b = 0; b < 5; b++)
/*     */     {
/*  88 */       i |= paramArrayOfint[b];
/*     */     }
/*  90 */     i = i >>> 1 | i & 0x1;
/*  91 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  96 */     int[] arrayOfInt = Nat160.createExt();
/*  97 */     Nat160.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  98 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 103 */     int i = Nat160.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 104 */     if (i != 0 || (paramArrayOfint3[9] == -1 && Nat.gte(10, paramArrayOfint3, PExt)))
/*     */     {
/* 106 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 108 */         Nat.incAt(10, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 115 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 117 */       Nat160.sub(P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 121 */       Nat160.sub(P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 127 */     byte[] arrayOfByte = new byte[20];
/*     */     
/*     */     do {
/* 130 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 131 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 5);
/*     */     }
/* 133 */     while (0 == Nat.lessThan(5, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 140 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 142 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 147 */     long l1 = paramArrayOfint1[5] & 0xFFFFFFFFL, l2 = paramArrayOfint1[6] & 0xFFFFFFFFL, l3 = paramArrayOfint1[7] & 0xFFFFFFFFL, l4 = paramArrayOfint1[8] & 0xFFFFFFFFL, l5 = paramArrayOfint1[9] & 0xFFFFFFFFL;
/*     */     
/* 149 */     long l6 = 0L;
/* 150 */     l6 += (paramArrayOfint1[0] & 0xFFFFFFFFL) + l1 + (l1 << 31L);
/* 151 */     paramArrayOfint2[0] = (int)l6; l6 >>>= 32L;
/* 152 */     l6 += (paramArrayOfint1[1] & 0xFFFFFFFFL) + l2 + (l2 << 31L);
/* 153 */     paramArrayOfint2[1] = (int)l6; l6 >>>= 32L;
/* 154 */     l6 += (paramArrayOfint1[2] & 0xFFFFFFFFL) + l3 + (l3 << 31L);
/* 155 */     paramArrayOfint2[2] = (int)l6; l6 >>>= 32L;
/* 156 */     l6 += (paramArrayOfint1[3] & 0xFFFFFFFFL) + l4 + (l4 << 31L);
/* 157 */     paramArrayOfint2[3] = (int)l6; l6 >>>= 32L;
/* 158 */     l6 += (paramArrayOfint1[4] & 0xFFFFFFFFL) + l5 + (l5 << 31L);
/* 159 */     paramArrayOfint2[4] = (int)l6; l6 >>>= 32L;
/*     */ 
/*     */ 
/*     */     
/* 163 */     reduce32((int)l6, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 168 */     if ((paramInt != 0 && Nat160.mulWordsAdd(-2147483647, paramInt, paramArrayOfint, 0) != 0) || (paramArrayOfint[4] == -1 && 
/* 169 */       Nat160.gte(paramArrayOfint, P)))
/*     */     {
/* 171 */       Nat.addWordTo(5, -2147483647, paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 177 */     int[] arrayOfInt = Nat160.createExt();
/* 178 */     Nat160.square(paramArrayOfint1, arrayOfInt);
/* 179 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 186 */     int[] arrayOfInt = Nat160.createExt();
/* 187 */     Nat160.square(paramArrayOfint1, arrayOfInt);
/* 188 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 190 */     while (--paramInt > 0) {
/*     */       
/* 192 */       Nat160.square(paramArrayOfint2, arrayOfInt);
/* 193 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 199 */     int i = Nat160.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 200 */     if (i != 0)
/*     */     {
/* 202 */       Nat.subWordFrom(5, -2147483647, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 208 */     int i = Nat.sub(10, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 209 */     if (i != 0)
/*     */     {
/* 211 */       if (Nat.subFrom(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 213 */         Nat.decAt(10, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 220 */     int i = Nat.shiftUpBit(5, paramArrayOfint1, 0, paramArrayOfint2);
/* 221 */     if (i != 0 || (paramArrayOfint2[4] == -1 && Nat160.gte(paramArrayOfint2, P)))
/*     */     {
/* 223 */       Nat.addWordTo(5, -2147483647, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP160R1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */