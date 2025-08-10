/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat224;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP224K1Field
/*     */ {
/*  14 */   static final int[] P = new int[] { -6803, -2, -1, -1, -1, -1, -1 };
/*     */   
/*  16 */   private static final int[] PExt = new int[] { 46280809, 13606, 1, 0, 0, 0, 0, -13606, -3, -1, -1, -1, -1, -1 };
/*     */   
/*  18 */   private static final int[] PExtInv = new int[] { -46280809, -13607, -2, -1, -1, -1, -1, 13605, 2 };
/*     */   
/*     */   private static final int P6 = -1;
/*     */   
/*     */   private static final int PExt13 = -1;
/*     */   private static final int PInv33 = 6803;
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  26 */     int i = Nat224.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  27 */     if (i != 0 || (paramArrayOfint3[6] == -1 && Nat224.gte(paramArrayOfint3, P)))
/*     */     {
/*  29 */       Nat.add33To(7, 6803, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  35 */     int i = Nat.add(14, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  36 */     if (i != 0 || (paramArrayOfint3[13] == -1 && Nat.gte(14, paramArrayOfint3, PExt)))
/*     */     {
/*  38 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/*  40 */         Nat.incAt(14, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  47 */     int i = Nat.inc(7, paramArrayOfint1, paramArrayOfint2);
/*  48 */     if (i != 0 || (paramArrayOfint2[6] == -1 && Nat224.gte(paramArrayOfint2, P)))
/*     */     {
/*  50 */       Nat.add33To(7, 6803, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  56 */     int[] arrayOfInt = Nat224.fromBigInteger(paramBigInteger);
/*  57 */     if (arrayOfInt[6] == -1 && Nat224.gte(arrayOfInt, P))
/*     */     {
/*  59 */       Nat.add33To(7, 6803, arrayOfInt);
/*     */     }
/*  61 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  66 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  68 */       Nat.shiftDownBit(7, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  72 */       int i = Nat224.add(paramArrayOfint1, P, paramArrayOfint2);
/*  73 */       Nat.shiftDownBit(7, paramArrayOfint2, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  79 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  84 */     int i = 0;
/*  85 */     for (byte b = 0; b < 7; b++)
/*     */     {
/*  87 */       i |= paramArrayOfint[b];
/*     */     }
/*  89 */     i = i >>> 1 | i & 0x1;
/*  90 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  95 */     int[] arrayOfInt = Nat224.createExt();
/*  96 */     Nat224.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  97 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 102 */     int i = Nat224.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 103 */     if (i != 0 || (paramArrayOfint3[13] == -1 && Nat.gte(14, paramArrayOfint3, PExt)))
/*     */     {
/* 105 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 107 */         Nat.incAt(14, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 114 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 116 */       Nat224.sub(P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 120 */       Nat224.sub(P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 126 */     byte[] arrayOfByte = new byte[28];
/*     */     
/*     */     do {
/* 129 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 130 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 7);
/*     */     }
/* 132 */     while (0 == Nat.lessThan(7, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 139 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 141 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 146 */     long l = Nat224.mul33Add(6803, paramArrayOfint1, 7, paramArrayOfint1, 0, paramArrayOfint2, 0);
/* 147 */     int i = Nat224.mul33DWordAdd(6803, l, paramArrayOfint2, 0);
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (i != 0 || (paramArrayOfint2[6] == -1 && Nat224.gte(paramArrayOfint2, P)))
/*     */     {
/* 153 */       Nat.add33To(7, 6803, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 159 */     if ((paramInt != 0 && Nat224.mul33WordAdd(6803, paramInt, paramArrayOfint, 0) != 0) || (paramArrayOfint[6] == -1 && 
/* 160 */       Nat224.gte(paramArrayOfint, P)))
/*     */     {
/* 162 */       Nat.add33To(7, 6803, paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 168 */     int[] arrayOfInt = Nat224.createExt();
/* 169 */     Nat224.square(paramArrayOfint1, arrayOfInt);
/* 170 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 177 */     int[] arrayOfInt = Nat224.createExt();
/* 178 */     Nat224.square(paramArrayOfint1, arrayOfInt);
/* 179 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 181 */     while (--paramInt > 0) {
/*     */       
/* 183 */       Nat224.square(paramArrayOfint2, arrayOfInt);
/* 184 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 190 */     int i = Nat224.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 191 */     if (i != 0)
/*     */     {
/* 193 */       Nat.sub33From(7, 6803, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 199 */     int i = Nat.sub(14, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 200 */     if (i != 0)
/*     */     {
/* 202 */       if (Nat.subFrom(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 204 */         Nat.decAt(14, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 211 */     int i = Nat.shiftUpBit(7, paramArrayOfint1, 0, paramArrayOfint2);
/* 212 */     if (i != 0 || (paramArrayOfint2[6] == -1 && Nat224.gte(paramArrayOfint2, P)))
/*     */     {
/* 214 */       Nat.add33To(7, 6803, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP224K1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */