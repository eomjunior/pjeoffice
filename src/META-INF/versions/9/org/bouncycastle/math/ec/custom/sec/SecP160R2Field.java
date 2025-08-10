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
/*     */ public class SecP160R2Field
/*     */ {
/*  14 */   static final int[] P = new int[] { -21389, -2, -1, -1, -1 };
/*  15 */   private static final int[] PExt = new int[] { 457489321, 42778, 1, 0, 0, -42778, -3, -1, -1, -1 };
/*     */   
/*  17 */   private static final int[] PExtInv = new int[] { -457489321, -42779, -2, -1, -1, 42777, 2 };
/*     */   
/*     */   private static final int P4 = -1;
/*     */   
/*     */   private static final int PExt9 = -1;
/*     */   private static final int PInv33 = 21389;
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  25 */     int i = Nat160.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  26 */     if (i != 0 || (paramArrayOfint3[4] == -1 && Nat160.gte(paramArrayOfint3, P)))
/*     */     {
/*  28 */       Nat.add33To(5, 21389, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  34 */     int i = Nat.add(10, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  35 */     if (i != 0 || (paramArrayOfint3[9] == -1 && Nat.gte(10, paramArrayOfint3, PExt)))
/*     */     {
/*  37 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/*  39 */         Nat.incAt(10, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  46 */     int i = Nat.inc(5, paramArrayOfint1, paramArrayOfint2);
/*  47 */     if (i != 0 || (paramArrayOfint2[4] == -1 && Nat160.gte(paramArrayOfint2, P)))
/*     */     {
/*  49 */       Nat.add33To(5, 21389, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  55 */     int[] arrayOfInt = Nat160.fromBigInteger(paramBigInteger);
/*  56 */     if (arrayOfInt[4] == -1 && Nat160.gte(arrayOfInt, P))
/*     */     {
/*  58 */       Nat160.subFrom(P, arrayOfInt);
/*     */     }
/*  60 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  65 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  67 */       Nat.shiftDownBit(5, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  71 */       int i = Nat160.add(paramArrayOfint1, P, paramArrayOfint2);
/*  72 */       Nat.shiftDownBit(5, paramArrayOfint2, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void inv(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  78 */     Mod.checkedModOddInverse(P, paramArrayOfint1, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int isZero(int[] paramArrayOfint) {
/*  83 */     int i = 0;
/*  84 */     for (byte b = 0; b < 5; b++)
/*     */     {
/*  86 */       i |= paramArrayOfint[b];
/*     */     }
/*  88 */     i = i >>> 1 | i & 0x1;
/*  89 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  94 */     int[] arrayOfInt = Nat160.createExt();
/*  95 */     Nat160.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  96 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 101 */     int i = Nat160.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 102 */     if (i != 0 || (paramArrayOfint3[9] == -1 && Nat.gte(10, paramArrayOfint3, PExt)))
/*     */     {
/* 104 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 106 */         Nat.incAt(10, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 113 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 115 */       Nat160.sub(P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 119 */       Nat160.sub(P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 125 */     byte[] arrayOfByte = new byte[20];
/*     */     
/*     */     do {
/* 128 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 129 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 5);
/*     */     }
/* 131 */     while (0 == Nat.lessThan(5, paramArrayOfint, P));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void randomMult(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/*     */     do {
/* 138 */       random(paramSecureRandom, paramArrayOfint);
/*     */     }
/* 140 */     while (0 != isZero(paramArrayOfint));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 145 */     long l = Nat160.mul33Add(21389, paramArrayOfint1, 5, paramArrayOfint1, 0, paramArrayOfint2, 0);
/* 146 */     int i = Nat160.mul33DWordAdd(21389, l, paramArrayOfint2, 0);
/*     */ 
/*     */ 
/*     */     
/* 150 */     if (i != 0 || (paramArrayOfint2[4] == -1 && Nat160.gte(paramArrayOfint2, P)))
/*     */     {
/* 152 */       Nat.add33To(5, 21389, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 158 */     if ((paramInt != 0 && Nat160.mul33WordAdd(21389, paramInt, paramArrayOfint, 0) != 0) || (paramArrayOfint[4] == -1 && 
/* 159 */       Nat160.gte(paramArrayOfint, P)))
/*     */     {
/* 161 */       Nat.add33To(5, 21389, paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 167 */     int[] arrayOfInt = Nat160.createExt();
/* 168 */     Nat160.square(paramArrayOfint1, arrayOfInt);
/* 169 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 176 */     int[] arrayOfInt = Nat160.createExt();
/* 177 */     Nat160.square(paramArrayOfint1, arrayOfInt);
/* 178 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 180 */     while (--paramInt > 0) {
/*     */       
/* 182 */       Nat160.square(paramArrayOfint2, arrayOfInt);
/* 183 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 189 */     int i = Nat160.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 190 */     if (i != 0)
/*     */     {
/* 192 */       Nat.sub33From(5, 21389, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 198 */     int i = Nat.sub(10, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 199 */     if (i != 0)
/*     */     {
/* 201 */       if (Nat.subFrom(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 203 */         Nat.decAt(10, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 210 */     int i = Nat.shiftUpBit(5, paramArrayOfint1, 0, paramArrayOfint2);
/* 211 */     if (i != 0 || (paramArrayOfint2[4] == -1 && Nat160.gte(paramArrayOfint2, P)))
/*     */     {
/* 213 */       Nat.add33To(5, 21389, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP160R2Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */