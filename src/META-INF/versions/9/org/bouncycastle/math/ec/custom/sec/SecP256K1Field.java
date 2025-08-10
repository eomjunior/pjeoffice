/*     */ package META-INF.versions.9.org.bouncycastle.math.ec.custom.sec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.security.SecureRandom;
/*     */ import org.bouncycastle.math.raw.Mod;
/*     */ import org.bouncycastle.math.raw.Nat;
/*     */ import org.bouncycastle.math.raw.Nat256;
/*     */ import org.bouncycastle.util.Pack;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SecP256K1Field
/*     */ {
/*  14 */   static final int[] P = new int[] { -977, -2, -1, -1, -1, -1, -1, -1 };
/*     */   
/*  16 */   private static final int[] PExt = new int[] { 954529, 1954, 1, 0, 0, 0, 0, 0, -1954, -3, -1, -1, -1, -1, -1, -1 };
/*     */ 
/*     */   
/*  19 */   private static final int[] PExtInv = new int[] { -954529, -1955, -2, -1, -1, -1, -1, -1, 1953, 2 };
/*     */   
/*     */   private static final int P7 = -1;
/*     */   
/*     */   private static final int PExt15 = -1;
/*     */   private static final int PInv33 = 977;
/*     */   
/*     */   public static void add(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  27 */     int i = Nat256.add(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  28 */     if (i != 0 || (paramArrayOfint3[7] == -1 && Nat256.gte(paramArrayOfint3, P)))
/*     */     {
/*  30 */       Nat.add33To(8, 977, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  36 */     int i = Nat.add(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/*  37 */     if (i != 0 || (paramArrayOfint3[15] == -1 && Nat.gte(16, paramArrayOfint3, PExt)))
/*     */     {
/*  39 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/*  41 */         Nat.incAt(16, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addOne(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  48 */     int i = Nat.inc(8, paramArrayOfint1, paramArrayOfint2);
/*  49 */     if (i != 0 || (paramArrayOfint2[7] == -1 && Nat256.gte(paramArrayOfint2, P)))
/*     */     {
/*  51 */       Nat.add33To(8, 977, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] fromBigInteger(BigInteger paramBigInteger) {
/*  57 */     int[] arrayOfInt = Nat256.fromBigInteger(paramBigInteger);
/*  58 */     if (arrayOfInt[7] == -1 && Nat256.gte(arrayOfInt, P))
/*     */     {
/*  60 */       Nat256.subFrom(P, arrayOfInt);
/*     */     }
/*  62 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void half(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/*  67 */     if ((paramArrayOfint1[0] & 0x1) == 0) {
/*     */       
/*  69 */       Nat.shiftDownBit(8, paramArrayOfint1, 0, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/*  73 */       int i = Nat256.add(paramArrayOfint1, P, paramArrayOfint2);
/*  74 */       Nat.shiftDownBit(8, paramArrayOfint2, i);
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
/*  86 */     for (byte b = 0; b < 8; b++)
/*     */     {
/*  88 */       i |= paramArrayOfint[b];
/*     */     }
/*  90 */     i = i >>> 1 | i & 0x1;
/*  91 */     return i - 1 >> 31;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiply(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/*  96 */     int[] arrayOfInt = Nat256.createExt();
/*  97 */     Nat256.mul(paramArrayOfint1, paramArrayOfint2, arrayOfInt);
/*  98 */     reduce(arrayOfInt, paramArrayOfint3);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void multiplyAddToExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 103 */     int i = Nat256.mulAddTo(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 104 */     if (i != 0 || (paramArrayOfint3[15] == -1 && Nat.gte(16, paramArrayOfint3, PExt)))
/*     */     {
/* 106 */       if (Nat.addTo(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 108 */         Nat.incAt(16, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void negate(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 115 */     if (0 != isZero(paramArrayOfint1)) {
/*     */       
/* 117 */       Nat256.sub(P, P, paramArrayOfint2);
/*     */     }
/*     */     else {
/*     */       
/* 121 */       Nat256.sub(P, paramArrayOfint1, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void random(SecureRandom paramSecureRandom, int[] paramArrayOfint) {
/* 127 */     byte[] arrayOfByte = new byte[32];
/*     */     
/*     */     do {
/* 130 */       paramSecureRandom.nextBytes(arrayOfByte);
/* 131 */       Pack.littleEndianToInt(arrayOfByte, 0, paramArrayOfint, 0, 8);
/*     */     }
/* 133 */     while (0 == Nat.lessThan(8, paramArrayOfint, P));
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
/* 147 */     long l = Nat256.mul33Add(977, paramArrayOfint1, 8, paramArrayOfint1, 0, paramArrayOfint2, 0);
/* 148 */     int i = Nat256.mul33DWordAdd(977, l, paramArrayOfint2, 0);
/*     */ 
/*     */ 
/*     */     
/* 152 */     if (i != 0 || (paramArrayOfint2[7] == -1 && Nat256.gte(paramArrayOfint2, P)))
/*     */     {
/* 154 */       Nat.add33To(8, 977, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void reduce32(int paramInt, int[] paramArrayOfint) {
/* 160 */     if ((paramInt != 0 && Nat256.mul33WordAdd(977, paramInt, paramArrayOfint, 0) != 0) || (paramArrayOfint[7] == -1 && 
/* 161 */       Nat256.gte(paramArrayOfint, P)))
/*     */     {
/* 163 */       Nat.add33To(8, 977, paramArrayOfint);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void square(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 169 */     int[] arrayOfInt = Nat256.createExt();
/* 170 */     Nat256.square(paramArrayOfint1, arrayOfInt);
/* 171 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void squareN(int[] paramArrayOfint1, int paramInt, int[] paramArrayOfint2) {
/* 178 */     int[] arrayOfInt = Nat256.createExt();
/* 179 */     Nat256.square(paramArrayOfint1, arrayOfInt);
/* 180 */     reduce(arrayOfInt, paramArrayOfint2);
/*     */     
/* 182 */     while (--paramInt > 0) {
/*     */       
/* 184 */       Nat256.square(paramArrayOfint2, arrayOfInt);
/* 185 */       reduce(arrayOfInt, paramArrayOfint2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtract(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 191 */     int i = Nat256.sub(paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 192 */     if (i != 0)
/*     */     {
/* 194 */       Nat.sub33From(8, 977, paramArrayOfint3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void subtractExt(int[] paramArrayOfint1, int[] paramArrayOfint2, int[] paramArrayOfint3) {
/* 200 */     int i = Nat.sub(16, paramArrayOfint1, paramArrayOfint2, paramArrayOfint3);
/* 201 */     if (i != 0)
/*     */     {
/* 203 */       if (Nat.subFrom(PExtInv.length, PExtInv, paramArrayOfint3) != 0)
/*     */       {
/* 205 */         Nat.decAt(16, paramArrayOfint3, PExtInv.length);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void twice(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 212 */     int i = Nat.shiftUpBit(8, paramArrayOfint1, 0, paramArrayOfint2);
/* 213 */     if (i != 0 || (paramArrayOfint2[7] == -1 && Nat256.gte(paramArrayOfint2, P)))
/*     */     {
/* 215 */       Nat.add33To(8, 977, paramArrayOfint2);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/oscar/Downloads/pjeoffice-pro-v2.5.16u-linux_x64/pjeoffice-pro/pjeoffice-pro.jar!/META-INF/versions/9/org/bouncycastle/math/ec/custom/sec/SecP256K1Field.class
 * Java compiler version: 9 (53.0)
 * JD-Core Version:       1.1.3
 */